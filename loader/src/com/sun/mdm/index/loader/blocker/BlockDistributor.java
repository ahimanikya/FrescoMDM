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
import com.sun.mdm.index.dataobject.epath.ChildMissingException;
import com.sun.mdm.index.dataobject.epath.DOEpath;
import com.sun.mdm.index.dataobject.objectdef.DataObjectAdapter;
import com.sun.mdm.index.dataobject.objectdef.Lookup;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterState;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterSynchronizer;
import com.sun.mdm.index.loader.common.FileManager;
import com.sun.mdm.index.loader.config.LoaderConfig;
import com.sun.mdm.index.loader.matcher.Bucket;
import com.sun.mdm.index.loader.matcher.Matcher;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathParser;
import com.sun.mdm.index.dataobject.DataObjectFileReader;
import com.sun.mdm.index.dataobject.DataObjectDirReader;
import com.sun.mdm.index.loader.common.ObjectNodeUtil;
import com.sun.mdm.index.matching.Standardizer;
import com.sun.mdm.index.matching.StandardizerFactory;
import com.sun.mdm.index.objects.SystemObject;

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
	
	public BlockDistributor(String[] matchpaths, Lookup inLk, Lookup blLk, boolean isSBR) throws Exception {
							
		this.matchEpaths_ = matchpaths;
	    blockDefinitions_ = readBlockConfiguration();
	    this.inputLk_ = inLk;
		this.blockLk_ = blLk;
		isSBR_ = isSBR;
	    matchGroups_ = groupEpaths(matchEpaths_);
		initializeBuckets();	
		String sisStandardize = config_.getSystemProperty("standardizationMode");
		if (sisStandardize != null) {
			isStandardize = Boolean.parseBoolean(sisStandardize);
		}
		mStandardizer = StandardizerFactory.getInstance();
	}
	
	static {
		ObjectNodeUtil.initDataObjectAdapter();
	}
	
	/**
	 * distributes match Data to different block buckets.
	 * @throws Exception
	 */
	public void distributeBlocks() throws Exception {
						
		DataObjectReader reader = getReader();
		
		while (true) {
		
		   DataObject inputdataObject = reader.readDataObject();
		   if (inputdataObject == null) {
			   break;
		   }
		   
		   if (!isSBR_ && isStandardize ) { // SBR data is already standardized
		     inputdataObject = standardize(inputdataObject);
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
		}
		
	//	logger.info("countNullBlids:" + countNullBlids);
		
		for (int i = 0; i < buckets_.length; i++) {
		  buckets_[i].close();
		  File f = buckets_[i].getFile();
		  if (f.length()>0) {	
			if (!isSBR_) {
		       clusterSynchronizer_.insertBlockBucket(buckets_[i].getFile().getName());
			} else {
			   clusterSynchronizer_.insertSBRBucket(buckets_[i].getFile().getName());
			}
		   } else {
			  f.delete();
		  }
		}	
		reader.close();						
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
			DataObjectWriter dataObjectWriter = new DataObjectFileWriter(files[i]);
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
	
	private DataObject standardize(DataObject d)throws Exception {
	    String id = d.remove(0); // GID
	    String syscode = d.remove(0); // syscode
	    String lid = d.remove(0); //lid
	    String updateDateTime =  d.remove(0);  // update date
	    String updateUser = d.remove(0);  // craete user
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
	
	
	private ObjectMeta[] groupEpaths(String[] matchEPaths) throws Exception {
		
		/*
		 * The algorithm has two parts
		 * 1.  it creates a Map of {key=childtype, value=List of fields}
		 *  and puts primary (root) object (Person) in primary object and primaryFields.
		 * 2. It converts Map to array of ObjectMeta. The first element in ObjectMeta
		 *    is a primary object (Person).
		 */
		
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
	 *  This contains an object type and the Epaths for fields that is contained in that
	 *  object type.
	 *  
	 *  
	 */
	
	private static class ObjectMeta {
	   
	   private EPath objectEpath; // such as Person.Address or Person
	   private int[] matchFieldPositions;
	   private int[] inputFieldPositions;
	   	    	   
	}
	
	
	private BlockDefinition[] readBlockConfiguration() throws Exception {
		
		List<BlockDefinition> blockDefList = config_.getBlockDefinitions();
		
		BlockDefinition[] blockdefs = new BlockDefinition[blockDefList.size()];
		blockDefList.toArray(blockdefs);
		
		/*
		BlockDefinition[] blockdefs = new BlockDefinition[3];
		
		blockdefs[0] = new BlockDefinition();
		BlockDefinition.Rule ruleC = new BlockDefinition.Rule();
		BlockDefinition.Rule rule = new BlockDefinition.Rule(new String[]{"Person.FnamePhoneticCode","Person.LnamePhoneticCode"});
		ruleC.addChild(rule);
	    rule = new BlockDefinition.Rule(new String[] {"Person.Alias.FnamePhoneticCode","Person.Alias.LnamePhoneticCode"});
		ruleC.addChild(rule);
		ruleC.setOperator(BlockDefinition.Operator.OR);
		blockdefs[0].addRule(ruleC);
		blockdefs[0].setId("1");
		
		blockdefs[1] = new BlockDefinition();
		blockdefs[1].addRule("Person.SSN");
		blockdefs[1].setId("2");
		
		blockdefs[2] = new BlockDefinition();
		blockdefs[2].addRule("Person.FnamePhoneticCode");
		blockdefs[2].addRule("Person.Gender");
		blockdefs[2].addRule("Person.DOB");
		blockdefs[2].setId("3");
		*/
		return blockdefs;
	}
			
}
