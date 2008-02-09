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
 * @(#)ChildElementIterator.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */

package com.sun.mdm.index.edm.services.configuration;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author sanjay.sharma
 *
 */

public class ChildElementIterator {
    
    private int currentIndex =0;
    private NodeList fields;
    private String filter;
    
    public ChildElementIterator ( Element element ) {
        this(element, null);
    }

    /**
     * constructor  
     *
     * @param Element 
     * @param filter.  To skip elements whose tagName does not matches with the filter. 
     * @return the next matched element
     *
     */

    public ChildElementIterator ( Element element, String filter ) {
        this.fields = element.getChildNodes();
        this.filter = filter;
    }
    
    /** 
     * Returns the next matching element   
     *
     * @exception Exception if anything goes wrong
     */
        
    public Object next() {
        for (int i =currentIndex; i < fields.getLength(); i++) {
            Node e = (Node) fields.item(i);
            if ( e.getNodeType() !=  Node.ELEMENT_NODE )  {
                currentIndex++;
                continue;
            }
            
            if ( ( filter != null ) && !((Element)e).getTagName().equals(filter) ) { 
                currentIndex++;
                continue;
            }
            currentIndex++;
            return e;
        }
        return null;
    }

    /** 
     * Returns true if there is atleast one more matching element   
     *
     * @exception Exception if anything goes wrong
     */
    
    public boolean hasNext() {
        for (int i =currentIndex; i < fields.getLength(); i++) {
            org.w3c.dom.Node e = (org.w3c.dom.Node) fields.item(i);
            if ( e.getNodeType() !=  org.w3c.dom.Node.ELEMENT_NODE ) {
                continue;
            }            
            if ( ( filter != null ) && !((Element)e).getTagName().equals(filter) ) { 
                continue;
            }

            return true;
        }
        return false;
    }
}