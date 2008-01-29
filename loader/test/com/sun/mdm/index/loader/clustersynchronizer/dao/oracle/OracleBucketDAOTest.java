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

package com.sun.mdm.index.loader.clustersynchronizer.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import junit.framework.TestCase;

import com.sun.mdm.index.loader.clustersynchronizer.ClusterSynchronizer;
import com.sun.mdm.index.loader.clustersynchronizer.SetMatchDoneCallable;
import com.sun.mdm.index.loader.clustersynchronizer.dao.BucketDAO;
import com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleBucketDAO;
import com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleDAOFactory;

/**
 * @author Sujit Biswas
 * 
 */
public class OracleBucketDAOTest extends TestCase {

	private static Logger logger = Logger.getLogger(OracleBucketDAOTest.class
			.getName());

	private ClusterSynchronizer clusterSynchronizer = ClusterSynchronizer
			.getInstance();

	private OracleBucketDAO oraBucketDao = new OracleBucketDAO();
	

	/**
	 * @param name
	 */
	public OracleBucketDAOTest(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleBucketDAO#getBlockBucket(java.lang.String)}.
	 */
	public void testGetBlockBucket() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleBucketDAO#getEUIDBucket(java.lang.String)}.
	 */
	public void testGetEUIDBucket() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleBucketDAO#getSBRBucket(java.lang.String)}.
	 */
	public void testGetSBRBucket() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleBucketDAO#insertBlockBucket(java.lang.String)}.
	 */
	public void testInsertBlockBucket() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleBucketDAO#insertEUIDBucket(java.lang.String)}.
	 */
	public void testInsertEUIDBucket() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleBucketDAO#insertSBRBucket(java.lang.String)}.
	 */
	public void testInsertSBRBucket() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleBucketDAO#setMatchDone(java.lang.String)}.
	 */
	public void testSetMatchDone() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleBucketDAO#setSBRDone(java.lang.String)}.
	 */
	public void testSetSBRDone() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleBucketDAO#waitMasterIndexGenerationReady()}.
	 */
	public void testWaitMasterIndexGenerationReady() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleBucketDAO#waitMatchingDone()}.
	 */
	public void testWaitMatchingDone() throws Exception {
		truncateClusterBucketTable();

		int count = 50;

		for (int i = 0; i < count; i++) {
			clusterSynchronizer.insertBlockBucket("block-bucket-" + i);
		}

		SetMatchDoneCallable c1 = new SetMatchDoneCallable("matcherThread-1");

		SetMatchDoneCallable c2 = new SetMatchDoneCallable("matcherThread-2");

		SetMatchDoneCallable c3 = new SetMatchDoneCallable("matcherThread-3");

		ExecutorService executor = Executors.newFixedThreadPool(3);


		 Future<ArrayList<String>> f1 =     executor.submit(c1);
		 Future<ArrayList<String>> f2 =     executor.submit(c2);
		 Future<ArrayList<String>> f3 =     executor.submit(c3);
		
		

		 ArrayList<String> b1 = f1.get();
		 ArrayList<String> b2 = f2.get();
		 ArrayList<String> b3 = f3.get();

		int buckets_allocated = b1.size() + b2.size()	+ b3.size();

		assertEquals(count, buckets_allocated);

		logger.info("buckets allocated to : " + c1.getName() + " : "
				+ b1.size() + ":" + b1.toString());
		logger.info("buckets allocated to : " + c2.getName() + " : "
				+ b2.size() + ":" + b2.toString());
		logger.info("buckets allocated to : " + c3.getName() + " : "
				+ b3.size() + ":" + b3.toString());

		long time1 = System.currentTimeMillis();
		clusterSynchronizer.waitMatchingDone();
		long time2 = System.currentTimeMillis();
		
		logger.info("time spent in waiting for match done in millis: " + (time2-time1));
		

		assertNull(clusterSynchronizer.getBlockBucket());
		assertTrue(  oraBucketDao.isBucketTypeStatus(BucketDAO.BLOCK, BucketDAO.DONE));

		;

	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleBucketDAO#isBucketTypeStatus(int, int)}.
	 */
	public void testIsBucketTypeStatus() throws Exception {
		truncateClusterBucketTable();

		int count = 50;

		for (int i = 0; i < count; i++) {
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

		assertEquals(count, buckets_allocated);

		logger.info("buckets allocated to : " + c1.getName() + " : "
				+ b1.size() + ":" + b1.toString());
		logger.info("buckets allocated to : " + c2.getName() + " : "
				+ b2.size() + ":" + b2.toString());
		logger.info("buckets allocated to : " + c3.getName() + " : "
				+ b3.size() + ":" + b3.toString());

		assertNull(clusterSynchronizer.getBlockBucket());

		logger.info("count of block buckets = " + oraBucketDao.countBucketType(BucketDAO.BLOCK));
		logger.info("count of block buckets done = " + oraBucketDao.countBucketTypeWithStatus(BucketDAO.BLOCK, BucketDAO.DONE));
		assertTrue(  oraBucketDao.isBucketTypeStatus(BucketDAO.BLOCK, BucketDAO.DONE));

		;

	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleBucketDAO#waitMatchingReady()}.
	 */
	public void testWaitMatchingReady() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleBucketDAO#waitSBRMatchingReady()}.
	 */
	public void testWaitSBRMatchingReady() {
		// fail("Not yet implemented");
	}

	/**
	 * @throws SQLException
	 */
	private void truncateClusterBucketTable() throws SQLException {
		Connection c = OracleDAOFactory.getConnection();
		c.createStatement().executeUpdate("delete from cluster_bucket");
		c.close();
	}
}
