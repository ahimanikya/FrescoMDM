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
import com.sun.mdm.index.objects.validation.exception.PatternMismatchedException;
import com.sun.mdm.index.objects.validation.exception.UnknownDataTypeException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.util.Localizer;


/**
 * @author jwu
 */
public class PatternValidator {

    private Pattern mPattern;
    private String  mRegularExpression;
    private transient final Localizer mLocalizer = Localizer.get();

    /**
     * Creates a new instance of PatternValidator
     */
    public PatternValidator() {
    }

    /**
     * @param pattern a regular expression
     * @todo Document this constructor
     */
    public PatternValidator(String pattern) {
        mPattern = Pattern.compile(pattern);
        mRegularExpression = pattern;
    }

    /**
     * get the pattern of the pattern validator
     *
     * @return String pattern
     */
    public String getPattern() {
        return mRegularExpression;
    }

    /**
     * @param field a string value
     * @throws ValidationException if field and patter do not match
     * @todo Document this method
     */
    public void validate(ObjectField field) throws ValidationException {
        Object value = field.getValue();
        if (value != null) {
            if (!(value instanceof java.lang.String)) {
                throw new UnknownDataTypeException(mLocalizer.t("OBJ714: Pattern Validator " + 
                                        "encountered an unrecognized type " + 
                                        "for this field: {0}", 
                                        field.getName()));
            }
            if (!mPattern.matcher((String) value).matches()) {
                throw new PatternMismatchedException(mLocalizer.t("OBJ715: Pattern mismatch " + 
                                        "for the value \"{0}\" " + 
                                        "retrieved from this field: {1}", 
                                        value, 
                                        field.getName()));
            }
        }
    }

    /**
     * @param value a string value
     * @throws ValidationException if field and patter do not match
     * @todo Document this method
     */
    public void validate(String value) throws ValidationException {
        if (value != null) {
            if (!mPattern.matcher((String) value).matches()) {
                throw new PatternMismatchedException(mLocalizer.t("OBJ76: Pattern mismatch " + 
                                        "for: {0}", value));
            }
        }
    }

}
