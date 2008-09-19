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
import com.sun.mdm.index.objects.metadata.ObjectFactory;
import com.sun.mdm.index.util.ConnectionUtil;


/**
 * This is used for Single Query option. Say if object graph is: System Person
 * Address Phone, SingleQPath would contain all the nodes: { System, Person,
 * Address, Phone } The reason it subclass QPath is that QPath creates subpaths
 * for objects those are not in QPath. In case of SingleQPath, the subpaths
 * would be those objects which have conditions but are not included in the
 * QPath. 
   Note: Because SingleQPath contains all the objects in the select tree, the
 * SQL statement created by it would produce lots of redundant data. So this is
 * more useful where the applications want tuples.
 *
 * @author sdua
 */
class SingleQPath extends QPath {
    
    /*
     *  List of all JoinList.
     */
    private ArrayList mjoinListAll = new ArrayList();


    /**
     * Creates a new instance of SingleQPath
     */
     SingleQPath() {
    }


    /**
     * 
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();

        buf.append(" Object Names: ");

        
        return buf.toString();
    }


    void add(JoinList joinList) {
        // mjoinList = joinList;
        mjoinListAll.add(joinList);
    }


    StringBuffer createJoins(int unionIndex) {
    	
    	if (ObjectFactory.isDatabaseANSI()) {
    		return createANSIJoins();
    	}
    	
        StringBuffer joinbuf = new StringBuffer();

        for (int i = 0; i < mjoinListAll.size(); i++) {
            JoinList joinList = (JoinList) mjoinListAll.get(i);
       

            for (int j = 0; j < (joinList.size() - 1); j++) {
                if ((i > 0) || (j > 0)) {
                    joinbuf.append(" AND ");
                }

                String primaryObject = (String) joinList.get(j);
                String secondaryObject = (String) joinList.get(j + 1);

               
                createOuterJoin(joinbuf, primaryObject, secondaryObject);
            }
        }

        return joinbuf;
    }
    
    /**
     * creates Joins that conform to SQL 99 ANSI syntax
     */
    StringBuffer createANSIJoins() {
        
        StringBuffer joinBuf = new StringBuffer();

         // TODO: support ANSI  for any levels
        // For ansi joins like for blocking query right now, we are supporting only two levels at the moment
         
        for (int i = 0; i < mjoinListAll.size(); i++) {
        	
            JoinList joinList = (JoinList) mjoinListAll.get(i);
            
            

             String primaryObject = (String) joinList.get(0);
             
             if (i == 0) {
             	String table = MetaDataService.getDBTableName(primaryObject);
             	joinBuf.append(" ").append(table).append(" ");
         	 }
             if (joinList.size() <= 1) {
             	continue;
             }
             String secondaryObject = (String) joinList.get(1);
            
            StringBuffer inner = createANSIJoin(null, primaryObject, secondaryObject, true, false);
            joinBuf.append(inner);
        }

        return joinBuf;
    }


    StringBuffer createOrderBy() {
        StringBuffer orderBy = new StringBuffer();
        String root = getObject(0);
        String table = MetaDataService.getDBTableName(root);
        String[] pk = MetaDataService.getDBTablePK(root);

        orderBy.append(" Order By 1 ");

      
        return orderBy;
    }


    SQLDescWithBindParameters createSQL(ConditionMap[] conditionMaps, Object selectObject, int maxRows, 
    		String[] hints) {
        StringBuffer conditionbuf;
        SQLDescriptor sqlDesc = new SQLDescriptor();
        if (hints == null ) {
        	hints = new String[0]; // so we don't have null checks later
        }
        
        int size = 0;
        if ( conditionMaps != null ) {
            size = conditionMaps.length;    
        }
        
        initPrepareIndexMap(size);
        
        sqlDesc.setRoot(getRoot());
        setOuterJoinIndices(conditionMaps);

        QualifiedField[] selectFields = (QualifiedField[]) selectObject;
        StringBuffer sql = null;
        StringBuffer sqlbuf = new StringBuffer();

        StringBuffer selectbuf = createSelectFields(sqlDesc, selectFields);
        String hnt = "";
        if (hints.length > 0) {
        	hnt = hints[0];
        }
        int dbType = ConnectionUtil.getDBProductID();
        if ( conditionMaps != null ) {
            for (int i = 0; i < conditionMaps.length; i++) {
                if (i > 0) {
                    sqlbuf.append(" Union ");
                }
                
                if (hints.length > i) {
                	hnt = hints[i];
                }

                StringBuffer fromTables = createFromTable(i);
                StringBuffer joinbuf = createJoins(i);
                conditionbuf = createConditions(conditionMaps[i],i);                
                
                if (i != conditionMaps.length -1 && dbType == ConnectionUtil.DB_SQLSERVER) {
                	// For SQL Server supporting only OPTION hint that is added to the SQL after last union.
                  hnt = "";
                  // For SQL server, and for last union query
                } else if (i == conditionMaps.length -1 && dbType == ConnectionUtil.DB_SQLSERVER ){
                     if (SQLServerSQLObject.isBlank(hnt) && hints.length > 0) { 
                		/* if last hint is not specified, then use the first hing
                		 in last hint.
                		 ex. if hint is specified as {"FAST 100"} in QueryObject
                		 Then hint[0] is "FAST 100" and hint[1] is null.
                		 But we should specify FAST 100 to the hint[1] which is last
                		 union query, because for SQL server OPTION hint is at end of query
                		*/
                		hnt = hints[0];
                	}
                }
                sql = createSQL(selectbuf, fromTables, joinbuf, conditionbuf, maxRows, hnt);
                sqlbuf.append(sql);
            }
        }
        else {
            
            StringBuffer fromTables = createFromTable(0);
            StringBuffer joinbuf = createJoins(0);
            sql = createSQL(selectbuf, fromTables, joinbuf, null, maxRows, hnt);
            sqlbuf.append(sql);
        }

        if (containsPrimaryKey(selectFields, getRoot())) {
            StringBuffer orderBy = createOrderBy();
            if (dbType == ConnectionUtil.DB_MYSQL && maxRows > 0) {
                sqlbuf.insert(sqlbuf.indexOf("LIMIT"), orderBy);
            } else {
                sqlbuf.append(orderBy);
            }
        }
 
        sqlDesc.setSQL(sqlbuf.toString());
       
        return new SQLDescWithBindParameters( sqlDesc, getFieldPrepareIndex());
        
    }


    StringBuffer createSelectFields(SQLDescriptor sqlDesc,
            QualifiedField[] selectFields) {
        StringBuffer selectbuf = new StringBuffer();
        boolean first = true;

       
        int attIndexLow = 1;
        int attIndexHigh = 1;
        ArrayList fields = new ArrayList();

        for (int i = 0; i < selectFields.length; i++) {
            String object = selectFields[i].getObject();
            String table = MetaDataService.getDBTableName(object);
            String field = selectFields[i].getField();
            fields.add(field);

            if (!first) {
                selectbuf.append(" , ");
            } else {
                first = false;
            }

            selectbuf.append(table);

           
            selectbuf.append(".");

            String column = MetaDataService.getColumnName(object + "." + field);
            selectbuf.append(column);
        }

        attIndexHigh = attIndexLow + selectFields.length;

        sqlDesc.addObjectSelectData(getRoot(), attIndexLow, attIndexHigh, null,
                fields, null);

        return selectbuf;
    }


    private void setOuterJoinIndices(ConditionMap[] conditionMaps) {
        for (int i = 0; i < mjoinListAll.size(); i++) {
            JoinList joinList = (JoinList) mjoinListAll.get(i);
            joinList.setOuterJoinIndices(conditionMaps);
        }
    }


    private boolean containsPrimaryKey(QualifiedField[] selectFields,
            String object) {
        boolean found = false;
        String table = MetaDataService.getDBTableName(object);
        String[] pk = MetaDataService.getDBTablePK(object);

        for (int i = 0; i < pk.length; i++) {
            String pkField = pk[i];
            found = false;

            for (int j = 0; j < selectFields.length; j++) {
                if (!object.equals(selectFields[j].getObject())) {
                    continue;
                }

                String field = selectFields[j].getField();
                String column = MetaDataService.getColumnName(object + "." 
                       + field);

                if (column.equals(pkField)) {
                    found = true;

                    break;
                }
            }

            if (!found) {
                break;
            }
        }

        return found;
    }
}
