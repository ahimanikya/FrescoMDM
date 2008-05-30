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

import java.util.regex.Pattern;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.objects.validation.exception.NullObjectException;
import com.sun.mdm.index.objects.validation.exception.InvalidReferencedModule;
import com.sun.mdm.index.objects.validation.exception.InvalidReferencedCode;
import com.sun.mdm.index.objects.validation.exception.InvalidContraintByField;
import com.sun.mdm.index.objects.validation.exception.UnknownDataTypeException;
import com.sun.mdm.index.objects.validation.exception.PatternMismatchedException;
import com.sun.mdm.index.codelookup.UserCode;
import com.sun.mdm.index.codelookup.UserCodeRegistry;
import com.sun.mdm.index.codelookup.CodeLookupException;
import java.util.regex.PatternSyntaxException;
import com.sun.mdm.index.util.Localizer;


/**
 * @author jwu
 */
public class UserCodeValidator implements FieldValidator {

    private String mReferencedFieldName = null;
    private String mReferencedFieldValue = null;
    private String mReferencedFieldModule = null;
    private transient final Localizer mLocalizer = Localizer.get();

    /**
     * Creates a new instance of UserCodeValidator
     */
    public UserCodeValidator(String referencedField) {
        mReferencedFieldName = referencedField;
    }

    public String getReferencedFieldName() {
        return mReferencedFieldName;
    }
    
    public void setReferencedField(String module, String value) {
        mReferencedFieldModule = module;
        mReferencedFieldValue = value;
    }
    
    /**
     * @param field object field
     * @param newObject a new object
     * @throws ValidationException if the value is missing for a required field
     *      or the spcefied filed is null
     * @todo Document this method
     */
    public void validate(ObjectField field, boolean newObject) throws ValidationException {

        if (field.getType() != FieldType.STRING) {
            throw new UnknownDataTypeException(mLocalizer.t("OBJ730: User Code Validator " + 
                                        "encountered an unrecognized type {0} " + 
                                        "for this field: {1}", 
                                        field.getType(), field.getName()));
        }
        
        String value = (String) field.getValue();
        if (value == null) {
            return;
        }

        UserCodeRegistry codeRegistry = null;
        try {
            codeRegistry = UserCodeRegistry.getInstance();
        } catch (CodeLookupException e) {
            throw new ValidationException(mLocalizer.t("OBJ731: Could not " + 
                                        "retrieve the User Code Registry: {0}", e));
        }
        if (mReferencedFieldModule == null) {
            throw new InvalidReferencedModule(mLocalizer.t("OBJ732: Referenced " + 
                                        "field module cannot be null."));
        }
        if (mReferencedFieldValue == null) {
            throw new InvalidReferencedCode(mLocalizer.t("OBJ733: Referenced " + 
                                        "code cannot be null."));
        }
        
        UserCode uc = codeRegistry.getUserCode(mReferencedFieldModule, mReferencedFieldValue);
        if (uc == null) {
            throw new InvalidContraintByField(mLocalizer.t("OBJ734: Could not " + 
                                        "retrieve the user code"));
        }
        String format = uc.getFormat();
        if (format != null) {
            try {
                if (!value.matches(format)) {
                    throw new PatternMismatchedException(mLocalizer.t("OBJ735: Value \"{0}\" " + 
                                        "does not match the pattern \"{1}\"", value, format));
                }
            } catch (PatternSyntaxException e) {
                    throw new ValidationException(mLocalizer.t("OBJ736: Incorrect format \"{0}\"", 
                                        format));
                    
            } catch (NullPointerException e) {
                ; // already checked for null, doesn't happen
            }
        }
    }
}
