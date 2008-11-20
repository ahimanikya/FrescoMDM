/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author wee
 */
public class NodeUtil {
    /** 
     * Returns the text of the child element identified by nodeTagName.
     *
     * @param element This is the element where the child element may be found.
     * @param nodeTagName This is the name of the child element.
     * @return text of the child element.
     * @throws Exception if an error is encountered.
     */
    public static String getChildNodeText (Element element, String nodeTagName) 
            throws Exception {

        NodeList elements = element.getElementsByTagName(nodeTagName); 
    
        for (int j =0; j < elements.getLength(); j++) {
            Node node = (Node) elements.item(j);
            NodeList elementsChild = node.getChildNodes();    
            for (int k =0; k < elementsChild.getLength(); k++) {
                Node node2 = (Node) elementsChild.item(k);
                if ( node2.getNodeType() == Node.TEXT_NODE ) {
                   return node2.getNodeValue();
                }
            }
        }
        return null;
    }
    
    /**
     * Returns the text of the element  
     *
     * @param element This is the element from which to retrieve the text.
     * @return text of the element.
     * @throws Exception if an error is encountered.
     */
    public static String getNodeText (Element element) throws Exception {
    
        NodeList elementsChild = element.getChildNodes();    

        for (int k =0; k < elementsChild.getLength(); k++) {
            Node node = (Node) elementsChild.item(k);
            if ( node.getNodeType() == Node.TEXT_NODE ) {
               return node.getNodeValue();
            }
        }
        return null;
    }

}
