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

package com.sun.mdm.index.loader.clustersynchronizer.dao.mssql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.sun.mdm.index.loader.clustersynchronizer.Bucket;
import com.sun.mdm.index.loader.clustersynchronizer.dao.BaseBucketDAO;

/**
 * @author Sujit Biswas
 * 
 */
public class MssqlBucketDAO extends BaseBucketDAO {

	protected static String bucket_insert = "insert into cluster_bucket ( bucketName, loaderName, type,state,versionno) values (?,?,?,?,?)";

	protected static String bucket_select = "select top 5 id, bucketName, loaderName, type,state,versionno from cluster_bucket where state=? AND type=? ";

	

	private static Logger logger = Logger.getLogger(MssqlBucketDAO.class
			.getName());

	

	/**
	 * @param bucketName
	 * @return
	 */
	protected int insertBucket(String bucketName, int type) {

		PreparedStatement ps = null;
		try {
			//Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(bucket_insert);
			ps = ps1;

			
			//ps.setInt(1, sequence());
			ps.setString(1, bucketName);
			ps.setString(2, "");

			ps.setInt(3, type);
			ps.setInt(4, NEW);
			ps.setInt(5, 0);

			int status = ps.executeUpdate();

			ps.close();
			//c.close();

			return status;

		} catch (SQLException e) {
			logger.info(e.getMessage());
			// e.printStackTrace();
		}
		return 0;
	}

	protected ArrayList<Bucket> getNewBuckets(int bucketType) {

		ArrayList<Bucket> buckets = new ArrayList<Bucket>();

		PreparedStatement ps = null;
		try {
			//Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(bucket_select);
			ps = ps1;

			ps.setInt(1, NEW);
			ps.setInt(2, bucketType);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				buckets.add(newBucket(rs));
			}
			rs.close();
			ps.close();
			//c.close();
		} catch (SQLException e) {
			logger.info(e.getMessage());
			// e.printStackTrace();
		}

		return buckets;

	}

}
