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

package com.sun.mdm.index.loader.sqlloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.logging.Logger;

import junit.framework.TestCase;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.DataObjectFileWriter;
import com.sun.mdm.index.dataobject.DataObjectWriter;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterState;
import com.sun.mdm.index.loader.clustersynchronizer.ClusterSynchronizer;
import com.sun.mdm.index.loader.clustersynchronizer.dao.DAOFactory;
import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas
 * 
 */
public class ScriptGeneratorTest extends TestCase {

	private Connection conn;
	private LoaderConfig config;
	private HashMap<String, DataObjectWriter> dataObjectWriterMap = new HashMap<String, DataObjectWriter>();

	private String[] tables;

	private static Logger logger = Logger.getLogger(ScriptGeneratorTest.class
			.getName());

	/**
	 * @param name
	 */
	public ScriptGeneratorTest(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		conn = DAOFactory.getConnection();
		config = LoaderConfig.getInstance();

		ScriptGenerator sg = new ScriptGenerator();
		tables = new String[sg.getTables().size()];

		for (int i = 0; i < tables.length; i++) {
			tables[i] = sg.getTables().get(i).getName();
		}

		init();

	}

	private void init() {

		try {
			String masterIndexDir = config.getWorkingDir() + "/masterindex/";
			for (int i = 0; i < tables.length; i++) {

				DataObjectWriter dw = new DataObjectFileWriter(masterIndexDir
						+ tables[i] + ".data");

				dataObjectWriterMap.put(tables[i], dw);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();

		for (String key : dataObjectWriterMap.keySet()) {
			dataObjectWriterMap.get(key).close();
		}
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.sqlloader.ScriptGenerator#generate()}.
	 */
	public void testGenerate() throws Exception {

		for (int i = 0; i < tables.length; i++) {
			writeTable(tables[i]);

			dataObjectWriterMap.get(tables[i]).close();

		}

		String workingDir = config.getWorkingDir();

		String sqlLoaderDir = workingDir + "/sqlldr/";

		File f = new File(sqlLoaderDir);

		String os = System.getProperty("os.name");

		for (int i = 0; i < tables.length; i++) {

			String command = "cmd /C " + tables[i];

			Runtime.getRuntime().exec(command);

			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(command, null, f.getAbsoluteFile());

			long t1 = System.currentTimeMillis();
			process.waitFor();
			long t2 = System.currentTimeMillis();

			System.out.println("time taken in millis: " + (t2 - t1));

			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			System.out.printf("Output of running %s is:", command);
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}

		}

	}

	public void testBulkLoader() throws Exception {

		truncateLoaderTable();
		
		
		for (int i = 0; i < tables.length; i++) {
			writeTable(tables[i]);

			dataObjectWriterMap.get(tables[i]).close();

		}

		ClusterSynchronizer.getInstance().initLoaderName("master____loader", true);
		
		String name = config.getSystemProperty("loaderName");
		ClusterSynchronizer.getInstance().initLoaderName(name, false);
		
		ClusterSynchronizer.getInstance().setLoaderState(ClusterState.BULK_LOADING_DONE);
		
		BulkLoader bl = new BulkLoader();

		long t1 = System.currentTimeMillis();
		bl.load();
		long t2 = System.currentTimeMillis();

		logger.info("time taken in millis: " + (t2 - t1));

	}

	/**
	 * @param tableName
	 * @throws SQLException
	 * @throws IOException
	 */
	private void writeTable(String tableName) throws SQLException, IOException {
		String sql = "select * from " + tableName;

		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery(sql);

		ResultSetMetaData rsmeta = rs.getMetaData();

		int columnCount = rsmeta.getColumnCount();
		while (rs.next()) {

			StringBuilder sb = new StringBuilder();

			DataObject d = new DataObject();

			for (int i = 1; i <= columnCount; i++) {

				int type = rsmeta.getColumnType(i);

				Object value = readResultSet(rs, type, i, tableName);

				if (value == null)
					d.addFieldValue("");
				else
					d.addFieldValue(value.toString());

				sb.append(value);
				if (i != columnCount)
					sb.append("|");
				else {
					sb
							.append("|"
									+ config
											.getSystemProperty("sqlldr.record.delimiter"));
					d.addFieldValue(config
							.getSystemProperty("sqlldr.record.delimiter"));
				}

			}
			logger.info(tableName + ":" + sb.toString());
			logger.info(d.toString());

			writeDataObject(d, tableName);

		}
	}

	private void writeDataObject(DataObject d, String tableName)
			throws IOException {
		dataObjectWriterMap.get(tableName).writeDataObject(d);

	}

	private Object readResultSet(ResultSet rs, int colType, int index,
			String tableName) throws SQLException {
		switch (colType) {
		case Types.BIT:
		case Types.BOOLEAN: {
			boolean bdata = rs.getBoolean(index);
			if (rs.wasNull()) {
				return null;
			} else {
				return new Boolean(bdata);
			}
		}

		case Types.TIME: {
			Time tdata = rs.getTime(index);
			if (rs.wasNull())
				return null;
			else
				return tdata;
		}

		case Types.DATE: {
			Date ddata = rs.getDate(index);
			if (rs.wasNull())
				return null;
			else {

				String format = config.getObjectDefinition().getDateFormat()
						+ " hh:mm:ss";

				if (tableName.endsWith("SYSTEMOBJECT")
						|| tableName.endsWith("SYSTEMSBR")
						|| tableName.endsWith("POTENTIALDUPLICATES")) {
					format = "dd-MMM-yy hh:mm:ss";
				}

				SimpleDateFormat df = new SimpleDateFormat(format);
				Timestamp ts = new Timestamp(ddata.getTime());

				return df.format(ddata);

				// return df.format(new java.util.Date());

			}
		}

		case Types.TIMESTAMP:
		case -100: // -100 = Oracle timestamp
		{
			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");

			Timestamp tsdata = rs.getTimestamp(index);
			if (rs.wasNull())
				return null;
			else
				return df.format(tsdata);
		}

		case Types.BIGINT: {
			long ldata = rs.getLong(index);
			if (rs.wasNull())
				return null;
			else
				return new Long(ldata);
		}

		case Types.DOUBLE:
		case Types.FLOAT: {
			double fdata = rs.getDouble(index);
			if (rs.wasNull())
				return null;
			else
				return new Double(fdata);
		}

		case Types.REAL: {
			float rdata = rs.getFloat(index);
			if (rs.wasNull())
				return null;
			else
				return new Float(rdata);
		}

		case Types.DECIMAL:
		case Types.NUMERIC: {
			BigDecimal bddata = rs.getBigDecimal(index);
			if (rs.wasNull())
				return null;
			else
				return bddata;
		}

		case Types.INTEGER: {
			int idata = rs.getInt(index);
			if (rs.wasNull())
				return null;
			else
				return new Integer(idata);
		}

		case Types.SMALLINT: {
			short sidata = rs.getShort(index);
			if (rs.wasNull())
				return null;
			else
				return new Short(sidata);
		}

		case Types.TINYINT: {
			byte tidata = rs.getByte(index);
			if (rs.wasNull())
				return null;
			else
				return new Byte(tidata);
		}

			// JDBC/ODBC bridge JDK1.4 brings back -9 for nvarchar columns in
			// MS SQL Server tables.
			// -8 is ROWID in Oracle.
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
		case -9:
		case -8: {
			String sdata = rs.getString(index);
			if (rs.wasNull())
				return null;
			else
				return sdata;
		}

		case Types.BINARY:
		case Types.VARBINARY:
		case Types.LONGVARBINARY: {
			byte[] bdata = rs.getBytes(index);
			if (rs.wasNull())
				return null;
			else {
				Byte[] internal = new Byte[bdata.length];
				for (int i = 0; i < bdata.length; i++)
					internal[i] = new Byte(bdata[i]);
				return new String(bdata);
			}
		}

		case Types.BLOB: {
			// We always get the BLOB, even when we are not reading the
			// contents.
			// Since the BLOB is just a pointer to the BLOB data rather than the
			// data itself, this operation should not take much time (as opposed
			// to getting all of the data in the blob).
			Blob blob = rs.getBlob(index);

			if (rs.wasNull())
				return null;

			// return "\"" + new String(blob.getBytes(1, (int) blob.length())) +
			// "\"";
			return "";

		}

		case Types.CLOB: {
			// We always get the CLOB, even when we are not reading the
			// contents.
			// Since the CLOB is just a pointer to the CLOB data rather than the
			// data itself, this operation should not take much time (as opposed
			// to getting all of the data in the clob).
			Clob clob = rs.getClob(index);

			if (rs.wasNull())
				return null;

			// CLOB exists, so try to read the data from it
			if (clob != null) {
				return clob.getSubString(1, 255);
			}
		}

		case Types.OTHER:
		default:
			return rs.getObject(index);
		}
	}
	
	/**
	 * @throws SQLException
	 */
	private void truncateClusterBucketTable() throws SQLException {
		Connection c = DAOFactory.getConnection();
		c.createStatement().executeUpdate("delete from cluster_bucket");
	}

	/**
	 * @throws SQLException
	 */
	private void truncateLoaderTable() throws SQLException {
		Connection c = DAOFactory.getConnection();
		c.createStatement().executeUpdate("delete from loader");
	}

}
