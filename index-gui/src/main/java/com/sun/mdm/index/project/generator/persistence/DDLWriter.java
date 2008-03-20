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
package com.sun.mdm.index.project.generator.persistence;

import java.util.ArrayList;
import com.sun.mdm.index.project.generator.TemplateWriter;
import com.sun.mdm.index.project.generator.exception.TemplateWriterException;
import com.sun.mdm.index.project.generator.exception.TemplateFileNotFoundException;
import com.sun.mdm.index.project.generator.exception.InvalidTemplateFileException;
import com.sun.mdm.index.project.generator.exception.UnmatchedTagsException;
import com.sun.mdm.index.parser.EIndexObject;
import com.sun.mdm.index.parser.NodeDef;
import com.sun.mdm.index.parser.ParserException;
import com.sun.mdm.index.parser.RelationDef;
import com.sun.mdm.index.parser.Utils;
import java.io.File;


/**
 * @author gzheng
 * @version
 */
public class DDLWriter {
    private TemplateWriter mTW = null;
    private EIndexObject mEIndexObject;
    private String mPath;
    private final String ORACLE_DB_TYPE = "oracle";             // Oracle is being used
    private final String SQL_SERVER_DB_TYPE = "sql server";     // SQL Server is being used
    private final String DB2_DB_TYPE = "db2";                   // DB2 is being used
    private final String SYBASE_DB_TYPE = "sybase";             // Sybase is being used
    

    /**
     * @param path output file path
     * @param eo elephant object
     * @param tmplPath file path to template
     * @exception TemplateWriterException template writer exception
     */
    public DDLWriter(String outPath, EIndexObject eo, String tmplPath)
        throws TemplateWriterException {
        try {
            // always create tables
            mTW = new TemplateWriter(tmplPath);
        } catch (TemplateFileNotFoundException ex) {
            throw ex;
        }
        mEIndexObject = eo;
        mPath = outPath;
    }

    /**
     * @exception ParserException parser exception
     * @exception InvalidTemplateFileException invalid template file exception
     * @exception UnmatchedTagsException unmatched tags exception
     */
    public void write(boolean full)
        throws ParserException, InvalidTemplateFileException, UnmatchedTagsException {
        RelationDef relDef = null;
        ArrayList relList = mEIndexObject.getRelationships();
        if (null != relList) {
            relDef = (RelationDef) relList.get(0);
        } else {
            throw new ParserException("No relationship defined");
        }

        String res = "";
        ArrayList cons = mTW.construct();
        ArrayList values = new ArrayList();

        //always create tables
        if (true) {
            // creating 'drop' statetments
            ArrayList nodesList = new ArrayList();

        if (relDef.getElements() != null) {
             nodesList.addAll(relDef.getElements());
            }
            nodesList.add(relDef.getName());
            values.add(nodesList);
            res += mTW.writeConstruct((String) cons.get(0), values);

            if (full) {
            // creating root node tables

            values.clear();
            values.add(relDef.getName());
            ArrayList vals;
            vals = getRootFieldNames(relDef);
            vals.remove(0);
            values.add(vals);
            vals = getRootFieldTypes(relDef);
            vals.remove(0);
            values.add(vals);
            String keys = getRootFieldKeys(relDef);
            values.add(keys);
            res += mTW.writeConstruct((String) cons.get(1), values);

            values.clear();
            values = getLeafNodes(relDef);
            if (values != null) {
                for (int i = 0; i < values.size(); i++) {
                    ArrayList al = (ArrayList) values.get(i);
                }
                res += mTW.writeConstruct((String) cons.get(2), values);
            } else {
                values = new ArrayList();
            }

              values.clear();
              values.add(nodesList);
              values.add(mEIndexObject.getSystems());

            res += mTW.writeConstruct((String) cons.get(3), values);
            }
        }
        //if there is a existing outPut file, then delete it. 
        File outFile = new File(mPath);
        if (outFile.exists()){
            outFile.delete();
        }
        Utils.writeFile(mPath, res);
    }


    private ArrayList getLeafChildren(RelationDef rel) {
        ArrayList ret = null;
        ArrayList childList = rel.getChildren();
        if (null != childList) {
            if (null == ret) {
                ret = new ArrayList();
            }
            ret.addAll(rel.getChildren());
        }

        ArrayList relList = rel.getRelations();
        if (null != relList) {
            if (null == ret) {
                ret = new ArrayList();
            }

            for (int i = 0; i < relList.size(); i++) {
                RelationDef r = (RelationDef) relList.get(i);
                ret.add(r.getName());
                ret.addAll(getLeafChildren(r));
            }
        }

        return ret;
    }


    private ArrayList getLeafNodes(RelationDef rel) {
        ArrayList ret = null;
        ArrayList childList = rel.getChildren();
        if (null != childList) {
            if (null == ret) {
                ret = new ArrayList();
            }

            for (int i = 0; i < childList.size(); i++) {
                NodeDef node = mEIndexObject.getNode((String) childList.get(i));
                String pname = rel.getName();
                String cname = node.getTag();
                ArrayList fieldnames = node.createFieldNames();
                fieldnames.remove(0);
                ArrayList fieldtypes = dbTypeMap(node.createFieldTypeSizes());
                String okey = "";
                ArrayList keys = node.createFieldKeys();
                if (keys != null && fieldnames != null) {
                    boolean keyFound = false;
                    for (int j = 0; j < keys.size(); j++) {
                        if (((String) keys.get(j)).equals("true")) {
                            okey += ", " + (String) fieldnames.get(j - 1);
                            keyFound = true;
                        }
                    }
                    // If no key was found, then use the object ID.  This is necessary
                    // for supporting unkeyed objects.
                    if (keyFound == false) {
                        okey += ", " + cname + "ID";
                    } 
                }
                
                fieldtypes.remove(0);
                ArrayList val = new ArrayList();
                val.add(pname);
                val.add(cname);
                val.add(fieldnames);
                val.add(fieldtypes);
                val.add(okey);
                ret.add(val);
            }
        }

        ArrayList relList = rel.getRelations();
        if (null != relList) {
            if (null == ret) {
                ret = new ArrayList();
            }

            for (int i = 0; i < relList.size(); i++) {
                RelationDef r = (RelationDef) relList.get(i);
                NodeDef node = mEIndexObject.getNode(r.getName());
                String pname = rel.getName();
                String cname = node.getTag();
                ArrayList fieldnames = node.createFieldNames();
                fieldnames.remove(0);
                ArrayList fieldtypes = dbTypeMap(node.createFieldTypeSizes());
                fieldtypes.remove(0);
                String okey = "";
                ArrayList keys = node.createFieldKeys();
                if (keys != null && fieldnames != null) {
                    boolean keyFound = false;
                    for (int j = 0; j < keys.size(); j++) {
                        if (((String) keys.get(j)).equals("true")) {
                            okey += ", " + (String) fieldnames.get(j - 1);
                            keyFound = true;
                        }
                    }
                    // If no key was found, then use the object ID.  This is necessary
                    // for supporting unkeyed objects.
                    if (keyFound == false) {
                        okey += ", " + cname + "ID";
                    } 
                }
                ArrayList val = new ArrayList();
                val.add(pname);
                val.add(cname);
                val.add(fieldnames);
                val.add(fieldtypes);
                val.add(okey);
                ret.add(val);
                ret.addAll(getLeafNodes(r));
            }
        }

        return ret;
    }


    private ArrayList getLeafParents(RelationDef rel) {
        ArrayList ret = null;
        ArrayList childList = rel.getChildren();
        if (null != childList) {
            if (null == ret) {
                ret = new ArrayList();
            }

            for (int i = 0; i < childList.size(); i++) {
                ret.add(rel.getName());
            }
        }

        ArrayList relList = rel.getRelations();
        if (null != relList) {
            if (null == ret) {
                ret = new ArrayList();
            }

            for (int i = 0; i < relList.size(); i++) {
                RelationDef r = (RelationDef) relList.get(i);
                ret.add(rel.getName());
                ret.addAll(getLeafParents(r));
            }
        }

        return ret;
    }


    private ArrayList getRootFieldNames(RelationDef rel) {
        NodeDef node = mEIndexObject.getNode(rel.getName());
        return node.createFieldNames();
    }


    private String getRootFieldKeys(RelationDef rel) {
        String ret = "";
        NodeDef node = mEIndexObject.getNode(rel.getName());
        ArrayList keys = node.createFieldKeys();
        ArrayList fields = node.createFieldNames();
        if (keys != null && fields != null) {
            for (int i = 0; i < keys.size(); i++) {
                if (((String) keys.get(i)).equals("true")) {
                    ret += ", " + (String) fields.get(i);
                }
            }
        }
        
        return ret;
    }


    private ArrayList getRootFieldTypes(RelationDef rel) {
        NodeDef node = mEIndexObject.getNode(rel.getName());
        return dbTypeMap(node.createFieldTypeSizes());
    }


    private String getRootName(RelationDef rel) {
        return rel.getName();
    }


    private ArrayList dbTypeMap(ArrayList values) {
        ArrayList ret = new ArrayList();
        String dbTypeID = mEIndexObject.getDataBase();
        if (dbTypeID.equalsIgnoreCase(ORACLE_DB_TYPE) == true) {
            for (int i = 0; i < values.size(); i++) {
                String type = (String) values.get(i);
    
                if (type.startsWith("String")) {
                    String s = type.substring(6);
                    ret.add("VARCHAR2(" + s + ")");
                } else if (type.equals("Byte") || 
                		   type.equals("Character")) {
                    ret.add("CHAR");
                } else if (type.equals("Boolean")) {
                    ret.add("SMALLINT");
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
                    ret.add("smallint");
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
        }   // TO DO:  Implement mappings for DB2 and Sybase
        return ret;
    }
}
