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

package com.sun.mdm.index.loader.sqlloader;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import com.sun.mdm.index.loader.clustersynchronizer.ClusterState;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterSynchronizer;
import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas
 * 
 */
public class BulkLoader {

	private ScriptGenerator scriptGenerator = new ScriptGenerator();

	private static Logger logger = Logger.getLogger(BulkLoader.class.getName());

	private LoaderConfig config = LoaderConfig.getInstance();

	private ClusterSynchronizer clusterSynchronizer = ClusterSynchronizer
			.getInstance();

	public void load() {

		scriptGenerator.generate();

		ArrayList<Table> tables = scriptGenerator.getTables();

		ExecutorService executor = Executors.newFixedThreadPool(tables.size());

		ArrayList<Future<String>> futures = new ArrayList<Future<String>>();

		for (Table t : tables) {
			OracleTableLoader o = new OracleTableLoader(t.getName());

			futures.add(executor.submit(o));
		}

		for (Future<String> f : futures) {
			try {
				f.get();
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}

		if (isMasterLoader()) {
			clusterSynchronizer.waitBulkLoadingDoneBySlaves();
			clusterSynchronizer.setClusterState(ClusterState.BULK_LOADING_DONE);
		} else {
			clusterSynchronizer.setLoaderState(ClusterState.BULK_LOADING_DONE);
		}
		
		executor.shutdown();

	}

	protected boolean isMasterLoader() {
		String isMasterLoader = config.getSystemProperty("isMasterLoader");

		return Boolean.parseBoolean(isMasterLoader);
	}

	public static void main(String[] args) {

		ClusterSynchronizer.getInstance().setLoaderState(
				ClusterState.BULK_LOADING_DONE);

		BulkLoader bl = new BulkLoader();

		long t1 = System.currentTimeMillis();
		bl.load();
		long t2 = System.currentTimeMillis();

		logger.info("time taken in millis: " + (t2 - t1));

	}

}
