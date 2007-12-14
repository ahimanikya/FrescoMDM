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
package com.sun.mdm.index.loader.clustersynchronizer.dao;

import com.sun.mdm.index.loader.clustersynchronizer.TimeOutException;
import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas
 * 
 */
public interface LoaderDAO {

	/**
	 * Will be invoked by each loader during initialization.
	 */
	public void initLoaderName(String loaderName, boolean isMaster);

	public void setClusterState(int state);
	
	public void setLoaderState(int state);

	public void waitMatchingReady() throws TimeOutException;

	public void waitSBRMatchingReady() throws TimeOutException;

	public void waitMasterIndexGenerationReady() throws TimeOutException;
	
	public void waitBulkLoadingDoneBySlaves();

	public String getMasterWorkingDir();

	/**
	 * called by the master loader to set the state of the analysis table to be
	 * ready
	 */
	public void setAnalysisReady();

	/**
	 * called by the slave loaders to check whether it will start to insert
	 * weight analysis record, will wait till the master loader signals that the
	 * slaves can start the analysis
	 */
	public void waitAnalysisReady();

	/**
	 * called by the master loader to check whether all the slaves are done with
	 * their analysis
	 */
	public void waitAnalysisDone();

	/**
	 * this will be called by the slave loaders once inserting of all the
	 * records is done
	 */
	public void setAnalysisDone();
	
	public void setConfig(LoaderConfig config);

}
