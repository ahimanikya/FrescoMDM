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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import junit.framework.TestCase;

import com.sun.mdm.index.loader.clustersynchronizer.dao.DAOFactory;
import com.sun.mdm.index.loader.config.LoaderConfig;
import com.sun.mdm.index.loader.log.LoaderLogManager;

/**
 * @author Sujit Biswas
 * 
 */
public class ClusterSynchronizerTest extends TestCase {

	private static final int count_10 = 10;

	//private static final int five_second = 5000;

	private static final int five_second = 5000;

	private static Logger logger = Logger
			.getLogger(ClusterSynchronizerTest.class.getName());

	private ClusterSynchronizer clusterSynchronizer = ClusterSynchronizer
			.getInstance();

	public ClusterSynchronizerTest(String name) {
		super(name);

		new LoaderLogManager().init();
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetInstance() {
		// fail("Not yet implemented");
	}

	public void testGetBlockBucket() throws Exception {

		// test directly the file retrieve
		clusterSynchronizer.retrieveBucketFromMaster("/eview/loader/block",
				"test/data/", "block-bucket-1");

		truncateClusterBucketTable();

		for (int i = 0; i < count_10; i++) {
			clusterSynchronizer.insertBlockBucket("block-bucket-" + i);
		}

		GetBlockBucketCallable c1 = new GetBlockBucketCallable("Thread-1");

		GetBlockBucketCallable c2 = new GetBlockBucketCallable("Thread-2");

		GetBlockBucketCallable c3 = new GetBlockBucketCallable("Thread-3");

		ExecutorService executor = Executors.newFixedThreadPool(3);

		Future<ArrayList<String>> f1 = executor.submit(c1);
		Future<ArrayList<String>> f2 = executor.submit(c2);
		Future<ArrayList<String>> f3 = executor.submit(c3);

		ArrayList<String> b1 = f1.get();
		ArrayList<String> b2 = f2.get();
		ArrayList<String> b3 = f3.get();

		int buckets_allocated = b1.size() + b2.size() + b3.size();

		assertEquals(count_10, buckets_allocated);

		logger.info("buckets allocated to : " + c1.getName() + " : "
				+ b1.size() + ":" + b1.toString());
		logger.info("buckets allocated to : " + c2.getName() + " : "
				+ b2.size() + ":" + b2.toString());
		logger.info("buckets allocated to : " + c3.getName() + " : "
				+ b3.size() + ":" + b3.toString());

		assertNull(clusterSynchronizer.getBlockBucket());

	}

	public void testGetSBRBucket() {
		// fail("Not yet implemented");
	}

	public void testInsertBlockBucket() throws Exception {
		truncateClusterBucketTable();

		for (int i = 0; i < count_10; i++) {
			clusterSynchronizer.insertBlockBucket("block-bucket-" + i);
			String s = clusterSynchronizer.getBlockBucket();

			assertTrue(s != null);
		}

	}

	public void testInsertEUIDBucket() throws Exception {
		truncateClusterBucketTable();

		for (int i = 0; i < count_10; i++) {
			clusterSynchronizer.insertEUIDBucket("euid-bucket-" + i);
			String s = clusterSynchronizer.getEUIDBucket();

			assertTrue(s != null);
		}
	}

	public void testGetEUIDBucket() throws Exception {
		truncateClusterBucketTable();

		for (int i = 0; i < count_10; i++) {
			clusterSynchronizer.insertEUIDBucket("euid-bucket-" + i);
		}

		LoaderTestCallable c1 = new GetEUIDBucketCallable("EUIDThread-1");

		LoaderTestCallable c2 = new GetEUIDBucketCallable("EUIDThread-2");

		LoaderTestCallable c3 = new GetEUIDBucketCallable("EUIDThread-3");

		ExecutorService executor = Executors.newFixedThreadPool(3);

		Future<ArrayList<String>> f1 = executor.submit(c1);
		Future<ArrayList<String>> f2 = executor.submit(c2);
		Future<ArrayList<String>> f3 = executor.submit(c3);

		ArrayList<String> b1 = f1.get();
		ArrayList<String> b2 = f2.get();
		ArrayList<String> b3 = f3.get();

		int buckets_allocated = b1.size() + b2.size() + b3.size();

		assertEquals(count_10, buckets_allocated);

		logger.info("buckets allocated to : " + c1.getName() + " : "
				+ b1.size() + ":" + b1.toString());
		logger.info("buckets allocated to : " + c2.getName() + " : "
				+ b2.size() + ":" + b2.toString());
		logger.info("buckets allocated to : " + c3.getName() + " : "
				+ b3.size() + ":" + b3.toString());

		assertNull(clusterSynchronizer.getEUIDBucket());
	}

	public void testInsertSBRBucket() throws Exception {
		truncateClusterBucketTable();

		for (int i = 0; i < count_10; i++) {
			clusterSynchronizer.insertSBRBucket("sbr-bucket-" + i);
		}

		int count = 0;
		while (true) {
			String s = clusterSynchronizer.getSBRBucket();
			if (s == null)
				break;
			else
				count++;
		}
		assertTrue(count == count_10);

	}

	public void testWaitMatchingDone() {
		// fail("Not yet implemented");
	}

	public void testSetMatchDone() throws Exception {

		truncateClusterBucketTable();

		for (int i = 0; i < count_10; i++) {
			clusterSynchronizer.insertBlockBucket("block-bucket-" + i);
		}

		SetMatchDoneCallable c1 = new SetMatchDoneCallable("matcherThread-1");

		SetMatchDoneCallable c2 = new SetMatchDoneCallable("matcherThread-2");

		SetMatchDoneCallable c3 = new SetMatchDoneCallable("matcherThread-3");

		ExecutorService executor = Executors.newFixedThreadPool(3);

		Future<ArrayList<String>> f1 = executor.submit(c1);
		Future<ArrayList<String>> f2 = executor.submit(c2);
		Future<ArrayList<String>> f3 = executor.submit(c3);

		ArrayList<String> b1 = f1.get();
		ArrayList<String> b2 = f2.get();
		ArrayList<String> b3 = f3.get();

		int buckets_allocated = b1.size() + b2.size() + b3.size();

		assertEquals(count_10, buckets_allocated);

		logger.info("buckets allocated to : " + c1.getName() + " : "
				+ b1.size() + ":" + b1.toString());
		logger.info("buckets allocated to : " + c2.getName() + " : "
				+ b2.size() + ":" + b2.toString());
		logger.info("buckets allocated to : " + c3.getName() + " : "
				+ b3.size() + ":" + b3.toString());
		assertNull(clusterSynchronizer.getBlockBucket());

		// TODO check all buckets are in Done state

		long t_1 = System.currentTimeMillis();
		clusterSynchronizer.waitMatchingDone();
		long t_2 = System.currentTimeMillis();

		logger.info("time taken in sec to check match done " + (t_2 - t_1));
		assertTrue(t_2 - t_1 < five_second);

	}

	
	public void testSetMasterIndexDone() throws Exception {

		truncateClusterBucketTable();

		for (int i = 0; i < count_10; i++) {
			clusterSynchronizer.insertEUIDBucket("euid-bucket-" + i);
		}

		SetMasterIndexDoneCallable c1 = new SetMasterIndexDoneCallable("masterIndexThread-1");

		SetMasterIndexDoneCallable c2 = new SetMasterIndexDoneCallable("masterIndexThread-2");

		SetMasterIndexDoneCallable c3 = new SetMasterIndexDoneCallable("masterIndexThread-3");

		ExecutorService executor = Executors.newFixedThreadPool(3);

		Future<ArrayList<String>> f1 = executor.submit(c1);
		Future<ArrayList<String>> f2 = executor.submit(c2);
		Future<ArrayList<String>> f3 = executor.submit(c3);

		ArrayList<String> b1 = f1.get();
		ArrayList<String> b2 = f2.get();
		ArrayList<String> b3 = f3.get();

		int buckets_allocated = b1.size() + b2.size() + b3.size();

		assertEquals(count_10, buckets_allocated);

		logger.info("buckets allocated to : " + c1.getName() + " : "
				+ b1.size() + ":" + b1.toString());
		logger.info("buckets allocated to : " + c2.getName() + " : "
				+ b2.size() + ":" + b2.toString());
		logger.info("buckets allocated to : " + c3.getName() + " : "
				+ b3.size() + ":" + b3.toString());
		assertNull(clusterSynchronizer.getEUIDBucket());

		// TODO check all buckets are in Done state

		long t_1 = System.currentTimeMillis();
		
		//TODO fix the below
		
		//clusterSynchronizer.waitMasterIndexDone();
		long t_2 = System.currentTimeMillis();

		logger.info("time taken in sec to check match done " + (t_2 - t_1));
		assertTrue(t_2 - t_1 < five_second);

	}

	
	
	public void testSetSBRDone() throws Exception {
		truncateClusterBucketTable();

		for (int i = 0; i < count_10; i++) {
			clusterSynchronizer.insertSBRBucket("sbr-block-bucket-" + i);
		}

		LoaderTestCallable c1 = new SetSBRDoneCallable("sbr-Thread-1");

		LoaderTestCallable c2 = new SetSBRDoneCallable("sbr-Thread-2");

		LoaderTestCallable c3 = new SetSBRDoneCallable("sbr-Thread-3");

		ExecutorService executor = Executors.newFixedThreadPool(3);

		Future<ArrayList<String>> f1 = executor.submit(c1);
		Future<ArrayList<String>> f2 = executor.submit(c2);
		Future<ArrayList<String>> f3 = executor.submit(c3);

		ArrayList<String> b1 = f1.get();
		ArrayList<String> b2 = f2.get();
		ArrayList<String> b3 = f3.get();

		int buckets_allocated = b1.size() + b2.size() + b3.size();

		assertEquals(count_10, buckets_allocated);

		logger.info("buckets allocated to : " + c1.getName() + " : "
				+ b1.size() + ":" + b1.toString());
		logger.info("buckets allocated to : " + c2.getName() + " : "
				+ b2.size() + ":" + b2.toString());
		logger.info("buckets allocated to : " + c3.getName() + " : "
				+ b3.size() + ":" + b3.toString());

		assertNull(clusterSynchronizer.getSBRBucket());

		// TODO check all buckets are in Done state

		long t_1 = System.currentTimeMillis();
		clusterSynchronizer.waitSBRDone();
		long t_2 = System.currentTimeMillis();

		logger.info("time taken in sec to check SBR done " + (t_2 - t_1));
		assertTrue(t_2 - t_1 < five_second);
	}

	public void testWaitMasterIndexGenerationReady() throws Exception {

		initLoaderTable();

//		try {
//			clusterSynchronizer.waitMasterIndexGenerationReady();
//			assertTrue(false);
//		} catch (TimeOutException e) {
//			assertTrue(true);
//		}

		clusterSynchronizer.setClusterState(ClusterState.MASTER_INDEX_GENERATE);

		try {

			long t_1 = System.currentTimeMillis();
			clusterSynchronizer.waitMasterIndexGenerationReady();

			long t_2 = System.currentTimeMillis();

			logger.info("time taken in sec to check master index ready "
					+ (t_2 - t_1));
			assertTrue(t_2 - t_1 < five_second);

			assertTrue(true);
		} catch (TimeOutException e) {
			assertTrue(false);
		}

	}

	public void testWaitSBRMatchingReady() throws Exception {
		initLoaderTable();

//		try {
//			clusterSynchronizer.waitSBRMatchingReady();
//			assertTrue(false);
//		} catch (TimeOutException e) {
//			assertTrue(true);
//		}

		clusterSynchronizer.setClusterState(ClusterState.POT_DUPLICATE_MATCH);

		try {

			long t_1 = System.currentTimeMillis();
			clusterSynchronizer.waitSBRMatchingReady();

			long t_2 = System.currentTimeMillis();

			logger.info("time taken in sec to check sbr match ready "
					+ (t_2 - t_1));
			assertTrue(t_2 - t_1 < five_second);

			assertTrue(true);
		} catch (TimeOutException e) {
			assertTrue(false);
		}
	}

	public void testInitLoaderName() throws Exception {
		initLoaderTable();

	}

	
	
	public void testWaitMatchingReady() throws Exception {
		initLoaderTable();

//		try {
//			clusterSynchronizer.waitMatchingReady();
//			assertTrue(false);
//		} catch (TimeOutException e) {
//			assertTrue(true);
//		}

		clusterSynchronizer.setClusterState(ClusterState.MATCHING);

		try {

			long t_1 = System.currentTimeMillis();
			clusterSynchronizer.waitMatchingReady();

			long t_2 = System.currentTimeMillis();

			logger.info("time taken in sec to check  match ready "
					+ (t_2 - t_1));
			assertTrue(t_2 - t_1 < five_second);

			assertTrue(true);
		} catch (TimeOutException e) {
			assertTrue(false);
		}
	}

	public void testSetClusterState() throws Exception {

		initLoaderTable();

		clusterSynchronizer.setClusterState(ClusterState.MATCHING);

		clusterSynchronizer.setClusterState(ClusterState.EUID_ASSIGNED);

		clusterSynchronizer.setClusterState(ClusterState.MASTER_INDEX_GENERATE);

		clusterSynchronizer.setClusterState(ClusterState.MASTER_INDEX_LOAD);

		clusterSynchronizer.setClusterState(ClusterState.POT_DUPLICATE_BLOCK);

		clusterSynchronizer.setClusterState(ClusterState.POT_DUPLICATE_MATCH);
	}

	
	public void testEndToEnd()throws Exception{
		truncateClusterBucketTable();
		truncateLoaderTable();
		
		LoaderConfig c1 = new XLoaderConfig("test/data/loader/loader-1/loader-config.xml");
		
		LoaderConfig c2 = new XLoaderConfig("test/data/loader/loader-2/loader-config.xml");
		
		
		ClusterSynchronizer cs1 = new ClusterSynchronizer(c1);
		
		ClusterSynchronizer cs2 = new ClusterSynchronizer(c2);
		
		XTestLoader t1 = new XTestLoader(c1,cs1);
		
		XTestLoader t2 = new XTestLoader(c2,cs2);
		
		ExecutorService executor = Executors.newFixedThreadPool(3);

		Future<String> f1 = executor.submit(t1);
		Future<String> f2 = executor.submit(t2);
		

		String b1 = f1.get();
		String b2 = f2.get();
		
		
		
		
	}
	
	/**
	 * @throws SQLException
	 */
	private void initLoaderTable() throws SQLException {
		truncateLoaderTable();

		clusterSynchronizer.initLoaderName("loader1", true);
		clusterSynchronizer.initLoaderName("loader2", false);
		clusterSynchronizer.initLoaderName("loader3", false);
	}

	/**
	 * @throws SQLException
	 */
	private void truncateClusterBucketTable() throws SQLException {
		Connection c = DAOFactory.getConnection();
		c.createStatement().executeUpdate("delete from cluster_bucket");
		c.close();
	}

	/**
	 * @throws SQLException
	 */
	private void truncateLoaderTable() throws SQLException {
		Connection c = DAOFactory.getConnection();
		c.createStatement().executeUpdate("delete from loader");
		c.close();
	}
}
