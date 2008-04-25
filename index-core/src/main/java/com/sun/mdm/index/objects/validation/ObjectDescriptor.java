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
import com.sun.mdm.index.util.Localizer;

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
    private transient final Localizer mLocalizer = Localizer.get();
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
            throw new NullObjectException(mLocalizer.t("OBJ681: The field descriptor " +
                                                    "cannot be null."));
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
            throw new NullObjectException(mLocalizer.t("OBJ682: The field descriptor " +
                                                    "cannot be null."));
        }
        if (reference == null) {
            throw new NullObjectException(mLocalizer.t("OBJ683: The reference descriptor " +
                                                    "cannot be null."));
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
            throw new NullObjectException(mLocalizer.t("OBJ684: The field descriptor " +
                                                    "array cannot be null."));
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
            throw new NullObjectException(mLocalizer.t("OBJ685: The field name " +
                                                    "cannot be null."));
        }
        if (validator == null) {
            throw new NullObjectException(mLocalizer.t("OBJ686: The validator " +
                                                    "cannot be null."));
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
            throw new NullObjectException(mLocalizer.t("OBJ687: The reference " +
                                                    "descriptor cannot be null."));
        }
        if (fieldName == null) {
            throw new NullObjectException(mLocalizer.t("OBJ688: The field name " +
                                                    "cannot be null."));
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
            throw new NullObjectException(mLocalizer.t("OBJ689: The reference descriptor " +
                                                    "cannot be null."));
        }
        if (fieldName == null) {
            throw new NullObjectException(mLocalizer.t("OBJ690: The field name " +
                                                    "cannot be null."));
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
            throw new NullObjectException(mLocalizer.t("OBJ691: The field name " +
                                                    "cannot be null."));
        }
        if (validator == null) {
            throw new NullObjectException(mLocalizer.t("OBJ692: The validator " +
                                                    "cannot be null."));
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
            throw new NullObjectException(mLocalizer.t("OBJ693: The field name " +
                                                    "cannot be null."));
        }
        if (validator == null) {
            throw new NullObjectException(mLocalizer.t("OBJ694: The validator " +
                                                    "cannot be null."));
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
            throw new NullObjectException(mLocalizer.t("OBJ695: The object node " +
                                                    "cannot be null."));
        }
        
        if (objectNode.isRemoved()) {
            return;
        }
        
        ObjectField[] objectFields = objectNode.pGetFields();
        boolean newObject = objectNode.isNew();
        String objectName = objectNode.pGetTag();

        if (objectFields.length != mFields.size()) {
            throw new ValidationException(mLocalizer.t("OBJ696: Internal error: the field " +
                                          "counts are mismatched for: {0}", objectName));
        }
        
        for (int i = 0; i < objectFields.length; i++) {
            FieldDescriptor fd = (FieldDescriptor) mFields.get(objectFields[i].getName());
            if (fd == null) {
                throw new ValidationException(mLocalizer.t("OBJ697: Validation " +
                                          "routine is not defined for {0}[{1}]",
                                          objectName, objectFields[i].getName()));
            }

            try {
                fd.validate(objectFields[i], newObject);
            } catch (UnknownDataTypeException e) {
                throw new ValidationException(mLocalizer.t("OBJ698: Invalid data " +
                                          "type for {0}[{1}]: {2}",
                                          objectName, objectFields[i].getName(), e.getMessage()));
            } catch (UpdateNotAllowedException e) {
                throw new ValidationException(mLocalizer.t("OBJ699: The value of " +
                                          "{0}[{1}] cannot be updated: {2}",
                                          objectName, objectFields[i].getName(), e.getMessage()));
            } catch (MissingValueOnRequiredError e) {
                throw new ValidationException(mLocalizer.t("OBJ700: A value for " +
                                          "{0}[{1}] is required: {2}",
                                          objectName, objectFields[i].getName(), e.getMessage()));
            }
            
            if (objectFields[i].getValue() != null) {
                ReferenceDescriptor rd = (ReferenceDescriptor) mReferences.get(objectFields[i].getName());
                if (rd != null) {
                    try {
                        rd.validate(objectFields[i], newObject);
                    } catch (InvalidReferencedCode e) {
                        throw new ValidationException(mLocalizer.t("OBJ701: {0} is " +
                                          "not a valid code for {1}[{2}].",
                                          objectFields[i].getValue(), 
                                          objectName,
                                          objectFields[i].getName()));
                    } catch (InvalidReferencedModule e) {
                        throw new ValidationException(mLocalizer.t("OBJ702: This is " +
                                          "not a valid referenced module for {0}[{1}]:{2}",
                                          objectName,
                                          objectFields[i].getName(),
                                          e));
                    }
                }

                ValueValidator vValidator = (ValueValidator) mValues.get(objectFields[i].getName());
                if (vValidator != null) {
                    try {
                        vValidator.validate(objectFields[i]);
                    } catch (MaximumConstraintException e) {
                        throw new ValidationException(mLocalizer.t("OBJ703: The value " +
                                      "{0} exceeds the maximum length allowed for {1}[{2}]",
                                      objectFields[i].getValue(),
                                      objectName,
                                      objectFields[i].getName()));
                    } catch (MinimumConstraintException e) {
                        throw new ValidationException(mLocalizer.t("OBJ704: The value " +
                                  "{0} the minimum length required for {1}[{2}]",
                                  objectFields[i].getValue(),
                                  objectName,
                                  objectFields[i].getName()));
                    }
                }

                PatternValidator pValidator = (PatternValidator) mPatterns.get(objectFields[i].getName());
                if (pValidator != null) {
                    try {
                        pValidator.validate(objectFields[i]);
                    } catch (PatternMismatchedException e) {
                        throw new ValidationException(mLocalizer.t("OBJ705: {0} " +
                                  "in {1}[{2}] does not match the pattern \"{3}\"",
                                  objectFields[i].getValue(),
                                  objectName,
                                  objectFields[i].getName(),
                                  pValidator.getPattern()));
                    }
                }
                
                UserReferenceDescriptor rDescriptor = (UserReferenceDescriptor) mUserReferences.get(objectFields[i].getName());
                if (rDescriptor != null) {
                    try {
                        rDescriptor.validate(objectFields[i], newObject);
                    } catch (InvalidReferencedCode e) {
                        throw new ValidationException(mLocalizer.t("OBJ706: {0} " +
                                  "is not a valid code for {1}[{2}]: {3}",
                                  objectFields[i].getValue(),
                                  objectName,
                                  objectFields[i].getName(),
                                  e));
                    } catch (InvalidReferencedModule e) {
                        throw new ValidationException(mLocalizer.t("OBJ707: This is not " +
                                  "a valid referenced module for {0}[{1}]: {2}",
                                  objectName,
                                  objectFields[i].getName(),
                                  e));
                    }
                }
                
                UserCodeValidator ucValidator = (UserCodeValidator) mUserCodes.get(objectFields[i].getName());
                if (ucValidator != null) {
                    ObjectField refField = null;
                    String refFieldName = ucValidator.getReferencedFieldName();
                    try {
                        refField = objectNode.getField(refFieldName);
                    } catch (ObjectException e) {
                        throw new ValidationException(mLocalizer.t("OBJ708: {0} " +
                                  "is not a valid constraint-by field " + 
                                  "name for {1}[{2}]: {3}",
                                  refFieldName,
                                  objectName,
                                  objectFields[i].getName(),
                                  e));
                    }
                    if (refField.getType() != FieldType.STRING) {
                        throw new ValidationException(mLocalizer.t("OBJ709: The value of {0} " +
                                  "must be of type String to be a valid constraint-by " + 
                                  "field for {1}[{2}]: {3}",
                                  refFieldName,
                                  objectName,
                                  objectFields[i].getName()));
                    }
                    
                    UserReferenceDescriptor urDescriptor 
                        = (UserReferenceDescriptor) mUserReferences.get(refFieldName);
                    if (urDescriptor == null) {
                        throw new ValidationException(mLocalizer.t("OBJ710: {0} " +
                                  "is not a valid constraint-by field for field " + 
                                  "for {1}[{2}]",
                                  refFieldName,
                                  objectName,
                                  objectFields[i].getName()));
                    }
                        
                    String module = urDescriptor.getModule();
                    String refValue = (String) refField.getValue();
                    if (refValue == null) {
                        throw new ValidationException(mLocalizer.t("OBJ711: {0} " +
                                  "must have value to be a valid constraint-by field " + 
                                  "for {1}[{2}]",
                                  refFieldName,
                                  objectName,
                                  objectFields[i].getName()));
                    }
                    
                    ucValidator.setReferencedField(module, refValue);
                    try {
                        ucValidator.validate(objectFields[i], newObject);
                    } catch (PatternMismatchedException e) {
                        throw new ValidationException(mLocalizer.t("OBJ712: Pattern " +
                                  "mismatch for {0} in {1}[{2}]: {3}",
                                  objectFields[i].getValue(),
                                  objectName,
                                  objectFields[i].getName(),
                                  e));
                    } catch (UnknownDataTypeException e) {
                        throw new ValidationException(mLocalizer.t("OBJ713: Invalid " +
                                  "data type for {0}[{1}]",
                                  objectName,
                                  objectFields[i].getName()));
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
