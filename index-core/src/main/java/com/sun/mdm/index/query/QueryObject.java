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
import com.sun.mdm.index.util.LogUtil;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.sql.ResultSet;

/**
 * The <b>QueryObject</b> class represents the criteria and conditions of an
 * object query in the master index. It is the primary class for interacting with
 * the Query Manager (class QueryManager), which executes each query. QueryObject
 * holds the query information read by the Query Manager, and is used to set select
 * fields for an object and to set conditions on object fields. The Query Manager
 * transforms the query object into a relational database SQL query. The generated
 * SQL statements contain select fields and conditions as the WHERE clause of the SQL
 * query and always include primary keys of the objects. The statements might also
 * include an "ORDER BY <I>primary_key</I>" clause.
 * <P>
 * You can query from multiple objects and set conditions on any object attributes.
 * The relationship between objects does not need to be specified by the client;
 * they are obtained internally and mapped by the Query Manager. Use the qualified
 * field name syntax to specify fields (see the eView Studio or eIndex Single Patient
 * Identifier User's Guide for more information about field
 * syntax conventions).
 * @author sdua
 */
public class QueryObject implements java.io.Serializable, QueryConstants {
    
    private ArrayList mconditions = new ArrayList();
    private ArrayList mselectFields = new ArrayList();
    private ArrayList morderFields = new ArrayList();
    private ArrayList mfromObjects = new ArrayList();
    private String mroot = null;
    private int mqueryOption = MULTIPLE_QUERY;
    private QueryParser mqueryParser = new MultiQueryParser(this);
    private SQLDescriptor[] msqlDesc = null;
    private String mstringDesc = null;
    private boolean mprepare = false;
    private boolean mINValues = false;
    private AssembleDescriptor massembleDesc;
    private Condition[][] mconditionsUnion;
    private String mprepareId;
    private int maxRows = -1;
    private int maxObjects = -1;
    private int fetchSize;
    private int resultSetType = -1;

    private Condition [] mreconstructedConditionsUnion;
    private String[][] mhints = new String[0][0];

	private QueryObjectCache mQoCache;

    /**
     * Creates a new instance of the QueryObject class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DL><DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public QueryObject() {
    }

    /**
     * This a copy constructor for the QueryObject class and creates a copy of
     * the specified query object. Fields that are constructed after parsing are
     * not copied into the new object, so the new QueryObject must be parsed (using
     * QueryObject.parse).
     * <p>
     * @param qo The query object to copy.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public QueryObject( QueryObject qo) {
    	for ( int i = 0; i < qo.mselectFields.size(); i++) {
    	   mselectFields.add(qo.mselectFields.get(i));

    	}

    	for ( int i = 0; i < qo.mconditions.size(); i++) {
     	   mconditions.add(new Condition((Condition)  qo.mconditions.get(i)));
     	}

    	if (qo.mconditionsUnion != null) {
    		mconditionsUnion = new Condition[qo.mconditionsUnion.length][];
    		for (int i = 0; i < qo.mconditionsUnion.length; i++) {
    			Condition[] conds = qo.mconditionsUnion[i];
    			if ( conds != null ) {
    			  mconditionsUnion[i] = new Condition[conds.length];
    			  for ( int j =0; j < conds.length; j++){
    				mconditionsUnion[i][j] = new Condition(conds[j]);
    			  }
    			}
    		}

    	}
        
        setQueryOption(qo.mqueryOption);
        massembleDesc = qo.massembleDesc;
        maxRows = qo.maxRows;
        maxObjects = qo.maxObjects;
        mprepareId = qo.mprepareId;
        mhints = qo.mhints;
        mreconstructedConditionsUnion = qo.mreconstructedConditionsUnion;
        msqlDesc = qo.msqlDesc;
        mprepare = qo.mprepare;
        mINValues = qo.mINValues;
        //fetchSize = qo.fetchSize;
        resultSetType = qo.resultSetType;
        
    }
    

    /*
          This is overload of addSelect. You can set alias of returned field using
          alias parameter.
          @param field column fetched from database
          @param alias the return field has this alias
          public void addSelect(String field, String alias) {
          }
      */

    /**
     * Sets the assemble descriptor (class AssembleDescriptor) for the QueryObject.
     * The assemble descriptor contains information used by the assembling engine,
     * such as the structure of the enterprise or system object, the iterator type,
     * and the result object assembler to be used by the assembling engine.
     * <p>
     * Note: This method is not required if you intend to delay the assembly of the
     * query object. Use the QueryManager.execute() method instead.
     * <p>
     * @param assDesc The assemble descriptor to use for this query object.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setAssembleDescriptor(AssembleDescriptor assDesc) {
        massembleDesc = assDesc;
    }


    /**
     * Specifies all the conditions of the query. All the conditions for this
     * query object can be set by calling this method once.
     * <p>
     * @param conditions An array of query conditions (including the name
     * operator, and value).
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setCondition(Condition[] conditions) {
        mconditions = new ArrayList();

        for (int i = 0; i < conditions.length; i++) {
            mconditions.add(conditions[i]);
        }
    }


    /**
     * Specifies multiple sets of conditions so the query object (more specifically
     * the Query Manager) will logically use a UNION operator between each set of
     * conditions in the SQL statement. The Condition[] array contains an array
     * of conditions, and the Condition array implicitly contains a UNION operator
     * for all Condition[] arrays.
     * <p>
     * @param conditions An array of query condition arrays.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setCondition(Condition[][] conditions) {
        mconditionsUnion = conditions;
    }


    /**
     * Specify the order of results using order by field. Note: the orderbyField
     * should belong to root object of the composite object. So if the value set
     * has primary object as its root (most of times, say Person ), then the
     * order by columns will be from the primary object.
     * Note: This is reserved for future use
     * @param orderbyField order by
     */
    public void setOrderBy(String orderbyField) {
    }


    /**
     * Sets an identification for the query object to be used for prepared
     * statements. If no identification is set, executeQuery is executed; if
     * an identification is set, the prepared statement is executed. If you call
     * QueryManager multiple times for the same query object, the object is prepared
     * for the first call (if it is not already in the prepared statement pool).
     * After the first call, values are used to bind the object to the already
     * prepared statement.
     * <p>
     * @param identifier The name of the query object (QueryObject).
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    public void setPreparedId(String identifier) {
        mprepareId = identifier;
    }


    /**
     * Specifies a query option that indicates to the parser whether to perform
     * a single or multiple query. Possible options are QueryConstants.SINGLE_QUERY or
     * QueryConstants.MULTIPLE_QUERY. SINGLE_QUERY is used for a tuple
     * query. MULTIPLE_QUERY is used for an object construction query. The default is
     * MULTIPLE_QUERY, which is less redundant than the SINGLE_QUERY option.
     * <p>
     * @param option An identification code that indicates which query option
     * to use. 1 indicates SINGLE_QUERY; 2 indicates MULTIPLE_QUERY.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setQueryOption(int option) {
        mqueryOption = option;

        if (mqueryOption == SINGLE_QUERY) {
            mqueryParser = new SingleQueryParser(this);
        } else if (mqueryOption == MULTIPLE_QUERY) {
            mqueryParser = new MultiQueryParser(this);
        }
    }

    /**
     * Sets ResultSet type for this QueryObject tree. This is optional method.
     *
     * @param type ResultSet Type
     */
    public void setResultSetType(int type) {
        if (type == ResultSet.TYPE_FORWARD_ONLY || type == ResultSet.TYPE_SCROLL_INSENSITIVE
                || type == ResultSet.TYPE_SCROLL_SENSITIVE) {
            resultSetType = type;
        }
    }
 
    /**
     * Sets the root Object for this QueryObject tree. This is optional method.
     *
     * @param root root object
     * @deprecated
     */
    public void setRootObject(String root) {
        mroot = root;
    }


    /**
     * Specifies multiple fields for the SELECT portion of the SQL statement.
     * <p>
     * @param fields An array of fields to be selected in the SQL query.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setSelect(String[] fields) {
        mselectFields = new ArrayList();

        for (int i = 0; i < fields.length; i++) {
            mselectFields.add(fields[i]);
        }
    }


    /**
     * Specifies a condition for the WHERE clause of the SQL query. This method
     * joins the condition to any previous conditions using the AND operator. If
     * a field is of the type "varchar2", the query object will automatically
     * enclose the value in quotes.
     * <p>
     * @param field The fully qualified field name for the WHERE clause.
     * @param operator The SQL operator for the WHERE clause. Available operators
     * are = (equal to) and LIKE.
     * @param value The value for the WHERE clause. The value can be of the
     * type <B>java.sql</B>.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void addCondition(String field, String operator, Object value) {
        Condition cond = new Condition(field, operator, value);
        mconditions.add(cond);
    }


    /**
     * This is an overload method of <B>addCondition</B>, differing in that the
     * value parameter is a String. This method specifies a condition for the
     * WHERE clause of the SQL query, using an AND operator to join any previous
     * conditions added. If a field is of the type "varchar2", the query object
     * automatically encloses the value in quotes.
     * <p>
     * @param field The fully qualified field name for the WHERE clause.
     * @param operator The SQL operator for the WHERE clause. Available operators
     * are = (equal to) and LIKE.
     * @param value The value for the WHERE clause.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void addCondition(String field, String operator, String value) {
        Condition cond = new Condition(field, operator, value);
        mconditions.add(cond);
    }


    /**
     * This is an overload method of <B>addCondition</B>, differing in that the
     * default operator is = (equal to) and the value is a string. This method
     * specifies a condition for the WHERE clause of the SQL query, using an AND
     * operator to join any previous conditions. If a field is of the type
     * "varchar2", the query object automatically encloses the value in quotes.
     * <p>
     * @param field The fully qualified field name for the WHERE clause.
     * @param value The value for the WHERE clause.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void addCondition(String field, String value) {
        Condition cond = new Condition(field, value);
        mconditions.add(cond);
    }


    /**
     * Adds a condition, specified by an instance of the Condition class
     * to the query object.
     * <p>
     * @param condition The condition to be added to the query object.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void addCondition(Condition condition) {
        mconditions.add(condition);
    }


    /**
     * Adds a field to the SELECT portion of the SQL statement. This field is
     * returned by SQL to the query object. Call this method for each field in
     * the SELECT statement, using the fully qualified field name.
     * <p>
     * @param field The name of a field to include in the SELECT portion of t
     * the SQL statement.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void addSelect(String field) {
        mselectFields.add(field);
    }
    /**
     * Limits the maximum number of rows returned from the query. The rows correspond
     * to the rows in the JDBC result set. Only the required rows are retrieved
     * from the database, so the rows are cut off at the database server rather than at
     * the client. Use this method to improve database performance.
     * <p>
     * @param rows The maximum number of rows to be retrieved.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     *
     */

    void setMaxRows(int rows) {
    	maxRows = rows;
    }

    /**
     * Adds Oracle hints to the SELECT statements of the SQL query. The text of each hint
     * is passed to the select statement with no modification. The inner array
     * hints are used when multiple SQL queries are generated by the Query
     * Manager. Each inner String[] element is used for a different SQL query.
     * Each outer array hint (String[][]) is used to add a hint to the
     * corresponding SELECT statement in the UNION generated by the
     * Query Manager (due to the condition added by the setCondition method).
     * For example, "hints[x][y]" adds hints to (x+1) SQL query and to (y+1) UNION
     * SELECT statement.
     * <p>
     * To use this method properly, determine how many queries are generated by running
     * in DEBUG mode first. Then you can set the SQL text appropriately. If
     * the number of hints is less than the number of generated SELECT statements, the
     * remaining SELECT statements are not passed any hints. If the number of hints is
     * greater than the number of SELECT statements, the extra hints are ignored.
     * The Query Manager automatically adds any necessary delimiters, such as "/*+" to
     * the hint.
     * <p>
     * Usage example: To pass the hint "FIRST_ROWS_100" to only the first SELECT
     * statement, use the following:
     *     String hints[][] = {{"FIRST_ROWS_100"}}
     * <p>
     * @param hints An array of hints to add to the SELECT statements generated by
     * the Query Manager.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
   public void setHints(String[][] hints) {
	  if (hints != null) {
	  	mhints = hints;
	  }
   }

    /**
     * Limits the maximum number of objects returned from a query. Only the
     * required rows are retrieved from the database, so the rows are cut off
     * at the database server rather than at the client. Use this method to
     * improve database performance.
     * <p>
     * Note: Internally, the Query Manager may convert maxObjects to "rownum < X" in
     * the WHERE clause (where "X" is the maximum number of objects you specify). The
     * Query Manager may retrieve more rows than specified in order to create the
     * maximum number of objects.
     * <p>
     * @param maxObjects The maximum number of objects to be retrieved.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */


    public void setMaxObjects(int maxObjects) {
   	this.maxObjects = maxObjects;
   }

    /**
     * Specifies the number of objects to return in each batch.
     * <p>
     * @param fetchSize The maximum number of objects to return in each batch.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */

    public void setFetchSize(int fetchSize) {
       this.fetchSize = fetchSize;
    }

    /**
     * Retrieves the specified number of objects to return in each batch.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>int</CODE> - The specified batch size.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    int getFetchSize() {
    	return fetchSize;
    }


    /**
     * Initializes the query object prior to parsing the conditions.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.</DL>
     *
     */
    void preInitializeForCacheComparision() {
        parseOriginalConditionsTree();
        initializeOriginalConditionsForPrepare();
        reconstructConditionExpression();
    }

    /**
     * Parses the conditions of the query object, creating the SQL statements to
     * use for the query.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>SQLDescriptor[]</CODE> - An array of SQL descriptors for the
     * query object.
     * @exception QMException Thrown if an error occurs while parsing the conditions.
     *
     */

    SQLDescriptor[] parse()
        throws QMException {
        if (msqlDesc != null) {
            return msqlDesc;
        }

        calcConditionsBlocks();

        //msqlDesc = mqueryParser.parse();
        SQLDescWithBindParameters [] sqlDescWBP = mqueryParser.parse();

        msqlDesc = new SQLDescriptor[sqlDescWBP.length];
        for ( int i=0; i < sqlDescWBP.length; i++ ) {
        	msqlDesc[i] = sqlDescWBP[i].getSqlDescriptor();
        }

        if ( isPrepare() ) {
        	List [] bindParam = new ArrayList[sqlDescWBP.length];
        	for ( int i=0; i < sqlDescWBP.length; i++ ) {
        		bindParam[i] = sqlDescWBP[i].getFieldPrepareIndex();
            }
        	if ( this.getCacheRef() != null ) {
        		this.getCacheRef().setBindParamList(bindParam);
        	}
        }

        return msqlDesc;
    }


    /**
     * Retrieves the assemble descriptor (class AssembleDescriptor) for the query
     * object. The assemble descriptor contains information used by the assembling
     * engine, such as the structure of the enterprise or system object, the iterator
     * type, and the result object assembler.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>AssembleDescriptor</CODE> - The assemble descriptor for the
     * query object.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    AssembleDescriptor getAssembleDescriptor() {
        return massembleDesc;
    }

    /**
     * Retrieves the maximum number of rows to be returned from the query.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>int</CODE> - The maximum number of rows to be returned.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    int getMaxRows() {
    	return maxRows;
    }

    /**
     * Retrieves the maximum number of objects to be returned from the query.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>int</CODE> - The maximum number of objects to be returned.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    int getMaxObjects() {
    	return maxObjects;
    }

    /**
     * Retrieves the fully qualified path for the primary keys of the root object in
     * the query object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String[]</CODE> - An array of root object primary keys.
     * @exception QMException Thrown if an error occurs while retrieving the
     * primary keys.
     *
     */
    String[] getRootId() throws QMException {
    	return mqueryParser.getRootId();
    }

    /**
     * Retrieves the SQL hints defined for the query object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String[][]</CODE> - Arrays of hints defined for the
     * query object.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    String[][] getHints() {
    	return mhints;
    }

    /**
     * Return the ResultSet Type for the query object.
     * @return <CODE>int</CODE> - Type of ResultSet
     * <DT><B>Throws:</B><DD>None.
     * @include
     */    
    int getResultSetType() {
        return resultSetType;
    }

    /**
     * Indicates whether the given query object includes conditions.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>boolean</CODE> - Boolean true or false. This method
     * returns <B>true</B> if the query object includes conditions; it
     * returns <B>false</b> the object does not include conditions.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    boolean containsCondition() {
    	 if ( ( mconditionsUnion == null  ||  mconditionsUnion.length == 0 )
			 && ( mconditions == null  ||  mconditions.size() == 0) )  {
                return false;
         }
    	 return true;
    }




    /**
     * Retrieves the conditions defined for the query object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Condition[]</CODE> - An array of conditions defined
     * for the query object.
     * <DT><B>Throws:</B><DD>None.
     *
     */
   Condition[] getConditionsUnion() {
        return mreconstructedConditionsUnion;
    }



    //=================================private/package methods=================
    QueryParser getParser() {
        return mqueryParser;
    }


    int getQueryOption() {
        return mqueryOption;
    }


    String getRoot() {
        return mroot;
    }


    SQLDescriptor[] getSQLDescriptor() {
        return msqlDesc;
    }

    void setSQLDescriptor(SQLDescriptor[] sqlDesc) {
        this.msqlDesc = sqlDesc;
    }

    
    String[] getSelectFields() {
        String[] afields = new String[mselectFields.size()];

        return (String[]) mselectFields.toArray(afields);
    }


    boolean isPrepare() {
        return mprepare;
    }


    void setQueryParser(QueryParser queryParser) {
        mqueryParser = queryParser;
    }


    void setQueryParser() {
        if (mqueryOption == QueryConstants.MULTIPLE_QUERY) {
            mqueryParser = new MultiQueryParser(this);
        } else if (mqueryOption == QueryConstants.SINGLE_QUERY) {
            mqueryParser = new SingleQueryParser(this);
        }
    }




    Condition[] getReconstructedConditionsUnion() {
    	return mreconstructedConditionsUnion;
    }


    private Condition[][] getOriginalConditionsUnion() {

        if ( ( mconditionsUnion == null )  || ( mconditionsUnion.length == 0 ) ) {
            // so no union operator and we put mconditions
            // single array into mconditionsUnion[][] of size 1.
            if ( ( mconditions == null ) || ( mconditions.size() == 0 ) )  {
                return null;
            }

            int length = mconditions.size();
            mconditionsUnion = new Condition[1][length];

            for (int i = 0; i < length; i++) {
                mconditionsUnion[0][i] = (Condition) mconditions.get(i);
            }
        }
 
        return mconditionsUnion;
    }

    private Condition reconstructDefaultConditionExpression(Condition[] conditions) {
        Condition conditionNode = null;
        if ( ( conditions != null ) && ( conditions.length > 0 ) ) {
            conditionNode = conditions[0];

            for (int i = 1; i < conditions.length; i++) {
 
                conditionNode.addConditions("AND",conditions[i]);
            }
        }
        return conditionNode;
    }

    private Condition reconstructFlatConditionExpression( Condition [] conditions ) {
        // Rearrange the Condition Nodes :
        Map objectConditionsMap = new TreeMap();

        Condition condInMap = null;

        Condition condNode = null;
        Condition cond = null;
        String objectName = null;

        for ( int i = 0; i < conditions.length; i++ ) {
            condNode =  conditions[i];
            ConditionLeafIterator itr = new ConditionLeafIterator(condNode);
            while ( itr.hasNext() )  {
                cond = (Condition)itr.next();
 
                objectName = cond.getObjectName();

                condInMap = (Condition)objectConditionsMap.get(objectName);

                if ( condInMap == null ) {
                    objectConditionsMap.put(objectName,cond);
                }
                else {
                    condInMap.addConditions("AND",cond);
                }
            }
        }

        // Now, loop through the Map
        Set objectConditionsSet = objectConditionsMap.entrySet();
        Map.Entry entry = null;
        Iterator itr = objectConditionsSet.iterator();
        Condition conditionNode = null;
        while ( itr.hasNext() ) {
            entry = (Map.Entry)itr.next();
            if ( conditionNode == null ) {
                conditionNode = new Condition((Condition)entry.getValue());
            }
            else {
                conditionNode.addConditions("AND",(Condition)entry.getValue());
            }
        }
        return conditionNode;
    }


    void reconstructConditionExpression()  {

        Condition [][] conditionsUnion = getOriginalConditionsUnion();

        if ( conditionsUnion == null ) {
             return;
        }

        mreconstructedConditionsUnion = new Condition[conditionsUnion.length];

        Condition [] conditions = null;
        for ( int i = 0; i < conditionsUnion.length; i++ ) {
            conditions =  conditionsUnion[i];
            boolean oldFormat = isOldFormatConditionExpression(conditions);
            if ( oldFormat ) {
                mreconstructedConditionsUnion[i] = reconstructFlatConditionExpression(conditions);
            }
            else {
                mreconstructedConditionsUnion[i] = reconstructDefaultConditionExpression(conditions);
            }
        }
    }

    void parseOriginalConditionsTree()  {

        Condition [][] conditionsUnion = getOriginalConditionsUnion();

        if ( conditionsUnion == null ) {
            return;
        }

        Condition condition = null;

        for ( int i=0; i < conditionsUnion.length; i++ ) {
            for ( int j=0; j < conditionsUnion[i].length; j++ ) {
                condition = conditionsUnion[i][j];
                condition.parseTree();
            }
        }
    }

    void calcConditionsBlocks()  {
        if ( mreconstructedConditionsUnion == null ) {
            return;
        }

        for ( int i = 0; i < mreconstructedConditionsUnion.length; i++ ) {
                mreconstructedConditionsUnion[i].calcBlock();
        }
    }

    boolean isOldFormatConditionExpression(Condition [] conditions)  {

        if ( conditions == null ) {
            return true;
        }

        Condition condition = null;
        for ( int i = 0; i < conditions.length; i++ ) {
            condition =  conditions[i];
            if ( !condition.isOldFormatConditionExpressionTree() ) {
                return false;
            }
        }
        return true;
    }

    void initializeOriginalConditionsForPrepare() {
        Condition [][] conditionsUnion = getOriginalConditionsUnion();
        if ( conditionsUnion == null ) {
            return;
        }

        int prepareStatus = getPrepareStatus(conditionsUnion);

        if ( prepareStatus == Condition.PREPARE_IN ) {
            propagatePrepareStatus (false,conditionsUnion);
            mprepare = false;
        }
        else if ( prepareStatus == Condition.PREPARE_ATLEAST_ONE  ) {
            mprepare = true;
        }
        else {
            mprepare = false;
        }
    }

    private int getPrepareStatus(Condition[][]  conditionsUnion) {
        int prepareStatus = Condition.PREPARE_NONE;
        int tmpPrepareStatus = Condition.PREPARE_NONE;

        if ( conditionsUnion == null ) {
            return Condition.PREPARE_NONE;
        }

        Condition condition = null;
        for ( int i=0; i < conditionsUnion.length; i++ ) {
            for ( int j=0; j < conditionsUnion[i].length; j++ ) {
                condition = conditionsUnion[i][j];
                tmpPrepareStatus = condition.getPrepareStatus();
                
                if ( tmpPrepareStatus == Condition.PREPARE_IN ) {
                    return Condition.PREPARE_IN;
                }
                if ( tmpPrepareStatus == Condition.PREPARE_ATLEAST_ONE ) {
                    prepareStatus = Condition.PREPARE_ATLEAST_ONE;
                }
            }
        }
        return prepareStatus;
    }

    private void propagatePrepareStatus(boolean prepareStatus, Condition[][] conditionsUnion) {

        if ( conditionsUnion == null ) {
            return;
        }

        Condition condition = null;
        for ( int i=0; i < conditionsUnion.length; i++ ) {
            for ( int j=0; j < conditionsUnion[i].length; j++ ) {
                condition = conditionsUnion[i][j];
                condition.propagatePrepareStatus(prepareStatus);
            }
        }
    }

    public int hashCode() {
        String [] selectFields = getSelectFields();
        StringBuffer sb = new StringBuffer();

        for ( int i=0; i < selectFields.length; i++ ) {
            sb.append(selectFields[i]);
        }

        sb.append(this.getQueryOption()).append(this.getMaxRows());
        Condition [] conditions = getReconstructedConditionsUnion();
        if (conditions != null) {
	        for ( int i=0; i < conditions.length; i++ ) {
    	        sb.append(conditions[i].getField());
        	}
        }
        return sb.toString().hashCode();
    }

    public boolean equals(Object theOther) {
	
        QueryObject theOtherObject = (QueryObject) theOther;
        if (  !compareSelectFields(theOtherObject)) {
           return false;
        }

        if ( this.getQueryOption() != theOtherObject.getQueryOption() ) {
            return false;
        }

        if ( this.getMaxRows() != theOtherObject.getMaxRows() ) {
            return false;
        }

        Condition [] conditions = getReconstructedConditionsUnion();
        Condition [] otherConditions = theOtherObject.getReconstructedConditionsUnion();
        
        if ( ( conditions == null ) || ( otherConditions == null ) ) {
            return false;
        }

        if ( conditions.length != otherConditions.length ) {
            return false;
        }

        Condition condition = null;
        for ( int i=0; i < conditions.length; i++ ) {
        	if ( !conditions[i].equals(otherConditions[i]) ) {
        		return false;
        	}
        }
        return true;
    }
	
    boolean compareWith(QueryObject otherQueryObject, Map bindingAssociation) {

        if (  !compareSelectFields(otherQueryObject)) {
           return false;
        }

        if ( this.getQueryOption() != otherQueryObject.getQueryOption() ) {
            return false;
        }

        if ( this.getMaxRows() != otherQueryObject.getMaxRows() ) {
            return false;
        }

      

        Condition [] conditions = getReconstructedConditionsUnion();
        Condition [] otherConditions = otherQueryObject.getReconstructedConditionsUnion();


        if ( ( conditions == null ) || ( otherConditions == null ) ) {
            return false;
        }

        if ( conditions.length != otherConditions.length ) {
            return false;
        }

        Condition condition = null;
        for ( int i=0; i < conditions.length; i++ ) {
        	if ( !conditions[i].compareWith(otherConditions[i],bindingAssociation) ) {
        		return false;
        	}
        }

      
        return true;
    }

    boolean compareSelectFields(QueryObject otherQueryObject) {
        String [] selectFields = getSelectFields();
        String [] otherSelectFields = otherQueryObject.getSelectFields();
        if ( selectFields.length != otherSelectFields.length ) {
            return false;
        }

        for ( int i=0; i < selectFields.length; i++ ) {
            if ( !selectFields[i].equals(otherSelectFields[i])) {
                return false;
            }
        }
        return true;
    }

    void setCacheRef(QueryObjectCache qoc) {
    	this.mQoCache = qoc;
    }

    QueryObjectCache getCacheRef() {
    	return mQoCache;
    }
    /*
     * Returns a String representation of this object.
     * @return a String representation of this object.
     */
    public String toString() {
        String s =  "QueryObject is - mConditionsUnion = {\n";
        if ( mreconstructedConditionsUnion != null ) {
           for ( int i = 0; i < mreconstructedConditionsUnion.length; i++ ) {
               s = s +  mreconstructedConditionsUnion[i] + ",\n";            
           }
        }
        s = s + "}\n";
	return s;
    }
}
