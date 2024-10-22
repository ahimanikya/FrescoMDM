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

/**
 * @author Sujit Biswas
 * 
 */
public class OracleControlFileWriter extends ControlFileWriter {

    private Table table;
    private String baseDir;

    /**
     * @param table
     * @param baseDir
     */
    public OracleControlFileWriter(Table table, String baseDir) {
        this.table = table;
        this.baseDir = baseDir;
    }

    public void write() {

        try {
            FileWriter w = new FileWriter(baseDir + "/" + table.getName() + ".ctl");

            w.write(getControlScript());

            w.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private String getControlScript() {

        StringBuilder sb = new StringBuilder();

        sb.append("load data \n");

        sb.append("\t infile '../masterindex/" + table.getName() + ".data' " + "\"str '" + getRecordLimiter() + "'\"" + "\n");
        sb.append("\t APPEND into table " + table.getName() + "\n");

        sb.append("\t fields terminated by \"|\" optionally enclosed by '\"' \n");

        sb.append("\t TRAILING NULLCOLS \n");

        sb.append("\t " + getColumns());

        return sb.toString();
    }

    private String getColumns() {

        StringBuilder sb = new StringBuilder();

        sb.append("( ");
        for (int i = 0; i < table.getColumns().size(); i++) {

            if (isDeltaColumn(table, i)) {
                continue;
            }

            sb.append(table.getColumns().get(i));

            if (!table.getColumnTypes().isEmpty() && isDate(table.getColumnTypes().get(i))) {
                sb.append(" date \"" + config.getObjectDefinition().getDateFormat() + " HH24:MI:SS" + "\"");
            }
            if (table.getColumnTypes().isEmpty() && isDate(table.getName(), table.getColumns().get(i))) {
                sb.append(" date \"" + "dd-mm-yy HH24:MI:SS" + "\"");
            }

            if (isDeltaColumn(table, i)) {
            }

            if (i == table.getColumns().size() - 1) {
                sb.append(" ) \n");
            } else {
                sb.append(", ");
            }
        }

        return sb.toString();
    }
}
