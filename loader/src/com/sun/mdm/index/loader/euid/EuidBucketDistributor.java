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

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.DataObjectFileWriter;
import com.sun.mdm.index.dataobject.DataObjectWriter;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterSynchronizer;
import com.sun.mdm.index.loader.config.LoaderConfig;

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

		int i = Integer.parseInt(s);

		return i % numBucket;
	}

	/**
	 * close all the bucket writer and update the euid-bucket file name in the
	 * loader database
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		for (int i : buckets.keySet()) {
			buckets.get(i).close();
			clusterSynchronizer.insertEUIDBucket(EUID_BUCKET_PREFIX + i);
		}
	}

}
