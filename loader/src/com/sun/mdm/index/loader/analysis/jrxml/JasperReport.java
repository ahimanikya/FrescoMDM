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
package com.sun.mdm.index.loader.analysis.jrxml;

import java.util.ArrayList;

import com.sun.mdm.index.loader.analysis.WeightAnalyzer;
import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas
 * 
 */
public class JasperReport {

	String name = "Weight_Analysis_Report";

	ArrayList<Field> fields = new ArrayList<Field>();

	Detail detail;

	/**
	 * 
	 */
	public JasperReport(ArrayList<String> mFields) {
		for (int i = 0; i < mFields.size(); i++) {

			String field = mFields.get(i);
			fields.add(new Field(field.replaceAll("\\.", "_") + "_1",
					"java.lang.String"));
			fields.add(new Field(field.replaceAll("\\.", "_") + "_2",
					"java.lang.String"));

			if (field.equalsIgnoreCase(WeightAnalyzer.SYSTEM_CODE))
				fields.add(new Field(WeightAnalyzer.TOTAL_WT,
						"java.lang.Double"));
			else
				fields.add(new Field(field.replaceAll("\\.", "_") + "_wt",
						"java.lang.Double"));

		}

		detail = new Detail(mFields);
	}

	String header = "<?xml version=\"1.0\"?>"
			+ "\n"
			+ "	<!DOCTYPE jasperReport"
			+ "\n"
			+ "  PUBLIC \"-//JasperReports//DTD Report Design//EN\""
			+ "\n"
			+ "	  \"http://jasperreports.sourceforge.net/dtds/jasperreport.dtd\"> "
			+ "\n";

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		String jasperTag = "<jasperReport name=\"Weight_Analysis_Report\" > \n";
		String jasperEndTag = "</jasperReport> \n";

		sb.append(header);
		sb.append(jasperTag);

		sb.append(fieldsString());

		sb.append(detail.toString());

		sb.append(jasperEndTag);

		return sb.toString();
	}

	private Object fieldsString() {

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < fields.size(); i++) {
			sb.append(fields.get(i).toString() + "\n");
		}

		return sb.toString();
	}

	public static void main(String[] args) {

		JasperReport r = new JasperReport(LoaderConfig.getInstance()
				.getMatchFields());

		System.out.println(r.toString());

	}

}
