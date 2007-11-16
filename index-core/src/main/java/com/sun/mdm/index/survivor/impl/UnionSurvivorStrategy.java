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
package com.sun.mdm.index.survivor.impl;

import java.util.Date;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.logging.Level;
import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectKey;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.exception.ObjectException;

import com.sun.mdm.index.survivor.StrategyCreationException;
import com.sun.mdm.index.survivor.SurvivorCalculationException;
import com.sun.mdm.index.survivor.SurvivorStrategyInterface;
import com.sun.mdm.index.survivor.SystemField;
import com.sun.mdm.index.survivor.SystemFieldList;
import com.sun.mdm.index.survivor.SystemFieldListMap;
import com.sun.mdm.index.util.Localizer;



/** Unions the values from each SystemObject and return it as a collection
 *
 * 
 */
public class UnionSurvivorStrategy
implements SurvivorStrategyInterface {
    
    private transient static final Logger mLogger = Logger.getLogger(UnionSurvivorStrategy.class);
    private transient final Localizer mLocalizer = Localizer.get();
    
    /** Creates a new instance of UnionSurvivorStrategy */
    public UnionSurvivorStrategy() {
    }
    
    /**
     * @see com.sun.mdm.index.survivor.SurvivorStrategyInterface#init
     */
    public void init(java.util.Collection parameters)
    throws StrategyCreationException {
    }
    
    /** Unions the values from each SystemObject and return it as a collection
     * @see com.sun.mdm.index.survivor.SurvivorStrategyInterface#selectField
     */
    public SystemField selectField(String candidateId, SystemFieldListMap fields)
    throws SurvivorCalculationException {
        HashMap set = new HashMap();
        
        Collection c = fields.values();
        Iterator iter = c.iterator();
        try {
            while (iter.hasNext()) {
                SystemFieldList sfl = (SystemFieldList) iter.next();
                SystemField sf = sfl.get(candidateId);
                Object value = sf.getValue();
                
                SystemField sysDate = sfl.get("LastModified");
                Date date = (Date) sysDate.getValue();
                
                if (value != null) {
                    if (value instanceof Collection) {
                        Collection v = (Collection) value;
                        Iterator vIter = v.iterator();
                        
                        while (vIter.hasNext()) {
                            add(set, vIter.next(), date);
                        }
                    } else {
                        add(set, value, date);
                    }
                }
            }
        }catch (ObjectException oex) {
            throw new SurvivorCalculationException(mLocalizer.t("SUR526: Could not " +
                                            "select field: {0}", oex));
        }
        
        ArrayList a;
        if (set.size() > 0) {
            a = new ArrayList(set.keySet());
        } else {
            a = null;
        }
        // return a collection in SystemField
        return new SystemField(candidateId, a);
    }
    
    /** {@inheritDoc}
     * @return shallow clone
     * @throws CloneNotSupportedException cloning is not supported
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    private void add(Map set, Object input, Date date) throws ObjectException, SurvivorCalculationException {
        // don't add objects into the set with duplicate objectkey values
        if (input instanceof ObjectNode) {
            ObjectNode sNode = (ObjectNode) input;
            ObjectKey sKey = sNode.pGetKey();
            if (sKey != null) {
                // since there is a key for the object, look in the list
                // to see if an object of the same key exists already
                Set s = set.entrySet();
                Iterator siter = s.iterator();
                while (siter.hasNext()) {
                    Map.Entry entry = (Map.Entry) siter.next();
                    Object ao = entry.getKey();
                    // assert ao is object node
                    // since object from set is object node, all objects in array
                    // should be object node also
                    if (!(ao instanceof ObjectNode)) {
                        throw new SurvivorCalculationException(mLocalizer.t("SUR527: Expected " +
                                            "candidate value to be an ObjectNode " + 
                                            "instance, but retrieved this instead: {0}", ao));
                    }
                    ObjectNode aNode = (ObjectNode) ao;
                    ObjectKey aKey = aNode.pGetKey();
                    if (sKey.equals(aKey)) {
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("Found element with same key in current set: " + sKey);
                        }
                        // wahoo, we have a match, compare which value is newer
                        Date aDate = (Date) entry.getValue();
                        
                        if (date.compareTo(aDate) > 0) {
                            if (mLogger.isLoggable(Level.FINE)) {
                                mLogger.fine("Input element is newer than existing element, overwriting");
                            }
                            // if input date is newer than date of existing object
                            // put in the new one instead
                            set.remove(aNode);
                            if (mLogger.isLoggable(Level.FINE)) {
                                mLogger.fine("Adding element to set: " + input);
                            }
                            set.put(input, date);
                            
                        }
                        // stop looking through the set, because we have modified the
                        // map, the iterator is no longer invalid
                        return;
                    }
                }
                // went through all the loops, but did not find any matching keys
                // simply add it to the set.
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Adding element to set: " + input);
                }
                set.put(input, date);
            } else {
                // if object node does not have a key, don't bother checking
                // for duplicate keys
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Adding element to set: " + input);
                }
                set.put(input, date);
            }
        } else {
            // object is not object node, so no key. just add it
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Adding element to set : " + input);
            }
            set.put(input, date);
        }
    }
}
