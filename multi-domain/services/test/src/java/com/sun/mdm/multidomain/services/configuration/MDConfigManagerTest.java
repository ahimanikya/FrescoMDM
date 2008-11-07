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
package com.sun.mdm.multidomain.services.configuration;

import com.sun.mdm.multidomain.services.core.ConfigException;
import java.util.HashMap;
import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * MDConfigurationManagerTest class.
 * @author rtam
 */
public class MDConfigManagerTest extends TestCase {
    
    private static MDConfigManager configManager = null;
    private static int DOMAIN_SCREEN_CONFIGS_SIZE = 2;
    private static int RELATIONSHIP_SCREEN_CONFIGS_SIZE = 2;
    private static int HIERARCHY_SCREEN_CONFIGS_SIZE = 0;
    private static int SCREEN_OBJECT_CONFIGS_SIZE = 3;
    
    public MDConfigManagerTest (String name) throws ConfigException {
        super(name);
        configManager = MDConfigManager.init();        
        if (configManager == null) { 
            System.out.println("Error: could not initialize the MDConfigmanager.");
            assert (false);
        }

    }
    
    public void setUp() {
    }
    
    public void testDomains() {
        try {
            // count the number of domain screen configurations
            HashMap<String, DomainScreenConfig> domainScreenConfigs = configManager.getDomainScreenConfigs();
            int size = domainScreenConfigs.size();
            if (size != DOMAIN_SCREEN_CONFIGS_SIZE) {
                System.out.println("Error: expected "  + DOMAIN_SCREEN_CONFIGS_SIZE +
                                   " domain screen configurations but actually " +
                                   " retrieved " + size + " domain screen configurations.");
                assert(false);
            }
            assert(true);
            // count the screens for each domain screen configuration
        } catch(Exception ex) {
            fail(ex.getMessage());
        }        
    }
    
    public void testRelationships() {
        try {
            // count the number of relationship screen configurations
            HashMap<String, RelationshipScreenConfig> relationshipScreenConfigs = configManager.getRelationshipScreenConfigs();
            int size = relationshipScreenConfigs.size();
            if (size != RELATIONSHIP_SCREEN_CONFIGS_SIZE) {
                System.out.println("Error: expected "  + RELATIONSHIP_SCREEN_CONFIGS_SIZE +
                                   " relationship screen configurations but actually " +
                                   " retrieved " + size + " relationship screen configurations.");
                assert(false);
            }
            assert(true);
            // count the screens for each relationship screen configuration
        } catch(Exception ex) {
            fail(ex.getMessage());
        }        
    }

    public void testScreens() {
        try {
            // count the number of screens
        	HashMap<Integer, ScreenObject> screenObjects = configManager.getScreens();
            int size = screenObjects.size();
            if (size != SCREEN_OBJECT_CONFIGS_SIZE) {
                System.out.println("Error: expected "  + SCREEN_OBJECT_CONFIGS_SIZE +
                                   " screen object configurations but actually " +
                                   " retrieved " + size + " screen object configurations.");
                assert(false);
            }
            assert(true);
            // check the initial screen
        } catch(Exception ex) {
            fail(ex.getMessage());
        }        
    }
    
}
