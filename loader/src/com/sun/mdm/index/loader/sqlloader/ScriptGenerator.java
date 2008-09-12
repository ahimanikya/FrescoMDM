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

import java.io.File;
import java.util.ArrayList;
import java.io.FileInputStream;
import org.xml.sax.InputSource;

import com.sun.mdm.index.dataobject.objectdef.ObjectDefinition;
import com.sun.mdm.index.loader.config.LoaderConfig;
import com.sun.mdm.index.parser.EIndexObject;
import com.sun.mdm.index.parser.Utils;
import java.io.InputStream;
import javax.naming.spi.ObjectFactory;

/**
 * @author Sujit Biswas
 * 
 */
public class ScriptGenerator {

    private String[] SBYN_SYSTEMOBJECT = {"SYSTEMCODE", "LID", "CHILDTYPE",
        "CREATEUSER", "CREATEFUNCTION", "CREATEDATE", "UPDATEUSER",
        "UPDATEFUNCTION", "UPDATEDATE", "STATUS"
    };
    private String[] SBYN_SYSTEMSBR = {"EUID", "CHILDTYPE", "CREATESYSTEM",
        "CREATEUSER", "CREATEFUNCTION", "CREATEDATE", "UPDATESYSTEM",
        "UPDATEUSER", "UPDATEFUNCTION", "UPDATEDATE", "STATUS",
        "REVISIONNUMBER"
    };
    private String[] SBYN_OVERWRITE = {"EUID", "PATH", "TYPE", "INTEGERDATA",
        "BOOLEANDATA", "STRINGDATA", "BYTEDATA", "LONGDATA", "DATEDATA",
        "FLOATDATA", "TIMESTAMPDATA"
    };
    private String[] SBYN_ENTERPRISE = {"SYSTEMCODE", "LID", "EUID"};
    private String[] SBYN_TRANSACTION = {"TRANSACTIONNUMBER", "LID1", "LID2",
        "EUID1", "EUID2", "FUNCTION", "SYSTEMUSER", "TIMESTAMP", "DELTA",
        "SYSTEMCODE", "LID", "EUID"
    };
    private String[] SBYN_ASSUMEDMATCH = {"ASSUMEDMATCHID", "EUID",
        "SYSTEMCODE", "LID", "WEIGHT", "TRANSACTIONNUMBER"
    };
    private String[] SBYN_POTENTIALDUPLICATES = {"POTENTIALDUPLICATEID",
        "WEIGHT", "TYPE", "DESCRIPTION", "STATUS", "HIGHMATCHFLAG",
        "RESOLVEDUSER", "RESOLVEDDATE", "RESOLVEDCOMMENT", "EUID2",
        "TRANSACTIONNUMBER", "EUID1"
    };
    private String mDatabaseType = null;     // database type

    private ArrayList<Table> tables = new ArrayList<Table>();
    private ArrayList<Table> objectDefTables = new ArrayList<Table>();
    private LoaderConfig config = LoaderConfig.getInstance();
    private File sqlldr;
    private File control;
    private File bad;
    private File discard;
    private File log;

    public ScriptGenerator() {
        init();
    }

    private void init() {
        mDatabaseType = config.getSystemProperty("database");
        initFolder();
        initTables();

    }

    private void initFolder() {
        String workingDir = config.getWorkingDir();

        sqlldr = new File(workingDir + "/sqlldr");

        if (!sqlldr.exists()) {
            sqlldr.mkdir();
        }

        control = new File(workingDir + "/sqlldr/control");
        if (!control.exists()) {
            control.mkdir();
        }


        bad = new File(workingDir + "/sqlldr/bad");

        if (!bad.exists()) {
            bad.mkdir();
        }

        discard = new File(workingDir + "/sqlldr/discard");

        if (!discard.exists()) {
            discard.mkdir();
        }

        log = new File(workingDir + "/sqlldr/log");

        if (!log.exists()) {
            log.mkdir();
        }

    }

    /**
     * 
     */
    private void initTables() {
        Table systemObject = new Table("SBYN_SYSTEMOBJECT", SBYN_SYSTEMOBJECT);

        Table systemSBR = new Table("SBYN_SYSTEMSBR", SBYN_SYSTEMSBR);

        Table overrite = new Table("SBYN_OVERWRITE", SBYN_OVERWRITE);

        Table enterprise = new Table("SBYN_ENTERPRISE", SBYN_ENTERPRISE);

        Table transaction = new Table("SBYN_TRANSACTION", SBYN_TRANSACTION);

        Table assumedMatch = new Table("SBYN_ASSUMEDMATCH", SBYN_ASSUMEDMATCH);

        Table potentialDuplicates = new Table("SBYN_POTENTIALDUPLICATES",
                SBYN_POTENTIALDUPLICATES);

        tables.add(systemObject);
        tables.add(systemSBR);
        tables.add(overrite);
        tables.add(enterprise);
        tables.add(transaction);
        tables.add(assumedMatch);
        tables.add(potentialDuplicates);

        addObjectDefinitionTables();

    }

    private void addObjectDefinitionTables() {
        ObjectDefinition objectDef = config.getObjectDefinition();

        Table parent = createTable(objectDef, false);
        addObjectDefinitionTable(parent);
        parent = createTable(objectDef, true);
        addObjectDefinitionTable(parent);

        for (ObjectDefinition o : objectDef.getChildren()) {
            Table child = createChildTable(o, false);
            addObjectDefinitionTable(child);
            child = createChildTable(o, true);
            addObjectDefinitionTable(child);
        }

    }

    /**
     * @param parent
     */
    private void addObjectDefinitionTable(Table table) {
        tables.add(table);
        objectDefTables.add(table);
    }

    private Table createChildTable(ObjectDefinition objectDef, boolean isSBR) {

        String parent = config.getObjectDefinition().getName();

        String[] columns = new String[objectDef.getFields().size() + 2];
        String[] columnTypes = new String[objectDef.getFields().size() + 2];

        columns[0] = parent + "id";
        columns[1] = objectDef.getName() + "id";

        for (int i = 0; i < objectDef.getFields().size(); i++) {
            columns[i + 2] = objectDef.getField(i).getName();
            columnTypes[i + 2] = objectDef.getField(i).getType();
        }

        String tableName = "SBYN_" + objectDef.getName().toUpperCase();

        if (isSBR) {
            tableName = tableName + "SBR";
        }
        return new Table(tableName, columns, columnTypes);
    }

    private Table createTable(ObjectDefinition objectDef, boolean isSBR) {

        String[] columns = new String[objectDef.getFields().size() + 3];
        String[] columnTypes = new String[objectDef.getFields().size() + 3];

        columns[0] = "systemcode";
        columns[1] = "lid";
        columns[2] = objectDef.getName() + "id";

        for (int i = 0; i < objectDef.getFields().size(); i++) {
            columns[i + 3] = objectDef.getField(i).getName();
            columnTypes[i + 3] = objectDef.getField(i).getType();
        }

        String tableName = "SBYN_" + objectDef.getName().toUpperCase();

        if (isSBR) {
            columns = new String[objectDef.getFields().size() + 2];
            columnTypes = new String[objectDef.getFields().size() + 2];

            columns[0] = "euid";
            columns[1] = objectDef.getName() + "id";

            for (int i = 0; i < objectDef.getFields().size(); i++) {
                columns[i + 2] = objectDef.getField(i).getName();
                columnTypes[i + 2] = objectDef.getField(i).getType();
            }

            tableName = tableName + "SBR";
        }
        return new Table(tableName, columns, columnTypes);
    }

    public void generate() {
        generateFiles();

    }

    private void generateFiles() {

        for (Table t : tables) {

            if (mDatabaseType.equalsIgnoreCase("Oracle")) {
                OracleControlFileWriter cfw = new OracleControlFileWriter(t, control.getAbsolutePath());
                cfw.write();
            } else if (mDatabaseType.equalsIgnoreCase("MySQL")) {
                MySQLControlFileWriter cfw = new MySQLControlFileWriter(t, control.getAbsolutePath());
                cfw.write();
            }

            Writer ef = new TableBatchFileWriter(t.getName(), sqlldr.getAbsolutePath());
            ef.write();
        }
        DropSQLWriter dsw = new DropSQLWriter(objectDefTables, sqlldr.getAbsolutePath());
        dsw.write();

        CreateSQLWriter csw = new CreateSQLWriter(objectDefTables, sqlldr.getAbsolutePath());
        csw.write();

        TruncateSQLWriter tsw = new TruncateSQLWriter(tables, sqlldr.getAbsolutePath());
        tsw.write();


        BatchFileWriter bfw = new BatchFileWriter(tables, sqlldr.getAbsolutePath());
        bfw.write();
    }

    protected ArrayList<Table> getTables() {
        return tables;
    }

    public static void main(String[] args) {
        ScriptGenerator s = new ScriptGenerator();
        s.generate();

    }
}
