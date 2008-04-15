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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.mdm.index.loader.clustersynchronizer.dao.javadb.JavadbDAOFactory;
import com.sun.mdm.index.loader.clustersynchronizer.dao.mssql.MssqlDAOFactory;
import com.sun.mdm.index.loader.clustersynchronizer.dao.oracle.OracleDAOFactory;
import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas
 * 
 */
public abstract class DAOFactory {

	public static final int JAVADB = 1;

	public static final int ORACLE = 2;

	public static final int MSSQL = 3;

	protected static Connection connection;

	protected static LoaderConfig loaderConfig = LoaderConfig.getInstance();

	public abstract BucketDAO getBucketDAO();

	public abstract LoaderDAO getLoaderDAO();
	
	private static Logger logger = Logger.getLogger(DAOFactory.class.getName());

	public static DAOFactory getDAOFactory(int dbType) {

		switch (dbType) {
		case JAVADB:
			return new JavadbDAOFactory();
		case ORACLE:
			return new OracleDAOFactory();
		case MSSQL:
			return new MssqlDAOFactory();

		default:
			return null;
		}
	}

	public static DAOFactory getDAOFactory(String dbType) {
		if (dbType.equalsIgnoreCase("Oracle"))
			return getDAOFactory(DAOFactory.ORACLE);
		else if (dbType.equalsIgnoreCase("JavaDB"))
			return getDAOFactory(DAOFactory.JAVADB);
		else if (dbType.equalsIgnoreCase("Mssql"))
			return getDAOFactory(DAOFactory.MSSQL);

		return null;
	}

	//private static Lock lock = new ReentrantLock();

	public static Connection getConnection() throws SQLException {

//		lock.lock();
//		if (connection != null) {
//			return connection;
//		}
//		lock.unlock();

		String driver = loaderConfig
				.getSystemProperty("cluster.database.jdbc.driver");

		try {
			Class.forName(driver).newInstance();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "check classpath has jdbc driver", e);
		}
		String username = loaderConfig
				.getSystemProperty("cluster.database.user");
		String password = loaderConfig
				.getSystemProperty("cluster.database.password");
		String url = loaderConfig.getSystemProperty("cluster.database.url");
		return DriverManager.getConnection(url, username, password);
	}

}
