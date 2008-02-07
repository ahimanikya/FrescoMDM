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
package com.sun.mdm.index.project.ui.wizards.generator;

import java.util.ArrayList;

/**
 * The Configuration settings for the complete object model,
 * including each field
 */
public class ConfigSettings {
    private String primaryNode;
    private String matchEngine;
    private boolean bMatchEngineSeebeyond = false;
    private FieldSettings[] fieldSettings;
    private String[] sourceSystems;
    private String duplicateThreshold = "7.25";
    private String matchThreshold = "29.0";
    private String transaction = "CONTAINER";
    private ArrayList edmAllNodes;
    private ArrayList subObjects;
    private boolean bMasterIndexEDM = false;

    /**
     * Get the name of the primary node, e.g. 'Company'
     * @return the name
     */
    public String getPrimaryNode() {
        return primaryNode;
    }

    /**
     * Set the name of the primary node, e.g. 'Company'
     * @param val the name
     */
    public void setPrimaryNode(String val) {
        primaryNode = val;
    }

    /**
     * Get the settings for all the fields
     * @return the fields settings
     */
    public FieldSettings[] getFieldSettings() {
        return fieldSettings;
    }

    /**
     * Set the settings for all the fields
     * @param val the fields settings
     */
    public void setFieldSettings(FieldSettings[] val) {
        fieldSettings = val;
    }

    /**
     * Get the MatchEngine
     * @return the name
     */
    public String getMatchEngine() {
        return matchEngine;
    }

    /**
     * Set the MatchEngine
     * @param val the MatchEngine
     */
    public void setMatchEngine(String val) {
        matchEngine = val;
    }

    /**
     * Set the MatchEngineSeebeyond
     * @param flag the bMatchEngineSeebeyond
     */
    public void setMatchEngineSeebeyond(boolean flag) {
        bMatchEngineSeebeyond = flag;
    }

    /**
     * @return isMatchEngineSeebeyond
     */
    public boolean isMatchEngineSeebeyond() {
        return bMatchEngineSeebeyond;
    }

    /**
     * Get the list of source systems
     * @return the source systems
     */
    public String[] getSourceSystems() {
        return sourceSystems;
    }

    /**
     * Set the source systems
     * @param val the source systems
     */
    public void setSourceSystems(String[] val) {
        sourceSystems = val;
    }
    
    /**
     * get DuplicateThreshold
     * @return DuplicateThreshold
     */
    public String getDuplicateThreshold() {
        return duplicateThreshold;
    }
    
    /**
     * Set DuplicateThreshold
     * @param val DuplicateThreshold
     */
    public void setDuplicateThreshold(String val) {
        duplicateThreshold = val;
    }
    
    /**
     * get MatchThreshold
     * @return MatchThreshold
     */
    public String getMatchThreshold() {
        return matchThreshold;
    }
    
    /**
     * Set MatchThreshold
     * @param val MatchThreshold
     */
    public void setMatchThreshold(String val) {
        matchThreshold = val;
    }
    
    /**
     * get Transaction
     * @return transaction
     */
    public String getTransaction() {
        return transaction;
    }
    
    /**
     * Set Transaction
     * @param val transaction
     */
    public void setTransaction(String val) {
        transaction = val;
    }
    
    /**
     * @return allNodes
     */
    public ArrayList getEdmAllNodes() {
        return edmAllNodes;
    }
    
    /**
     * Set allNodes
     * @param val 
     */
    public void setEdmAllNodes(ArrayList val) {
        edmAllNodes = val;
    }
    
    /**
     * @return Relationships
     */
    public ArrayList getSubObjects() {
        return subObjects;
    }

    /**
     * Set Relationships
     * @param val 
     */
    public void setSubObjects(ArrayList val) {
        subObjects = val;
    }
    
    /**
     * get MasterEindexEDM
     * @return boolean bMasterIndexEDM
     */
    public boolean getMasterIndexEDM() {
        return bMasterIndexEDM ;
    }
    
    /**
     * set MasterEindexEDM
     * @param boolean val
     */
    public void setMasterIndexEDM(boolean val) {
        bMasterIndexEDM = val;
    }
}
