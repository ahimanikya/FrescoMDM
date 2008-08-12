/*
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
package com.sun.mdm.index.query;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.mdm.index.objects.metadata.MetaDataService;

/**
 * supports MySQL specific queries
 *
 */
public class MySQLSQLObject {

	private static Pattern blankPattern = Pattern.compile("[\\s]*");


	/**
	 * forms Join clause. Use MySQL specific syntax
	 * @param pTable
	 * @param pk
	 * @param sTable
	 * @param fkey
	 * @param outerJoin if true, use outer join
	 * @return join clause
	 */
	static String createJoin(String pTable, String[] pk, String sTable, String[] fkey, boolean outerJoin) {
	    StringBuffer joinFields = new StringBuffer();
	    
	    
	    for (int i = 0; i < pk.length && i < fkey.length; i++) {
			if (i > 0) {
				joinFields.append(" AND ");
			}
			joinFields.append(" ");
			joinFields.append(pTable);
			joinFields.append(".");
			joinFields.append(pk[i]);
			joinFields.append(" = ");
			joinFields.append(sTable);
			joinFields.append(".");
			joinFields.append(fkey[i]);
			if (outerJoin == false) {
				joinFields.append(" ");
			} else {
				joinFields.append(" (+) ");
			}
		}
	    return joinFields.toString();
	}
	    

	
	
	/* create SQL statement
	 * @param selectbuf select string 
	 * @param fromTable tables for non-ansi form, if ansi, then fromTable is null
	 * @joinbuf  join criteria. If ansi then join also includes table names
	 *  
	 * 
	 */
	
	static StringBuffer createSQL(StringBuffer selectbuf,
			StringBuffer fromTable, StringBuffer joinbuf, StringBuffer conditionbuf, 
			int maxRows, String hint) {
		StringBuffer sql = new StringBuffer();
		if (conditionbuf == null) {
			conditionbuf = new StringBuffer();
		}

		boolean ansi = false;
		boolean and = false; // and == true indicates use AND otherwise Where in conditions
		if (fromTable == null) {
			ansi = true;
		}
		hint = convertHint(hint);

		sql.append("SELECT ");
		sql.append(hint);
		sql.append(" ");
		
		String maxstr = null;
		if (maxRows > -1) {
			maxstr = getMaxRowsCondition(maxRows);
		}
				
		sql.append(selectbuf);
		sql.append(" FROM ");
		if (ansi == false) { // non-ansi sql
			sql.append(fromTable);
		} else {
			sql.append(joinbuf);
		}
		
		if (ansi == false) {  //non-ansi sql		
		  if ((joinbuf.length() > 0) || (conditionbuf.length() > 0)) {
			sql.append(" WHERE ");			
			sql.append(joinbuf);
			
			if ((joinbuf.length() > 0) && (conditionbuf.length() > 0)) {
				sql.append(" AND ");
			}
			and = true;
			sql.append(conditionbuf);
		  }
		} else { // IN ansi join is not part of Where
			if (conditionbuf.length() > 0) {
				sql.append(" WHERE ");			
				sql.append(conditionbuf);
				and = true;
			}
		}
		if (maxstr != null) {
		   if (and == true) {
		      sql.append(" AND ");
			} else {
				sql.append(" Where ");
			}
		   sql.append(maxstr);
		}
		
		return sql;
	}


	private static String getMaxRowsCondition(int maxRows) {
		String rows = String.valueOf(maxRows);
		return "  LIMIT  " + rows;
	}

	/*
	 * add hint delimiter to hint
	 */
	private static String convertHint(String hint) {

		if (hint == null || hint.equals("")) {

			return "";
		}
		if (hint.indexOf("/*+") > -1) {
			return hint;
		}

		/*
		 * return same if it is blank string
		 * 
		 */
		Matcher m = blankPattern.matcher(hint);
		if (m.matches()) {
			return hint;
		}

		hint = "/*+ " + hint + " */";
		return hint;
	}
}
