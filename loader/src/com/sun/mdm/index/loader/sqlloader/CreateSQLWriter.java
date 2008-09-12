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
import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas
 * 
 */
public class CreateSQLWriter extends DropSQLWriter {

    private LoaderConfig config = LoaderConfig.getInstance();

    public CreateSQLWriter(ArrayList<Table> objectDefTables, String baseDir) {
        super(objectDefTables, baseDir);
    }

    @Override
    protected String getConstraintAction() {
        return " ENABLE CONSTRAINT ";
    }

    @Override
    protected String getScript() {
        StringBuilder sb = new StringBuilder();

        if (config.getSystemProperty("database").equalsIgnoreCase("Oracle")) {
            writeContraintSQLForFixedTables(sb);
            sb.append("\n\n");
            writeSQLForVariableTables(sb);
            sb.append("\n\n");
        }
        writeIndexSQLForFixedTables(sb);

        return sb.toString();
    }

    @Override
    protected void writeSQLForVariableTables(StringBuilder sb) {
        Table parent = tables.get(0);
        Table parentSBR = tables.get(1);

        String parent_pk = parent.getColumns().get(2);

        for (int i = 0; i < tables.size(); i++) {

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

    @Override
    protected String getFileName() {
        return "create.sql";
    }

    @Override
    protected void writeContraintSQLForFixedTables(StringBuilder sb) {
        sb.append(enableConstraint);
    }

    @Override
    protected void writeIndexSQLForFixedTables(StringBuilder sb) {
        if (config.getSystemProperty("database").equalsIgnoreCase("MySQL")) {
            sb.append(createForeignKey);
        }
        sb.append(createIndex);
    }
    private static String enableConstraint = "ALTER TABLE SBYN_SYSTEMOBJECT ENABLE CONSTRAINT PK_SBYNSYSTEMOBJECT ;\n" + "ALTER TABLE SBYN_SYSTEMOBJECT ENABLE CONSTRAINT FK_SYSTEMOBJECT_SYSTEMCODE ;\n" + "ALTER TABLE SBYN_SYSTEMSBR ENABLE CONSTRAINT PK_SBYNSYSTEMSBR ;\n" + "ALTER TABLE SBYN_OVERWRITE ENABLE CONSTRAINT FK_SYSTEMSBR_EUID ;\n" + "ALTER TABLE SBYN_OVERWRITE ENABLE CONSTRAINT PK_SBROVERWRITE ;\n" + "ALTER TABLE SBYN_ENTERPRISE ENABLE CONSTRAINT FK_ENTERPRISE_SYSTEMCODE_LID ;\n" + "ALTER TABLE SBYN_ENTERPRISE ENABLE CONSTRAINT FK_ENTERPRISE_EUID ;\n" + "ALTER TABLE SBYN_ENTERPRISE ENABLE CONSTRAINT PK_ENTERPRISE ;\n" + "ALTER TABLE SBYN_TRANSACTION ENABLE CONSTRAINT PK_TRANSACTION ;\n" + "ALTER TABLE SBYN_TRANSACTION ENABLE CONSTRAINT AK_TRANSACTION ;\n" + "ALTER TABLE SBYN_ASSUMEDMATCH ENABLE CONSTRAINT FK_AM_TRANSACTIONNUMBER ;\n" + "ALTER TABLE SBYN_POTENTIALDUPLICATES ENABLE CONSTRAINT PK_POTENTIALDUPLICATES ;\n" + "ALTER TABLE SBYN_MERGE ENABLE CONSTRAINT FK_SBYN_MERGE;\n" + "ALTER TABLE SBYN_MERGE ENABLE CONSTRAINT PK_SBYN_MERGE;\n";
    private static String createForeignKey = // for MySQL
            "ALTER TABLE SBYN_ENTERPRISE ADD CONSTRAINT FK_ENTERPRISE_EUID FOREIGN KEY (EUID) REFERENCES SBYN_SYSTEMSBR(EUID);\n"+
            "ALTER TABLE SBYN_ASSUMEDMATCH ADD CONSTRAINT FK_AM_TRANSACTIONNUMBER FOREIGN KEY (TRANSACTIONNUMBER) REFERENCES SBYN_TRANSACTION(TRANSACTIONNUMBER);\n";
    private static String createIndex = 
            "CREATE INDEX SBYN_ENTERPRISE1 on SBYN_ENTERPRISE (EUID ASC);\n" +
            "CREATE INDEX SBYN_TRANSACTION1 on SBYN_TRANSACTION (TIMESTAMP ASC);\n" + 
            "CREATE INDEX SBYN_TRANSACTION2 on SBYN_TRANSACTION (FUNCTION ASC);\n" + 
            "CREATE INDEX SBYN_TRANSACTION4 on SBYN_TRANSACTION (EUID2 ASC, TIMESTAMP ASC);\n" + 
            "CREATE INDEX SBYN_TRANSACTION3 on SBYN_TRANSACTION (TIMESTAMP ASC, TRANSACTIONNUMBER ASC);\n" + 
            "CREATE INDEX SBYN_ASSUMEDMATCH1 on SBYN_ASSUMEDMATCH (EUID ASC);\n" + 
            "CREATE INDEX SBYN_ASSUMEDMATCH2 ON SBYN_ASSUMEDMATCH (TRANSACTIONNUMBER ASC);\n" + 
            "CREATE INDEX SBYN_POTENTIALDUPLICATES1 on SBYN_POTENTIALDUPLICATES (EUID1 ASC);\n" + 
            "CREATE INDEX SBYN_POTENTIALDUPLICATES2 on SBYN_POTENTIALDUPLICATES (EUID2 ASC);\n" + 
            "CREATE INDEX SBYN_POTENTIALDUPLICATES3 ON SBYN_POTENTIALDUPLICATES (TRANSACTIONNUMBER ASC);\n";
}
