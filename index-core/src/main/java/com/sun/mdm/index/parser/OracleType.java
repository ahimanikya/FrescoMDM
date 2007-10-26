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
public class OracleType {
    private final String mFmt = "            [elephantOracleType]";
    private final String mTagColumn = "column";
    private final String mTagDataType = "columntype";
    private final String mTagSize = "size";
    private final String mTagNulls = "nulls";
    private boolean bNulls;

    private String strColumn;
    private String strDataSize;
    private String strDataType;


    /**
     * @return String string
     */
    public String getColumnName() {
        return strColumn;
    }


    /**
     * @return String string
     */
    public String getColumnType() {
        String strRet = null;
        if (strDataType.equals("varchar2") 
                || strDataType.equals("blob") 
                || strDataType.equals("clob") 
                || strDataType.equals("rowid") 
                || strDataType.equals("nvarchar2")) {
            strRet = FieldType.STRINGFIELD;
        }

        if (strDataType.equals("number") 
                || strDataType.equals("double")) {
            strRet = FieldType.DOUBLEFIELD;
        }

        if (strDataType.equals("char") 
                || strDataType.equals("nchar")) {
            strRet = FieldType.BYTEFIELD;
        }

        if (strDataType.equals("long")) {
            strRet = FieldType.LONGFIELD;
        }

        if (strDataType.equals("float")) {
            strRet = FieldType.FLOATFIELD;
        }

        if (strDataType.equals("date")) {
            strRet = FieldType.DATEFIELD;
        }

        return strRet;
    }


    /**
     * default constructor
     */
    public OracleType() {
    }


    /**
     * @param node Node
     */
    void parse(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagColumn.equals(((Element) nl.item(i)).getTagName())) {
                        strColumn = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagDataType.equals(((Element) nl.item(i)).getTagName())) {
                        strDataType = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagSize.equals(((Element) nl.item(i)).getTagName())) {
                        strDataSize = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagNulls.equals(((Element) nl.item(i)).getTagName())) {
                        if (Utils.getStrElementValue(nl.item(i)).equals("yes")) {
                            bNulls = true;
                        } else {
                            bNulls = false;
                        }
                    }
                }
            }
        }
    }

}
