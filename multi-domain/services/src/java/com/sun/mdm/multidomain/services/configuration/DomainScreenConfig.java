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

import com.sun.mdm.multidomain.services.configuration.Domain;

import java.util.ArrayList;

public class DomainScreenConfig extends ObjectScreenConfig {
    private Domain mDomain;             // domain associated with this Domain Screen Configuration
    private SummaryID mSummaryID;        // summary ID for a domain
    private ArrayList<GroupScreenConfig> mGroupScreenConfigs;   // ArrayList of GroupScreenConfig objects
    private ArrayList<HierarchyScreenConfig> mHierarchyScreenConfigs;   // ArrayList of hierarchyScreenConfig objects

    public DomainScreenConfig() {
    }
    
    public Domain getDomain() {     // retrieves the Domain object for this domain
        return null;
    }

    public void setDomain(Domain domain) {       // sets the Domain object for this domain
        mDomain = domain;
    }

    public SummaryID getSummaryID() {   // retrieves the SummaryID for this domain
        return mSummaryID;
    }

    public void setSummaryID(SummaryID summaryID) { // sets the SummaryID for this domain
        mSummaryID = summaryID;
    }

    public ArrayList<GroupScreenConfig> getGroupScreenConfigs() {  // retrieves the GroupScreenConfig instances
        return mGroupScreenConfigs;
    }

    public void setGroupScreenConfigs(ArrayList<GroupScreenConfig> configs) {  // sets the GroupScreenConfig instances
        mGroupScreenConfigs = configs;
    }

    public GroupScreenConfig getGroupScreenConfig(String hierarchyName) {   // retrieves the GroupScreenConfig object with the matching name
        return null;
    }

    public ArrayList<HierarchyScreenConfig> getHierarchyScreenConfigs() {      // retrieves the HierarchyScreenConfig instances
        return mHierarchyScreenConfigs;
    }

    public void setHierarchyScreenConfigs(ArrayList<HierarchyScreenConfig> configs) {  // sets the HierarchyScreenConfig instances
        mHierarchyScreenConfigs = configs;
    }

    public HierarchyScreenConfig getHierarcyScreenConfig(String hierarchyName, String targetDomainName) {          // retrieves the HierarchyScreenConfig object with the matching name and target domain
        return null;
    }
    
}