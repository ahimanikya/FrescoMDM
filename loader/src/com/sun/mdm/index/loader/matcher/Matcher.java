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


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.sun.mdm.index.dataobject.DataObjectFileReader;
import com.sun.mdm.index.dataobject.DataObjectReader;
import com.sun.mdm.index.dataobject.objectdef.Lookup;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterSynchronizer;
import com.sun.mdm.index.loader.common.FileManager;
import com.sun.mdm.index.loader.config.LoaderConfig;
import com.sun.mdm.index.loader.analysis.WeightAnalyzer;


/** Matches block of records.
 * 1. each concurrent Matcher in a different server, process one block bucket at a time
 * (via ClusterSynchronizer). 
   2. Load block bucket in memory. 
   3. Match records for each block.
   4. Determine matches and write Match Records to a Match file.
   5. Merge Match Files into single Match File.
 * @author Swaranjit Dua
 *
 */
public class Matcher {

	private double matchThreshold_ = 0;
	private int poolSize_ = 1;
	private ExecutorService executor_;
	private String[] paths_;
	private String[] matchTypes_;
	private Lookup lookup_;
	private List<File> matchFiles_ = new ArrayList<File>();
	private ClusterSynchronizer clusterSynchronizer_ = ClusterSynchronizer.getInstance();
	private LoaderConfig config_ =  LoaderConfig.getInstance();
	boolean isMasterLoader_;
	private boolean ismatchAnalyzer = false;
	private double duplicateThreshold_ = 0;
	private boolean isSBR_ = false;
	private File bucketFile_;
	private static Logger logger = Logger.getLogger(Matcher.class
			.getName());
	private int matchFlushSize_ = 0;
	private String dateFormatString_;
	private int blockPrintSize_ = 0;

	public Matcher(String[] paths,  String[] matchTypes, Lookup blLk, boolean isSBR, String dateFormat) throws Exception {
		matchThreshold_ = config_.getMatchThreshold();
		duplicateThreshold_ = config_.getDuplicateThreshold();
		isSBR_ = isSBR;		
		String isSMasterLoader = config_.getSystemProperty("isMasterLoader");
		isMasterLoader_ = Boolean.parseBoolean(isSMasterLoader);

		String isSmatchAnalyzer = config_.getSystemProperty("matchAnalyzerMode");
		if (isSmatchAnalyzer != null && !isSBR) {
			ismatchAnalyzer = Boolean.parseBoolean(isSmatchAnalyzer);
		}

		String numThreads = config_.getSystemProperty("numThreads");
		if (numThreads != null) {
			poolSize_ = Integer.parseInt(numThreads);
		}

		String smatchFlushSize = config_.getSystemProperty("matchFlushSize");
		if (smatchFlushSize != null) {
			matchFlushSize_ = Integer.parseInt(smatchFlushSize);
		}

		String sblockPrintSize = config_.getSystemProperty("blockPrintSize");
		if (sblockPrintSize != null) {
			blockPrintSize_ = Integer.parseInt(sblockPrintSize);
		}

		executor_ = Executors.newFixedThreadPool(poolSize_);
		paths_ = paths;
		matchTypes_ = matchTypes;
		lookup_ = blLk;
		dateFormatString_ = dateFormat;					
	}


	/**
	 * the core workhorse function.
	 * Keep reading Block Bucket file, matches and output to Match Files.
	 * Do until no more Block Bucket is to be matched.
	 * @throws Exception
	 */
	public void match() throws Exception {

		WeightAnalyzer analyzer = null;

		boolean first = false;
		while (true) {
			bucketFile_ = getBucketFile();
			if (bucketFile_ == null) {
				break;
			}			

			Comparator<MatchRecord> comp = getComparator();									
			DataObjectReader reader = new DataObjectFileReader(bucketFile_.getAbsolutePath(), true);		
			Bucket bucket = new Bucket(reader, bucketFile_, isSBR_);
			bucket.setBlockPrintSize(blockPrintSize_);
			logger.info("Block bucket:"+ bucket.getFile().getName() + " processing ");

			bucket.load();

			/**
			 * Each TreeMap is for different MatcherTask that is executed on a pooled 
			 * thread. This treeMap stores the MatchResult from each thread.
			 */
			TreeMap<MatchRecord, String>[] treeMaps = new TreeMap[poolSize_];		
			/*
		 All MatcherTask would share the same MatchCursor and match on
		 one BlockPosition at a time.
			 */
			Bucket.MatchCursor cursor = bucket.getMatchCursor();
			CountDownLatch endGate = new CountDownLatch(poolSize_);

			for (int i = 0; i < poolSize_; i++)  {
				treeMaps[i] = new TreeMap<MatchRecord,String>(comp);
				MatcherTask task = new MatcherTask((TreeMap<MatchRecord,String>)treeMaps[i], 
						cursor, paths_,  matchTypes_, lookup_, matchThreshold_, duplicateThreshold_, endGate, 
						isSBR_, this, matchFlushSize_, dateFormatString_);

				if (first == false && ismatchAnalyzer) {				
					first = true;
					String[] matchEpaths = task.getMatchEpaths();
					analyzer = initAnalyzer(matchEpaths);

				}
				task.setMatchAnalyzer(analyzer);

				executor_.execute(task);
			}


			/**
			 * wait for all MatcherTasks to finish, that will output MatchRecord
			 *  in the passed TreeMap.
			 */
			endGate.await();

			bucket.close();

			if (ismatchAnalyzer) {
				continue; // skip match files
			}

			TreeMap<MatchRecord,String> allMap = new TreeMap<MatchRecord,String>(comp);

			for (int i = 0; i < treeMaps.length; i++)  {			
				allMap.putAll(treeMaps[i]);
			}

			flushMap(allMap);


		} // end while true

		executor_.shutdown();

		if (!ismatchAnalyzer) {
			mergeMatchFiles();  
		} else {
			analyzer.close();
		}

	}

	private WeightAnalyzer initAnalyzer(String[] matchEpaths)  {
		WeightAnalyzer analyzer;

		ArrayList<String> matchPathList = new ArrayList<String>();
		for (String path: matchEpaths) {
			matchPathList.add(path);
		}
		matchPathList.add(0,"systemcode");
		matchPathList.add(1,"localid");				
		analyzer = new WeightAnalyzer(matchPathList);
		return analyzer;   
	}

	private void mergeMatchFiles() throws Exception {
		File output = null;
		if (!isSBR_) {
			output = FileManager.createMatchStageFile();
		} else {
			output = FileManager.createSBRMatchStageFile();
		}
		MatchFileMerger fileMerger = new MatchFileMerger(isSBR_);
		fileMerger.merge(matchFiles_, output);
		if (!isSBR_) {
			clusterSynchronizer_.setMatchDone(output.getName());
		} else {
			clusterSynchronizer_.setSBRDone(output.getName());
		}

		if (isMasterLoader_) {
			if (isSBR_) {
				clusterSynchronizer_.waitSBRDone();	
				File[] finalMatchStageFiles = FileManager.getAllSBRMatchStageFiles();
				File finalMatch = FileManager.getFinalSBRMatchFile();
				List<File> finalMatchStageFileList = new ArrayList<File>();
				for (int i = 0; i < finalMatchStageFiles.length; i++) {
					finalMatchStageFileList.add(finalMatchStageFiles[i]);
				}
				fileMerger = new MatchFileMerger(isSBR_);
				logger.info("merging match files");
				fileMerger.merge(finalMatchStageFileList, finalMatch);
			} else {
				clusterSynchronizer_.waitMatchingDone();	
				File[] finalMatchStageFiles = FileManager.getAllMatchStageFiles();
				File finalMatch = FileManager.getFinalMatchFile();
				List<File> finalMatchStageFileList = new ArrayList<File>();
				for (int i = 0; i < finalMatchStageFiles.length; i++) {
					finalMatchStageFileList.add(finalMatchStageFiles[i]);
				}		 
				fileMerger = new MatchFileMerger(isSBR_);
				logger.info("merging match files");
				fileMerger.merge(finalMatchStageFileList, finalMatch);
			}
		}
		logger.info("merged match files");
	}


	File getBucketFile() throws IOException {
		String fileName = null;
		if (!isSBR_) {
			fileName = clusterSynchronizer_.getBlockBucket();
		} else {
			fileName = clusterSynchronizer_.getSBRBucket();

		}
		if (fileName == null) {
			return null;
		}
		File  file = null;
		if (!isSBR_) {
			String blockDir = FileManager.getBlockBucketDir();				
			file = new File(blockDir, fileName);
		} else {
			String blockDir = FileManager.getsbrBlockBucketDir();				
			file = new File(blockDir, fileName);
		}
		return file;
	}


	private int counter;
	private Object lock = new Object();
	void flushMap(TreeMap<MatchRecord,String> map) throws Exception {		
		File matchFile;
		logger.info("TreeMap size: " + map.size());
		synchronized(lock) {
			counter++;
			if (!isSBR_) {
				matchFile = FileManager.createMatchFile(bucketFile_, counter);	
			} else {
				matchFile = FileManager.createSBRMatchFile(bucketFile_, counter);
			}
		}

		boolean flag = write(map, matchFile);
		synchronized(lock) {
			if (flag == true) {
				matchFiles_.add(matchFile);
			}
		}
		map.clear();
	}

	private boolean write( TreeMap<MatchRecord,String> map, File matchFile) throws IOException {			
		Set<Map.Entry<MatchRecord,String>> set = map.entrySet();

		if (set.isEmpty()) {
			return false;
		}

		MatchWriter writer = null;
		if (!isSBR_) {
			writer = new MatchGIDWriter(matchFile);
		} else {
			writer = new MatchEUIDWriter(matchFile);
		}

		for(Map.Entry<MatchRecord,String> entry : set) {
			MatchRecord record = entry.getKey();
			writer.write(record);
		}
		writer.close();	
		return true;
	}


	private Comparator<MatchRecord> getComparator() {
		Comparator<MatchRecord> comp = new Comparator<MatchRecord>() {
			public int compare(MatchRecord record1, MatchRecord record2) {
				return record1.compare(record2);
			}			
		};
		return comp;	
	}	
}
