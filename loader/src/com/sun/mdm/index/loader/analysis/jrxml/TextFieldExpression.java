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
public class TextFieldExpression {

	String expression;

	public TextFieldExpression(ArrayList<String> fields, String type) {
		if (type.equalsIgnoreCase("name"))
			createNameExpression(fields);

		if (type.equalsIgnoreCase("value1"))
			createValue1Expression(fields);

		if (type.equalsIgnoreCase("value2"))
			createValue2Expression(fields);

		if (type.equalsIgnoreCase("weight"))
			createWeightExpression(fields);

	}

	private void createWeightExpression(ArrayList<String> fields) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fields.size(); i++) {

			String value = fields.get(i).replaceAll("\\.", "_") + "_wt";

			sb.append("\t\t\t\t");

			if (fields.get(i).equalsIgnoreCase("Lid")){
				sb.append("\"  \"");
			}
				
			else if(fields.get(i).equalsIgnoreCase(WeightAnalyzer.SYSTEM_CODE)){
				value = "Total_wt";
				sb.append("" + "<![CDATA[$F{" + value + "}]]>" + "");
			}
			else{
				sb.append("" + "<![CDATA[$F{" + value + "}]]>" + "");
			}
			
			

			if (i < fields.size() - 1) {
				sb.append("+");
				sb.append("\"\\n\"");
				sb.append("+");
			}

			sb.append("\n");
		}

		expression = sb.toString();

	}

	private void createValue2Expression(ArrayList<String> fields) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fields.size(); i++) {

			String value = fields.get(i).replaceAll("\\.", "_") + "_2";

			sb.append("\t\t\t\t");
			sb.append("" + "<![CDATA[$F{" + value + "}]]>" + "");

			if (i < fields.size() - 1) {
				sb.append("+");
				sb.append("\"\\n\"");
				sb.append("+");
			}

			sb.append("\n");
		}

		expression = sb.toString();

	}

	private void createValue1Expression(ArrayList<String> fields) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fields.size(); i++) {

			String value = fields.get(i).replaceAll("\\.", "_") + "_1";

			sb.append("\t\t\t\t");
			sb.append("" + "<![CDATA[$F{" + value + "}]]>" + "");

			if (i < fields.size() - 1) {
				sb.append("+");
				sb.append("\"\\n\"");
				sb.append("+");
			}

			sb.append("\n");
		}

		expression = sb.toString();

	}

	private void createNameExpression(ArrayList<String> fields) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fields.size(); i++) {

			sb.append("\t\t\t\t");
			sb.append("\"" + fields.get(i) + "\"");

			if (i < fields.size() - 1) {
				sb.append("+");
				sb.append("\"\\n\"");
				sb.append("+");
			}

			sb.append("\n");
		}

		expression = sb.toString();

	}

	@Override
	public String toString() {
		return "\t\t\t<textFieldExpression class=\"java.lang.String\">" + "\n"
				+ expression + "\t\t\t </textFieldExpression>";

	}

	public static void main(String[] args) {

		LoaderConfig.getInstance().getMatchFields();
		TextFieldExpression exp = new TextFieldExpression(LoaderConfig
				.getInstance().getMatchFields(), "name");

		System.out.println(exp.toString());

		LoaderConfig.getInstance().getMatchFields();
		exp = new TextFieldExpression(LoaderConfig.getInstance()
				.getMatchFields(), "value1");

		System.out.println(exp.toString());

		LoaderConfig.getInstance().getMatchFields();
		exp = new TextFieldExpression(LoaderConfig.getInstance()
				.getMatchFields(), "value2");

		System.out.println(exp.toString());

		LoaderConfig.getInstance().getMatchFields();
		exp = new TextFieldExpression(LoaderConfig.getInstance()
				.getMatchFields(), "weight");

		System.out.println(exp.toString());

	}

}
