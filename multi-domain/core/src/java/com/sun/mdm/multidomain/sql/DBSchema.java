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
package com.sun.mdm.multidomain.sql;

/**
 *
 * @author davidp
 */
public final class DBSchema {

    /*
     * Multi-domain database table names
     * */
    public static enum DBTable {

        /* Table names and primary-key column names.
         * */
        RELATIONSHIP_DEF("RELATIONSHIP_DEF", "RELATIONSHIP_DEF_ID"),
        RELATIONSHIP_EA("RELATIONSHIP_EA", "EA_ID"),
        RELATIONSHIP("RELATIONSHIP", "RELATIONSHIP_ID"),
        RELATIONSHIP_EAV("RELATIONSHIP_EAV", "EA_ID"),
        HIERARCHY_DEF("HIERARCHY_DEF", "HIERARCHY_DEF_ID"),
        HIERARCHY_NODE_EA("HIERARCHY_NODE_EA", "EA_ID"),
        HIERARCHY_NODE("HIERARCHY_NODE", "HIERARCHY_NODE_ID"),
        HIERARCHY_NODE_EAV("HIERARCHY_NODE_EAV", "EA_ID"),
        DOMAINS("DOMAINS", "DOMAIN_NAME"),;
        public final String tableName;
        public final String pkColumnName;

        DBTable(String tbl, String pkCol) {
            this.tableName = tbl;
            this.pkColumnName = pkCol;
        }
    }

    /*
     * Columns for RELATIONSHIP_DEF table
     * */
    public static enum RELATIONSHIP_DEF {

        RELATIONSHIP_DEF_ID("RELATIONSHIP_DEF_ID"),
        RELATIONSHIP_NAME("RELATIONSHIP_NAME"),
        DESCRIPTION("DESCRIPTION"),
        SOURCE_DOMAIN("SOURCE_DOMAIN"),
        TARGET_DOMAIN("TARGET_DOMAIN"),
        BIDIRECTIONAL("BIDIRECTIONAL"),
        EFFECTIVE_FROM_REQ("EFFECTIVE_FROM_REQ"),
        EFFECTIVE_TO_REQ("EFFECTIVE_TO_REQ"),
        PURGE_DATE_REQ("PURGE_DATE_REQ"),
        EFFECTIVE_FROM_INCL("EFFECTIVE_FROM_INCL"),
        EFFECTIVE_TO_INCL("EFFECTIVE_TO_INCL"),
        PURGE_DATE_INCL("PURGE_DATE_INCL"),
        PLUGIN("PLUGIN"),;
        public final String columnName;
        public final String prefixedColumnName;

        RELATIONSHIP_DEF(String col) {
            this.columnName = col;
            StringBuffer sb = new StringBuffer(getTableName());
            sb.append(".");
            sb.append(col);
            this.prefixedColumnName = sb.toString();
        }

        public static String getTableName() {
            return DBTable.RELATIONSHIP_DEF.tableName;
        }

        public static String getPKColumName() {
            return DBTable.RELATIONSHIP_DEF.pkColumnName;
        }
    }

    /*
     * Columns for RELATIONSHIP_EA table
     * */
    public static enum RELATIONSHIP_EA {

        EA_ID("EA_ID"),
        RELATIONSHIP_DEF_ID("RELATIONSHIP_DEF_ID"),
        ATTRIBUTE_NAME("ATTRIBUTE_NAME"),
        COLUMN_NAME("COLUMN_NAME"),
        COLUMN_TYPE("COLUMN_TYPE"),
        DEFAULT_VALUE("DEFAULT_VALUE"),
        IS_SEARCHABLE("IS_SEARCHABLE"),
        IS_REQUIRED("IS_REQUIRED"),
        IS_INCLUDED("IS_INCLUDED"),;
        public final String columnName;
        public final String prefixedColumnName;

        RELATIONSHIP_EA(String col) {
            this.columnName = col;
            StringBuffer sb = new StringBuffer(getTableName());
            sb.append(".");
            sb.append(col);
            this.prefixedColumnName = sb.toString();
        }

        public static String getTableName() {
            return DBTable.RELATIONSHIP_EA.tableName;
        }

        public static String getPKColumName() {
            return DBTable.RELATIONSHIP_EA.pkColumnName;
        }
    }

    /*
     * Columns for RELATIONSHIP table
     * */
    public static enum RELATIONSHIP {

        RELATIONSHIP_ID("RELATIONSHIP_ID"),
        RELATIONSHIP_DEF_ID("RELATIONSHIP_DEF_ID"),
        SOURCE_EUID("SOURCE_EUID"),
        TARGET_EUID("TARGET_EUID"),
        EFFECTIVE_FROM_DATE("EFFECTIVE_FROM_DATE"),
        EFFECTIVE_TO_DATE("EFFECTIVE_TO_DATE"),
        PURGE_DATE("PURGE_DATE"),;
        public final String columnName;
        public final String prefixedColumnName;

        RELATIONSHIP(String col) {
            this.columnName = col;
            StringBuffer sb = new StringBuffer(getTableName());
            sb.append(".");
            sb.append(col);
            this.prefixedColumnName = sb.toString();
        }

        public static String getTableName() {
            return DBTable.RELATIONSHIP.tableName;
        }

        public static String getPKColumName() {
            return DBTable.RELATIONSHIP.pkColumnName;
        }
    }

    /*
     * Columns for RELATIONSHIP_EA table
     * */
    public static enum RELATIONSHIP_EAV {

        EAV_ID("EAV_ID"),
        RELATIONSHIP_ID("RELATIONSHIP_ID"),;
        public final String columnName;
        public final String prefixedColumnName;

        RELATIONSHIP_EAV(String col) {
            this.columnName = col;
            StringBuffer sb = new StringBuffer(getTableName());
            sb.append(".");
            sb.append(col);
            this.prefixedColumnName = sb.toString();
        }

        public static String getTableName() {
            return DBTable.RELATIONSHIP_EAV.tableName;
        }

        public static String getPKColumName() {
            return DBTable.RELATIONSHIP_EAV.pkColumnName;
        }
    }

    /*
     * Columns for HIERARCHY_DEF table
     * */
    public static enum HIERARCHY_DEF {

        HIERARCHY_DEF_ID("HIERARCHY_DEF_ID"),
        HIERARCHY_NAME("HIERARCHY_NAME"),
        DESCRIPTION("DESCRIPTION"),
        DOMAIN("DOMAIN"),
        EFFECTIVE_FROM_DATE("EFFECTIVE_FROM_DATE"),
        EFFECTIVE_TO_DATE("EFFECTIVE_TO_DATE"),
        PLUGIN("PLUGIN"),;
        public String columnName;
        public String prefixedColumnName;

        HIERARCHY_DEF(String col) {
            this.columnName = col;
            StringBuffer sb = new StringBuffer(getTableName());
            sb.append(".");
            sb.append(col);
            this.prefixedColumnName = sb.toString();
        }

        public static String getTableName() {
            return DBTable.HIERARCHY_DEF.tableName;
        }

        public static String getPKColumName() {
            return DBTable.HIERARCHY_DEF.pkColumnName;
        }
    }

    /*
     * Columns for HIERARCHY_NODE_EA table
     * */
    public static enum HIERARCHY_NODE_EA {

        EA_ID("EA_ID"),
        HIERARCHY_DEF_ID("HIERARCHY_DEF_ID"),
        ATTRIBUTE_NAME("ATTRIBUTE_NAME"),
        COLUMN_NAME("COLUMN_NAME"),
        COLUMN_TYPE("COLUMN_TYPE"),
        DEFAULT_VALUE("DEFAULT_VALUE"),
        IS_SEARCHABLE("IS_SEARCHABLE"),
        IS_REQUIRED("IS_REQUIRED"),
        IS_INCLUDED("IS_INCLUDED"),;
        public String columnName;
        public String prefixedColumnName;

        HIERARCHY_NODE_EA(String col) {
            this.columnName = col;
            StringBuffer sb = new StringBuffer(getTableName());
            sb.append(".");
            sb.append(col);
            this.prefixedColumnName = sb.toString();
        }

        public static String getTableName() {
            return DBTable.HIERARCHY_NODE_EA.tableName;
        }

        public static String getPKColumName() {
            return DBTable.HIERARCHY_NODE_EA.pkColumnName;
        }
    }

    /*
     * Columns for HIERARCHY_NODE table
     * */
    public static enum HIERARCHY_NODE {

        HIERARCHY_NODE_ID("HIERARCHY_NODE_ID"),
        HIERARCHY_DEF_ID("HIERARCHY_DEF_ID"),
        PARENT_NODE_ID("PARENT_NODE_ID"),
        EUID("EUID"),
        PARENT_EUID("PARENT_EUID"),
        EFFECTIVE_FROM_DATE("EFFECTIVE_FROM_DATE"),
        EFFECTIVE_TO_DATE("EFFECTIVE_TO_DATE"),;
        public final String columnName;
        public final String prefixedColumnName;

        HIERARCHY_NODE(String col) {
            this.columnName = col;
            StringBuffer sb = new StringBuffer(getTableName());
            sb.append(".");
            sb.append(col);
            this.prefixedColumnName = sb.toString();
        }

        public static String getTableName() {
            return DBTable.HIERARCHY_NODE.tableName;
        }

        public static String getPKColumName() {
            return DBTable.HIERARCHY_NODE.pkColumnName;
        }
    }

    /*
     * Columns for HIERARCHY_NODE_EAV table
     * */
    public static enum HIERARCHY_NODE_EAV {

        EAV_ID("EAV_ID"),
        HIERARCHY_NODE_ID("HIERARCHY_NODE_ID"),;
        public final String columnName;
        public final String prefixedColumnName;

        HIERARCHY_NODE_EAV(String col) {
            this.columnName = col;
            StringBuffer sb = new StringBuffer(getTableName());
            sb.append(".");
            sb.append(col);
            this.prefixedColumnName = sb.toString();
        }

        public static String getTableName() {
            return DBTable.HIERARCHY_NODE_EAV.tableName;
        }

        public static String getPKColumName() {
            return DBTable.HIERARCHY_NODE_EAV.pkColumnName;
        }
    }

    /*
     * Columns for DOMAINS table
     * */
    public static enum DOMAINS {

        DOMAIN_NAME("DOMAIN_NAME"),
        CONTEXT_FACTORY("CONTEXT_FACTORY"),
        URL("URL"),
        PRINCIPAL("PRINCIPAL"),
        CREDENTIAL("CREDENTIAL"),;
        public final String columnName;
        public final String prefixedColumnName;

        DOMAINS(String col) {
            this.columnName = col;
            StringBuffer sb = new StringBuffer(getTableName());
            sb.append(".");
            sb.append(col);
            this.prefixedColumnName = sb.toString();
        }

        public static String getTableName() {
            return DBTable.DOMAINS.tableName;
        }

        public static String getPKColumName() {
            return DBTable.DOMAINS.pkColumnName;
        }
    }
    /*
     * No instantiation
     * */

    private DBSchema() {
    }
}
