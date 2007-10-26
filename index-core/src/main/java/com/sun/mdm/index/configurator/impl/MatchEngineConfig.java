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
package com.sun.mdm.index.configurator.impl;

import java.util.HashMap;

import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;

import org.w3c.dom.Node;
import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.configurator.ConfigurationException;



/**
 * Handles the the SBME Configuration files
 *
 * @version $Revision: 1.1 $
 */
public class MatchEngineConfig implements ConfigurationInfo {

    /** Module Name to use with the Configuration Service to load the Match Engine configuration */
    public static final String MODULE_NAME = "MatchEngine";
    
    /** Configuration type to request match configuration files */
    public static final String CFG_TYPE_MATCH = "MATCH";
    
    /** Configuration type to request standardization configuration files */
    public static final String CFG_TYPE_STANDARDIZATION = "STAND";

    /**  HashMap from the configType.file name to the contents as a byte array */
    private HashMap nameToContents = new HashMap();
    
    

    /**
     * Creates new instance
     */
    public MatchEngineConfig() {
    }

    /**
     * Getter for the configuration file contents.
     *
     * @param configType The type of configuration files. Use the constants
     * CFG_TYPE_MATCH or CFG_TYPE_STANDARDIZATION.
     * @param name The file name to get the file contents for.
     * @return the file contents, null if no file exists.
     */
    public byte[] getFileContents(String configType, String name) {
        byte[] contents = null;
        
        ClassLoader cl = this.getClass().getClassLoader();
        if (CFG_TYPE_MATCH.equals(configType)) {
            name = "match/" + name;
        } else if (CFG_TYPE_STANDARDIZATION.equals(configType)) {
            name = "stand/" + name;
        }
            
        InputStream is = cl.getResourceAsStream(name);
        byte[] buffer = new byte[255];
        if (is != null) {
            BufferedInputStream bis = null;
            ByteArrayOutputStream bos = null;
            try {
                bis = new BufferedInputStream(is);
                bos = new ByteArrayOutputStream();
                
                int read = -1;
                while ((read = bis.read(buffer)) > 0) {
                    bos.write(buffer, 0, read);
                }
                
                contents = bos.toByteArray();
            } catch (IOException iex) {
                // ignore
            } finally {
              try {
                  bis.close();
              } catch (Exception ex) { 
                // ignore 
              }
              
              try {
                  is.close();
              } catch (Exception exx) {
                // ignore
              }
            }
            
        }
        return contents;
    }
    
    
    /**
     * Getter for the configuration file contents.
     *
     * @param configType the type of configuration files. Use the constants
     * CFG_TYPE_MATCH or CFG_TYPE_STANDARDIZATION.
     * @param name The file name to get the file contents for.
     * @param domain The domain name to get the file contents for.
     * @return the file contents, null if no file exists.
     */
    public byte[] getFileContents(String configType, String name, String domain) {
        byte[] contents = null;
        
        ClassLoader cl = this.getClass().getClassLoader();
        if (CFG_TYPE_MATCH.equals(configType)) {
            name = "match/" + name;
        } else if (CFG_TYPE_STANDARDIZATION.equals(configType)) {
            if(cl.getResourceAsStream("stand/" + domain + "/" + name) != null){
                name = "stand/" + domain + "/" + name;                
            }else{
                name = "stand/" + "biz" + "/" + name;  
            }
        }
            
        InputStream is = cl.getResourceAsStream(name);
        byte[] buffer = new byte[255];
        if (is != null) {
            BufferedInputStream bis = null;
            ByteArrayOutputStream bos = null;
            try {
                bis = new BufferedInputStream(is);
                bos = new ByteArrayOutputStream();
                
                int read = -1;
                while ((read = bis.read(buffer)) > 0) {
                    bos.write(buffer, 0, read);
                }
                
                contents = bos.toByteArray();
            } catch (IOException iex) {
                // ignore
            } finally {
              try {
                  bis.close();
              } catch (Exception ex) { 
                // ignore 
              }
              
              try {
                  is.close();
              } catch (Exception exx) {
                // ignore
              }
            }
            
        }
        return contents;
    }

    /**
     * Adds a file to this configuration.
     *
     * @param configType The type of configuration files. Use the constants
     * CFG_TYPE_MATCH or CFG_TYPE_STANDARDIZATION.
     * @param name The file name.
     * @param contents The file contents.
     */    
    public void addFile(String configType, String name, byte[] contents) {
        nameToContents.put(configType + "." + name, contents);
    }

    /**
     * Getter for ModuleType attribute object.
     *
     * @return the type of this configuration module
     */
    public String getModuleType() {
        return MODULE_NAME;
    }    
    
    /** Finish.
     *
     * @return result code.
     */
    public int finish() {
        return 0;
    }


    /** Initialize.
     *
     * @return result code.
     */
    public int init() {
        return 0;
    }

    /** Parse an XML node.
     *
     * @param node XML node starting the configuration node.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     */
    public void parse(Node node)
        throws ConfigurationException {
    }

}
