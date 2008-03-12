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

/**
 * @author Sujit Biswas
 * 
 */
public interface BucketDAO {

	// state of bucket files
	public int NEW = 0;

	public int ASSIGNED = 1;

	public int DONE = 2;

	// bucket files
	public int BLOCK = 0;

	public int MATCH = 1;

	public int EUID = 2;

	public int SBR_BLOCK = 3;

	
	
	

	public String getBlockBucket(String loaderName);

	public int insertBlockBucket(String bucketName);

	public String getEUIDBucket(String loaderName);

	public int insertEUIDBucket(String bucketName);

	public String getSBRBucket(String loaderName);

	public int insertSBRBucket(String bucketName);

	public void waitMatchingDone();
	
	public void waitSBRDone();

	public void setMatchDone(String loaderName);

	public void setSBRDone(String sbrFile);
	
	public void setMasterIndexDone(String loaderName);
	
	public void waitMasterIndexDone();
	
	public void close();
	

}
