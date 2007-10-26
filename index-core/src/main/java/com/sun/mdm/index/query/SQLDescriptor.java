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

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import com.sun.mdm.index.objects.metadata.MetaDataService;
import com.sun.mdm.index.util.LogUtil;

/**
 * This class contains data that is used to issue a SQL query and to assemble
 * composite object tree. So a QueryParser after parsing the QueryObject,
 * converts it into SQLDescriptors (array of SQLDescriptors, if it requires
 * multiple SQL queries) and pass SQLDescritor back to QueryManager. So the data
 * contained in this class is: SQL statements. key indices of all the objects in
 * the SQL statement. Attribute indices of the objects that will be selected
 * from this query. Parent key indices of an object.
 *
 * @author sdua
 */
 class SQLDescriptor implements java.io.Serializable {
    
    private HashMap mobjectAttributes = new HashMap();
    private HashMap mobjectPKeys = new HashMap();
    
   
    
    private String mroot;
    private String msql;


    /**
     * Creates a new instance of SQLDescriptor
     */
     SQLDescriptor() {
    }


    /**
     *  Getter for SQL statement of the SQLDescriptor object
     *  @return SQL string. This could be either a prepared SQL string
     *  or a executable SQL string.
     */
     String getSQL() {
        return msql;
    }


   
   

    /** 
        String form of the object
     * @return String
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("SQLDescriptor: root: ").append(mroot).append('\n');
        buf.append("SQL:\n");
        buf.append(LogUtil.wrapString(msql));
        buf.append("\nObjectAttributes HashMap:\n");
        buf.append(LogUtil.mapToString(mobjectAttributes));
        buf.append("ObjectKeys HashMap:\n");
        buf.append(LogUtil.mapToString(mobjectPKeys));

        return buf.toString();
    }


    String[] getAttributes(String objectName) {
        AttrKeyMeta attrKeyMeta = (AttrKeyMeta) mobjectAttributes.get(objectName);

        return attrKeyMeta.getAttributes();
    }

 


    Integer[] getKeyIndices(String object) {
        // if an object has its data selected, then it is stored in mobjectAttributes
        // HashMap. If it has only its keys selected, then it is stored in
        // mobjectPKeys table
        AttrKeyMeta attrKeyMeta = (AttrKeyMeta) mobjectAttributes.get(object);

        if (attrKeyMeta == null) {
            attrKeyMeta = (AttrKeyMeta) mobjectPKeys.get(object);
        }

        if (attrKeyMeta != null) {
            return attrKeyMeta.mkeyIndices;
        }

        return null;
    }


    String getRoot() {
        return mroot;
    }


    void setParentKeyData(String objectName, Integer[] keyIndices) {
        AttrKeyMeta attrKeyMeta = (AttrKeyMeta) mobjectAttributes.get(objectName);
        attrKeyMeta.setParentIndices(keyIndices);
    }


    void setRoot(String root) {
        mroot = root;
    }


    void setSQL(String sql) {
        msql = sql;
    }


    void addObjectKeyData(String objectName, Integer[] mkeyIndices) {
        AttrKeyMeta attrKeyMeta = new AttrKeyMeta(objectName, mkeyIndices);
        mobjectPKeys.put(objectName, attrKeyMeta);
    }


    void addObjectSelectData(String objectName, int attrIndexLow,
            int attrIndexHigh, Integer[] mkeyIndices, ArrayList fields,
            ArrayList extraKeyColumns) {
        int extraSize = (extraKeyColumns != null) ? extraKeyColumns.size() : 0;
        int size = fields.size() - extraSize;
        String[] attributes = new String[size];

        for (int i = 0; i < size; i++) {
            attributes[i] = (String) fields.get(i);
        }

        //String[] attributes = new String(String[]) fields.toArray(new String[fields.size());
        AttrKeyMeta attrKeyMeta = new AttrKeyMeta(objectName, attrIndexLow,
                attrIndexHigh, mkeyIndices, attributes);
        mobjectAttributes.put(objectName, attrKeyMeta);
    }


    boolean contains(String object) {
        return mobjectAttributes.containsKey(object);
    }


    void copyParentKeyToSelectData() {
        Set objectSet = mobjectAttributes.keySet();
        Iterator it = objectSet.iterator();

        while (it.hasNext()) {
            String object = (String) it.next();
            String parent = MetaDataService.getParentTypePath(object);

            if (object.equals(mroot) || mobjectAttributes.containsKey(parent)) {
                continue;
            } else {
                AttrKeyMeta parentKeyMeta = (AttrKeyMeta) mobjectPKeys.get(parent);
                AttrKeyMeta childKeyMeta = (AttrKeyMeta) mobjectAttributes.get(object);
                childKeyMeta.setParentIndices(parentKeyMeta);
            }
        }
    }


    void copyTo(CreateObjectMeta cObjMeta) {
        String object = cObjMeta.getObjName();
        AttrKeyMeta keyMeta = (AttrKeyMeta) mobjectAttributes.get(object);
        cObjMeta.setAttrIndexLow(keyMeta.mattrIndexLow);
        cObjMeta.setAttrIndexHigh(keyMeta.mattrIndexHigh);
        cObjMeta.setKeyIndices(keyMeta.mkeyIndices);
        cObjMeta.setParentKeyIndices(keyMeta.mparentKeyIndices);
        cObjMeta.setAttributes(keyMeta.mattributes);
    }


    private class AttrKeyMeta implements java.io.Serializable {
        int mattrIndexHigh;
        int mattrIndexLow;
        Integer[] mkeyIndices;
        String mobjName;
        Integer[] mparentKeyIndices;
        private String[] mattributes;


        AttrKeyMeta(String objName, int attrIndexLow, int attrIndexHigh,
                Integer[] keyIndices, String[] attributes) {
            mobjName = objName;
            mattrIndexLow = attrIndexLow;
            mattrIndexHigh = attrIndexHigh;
            mkeyIndices = keyIndices;
            mattributes = attributes;
        }


        AttrKeyMeta(String objName, Integer[] keyIndices) {
            mobjName = objName;
            mkeyIndices = keyIndices;

          
        }


        /**
         *  
         */
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("  [ SQLDescriptor AttKeyMetaData:: \n");
            sb.append("  ObjectName:" + mobjName);

            sb.append("\n  attIndexLow:").append(mattrIndexLow);
            sb.append("\n  attIndexHigh:").append(mattrIndexHigh);

            if (mkeyIndices != null) {
                sb.append("\n  KeyIndices:");

                for (int i = 0; i < mkeyIndices.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }

                    sb.append(mkeyIndices[i].toString());
                }
            }

            if (mparentKeyIndices != null) {
                sb.append("\n  ParentKeyIndices:");

                for (int i = 0; i < mparentKeyIndices.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }

                    sb.append(mparentKeyIndices[i].toString());
                }
            }

            if (mattributes != null) {
                sb.append("\n  Attributes:");

                for (int i = 0; i < mattributes.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }

                    sb.append(mattributes[i].toString());
                }
            }

            sb.append(" ] ");

            return sb.toString();
        }


        String[] getAttributes() {
            return mattributes;
        }


        void setParentIndices(Integer[] keyIndices) {
            mparentKeyIndices = keyIndices;
        }


        void setParentIndices(AttrKeyMeta parentKeyMeta) {
            mparentKeyIndices = parentKeyMeta.mkeyIndices;
        }
    }
}
