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

import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.validation.exception.MissingValueOnRequiredError;
import com.sun.mdm.index.objects.validation.exception.NullObjectException;
import com.sun.mdm.index.objects.validation.exception.UnknownDataTypeException;
import com.sun.mdm.index.objects.validation.exception.UpdateNotAllowedException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.util.Localizer;


/**
 * @author jwu
 */
public class FieldDescriptor implements FieldValidator {

    /**
     * None
     */
    public static final int NONE = 0;
    /**
     * Updatable
     */
    public static final int UPDATEABLE = 1;
    /**
     * Nillable
     */
    public static final int REQUIRED = 2;

    private int iDataType = 0;
    private boolean bUpdateable = false;
    private boolean bRequired = false;
    private String sFieldName = null;

    private transient final Localizer mLocalizer = Localizer.get();
    
    /**
     * Creates a new instance of Field
     *
     * @param name field name
     * @exception ValidationException
     * @throws ValidationException if name is null
     */
    public FieldDescriptor(String name) throws ValidationException {
        if (name == null) {
            throw new NullObjectException(mLocalizer.t("OBJ752: The name " + 
                                            "parameter cannot be null."));
        }
        sFieldName = name;
        iDataType = FieldType.UNKNOWN;
    }

    /**
     * @param name field name
     * @param dataType data type
     * @exception ValidationException
     * @throws ValidationException if name is null
     * @todo Document this constructor
     */
    public FieldDescriptor(String name, int dataType) throws ValidationException {
        this(name);
        setFieldType(dataType);
    }

    /**
     * @param name field name
     * @param dataType data type
     * @param attributes field attributes
     * @exception ValidationException
     * @throws ValidationException if name is null
     * @todo Document this constructor
     */
    public FieldDescriptor(String name, int dataType, int attributes) throws ValidationException {
        this(name);
        setFieldType(dataType);
        setAllAttributes(attributes);
    }

    /**
     * Get field description of this field
     *
     * @return field description of this field
     */
    public String getFieldDescription() {

        if (iDataType < 0 || iDataType > FieldType.MAX_TYPES) {
            return "Unknown";
        }
        return FieldType.DESCRIPTION[iDataType];
    }

    /**
     * Get field name of this field
     *
     * @return field name of this field
     */
    public String getFieldName() {
        return sFieldName;
    }

    /**
     * Get field type of this field
     *
     * @return field type of this field
     */
    public int getFieldType() {
        return iDataType;
    }

    /**
     * Get Required attribute of this field
     *
     * @return true if value required ; false otherwise
     */
    public boolean isRequired() {
        return bRequired;
    }

    /**
     * Get Updateable attribute of this field
     *
     * @return true if this field can be updated; false otherwise
     */
    public boolean isUpdateable() {
        return bUpdateable;
    }

    /**
     * Set all attributes of this FieldDescriptor
     *
     * @param attributes all attributes
     */
    public void setAllAttributes(int attributes) {

        bUpdateable = (attributes & UPDATEABLE) > 0;
        bRequired = (attributes & REQUIRED) > 0;
    }

    /**
     * Set an attribute of this FieldDescriptor
     *
     * @param fieldAttribute attribute
     * @param flag true or false
     */
    public void setAttribute(int fieldAttribute, boolean flag) {
        switch (fieldAttribute) {
        case UPDATEABLE:
            bUpdateable = flag;
            break;
        case REQUIRED:
            bRequired = flag;
            break;
        default:
            break;
        }
    }

    /**
     * Set the FieldType of this FieldDescriptor
     *
     * @param dataType field type
     * @throws ValidationException if data type is not recognized
     */
    public void setFieldType(int dataType) throws ValidationException {

        if (dataType < 0 || dataType > FieldType.MAX_TYPES) {
            throw new UnknownDataTypeException(mLocalizer.t("OBJ645: FieldDescriptor " + 
                                        "encountered an unrecognized data type: {0}", 
                                        dataType));
        }
        iDataType = dataType;
    }

    /**
     * @todo Document this method
     * @return a string of this field descriptor
     */
    public String toString() {

        StringBuffer sb = new StringBuffer(sFieldName + ":");

        sb.append(bUpdateable ? "Updateable" : "Readonly").append(":");
        sb.append(bRequired ? "Mandatory" : "Optional").append(":");
        sb.append(getFieldDescription());
        return sb.toString();
    }

    /**
     * @param field object field
     * @param newObject a new object
     * @throws ValidationException if the value is missing for a required field
     *      or the spcefied filed is null
     * @todo Document this method
     */
    public void validate(ObjectField field, boolean newObject) throws ValidationException {
        if (field == null) {
            throw new NullObjectException(mLocalizer.t("OBJ753: The field " + 
                                            "parameter cannot be null."));
        }

        if (field.getType() != getFieldType()) {
            throw new UnknownDataTypeException(mLocalizer.t("OBJ646: FieldDescriptor " + 
                                        "encountered an unrecognized type {0} " + 
                                        "for this field: {1}", 
                                        field.getType(), field.getName()));
        }

        if (!newObject) {
            if (field.isChanged() && !bUpdateable) {
                throw new UpdateNotAllowedException(mLocalizer.t("OBJ647: Updates " + 
                                        "are not allowed for this field: {0}", 
                                        field.getName()));
            }
        }

        Object value = field.getValue();
        if (value == null) {
            if (isRequired()) {
                throw new MissingValueOnRequiredError(mLocalizer.t("OBJ648: A value " + 
                                        "is required for this field: {0}", 
                                        field.getName()));
            }
        }
    }
}
