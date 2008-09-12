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
import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas
 * 
 */
public class MySQLControlFileWriter extends ControlFileWriter {

    private Table table;
    private String baseDir;
    private StringBuffer setClause = null;
    private StringBuffer updateStmt = null;
    private static String nl = System.getProperty("line.separator");
    private static String fs = System.getProperty("file.separator");
    private static String datetimeFormat1 = "%d-%b-%y %T";
    private static String datetimeFormat2 = "%m/%d/%Y %T";
    private LoaderConfig config = LoaderConfig.getInstance();

    /**
     * @param table
     * @param baseDir
     */
    public MySQLControlFileWriter(Table table, String baseDir) {
        this.table = table;
        this.baseDir = baseDir;
    }

    public void write() {

        try {
            FileWriter w = new FileWriter(baseDir + fs + table.getName() + ".sql");

            w.write(getControlScript());

            w.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private String getControlScript() {

        StringBuilder sb = new StringBuilder();
        sb.append("set foreign_key_checks = 0;" + nl);
        sb.append("load data local infile ");

        sb.append("'../masterindex/" + table.getName() + ".data' into table " + table.getName() + nl);

        sb.append("fields terminated by '|' lines terminated by '" + getRecordLimiter() + "' starting by ''" + nl);

        sb.append(getColumns());
        if (setClause != null) {
            sb.append(nl + setClause);
        }
        sb.append(';');
        if (updateStmt != null){
            sb.append(nl + updateStmt + "commit;");
        }
        sb.append(nl + "set foreign_key_checks = 1;");


        return sb.toString();
    }

    private String getColumns() {

        StringBuilder sb = new StringBuilder();

        sb.append("(");
        String dtfmt = null;
        if (table.getName().equalsIgnoreCase("SBYN_SYSTEMSBR") || table.getName().equalsIgnoreCase("SBYN_SYSTEMOBJECT") ||
                table.getName().equalsIgnoreCase("SBYN_TRANSACTION") || table.getName().equalsIgnoreCase("SBYN_POTENTIALDUPLICATES")) {
            dtfmt = datetimeFormat1;
        } else {
            dtfmt = datetimeFormat2;
        }
        for (int i = 0; i < table.getColumns().size(); i++) {

            if (isDeltaColumn(table, i)) {
                continue;
            }
            String colName = table.getColumns().get(i);

            if (!table.getColumnTypes().isEmpty() && isDate(table.getColumnTypes().get(i)) ||
                    table.getColumnTypes().isEmpty() && isDate(table.getName(), colName)) {
                sb.append('@' + colName);
                if (setClause == null) {
                    setClause = new StringBuffer("set" + nl);
                } else {
                    setClause.append(',' + nl);
                }
                setClause.append(colName + " = str_to_date (@" + colName + ", '" +
                        dtfmt + "')");

                String updateDatetime = "update " + table.getName() + " set " +
                        colName + " = null where " + colName + " = '0000-00-00 00:00:00';" + nl;
                if (updateStmt == null){
                    updateStmt = new StringBuffer(updateDatetime);
                } else {
                    updateStmt.append(updateDatetime);
                }
 
            } else {
                sb.append(colName);
            }

            if (isDeltaColumn(table, i)) {
            }

            if (i == table.getColumns().size() - 1) {
                sb.append(")");
            } else {
                sb.append(", " + nl);
            }
        }

        return sb.toString();
    }
}
