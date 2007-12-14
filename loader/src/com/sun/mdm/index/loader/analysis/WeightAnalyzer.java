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

package com.sun.mdm.index.loader.analysis;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.sun.mdm.index.Resource;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterSynchronizer;
import com.sun.mdm.index.loader.clustersynchronizer.dao.DAOFactory;
import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas
 * 
 */
public class WeightAnalyzer {

	public static final String SYSTEM_CODE = "SystemCode";
	public static final String LID = "Lid";

	private static final int BATCH_SIZE = 1000;

	private static Logger logger = Logger.getLogger(WeightAnalyzer.class
			.getName(), Resource.BUNDLE_NAME);

	private Connection connection;

	private ArrayList<String> matchFields;

	private String insertSQL;

	private PreparedStatement insertPrepareStmt;

	private LoaderConfig config;

	ClusterSynchronizer clusterSynchronizer = ClusterSynchronizer.getInstance();

	/**
	 * 
	 */
	public WeightAnalyzer() {
		init();
	}

	/**
	 * the expectation is that the match fields has a specific order, the order
	 * may be based on the Object Definition or any order which is suitable to
	 * hierarchical object definition representation using string and delimiter
	 * 
	 * @param matchFields
	 */
	public WeightAnalyzer(ArrayList<String> matchFields) {
		this.matchFields = matchFields;
		init();
	}

	/**
	 * 
	 */
	private void init() {
		try {
			connection = DAOFactory.getConnection();
		} catch (SQLException e) {
			logger.info(e.getMessage());
		}

		config = LoaderConfig.getInstance();
		initMatchFields();

		createWeightAnalysisTable();

		initInsertStatement();
		
		
		File reportDir = new File("reports");

		if (!reportDir.exists()) {
			reportDir.mkdir();
		}
		
	}

	/**
	 * 
	 */
	private void initMatchFields() {
		if (this.matchFields == null) {
			matchFields = new ArrayList<String>(config.getMatchFields());
			matchFields.add(0, SYSTEM_CODE);
			matchFields.add(1, LID);
			// TODO order the match fields in the specific-order
			matchFields.toArray(new String[0]);
		}

	}

	private void initInsertStatement() {
		try {
			insertPrepareStmt = connection.prepareStatement(getInsertSQL());
		} catch (SQLException e) {
			logger.info(e.getMessage());
		}
	}

	private void createWeightAnalysisTable() {

		if (!isMasterLoader()) {
			clusterSynchronizer.waitAnalysisReady();
			return;
		}

		clusterSynchronizer.setAnalysisReady();

		// TODO this should be configurable, either drop table or truncate table
		// based on the configuration

		try {
			Statement stmt = connection.createStatement();
			stmt.execute("drop table weight_analysis");
			stmt.close();
		} catch (SQLException e) {
			logger.config(e.getMessage());
		}

		String create_sql = createSQL();

		try {
			Statement stmt = connection.createStatement();
			stmt.execute(create_sql);
			stmt.close();
		} catch (SQLException e) {
			logger.info(e.getMessage());
		}

	}

	private String createSQL() {
		StringBuilder sb = new StringBuilder("create table weight_analysis (  ");

		// TODO get the field size from ObjectDefinition
		for (int i = 0; i < matchFields.size(); i++) {

			String field = matchFields.get(i);
			sb.append(field.replaceAll("\\.", "_") + "_1 varchar(100),");
			sb.append(field.replaceAll("\\.", "_") + "_2 varchar(100),");

			if (i + 1 == matchFields.size())
				sb.append(field.replaceAll("\\.", "_") + "_wt float )");
			else {

				if (field.equalsIgnoreCase(SYSTEM_CODE))
					sb.append("Total_wt float,");
				else
					sb.append(field.replaceAll("\\.", "_") + "_wt float,");
			}

		}

		return sb.toString();
	}

	private int counter = 1;
	public static final String TOTAL_WT = "Total_wt";

	public synchronized void  insert(ArrayList<String> object1, ArrayList<String> object2,
			ArrayList<Double> weights) {

		boolean execute = false;
		try {

			if (counter % BATCH_SIZE == 0) {
				execute = true;
			}
			addBatch(object1, object2, weights, insertPrepareStmt);

			if (execute) {
				insertPrepareStmt.executeBatch();
				insertPrepareStmt.clearBatch();
			}

			counter++;

		} catch (SQLException e) {
			logger.info(e.getMessage());
		}

	}

	public void close() {
		try {
			if (counter % BATCH_SIZE != 1) {
				insertPrepareStmt.executeBatch();
				insertPrepareStmt.close();

			}

			if (!isMasterLoader())
				clusterSynchronizer.setAnalysisDone();
			else {
				clusterSynchronizer.waitAnalysisDone();

				Report r = new Report(connection, matchFields);
				r.generate();
			}

			connection.close();
		} catch (SQLException e) {
			logger.info(e.getMessage());
		}

	}

	/**
	 * @param object1
	 * @param object2
	 * @param weights
	 * @param ps
	 * @throws SQLException
	 */
	private void addBatch(ArrayList<String> object1, ArrayList<String> object2,
			ArrayList<Double> weights, PreparedStatement ps)
			throws SQLException {

		for (int i = 0; i < object1.size(); i++) {
			ps.setString(i * 3 + 1, object1.get(i));
			ps.setString(i * 3 + 2, object2.get(i));
			ps.setDouble(i * 3 + 3, weights.get(i));
		}

		ps.addBatch();
	}

	public static void main(String[] args) {
		WeightAnalyzer analyser = new WeightAnalyzer();

		analyser.toString();
	}

	private String getInsertSQL() {

		if (insertSQL == null) {

			// id, euid1, euid2, weight
			StringBuilder sb = new StringBuilder(
					"insert into weight_analysis values ( ");
			for (int i = 0; i < matchFields.size(); i++) {
				if (i + 1 == matchFields.size())
					// object1-field, object2-field,weight
					sb.append("?,?,? )");
				else {
					// object1-field, object2-field,weight
					sb.append("?,?,?,");
				}

			}
			insertSQL = sb.toString();
		}

		return insertSQL;
	}

	private boolean isMasterLoader() {
		String isMasterLoader = config.getSystemProperty("isMasterLoader");

		return Boolean.parseBoolean(isMasterLoader);
	}

}
