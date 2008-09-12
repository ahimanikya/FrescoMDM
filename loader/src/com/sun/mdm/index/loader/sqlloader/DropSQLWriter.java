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
import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas
 * 
 */
public class DropSQLWriter implements Writer {

    private LoaderConfig config = LoaderConfig.getInstance();
    protected ArrayList<Table> tables = new ArrayList<Table>();
    protected String baseDir;

    public DropSQLWriter(ArrayList<Table> objectDefTables, String baseDir) {
        this.baseDir = baseDir;
        this.tables = objectDefTables;
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
        return "drop.sql";
    }

    /**
     * 
     */
    protected String getScript() {
        StringBuilder sb = new StringBuilder();

        if (config.getSystemProperty("database").equalsIgnoreCase("Oracle")) {
            writeSQLForVariableTables(sb);
            sb.append("\n\n");

            writeContraintSQLForFixedTables(sb);
            sb.append("\n\n");
        }
        writeIndexSQLForFixedTables(sb);

        return sb.toString();
    }

    /**
     * @param sb
     */
    protected void writeSQLForVariableTables(StringBuilder sb) {
        Table parent = tables.get(0);
        Table parentSBR = tables.get(1);

        String parent_pk = parent.getColumns().get(2);

        for (int i = tables.size() - 1; i >= 0; i--) {

            String tableName = tables.get(i).getName();
            String tableNameWithoutPrefix = tableName.substring("SBYN_".length());

            String fk_contraint_name = null;

            if (tableName.equals(parent.getName()) || tableName.equals(parentSBR.getName())) {
                if (tableName.equals(parent.getName())) {
                    fk_contraint_name = "FK_" + tableNameWithoutPrefix + "_SYSTEMCODE_LID;";
                } else {// i==1

                    fk_contraint_name = "FK_" + tableNameWithoutPrefix + "_EUID;";
                }
            } else {
                fk_contraint_name = "FK_" + tableNameWithoutPrefix + "_" + parent_pk + ";";
            }

            String fk_contraint = "ALTER TABLE " + tableName + getConstraintAction() + fk_contraint_name;
            String pk_contraint = "ALTER TABLE " + tableName + getConstraintAction() + "PK_" + tableNameWithoutPrefix + ";";
            String u_contraint = "ALTER TABLE " + tableName + getConstraintAction() + "U_" + tableNameWithoutPrefix + ";";

            sb.append(fk_contraint + "\n");
            sb.append(pk_contraint + "\n");
            sb.append(u_contraint + "\n");

        }
    }

    /**
     * @param sb
     */
    protected void writeIndexSQLForFixedTables(StringBuilder sb) {
        if (config.getSystemProperty("database").equalsIgnoreCase("Oracle")) {
            sb.append(dropIndex);
            for (int i = 0; i < tables.size(); i++) {
                String tableName = tables.get(i).getName();
                String tableNameWithoutPrefix = tableName.substring("SBYN_".length());

                sb.append("drop index U_" + tableNameWithoutPrefix + ";\n");
            }
        } else {
            sb.append(mySQLDropForeignKey);
            sb.append(mySQLDropIndex);
        }
    }

    /**
     * @param sb
     */
    protected void writeContraintSQLForFixedTables(StringBuilder sb) {
        sb.append(disableConstraints);
    }

    /**
     * @return
     */
    protected String getConstraintAction() {
        return " DISABLE CONSTRAINT ";
    }
    private static String disableConstraints =
            "ALTER TABLE SBYN_OVERWRITE DISABLE CONSTRAINT FK_SYSTEMSBR_EUID ;\n" + "ALTER TABLE SBYN_OVERWRITE DISABLE CONSTRAINT PK_SBROVERWRITE ;\n" + "ALTER TABLE SBYN_ENTERPRISE DISABLE CONSTRAINT FK_ENTERPRISE_SYSTEMCODE_LID ;\n" + "ALTER TABLE SBYN_ENTERPRISE DISABLE CONSTRAINT FK_ENTERPRISE_EUID ;\n" + "ALTER TABLE SBYN_ENTERPRISE DISABLE CONSTRAINT PK_ENTERPRISE ;\n" + "ALTER TABLE SBYN_SYSTEMOBJECT DISABLE CONSTRAINT PK_SBYNSYSTEMOBJECT ;\n" + "ALTER TABLE SBYN_SYSTEMOBJECT DISABLE CONSTRAINT FK_SYSTEMOBJECT_SYSTEMCODE ;\n" + "ALTER TABLE SBYN_SYSTEMSBR DISABLE CONSTRAINT PK_SBYNSYSTEMSBR ;\n" + "ALTER TABLE SBYN_ASSUMEDMATCH DISABLE CONSTRAINT FK_AM_TRANSACTIONNUMBER ;\n" + "ALTER TABLE SBYN_POTENTIALDUPLICATES DISABLE CONSTRAINT PK_POTENTIALDUPLICATES ;\n" + "ALTER TABLE SBYN_ASSUMEDMATCH DISABLE CONSTRAINT PK_ASSUMEDMATCH;\n" + "ALTER TABLE SBYN_MERGE DISABLE CONSTRAINT FK_SBYN_MERGE;\n" + "ALTER TABLE SBYN_MERGE DISABLE CONSTRAINT PK_SBYN_MERGE;\n" + "ALTER TABLE SBYN_TRANSACTION DISABLE CONSTRAINT PK_TRANSACTION ;\n" + "ALTER TABLE SBYN_TRANSACTION DISABLE CONSTRAINT AK_TRANSACTION ;\n";
    private static String dropIndex = 
            "DROP INDEX SBYN_ENTERPRISE1 ;\n" +
            "DROP INDEX SBYN_TRANSACTION1 ;\n" + 
            "DROP INDEX SBYN_TRANSACTION2 ;\n" + 
            "DROP INDEX SBYN_TRANSACTION4 ;\n" +
            "DROP INDEX SBYN_TRANSACTION3 ;\n" + 
            "DROP INDEX SBYN_ASSUMEDMATCH1 ;\n" + 
            "DROP INDEX SBYN_ASSUMEDMATCH2 ;\n" + 
            "DROP INDEX SBYN_POTENTIALDUPLICATES1 ;\n" + 
            "DROP INDEX SBYN_POTENTIALDUPLICATES2 ;\n" + 
            "DROP INDEX SBYN_POTENTIALDUPLICATES3 ;\n";
    private static String mySQLDropForeignKey = 
            "ALTER TABLE SBYN_ASSUMEDMATCH DROP FOREIGN KEY FK_AM_TRANSACTIONNUMBER;\n" +
            "ALTER TABLE SBYN_ENTERPRISE DROP FOREIGN KEY FK_ENTERPRISE_EUID;\n";
     private static String mySQLDropIndex =       
            "ALTER TABLE SBYN_ENTERPRISE DROP INDEX SBYN_ENTERPRISE1;\n" +
            "ALTER TABLE SBYN_TRANSACTION DROP INDEX SBYN_TRANSACTION1;\n" + 
            "ALTER TABLE SBYN_TRANSACTION DROP INDEX SBYN_TRANSACTION2;\n" + 
            "ALTER TABLE SBYN_TRANSACTION DROP INDEX SBYN_TRANSACTION4;\n" +
            "ALTER TABLE SBYN_TRANSACTION DROP INDEX SBYN_TRANSACTION3;\n" + 
            "ALTER TABLE SBYN_ASSUMEDMATCH DROP INDEX SBYN_ASSUMEDMATCH1;\n" + 
            "ALTER TABLE SBYN_ASSUMEDMATCH DROP INDEX SBYN_ASSUMEDMATCH2;\n" + 
            "ALTER TABLE SBYN_POTENTIALDUPLICATES DROP INDEX SBYN_POTENTIALDUPLICATES1;\n" + 
            "ALTER TABLE SBYN_POTENTIALDUPLICATES DROP INDEX SBYN_POTENTIALDUPLICATES2;\n" + 
            "ALTER TABLE SBYN_POTENTIALDUPLICATES DROP INDEX SBYN_POTENTIALDUPLICATES3;\n";
}
