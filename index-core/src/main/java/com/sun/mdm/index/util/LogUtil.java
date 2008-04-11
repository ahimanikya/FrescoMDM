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
package com.sun.mdm.index.util;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Iterator;

/**
 * Log utility functions
 * @author  dcidon
 */
public class LogUtil {
    
    private static PackageTree sLogMapping;

    static {

        try {
            //Construct a package tree with default string "Common"
            sLogMapping = new PackageTree("Common");
            
            //Construct hashmap used to load tree
            HashMap hm = new HashMap();
            hm.put("Security", new String[] {
                        "com.sun.mdm.index.security" });

            hm.put("UpdateManager", new String[] {
                        "com.sun.mdm.index.update" });

            hm.put("MatchEngineController", new String[] {
                        "com.sun.mdm.index.matching",
                        "com.sun.mdm.index.phonetic" });

            hm.put("CodeLookup", new String[] {
                        "com.sun.mdm.index.ejb.codelookup",
                        "com.sun.mdm.index.codelookup" });

            hm.put("MasterController", new String[] {
                        "com.sun.mdm.index.ejb.master"});

            hm.put("OPS", new String[] {
                        "com.sun.mdm.index.ops",
                        "com.sun.mdm.index.idgen" });

            hm.put("Search", new String[] {
                        "com.sun.mdm.index.master", 
                        "com.sun.mdm.index.ejb.page",
                        "com.sun.mdm.index.page",
                        "com.sun.mdm.index.query",
                        "com.sun.mdm.index.querybuilder"});

            hm.put("PotentialDuplicateManager", new String[] {
                        "com.sun.mdm.index.potdup" });

            hm.put("SurvivorCalculator", new String[] {
                        "com.sun.mdm.index.ejb.survivor" });

            hm.put("AssumedMatchManager", new String[] {
                        "com.sun.mdm.index.assumedmatch"});

            hm.put("Audit", new String[] {
                        "com.sun.mdm.index.audit"});

            hm.put("ConfigurationService", new String[] {
                        "com.sun.mdm.index.configurator" });

            hm.put("DecisionMaker", new String[] {
                        "com.sun.mdm.index.decision" });

            hm.put("Outbound", new String[] {
                        "com.sun.mdm.index.outbound" });

            hm.put("SurvivorCalculator", new String[] {
                        "com.sun.mdm.index.survivor" });

            hm.put("Objects", new String[] {
                        "com.sun.mdm.index.objects" });

            hm.put("MIDM", new String[] {
                        "com.sun.mdm.index.edm" });

            hm.put("MatchEngine", new String[] {
                        "com.sun.mdm.matcher" });

            hm.put("StandardizationEngine", new String[] {
                        "com.sun.mdm.standardizer" });

            hm.put("Report", new String[] {
                        "com.sun.mdm.index.ejb.report",
                        "com.sun.mdm.index.report" });

            hm.put("Util", new String[] {
                        "com.sun.mdm.index.util" });

            hm.put("Filter", new String[] {
                        "com.sun.mdm.index.filter" });     
                        
            sLogMapping.addAssignment(hm);
        } catch (Exception e) {
        }
    }
    
    /** Log4j prefix */
    private static final String LOG_PREFIX = "SUN.MDM.INDEX.";
    
    /** Get logger for given object
     * @param obj object to get logger for
     * @return logger
     */
    public static Logger getLogger(Object obj) {
        return getLogger(obj.getClass().getName());
    }
    
     /** Get logger for given object
     * @param className class name
     * @return logger
     */
    public static Logger getLogger(String className) {
        String componentName = (String) sLogMapping.getObjectValue(className);
        return Logger.getLogger(LOG_PREFIX + componentName + "." + className);
    }
    
    public static String mapToString(Map map) {
        StringBuffer sb = new StringBuffer();
        Set keys = map.keySet();
        if (keys != null) {
            Iterator i = keys.iterator();
            while (i.hasNext()) {
                Object key = i.next();
                Object value = map.get(key);
                sb.append(key).append('=').append(value).append('\n');
            }
        }
        return sb.toString();
    }
    
    public static String listToString(List list) {
        StringBuffer sb = new StringBuffer();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            Object value = i.next();
            sb.append(value).append('\n');
        }
        return sb.toString();
    }
    
    public static String wrapString(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        char[] charArray = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        int count = 0;
        boolean wrap = false;
        for (int i=0; i < charArray.length; i++) {
            sb.append(charArray[i]);
            count++;
            if (count > 80) {
                wrap = true;
            }
            if (wrap && Character.isWhitespace(charArray[i])) {
                sb.append('\n');
                wrap = false;
                count = 0;
            }
        }
        return sb.toString();
    }
    
    public static void main(String args[]) {
        String[] testClasses = {
            "com.sun.mdm.index.ejb.master.MasterControllerEJB",
            "com.sun.mdm.index.objects.SystemObject",
            "com.sun.mdm.index.codegen.SomeClass",
            "com.sun.mdm.index.codegen.runtime.SomeClass"
        };
        for (int i = 0; i < testClasses.length; i++) {
            System.out.println(sLogMapping.getObjectValue(testClasses[i]));
        }
    }
    
}
