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
package com.sun.mdm.multidomain.services.security;

import java.util.List;
import java.util.ArrayList;

import com.sun.mdm.multidomain.services.core.ConfigException;

/**
 * ACL class.
 * @author cye
 */
public class ACL {    
    private List<Entry> entries = new ArrayList<Entry>();
            
    public ACL(){
    }
    
    public ACL(List<String> operations)
        throws ConfigException {
        initialize(operations);
    }
    
    public void initialize(List<String> operations) 
        throws ConfigException {
        for (String operation : operations) {
            List<ACL.Entry> methods = Operations.getMethods(operation);
            for (ACL.Entry method : methods) {
                if (!entries.contains(method)) {
                    entries.add(method);
                }
            }
        }      
    }
            
    public boolean checkPermission(String name) {
        return entries.contains(name);
    }
    
    public boolean checkPermission(ACL.Entry entry) {
        boolean isPermitted = false;
        for (Entry e : entries) {
         if (e.getName().equals(entry.getName()) &&
                e.getAction().equals(entry.getAction()) ) {
                isPermitted = true;
                break;
            }          
        }
        return isPermitted;
    }
    
    public boolean checkPermission(String name, String action) {
        boolean isPermitted = false;
        for (Entry e : entries) {
            if (e.getName().equals(name) &&
                e.getAction().equals(action) ) {
                isPermitted = true;
                break;
            }
        }
        return isPermitted;
    }
    
    public static class Entry {
        private String name;
        private String action;
        
        public Entry(){  
        }
        public Entry(String name, String action) {
            this.name = name;
            this.action = action;  
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getAction() {
            return action;
        }
        public void setAction(String action) {
            this.action = action;
        }        
        @Override
        public boolean equals(Object o) {
            boolean isEqual = false;
            if (o != null && o instanceof Entry && this.name != null) {
                if (this.name.equals(((Entry)o).getName())){
                    isEqual = true;
                } 
            } 
            return isEqual;
        } 
    }
    
}
