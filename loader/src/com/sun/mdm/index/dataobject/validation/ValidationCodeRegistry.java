/**
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
package com.sun.mdm.index.dataobject.validation;

import net.java.hulp.i18n.Logger;

import java.sql.Connection;
import java.sql.SQLException;

import com.sun.mdm.index.codelookup.CodeRegistry;
import com.sun.mdm.index.codelookup.UserCodeRegistry;
import com.sun.mdm.index.codelookup.CodeLookupException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.loader.config.LoaderConfig;
import com.sun.mdm.index.loader.util.Localizer;
import com.sun.mdm.index.loader.clustersynchronizer.dao.DAOFactory;

/**
 *  ValidationCodeRegistry class
 * @author cye
 *
 */
public class ValidationCodeRegistry {
	private static Logger logger = Logger.getLogger("com.sun.mdm.index.dataobject.validation.ValidationCodeRegistry");
	private static Localizer localizer = Localizer.getInstance();

	private static ValidationCodeRegistry instance;
    private UserCodeRegistry userCodeRegistry;
    private CodeRegistry codeRegistry;
    private DAOFactory daoFactory;
    
	/**
	 * Protected Constructor for singleton class
	 */
    protected ValidationCodeRegistry() 
    	throws ValidationException {
    	initialize();
    }
    
    /**
     * Initialize ValidationCodeRegistry
     */
    private void initialize() 
    	throws ValidationException {
    	try {
    		String dataBaseType = LoaderConfig.getInstance().getSystemProperty("cluster.database");
    		daoFactory = DAOFactory.getDAOFactory(dataBaseType);
    		Connection connection = daoFactory.getConnection();
    		codeRegistry = CodeRegistry.getInstance(connection);
    		connection = daoFactory.getConnection();    		
    		userCodeRegistry = UserCodeRegistry.getInstance(connection);    		
    	} catch(CodeLookupException cex) {
    		throw new ValidationException(cex);    		
    	} catch (SQLException sex) {
    		throw new ValidationException(sex);
    	}
    }
    
    /**
     * Gets an instance of ValidationCodeRegistry 
     * @return ValidationCodeRegistry
     */
    synchronized public static ValidationCodeRegistry getInstance() 
    	throws ValidationException {    	
   		if (instance == null) {
   			instance = new ValidationCodeRegistry();
   		}
    	return instance;
    }

    /**
     * Gets UserCodeRegistry
     * @return UserCodeRegistry
     */
    public UserCodeRegistry getUserCodeRegistry() {
    	return userCodeRegistry;
    }
    
    /**
     * Gets CodeRegistry
     * @return CodeRegistry
     */
    public CodeRegistry getCodeRegistry() {
    	return codeRegistry;
    }

    /**
     * Gets connection.
     * @return Connection
     * @throws ValidationException
     */
    public Connection getConnection() 
    	throws ValidationException { 
    	Connection connection = null;
    	try {
    		connection = daoFactory.getConnection();
    		return connection;
    	} catch(SQLException sex) {
    		throw new ValidationException(sex);
    	}
    }
    
}
