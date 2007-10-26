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

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.objects.validation.exception.PatternMismatchedException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import com.sun.mdm.index.util.ConnectionUtil;


/**
 * @author jwu
 */
public class AuxIdValidator implements ObjectValidator {

    private static String selectAuxIdDef 
            = "select auxiddef, idtype, idlength, format, variablelength "
                + "from psn_auxiddef "
                + "order by idtype";

    private Hashtable mIdDefs = null;

    /**
     * Creates a new instance of AuxIdValidator
     */
    public AuxIdValidator() {
    }

    /**
     * @param node object node
     * @exception ValidationException ObjectException thrown by ObjectNode
     */
    public void validate(ObjectNode node) throws ValidationException {
        Integer idDefId;
        if (mIdDefs == null) {
            try {
                Connection con = ConnectionUtil.getConnection();
                init(con);
                con.close();
            } catch (Exception e) {
                throw new ValidationException(e.getMessage());
            }
        }

        try {
            idDefId = (Integer) node.getValue("AuxIdDef");
        } catch (ObjectException e) {
            throw new ValidationException(e.getMessage());
        }

        AuxIdDefinition auxIdDef = (AuxIdDefinition) mIdDefs.get(idDefId);
        if (auxIdDef == null) {
            throw new ValidationException("\"" + auxIdDef + "\" is not a valid Aux ID Type");
        }

        try {
            auxIdDef.validate((String) node.getValue("Id"));
        } catch (ObjectException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    private void init(Connection con) {
        mIdDefs = new Hashtable();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectAuxIdDef);
            while (rs.next()) {
                Integer idDef = new Integer(rs.getInt(1));
                AuxIdDefinition auxIdDef = new AuxIdDefinition(idDef, rs.getString(2),
                        rs.getInt(3), rs.getString(4), (rs.getString(5).equals("Y") ? true : false));
                mIdDefs.put(idDef, auxIdDef);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private class AuxIdDefinition {
        private PatternValidator validator = null;
        private int idLength;
        private String idName;
        private Integer idDef;
        private String idType;
        private String pattern;
        private boolean variableLength;

        AuxIdDefinition(Integer idDef, String idType, int idLength, String pattern, boolean variable) {
            this.idDef = idDef;
            this.idType = idType;
            this.pattern = pattern;
            this.idLength = idLength;
            this.variableLength = variable;
            if (pattern != null) {
                this.validator = new PatternValidator(pattern);
            }
        }

        void validate(Object value) throws ValidationException {
            if (value.getClass() != java.lang.String.class) {
                throw new ValidationException("Invalid data type for validation");
            }
            if (((String) value).length() < idLength && !variableLength) {
                throw new ValidationException("The required Length for ID of type \"" + idType + "\" is " 
                    + idLength);
            }
            if (validator != null) {
                try {
                    validator.validate((String) value);
                } catch (PatternMismatchedException e) {
                    throw new ValidationException("The value in ID, \"" + value  
                            + "\" does not match the pattern \"" + this.pattern + "\""); 
                }
            }
        }

    }
}
