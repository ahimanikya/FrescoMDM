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
package com.sun.mdm.index.configurator;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.HashMap;
import java.util.logging.Level;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.sun.mdm.index.configurator.impl.MatchEngineConfig;
import com.sun.mdm.index.util.Localizer;

import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/** ConfigurationMBean implementation
 *
 * @version $Revision: 1.1 $
 */
public class ConfigurationService implements ConfigurationMBean {
    
    
    /** XML node attribute for the module name */
    public static final String ATR_MODULE_NAME = "module-name";
    
    /** XML node attribute for parser class */
    public static final String ATR_PARSER_CLASS = "parser-class"; 
    
    /** JVM property name for BBE home directory */
    static final String HOME_SYS_PROP = "bbe.home";
    
    // File names
    static final String MASTER_FILE_NAME = "master.xml";
    static final String UPDATE_FILE_NAME = "update.xml";
    static final String QUERY_FILE_NAME = "query.xml";
    static final String MEFA_FILE_NAME = "mefa.xml";
    static final String VALIDATION_FILE_NAME = "validation.xml";
    static final String SECURITY_FILE_NAME = "security.xml";

    static final String MATCH_DIR = "match";
    static final String STAND_DIR = "stand";
    
    /** singleton instance */
    private static ConfigurationService instance = null;
    
    /** map to hold configuration objects to module names */
    private HashMap configs;
    
    /**
     * Observable to notify of configuration changes that happen after the initial load
     */
    private static ConfigurationServiceObservable configServiceObservable 
            = new ConfigurationServiceObservable();
    
    private static transient Logger mLogger = Logger.getLogger("com.sun.mdm.index.configurator.ConfigurationService");
    private static transient Localizer mLocalizer = Localizer.get();
    
    /** Creates new ConfigurationService instance */
    public ConfigurationService() {
        // A synchronized hashmap is not used because access to instance
        // is already synchronized.
        configs = new HashMap();
    }
    
    /** Returns the configuration instance given the name.
     *
     * @param name Name of the configuration section.
     * @return configuration information.
     */
    public ConfigurationInfo getConfiguration(String name) {
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("getConfiguration() looking up name :" + name);
        }
        return (ConfigurationInfo) configs.get(name);
    }
        
    /** Load the configuration file.
     *
     * @param fileStream input stream to the file.
     * @throws IOException if there is an error accessing the file.
     * @throws SAXException if there is an error parsing the file.
     * @throws ParserConfigurationException if there is an invalid parser 
     * configuration.
     * @throws DOMException if there is an error accessing the DOM tree.
     * @throws ClassNotFoundException if the parser class not found.
     * @throws InstantiationException if the parser can not be instantiated.
     * @throws IllegalAccessException if it is unable to access parser default
     * constructor.
     * @throws ConfigurationException if it encounters unexpected values.
     */
    public void load(InputStream fileStream)
            throws IOException, SAXException, ParserConfigurationException,
                   DOMException, ClassNotFoundException, InstantiationException,
                   IllegalAccessException, ConfigurationException {
                    
        Document doc;
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Creating builder factory");
        }
        
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
            // ignore fatal errors (an exception is guaranteed)
            public void fatalError(SAXParseException exception)
            throws SAXException {
            }
            
            // treat validation errors as fatal
            public void error(SAXParseException e)
            throws SAXParseException {
                throw e;
            }
            
            // log warnings
            public void warning(SAXParseException err)
            throws SAXParseException {
                // use logging facility to capture warning
                mLogger.warn(mLocalizer.x("CFG010: Parsing error encountered at line {0}:  uri {1}: {2}", 
                                           err.getLineNumber(), err.getSystemId(), err));
            }
        });
        
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Builder created: " + builder.toString());
        }
        
        org.xml.sax.InputSource input = new org.xml.sax.InputSource(fileStream);
        doc = builder.parse(input);
        parse(doc);
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Document parsed");
        }
        
    }
    
    /** Returns an inputstream to the configuration file.
     *
     * @throws IOException error constructing the URL.
     * @return an inputstream to the configuration file.
     */
    private InputStream getConfigFileStream(String fileName)
            throws IOException {
                
        String bbeHome = System.getProperty(HOME_SYS_PROP);
        
        // first look for the file in bbe.home, then try using the classloader
        if ((bbeHome != null) && !bbeHome.equals("")) {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("bbe.home IS defined, locating file in bbe.home");
            }
            String filePath = bbeHome + java.io.File.separator + "config"
            + java.io.File.separator + fileName;
            
            java.io.File f = new java.io.File(filePath);
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("bbe.home IS defined, locating file in bbe.home: " + f.getAbsolutePath());
            }
            return new FileInputStream(f);
        } else {
            // bbe.home not defined, so use the class loader
            // throw exception if class loader cannot find the requested file
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("bbe.home is not defined, using the class loader to locate file");
            }
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                mLogger.info(mLocalizer.x("CFG011: Unable to find the configuration file in classpath : {0}" , fileName));
                throw new IOException("Unable to find configuration file in classpath : " 
                    + fileName);
            }
            return is;
        }
        
    }
    
    /** Returns a reference to the singleton instance.  This is syncronized to
     * prevent multiple initial loading by different threads.
     *
     * @throws InstantiationException throws error if unable to create instance
     * @return reference to singleton instance
     */
    public static synchronized ConfigurationService getInstance()
            throws InstantiationException {
                
    	InputStream  is = null;
        if (instance == null) {
            instance = new ConfigurationService();
            
            try {
                mLogger.info(mLocalizer.x("CFG001: Loading master configuration file: {0}" , MASTER_FILE_NAME));
                is = instance.getConfigFileStream(MASTER_FILE_NAME);
                instance.load(is);
                is.close();
                mLogger.info(mLocalizer.x("CFG002: Loading: update configuration file: {0}" , UPDATE_FILE_NAME));
                is = instance.getConfigFileStream(UPDATE_FILE_NAME);
                instance.load(is);
                is.close();
                mLogger.info(mLocalizer.x("CFG003: Loading: query configuration file: {0}" , QUERY_FILE_NAME));
                is = instance.getConfigFileStream(QUERY_FILE_NAME);
                instance.load(is);
                is.close();
                mLogger.info(mLocalizer.x("CFG004: Loading: match configuration file: {0}" , MEFA_FILE_NAME));
                is = instance.getConfigFileStream(MEFA_FILE_NAME);
                instance.load(is);
                is.close();
                mLogger.info(mLocalizer.x("CFG005: Loading: validation configuration file: {0}" , VALIDATION_FILE_NAME));
                is = instance.getConfigFileStream(VALIDATION_FILE_NAME);
                instance.load(is);
                is.close();
                mLogger.info(mLocalizer.x("CFG006: Loading: security configuration file: {0}" , SECURITY_FILE_NAME));
                is = instance.getConfigFileStream(SECURITY_FILE_NAME);
                instance.load(is);
                is.close();
                is = null;
                
                mLogger.info(mLocalizer.x("CFG007: Loading match engine configuration files."));
                // instead of loading all the text files, simply delegate to the get 
                // method to load from the classpath when requested
                MatchEngineConfig matchConfig = new MatchEngineConfig();
                instance.configs.put(MatchEngineConfig.MODULE_NAME, matchConfig);
                mLogger.info(mLocalizer.x("CFG008: All configuration files loaded."));
            } catch (Exception e) {
                mLogger.severe(mLocalizer.x("CFG009: ConfigurationService could not load one or more files." + e.getMessage()));
                instance = null;
                throw new InstantiationException(e.getMessage());
            } finally {
            	try {
            	  if ( is != null){
            		is.close();
            	  }
            	} catch (Exception io){
            		throw new InstantiationException(io.getMessage());
            		
            	}
            }
        }
        
        return instance;
    }
    
    /**
     * Remove the Observer from being notified of configuration changes from what
     * was originally loaded.
     *
     * @param anObserver the observer to remove.
     */
    public static void deleteConfigChangeObserver(java.util.Observer anObserver) {
        configServiceObservable.deleteObserver(anObserver);
    }
        
    /** Parses the DOM representation of XML configuration.
     * Loads and instantiates the parser class to parse each sub-module.
     *
     * @param doc DOM document representing the configuration file.
     * @throws ConfigurationException unexpected value
     * @throws DOMException DOM access exception
     * @throws ClassNotFoundException unable to load the parser class
     * @throws InstantiationException unable to create the module specific parser object
     * @throws IllegalAccessException unable to access default constructor for module specific parser
     */
    private void parse(Document doc)
            throws ConfigurationException, DOMException, ClassNotFoundException,
                   InstantiationException, IllegalAccessException {

        Element elem = doc.getDocumentElement();
        
        NodeList nl = elem.getChildNodes();
        
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                // if it's element node, by XSD def, it must be of ModuleConfig type
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Parsing node: " + n.getNodeName());
                }
                
                Element e = (Element) n;
                String moduleName = e.getAttribute(ATR_MODULE_NAME);
                String parserClassName = e.getAttribute(ATR_PARSER_CLASS);
                
                Class parserClass = Class.forName(parserClassName);
                Object obj = parserClass.newInstance();
                
                if (ConfigurationInfo.class.isInstance(obj)) {
                    ConfigurationInfo config = (ConfigurationInfo) obj;
                    config.parse(n);
                    
                    configs.put(moduleName, config);
                } else {
                    ;
                    // should ignore for a module not found? or throw exception?
                    //throw new Exception("blah");
                }
            }
        }
    }

    
    /** Ensures the singleton instance will be reloaded with the latest configuration
     * with the next call to getInstance.
     *
     * CAUTION! This should currently ONLY be used for unit testing, because this
     * does NOT ensure that clients using the configuration service instances
     * re-load cached configuration information.
     *
     * To ensure that happens, the configuration service clients should register
     * an Observer and re-load the cached values if appropriate.
     *
     * A call to this method triggers a ConfigChangeObserver update notification.
     */
    public static void resetInstance() {
        synchronized (ConfigurationService.class) {
            instance = null;
        }
        configServiceObservable.setChanged();
        configServiceObservable.notifyObservers();
    }
    
    /**
     * Add an Observer to be notified it the configuration changes from what
     * was originally loaded.
     *
     * @param anObserver the observer to notify.
     */
    public static void addConfigChangeObserver(java.util.Observer anObserver) {
        configServiceObservable.addObserver(anObserver);
    }
      
    /**
     * Inner class to notify of configuration changes after the initial load.
     *
     * @see java.util.Observable
     */
    protected static class ConfigurationServiceObservable extends java.util.Observable {
        /**
         * @see java.util.Observable
         */
        protected void setChanged() {
            super.setChanged();
        }
    }

}
