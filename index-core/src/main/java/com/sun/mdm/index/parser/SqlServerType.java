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
public class SqlServerType {
    private final String mFmt = "            [elephantSQLServerType]";
    private final String mTagFieldName = "databaseFieldName";
    private final String mTagDataType = "columntype";
    private final String mTagDataSize = "data-size";
    private final String mTagRequired = "required";
    private boolean bRequired;
    private String strDataSize;
    private String strDataType;
    private String strDatabaseFieldName;


    /**
     * @return String string
     */
    public String getColumnName() {
        return strDatabaseFieldName;
    }


    /**
     * @return String string
     */
    public String getColumnType() {
        String strRet = null;
        if (strDataType.equals("varchar")) {
            strRet = FieldType.STRINGFIELD;
        }

        if (strDataType.equals("number")) {
            strRet = FieldType.DOUBLEFIELD;
        }

        if (strDataType.equals("char")) {
            strRet = FieldType.BYTEFIELD;
        }

        if (strDataType.equals("date")) {
            strRet = FieldType.DATEFIELD;
        }

        return strRet;
    }


    /**
     * default constructor
     */
    public SqlServerType() {
    }


    /**
     * @param node node
     */
    void parse(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagFieldName.equals(((Element) nl.item(i)).getTagName())) {
                        strDatabaseFieldName = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagDataType.equals(((Element) nl.item(i)).getTagName())) {
                        strDataType = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagDataSize.equals(((Element) nl.item(i)).getTagName())) {
                        strDataSize = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagRequired.equals(((Element) nl.item(i)).getTagName())) {
                        if ("yes" == Utils.getStrElementValue(nl.item(i))) {
                            bRequired = true;
                        } else {
                            bRequired = false;
                        }
                    }
                }
            }
        }
    }

}
