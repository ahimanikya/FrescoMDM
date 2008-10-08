/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2008 Sun Microsystems, Inc. All Rights Reserved.
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
package com.sun.mdm.multidomain.services.configuration;

import com.sun.mdm.multidomain.relationship.Relationship;
//import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import java.util.ArrayList;

public class GroupScreenConfig extends ObjectScreenConfig {
    private ArrayList<FieldConfig> fixedGroupAttributesFieldConfigs; // ArrayList of FieldConfig objects for the fixedGroupAttributes in the GroupObject.
    private ArrayList<FieldConfig> extendedGroupAttributesFieldConfigs;  // ArrayList of FieldConfig objects for the extendedGroupAttributes in the GroupObject.

    public GroupScreenConfig() { 
    }
    
    public Relationship getGroup() {       // retrieves the Group object for this group
        return null;
    }

    public ArrayList<FieldConfig> getFixedAttributesConfigs() {      // retrieves the FieldConfig objects for the fixedGroupAttributes.
        return fixedGroupAttributesFieldConfigs;
    }

    public void setFixedAttributesConfigs(ArrayList<FieldConfig> attributes) { // sets the FieldConfig objects for the fixedGroupAttributes.
        fixedGroupAttributesFieldConfigs = attributes;
    }

    public ArrayList<FieldConfig> getExtendedAttributesConfigs() {       // retrieves the FieldConfig objects for the extendedGroupAttributes.
        return extendedGroupAttributesFieldConfigs;
    }

    public void setExtendedAttributesConfigs(ArrayList<FieldConfig> attributes) { // sets the FieldConfig objects for the extendedGroupAttributes.
        extendedGroupAttributesFieldConfigs = attributes;
    }

}
