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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.mdm.index.loader.analysis.jrxml.JrxmlFile;
import com.sun.mdm.index.loader.clustersynchronizer.dao.DAOFactory;
import com.sun.mdm.index.loader.config.LoaderConfig;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 * @author Sujit Biswas
 * 
 */
public class Report {

	private Connection connection;

	private static String query = "select * from weight_analysis order by Total_wt DESC";
	
	private ArrayList<String> matchFields;

	

	public Report(Connection connection, ArrayList<String> matchFields) {
		this.connection = connection;
		
		this.matchFields=matchFields;
	}

	public void generate() {

		try {
			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(query);

			File reportFile = new File("conf/weight-analysis.jasper");

			File destinationPdf = new File("reports/weight-analysis.pdf");

			File destinationHtml = new File("reports/weight-analysis.html");

			
			
			
			createJrxmlFile();
			
			JasperCompileManager
					.compileReportToFile("conf/weight-analysis.jrxml",
							"conf/weight-analysis.jasper");

			if (false) {

				//TODO does not work 
				JasperRunManager.runReportToHtmlFile(reportFile.getPath(),
						destinationHtml.getPath(),
						new HashMap<Object, Object>(),
						new JRResultSetDataSource(resultSet));

			} else {

				JasperRunManager.runReportToPdfFile(reportFile.getPath(),
						destinationPdf.getPath(),
						new HashMap<Object, Object>(),
						new JRResultSetDataSource(resultSet));

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void createJrxmlFile() {
		JrxmlFile jrxml = new JrxmlFile(matchFields);
		jrxml.write();
		
	}

	public static void main(String[] args) {
		try {
			Report r = new Report( DAOFactory.getConnection()   ,LoaderConfig.getInstance().getMatchFields());
			r.generate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
