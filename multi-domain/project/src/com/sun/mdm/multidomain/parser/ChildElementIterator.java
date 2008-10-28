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
