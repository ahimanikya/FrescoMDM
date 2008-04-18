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


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.sql.Connection;

import com.sun.mdm.index.matching.Standardizer;
import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.configurator.impl.decision.DecisionMakerConfiguration;
import com.sun.mdm.index.configurator.impl.standardization.StandardizationConfiguration;
import com.sun.mdm.index.dataobject.DataObjectFileReader;
import com.sun.mdm.index.dataobject.DataObjectReader;
import com.sun.mdm.index.dataobject.objectdef.Field;
import com.sun.mdm.index.dataobject.objectdef.Lookup;
import com.sun.mdm.index.dataobject.objectdef.ObjectDefinition;
import com.sun.mdm.index.decision.impl.DefaultDecisionMaker;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterSynchronizer;
import com.sun.mdm.index.loader.common.FileManager;
import com.sun.mdm.index.loader.config.LoaderConfig;
import static com.sun.mdm.index.loader.masterindex.MIConstants.*;
import com.sun.mdm.index.loader.clustersynchronizer.dao.DAOFactory;
import com.sun.mdm.index.matching.StandardizerFactory;

/**
 * 1. 
 * @author Swaranjit Dua
 *
 */
public class MasterIndex {
	
	private int poolSize_ = 1;
	private ExecutorService executor_;
	//private Lookup lookup_;
	private MasterImageWriter writer;

	/*
	private static String[] fixedTables = {SYSTEMOBJECT, SYSTEMSBR, ENTERPRISE,
			TRANSACTION, ASSUMEDMATCH };
*/
	ClusterSynchronizer clusterSynchronizer_ = ClusterSynchronizer.getInstance();
	private LoaderConfig config_ =  LoaderConfig.getInstance();
	private ObjectDefinition objectDef_;
	private static Logger logger = Logger.getLogger(MasterIndex.class
			.getName());
	Connection con_;
	
	

	
	
	public MasterIndex() throws Exception {
		
		String isSMasterLoader = config_.getSystemProperty("isMasterLoader");
		//isMasterLoader_ = Boolean.parseBoolean(isSMasterLoader);
		
		String numThreads = config_.getSystemProperty("numThreads");
		if (numThreads != null) {
		    poolSize_ = Integer.parseInt(numThreads);
		}
		
		executor_ = Executors.newFixedThreadPool(poolSize_);
		
		con_ = DAOFactory.getConnection();
		objectDef_ = config_.getObjectDefinition();
		//getEUIDLookup();
		writer = new MasterImageWriter();
		
	}
	

		
   public void generateMasterIndex() throws Exception {
		
	 File bucketFile;
	 DecisionMakerConfiguration dmConfig = (DecisionMakerConfiguration) ConfigurationService
     .getInstance().getConfiguration(
             DecisionMakerConfiguration.DECISION_MAKER);
	 DefaultDecisionMaker decision = (DefaultDecisionMaker) dmConfig.getDecisionMaker();
     boolean sameSystemMatch = false; //decision.isSameSystemMatchEnabled();          
	 
     
	 Standardizer[] standardizer = new Standardizer[poolSize_];
	 for (int i = 0; i < poolSize_; i++)  {	 
	   standardizer[i] = StandardizerFactory.getInstance();	 	 
	 }
	 
	 //Standardizer standardizer = StandardizerFactory.getInstance();
	 
     while (true) {
		bucketFile = getBucketFile();
		if (bucketFile == null) {
			break;
		}			    												
		DataObjectReader reader = new DataObjectFileReader(bucketFile.getAbsolutePath(), true);		
		EUIDBucket bucket = new EUIDBucket(reader, bucketFile.getName());
		logger.info("EUID bucket:"+ bucketFile.getName() + " processing");
		bucket.load();
				
		/**
		 * Each Map is for different MIndexTask that is executed on a pooled 
		 * thread. allTableData stores the Map<String,TableData> for each thread.
		 */
		List<Map<String,TableData>> allTableData = new ArrayList<Map<String,TableData>>();
				
		/*
		 All MIndexTask would share the same EOCursor that is one per bucket, 
		 and compute one EO/SBR and related objects at a time.
		*/
		EUIDBucket.EOCursor cursor = bucket.getEOCursor();
		CountDownLatch endGate = new CountDownLatch(poolSize_);
		for (int i = 0; i < poolSize_; i++)  {
			// tableMap- String - name of table, TableData- data for that table
			Map<String,TableData> tableMap = new HashMap<String, TableData>();
			allTableData.add(tableMap);
			MIndexTask task = 
				 new MIndexTask(tableMap, cursor, objectDef_, standardizer[i], endGate, con_,
						 sameSystemMatch);
		    executor_.execute(task);		    
		}
		
		/**
		 * wait for all MIndexTasks to finish
		 */
		endGate.await();
		writer.write(allTableData);
		bucket.close();
	
	 } // end while true
     		 
	// clusterSynchronizer_.setMatchDone(output.getName());
     
     con_.close();
     writer.close(); 
     executor_.shutdown();
              
   }
	
	
	private File getBucketFile() throws IOException {
	    String fileName = clusterSynchronizer_.getEUIDBucket();
		if (fileName == null) {
			return null;
		}
		String euidDir = FileManager.getEUIDBucketDir();				
		 File  file = new File(euidDir, fileName);
		
		return file;
	}
		
		
}
