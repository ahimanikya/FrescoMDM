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
package com.sun.mdm.index.loader.main;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.sun.mdm.index.dataobject.objectdef.Lookup;
import com.sun.mdm.index.dataobject.objectdef.Field;
import com.sun.mdm.index.dataobject.objectdef.ObjectDefinition;
import com.sun.mdm.index.dataobject.objectdef.ObjectDefinitionBuilder;
import com.sun.mdm.index.loader.blocker.BlockDistributor;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterSynchronizer;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterState;
import com.sun.mdm.index.loader.common.FileManager;
import com.sun.mdm.index.loader.common.ObjectNodeUtil;
import com.sun.mdm.index.loader.common.Util;
import com.sun.mdm.index.loader.matcher.Matcher;
import com.sun.mdm.index.loader.config.LoaderConfig;
import com.sun.mdm.index.loader.euid.EuidIndexAssigner;
import com.sun.mdm.index.loader.log.LoaderLogManager;
import com.sun.mdm.index.loader.masterindex.MasterIndex;
import com.sun.mdm.index.loader.masterindex.PotDupGenerator;
import com.sun.mdm.index.loader.sqlloader.BulkLoader;

import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.configurator.ConfigurationInfo;


/** Main class to bulk Match and load data to eview master index. 
 *  This in turn invokes:
 *  BlockDistributor: to distribute blocks to Bucket file.
 *  Matcher: to do match on records within a block
 *  EUID Assigner: To assings EUID to the system objects.
 *  Master Index Generator: To calculate SBR, transactions, assumed matches,
 *  Enterprise Objects.
 *  Database Loader: to load data into eview master index
 *  Potential duplicate Generator: To generate potential duplicates
 * 
 * @author Swaranjit Dua
 *
 */

public class BulkMatcherLoader {
	
	private static Logger logger = Logger.getLogger(BulkMatcherLoader.class
			.getName());
	private LoaderConfig config_ = LoaderConfig.getInstance();
	private String[] matchPaths_;
	private String[] sbrmatchPaths_;
	private String[] matchTypes_;
	private Lookup inputLookup_;
	private Lookup blockLk_;
	private Lookup sbrblockLk_;
	boolean isMasterLoader_ = true; 
	ClusterSynchronizer clusterSynchronizer_;
	private String loaderName_;
	private Lookup sbrLookup_;
	private boolean ismatchAnalyzer;
	private boolean delInterMediateDir = true;
	private boolean bulkLoad = false;
	private static int UPTO_EUIDASSIGN = 1; // generate upto EUIDAssign
	private static int MIGENERATE_ONLY = 2; // generate master Index only
	private static int BLOCKDISTRIBUTE_ONLY = 3;
	private static int BLOCKDISTRIBUTE_AFTER = 4;
	private int loadMode_ = 0; // default mode
	public BulkMatcherLoader() throws Exception {
            new LoaderLogManager().init();
		logger.info("bulk_boader_started");				
		loadConfig();
		
		ConfigurationService.getInstance();
		
		logger.info("configuation_loaded");
		
		if (isMasterLoader_) {
		   logger.info("master_loader:" + loaderName_);
		} else {
		   logger.info("slave_loader:" + loaderName_);
		}
		
	    ObjectNodeUtil.initDataObjectAdapter();	
	}
	
	public void bulkMatchLoad() throws Exception {	
	 if (loadMode_ != MIGENERATE_ONLY)	 {
	  if (loadMode_ != BLOCKDISTRIBUTE_AFTER) {
		clusterSynchronizer_.initLoaderName(loaderName_, isMasterLoader_);
	    if (isMasterLoader_) { 
		  logger.info("block_distribution_started");
		  clusterSynchronizer_.setClusterState(ClusterState.BLOCK_DISTRIBUTION);		
	      BlockDistributor blockDistributor = new BlockDistributor(matchPaths_, inputLookup_, blockLk_, false);	
	      blockDistributor.distributeBlocks();
	      logger.info("block_distribution_completed");
	      clusterSynchronizer_.setClusterState(ClusterState.MATCHING);	    
	    } else {
		  logger.info("waiting_for_block_distribution");
		  clusterSynchronizer_.waitMatchingReady();		 
	    }
	  }
	    
	    if (loadMode_ == BLOCKDISTRIBUTE_ONLY) {
	    	return;
	    }
	    logger.info("matcher_started");	
	    Matcher matcher = new Matcher(matchPaths_, matchTypes_, blockLk_, false);
	    matcher.match();
	    logger.info("matching_done"); 
	 
	    FileManager.deleteBlockDir(false);
	    if (ismatchAnalyzer) {
		  return;
	    }
	    logger.info("EUID_Assigner_started");	
	  
	    if (isMasterLoader_) { 
	      EuidIndexAssigner euidAssigner = new EuidIndexAssigner();
	      euidAssigner.start();
	    }  else {
		    clusterSynchronizer_.waitMasterIndexGenerationReady(); 
	    }
	  
	    FileManager.deleteMatchDir(false);
	  
	    logger.info("EUID_Assigner_Done");
	  	  	  
	  }
	 
      if (loadMode_ != UPTO_EUIDASSIGN) {  // Do following operations only if mode is not EUIDASSIGN	 
	    logger.info("master_index_generation_started");	
	    MasterIndex masterIndex = new MasterIndex();
	    masterIndex.generateMasterIndex();
	    
	    if (loadMode_ == MIGENERATE_ONLY ) {
	      logger.info("Master Index Generated");
	      
	  	  System.exit(0);
	    }
	  
	    File file = FileManager.getInputSBRFile();
	    String f = file.getName();
	  
	    clusterSynchronizer_.setMasterIndexDone(f);
	  
	    if (isMasterLoader_) {
		  clusterSynchronizer_.waitMasterIndexDone();
	    }
	  
	    FileManager.deleteEUIDDir(false);
	  
	    logger.info("master_index_generation_completed");
	  
	    logger.info("potential_duplicates_started");
	  
	    if (isMasterLoader_) { 	 
			clusterSynchronizer_.setClusterState(ClusterState.POT_DUPLICATE_BLOCK);							
		    BlockDistributor blockDistributor = new BlockDistributor(sbrmatchPaths_, sbrLookup_, sbrblockLk_, true);	
		    blockDistributor.distributeBlocks();
		    logger.info("Potental Dups SBR block distribution completed");
		    clusterSynchronizer_.setClusterState(ClusterState.POT_DUPLICATE_MATCH);	    
		 } else {
			 //logger.info("waiting for Pot Dup SBR block distribution to be completed");
			 clusterSynchronizer_.waitSBRMatchingReady();		 
		 }
		 
		 	
	   Matcher matcher = new Matcher(sbrmatchPaths_, matchTypes_, sbrblockLk_, true);
	   matcher.match();
	   logger.info("Potental Dups maching completed");
	   FileManager.deleteSBRBlockDir(false);
	   FileManager.deleteSBRInputDir(true);
	   if (isMasterLoader_) { 	 
	     PotDupGenerator potGen = new PotDupGenerator();
	     potGen.generatePotDups();
	   }
	 
	   logger.info("Potential duplicates completed");
		   	  
	   FileManager.deleteSBRMatchDir(false);
	 
	   if (bulkLoad) {	 
	      BulkLoader bl = new BulkLoader();
	      bl.load(); 
	   }
      }
	 
	  logger.info("Bulk Loader Completed");
	
	}
	
	private void cleanDirs() {
		FileManager.deleteBlockDir(true);
		FileManager.deleteMatchDir(true);
		FileManager.deleteEUIDDir(true);
		FileManager.deleteMasterIndexDir(true);
		FileManager.deleteSBRBlockDir(true);
		FileManager.deleteSBRMatchDir(true);
		FileManager.deleteSBRInputDir(true);
	}
	
	private void loadConfig() throws Exception {
						
		String workingDir = config_.getSystemProperty("workingDir");
		loaderName_ = config_.getSystemProperty("loaderName");
		FileManager.setWorkingDir(workingDir, loaderName_, delInterMediateDir);
		String sloadMode= config_.getSystemProperty("loadMode");
		if (sloadMode != null) {
			 if (sloadMode.equals("MIGENERATE_ONLY")) { 
			  loadMode_ = MIGENERATE_ONLY;
			} else if (sloadMode.equals("UPTO_EUIDASSIGN")) { 
			  loadMode_ = UPTO_EUIDASSIGN;
			} else if (sloadMode.equals("BLOCKDISTRIBUTE_AFTER")) { 
			  loadMode_ = BLOCKDISTRIBUTE_AFTER;
			} else if (sloadMode.equals("BLOCKDISTRIBUTE_ONLY")) { 
			  loadMode_ = BLOCKDISTRIBUTE_ONLY;
			} 
        }
		if (loadMode_ != MIGENERATE_ONLY && loadMode_ != BLOCKDISTRIBUTE_AFTER) {
		  cleanDirs();
		  FileManager.initDirs();
		}
				
        clusterSynchronizer_ = ClusterSynchronizer.getInstance();
		
		String isMasterLoader = config_.getSystemProperty("isMasterLoader");
		isMasterLoader_ = Boolean.parseBoolean(isMasterLoader);
		
		//clusterSynchronizer_.initLoaderName(loaderName_, isMasterLoader_);
		
		String isSmatchAnalyzer = config_.getSystemProperty("matchAnalyzerMode");
		if (isSmatchAnalyzer != null) {
		  ismatchAnalyzer = Boolean.parseBoolean(isSmatchAnalyzer);
		}
		
		String sdelInterMediateDir = config_.getSystemProperty("deleteIntermediateDirs");
		if (sdelInterMediateDir != null) {
			delInterMediateDir = Boolean.parseBoolean(sdelInterMediateDir);
		}
		
		String sbulkLoad = config_.getSystemProperty("BulkLoad");
		if (sbulkLoad != null) {
			bulkLoad = Boolean.parseBoolean(sbulkLoad);
		}
		
		List<String> matchlPaths = config_.getMatchFields();//readMatchConfiguration();	
		String[] matchPaths = new String[matchlPaths.size()];
		matchlPaths.toArray(matchPaths);
		
		matchTypes_ = readMatchTypes();
		matchPaths_ = addExtraBucketFields(matchPaths);
		sbrmatchPaths_ = addExtraSBRFields(matchPaths);				
		initLookup();				
				
	}	
	
	
	public static void  main(String[] args) {
		try {		 
		 BulkMatcherLoader bulkMatcher = new BulkMatcherLoader();
		 bulkMatcher.bulkMatchLoad();
		 
		} catch (Throwable ex) {
			logger.severe(ex + ex.getMessage());
			logger.severe(Util.getStackTrace(ex));
			ex.printStackTrace();
		}			
	}
	
	private void initLookup() throws Exception {
				 		
		ObjectDefinition obd = ObjectDefinitionBuilder.buildObjectDefinition(matchPaths_);
		blockLk_ = Lookup.createLookup(obd);
		
		obd = ObjectDefinitionBuilder.buildObjectDefinition(sbrmatchPaths_);
		sbrblockLk_ = Lookup.createLookup(obd);
		
		inputLookup_ = getInputLookup();
		
		sbrLookup_ = getSBRLookup();
	}
	
		
	private String[] readMatchTypes() {

		List<String> matchlTypes = config_.getMatchFieldTypes();
		String[] matchTypes = new String[matchlTypes.size()];
		matchlTypes.toArray(matchTypes);
		
		return matchTypes;
	}
	
	private String[] addExtraBucketFields(String[] paths) {
		int size = paths.length + 4; 
		String[] blockEpaths = new String[size];
		int index = paths[0].indexOf(".");
		
		String primaryObject = null;
		
		if (index > 0) {
		   primaryObject = paths[0].substring(0, index);	
		}
		
		blockEpaths[0] = primaryObject + ".blockID";
		blockEpaths[1] = primaryObject + ".GID";
		blockEpaths[2] = primaryObject + ".systemcode";		
		blockEpaths[3] = primaryObject + ".lid";
		
		System.arraycopy(paths, 0, blockEpaths, 4, paths.length);		
		return blockEpaths;
	}
	
	private String[] addExtraSBRFields(String[] paths) {
		int size = paths.length + 2; 
		String[] blockEpaths = new String[size];
		int index = paths[0].indexOf(".");
		
		String primaryObject = null;		
		if (index > 0) {
		   primaryObject = paths[0].substring(0, index);	
		}
		
		blockEpaths[0] = primaryObject + ".blockID";
		blockEpaths[1] = primaryObject + ".EUID";
			
		System.arraycopy(paths, 0, blockEpaths, 2, paths.length);		
		return blockEpaths;
	}
	
	private Lookup getInputLookup() throws Exception {
		ObjectDefinition obd = config_.getObjectDefinition();
		Field f = new Field();
		f.setName("GID");
		obd.addField(0,f);
		
				
		f = new Field();
		f.setName("systemcode");
		obd.addField(1,f);
		
		f = new Field();
		f.setName("lid");
		obd.addField(2,f);
		
		f = new Field();
		f.setName("updateDateTime");
		obd.addField(3,f);
		
		f = new Field();
		f.setName("createUser");
		obd.addField(4,f);
	
		Lookup lookup = Lookup.createLookup(obd);
		return lookup;
	}
	
	private Lookup getSBRLookup() throws Exception {
		ObjectDefinition obd = config_.getObjectDefinition();
		Field f = new Field();
		f.setName("EUID");
		obd.addField(0,f);
		
		Lookup lookup = Lookup.createLookup(obd);
		return lookup;
	}
	
	
	

}
