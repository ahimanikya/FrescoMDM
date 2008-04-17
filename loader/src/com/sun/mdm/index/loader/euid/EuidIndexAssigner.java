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

package com.sun.mdm.index.loader.euid;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.DataObjectReader;
import com.sun.mdm.index.idgen.EuidGenerator;
import com.sun.mdm.index.idgen.SEQException;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterState;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterSynchronizer;
import com.sun.mdm.index.loader.clustersynchronizer.dao.DAOFactory;
import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * 
 * this is main class which represents the euid-assigner component. The set of
 * task done by the euid-assigner is
 * <ol>
 * <li> read the assumed match file
 * <li> index each of the sysid appearing in the assumed match file in the euid
 * index file
 * <li> group all the sysids which are assumed match to each other, assign same
 * euid to given group of sysid
 * <li>write the euid value for a given sysid to the euid index file
 * <li>read each input record from the good file and assign euid to each of
 * them
 * <li>distribute the input records based on their euid to euid buckets
 * </ol>
 * <p>
 * 
 * @author Sujit Biswas
 * 
 */
public class EuidIndexAssigner {

	private static Logger logger = Logger.getLogger(EuidIndexAssigner.class
			.getName());

	private MatchFileReader matchFileReader;

	private EuidIndexFile euidIndexFile;

	private EuidGenerator euidGenerator;

	private EuidBucketDistributor distributor;

	private LoaderConfig config;

	private boolean debug = false;

	public EuidIndexAssigner() {
		config = LoaderConfig.getInstance();
		;

		String workingDir = config.getSystemProperty("workingDir");

		// TODO check the name of the final match file
		String matchFile = workingDir + "/match/stage/finalMatch";
		String euidFile = workingDir + "/euid/euidIndex.txt";

		euidGenerator = config.getEuidGenerator();

		matchFileReader = new MatchFileReader(matchFile);
		euidIndexFile = new EuidIndexFile(euidFile, euidGenerator
				.getEUIDLength());

		distributor = new EuidBucketDistributor();

	}

	/**
	 * start the entire euid-assigner process, return only when the complete
	 * process is done
	 * 
	 */
	public void start() {
		logger.info("Euid assigner started at : " + new Date());

		initIndex();
		assignEuids();
		distributeBucket();

		/**
		 * set the state of the loader to the next stage
		 */
		ClusterSynchronizer.getInstance().setClusterState(
				ClusterState.MASTER_INDEX_GENERATE);

		close();

	}

	/**
	 * read records from the match file and populate the euidIndexFile with the
	 * position of the sysid
	 * 
	 */
	protected void initIndex() {

		long totalrecord = Long.parseLong(config
				.getSystemProperty("totalNoOfRecords"));

		/*
		 * cleanup the content of euidIndex file and allocate space for n
		 * records
		 */
		try {
			euidIndexFile.allocateSpace(totalrecord);
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}

		while (true) {

			List<MatchFileRecord> l = matchFileReader
					.readRecordsWithSameSysid();

			if (l.isEmpty())
				break;

			MatchFileRecord rec = l.get(0);

			setIndex(rec);

		}

	}

	/**
	 * close the match file reader and the euid-index file
	 */
	protected void close() {
		try {
			matchFileReader.close();
			euidIndexFile.close();
			updateSequenceTable();
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}

		logger.info("Euid assigner finish at : " + new Date());

	}

	private void updateSequenceTable() throws SEQException, SQLException {
		String euid = euidGenerator.getNextEUID(null);

		HashMap<String, String> params = config.getEuidParams();

		int checksumlen = Integer.parseInt(params.get("ChecksumLength"));

		long chunkSize = Long.parseLong(params.get("ChunkSize"));

		int seqStrLen = euid.length() - checksumlen;

		String seqStr = euid.substring(0, seqStrLen);

		long l = Long.parseLong(seqStr);

		long mod = l % chunkSize;

		long seqNo = l + chunkSize - mod;

		String sql = "update sbyn_seq_table set seq_count=? where seq_name='EUID'";

		Connection c = DAOFactory.getConnection();

		PreparedStatement ps = c.prepareStatement(sql);

		ps.setLong(1, seqNo);

		int i = ps.executeUpdate();
		
		if(i==0){
			logger.fine("make sure that the sbyn_seq_table exist");
		}

		ps.close();

		c.close();

	}

	/**
	 * set the index in the EuidIndex file for the given MatchFileRecord
	 * 
	 * @param element
	 */
	private void setIndex(MatchFileRecord element) {
		EuidIndexFileRecord record = new EuidIndexFileRecord(null, element
				.getIndex());
		euidIndexFile.position(element.getSysid1());
		euidIndexFile.writeRecord(record);
	}

	/**
	 * assign euid to all the sysids appearing in the assumed match file
	 * 
	 */
	protected void assignEuids() {
		try {
			matchFileReader.seek(0);
			euidIndexFile.seek(0);
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}

		long index = 0;
		while (true) {

			HashSet<Long> sysids = new HashSet<Long>();
			List<MatchFileRecord> list = matchFileReader
					.readRecordsWithSameSysid(index);

			if (list.isEmpty())
				break;			

			if(isAssigned(list.get(0))){
				index=list.get(list.size()-1).getIndex();
				continue;
			}
			
			Iterator<MatchFileRecord> iterator = list.iterator();
			while (iterator.hasNext()) {
				MatchFileRecord r = (MatchFileRecord) iterator.next();

				index = r.getIndex();
				collectSysids(sysids, r);
			}

			if (!sysids.isEmpty()) {
				assignEuid(sysids);
				//logger.info(sysids.toString());
			}

		}

	}

	/**
	 * assign euid for a given set of sysids which are assumed match to each
	 * other
	 * 
	 * @param sysids
	 */
	private void assignEuid(HashSet<Long> sysids) {
		String euid = null;
		try {
			euid = euidGenerator.getNextEUID(null);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}

		if (debug) {
			logger.info("EUID : " + euid + ", assigned to " + sysids);
		}

		for (long sysid : sysids) {
			euidIndexFile.assignEuid(euid, sysid);
		}

	}

	/**
	 * collect all the sysids which are assumed match to each other, starting
	 * with the sysids(S1 S2) in the MatchFileRecord r
	 * 
	 * @param sysids
	 *            hashset containing the sysids
	 * @param r
	 *            MatchFileRecord containing the starting sysids(S1 S2)
	 */
	private void collectSysids(HashSet<Long> sysids, MatchFileRecord r) {

		if (!sysids.contains(r.getSysid1()))
			sysids.add(r.getSysid1());

		Stack<Long> stack = new Stack<Long>();
		stack.add(r.getSysid2());

		while (!stack.isEmpty()) {
			long l = stack.pop();
			if (!sysids.contains(l)) {
				sysids.add(l);
				List<MatchFileRecord> list = getMatchRecordsForSysid2(l);
				for(MatchFileRecord r1: list){
					stack.push(r1.getSysid2());
				}
			}

		}

		

	}

	

	/**
	 * get the list of consecutive records for a given sysid from the assumed
	 * match file
	 * 
	 * @param sysid2
	 * @return
	 */
	private List<MatchFileRecord> getMatchRecordsForSysid2(long sysid2) {

		long index = euidIndexFile.getIndex(sysid2);
		List<MatchFileRecord> list = matchFileReader
				.readRecordsWithSameSysid(index - 1);
		return list;
	}

	/**
	 * check if the euid for a given sysid is already assigned
	 * 
	 * @param sysid
	 * @return
	 */
	private boolean isAssigned(MatchFileRecord r) {
		
		if(r.getSysid1() > r.getSysid1())
			return true;
		return euidIndexFile.isAssigned(r.getSysid1());
	}
	
	
	/**
	 * check if the euid for a given sysid is already assigned
	 * 
	 * @param sysid
	 * @return
	 */
	private boolean isAssigned(long sysid) {
		return euidIndexFile.isAssigned(sysid);
	}

	/**
	 * @return the euidGenerator
	 */
	protected EuidGenerator getEuidGenerator() {
		return euidGenerator;
	}

	/**
	 * read one record at a time from the good file, assign euid and distribute
	 * the dataobject in the corresponding bucket, the bucket id allocated to a
	 * dataObject is based on euid assigned to that dataobject
	 * 
	 * <p>
	 * 
	 * this will be long running method based on the no of input records in the
	 * good file
	 * 
	 */
	protected void distributeBucket() {

		try {
			DataObjectReader reader = config.getDataObjectReader();
			while (true) {
				DataObject d = reader.readDataObject();

				if (d == null) {
					break;
				} else {
					long sysid = Long.parseLong(d.getFieldValue(0));
					EuidWeight ew = getEuid(sysid);

					distributor.distribute(d, ew.getEuid(), String.valueOf(ew
							.getWeight()));
				}
			}

			distributor.close();

		} catch (Exception e) {
			logger.severe(e.getMessage());
		}

	}

	/**
	 * get the euid and maxWeight for given sysid, read the value from the euid
	 * index file if the euid has already been assigned or assign the next euid
	 * using the euid generator. For max weight consider all the link for a
	 * given sysid and chose the max of the all links
	 * 
	 * @param sysid
	 * @return
	 * @throws Exception
	 */
	private EuidWeight getEuid(long sysid) throws Exception {

		EuidWeight ew = new EuidWeight();

		if (isAssigned(sysid)) {

			EuidIndexFileRecord record = euidIndexFile.getRecord(sysid);
			ew.setEuid(record.getEuid());

			ew.setWeight(getWeight(record.getSysidIndex()));

			return ew;
		}
		ew.setEuid(euidGenerator.getNextEUID(null));

		return ew;
	}

	private double getWeight(long sysidIndex) {
		List<MatchFileRecord> records = matchFileReader
				.readRecordsWithSameSysid(sysidIndex - 1);

		if (debug)
			logger.info(records.toString());

		double maxWeight = 0;
		for (MatchFileRecord r : records) {
			if (r.getWeight() > maxWeight) {
				maxWeight = r.getWeight();
			}
		}

		return maxWeight;
	}

	public static void main(String[] args) {
		// LoaderConfig cf = LoaderConfig.getInstance();
		// for (int i = 0; i < 10; i++) {
		// try {
		// DataObjectReader r = cf.getDataObjectReader();
		// r.readDataObject();
		// r.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }

		EuidIndexAssigner ea = new EuidIndexAssigner();
		ea.start();
	}

}
