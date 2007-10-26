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
package com.sun.mdm.index.query;

import java.util.ArrayList;

import com.sun.mdm.index.objects.metadata.MetaDataService;


/**
 * This is used by MultiQueryParser. This class basically defines the creating
 * of Select fields in the target SQL statement.
 *
 * @author sdua
 */
class MultiQPath extends QPath {
    
    /**
     * Creates a new instance of MultiQPath
     */
    MultiQPath() {
    }


    QPath cloneLessLeaf() {
        QPath qpath = new MultiQPath();

        for (int i = 0; i < (size() - 1); i++) {
            qpath.add(getObject(i));
        }

        return qpath;
    }


    //===================================================================
    StringBuffer createSelectFields(SQLDescriptor sqlDesc, SelectMap selectMap) {
        StringBuffer sql = new StringBuffer();
        int attIndexLow = 0;
        int attIndexHigh = 0;
        StringBuffer selectbuf = new StringBuffer();
        boolean first = true;

        for (int i = 0; i < size(); i++) {
            String object = getObject(i);
            String table = MetaDataService.getDBTableName(object);
            QPath qpath = selectMap.getQPath(object);

            // so this QPath object attributes have not been selected in any other SQL statement.
            // so we select an object attributes in only one SQL statement, even though it may appear
            // in several QPaths.
            if ((qpath == null) || (qpath == this)) {
                ArrayList fields = (ArrayList) selectMap.getFields(object);
                ArrayList extraKeyColumns = (ArrayList) selectMap.getExtraKeyColumns(object);
                ArrayList columns = new ArrayList();
                selectMap.setQPath(object, this);
                attIndexLow = attIndexHigh + 1;

                for (int j = 0; j < fields.size(); j++) {
                    if (!first) {
                        selectbuf.append(" , ");
                    } else {
                        first = false;
                    }

                    selectbuf.append(table);

                    // use DBMetadataservice
                    selectbuf.append(".");

                    String column = null;

                    if (!extraKeyColumns.contains(fields.get(j))) {
                        column = MetaDataService.getColumnName(object + "." 
                                + (String) fields.get(j));
                    } else {
                        column = (String) fields.get(j);
                    }

                    selectbuf.append(column);
                }

                attIndexHigh = (attIndexLow + fields.size()) - 1;

                ArrayList keyArrIndices = (ArrayList) selectMap.getKeyIndices(object);
                Integer[] keyIndices = new Integer[keyArrIndices.size()];

                // now the real keyIndices have changed as per the SQL statement
                for (int j = 0; j < keyIndices.length; j++) {
                    keyIndices[j] = new Integer(((Integer) keyArrIndices.get(j)).intValue() 
                           + attIndexLow);
                }

                sqlDesc.addObjectSelectData(object, attIndexLow, attIndexHigh,
                        keyIndices, fields, extraKeyColumns);
            } else {
                ArrayList keyFields = (ArrayList) selectMap.getKeyFields(object);
                ArrayList extraKeyColumns = (ArrayList) selectMap.getExtraKeyColumns(object);
                attIndexLow = attIndexHigh + 1;

                Integer[] keyIndices = new Integer[keyFields.size()];

                for (int j = 0; j < keyFields.size(); j++) {
                    String keyField = (String) keyFields.get(j);

                    if (!first) {
                        selectbuf.append(" , ");
                    } else {
                        first = false;
                    }

                    selectbuf.append(table);
                    selectbuf.append(".");
                    
                    String column = null;
                    if (!extraKeyColumns.contains(keyField)) {
                        column = MetaDataService.getColumnName(object + "." 
                                + keyField);
                    } else {
                        column = keyField;
                    }

      
                    selectbuf.append(column);
                    keyIndices[j] = new Integer(attIndexLow + j);
                }

                attIndexHigh = (attIndexLow + keyFields.size()) - 1;
                sqlDesc.addObjectKeyData(object, keyIndices);
            }
        }

        sqlDesc.copyParentKeyToSelectData();

        return selectbuf;
    }
}
