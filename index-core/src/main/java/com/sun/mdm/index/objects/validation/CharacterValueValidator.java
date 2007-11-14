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
import com.sun.mdm.index.util.Localizer;

/**
 * @author jwu
 */
public class CharacterValueValidator implements ValueValidator {

    private Integer mMinValue = null;
    private Integer mMaxValue = null;

    private transient final Localizer mLocalizer = Localizer.get();
    
    /**
     * Creates a new instance of CharacterValueValidator
     *
     * @param max maximum length
     * @param min minimum length
     */
    public CharacterValueValidator(Integer max, Integer min) {
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

        if (field.getType() != FieldType.CHAR) {
            throw new UnknownDataTypeException(mLocalizer.t("OBJ637: Character " + 
                                        "Value Validator encountered an unrecognized " + 
                                        "type: {0}", field.getName()));
        }

        Object value = field.getValue();
        if (value != null) {
            if (!(value instanceof java.lang.Character)) {
                throw new UnknownDataTypeException(mLocalizer.t("OBJ638: Character " + 
                                        "Value Validator encountered a field value with " + 
                                        "an unrecognized type: {0}", field.getName()));
            }
            Character fieldValue = (Character) value;
            if (mMinValue != null) {
                if (fieldValue.toString().length() < mMinValue.intValue()) {
                    throw new MinimumConstraintException(mLocalizer.t("OBJ639: The field " + 
                                        "value {0} for the field {1} is less than " + 
                                        "the expected minimum value for this field: {2}", 
                                        fieldValue, field.getName(), mMinValue.intValue()));
                }
            }

            if (mMaxValue != null) {
                if (fieldValue.toString().length() > mMaxValue.intValue()) {
                    throw new MaximumConstraintException(mLocalizer.t("OBJ640: The field " + 
                                        "value {0} for the field {1} is greater than " + 
                                        "the expected maximum value for this field: {2}", 
                                        fieldValue, field.getName(), mMaxValue.intValue()));
                }
            }
        }
    }
}
