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

import java.util.ArrayList;
import java.util.Hashtable;

import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.loader.util.Localizer;
import com.sun.mdm.index.dataobject.validation.ValidationRuleRegistry;

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
import com.sun.mdm.index.objects.validation.FieldDescriptor;
import com.sun.mdm.index.objects.validation.ReferenceDescriptor;
import com.sun.mdm.index.objects.validation.ValueValidator;
import com.sun.mdm.index.objects.validation.PatternValidator;
import com.sun.mdm.index.objects.validation.ObjectDescriptor;
import com.sun.mdm.index.objects.validation.UserCodeValidator;
import com.sun.mdm.index.objects.validation.UserReferenceDescriptor;
import com.sun.mdm.index.objects.validation.FieldType;

/**
 * This class is for DataObjectDescriptor
 * @author cye
 */
public class DataObjectDescriptor extends ObjectDescriptor {

	private static Logger logger = Logger.getLogger("com.sun.mdm.index.dataobject.validation.DataObjectDescriptor");
	private static Localizer localizer = Localizer.getInstance();

    public DataObjectDescriptor(String objectName) {
    	super(objectName);
    }

    public DataObjectDescriptor() {
    	super();
    }
    
    public void validate(ObjectNode objectNode) 
    	throws ValidationException {
    	
        if (objectNode == null) {
            throw new NullObjectException(localizer.t("LDR056: The object node " + "cannot be null."));
        }        
        if (objectNode.isRemoved()) {
            return;
        }
        
        ObjectField[] objectFields = objectNode.pGetFields();
        boolean newObject = objectNode.isNew();
        String objectName = objectNode.pGetTag();

        if (objectFields.length != mFields.size()) {
            throw new ValidationException(localizer.t("LDR057: Internal error: the field " +
                                          			  "counts are mismatched for: {0}", objectName));
        }
        
        for (int i = 0; i < objectFields.length; i++) {
            FieldDescriptor fd = (FieldDescriptor) mFields.get(objectFields[i].getName());
            if (fd == null) {
                throw new ValidationException(localizer.t("LDR058: Validation " +
                                          				  "routine is not defined for {0}[{1}]",
                                          				  objectName, objectFields[i].getName()));
            }

            try {
                fd.validate(objectFields[i], newObject);
            } catch (UnknownDataTypeException e) {
                throw new ValidationException(localizer.t("LDR059: Invalid data " +
                                          			      "type for {0}[{1}]: {2}",
                                          			      objectName, objectFields[i].getName(), e.getMessage()));
            } catch (UpdateNotAllowedException e) {
                throw new ValidationException(localizer.t("LDR060: The value of " +
                                          				  "{0}[{1}] cannot be updated: {2}",
                                          				  objectName, objectFields[i].getName(), e.getMessage()));
            } catch (MissingValueOnRequiredError e) {
                throw new ValidationException(localizer.t("LDR061: A value for " +
                                          				  "{0}[{1}] is required: {2}",
                                          				  objectName, objectFields[i].getName(), e.getMessage()));
            }
            
            if (objectFields[i].getValue() != null) {
                ReferenceDescriptor rd = (ReferenceDescriptor) mReferences.get(objectFields[i].getName());
                if (rd != null) {
                    try {
                        rd.validate(objectFields[i], newObject);
                    } catch (InvalidReferencedCode e) {
                        throw new ValidationException(localizer.t("LDR062: {0} is " +
                                          					      "not a valid code for {1}[{2}].",
                                          					      objectFields[i].getValue(), 
                                          					      objectName,
                                          					      objectFields[i].getName()));
                    } catch (InvalidReferencedModule e) {
                        throw new ValidationException(localizer.t("LDR063: This is " +
                                          						  "not a valid referenced module for {0}[{1}]:{2}",
                                          						  objectName,
                                          						  objectFields[i].getName(),e));
                    }
                }

                ValueValidator vValidator = (ValueValidator) mValues.get(objectFields[i].getName());
                if (vValidator != null) {
                    try {
                        vValidator.validate(objectFields[i]);
                    } catch (MaximumConstraintException e) {
                        throw new ValidationException(localizer.t("LDR064: The value " +
                                      							  "{0} exceeds the maximum length allowed for {1}[{2}]",
                                      							  objectFields[i].getValue(),
                                      							  objectName,
                                      							  objectFields[i].getName()));
                    } catch (MinimumConstraintException e) {
                        throw new ValidationException(localizer.t("LDR065: The value " +
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
                        throw new ValidationException(localizer.t("LDR066: {0} " +
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
                        throw new ValidationException(localizer.t("LDR067: {0} " +
                                  								  "is not a valid code for {1}[{2}]: {3}",
                                  								  objectFields[i].getValue(),
                                  								  objectName,
                                  								  objectFields[i].getName(), e));
                    } catch (InvalidReferencedModule e) {
                        throw new ValidationException(localizer.t("LDR068: This is not " +
                                  								  "a valid referenced module for {0}[{1}]: {2}",
                                  								  objectName,
                                  								  objectFields[i].getName(),e));
                    }
                }
                
                UserCodeValidator ucValidator = (UserCodeValidator) mUserCodes.get(objectFields[i].getName());
                if (ucValidator != null) {
                    ObjectField refField = null;
                    String refFieldName = ucValidator.getReferencedFieldName();
                    try {
                        refField = objectNode.getField(refFieldName);
                    } catch (ObjectException e) {
                        throw new ValidationException(localizer.t("LDR069: {0} " +
                                  								  "is not a valid constraint-by field " + 
                                  								  "name for {1}[{2}]: {3}",
                                  								  refFieldName,
                                  								  objectName,
                                  								  objectFields[i].getName(), e));
                    }
                    if (refField.getType() != FieldType.STRING) {
                        throw new ValidationException(localizer.t("LDR070: The value of {0} " +
                                  								  "must be of type String to be a valid constraint-by " + 
                                  								  "field for {1}[{2}]: {3}",
                                  								  refFieldName,
                                  								  objectName,
                                  								  objectFields[i].getName()));
                    }
                    
                    UserReferenceDescriptor urDescriptor 
                        = (UserReferenceDescriptor) mUserReferences.get(refFieldName);
                    if (urDescriptor == null) {
                        throw new ValidationException(localizer.t("LDR071: {0} " +
                                  								  "is not a valid constraint-by field for field " + 
                                  								  "for {1}[{2}]",
                                  								  refFieldName,
                                  								  objectName,
                                  								  objectFields[i].getName()));
                    }
                        
                    String module = urDescriptor.getModule();
                    String refValue = (String) refField.getValue();
                    if (refValue == null) {
                        throw new ValidationException(localizer.t("LDR072: {0} " +
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
                        throw new ValidationException(localizer.t("LDR073: Pattern " +
                                  								  "mismatch for {0} in {1}[{2}]: {3}",
                                  								  objectFields[i].getValue(),
                                  								  objectName,
                                  								  objectFields[i].getName(), e));
                    } catch (UnknownDataTypeException e) {
                        throw new ValidationException(localizer.t("LDR074: Invalid " +
                                  								  "data type for {0}[{1}]",
                                  								  objectName,
                                  								  objectFields[i].getName()));
                    }
                }
            }
        }
        
        ValidationRuleRegistry reg = ValidationRuleRegistry.getInstance();
        
        ArrayList<ObjectNode> childNodes = objectNode.pGetChildren();
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
