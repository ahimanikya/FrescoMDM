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

import java.util.ArrayList;
import java.util.Hashtable;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.validation.exception.NullObjectException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.objects.validation.exception.MissingValueOnRequiredError;
import com.sun.mdm.index.objects.validation.exception.UpdateNotAllowedException;
import com.sun.mdm.index.objects.validation.exception.UnknownDataTypeException;
import com.sun.mdm.index.objects.validation.exception.InvalidReferencedCode;
import com.sun.mdm.index.objects.validation.exception.InvalidReferencedModule;
import com.sun.mdm.index.objects.validation.exception.PatternMismatchedException;
import com.sun.mdm.index.objects.validation.exception.MinimumConstraintException;
import com.sun.mdm.index.objects.validation.exception.MaximumConstraintException;
import com.sun.mdm.index.util.LogUtil;
/**
 * @author jwu
 */
public class ObjectDescriptor {

    //private ArrayList mFields;
    private Hashtable mFields;
    private String mObjectName;
    private Hashtable mPatterns;
    private Hashtable mReferences;
    private Hashtable mUserReferences;
    private Hashtable mValues;
    private Hashtable mUserCodes;

    /**
     * Creates a new instance of ObjectDescriptor
     *
     * @param objectName name of the object
     */
    public ObjectDescriptor(String objectName) {
        this();
        mObjectName = objectName;
    }

    /**
     * @todo Document this constructor
     */
    public ObjectDescriptor() {
        mFields = new Hashtable();
        mReferences = new Hashtable();
        mValues = new Hashtable();
        mPatterns = new Hashtable();
        mUserReferences = new Hashtable();
        mUserCodes = new Hashtable();
    }

    /**
     * Get the object name of this ObjectDescriptor
     *
     * @return the object name
     */
    public String getObjectName() {
        return mObjectName;
    }

    /**
     * Set the object name of this ObjectDescriptor
     *
     * @param objectName object name
     */
    public void setObjectName(String objectName) {
        mObjectName = objectName;
    }

    /**
     * Adds a FieldDescriptor to this ObjectDescriptor object
     *
     * @param fldDesc a FieldDescriptor
     * @throws ValidationException NullObjectException
     */
    public void addFieldDescriptor(FieldDescriptor fldDesc) throws ValidationException {
        if (fldDesc == null) {
            throw new NullObjectException("Null field descriptor");
        }
        mFields.put(fldDesc.getFieldName(), fldDesc);
    }

    /**
     * Adds a FieldDescriptor and a ReferenceDescriptor to this ObjectDescriptor
     * object
     *
     * @param fldDesc a FieldDescriptor
     * @param reference a referenceDescriptor
     * @throws ValidationException NullObjectException
     */
    public void addFieldDescriptor(FieldDescriptor fldDesc, ReferenceDescriptor reference) throws ValidationException {
        if (fldDesc == null) {
            throw new NullObjectException("Null field descriptor");
        }
        if (reference == null) {
            throw new NullObjectException("Null reference descriptor");
        }
        mFields.put(fldDesc.getFieldName(), fldDesc);
        addReferenceDescriptor(fldDesc.getFieldName(), reference);
    }

    /**
     * Adds a list of FieldDescriptor to this ObjectDescriptor object
     *
     * @param fldDescArray an ArrayList of FieldDescriptor
     * @throws ValidationException NullObjectException
     */
    public void addFieldDescriptor(ArrayList fldDescArray) throws ValidationException {
        if (fldDescArray == null) {
            throw new NullObjectException("Null field descriptor array");
        }
        for (int i = 0; i < fldDescArray.size(); i++) {
            FieldDescriptor fd = (FieldDescriptor) fldDescArray.get(i);
            mFields.put(fd.getFieldName(), fd);
        }
    }

    /**
     * Assign a ValueValidator to a field
     *
     * @param fieldName field name
     * @param validator a PatternValidator
     * @throws ValidationException NullObjectException
     */
    public void addPatternValidator(String fieldName, PatternValidator validator) throws ValidationException {
        if (fieldName == null) {
            throw new NullObjectException("Null field name specified");
        }
        if (validator == null) {
            throw new NullObjectException("Null validator specified");
        }
        mPatterns.put(fieldName, validator);
    }

    /**
     * Adds a ReferenceDescriptor to this ObjectDescriptor object
     *
     * @param fieldName field name
     * @param reference a referenceDescriptor
     * @throws ValidationException NullObjectException
     */
    public void addReferenceDescriptor(String fieldName, ReferenceDescriptor reference) throws ValidationException {
        if (reference == null) {
            throw new NullObjectException("Null reference descriptor");
        }
        if (fieldName == null) {
            throw new NullObjectException("Null field name");
        }

        mReferences.put(fieldName, reference);
    }

    /**
     * Adds a UserReferenceDescriptor to this ObjectDescriptor object
     *
     * @param fieldName field name
     * @param reference a referenceDescriptor
     * @throws ValidationException NullObjectException
     */
    public void addUserReferenceDescriptor(String fieldName, UserReferenceDescriptor reference) throws ValidationException {
        if (reference == null) {
            throw new NullObjectException("Null reference descriptor");
        }
        if (fieldName == null) {
            throw new NullObjectException("Null field name");
        }

        mUserReferences.put(fieldName, reference);
    }

    /**
     * Assign a ValueValidator to a field
     *
     * @param fieldName field name
     * @param validator a ValueValidator
     * @throws ValidationException NullObjectException
     */
    public void addValueValidator(String fieldName, ValueValidator validator) throws ValidationException {
        if (fieldName == null) {
            throw new NullObjectException("Null field name specified");
        }
        if (validator == null) {
            throw new NullObjectException("Null validator specified");
        }
        mValues.put(fieldName, validator);
    }

    /**
     * Assign a ValueValidator to a field
     *
     * @param fieldName field name
     * @param validator a ValueValidator
     * @throws ValidationException NullObjectException
     */
    public void addUserCodeValidator(String fieldName, UserCodeValidator validator) throws ValidationException {
        if (fieldName == null) {
            throw new NullObjectException("Null field name specified");
        }
        if (validator == null) {
            throw new NullObjectException("Null validator specified");
        }
        mUserCodes.put(fieldName, validator);
    }

    /**
     * @todo Document this method
     * @return a string that describes this ObjectDescriptor
     */
    public String toString() {
        return getObjectName() + "\n" + LogUtil.wrapString(mFields.toString()) 
        + "\n" + LogUtil.wrapString(mReferences.toString()) + "\n"
                 + LogUtil.wrapString(mValues.toString());
    }

    /**
     * Validate an object
     *
     * @param objectNode object node
     * @throws ValidationException Field counts mismatched,
     */
    public void validate(ObjectNode objectNode) throws ValidationException {

        if (objectNode == null) {
            throw new NullObjectException("Null object node");
        }
        
        if (objectNode.isRemoved()) {
            return;
        }
        
        ObjectField[] objectFields = objectNode.pGetFields();
        boolean newObject = objectNode.isNew();
        String objectName = objectNode.pGetTag();

        if (objectFields.length != mFields.size()) {
            throw new ValidationException("Internal error: Field counts mismatched for " 
                + objectName);
        }
        
        for (int i = 0; i < objectFields.length; i++) {
            FieldDescriptor fd = (FieldDescriptor) mFields.get(objectFields[i].getName());
            if (fd == null) {
                throw new ValidationException("Validation routine not defined for "
                         + objectName + "[" + objectFields[i].getName() + "]");
            }

            try {
                fd.validate(objectFields[i], newObject);
            } catch (UnknownDataTypeException e) {
                throw new ValidationException("Invalid data type for "
                         + objectName + "[" + objectFields[i].getName() + "]");
            } catch (UpdateNotAllowedException e) {
                throw new ValidationException("The value of " 
                    + objectName + "[" + objectFields[i].getName() + "]"
                    + " cannot be updated");
            } catch (MissingValueOnRequiredError e) {
                throw new ValidationException("A value for " 
                    + objectName + "[" + objectFields[i].getName() + "]"
                    + " is required");
            }
            
            if (objectFields[i].getValue() != null) {
                ReferenceDescriptor rd = (ReferenceDescriptor) mReferences.get(objectFields[i].getName());
                if (rd != null) {
                    try {
                        rd.validate(objectFields[i], newObject);
                    } catch (InvalidReferencedCode e) {
                        throw new ValidationException("\"" + objectFields[i].getValue() + "\"" 
                            + " is not a valid code for "
                            + objectName + "[" + objectFields[i].getName() + "]");
                    } catch (InvalidReferencedModule e) {
                        throw new ValidationException("\"" + e.getMessage() + "\"" 
                            + " is not a valid referenced module for "
                            + objectName + "[" + objectFields[i].getName() + "]");
                    }
                }

                ValueValidator vValidator = (ValueValidator) mValues.get(objectFields[i].getName());
                if (vValidator != null) {
                    try {
                        vValidator.validate(objectFields[i]);
                    } catch (MaximumConstraintException e) {
                        if (objectFields[i].getType() == FieldType.STRING) {
                            throw new ValidationException("The value, "
                                + objectFields[i].getValue() 
                                + ", exceeds the maximum length allowed for "
                                + objectName + "[" + objectFields[i].getName() + "]");
                        } else {
                            throw new ValidationException("The value, "
                                + objectFields[i].getValue() 
                                + ", exceeds the maximum value allowed for "
                                + objectName + "[" + objectFields[i].getName() + "]");
                        }
                    } catch (MinimumConstraintException e) {
                        if (objectFields[i].getType() == FieldType.STRING) {
                            throw new ValidationException("The value, "
                                + objectFields[i].getValue() 
                                + ", failed the minimum length required for "
                                + objectName + "[" + objectFields[i].getName() + "]");
                        } else {
                            throw new ValidationException("The value, "
                                + objectFields[i].getValue() 
                                + ", failed the minimum value required for "
                                + objectName + "[" + objectFields[i].getName() + "]");
                        }
                    }
                }

                PatternValidator pValidator = (PatternValidator) mPatterns.get(objectFields[i].getName());
                if (pValidator != null) {
                    try {
                        pValidator.validate(objectFields[i]);
                    } catch (PatternMismatchedException e) {
                        throw new ValidationException("\"" + objectFields[i].getValue() + "\" in "
                            + objectName + "[" + objectFields[i].getName() + "]"
                            + " does not match the pattern \"" + pValidator.getPattern() + "\""); 
                    }
                }
                
                UserReferenceDescriptor rDescriptor = (UserReferenceDescriptor) mUserReferences.get(objectFields[i].getName());
                if (rDescriptor != null) {
                    try {
                        rDescriptor.validate(objectFields[i], newObject);
                    } catch (InvalidReferencedCode e) {
                        throw new ValidationException("\"" + objectFields[i].getValue() + "\"" 
                            + " is not a valid code for "
                            + objectName + "[" + objectFields[i].getName() + "]");
                    } catch (InvalidReferencedModule e) {
                        throw new ValidationException("\"" + e.getMessage() + "\"" 
                            + " is not a valid referenced module for "
                            + objectName + "[" + objectFields[i].getName() + "]");
                    }
                }
                
                UserCodeValidator ucValidator = (UserCodeValidator) mUserCodes.get(objectFields[i].getName());
                if (ucValidator != null) {
                    ObjectField refField = null;
                    String refFieldName = ucValidator.getReferencedFieldName();
                    try {
                        refField = objectNode.getField(refFieldName);
                    } catch (ObjectException e) {
                        throw new ValidationException("\"" + refFieldName + "\"" 
                            + " is not a valid constraint-by field name for "
                            + objectName + "[" + objectFields[i].getName() + "]");
                    }
                    if (refField.getType() != FieldType.STRING) {
                        throw new ValidationException("The value of \"" + refFieldName 
                            + "\" must be of type STRING to be a valid constraint-by field for "
                            + objectName + "[" + objectFields[i].getName() + "]");
                    }
                    
                    UserReferenceDescriptor urDescriptor 
                        = (UserReferenceDescriptor) mUserReferences.get(refFieldName);
                    if (urDescriptor == null) {
                        throw new ValidationException("\"" + refFieldName + "\"" 
                            + " is not a valid constraint-by field for "
                            + objectName + "[" + objectFields[i].getName() + "]");
                    }
                        
                    String module = urDescriptor.getModule();
                    String refValue = (String) refField.getValue();
                    if (refValue == null) {
                        throw new ValidationException("\"" + refFieldName + "\"" 
                            + " must have value to be a valid constraint-by field for "
                            + objectName + "[" + objectFields[i].getName() + "]");
                    }
                    
                    ucValidator.setReferencedField(module, refValue);
                    try {
                        ucValidator.validate(objectFields[i], newObject);
                    } catch (PatternMismatchedException e) {
                        throw new ValidationException("\"" + objectFields[i].getValue() + "\" in "
                            + objectName + "[" + objectFields[i].getName() + "] "
                            + e.getMessage()); 
                    } catch (UnknownDataTypeException e) {
                        throw new ValidationException("Invalid data type for "
                             + objectName + "[" + objectFields[i].getName() + "]");
                    }
                }
            }
        }
        
        ValidationRuleRegistry reg = ValidationRuleRegistry.getInstance();
        ObjectValidator customValidator = reg.getCustomValidatorByObject(objectName);
        if (customValidator != null) {
            customValidator.validate(objectNode);
        }
        
        ArrayList childNodes = objectNode.pGetChildren();
        if (childNodes != null) {
            for (int i = 0; i < childNodes.size(); i++) {
                ObjectNode childObject = (ObjectNode) childNodes.get(i);
                String childObjectName = childObject.pGetTag();
                ObjectDescriptor odsc = reg.getObjectDescriptor(childObjectName);
                odsc.validate(childObject);
            }
        }
    }
}
