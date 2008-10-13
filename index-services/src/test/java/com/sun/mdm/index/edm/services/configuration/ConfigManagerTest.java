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
 
package com.sun.mdm.index.edm.services.configuration;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;

import java.util.ArrayList;
import java.util.Iterator;

/** Test class
 * @author cubiclecowboy
 */
public class ConfigManagerTest extends TestCase {

    private Integer RECORD_DETAILS_SCREEN_ID = new Integer(1);    
    private Integer TRANSACTIONS_SCREEN_ID = new Integer(2);    
    private Integer DUPLICATE_RECORDS_SCREEN_ID = new Integer(3);    
    private Integer ASSUMED_MATCHES_SCREEN_ID = new Integer(4);    
    private Integer SOURCE_RECORD_SCREEN_ID = new Integer(5);    
    private Integer REPORTS_SCREEN_ID = new Integer(6);    
    private Integer AUDIT_LOG_SCREEN_ID = new Integer(7);    
    private Integer DASHBOARD_SCREEN_ID = new Integer(8);    

    private int RECORD_DETAILS_SCREEN_DISPLAY_ORDER= 2;
    private int TRANSACTIONS_SCREEN_DISPLAY_ORDER = 4;
    private int DUPLICATE_RECORDS_SCREEN_DISPLAY_ORDER = 1;
    private int ASSUMED_MATCHES_SCREEN_DISPLAY_ORDER = 3;
    private int SOURCE_RECORD_SCREEN_DISPLAY_ORDER = 6;
    private int REPORTS_SCREEN_DISPLAY_ORDER = 5;
    private int AUDIT_LOG_SCREEN_DISPLAY_ORDER = 7;
    private int DASHBOARD_SCREEN_DISPLAY_ORDER = 0;

    private String RECORD_DETAILS_SCREEN_TITLE = "Record Details";
    private String TRANSACTIONS_SCREEN_TITLE = "Transactions";    
    private String DUPLICATE_RECORDS_SCREEN_TITLE = "Duplicate Records";    
    private String ASSUMED_MATCHES_SCREEN_TITLE = "Assumed Matches";    
    private String SOURCE_RECORD_SCREEN_TITLE = "Source Record";
    private String REPORTS_SCREEN_TITLE = "Reports";
    private String AUDIT_LOG_SCREEN_TITLE = "Audit Log";
    private String DASHBOARD_SCREEN_TITLE = "Dashboard";

    /** Creates new tester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    /** Creates a new instance of Tester */
    public ConfigManagerTest() {
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        try {
            
            // Initialize the ConfigManager
            System.out.println("Initializing the configuration manager");
            ConfigManager.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Verify the configuration of the main screens.
    
    public void testMainScreenConfigurations() throws Exception {
        
        // Retrieve the ConfigManager
        ConfigManager configManager = ConfigManager.getInstance();

        // Check the main tab configurations 
                
        // record details screen
        
        ScreenObject scrObj = configManager.getScreen(RECORD_DETAILS_SCREEN_ID);
        Integer screenID = scrObj.getID();
        if (!screenID.equals(RECORD_DETAILS_SCREEN_ID)) {
            System.out.println("Error for RECORD_DETAILS_SCREEN.  Expected " + 
                    RECORD_DETAILS_SCREEN_ID + " for the screen ID but retrieved " +
                    screenID + " instead.");
            assertTrue(false);
        }

        int displayOrder = scrObj.getDisplayOrder();
        if (displayOrder != RECORD_DETAILS_SCREEN_DISPLAY_ORDER) {
            System.out.println("Error for RECORD_DETAILS_SCREEN.  Expected " + 
                    RECORD_DETAILS_SCREEN_DISPLAY_ORDER + " for the screen display order but retrieved " +
                    displayOrder + " instead.");
            assertTrue(false);
        }

        String displayTitle = scrObj.getDisplayTitle();
        if (!displayTitle.equalsIgnoreCase(RECORD_DETAILS_SCREEN_TITLE)) {
            System.out.println("Error for RECORD_DETAILS_SCREEN.  Expected " + 
                    RECORD_DETAILS_SCREEN_TITLE + " for the screen display title but retrieved " +
                    displayTitle + " instead.");
            assertTrue(false);
        }
        
        // transactions screen

        scrObj = configManager.getScreen(TRANSACTIONS_SCREEN_ID);
        screenID = scrObj.getID();
        if (!screenID.equals(TRANSACTIONS_SCREEN_ID)) {
            System.out.println("Error for TRANSACTIONS_SCREEN.  Expected " + 
                    TRANSACTIONS_SCREEN_ID + " for the screen ID but retrieved " +
                    screenID + " instead.");
            assertTrue(false);
        }

        displayOrder = scrObj.getDisplayOrder();
        if (displayOrder != TRANSACTIONS_SCREEN_DISPLAY_ORDER) {
            System.out.println("Error for TRANSACTIONS_SCREEN.  Expected " + 
                    TRANSACTIONS_SCREEN_DISPLAY_ORDER + " for the screen display order but retrieved " +
                    displayOrder + " instead.");
            assertTrue(false);
        }

        displayTitle = scrObj.getDisplayTitle();
        if (!displayTitle.equalsIgnoreCase(TRANSACTIONS_SCREEN_TITLE)) {
            System.out.println("Error for TRANSACTIONS_SCREEN.  Expected " + 
                    TRANSACTIONS_SCREEN_TITLE + " for the screen display title but retrieved " +
                    displayTitle + " instead.");
            assertTrue(false);
        }
        
        // duplicate records screen
        
        scrObj = configManager.getScreen(DUPLICATE_RECORDS_SCREEN_ID);
        screenID = scrObj.getID();
        if (!screenID.equals(DUPLICATE_RECORDS_SCREEN_ID)) {
            System.out.println("Error for DUPLICATE_RECORDS_SCREEN.  Expected " + 
                    DUPLICATE_RECORDS_SCREEN_ID + " for the screen ID but retrieved " +
                    screenID + " instead.");
            assertTrue(false);
        }

        displayOrder = scrObj.getDisplayOrder();
        if (displayOrder != DUPLICATE_RECORDS_SCREEN_DISPLAY_ORDER) {
            System.out.println("Error for DUPLICATE_RECORDS_SCREEN.  Expected " + 
                    DUPLICATE_RECORDS_SCREEN_DISPLAY_ORDER + " for the screen display order but retrieved " +
                    displayOrder + " instead.");
            assertTrue(false);
        }

        displayTitle = scrObj.getDisplayTitle();
        if (!displayTitle.equalsIgnoreCase(DUPLICATE_RECORDS_SCREEN_TITLE)) {
            System.out.println("Error for DUPLICATE_RECORDS_SCREEN.  Expected " + 
                    DUPLICATE_RECORDS_SCREEN_TITLE + " for the screen display title but retrieved " +
                    displayTitle + " instead.");
            assertTrue(false);
        }
        
        // assumed matches screen
        
        scrObj = configManager.getScreen(ASSUMED_MATCHES_SCREEN_ID);
        screenID = scrObj.getID();
        if (!screenID.equals(ASSUMED_MATCHES_SCREEN_ID)) {
            System.out.println("Error for ASSUMED_MATCHES_SCREEN.  Expected " + 
                    ASSUMED_MATCHES_SCREEN_ID + " for the screen ID but retrieved " +
                    screenID + " instead.");
            assertTrue(false);
        }

        displayOrder = scrObj.getDisplayOrder();
        if (displayOrder != ASSUMED_MATCHES_SCREEN_DISPLAY_ORDER) {
            System.out.println("Error for ASSUMED_MATCHES_SCREEN.  Expected " + 
                    ASSUMED_MATCHES_SCREEN_DISPLAY_ORDER + " for the screen display order but retrieved " +
                    displayOrder + " instead.");
            assertTrue(false);
        }

        displayTitle = scrObj.getDisplayTitle();
        if (!displayTitle.equalsIgnoreCase(ASSUMED_MATCHES_SCREEN_TITLE)) {
            System.out.println("Error for ASSUMED_MATCHES_SCREEN.  Expected " + 
                    ASSUMED_MATCHES_SCREEN_TITLE + " for the screen display title but retrieved " +
                    displayTitle + " instead.");
            assertTrue(false);
        }
        
        // source record screen
        
        scrObj = configManager.getScreen(SOURCE_RECORD_SCREEN_ID);
        screenID = scrObj.getID();
        if (!screenID.equals(SOURCE_RECORD_SCREEN_ID)) {
            System.out.println("Error for SOURCE_RECORD_SCREEN.  Expected " + 
                    SOURCE_RECORD_SCREEN_ID + " for the screen ID but retrieved " +
                    screenID + " instead.");
            assertTrue(false);
        }

        displayOrder = scrObj.getDisplayOrder();
        if (displayOrder != SOURCE_RECORD_SCREEN_DISPLAY_ORDER) {
            System.out.println("Error for SOURCE_RECORD_SCREEN.  Expected " + 
                    SOURCE_RECORD_SCREEN_DISPLAY_ORDER + " for the screen display order but retrieved " +
                    displayOrder + " instead.");
            assertTrue(false);
        }

        displayTitle = scrObj.getDisplayTitle();
        if (!displayTitle.equalsIgnoreCase(SOURCE_RECORD_SCREEN_TITLE)) {
            System.out.println("Error for SOURCE_RECORD_SCREEN.  Expected " + 
                    SOURCE_RECORD_SCREEN_TITLE + " for the screen display title but retrieved " +
                    displayTitle + " instead.");
            assertTrue(false);
        }
        
        // reports screen
        
        scrObj = configManager.getScreen(REPORTS_SCREEN_ID);
        screenID = scrObj.getID();
        if (!screenID.equals(REPORTS_SCREEN_ID)) {
            System.out.println("Error for REPORTS_SCREEN.  Expected " + 
                    REPORTS_SCREEN_ID + " for the screen ID but retrieved " +
                    screenID + " instead.");
            assertTrue(false);
        }

        displayOrder = scrObj.getDisplayOrder();
        if (displayOrder != REPORTS_SCREEN_DISPLAY_ORDER) {
            System.out.println("Error for REPORTS_SCREEN.  Expected " + 
                    REPORTS_SCREEN_DISPLAY_ORDER + " for the screen display order but retrieved " +
                    displayOrder + " instead.");
            assertTrue(false);
        }

        displayTitle = scrObj.getDisplayTitle();
        if (!displayTitle.equalsIgnoreCase(REPORTS_SCREEN_TITLE)) {
            System.out.println("Error for REPORTS_SCREEN.  Expected " + 
                    REPORTS_SCREEN_TITLE + " for the screen display title but retrieved " +
                    displayTitle + " instead.");
            assertTrue(false);
        }
        
        // audit log screen
        
        scrObj = configManager.getScreen(AUDIT_LOG_SCREEN_ID);
        screenID = scrObj.getID();
        if (!screenID.equals(AUDIT_LOG_SCREEN_ID)) {
            System.out.println("Error for AUDIT_LOG_SCREEN.  Expected " + 
                    AUDIT_LOG_SCREEN_ID + " for the screen ID but retrieved " +
                    screenID + " instead.");
            assertTrue(false);
        }

        displayOrder = scrObj.getDisplayOrder();
        if (displayOrder != AUDIT_LOG_SCREEN_DISPLAY_ORDER) {
            System.out.println("Error for AUDIT_LOG_SCREEN.  Expected " + 
                    AUDIT_LOG_SCREEN_DISPLAY_ORDER + " for the screen display order but retrieved " +
                    displayOrder + " instead.");
            assertTrue(false);
        }

        displayTitle = scrObj.getDisplayTitle();
        if (!displayTitle.equalsIgnoreCase(AUDIT_LOG_SCREEN_TITLE)) {
            System.out.println("Error for AUDIT_LOG_SCREEN.  Expected " + 
                    AUDIT_LOG_SCREEN_TITLE + " for the screen display title but retrieved " +
                    displayTitle + " instead.");
            assertTrue(false);
        }
        
        // dashboard screen
        
        scrObj = configManager.getScreen(DASHBOARD_SCREEN_ID);
        screenID = scrObj.getID();
        if (!screenID.equals(DASHBOARD_SCREEN_ID)) {
            System.out.println("Error for DASHBOARD_SCREEN.  Expected " + 
                    DASHBOARD_SCREEN_ID + " for the screen ID but retrieved " +
                    screenID + " instead.");
            assertTrue(false);
        }

        displayOrder = scrObj.getDisplayOrder();
        if (displayOrder != DASHBOARD_SCREEN_DISPLAY_ORDER) {
            System.out.println("Error for DASHBOARD_SCREEN.  Expected " + 
                    DASHBOARD_SCREEN_DISPLAY_ORDER + " for the screen display order but retrieved " +
                    displayOrder + " instead.");
            assertTrue(false);
        }

        displayTitle = scrObj.getDisplayTitle();
        if (!displayTitle.equalsIgnoreCase(DASHBOARD_SCREEN_TITLE)) {
            System.out.println("Error for DASHBOARD_SCREEN.  Expected " + 
                    DASHBOARD_SCREEN_TITLE + " for the screen display title but retrieved " +
                    displayTitle + " instead.");
            assertTrue(false);
        }
        
        assertTrue(true);
    }

    // Check if the Epaths have been properly stored in the configuration.
            
    public void testEPathStrings() throws Exception {
            
        // Retrieve the ConfigManager
        ConfigManager configManager = ConfigManager.getInstance();
        
        // Retrieve the configuration for the duplicate records screen
        ScreenObject scrObj = configManager.getScreen(DUPLICATE_RECORDS_SCREEN_ID);
        
        // Retrieve the search criteria fields
        
        ArrayList sResultsConfigArrayList = scrObj.getSearchResultsConfig();
        assertTrue(sResultsConfigArrayList.size() == 1);    // should be only one search result config object
        Iterator srcalIterator = sResultsConfigArrayList.iterator();
        SearchResultsConfig srConfig = (SearchResultsConfig) srcalIterator.next();
        ArrayList epaths = srConfig.getEPaths();
        int ePathCount = epaths.size();
        
        assertTrue(ePathCount == 9);
    }

    // Check the configuration of the Reports subscreens
    
    public void testReportsSubscreen() throws Exception {
        
        ConfigManager configManager = ConfigManager.getInstance();
        
        // reports screen
        
        ScreenObject scrObj = configManager.getScreen(REPORTS_SCREEN_ID);
        Integer screenID = scrObj.getID();

        // retrieve the subscreens        
        ArrayList subscreensConfig = scrObj.getSubscreensConfig();
        assertTrue(subscreensConfig.size() == 7);
        
        boolean ret = true;
        Iterator subscreensIter = subscreensConfig.iterator();
        while (subscreensIter.hasNext()) {
            ScreenObject subScrObj = (ScreenObject) subscreensIter.next();
            int scrID = subScrObj.getID().intValue();
            ArrayList srcConfigList = subScrObj.getSearchResultsConfig();
            assert(srcConfigList.size() == 1);
            SearchResultsConfig srcObj = (SearchResultsConfig) srcConfigList.get(0);
            ArrayList fieldConfigs = srcObj.getFieldConfigs();
            int fieldConfigsSize = fieldConfigs.size();
            // TODO:  Add checks for individual field group elements
            switch(scrID) {
                case 0:     // Potential Duplicate Report
                    if (fieldConfigsSize != 2) {
                        System.out.println("Error: Potential Duplicate report " +
                                           "expected 2 field groups in fieldConfigs " +
                                           "but retrieved " + fieldConfigsSize);
                        ret = false;
                    }
                    break;
                case 1:     // Deactivated Report
                    if (fieldConfigsSize != 0) {
                        System.out.println("Error: Deactivated report " +
                                           "expected 0 field groups in fieldConfigs " +
                                           "but retrieved " + fieldConfigsSize);
                        ret = false;
                    }
                    break;
                case 2:     // Merge Report
                    if (fieldConfigsSize != 2) {
                        System.out.println("Error: Merge report " +
                                           "expected 2 field groups in fieldConfigs " +
                                           "but retrieved " + fieldConfigsSize);
                        ret = false;
                    }
                    break;
                case 3:     // Unmerge Report
                    if (fieldConfigsSize != 0) {
                        System.out.println("Error: Unmerge report " +
                                           "expected 0 field groups in fieldConfigs " +
                                           "but retrieved " + fieldConfigsSize);
                        ret = false;
                    }
                    break;
                case 4:     // Update Report
                    if (fieldConfigsSize != 2) {
                        System.out.println("Error: Update report " +
                                           "expected 2 field groups in fieldConfigs " +
                                           "but retrieved " + fieldConfigsSize);
                        ret = false;
                    }
                    break;
                case 5:     // Activity Report
                    if (fieldConfigsSize != 0) {
                        System.out.println("Error: Activity report " +
                                           "expected 0 field groups in fieldConfigs " +
                                           "but retrieved " + fieldConfigsSize);
                        ret = false;
                    }
                    break;
                case 6:     // Assumed Matches Report
                    if (fieldConfigsSize != 1) {
                        System.out.println("Error: Assumed Matches report " +
                                           "expected 1 field group in fieldConfigs " +
                                           "but retrieved " + fieldConfigsSize);
                        ret = false;
                    }
                    break;
                default:
                    System.out.println("Error: unrecognized screen ID: " + scrID);
                    ret = false;
            }
        }
        assertTrue(ret);      
    }


    // Check the parsing of the field information
    
    public void testFieldSizeAndLength() throws Exception {
        
        ConfigManager configManager = ConfigManager.getInstance();
        ObjectNodeConfig objNodeConfig = (ObjectNodeConfig) configManager.getObjectNodeConfig("Person");
        boolean ret = true;
        
        // get required and updateable attributes from object definition
        FieldConfig[] fieldconfig = objNodeConfig.getFieldConfigs();        
        boolean fieldFound = false;
        for (int i = 0; i < fieldconfig.length; i++) {
            String displayName = fieldconfig[i].getDisplayName();
            if (displayName.equalsIgnoreCase("First Name") == true) {
                fieldFound = true;
                int maxSize = fieldconfig[i].getMaxSize();
                int maxLength = fieldconfig[i].getMaxLength();
                if (maxSize != 40) {
                    ret = false;
                }
                if (maxLength != 40) {
                    ret = false;
                }
            }
        }        
        if (fieldFound == false) {
            ret = false;
        }
        assertTrue(ret);
    }        
    
    // Check the parsing of the field information
    
    public void testChildNodeConfig() throws Exception {
        
        ConfigManager configManager = ConfigManager.getInstance();
        ObjectNodeConfig objNodeConfig = (ObjectNodeConfig) configManager.getObjectNodeConfig("Phone");
        boolean ret = true;

        boolean mergeMustDelete = objNodeConfig.getMustDelete();
        if (mergeMustDelete != true) {
            System.out.println("Error: merge-must-delete expected to be true but was actually " + mergeMustDelete);
            ret = false;
        }
        int displayOrder = objNodeConfig.getDisplayOrder();
        if (displayOrder != 3) {
            System.out.println("Error: displayOrder expected to be 3 but was actually " + displayOrder);
            ret = false;
        }
        assertTrue(ret);
    }        
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(ConfigManagerTest.class));
    }
    
}
