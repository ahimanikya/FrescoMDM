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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.DataObjectFileWriter;
import com.sun.mdm.index.dataobject.DataObjectWriter;
import com.sun.mdm.index.dataobject.DataObjectFileReader;
import com.sun.mdm.index.dataobject.InvalidRecordFormat;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterSynchronizer;
import com.sun.mdm.index.loader.config.LoaderConfig;
import java.io.File;

/**
 * splits a bucket file into many buckets. This is used so that a bucket that is too big to be completely
 * loaded into memory can be split and then can be completely loaded in memory.
 * 
 * @author Swaranjit Dua
 * 
 */
public class BucketSplitter {

	private String BUCKET_PREFIX;

	private String bucketDir;

	private int numBucket;
	private int MAX_BUCKET_SPLIT = 13;
	private int MIN_BUCKET_SPLIT = 6;
	private long maxFileSize = 60000000; // 60M
	private int initFileNum =  0;
	
	private HashSet<Integer> removeIds = new HashSet<Integer>();
	ClusterSynchronizer clusterSynchronizer = ClusterSynchronizer.getInstance();

	private HashMap<Integer, DataObjectWriter> buckets = new HashMap<Integer, DataObjectWriter>();

	/**
	 * 
	 * @param bucketPrefix such as EUID_ or block_
	 * @param bDir  bucketDir such as Block or euid
	 * @param maxBucketNum max number of bucket that has been already allocated in the buckets
	 * @param buckets contains current buckets some of which may require to be split
	 */
	public BucketSplitter(String bucketPrefix, String bDir, int maxBucketNum, HashMap<Integer, 
			DataObjectWriter> buckets) {
		bucketDir = bDir;
		BUCKET_PREFIX = bucketPrefix;
		numBucket = maxBucketNum;
		this.buckets = buckets;
		LoaderConfig config = LoaderConfig.getInstance();
		
		
		String sbucketCacheSize = config.getSystemProperty("BucketCacheSize");
		long bucketCacheSize = 0;
		if (sbucketCacheSize != null) {
			bucketCacheSize = Long.parseLong(sbucketCacheSize);
		}
		if (bucketCacheSize > 0) {
			  maxFileSize = bucketCacheSize;
		}
	}
	

	/**
	 * splits the buckets
	 * @return Map that has (key=id of all buckets including new, value=not to be used)
	 * @throws Exception
	 */

	public HashMap<Integer, DataObjectWriter> splits() throws LoaderException {
		boolean foundsplit = true;
		initFileNum+= numBucket;
		numBucket = 11;
		int countNoSplit = 0;
		
		int maxCountNoSplit = MAX_BUCKET_SPLIT - MIN_BUCKET_SPLIT;
		while (foundsplit) {
			foundsplit = false;
			HashMap<Integer, DataObjectWriter> curBuckets = new HashMap<Integer, DataObjectWriter>();
			curBuckets.putAll(buckets);
			/*
			 * split the buckets, if any bucket is split in previous iteration.
			 */
			boolean splitMany = false;
			for (int i : curBuckets.keySet()) {
				int prevsize = buckets.size();
				boolean sf = split(bucketDir, BUCKET_PREFIX + i, i);
				if (sf == true) {
					foundsplit = true;
				
				    if (buckets.size() > prevsize+1) {
					/*
					 * So all the records in bucket were moved to more than one bucket
					 */
					   splitMany = true;
				     }
				}
			}
			if (foundsplit && !splitMany ) {
				countNoSplit++;
				MAX_BUCKET_SPLIT++; // vary hash %numBucket operator to have more variations in hashing
				                    // increasing chances that two different records are split into different buckets
				
			}
			if (countNoSplit == maxCountNoSplit) {
				throw new LoaderException( "Bucket can't be split. Please increase Bucket Cache");
			}
 		}
		
		for (int i : removeIds) {
			buckets.remove(i);
		}
		
		return buckets;
		
	}

	
	private void writeDataObject(DataObject d, int bucketid) throws IOException {
		DataObjectWriter writer = getDataObjectWriter(bucketid);
		writer.writeDataObject(d);

	}

	/*
	 * 
	 * @param bucketid
	 * @return the DataObject writer for a given bucket id, will create a new
	 *         one if the writer does not exist
	 * @throws IOException
	 */
	private DataObjectWriter getDataObjectWriter(int bucketid)
			throws IOException {
		DataObjectWriter w = buckets.get(bucketid);

		if (w == null) {
			String s = bucketDir + File.separator + BUCKET_PREFIX + bucketid;
			w = new DataObjectFileWriter(s, true);
			buckets.put(bucketid, w);
		}
		return w;
	}

	/**
	 * assuming the no of bucket is less than 9999-9999 consider only the last
	 * eight digit of the euid to allocate a bucketid
	 * 
	 * @param euid
	 * @return
	 */
	private int getBucketID(String id) {
		

		int i = Math.abs(id.hashCode());

		int bid =  i % numBucket;
				
		return bid + initFileNum;
	}

	/**
	 * close all the bucket writer and update the euid-bucket file name in the
	 * loader database
	 * 
	 * @throws IOException
	 */
	public void close() throws LoaderException {
		try {
		for (int i : buckets.keySet()) {
			buckets.get(i).close();
		
		}
		splits();
		} catch (IOException io) {
			throw new LoaderException (io);
		}
	}



	private boolean split(String dir, String file, int bindex) throws LoaderException {
		try {
		boolean splitFlag = false;
		String firsteuid = null;
		File f = new File(dir, file);

		if (f.length() > maxFileSize ) {
			splitFlag = true;

			DataObjectFileReader reader = new DataObjectFileReader(f.getAbsolutePath(), true);
			boolean foundTwoEuid = false;
			while (true) {
				DataObject d = reader.readDataObject();
				if (d == null) {
					break;
				}
				String euid = d.getFieldValue(0);
				if (firsteuid == null) {
					firsteuid = euid;
				}

				if (!firsteuid.equals(euid)) {
					foundTwoEuid = true;
				}

				int bid = getBucketID(euid);
				writeDataObject(d, bid);
			}
			reader.close();
			f.delete();
			removeIds.add(bindex);
			for (int i : buckets.keySet()) {
				if (i >= initFileNum) {
					buckets.get(i).close();
				}			
				
			}

			if (foundTwoEuid == false) {
				throw new LoaderException( "Bucket can't be split. Please increase Bucket Cache or " 
						+ "change Application configuraiton");
			}
			initFileNum+= numBucket;
			if (numBucket > MIN_BUCKET_SPLIT) {		
				numBucket = numBucket - 1;
			} else {
				numBucket = MAX_BUCKET_SPLIT;
			}
		}				
		return splitFlag;
		} catch (IOException e) {
			throw new LoaderException (e);
		}  catch (InvalidRecordFormat ie) {
			throw new LoaderException (ie);
		}
	}
}
