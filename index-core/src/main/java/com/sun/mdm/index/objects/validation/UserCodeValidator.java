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


/**
 * @author jwu
 */
public class UserCodeValidator implements FieldValidator {

    private String mReferencedFieldName = null;
    private String mReferencedFieldValue = null;
    private String mReferencedFieldModule = null;
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
            throw new UnknownDataTypeException(field.getName());
        }
        
        String value = (String) field.getValue();
        if (value == null) {
            return;
        }

        UserCodeRegistry codeRegistry = null;
        try {
            codeRegistry = UserCodeRegistry.getInstance();
        } catch (CodeLookupException e) {
            throw new ValidationException(e);
        }
        if (mReferencedFieldModule == null) {
            throw new InvalidReferencedModule();
        }
        if (mReferencedFieldValue == null) {
            throw new InvalidReferencedCode();
        }
        
        UserCode uc = codeRegistry.getUserCode(mReferencedFieldModule, mReferencedFieldValue);
        if (uc == null) {
            throw new InvalidContraintByField();
        }
        String format = uc.getFormat();
        if (format != null) {
            try {
                if (!value.matches(format)) {
                    throw new PatternMismatchedException("does not match the pattern \"" + format + "\"");
                }
            } catch (PatternSyntaxException e) {
                    throw new ValidationException("Incorrect format, \"" + format + "\", defined for ");
            } catch (NullPointerException e) {
                ; // already checked for null, doesn't happen
            }
        }
    }
}
