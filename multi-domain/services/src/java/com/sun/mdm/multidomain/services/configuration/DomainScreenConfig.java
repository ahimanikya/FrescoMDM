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

import com.sun.mdm.multidomain.services.model.Domain;

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
    private SummaryLabel mSummaryLabel;        // summary label for a domain
    private ArrayList<GroupScreenConfig> mGroupScreenConfigs;   // ArrayList of GroupScreenConfig objects
    private HashMap <String, HierarchyScreenConfig> mHierarchyScreenConfigs;   // key is the name of the hierarchyType

    /** Constructor for DomainScreenConfig
     * 
     */
    public DomainScreenConfig() {
        mGroupScreenConfigs = new ArrayList<GroupScreenConfig>();
        mHierarchyScreenConfigs = new HashMap<String, HierarchyScreenConfig>();
    }
    
    /** Constructor for DomainScreenConfig
     * 
     * @param domain Domain
     */
    public DomainScreenConfig(Domain domain) {
        mGroupScreenConfigs = new ArrayList<GroupScreenConfig>();
        mHierarchyScreenConfigs = new HashMap<String, HierarchyScreenConfig>();
        mDomain = domain;
    }
    
    /** Retrieve the Domain object for this domain.
     * 
     * @return The Domain object for this domain.
     */
    public Domain getDomain() {     
        return mDomain;
    }

    /** Sets the Domain object for this domain.
     * 
     * @param domain  The Domain object for this domain.
     */
    public void setDomain(Domain domain) {       
        mDomain = domain;
    }

    /** Retrieve the SummaryLabel for this domain
     * 
     * @return The summary label for this domain.
     */
    public SummaryLabel getSummaryLabel() {   
        return mSummaryLabel;
    }

    /** Sets the SummaryLabel for this domain
     * 
     * @param summaryLabel  The summary label for this domain.
     */
    public void setSummaryLabel(SummaryLabel summaryLabel) { 
        mSummaryLabel = summaryLabel;
    }

    /** Retrieve the GroupScreenConfig instances for this domain
     * 
     * @return The group screen configurations for this domain.
     */
    public ArrayList<GroupScreenConfig> getGroupScreenConfigs() {  
        return mGroupScreenConfigs;
    }

    /** Sets the GroupScreenConfig instances for this domain
     * 
     * @param  configs  The group screen configurations for this domain.
     */
    public void setGroupScreenConfigs(ArrayList<GroupScreenConfig> configs) {  
        mGroupScreenConfigs = configs;
    }

    /** Retrieve the GroupScreenConfig instances for a hierarchy.
     * 
     * @param  hierarchyName  Hierarchy name.
     * @return The group screen configuration for a hierarchy.
     */
    public GroupScreenConfig getGroupScreenConfig(String hierarchyName) {   
        return null;
    }

    /** Retrieve the HierarchyScreenConfig instances.
     * 
     * @return The hierarchy screen configurations.
     */
    public HashMap<String, HierarchyScreenConfig> getHierarchyScreenConfigs() {      
        return mHierarchyScreenConfigs;
    }

    /** Retrieve the HierarchyScreenConfig instance for a specific hiearchy.
     * 
     * @param  hierarchyName  The hierarchy name.
     * @return The hierarchy screen configuration for a specific hierarchy.
     */
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
    
    /** Add a hierarchy screen configuration.
     * 
     * @param hSC  Hiearchy screen configuration.
     */
    public void addHierarchyScreenConfig(HierarchyScreenConfig hSC) 
            throws Exception {

        if (hSC == null) {
            throw new Exception(mLocalizer.t("CFG524: Hierarchy screen configuration " + 
                                            "cannot be null."));
        }
        String hierarchyName = hSC.getHierarchyDef().getName();
        if (mHierarchyScreenConfigs.containsKey(hierarchyName)) {
            throw new Exception(mLocalizer.t("CFG525: Hierarchy screen configuration " + 
                                             "cannot be added because it conflicts " +
                                             "with the name of an existing hierarchy: {0}.", 
                                             hierarchyName));
        }
        mHierarchyScreenConfigs.put(hierarchyName, hSC);
    }
    
    /** Remove a hierarchy screen configuration.
     * 
     * @param hierarchyName  Name of the hierarchy.
     */
    public void removeHierarchyScreenConfig(String hierarchyName) 
            throws Exception {

        if (hierarchyName == null || hierarchyName.length() == 0) {
            throw new Exception(mLocalizer.t("CFG549: Hierarchy name cannot be null " +
                                             "or an empty string."));
        }
        try {
            mHierarchyScreenConfigs.remove(hierarchyName);
        } catch (Exception e) {
            throw new Exception(mLocalizer.t("CFG550: Could not remove the hierarchy " +
                                             "named: {0}.", hierarchyName));
        }
    }
    
}
