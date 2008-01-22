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
package com.sun.mdm.index.matching.adapter;

import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.configurator.impl.MatchEngineConfig;
import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.util.Localizer;
import com.stc.sbme.api.SbmeConfigFilesAccess;
import com.stc.sbme.api.SbmeConfigurationException;

/**
 * Class to provide the Master Index standardization engine with user configuration
 *
 * It allows the Standardization Engine access to the Configuration Service - and 
 * therefore the repository.
 *
 * @author  aegloff
 * @version $Revision: 1.1 $
 */
public class SbmeStandardizerAdapterConfig
        implements com.sun.mdm.index.matching.StandardizerEngineConfiguration {

    private MatchEngineConfig mMatchEngineConfig;
    private CSConfigFileAccess mCSConfigFileAccess;

    private transient static final Localizer mLocalizer = Localizer.get();
            
    /** Creates new SbmeMatcherAdapterConfig */
    public SbmeStandardizerAdapterConfig() {
    }
            
    /**
     * Get access to the standardization engine configuration files from the Configuration
     * Service
     * @return the configuration file access
     * @throws java.lang.InstantiationException 
     * @throws com.sun.mdm.index.configurator.ConfigurationException 
     */
    public CSConfigFileAccess getConfigFileAccess() 
            throws InstantiationException, ConfigurationException {
        if (mCSConfigFileAccess == null) {
            mCSConfigFileAccess = new CSConfigFileAccess(getMatchEngineConfig());
        }
        return mCSConfigFileAccess;        
    }
    
    /**
     * Get the Configuration Service 'Match Engine configuration' which allows
     * access to the configuration files content for standardization, 
     * even if stored in the repository
     * @return access to the configuration details for the standardization engine
     * @throws InstantiationException if the configuration could not be retrieved
     */
    private MatchEngineConfig getMatchEngineConfig() 
            throws InstantiationException {
        if (mMatchEngineConfig == null) {
            ConfigurationService cfgFactory = ConfigurationService.getInstance();
            mMatchEngineConfig = 
                (MatchEngineConfig) cfgFactory.getConfiguration(MatchEngineConfig.MODULE_NAME);
        }

        return mMatchEngineConfig;
    }
    

    /**
     * Inner class that implements the Master Index standardization engine interface
     * giving access to the configuration files, e.g. stored in the repository
     */
    public static class CSConfigFileAccess implements SbmeConfigFilesAccess {

        private MatchEngineConfig mConfig;
        
        /**
         * Constructor to create instance
         * @param cfg the MatchEngineConfig ConfigurationService
         * instance to use to retrieve configuration files.
         */
        public CSConfigFileAccess(MatchEngineConfig cfg) {
            mConfig = cfg;
        }
        
        /**
         * Retrieves the configuration file contents as a stream from the 
         * configuration service. This might be retrieved from the repository.
         * @param name the name of the configuration file to retrieve
         * @return the configuration file contents as a stream
         * @throws SbmeConfigurationException if retrieving the configuration file failed
         */
        public java.io.InputStream getConfigFileAsStream(String name) 
            throws SbmeConfigurationException {
            
            java.io.ByteArrayInputStream stream = null;
                
            if (mConfig == null) {
                throw new SbmeConfigurationException(mLocalizer.t("MAT540: Failed to " + 
                                                "retrieve the standardization engine " + 
                                                "configuration from the configuration " + 
                                                "service. MatchEngineConfig to access " + 
                                                "the configuration service is null."));
            }
                
            try {
                byte[] fileContents = mConfig.getFileContents(MatchEngineConfig.CFG_TYPE_STANDARDIZATION, name);
                if (fileContents != null) {
                    stream = new java.io.ByteArrayInputStream(fileContents);
                }
            } catch (RuntimeException ex) {
                throw new SbmeConfigurationException(mLocalizer.t("MAT541: Could not " +
                                                "retrieve the configuration file {0}" + 
                                                "from the configuration " + 
                                                "service. MatchEngineConfig to access " + 
                                                "the configuration service is null: {1}",
                                                name, ex));
            }

            return stream;
        }    
         /**
         * Retrieves the configuration file contents as a stream from the 
         * configuration service. This might be retrieved from the repository.
         * @param name the name of the configuration file to retrieve
         * @param domain the domain of the configuration file to retrieve
         * @throws SbmeConfigurationException if retrieving the configuration file failed
         */
        public java.io.InputStream getConfigFileAsStream(String name, String domain) 
            throws SbmeConfigurationException {
            
            java.io.ByteArrayInputStream stream = null;
                
            if (mConfig == null) {
                throw new SbmeConfigurationException(mLocalizer.t("MAT542: Failed to " +
                                                "retrieve the standardization engine " + 
                                                "configuration from the configuration service. " + 
                                                "MatchEngineConfig to access the " + 
                                                "configuration service is null."));
            }
                
            try {
                byte[] fileContents = mConfig.getFileContents(MatchEngineConfig.CFG_TYPE_STANDARDIZATION, name, domain);
                if (fileContents != null) {
                    stream = new java.io.ByteArrayInputStream(fileContents);
                }
            } catch (RuntimeException ex) {
                throw new SbmeConfigurationException(mLocalizer.t("MAT543: Failed to " +
                                                "retrieve the configuration file {0}: {1}",
                                                name, ex));
            }

            return stream;
        }
    }    
}
