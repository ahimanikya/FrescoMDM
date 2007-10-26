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

import com.sun.mdm.index.codelookup.CodeLookupException;
import com.sun.mdm.index.codelookup.UserCodeRegistry;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.validation.exception.InvalidReferencedCode;
import com.sun.mdm.index.objects.validation.exception.InvalidReferencedModule;
import com.sun.mdm.index.objects.validation.exception.NullObjectException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

/**
 * @author jwu
 */
public class UserReferenceDescriptor implements FieldValidator {

    private String mModule = null;
    private UserCodeRegistry mUserCodeRegistry = null;
    private final Logger mLogger;
    
    /**
     * Creates a new instance of ReferenceDescriptor
     *
     * @param module module name
     */
    public UserReferenceDescriptor(String module) {
        mLogger = LogUtil.getLogger(this);
        if (module == null) {
            throw new RuntimeException("Module parameter cannot be null");
        }

        try {
            mUserCodeRegistry = UserCodeRegistry.getInstance();
        } catch (CodeLookupException e) {
            mLogger.error("CodeLookupException", e);
        }
        if (mUserCodeRegistry == null) {
            throw new RuntimeException("User code registry cannot be be located");
        }
        mModule = module;
    }

    /**
     * Get the module name of this ReferenceDescriptor
     *
     * @return the module name of this ReferenceDescriptor
     */
    public String getModule() {
        return mModule;
    }

    /**
     * Check if the sepcified value is a valid code
     *
     * @param code code value
     * @return true if valid code; false otherwise
     */
    public boolean isValidCode(String code) {
        if (code == null) {
            return false;
        }
        return mUserCodeRegistry.hasCode(mModule, code);
    }

    /**
     * Validate a ObjectField
     *
     * @param field an object field
     * @param newObject a new object
     * @throws ValidationException InvalidReferencedCode
     */
    public void validate(ObjectField field, boolean newObject) throws ValidationException {
        if (field == null) {
            throw new NullObjectException("Null object field");
        }
        if (field.getValue().getClass() != java.lang.String.class) {
            throw new InvalidReferencedCode(field.getName());
        }

        if (!mUserCodeRegistry.hasModule(mModule)) {
            throw new InvalidReferencedModule(mModule);
        }
        
        if (!isValidCode((String) field.getValue())) {
            throw new InvalidReferencedCode(field.getName());
        }
    }

}
