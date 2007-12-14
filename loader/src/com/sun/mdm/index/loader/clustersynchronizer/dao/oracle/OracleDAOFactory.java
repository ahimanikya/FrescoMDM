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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import com.sun.mdm.index.loader.clustersynchronizer.dao.BucketDAO;
import com.sun.mdm.index.loader.clustersynchronizer.dao.DAOFactory;
import com.sun.mdm.index.loader.clustersynchronizer.dao.LoaderDAO;
import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas
 *
 */
public class OracleDAOFactory extends DAOFactory {

	private static Logger logger = Logger.getLogger(OracleDAOFactory.class
			.getName());

	public static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	
	
	
	public static Connection getConnection(Properties connProps)
			throws SQLException {
		try {
			Class.forName(DRIVER).newInstance();
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		String username = connProps.getProperty("username");
		String password = connProps.getProperty("password");
		String url = connProps.getProperty("url");
		return DriverManager.getConnection(url, username, password);

	}

	@Override
	public BucketDAO getBucketDAO() {
		// TODO Auto-generated method stub
		return new OracleBucketDAO();
	}

	@Override
	public LoaderDAO getLoaderDAO() {
		return new OracleLoaderDAO(LoaderConfig.getInstance());
	}

}
