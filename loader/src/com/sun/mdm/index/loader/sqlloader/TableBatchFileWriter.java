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
 * generate .bat for windows and .sh file for unix for each of the mdm tables
 * whose images will be loaded by the oracle sql loader
 * 
 * @author Sujit Biswas, David Peh
 * 
 */
public class TableBatchFileWriter implements Writer {

    private String table;
    private String baseDir;
    private String database;
    LoaderConfig config = LoaderConfig.getInstance();
    private static String ls = System.getProperty("line.separator");
    private static String fs = System.getProperty("file.separator");

    /**
     * @param userid
     * @param table
     */
    public TableBatchFileWriter(String table, String baseDir) {
        this.baseDir = baseDir;
        this.table = table;
        this.database = config.getSystemProperty("database");
    }

    /* (non-Javadoc)
     * @see com.sun.mdm.index.loader.sqlloader.Writer#write()
     */
    public void write() {
        try {
            FileWriter w = new FileWriter(baseDir + fs + table + ".bat");// windows

            if (database.equalsIgnoreCase("Oracle")) {
                w.write(getOracleExeScript());
            } else {  // default to MySQL

                w.write(getMySQLExeScript());
            }
            w.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            FileWriter w = new FileWriter(baseDir + fs + table + ".sh");// unix,
            // linux,
            // solaris

            if (database.equalsIgnoreCase("Oracle")) {
                w.write(getOracleExeScript());
            } else { // default to MySQL

                w.write(getMySQLExeScript());
            }
            w.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String getOracleExeScript() {
        StringBuilder sb = new StringBuilder();

        sb.append("sqlldr ");
        sb.append(" userid=" + config.getSystemProperty("sqlldr.userid"));

        sb.append(" control=control/" + table + ".ctl");
        sb.append(" bad=bad/" + table + ".bad");
        sb.append(" log=log/" + table + ".log");

        // TODO make this true after testing
        sb.append(" direct=true");
        sb.append(" parallel=true");
        sb.append(" discard=discard/" + table + ".discard");

        return sb.toString();
    }

    private String getMySQLExeScript() {
        StringBuilder sb = new StringBuilder();

        sb.append("mysql");
        sb.append(" --host=" + config.getSystemProperty("mysql.host"));
        sb.append(" --database=" + config.getSystemProperty("mysql.database.name"));
        sb.append(" --user=" + config.getSystemProperty("mysql.user"));
        sb.append(" --password=" + config.getSystemProperty("mysql.password"));
        sb.append(" --show-warnings --execute=");
        sb.append("\"source " + "control" + "/" + table + ".sql\"");

        return sb.toString();
    }
}
