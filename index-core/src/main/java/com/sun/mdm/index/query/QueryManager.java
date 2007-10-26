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
import java.sql.Connection;


/**
 * The <B>QueryManager</B> class executes the SQL queries against the master
 * index database, providing a single point of access to database queries. The
 * Query Manager reads an instance of the QueryObject class in order
 * to execute the SQL query. It then calls the AssemblerEngine class, which
 * uses the value object assembler specified in QueryObject to construct a
 * Query Manager iterator (class QMIterator). The iterator is used to iterate
 * through a collection of composite value objects. The composite value objects
 * are used by other eView components, such as the match engine and the Enterprise
 * Data Manager.
 * <p>
 * The Query Manager operates in two modes, execution mode and assembling mode. In
 * execution mode, the Query Manager parses and executes the SQL statements, using
 * information from the query object. In assembling mode, the Query Manager assembles
 * the JDBC result set into the set of objects that can be used by the client. The
 * resulting composite objects can be of any user-defined structure.
 *
 *@author sdua
 */


public interface QueryManager {

    /**
     * Executes the query or queries specified in the QueryObject class, and
     * uses the assembler engine to create an iterator of the objects returned
     * by the queries.
     * <p>
     * @param qobject The query object containing the criteria and conditions
     * of the queries to execute.
     * @return <CODE>QMIterator</CODE> - An iterator that can be used to
     * retrieve each object returned from the queries.
     * @exception QMException Thrown if an error occurs while executing the
     * query or assembling the results.
     * @include
     */
     QMIterator executeAssemble(QueryObject qobject)
        throws QMException;
    
    /**
     * Executes the query or queries specified in the QueryObject class, and
     * uses the assembler engine to create an iterator of the objects returned
     * by the queries.
     * <p>
     * @param connection The database connection used for this operation.
     * @param qobject The query object containing the criteria and conditions
     * of the queries to execute.
     * @return <CODE>QMIterator</CODE> - An iterator that can be used to
     * retrieve each object returned from the queries.
     * @exception QMException Thrown if an error occurs while executing the
     * query or assembling the results.
     * @include
     */
    QMIterator executeAssemble(Connection con, QueryObject qobject)
            throws QMException;

    /**
     * Executes the query or queries specified in the QueryObject class, but
     * does not assemble the returned objects into an iterator. The assembling
     * information can later be specified to the returned QueryResults object
     * using the AssembleDescriptor class.
     * <p>
     * @param qobject The query object containing the criteria and conditions
     * of the queries to execute.
     * @return <CODE>QueryResults</CODE> - The objects that were returned as a
     * result of the queries.
     * @exception QMException Thrown if an error occurs while executing the
     * query.
     * @include
     */
     QueryResults execute(QueryObject qobject)
        throws QMException;

    /**
     * Executes the query or queries specified in the QueryObject class, but
     * does not assemble the returned objects into an iterator. The assembling
     * information can later be specified to the returned QueryResults object
     * using the AssembleDescriptor class.
     * <p>
     * @param connection The database connection used for this operation.
     * @param qobject The query object containing the criteria and conditions
     * of the queries to execute.
     * @return <CODE>QueryResults</CODE> - The objects that were returned as a
     * result of the queries.
     * @exception QMException Thrown if an error occurs while executing the
     * query.
     * @include
     */
     QueryResults execute(Connection con, QueryObject qobject)
        throws QMException;



    /**
     * Prepares and caches the query object (class QueryObject) without executing
     * the queries. The prepared query is set in PreparedConnectionPool. If you
     * prepare and cache a query object (meaning the PreparedId is set) and then
     * call the <b>execute</b> method, the query object is prepared before execution
     * (if it is not already in pool). This method is optional.
     * <p>
     * @param qobject The query object containing the criteria and conditions
     * of the queries to execute.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception QMException Thrown if an error occurs while preparing the
     * query object.
     * @include
     */
     void prepare(QueryObject qobject)
        throws QMException;
}
