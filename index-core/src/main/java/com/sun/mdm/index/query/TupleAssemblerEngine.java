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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * @author sdua
 */
public class TupleAssemblerEngine implements AssemblerEngine, Cloneable {
    
    private boolean mmoreRows = true;
    private boolean mbegin = false;
    private AssembleDescriptor massDesc;
    private AttributesData mattrsData;
    private Connection mconnection;
    private Statement mstatement;
    private ResultSet mres;
    private String mrootName;
    private SQLDescriptor msqlDesc;
    //private Statement mstatement;
    private ResultObjectAssembler mvalueObjectFactory;
    // Indicates if this class should close the database connection or if 
    // it should be closed by a higher level class.  The default value is 
    // set to true to be backwards-compatible.
    private boolean mCloseDbConnection = true;  


    /**
     * Creates a new instance of AssemblerEngine
     */
    public TupleAssemblerEngine() {
    }


    /**
     * clone the engine. the cloned engine only contains data that is readable only
     * and so can be shared and cached.
     * @return cloned TupleAssemblerEngine
     * @exception CloneNotSupportedException CloneNotSupportedException
     */
    public Object clone()
        throws CloneNotSupportedException {
        TupleAssemblerEngine engine = (TupleAssemblerEngine) super.clone();

        return engine;
    }


    /**
     * close the engine. which in turns closes any database connection it is holding.
     * @exception QMException QMException
     * 
     */
    public void close()
        throws QMException {
        try {
            if (mconnection != null) {
               mres.close();
               mstatement.close();
               if (mCloseDbConnection == true) {
                   mconnection.close();
                   mconnection = null;
               }
            }
        } catch (SQLException sqe) {
            throw new QMException(sqe.getMessage());
        }
    }


    /**
     * finalizer
     *
     * @exception QMException QMException
     */
    public void finalizer()
        throws QMException {
        try {
            super.finalize();

            if (mconnection != null) {
               if (mCloseDbConnection == true) {
                   mconnection.close();
               }
            }
        } catch (SQLException sqe) {
            throw new QMException(sqe);
        } catch (Throwable th) {
            throw new QMException(th);
        }
    }


    /**
     * returns true if has a Next object else returns false
     * @return true if next object exists else false
     * @exception QMException QMException
     */
    public boolean hasNext()
        throws QMException {
        try {
            if (!mbegin) {
                mmoreRows = mres.next();
                mbegin = true;
            }

            return mmoreRows;
        } catch (SQLException sqe) {
            throw new QMException(sqe.getMessage());
        }
    }


    /**
     * initilization of TupleAssemblerEngine
     *
     * @param sqlDescs SQLDescriptor[] that contains SQL statements and their
     *      meta data to be used for initialization.
     * @param assDesc AssembleDescriptor
     * @exception QMException QMException
     */
    public void initCompile(SQLDescriptor[] sqlDescs, AssembleDescriptor assDesc)
        throws QMException {
        try {
            // The array contains only one SQLDescriptor
            msqlDesc = sqlDescs[0];
            massDesc = assDesc;
        } catch (Exception ex) {
            throw new QMException(ex);
        }
    }


    /**
     * This initialization is used during run time. QueryManager passes Connection and
     *  ResultSet[] to the TupleAssemblerEngine so that AssemblerEngine can retrieve data
     * from the database and compose the tuples.
     *
     * @param con Connection
     * @param resultSets ResultSet[]
     * @throws QMException QMException
     */
    public void initRun(Connection con, ResultSet[] resultSets,Statement[] statements, int maxRows)
            throws QMException {
                
        initRun(con, resultSets,statements, maxRows, true);
    }
    
    /**
     * This initialization is used during run time. QueryManager passes Connection and
     *  ResultSet[] to the TupleAssemblerEngine so that AssemblerEngine can retrieve data
     * from the database and compose the tuples.
     *
     * @param con Connection
     * @param resultSets ResultSet[]
     * @param statements Statement[] 
     * @param maxRows  Maximum number of rows.
     * @param closeDbConnection  set to true if the database connection is to be 
     * closed by this class, false if some other calling class is to close the 
     * database connection instead.
     * @throws QMException QMException
     */
    public void initRun(Connection con, ResultSet[] resultSets, Statement[] statements, 
                        int maxRows, boolean closeDbConnection)
            throws QMException {
        try {
        	/*
        	 *  Note we don't need maxRows, so just ignore it
        	 */
            mmoreRows = true;
            mbegin = false;

            // The array contains only one SQLDescriptor
            mconnection = con;
            mCloseDbConnection = closeDbConnection;
            
            mstatement = statements[0];
            mres = resultSets[0];
            mvalueObjectFactory = massDesc.getAssembler();
            mrootName = msqlDesc.getRoot();
            mattrsData = new AttributesData(mres,
                    msqlDesc.getAttributes(mrootName), 0);
        } catch (Exception ex) {
            throw new QMException(ex);
        }
    }


    /**
     * returns next tuple object retrieved from the SQL query.
     * @return next tuple 
     * @exception QMException QMException
     */
    public Object next()
        throws QMException {
        try {
            if (!mmoreRows) {
                return null;
            }

            if (!mbegin) {
                mmoreRows = mres.next();

                if (!mmoreRows) {
                    return null;
                }

                mbegin = true;
            }

            Object rootObject = mvalueObjectFactory.createRoot(mrootName,
                    mattrsData);
            nextRow();

            return rootObject;
        } catch (SQLException sqe) {
            throw new QMException(sqe);
        }
    }

    /**
     * Sets the value of the closeDbConnection class variable
     *
     * @param val  Value to which closeDbConnection is to be set.
     */
    public void setDbConnectionCondition(boolean val) {
        mCloseDbConnection = val;
    }
    /**
     * Gets the value of the closeDbConnection class variable
     *
     * @return Value of closeDbConnection.
     */
    public boolean setDbConnectionCondition() {
        return mCloseDbConnection;
    }

    private boolean nextRow()
        throws SQLException {
        mmoreRows = mres.next();

        return mmoreRows;
    }
}
