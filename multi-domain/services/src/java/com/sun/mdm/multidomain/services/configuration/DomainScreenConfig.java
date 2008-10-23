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

import com.sun.mdm.multidomain.association.Domain;

import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class DomainScreenConfig extends ObjectScreenConfig {
    
    private static transient final Logger mLogger = Logger.getLogger("com.sun.mdm.multidomain.services.configuration.DomainScreenConfig");
    private static transient final Localizer mLocalizer = Localizer.get();
    
    private Domain mDomain = null;             // domain associated with this Domain Screen Configuration
    private SummaryID mSummaryID;        // summary ID for a domain
    private ArrayList<GroupScreenConfig> mGroupScreenConfigs;   // ArrayList of GroupScreenConfig objects
    private HashMap <String, HierarchyScreenConfig> mHierarchyScreenConfigs;   // key is the name of the hierarchyType

    public DomainScreenConfig() {
        mGroupScreenConfigs = new ArrayList<GroupScreenConfig>();
        mHierarchyScreenConfigs = new HashMap<String, HierarchyScreenConfig>();
    }
    
    public DomainScreenConfig(Domain domain) {
        mGroupScreenConfigs = new ArrayList<GroupScreenConfig>();
        mHierarchyScreenConfigs = new HashMap<String, HierarchyScreenConfig>();
        mDomain = domain;
    }
    
    // retrieves the Domain object for this domain
    
    public Domain getDomain() {     
        return null;
    }

    // sets the Domain object for this domain
    
    public void setDomain(Domain domain) {       
        mDomain = domain;
    }

    // retrieves the SummaryID for this domain
    
    public SummaryID getSummaryID() {   
        return mSummaryID;
    }

    // sets the SummaryID for this domain
    
    public void setSummaryID(SummaryID summaryID) { 
        mSummaryID = summaryID;
    }

    // retrieves the GroupScreenConfig instances    
    
    public ArrayList<GroupScreenConfig> getGroupScreenConfigs() {  
        return mGroupScreenConfigs;
    }

    // sets the GroupScreenConfig instances
    
    public void setGroupScreenConfigs(ArrayList<GroupScreenConfig> configs) {  
        mGroupScreenConfigs = configs;
    }

    // retrieves the GroupScreenConfig object with the matching name
    
    public GroupScreenConfig getGroupScreenConfig(String hierarchyName) {   
        return null;
    }

    // retrieves the HierarchyScreenConfig instances
    
    public HashMap<String, HierarchyScreenConfig> getHierarchyScreenConfigs() {      
        return mHierarchyScreenConfigs;
    }

    // returns a Hierarchy screen configuration for a specific hierarchy
    
    public HierarchyScreenConfig getHierarchyScreenConfig(String hierarchyName) 
            throws Exception {
                
        if (hierarchyName == null || hierarchyName.length() == 0) {
            throw new Exception(mLocalizer.t("CFG522: Hierarchy name cannot be null " +
                                             "or an empty string."));
        }
        HierarchyScreenConfig hSC = mHierarchyScreenConfigs.get(hierarchyName);
        if (hSC == null) {
            throw new Exception(mLocalizer.t("CFG523: Could not retrieve the hierarchy " +
                                             "named \"{0}\" for the domain named \"{1}\".", 
                                             hierarchyName, getDomain().getName()));
        }
        return hSC;
    }
    
    // add a Hierarchy screen config object
    public void addHierarchyScreenConfig(HierarchyScreenConfig hSC) 
            throws Exception {

        if (hSC == null) {
            throw new Exception(mLocalizer.t("CFG524: Hierarchy screen configuration " + 
                                            "cannot be null."));
        }
        String hierarchyName = hSC.getHierarchyType().getName();
        if (mHierarchyScreenConfigs.containsKey(hierarchyName)) {
            throw new Exception(mLocalizer.t("CFG525: Hierarchy screen configuration " + 
                                             "cannot be added because it conflicts " +
                                             "with the name of an existing hierarchy: {0}.", 
                                             hierarchyName));
        }
        mHierarchyScreenConfigs.put(hierarchyName, hSC);
    }
    
}
