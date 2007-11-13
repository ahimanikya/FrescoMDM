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
package com.sun.mdm.index.configurator.impl.master; 

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;


/**
 * Master Controller configuration
 * @author dcidon
 */
public class MasterControllerConfiguration implements ConfigurationInfo {

    /** Module type name
     */    
    public static final String MASTER_CONTROLLER = "MasterController";
    private boolean mPessimisticEnabled;
    private boolean mMergedRecordUpdate;
    private EOSearchOptions mSearchOptions;
    private transient Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient Localizer mLocalizer = Localizer.get();
    
    private String mUserLogicClass = null;
    private String mUserLogicClassGui = null;

    /** Creates new MasterControllerConfiguration instance*/
    public MasterControllerConfiguration() {
    }

    /** Get allowed merged record update flag.
     *
     * @return allow merge record update flag.
     */
    public boolean isMergedRecordUpdateEnabled() {
        return mMergedRecordUpdate;
    }

    /** Get module type.
     *
     * @return module type
     */
    public String getModuleType() {
        return MASTER_CONTROLLER;
    }

    /** Get logic class.
     *
     * @return logic class
     */
    public String getUserLogicClass() {
        return mUserLogicClass;
    }

    /** Get logic class GUI.
     *
     * @return logic class GUI.
     */
    public String getUserLogicClassGui() {
        return mUserLogicClassGui;
    }

    /** Get search options.
     *
     * @return search options.
     */
    public EOSearchOptions getSearchOptions() {
        return mSearchOptions;
    }


    /** Returns true if pessimistic parameter set to true in configuration.
     *
     * @return true if pessimistic parameter set to true, false otherwise.
     */
    public boolean isPessimisticEnabled() {
        return mPessimisticEnabled;
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
        NodeList optionList = node.getChildNodes();
        int builderCount = optionList.getLength();
        for (int i = 0; i < builderCount; i++) {
            Node option = optionList.item(i);
            if (option.getNodeType() == Node.ELEMENT_NODE) {
                String optionName = option.getNodeName();
                if (optionName.equals("update-mode")) {
                    parseUpdateMode(option);
                } else if (optionName.equals("merged-record-update")) {
                    parseMergedRecordUpdate(option);
                } else if (optionName.equals("logic-class")) {
                    parseLogicClass(option);
                } else if (optionName.equals("logic-class-gui")) {
                    parseLogicClassGui(option);
                } else if (optionName.equals("execute-match")) {
                    parseExecuteMatch(option);
                } else if(optionName.equals("transaction")){
                	//do nothing
                } else {
                    throw new ConfigurationException(mLocalizer.t("CFG527: Unrecognized tag for " + 
                                                        "MasterControllerConfiguration: {0}", optionName));
                }
            }
        }
        mLogger.info(mLocalizer.x("CFG034: MasterControllerConfiguration search options are: {0}", mSearchOptions));
    }


    /** Parse an XML node for execute match.
     *
     * @param node XML node starting the execute match configuration node.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     */
    private void parseExecuteMatch(Node node)
            throws ConfigurationException {
        NodeList optionList = node.getChildNodes();
        int builderCount = optionList.getLength();
        for (int i = 0; i < builderCount; i++) {
            Node option = optionList.item(i);
            if (option.getNodeType() == Node.ELEMENT_NODE) {
                String optionName = option.getNodeName();
                if (optionName.equals("query-builder")) {
                    parseExecuteMatchQueryBuilder(option);
                } else {
                    throw new ConfigurationException(mLocalizer.t("CFG528: Could not parse the ExecuteMatch " + 
                                                        "XML node.  This is an unrecognized tag: {0}", optionName));
                }
            }
        }
    }


    /** Parse an XML node for merged record update.
     *
     * @param node XML node starting the merged record update configuration node.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     */
    private void parseMergedRecordUpdate(Node node)
            throws ConfigurationException {
        Node optionValue = node.getFirstChild();
        String value = optionValue.getNodeValue();
        if (value.equalsIgnoreCase("Enabled")) {
            mMergedRecordUpdate = true;
        } else if (value.equalsIgnoreCase("Disabled")) {
            mMergedRecordUpdate = false;
        } else {
            throw new ConfigurationException(mLocalizer.t("CFG529: Invalid value for the " + 
                                                        "MergedRecordUpdate XML node: {0}", value));
        }
    }    

    /** Parse an XML node for execute match query builder.
     *
     * @param node XML node starting the execute match query builder 
     * configuration node.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     */
    private void parseExecuteMatchQueryBuilder(Node node)
            throws ConfigurationException {
        try {
            NamedNodeMap attributes = node.getAttributes();
            String matchQueryBuilderId = attributes.getNamedItem("name").getNodeValue();
            mSearchOptions = new EOSearchOptions(matchQueryBuilderId, null);
            NodeList optionList = node.getChildNodes();
            int builderCount = optionList.getLength();
            for (int i = 0; i < builderCount; i++) {
                Node option = optionList.item(i);
                if (option.getNodeType() == Node.ELEMENT_NODE) {
                    String optionName = option.getNodeName();
                    if (optionName.equals("option")) {
                        parseOption(option);
                    } else {
                        throw new ConfigurationException(mLocalizer.t("CFG530: Could not parse the" + 
                                                        "ExecuteMatchQueryBuilder XML node.  " + 
                                                        "This is an unrecognized tag: {0}", optionName));
                    }
                }
            }
        } catch (Exception e) {
            throw new ConfigurationException(mLocalizer.t("CFG531: Could not parse the" + 
                                                        "ExecuteMatchQueryBuilder XML node: {0}", e));
        }
    }


    /** Parse an XML node for options.
     *
     * @param node XML node starting options configuration node.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     */
    private void parseOption(Node node)
            throws ConfigurationException {
        NamedNodeMap attributes = node.getAttributes();
        String key = attributes.getNamedItem("key").getNodeValue();
        String value = attributes.getNamedItem("value").getNodeValue();
        mSearchOptions.setOption(key, value);
    }


    /** Parse an XML node for updates.
     *
     * @param node XML node starting update configuration node.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     */
    private void parseUpdateMode(Node node)
            throws ConfigurationException {
        Node optionValue = node.getFirstChild();
        String updateMode = optionValue.getNodeValue();
        if (updateMode.equals("Pessimistic")) {
            mPessimisticEnabled = true;
        } else if (updateMode.equals("Optimistic")) {
            mPessimisticEnabled = false;
        } else {
            throw new ConfigurationException(mLocalizer.t("CFG532: Could not parse the" + 
                                                        "UpdateMode XML node.  " + 
                                                        "This is an unrecognized update mode: {0}", updateMode));
        }
    }


    /** Parse an XML node for logic class.
     *
     * @param node XML node starting logic class configuration node.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     */
    private void parseLogicClass(Node node)
            throws ConfigurationException {
        Node optionValue = node.getFirstChild();
        if (optionValue != null) {
            mUserLogicClass = optionValue.getNodeValue();
        }
    }

    /** Parse an XML node for logic class GUI.
     *
     * @param node XML node starting logic class GUI configuration node.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     */
    private void parseLogicClassGui(Node node)
            throws ConfigurationException {
        Node optionValue = node.getFirstChild();
        if (optionValue != null) {
            mUserLogicClassGui = optionValue.getNodeValue();
        }
    }

    /** Returns a string representation of the MasterControllerConfiguration
     * instance.
     *
     * @return string representation of the MasterControllerConfiguration
     * instance.
     */
    public String toString() {
        return "Update Mode: " + mPessimisticEnabled + "\n" 
                + "Search Options: " + mSearchOptions;
    }

}
