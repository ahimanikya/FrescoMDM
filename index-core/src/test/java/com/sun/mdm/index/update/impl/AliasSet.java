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
 
package com.sun.mdm.index.update.impl;
import com.sun.mdm.index.objects.AliasObject;
import com.sun.mdm.index.objects.exception.ObjectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A set of aliases
 * @author  dcidon
 */
public class AliasSet {
    
    private final HashMap mMap = new HashMap();
    
    /**
     * Add alias to set
     * @param alias alias to be added
     */
    public void addAlias(AliasObject alias) {
        AliasContainer ac = new AliasContainer(alias);
        mMap.put(ac, alias);
    }
    
    /**
     * Remove alias to set
     * @param alias to be removed
     */
    public void removeAlias(AliasObject alias) {
        AliasContainer ac = new AliasContainer(alias);
        mMap.remove(ac);
    }
    
    /**
     * Add aliases to set
     * @param c collection of aliases
     */
    public void addAliases(Collection c) {
        Iterator i = c.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            AliasObject alias = (AliasObject) obj;
            addAlias(alias);
        }
    }
    
    /**
     * Remove aliases from set
     * @param c collection of aliases
     */
    public void removeAliases(Collection c) {
        Iterator i = c.iterator();
        while (i.hasNext()) {
            AliasObject alias = (AliasObject) i.next();
            removeAlias(alias);
        }
    }
    
    /**
     * Get all aliases in set
     * @return alias array
     */
    public AliasObject[] getAliases() {
        Collection c = mMap.values();
        if (c == null) {
            return new AliasObject[0];
        }
        Object aliasObjs[] = c.toArray();
        AliasObject aliases[] = new AliasObject[aliasObjs.length];
        for (int i = 0; i < aliases.length; i++) {
            aliases[i] = (AliasObject) aliasObjs[i];
        }
        return aliases;
    }
    
    /**
     * Return true if alias in set
     * @param alias alias
     * @return true if found
     */
    public boolean hasAlias(AliasObject alias) {
        AliasContainer ac = new AliasContainer(alias);
        return mMap.containsKey(ac);
    }
    
    /**
     * Get alias if in set or return null
     * @param alias alias
     * @return alias or null
     */
    public AliasObject getAlias(AliasObject alias) {
        AliasContainer ac = new AliasContainer(alias);
        return (AliasObject) mMap.get(ac);
    }
    
    /**
     * Copy set
     * @throws ObjectException exception
     * @return Object copy
     */
    public Object copy() throws ObjectException {
        AliasSet newSet = new AliasSet();
        AliasObject[] aliases = getAliases();
        for (int i = 0; i < aliases.length; i++) {
            newSet.addAlias((AliasObject) aliases[i].copy());
        }
        return newSet;
    }
    
}

