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
import com.sun.mdm.multidomain.services.model.ObjectDefinition;
import com.sun.mdm.multidomain.services.core.ObjectFactory;
import com.sun.mdm.multidomain.services.core.ObjectDefinitionBuilder;
import com.sun.mdm.multidomain.services.core.ObjectFactoryRegistry;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;

import java.io.InputStream;

import junit.framework.TestCase;

/**
 * MDConfigurationManagerTest class.
 * @author rtam
 */
public class MDConfigManagerTest extends TestCase {
    
    private static MDConfigManager configManager = null;
    
    public MDConfigManagerTest (String name) throws ConfigException {
        super(name);
        configManager = MDConfigManager.init();        
        if (configManager == null) { 
            System.out.println("Error: could not initialize the MDConfigmanager.");
            assertTrue (false);
        }

    }
    
    public void setUp() {
    }
    
    public void testDomains() {
        try {
            // count the number of domain screen configurations
            HashMap<String, DomainScreenConfig> domainScreenConfigs = configManager.getDomainScreenConfigs();
            int domainScreenConfigsSize = 5;
            int size = domainScreenConfigs.size();
            if (size != domainScreenConfigsSize) {
                System.out.println("Error: expected "  + domainScreenConfigsSize +
                                   " domain screen configurations but actually" +
                                   " retrieved " + size + " domain screen configurations.");
                assertTrue(false);
            } else {
                assertTrue(true);
                // TODO
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
            int relationshipScreenConfigsSize = 5;
            int size = relationshipScreenConfigs.size();
            if (size != relationshipScreenConfigsSize) {
                System.out.println("Error: expected "  + relationshipScreenConfigsSize +
                                   " relationship screen configurations but actually" +
                                   " retrieved " + size + " relationship screen configurations.");
                assertTrue(false);
            } else {
                
                int relationshipConfigCount = 0;
                Collection<RelationshipScreenConfig> values = relationshipScreenConfigs.values();
                for (RelationshipScreenConfig rsc : values) {
                    // count the individual screen configurations
                    Collection<RelationshipScreenConfigInstance> relConfigs 
                                = rsc.getRelationships().values();
                    relationshipConfigCount += relConfigs.size();
                }
                int relationshipScreenConfigInstancesSize = 7;
                if (relationshipConfigCount != relationshipScreenConfigInstancesSize) {
                    System.out.println("Error: expected "  + relationshipScreenConfigInstancesSize +
                                       " relationship screen configuration instances " + 
                                       "but actually retrieved " + relationshipConfigCount + 
                                       " relationship screen configuration instances.");
                } else {
                    assertTrue(true);
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
            int pageDefConfigSize = 1;
            if (size != pageDefConfigSize) {
                System.out.println("Error: expected "  + pageDefConfigSize +
                                   " screen object configurations but actually" +
                                   " retrieved " + size + " screen object configurations.");
                assertTrue(false);
            } else {
                // check the initial screen
                int initialScreenID = configManager.getInitialScreenID();
                int pageDefInitialScreenID = 0;
                if (initialScreenID != pageDefInitialScreenID) {
                    System.out.println("Error: expected top level page definition"  +
                                       " to have an initial subscreen ID of "+ pageDefInitialScreenID +
                                       " but actually retrieved " + initialScreenID);
                    assertTrue(false);
                }
                Collection<ScreenObject> values = screenObjects.values();
                for (ScreenObject scrObj : values) {
                    int initialSubscreenID = scrObj.getInitialSubscreenID();
                    int pageDefInitialSubscreenID = 0;
                    if (initialSubscreenID != pageDefInitialSubscreenID) {
                        System.out.println("Error: expected an initial subscreen ID of " + 
                                           pageDefInitialSubscreenID +
                                           " but actually retrieved " + initialSubscreenID);
                        assertTrue(false);
                    } else {
                        Collection<ScreenObject> subscreens = scrObj.getSubscreens();
                        int subscreenCount = subscreens.size();
                        int pageDefSubscreenConfigSize = 2;
                        if (subscreenCount != pageDefSubscreenConfigSize) {
                            System.out.println("Error: expected " + pageDefSubscreenConfigSize +
                                               " subscreens but actually retrieved " + subscreenCount);
                            assertTrue(false);
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
                                assertTrue(false);
                            }
                            int maxRecords = scrObj.getMaxRecords();
                            if (maxRecords != expectedMaxRecords) {
                                System.out.println("Error: expected first subscreen " +
                                                   " to have a max records of " + 
                                                   expectedMaxRecords + " but retrieved " +
                                                   " a page size of " + maxRecords);
                                assertTrue(false);
                            }

                            String viewpath = scrObj.getViewPath();
                            if (expectedViewPath.compareTo(viewpath) != 0) {
                                System.out.println("Error: expected first subscreen " +
                                                   " to have a view path of " + 
                                                   expectedViewPath + " but retrieved " +
                                                   " a view path of " + viewpath);
                                assertTrue(false);
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
                                assertTrue(false);
                            }
                            maxRecords = scrObj.getMaxRecords();
                            if (maxRecords != expectedMaxRecords) {
                                System.out.println("Error: expected second subscreen " +
                                                   " to have a max records of " + 
                                                   expectedMaxRecords + " but retrieved " +
                                                   " a page size of " + maxRecords);
                                assertTrue(false);
                            }
                            viewpath = scrObj.getViewPath();
                            if (expectedViewPath.compareTo(viewpath) != 0) {
                                System.out.println("Error: expected second subscreen " +
                                                   " to have a view path of " + 
                                                   expectedViewPath + " but retrieved " +
                                                   " a view path of " + viewpath);
                                assertTrue(false);
                            }
                            
                            assertTrue(true);
                        }
                    }
                }
            }
        } catch(Exception ex) {
            fail(ex.getMessage());
        }        
    }
    
    public void testDateFormats() {
        try {
            String dateFormat = configManager.getDateFormatForMultiDomain();
            String multiDomainDateFormat = "MM/dd/yyyy";
            if (dateFormat.compareTo(multiDomainDateFormat) != 0) {
                System.out.println("Error: expected multi-domain date format to be " + 
                                   multiDomainDateFormat + " but retrieved " + dateFormat);
                assertTrue(false);
            }
            
            ObjectDefinitionBuilder builder = new ObjectDefinitionBuilder();
            InputStream  stream = ObjectFactory.class.getResourceAsStream("/Domains/Person/object.xml");      
            ObjectDefinition person = builder.parse(stream);
            
            ObjectFactory objectFactory = ObjectFactoryRegistry.lookup("Person");        
            
            objectFactory.create("Person");

            dateFormat = configManager.getDateFormatForDomain("Person");
            String personDomainDateFormat = "MM/dd/yyyy";
            if (dateFormat.compareTo(personDomainDateFormat) != 0) {
                System.out.println("Error: expected  person domain date format to be " + 
                                   personDomainDateFormat + " but retrieved " + dateFormat);
                assertTrue(false);
            }
            
        } catch(Exception ex) {
            fail(ex.getMessage());
        }        
    }
    
    public void testDomainScreenConfig() {
        try {
            DomainScreenConfig domainSCFG = configManager.getDomainScreenConfig("Person");
            HashMap<String, ObjectNodeConfig> objNodeConfigMap = configManager.getObjectNodeConfig("Person");
                       
            ArrayList<SearchScreenConfig> searchSCFGS = domainSCFG.getSearchScreenConfigs();
            for (SearchScreenConfig searchCFG : searchSCFGS) {
                ArrayList<FieldConfigGroup> searchFieldCGS = searchCFG.getFieldConfigGroups();
                for (FieldConfigGroup fieldCG : searchFieldCGS) {
                    ArrayList<FieldConfig> fields = fieldCG.getFieldConfigs();
                    for (FieldConfig field : fields) {
                        
                        String fieldName = field.getFieldName();
                        ObjectNodeConfig objNodeConfig = objNodeConfigMap.get(field.getObjRef());
                        FieldConfig nField = objNodeConfig.getFieldConfig(fieldName);
                        if (nField != null) {
                            System.out.println(nField.toString());                        
                        } else {
                            assertTrue(false);
                        }
                    }
                }
            }            
        } catch (Exception ex) {                    
            ex.printStackTrace();
            fail(ex.getMessage());            
        }        
    }
}
