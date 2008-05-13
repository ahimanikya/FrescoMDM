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

package com.sun.mdm.index.loader.euid;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.DataObjectFileWriter;
import com.sun.mdm.index.dataobject.DataObjectWriter;
import com.sun.mdm.index.dataobject.DataObjectFileReader;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterSynchronizer;
import com.sun.mdm.index.loader.common.BucketSplitter;
import com.sun.mdm.index.loader.config.LoaderConfig;
import java.io.File;

/**
 * distribute the input file or good file records into various euid buckets based
 * on the euid assigned to given record.
 * 
 * <p>
 * The number of buckets is configurable
 * 
 * 
 * @author Sujit Biswas
 * 
 */
public class EuidBucketDistributor {

	private static final String EUID_BUCKET_PREFIX = "EUIDB_";

	private String bucketDir;

	private int numBucket;

	private HashMap<Integer, DataObjectWriter> buckets = new HashMap<Integer, DataObjectWriter>();
	private HashSet<Integer> removeIds = new HashSet<Integer>();

	ClusterSynchronizer clusterSynchronizer = ClusterSynchronizer.getInstance();

	/**
	 * @param numBucket
	 * @param euidIndexFile
	 * @param bucketDir
	 */
	public EuidBucketDistributor() {

		LoaderConfig config = LoaderConfig.getInstance();

		String workingDir = config.getSystemProperty("workingDir");

		bucketDir = workingDir + "/euid/";

		numBucket = Integer.parseInt(config.getSystemProperty("numEUIDBuckets"));
		

	}

	/**
	 * write the dataObject in the corresponding bucket, The bucket if is
	 * decided, based on the euid assigned to the DataObject
	 * 
	 * @param d
	 * @param euid
	 * @throws IOException
	 */
	public void distribute(DataObject d, String euid,String weight) throws IOException {

		int bucketid = getBucketID(euid);
		d.add(3, weight);
		d.add(0, euid);

		writeDataObject(d, bucketid);

	}

	private void writeDataObject(DataObject d, int bucketid) throws IOException {
		DataObjectWriter writer = getDataObjectWriter(bucketid);
		writer.writeDataObject(d);

	}

	/**
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
			String s = bucketDir + EUID_BUCKET_PREFIX + bucketid;
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
	private int getBucketID(String euid) {
		byte[] b = euid.getBytes();

		int lastEightDigit = b.length - 8;

		String s = new String(b, lastEightDigit, 8);
		int i = Math.abs(s.hashCode());

		//int i = Integer.parseInt(s);

		int bid =  i % numBucket;


		return bid + initFileNum;
	}

	/**
	 * close all the bucket writer and update the euid-bucket file name in the
	 * loader database
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException, Exception {
		for (int i : buckets.keySet()) {
			buckets.get(i).close();

			//	clusterSynchronizer.insertEUIDBucket(EUID_BUCKET_PREFIX + i);
		}
		splits();				
	}


	private int maxFileSize = 20000000;;

	private int initFileNum =  0;


	private void splits() throws Exception {
		BucketSplitter splitter = new BucketSplitter(EUID_BUCKET_PREFIX, bucketDir, numBucket, buckets);
		buckets = splitter.splits();
		for (int i : buckets.keySet()) {				
			clusterSynchronizer.insertBlockBucket(EUID_BUCKET_PREFIX + i);
		}
		/*
		boolean foundsplit = true;
		initFileNum+= numBucket;
		numBucket = 11;
		while (foundsplit) {
			foundsplit = false;
			HashMap<Integer, DataObjectWriter> curBuckets = new HashMap<Integer, DataObjectWriter>();
			curBuckets.putAll(buckets);
			
			for (int i : curBuckets.keySet()) {
				boolean sf = split(bucketDir, EUID_BUCKET_PREFIX + i, i);
				if (sf == true) {
					foundsplit = true;
				}
			}
		}
		for (int i : removeIds) {
			buckets.remove(i);
		}

		for (int i : buckets.keySet()) {				
			clusterSynchronizer.insertEUIDBucket(EUID_BUCKET_PREFIX + i);
		}
		*/
	}


	private boolean split(String dir, String file, int bindex) throws IOException, Exception {
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
				//	clusterSynchronizer.insertEUIDBucket(EUID_BUCKET_PREFIX + i);
			}

			if (foundTwoEuid == false) {
				throw new Exception("EUID Bucket can't be split.");
			}
			initFileNum+= numBucket;
			if (numBucket > 2) {		
				numBucket = numBucket - 2;
			} else {
				numBucket = 11;
			}

		}				
		return splitFlag;
	}


}
