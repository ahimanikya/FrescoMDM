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
import java.sql.Statement;

/**
 * @author sdua
 */
public interface AssemblerEngine {
    
    /**
     * This is used during compilation and any data structures are cached. So
     * the data strcutures should be read only and can be shared.
     *
     * @param sqlDescs SQLDescriptor[] that contains SQL statements and their
     *      meta data to be used for initialization.
     * @param assDesc AssembleDescriptor
     * @throws QMException QMException
     */
     void initCompile(SQLDescriptor[] sqlDescs, AssembleDescriptor assDesc)
        throws QMException;


   
    /**
     * This initialization is used during run time. QueryManager passes Connection and
     *  ResultSet[] to the AssemblerEngine so that AssemblerEngine can retrieve data
     * from the database and compose the objects.
     *
     * @param con Connection
     * @param resultSets ResultSet[]
     * @param statements Statement[]
     * @throws QMException QMException
     */
      void initRun(Connection con, ResultSet[] resultSets, Statement[] statements, int maxRows)
        throws QMException;

    /**
     * This initialization is used during run time. QueryManager passes Connection and
     *  ResultSet[] to the AssemblerEngine so that AssemblerEngine can retrieve data
     * from the database and compose the objects.
     *
     * @param con Connection
     * @param resultSets ResultSet[]
     * @param statements Statement[]
     * @param maxRows maximum number of rows
     * @param closeDbConnection  set to true if the database connection is to be 
     * closed by the AssemblerEngine, false if some other calling class is to close 
     * the database connection instead.
     * @throws QMException QMException
     */
      void initRun(Connection con, ResultSet[] resultSets, Statement[] statements, 
                   int maxRows, boolean closeDbConnection)
        throws QMException;

    /**
     * returns next root object
     * @return next root object
     * @throws QMException QMException
     */
     Object next()
        throws QMException;


    /**
     * returns true if has a Next object else returns false
     * @return true of false
     *
     * @throws QMException QMException
     */
     boolean hasNext()
        throws QMException;


    /**
     * close the Engine
     *
     * @throws QMException QMException  
     */
     void close()
        throws QMException;


    /**
     * @todo Document this method
     * @return clone of this object.
     * @throws CloneNotSupportedException CloneNotSupportedException
     */
     Object clone()
        throws CloneNotSupportedException;
}
