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

//import com.sun.mdm.index.objects.PersonObject;
//import com.sun.mdm.index.objects.AddressObject;
//import com.sun.mdm.index.objects.PhoneObject;
import com.sun.mdm.index.dataobject.objectdef.DataObjectAdapter;
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
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.loader.common.ObjectNodeUtil;
import com.sun.mdm.index.loader.common.Util;
import com.sun.mdm.index.loader.config.LoaderConfig;
import com.sun.mdm.index.matching.Standardizer;
import com.sun.mdm.index.matching.StandardizerFactory;
import com.sun.mdm.index.loader.matcher.MatcherTask;

import static com.sun.mdm.index.loader.masterindex.MIConstants.*;

/**
 *  Worker task that creates Master Index data from input data.
 *  One of the main task is to create SBR from input data.
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
	private UpdateHelper  mHelper = new UpdateHelper();
	private SurvivorCalculator mCalculator;
	private Map<String, TableData> tableMap_;
	private EUIDBucket.EOCursor eoCursor_;
	private CountDownLatch endGate_;
	private static Logger logger = Logger.getLogger(MIndexTask.class
			.getName());
	private static ObjectDefinition objDef_;
	private Connection con_;
    private String sdate_;
    private Date date_;
    private static String dateFormatString_;
    
    private static String empty_s = "";
    private static String ADD = "Add";
    private static String ASSUMEDMATCHSEQ = "ASSUMEDMATCH";
    
    
   // private static SimpleDateFormat dateFormat_;  // This format is used in
        // user defined objects per object de
    	// new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
    private  SimpleDateFormat outdateFormat_; 
    private  SimpleDateFormat sysdateFormat_ = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
	// This format is used for storing dates in systemobjects, sbr, transaction table,
      // per database loader .ctl.
	MIndexTask(Map<String, TableData> tableMap, EUIDBucket.EOCursor cursor,  
			 ObjectDefinition objDef, Standardizer standardizer, CountDownLatch endGate, Connection con) 
			      throws Exception {
		
		tableMap_ = tableMap;
		mCalculator = new SurvivorCalculator(standardizer);	
		eoCursor_ = cursor;
		endGate_ = endGate;
				
		con_ = con;
		outdateFormat_ = new SimpleDateFormat(dateFormatString_ + " hh:mm:ss"); // output date format is
		  // written to master index files is - "object def date Format" + "hh:mm:ss"
		date_ = new Date();	    
	    sdate_ = sysdateFormat_.format(date_);
	}
		
	public void run() {
	  try {				
		while (true ) {		
		  List<DataObject> solist = eoCursor_.next();
		  if (solist == null) {
			  break;
		  }		
		  
		  Map<String,String> weightMap = new HashMap<String,String>();
          if (solist.size() < 1) {
			  weightMap = null;
		  } else {
		    for(DataObject d: solist) {
			  String syslocalid = d.getFieldValue(2) + d.getFieldValue(3);
			  String weight = d.getFieldValue(4); // weight is at position 5 in EUID bucket
			  String wt = weightMap.get(syslocalid);			  
			  if (wt == null && !weight.equals(MatcherTask.SDUPSCORE)) {
				weightMap.put(syslocalid, weight);  
			  }			 
		    }
		  }
		  
		 EnterpriseObject eo =  calculateSBR(solist);
		 addEnterprise(eo, weightMap);		 		 
		}
	  } catch (Throwable ex){
		  logger.severe(ex + ex.getMessage());
		  logger.severe(Util.getStackTrace(ex));
		  ex.printStackTrace();
	  } finally {		
	     endGate_.countDown();
	  }
		
	}
	
	static {
		objDef_ = initDataObjectAdapter();
	}
	
	private static ObjectDefinition initDataObjectAdapter() {
		ObjectDefinition objDef = config.getObjectDefinition();
		addIDs(objDef);
	//	DataObjectAdapter.init(objDef);
		dateFormatString_ = objDef.getDateFormat();
		//String timeFormat = config.getSystemProperty("TimeFormat");
		//if (timeFormat != null) {
			//dateFormatString_ = dateFormatString_ + " " + timeFormat;
		//}
		
		
		return objDef;
	}
	
	private void addEnterprise(EnterpriseObject eo, Map<String,String> weights) throws Exception {
		String euid = eo.getEUID();
		Collection systems = eo.getSystemObjects();
		

		int i = 0;
		String transnum = null;
		for (Object o: systems) {			
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
		    
		     if(weights != null) {
		       String weight = weights.get(syscode+localid);
		       if (weight != null && Double.parseDouble(weight) > 0) {
		         addAssumedMatchTable(euid, syscode, localid, weight, transnum);
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
		
		
	private EnterpriseObject calculateSBR(List<DataObject> solist) throws Exception {
	   EnterpriseObject eo = null;
	   for (int i = 0; i < solist.size() ; i++) {
	      DataObject dob = solist.get(i);
	      String euid = dob.getFieldValue(0);
	      
		  SystemObject so = getSystemObject(dob);	  		  
		   		  
		  String syscode = so.getSystemCode();
	      String lid = so.getLID();
	      		                    				                 
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
		       //              recordChanged = true;
		      } else {
		          // Set the timestamp (UpdateHelper only modifies child object,
		          // so this has to be done separately)		         
		          so = eo.getSystemObject(syscode, lid);		         		         
		      }               		       		           		          		    	 
		    }	
		  //addTransactionNumber(euid, syscode, lid);
		 }
	     mCalculator.determineSurvivor(eo);	    
	     return eo;
	}
	
	
	
	
	SystemObject getSystemObject(DataObject d) throws Exception {
		
		String euid = d.remove(0); // EUID
		String gid = d.remove(0); // GID
	    String syscode = d.remove(0); // syscode
	    String lid = d.remove(0); //lid
	    String weight = d.remove(0);
	    String updateDateTime =  d.remove(0);  // update date
	    String updateUser = d.remove(0);  // craete user
	    SystemObject so = ObjectNodeUtil.getSystemObject(d, lid, syscode,
				updateDateTime, updateUser);
	    
	    d.add(0,euid);
	    d.add(1,gid);
	    d.add(2,syscode);
	    d.add(3,lid);
	    d.add(4,weight);
	    d.add(5,updateDateTime);
	    d.add(6,updateUser);
					
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
        
        for (ObjectDefinition child: children) {
        	addIDs(child);
        }        
	}
	
	
	
	
	private EnterpriseObject createEnterpriseObject(SystemObject so) throws Exception {
		so.setCreateFunction(SystemObject.ACTION_ADD);
        so.setUpdateFunction(SystemObject.ACTION_ADD);
        so.setCreateUser(so.getUpdateUser());
        EnterpriseObject eo = mHelper.createEO(so); 
		return eo;
	}
	
	
	private String addTransactionNumber(String euid, String syscode, String localid, String user
			, String transnum) 
	throws Exception {
	           
       //String transnum = com.sun.mdm.index.idgen.CUIDManager.getNextUID(con_,
         //                                  "TRANSACTIONNUMBER");
       //String transnum = null;
    
       List<String> list = new ArrayList<String>();
       /*
       "TRANSACTIONNUMBER", "LID1", "LID2",
		"EUID1", "EUID2", "FUNCTION", "SYSTEMUSER", "TIMESTAMP", "DELTA",
		"SYSTEMCODE", "LID", "EUID" */
       list.add(transnum);
       list.add(empty_s); //LID1
       list.add(empty_s); // LID2
       list.add(empty_s); // EUID1
       list.add(empty_s);  // EUID2
       list.add(ADD);
       list.add(user); // SYSTEMUSER
       list.add(sdate_);  // TIMESTAMP new java.util.Date()
       //list.add(empty_s); // DELTA
       list.add(syscode);
       list.add(localid);
       list.add(euid);
       
       addData(TRANSACTION, list);
            
        // tObj.setSystemUser(sbr.getCreateUser());
       return transnum;
	}
	
	
	private void addAssumedMatchTable(String euid, String systemcode,
			String lid, String weight, String transnum) throws Exception {
		
		String assumedMatchId = com.sun.mdm.index.idgen.CUIDManager.getNextUID(con_,
        ASSUMEDMATCHSEQ);
				
		//{ "ASSUMEDMATCHID", "EUID",
			//"SYSTEMCODE", "LID", "WEIGHT", "TRANSACTIONNUMBER" };
		List<String> list = new ArrayList<String>();
		list.add(assumedMatchId);
		list.add(euid);
		list.add(systemcode);
		list.add(lid);
		list.add(weight);
		list.add(transnum);
		addData(ASSUMEDMATCH,list);
	}
	
	private void addSystemObject(SystemObject so) throws Exception {
		ObjectNode primaryO = so.getObject();
		String tag = primaryO.pGetTag();
		addSystemTable(so, tag);
		List<String> list = new ArrayList<String>();
		list.add(so.getSystemCode());
		list.add(so.getLID());
		addConfigTableData(objDef_, so.getObject(), list, null, false ); 
		//addPrimaryTable(so, list, tag, false);
	}
	
	private void addSBR(String euid, SBR sbr, String transnum) throws Exception {
		ObjectNode primaryO = sbr.getObject();		
		String tag = primaryO.pGetTag();
		addSBRTable(sbr, euid, tag);
		writeSBRPot(primaryO, euid, transnum); 
		List<String> list = new ArrayList<String>();
		list.add(euid);
		addConfigTableData(objDef_, sbr.getObject(), list, null, true);
		
	}
	
	private void writeSBRPot(ObjectNode primarySBR, String euid, String transnum) throws Exception {
		DataObject data = ObjectNodeUtil.fromObjectNode(primarySBR);
		data.add(0, euid+":TRANS:" + transnum );  // add back EUID, that is not in data Object returned
		// from Util.fromObjectNode
		//deleteChildIDs(data);
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
		
		for (ChildType ct: childtypes) {
			List<DataObject> children = ct.getChildren();
			
			for (DataObject c: children) {
				c.remove(0);
			}
		}
		
		
	}
	
	private void addSystemTable(SystemObject so, String tag) throws Exception {
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
	}
	

	private void addSBRTable(SBR sbr, String euid, String tag) throws Exception {	
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
	}
	
	
	/*
	private void addPrimaryTable(ObjectNode o, List<String> list, String tag, boolean isSBR ) throws Exception {	   				
		addConfigTableData(objDef_, o, list, null, isSBR ); 		  						    
	}
	*/
	
		
	private void addConfigTableData(ObjectDefinition objectDef, ObjectNode o, 
			List<String>  list, String parentid, boolean isSBR) 
	  throws Exception {
		if  (list == null) { // so it is child table, else euid or localid and 
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
			seqName = seqName+"SBR";
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
				if (value instanceof Date)
				{
				  val = outdateFormat_.format(value);
				} else {
				   val = value.toString();
				}
			}		
			list.add(val);			
		}		
		addData(table, list);
		
		List<ObjectDefinition> odchildren = objectDef.getChildren();
		for ( ObjectDefinition childod : odchildren ) {
		  String tag = childod.getName();
		  List children = o.pGetChildren(tag);
		
		  if (children != null) {
			for (Object c : children ) {
				addConfigTableData(childod, (ObjectNode)c,
						null, idval, isSBR);
			}
		  }
		}
	}
	

	public static void main(String[] args) {
		try {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");	
		
		format.parse("02/02/2001 00:00:00");
		} catch (Exception ex) {
			System.out.println(ex+ ex.getMessage());
			ex.printStackTrace();
		}
				
	}
	
	
	/*
	SystemObject getSystemObject1()throws Exception {
		
		PersonObject per = new PersonObject();
		
		per.setFirstName("Swaranjit");
		per.setLastName("Dua");
		per.setSSN("123456789");
		per.setLanguage("Hindi");
		per.setFnamePhoneticCode("Swaranjit");
		per.setLnamePhoneticCode("Dua");
		per.setStdMiddleName("S");
		
		
		SystemObject sys = new SystemObject();
		sys.setLID("111");
		sys.setSystemCode("A");
		sys.setChildType("Person");
		sys.setStatus(SystemObject.STATUS_ACTIVE);
		
		sys.setObject(per);
	
		return sys;
		
	}
	
SystemObject getSystemObject2()throws Exception {
		
		PersonObject per = new PersonObject();
		
		per.setFirstName("Swaranjit2");
		per.setLastName("Dua2");
		per.setSSN("1234567892");
		per.setLanguage("Hindi2");
		per.setFnamePhoneticCode("Swaranjit2");
		per.setLnamePhoneticCode("Dua2");
		per.setStdMiddleName("S");
		
		AddressObject add = new AddressObject();
		add.setAddressLine1("100 First Av");
		add.setCity("Monrovia");
		add.setCounty("LA");
		add.setAddressType("WORK");
		add.setStreetType("Ave");
		
		per.addChild(add);
		
		AddressObject add2 = new AddressObject();
		add2.setAddressLine1("200 First Av");
		add2.setCity("Arcadia");
		add2.setAddressType("HOME");
		add2.setCounty("SanB");
		add.setStreetType("Ave");
		per.addChild(add2);
		
		PhoneObject phone = new PhoneObject();
		phone.setPhone("8008008000");
		phone.setPhoneExt("234");
		per.addChild(phone);
		
		 
		SystemObject sys = new SystemObject();
		sys.setLID("222");
		sys.setSystemCode("B");
		sys.setChildType("Person");
		sys.setStatus(SystemObject.STATUS_ACTIVE);
		
		sys.setObject(per);
	
		return sys;
		
	}
	
	*/
	
   				
}
