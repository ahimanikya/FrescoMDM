/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */
package com.sun.mdm.index.loader.masterindex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.text.SimpleDateFormat;


import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;
import java.sql.Connection;
import com.sun.mdm.index.loader.common.LoaderException;
import com.sun.mdm.index.configurator.impl.decision.DecisionMakerConfiguration;
import com.sun.mdm.index.decision.impl.DefaultDecisionMaker;
import com.sun.mdm.index.configurator.ConfigurationService;

import com.sun.mdm.index.dataobject.objectdef.Field;
import com.sun.mdm.index.dataobject.ChildType;

import com.sun.mdm.index.survivor.SurvivorCalculator;
import com.sun.mdm.index.update.UpdateHelper;

import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.objectdef.Lookup;
import com.sun.mdm.index.dataobject.objectdef.ObjectDefinition;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.idgen.EuidGenerator;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.loader.common.ObjectNodeUtil;
import com.sun.mdm.index.loader.common.Util;
import com.sun.mdm.index.loader.config.LoaderConfig;
import com.sun.mdm.index.matching.Standardizer;
import com.sun.mdm.index.matching.StandardizerFactory;
import com.sun.mdm.index.loader.matcher.MatcherTask;
import com.sun.mdm.index.idgen.SEQException;
import com.sun.mdm.index.objects.exception.ObjectException;
import static com.sun.mdm.index.loader.masterindex.MIConstants.*;

/**
 *  Worker task that creates Master Index data from input data.
 *  The data created is for tables:
 *  Enterprise
 *  SystemObject
 *  SystemSBR
 *  AssumedMatch
 *  Transaction
 *  user defined tables such as Person, PersonSBR, Address etc.
 *  
 *  each concurrent MIndexTask is executed in a different thread 

 * @author Swaranjit Dua
 *
 */
public class MIndexTask implements Runnable {
	private static LoaderConfig config = LoaderConfig.getInstance();
	private UpdateHelper mHelper = new UpdateHelper();
	private SurvivorCalculator mCalculator;
	private Map<String, TableData> tableMap_;
	private EUIDBucket.EOCursor eoCursor_;
	private CountDownLatch endGate_;
	private static Logger logger = Logger.getLogger(MIndexTask.class.getName());
	private static ObjectDefinition objDef_;
	private Connection con_;
	private String sdate_;
	private Date date_;
	private static String dateFormatString_;
	private boolean sameSystemMatch_;
	private EuidGenerator euidGenerator;

	private static String empty_s = "";
	private static String ADD = "Add";
	private static String ASSUMEDMATCHSEQ = "ASSUMEDMATCH";
	private static String TRANS_PIGGY = ":T:";

	private SimpleDateFormat outdateFormat_;
	private SimpleDateFormat sysdateFormat_ = new SimpleDateFormat(
	"dd-MMM-yy hh:mm:ss");

	// This format is used for storing dates in systemobjects, sbr, transaction table,
	// per database loader .ctl.

	MIndexTask(Map<String, TableData> tableMap, EUIDBucket.EOCursor cursor,
			ObjectDefinition objDef, Standardizer standardizer,
			CountDownLatch endGate, Connection con, boolean sameSystemMatch)
			throws LoaderException {
		try {
			tableMap_ = tableMap;
			mCalculator = new SurvivorCalculator(standardizer);
			eoCursor_ = cursor;
			endGate_ = endGate;

			con_ = con;
			outdateFormat_ = new SimpleDateFormat(dateFormatString_ + " hh:mm:ss"); // output date format is
			// written to master index files is - "object def date Format" + "hh:mm:ss"
			date_ = new Date();
			sdate_ = sysdateFormat_.format(date_);
			euidGenerator = config.getEuidGenerator();
			sameSystemMatch_ = sameSystemMatch;
		} catch (Exception ex) {
			throw new LoaderException (ex);
		}
	}

	public void run() {
		try {
			while (true) {
				List<DataObject> solist = eoCursor_.next();
				if (solist == null) {
					break;
				}

				List<List<DataObject>> solists = splitSameSystem(solist);
				shiftSOs(solists);
				for (List<DataObject> slist : solists) {

					Map<String, String> weightMap = new HashMap<String, String>();
					if (slist.size() < 1) {
						weightMap = null;
					} else {
						for (DataObject d : slist) {
							String syslocalid = d.getFieldValue(2)
							+ d.getFieldValue(3);
							String weight = d.getFieldValue(4); // weight is at position 5 in EUID bucket
							String wt = weightMap.get(syslocalid);
							if (wt == null
									&& !weight.equals(MatcherTask.SDUPSCORE)) {
								weightMap.put(syslocalid, weight);
							}
						}
					}

					EnterpriseObject eo = calculateSBR(slist);
					addEnterprise(eo, weightMap);
				}
			}

		} catch (Throwable ex) {
			logger.severe(ex + ex.getMessage());
			logger.severe(Util.getStackTrace(ex));
			ex.printStackTrace();

		} finally {
			endGate_.countDown();
		}

	}

	/**
	 * takes an input list consisting of system objects and if sameSystemMatch is set to true,
	 * and if a system object for a particular system is in the list, then another system object
	 * with same system is taken out of the input list, and added to the list which does not has
	 * same system. If no such list exist, then a new list is created and SO is added to that list.
	 * @param solist
	 * @return  List of SO list. If samesystemMatch is false, there will be only one list.
	 * @throws Exception
	 */
	private List<List<DataObject>> splitSameSystem(List<DataObject> solist)
	throws Exception {
		List<List<DataObject>> solists = new ArrayList<List<DataObject>>();
		boolean found = true; // found = true means new list is to be created
		String curEUID = null;
		for (DataObject d : solist) {
			String syscode = d.getFieldValue(2);
			String localid = d.getFieldValue(3);
			/*
			 * for each dataobject in input solist, go through all the lists in solists,
			 * If in any list, that systemcode is not found, add to that list.
			 * If every list in solists has that systemcode, then create a new list.
			 */
			for (List<DataObject> list : solists) {
				found = false;
				if (sameSystemMatch_) {
					for (DataObject dobject : list) {
						curEUID = dobject.getFieldValue(0);
						String syscode2 = dobject.getFieldValue(2);
						String localid2 = dobject.getFieldValue(3);
						if (syscode.equals(syscode2)
								&& localid.equals(localid2)) {
							found = false; // so add to the existing list
							break;
						} else if (syscode.equals(syscode2)) {
							found = true;
							continue;
						}
					}
				}
				if (found == false) {
					d.setFieldValue(0, curEUID);
					list.add(d);
					break;
				}
			}
			/** 
			 * this code is executed, at very first time, and when same system match is found in all existing  lists
			 */
			if (found == true) {
				List<DataObject> newlist = new ArrayList<DataObject>();
				if (curEUID == null) {
					curEUID = d.getFieldValue(0);
				} else {
					curEUID = euidGenerator.getNextEUID(con_);
					d.setFieldValue(0, curEUID);
				}
				newlist.add(d);
				solists.add(newlist);
			}
		}

		return solists;
	}

	static {
		objDef_ = initDef();
	}

	/**
	 * shifts SO to the front of the list, that has max assumed match weight. This is done because
	 * assumed match weight for the first SO in the list is not added to the assumed match table. 
	 * The reason to leave out max score is to balance out the match weight associated with a SO  
	 * which are max of all match weights with different SOs in the SO list (EUID Bucket)
	 * @param solists
	 * @return
	 */
	private void shiftSOs(List<List<DataObject>> solists) {

		for (List<DataObject> solist : solists) {
			int pos = 0;
			double max = 0;
			for (int i = 0; i < solist.size(); i++) {
				DataObject d = solist.get(i);
				String sscore = d.getFieldValue(4);
				double score = Double.parseDouble(sscore);
				if (score > max) {
					pos = i;
					max = score;
				}
			}
			if (solist.size() > 1 && pos != 0) { // shift only if there are > 1 SOs
				DataObject d = solist.remove(pos);
				solist.add(0, d);
			}
		}
	}

	/**
	 * Not used. ONly for debugging
	 * @param solists
	 */
	private void verifyIncorrectEUID(List<List<DataObject>> solists) {
		for (List<DataObject> list : solists) {
			String euid = null;
			for (int i = 0; i < list.size(); i++) {
				DataObject d = list.get(i);
				String e = d.getFieldValue(0);
				if (i == 0) {
					euid = e;
				} else if (!e.equals(euid)) {
					logger.info("euid: " + euid + "e: " + e);
				}
			}
		}

		if (solists.size() > 1) {
			for (List<DataObject> list : solists) {

				for (int i = 0; i < list.size(); i++) {
					DataObject d = list.get(i);
					String euid = d.getFieldValue(0);
					String syscode = d.getFieldValue(2);
					String localid = d.getFieldValue(3);
					String e = d.getFieldValue(0);
					//logger.info("euid:" + euid + " syscode:" + syscode
					//	+ " lid:" + localid);
				}
			}
		}
	}

	private static ObjectDefinition initDef() {
		ObjectDefinition objDef = config.getObjectDefinition();
		addIDs(objDef);		
		dateFormatString_ = objDef.getDateFormat();		
		return objDef;
	}

	private void addEnterprise(EnterpriseObject eo, Map<String, String> weights)
	throws Exception {
		String euid = eo.getEUID();
		Collection systems = eo.getSystemObjects();

		int i = 0;
		String transnum = null;
		for (Object o : systems) {
			SystemObject sys = (SystemObject) o;
			String localid = sys.getLID();
			String syscode = sys.getSystemCode();
			addEnterpriseTable(euid, localid, syscode);
			addSystemObject(sys);
			String user = sys.getUpdateUser();

			if (i == 0) {
				transnum = com.sun.mdm.index.idgen.CUIDManager.getNextUID(con_,
				"TRANSACTIONNUMBER");
				addTransactionNumber(euid, syscode, localid, user, transnum);
			} else {

				if (weights != null) {
					String weight = weights.get(syscode + localid);
					if (weight != null && Double.parseDouble(weight) > 0) {
						addAssumedMatchTable(euid, syscode, localid, weight,
								transnum);
					}
				}
			}
			i++; // weights List has a weight corresponding to each system object
		}
		SBR sbr = eo.getSBR();
		addSBR(euid, sbr, transnum);
	}

	private void addEnterpriseTable(String euid, String localid, String syscode) {
		List<String> list = new ArrayList<String>();
		list.add(syscode);
		list.add(localid);
		list.add(euid);
		addData(ENTERPRISE, list);
	}

	private void addData(String tab, List<String> list) {
		String table = tab.toUpperCase();
		TableData data = tableMap_.get(table);
		if (data == null) {
			data = new TableData(table);
			data.addData(list);
			tableMap_.put(table, data);
		} else {
			data.addData(list);
		}
	}

	private void addData(String tab, DataObject d) {
		String table = tab.toUpperCase();
		TableData data = tableMap_.get(table);
		if (data == null) {
			data = new TableData(table);
			data.addData(d);
			tableMap_.put(table, data);
		} else {
			data.addData(d);
		}
	}

	private EnterpriseObject calculateSBR(List<DataObject> solist)
	throws Exception {
		EnterpriseObject eo = null;
		for (int i = 0; i < solist.size(); i++) {

			DataObject dob = solist.get(i);
			try {
				String euid = dob.getFieldValue(0);

				SystemObject so = getSystemObject(dob);

				if (i == 0) {
					eo = createEnterpriseObject(so);
					eo.setEUID(euid);
				} else {

					//boolean recordChanged = false;
					boolean copyflag = true;
					boolean replaceSO = false;

					// Update the existing SO, or add it
					so.setUpdateFunction(SystemObject.ACTION_ADD);
					so.setCreateFunction(SystemObject.ACTION_ADD);

					if (mHelper.updateSO(so, eo, copyflag, replaceSO) == null) {
						eo.addSystemObject(so);
					}
				}
			} catch (Exception ex) {
				logger.severe("DataObject:" + dob.toString());
				throw ex;
			}

		}
		try {
			mCalculator.determineSurvivor(eo);

		} catch (Exception ex) {
			logger.severe("EO:" + eo.toString());

			throw ex;
		}
		return eo;
	}

	SystemObject getSystemObject(DataObject d) throws Exception {

		String euid = d.remove(0); // EUID
		String gid = d.remove(0); // GID
		String syscode = d.remove(0); // syscode
		String lid = d.remove(0); //lid
		String weight = d.remove(0);
		String updateDateTime = d.remove(0); // update date
		String updateUser = d.remove(0); // create user
		SystemObject so = ObjectNodeUtil.getSystemObject(d, lid, syscode,
				updateDateTime, updateUser);

		d.add(0, euid);
		d.add(1, gid);
		d.add(2, syscode);
		d.add(3, lid);
		d.add(4, weight);
		d.add(5, updateDateTime);
		d.add(6, updateUser);

		return so;
	}

	/*
	 * These ids like PersonId is not part of set of fields defined in
	 * object.xml, but are implicitly created when creating Master Index
	 * object schema classes such as PersonObject.
	 */
	private static void addIDs(ObjectDefinition objdef) {
		String name = objdef.getName();
		String idName = name + "Id";
		Field f = new Field();
		f.setName(idName);
		objdef.addField(0, f);

		List<ObjectDefinition> children = objdef.getChildren();

		for (ObjectDefinition child : children) {
			addIDs(child);
		}
	}

	private EnterpriseObject createEnterpriseObject(SystemObject so)
	throws Exception {
		so.setCreateFunction(SystemObject.ACTION_ADD);
		so.setUpdateFunction(SystemObject.ACTION_ADD);
		so.setCreateUser(so.getUpdateUser());
		EnterpriseObject eo = mHelper.createEO(so);
		return eo;
	}

	private String addTransactionNumber(String euid, String syscode,
			String localid, String user, String transnum) throws LoaderException {

		List<String> list = new ArrayList<String>();
		/*
		"TRANSACTIONNUMBER", "LID1", "LID2",
		"EUID1", "EUID2", "FUNCTION", "SYSTEMUSER", "TIMESTAMP", "DELTA",
		"SYSTEMCODE", "LID", "EUID" */
		list.add(transnum);
		list.add(empty_s); //LID1
		list.add(empty_s); // LID2
		list.add(empty_s); // EUID1
		list.add(empty_s); // EUID2
		list.add(ADD);
		list.add(user); // SYSTEMUSER
		list.add(sdate_); // TIMESTAMP new java.util.Date()
		list.add(syscode);
		list.add(localid);
		list.add(euid);

		addData(TRANSACTION, list);

		// tObj.setSystemUser(sbr.getCreateUser());
		return transnum;
	}

	private void addAssumedMatchTable(String euid, String systemcode,
			String lid, String weight, String transnum) throws LoaderException {

		try {
			String assumedMatchId = com.sun.mdm.index.idgen.CUIDManager.getNextUID(
					con_, ASSUMEDMATCHSEQ);

			//{ "ASSUMEDMATCHID", "EUID",
			//"SYSTEMCODE", "LID", "WEIGHT", "TRANSACTIONNUMBER" };
			List<String> list = new ArrayList<String>();
			list.add(assumedMatchId);
			list.add(euid);
			list.add(systemcode);
			list.add(lid);
			list.add(weight);
			list.add(transnum);
			addData(ASSUMEDMATCH, list);
		} catch (SEQException s) {
			throw new LoaderException (s);
		}
	}

	private void addSystemObject(SystemObject so) throws LoaderException {
		try {
			ObjectNode primaryO = so.getObject();
			String tag = primaryO.pGetTag();
			addSystemTable(so, tag);
			List<String> list = new ArrayList<String>();
			list.add(so.getSystemCode());
			list.add(so.getLID());
			addConfigTableData(objDef_, so.getObject(), list, null, false);
			//addPrimaryTable(so, list, tag, false);
		} catch (ObjectException e) {
			throw new LoaderException (e);
		}
	}

	private void addSBR(String euid, SBR sbr, String transnum) throws LoaderException {
		ObjectNode primaryO = sbr.getObject();
		String tag = primaryO.pGetTag();
		addSBRTable(sbr, euid, tag);
		writeSBRPot(primaryO, euid, transnum);
		List<String> list = new ArrayList<String>();
		list.add(euid);
		addConfigTableData(objDef_, sbr.getObject(), list, null, true);

	}

	private void writeSBRPot(ObjectNode primarySBR, String euid, String transnum)
	throws LoaderException {
		DataObject data = ObjectNodeUtil.fromObjectNode(primarySBR);
		data.add(0, euid + TRANS_PIGGY + transnum); // add back EUID, that is not in data Object returned
		addData(POTENTIALDUPLICATES, data);

	}

	/*
	 * remove 1st field from each child. This is invoked after converson
	 * from ObjectNode to DataObject. The ObjectNode contains ids such as 
	 * AddressId. But object definition does has these Ids. So these must be
	 * removed before DataObject is output.
	 */
	private void deleteChildIDs(DataObject d) {

		List<ChildType> childtypes = d.getChildTypes();

		for (ChildType ct : childtypes) {
			List<DataObject> children = ct.getChildren();

			for (DataObject c : children) {
				c.remove(0);
			}
		}

	}

	private void addSystemTable(SystemObject so, String tag) throws LoaderException  {
		try {
			String lid = so.getLID();
			List<String> list = new ArrayList<String>();
			//  { "SYSTEMCODE", "LID", "CHILDTYPE",
			//	"CREATEUSER", "CREATEFUNCTION", "CREATEDATE", "UPDATEUSER",
			//"UPDATEFUNCTION", "UPDATEDATE", "STATUS" };
			String syscode = so.getSystemCode();
			String createuser = so.getCreateUser();
			Date createDate = so.getCreateDateTime();
			Date updateDate = so.getUpdateDateTime();
			String updatefunction = so.getUpdateFunction();
			String createfunction = so.getCreateFunction();
			String status = so.getStatus();
			list.add(syscode);
			list.add(lid);
			list.add(tag);
			list.add(createuser);
			list.add(createfunction);
			list.add(sysdateFormat_.format(createDate));
			list.add(createuser);
			list.add(updatefunction);
			list.add(sysdateFormat_.format(updateDate));
			list.add(status);
			addData(SYSTEMOBJECT, list);
		} catch (ObjectException e) {
			throw new LoaderException(e);
		}
	}

	private void addSBRTable(SBR sbr, String euid, String tag) throws LoaderException {
		try {
			List<String> list = new ArrayList<String>();

			//{ "EUID", "CHILDTYPE", "CREATESYSTEM",
			//	"CREATEUSER", "CREATEFUNCTION", "CREATEDATE", "UPDATESYSTEM",
			//"UPDATEUSER", "UPDATEFUNCTION", "UPDATEDATE", "STATUS",
			//"REVISIONNUMBER" };
			String createSystem = sbr.getCreateSystem();
			String createuser = sbr.getCreateUser();
			Date createDate = sbr.getCreateDateTime();
			Date updateDate = sbr.getUpdateDateTime();
			String updatefunction = sbr.getUpdateFunction();
			String createfunction = sbr.getCreateFunction();
			String status = sbr.getStatus();
			list.add(euid);
			list.add(tag);
			list.add(createSystem);
			list.add(createuser);
			list.add(createfunction);
			list.add(sysdateFormat_.format(createDate));
			list.add(sbr.getUpdateSystem());
			list.add(createuser);
			list.add(updatefunction);
			list.add(sysdateFormat_.format(updateDate));
			list.add(status);
			list.add("1");
			addData(SYSTEMSBR, list);
		} catch (ObjectException e) {
			throw new LoaderException (e);
		}
	}

	private void addConfigTableData(ObjectDefinition objectDef, ObjectNode o,
			List<String> list, String parentid, boolean isSBR) 
	throws LoaderException  {
		try {
			if (list == null) { // so it is child table, else euid or localid and 
				// systemcode is already populated in the list that is required for primaryTable such as Person
				list = new ArrayList<String>();
			}

			String objname = objectDef.getName();
			String table = objname;
			if (isSBR) {
				table = table + "SBR";
			}

			if (parentid != null) {
				list.add(parentid);
			}

			String id = objname + "id";
			//String idval = (String) o.getValue(id);
			String seqName = objname.toUpperCase();
			if (isSBR) {
				seqName = seqName + "SBR";
			}

			String idval = com.sun.mdm.index.idgen.CUIDManager.getNextUID(con_,
					seqName);
			list.add(idval);

			// first field is Id like PersonId
			for (int i = 1; i < objectDef.getFields().size(); i++) {
				String name = objectDef.getField(i).getName();
				Object value = o.getValue(name);
				String val = empty_s;

				if (value != null) {
					if (value instanceof Date) {
						val = outdateFormat_.format(value);
					} else {
						val = value.toString();
					}
				}
				list.add(val);
			}
			addData(table, list);

			List<ObjectDefinition> odchildren = objectDef.getChildren();
			for (ObjectDefinition childod : odchildren) {
				String tag = childod.getName();
				List children = o.pGetChildren(tag);

				if (children != null) {
					for (Object c : children) {
						addConfigTableData(childod, (ObjectNode) c, null, idval,
								isSBR);
					}
				}
			}

		} catch (ObjectException oe)  {
			throw new LoaderException (oe);
		}  catch (SEQException se)  {
			throw new LoaderException (se);
		}

	}


}
