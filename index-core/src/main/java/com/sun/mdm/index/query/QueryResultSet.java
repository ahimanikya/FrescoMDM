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

import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * This is the core class reponsible for object assembling. This iterates
 * through JDBC ResultSet and invoke ResultObjectAssembler methods. Each of this
 * object is holding a different instance of JDBC ResultSet. For each invocation
 * of next(), it iterates through JDBC ResultSet until it finds the next root.
 *
 * @author sdua
 */
class QueryResultSet {
    
    private boolean mcreateRootFlag = false;
    private boolean mbegin = false;
    private boolean mmoreRows = true;

    /*
          List of CurrentObject which holds the hold information about the object
          being constructed corresponding to JDBC ResultSet Attributes.
      */
    private CurrentObject[] mcurrentObjectList;
    private ResultSet mres;
    private Object mroot;
    private Integer[] mrootKeyIndices;
    private Object[] mrootKeyValues;
    private int curRowNum;
    private int maxRows = -1;
    private boolean mRootPartial = false;

    /*
          ResultObjectAssembler instance that creates custom value objects.
      */
    private ResultObjectAssembler mvalueObjectFactory;


    /**
     * Creates a new instance of QueryResultSet
     *
     * @param res
     * @param createObjectList
     * @param ResultObjectAssembler
     * @param createRootFlag
     * @param assObjectStates
     */
    QueryResultSet(ResultSet res, CreateObjectMeta[] createObjectList,
            ResultObjectAssembler valueObjectFactory,
            AssembleObjectState[] assObjectStates, boolean createRootFlag, int maxrows) {
        mres = res;
        mcreateRootFlag = createRootFlag;
        mvalueObjectFactory = valueObjectFactory;
        createCurrentObjectList(createObjectList, assObjectStates, mres);
        maxRows = maxrows;
    }


    /**
     *
     */
    public String toString() {
        String buf = "";

        for (int i = 0; i < mcurrentObjectList.length; i++) {
            CurrentObject curObject = mcurrentObjectList[i];
            buf += curObject.toString();
        }

        return buf;
    }


    final Object[] getRootKeyValues() {
        return mrootKeyValues;
    }


    final void setRoot(Object root, Object[] rootKeyValues) {
        mroot = root;
        mrootKeyValues = rootKeyValues;

        CurrentObject curObject = mcurrentObjectList[0];

        if (curObject.getParentObjState() == null) {
            curObject.setParent(mroot);
        }
    }


    final void close()
        throws SQLException {
        mres.close();
    }


    final void createCurrentObjectList(CreateObjectMeta[] createObjectList,
            AssembleObjectState[] assObjectStates, ResultSet res) {
        mcurrentObjectList = new CurrentObject[createObjectList.length];

        for (int i = 0; i < mcurrentObjectList.length; i++) {
            mcurrentObjectList[i] = new CurrentObject(createObjectList[i],
                    mcurrentObjectList, assObjectStates, res, i);

            if (i == 0) {
                mrootKeyIndices = createObjectList[0].getParentKeyIndices();
            }

        }
    }


    final boolean equals(Object[] values1, Object[] values2) {
        if ((values1 == null) || (values2 == null)
                 || (values1.length != values2.length)) {
            return false;
        }

        for (int i = 0; i < values1.length; i++) {
            if (!values1[i].equals(values2[i])) {
                return false;
            }
        }

        return true;
    }



    final Object next()
        throws SQLException, VOAException {
    	
        if (!mbegin) {
            nextRow();
            mbegin = true;
        }
        
        if (!mmoreRows) {
             return null;
        }

        if (mcreateRootFlag) {
            createRoot();
        }

        int istart = 0;
        /*
         *  if we created root object in this QueryResultSet, then first element in
         * currentObjectList contains root object, which we created above. So we start creating 
         * children from 2nd element in this array. If this object does not create
         * root object, then we start from the 1st element.
         */
        istart = mcreateRootFlag ? 1 : 0;

        /*
         *  loop until the next JDBC ResultSet contains different root object
         *  otherwise the JDBC row contains same root object keyvalues but different children
         *  So keep adding children to the same root object within this loop
         */
        while (!isNextRoot()) {
        	
            for (int i = istart; i < mcurrentObjectList.length; i++) {
                CurrentObject curObject = mcurrentObjectList[i];

                String objName = curObject.getObjName();
                Object[] keyValues = curObject.getKeyValues(mres);

                /*
                      
                      
                      Note: Here we are converting JDBC linear rows to  value object tree. We invoke 
                      ResultObjectAssembler to convert JDBC rows to value objects. However we  invoke 
                      ResultObjectAssembler only if it is really different child than previously obtained.
                       (To determine different child, we
                      compare the keyvalues of this row with that previous row (that is saved in currentObjectList).
                      
                      if logic: if curObject has different key values then the just retrieved
                      keyvalues from JBDC then only we will create this object. 
                      Also if this is leaf object, then it does not has any children
                      so we always create this object.
                      
                  */
                if (checkNonEmpty(keyValues)
                         && (lastObject(i) || !curObject.equals(keyValues))) {
                    Object parent = getParent(curObject);
                    AttributesData attData = curObject.mattributesData;
                    // convert JDBC row to value object Node.
                    Object object = mvalueObjectFactory.createObjectAttributes(mroot,
                            parent, objName, attData);
                    // update currentObjectList with these set of new row values
                    curObject.setObject(object);
                    curObject.setKeyValues(keyValues); // update CurrentObjectList with this row keyvalues
                    curObject.setChild(keyValues);
                }
            }

            nextRow();
        }
        
        if (mRootPartial) {
      	  return null;
      	}

        return mroot;
    }


    private final Object getParent(CurrentObject curObject)
        throws SQLException {
        Object parent;

        if (curObject.getParent() != null) {
            parent = curObject.getParent();
        } else {
            //int parentIndex = curObject.getCreateObjectMeta().getParentIndex();
            Object[] parentKeyValues = curObject.getParentKeyValues(mres);
            AssembleObjectState assObjState = curObject.getParentObjState();
            parent = assObjState.getObject(parentKeyValues);
        }

        return parent;
    }


    private final Object[] getRootKeyValues(ResultSet res)
        throws SQLException {
        int length = mrootKeyIndices.length;
        Object[] values = new Object[length];

        for (int i = 0; i < length; i++) {
            int keyIndex = mrootKeyIndices[i].intValue();
            values[i] = res.getObject(keyIndex);
        }

        return values;
    }

    /*
     *  Is the next row fetched from database contains different keyvalues for the
     * root object. 
     * @return true if next row contains different root object else false.
     * 
     *  Note: Here we also determine if the rows retreived contain Partial object. We can get Partial object, only if
     * Select query contains oracle where clause as "rownum < maxRows ". If the number of rows obtained is 
     * less than maxRows, 
     * then we know database returned all the rows for the where condition. However if rows fetched is same as
     * maxRows (that contains rownum < maxRows clause), then it is possible that we did not receive all the children
     * for the primary object. So in this case we discard the last object, that might be partial.
     * Ex: JDBC where clause: rownum < 4
     *      Say Person is root object and Address is child. In JDBC Resultset we get Root and child objects
     *     as JDBC rows with root values  being repeated and different children values in different JDBC rows.
     *      JDBC result set:  Person1 Address1  
     *                        Person2 Address2
     *                        Person2 Address3
     *   So in this case, it is possible that if SQL did not contain rownum < 4 condition, then next row fetched might
     *  be Person2 Address4. So if we form Person2 and Address2, Address3 then we are getting partial root Person2 object.
     *   This problem may even magnify if there were multiple queries (Mulitpath as Person Address and Person Phone) 
     * and we could end up 
     * with multiple partial root objects with empty children. So to avoid this, in a condition where rownum == maxRows,
     * we deduce that it is partial root object (we did not fetch all its children). So in that case, 
     * we will eventually return null from next() for the root object and we will not create Person2 object.
     */

    private final boolean isNextRoot()
        throws SQLException {
        /* 
         * if we have reached end, and we have not set maxRows, then current object is retrieved. So next row contains
         * another root, which in this case is null
         */
    	if (!mmoreRows  && maxRows == -1) {
            return true;
            /*
             *  if we have reached end, and total number of rows retrieved < maxRows, so again there is no more data afterwards. 
             * Which means current object is complete and next object is null.
             */
        } else if (!mmoreRows  && curRowNum < maxRows) {
            return true;
            /*
             *  if we have reached end, and total number of rows retrieved >= maxRows, so again there is no more data afterwards. 
             *  But in this case we may not have retrieve all rows (children) for current root, since query is limited to maxRows, so
             * current objroect retrieved could be partial and we need to discard this partial root.
             */
        } else if (!mmoreRows  && curRowNum >= maxRows) {
        	mRootPartial = true;
            return true;
        }
        

        CurrentObject curObject = mcurrentObjectList[0];
        Object[] keyValues;

        if (mcreateRootFlag) {
            //String objName = curObject.mobjName;
            keyValues = curObject.getKeyValues(mres);

            if (curObject.equals(keyValues)) {
                return false;
            }
        } else {
            keyValues = getRootKeyValues(mres);
            if (equals(mrootKeyValues, keyValues)) {
                return false;
            }
        }
        
        return true;
    }


    /// ========  private/package methods ==================================
    private final void createRoot()
        throws SQLException, VOAException {
        CurrentObject curObject = mcurrentObjectList[0];
        String objName = curObject.mobjName;
        Object[] keyValues = curObject.getKeyValues(mres);
        mrootKeyValues = keyValues;

        AttributesData attData = curObject.mattributesData;
        mroot = mvalueObjectFactory.createRoot(objName, attData);
        curObject.mobject = mroot;
        curObject.mkeyValues = keyValues;
        curObject.setChild(keyValues);
    }


    private final boolean lastObject(int index) {
        return (index == (mcurrentObjectList.length - 1));
    }


    private final boolean nextRow()
        throws SQLException {
        mmoreRows = mres.next();
        if (mmoreRows) {
           curRowNum++;
        }

        return mmoreRows;
    }
    
    private final boolean checkNonEmpty(Object[] keyValues) {
        if (keyValues == null) {
            return false;
        }
        for (int i = 0; i < keyValues.length; i++) {
            if (keyValues[i] == null) {
                return false;
            }
        }
        return true;
        
    }


    /**
     * This holds the information about one object. Some of the information is
     * fixed and copied from CretaeObjectMeta like attribute indices, primary
     * key indices. Some other attributes like current object being constructed
     * by ResultObjectAssembler are dynamic.
     */
    private class CurrentObject {
        private final int mattrIndexHigh;
        private final int mattrIndexLow;
        private final boolean misLeaf;
        private final int mkeySize;
        private final int mparentKeySize;

        // This is initialized during creation.
        private AssembleObjectState massObjState;
        private AttributesData mattributesData;
        private int mchildIndex;
        private CreateObjectMeta mcreateObjMeta;
        private CurrentObject[] mcurrentObjectList;
        private Integer[] mkeyIndices;

        // following data will change during retrival of data from JDBC.
        private Object[] mkeyValues;
        // following will be copied from CreateObjectMeta.
        private String mobjName;
        private Object mobject;
        private Object mparent;
        private Integer[] mparentKeyIndices;
        private AssembleObjectState mparentObjState;


        CurrentObject(CreateObjectMeta createObjMeta,
                CurrentObject[] currentObjectList,
                AssembleObjectState[] assObjectStates, ResultSet res, int index) {
            mcreateObjMeta = createObjMeta;
            mobjName = createObjMeta.getObjName();
            mattrIndexLow = createObjMeta.getAttrIndexLow();
            mattrIndexHigh = createObjMeta.getAttrIndexHigh();
            mkeyIndices = createObjMeta.getKeyIndices();
            mparentKeyIndices = createObjMeta.getParentKeyIndices();
            mchildIndex = createObjMeta.getChildIndex();
            mcurrentObjectList = currentObjectList;
            mkeySize = mkeyIndices.length;
            mparentKeySize = (mparentKeyIndices != null)
                     ? mparentKeyIndices.length : 0;
            misLeaf = (index == (mcurrentObjectList.length - 1));
            mattributesData = new AttributesData(res,
                    createObjMeta.getAttributes(), mattrIndexLow - 1);
            mchildIndex = createObjMeta.getChildIndex();

            for (int i = 0; i < assObjectStates.length; i++) {
                AssembleObjectState assObjState = createObjMeta.getAssembleObjectState();
                AssembleObjectState parentObjState = createObjMeta.getParentObjectState();
                String objName = assObjectStates[i].getObjName();

                if ((assObjState != null) 
                        && objName.equals(assObjState.getObjName())) {
                    massObjState = assObjectStates[i];
                }

                if ((parentObjState != null) 
                       && objName.equals(parentObjState.getObjName())) {
                    mparentObjState = assObjectStates[i];
                }
            }
        }


        /**
         * @todo Document this method
         */
        public String toString() {
            String buf;
            buf = "Object Name:" + mobjName;

            buf += (" attrIndexLow:" + mattrIndexLow);
            buf += ("attrIndexHigh:" + mattrIndexHigh);
            buf += ("keyIndices:" + mkeyIndices);
            buf += ("parentkeyIndices:" + mparentKeyIndices);
            buf += ("isLeaf:" + misLeaf);
            buf += ("childIndex:" + mchildIndex);
            buf += ("assembleObjectState:" + massObjState);
            buf += ("parentObjectState:" + mparentObjState);

            return buf;
        }


        final Object[] getKeyValues(ResultSet res)
            throws SQLException {
            Object[] values = new Object[mkeySize];

            for (int i = 0; i < mkeySize; i++) {
                int keyIndex = mkeyIndices[i].intValue();
                values[i] = res.getObject(keyIndex);
            }

            return values;
        }


        final String getObjName() {
            return mobjName;
        }


        final Object getParent() {
            return mparent;
        }


        final Object[] getParentKeyValues(ResultSet res)
            throws SQLException {
            Object[] values = new Object[mparentKeySize];

            for (int i = 0; i < mparentKeySize; i++) {
                int keyIndex = mparentKeyIndices[i].intValue();
                values[i] = res.getObject(keyIndex);
            }

            return values;
        }


        final AssembleObjectState getParentObjState() {
            return mparentObjState;
        }


        final void setChild(Object[] keyValues) {
            if (misLeaf) {
                return;
            }

            if (mchildIndex >= 0) {
                mcurrentObjectList[mchildIndex].mparent = mobject;
            }

            if (massObjState != null) {
                massObjState.add(mobject, keyValues);
            }
        }


        final void setKeyValues(ResultSet res)
            throws SQLException {
            for (int i = 0; i < mkeySize; i++) {
                int keyIndex = mkeyIndices[i].intValue();
                mkeyValues[i] = res.getObject(keyIndex);
            }
        }


        final void setKeyValues(Object[] keyValues) {
            mkeyValues = keyValues;
        }


        final void setObject(Object object) {
            mobject = object;
        }


        final void setParent(Object parent) {
            mparent = parent;
        }


        final boolean equals(Object[] values) {
            if ((mkeyValues == null) || (values.length != mkeyValues.length)) {
                return false;
            }

            for (int i = 0; i < values.length; i++) {
                if (!values[i].equals(mkeyValues[i])) {
                    return false;
                }
            }

            return true;
        }
    }
}
