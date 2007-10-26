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

import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.configurator.ConfigurationInfo;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.Hashtable;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;


/**
 * @version $Revision: 1.1 $
 */
public class UpdateManagerConfig 
    implements ConfigurationInfo {
    
    
    private static final String EMERGE_POLICY = "EnterpriseMergePolicy";
    private static final String EUNMERGE_POLICY = "EnterpriseUnmergePolicy";
    private static final String EUPDATE_POLICY = "EnterpriseUpdatePolicy";
    private static final String ECREATE_POLICY = "EnterpriseCreatePolicy";
    private static final String SMERGE_POLICY = "SystemMergePolicy";
    private static final String SUNMERGE_POLICY = "SystemUnmergePolicy";
    private static final String UNDO_ASSUME_POLICY = "UndoAssumeMatchPolicy";
    
    private final Logger logger = LogUtil.getLogger(this);
    
    private boolean mSkipUpdateIfNoChange = true;

    private Hashtable mClassNames;
    
    /** Creates a new instance of UpdateManagerConfig */
    public UpdateManagerConfig() {
        mClassNames = new Hashtable();
    }
    
    /** Initialize.
     *
     * @return result code.
     */
    public int init() {
        return 0;
    }

    /** Finish.
     *
     * @return result code.
     */
    public int finish() {
        return 0;
    }
    
    /** Return String representing the module type.
     *
     * @return Return String representing the module type.
     *
     */
    public String getModuleType() {
        return "UpdateManagerConfig";
    }

    /** Returns the #text value of an XML node.
     *
     * @param node XML node.
     * @return #text value as a String object.
     */
    private String getStrElementValue(Node node) {
        Node tnode = node.getFirstChild();
        if (tnode == null) {
            return null;
        } else {
            return tnode.getNodeValue();
        }
    }
    
    /** Parse an XML node.
     *
     * @param node XML node starting the configuration node.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     */
    public void parse(Node node) throws ConfigurationException {
        NodeList tempNL = node.getChildNodes();
        
        int count = tempNL.getLength();
        for (int i = 0; i < count; i++) {
            Node n = tempNL.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                String s = getStrElementValue(n);
                String name = n.getNodeName();
                if (s != null) {
                    if (name.equals("SkipUpdateIfNoChange")) {
                        mSkipUpdateIfNoChange = Boolean.valueOf(s).booleanValue();
                    } else {
                        mClassNames.put(name, s);
                    }
                }
            }
        }
        logger.info("Update Manager Configuration: policy class names:\n" + LogUtil.mapToString(mClassNames));
    }
    
    /** Return the Enterprise unmerge policy.
     *
     * @return Return the Enterprise unmerge policy.
     */    
    public String getEnterpriseUnmergePolicy() {
        return (String) mClassNames.get(EUNMERGE_POLICY);
    }
    
    /** Return the Enterprise merge policy.
     * @return Return the Enterprise merge policy.
     */    
    public String getEnterpriseMergePolicy() {
        return (String) mClassNames.get(EMERGE_POLICY);
    }
    
    /** Return the Enterprise update policy.
     *
     * @return Return the Enterprise update policy.
     */    
    public String getEnterpriseUpdatePolicy() {
        return (String) mClassNames.get(EUPDATE_POLICY);
    }
    
    /** Return the Enterprise create policy.
     *
     * @return Return the Enterprise create policy.
     */    
    public String getEnterpriseCreatePolicy() {
        return (String) mClassNames.get(ECREATE_POLICY);
    }
    
    /** Return the System merge policy.
     *
     * @return Return the System merge policy.
     */    
    public String getSystemMergePolicy() {
        return (String) mClassNames.get(SMERGE_POLICY);
    }
    
    /** Return the System unmerge policy.
     *
     * @return Return the System unmerge policy.
     */    
    public String getSystemUnmergePolicy() {
        return (String) mClassNames.get(SUNMERGE_POLICY);
    }
    
    /** Return the undo assume match policy.
     *
     * @return Return the undo assumed match policy.
     */    
    public String getUndoAssumePolicy() {
        return (String) mClassNames.get(UNDO_ASSUME_POLICY);
    }
    
    /** Return the value of the "Skip Update if there is no change" attribute.
     *
     * @return Return the "Skip Update if there is no change" attribute.
     */    
    public boolean getSkipUpdateIfNoChange() {
        return mSkipUpdateIfNoChange;
    }
}
