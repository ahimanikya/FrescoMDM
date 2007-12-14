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

import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas
 * 
 */
public class Band {

	private static final int WEIGHT_X = 480;
	private static final int VALUE_2_X = 320;
	private static final int VALUE_1_X = 160;
	private static final int NAME_X = 0;
	int height;
	boolean isSplitAllowed = false;

	ArrayList<TextField> textFields = new ArrayList<TextField>();

	public Band(ArrayList<String> fields) {
		height = fields.size() * 20 + 40;

		createTextFieldForName(fields);
		createTextFieldForValue1(fields);
		createTextFieldForValue2(fields);
		createTextFieldForWeight(fields);
	}

	private void createTextFieldForWeight(ArrayList<String> fields) {
		textFields.add(new TextField(WEIGHT_X, fields, "weight"));

	}

	private void createTextFieldForValue2(ArrayList<String> fields) {
		textFields.add(new TextField(VALUE_2_X, fields, "value2"));

	}

	private void createTextFieldForValue1(ArrayList<String> fields) {
		textFields.add(new TextField(VALUE_1_X, fields, "value1"));

	}

	private void createTextFieldForName(ArrayList<String> fields) {
		textFields.add(new TextField(NAME_X, fields, "name"));

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("\t\t<band height= \"" + height + "\"" + " isSplitAllowed=\""
				+ isSplitAllowed + "\"" + "> \n");

		for (TextField tf : textFields) {
			sb.append(tf);
		}

		sb.append("\n\t\t</band>\n");

		return sb.toString();
	}

	public static void main(String[] args) {

		LoaderConfig.getInstance().getMatchFields();
		Band exp = new Band(LoaderConfig.getInstance().getMatchFields());

		System.out.println(exp.toString());
	}
}
