/*
 * BEGIN_HEADER - DO NOT EDIT
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * If applicable add the following below this CDDL HEADER,
 * with the fields enclosed by brackets "[]" replaced with
 * your own identifying information: Portions Copyright
 * [year] [name of copyright owner]
 */

/*
 * @(#)ValidationService.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.index.edm.services.configuration;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import com.sun.mdm.index.edm.control.QwsController;
import com.sun.mdm.index.edm.common.PullDownListItem;
import com.sun.mdm.index.codelookup.CodeDescription;
import com.sun.mdm.index.codelookup.UserCode;
import com.sun.mdm.index.master.SystemDefinition;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

/**
 * @todo Document this class
 * @author Xi Song
 */
public class ValidationService {
    private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.service.configuration.ValidationService");
    static ValidationService instance = null;
    static HashMap vmmap;
    static HashMap sysmap;
    static Map usercodemap;
    static String currentModule;

    /** constant
     */
    public static final String CONFIG_MODULE_FUNCTION = "FUNCTION";
    /** constant
     */
    public static final String CONFIG_MODULE_AUDIT_FUNCTION = "AUDITFUNCTION";
    /** constant
     */
    public static final String CONFIG_MODULE_SYSTEM = "SYSTEMS_DEFINED_IN_SBYN_SYSTEMS_TABLE";
    /** constant
     */
    public static final String CONFIG_MODULE_RESOLVETYPE = "RESOLVETYPE";

    public static void init() throws Exception {
        vmmap = new HashMap(0);
        sysmap = new HashMap(0); 
        usercodemap = new HashMap(0);   //JEFF


        // set transaction function
        Map vm = new HashMap();
        CodeDescription cd = new CodeDescription(CONFIG_MODULE_FUNCTION, "Add", "Add");
        vm.put("Add", cd);
        cd = new CodeDescription(CONFIG_MODULE_FUNCTION, "Update", "Update");
        vm.put("Update", cd);
        cd = new CodeDescription(CONFIG_MODULE_FUNCTION, "euidMerge", "EUID Merge");
        vm.put("euidMerge", cd);
        cd = new CodeDescription(CONFIG_MODULE_FUNCTION, "euidUnMerge", "EUID Unmerge");
        vm.put("euidUnMerge", cd);
        cd = new CodeDescription(CONFIG_MODULE_FUNCTION, "euidActivate", "EUID Activate");
        vm.put("euidActivate", cd);
        cd = new CodeDescription(CONFIG_MODULE_FUNCTION, "euidDeactivate", "EUID Deactivate");
        vm.put("euidDeactivate", cd);
        cd = new CodeDescription(CONFIG_MODULE_FUNCTION, "lidMerge", "System Record Merge");
        vm.put("lidMerge", cd);
        cd = new CodeDescription(CONFIG_MODULE_FUNCTION, "lidUnMerge", "System Record Unmerge");
        vm.put("lidUnMerge", cd);
        cd = new CodeDescription(CONFIG_MODULE_FUNCTION, "lidTransfer", "System Record Transfer");
        vm.put("lidTransfer", cd);
        vmmap.put(CONFIG_MODULE_FUNCTION, vm);
        
        // set audit log functions
        vm = new HashMap();
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "Add", "Add");
        vm.put("Add", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "Update","Update");
        vm.put("Update", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "EO Search Result", "EO Search Result");
        vm.put("EO Search Result", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "EO View/Edit", "EO View/Edit");
        vm.put("EO View/Edit", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "EO Comparison", "EO Comparison");
        vm.put("EO Comparison", cd);
        //cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "Audit Log Search Result", "Audit Log Search Result");
        //vm.put("Audit Log Search Result", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "History Comparison", "History Comparison");
        vm.put("History Comparison", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "History Search Result", "History Search Result");
        vm.put("History Search Result", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "Matching Review Search Result", "Matching Review Search Result");
        vm.put("Matching Review Search Result", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "Assumed Match Search Result", "Assumed Match Search Result");
        vm.put("Assumed Match Search Result", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "Associated Potential Duplicates", "Associated Potential Duplicates");
        vm.put("Associated Potential Duplicates", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "Potential Duplicate Comparison", "Potential Duplicate Comparison");
        vm.put("Potential Duplicate Comparison", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "Assumed Match Comparison", "Assumed Match Comparison");
        vm.put("Assumed Match Comparison", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "Undo Assumed Match", "Undo Assumed Match");
        vm.put("Undo Assumed Match", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "Resolve", "Resolve");
        vm.put("Resolve", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "Unresolve", "Unresolve");
        vm.put("Unresolve", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "Auto Resolve", "Auto Resolve");
        vm.put("Auto Resolve", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "Merge", "Merge");
        vm.put("Merge", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "Unmerge Comparison", "Unmerge Comparison");
        vm.put("Unmerge Comparison", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "EUID Unmerge", "EUID Unmerge");
        vm.put("EUID Unmerge", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "LID Unmerge", "LID Unmerge");
        vm.put("LID Unmerge", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "EUID Merge Confirm", "EUID Merge Confirm");
        vm.put("EUID Merge Confirm", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "LID Merge Confirm", "LID Merge Confirm");
        vm.put("LID Merge Confirm", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "LID Merge - Selection", "LID Merge - Selection");
        vm.put("LID Merge - Selection", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "EUID Unmerge Confirm", "EUID Unmerge Confirm");
        vm.put("EUID Unmerge Confirm", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "LID Unmerge Confirm", "LID Unmerge Confirm");
        vm.put("LID Unmerge Confirm", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "View Merge Tree", "View Merge Tree");
        vm.put("View Merge Tree", cd);
        cd = new CodeDescription(CONFIG_MODULE_AUDIT_FUNCTION, "Merge Tree Comparison", "Merge Tree Comparison");
        vm.put("Merge Tree Comparison", cd);
        vmmap.put(CONFIG_MODULE_AUDIT_FUNCTION, vm);

        vm = new HashMap();
        cd = new CodeDescription(CONFIG_MODULE_RESOLVETYPE, "R", "Resolved");
        vm.put("R", cd);
        cd = new CodeDescription(CONFIG_MODULE_RESOLVETYPE, "A", "Permanently Resolved");
        vm.put("A", cd);
        cd = new CodeDescription(CONFIG_MODULE_RESOLVETYPE, "U", "Unresolved");
        vm.put("U", cd);
        vmmap.put(CONFIG_MODULE_RESOLVETYPE, vm);
        
        vm = new HashMap();
        SystemDefinition[] sysdefs = new SystemDefinition[0];

        sysdefs = QwsController.getMasterController().lookupSystemDefinitions();
        if (sysdefs != null) {
                for (int i = 0; i < sysdefs.length; i++) {
                  if (!sysdefs[i].getSystemCode().equalsIgnoreCase("qws")) {
                    cd = new CodeDescription(CONFIG_MODULE_SYSTEM, sysdefs[i].getSystemCode(), 
                                                            sysdefs[i].getDescription());
                    vm.put(sysdefs[i].getSystemCode(), cd);
                    // cache the system record info so that maethod can retrieve inputmask and valuemask
                    sysmap.put(sysdefs[i].getSystemCode(), sysdefs[i]);
                  }
                }
        } else {
            mLogger.debug("Source systems are not defined in database.");
            throw new Exception ("Source systems are not defined in database.");
        }
        vmmap.put(CONFIG_MODULE_SYSTEM, vm);
        // Load all user codes
        usercodemap = (Map) QwsController.getUserCodeLookup().getAllCodes();
        if (usercodemap != null) {
            for (Iterator iter = usercodemap.keySet().iterator(); iter.hasNext();) {
                currentModule = (String) iter.next();
                break;
            }
        } else {
            mLogger.debug("User codes are not defined in database.");
            throw new Exception ("User codes are not defined in database.");
        }
    }

    ValidationService() {
        super();
    }

    /**
     * @todo Document: Getter for Instance attribute of the ValidationService
     *      class
     * @return the only instance of the class
     */
    public static ValidationService getInstance() {
        if (instance == null) {
            instance = new ValidationService();
        }

        return instance;
    }
        
    /**
     * @todo Document: Getter for user code ValueCount attribute of the ValidationService
     *      object
     * @param module name of the list
     * @return number of items in the user code value list
     */
    public int getUserCodeValueCount(String module) {
        if (module == null) {
            return 0;
        }
        Map vm = (Map) usercodemap.get(module);
        if (vm == null) {
            try {
                vm = (Map) QwsController.getUserCodeLookup().getCodesByModule(module);
            } catch (Exception e) {
                // There is no stacktrace in QwsController, so the stacktrace
                // should be logged here.
                mLogger.error("Error occurs while retrieving user codes: ", e);
                vm = null;
            }
            if (vm == null) {
                return 0;
            }
            usercodemap.put(module, vm);
        }
        
        return vm.size();
    }
    
    /**
     * @todo Document: Getter for user code ValueItems attribute of the ValidationService
     *      object
     * @param module name of the list
     * @return a PullDownListItem object
     */
    public PullDownListItem[] getUserCodeValueItems(String module) {
        Map vm = (Map) usercodemap.get(module);
        if (vm == null) {
            try {
                vm = (Map) QwsController.getUserCodeLookup().getCodesByModule(module);
            } catch (Exception e) {
                // There is no stacktrace in QwsController, so the stacktrace
                // should be logged here.
                mLogger.error("Error occurs while retrieving user codes: ", e);
                vm = null;
            }
            if (vm == null) {
                return null;
            }
            usercodemap.put(module, vm);
        }

        PullDownListItem[] items = new PullDownListItem[vm.size()];
        int i = 0;
        for (Iterator iter = vm.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            UserCode val = (UserCode) vm.get(key);
            items[i++] = new PullDownListItem(key, val.getDescription());
        }
        return items;
    }
    
    /**
     * @todo Document: Getter for all user code input mask array.
     * @return userCodeMask input mask array.
     */
    public String[][] getUserCodeInputMasks(String module) {
        if (module == null) {
            return new String[2][];
        }
        int count = getUserCodeValueCount(module);
        if (count == 0) {
            return new String[2][];
        }
        int index = 0;
        String[][] userCodeMask = new String[2][count];
        Map map = (Map) usercodemap.get(currentModule);
        for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            UserCode codeDef = (UserCode) map.get(key);
            userCodeMask[0][index] = codeDef.getCode();
            userCodeMask[1][index] = (codeDef.getInputMask() == null ? "" : codeDef.getInputMask());
            index++;
        }
        return userCodeMask;
    }
    
    /**
     * @todo Document: Getter for user code input mask of the type
     * @param code type of user code
     * @param code the code value
     * @return input mask the input mask of the code of type
     */
    public String getUserCodeInputMask(String module, String code) {
        if (module == null || module.trim().length() == 0 ||
            code == null || code.trim().length() == 0) {
            return null;
        }
        Map map = (Map) usercodemap.get(module);
        if (map == null) {
            return null;
        }
        UserCode codeDef = (UserCode) map.get(code);
        if (codeDef == null) {
            return null;
        }
        return codeDef.getInputMask();
    }
        
    /**
     * @todo Document: Getter for code list value mask.
     * @param codeList type of code list value
     * @param code the code value
     * @return value mask the value mask of the code of the code list
     */
    public String getUserCodeValueMask(String module, String code) {
        if (module == null || module.trim().length() == 0 ||
            code == null || code.trim().length() == 0) {
            return null;
        }
        Map map = (Map) usercodemap.get(module);
        if (map == null) {
            return null;
        }
        UserCode codeDef = (UserCode) map.get(code);
        if (codeDef == null) {
            return null;
        }
        return codeDef.getValueMask();
    }
    
    /**
     * @todo Document: Getter for user code description attribute of the ValidationService
     *      object
     * @param valueListName the name of the user code module
     * @param code the code among the list
     * @return description of the code pair
     */
    public String getUserCodeDescription(String module, String code) {
        if (module == null || module.trim().length() == 0 ||
            code == null || code.trim().length() == 0) {
            return null;
        }
        Map map = (Map) usercodemap.get(module);
        if (map == null) {
            return null;
        }
        UserCode codeDef = (UserCode) map.get(code);
        if (codeDef == null) {
            return null;
        }
        return codeDef.getDescription();
    }
    
    /**
     * @todo Document: Getter for local ID input mask.
     * @param systemCode the code of the system
     * @return input mask the input mask of the local ID for the system.
     */
    public String getInputMask(String systemCode) {
        if (systemCode == null || systemCode.trim().length() == 0) {
            return null;
        }
        SystemDefinition sysdef = (SystemDefinition) sysmap.get(systemCode);
        if (sysdef == null) {
            return null;
        }
        return sysdef.getInputMask();
    }
    
    /**
     * @todo Document: Getter for local ID input mask.
     * @param systemCode the code of the system
     * @return input mask the input mask of the local ID for the system.
     */
    public String getValueMask(String systemCode) {
        if (systemCode == null || systemCode.trim().length() == 0) {
            return null;
        }
        SystemDefinition sysdef = (SystemDefinition) sysmap.get(systemCode);
        if (sysdef == null) {
            return null;
        }
        return sysdef.getValueMask();
    }
    
    /**  Retrieves the system definition given the system code
     * 
     * @param systemCode system code
     * @return system description
     */
    public String getSystemDescription(String systemCode) {
        if (systemCode == null || systemCode.trim().length() == 0) {
            return null;
        }
        SystemDefinition sysdef = (SystemDefinition) sysmap.get(systemCode);
        if (sysdef == null) {
            return null;
        }
        return sysdef.getDescription();
    }
    
    /**
     * @todo Document: Getter for system/local ID input mask array.
     * @return systemMask system/input mask array.
     */
    public String[][] getSystemInputMasks() {
        
        int count = getValueCount(CONFIG_MODULE_SYSTEM);
        if (count == 0) {
            return new String[2][];
        }
        int index = 0;
        String[][] sysmask = new String[2][count];
        for (Iterator iter = sysmap.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            SystemDefinition sysdef = (SystemDefinition) sysmap.get(key);
            sysmask[0][index] = sysdef.getSystemCode();
            sysmask[1][index] = (sysdef.getInputMask() == null ? "" : sysdef.getInputMask());
            index++;
        }
        return sysmask;
    }

    /**
     * @todo Document: Getter for ValueCount attribute of the ValidationService
     *      object
     * @param valueListName the name of the list
     * @return number of items in the value list
     */
    public int getValueCount(String valueListName) {
        Map vm = (Map) vmmap.get(valueListName);
        if (vm == null) {
        try {
               vm = (Map) QwsController.getValidationService().getCodesByModule(valueListName);
        } catch (Exception e) {
            // There is no stacktrace in QwsController, so the stacktrace
            // should be logged here.
            mLogger.error("Error occurs while retrieving code list: ", e);
            vm = null;
        }
        if (vm == null) {
            return 0;
        }
        vmmap.put(valueListName, vm);
    }
    return vm.size();
    }

    /**
     * @todo Document: Getter for ValueItems attribute of the ValidationService
     *      object
     * @param valueListName the name of the list
     * @return a PullDownListItem object
     */
    public PullDownListItem[] getValueItems(String valueListName) {
        Map vm = (Map) vmmap.get(valueListName);
        if (vm == null) {
            try {
                vm = (Map) QwsController.getValidationService()
                            .getCodesByModule(valueListName);
            } catch (Exception e) {
                // There is no stacktrace in QwsController, so the stacktrace
                // should be logged here.
                mLogger.error("Error occurs while retrieving code list: ", e);
                vm = null;
            }
            if (vm == null) {
                return null;
            }
            vmmap.put(valueListName, vm);
        }

        PullDownListItem[] items = new PullDownListItem[vm.size()];
        int i = 0;
        for (Iterator iter = vm.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            CodeDescription val = (CodeDescription) vm.get(key);
            items[i++] = new PullDownListItem(key, val.getDescription());
        }
        return items;
    }
    
    /**
     * @todo Document: Getter for description attribute of the ValidationService
     *      object
     * @param valueListName the name of the list
     * @param code the code among the list
     * @return description of the code pair
     */
    public String getDescription(String valueListName, String code) {
        Map vm = (Map) vmmap.get(valueListName);
        if (vm == null) {
            try {
                   vm = (Map) QwsController.getValidationService()
                            .getCodesByModule(valueListName);
            } catch (Exception e) {
                // There is no stacktrace in QwsController, so the stacktrace
                // should be logged here.
                mLogger.error("Error occurs while retrieving code list: ", e);
                vm = null;
            }
            if (vm == null) {
                return null;
            }
            vmmap.put(valueListName, vm);
        }

        for (Iterator iter = vm.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            if (code.equals(key)) {
                CodeDescription val = (CodeDescription) vm.get(key);
                return val.getDescription();
            }
        }
        return null;
    }
}
