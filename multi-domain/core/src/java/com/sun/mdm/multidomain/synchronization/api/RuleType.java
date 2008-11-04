/*
* DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
*
* Copyright 1997-2007 Sun Microsystems, Inc. All Rights Reserved.
*
* The contents of this file are subject to the terms of the Common
* Development and Distribution License ("CDDL")(the "License"). You
* may not use this file except in compliance with the License.
*
* You can obtain a copy of the License at
* https://open-esb.dev.java.net/public/CDDLv1.0.html
* or mural/license.txt. See the License for the specific language
* governing permissions and limitations under the License. *
* When distributing Covered Code, include this CDDL Header Notice
* in each file and include the License file at mural/license.txt.
* If applicable, add the following below the CDDL Header, with the
* fields enclosed by brackets [] replaced by your own identifying
* information: "Portions Copyrighted [year] [name of copyright owner]"
*/ 

package com.sun.mdm.multidomain.synchronization.api;

import java.util.HashMap;
import java.util.Map;

import net.java.hulp.i18n.LocalizedString;

/**
 * Enumeration class containing rule types.
 *
 * @author Shant Gharibi (shant.gharibi@sun.com)
 */
public enum RuleType {
    RELATIONSHIP("RELATIONSHIP"),
    HIERARCHY("HIERARCHY"),
    GROUP("GROUP"),
    CATEGORY("CATEGORY")
    ;
    
    private final String type;
    private static final Map<String, RuleType> typeMap = new HashMap<String, RuleType>();
    static {
        for (final RuleType type : RuleType.values()) {
            typeMap.put(type.toString(), type);
        }
    }
    
    /**
     * Construct a RuleType
     * 
     * @param type the string representation of the rule type
     */
    RuleType(final String type) {
        this.type = type;
    }
    
    @Override
	public String toString(){
        return this.type;
    }
    
    /**
     * Return an RuleType based on its string representation.
     * 
     * @param type the rule type as a string
     * @return the RuleType enumeration class based on the input string
     * @throws Exception if no such rule type exists
     */
    public static RuleType getRuleType(final String type) {
        String normalizedType = type.toUpperCase();
        
        if (!typeMap.containsKey(normalizedType)) {
    		net.java.hulp.i18n.Logger sLog = net.java.hulp.i18n.Logger.getLogger(RuleType.class);
    		Localizer localizer = Localizer.get();
    		LocalizedString message = localizer.x("SYN001: No such rule type: {0}", normalizedType);
    		sLog.severe(message); 
            throw new SynchronizationException(message);
        }

        return typeMap.get(normalizedType);
    }
 
}
