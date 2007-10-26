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

import com.sun.mdm.index.survivor.StrategyCreationException;
import com.sun.mdm.index.survivor.StrategyParameter;
import com.sun.mdm.index.survivor.SurvivorCalculationException;
import com.sun.mdm.index.survivor.SurvivorStrategyInterface;
import com.sun.mdm.index.survivor.SystemField;
import com.sun.mdm.index.survivor.SystemFieldList;
import com.sun.mdm.index.survivor.SystemFieldListMap;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;


/** Default survivor strategy class.  Selects the field from the system specified
 * in the initialization parameter
 
 * @version $Revision: 1.1 $
 */
public class DefaultSurvivorStrategy implements SurvivorStrategyInterface,
Cloneable, java.io.Serializable {
    private String mPreferredSystem;
    
    /** Creates new MySurvivalStrategy */
    public DefaultSurvivorStrategy() {
    }
    
    /** Selects the field from the system specified in the initialization parameter
     * @see com.sun.mdm.index.survivor.SurvivorStrategyInterface#selectField
     */
    public SystemField selectField(String candidateId, SystemFieldListMap fields)
    throws SurvivorCalculationException {
        Collection list = fields.get(mPreferredSystem);
        if (list != null && list.size() > 0) {
            Iterator lIter = list.iterator();
            SystemFieldList fl = (SystemFieldList) lIter.next();
            
            /*********** need rethink, QWS will not always be present ***/
            if (fl == null) {
                return null;
                
                //throw new SurvivorCalculationException("System field values not found : " + mPreferredSystem);
            }
            
            Iterator iter = fl.iterator();
            
            while (iter.hasNext()) {
                SystemField f = (SystemField) iter.next();
                
                if (f.getName().equals(candidateId)) {
                    return f;
                }
            }
        }
        return null;
    }
    
    /**
     * {@inheritDoc}
     * @return clone of the object
     * @throws CloneNotSupportedException cloning is not supported
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    /** @see com.sun.mdm.index.survivor.SurvivorStrategyInterface#selectField
     */
    public void init(java.util.Collection parameters)
    throws StrategyCreationException {
        if (parameters == null) {
            throw new StrategyCreationException(
            "Missing initialization parameters");
        }
        
        Iterator iter = parameters.iterator();
        
        while (iter.hasNext()) {
            StrategyParameter p = (StrategyParameter) iter.next();
            
            if (p.getName().equals("preferredSystem")) {
                mPreferredSystem = (String) p.getValueObject();
            }
        }
        
        if ((mPreferredSystem == null) || mPreferredSystem.equals("")) {
            throw new StrategyCreationException(
            "Incorrect or missing parameter value for preferredSystem");
        }
    }
}
