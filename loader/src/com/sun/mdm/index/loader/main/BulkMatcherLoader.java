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
import java.io.File;
import java.util.List;

import com.sun.mdm.index.dataobject.objectdef.Lookup;
import com.sun.mdm.index.dataobject.objectdef.Field;
import com.sun.mdm.index.dataobject.objectdef.ObjectDefinition;
import com.sun.mdm.index.dataobject.objectdef.ObjectDefinitionBuilder;
import com.sun.mdm.index.loader.blocker.BlockDistributor;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterSynchronizer;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterState;
import com.sun.mdm.index.loader.common.FileManager;
import com.sun.mdm.index.loader.common.ObjectNodeUtil;
import com.sun.mdm.index.loader.matcher.Matcher;
import com.sun.mdm.index.loader.config.LoaderConfig;
import com.sun.mdm.index.loader.euid.EuidIndexAssigner;
import com.sun.mdm.index.loader.log.LoaderLogManager;
import com.sun.mdm.index.loader.masterindex.MasterIndex;
import com.sun.mdm.index.loader.masterindex.PotDupGenerator;
import com.sun.mdm.index.loader.sqlloader.BulkLoader;
import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.loader.util.Localizer;
import net.java.hulp.i18n.Logger;
import com.sun.mdm.index.loader.common.LoaderException;

/** 
 *  Main driver class that bulk Match  and load Master Data to master index schema. 
 *  This in turn invokes:
 *  BlockDistributor: to distribute blocks to Bucket file.
 *  Matcher: to do match operation on records within a block
 *  EUID Assigner: To assign EUID to the system objects.
 *  Master Index Generator: To calculate SBR, transactions, assumed matches,
 *  Enterprise Objects.
 *  Database Loader: to load data into master index
 *  Potential duplicate Generator: To generate potential duplicates
 * 
 * @author Swaranjit Dua, Charles Ye
 */

public class BulkMatcherLoader {

	private static Logger logger = Logger.getLogger("com.sun.mdm.index.loader.main.BulkMatcherLoader");
	private static Localizer localizer = Localizer.getInstance();
	    
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
	private ObjectDefinition inputobd_;
	private String dateFormatString_;

	public BulkMatcherLoader() throws Exception {
		
		RuntimeStats();
		new LoaderLogManager().init();
		logger.info(localizer.x("LDR001: Bulk Loader Started"));

		config_.initValidation();		
		config_.validation();
		
		loadConfig();
		ConfigurationService.getInstance();	
		logger.info(localizer.x("LDR002: Configuration loaded"));

		if (isMasterLoader_) {
			logger.info(localizer.x("LDR003: Master Loader: {0}", loaderName_));
		} else {
			logger.info(localizer.x("LDR004: Slave Loader: {0}", loaderName_));
		}

		ObjectNodeUtil.initDataObjectAdapter();	
		dateFormatString_ = ObjectNodeUtil.getDateFormatString();
	}

	public void bulkMatchLoad() throws Exception {

		if (loadMode_ != MIGENERATE_ONLY)	 {
			if (loadMode_ != BLOCKDISTRIBUTE_AFTER) {
				clusterSynchronizer_.initLoaderName(loaderName_, isMasterLoader_);
				if (isMasterLoader_) { 
					clusterSynchronizer_.setClusterState(ClusterState.BLOCK_DISTRIBUTION);		
					BlockDistributor blockDistributor = new BlockDistributor(matchPaths_, inputLookup_, inputobd_, blockLk_, false);	
					RuntimeStats();
					logger.info(localizer.x("LDR005: Block Distribution Started"));
					blockDistributor.distributeBlocks();
					logger.info(localizer.x("LDR006: Block Distribution Completed"));
					RuntimeStats();

					clusterSynchronizer_.setClusterState(ClusterState.MATCHING);	    
				} else {
					logger.info(localizer.x("LDR007: Waiting for Block Distribution"));
					clusterSynchronizer_.waitMatchingReady();		 
				}
			}

			if (loadMode_ == BLOCKDISTRIBUTE_ONLY) {
				return;
			}
			logger.info(localizer.x("LDR008: Matcher Started"));
			Matcher matcher = new Matcher(matchPaths_, matchTypes_, blockLk_, false, dateFormatString_);
			matcher.match();
			logger.info(localizer.x("LDR009: Matching Done"));
			RuntimeStats();

			FileManager.deleteBlockDir(false);
			if (ismatchAnalyzer) {
				return;
			}
				
			logger.info(localizer.x("LDR010: EUID Assigner Started"));
			if (isMasterLoader_) { 
				EuidIndexAssigner euidAssigner = new EuidIndexAssigner();
				euidAssigner.start();
			}  else {
				clusterSynchronizer_.waitMasterIndexGenerationReady(); 
			}
			RuntimeStats();
			FileManager.deleteMatchDir(false);
			logger.info(localizer.x("LDR011: EUID Assigner Done"));
		}

		if (loadMode_ != UPTO_EUIDASSIGN) {  
			// Do following operations only if mode is not EUIDASSIGN
			logger.info(localizer.x("LDR012: Master Index Generation Started"));
			MasterIndex masterIndex = new MasterIndex();
			masterIndex.generateMasterIndex();

			RuntimeStats();
			if (loadMode_ == MIGENERATE_ONLY ) {
				logger.info(localizer.x("LDR013: Master Index Generated"));
				System.exit(0);
			}

			File file = FileManager.getInputSBRFile();
			String f = file.getName();

			clusterSynchronizer_.setMasterIndexDone(f);

			if (isMasterLoader_) {
				clusterSynchronizer_.waitMasterIndexDone();
			}
			FileManager.deleteEUIDDir(false);
			logger.info(localizer.x("LDR014: Master Index Generation Completed"));

			logger.info(localizer.x("LDR015: Potential Duplicates Started"));
			if (isMasterLoader_) { 	 
				clusterSynchronizer_.setClusterState(ClusterState.POT_DUPLICATE_BLOCK);							
				BlockDistributor blockDistributor = new BlockDistributor(sbrmatchPaths_, sbrLookup_, inputobd_,  sbrblockLk_, true);	
				blockDistributor.distributeBlocks();
				logger.info(localizer.x("LDR016: Potental Duplicates SBR Block Distribution Completed"));			
				clusterSynchronizer_.setClusterState(ClusterState.POT_DUPLICATE_MATCH);	    
			} else {
				//logger.info("waiting for Pot Dup SBR block distribution to be completed");
				clusterSynchronizer_.waitSBRMatchingReady();		 
			}

			logger.info(localizer.x("LDR017: Potential Duplicates Maching Started"));	
			Matcher matcher = new Matcher(sbrmatchPaths_, matchTypes_, sbrblockLk_, true, dateFormatString_);
			matcher.match();
			logger.info(localizer.x("LDR018: Potential Duplicates Maching Completed"));			
			FileManager.deleteSBRBlockDir(false);
			FileManager.deleteSBRInputDir(false);
			
			PotDupGenerator potGen = new PotDupGenerator();
			potGen.generatePotDups();
			
			logger.info(localizer.x("LDR019: Potential Duplicates Completed"));
			FileManager.deleteSBRMatchDir(false);

			if (bulkLoad) {	 
				BulkLoader bl = new BulkLoader();
				bl.load(); 
			}
		}
		logger.info(localizer.x("LDR020: Bulk Loader Completed"));
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
		String isMasterLoader = config_.getSystemProperty("isMasterLoader");
		isMasterLoader_ = Boolean.parseBoolean(isMasterLoader);

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
			logger.severe(localizer.x("LDR024: Bulk Loader failed: {0}", ex.getMessage()), ex);
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

		inputobd_ = obd;

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

	private void RuntimeStats() {

		Runtime rt = Runtime.getRuntime();
		long free = rt.freeMemory();
		long total = rt.totalMemory();
		long max = rt.maxMemory();
		logger.info(localizer.x("LDR021: Maximum Memory: {0}", "" + max/10000 + "M"));
		logger.info(localizer.x("LDR022: Total Memory: {0}", "" + total/10000 + "M"));
		logger.info(localizer.x("LDR023: Free Memory: {0}", "" + free/10000 + "M"));
	}
}
