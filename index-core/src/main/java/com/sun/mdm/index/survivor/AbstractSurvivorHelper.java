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
package com.sun.mdm.index.survivor;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import net.java.hulp.i18n.Logger;
import net.java.hulp.i18n.LocalizationSupport;

import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObjectException;

import com.sun.mdm.index.configurator.impl.SurvivorHelperConfig;



/** Base helper class that provides the following utility methods on omega:
 *   get a set of candidate fields that need survivor calculation
 *   get a set of system information fields for each candidate field
 *   set the value of a candidate field
 *
 *
 * @version $Revision: 1.1 $
 */
public abstract class AbstractSurvivorHelper
implements Cloneable, java.io.Serializable {
    
    /** cache to store the strategy object for each candidate field
     *  Key: candidate field id        Value: survivor strategy objects
     */
    protected Map mStrategyCache;
    
    /** cache to store system information field metadata for each candidate field
     *  Key: candidate field id        Value: collection of EPath objects
     */
    protected Map mFieldCache;
    
    
    /** The default strategy
     */
    private SurvivorStrategyInterface mDefaultStrategy;
    
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    
    /** default constructor */
    public AbstractSurvivorHelper() {
        mStrategyCache = new HashMap(10);
        mFieldCache = new HashMap(25);
        mDefaultStrategy = null;
    }
    
    /** initialize the helper using a configuration object
     * @param config configuration object
     * @throws StrategyCreationException exception when creating a survivor calculation strategy
     * @throws HelperCreationException error creating the helper class
     */
    public void init(SurvivorHelperConfig config)
    throws StrategyCreationException, HelperCreationException {
        
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("AbstractSurvivorCalculator: initializing helper");
        }
        
        
        // XSD spec the default must exist, so error if otherwise
        SurvivorHelperConfig.StrategyConfig ds = config.getDefaultStrategy();
        String className = ds.getStrategyClassName();
        try {
           
            
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("The default strategy class name is: " + className);
            }
            
            Class strategyClass = Class.forName(className);
            Object obj = strategyClass.newInstance();
            
            if (!SurvivorStrategyInterface.class.isInstance(obj)) {
                throw new StrategyCreationException(mLocalizer.t("SUR500: Survivor strategy class " +
                                                "{0} does not implement SurvivorStrategyInterface.", 
                                                strategyClass.getName()));
            }
            
            SurvivorStrategyInterface sti = (SurvivorStrategyInterface) obj;
            
            // call init on strategy with parameter collection
            sti.init(ds.getInitParameters());
            
            // store strategy instance in cache, using candidate field as key
            mDefaultStrategy = sti;
        } catch (ClassNotFoundException cnfex) {
            throw new StrategyCreationException(mLocalizer.t("SUR501: Default Strategy class not found: " +
                                                "{0}:{1}", className, cnfex));
        } catch (InstantiationException iex) {
            throw new StrategyCreationException(mLocalizer.t("SUR502: Unable to instantiate default strategy: " +
                                                "{0}:{1}", className, iex));
        } catch (IllegalAccessException iaex) {
            throw new StrategyCreationException(mLocalizer.t("SUR503: Unable to access default constructor for default strategy: " +
                                                "{0}:{1}", className, iaex));
        }
        
        mFieldCache = config.getFieldCache();
        
        Map table = config.getStrategyCache();
        Set s = table.keySet();
        Iterator iter = s.iterator();
        
        while (iter.hasNext()) {
            String candidateName = (String) iter.next();
            SurvivorHelperConfig.StrategyConfig c = (SurvivorHelperConfig.StrategyConfig) table.get(candidateName);
            String className2 = c.getStrategyClassName();
            try {
                Class strategyClass = Class.forName(className2);
                Object obj = strategyClass.newInstance();
                
                if (!SurvivorStrategyInterface.class.isInstance(obj)) {
                    throw new StrategyCreationException(mLocalizer.t("SUR504: Survivor strategy class: " +
                                                "{0} does not implement SurvivorStrategyInterface.", 
                                                strategyClass.getName()));
                }
                
                SurvivorStrategyInterface sti = (SurvivorStrategyInterface) obj;
                
                // call init on strategy with parameter collection
                sti.init(c.getInitParameters());
                
                // store strategy instance in cache, using candidate field as key
                mStrategyCache.put(candidateName, sti);
            } catch (ClassNotFoundException cnfex) {
                throw new StrategyCreationException(mLocalizer.t("SUR505: Strategy class not found: " +
                                                "{0}:{1}", className2, cnfex));
            } catch (InstantiationException iex) {
                throw new StrategyCreationException(mLocalizer.t("SUR506: Unable to instantiate strategy: " +
                                                "{0}:{1}", className2, iex));
            } catch (IllegalAccessException iaex) {
                throw new StrategyCreationException(mLocalizer.t("SUR507: Unable to access default constructor for strategy: " +
                                                "{0}:{1}", className2, iaex));
            }
        }
    }
    
    // ************************ end configuration service supports *****************
    
    
    
    /** sets the candidate field on the omega object using the given value
     * @return the Omega object with updated values
     * return null if error setting candidate field value
     * @param eo entity object
     * @param candidateField field name and value
     * @throws SurvivorCalculationException unexpected values
     * @throws SystemObjectException error accessing entity object
     */
    public abstract ObjectNode setCandidateFieldValue(ObjectNode eo,
    SystemField candidateField)
    throws SurvivorCalculationException, SystemObjectException;
    
    /** returns a mapping of system field values for the candidate field
     * @return Mapping of system fields using system id as Key.
     * Key: system id         Value: collection of SystemField value objects
     * return null if error getting system fields
     * @param candidateId candidate field name
     * @param rec omega object
     * @throws SurvivorCalculationException unexpected value
     * @throws SystemObjectException error accessing entity object
     */
    public abstract SystemFieldListMap getSystemFields(EnterpriseObject rec,
    String candidateId)
    throws SurvivorCalculationException, SystemObjectException;
    
    /** set the cache
     * @param m a map
     *
     */
    public void setCache(Map m) {
        mStrategyCache = m;
    }
    
    /** executes the strategy associated with the candidate field
     * @return return the value of the survivor field
     * @param candidateField candidate field name
     * @param fields mapping of system id to collection of system field values
     * @throws SurvivorCalculationException unexpect values
     */
    public SystemField executeStrategy(String candidateField, SystemFieldListMap fields)
    throws SurvivorCalculationException {
        SurvivorStrategyInterface surv = getStrategy(candidateField);
        SystemField o = null;
        
        if (surv != null) {
            o = surv.selectField(candidateField, fields);
        } else if (mDefaultStrategy != null) {
            o = mDefaultStrategy.selectField(candidateField, fields);
        } else {
            mLogger.warn(mLocalizer.x("SUR001: No strategy found for candidate " 
                                      + "field {0}: ", candidateField));
        }
        
        return o;
    }
    
    /** returns the strategy used for a particular candidate field.
     * The default strategy is returned if none is specified in the configuration.
     * @param candidateId candidate field
     * @return returns the strategy used for a particular candidate field.
     * The default strategy is returned if none is specified in the configuration.
     */
    protected SurvivorStrategyInterface getStrategy(String candidateId) {
        Object o = mStrategyCache.get(candidateId);
        
        return (SurvivorStrategyInterface) o;
    }
    
    /** returns the meta data of system fields for the specified candidiate field
     * @param candidateId candidate field name
     * @return returns the meta data of system fields for the specified candidiate field
     */
    protected Collection getSystemFieldMeta(String candidateId) {
        Object o = mFieldCache.get(candidateId);
        
        return (Collection) o;
    }
    
    /** return a list of candidate field names in the SBR
     * @return return a list of candidate field names in the SBR
     */
    public Collection getCandidateFieldNames() {
        Set s = mFieldCache.keySet();
        List ret = new ArrayList();
        Iterator iter = s.iterator();
        
        while (iter.hasNext()) {
            String temp = (String) iter.next();
            ret.add(temp);
        }
        
        return ret;
    }
    
    /** clone the object
     * @throws CloneNotSupportedException clone not supported
     * @return clone of the object
     */
    public Object clone() throws CloneNotSupportedException {
        Object o = super.clone();
        
        //        Object map = mStrategyCache.clone();
        //        ((AbstractSurvivorHelper)o).setCache((Hashtable)map);
        return o;
    }
    
}
