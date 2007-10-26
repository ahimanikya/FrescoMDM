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

import java.util.Date;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.validation.exception.NullObjectException;
import com.sun.mdm.index.objects.validation.exception.UnknownDataTypeException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.objects.validation.exception.MaximumConstraintException;
import com.sun.mdm.index.objects.validation.exception.MinimumConstraintException;


/**
 * @author jwu
 */
public class DateValueValidator implements ValueValidator {

    private Date mMinValue = null;
    private Date mMaxValue = null;

    /**
     * Creates a new instance of DateValueValidator
     *
     * @param max maximum date value
     * @param min minimum date value
     */
    public DateValueValidator(Date max, Date min) {
        if (max != null) {
            mMaxValue = max;
        }
        if (min != null) {
            mMinValue = min;
        }
    }

    /**
     * @param field object field
     * @todo Document this method
     * @throws ValidationException if specified field is null
     */
    public void validate(ObjectField field) throws ValidationException {
        if (field == null) {
            throw new NullObjectException();
        }

        if (field.getType() != FieldType.DATE) {
            throw new UnknownDataTypeException(field.getName());
        }

        Object value = field.getValue();
        if (value != null) {
            if (!(value instanceof java.util.Date)) {
                throw new UnknownDataTypeException(field.getName());
            }
            Date fieldValue = (Date) value;
            if (mMinValue != null) {
                if (fieldValue.compareTo(mMinValue) < 0) {
                    throw new MinimumConstraintException(field.getName());
                }
            }

            if (mMaxValue != null) {
                if (fieldValue.compareTo(mMaxValue) > 0) {
                    throw new MaximumConstraintException(field.getName());
                }
            }
        }
    }
}
