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

package com.sun.mdm.index.loader.clustersynchronizer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas
 * 
 */
public class XTestLoader implements Callable<String> {

	private static final int bucketNum = 50;
	private LoaderConfig config;
	private ClusterSynchronizer clusterSynchronizer;

	private String workingDir;
	
	private Logger logger = Logger.getLogger(XTestLoader.class.getName());

	/**
	 * @param config
	 * @param clusterSynchronizer
	 */
	public XTestLoader(LoaderConfig config,
			ClusterSynchronizer clusterSynchronizer) {
		this.config = config;
		this.clusterSynchronizer = clusterSynchronizer;
		
	}

	private void init() {

		String loaderName = config.getSystemProperty("loaderName");

		clusterSynchronizer.initLoaderName(loaderName, isMasterLoader());

		workingDir = config.getSystemProperty("workingDir");

	}

	protected boolean isMasterLoader() {
		String isMasterLoader = config.getSystemProperty("isMasterLoader");

		return Boolean.parseBoolean(isMasterLoader);
	}

	private void distributeBlock() throws IOException, TimeOutException {

		File blockDir = getLoaderFile(workingDir, "block");

		if (isMasterLoader()) {

			clusterSynchronizer
					.setClusterState(ClusterState.BLOCK_DISTRIBUTION);

			for (int i = 0; i < bucketNum; i++) {
				String fileName = blockDir.getAbsolutePath() + "/block-" + i;

				writeFile(fileName);

				clusterSynchronizer.insertBlockBucket("block-" + i);
			}

			clusterSynchronizer.setClusterState(ClusterState.MATCHING);

		} else {
			clusterSynchronizer.waitMatchingReady();
		}

	}

	/**
	 * @return
	 */
	private File getLoaderFile(String base, String name) {
		File file = new File(base, name);
		return file;
	}

	private void match() throws IOException {

		if (isMasterLoader()) {
			while (true) {
				String b = clusterSynchronizer.getBlockBucket();

				if (b == null) {
					break;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			String matchStageFile = workingDir
					+ "/match/stage/matchStage-master";

			writeFile(matchStageFile);

			clusterSynchronizer.setMatchDone("matchStage-master");

			clusterSynchronizer.waitMatchingDone();

			// create finalMatchFile

			clusterSynchronizer.setClusterState(ClusterState.EUID_ASSIGNED);

		} else {

			while (true) {
				String b = clusterSynchronizer.getBlockBucket();

				if (b == null) {
					break;
				}

			}

			String matchStageFile = workingDir
					+ "/match/stage/matchStage-slave";

			writeFile(matchStageFile);

			clusterSynchronizer.setMatchDone("matchStage-slave");

		}

	}

	/**
	 * @param file
	 * @throws IOException
	 */
	private void writeFile(String file) throws IOException {
		FileWriter fw = new FileWriter(file);
		fw.write(file);
		fw.close();
	}

	private void distributeEUID() throws IOException, TimeOutException {

		File euidDir = getLoaderFile(workingDir, "euid");

		if (isMasterLoader()) {

			for (int i = 0; i < bucketNum; i++) {
				String fileName = euidDir.getAbsolutePath() + "/EUID-" + i;

				writeFile(fileName);

				clusterSynchronizer.insertEUIDBucket("EUID-" + i);
			}

			clusterSynchronizer
					.setClusterState(ClusterState.MASTER_INDEX_GENERATE);

		} else {
			clusterSynchronizer.waitMasterIndexGenerationReady();
		}
	}

	private void generateMasterIndex() throws IOException {

		File sbrInputDir = getLoaderFile(workingDir, "sbr-input");
		
		if (isMasterLoader()) {
			while (true) {
				String b = clusterSynchronizer.getEUIDBucket();

				if (b == null) {
					break;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			
			
			String fileName = sbrInputDir.getAbsolutePath() + "/stage/sbrdata-master.txt" ;

			writeFile(fileName);
			
			clusterSynchronizer.setMasterIndexDone("sbrdata-master.txt");
			clusterSynchronizer.waitMasterIndexDone();

			// create finalMatchFile
			clusterSynchronizer
					.setClusterState(ClusterState.POT_DUPLICATE_BLOCK);

		} else {

			while (true) {
				String b = clusterSynchronizer.getEUIDBucket();

				if (b == null) {
					break;
				}

			}

			String fileName = sbrInputDir.getAbsolutePath() + "/stage/sbrdata-slave.txt" ;

			writeFile(fileName);
			
			clusterSynchronizer.setMasterIndexDone("sbrdata-slave.txt");

		}

	}

	private void generatePotentialDuplicate() throws IOException,
			TimeOutException {

		File sbrBlockDir = getLoaderFile(workingDir, "sbr-block");

		if (isMasterLoader()) {

			clusterSynchronizer
					.setClusterState(ClusterState.POT_DUPLICATE_BLOCK);

			for (int i = 0; i < bucketNum; i++) {
				String fileName = sbrBlockDir.getAbsolutePath() + "/sbr-block-"
						+ i;

				writeFile(fileName);

				clusterSynchronizer.insertSBRBucket("sbr-block-" + i);
			}

			clusterSynchronizer
					.setClusterState(ClusterState.POT_DUPLICATE_MATCH);

		} else {
			clusterSynchronizer.waitSBRMatchingReady();
		}

		if (isMasterLoader()) {
			while (true) {
				String b = clusterSynchronizer.getSBRBucket();

				if (b == null) {
					break;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			String matchStageFile = workingDir
					+ "/sbr-match/stage/matchStage-master";

			writeFile(matchStageFile);

			clusterSynchronizer.setSBRDone("matchStage-master");

			clusterSynchronizer.waitSBRDone();

			// create finalMatchFile

			clusterSynchronizer.setClusterState(ClusterState.MASTER_INDEX_LOAD);

		} else {

			while (true) {
				String b = clusterSynchronizer.getSBRBucket();

				if (b == null) {
					break;
				}

			}

			String matchStageFile = workingDir
					+ "/sbr-match/stage/matchStage-slave";

			writeFile(matchStageFile);

			clusterSynchronizer.setSBRDone("matchStage-slave");

		}

	}

	public String call() throws Exception {

		init();
		logger.info("before distributeBlock");
		distributeBlock();
		logger.info("before match");
		
		match();

		logger.info("before distributeEUID" + clusterSynchronizer.isMasterLoader());
		distributeEUID();
		
		logger.info("before generateMasterIndex"+ clusterSynchronizer.isMasterLoader());

		generateMasterIndex();
		
		logger.info("before generatePotentialDuplicate"+ clusterSynchronizer.isMasterLoader());

		generatePotentialDuplicate();
		
		logger.info("before done"+ clusterSynchronizer.isMasterLoader());

		return "done";
	}

}
