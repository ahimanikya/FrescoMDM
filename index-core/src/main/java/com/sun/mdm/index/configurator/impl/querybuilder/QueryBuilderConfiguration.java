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
package com.sun.mdm.index.configurator.impl.querybuilder;

import java.util.HashMap;
import java.util.logging.Level;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.querybuilder.QueryBuilder;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * Query builder configuration.
 * @author dcidon
 */
public class QueryBuilderConfiguration implements ConfigurationInfo {

    /** Module type name */    
    public static final String QUERY_BUILDER = "QueryBuilder";

    //Map query builder name to query builder info
    private final HashMap queryBuilderInfoMap = new HashMap();
    private transient Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient Localizer mLocalizer = Localizer.get();
    

    /** Creates a new QueryBuilderConfiguration instance. */
    public QueryBuilderConfiguration() {
    }


    /** Get module type.
     *
     * @return module type
     */
    public String getModuleType() {
        return QUERY_BUILDER;
    }


    /** Given search id, get an instance of its associated query builder.
     *
     * @param searchId Search ID.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     * @return query builder
     */
    public QueryBuilder getQueryBuilder(String searchId)
        throws ConfigurationException {
        try {
            QueryBuilderInfoStruct qbInfo = (QueryBuilderInfoStruct) queryBuilderInfoMap.get(searchId);
            if (qbInfo == null) {
                throw new ConfigurationException(mLocalizer.t("CFG537: Invalid search ID: {0}", searchId));
            } else {
                if (qbInfo.queryBuilderInstance == null) {
                    qbInfo.queryBuilderInstance = (QueryBuilder) qbInfo.queryBuilderClass.newInstance();
                    if (qbInfo.queryBuilderConfig != null) {
                        qbInfo.queryBuilderInstance.init(qbInfo.queryBuilderConfig);
                    }
                    qbInfo.queryBuilderInstance.setStandardizeRequired(qbInfo.standardize);
                    qbInfo.queryBuilderInstance.setPhoneticizeRequired(qbInfo.phoneticize);
                }
            }
            
            return qbInfo.queryBuilderInstance;
        } catch (Exception e) {
            throw new ConfigurationException(mLocalizer.t("CFG538: Could not retrieve " + 
                                    "the query builder for search ID: {0}", searchId));
        }
    }


    /** Return all of the query builder search IDs.
     *
     * @return array of search IDs.
     */
    public String[] getQueryBuilderIds() {
        Object obj[] = queryBuilderInfoMap.keySet().toArray();
        String retArray[] = new String[obj.length];
        for (int i = 0; i < obj.length; i++) {
            retArray[i] = (String) obj[i];
        }
        return retArray;
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
                
        //Sub-elements are query-builder elements
        try {
            NodeList queryBuilderList = node.getChildNodes();
            int builderCount = queryBuilderList.getLength();
            for (int i = 0; i < builderCount; i++) {
                Node queryBuilder = queryBuilderList.item(i);
                if (queryBuilder.getNodeType() == Node.ELEMENT_NODE) {
                    QueryBuilderInfoStruct qbInfo = new QueryBuilderInfoStruct();
                    NamedNodeMap attributes = queryBuilder.getAttributes();
                    String builderName = attributes.getNamedItem("name").getNodeValue();
                    String builderClassName = attributes.getNamedItem("class").getNodeValue();
                    Node phoneticize = attributes.getNamedItem("phoneticize");
                    Node standardize = attributes.getNamedItem("standardize");
                    qbInfo.standardize = getBooleanValue(standardize);
                    qbInfo.phoneticize = getBooleanValue(phoneticize);
                    qbInfo.queryBuilderClass = Class.forName(builderClassName);
                    Node parserClassNode = attributes.getNamedItem("parser-class");
                    if (parserClassNode != null) {
                        Class parserClass = Class.forName(parserClassNode.getNodeValue());
                        qbInfo.queryBuilderConfig = (ConfigurationInfo) parserClass.newInstance();
                        NodeList configNodeList = queryBuilder.getChildNodes();
                        int configCount = configNodeList.getLength();
                        for (int j = 0; j < configCount; j++) {
                            Node configNode = configNodeList.item(j);
                            if (configNode.getNodeType() == Node.ELEMENT_NODE) {
                                qbInfo.queryBuilderConfig.parse(configNode);
                                break;
                            }
                        }
                    }
                    queryBuilderInfoMap.put(builderName, qbInfo);
                    mLogger.info(mLocalizer.x("CFG027: Query Builder Configuration mappings are: {0}", LogUtil.mapToString(queryBuilderInfoMap)));
                }
            }
        } catch (Exception e) {
            throw new ConfigurationException(mLocalizer.t("CFG539: Could not parse " + 
                                    "the QueryBuilderConfiguration XML node: {0}", e));
        }
    }


    /** Retrieves the boolean value of an XML node.
     *
     * @param node XML node.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     */
    private boolean getBooleanValue(Node node)
            throws ConfigurationException {
                
        if (node == null) {
            return false;
        } else {
            String val = node.getNodeValue();
            if (val.equalsIgnoreCase("true")) {
                return true;
            } else if (val.equalsIgnoreCase("false")) {
                return false;
            } else {
                throw new ConfigurationException(mLocalizer.t("CFG540: QueryBuilderConfiguration " + 
                                            "encountered an invalid boolean value: {0}", val));
            }
        }
    }


    // Inner class for storing Query Builder information.
    
    private class QueryBuilderInfoStruct {
        boolean phoneticize;
        Class queryBuilderClass;
        ConfigurationInfo queryBuilderConfig;
        QueryBuilder queryBuilderInstance;
        boolean standardize;
    
        
        /** String representation of the QueryBuilderInfoStruct inner class.
         *
         * @return String representation of the QueryBuilderInfoStruct inner class.
         */
        public String toString() {
             
            String queryConfig = "";             
            if (queryBuilderConfig != null) {
                queryConfig = "\n ConfigurationInfo:\n" + queryBuilderConfig.toString();
            }
             
            String queryInstance = "";
            if (queryBuilderInstance != null) {
                queryInstance = "\n queryBuilderInstance:\n" + queryBuilderInstance.toString();
            }
             
            String str = "QueryBuilderInfoStruct:" 
                          + "\n phoneticize:" 
                          + phoneticize 
                          + "\n queryBuilderClass:" 
                          + queryBuilderClass 
                          + queryConfig 
                          + queryInstance 
                          + " standardize: " 
                          + standardize;
         
            return str;
        }
    }
   
}
