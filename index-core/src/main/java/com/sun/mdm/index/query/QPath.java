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
import com.sun.mdm.index.objects.metadata.MetaDataService;
import com.sun.mdm.index.objects.metadata.ObjectFactory;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.sun.mdm.index.util.ConnectionUtil;

/**
 * This object represents object names from root to a leaf. So if the object
 * tree is of form System Person Address Phone Alias, the QPaths would be {
 * System, Person, Address }, { System, Person, Phone }, { System, Person, Alias
 * }
 *
 * @author sdua
 */
class QPath {

    // mobjects contains all the object names in this path. The root is
    // the first object and the leaf is the last object in this list.
    private ArrayList mobjects = new ArrayList();
    private int msize = mobjects.size();
    private List mfieldPrepareIndex;
    //This Node name represents the lowest level of all subpaths which intersects
    // this QPath. This is used in determining outer joins. The [] represents
    // each such node for each Condition[] to be used in union.
    private String[] mlowestSubpathIntersectNode;

    //  // A QPath has many subpaths. Each subpath contains condition specified
    // in Condition[]. The [] signifies each subpath list for each Condition[]
    // in the Condition[][]. Note in Condition[][], inner array is used in the
    // union operation.
    private ArrayList[] msubpaths;
    private Condition[] mConditions;
    private int mPrepareIndex = 1;
    private static Pattern blankPattern = Pattern.compile("[\\s]*");

    /**
     * Creates a new instance of QPath
     */
    public QPath() {
    }

    /**
     * String representation of QPath
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();

        buf.append(" Object Names: ");
        buf.append(mobjects.toString());

        for (int i = 0; (msubpaths != null) && (i < msubpaths.length); i++) {
            ArrayList subpathList = (ArrayList) msubpaths[i];

            if (i > 0) {
                buf.append("  Union  subpath:== ");
            } else {
                buf.append("  subpath:== ");
            }

            for (int j = 0; j < subpathList.size(); j++) {
                SubQPath subpath = (SubQPath) subpathList.get(j);
                buf.append((String) subpath.toString());
                buf.append(" ; ");
            }
        }

        return buf.toString();
    }

    int getIndex(String object) {
        return mobjects.indexOf(object);
    }

    String getLeaf() {
        return (String) mobjects.get(msize - 1);
    }

    String getObject(int i) {
        return (String) mobjects.get(i);
    }

    String getRoot() {
        return (String) mobjects.get(0);
    }


    // <TODO> 
    List getFieldPrepareIndex() {
        return mfieldPrepareIndex;
    }

    void add(String object) {
        mobjects.add(object);
        msize = mobjects.size();
    }


    // clones this QPath object. In the cloning process, the leaf object is
    // omitted.
    QPath cloneLessLeaf() {
        QPath qpath = new QPath();

        for (int i = 0; i < (mobjects.size() - 1); i++) {
            qpath.add((String) mobjects.get(i));
        }

        return qpath;
    }

    boolean contains(String object) {
        return mobjects.contains(object);
    }


    /*
    create condition string. Condition string is any condition on the objects
    in QPath plus a subquery of msubpaths.
    Say if QPath is { System Person Address }, subPath { Person, Phone}
    and condition is Person.firstName = 'swaranjit' and Phone.area = '626'.
    Then the condition string returned by this method would be:
    " Person.firstName = 'swaranjit'
    and personid in
    ( Select personid from person, phone
    where person.personid = phone.personid
    and phone.area = '626' ) "
     */
    StringBuffer createConditions(ConditionMap conditionMap, int unionIndex) {
        StringBuffer conditionbuf = new StringBuffer();


        boolean andFlag = (msize > 1) ? true : false;

        // loop through each object in this QPath, and find conditions for each
        for (int j = 0; j < msize; j++) {
            String object = getObject(j);
            ArrayList conditions = conditionMap.get(object);

            if (conditions != null) {
                for (int i = 0; i < conditions.size(); i++) {
                    Condition cond = (Condition) conditions.get(i);
                    cond.setAlternateSubQuery("");
                }
            }
        }

        // now create subquery from subpaths.
        for (int j = 0; j < msubpaths[unionIndex].size(); j++) {
            SubQPath subpath = (SubQPath) msubpaths[unionIndex].get(j);

            // SubQPath will return the sub query.
            //java.util.List subquery = subpath.createSubQuery(conditionMap, unionIndex, prepareIndex);
            java.util.List subquery = subpath.createSubQuery(conditionMap, unionIndex);
        }

        // Now get Condition Descriptor
        ConditionDescriptor condDesc = mConditions[unionIndex].getConditionDescriptor();

        List bindParamList = condDesc.getBindParams();

        for (int i = 0; i < bindParamList.size(); i++) {
            mfieldPrepareIndex.add(bindParamList.get(i));
        }


        return condDesc.getConditionString();

    }

    StringBuffer createFromTable(int unionIndex) {
        if (ObjectFactory.isDatabaseANSI()) {
            /*
            The tables would be included in createJoins for SQL-99 ANSI queries
             */
            return null;

        }


        StringBuffer fromTable = new StringBuffer();

        // make the driving table to be root, so root should appear in the end
        for (int i = msize - 1; i >= 0; i--) {
            //for (int i = outerJoinIndex; i >= 0; i--) {
            String object = getObject(i);
            String table = MetaDataService.getDBTableName(object);

            if (i < (msize - 1)) {
                // if (i < outerJoinIndex ) {
                fromTable.append(" , ");
            }

            fromTable.append(table);
        }

        return fromTable;
    }

    StringBuffer createJoins(int unionIndex) {
        if (ObjectFactory.isDatabaseANSI()) {
            return createANSIJoins();
        }
        StringBuffer joinbuf = new StringBuffer();

        for (int i = 0; i < (msize - 1); i++) {
            if (i > 0) {
                joinbuf.append(" AND ");
            }

            String primaryObject = getObject(i);
            String secondaryObject = getObject(i + 1);

            createOuterJoin(joinbuf, primaryObject, secondaryObject);

        }
        return joinbuf;
    }

    StringBuffer createOrderBy(SQLDescriptor sqlDesc) {
        StringBuffer orderBy = new StringBuffer();

        boolean orderFlag = false;
        // order by will not include the leaf object
        for (int i = 0; i < (msize - 1); i++) {
            String object = getObject(i);
            // We don't order by SO or SBR becuase SO/SBR has 1 to 1 with releationship with primary object such as Person
            // hack: Currently we are retrieving Enterprise only where condition is set on EUID. So order by on EUID is
            // not necessary.
            //  @@todo make such check using a method from Condition instead of hardcoding it.
            if (object.equals("Enterprise") || object.equals("Enterprise.SystemObject") || object.equals("Enterprise.SystemSBR")) {
                continue;
            }
            Integer[] keyIndices = sqlDesc.getKeyIndices(object);

            for (int j = 0; j < keyIndices.length; j++) {
                if (orderFlag == false) {
                    orderBy.append(" Order By ");
                    orderFlag = true;
                } else {
                    orderBy.append(" , ");
                }

                Integer orderIndex = keyIndices[j];
                orderBy.append(orderIndex.toString());
            }

        }

        return orderBy;
    }

    /*
     *creates join String between primary and secondaryObject.
     */
    void createJoin(StringBuffer joinFields, String primaryObject,
            String secondaryObject) {

        createJoin(joinFields, primaryObject,
                secondaryObject, true); // Manish : Always create outer joins


    }

    private void createJoin(StringBuffer joinFields, String primaryObject,
            String secondaryObject, boolean outerJoin) {

        String[] pk = getPrimaryKey(primaryObject, secondaryObject);
        String[] fkey = getForeignKey(primaryObject, secondaryObject);

        String pTable = MetaDataService.getDBTableName(primaryObject);
        String sTable = MetaDataService.getDBTableName(secondaryObject);

        switch (ConnectionUtil.getDBProductID()) {
            case ConnectionUtil.DB_SQLSERVER:
                String joinField = SQLServerSQLObject.createJoin(pTable, pk, sTable, fkey, outerJoin);
                joinFields.append(joinField);
                break;

            case ConnectionUtil.DB_ORACLE:
                joinField = OracleSQLObject.createJoin(pTable, pk, sTable, fkey, outerJoin);
                joinFields.append(joinField);
                break;
            case ConnectionUtil.DB_MYSQL:
                joinField = MySQLSQLObject.createJoin(pTable, pk, sTable, fkey, outerJoin);
                joinFields.append(joinField);
                break;
            case ConnectionUtil.DB_AXION:
                joinField = AxionSQLObject.createJoin(pTable, pk, sTable, fkey, outerJoin);
                joinFields.append(joinField);
                break;

        }

    }

    StringBuffer createANSIJoins() {

        StringBuffer inner = null;

        for (int i = msize - 1; i > 0; i--) {

            String primaryObject = getObject(i - 1);
            String secondaryObject = getObject(i);
            inner = createANSIJoin(inner, primaryObject, secondaryObject, true, true);
        }
        if (inner == null) { // only one object

            String table = MetaDataService.getDBTableName(getObject(0));
            inner = new StringBuffer(table);
        }

        return inner;
    }

    /**
     * 
     * @param innerBuf  if null, use secondary object table within the constructed clause otherwise use innerBuf
     *          in the constructed ANSI join as in example below. If nesting is false, innerBuf should be false too.
     * @param primaryObject
     * @param secondaryObject
     * @param outerJoin
     * @param nesting false:  ansi clause is of to constructed in form:
     *   ex: sbyn_person  
    left outer join sbyn_address  on sbyn_person.personid=sbyn_address.personid
    left outer join sbyn_alias  on sbyn_person.personid = sbyn_alias.personid 
     *  
     *    nesting true:  ansi clause is of form:
     *   ex: (sbyn_systemobject left outer join 
    (sbyn_person left outer join sbyn_address on sbyn_person.personid=sbyn_address.personid) 
    on sbyn_systemobject.systemcode = sbyn_person.systemcode and sbyn_systemobject.lid = sbyn_person.lid ) 
     *    
     * @return
     */
    StringBuffer createANSIJoin(StringBuffer innerBuf, String primaryObject,
            String secondaryObject, boolean outerJoin, boolean nesting) {

        String[] pk = getPrimaryKey(primaryObject, secondaryObject);
        String[] fkey = getForeignKey(primaryObject, secondaryObject);

        StringBuffer joinBuff = new StringBuffer();

        String pTable = MetaDataService.getDBTableName(primaryObject);
        String sTable = MetaDataService.getDBTableName(secondaryObject);

        if (nesting == true) {
            joinBuff.append(" ( ").append(pTable);
        }
        if (outerJoin == true) {
            joinBuff.append(" left outer join ");
        } else {
            joinBuff.append(" inner join ");
        }


        if (innerBuf == null || nesting == false) {
            joinBuff.append(sTable);
        } else {
            joinBuff.append(innerBuf);
        }

        joinBuff.append(" on ");

        for (int i = 0; i < pk.length && i < fkey.length; i++) {
            if (i > 0) {
                joinBuff.append(" AND ");
            }
            joinBuff.append(" ");
            joinBuff.append(pTable);
            joinBuff.append(".");
            joinBuff.append(pk[i]);
            joinBuff.append(" = ");
            joinBuff.append(sTable);
            joinBuff.append(".");
            joinBuff.append(fkey[i]);
            joinBuff.append(" ");
        }
        if (nesting == true) {
            joinBuff.append(" ) ");
        }

        return joinBuff;
    }

    /*
    creates join String between primary and secondaryObject.
     */
    void createOuterJoin(StringBuffer joinFields, String primaryObject,
            String secondaryObject) {
        createJoin(joinFields, primaryObject, secondaryObject, true);
    }

    /**
     * create SQLDescriptor. The selectMap gives all the fields to be selected
     * in the SQL statement. conditionMap gives all the conditions that needs to
     * be set in this SQL statement. Note: This implemenation is done from
     * MultiQPath (multiple queries) point of view. subclass SinglePath needs to
     * override this method.
     *
     * @param conditionMap
     * @param selectObject
     */
    SQLDescWithBindParameters createSQL(ConditionMap[] conditionMap, Object selectObject, int maxRows, String[] hints) {
        SelectMap selectMap = (SelectMap) selectObject;
        StringBuffer conditionbuf;
        SQLDescriptor sqlDesc = new SQLDescriptor();
        StringBuffer sqlbuf = new StringBuffer();
        StringBuffer sql = null;
        if (hints == null) {
            hints = new String[0]; // so we don't have null checks later

        }

        //####@@@@ Oct-22-04
        int size = 0;
        if (conditionMap != null) {
            size = conditionMap.length;
        }

        initPrepareIndexMap(size);

        sqlDesc.setRoot(getRoot());

        // the subclass implements createSelectFields.
        // reason being, MultipleQuery and SingleQuery populates sqlDesc differently.
        StringBuffer selectbuf = createSelectFields(sqlDesc, selectMap);
        String hnt = "";
        if (hints.length > 0) {
            hnt = hints[0];
        }
        int dbType = ConnectionUtil.getDBProductID();
        if (conditionMap != null) {
            for (int i = 0; i < conditionMap.length; i++) {
                if (i > 0) {
                    sqlbuf.append(" Union ");
                }

                if (hints.length > i) {
                    hnt = hints[i];
                }

                StringBuffer fromTables = createFromTable(i);
                StringBuffer joinbuf = createJoins(i);
                conditionbuf = createConditions(conditionMap[i], i);
                
                if (i != conditionMap.length - 1 && dbType == ConnectionUtil.DB_SQLSERVER) {
                    // For SQL Server supporting only OPTION hint that is added to the SQL after last union.
                    hnt = "";
                } else if (i == conditionMap.length - 1 && dbType == ConnectionUtil.DB_SQLSERVER) {
                    if (SQLServerSQLObject.isBlank(hnt) && hints.length > 0) {
                        /* if last hint is not specified, then use the first hing
                        in last hint.
                        ex. if hint is specified as {"FAST 100"} in QueryObject
                        Then hint[0] is "FAST 100" and hint[1] is null.
                        But we should specify FAST 100 to the hint[1] which is last
                        union query.
                         */
                        hnt = hints[0];
                    }
                }
                sql = createSQL(selectbuf, fromTables, joinbuf, conditionbuf, maxRows, hnt);
                sqlbuf.append(sql);
            }
        } else {

            StringBuffer fromTables = createFromTable(0);
            StringBuffer joinbuf = createJoins(0);
            sql = createSQL(selectbuf, fromTables, joinbuf, null, maxRows, hnt);
            sqlbuf.append(sql);

        }

        StringBuffer orderBy = createOrderBy(sqlDesc);
        if (dbType == ConnectionUtil.DB_MYSQL && maxRows > 0) {
            sqlbuf.insert(sqlbuf.indexOf("LIMIT"), orderBy);
        } else {
            sqlbuf.append(orderBy);
        }
        sqlDesc.setSQL(sqlbuf.toString());


        return new SQLDescWithBindParameters(sqlDesc, mfieldPrepareIndex);

    }


    /*
    form the SQL statement
     */
    StringBuffer createSQL(StringBuffer selectbuf, StringBuffer fromTable,
            StringBuffer joinbuf, StringBuffer conditionbuf, int maxRows, String hint) {

        StringBuffer sql = new StringBuffer();
        switch (ConnectionUtil.getDBProductID()) {
            case ConnectionUtil.DB_SQLSERVER:
                sql = SQLServerSQLObject.createSQL(selectbuf, fromTable, joinbuf, conditionbuf, maxRows, hint);
                break;
            case ConnectionUtil.DB_AXION:
                sql = AxionSQLObject.createSQL(selectbuf, fromTable, joinbuf, conditionbuf, maxRows, hint);
                break;
            case ConnectionUtil.DB_MYSQL:
                sql = MySQLSQLObject.createSQL(selectbuf, fromTable, joinbuf, conditionbuf, maxRows, hint);
                break;
            case ConnectionUtil.DB_ORACLE:
            default:
                sql = OracleSQLObject.createSQL(selectbuf, fromTable, joinbuf, conditionbuf, maxRows, hint);
                break;
        }

        return sql;
    }

    /**
     * The subclass would implement this method.
     *
     * @param sqlDesc
     * @param selectMap
     */
    StringBuffer createSelectFields(SQLDescriptor sqlDesc, SelectMap selectMap) {
        return null;
    }

    /**
     * This methods creates list of subpaths for each QPath. Given a QPath a
     * subpath is formed from other QPaths. A subpath is navigation from the
     * lowest leaf in a QPath that has a condition, to the intersecting node in
     * QPath. So if this QPath is { System Person Address } and other QPaths are
     * { System Person Phone }, { System Person Alias } and conditions are
     * Phone.area = '626' and System.EUID = 'E1', then subpaths would be:
     * {Person Phone}. Alias path is not included because it does not have
     * condition. System is not included in subpath because it is already in the
     * QPath. If there are more than one CondtionMap, then each QPath contains
     * list of subpaths list. So each list of subpaths list is used in Union
     * operation. Note: Each ConditionMap in ConditionMap[] is for a different
     * union operation of conditions.
     *
     * @param conditionPaths
     * @param conditionMaps
     */
    void createsubQPath(QPath[] conditionPaths, ConditionMap[] conditionMaps) {

        //####@@@@ Oct-22-04
        if (conditionMaps == null) {
            return;
        }

        msubpaths = new ArrayList[conditionMaps.length];
        mlowestSubpathIntersectNode = new String[conditionMaps.length];

        // The For each conditionMap, there will be list of subpaths


        for (int i = 0; i < conditionMaps.length; i++) {
            ConditionMap conditionMap = conditionMaps[i];
            msubpaths[i] = new ArrayList();

            // find the valid subquerys for each QPath
            for (int j = 0; j < conditionPaths.length; j++) {
                QPath conditionPath = conditionPaths[j];
                String leaf = conditionMap.getLeafNode(conditionPath);

                // so this conditionPath does not contain any condition for this
                // Union []. In other words no condition for this conditionMap
                if (leaf == null) {
                    continue;
                }

                // if this condition path leaf is already in this QPath, we don't need subpath
                if (this.contains(leaf)) {
                    setLowestSubpathObject(leaf, i);

                    continue;
                }

                SubQPath subpath = new SubQPath();
                int leafIndex = conditionPath.getIndex(leaf);

                // We are navigating from bottom to top. we start from the
                // lowest object that has a condition attached to it.
                for (int k = leafIndex; k >= 0; k--) {
                    String object = conditionPath.getObject(k);

                    if (contains(object)) {
                        // so the subpath interesects qpath and we are done
                        if (subpath != null) {
                            subpath.add(object);
                        }

                        setLowestSubpathObject(object, i);

                        break;
                    }

                    if (subpath == null) {
                        subpath = new SubQPath();
                    }

                    subpath.add(object);
                }

                if (subpath != null) {
                    msubpaths[i].add(subpath);
                }
            }
        }
    }

    void initPrepareIndexMap(int size) {

        mfieldPrepareIndex = new ArrayList();
    }

    int size() {
        return msize;
    }

    private int getOuterJoinIndex(int unionIndex) {
        String lowestOuterJoinObject = mlowestSubpathIntersectNode[unionIndex];

        int outerJoinIndex = (lowestOuterJoinObject != null)
                ? getIndex(lowestOuterJoinObject) : 0;

        return outerJoinIndex;
    }

    private boolean isDescenedant(String object, String ancestor) {
        if (ancestor == null) {
            return true;
        }

        for (int i = 0; i < msize; i++) {
            String pathObject = getObject(i);

            if (pathObject.equals(object)) {
                return false;
            }

            if (pathObject.equals(ancestor)) {
                return true;
            }
        }

        return false;
    }

    Condition[] getConditions() {
        return mConditions;
    }

    void setConditions(Condition[] conditions) {
        this.mConditions = conditions;
    }

    //===================================================================
    private void setLowestSubpathObject(String object, int index) {
        if (isDescenedant(object, mlowestSubpathIntersectNode[index])) {
            mlowestSubpathIntersectNode[index] = object;
        }
    }

    private String[] getPrimaryKey(String primaryObject, String secondaryObject) {
        String[] pk = null;


        if (primaryObject.equalsIgnoreCase("Enterprise")) {
            if (secondaryObject.equalsIgnoreCase("Enterprise.SystemObject")) {
                pk = new String[]{"SystemCode", "LID"};

            } else {
                pk = new String[]{"EUID"};

            }
        } else {
            pk = MetaDataService.getDBTablePK(primaryObject);

        }
        return pk;
    }

    private String[] getForeignKey(String primaryObject, String secondaryObject) {

        String[] fkey = null;

        if (primaryObject.equalsIgnoreCase("Enterprise")) {
            if (secondaryObject.equalsIgnoreCase("Enterprise.SystemObject")) {
                fkey = new String[]{"SystemCode", "LID"};

            } else {
                fkey = new String[]{"EUID"};

            }
        } else {


            String primaryTag = primaryObject.substring(primaryObject.lastIndexOf(
                    '.') + 1);
            fkey = MetaDataService.getDBTableFK(secondaryObject, primaryTag);

        }
        return fkey;
    }


    // 
    private class SubQPath {
        // list of objects in this SubQPath.
        // the root is the last added node and it intersects the QPath.

        private ArrayList mobjects = new ArrayList();

        /**
         * 
         */
        public String toString() {
            return " SubQueryPath Objects:" + mobjects.toString();
        }

        String getRoot() {
            return (String) mobjects.get(mobjects.size() - 1);
        }

        void add(String object) {
            mobjects.add(object);
        }

        java.util.List createSubQuery(ConditionMap conditionMap, int unionIndex) {

            StringBuffer subQuery = new StringBuffer();
            String object = getRoot();
            String table = MetaDataService.getDBTableName(object);
            String[] pkey = MetaDataService.getDBTablePK(object);

            String firstSecObject = (String) mobjects.get(mobjects.size() - 2);
            String firstSecTable = MetaDataService.getDBTableName(firstSecObject);

            String primaryTag = object.substring(object.lastIndexOf('.') + 1);
            String[] fkey = MetaDataService.getDBTableFK(firstSecObject, primaryTag);


            subQuery.append("  ");
            subQuery.append(table);
            subQuery.append(".");
            subQuery.append(pkey[0]);
            subQuery.append(" IN ( Select ");
            //subQuery.append(table);
            subQuery.append(firstSecTable);
            subQuery.append(".");
            //subQuery.append(pkey[0]);
            subQuery.append(fkey[0]);
            subQuery.append(" From ");



            for (int i = 0; i < mobjects.size() - 1; i++) {
                if (i > 0) {
                    subQuery.append(" , ");
                }

                table = MetaDataService.getDBTableName((String) mobjects.get(i));
                subQuery.append(table);
            }

            subQuery.append(" Where ");
            boolean anyJoin = false;


            for (int i = mobjects.size() - 2; i > 0; i--) {
                String primaryObject = (String) mobjects.get(i);
                String secondaryObject = (String) mobjects.get(i - 1);

                if (i < (mobjects.size() - 2)) {
                    subQuery.append(" AND ");
                    anyJoin = true;
                }
                createJoin(subQuery, primaryObject, secondaryObject);
            }

            if (anyJoin) {
                subQuery.append(" AND ");
            }

            ArrayList stringBuffers = new ArrayList();


            appendCondition(stringBuffers, subQuery, conditionMap, unionIndex);

            for (int i = 0; i < stringBuffers.size(); i++) {
                StringBuffer sb = (StringBuffer) stringBuffers.get(i);

            }
            return stringBuffers;
        }

        private void appendCondition(java.util.List buffers, StringBuffer sbuf,
                ConditionMap conditionMap, int unionIndex) {
            for (int j = 0; j < (mobjects.size() - 1); j++) {
                String object = (String) mobjects.get(j);
                ArrayList conditions = conditionMap.get(object);

                if (conditions != null) {
                    for (int i = 0; i < conditions.size(); i++) {
                        Condition cond = (Condition) conditions.get(i);
                        StringBuffer tmpBuf = new StringBuffer();
                        tmpBuf.append(sbuf);

                        cond.setAlternateSubQuery("");
                        cond.setAlternateSubQuery(tmpBuf.toString());
                        buffers.add(tmpBuf);
                    }
                }
            }

        }
    }
}
