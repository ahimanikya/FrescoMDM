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
public class TextField {

	ReportElement reportElement;
	TextFieldExpression textFieldExpression;

	public TextField(int x, ArrayList<String> fields, String type) {

		reportElement = new ReportElement(x, fields);
		textFieldExpression = new TextFieldExpression(fields, type);

	}

	@Override
	public String toString() {
		return "\n\t\t<textField>" + "\n" + reportElement.toString() + "\n"
				+ textFieldExpression + "\n" + "\t\t</textField>";
	}

	
	public static void main(String[] args) {

		LoaderConfig.getInstance().getMatchFields();
		TextField exp = new TextField(0,LoaderConfig
				.getInstance().getMatchFields(), "name");
		
		System.out.println(exp.toString());
		
		LoaderConfig.getInstance().getMatchFields();
		exp = new TextField(150,LoaderConfig
				.getInstance().getMatchFields(), "value1");
		
		System.out.println(exp.toString());
		
	}
}
