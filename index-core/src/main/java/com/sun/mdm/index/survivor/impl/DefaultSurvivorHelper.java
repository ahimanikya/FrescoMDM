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

import com.sun.mdm.index.configurator.impl.SurvivorHelperConfig;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SystemObjectException;

import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathParser;

import com.sun.mdm.index.survivor.SurvivorCalculationException;
import com.sun.mdm.index.survivor.SystemField;
import com.sun.mdm.index.survivor.SystemFieldList;
import com.sun.mdm.index.survivor.SystemFieldListMap;
import com.sun.mdm.index.survivor.HelperCreationException;
import com.sun.mdm.index.survivor.StrategyCreationException;
import com.sun.mdm.index.util.Localizer;

import java.util.Hashtable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

/**Default survivor helper implementation, using ePath
 *

 * @version $Revision: 1.1 $
 */
public class DefaultSurvivorHelper
    extends com.sun.mdm.index.survivor.AbstractSurvivorHelper {
        
    private transient final Localizer mLocalizer = Localizer.get();
    
    /** pre-parse epaths and cache the ePath objects here
     */
    protected Map mEPathCache;
        
    /** Creates new DefaultSurvivorHelper */
    public DefaultSurvivorHelper() {
        super();
        mEPathCache = new HashMap(25);
    }

    /** returns a mapping of system field values for the candidate field
     * @return Mapping of system fields using system id as Key.
     * Key: system id         Value: collection of SystemField value objects
     * return null if error getting system fields
     * @param candidateId candidate field name
     * @param rec omega object
     * @throws SurvivorCalculationException unexpected value
     * @throws SystemObjectException error accessing entity object
     */
    public SystemFieldListMap getSystemFields(EnterpriseObject rec, String candidateId) 
        throws SurvivorCalculationException, SystemObjectException {
        try {
            Collection systems = rec.getSystemObjects();

            Iterator sysIter = systems.iterator();

            SystemFieldListMap sysFields = new SystemFieldListMap();

            // iterate through the collection of system objects
            // only process system objects that are active
            while (sysIter.hasNext()) {
                SystemObject so = (SystemObject) sysIter.next();

                // ************** need to check status here
                if (so.getStatus().equals(SystemObject.STATUS_ACTIVE) && !so.isRemoved()) {
                    // for each system object, get system id
                    String sysId = so.getSystemCode();
                    String lid = so.getLID();
                    
                    // for each system, get the little omega
                    ObjectNode o = so.getObject();

                    if (o != null) {
                        // get a list of EPath objects
                        Collection fieldNames = getSystemFieldMeta(candidateId);

                        // create a field list to fill with SystemField values later
                        SystemFieldList fieldValues = new SystemFieldList();

                        if (fieldNames != null) {
                            // iterator for the list of EPath objects to retrieve for this candidate field
                            Iterator nameIter = fieldNames.iterator();

                            while (nameIter.hasNext()) {
                                // get the actual value from the omega object using EPath
                                EPath path = (EPath) nameIter.next();

                                String fieldName = path.getName();
                                Object value;

                                if (!fieldName.equals("LastModified")) {
                                    // use EPathAPI method to traverse the object graph and 
                                    // return values
                                    value = EPathAPI.getFieldValue(path, o);

                                    SystemField fieldValue = new SystemField(fieldName, value);

                                    // add the values to the collection that's returned
                                    fieldValues.add(fieldValue);
                                }
                            }
                             // end while
                        }

                        // always retrieve the value for the candidate field
                        Object value;
                        value = EPathAPI.getFieldValue(getEPath(candidateId), o);
                        fieldValues.add(new SystemField(candidateId, value));

                        // always return the last modified date
                        value = so.getUpdateDateTime();
                        fieldValues.add(new SystemField("LastModified", value));

                        sysFields.put(sysId, lid, fieldValues);
                    }
                     // end if entityobject node is not null
                }
                 // end if system is active
            }
             // while systemobjects iter.hasNext()

            return sysFields;
        } catch (Exception ex) {
            throw new SurvivorCalculationException(mLocalizer.t("SUR521: Could not " +
                                                "retrieve system fields: {0}", ex));
        }
    }

    /** sets the candidate field on the omega object using the given value
     * @return the Omega object with updated values
     * return null if error setting candidate field value
     * @param eo entity object
     * @param candidateField field name and value
     * @throws SurvivorCalculationException unexpected values
     * @throws SystemObjectException error accessing entity object
     */   
    public ObjectNode setCandidateFieldValue(ObjectNode eo, SystemField candidateField) 
        throws SurvivorCalculationException, SystemObjectException {
        try {
            EPathAPI.setFieldValue(getEPath(candidateField.getName()), eo,
                candidateField.getValue(), false);
        } catch (Exception ex) {
            throw new SurvivorCalculationException(mLocalizer.t("SUR522: Unable to " +
                                            "set candidate field value for " + 
                                            "field: {0}:{1}", 
                                            candidateField.getName(), ex));
        }

        return eo;
    }

    public void init(SurvivorHelperConfig config) 
        throws StrategyCreationException, HelperCreationException {
        super.init(config);
        
        // preparse all of the ePaths and store them in the cache
        try {
            Collection candidates = getCandidateFieldNames();
            Iterator canIter = candidates.iterator();
            while (canIter.hasNext()) {
                String canPath = (String) canIter.next();
                EPath e = EPathParser.parse(canPath); 
                mEPathCache.put(canPath, e);
            }
        } catch (EPathException eex) {
            throw new HelperCreationException(mLocalizer.t("SUR523: Could not " +
                                            "initialize DefaultSurvivorHelper: {0}", eex));
        }
    }    
    
    protected EPath getEPath(String s) 
        throws EPathException {
        EPath e = (EPath) mEPathCache.get(s);
        if (e == null) {
            e = EPathParser.parse(s);
            mEPathCache.put(s, e);
        }
        return e;
    }
}
