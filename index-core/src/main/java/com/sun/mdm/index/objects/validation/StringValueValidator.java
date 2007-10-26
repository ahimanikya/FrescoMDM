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
import com.sun.mdm.index.objects.validation.exception.NullObjectException;
import com.sun.mdm.index.objects.validation.exception.UnknownDataTypeException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.objects.validation.exception.MaximumConstraintException;
import com.sun.mdm.index.objects.validation.exception.MinimumConstraintException;


/**
 * @author jwu
 */
public class StringValueValidator implements ValueValidator {

    private Integer mMinValue = null;
    private Integer mMaxValue = null;

    /**
     * Creates a new instance of LongValueValidator
     *
     * @param max maximum length
     * @param min minimum length
     */
    public StringValueValidator(Integer max, Integer min) {
        if (max != null) {
            mMaxValue = max;
        }
        if (min != null) {
            mMinValue = min;
        }
    }

    /**
     * Validate this field
     *
     * @param field ObjectField
     * @throws ValidationException NullObjectException, UnknownDataTypeException
     */
    public void validate(ObjectField field) throws ValidationException {
        if (field == null) {
            throw new NullObjectException();
        }

        if (field.getType() != FieldType.STRING) {
            throw new UnknownDataTypeException(field.getName());
        }

        Object value = field.getValue();
        if (value != null) {
            if (!(value instanceof java.lang.String)) {
                throw new UnknownDataTypeException(field.getName());
            }
            String fieldValue = (String) value;
            if (mMinValue != null) {
                if (fieldValue.length() < mMinValue.intValue()) {
                    throw new MinimumConstraintException(field.getName());
                }
            }

            if (mMaxValue != null) {
                if (fieldValue.length() > mMaxValue.intValue()) {
                    throw new MaximumConstraintException(field.getName());
                }
            }
        }
    }
}
