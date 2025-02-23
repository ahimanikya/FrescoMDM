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
import com.sun.mdm.index.loader.clustersynchronizer.dao.DAOFactory;
import com.sun.mdm.index.matching.StandardizerFactory;
import com.sun.mdm.index.loader.common.LoaderException;
import com.sun.mdm.index.loader.util.Localizer;
import net.java.hulp.i18n.Logger;

/**
 * This component generates Master Index Files.
 * Reads one EUID Bucket at a time and pass it to MIndexTask that creates Master Index data
 *  
 * @author Swaranjit Dua
 *
 */
public class MasterIndex {

	private static Logger logger = Logger.getLogger("com.sun.mdm.index.loader.masterindex.MasterIndex");
	private static Localizer localizer = Localizer.getInstance();

	private int poolSize_ = 1;
	private ExecutorService executor_;
	private MasterImageWriter writer;
	private ClusterSynchronizer clusterSynchronizer_ = ClusterSynchronizer.getInstance();
	private LoaderConfig config_ =  LoaderConfig.getInstance();
	private ObjectDefinition objectDef_;
	Connection con_;

	public MasterIndex() throws LoaderException {
		try {

			String isSMasterLoader = config_.getSystemProperty("isMasterLoader");

			String numThreads = config_.getSystemProperty("numThreads");
			if (numThreads != null) {
				poolSize_ = Integer.parseInt(numThreads);
			}

			executor_ = Executors.newFixedThreadPool(poolSize_);

			con_ = DAOFactory.getConnection();
			objectDef_ = config_.getObjectDefinition();
			writer = new MasterImageWriter();
		} catch (java.sql.SQLException s) {
			throw new LoaderException (s);
		}

	}

	public void generateMasterIndex() throws LoaderException {
		try {
			File bucketFile;
			DecisionMakerConfiguration dmConfig = (DecisionMakerConfiguration) ConfigurationService
			.getInstance().getConfiguration(
					DecisionMakerConfiguration.DECISION_MAKER);
			DefaultDecisionMaker decision = (DefaultDecisionMaker) dmConfig.getDecisionMaker();
			boolean sameSystemMatch = decision.isSameSystemMatchEnabled();          


			Standardizer[] standardizer = new Standardizer[poolSize_];
			for (int i = 0; i < poolSize_; i++)  {	 
				standardizer[i] = StandardizerFactory.getInstance();	 	 
			}

			while (true) {
				bucketFile = getBucketFile();
				if (bucketFile == null) {
					break;
				}			    												
				DataObjectReader reader = new DataObjectFileReader(bucketFile.getAbsolutePath(), true);		
				EUIDBucket bucket = new EUIDBucket(reader, bucketFile.getName());
				logger.info(localizer.x("LDR054: Loading EUID bucket:{0}", bucketFile.getName()));
				
				bucket.load();

				/*
				 * Each Map is for different MIndexTask that is executed on a pooled 
				 * thread. allTableData stores the Map<String,TableData> for each thread.
				 */
				List<Map<String,TableData>> allTableData = new ArrayList<Map<String,TableData>>();

				/*
		 		 * All MIndexTask would share the same EOCursor that point to same bucket, 
		 		 * but different instance of "SO list". These compute one EO/SBR and related 
		 		 * objects at a time.
				 */
				EUIDBucket.EOCursor cursor = bucket.getEOCursor();
				CountDownLatch endGate = new CountDownLatch(poolSize_);
				for (int i = 0; i < poolSize_; i++)  {
					Map<String,TableData> tableMap = new HashMap<String, TableData>();
					allTableData.add(tableMap);
					MIndexTask task = 
						new MIndexTask(tableMap, cursor, objectDef_, standardizer[i], endGate, con_,
								sameSystemMatch);
					executor_.execute(task);		    
				}
				
				/*
				 * wait for all MIndexTasks to finish
				 */
				endGate.await();
				writer.write(allTableData);
				bucket.close();
			} // end while true
			con_.close();
			writer.close(); 
			executor_.shutdown();
		} catch (Exception e) {
			throw new LoaderException (e);
		}
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
