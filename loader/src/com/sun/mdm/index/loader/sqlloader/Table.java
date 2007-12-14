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

import java.util.ArrayList;

/**
 * @author Sujit Biswas
 * 
 */
public class Table {

	private String name;
	
	//TODO encapsulate this in a column class
	private ArrayList<String> columns = new ArrayList<String>();
	private ArrayList<String> columnTypes = new ArrayList<String>();

	/**
	 * @param name
	 * @param columns
	 */
	public Table(String name, String[] columns) {
		this.name = name;
		for (String c : columns) {
			this.columns.add(c);
		}
	}
	
	public Table(String name, String[] columns,String[] columnTypes) {
		this.name = name;
		for (String c : columns) {
			this.columns.add(c);
		}
		for (String ct : columnTypes) {
			this.columnTypes.add(ct);
		}
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getColumns() {
		return columns;
	}

	public void setColumns(ArrayList<String> columns) {
		this.columns = columns;
	}

	public ArrayList<String> getColumnTypes() {
		return columnTypes;
	}

	public void setColumnTypes(ArrayList<String> columnTypes) {
		this.columnTypes = columnTypes;
	}

}
