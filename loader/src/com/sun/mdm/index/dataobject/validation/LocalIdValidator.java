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

import java.util.Hashtable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.loader.util.Localizer;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.objects.validation.ObjectValidator;
import com.sun.mdm.index.objects.validation.LocalIdValidator.LocalIdDefinition;
import com.sun.mdm.index.objects.metadata.MetaDataService;

import com.sun.mdm.index.loader.config.ValidationConfiguration;

/**
 * This class is validate system local id. 
 * @author cye
 */
public class LocalIdValidator implements ObjectValidator {
	private static Logger logger = Logger.getLogger("com.sun.mdm.index.dataobject.validation.LocalIdValidator");
	private static Localizer localizer = Localizer.getInstance();

    private static final String SELECT_LOCALID_SQL = 
             "select SYSTEMCODE, DESCRIPTION, ID_LENGTH, FORMAT "
             + "from SBYN_SYSTEMS where STATUS = 'A'";
    private static final int MAXIMUM_LOCALID_LENGTH = 40;
    
	private Hashtable<String, LocalIdDefinition> 
			localIdDefinitions = new Hashtable<String, LocalIdDefinition>();

	/**
	 * Constructor
	 */
    public LocalIdValidator() {          
    }
	
    /**
     * Adds a system localId validator.
     * @param systemId	system code.
     * @param idLength	the length of system code. 
     * @param idFormat	the format of system code. 
     */
    public void add(String systemId, String systemDescription, int idLength, String idFormat) {
    	
    	com.sun.mdm.index.objects.validation.LocalIdValidator x = 
    		new com.sun.mdm.index.objects.validation.LocalIdValidator(); 
    	LocalIdDefinition localIdDefinition = x.new LocalIdDefinition(systemId, systemDescription, idLength, idFormat);
    	localIdDefinitions.put(systemId, localIdDefinition);    	
    }

    /**
     * Loads localId validation from sbyn_systems table.
     * @throws ValidationException
     */
    public void load() 
    	throws ValidationException {
    	Connection con = null;       
        try {
             con = ValidationCodeRegistry.getInstance().getConnection();             
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_LOCALID_SQL);
             while (rs.next()) {
                 String systemId = rs.getString(1);
                 String description = rs.getString(2);
                 int lenId = rs.getInt(3);
                 if (lenId == 0) {
                    lenId = MetaDataService.getFieldSize("Enterprise.SystemObject.LocalID");
                 }
                 if (lenId > MAXIMUM_LOCALID_LENGTH) {
                    lenId = MAXIMUM_LOCALID_LENGTH;
                 }
                 String format = rs.getString(4);
                 com.sun.mdm.index.objects.validation.LocalIdValidator x = 
             		new com.sun.mdm.index.objects.validation.LocalIdValidator();                  
                 LocalIdDefinition localIdDefinition = x.new LocalIdDefinition(systemId, description, lenId, format);
                 localIdDefinitions.put(systemId, localIdDefinition);
             }
             rs.close();
             stmt.close();                             	 
         } catch (SQLException sex) {
             throw new ValidationException(localizer.t("LDR075: Could not " + 
                                     				   "initialize LocalIDValidator: {0}", sex));
         } catch (ObjectException oex) {
             throw new ValidationException(localizer.t("LDR076: Could not " + 
                                     			 	   "initialize LocalIDValidator: {0}", oex));             
         }  finally {
        	 try {
        		 if (con != null) {
        			 con.close();
        		 }
        	 } catch (SQLException ignore) {
        	 }
         }
    }
    
    /**
     * Validates system object.
     * @param objectNode	system object.
     * @exception	ValidationException
     */
	public void validate(ObjectNode objectNode) 
		throws ValidationException {
        String systemId = null;
        String id = null;
         
        try {
            systemId = (String) objectNode.getValue("SystemCode");
            id = (String) objectNode.getValue("LocalID");
        } catch (ObjectException oex) {
            throw new ValidationException("LocalIdValidator could not retrieve the SystemCode or the LocalID", oex);
        }
        if (systemId == null) {
            throw new ValidationException("The value of SystemObject[SystemCode] is required.");
        }
        if (id == null) {
            throw new ValidationException("The value of SystemObject[LocalID] is required.");
        }                       
        LocalIdDefinition localIdDefinition = null;
        //always use default localId validator if it is configured.
        localIdDefinition = (LocalIdDefinition)localIdDefinitions.get(ValidationConfiguration.DEFAULT_SYSTEM_CODE);
        if (localIdDefinition == null) {        
        		localIdDefinition = (LocalIdDefinition)localIdDefinitions.get(systemId);
        }
        if (localIdDefinition == null) {
            throw new ValidationException(systemId + " is not a valid system code.");
        }
        localIdDefinition.validate(id);		
	}
	
}
