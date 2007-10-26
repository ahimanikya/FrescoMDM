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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import com.sun.mdm.index.objects.metadata.MetaDataService;


/**
 * This class contains a map of Object names and all the fields to be selected
 * in the query. When this object is created, it will also add the primary key
 * to the map if not already selected in the selectFields list. So it guarantees
 * that a key field will always be retrieved in the query and that helps in
 * Object Assembling later in the Assembling process. Currently this Map is used
 * only by MultiQueryParser (for multiple queries).
 *
 * @author sdua
 */
class SelectMap {
    
    private HashMap mselectMap = new HashMap();


    /**
     * Creates a new instance of SelectMap
     *
     * @param selectObjects The different objects that needs to be retrieved.
     * @param selectFields The fields corresponding to selectObjects.
     */
    SelectMap(HashSet selectObjects, QualifiedField[] selectFields) {
        QPathSelectFields qSelectList;

        // first iterate through the select Fields and add them to the selectMap
        // mapped through the object.
        for (int i = 0; i < selectFields.length; i++) {
            String object = selectFields[i].mobject;
            String field = selectFields[i].mfield;
            qSelectList = (QPathSelectFields) mselectMap.get(object);

            if (qSelectList == null) {
                String[] key = MetaDataService.getDBTablePK(object);
                qSelectList = new QPathSelectFields(field, object, key);

                mselectMap.put(object, qSelectList);
            } else {
                qSelectList.add(field);
            }
        }

        
        Iterator it = selectObjects.iterator();

        // iterate through all the objects and if their key fields are not already
        // specified in the selectFields, then add them to the mselectMap.
        // So in effect we will always query for the key field, even if user
        // does not specify in his selectFields.
        while (it.hasNext()) {
            String object = (String) it.next();
            qSelectList = (QPathSelectFields) mselectMap.get(object);

            if (qSelectList != null) {
                qSelectList.setKey();
            } else {
                // so we have object which is not specified in the selectField,

                // but whose keyfields needs to be retrieved.
                String[] key = MetaDataService.getDBTablePK(object);
                qSelectList = new QPathSelectFields(object, key);
                qSelectList.setKey();
                mselectMap.put(object, qSelectList);
            }
        }
    }


    // these are the columns that are not selected in the selectfields, but
    // are part of keyfield and so need to be retrived in the SQL.
    ArrayList getExtraKeyColumns(String object) {
        QPathSelectFields qSelectFields = (QPathSelectFields) mselectMap.get(object);

        return qSelectFields.mextraKeyColumns;
    }


    ArrayList getFields(String object) {
        QPathSelectFields qSelectFields = (QPathSelectFields) mselectMap.get(object);

        return qSelectFields.mselectFields;
    }


    ArrayList getKeyFields(String object) {
        QPathSelectFields qSelectFields = (QPathSelectFields) mselectMap.get(object);

        return qSelectFields.mkeyFields;
    }


    ArrayList getKeyIndices(String object) {
        QPathSelectFields qSelectFields = (QPathSelectFields) mselectMap.get(object);

        return qSelectFields.mkeyIndices;
    }


    QPath getQPath(String object) {
        QPathSelectFields qfields = (QPathSelectFields) mselectMap.get(object);

        return qfields.mqpath;
    }


    // sets the QPath for each object. So this identifies which object is retreived
    // by which QPath.
    void setQPath(String object, QPath qpath) {
        QPathSelectFields qfields = (QPathSelectFields) mselectMap.get(object);
        qfields.mqpath = qpath;
    }


    /*
     *  Maintains data structure about the fields that are used in the SQL query.
     */
    private class QPathSelectFields {
        // say selectfields are firstName, lastName, personid
        ArrayList mselectFields = new ArrayList();

        // those keyfields which are not selected in selectfields.
        ArrayList mextraKeyColumns = new ArrayList();

        // say personid
        ArrayList mkeyFields = new ArrayList();

        // indices of key fields in this object.
        // Example personid index: {2}. This is later used during SQL statement execution
        // to identify the keyfields.
        ArrayList mkeyIndices = new ArrayList();

        // the QPath these select fields belong to
        QPath mqpath;

        //private String[] mkey;
        // same as mkeyFields. @todo May be not needed. Need to revisit it later.
        private String[] mkeyNames;
        private String mobject;

        // objectkeys are required for creation of an object.
        private String[] mobjectKeys;


       
        QPathSelectFields(String field, String object, String[] key) {
            mkeyNames = key;
            mobject = object;
            add(field);
            mobjectKeys = MetaDataService.getObjectKeys(object);

        
        }


        QPathSelectFields(String object, String[] key) {
            mkeyNames = key;
            mobject = object;
            mobjectKeys = MetaDataService.getObjectKeys(object);
        }


        /*
              adds the keyfield names to the mselectFields if it is already not in
              the mselectFields.
          */
        void setKey() {
            // Object attributes will be always retrieved from the database and
            // these attributes will be passed to ValueObjectAssembler.
            // ObjectKey is useful to bind parent child objects.
            for (int i = 0; (mobjectKeys != null) && (i < mobjectKeys.length);
                    i++) {
                String objKey = mobjectKeys[i];

                if (!mselectFields.contains(objKey)) {
                    add(objKey);
                }
            }

            // Primary key attributes will also be always retrieved from the database
            // but if they are already not in selectfields, these attributes are
            // not passed to ValueObjectAssembler. PK is useful to deteremine parent
            // child relationshipds by AssemblerEngine.
            for (int i = 0; i < mkeyNames.length; i++) {
                String keyName = (String) mkeyNames[i];

                if (containsIgnoreCase(keyName)) {
                    continue;
                }

                // so the keyName has not been selected in the selectFields, so add it.
                mselectFields.add(keyName);
                mextraKeyColumns.add(keyName);
                mkeyIndices.add(new Integer(mselectFields.size() - 1));
                mkeyFields.add(keyName);
            }
        }


        /**
         * adds field to the list. If this field, contains a Keyfield, then also
         * update mkeyIndices and mkeyFields.
         *
         * @param field
         */
        void add(String field) {
            mselectFields.add(field);

            if (containsKeyField(field)) {
                mkeyIndices.add(new Integer(mselectFields.size() - 1));
                mkeyFields.add(field);
            }
        }


        private boolean containsIgnoreCase(String str) {
            for (int i = 0; i < mselectFields.size(); i++) {
                if (str.equalsIgnoreCase((String) mselectFields.get(i))) {
                    return true;
                }
            }

            return false;
        }


        private boolean containsKeyField(String field) {
            for (int i = 0; i < mkeyNames.length; i++) {
                if (field.equalsIgnoreCase(mkeyNames[i])) {
                    return true;
                }
            }

            return false;
        }
    }
}
