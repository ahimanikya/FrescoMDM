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
import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * @author jwu
 */
public class UserReferenceDescriptor implements FieldValidator {

    private String mModule = null;
    private UserCodeRegistry mUserCodeRegistry = null;
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    
    /**
     * Creates a new instance of ReferenceDescriptor
     *
     * @param module module name
     */
    public UserReferenceDescriptor(String module) {
        if (module == null) {
            throw new RuntimeException(mLocalizer.t("OBJ737: module parameter " + 
                                        "cannot be null."));
        }

        try {
            mUserCodeRegistry = UserCodeRegistry.getInstance();
        } catch (CodeLookupException e) {
            mLogger.warn(mLocalizer.x("OBJ041: User code registry could not be retrieved: {0}", e));
        }
        if (mUserCodeRegistry == null) {
            throw new RuntimeException(mLocalizer.t("OBJ738: User code registry " + 
                                        "could not be located."));
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
            throw new NullObjectException(mLocalizer.t("OBJ739: Object field " + 
                                        "cannot be null."));
        }
        if (field.getValue().getClass() != java.lang.String.class) {
            throw new InvalidReferencedCode(mLocalizer.t("OBJ740: Invalid reference code " + 
                                        "for this field: {0}", field.getName()));
        }

        if (!mUserCodeRegistry.hasModule(mModule)) {
            throw new InvalidReferencedModule(mLocalizer.t("OBJ741: Invalid reference module " + 
                                        "for this field: {0}", mModule));
        }
        
        if (!isValidCode((String) field.getValue())) {
            throw new InvalidReferencedCode(mLocalizer.t("OBJ742: Invalid reference code " + 
                                        "for this field: {0}", field.getName()));
        }
    }

}
