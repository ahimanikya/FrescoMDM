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
package com.sun.mdm.index.matching;

import com.sun.mdm.index.configurator.impl.standardization.SystemObjectStandardization;
import com.sun.mdm.index.configurator.impl.standardization.StandardizationConfiguration;
import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.objects.exception.ObjectException;

import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

/**
 * Loads and forwards requests to the
 * adapter interface implementation configured.
 * 
 * @author aegloff
 * $Revision: 1.1 $
 */

public class StandardizerImpl implements Standardizer{
    
    private StandardizerAPI standardizerAPIImpl;
    private StandardizerEngineConfiguration standardizerEngineConfig;
    private StandardizationConfiguration standardizationConfig;
    private final Logger mLogger = LogUtil.getLogger(this);
    
    
    /**
     * No argument constructor.
     */
    public StandardizerImpl() throws Exception {
    	initialize();
    }
    
    /**
     * Load and initialize the configured standardizer API implementation
     * @throws StandardizationException Initializing the configured API implementation failed
     * @throws InstantiationException instantiating the configured API implementation
     * class failed
     * @throws ClassNotFoundException The class for the configured API implementation
     * class could not be found 
     * @throws IllegalAccessException The current security settings do not allow
     * loading and instantiating the class configured for API implementation
     */    
    public void initialize() 
            throws StandardizationException, InstantiationException, ClassNotFoundException, IllegalAccessException {
        mLogger.debug("initialize()");
        try {
            // use StandardizerAPIHelper as factory 
            standardizerAPIImpl = new StandardizerAPIHelper().getStandardizerAPIImpl();
            if (standardizerAPIImpl == null) {
                throw new StandardizationException("No StandardizerAPI implementation configured.");
            }
            standardizerEngineConfig = new StandardizerAPIHelper().getStandardizerEngineConfigImpl();
            if (standardizerEngineConfig == null) {
                mLogger.debug("No StandardizerEngineConfig implementation configured.");
            }            
            
            standardizerAPIImpl.initialize(standardizerEngineConfig);
        } catch (StandardizationException ex) {
            mLogger.error("Initializing the standardization engine failed.", ex);
            throw ex;            
        } catch (InstantiationException ex) {
            mLogger.error("Instantiating the user API implementation class failed.", ex);
            throw ex;            
        } catch (ClassNotFoundException ex) {
            mLogger.error("Loading the user API implementation class failed.", ex);
            throw ex;            
        } catch (IllegalAccessException ex) {
            mLogger.error("Accessing the user API implementation class failed.", ex);
            throw ex;            
        } catch (RuntimeException ex) {
            mLogger.error("Initialize failed.", ex);
            throw ex;            
        } catch (LinkageError ex) {
            mLogger.error("Failed to load a native library.", ex);
            throw ex;            
        }
    }
    
    public void shutdown() throws Exception {   	 
    	if (standardizerAPIImpl != null) {
    		standardizerAPIImpl.shutdown();
    		standardizerAPIImpl = null;
        }    
    }    
    
    /**
     * Standardize a SystemObject
     * @param objToStandardize the SystemObject to standardize
     * @throws StandardizationException the standardization process failed
     * @throws ObjectException accessing/manipulating the value objects failed
     * @throws InstantiationException instantiating value objects failed
     * @return the standardized SystemObject
     */    
    public com.sun.mdm.index.objects.SystemObject standardize(com.sun.mdm.index.objects.SystemObject objToStandardize) 
            throws StandardizationException, ObjectException, InstantiationException {
        if (objToStandardize == null) {
            mLogger.debug("Standardize was called on a null object, returning it unchanged.");
        } else if (objToStandardize.getObject() == null) {
            mLogger.debug("Standardize was called on a SystemObject containing a null object, returning it unchanged.");
        } else {
            String objType = objToStandardize.getObject().pGetType();
            if (objType != null) {
                SystemObjectStandardization metaData = getStandardizationMetaData(objType);
                if (metaData != null) {
                    standardizerAPIImpl.standardize(objToStandardize, metaData);
                } else {
                    throw new StandardizationException("Could not retrieve the standardization configuration for "
                        + "object type: " + objType + ". The configuration service returned null.");
                }
            } else {
                throw new ObjectException("The object to be standardized does not have a type attribute set. "
                    + "The standardizer is unable to retrieve standardardization configuration without it.");
            }
        }
        
        return objToStandardize;
    }

    /**
     * Retrieve the Configuration object that allows access to the standardization
     * metadata for SystemObjects
     * @throws InstantiationException instantiating the classes needed to access the 
     * configuration failed
     * @return standardization configuration object
     */    
    StandardizationConfiguration getStandardizationConfig() throws java.lang.InstantiationException {
        if (standardizationConfig == null) {
            ConfigurationService cfgFactory = ConfigurationService.getInstance();
            standardizationConfig = (StandardizationConfiguration) cfgFactory.getConfiguration(
                StandardizationConfiguration.STANDARDIZATION);
        }
        return standardizationConfig;
    }
    

    /**
     * Retrieve the metadata configured for a specific SystemObject
     * @param sysObjName the name of the SystemObject to retrieve the configuration for
     * @throws InstantiationException instantiating the classes needed to access the 
     * configuration failed
     */
    SystemObjectStandardization getStandardizationMetaData(String sysObjName) throws java.lang.InstantiationException {
        SystemObjectStandardization metaData = getStandardizationConfig().getSystemObjectStandardization(sysObjName);
        return metaData;
    }
    
}
