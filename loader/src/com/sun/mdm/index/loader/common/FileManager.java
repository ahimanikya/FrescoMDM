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
package com.sun.mdm.index.loader.common;


import java.io.File;
import java.io.IOException;

import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * Creates 
 *    Different kind of "File" objects like
 *     Block Bucket "File"
 *     EUID bucket File
 *     SBr block bucket File
 *     
 * @author Swaranjit Dua
 *
 */

public class FileManager {
	
	private static int blockBucketCounter;
	private static int euidBucketCounter;
	private static int sbrBlockBucketCounter;
	private static int matchFileCounter;
	private static String workingDir;
	private static String blockBucketDir;
	private static String euidBucketDir;
	private static String sbrblockBucketDir;
	private static String sbrInputDir;
	private static String sbrInputStageDir;
	private static File   dsbrInputStageDir;
	private static String matchFileDir;
	private static String sbrmatchFileDir;
	private static String matchFileStageDir;
	private static String sbrmatchFileStageDir;
	private static String masterImageDir;
	private static String loader;
	//private static int bufferSize = 16000;
	
	private static final String BLOCK_BUCKET_PREFIX = "BlockB_";
	private static final String EUID_BUCKET_PREFIX = "EUIDB_";
	private static final String SBRBLOCK_BUCKET_PREFIX = "SBRBlockB_";
	private static final String MATCH_FILE_PREFIX = "Match_";
	private static final String MATCH_STAGE_PREFIX = "MatchStage_";
	private static final String SBRMATCH_FILE_PREFIX = "SBRMatch_";
	private static final String SBRMATCH_STAGE_PREFIX = "SBRMatchStage_";
	
	private static LoaderConfig config_ = LoaderConfig.getInstance();
	
		
	public static void setWorkingDir (String workdir, String loaderName) {
		workingDir = workdir;
		blockBucketDir = workingDir + File.separator + "block";
		euidBucketDir = workingDir + File.separator + "euid";
		sbrblockBucketDir = workingDir + File.separator + "sbr-block";
		sbrInputDir = workingDir + File.separator + "sbr-input";
		sbrInputStageDir = sbrInputDir + File.separator + "stage";
		matchFileDir = workingDir + File.separator + "match";
		masterImageDir = workingDir + File.separator + "masterIndex";
		matchFileStageDir = workingDir + File.separator + "match" + File.separator + "stage";
		sbrmatchFileDir = workingDir + File.separator + "sbr-match";
		sbrmatchFileStageDir = workingDir + File.separator + "sbr-match" + File.separator + "stage";
		loader = loaderName;
	}
	
	public static void initDirs() {
		File dir = new File(workingDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		dir = new File(blockBucketDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		dir = new File(sbrblockBucketDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		dir = new File(euidBucketDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		dir = new File(sbrInputDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		dir = new File(sbrInputStageDir);
		dsbrInputStageDir = dir;
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		dir = new File(matchFileDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		dir = new File(matchFileStageDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		dir = new File(sbrmatchFileDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		dir = new File(sbrmatchFileStageDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		dir = new File(masterImageDir);
		if (!dir.exists()) {
			dir.mkdir();
		}		
	}
	
	
	/**
	 * creates n number of unique block bucket File
	 * @param n number of files to be created
	 * @return File[] 
	 * @throws IOException
	 */
	public static File[] createBlockBucketFiles(int n) 
	   throws IOException {
		File[] files = new File[n];
		for ( int i = 1; i <= n; i++) {
			String file = BLOCK_BUCKET_PREFIX + i + ".txt" ;
			files[i-1] = new File(blockBucketDir, file);
					
		}	 	
		blockBucketCounter += n;
		return files;
	}
	
	/**
	 * creates n number of unique EUID bucket File
	 * @param n number of files to be created
	 * @return File[] 
	 * @throws IOException
	 */
	
	public static File[] createEUIDBucketFiles(int n) 
	   throws IOException {
		File[] files = new File[n];
		for ( int i = 1; i <= n; i++) {
			String file = EUID_BUCKET_PREFIX + i;
			files[i-1] = new File(euidBucketDir, file);
			
		}	 	
		euidBucketCounter += n;
		return files;
	}
	
	/**
	 * creates n number of unique SBR block bucket File
	 * @param n number of files to be created
	 * @return File[] 
	 * @throws IOException
	 */
	
	public static File[] createSbrblockBucketFiles(int n) 
	   throws IOException {
		File[] files = new File[n];
		for ( int i = 1; i <= n; i++) {
			String file = SBRBLOCK_BUCKET_PREFIX + i;
			files[i-1] = new File(sbrblockBucketDir, file);						
		}
	 	
		sbrBlockBucketCounter += n;
		return files;
	}
	
	
	public static File createMatchFile(File bucket) {
		String name = bucket.getName();
	    int index = name.indexOf("_");
	    int endindex = name.indexOf('.');
	    String bucketNum;
	    if (endindex != -1) {
	    	bucketNum = name.substring(index+1, endindex);	
	    } else {
	    	bucketNum = name.substring(index+1);
	    }
	    
	    
	    File file = new File(matchFileDir, MATCH_FILE_PREFIX + bucketNum);
	    
	    return file;
	}
	
	public static File createSBRMatchFile(File bucket) {
		String name = bucket.getName();
	    int index = name.indexOf("_");
	    int endindex = name.indexOf('.');
	    String bucketNum;
	    if (endindex != -1) {
	    	bucketNum = name.substring(index+1, endindex);	
	    } else {
	    	bucketNum = name.substring(index+1);
	    }
	    
	    
	    File file = new File(sbrmatchFileDir, MATCH_FILE_PREFIX + bucketNum);
	    
	    return file;
	}
	
	public static File createMatchStageFile() {
		File file = new File(matchFileStageDir, MATCH_STAGE_PREFIX+loader);		
	    	    	  return file;  
	}
	
	public static File createSBRMatchStageFile() {
		File file = new File(sbrmatchFileStageDir, MATCH_STAGE_PREFIX+loader);		
	    	    	  return file;  
	}
	
	public static File getInputGoodFile() {
		String goodfile = config_.getSystemProperty("goodFile");
		File file = new File(workingDir, goodfile);		
	    	    	  return file;  
	}
	
	public static File getInputSBRFile() {
		
		File file = new File(sbrInputStageDir, "SBRInput" + loader);		
	    	    	  return file;  
	}
	
	public static File[] getAllMatchStageFiles() {
		File dir = new File(matchFileStageDir);
		
		String[] fileList = dir.list();
		
		File[] files = new File[fileList.length];
		
		for (int i = 0; i < fileList.length; i++) {
			files[i] = new File(dir, fileList[i]);			
		}
		
		return files;
		
	}
	
	public static File[] getAllSBRMatchStageFiles() {
		File dir = new File(sbrmatchFileStageDir);
		
		String[] fileList = dir.list();
		
		File[] files = new File[fileList.length];
		
		for (int i = 0; i < fileList.length; i++) {
			files[i] = new File(dir, fileList[i]);			
		}
		
		return files;
		
	}
	
	public static File getFinalMatchFile() {
		File file = new File(matchFileStageDir, "finalMatch");
		
		return file;		
	}
	
	public static File getFinalSBRMatchFile() {
		File file = new File(sbrmatchFileStageDir, "finalSBRMatch");
		
		return file;		
	}
	
	public static String getBlockBucketDir() {
		return blockBucketDir;
	}
	
	public static String getsbrBlockBucketDir() {
		return sbrblockBucketDir;
	}
	
	public static String getsbrMatchDir() {
		return sbrmatchFileDir;
	}
	
	public static String getsbrMatchStageDir() {
		return sbrmatchFileStageDir;
	}
	
	
	public static String getMatchFileDir() {
		return matchFileDir;
	}
	
	public static String getMatchFileStageDir() {
		return matchFileStageDir;
	}
	
	public static File getSBRStageDir() {
		return dsbrInputStageDir;
	}
	
	public static String getEUIDBucketDir() {
		return euidBucketDir;
	}
	
	public static String getMasterImageDir() {
		return masterImageDir;
	}
						
}
