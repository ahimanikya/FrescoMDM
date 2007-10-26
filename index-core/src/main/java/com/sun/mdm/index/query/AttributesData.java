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
 * The <B>AttributesData</B> class provides access to the data returned from a
 * generated SQL query through the value object assembler methods (such as those
 * in the classes that implement the ResultObjectAssembler interface).
 * AttributesData is a wrapper for the java.sql.ResultSet class, and is passed by
 * the QueryResultSet class to the result object assembler so the assembler can
 * retrieve the data from the JDBC result set.
 *
 * @author sdua
 */
public class AttributesData {
    
    private String[] mattributeNames;
    private int mrelativeIndex;
    private ResultSet mres;


    /**
     * Creates a new instance of the AttributesData class. This should not be
     * directly created by the client.
     * <p>
     * @param res The JDBC results of a SQL query.
     * @param attributes The attributes of the JDBC results set.
     * @param relativeIndex The position of the attributes data object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    AttributesData(ResultSet res, String[] attributes, int relativeIndex) {
        mattributeNames = attributes;
        mrelativeIndex = relativeIndex;
        mres = res;
    }


    /**
     * Retrieves the information an attribute returned by the Query Manager at
     * the specified position (the <I>i</I>th index). The index ranges from 1
     * to n, where n is the number of attributes for the  object defined in the
     * SELECT portion of the query object. The value of <I>i</I> is the SELECT
     * field name index relative to that object. Use this method instead of
     * get(String name) for faster retrieval.
     * <p>
     * @param index The index of the attribute value.
     * @return <CODE>Object</CODE> - The corresponding object for the <I>i</I>th
     * index.
     * @exception SQLException Thrown if an error occurs while retrieving the attribute
     * data.
     * @include
     */
    public Object get(int index)
        throws SQLException {
        return mres.getObject(mrelativeIndex + index + 1);
    }


    /**
     * Retrieves the information for an attribute returned by the Query Manager
     * using the name of the attribute.
     * <p>
     * @param name The name of the attribute to retrieve.
     * @return <CODE>Object</CODE> - The data corresponding to the attribute name.
     * @exception SQLException Thrown if an error occurs while retrieving the attribute
     * data.
     * @include
     */
    public Object get(String name)
        throws SQLException {
        return mres.getObject(name);
    }


    /**
     * Retrieves the attribute names in the current instance of the AttributesData
     * class, calling ResultSet methods to retrieve the data.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String[]</CODE> - An array of attribute names.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String[] getAttributeNames() {
        return mattributeNames;
    }


    /**
     * Retrieves a count of the attribute names in the current instance of
     * the AttributesData class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>int</CODE> - The number of attribute names.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public int size() {
        return mattributeNames.length;
    }


    /**
     * Sets the attribute names in the current instance of the AttributesData
     * class.
     * <p>
     * @param attributeNames A list of attribute names.
     * @return <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    void setAttributeNames(String[] attributeNames) {
        mattributeNames = attributeNames;
    }
}
