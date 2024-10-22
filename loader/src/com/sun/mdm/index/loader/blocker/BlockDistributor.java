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
package com.sun.mdm.index.loader.blocker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.DataObjectFileWriter;
import com.sun.mdm.index.dataobject.DataObjectReader;
import com.sun.mdm.index.dataobject.DataObjectWriter;

import com.sun.mdm.index.dataobject.epath.DOEpath;

import com.sun.mdm.index.dataobject.objectdef.Lookup;
import com.sun.mdm.index.dataobject.objectdef.ObjectDefinition;

import com.sun.mdm.index.loader.clustersynchronizer.ClusterSynchronizer;
import com.sun.mdm.index.loader.common.FileManager;
import com.sun.mdm.index.loader.config.LoaderConfig;
import com.sun.mdm.index.loader.matcher.Bucket;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathParser;
import com.sun.mdm.index.objects.epath.EPathException;

import com.sun.mdm.index.dataobject.DataObjectDirReader;
import com.sun.mdm.index.loader.common.ObjectNodeUtil;
import com.sun.mdm.index.matching.Standardizer;
import com.sun.mdm.index.matching.StandardizerFactory;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.loader.common.BucketSplitter;
import com.sun.mdm.index.loader.common.LoaderException;


/**
 * Read input data, 
 * Distributes match fields data to different block bucket files.
 * @author Swaranjit Dua
 *
 */

public class BlockDistributor {
	private LoaderConfig config_ = LoaderConfig.getInstance();
	private int numBuckets_ = 1;
	private BlockDefinition[] blockDefinitions_;
	private String[] matchEpaths_;
	private ObjectMeta[] matchGroups_;
	private Bucket[] buckets_;
	private Lookup inputLk_;
	private Lookup blockLk_;
	private boolean isSBR_;
	private Standardizer mStandardizer = null;
	ClusterSynchronizer clusterSynchronizer_ = ClusterSynchronizer.getInstance();
	private static Logger logger = Logger.getLogger(BlockDistributor.class
			.getName());
	private boolean isStandardize = false;
	private DataObjectReader reader_ = null;
	private ObjectDefinition inputobd_;
	private DataObjectWriter inputWriter_;


	public BlockDistributor(String[] matchpaths, Lookup inLk, ObjectDefinition inputobd, Lookup blLk, boolean isSBR) 
	throws LoaderException {
		try {
			this.matchEpaths_ = matchpaths;
			blockDefinitions_ = readBlockConfiguration();
			this.inputLk_ = inLk;
			this.blockLk_ = blLk;
			isSBR_ = isSBR;
			matchGroups_ = groupEpaths(matchEpaths_);
			initializeBuckets();	
			String sisStandardize = config_.getSystemProperty("standardizationMode");
			if (sisStandardize != null && !isSBR_) { // SBR data is already standardized
				isStandardize = Boolean.parseBoolean(sisStandardize);
			}
			if (isStandardize) { // standardize only if standardize option is set
				// and block distribution is done for Block buckets
				mStandardizer = StandardizerFactory.getInstance();
				inputWriter_ = new DataObjectFileWriter(FileManager.getInputStandardizedFile().getAbsolutePath(), true);

			}
			inputobd_ = inputobd;
		} catch (Exception ex) {
			throw new LoaderException(ex);
		}

	}



	/**
	 * distributes match Data to different block buckets.
	 * @throws Exception
	 */
	public void distributeBlocks() throws LoaderException {	
		DataObject inputdataObject = null;
		try {
			DataObjectReader reader = getReader();

			int countRec = 0;
			while (true) {					
				inputdataObject = reader.readDataObject();

				if (inputdataObject == null) {
					break;
				}		   
				countRec++;
				if (isStandardize ) { // 
					inputdataObject = standardize(inputdataObject);
					inputWriter_.writeDataObject(inputdataObject);
				}

				List<String> blockids = getBlockIds(inputdataObject);

				if (blockids != null && blockids.size() > 0) {
					DataObject matchObject = new DataObject();			   
					setMatchObject(matchObject, inputdataObject);

					for (int i = 0; i < blockids.size(); i++) {
						String blockid = blockids.get(i);
						matchObject.setFieldValue(0, blockid);
						int bucketNum = getBucketNum(blockid);
						Bucket bucket = buckets_[bucketNum];
						bucket.write(matchObject);			   
					}
				}
				if (!isSBR_) {
					writeSystemBlock(inputdataObject);
				}

			}
			reader.close();
			if (isStandardize) {
				inputWriter_.close();
			}
			splitBuckets();
			logger.info("Number of Input records:" + countRec);
		}  catch (Throwable th) {
			if (inputdataObject != null) {
				logger.severe("DataObject:" + inputdataObject.toString());
			}
			if (th instanceof Exception) {
				throw new LoaderException((LoaderException) th);
			}
		}
	}

	private void splitBuckets() throws LoaderException {

		HashMap<Integer, DataObjectWriter> buckets = new HashMap<Integer, DataObjectWriter>();

		for (int i = 0; i < buckets_.length; i++) {
			buckets_[i].close();
			File f = buckets_[i].getFile();
			if (f.length()>0) {	
				buckets.put(i, null);
			} else {
				f.delete();
			}
		}			

		if (!isSBR_) {
			String bDir = FileManager.getBlockBucketDir();
			String bucketPrefix = FileManager.BLOCK_BUCKET_PREFIX;
			BucketSplitter splitter = new BucketSplitter(bucketPrefix, bDir, numBuckets_, buckets);
			buckets = splitter.splits();
			for (int i : buckets.keySet()) {				
				clusterSynchronizer_.insertBlockBucket(bucketPrefix + i);
			}
		} else {
			String bDir = FileManager.getsbrBlockBucketDir();
			String bucketPrefix = FileManager.SBRBLOCK_BUCKET_PREFIX;
			BucketSplitter splitter = new BucketSplitter(bucketPrefix, bDir, numBuckets_, buckets);
			buckets = splitter.splits();
			for (int i : buckets.keySet()) {				
				clusterSynchronizer_.insertSBRBucket(bucketPrefix + i);
			}
		}


	}


	public static boolean isSystemBlock(String blockid) {
		if(blockid != null && blockid.startsWith("Systemlid")) {
			return true;
		}
		return false;
	}


	/**
	 * This is special block that contains only systemcode, lid, GID. The purpose of this block is to associate
	 * duplicate records that have same systemcode/lid and later these duplicates are assigned same EUID and then
	 * are merged by Master Index Generator.
	 */

	private void writeSystemBlock(DataObject d) throws Exception {
		String GID = d.getFieldValue(0);
		String systemcode = d.getFieldValue(1);
		String lid = d.getFieldValue(2);

		String blockid = "Systemlid:"+ systemcode + lid;
		DataObject sysmatchObject = new DataObject();
		sysmatchObject.addFieldValue(blockid);
		sysmatchObject.addFieldValue(GID);
		sysmatchObject.addFieldValue(systemcode);
		sysmatchObject.addFieldValue(lid);
		int bucketNum = getBucketNum(blockid);
		Bucket bucket = buckets_[bucketNum];
		bucket.write(sysmatchObject);

		//	systemMap.put(systemcode+lid, systemcode+lid);

	}


	private void initializeBuckets() throws IOException {
		long memorySize = 100000000;// bytes
		long numRecords = 1000000;
		int avgRecordSize = 200;
		int bucketFactor = 2;
		long maxBucketSize = memorySize/10;
		String snumbuckets = config_.getSystemProperty("numBlockBuckets");
		numBuckets_ = Integer.parseInt(snumbuckets);
		File[] files = null;
		if (!isSBR_) {
			//numBuckets_ = (int)(numRecords*avgRecordSize*bucketFactor/maxBucketSize);
			files = FileManager.createBlockBucketFiles(numBuckets_);
		} else {
			files = FileManager.createSbrblockBucketFiles(numBuckets_);
		}

		buckets_ = new Bucket[numBuckets_];
		for (int i = 0; i < numBuckets_; i++) {
			DataObjectWriter dataObjectWriter = new DataObjectFileWriter(files[i].getAbsolutePath(), true);
			buckets_[i] = new Bucket(dataObjectWriter, files[i]);
		}		
	}



	private int getBucketNum(String id) {
		long hash = JSHash(id);
		int bucketNum = (int)(hash%numBuckets_);
		bucketNum = Math.abs(bucketNum);
		return bucketNum;

	}


	private long JSHash(String str) {
		long hash = 1315423911;
		return JSHash(str, hash );
	}

	private long JSHash(String str, long hash)
	{	      
		for(int i = 0; i < str.length(); i++)
		{
			hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
		}
		return hash;
	}

	private int countNullBlids = 0;

	private List<String> getBlockIds(DataObject dataObject) throws Exception {		
		List<String> blockids = new ArrayList<String>(); 
		for (int i = 0; i < blockDefinitions_.length; i++) {
			BlockDefinition blockDef = blockDefinitions_[i];
			List<String> bids = blockDef.getBlockIds(dataObject, inputLk_);
			if (bids!= null && bids.size() > 0) {
				blockids.addAll(bids);
			} else {
				countNullBlids++;
			}
		}

		return blockids;
	}

	/**
	 * set match fields data from input Data to the matchDataObject.
	 * precondtion: matchobject is an empty DataObject.
	 * Match configuration is stored in matchGroups which is an array.
	 * We are assuming that object is only two level, ie. Person.Address.
	 * SO based on matchGroups configuration, this copies the object and their fields values from input data 
	 * to matchObject.
	 * for more than 2 levels would require a different strategy to 
	 * traverse these dataobjects like a DFS tree search.
	 * @param matchObject top level object to which fields and objects need to be copied from input data
	 * @param inputData top level input data from which data needs to be copied. 
	 */

	private void setMatchObject(DataObject matchObject, DataObject inputData) 
	throws Exception {

		for (int i = 0; i < matchGroups_.length; i++) {

			ObjectMeta objectMeta = matchGroups_[i];

			EPath objectEpath = objectMeta.objectEpath;
			int[] matchFieldIndices = objectMeta.matchFieldPositions;
			int[] inputFieldIndices = objectMeta.inputFieldPositions;

			if (i == 0) { // first one is the root
				for (int k = 0; k < matchFieldIndices.length; k++) {

					int matchFieldIndex = matchFieldIndices[k];
					int inputFieldIndex = inputFieldIndices[k];

					String value = inputData.getFieldValue(inputFieldIndex);
					matchObject.setFieldValue(matchFieldIndex, value);
				}

			} else {

				List<DataObject> children = DOEpath.getDataObjectList(
						objectEpath, inputData, inputLk_);
				if (children != null) {			   			
					for (int j = 0; j < children.size(); j++) {
						DataObject inputChild = children.get(j);  
						DataObject child = new DataObject();
						DOEpath.addDataObject(objectEpath, matchObject, child, blockLk_);
						for (int k = 0; k < matchFieldIndices.length; k++) {			    	
							int matchFieldIndex = matchFieldIndices[k];
							int inputFieldIndex = inputFieldIndices[k];			    	
							String value = inputChild.getFieldValue(inputFieldIndex);
							child.setFieldValue(matchFieldIndex, value);
						}

					}
				}

			}
		}
	}

	DataObjectReader getReader() throws Exception  {
		DataObjectReader reader = null;
		if (reader_ != null) {
			return reader_;
		}
		//File file = FileManager.getInputGoodFile();				
		//DataObjectReader reader = new DataObjectFileReader(file);		
		//DataObjectReader reader = new DataObjectCreateReader(inputLk_);
		if (!isSBR_) {
			reader = config_.getDataObjectReader();
		} else {

			File dir = FileManager.getSBRStageDir();				
			reader = new DataObjectDirReader(dir);
		}
		return reader;			
	}

	void setReader(DataObjectReader reader) throws Exception {
		reader_ = reader;
	}

	private DataObject standardize(DataObject d)throws Exception {
		String id = d.remove(0); // GID
		String syscode = d.remove(0); // syscode
		String lid = d.remove(0); //lid
		String updateDateTime =  d.remove(0);  // update date
		String updateUser = d.remove(0);  // create user
		// Pass only object attributes in DataObject
		SystemObject so = ObjectNodeUtil.getSystemObject(d, lid, syscode,
				updateDateTime, updateUser);

		mStandardizer.standardize(so);
		ObjectNode o = so.getObject();

		DataObject data = ObjectNodeUtil.fromObjectNode(o);

		data.add(0,id);
		data.add(1,syscode);
		data.add(2,lid);
		data.add(3,updateDateTime);
		data.add(4,updateUser);
		return data;

	}


	private ObjectMeta[] groupEpaths(String[] matchEPaths) throws LoaderException {

		/*
		 * The algorithm has two parts
		 * 1.  it creates a Map of {key=childtype, value=List of fields}
		 *  and puts primary (root) object (Person) in primary object and primaryFields.
		 * 2. It converts Map to array of ObjectMeta. The first element in ObjectMeta
		 *    is a primary object (Person).
		 */

		try {
			EPath primaryObject = null;
			List<String> primaryFields = new ArrayList<String>();
			Map<String, List<String>> map = new HashMap<String,List<String>>();
			for (int i = 0; i < matchEPaths.length; i++) {
				EPath e = EPathParser.parse(matchEPaths[i]);
				String objectType = e.getLastChildPath();
				String lastChildName = e.getLastChildName();
				String fieldName = e.getFieldTag();

				if (fieldName.equals("blockID")) {
					// Don't access blockId from input data. 
					continue;
				}

				// check if it is primary object, then don't add it to map
				if (objectType.equals(lastChildName)) {
					if (primaryObject == null) {
						primaryObject = EPathParser.parse(objectType);
					}
					primaryFields.add(fieldName);			
				} else { 
					List<String> list = map.get(objectType);
					if (list == null) {
						list = new ArrayList<String>();
						list.add(fieldName);
						map.put(objectType, list);
					} else {
						list.add(fieldName);				
					}
				}		    				
			}

			/*
			 *  convert map to array of ObjectMeta
			 */
			int size = map.size();

			ObjectMeta[] matchGroup = new ObjectMeta[size+1];

			matchGroup[0] = new ObjectMeta();
			matchGroup[0].objectEpath = primaryObject;
			//matchGroup[0].partialfieldEpaths = new EPath[primaryFields.size()];
			//primaryFields.toArray(matchGroup[0].partialfieldEpaths);
			setFieldIndices(matchGroup[0], primaryFields);
			Set<Map.Entry<String,List<String>>> set = map.entrySet();
			Iterator<Map.Entry<String, List<String>>> iterator = set.iterator();
			for (int i = 1; iterator.hasNext(); i++ ) {

				matchGroup[i] = new ObjectMeta();
				Map.Entry<String,List<String>> entry = iterator.next();
				matchGroup[i].objectEpath = EPathParser.parse(entry.getKey());
				List<String> list = entry.getValue();
				setFieldIndices(matchGroup[i], list);

			}
			return matchGroup;	
		} catch (EPathException epx) {
			throw new LoaderException(epx);
		}
	}

	private void setFieldIndices(ObjectMeta objMeta, List<String> fields) {
		objMeta.inputFieldPositions = new int[fields.size()];
		objMeta.matchFieldPositions = new int[fields.size()];
		String prefix = objMeta.objectEpath.getName();
		for (int i = 0; i < fields.size(); i++) {
			objMeta.inputFieldPositions[i] = inputLk_.getFieldIndex(fields.get(i), prefix); 
			objMeta.matchFieldPositions[i] = blockLk_.getFieldIndex(fields.get(i), prefix); 
		}				
	}



	/**
	 *  This stores field position for a match field in the input data and the position in the output match 
	 *  record.
	 *  The fields considered are for one object type like Person, Address etc.
	 *  
	 *  
	 */

	private static class ObjectMeta {

		private EPath objectEpath; // such as Person.Address or Person
		private int[] matchFieldPositions;
		private int[] inputFieldPositions;

	}


	private BlockDefinition[] readBlockConfiguration() throws LoaderException {

		List<BlockDefinition> blockDefList = config_.getBlockDefinitions();

		BlockDefinition[] blockdefs = new BlockDefinition[blockDefList.size()];
		blockDefList.toArray(blockdefs);

		return blockdefs;
	}




}
