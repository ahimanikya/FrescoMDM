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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import com.sun.mdm.index.loader.clustersynchronizer.Bucket;

/**
 * @author Sujit Biswas
 * 
 */
public abstract class BaseBucketDAO implements BucketDAO {

	private static Logger logger = Logger.getLogger(BaseBucketDAO.class
			.getName());

	public BaseBucketDAO() {
		super();
	}

	protected static String bucket_insert = "";

	protected static String bucket_select = "";

	protected static String bucket_update = "update cluster_bucket SET versionno=?, state=?, loaderName=? where id=? AND versionno=?";

	protected static String bucket_status_update = "update cluster_bucket SET  state=? where loaderName=? AND type=?";

	protected static String select_count_bucket_type_done_sql = "SELECT COUNT(*) FROM cluster_bucket WHERE type=? and state=?";

	protected static String select_count_bucket_type_sql = "SELECT COUNT(*) FROM cluster_bucket WHERE type=?";
	
	protected static AtomicInteger seq = new AtomicInteger(0);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.loader.clustersynchronizer.BucketDAO#getBlockBucket()
	 */
	public String getBlockBucket(String loaderName) {
		ArrayList<Bucket> buckets = getNewBuckets(BLOCK);

		/**
		 * all buckets are assigned
		 */
		if (buckets.isEmpty()) {
			return null;
		}

		/**
		 * allocate one of the new buckets
		 */
		for (Bucket b : buckets) {
			int i = allocateBucket(b, loaderName);

			/**
			 * return if allocated
			 */
			if (i == 1)
				return b.getName();
		}

		/**
		 * try again
		 */
		return getBlockBucket(loaderName);
	}

	private int allocateBucket(Bucket b, String loaderName) {

		PreparedStatement ps = null;
		try {
			Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(bucket_update);
			ps = ps1;

			ps.setInt(1, b.getVersionno() + 1);
			ps.setInt(2, ASSIGNED);

			ps.setString(3, loaderName);

			ps.setInt(4, b.getId());
			ps.setInt(5, b.getVersionno());

			int status = ps.executeUpdate();

			ps.close();
			c.close();

			return status;

		} catch (SQLException e) {

			try {
				ps.close();
			} catch (SQLException e1) {
				logger.info(e1.getMessage());
			}

			logger.info(e.getMessage());
		}finally{
			
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.loader.clustersynchronizer.BucketDAO#getEUIDBucket()
	 */
	public String getEUIDBucket(String loaderName) {
		ArrayList<Bucket> buckets = getNewBuckets(EUID);

		/**
		 * all buckets are assigned
		 */
		if (buckets.isEmpty()) {
			return null;
		}

		/**
		 * allocate one of the new buckets
		 */
		for (Bucket b : buckets) {
			int i = allocateBucket(b, loaderName);

			/**
			 * return if allocated
			 */
			if (i == 1)
				return b.getName();
		}

		/**
		 * try again
		 */
		return getEUIDBucket(loaderName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.loader.clustersynchronizer.BucketDAO#getSBRBucket()
	 */
	public String getSBRBucket(String loaderName) {
		ArrayList<Bucket> buckets = getNewBuckets(SBR_BLOCK);

		/**
		 * all buckets are assigned
		 */
		if (buckets.isEmpty()) {
			return null;
		}

		/**
		 * allocate one of the new buckets
		 */
		for (Bucket b : buckets) {
			int i = allocateBucket(b, loaderName);

			/**
			 * return if allocated
			 */
			if (i == 1)
				return b.getName();
		}

		/**
		 * try again
		 */
		return getSBRBucket(loaderName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.loader.clustersynchronizer.BucketDAO#insertBlockBucket(java.lang.String)
	 */
	public int insertBlockBucket(String bucketName) {
		return insertBucket(bucketName, BLOCK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.loader.clustersynchronizer.BucketDAO#insertEUIDBucket(java.lang.String)
	 */
	public int insertEUIDBucket(String bucketName) {
		return insertBucket(bucketName, EUID);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.loader.clustersynchronizer.BucketDAO#insertSBRBucket(java.lang.String)
	 */
	public int insertSBRBucket(String bucketName) {
		return insertBucket(bucketName, SBR_BLOCK);

	}

	protected abstract int insertBucket(String bucketName, int sbr_block);

	protected abstract ArrayList<Bucket> getNewBuckets(int bucketType);

	protected Bucket newBucket(ResultSet rs) throws SQLException {
		Bucket b = new Bucket();

		b.setId(rs.getInt(1));
		b.setName(rs.getString(2));
		b.setLoaderName(rs.getString(3));

		b.setType(rs.getInt(4));
		b.setState(rs.getInt(5));
		b.setVersionno(rs.getInt(6));

		return b;
	}

	public void setMatchDone(String loaderName) {
		// use the loaderName to set all the allocated block bucket state DONE
		updateBucketStatus(loaderName, DONE, BLOCK);
	}
	
	public void setMasterIndexDone(String loaderName) {
		// use the loaderName to set all the allocated EUID bucket state DONE
		updateBucketStatus(loaderName, DONE, EUID);
	}

	/**
	 * @param loaderName
	 */
	private void updateBucketStatus(String loaderName, int state, int type) {

		PreparedStatement ps = null;
		try {
			Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(bucket_status_update);
			ps = ps1;

			ps.setInt(1, state);

			ps.setString(2, loaderName);

			ps.setInt(3, type);

			ps.executeUpdate();

			ps.close();
			c.close();

		} catch (SQLException e) {
			logger.info(e.getMessage());
			//e.printStackTrace();
		}
	}

	public void setSBRDone(String loaderName) {
		// TODO use the loaderName to get all the allocated SBR bucket, and set
		// the state of SBR bucket to DONE

		/**
		 * indicates SBR file creation is done for that loader. 1.copy SBR file
		 * to master loader SBR file staging area. 2.set states for all bucket
		 * Files for that loader to DONE.
		 */

		updateBucketStatus(loaderName, DONE, SBR_BLOCK);

	}

	public void waitMatchingDone() {
		while (!isBucketTypeStatus(BLOCK, DONE)) {
			try {
				// TODO read polling interval from configuration file
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.info(e.getMessage());
				//e.printStackTrace();
			}
		}

	}
	
	
	public void waitMasterIndexDone() {
		while (!isBucketTypeStatus(EUID, DONE)) {
			try {
				// TODO read polling interval from configuration file
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.info(e.getMessage());
				//e.printStackTrace();
			}
		}

	}
	
	

	public void waitSBRDone() {
		while (!isBucketTypeStatus(SBR_BLOCK, DONE)) {
			try {
				// TODO read polling interval from configuration file
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.info(e.getMessage());
				//e.printStackTrace();
			}
		}

	}

	/**
	 * this method checks given a bucket type whether all the buckets of that
	 * type has the given status
	 * 
	 * 
	 * @param bucketType
	 * 
	 * @return
	 */
	public boolean isBucketTypeStatus(int bucketType, int status) {
		int bucket_count = countBucketType(bucketType);
		int bucket_done_count = countBucketTypeWithStatus(bucketType, status);

		return bucket_done_count == bucket_count;
	}

	/**
	 * @param bucketType
	 * @param status
	 * 
	 * @return
	 */
	public int countBucketTypeWithStatus(int bucketType, int status) {

		int bucket_done_count = 0;

		PreparedStatement ps = null;
		try {
			Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(select_count_bucket_type_done_sql);
			ps = ps1;

			ps.setInt(1, bucketType);
			ps.setInt(2, status);
			ResultSet rs = ps.executeQuery();
			rs.next();
			bucket_done_count = rs.getInt(1);
			rs.close();
			ps.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bucket_done_count;
	}

	/**
	 * @param bucketType
	 * @param bucket_count
	 * @return
	 */
	public int countBucketType(int bucketType) {
		int bucket_count = 0;

		PreparedStatement ps = null;
		try {
			Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(select_count_bucket_type_sql);
			ps = ps1;

			ps.setInt(1, bucketType);
			ResultSet rs = ps.executeQuery();
			rs.next();
			bucket_count = rs.getInt(1);
			rs.close();
			ps.close();
			c.close();

		} catch (SQLException e) {
			logger.info(e.getMessage());
			//e.printStackTrace();
		}
		return bucket_count;
	}

	/**
	 * @return
	 */
	protected int sequence() {
		return seq.getAndIncrement();
	}

}
