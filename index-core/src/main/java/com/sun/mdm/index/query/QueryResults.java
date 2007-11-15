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

import com.sun.mdm.index.util.Localizer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * The <B>QueryResults</B> class is used for tuple creation and for late assembling
 * of tuples. This is returned from QueryManager.execute() when
 * QueryObject.setAssembleDescriptor is set to null. QueryResults wraps the
 * java.sql.ResultSet class.

 *
 * @author sdua
 */
public class QueryResults implements QueryConstants, java.io.Serializable {
    
    private transient final Localizer mLocalizer = Localizer.get();
    
    private AssembleDescriptor massembleDesc;
    private Connection mconnection;
    private int mqueryOption;
    private ResultSet[] mrsets;
    private SQLDescriptor[] msqlDesc;
    private Statement[] mstatements;
    // Indicates if the Assembler engine should close the database connection
    // or if it should be closed by a higher level class.  The default value 
    // is set to true to be backwards-compatible.
    private boolean mCloseDbConnection = true;  


    /**
     * Creates a new instance of QueryResults
     *
     * @param con Connection
     * @param sqlDesc SQLDescriptor[]
     * @param queryOption is MULTI_QUERY or SINGLE_QUERY. default is SINGLE_QUERY
     */

  


    /**
     * Creates a new instance of the QueryResults class.
     * <p>
     * @param con A connection to the database.
     * @param resultSets An array of query results.
     * @param statements An array of SQL statements.
     * @param sqlDesc An array of SQL descriptors.
     * @param queryOption An indicator of the query type. 1 indicates SINGLE_QUERY;
     * 2 indicates MULTIPLE_QUERY. The default is SINGLE_QUERY.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
     QueryResults(Connection con, ResultSet[] resultSets, Statement[] statements,
            SQLDescriptor[] sqlDesc, int queryOption) {
        mconnection = con;
        mrsets = resultSets;
        msqlDesc = sqlDesc;
        mqueryOption = queryOption;
        mstatements = statements;
        mCloseDbConnection = true;
    }

    /**
     * Creates a new instance of the QueryResults class.
     * <p>
     * @param con A connection to the database.
     * @param resultSets An array of query results.
     * @param statements An array of SQL statements.
     * @param sqlDesc An array of SQL descriptors.
     * @param queryOption An indicator of the query type. 1 indicates SINGLE_QUERY;
     * 2 indicates MULTIPLE_QUERY. The default is SINGLE_QUERY.
     * @param closeDbConnection  Set to true if the database connection is to
     * be closed by the Assmbler Engine, false otherwise.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
     QueryResults(Connection con, ResultSet[] resultSets, Statement[] statements,
            SQLDescriptor[] sqlDesc, int queryOption, boolean closeDbConnection) {
        mconnection = con;
        mrsets = resultSets;
        msqlDesc = sqlDesc;
        mqueryOption = queryOption;
        mstatements = statements;
        mCloseDbConnection = closeDbConnection;
    }


    /**
     * Sets the assemble descriptor (AssembleDescriptor class) for the given query
     * results object. The assemble descriptor contains information such as the
     * query results composite object, iterator type, and the assembler to use.
     * <p>
     * @param assDesc The assemble descriptor to use for this query object.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
    */

    public void setAssembleDescriptor(AssembleDescriptor assDesc) {
        massembleDesc = assDesc;

        if ((mqueryOption == SINGLE_QUERY)
                && (massembleDesc.getAssemblerEngine() == null)) {
            massembleDesc.setAssemblerEngine(new TupleAssemblerEngine());
        }
    }


    /**
     * Initiates the tuple assembly process. After performing an internal initialization,
     * this method returns a QMIterator object, from which the tuples can then be retrieved.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>QMIterator</CODE> - An iterator of assembled tuples for the query results
     * object.
     * @exception QMException Thrown if an error occurs while assembling the tuple.
     * @include
     *
     */
    public QMIterator assemble()
            throws QMException {
        try {
      
            QMIterator qmIterator = null;
            if (qmIterator == null) {
                qmIterator = new QMIterator();
            } else {
                qmIterator = (QMIterator) qmIterator.clone();
            }

            qmIterator.initCompile(msqlDesc, massembleDesc);
            // maxRows for QueryResults is ignored because it is not needed
            // during assembly of Tuples. It may have been though already set
            // in the SQL query. So we just pass -1
            qmIterator.initRun(mconnection, mrsets, mstatements, -1, mCloseDbConnection);
            if (mCloseDbConnection == true) {
                mconnection = null; // it does not need mconnection any more. This is to prevent
                                    // closing connection twice from QMIterator and QueryResults
            }

            return qmIterator;
        } catch (Exception ex) {
            throw new QMException(mLocalizer.t("QUE555: assemble() failed."));
        }
    }


    /**
     * Closes the instance of the QueryResults class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DL><DT><B>Returns:</B><DD> <CODE>void</CODE> - None.</DL>
     * @exception QMException Thrown if an error occurs while closing the instance.
     * @include
     */
    public void close() throws QMException {
        try {
            if (mconnection != null) {
            	for (int i = 0; i < mrsets.length; i++) {
                    mrsets[i].close();
                    mstatements[i].close();
                }
                if (mCloseDbConnection == true) {
                    mconnection.close();
                    mconnection = null;
                }
            }
        } catch (SQLException sqe) {
            throw new QMException(mLocalizer.t("QUE556: Could not close database connection."));
        }
    }

    /**
     * get the ResultSets
     *
     * @return ResultSet[]
     */
    public ResultSet[] getResultSets() {
        return mrsets;
    }
}
