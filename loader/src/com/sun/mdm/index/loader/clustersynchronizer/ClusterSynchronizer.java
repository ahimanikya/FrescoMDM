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

import com.sun.mdm.index.loader.clustersynchronizer.dao.DAOFactory;
import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas
 * 
 */
public class ClusterSynchronizer extends ClusterSynchronizerAbs {

	private static ClusterSynchronizer instance = new ClusterSynchronizer();

	// private static Logger logger = Logger.getLogger(ClusterSynchronizer.class
	// .getName());

	/**
	 * each loader should have a singleton instance of a cluster synchronizer
	 * 
	 * @return
	 */
	public static ClusterSynchronizer getInstance() {
		return instance;
	}

	private ClusterSynchronizer() {

		String dbType = loaderConfig.getSystemProperty("cluster.database");

		DAOFactory daf = DAOFactory.getDAOFactory(dbType);
		bucketDAO = daf.getBucketDAO();
		loaderDAO = daf.getLoaderDAO();

	}

	protected ClusterSynchronizer(LoaderConfig config) {

		this.loaderConfig = config;
		String dbType = loaderConfig.getSystemProperty("cluster.database");

		DAOFactory daf = DAOFactory.getDAOFactory(dbType);
		bucketDAO = daf.getBucketDAO();
		loaderDAO = daf.getLoaderDAO();

		loaderDAO.setConfig(loaderConfig);

	}

	/**
	 * @param bucketName
	 * @return
	 * @see com.sun.mdm.index.loader.clustersynchronizer.dao.BucketDAO#insertBlockBucket(java.lang.String)
	 */
	public int insertBlockBucket(String bucketName) {
		return bucketDAO.insertBlockBucket(bucketName);
	}

	/**
	 * allocates a new bucket to staging area of a loader and return name of
	 * bucket file. 1.Make database call - “select for update” for ClusterState
	 * table to lock the table. 2.Once it locks the table, get a NEW fileBucket
	 * from ClusterBuckets table. Change its state to ASSIGNED 3.copy the file
	 * to local staging area (using ftp/shell script mechanism) 4.return file
	 * Name or null (if there is no NEW file).
	 */
	public String getBlockBucket() {

		String loaderName = loaderConfig.getSystemProperty("loaderName");

		return getBlockBucket(loaderName);

	}

	/**
	 * @param bucketName
	 * @return
	 * @see com.sun.mdm.index.loader.clustersynchronizer.dao.BucketDAO#insertSBRBucket(java.lang.String)
	 */
	public int insertSBRBucket(String bucketName) {
		return bucketDAO.insertSBRBucket(bucketName);
	}

	/**
	 * @return
	 * @see com.sun.mdm.index.loader.clustersynchronizer.dao.BucketDAO#getSBRBucket()
	 */
	public String getSBRBucket() {
		String loaderName = loaderConfig.getSystemProperty("loaderName");
		return this.getSBRBucket(loaderName);
	}

	/**
	 * @param bucketName
	 * @return
	 * @see com.sun.mdm.index.loader.clustersynchronizer.dao.BucketDAO#insertEUIDBucket(java.lang.String)
	 */
	public int insertEUIDBucket(String bucketName) {
		return bucketDAO.insertEUIDBucket(bucketName);
	}

	/** Works like getBlockBucket, except it returns NEW EUID bucket. */
	public String getEUIDBucket() {
		String loaderName = loaderConfig.getSystemProperty("loaderName");
		return this.getEUIDBucket(loaderName);

	}

	/**
	 * Will return , if all block buckets in clusterBucket table have state
	 * DONE, else will poll internally until state is set to DONE. Invoked by a
	 * Master loader after getBlockBucket() returns null. So can proceed to EUID
	 * Assignment.
	 */
	public void waitMatchingDone() {
		bucketDAO.waitMatchingDone();
	}

	/**
	 * Will return , if all EUID buckets in clusterBucket table have state DONE,
	 * else will poll internally until state is set to DONE. Invoked by a Master
	 * loader after getEUIDBucket() returns null. So can proceed to
	 * potential-duplicate generation.
	 */
	public void waitMasterIndexDone() {
		bucketDAO.waitMasterIndexDone();
	}

	/**
	 * indicates match is done for that loader. 1.copy matchFile to master
	 * loader MatchFile staging area. 2.set states for all bucket Files for that
	 * loader to DONE.
	 */
	public void setMatchDone(String matchFile) {

		String loaderName = loaderConfig.getSystemProperty("loaderName");
		this.setMatchDone(matchFile, loaderName);
	}

	/**
	 * 1. copy the sbrdataFile from the slave loader stage(sbr-input/stage/) to
	 * the master loader stage(sbr-input/stage) 2. indicate that masterIndex
	 * generation has been done for that loader, set the state of all the EUID
	 * bucket files allocated to that loader to DONE
	 */
	public void setMasterIndexDone(String sbrdataFile) {

		copySBRDataFileToStagingArea(sbrdataFile);
		String loaderName = loaderConfig.getSystemProperty("loaderName");
		bucketDAO.setMasterIndexDone(loaderName);
	}

	/**
	 * indicates SBR file creation is done for that loader. 1.copy SBR file to
	 * master loader SBR file staging area. 2.set states for all bucket Files
	 * for that loader to DONE.
	 */
	public void setSBRDone(String sbrFile) {
		// copy SBR file to master loader SBR file staging area

		String loaderName = loaderConfig.getSystemProperty("loaderName");

		setSBRDone(sbrFile, loaderName);
	}

	/**
	 * true/false if SBR matching for all loaders is done. Will return true, if
	 * all SBR block buckets in clusterBucket table have state DONE, else will
	 * poll internally until state is set to DONE. Invoked by a Master loader
	 * after getSBRBucket() returns null. So can proceed to next step.
	 */
	public void waitSBRDone() {
		bucketDAO.waitSBRDone();
	}

	/**
	 * Will be invoked by each loader during initialization.
	 */
	public void initLoaderName(String loaderName, boolean isMaster) {
		loaderDAO.initLoaderName(loaderName, isMaster);
	}

	/**
	 * invoked by master loader to change state of Cluster 1.set state to
	 * DISTRIBUTION, before starting distribution 2.set state to MATCH, after
	 * block buckets are distribute. 3.set state to EUID_ASSIGN after all bucket
	 * files states are set to DONE 4.set state to SBR_CALCULATION, after EUID
	 * buckets are generated 5.set state to POTDUP_BLOCK, after all EUID bucket
	 * states are set to DONE. 6.Set state to POTDUP_MATCH, after SBR Buckets
	 * are generated. 7.Set state to LOADER, after all SBR bucket states are set
	 * to DONE.
	 */
	public void setClusterState(int state) {
		loaderDAO.setClusterState(state);
	}

	
	public void setLoaderState(int state) {
		loaderDAO.setLoaderState(state);
	}
	
	/**
	 * poll internally until MasterIndexGenerator is ready to be proceeded.
	 * Invoked by slave loaders
	 */
	public void waitMasterIndexGenerationReady() throws TimeOutException {

		loaderDAO.waitMasterIndexGenerationReady();
	}

	/**
	 * this method internally poll until SBR matching is ready would be invoked
	 * by slave loaders before invoking Matching SBRs to find potential
	 * duplicates
	 */
	public void waitSBRMatchingReady() throws TimeOutException {
		loaderDAO.waitSBRMatchingReady();
	}
	
	
	

	/**
	 * this method internally poll until Matcher is ready to go or time expires
	 * DataLoaderException – stop the loading and exit would be invoked by slave
	 * loader
	 */
	public void waitMatchingReady() throws TimeOutException {

		loaderDAO.waitMatchingReady();
	}

	/**
	 * called by the master loader to set the state of the analysis table to be
	 * ready
	 */
	public void setAnalysisReady() {
		loaderDAO.setAnalysisReady();
	}

	/**
	 * called by the slave loaders to check whether it will start to insert
	 * weight analysis record, will wait till the master loader signals that the
	 * slaves can start the analysis
	 */
	public void waitAnalysisReady() {
		loaderDAO.waitAnalysisReady();
	}

	/**
	 * called by the master loader to check whether all the slaves are done with
	 * their analysis
	 */
	public void waitAnalysisDone() {
		loaderDAO.waitAnalysisDone();
	}

	/**
	 * this will be called by the slave loaders once inserting of all the
	 * records is done
	 */
	public void setAnalysisDone() {
		loaderDAO.setAnalysisDone();
	}
	
	public void waitBulkLoadingDoneBySlaves() {
		loaderDAO.waitBulkLoadingDoneBySlaves();
	}
	
	public void close(){
		loaderDAO.close();
		bucketDAO.close();
	}
}
