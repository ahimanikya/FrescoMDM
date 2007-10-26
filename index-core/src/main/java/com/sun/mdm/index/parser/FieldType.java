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
package com.sun.mdm.index.parser;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


/**
 * @author gzheng
 * @version
 */
public class FieldType {
   
    /**
     * string
     */
    public static final String STRINGFIELD = "java.lang.String";
    /**
     * byte
     */
    public static final String BYTEFIELD = "byte";
    /**
     * double
     */
    public static final String DOUBLEFIELD = "double";
    /**
     * float
     */
    public static final String FLOATFIELD = "float";
    /**
     * long
     */
    public static final String LONGFIELD = "long";
    /**
     * java.util.Date
     */
    public static final String DATEFIELD = "java.util.Date";
    

    private final String mFmt = "        [elephantFieldType]";
    private final String mTagId = "id";
    private final String mTagFieldName = "field-name";
    private final String mTagMatchOrTrack = "matchFlag";
    private final String mTagDatabaseOracel = "database-oracle";
    private final String mTagDatabaseSQLServer = "database-sqlserver";
    private final String mTagDatabaseSybase = "database-sybase";
    private final String mTagConstraint = "constraint";
    private final String mTagMatch = "match";
    private OracleType eotDatabaseOracle = null;
    private SqlServerType esqlstDatabaseSQLServer = null;
    private SybaseType estDatabaseSybase = null;
    private ConstraintType ctConstraint = null;
    private MatchType mtMatch = null;
    private boolean bMatchFlag;
    private String strFieldName;

    private String strId;


    /**
     * @return String ret string
     */
    public String getFieldName() {
        return strFieldName;
    }


    /**
     * @return String ret string
     */
    public String getFieldSigma() {
        String strRet = null;

        if (null != mtMatch) {
            strRet = mtMatch.getStandardizationType();
        }

        return strRet;
    }


    /**
     * @return String ret string
     */
    public String getFieldType() {
        String strRet = null;
        if (null != eotDatabaseOracle) {
            strRet = eotDatabaseOracle.getColumnType();
        } else if (null != estDatabaseSybase) {
            strRet = estDatabaseSybase.getColumnType();
        } else if (null != esqlstDatabaseSQLServer) {
            strRet = esqlstDatabaseSQLServer.getColumnType();
        }

        return strRet;
    }


    /**
     * @return boolean ret boolean flag
     */
    public boolean getMatchFlag() {
        return bMatchFlag;
    }


    /**
     * @param node node
     */
    void parse(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagId.equals(((Element) nl.item(i)).getTagName())) {
                        strId = Utils.getStrElementValue(nl.item(i));
                    } else if (mTagFieldName.equals(((Element) nl.item(i)).getTagName())) {
                        strFieldName = Utils.getStrElementValue(nl.item(i));
                    } else if (mTagMatchOrTrack.equals(((Element) nl.item(i)).getTagName())) {
                        if (Utils.getStrElementValue(nl.item(i)).equals("true")) {
                            bMatchFlag = true;
                        } else {
                            bMatchFlag = false;
                        }
                    } else if (mTagDatabaseOracel.equals(((Element) nl.item(i)).getTagName())) {
                        eotDatabaseOracle = new OracleType();
                        eotDatabaseOracle.parse(nl.item(i));
                    } else if (mTagDatabaseSQLServer.equals(((Element) nl.item(i)).getTagName())) {
                        esqlstDatabaseSQLServer = new SqlServerType();
                        esqlstDatabaseSQLServer.parse(nl.item(i));
                    } else if (mTagDatabaseSybase.equals(((Element) nl.item(i)).getTagName())) {
                        estDatabaseSybase = new SybaseType();
                        estDatabaseSybase.parse(nl.item(i));
                    } else if (mTagConstraint.equals(((Element) nl.item(i)).getTagName())) {
                        ctConstraint = new ConstraintType();
                        ctConstraint.parse(nl.item(i));
                    } else if (mTagMatch.equals(((Element) nl.item(i)).getTagName())) {
                        mtMatch = new MatchType();
                        mtMatch.parse(nl.item(i));
                    }
                }
            }
        }
    }
}
