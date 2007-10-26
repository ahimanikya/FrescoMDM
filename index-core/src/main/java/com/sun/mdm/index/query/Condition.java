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

import com.sun.mdm.index.objects.metadata.MetaDataService;
import java.util.*;

/**
 * The <B>Condition</B> class represents a condition in the WHERE clause of a
 * SQL statement that is used to build a query object. Each condition in the
 * clause requires a different instance of the <B>Condition</B> class. A
 * condition contains a field name, an operator, and one or more values for
 * the field. By default, conditions are joined to each other by AND operators,
 * but you can specify OR by using the <B>addConditions</B> method and specifying
 * "OR" as the join type. A Condition objet can be a condition container that
 * includes both a left condition and a list of right conditions. By using the
 * appropriate methods, you can build a next OR/AND condition tree.
 * <P>
 * For example, to create the condition statement "Enterprise.SystemSBR.Person.FirstName =
 * John OR Enterprise.SystemSBR.Person.FirstName = James", use the following methods.
 * <P>
 * Condition condition = new Condition ("Enterprise.SystemSBR.Person.FirstName",
 * "=", "John", true);
 * <P>
 * condition.addConditions("OR", new Condition("Enterprise.SystemSBR.Person.FirstName",
 * "=", "James", true));
 *
 * @author sdua
 */
public class Condition {
    
    private boolean mprepare = false;

    /*
     * The string representation of a Condition object
     */
    private String mconditionStr;

    /*
     * A fully qualified field name (includes Object.fieldname)
     */
    private String mfQualField;

    /*
     * A field name without an object name.
     */
    private String mfieldName;
    private String mobjectName;
    private String moperator;
    private String mtable;

    /*
     * Type of field
     */
    private String mtype;
    private Object mvalue;

    /*
     * values[] is used with IN operator.
     */
    private Object[] mvalues;

    static  int PREPARE_IN = 1;
    static  int PREPARE_ATLEAST_ONE = 2;
    static  int PREPARE_NONE = 3;
    private boolean isBlock;
    Condition mCondition;
    private List joinConditionList;
    private String mAlternateSubQuery = "";

    /**
     * Creates a new instance of the Condition class.
     * <p>
     * @param field The qualified field name for the condition (qualified field
     * names are in the format <I>object_name</I>.<I>field_name</I>.
     * @param operator The condition operator. Supported operators include
     * LIKE, =, >, <, >=, and <=.
     * @param value The value for the condition (do not use quotes).
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Condition(String field, String operator, Object value) {
        this(field, operator, value, false);
    }

    /**
     * Returns a String representation of this class
     * @return a String representation of this class
     */

    public String toString() {
        
        String s = " ";
        if ( mvalue != null ) {
            s = "Condition = " + mfQualField + "\nOperator = " + moperator + "\nValue = " + mvalue; 
        }
        if ( mCondition != null ) {
            s = s + "\nCompound condition = " + mCondition;
        }
        if ( joinConditionList != null ) {
                s = s + "\nJoin list = {";
            for ( int j=0; j < joinConditionList.size(); j++ ) {
                s = s + "\n" + joinConditionList.get(j) ;
            }
            s = s + "\n}";
        }
        return s;
    }

    /**
     * Creates a new instance of the Condition class where the operator is
     * = (equals) by default.
     * <p>
     * @param field The qualified field name for the condition (qualified field
     * names are in the format <I>object_name</I>.<I>field_name</I>).
     * @param value The value for the condition (do not use quotes).
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Condition(String field, Object value) {
        this(field, "=", value, false);
    }


    /**
     * Creates a new instance of the Condition class that uses an array of values,
     * and only supports the IN operator.
     * <p>
     * @param field The qualified field name for the condition (qualified field
     * names are in the format <I>object_name</I>.<I>field_name</I>).
     * @param operator The condition operator (currently, IN is the only supported
     * operator).
     * @param values An array of values for the condition.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Condition(String field, String operator, Object[] values) {
        this( field, operator, values, false );
    }

    /**
     * Creates a new instance of the Condition class that uses an array of values,
     * and only supports the IN operator. You can also specify whether to bind the
     * values to a prepared statement.
     * <p>
     * @param field The qualified field name for the condition (qualified field
     * names are in the format <I>object_name</I>.<I>field_name</I>).
     * @param operator The condition operator (currently, IN is the only supported
     * operator).
     * @param values An array of values for the condition.
     * @param prepare A Boolean indicator of whether to bind the values to a prepared
     * statement. Specify <B>true</B> to bind the values; otherwise specify
     * <B>false</B>.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Condition(String field, String operator, Object[] values, boolean prepare) {
        mfQualField = field;
        moperator = "IN";

        //####@@@@
        //mvalues = values;

        mprepare = prepare;

        // if mprepare is already false. do not even bother calculating anything.
        if ( ( !prepare)  || values.length > 30 )  {
            mvalues = values;
            mprepare = false;
            return;
        }

        int length = values.length;

        int size = 0;

        if ( length == 1 ) {
            size = 1;
        }
        else if ( length <= 5 ) {
            size = 5;
        }
        else {
            size = (((int)(length % 10)) > 0 ) ? ((int)(length / 10) + 1 ) * 10 : (int)((int)(length / 10)) * 10 ;
        }
        mvalues = new Object[size];

        for ( int i=0; i < size; i++ ) {
            if ( i < length )  {
                mvalues[i] = values[i];
            }
            else {
                mvalues[i] = values[length-1];
            }
        }
    }


    /**
     * Creates a new instance of the Condition class. This methods allows you to
     * specify whether to bind the values to a prepared statement.
     * <p>
     * @param field The qualified field name for the condition (qualified field
     * names are in the format <I>object_name</I>.<I>field_name</I>).
     * @param operator The condition operator. Supported operators include
     * LIKE, =, >, and <.
     * @param value The value for the condition (do not use quotes).
     * @param prepare A Boolean indicator of whether to bind the values to a
     * prepared statement. Specify <B>true</B> to bind the values; otherwise
     * specify <B>false</B>.
     * <DT><B>Throws:</B><DD>None.
     * @include
     *
     */
    public Condition(String field, String operator, Object value,
            boolean prepare) {
                mfQualField = field;
        moperator = operator;

        mprepare = prepare;

        if (operator.equals("IN")) {
            mvalues = new Object[1];
            mvalues[0] = value;
            //####@@@@
            //mprepare = false;
        } else {
            mvalue = value;
            //####@@@@
            //mprepare = prepare;
        }
    }

    /**
     * This a copy constructor for the Condition class and creates a copy of
     * the specified condition object enclosed in parenthesis. This condition
     * arguement becomes the left condition in a condition container, allowing
     * for nested AND/OR operators in the WHERE clauses of your queries.
     * <p>
     * @param condition The Condition object to copy.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */

    public Condition(Condition condition) {
       this.mCondition = condition;
    }


    /**
     * Reformats a date to a String.
     * <p>
     * @param value The value to reformat.
     * @param returnType The type of value to reformat. You can specify
     * "DATE" or "TIMESTAMP".
     * <DT><B>Throws:</B><DD>None.
     *
     */
     static String formatDate(Object value, String returnType) {
        String datestr = value.toString();

        if (returnType.equals("DATE")) {
            if (value instanceof java.util.Date
                    || value instanceof java.sql.Timestamp) {
                long time = ((java.util.Date) value).getTime();
                java.sql.Date date = new java.sql.Date(time);
                datestr = date.toString();
            } else if (value instanceof java.sql.Date) {
                datestr = value.toString();
            }
        } else if (returnType.equals("TIMESTAMP")) {
            if (value instanceof java.util.Date
                   || value instanceof java.sql.Date) {
                long time = ((java.util.Date) value).getTime();
                java.sql.Timestamp tdate = new java.sql.Timestamp(time);
                datestr = tdate.toString();
            } else if (value instanceof java.sql.Timestamp) {
                datestr = value.toString();
            }
        }

        return datestr;
    }


    /**
     * return String representation of this object.
     *@return String
     */
    /*
    public String toString() {
        String buf = "[ " + mfQualField + " " + moperator + " [ ";

        if (mvalues != null) {
            for (int i = 0; i < mvalues.length; i++) {
                buf += (" " + mvalues[i]);
            }

            buf += "  ] ";
        } else {
            buf += (mvalue + " ] ");
        }

        return buf;
    }
    */

    /**
     * convert this Condition object to a string that can be used in the SQL
     * statement. So all the appropriate type conversions happen here.
     */
    /*
    String getConditionString() {
        if (mconditionStr == null) {
            if (mfieldName == null) {
                parse();
            }

            mtable = MetaDataService.getDBTableName(mobjectName);

            String column = MetaDataService.getColumnName(mobjectName
            + "." + mfieldName);
            mtype = MetaDataService.getColumnType(mobjectName + "."
            + mfieldName);

            StringBuffer buf = new StringBuffer();
            buf.append(" ");
            buf.append(mtable);
            buf.append(".");
            buf.append(column);
            buf.append("  ");

            if (mprepare) {
                buf.append(moperator);
                buf.append(" ? ");
            } else if (mvalue != null) {
                buf.append(moperator);
                buf.append("  ");
                appendValue(buf, mvalue);
            } else if (mvalues != null) {
                buf.append(moperator);
                buf.append("  ");
                appendValues(buf);
            } else {
                buf.append(" is null ");
            }

            mconditionStr = buf.toString();
        }

        return mconditionStr;
    }
    */

    /**
     * Retrieves the field name to be used for this condition in
     * the WHERE clause.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The name of the field to use.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    String getField() {
        if ( this.mCondition != null ) {
           return mCondition.getField();
        }
        return mfQualField;
    }

    /**
     * Retrieves the name of the object to which the field in this condition
     * belongs.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The name of the field's object.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    String getObjectName() {
        if ( this.mCondition != null ) {
           return mCondition.getObjectName();
        }

        return mobjectName;
    }

    /**
     * Retrieves the SQL operator to be used for this condition in the
     * WHERE clause.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The operator for the WHERE clause.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    String getOperator() {
        return moperator;
    }

    /**
     * Retrieves the value to be used for this condition in the WHERE clause.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Object</CODE> - The value for the WHERE clause.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    Object getValue() {
        return mvalue;
    }

    /**
     * Retrieves an array of values to be used for this condition in the
     * WHERE clause.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Object</CODE> - An array of values for the WHERE clause.
     * <DT><B>Throws:</B><DD>None.
     *
     */

    Object getValues() {
        return mvalues;
    }

    /**
     * Retrieves the column type from an instance of the Condition class,
     * based on the path of the condition field.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The type of database column used
     * in the WHERE clause.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    String getType() {
    	if (mtype == null) {
    	   mtype = MetaDataService.getColumnType(mobjectName + "."
                    + mfieldName);
    	}
    	return mtype;
    }

    /**
     * Indicates whether the operator for the condition is IN.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Boolean</CODE> - An indicator of whether the operator in
     * this condition is IN. Boolean <B>true</B> indicates the operator is
     * IN; Boolean <B>false</B> indicates it is not.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    boolean isInOperator() {
        return moperator.equals("IN");
    }

    /**
     * Indicates whether the statement will be prepared.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Boolean</CODE> - An indicator of whether the condition
     * will be prepared. Boolean <B>true</B> indicates the condition will be
     * prepared; Boolean <B>false</B> indicates it will not be prepared.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    boolean isPrepare() {
        return mprepare;
    }

    /**
     * Sets the prepared statement indicator for an instance of the Condition
     * class.
     * <p>
     * @param prepare A Boolean indicator of whether to prepare the statement
     * for the condition. Specify <B>true</B> to prepare the statement;
     * otherwise specify <B>false</B>.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    void setPrepare(boolean prepare) {
        mprepare = prepare;
    }


   

    // ===================================================================
    private void appendValue(StringBuffer buf, Object value) {
        if (mtype.equals("VARCHAR2") || mtype.equals("CHAR")) {
            buf.append("'");
            buf.append(escapeQuote(value));
            buf.append("'");
        } else if (mtype.equals("DATE")) {
            String datestr = Condition.formatDate(value, mtype);
            buf.append(" { d '" + datestr + "' } ");
        } else if (mtype.equals("TIMESTAMP")) {
            String datestr = Condition.formatDate(value, mtype);
            buf.append(" { t '" + datestr + "' } ");
        } else if (mtype.equals("BOOLEAN")) {
            buf.append(convertBoolToNumStr(value));
        } else {
            //  non String
            buf.append(value);
        }
    }


    //
    private void appendValues(StringBuffer buf, boolean prepare ) {
        if (mvalues.length == 0) {
          buf.append("( '' )");
        }
		else {
    	  for (int i = 0; i < mvalues.length; i++) {
            if (i == 0) {
                buf.append(" ( ");
            } else {
                buf.append(" , ");
            }

            if ( prepare ) {
                buf.append(" ? ");
            }
            else {
                Object value = mvalues[i];
                appendValue(buf, value);
            }

            if (i == (mvalues.length - 1)) {
                buf.append(" ) ");
            }
    	  }
        }
    }

   
    private Object escapeQuote(Object value) {
        if (value instanceof java.lang.String && value != null && (((String) value).indexOf('\'') != -1)) {
            return ((String) value).replaceAll("'", "''");
        }
        return value;
    }
    
    /**
     * Java boolean is mapped to Number in database.
     * 
     * @param value Boolean object
     * @return "0" if value is true; "1" if it is false; otherwise null.
     */
    private Object convertBoolToNumStr (Object value) {
    	if (value == null) {
    		return value;
    	}
    	
    	if (((Boolean)value).booleanValue() == true) {
    		return "1";
    	} else {		
    		return "0";
    	}
    }

    /**
     * Adds an existing condition to a generated SQL statement.
     * <p>
     * @param joinType The type of join operator to use between this condition
     * and the previous one.
     * @param condition The condition to add to the statement.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */

    public void addConditions(String joinType, Condition condition ) {
        JoinCondition joinCondition = new JoinCondition(joinType, condition);
        // See if there already some joins
        if ( joinConditionList == null ) {
            joinConditionList = new ArrayList();
            // Push the existing condition values in a new condition.
            // only if it is already not pushed
            if ( this.mCondition == null ) {
                if ( mvalues != null ) {
                    this.mCondition = new Condition(mfQualField, moperator, mvalues);
                }
                else {
                    this.mCondition = new Condition(mfQualField, moperator, mvalue, mprepare);
                    //#### Sanjay
                    this.mCondition.parseTree();
                }
            }
            mfQualField = null;
            moperator = null;
            mvalue = null;
            mprepare = false;
        }
        this.joinConditionList.add(joinCondition);
    }

    /**
     * Adds a new condition to a generated SQL statement.
     * <p>
     * @param joinType The type of join operator to use between this condition
     * and the previous one.
     * @param field The qualified field name for the condition (qualified field
     * names are in the format <I>object_name</I>.<I>field_name</I>).
     * @param operator The condition operator. Supported operators include
     * LIKE, =, >, and <.
     * @param value The value for the condition (do not use quotes).
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void addCondition(String joinType, String field, String operator, Object value) {
        Condition cond = new Condition( field, operator, value );
        addConditions(joinType, cond );
    }

    /**
     * Adds a new condition to a generated SQL statement where the operator within
     * the condition is = (equals) by default.
     * <p>
     * @param joinType The type of join operator to use between this condition and
     * the previous one.
     * @param field The qualified field name for the condition (qualified field
     * names are in the format <I>object_name</I>.<I>field_name</I>.
     * @param value The value for the condition (do not use quotes).
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void addCondition(String joinType, String field, Object value) {
        Condition cond = new Condition( field, value );
        addConditions(joinType, cond );
    }

    /**
     * Adds a new condition to a generated SQL statement using an array of values.
     * This method only supports the IN operator within the new condition.
     * <p>
     * @param joinType The type of join operator to use between this condition
     * and the previous one.
     * @param field The qualified field name for the condition (qualified field
     * names are in the format <I>object_name</I>.<I>field_name</I>).
     * @param operator The condition operator (currently, IN is the only supported
     * operator).
     * @param values An array of values for the condition.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void addCondition(String joinType, String field, String operator, Object[] values) {
        Condition cond = new Condition( field, operator, values );
        addConditions(joinType, cond );

    }

    /**
     * Adds a new condition to a generated SQL statement and specifies whether to
     * bind the value to a prepared statement.
     * <p>
     * @param joinType The type of join operator to use between this condition
     * and the previous one.
     * @param field The qualified field name for the condition (qualified field
     * names are in the format <I>object_name</I>.<I>field_name</I>).
     * @param operator The condition operator. Supported operators include
     * LIKE, =, >, and <.
     * @param value The value for the condition.
     * @param prepare A Boolean indicator of whether bind the value to a prepared
     * statement. Specify <B>true</B> to bind the value; otherwise specify
     * <B>false</B>.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */

    public void addCondition(String joinType, String field, String operator, Object value, boolean prepare) {
        Condition cond = new Condition( field, operator, value, prepare );
        addConditions(joinType, cond );
    }


    /**
     * Retrieves a list of join operators for the conditions.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>List</CODE> - A list of join operators.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    List getJoinConditionList() {
        return this.joinConditionList;
    }

    /**
     * Retrieves the condition descriptor for the prepared statement.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>ConditionDescriptor</CODE> - The condition descriptor.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    ConditionDescriptor getConditionDescriptor() {
        ConditionDescriptor condDesc = new ConditionDescriptor();
        setConditionDescriptor(condDesc);
        return condDesc;
    }


    /**
     *
     * @param condDesc
     *
     * A Condition Object may represent a Condition Expression
     * And to Calculate a Condition Descriptor requires to
     * traverse to all the child Objects. A Condition Descriptor
     * for a Condition represents a Condition Expression.
     */

    void setConditionDescriptor(ConditionDescriptor condDesc) {

        // This will be  called only for Block conditions

        condDesc.appendConditionString(this.getAlternateSubQuery());

        int joinCount = 0;

        if ( joinConditionList != null ) {
            joinCount = joinConditionList.size();
        }

        if ( joinCount > 0 ) {
            condDesc.appendConditionString(" ( ");
        }

        if ( this.mCondition != null ) {
            mCondition.setConditionDescriptor(condDesc);
        }
        else {
                mtable = MetaDataService.getDBTableName(mobjectName);

                String column = MetaDataService.getColumnName(mobjectName
                    + "." + mfieldName);
                mtype = MetaDataService.getColumnType(mobjectName + "."
                    + mfieldName);

                StringBuffer buf = new StringBuffer();
                buf.append(" ");
                buf.append(mtable);
                buf.append(".");
                buf.append(column);
                buf.append("  ");

                if (mvalue != null) {
                    if ( mprepare ) {
                        buf.append(moperator);
                        buf.append(" ? ");
                        condDesc.appendBindParam(this);
                    }
                    else {
                        buf.append(moperator);
                        buf.append("  ");
                        appendValue(buf, mvalue);
                    }
                } else if (mvalues != null) {
                    buf.append(moperator);
                    buf.append("  ");
                    appendValues(buf, mprepare);
                    if ( mprepare ) {
                        condDesc.appendBindParam(this);
                    }
                } else {
                    buf.append(" is null ");
                }

                condDesc.appendConditionString(buf);
        }

        if ( joinCount > 0 ) {
            Iterator itr = joinConditionList.iterator();
            while ( itr.hasNext() ) {
              ((JoinCondition)itr.next()).setConditionDescriptor(condDesc);
            }
        }

        if ( joinCount > 0 ) {
            condDesc.appendConditionString(" ) ");
        }

        if ( !this.getAlternateSubQuery().equals("")) {
            condDesc.appendConditionString(" ) ");
        }
    }


    /**
     * Sets a list of all conditions in a condition expression if
     * the following is true:
     * <p>
     * <UL>
	 * <LI>The condition node is a block.
     * <LI>The condition node's parent is not a block.
     * </UL>
     * <p>
     * This keeps conditions together if they belong to the same object, and helps
     * generate optimized queries.
     * <p>
     * @param list A list of conditions.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     *
     */

    void setConditionsBlockList(List list) {
        // If this guy is a block, do not go any further. No one is interested in indivisual conditions
        if ( isBlock ) {
           list.add(this);
           return;
        }

        if ( this.mCondition != null) {
            mCondition.setConditionsBlockList(list);
        }

        if ( joinConditionList != null ) {
            if ( joinConditionList.size() > 0 ) {
                Iterator itr = joinConditionList.iterator();
                while ( itr.hasNext() ) {
                    ((JoinCondition)itr.next()).getCondition().setConditionsBlockList(list);
                }
            }
        }
    }


    /**
     * Sets a list of all conditions in a condition expression that are
     * leafs in the object tree.
     * <p>
     * @param list A list of conditions.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     *
     */

    void setConditionsLeafList(List list) {

        if ( this.isLeaf() ) {
           list.add(this);
           return;
        }

        if ( this.mCondition != null) {
            mCondition.setConditionsLeafList(list);
        }

        if ( joinConditionList != null ) {
            if ( joinConditionList.size() > 0 ) {
                Iterator itr = joinConditionList.iterator();
                while ( itr.hasNext() ) {
                    ((JoinCondition)itr.next()).getCondition().setConditionsLeafList(list);
                }
            }
        }
    }

    /**
     * Retrieves a subquery for the condition expression. See the description
     * for <B>setAlternateSubQuery</B> for more information.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The subquery for the condition expression.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    String getAlternateSubQuery() {
        return mAlternateSubQuery;
    }

    /**
     * Returns an indicator of whether a condition node is a block.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Boolean</CODE> - A Boolean indicator of whether the node is
     * a block. <B>True</B> indicates the node is a block; <B>false</B> indicates
     * the node is not a block.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    boolean isBlock() {
        return this.isBlock;
    }

    /**
     * Returns an indicator of whether a condition node is a leaf in the object tree.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Boolean</CODE> - A Boolean indicator of whether the node is
     * a leaf. <B>True</B> indicates the node is a leaf; <B>false</B> indicates
     * the node is not a leaf.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    boolean isLeaf() {
        return ( ( this.mCondition == null ) && ( joinConditionList == null ) ) ;
    }

    /**
     * Sets a subquery for the condition expression. If a condition expression
     * contains objects whose fields are part of the select list, part of
     * the condition expression must be converted to a subquery. The SQL
     * constructor decides if the condition should return the condition
     * expression as is or if part of the expression must be transformed into a
     * subquery.
     * <p>
     * @param alternateSubQuery A subquery for the condition expression.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     *
     */

    void setAlternateSubQuery(String alternateSubQuery) {
        if ( alternateSubQuery == null ) this.mAlternateSubQuery = "";
        else this.mAlternateSubQuery = alternateSubQuery;
    }

    void parseTree() {

        if ( this.mCondition != null ) {
            mCondition.parseTree();
        }
        else {
           int lastindex = mfQualField.lastIndexOf('.');
           mfieldName = mfQualField.substring(lastindex + 1);
           mobjectName = mfQualField.substring(0, lastindex);
        }

        Condition condition = null;
        if ( joinConditionList != null ) {
            for ( int i=0; i < joinConditionList.size(); i++ )  {
                condition = ((JoinCondition)joinConditionList.get(i)).getCondition();
                condition.parseTree();
            }
        }
    }

    /**
     *
     * The Condition Tree is traversed to calculate if this node will be
     * considered a Block or not. A Condition Node is considered a Block
     * if all its child nodes belongs to same Object as the this node itself.
	 *
	 * Node.isBlock()
     *
	 *    if Node.isLeaf() then
	 * 	Node.isBlock = true
     *
	 *   else
	 *
	 *      If the Statement A is true then
	 *	   Node.isBlock = true
	 *      else
	 *	   Node.isBlock = false
	 *      end if
	 *   end if
     *
     *
	 * Statement A:
	 *      For each Children of the Node
	 *	  child.ObjectName == node.objectName & child.isBlock = true
	 *      end for
	 *
	 */


    void calcBlock()  {

        String objectName = "";

        this.isBlock = true;

        if ( this.mCondition != null ) {
            mCondition.calcBlock();
            this.isBlock = mCondition.isBlock();
            objectName = mCondition.getObjectName();
        }
        else {
           objectName = getObjectName();
        }

        if ( joinConditionList != null ) {
            Condition condition = null;
            for ( int i=0; i < joinConditionList.size(); i++ )  {
                condition = ((JoinCondition)joinConditionList.get(i)).getCondition();
                condition.calcBlock();
                if (!( condition.isBlock() && condition.getObjectName().equals(objectName) )) {
                    this.isBlock = false;
                }
            }
        }
        if ( this.isBlock && ( this.mobjectName == null ) ) {
            this.mobjectName = objectName;
        }
    }

    /**
     * Three types of status possible
     *  1. One of the node is containing IN  PREPARE_IN
     *  2. At least one node is prepared     PREPARE_ATLEAST_ONE
     *  3. None of the node is prepared      PREPARE_NONE
     *
     * When  get a IN, just return as it takes the precedence.
     * Or else keep traversing. However once ATLEAST_ONE is set,
     * this value if retained.
     *
     */

    int getPrepareStatus()  {

        int prepareStatus = PREPARE_NONE;
        int tmpPrepareStatus = PREPARE_NONE;


        if ( this.mCondition != null ) {
            tmpPrepareStatus = mCondition.getPrepareStatus();
            if ( tmpPrepareStatus == PREPARE_IN ) {
                return PREPARE_IN;
            }
            if ( tmpPrepareStatus == PREPARE_ATLEAST_ONE ) {
                prepareStatus = PREPARE_ATLEAST_ONE;
            }
        }
        else {
            if ( this.isInOperator() && (!this.isPrepare())  ) {
                return PREPARE_IN;
            }
            if ( this.isPrepare() ) {
               prepareStatus = PREPARE_ATLEAST_ONE;
            }
        }

        if ( joinConditionList != null ) {
            Condition condition = null;
            for ( int i=0; i < joinConditionList.size(); i++ )  {
                condition = ((JoinCondition)joinConditionList.get(i)).getCondition();
                tmpPrepareStatus = condition.getPrepareStatus();
                if ( tmpPrepareStatus == PREPARE_IN ) {
                    return PREPARE_IN;
                }
                if ( tmpPrepareStatus == PREPARE_ATLEAST_ONE ) {
                    prepareStatus = PREPARE_ATLEAST_ONE;
                }
            }
        }
        return prepareStatus;
    }

    void propagatePrepareStatus(boolean prepareStatus)  {

        if ( this.mCondition != null ) {
            mCondition.propagatePrepareStatus(prepareStatus);
        }
        else {
            this.mprepare = prepareStatus;
        }

        if ( joinConditionList != null ) {
            Condition condition = null;
            for ( int i=0; i < joinConditionList.size(); i++ )  {
                condition = ((JoinCondition)joinConditionList.get(i)).getCondition();
                condition.propagatePrepareStatus(prepareStatus);
            }
        }
    }

    /**
     *
     * This method will return true if and only if all the Conditions in the
     * Condition Expression are connected by AND Operator. This is irrespective
     * of weather the conditions are in Array structure or in a Condition Tree
     *
     */


    boolean isOldFormatConditionExpressionTree()  {
        if ( mCondition != null ) {
            if ( !mCondition.isOldFormatConditionExpressionTree() ) {
                return false;
            }
        }
        java.util.List joinConditionList = this.getJoinConditionList();

        if ( joinConditionList != null ) {
            JoinCondition joinCondition = null;
            for ( int i = 0; i < joinConditionList.size(); i++ ) {
                joinCondition = (JoinCondition)joinConditionList.get(i);
                if ( !( "AND".equals(joinCondition.getJoinType()) && joinCondition.getCondition().isOldFormatConditionExpressionTree() ) ) {
                    return false;
                }
            }
        }
        else {
           return true;
        }
        return true;

    }

    public boolean equals(Condition theOtherCondition) {

        if ( this.mCondition != null && theOtherCondition.mCondition != null ) {
            if (!mCondition.equals(theOtherCondition.getLeftOperand())) {
                return false;
            }
        } else if ( this.mCondition == null && theOtherCondition.mCondition == null ) {
            if (!this.getField().equals(theOtherCondition.getField()) 
                	|| !this.getObjectName().equals(theOtherCondition.getObjectName())
                	|| this.getOperator() != theOtherCondition.getOperator() 
                	|| this.isPrepare() != theOtherCondition.isPrepare())  {
                return false;
            } else {
                if (this.getOperator().equals("IN")) {
                    if (this.getValues() != null) {
                        if ( theOtherCondition.getValues() == null ) {
                            return false;
                        }
                    }  else {
                        return false;
                    }

                    Object[] values = (Object[])this.getValues();
                    Object[] otherValues = (Object[])theOtherCondition.getValues();

                    if ( values.length != otherValues.length ) {
                        return false;
                    }

                    if ( !this.isPrepare() ) {
                        for (int i = 0; i < values.length; i++) {
                            if ( !values[i].equals(otherValues[i])) {
                                return false;
                            }
                        }
                    }
                } else if ( !this.isPrepare() ) {
                    Object myValue = getValue();
                    Object otherValue = theOtherCondition.getValue();
                    if (myValue != null && otherValue != null) {
                        if ( !myValue.equals(otherValue) ) {
                            return false;
                        }
                    }
                }
            }
        } else {
            return false;
        }

        Condition condition = null;
        Condition otherCondition = null;
        List otherJoinConditionList = theOtherCondition.getJoinConditionList();

        if ( ( this.joinConditionList != null ) && ( otherJoinConditionList != null  ) ) {
            if ( this.joinConditionList.size() != otherJoinConditionList.size() )  {
                return false;
            }
            else {
                for ( int i=0; i < joinConditionList.size(); i++ )  {
                    condition = ((JoinCondition)joinConditionList.get(i)).getCondition();
                    otherCondition = ((JoinCondition)otherJoinConditionList.get(i)).getCondition();
                    if (!condition.equals(otherCondition)) {
                        return false;
                    }
                }
            }
        }  else if ( !( this.joinConditionList == null && theOtherCondition.getJoinConditionList() == null  ) ) {
            return false;
        }
        return true;
    }

    public String hashString() {
        StringBuffer sb = new StringBuffer();
        if ( mvalue != null ) {
            sb.append(mfQualField).append(moperator).append(mobjectName); 
        }
        if ( mCondition != null ) {
            sb.append(mCondition.hashString());
        }
        if ( joinConditionList != null ) {
            for ( int i=0; i < joinConditionList.size(); i++ ) {
                JoinCondition jc = (JoinCondition) joinConditionList.get(i);
                sb.append(jc.hashString());
            }
        }
        return sb.toString();
    }


    public int hashCode() {
        return this.hashString().hashCode(); 
    }
  
   void chains(Condition otherCondition, Hashtable conditionsLookup) {
    	if (this.mCondition != null && otherCondition.mCondition != null) {
            this.mCondition.chains(otherCondition.mCondition, conditionsLookup);
    	} else {
            conditionsLookup.put(otherCondition, this);
        }
        if (joinConditionList != null && otherCondition.joinConditionList != null) {
            List otherJoinList = otherCondition.getJoinConditionList();
            for ( int i=0; i < joinConditionList.size(); i++ )  {
        	Condition condition = ((JoinCondition)joinConditionList.get(i)).getCondition();
        	Condition otherJoinCondition = ((JoinCondition) otherJoinList.get(i)).getCondition();
    		condition.chains(otherJoinCondition, conditionsLookup);
            }
        }
    }
 
    
    /**
     * If the two conditions are EQUAL then a entry
	 * key = this & value = newCondition  is added to the binding parameters.
	 * This is required to replace the old binding of the cached QueryObject
	 * with the new one.
	 *
	 */

    boolean compareWith(Condition otherCondTree, Map bindingAssociation) {
        boolean isSame = true;
        if ( this.mCondition != null && otherCondTree.mCondition != null ) {
            isSame = mCondition.compareWith(otherCondTree.getLeftOperand(),bindingAssociation);
            if ( !isSame ) {
                return false;
            }
        }
        else if ( this.mCondition == null && otherCondTree.mCondition == null ) {
            if (!(       (  this.getField().equals( otherCondTree.getField()) )
                    && (  this.getObjectName().equals( otherCondTree.getObjectName()) )
                    && (  this.getOperator() == otherCondTree.getOperator() )
                    && (  this.isPrepare() == otherCondTree.isPrepare() ))  )  {

                return false;
            }
            else {
                // So far so good.
                // Now check if for values

                if (this.getOperator().equals("IN")) {
                    if (this.getValues() != null) {
                        if ( otherCondTree.getValues() == null ) {
                            return false;
                        }
                    }
                    else {
                        return false;
                    }

                    Object[] values = (Object[])this.getValues();
                    Object[] otherValues = (Object[])otherCondTree.getValues();

                    if ( values.length != otherValues.length ) {
                        return false;
                    }

                    if ( !this.isPrepare() ) {
                        for (int i = 0; i < values.length; i++) {
                            if ( !values[i].equals(otherValues[i])) {
                                return false;
                            }
                        }
                    }
                }
                else if ( !this.isPrepare() ) {
                    if ( !getValue().equals(otherCondTree.getValue()) ) {
                        return false;
                    }
                }
                bindingAssociation.put(this,otherCondTree);
            }
        }
        else {
            return false;
        }

        Condition condition = null;
        Condition otherCondition = null;

        List otherJoinConditionList = otherCondTree.getJoinConditionList();

        if ( ( this.joinConditionList != null ) && ( otherJoinConditionList != null  ) ) {
            if ( this.joinConditionList.size() != otherJoinConditionList.size() )  {
                return false;
            }
            else {
                for ( int i=0; i < joinConditionList.size(); i++ )  {
                    condition = ((JoinCondition)joinConditionList.get(i)).getCondition();
                    otherCondition = ((JoinCondition)otherJoinConditionList.get(i)).getCondition();
                    isSame = condition.compareWith(otherCondition,bindingAssociation);
                    if ( !isSame ) {
                        return false;
                    }
                }
            }
        }
        else if ( !( this.joinConditionList == null && otherCondTree.getJoinConditionList() == null  ) ) {
            return false;
        }
        return true;
    }

    Condition getLeftOperand() {
        return this.mCondition;
    }

    int getParamSize() {
        if ( mvalue != null ) {
            return 1;
        }
        else if ( mvalues != null ) {
            return mvalues.length;
        }
        else {
            return 0;
        }
    }

}
