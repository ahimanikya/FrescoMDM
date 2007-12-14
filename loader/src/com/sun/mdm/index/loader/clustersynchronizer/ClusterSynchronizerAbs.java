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

import com.sun.mdm.index.loader.clustersynchronizer.dao.BucketDAO;
import com.sun.mdm.index.loader.clustersynchronizer.dao.LoaderDAO;
import com.sun.mdm.index.loader.clustersynchronizer.ftp.LoaderFtpClient;
import com.sun.mdm.index.loader.config.LoaderConfig;

public abstract class ClusterSynchronizerAbs {

	protected LoaderConfig loaderConfig = LoaderConfig.getInstance();
	protected BucketDAO bucketDAO;
	protected LoaderDAO loaderDAO;

	public ClusterSynchronizerAbs() {
		super();
	}

	protected String getBlockBucket(String loaderName) {
		String file = bucketDAO.getBlockBucket(loaderName);
	
		// get the file using ftp
		if (file != null) {
			retrieveBlockBucketFromMaster(file);
		}
	
		return file;
	}

	protected String getSBRBucket(String loaderName) {
		String file =  bucketDAO.getSBRBucket(loaderName);
		
		if(file != null){
			retrieveSBRBlockBucketFromMaster(file);
		}
		
		
		return file;
	}

	protected String getEUIDBucket(String loaderName) {
		String file = bucketDAO.getEUIDBucket(loaderName);
		
		if(file != null){
			retrieveEUIDBucketFromMaster(file);
		}
		
		
		return file;
	
	}

	protected void setMatchDone(String matchFile, String loaderName) {
	
		copyMatchFileToStagingArea(matchFile);
		bucketDAO.setMatchDone(loaderName);
	}

	protected void setSBRDone(String sbrFile, String loaderName) {
	
		copySBRFileToStagingArea(sbrFile);
	
		bucketDAO.setSBRDone(loaderName);
	}

	/**
	 * @return
	 */
	private LoaderFtpClient newFtpClient() {
		String ftpHostName = loaderConfig.getSystemProperty("ftp.server");
		String ftpUser = loaderConfig.getSystemProperty("ftp.username");
		String ftpPassword = loaderConfig.getSystemProperty("ftp.password");
	
		LoaderFtpClient ftp = new LoaderFtpClient(ftpHostName, ftpUser,
				ftpPassword);
		return ftp;
	}

	protected boolean isMasterLoader() {
		String isMasterLoader = loaderConfig
				.getSystemProperty("isMasterLoader");
	
		return Boolean.parseBoolean(isMasterLoader);
	}

	private void copyMatchFileToStagingArea(String matchFile) {
	
		String localDir = loaderConfig.getSystemProperty("workingDir")
				+ "/match/stage/";
	
		String remoteDir = getMasterWorkingDir() + "/match/stage";
	
		if (!isMasterLoader())
			newFtpClient().storeFile(remoteDir, localDir, matchFile);
	
	}
	
	protected void copySBRDataFileToStagingArea(String sbrdataFile) {
		
		String localDir = loaderConfig.getSystemProperty("workingDir")
				+ "/sbr-input/stage/";
	
		String remoteDir = getMasterWorkingDir() + "/sbr-input/stage";
	
		if (!isMasterLoader())
			newFtpClient().storeFile(remoteDir, localDir, sbrdataFile);
	
	}
	

	private void copySBRFileToStagingArea(String sbrFile) {
		String localDir = loaderConfig.getSystemProperty("workingDir")
				+ "/sbr-match/stage/";
	
		String remoteDir = getMasterWorkingDir() + "/sbr-match/stage";
	
		if (!isMasterLoader())
			newFtpClient().storeFile(remoteDir, localDir, sbrFile);
	
	}

	protected String getMasterWorkingDir() {
		// TODO Auto-generated method stub
		return loaderDAO.getMasterWorkingDir();
	}

	private void retrieveBlockBucketFromMaster(String file) {
		String localDir = loaderConfig.getSystemProperty("workingDir")
				+ "/block/";
	
		String remoteDir = getMasterWorkingDir() + "/block";
	
		retrieveBucketFromMaster(remoteDir, localDir, file);
	
	}

	private void retrieveSBRBlockBucketFromMaster(String file) {
		String localDir = loaderConfig.getSystemProperty("workingDir")
				+ "/sbr-block/";
	
		String remoteDir = getMasterWorkingDir() + "/sbr-block";
	
		retrieveBucketFromMaster(remoteDir, localDir, file);
	
	}

	private void retrieveEUIDBucketFromMaster(String file) {
		String localDir = loaderConfig.getSystemProperty("workingDir")
				+ "/euid/";
	
		String remoteDir = getMasterWorkingDir() + "/euid";
	
		retrieveBucketFromMaster(remoteDir, localDir, file);
	
	}

	/**
	 * @param remoteDir
	 * @param localDir
	 * @param file
	 */
	protected void retrieveBucketFromMaster(String remoteDir, String localDir, String file) {
		if (!isMasterLoader())
			newFtpClient().retrieveFile(remoteDir, localDir, file);
	}

}
