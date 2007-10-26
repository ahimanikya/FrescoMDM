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
package com.sun.mdm.index.parser;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author gzheng
 * @version
 */
public class BlockType {
    /**
     * format string
     */
    private static final String MFMT = "            [elephantBlockType]";
    /**
     * block definition
     */
    public static final String TAGBLOCKDEFINITION = "block-definition";
    /**
     * startswith tag
     */
    public static final String TAGSTARTSWITH = "startswith";
    /**
     * exact tag
     */
    public static final String TAGEXACT = "exact";
    /**
     * soundex tag
     */
    public static final String TAGSOUNDEX = "soundex";
    /**
     * nysiis tag
     */
    public static final String TAGNYSIIS = "nysiis";
    /**
     * field tag
     */
    public static final String TAGFIELD = "field";
    /**
     * priority tag
     */
    public static final String TAGPRIORITY = "priority";
    /**
     * length tag
     */
    public static final String TAGLENGTH = "length";
    /**
     * atr name
     */
    public static final String ATRNAME = "name";
    /**
     * atr id
     */
    public static final String ATRID = "id";

    /**
     * block number
     */
    public static final String TAGBLOCKNUMBER = "number";

    private ArrayList rules;

    private String strBlockNumber;


    /**
     * default constructor
     */
    public BlockType() {
        rules = new ArrayList();
        strBlockNumber = "";
    }


    /**
     * @return String ret string
     */
    public String getBlockNumber() {
        return strBlockNumber;
    }


    /**
     * @return ArrayList array list
     */
    public ArrayList getBlockingRules() {
        return rules;
    }


    /**
     * parse
     * @param node Node
     * @exception NumberFormatException number format exception
     */
    void parse(Node node)
        throws NumberFormatException {
        /*
              if (node.hasChildNodes()) {
              NodeList nl = node.getChildNodes();
              for(int i=0; i<nl.getLength(); i++) {
              if(nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
              if(_tag_block_number.equals(((Element)nl.item(i)).getTagName()))
              strBlockNumber = Utils.getStrElementValue(nl.item(i));
              if(_tag_order.equals(((Element)nl.item(i)).getTagName()))
              strOrder = Utils.getStrElementValue(nl.item(i));
              if(_tag_range.equals(((Element)nl.item(i)).getTagName()))
              strRange = Utils.getStrElementValue(nl.item(i));
              }
              }
              }
          */
        NodeList defs = node.getChildNodes();

        for (int i = 0; i < defs.getLength(); i++) {
            Node n = defs.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                if (n.getNodeName().equals(TAGBLOCKNUMBER)) {
                    strBlockNumber = Utils.getStrElementValue(n);
                }
                if (n.getNodeName().equals(TAGSTARTSWITH)) {
                    parseStartsWith(n);
                } else if (n.getNodeName().equals(TAGEXACT)) {
                    parseExact(n);
                } else if (n.getNodeName().equals(TAGSOUNDEX)) {
                    parseSoundex(n);
                } else if (n.getNodeName().equals(TAGNYSIIS)) {
                    parseNysiis(n);
                }
            }
        }
        // end for
    }


    /**
     * parse block type
     * @param e Node
     * @param r BlockingRuleType
     * @exception NumberFormatException number format exception
     */
    private void parseBlockType(Node e, BlockingRuleType r)
        throws NumberFormatException {

        if (e.hasChildNodes()) {
            NodeList nl = e.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (TAGPRIORITY.equals((
                            (Element) nl.item(i)).getTagName())) {
                        // get priority
                        String priority = Utils.getStrElementValue(nl.item(i));
                        r.setPriority(Integer.parseInt(priority));
                    }
                    if (TAGFIELD.equals((
                            (Element) nl.item(i)).getTagName())) {
                        // get field name
                        String fieldName 
                            = ((Element) nl.item(i)).getAttribute(ATRNAME);
                        r.parseField(fieldName);
                    }
                }
            }
        }
    }


    /**
     * @param e Node
     * @exception NumberFormatException number format exception
     */
    private void parseExact(Node e)
        throws NumberFormatException {
        BlockingRuleType r = new BlockingRuleType(BlockingRuleType.TYPE_EXACT);
        parseBlockType(e, r);
        rules.add(r);
    }


    /**
     * @param e Node
     * @exception NumberFormatException number format exception
     */
    private void parseNysiis(Node e)
        throws NumberFormatException {
        BlockingRuleType r = new BlockingRuleType(BlockingRuleType.TYPE_NYSIIS);
        parseBlockType(e, r);
        rules.add(r);
    }


    /**
     * @param e Node
     * @exception NumberFormatException number format exception
     */
    private void parseSoundex(Node e)
        throws NumberFormatException {
        BlockingRuleType r 
            = new BlockingRuleType(BlockingRuleType.TYPE_SOUNDEX);
        parseBlockType(e, r);
        rules.add(r);
    }


    /**
     * @param e Node
     * @exception NumberFormatException number format exception
     */
    private void parseStartsWith(Node e)
        throws NumberFormatException {
        BlockingRuleType r 
            = new BlockingRuleType(BlockingRuleType.TYPE_STARTSWITH);
        parseBlockType(e, r);
        rules.add(r);
    }
}
