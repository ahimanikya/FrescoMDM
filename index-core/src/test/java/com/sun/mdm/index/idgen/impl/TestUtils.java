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

package com.sun.mdm.index.idgen.impl; 

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.util.JNDINames;
import com.sun.mdm.index.util.ejbproxy.EJBTestProxy;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 * test util
 * @author gw194542
 */
public class TestUtils {
    private static Context context = null;
    
    /**
     * default constructor
     * @exception NamingException naming exception
     */
    public TestUtils() throws NamingException {
        if (context == null) {
            context = EJBTestProxy.getInitialContext();
        }
    }

    /**
     * get connection
     * @return Connection jdbc connection
     * @exception NamingException naming exception
     * @exception SQLException sql exception
     */
    public Connection getConnection() throws NamingException, SQLException {
        DataSource ds = (DataSource) context.lookup(JNDINames.BBE_DATASOURCE);
        Connection con = ds.getConnection();
        con.setAutoCommit(false);
        return con;
    }

    
    /**
     * clean up database
     * @exception SQLException sql exception
     * @exception NamingException naming exception
     */
    public void clearDB() throws SQLException, NamingException {
        Connection con = getConnection();
        ExecuteMatchHelper executeMatchHelper = new ExecuteMatchHelper(); 
        executeMatchHelper.clearDb(con);
        
    }
}
