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

import com.sun.mdm.multidomain.relationship.RelationshipType;
import java.util.ArrayList;

public class RelationshipScreenConfigInstance extends ObjectScreenConfig {

//    private Relationship mRelationship = null;
    private RelationshipType mRelationshipType = null;
    // RESUME HERE
    // Should this be obtained from the RelationshipType?
    private String mRelationshipDisplayName = null;
    private String mRelationshipName = null;
    private ArrayList<FieldConfig> mPredefinedAttributes;
    private ArrayList<FieldConfig> mExtendedAttributes;
    
    public RelationshipScreenConfigInstance() {
        mPredefinedAttributes = new ArrayList<FieldConfig> ();
        mExtendedAttributes = new ArrayList<FieldConfig> ();
    }
    
    public RelationshipScreenConfigInstance(String relName,
                                            String relDisplayName,
                                            ArrayList<FieldConfig> predefinedAttributes,
                                            ArrayList<FieldConfig> extendedAttributes) {
        mRelationshipName = relName;                                         
        mRelationshipDisplayName = relDisplayName;
        mPredefinedAttributes = predefinedAttributes;
        mExtendedAttributes = extendedAttributes;
    }
    
    public RelationshipScreenConfigInstance(RelationshipType rel) {
        mRelationshipType = rel;
    }
    
    public void setRelationship(RelationshipType rel) {
        mRelationshipType = rel;
    }
    
    public RelationshipType getRelationshipType() {
        return mRelationshipType;
    }
    
    public void setRelationshipName(String relName) {
        mRelationshipName = relName;
    }
    
    public String getRelationshipName() {
        return mRelationshipName;
    }
    
    public void setRelationshipDisplayName(String relDisplayName) {
        mRelationshipDisplayName = relDisplayName;
    }
    
    public String getRelationshipDisplayName() {
        return mRelationshipDisplayName;
    }
    
    public void setPredefinedAttributes(ArrayList<FieldConfig> predefinedAttributes) {
        mPredefinedAttributes = predefinedAttributes;
    }

    public ArrayList<FieldConfig> getPredefinedAttributes() {
        return mPredefinedAttributes;
    }
    
    public void setExtendedAttributes(ArrayList<FieldConfig> extendedAttributes) {
        mExtendedAttributes = extendedAttributes;
    }

    public ArrayList<FieldConfig> getExtendedAttributes() {
        return mExtendedAttributes;
    }
    
    // RESUME HERE
    // add more stuff from LinkType.java
    
}
