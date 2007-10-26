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
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;
import com.sun.mdm.index.util.ConnectionUtil;

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
    private final Logger mLogger = LogUtil.getLogger(this);
    private boolean initialized = false;

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
                throw new ValidationException(mError.getMessage());
        }
 
        try {
            systemId = (String) node.getValue("SystemCode");
            id = (String) node.getValue("LocalID");
        } catch (ObjectException e) {
            throw new ValidationException(e.getMessage());
        }
        if (systemId == null) {
            throw new ValidationException("The value for SystemObject[SystemCode] is required");
        }
        if (id == null) {
            throw new ValidationException("The value for SystemObject[LocalId] is required");
        }
        
        LocalIdDefinition localIdDef = (LocalIdDefinition) mhIdDefs.get(systemId);
        if (localIdDef == null) {
            throw new ValidationException("\"" + systemId + "\", is not a valid System");
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
            
          
    	 
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
           
        } catch (ObjectException e) {
            throw new RuntimeException(e.getMessage());
           
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
           
        }  finally {
          try {
        	con.close();
          } catch (SQLException e) {
              throw new RuntimeException(e.getMessage());
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
                throw new ValidationException("The value, " + id 
                    + ", exceeds the maximum length allowed for "
                    + "SystemObject[LocalId]");
            }
            
            if (pattern != null) {
                if (!pattern.matcher(id).matches()) {
                    throw new ValidationException(systemId, null, format, id, "The value, " + id
                        + ", does not conform to the format of SystemObject[LocalId] for "
                        + systemId + " which is \"" + format + "\"");
                }
            }
                
        }
    }
}
