/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

import org.w3c.dom.Node;

/**
 *
 * @author wee
 */
public class RelationshipUtil {

    public static String getStrElementValue(Node node) {
        Node tnode = node.getFirstChild();

        if (null != tnode) {
            return tnode.getNodeValue();
        } else {
            return "";
        }
    }

    public static int getIntElementValue(Node node) {
        Node tnode = node.getFirstChild();

        if (null != tnode) {
            return Integer.parseInt(tnode.getNodeValue());
        } else {
            return -1;
        }
    }
}


