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

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Sujit Biswas
 *
 */

/**
 * @author Sujit Biswas
 * 
 */
public class TruncateSQLWriter implements Writer {

	protected ArrayList<Table> tables = new ArrayList<Table>();

	protected String baseDir;

	public TruncateSQLWriter(ArrayList<Table> tables, String baseDir) {
		this.baseDir = baseDir;
		this.tables = tables;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.mdm.index.loader.sqlloader.Writer#write()
	 */
	public void write() {

		try {
			FileWriter w = new FileWriter(baseDir + "/" + getFileName());// windows

			w.write(getScript());

			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @return
	 */
	protected String getFileName() {
		return "truncate.sql";
	}

	/**
	 * 
	 */
	protected String getScript() {
		StringBuilder sb = new StringBuilder();

		writeSQLForTables(sb);

		return sb.toString();
	}

	/**
	 * @param sb
	 */
	protected void writeSQLForTables(StringBuilder sb) {
		for(Table t : tables){
			
			sb.append("truncate table " + t.getName() + " ; \n");
			
		}
		
		}
	}

	

	

	

	
