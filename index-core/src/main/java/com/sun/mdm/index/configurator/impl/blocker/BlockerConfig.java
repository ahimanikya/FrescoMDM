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
package com.sun.mdm.index.configurator.impl.blocker;

import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.epath.EPathParser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;

import java.util.Map;
import java.util.Hashtable;
import java.util.Collection;
import java.util.logging.Level;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;


/** Blocker configuration class.
 *
 * @version $Revision: 1.1 $
 */
public class BlockerConfig implements ConfigurationInfo {
    
    /** xml tags */
    public static final String MODULE_NAME = "Blocker";
    public static final String TAG_BLOCK_DEFINITION = "block-definition";
    public static final String ATR_NUMBER = "number";
    public static final String TAG_BLOCK_RULE = "block-rule";
    public static final String TAG_EXACT = "equals";
    public static final String TAG_STARTS_WITH = "starts-with";
    public static final String TAG_NOT_EQUALS = "not-equals";
    public static final String TAG_LESS_THAN = "less-than";
    public static final String TAG_LESS_THAN_EQUALS = "less-than-equals";
    public static final String TAG_GREATER_THAN = "greater-than";
    public static final String TAG_GREATER_THAN_EQUALS = "greater-than-equals";
    public static final String TAG_CONTAINS = "contains";
    public static final String TAG_RANGE = "range";
    public static final String TAG_FIELD = "field";
    public static final String TAG_SOURCE = "source";
    public static final String TAG_DEFAULT = "default";
    public static final String TAG_LOWER = "lower-bound";
    public static final String TAG_UPPER = "upper-bound";
    public static final String TAG_SQL = "sql";
    public static final String TAG_HINT = "hint";
    
    /** block definitions */
    private Map definitions;
    
    private transient Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient Localizer mLocalizer = Localizer.get();
    

    /** Creates a new instance of BlockerConfig */
    public BlockerConfig() {
        definitions = new Hashtable();
    }

    /** Returns a String representation of the object.
     *
     * @return String representation of the object.
     */    
    public String toString() {
        if (definitions == null) {
            return "";
        } else {
            return definitions.keySet().toString();
        }
    }

    /** Add a block definition.
     *
     * @param blockDef Block definition to add.
     */    
    public void addDefinition(BlockDefinition blockDef) {
        definitions.put(blockDef.getId(), blockDef);
    }

    /** Get definitions.
     *
     * @return block definitions.
     */    
    public Collection getDefinitions() {
        return definitions.values();
    }

    /** Get definition by id.
     *
     * @param id ID of block definition.
     * @return block definition.
     */    
    public BlockDefinition getDefinition(String id) {
        return (BlockDefinition) definitions.get(id);
    }

    /** Finish.
     *
     * @return result code.
     */
    public int finish() {
        return 0;
    }

    /** Return String representing the module type
     *
     * @return return String representing the module type
     */
    public String getModuleType() {
        return "BlockerConfig";
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
    public void parse(Node node) throws ConfigurationException {
        try {
            NodeList defs = node.getChildNodes();
            int len = defs.getLength();

            for (int i = 0; i < len; i++) {
                Node tmpNode = defs.item(i);

                if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
                    if (tmpNode.getNodeName().equals(TAG_BLOCK_DEFINITION)) {
                        BlockDefinition bd = parseBlockDefinition(tmpNode);

                        if (bd != null) {
                            addDefinition(bd);
                        }
                    }
                }
            }
            mLogger.info(mLocalizer.x("CFG026: Block Definition mappings are: {0}", LogUtil.mapToString(definitions)));
        } catch (Exception ex) {
            throw new ConfigurationException(mLocalizer.t("CFG514: BlockerConfig " + 
                                        "could not parse an XML configuration node: {0}", ex));
        }
    }


    /** Parse a block rule.
     *
     * @param node XML node.
     * @throws DOMException if there is a DOM tree access exception.
     * @throws EPathException if an invalid EPath was used.
     * @return a block rule object.
     */    
    private BlockRule parseBlockRule(Node node)
            throws DOMException, EPathException, ConfigurationException {
                
        BlockRule br = new BlockRule();

        NodeList conds = node.getChildNodes();
        int len = conds.getLength();

        for (int i = 0; i < len; i++) {
            Node tmpNode = conds.item(i);

            if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
                AbstractCondition ac = parseCondition(tmpNode);

                if (ac != null) {
                    br.addCondition(ac);
                }
            }
        }

        return br;
    }

    /** Parse block definition.
     *
     * @param node XML node.
     * @throws DOMException if there is a DOM tree access exception.
     * @throws EPathException if an invalid EPath was used.
     * @return a block definition
     */    
    private BlockDefinition parseBlockDefinition(Node node)
            throws DOMException, EPathException, ConfigurationException {
                
        BlockDefinition bd = new BlockDefinition();
        NamedNodeMap attrs = node.getAttributes();

        Node attrNode = attrs.getNamedItem(ATR_NUMBER);
        String number = attrNode.getNodeValue();
        bd.setId(number);

        NodeList rules = node.getChildNodes();
        int len = rules.getLength();

        for (int i = 0; i < len; i++) {
            Node tmpNode = rules.item(i);

            if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
                if (tmpNode.getNodeName().equals(TAG_BLOCK_RULE)) {
                    BlockRule br = parseBlockRule(tmpNode);

                    if (bd != null) {
                        bd.addRule(br);
                    }
                } else if (tmpNode.getNodeName().equals(TAG_SQL)) {
                    bd.setSql(getStrElementValue(tmpNode));
                } else if (tmpNode.getNodeName().equals(TAG_HINT)) {
                    bd.setHint(getStrElementValue(tmpNode));
                }
            }
        }

        return bd;
    }

    /** Parse a condition.
     *
     * @param node XML node.
     * @throws DOMException if there is a DOM tree access exception.
     * @throws EPathException if an invalid EPath was used.
     * @return a condition object
     */    
    private AbstractCondition parseCondition(Node node)
            throws DOMException, EPathException, ConfigurationException {
                
        String conditionType = node.getNodeName();
        AbstractCondition ac;

        if ((conditionType != null) && conditionType.equals(TAG_STARTS_WITH)) {
            ac = new StartsWithCondition();
        } else if ((conditionType != null) && conditionType.equals(TAG_EXACT)) {
            ac = new ExactCondition();
        } else if ((conditionType != null) && conditionType.equals(TAG_NOT_EQUALS)) {
            ac = new NotEqualsCondition();
        } else if ((conditionType != null) && conditionType.equals(TAG_GREATER_THAN)) {
            ac = new GreaterThanCondition();
        } else if ((conditionType != null) && conditionType.equals(TAG_LESS_THAN)) {
            ac = new LessThanCondition();
        } else if ((conditionType != null) && conditionType.equals(TAG_GREATER_THAN_EQUALS)) {
            ac = new GreaterThanEqualsCondition();
        } else if ((conditionType != null) && conditionType.equals(TAG_LESS_THAN_EQUALS)) {
            ac = new LessThanEqualsCondition();
        } else if ((conditionType != null) && conditionType.equals(TAG_CONTAINS)) {
            ac = new ContainsCondition();
        } else if ((conditionType != null) && conditionType.equals(TAG_RANGE)) {
            ac = new RangeCondition();
        } else {
            throw new ConfigurationException(mLocalizer.t("CFG515: Unrecognized parse " + 
                                                          "condition: {0}", conditionType));
        }

        NodeList nl = node.getChildNodes();
        int len = nl.getLength();

        for (int i = 0; i < len; i++) {
            Node tmpNode = nl.item(i);

            if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = tmpNode.getNodeName();
                if (nodeName.equals(TAG_SOURCE)) {
                    String source = getStrElementValue(tmpNode);
                    ac.setSource(EPathParser.parse(source));
                } else if (nodeName.equals(TAG_FIELD)) {
                    String field = getStrElementValue(tmpNode);
                    ac.setField(field);
                } else if (nodeName.equals(TAG_DEFAULT)) {
                    if (ac instanceof RangeCondition) {
                        //RANGE_CONDITION: handle <default> tag 
                        RangeCondition rc = (RangeCondition) ac;
                        NodeList nl2 = tmpNode.getChildNodes();
                        int len2 = nl2.getLength();
                        for (int j = 0; j < len2; j++) {
                            Node tmpNode2 = nl2.item(j);
                            if (tmpNode2.getNodeType() == Node.ELEMENT_NODE) {
                                String nodeName2 = tmpNode2.getNodeName();
                                NamedNodeMap attrs = tmpNode2.getAttributes();
                                if (attrs == null) {
                                    throw new ConfigurationException(mLocalizer.t("CFG516: " + 
                                                        "Type attribute must be defined for: {0}", nodeName2));
                                }
                                Node defaultType = attrs.getNamedItem("type");
                                if (defaultType == null) {
                                    throw new ConfigurationException(mLocalizer.t("CFG517: " + 
                                                        "Default type must be defined for: {0}", nodeName2));
                                }
                                if (nodeName2.equals(TAG_LOWER)) {
                                    rc.setDefaultLowerType((String) defaultType.getNodeValue());
                                    rc.setDefaultLowerValue(getStrElementValue(tmpNode2));
                                } else if (nodeName2.equals(TAG_UPPER)) {
                                    rc.setDefaultUpperType((String) defaultType.getNodeValue());
                                    rc.setDefaultUpperValue(getStrElementValue(tmpNode2));
                                } else {
                                    throw new ConfigurationException(mLocalizer.t("CFG518: " + 
                                                        "Only upper-bound / lower-bound tag allowed. " + 
                                                        "This is an invalid tag: {0}", nodeName2));
                                }
                            }
                        }
                    } else {
                        throw new ConfigurationException(mLocalizer.t("CFG519: Only range " + 
                                                        "conditions can have a default tag."));
                    }

                }
            }
        }

        return ac;
    }

    /** Return the TEXT value of a node.
     *
     * @param node XML node.
     * @return the TEXT value of a node.
     */    
    private String getStrElementValue(Node node) {
        Node tnode = node.getFirstChild();

        if (tnode != null) {
            return tnode.getNodeValue();
        } else {
            return null;
        }
    }
}
