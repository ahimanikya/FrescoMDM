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
import com.stc.sbme.api.SbmeConfigFilesAccess;
import com.stc.sbme.api.SbmeConfigurationException;
import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.Logger;

/**
 * Class to provide the eView match engine with user configuration
 *
 * It allows the Match Engine access to the Configuration Service - and 
 * therefore the repository.
 *
 * @author  aegloff
 * @version $Revision: 1.1 $
 */
public class SbmeMatcherAdapterConfig
        implements com.sun.mdm.index.matching.MatchEngineConfiguration {

    private MatchEngineConfig mMatchEngineConfig;
    private CSConfigFileAccess mCSConfigFileAccess;
            
    
    /** Creates new SbmeMatcherAdapterConfig */
    public SbmeMatcherAdapterConfig() {
    }

    /**
     * Hard-coded sample until the registry integration is done
     * @return the configuration file name
     */
    public String getConfigurationFileName() {
        // TODO: match engine might not require this anymore 
        return "matchConfigFile.txt";
    }

    /**
     * Get access to the match engine configuration files from the Configuration
     * Service
     * @return the configuration file access
     */
    CSConfigFileAccess getConfigFileAccess() 
            throws InstantiationException, ConfigurationException {
        if (mCSConfigFileAccess == null) {
            mCSConfigFileAccess = new CSConfigFileAccess(getMatchEngineConfig());
        }
        return mCSConfigFileAccess;        
    }
    
    /**
     * Get the Configuration Service 'Match Engine configuration' which allows
     * access to the configuration files content, even if stored in the repository
     * @return access to the configuration details for the match engine
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
     * Inner class that implements the eView Match engine interface
     * giving access to the configuration files, e.g. stored in the repository
     */
    public static class CSConfigFileAccess implements SbmeConfigFilesAccess {

        private MatchEngineConfig mConfig;
        private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
        private transient final Localizer mLocalizer = Localizer.get();
    
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

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Retrieving configuration file: " + name);
            }
            
            java.io.ByteArrayInputStream stream = null;
                
            if (mConfig == null) {
                throw new SbmeConfigurationException(mLocalizer.t("MAT528: Failed to retrieve the " + 
                                                    "match engine configuration from the " + 
                                                    "configuration service. MatchEngineConfig " +  
                                                    "to access the configuration service is null."));
            }
                
            try {
                byte[] fileContents = mConfig.getFileContents(MatchEngineConfig.CFG_TYPE_MATCH, name);
                stream = new java.io.ByteArrayInputStream(fileContents);

                int fileLength = 0;
                if (fileContents != null) {
                    fileLength = fileContents.length;
                }
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Size of retrieved file: " + fileLength);
                }

            } catch (RuntimeException ex) {
                throw new SbmeConfigurationException(mLocalizer.t("MAT529: Failed to retrieve " + 
                                                            "the configuration file: {0}", ex));
            }

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Bytes available from returned configuration file stream: " + stream.available());
            }
            
            return stream;
        }
        
        public java.io.InputStream getConfigFileAsStream(String name, String domain) 
            throws SbmeConfigurationException {
            
            return null;
        }
        
    }
              
}
