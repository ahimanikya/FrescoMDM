/*
 * BEGIN_HEADER - DO NOT EDIT
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * If applicable add the following below this CDDL HEADER,
 * with the fields enclosed by brackets "[]" replaced with
 * your own identifying information: Portions Copyright
 * [year] [name of copyright owner]
 */

/*
 * @(#)EosFieldGroupConfig.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.index.edm.services.configuration;

import java.util.ArrayList;

/**
 * Model field groups
 *
 */
public class FieldConfigGroup implements java.io.Serializable {
    private ArrayList mFieldGroup;     // FieldConfig objects
    private String mDescription;     // description for this field group


    /**
     * Constructor for the FieldGroupConfig object
     *
     * @param description Description for this group of FieldConfig objects.
     * @param fieldConfigs  ArrayList of FieldConfig objects.
     */
    public FieldConfigGroup(String description, ArrayList fieldConfigs) {
        super();
        mDescription = description;
        mFieldGroup = fieldConfigs;
    }

    /**
     * Constructor for the FieldGroupConfig object
     *
     * @param description Description for this group of FieldConfig objects.
     * @param fieldConfigs  ArrayList of FieldConfig objects.
     */
    public FieldConfigGroup(String description, FieldConfig[] fieldConfigs) {
        super();
        mDescription = description;
        mFieldGroup = new ArrayList();
        for (int i = 0; i < fieldConfigs.length; i++) {
            mFieldGroup.add((FieldConfig) fieldConfigs[i]);
        }
    }

    /** Getter for mFieldConfigs attribute.
     *
     * @return an ArrayList of FieldConfig objects.
     */
    public ArrayList getFieldConfigs()  {
        return mFieldGroup;
    }

    /** Getter for mDescription attribute.
     *
     * @return value of the mDescription attribute.
     */
    public String getDescription() {
        return mDescription;
    }
    
}
