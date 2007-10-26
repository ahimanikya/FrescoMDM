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
 * supports SQL server specifics query syntax
 * @author sdua
 *
 */
public class SQLServerSQLObject {

	private static Pattern blankPattern = Pattern.compile("[\\s]*");	
	
	/**
	 * joins using non-ansi SQL
	 * @param pTable
	 * @param pk
	 * @param sTable
	 * @param fkey
	 * @param outerJoin
	 * @return join String
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
		   if (outerJoin == false) {
		      joinFields.append(" = ");
		   } else {
			  joinFields.append(" *= ");
		   }			
		   joinFields.append(sTable);
		   joinFields.append(".");
		   joinFields.append(fkey[i]);
		   joinFields.append(" ");
	   }
	   return joinFields.toString();
	
	}
	
	/**
	 * creates SQL statement. 
	 * @param selectbuf select statement
	 * @param fromTable  for ansi sql, fromTable is null
	 * @param joinbuf join clause. for ansi includes table names
	 * @param conditionbuf where clause
	 * @param maxRows if >0, use it in select query
	 * @param hint , if non-empty use it as OPTION hint
	 * @return
	 */
	static StringBuffer createSQL(StringBuffer selectbuf,
			StringBuffer fromTable, StringBuffer joinbuf, StringBuffer conditionbuf, 
			int maxRows, String hint) {
		StringBuffer sql = new StringBuffer();
		if (conditionbuf == null) {
			conditionbuf = new StringBuffer();
		}

		hint = convertHint(hint);

		sql.append("SELECT ");
		if (maxRows > -1) {
			sql.append(getMaxRowsCondition(maxRows));
		}		
		sql.append(" ");
		sql.append(selectbuf);
		sql.append(" FROM ");
		if (fromTable != null) { // non-ansi sql
			sql.append(fromTable);
		} else {
			sql.append(joinbuf);
		}
		
		if (fromTable != null) {  //non-ansi sql		
		  if ((joinbuf.length() > 0) || (conditionbuf.length() > 0)) {
			sql.append(" WHERE ");			
			sql.append(joinbuf);
			
			if ((joinbuf.length() > 0) && (conditionbuf.length() > 0)) {
				sql.append(" AND ");
			}
			sql.append(conditionbuf);
		  }
		} else {
			if (conditionbuf.length() > 0) {
				sql.append(" WHERE ");			
				sql.append(conditionbuf);
			}
		}
		sql.append(hint);
		return sql;
	}

	private static String convertHint(String hint) {

		if (hint == null || hint.equals("")) {

			return "";
		}

		/*
		 * return same if it is blank string
		 * 
		 */
		Matcher m = blankPattern.matcher(hint);
		if (m.matches()) {
			return hint;
		}

		hint = " Option (" + hint + " )";
		return hint;
	}
	
	private static String getMaxRowsCondition(int maxRows) {
		// SQL Server use "top" to limit the number of row to return
		return " TOP " + maxRows + " ";
	}	
	/**
	 * is the String blank
	 * @param s
	 * @return
	 */
	
	static boolean isBlank(String s) {
		if (s == null) {
			return true;
		}
		Matcher m = blankPattern.matcher(s);
		if (m.matches()) {
			return true;
		}
		return false;
	}
	
}
