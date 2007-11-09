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
 * The <B>QMIterator</B> class is called by the Query Manager to iterate through
 * a composite object list.
 *
 * @author sdua
 */
public class QMIterator implements java.io.Serializable, Cloneable {
    
    private AssemblerEngine masmEngine;
    private AssembleDescriptor massDesc;
    private String mrootObjectName;
    private SQLDescriptor[] msqlDesc;


    /**
     * Creates a new instance of the QMIterator class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DL><DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public QMIterator() {
    }


    /**
     * Retrieves the name of the root object from an instance of QMIterator.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The root object name.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getRootObjectName() {
        return mrootObjectName;
    }


    /**
     * Clones the given instance of the QMIterator class. This method is internal
     * to the Query Manager framework.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Object</CODE> - A copy of the given QMIterator object.
     * @exception CloneNotSupportedException Thrown to indicate that the object
     * could not be cloned.
     * @include
     */
    public Object clone()
        throws CloneNotSupportedException {
        QMIterator iterator = (QMIterator) super.clone();

        if (masmEngine != null) {
            iterator.masmEngine = (AssemblerEngine) masmEngine.clone();
        }

        return iterator;
    }


    /**
     * Closes the connection to the database.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DL><DT><B>Returns:</B><DD> <CODE>void</CODE> - None.</DL>
     * @exception QMException Thrown if an error occurs while closing the connection.
     * @include
     */
    public void close()
        throws QMException {
        masmEngine.close();
    }


    /**
     * Returns a Boolean indicator of whether the iterator contains a next object
     * to retrieve.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Boolean</CODE> - An indicator of whether there is another
     * object to retrieve. <B>True</B> indicates there is an object to retrieve;
     * <B>false</B> indicates there is not.
     * @exception QMException Thrown if an error occurs while checking for the
     * next object in the iterator.
     * include
     *
     */
    public boolean hasNext()
        throws QMException {
        boolean more = masmEngine.hasNext();

        if (!more) {
            masmEngine.close();
             QueryManagerImpl.decrementConnectionCounter();
        }

        return more;
    }


    /**
     * Retrieves the next object constructed by the assembler engine.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Object</CODE> - The next object in the iterator.
     * @exception QMException Thrown if an error occurs while trying to retrieve
     * the next object.
     * @include
     */
    public Object next()
        throws QMException {
        Object object = masmEngine.next();

        if (object == null) {
            masmEngine.close();
        }

        return object;
    }


    /*
     *  This method initializes the data structures that can be shared and cached
     *  (only readable data structures)
     */
    void initCompile(SQLDescriptor[] sqlDesc, AssembleDescriptor assDesc)
        throws QMException {
        msqlDesc = sqlDesc;
        massDesc = assDesc;
        masmEngine = assDesc.getAssemblerEngine();

        if (masmEngine == null) {
            masmEngine = new AssemblerEngineImpl();
            assDesc.setAssemblerEngine(masmEngine);
        }

        masmEngine.initCompile(sqlDesc, assDesc);
        mrootObjectName = sqlDesc[0].getRoot();
    }

    /*
     *This method initializes the data structures that are used during run time and can
     * be writeable.
    */

    void initRun(Connection con, ResultSet[] resultSets, Statement[] statements, int maxRows)
            throws QMException {
        //masmEngine = massDesc.getAssemblerEngine();
        initRun(con, resultSets, statements, maxRows, true);
    }

    /**
     * This method initializes the data structures that are used during run time 
     * and can be writeable.
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
            throws QMException {
        masmEngine.initRun(con, resultSets, statements, maxRows, closeDbConnection);
    }
    
    void setAssemblerEngine(AssemblerEngine assEngine) {
    	masmEngine = assEngine;
    }

    AssemblerEngine getAssemblerEngine() {
    	return masmEngine;
    }
}
