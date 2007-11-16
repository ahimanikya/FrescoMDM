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
package com.sun.mdm.index.objects.validation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.regex.Pattern;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.objects.metadata.MetaDataService;
import com.sun.mdm.index.util.ConnectionUtil;
import com.sun.mdm.index.util.Localizer;

/**
 * @author jwu
 */
public class LocalIdValidator implements ObjectValidator {

    /**
     * sbyn_systems does not support id_length, formatm etc yes.
     */
    private static String selectLocalIdSQL
             = "select systemcode, id_length, format "
             + "from sbyn_systems where status = 'A'";

    private static final int MAXIMUM_LENGTH = 40;
    
    private Hashtable mhIdDefs = new Hashtable();
    private final static String DB_PROP_KEY ="resJNDI";
    private final static String DB_PROP_FILE="eviewdb.properties";
    private boolean initialized = false;
    private transient final Localizer mLocalizer = Localizer.get();

    /**
     * Creates a new instance of LocalIdValidator
     */
    private Exception mError = null;

    public LocalIdValidator() {
       
    	
    }

    /**
     * @param node object node
     * @exception ValidationException ObjectException thrown by ObjectNode
     */
    synchronized public void validate(ObjectNode node) throws ValidationException {
        String systemId = null;
        String id = null;
        try {
             init();
        	
        } catch (Exception ex) {
        	 mError = ex;
        }
        
        if (mhIdDefs == null) {
                throw new ValidationException(mLocalizer.t("OBJ668: ID definitions " + 
                                    "are null in Local ID Validator: {0}", mError));
        }
 
        try {
            systemId = (String) node.getValue("SystemCode");
            id = (String) node.getValue("LocalID");
        } catch (ObjectException e) {
            throw new ValidationException(mLocalizer.t("OBJ669: Local ID Validator " + 
                                    "could not retrieve the SystemCode " + 
                                    "or the Local ID: {0}", e));
        }
        if (systemId == null) {
            throw new ValidationException(mLocalizer.t("OBJ670: The value for " + 
                                    "SystemObject[SystemCode] is required."));
        }
        if (id == null) {
            throw new ValidationException(mLocalizer.t("OBJ671: The value for " + 
                                    "SystemObject[LocalID] is required."));
        }
        
        LocalIdDefinition localIdDef = (LocalIdDefinition) mhIdDefs.get(systemId);
        if (localIdDef == null) {
            throw new ValidationException(mLocalizer.t("OBJ672: This is " + 
                                    "not a valid System Code: {0}", systemId));
        }
        localIdDef.validate(id);
    }

     private void init() {
       Connection con = null;
       if (initialized == false) {
    	   
          initialized = true;
          
      
         try {
            con = ConnectionUtil.getConnection();
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectLocalIdSQL);
            while (rs.next()) {
                String systemId = rs.getString(1);
                int lenId = rs.getInt(2);
                if (lenId == 0) {
                    lenId = MetaDataService.getFieldSize("Enterprise.SystemObject.LocalID");
                }
                if (lenId > MAXIMUM_LENGTH) {
                    lenId = MAXIMUM_LENGTH;
                }
                String format = rs.getString(3);
                LocalIdDefinition localIdDef = new LocalIdDefinition(systemId, lenId, format);
                mhIdDefs.put(systemId, localIdDef);
            }
            rs.close();
            stmt.close();
            
          
    	 
        } catch (Exception e) {
            throw new RuntimeException(mLocalizer.t("OBJ673: Could not " + 
                                    "initialize LocalIDValidator: {0}", e));
        }  finally {
          try {
        	con.close();
          } catch (SQLException e) {
              throw new RuntimeException(mLocalizer.t("OBJ674: Could not " + 
                                    "close the database connection: {0}", e));
          }
        }
       }
      
    }

    private class LocalIdDefinition {
        private int localIddLength;
        private String systemId;
        private String format = null;
        private Pattern pattern = null;

        LocalIdDefinition(String systemId, int len, String format) {
            this.systemId = systemId;
            this.localIddLength = len;
            this.format = format;
            if (format != null) {
                this.pattern = Pattern.compile(format);
            }
        }

        void validate(String id) throws ValidationException {

            if (id.length() > localIddLength) {
                throw new ValidationException(mLocalizer.t("OBJ675: The value " + 
                                    "for ID {0} exceeds the maximum length " +
                                    "allowed for a Local ID: {1}", id, localIddLength));
            }
            
            if (pattern != null) {
                if (!pattern.matcher(id).matches()) {
                    throw new ValidationException(mLocalizer.t("OBJ676: The value " + 
                                        "of the Local ID ({0}) does not conform " + 
                                        "to the format of the Local ID for {1}, " +
                                        "which is this pattern \"{2}\"", 
                                        id, systemId, format));
                    
                }
            }
                
        }
    }
}
