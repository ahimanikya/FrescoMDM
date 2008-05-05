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
package com.sun.mdm.index.loader.matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.objectdef.Lookup;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.epath.EPathParser;
import com.sun.mdm.index.loader.analysis.WeightAnalyzer;
import com.sun.mdm.index.filter.ExclusionListLookup;
import com.sun.mdm.index.filter.FilterConstants;
import com.sun.mdm.index.loader.blocker.BlockDistributor;
import com.sun.mdm.index.loader.common.Util;

/**
 *  workhorse class that compares block of records
 *  Each concurrent MatcherTask is executed in a different thread 
    Matches one BlockPosition for a Block at a time.
    Many concurrent threads matches different portion of same bucket at a given time.
    The MatchRecords from the matcherTask are added to the input TreeMap.
    Uses Match Engine to do the actual matching.

 * @author Swaranjit Dua
 *
 */
public class MatcherTask implements Runnable {

	private DataObjectTupleCursor tupleCursor_;
	private double matchThreshold_ = 0;
	private double duplicateThreshold_ = 0;
	private TreeMap<MatchRecord, String> matchTree_ ;
	private Bucket.MatchCursor matchCursor_;
	private CountDownLatch endGate_;
	private MatcherAdapter matchEngine_;
	private static Logger logger = Logger.getLogger(MatcherTask.class
			.getName());
	private WeightAnalyzer matchAnalyzer_;
	private boolean isSBR_;
	private static int DUPSCORE = 999999;
	public static final String SDUPSCORE = "999999";
	private static final String empty_str = "";
	private Matcher matcher_;
	private int treeFlushSize_ = 100000;

	MatcherTask(TreeMap<MatchRecord,String> map, 
			Bucket.MatchCursor cursor, String[] paths, String[] matchTypes, 
			Lookup blLk, double matchThreshold, double duplicateThreshold,
			CountDownLatch endGate, boolean isSBR, Matcher matcher, int matchFlushSize,
			String dateFormat) throws Exception {
		matchThreshold_ = matchThreshold;
		duplicateThreshold_ = duplicateThreshold;
		tupleCursor_ = new DataObjectTupleCursor(paths, matchTypes, blLk, isSBR);
		matchTree_ = map;
		matchCursor_ = cursor;
		endGate_ = endGate;
		matchEngine_ = new MatcherAdapter(tupleCursor_.matchFieldIDs_, dateFormat);
		isSBR_ = isSBR;
		matcher_ = matcher;

		if (matchFlushSize != 0) {
			treeFlushSize_ = matchFlushSize;
		}
	}


	public void run() {
		try {				
			while (true ) {		
				BlockPosition blockPosition = matchCursor_.next();
				if (blockPosition == null) {
					break;
				}	
				int size = blockPosition.getBlock().getSize();
				String bucketName = matchCursor_.getBucketFile();


				match(blockPosition);		  
			}
		} catch (Throwable ex){
			logger.severe(ex + ex.getMessage());
			logger.severe(Util.getStackTrace(ex));
			ex.printStackTrace();
		} finally {

			endGate_.countDown();
		}

	}


	String[] getMatchEpaths() {
		return tupleCursor_.matchEPaths_;
	}

	void setMatchAnalyzer(WeightAnalyzer analyzer) {
		matchAnalyzer_ = analyzer;
	}

	private void match(BlockPosition blockPosition) throws Exception {
		//List<MatchRecord> list = new ArrayList<MatchRecord>();
		int recordPosition = blockPosition.getRecordPosition();
		Block block = blockPosition.getBlock();
		String blockid = block.getBlockId();
		int size = block.getSize();
		/*
		 * matches a given record within block with all other records
		 * in the same block, that are at position > recordPosition.
		 */
		DataObject data1 = block.getRecord(recordPosition);
		String gid1 = data1.getFieldValue(1);
		for (int i = recordPosition +1; i < size; i++) {
			DataObject data2 = block.getRecord(i);
			String gid2 = data2.getFieldValue(1);
			boolean matched = block.isMatched();
			double score = matchThreshold_;

			if (BlockDistributor.isSystemBlock(blockid) && !isSBR_) {
				score = DUPSCORE;
				MatchRecord record = new MatchGIDRecord(gid1, gid2, score);
				matchTree_.put(record, empty_str);
				record = new MatchGIDRecord(gid2, gid1, score);
				matchTree_.put(record, empty_str);
				continue;

			}  else if (matched == false) { // do matching only if block is not already set to MATCHED
				// if it is already set to MATCHED in block definition, bypass matching and every
				// record in the block match to each other
				score = match(data1, data2);
			}

			if (isSBR_) {
				if (score >= duplicateThreshold_ && score < matchThreshold_) {
					String euid1 = data1.getFieldValue(1);
					String euid2 = data2.getFieldValue(1);
					MatchRecord record = new MatchEUIDRecord(euid1, euid2, score);
					matchTree_.put(record, empty_str);		    		
				}
			}
			else if (score >= matchThreshold_ || (matchAnalyzer_ != null && score >= duplicateThreshold_)) {

				MatchRecord record = new MatchGIDRecord(data1, data2, score);
				matchTree_.put(record, empty_str);
				record = new MatchGIDRecord(data2, data1, score);
				matchTree_.put(record, empty_str);
				/**
				 * put any systemcode/lid duplicates (with different gid) on the Match File too. Duplicates
				 * are not used during matching. These all will have same EUID
				 */
				List<String> dupList = block.getDup(gid1);
				if (dupList!= null) {
					for(String gid: dupList) {
						record = new MatchGIDRecord(gid1, gid, score);
						matchTree_.put(record, empty_str);
						record = new MatchGIDRecord(gid, gid1, score);
						matchTree_.put(record, empty_str);
					}
				}
				dupList = block.getDup(gid2);
				if (dupList!= null) {
					for(String gid: dupList) {
						record = new MatchGIDRecord(gid2, gid, score);
						matchTree_.put(record, empty_str);
						record = new MatchGIDRecord(gid, gid2, score);
						matchTree_.put(record, empty_str);
					}
				}

			}
		}
		if (matchTree_.size() > treeFlushSize_ ) {
			matcher_.flushMap(matchTree_);	    	
		}

	}


	private double match(DataObject dataObject1, DataObject dataObject2) 
	throws Exception {


		String syscode1 = null;
		String syscode2 = null;
		String lid1 = null;
		String lid2 = null;

		if (matchAnalyzer_!= null) {
			syscode1 = dataObject1.getFieldValue(2);
			syscode2 = dataObject2.getFieldValue(2);
			lid1 = dataObject1.getFieldValue(3);
			lid2 = dataObject2.getFieldValue(3);
		}

		List<List<String>> tuples1 = convertTuple(dataObject1);
		List<List<String>> tuples2 = convertTuple(dataObject2);
		double score = 0;
		double maxscore = 0;
		for (int i = 0; i < tuples1.size(); i++) {
			for (int j = 0; j <tuples2.size(); j++) {
				List<String> matchTuple1 = tuples1.get(i);
				List<String> matchTuple2 = tuples2.get(j);
				matchTuple1 = filter(matchTuple1);
				matchTuple2 = filter(matchTuple2);
				if (matchAnalyzer_ != null) {
					score = matchAnalysis(matchTuple1, matchTuple2, syscode1, syscode2, lid1, lid2);
				} else {
					score = match(matchTuple1, matchTuple2);
				}		   
				maxscore = (score > maxscore) ? score: maxscore;
			}
		}
		return maxscore;

	}


	private double match(List<String> tuple1, List<String> tuple2) throws Exception
	{		
		String[] values = tuple1.toArray(new String[tuple1.size()]);
		String[] refValues = tuple2.toArray(new String[tuple2.size()]);
		try {
			return matchEngine_.compareRecords(values, refValues);

		} catch (Exception ex) {
			StringBuilder str = new StringBuilder();
			for(int i = 0; i < values.length; i++) {
				String value = values[i];
				if (i != 0) {
					str.append(",");
				}	
				str.append(value);
			}
			logger.info("values:" + str);
			str = new StringBuilder();
			for(int i = 0; i < refValues.length; i++) {
				String value = refValues[i];
				if (i != 0) {
					str.append(",");
				}
				str.append(value);
			}
			logger.info("refvalues:" + str);
			throw ex;
		}							
	}


	private double matchAnalysis(List<String> tuple1, List<String> tuple2, String syscode1, String syscode2,
			String lid1, String lid2) throws Exception
			{
		String[] values = tuple1.toArray(new String[tuple1.size()]);
		String[] refValues = tuple2.toArray(new String[tuple2.size()]);
		double weight = 0;

		String[] matchFields = tupleCursor_.matchFieldIDs_;
		ArrayList<Double> weights = new ArrayList<Double>();
		for (int i = 0; i < matchFields.length; i++ ) {
			String matchField = matchFields[i];
			double wt =  matchEngine_.compareRecords(values[i], refValues[i], matchField);				
			weights.add(wt);
			weight += wt;				
		}

		if (weight < matchThreshold_) {
			return weight;
		}

		ArrayList<String> systuple1 = addSyscodes(tuple1, syscode1, lid1);
		ArrayList<String> systuple2 = addSyscodes(tuple2, syscode2, lid2);

		weights.add(0, weight);
		weights.add(1, weight);

		matchAnalyzer_.insert(systuple1, systuple2, weights);

		return weight;

			}


	/*
	 * A dataObject has a tree struture. For matching between two dataObjects
	 * require matching each different instance of child object among a dataObject.
	 * So the task is to convert a tree to a list of tuples, such that
	 * every tuple is different with each other, in terms of at least one object instance
	 *  and each tuple contains
	 * 1. All field values of primary object.
	 * 2. A different instance of child object for each child object type.
	 * A dataObject that has N child types each with M child instances would 
	 * result in (M exponent N) tuples.
	 * Ex: A tree:
	 *                  John (primary object) with three child object types
	 *                              1st childtype - 2 instances
	 *                              2nd childtype - 2 instances
	 *                              3rd childtype - 3 instances
	 *         Monrovia   626-000-0000    Joe
	 *         Arcadia    999-999-0000    George
	 *                                    Foo
	 *                    
	 *                would result in :2*2*3 tuples.                          
	 *                    
	 */
	private List<List<String>> convertTuple(DataObject dataObject) {

		tupleCursor_.initObjectTypeInstance(dataObject);
		List<List<String>> tuples = new ArrayList<List<String>>();

		while (true) {			
			List<String> tuple = getPrimaryTuple(dataObject);
			for (int i = 0; i < tupleCursor_.childTypeIndices_.length; i++ ) {

				DataObject child = null;
				try {
					child = dataObject.getChild(
							tupleCursor_.childTypeIndices_[i], 
							tupleCursor_.curInstanceIndex_[i]);
				} catch(Exception ex) { }

				for ( int j = 0; j < tupleCursor_.fieldpositions_[i].length; j++) {
					int fieldPosition = tupleCursor_.fieldpositions_[i][j];
					String value = null; 
					if (child != null) {
						value = child.getFieldValue(fieldPosition);
					}
					tuple.add(value);
				}			   			   
			}
			tuples.add(tuple);
			boolean inc = tupleCursor_.incrementCursor();
			if (inc == false) {
				break;
			}
		}
		return tuples;		
	}

	private List<String> filter(List<String> matchValues) {
		String[] epaths = tupleCursor_.matchEPaths_;
		for (int i = 0; i < matchValues.size(); i++) {
			String value = matchValues.get(i);
			String field = epaths[i];
			if (isFieldValueFiltered(field, value)) {
				matchValues.set(i, "");
			}
		}
		return matchValues;
	}


	private boolean isFieldValueFiltered(String field, String value) {
		//return false;
		ExclusionListLookup exlookup = new ExclusionListLookup();
		return exlookup.isFieldValueInExclusion(value, field, FilterConstants.MATCH_EXCLUSION_TYPE);			
	}

	private List<String> getPrimaryTuple(DataObject dataObject) {
		List<String> tuple = new ArrayList<String>();

		for ( int j = 0; j < tupleCursor_.primaryFieldPositions_.length; j++) {
			int fieldPosition = tupleCursor_.primaryFieldPositions_[j];
			String value = dataObject.getFieldValue(fieldPosition);
			tuple.add(value);
		}
		return tuple;		
	}

	private ArrayList<String> addSyscodes(List<String> tuple, String syscode, String lid) {
		ArrayList<String> systuple = new ArrayList<String>();
		systuple.add(syscode);
		systuple.add(lid);
		for (String str: tuple) {
			systuple.add(str);
		}

		return systuple;		
	}


	private static class MatchType{
		private String matchType_;
		private String fieldName_;
		private String epath_;
		MatchType(String matchType, String fieldName, String epath) {
			matchType_ = matchType;
			fieldName_ = fieldName;
			epath_ = epath;
		}
	}

	/**
	 * This functionality is like an odometer. This is used in conversion of a dataobject
	 * to list of tuples. At any given time, this points to one object instance
	 * for each child type in a root data object.
	 * Assumption: Assumes that DataObject is only two levels deep hierarchy, e.g. only
	 * one level of children. To support multiple level of child hierarchy
	 * would require Depth First Search variation of algorithm.
	 * 
	 * @author Swaranjit Dua
	 *
	 */
	private static class DataObjectTupleCursor {
		/*
		 * matchFieldIds that are sent to Match Engine.
		 * Note: Note the positions of field IDs in this array is different than input MatchTypes passed in
		 * MatcherTask constructor. 
		 */
		private String[] matchFieldIDs_;

		/**
		 * matchEpaths that correspond to matchFieldIDs. Used by MatchAnalyzer
		 */
		private String[] matchEPaths_;

		/*
		 * Stores the field positions for primary object such as Person.
		 */
		private int[] primaryFieldPositions_;

		/**
		 * [] represents the order of match objects types that will be sent to
		 * match engine. It is fixed and depends on configuration
		 */
		private int[] childTypeIndices_;
		/*
		 * [][] is array {of childType indices} of array of fields positions 
		 * that will be sent to match engine. Fixed and depends on configuration
		 *
		 */
		private int[][] fieldpositions_;


		/*
		 * varies from one dataobject instance to another.
		 * [] is num of Instances for each child object type
		 */
		private int[] numInstances_;
		/*
		 * Contatins current indices of each child type object instance that is used to convert 
		 * to a tuple. Varies from one tuple to another
		 */

		private int[] curInstanceIndex_;

		DataObjectTupleCursor(String[] matchEPaths, String[] matchTypes, Lookup lk, boolean isSBR) throws EPathException {


			/*
			 * The algorithm has two parts
			 * 1.  it creates a Map of {key=childtype, value=List of fields}
			 *  and puts primary (root) object (Person) in primary object and primaryFields.
			 * 2. It converts Map to field indices
			 */
			List<String> matchFieldIds = new ArrayList<String>();
			List<String> matchFieldIDPaths = new ArrayList<String>(); // These epaths will correspond to matchfieldIDs.

			String primaryObject = null;
			List<MatchType> primaryFields = new ArrayList<MatchType>();
			Map<String, List<MatchType>> map = new HashMap<String,List<MatchType>>();

			for (int i = 0; i < matchEPaths.length; i++) {
				EPath e = EPathParser.parse(matchEPaths[i]);

				String objectType = e.getLastChildPath();
				String lastChildName = e.getLastChildName();
				String fieldName = e.getFieldTag();			
				if (fieldName.equals("blockID") 
						|| fieldName.equals("GID")
						|| fieldName.equals("systemcode")
						|| fieldName.equals("lid")
						|| fieldName.equals("EUID")
				) {
					// Don't access these fields from block data. 
					continue;
				}
				int shiftIndex = 4; // shiftIndex is for the fields that are im matchFields but is not a match attribute.
				if (isSBR) {
					shiftIndex = 2; // blockID, EUID 
				}
				MatchType matchType = new MatchType(matchTypes[i-shiftIndex], fieldName, matchEPaths[i]); // match Types start after. 
				// id fields. We come here after i = shfitIndex.

				// check if it is primary object, then don't add it to map
				if (objectType.equals(lastChildName)) {
					if (primaryObject == null) {
						primaryObject = objectType;
					}

					primaryFields.add(matchType);			
				} else { 
					List<MatchType> list = map.get(objectType);
					if (list == null) {
						list = new ArrayList<MatchType>();
						list.add(matchType);
						map.put(objectType, list);
					} else {
						list.add(matchType);				
					}
				}		    				
			}


			/*
			 *  convert map to indices
			 */
			int size = map.size();

			childTypeIndices_ = new int[size];
			//ObjectMeta[] matchGroup = new ObjectMeta[size+1];
			fieldpositions_ = new int[size][];
			numInstances_ = new int[size];
			curInstanceIndex_ = new int[size];
			primaryFieldPositions_ = new int[primaryFields.size()];
			setFieldIndices(primaryFieldPositions_, primaryObject, matchFieldIds, matchFieldIDPaths, primaryFields, lk);		

			Set<Map.Entry<String,List<MatchType>>> set = map.entrySet();
			Iterator<Map.Entry<String, List<MatchType>>> iterator = set.iterator();
			for (int i = 0; iterator.hasNext(); i++ ) {
				Map.Entry<String,List<MatchType>> entry = iterator.next();
				String objectName = entry.getKey();
				List<MatchType> fieldlist = entry.getValue();
				childTypeIndices_[i] = lk.getChildTypeIndex(objectName);
				fieldpositions_[i] = new int[fieldlist.size()];
				setFieldIndices(fieldpositions_[i], objectName, matchFieldIds, matchFieldIDPaths, fieldlist, lk);

			}		
			matchFieldIDs_ = new String[matchFieldIds.size()];		
			matchFieldIds.toArray(matchFieldIDs_);
			matchEPaths_ = new String[matchFieldIDPaths.size()];		
			matchFieldIDPaths.toArray(matchEPaths_);

		}



		private void setFieldIndices(int[] fieldpositions, String objectName, List<String> matchFieldIds, List<String> matchFieldIDPaths, List<MatchType> fields, Lookup lk) {

			for (int i = 0; i < fields.size(); i++) {				 
				fieldpositions[i] = lk.getFieldIndex(fields.get(i).fieldName_, objectName); 
				matchFieldIds.add(fields.get(i).matchType_);
				matchFieldIDPaths.add(fields.get(i).epath_);
			}					
		}

		/**
		 * increments the curInstanceIndex[] indices in the tuplecursor for 
		 * each chlid type to the ones that is not visisted.
		 * Used for converting tree to list of tuples.
		 * Analogy like a odometer. The ones iterate from 0 to 9, and then
		 * ones are reset to 0. Then tens go from 0 to 1 and ones re-iterate from 0 to 9 again.
		 * and so on...
		 * @return true there is some more tuples to be visited
		 *         false no more instances to be visited
		 */ 
		private boolean incrementCursor() {
			int length = childTypeIndices_.length;
			int i;
			/*
			 * iterate from the last child type to the beginning.
			 * 
			 */
			for (i = length -1; i >= 0; i--) {
				curInstanceIndex_[i]++;

				if (curInstanceIndex_[i] < numInstances_[i]) {
					/*
					 * so more instances to be visited. SO we are done and return true.
					 */
					return true;
				} else {
					curInstanceIndex_[i] = 0; // resets current index pointer to 1st object
					// instance for this child type.
					// and continue in the for loop to increment the current index pointer 
					//for previous child type.
				}
			}
			if (i < 0) {
				return false; // so iterated through all instances
			}
			return true;
		}

		private void initObjectTypeInstance(DataObject dataObject) {
			for (int i = 0; i < childTypeIndices_.length; i++) {
				try {
					numInstances_[i] = 0;
					if (dataObject.hasChild(childTypeIndices_[i])) {
						List<DataObject> children = dataObject.getChildren(childTypeIndices_[i]);

						if (children != null) {
							numInstances_[i] = children.size();
						} else {
							numInstances_[i] = 0;
						}
					}
				} catch (Exception ex) {

					logger.info(ex + ex.getMessage());

				}  finally {			 
					curInstanceIndex_[i] = 0;
				}

			}
		}	   	
	}



}
