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
import java.util.Collection;

import junit.framework.TestCase;

/**
 * MDConfigurationManagerTest class.
 * @author rtam
 */
public class MDConfigManagerTest extends TestCase {
    
    private static MDConfigManager configManager = null;
    private static int DOMAIN_SCREEN_CONFIGS_SIZE = 5;
    private static int RELATIONSHIP_SCREEN_CONFIGS_SIZE = 5;
    private static int RELATIONSHIP_SCREEN_CONFIG_INSTANCES_SIZE = 7;
    private static int HIERARCHY_SCREEN_CONFIGS_SIZE = 0;
    private static int PAGE_DEF_CONFIG_SIZE = 1;
    private static int PAGE_DEF_SUBSCREEN_CONFIG_SIZE = 2;
    private static int PAGE_DEF_INITIAL_SCREEN_ID = 0;
    private static int PAGE_DEF_INITIAL_SUBSCREEN_ID = 0;

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
                                   " domain screen configurations but actually" +
                                   " retrieved " + size + " domain screen configurations.");
                assert(false);
            } else {
                System.out.println("testDomains succeeded.");
                assert(true);
                // count the screens for each domain screen configuration
            }
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
                                   " relationship screen configurations but actually" +
                                   " retrieved " + size + " relationship screen configurations.");
                assert(false);
            } else {
                
                int relationshipConfigCount = 0;
                Collection<RelationshipScreenConfig> values = relationshipScreenConfigs.values();
                for (RelationshipScreenConfig rsc : values) {
                    // count the individual screen configurations
                    Collection<RelationshipScreenConfigInstance> relConfigs 
                                = rsc.getRelationships().values();
                    relationshipConfigCount += relConfigs.size();
                }
                if (relationshipConfigCount != RELATIONSHIP_SCREEN_CONFIG_INSTANCES_SIZE) {
                    System.out.println("Error: expected "  + RELATIONSHIP_SCREEN_CONFIG_INSTANCES_SIZE +
                                       " relationship screen configuration instances " + 
                                       "but actually retrieved " + relationshipConfigCount + 
                                       " relationship screen configuration instances.");
                } else {
                    System.out.println("testRelationships succeeded.");
                    assert(true);
                }
            }
        } catch(Exception ex) {
            fail(ex.getMessage());
        }        
    }

    public void testScreens() {
        try {
            // count the number of screens
            HashMap<Integer, ScreenObject> screenObjects = configManager.getScreens();
            int size = screenObjects.size();
            if (size != PAGE_DEF_CONFIG_SIZE) {
                System.out.println("Error: expected "  + PAGE_DEF_CONFIG_SIZE +
                                   " screen object configurations but actually" +
                                   " retrieved " + size + " screen object configurations.");
                assert(false);
            } else {
                // check the initial screen
                int initialScreenID = configManager.getInitialScreenID();
                if (initialScreenID != PAGE_DEF_INITIAL_SCREEN_ID) {
                    System.out.println("Error: expected top level page definition"  +
                                       " to have an initial subscreen ID of "+ PAGE_DEF_INITIAL_SCREEN_ID +
                                       " but actually retrieved " + initialScreenID);
                    assert(false);
                }
                Collection<ScreenObject> values = screenObjects.values();
                for (ScreenObject scrObj : values) {
                    int initialSubscreenID = scrObj.getInitialSubscreenID();
                    if (initialSubscreenID != PAGE_DEF_INITIAL_SUBSCREEN_ID) {
                        System.out.println("Error: expected an initial subscreen ID of " + 
                                           PAGE_DEF_INITIAL_SUBSCREEN_ID +
                                           " but actually retrieved " + initialSubscreenID);
                        assert(false);
                    } else {
                        Collection<ScreenObject> subscreens = scrObj.getSubscreens();
                        int subscreenCount = subscreens.size();
                        if (subscreenCount != PAGE_DEF_SUBSCREEN_CONFIG_SIZE) {
                            System.out.println("Error: expected " + PAGE_DEF_SUBSCREEN_CONFIG_SIZE +
                                               " subscreens but actually retrieved " + subscreenCount);
                            assert(false);
                        } else {
                            ArrayList<ScreenObject> subScreensList = new ArrayList<ScreenObject>(subscreens);
                            int expectedPageSize = 15;
                            int expectedMaxRecords = 100;
                            String expectedViewPath = "manage/history_relationship";
                            
                            scrObj = subScreensList.get(0);
                            int pageSize = scrObj.getPageSize();
                            if (pageSize != expectedPageSize) {
                                System.out.println("Error: expected first subscreen " +
                                                   " to have a page size of " + 
                                                   expectedPageSize + " but retrieved " +
                                                   " a page size of " + pageSize);
                                assert(false);
                            }
                            int maxRecords = scrObj.getMaxRecords();
                            if (maxRecords != expectedMaxRecords) {
                                System.out.println("Error: expected first subscreen " +
                                                   " to have a max records of " + 
                                                   expectedMaxRecords + " but retrieved " +
                                                   " a page size of " + maxRecords);
                                assert(false);
                            }

                            String viewpath = scrObj.getViewPath();
                            if (expectedViewPath.compareTo(viewpath) != 0) {
                                System.out.println("Error: expected first subscreen " +
                                                   " to have a view path of " + 
                                                   expectedViewPath + " but retrieved " +
                                                   " a view path of " + viewpath);
                                assert(false);
                            }
                            
                            scrObj = subScreensList.get(1);
                            expectedPageSize = 12;
                            expectedMaxRecords = 97;
                            pageSize = scrObj.getPageSize();
                            expectedViewPath = "manage/manage_relationship";
                            if (pageSize != expectedPageSize) {
                                System.out.println("Error: expected second subscreen " +
                                                   " to have a page size of " + 
                                                   expectedPageSize + " but retrieved " +
                                                   " a page size of " + pageSize);
                                assert(false);
                            }
                            maxRecords = scrObj.getMaxRecords();
                            if (maxRecords != expectedMaxRecords) {
                                System.out.println("Error: expected second subscreen " +
                                                   " to have a max records of " + 
                                                   expectedMaxRecords + " but retrieved " +
                                                   " a page size of " + maxRecords);
                                assert(false);
                            }
                            viewpath = scrObj.getViewPath();
                            if (expectedViewPath.compareTo(viewpath) != 0) {
                                System.out.println("Error: expected second subscreen " +
                                                   " to have a view path of " + 
                                                   expectedViewPath + " but retrieved " +
                                                   " a view path of " + viewpath);
                                assert(false);
                            }
                            
                            System.out.println("testScreens succeeded.");
                            assert(true);
                        }
                    }
                }
            }
        } catch(Exception ex) {
            fail(ex.getMessage());
        }        
    }
    
}
