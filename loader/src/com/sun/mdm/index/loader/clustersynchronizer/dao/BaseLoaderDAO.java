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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.mdm.index.loader.clustersynchronizer.ClusterState;
import com.sun.mdm.index.loader.clustersynchronizer.TimeOutException;
import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas
 * 
 */
public abstract class BaseLoaderDAO implements LoaderDAO {

	private static String insert_loader_sql = "insert into loader (name,machinename,rmiport,ismaster,state,workingDir) values (?,?,?,?,?,?)";

	private static String update_loader_sql = "update loader set state=? where ismaster=? AND machinename=? ";

	private static String update_loader_state_sql = "update loader set state=? where name=? AND machinename=? ";

	private static String insert_analysis_sql = "insert into analysis_state (state, ismaster,loadername) values (?,?,?)";

	private static String select_analysis_ready_sql = "select state from analysis_state where ismaster=1";

	private static String select_analysis_done_sql = "select done from analysis_state where ismaster<>1";

	private static String update_analysis_done_sql = "update analysis_state  set done=1 where loaderName=?";

	private static Logger logger = Logger.getLogger(BaseLoaderDAO.class
			.getName());

	private LoaderConfig config;
	protected Connection c;

	/**
	 * @param config
	 * 
	 */
	public BaseLoaderDAO(LoaderConfig config) {
		this.config = config;
		try {
			c = DAOFactory.getConnection();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "check connection params", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.loader.clustersynchronizer.dao.LoaderDAO#initLoaderName(java.lang.String,
	 *      boolean)
	 */
	public void initLoaderName(String loaderName, boolean isMaster) {
		// TODO Auto-generated method stub

		String hostname = getHostname();

		Integer rmiPort = Integer.decode(config.getSystemProperty("rmiPort"));

		String basedir = config.getSystemProperty("ftp.workingDir");

		PreparedStatement ps = null;
		try {
			//Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(insert_loader_sql);
			ps = ps1;

			ps.setString(1, loaderName);
			ps.setString(2, hostname);

			ps.setInt(3, rmiPort);
			ps.setInt(4, isMaster ? 1 : 0);
			ps.setInt(5, ClusterState.BLOCK_DISTRIBUTION);
			ps.setString(6, basedir);

			ps.executeUpdate();

			ps.close();
			//c.close();

		} catch (SQLException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * @return
	 */
	private String getHostname() {
		String hostname = null;

		try {
			hostname = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hostname;
	}

	private String getLoaderName() {
		return config.getSystemProperty("loaderName");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.loader.clustersynchronizer.dao.LoaderDAO#setClusterState(int)
	 */
	public void setClusterState(int state) {
		PreparedStatement ps = null;
		try {
			//Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(update_loader_sql);
			ps = ps1;

			ps.setInt(1, state);
			ps.setInt(2, 1);

			ps.setString(3, getHostname());

			int status = ps.executeUpdate();

			if (status == 0) {
				logger.info("cluster state could not changed");
			}

			ps.close();
			//c.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// logger.info(e.getMessage());
		}

	}

	/**
	 * should only be called by slave loader
	 * 
	 */
	public void setLoaderState(int state) {
		PreparedStatement ps = null;
		try {
			//Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(update_loader_state_sql);
			ps = ps1;

			ps.setInt(1, state);
			ps.setString(2, getLoaderName());

			ps.setString(3, getHostname());

			int status = ps.executeUpdate();

			if (status == 0) {
				logger.info("loader state could not changed");
			}

			ps.close();
			//c.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// logger.info(e.getMessage());
		}

	}
	
	public void waitBulkLoadingDoneBySlaves(){
		
		String pollInterval = config.getSystemProperty("pollInterval");
		long interval = Long.decode(pollInterval);

		while (!isBulkLoadingDoneBySlaves()) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				logger.info(e.getMessage());
			}

		}
		
	}

	private boolean isBulkLoadingDoneBySlaves() {
		String sql = "select state from loader where ismaster<>1";
		
		boolean result = true;
		int state = -1;

		PreparedStatement ps = null;
		try {
			//Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(sql);
			ps = ps1;

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				state = rs.getInt(1);
				if(state != ClusterState.BULK_LOADING_DONE){
					result = false;
					break;
				}
			}

			rs.close();
			ps.close();
			//c.close();
		} catch (SQLException e) {
			logger.info(e.getMessage());
			// e.printStackTrace();
		}
		return result;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.loader.clustersynchronizer.dao.LoaderDAO#waitMasterIndexGenerationReady()
	 */
	public void waitMasterIndexGenerationReady() throws TimeOutException {

		// TODO both the system property value can be made specific to state the
		// loader is in
		String pollInterval = config.getSystemProperty("pollInterval");
		String maxWaitTime = config.getSystemProperty("maxWaitTime");

		long interval = Long.decode(pollInterval);
		long waitTime = Long.decode(maxWaitTime);

		long t1 = System.currentTimeMillis();

		while (!isState(ClusterState.MASTER_INDEX_GENERATE)) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				logger.info(e.getMessage());
				// e.printStackTrace();
			}

			long t2 = System.currentTimeMillis();

			if (t2 - t1 > waitTime) {
				throw new TimeOutException();
			}

		}

	}

	private boolean isState(int s) {

		int state = getState();

		if (state >= s) {
			return true;
		}

		return false;
	}

	private static String get_state_sql = "select  state from loader where ismaster=?  ";

	/**
	 * @return
	 */
	private int getState() {
		int state = -1;

		PreparedStatement ps = null;
		try {
			//Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(get_state_sql);
			ps = ps1;

			ps.setInt(1, 1);
			// ps.setString(2, getHostname());

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				state = rs.getInt(1);
			}

			rs.close();
			ps.close();
			//c.close();
		} catch (SQLException e) {
			logger.info(e.getMessage());
			// e.printStackTrace();
		}
		return state;
	}

	private boolean isAnalysisReady() {
		int state = -1;

		PreparedStatement ps = null;
		try {
			//Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(select_analysis_ready_sql);
			ps = ps1;

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				state = rs.getInt(1);
			}

			rs.close();
			ps.close();
			//c.close();
		} catch (SQLException e) {
			logger.info(e.getMessage());
			// e.printStackTrace();
		}
		return state == ClusterState.ANALYSIS_READY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.loader.clustersynchronizer.dao.LoaderDAO#waitMatchingReady()
	 */
	public void waitMatchingReady() throws TimeOutException {
		// TODO both the system property value can be made specific to state the
		// loader is in
		String pollInterval = config.getSystemProperty("pollInterval");
		String maxWaitTime = config.getSystemProperty("maxWaitTime");

		long interval = Long.decode(pollInterval);
		long waitTime = Long.decode(maxWaitTime);

		long t1 = System.currentTimeMillis();

		while (!isState(ClusterState.MATCHING)) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				logger.info(e.getMessage());
				// e.printStackTrace();
			}

			long t2 = System.currentTimeMillis();

			if (t2 - t1 > waitTime) {
				throw new TimeOutException();
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.loader.clustersynchronizer.dao.LoaderDAO#waitSBRMatchingReady()
	 */
	public void waitSBRMatchingReady() throws TimeOutException {
		// TODO both the system property value can be made specific to state the
		// loader is in
		String pollInterval = config.getSystemProperty("pollInterval");
		String maxWaitTime = config.getSystemProperty("maxWaitTime");

		long interval = Long.decode(pollInterval);
		long waitTime = Long.decode(maxWaitTime);

		long t1 = System.currentTimeMillis();

		while (!isState(ClusterState.POT_DUPLICATE_MATCH)) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				logger.info(e.getMessage());
				e.printStackTrace();
			}

			long t2 = System.currentTimeMillis();

			if (t2 - t1 > waitTime) {
				throw new TimeOutException();
			}

		}

	}

	private static String get_workingDir_sql = "select  workingDir from loader where ismaster=?";

	/**
	 * @return
	 */
	public String getMasterWorkingDir() {
		String workingDir = null;

		PreparedStatement ps = null;
		try {
			//Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(get_workingDir_sql);
			ps = ps1;

			ps.setInt(1, 1);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				workingDir = rs.getString(1);
			}

			rs.close();
			ps.close();
			//c.close();
		} catch (SQLException e) {
			logger.info(e.getMessage());
			// e.printStackTrace();
		}
		return workingDir;
	}

	/**
	 * called by the master loader to set the state of the analysis table to be
	 * ready
	 */
	public void setAnalysisReady() {

		PreparedStatement ps = null;

		String loaderName = config.getSystemProperty("loaderName");

		int isMaster = isMasterLoader() ? 1 : 0;
		try {
			//Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(insert_analysis_sql);
			ps = ps1;

			ps.setInt(1, ClusterState.ANALYSIS_READY);
			ps.setInt(2, isMaster);// isMaster=true or false

			ps.setString(3, loaderName);

			int status = ps.executeUpdate();

			if (status == 0) {
				logger.info("weight analysis state could not changed");
			}

			ps.close();
			//c.close();

		} catch (SQLException e) {
			logger.info(e.getMessage());
		}

	}

	/**
	 * called by the slave loaders to check whether it will start to insert
	 * weight analysis record, will wait till the master loader signals that the
	 * slaves can start the analysis
	 */
	public void waitAnalysisReady() {

		String pollInterval = config.getSystemProperty("pollInterval");
		long interval = Long.decode(pollInterval);

		while (!isAnalysisReady()) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				logger.info(e.getMessage());
			}

		}

	}

	/**
	 * called by the master loader to check whether all the slaves are done with
	 * their analysis
	 */
	public void waitAnalysisDone() {
		String pollInterval = config.getSystemProperty("pollInterval");
		long interval = Long.decode(pollInterval);

		while (!isAnalysisDone()) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				logger.info(e.getMessage());
			}

		}
	}

	private boolean isAnalysisDone() {
		boolean done = true;

		PreparedStatement ps = null;
		try {
			//Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(select_analysis_done_sql);
			ps = ps1;

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int i = rs.getInt(1);

				if (i != ClusterState.ANALYSIS_DONE) {
					done = false;
					break;
				}

			}

			rs.close();
			ps.close();
			//c.close();
		} catch (SQLException e) {
			logger.info(e.getMessage());
		}
		return done;
	}

	/**
	 * this will be called by the slave loaders once inserting of all the
	 * records is done
	 */
	public void setAnalysisDone() {

		String loaderName = config.getSystemProperty("loaderName");

		PreparedStatement ps = null;
		try {
			//Connection c = DAOFactory.getConnection();
			PreparedStatement ps1 = c.prepareStatement(update_analysis_done_sql);
			ps = ps1;

			ps.setString(1, loaderName);

			int status = ps.executeUpdate();

			if (status == 0) {
				logger.info("set analysis done could not changed");
			}

			ps.close();
			//c.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	protected boolean isMasterLoader() {
		String isMasterLoader = config.getSystemProperty("isMasterLoader");

		return Boolean.parseBoolean(isMasterLoader);
	}

	public void setConfig(LoaderConfig config) {
		this.config = config;

	}

	public void close() {
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
