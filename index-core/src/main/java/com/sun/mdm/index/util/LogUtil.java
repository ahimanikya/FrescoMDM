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

            hm.put("EWays", new String[] {
                        "com.sun.mdm.index.eways" });

            hm.put("MatchEngineController", new String[] {
                        "com.sun.mdm.index.matching",
                        "com.sun.mdm.index.phonetic" });

            hm.put("CodeLookup", new String[] {
                        "com.sun.mdm.index.ejb.codelookup",
                        "com.sun.mdm.index.codelookup" });

            hm.put("EGate", new String[] {
                        "com.sun.mdm.index.ejb.egate" });

            hm.put("MasterController", new String[] {
                        "com.sun.mdm.index.ejb.master",
                        "com.sun.mdm.index.master" });

            hm.put("OPS", new String[] {
                        "com.sun.mdm.index.ejb.ops",
                        "com.sun.mdm.index.ops",
                        "com.sun.mdm.index.persistence" });

            hm.put("PagingSystem", new String[] {
                        "com.sun.mdm.index.ejb.page",
                        "com.sun.mdm.index.page" });

            hm.put("PotentialDuplicateManager", new String[] {
                        "com.sun.mdm.index.ejb.potdup",
                        "com.sun.mdm.index.master.search.potdup",
                        "com.sun.mdm.index.potdup" });

            hm.put("QueryManager", new String[] {
                        "com.sun.mdm.index.query",
                        "com.sun.mdm.index.query",
                        "com.sun.mdm.index.querybuilder",
                        "com.sun.mdm.index.querymgr" });

            hm.put("SurvivorCalculator", new String[] {
                        "com.sun.mdm.index.ejb.survivor" });

            hm.put("AssumedMatchManager", new String[] {
                        "com.sun.mdm.index.assumedmatch",
                        "com.sun.mdm.index.master.search.assumedmatch" });

            hm.put("Audit", new String[] {
                        "com.sun.mdm.index.audit",
                        "com.sun.mdm.index.master.search.audit" });

            hm.put("ConfigurationService", new String[] {
                        "com.sun.mdm.index.configurator" });

            hm.put("DecisionMaker", new String[] {
                        "com.sun.mdm.index.decision" });

            hm.put("IDGenerator", new String[] {
                        "com.sun.mdm.index.idgen" });

            hm.put("TransactionManager", new String[] {
                        "com.sun.mdm.index.master.search.transaction" });

            hm.put("Outbound", new String[] {
                        "com.sun.mdm.index.outbound" });

            hm.put("SurvivorCalculator", new String[] {
                        "com.sun.mdm.index.survivor" });

            hm.put("Generator", new String[] {
                        "com.sun.mdm.index.database",
                        "com.sun.mdm.index.generator",
                        "com.sun.mdm.index.parser" });

            hm.put("EnterpriseDesigner", new String[] {
                        "com.sun.mdm.index.codegen",
                        "com.sun.mdm.index.dbtool",
                        "com.sun.mdm.index.ede",
                        "com.sun.mdm.index.integration",
                        "com.sun.mdm.index.netbean",
                        "com.sun.mdm.index.wsdl" });

            hm.put("CodeGenRuntime", new String[] {
                        "com.sun.mdm.index.codegen.runtime"});            
                        
            hm.put("Integration", new String[] {
                        "com.sun.mdm.index.integration" });

            hm.put("Objects", new String[] {
                        "com.sun.mdm.index.objects" });

            hm.put("Validation", new String[] {
                        "com.sun.mdm.index.objects.validation" });

            hm.put("QWS", new String[] {
                        "com.sun.mdm.index.qws" });

            hm.put("SBME", new String[] {
                        "com.stc.sbme" });

            hm.put("Upgrade", new String[] {
                        "com.sun.mdm.index.upgrade" });

            hm.put("Webservice", new String[] {
                        "com.sun.mdm.index.webservice" });

            hm.put("Wizard", new String[] {
                        "java.com.sun.mdm.index.wizard" });        
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
