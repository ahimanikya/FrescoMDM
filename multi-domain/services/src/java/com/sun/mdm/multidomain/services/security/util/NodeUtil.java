/*
 * BEGIN_HEADER - DO NOT EDIT
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * If applicable add the following below this CDDL HEADER,
 * with the fields enclosed by brackets "[]" replaced with
 * your own identifying information: Portions Copyright
 * [year] [name of copyright owner]
 */

/*
 * @(#)NodeUtil.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */

package com.sun.mdm.multidomain.services.security.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * NodeUtil.java
 *
 * Created on October 3, 2007, 2:50 PM
 * @author  rtam
 */
public class NodeUtil {
    
    /** Creates a new instance of NodeUtil */
    public NodeUtil() {
    }
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
