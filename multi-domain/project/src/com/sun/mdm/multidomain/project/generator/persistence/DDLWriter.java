/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.project.generator.persistence;

import com.sun.mdm.multidomain.parser.Attribute;
import com.sun.mdm.multidomain.parser.Definition;
import com.sun.mdm.multidomain.parser.MultiDomainModel;
import com.sun.mdm.multidomain.parser.Utils;
import com.sun.mdm.multidomain.project.generator.TemplateWriter;
import com.sun.mdm.multidomain.project.generator.exception.TemplateFileNotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author wee
 */
public class DDLWriter {

    private TemplateWriter mTW = null;
    private MultiDomainModel mMulitDomainObject;
    private String mPath;
    private final String ORACLE_DB_TYPE = "oracle";             // Oracle is being used

    private final String SQL_SERVER_DB_TYPE = "sql server";     // SQL Server is being used

    private final String DB2_DB_TYPE = "db2";                   // DB2 is being used

    private final String SYBASE_DB_TYPE = "sybase";             // Sybase is being used

    private final String MYSQL_DB_TYPE = "mysql";               // MySQL is being used


    /**
     * @param path output file path
     * @param eo elephant object
     * @param tmplPath file path to template
     * @exception TemplateWriterException template writer exception
     */
    public DDLWriter(String outPath, MultiDomainModel mo, String tmplPath)
            throws TemplateFileNotFoundException {
        try {
            // always create tables
            mTW = new TemplateWriter(tmplPath);
        } catch (TemplateFileNotFoundException ex) {
            throw ex;
        }
        
        mMulitDomainObject = mo;
        mPath = outPath;
    }

    /**
     * @exception ParserException parser exception
     * @exception InvalidTemplateFileException invalid template file exception
     * @exception UnmatchedTagsException unmatched tags exception
     */
    public void write(boolean full)
            throws Exception {

        ArrayList cons = mTW.construct();
        ArrayList values = new ArrayList();
        String res = "";
        
        //ArrayList definitions = mMulitDomainObject.getAllDefinitions();
        for (int i = 0; i < cons.size(); i++) {
            if (cons.get(i) instanceof String) {
                String dbStr = (String) cons.get(i);
                if (dbStr.indexOf("_<column-name>_") >= 0) {
                    res += constructColumns(dbStr);
                } else if (dbStr.indexOf("<column-name>") >= 0) {
                    res += constructColumns(dbStr).trim();
                } else if (dbStr.indexOf("INSERT INTO DOMAINS") >= 0) {
                    res += constructDomains(dbStr);
                } else {
                    res += dbStr;
                }
            } else {
                ArrayList insertList = (ArrayList)  cons.get(i);
                String insertStr = (String) insertList.get(0);
                if (insertStr.indexOf("RELATIONSHIP") >= 0) {
                    res += constructRelInsert(insertList);
                } else {
                    res += constructHierInsert(insertList);                    
                }
                
                
            }
        }
        
        res += "\r\n commit; \r\n";


        File outFile = new File(mPath);
        if (outFile.exists()) {
            outFile.delete();
        }
        Utils.writeFile(mPath, res);
    }


        
    private String constructDomains(String insertDomainTemplate) {
        
        String insertStr = "";
        for (String domainName : mMulitDomainObject.getDomainNames()) {
            String tempDomain = insertDomainTemplate;
            tempDomain = tempDomain.replaceAll("<domain_name>", domainName);
            insertStr += tempDomain;
        }
        
        return insertStr;
        
    }    
    private String constructHierInsert(ArrayList insertTemplate) {
        String insertStr = "";
        
        for (Definition definition : mMulitDomainObject.getAllDefinitions()) {            
            if (definition.getType().equals(Definition.TYPE_HIERARCHY)) {
                String insertDef = (String) insertTemplate.get(0);
                String descr = definition.getDescription();
                insertDef = insertDef.replaceAll("<hierarchy_name>", definition.getName());
                insertDef = insertDef.replaceAll("<hierarchy_description>", descr == null || descr.length() == 0 ? "" : descr);
                insertDef = insertDef.replaceAll("<domain>", definition.getDomain());
                insertDef = insertDef.replaceAll("<bi_directional>", definition.getDirection().equals("1") ? "F" : "T");
                insertDef = insertDef.replaceAll("<effective_from_date>",convertToDBDate(definition.getEffectiveFrom()));
                insertDef = insertDef.replaceAll("<effective_to_date>", convertToDBDate(definition.getEffectiveTo()));
                insertDef = insertDef.replaceAll("<effective_from_req>", definition.getPredefinedRequiredVal("start-date") ? "T" : "F");
                insertDef = insertDef.replaceAll("<effective_to_req>", definition.getPredefinedRequiredVal("end-date") ? "T" : "F");
                insertDef = insertDef.replaceAll("<effective_from_incl>", definition.getPredefinedIncludedVal("start-date") ? "T" : "F");
                insertDef = insertDef.replaceAll("<effective_to_incl>", definition.getPredefinedIncludedVal("end-date") ? "T" : "F");
                insertDef = insertDef.replaceAll("<plugin>", definition.getPlugin());
                insertStr += insertDef + constructExtendedAttr(definition.getExtendedAttributes(), (String) insertTemplate.get(1));
                
                //insertDef = insertDef.replaceAll("<effective_from_req>", definition.get ? "F" : "T");
                
            } 
        }
        
        return insertStr;
        
    }
    
    private String constructRelInsert(ArrayList insertTemplate) {
        String insertStr = "";
        
        for (Definition definition : mMulitDomainObject.getAllDefinitions()) {            
            if (definition.getType().equals(Definition.TYPE_RELATIONSHIP)) {
                String insertDef = (String) insertTemplate.get(0);
                String descr = definition.getDescription();
                insertDef = insertDef.replaceAll("<relationship_name>", definition.getName());
                insertDef = insertDef.replaceAll("<description>", descr == null || descr.length() == 0 ? "" : descr);
                insertDef = insertDef.replaceAll("<source_domain>", definition.getSourceDomain());
                insertDef = insertDef.replaceAll("<target_domain>", definition.getTargetDomain());
                insertDef = insertDef.replaceAll("<bi_directional>", definition.getDirection().equals("1") ? "F" : "T");
                insertDef = insertDef.replaceAll("<effective_from_req>", definition.getPredefinedRequiredVal("start-date") ? "T" : "F");
                insertDef = insertDef.replaceAll("<effective_to_req>", definition.getPredefinedRequiredVal("end-date") ? "T" : "F");
                insertDef = insertDef.replaceAll("<purge_date_req>", definition.getPredefinedRequiredVal("purge-date") ? "T" : "F");
                insertDef = insertDef.replaceAll("<effective_from_incl>", definition.getPredefinedIncludedVal("start-date") ? "T" : "F");
                insertDef = insertDef.replaceAll("<effective_to_incl>", definition.getPredefinedIncludedVal("end-date") ? "T" : "F");
                insertDef = insertDef.replaceAll("<purge_date_incl>", definition.getPredefinedIncludedVal("purge-date") ? "T" : "F");
                insertDef = insertDef.replaceAll("<plugin>", definition.getPlugin());
                insertStr += insertDef + constructExtendedAttr(definition.getExtendedAttributes(), (String) insertTemplate.get(1));
                
                //insertDef = insertDef.replaceAll("<effective_from_req>", definition.get ? "F" : "T");
                
            } 
        }
        
        return insertStr;
        
    }
       
    private String convertToDBDate(String dateStr) {
        
        String dbDate = "null";
        
        if (dateStr == null || dateStr.length() == 0 ) {
            return dbDate;
        }
        
        String dbType = mMulitDomainObject.getDatabase();
        if (dbType.equalsIgnoreCase(ORACLE_DB_TYPE) == true) {
            dbDate = "to_date('" + dateStr + "', 'mm/dd/yyyy')";
        } else if (dbType.equalsIgnoreCase(SQL_SERVER_DB_TYPE) == true) {
            
        } else if (dbType.equalsIgnoreCase(MYSQL_DB_TYPE) == true) {
            dbDate = "STR_TO_DATE('" + dateStr + "', '%m/%d/%y')";
        }
        
        return dbDate;
        
    }
    
    private String constructExtendedAttr(ArrayList<Attribute> attributes, String insertStrTemplate) {
        
        String insertStr = "";
        
        
        
        for (Attribute attr : attributes) {
            String tempStr = insertStrTemplate;
            tempStr = tempStr.replaceAll("<attribute_name>", attr.getName());
            tempStr = tempStr.replaceAll("<column_name>", attr.getColumnName());
            tempStr = tempStr.replaceAll("<column_type>", attr.getDataType());
            tempStr = tempStr.replaceAll("<default_value>", attr.getDefaultValue());
            tempStr = tempStr.replaceAll("<is_searchable>", attr.isSearchable() ?  "T" : "F");
            tempStr = tempStr.replaceAll("<is_required>", attr.isRequired() ?  "T" : "F");
            insertStr += tempStr;
        }
        
        return insertStr;
        
    }
    
    
    private String constructHierarchyInsert(ArrayList insertTemplate) {
        String insertStr = "";
        
        return insertStr;
        
    }    
    
    private String constructColumns(String columnTemplate) {
        
        //String[] types = {"string", "boolean", "int", "date", "char", "float"};
        String columns = "";
        int colIdx = 1;
        Map dataTypeMap = mMulitDomainObject.getDatatypeMap();
        String[]  types = (String[]) dataTypeMap.keySet().toArray(new String[0]);
        for (String type : types) {
            int colCount = Integer.parseInt((String) dataTypeMap.get(type.toLowerCase()));
            String dbColType = dbType(type);
            colIdx = 1;
            for (int i = 0; i < colCount; i++) {
                String tempCol = columnTemplate;
                colIdx += i;
                tempCol = tempCol.replaceAll("<column-name>", type.toUpperCase() + "_" + i);
                tempCol = tempCol.replaceAll("<column-type>", dbColType);
                //tempCol = tempCol.trim();
                columns += tempCol;
            }
        }
        
        return columns;
        
        /**
        for (Definition definition : mMulitDomainObject.getAllDefinitions()) {
            for (Attribute attr : definition.getExtendedAttributes()) {
                columnTemplate.replaceAll("$<column-name>", attr);
            }
        }
         */ 
        
        
    }

    private String dbType(String value) {
        ArrayList ret = new ArrayList();
        String retStr = "";
        String dbTypeID = mMulitDomainObject.getDatabase();
        if (dbTypeID.equalsIgnoreCase(ORACLE_DB_TYPE) == true) {
                if (value.startsWith("string")) {
                    //String s = value.substring(6);
                    retStr = "VARCHAR2(4000)";
                } else if (value.equals("byte") ||
                        value.equals("char")) {
                     retStr = "CHAR";
                } else if (value.equals("boolean")) {
                     retStr = "SMALLINT";
                } else if (value.equals("int")) {
                     retStr = "INTEGER";
                } else if (value.equals("long")) {
                     retStr = "LONG";
                } else if (value.equals("float")) {
                     retStr = "FLOAT";
                } else if (value.equals("date")) {
                     retStr = "DATE";
                }
        } else if (dbTypeID.equalsIgnoreCase(SQL_SERVER_DB_TYPE) == true) {

                if (value.startsWith("string")) {
                    String s = value.substring(6);
                     retStr = "varchar(" + s + ")";
                } else if (value.equals("byte") ||
                        value.equals("char")) {
                     retStr = "char";
                } else if (value.equals("boolean")) {
                     retStr = "smallint";
                } else if (value.equals("int")) {
                     retStr = "numeric";
                } else if (value.equals("long")) {
                     retStr = "varchar(max)";
                } else if (value.equals("float")) {
                     retStr = "float";
                } else if (value.equals("date")) {
                    ret.add("datetime");
                }
        } else if (dbTypeID.equalsIgnoreCase(MYSQL_DB_TYPE) == true) {
                if (value.startsWith("string")) {
                    String s = value.substring(6);
                     retStr = "VARCHAR(4000)";
                } else if (value.equals("byte") ||
                        value.equals("char")) {
                     retStr = "CHAR(1)";
                } else if (value.equals("boolean")) {
                     retStr = "ENUM('T', 'F') DEFAULT 'F' NOT NULL";
                } else if (value.equals("int")) {
                     retStr = "INTEGER";
                } else if (value.equals("long")) {
                     retStr = "INTEGER";
                } else if (value.equals("float")) {
                     retStr = "FLOAT";
                } else if (value.equals("date")) {
                     retStr = "DATETIME";
                }
        }   // TO DO:  Implement mappings for DB2 and Sybase

        return retStr;
    }
    
    
    private ArrayList dbTypeMap(ArrayList values) {
        ArrayList ret = new ArrayList();
        String dbTypeID = mMulitDomainObject.getDatabase();
        if (dbTypeID.equalsIgnoreCase(ORACLE_DB_TYPE) == true) {
            for (int i = 0; i < values.size(); i++) {
                String type = (String) values.get(i);

                if (type.startsWith("String")) {
                    String s = type.substring(6);
                    ret.add("VARCHAR2(" + s + ")");
                } else if (type.equals("Byte") ||
                        type.equals("Character") || type.equals("Boolean")) {
                    ret.add("CHAR");
                } else if (type.equals("Integer")) {
                    ret.add("INTEGER");
                } else if (type.equals("Long")) {
                    ret.add("LONG");
                } else if (type.equals("Float")) {
                    ret.add("FLOAT");
                } else if (type.equals("Date")) {
                    ret.add("DATE");
                }
            }
        } else if (dbTypeID.equalsIgnoreCase(SQL_SERVER_DB_TYPE) == true) {
            for (int i = 0; i < values.size(); i++) {
                String type = (String) values.get(i);

                if (type.startsWith("String")) {
                    String s = type.substring(6);
                    ret.add("varchar(" + s + ")");
                } else if (type.equals("Byte") ||
                        type.equals("Character")) {
                    ret.add("char");
                } else if (type.equals("Boolean")) {
                    ret.add("char");
                } else if (type.equals("Integer")) {
                    ret.add("numeric");
                } else if (type.equals("Long")) {
                    ret.add("varchar(max)");
                } else if (type.equals("Float")) {
                    ret.add("float");
                } else if (type.equals("Date")) {
                    ret.add("datetime");
                }
            }
        } else if (dbTypeID.equalsIgnoreCase(MYSQL_DB_TYPE) == true) {
            for (int i = 0; i < values.size(); i++) {
                String type = (String) values.get(i);

                if (type.startsWith("String")) {
                    String s = type.substring(6);
                    ret.add("varchar(" + s + ")");
                } else if (type.equals("Byte") ||
                        type.equals("Character")) {
                    ret.add("char");
                } else if (type.equals("Boolean")) {
                    ret.add("ENUM('T', 'F')");
                } else if (type.equals("Integer")) {
                    ret.add("smallint");
                } else if (type.equals("Long")) {
                    ret.add("integer)");
                } else if (type.equals("Float")) {
                    ret.add("float");
                } else if (type.equals("Date")) {
                    ret.add("datetime");
                }
            }
        }   // TO DO:  Implement mappings for DB2 and Sybase

        return ret;
    }
}

