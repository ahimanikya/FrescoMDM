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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Node;

import junit.framework.TestCase;

/**
 * HierarchyDefTest
 * @author cye
 */
public class HierarchyDefTest extends TestCase {

    public HierarchyDefTest (String name) {
        super(name);
    }
    
    public void setUp() {
    }
   
    public Node getDomFromSource(Source source) throws IOException {
        Node result = null;
        try {
            DOMResult domResult = new DOMResult();
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.transform(source, domResult);
            result = domResult.getNode();
        } catch (Exception e) {
            throw new IOException("Error while creating document: " + e.getMessage());
        }
        return result;
    }
    
    public void test001() throws Exception {        
    	/*  test case001
    	 *  <hierarchy>
    	 *   <node>
    	 * 		<name>node1<name>
    	 *   <node>
    	 *  <hierarchy>
    	 */    	
        StringBuffer objectXmlBuffer = new StringBuffer();
        objectXmlBuffer.append("<" + HierarchyDef.tagHierarchy + ">");        
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
        objectXmlBuffer.append("node1");
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");
        objectXmlBuffer.append("</" + HierarchyDef.tagHierarchy + ">");        
        
        String ObjectXmlString = objectXmlBuffer.toString();

        InputStream is = new ByteArrayInputStream(ObjectXmlString.getBytes());
	    Source inputSource = new StreamSource(is);        
        Node node = getDomFromSource(inputSource);
        
        HierarchyDef hierarchy = new HierarchyDef(); 
        
        hierarchy.parse(node.getFirstChild());

        System.out.println(hierarchy.toString());
        
        HierarchyNodeDef hNodeDef = hierarchy.getNode();        
        assertTrue("node1".equals(hNodeDef.getName()));
        assertTrue(hNodeDef.getChildren() == null);
        assertTrue(hNodeDef.getParent() == null);                
    }

    public void test002() throws Exception { 
        
    	/*  test case002
    	 * 		node1
    	 *        |
    	 *      node2
    	 */
    	
        StringBuffer objectXmlBuffer = new StringBuffer();
        
        objectXmlBuffer.append("<" + HierarchyDef.tagHierarchy + ">");                
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
        
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
        objectXmlBuffer.append("node1");
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");
        
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
        objectXmlBuffer.append("node2");
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");        
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");
        
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");
        objectXmlBuffer.append("</" + HierarchyDef.tagHierarchy + ">");        
        
        String ObjectXmlString = objectXmlBuffer.toString();

        InputStream is = new ByteArrayInputStream(ObjectXmlString.getBytes());
	    Source inputSource = new StreamSource(is);        
        Node node = getDomFromSource(inputSource);
        
        HierarchyDef hierarchy = new HierarchyDef();         
        hierarchy.parse(node.getFirstChild());
        System.out.println(hierarchy.toString());
        
        HierarchyNodeDef hNodeDef = hierarchy.getNode();

        assertTrue("node1".equals(hNodeDef.getName()));
        assertTrue(hNodeDef.getChildren() != null);

        assertTrue(hNodeDef.getChildren().size() == 1);        
        assertTrue("node2".equals(hNodeDef.getChildren().get(0).getName()));
        assertTrue(hNodeDef.getChildren().get(0).getChildren() == null);
        
        assertTrue(hNodeDef.getChildren().get(0).getParent() != null);
        assertTrue("node1".equals(hNodeDef.getChildren().get(0).getParent().getName()));                
    }

    public void test003() throws Exception { 
        
    	/*  test case003
    	 * 		  node1
    	 *     /    |    \
    	 *  node2 node3 node4
    	 */
    	    	
        StringBuffer objectXmlBuffer = new StringBuffer();
        objectXmlBuffer.append("<" + HierarchyDef.tagHierarchy + ">");         
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
        
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
        objectXmlBuffer.append("node1");
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");
        
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
        objectXmlBuffer.append("node2");
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");        
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");

        objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
        objectXmlBuffer.append("node3");
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");        
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");

        objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
        objectXmlBuffer.append("node4");
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");        
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");
        
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");
        objectXmlBuffer.append("</" + HierarchyDef.tagHierarchy + ">");        
        String ObjectXmlString = objectXmlBuffer.toString();

        InputStream is = new ByteArrayInputStream(ObjectXmlString.getBytes());
	    Source inputSource = new StreamSource(is);        
        Node node = getDomFromSource(inputSource);
        
        HierarchyDef hierarchy = new HierarchyDef();         
        hierarchy.parse(node.getFirstChild());
        System.out.println(hierarchy.toString());
        
        HierarchyNodeDef hNodeDef = hierarchy.getNode();
        
        assertTrue("node1".equals(hNodeDef.getName()));
        assertTrue(hNodeDef.getChildren() != null);
        assertTrue(hNodeDef.getParent() == null);

        assertTrue(hNodeDef.getChildren().size() == 3);       
        
        assertTrue("node2".equals(hNodeDef.getChildren().get(0).getName()));
        assertTrue(hNodeDef.getChildren().get(0).getChildren() == null);
        assertTrue("node1".equals(hNodeDef.getChildren().get(0).getParent().getName()));
        
        assertTrue("node3".equals(hNodeDef.getChildren().get(1).getName()));
        assertTrue(hNodeDef.getChildren().get(1).getChildren() == null);
        assertTrue("node1".equals(hNodeDef.getChildren().get(1).getParent().getName()));
        
        assertTrue("node4".equals(hNodeDef.getChildren().get(2).getName()));
        assertTrue(hNodeDef.getChildren().get(2).getChildren() == null);
        assertTrue("node1".equals(hNodeDef.getChildren().get(2).getParent().getName()));        
    }
    
    public void test004() throws Exception { 
        
    	/*  test case004
    	 * 		node1
    	 *        |
    	 *      node2
    	 *        |
    	 *      node3
    	 *        |
    	 *      node4     
    	 */
    	
        StringBuffer objectXmlBuffer = new StringBuffer();
        objectXmlBuffer.append("<" + HierarchyDef.tagHierarchy + ">");                
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
        
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
        objectXmlBuffer.append("node1");
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");
        
        	objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
        	objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
        	objectXmlBuffer.append("node2");
        	objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");

        		objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
        		objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
        		objectXmlBuffer.append("node3");
        		objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");  
        
        			objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
        			objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
        			objectXmlBuffer.append("node4");
        			objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");        
        			objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");
        
        		objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");
        
        	objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");
        
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");
        objectXmlBuffer.append("</" + HierarchyDef.tagHierarchy + ">");        
        
        String ObjectXmlString = objectXmlBuffer.toString();

        InputStream is = new ByteArrayInputStream(ObjectXmlString.getBytes());
	    Source inputSource = new StreamSource(is);        
        Node node = getDomFromSource(inputSource);
        
        HierarchyDef hierarchy = new HierarchyDef();         
        hierarchy.parse(node.getFirstChild());
        System.out.println(hierarchy.toString());
        
        HierarchyNodeDef hNodeDef = hierarchy.getNode();
        
        assertTrue("node1".equals(hNodeDef.getName()));
        assertTrue(hNodeDef.getChildren() != null);
        assertTrue(hNodeDef.getParent() == null);
        assertTrue(hNodeDef.getChildren().size() == 1);       
        
        HierarchyNodeDef hNodeDef2 = hNodeDef.getChildren().get(0);
        assertTrue("node2".equals(hNodeDef2.getName()));
        assertTrue(hNodeDef2.getChildren() != null);
        assertTrue(hNodeDef2.getChildren().size() == 1);        
        assertTrue("node1".equals(hNodeDef2.getParent().getName()));
        
        HierarchyNodeDef hNodeDef3 = hNodeDef2.getChildren().get(0);
        assertTrue("node3".equals(hNodeDef3.getName()));
        assertTrue(hNodeDef3.getChildren() != null);
        assertTrue(hNodeDef3.getChildren().size() == 1);        
        assertTrue("node2".equals(hNodeDef3.getParent().getName()));
        
        HierarchyNodeDef hNodeDef4 = hNodeDef3.getChildren().get(0);
        assertTrue("node4".equals(hNodeDef4.getName()));
        assertTrue(hNodeDef4.getChildren() == null);
        assertTrue("node3".equals(hNodeDef4.getParent().getName()));
        
    }

    public void test005() throws Exception {         
    	/*  test case005
    	 * 		node1
    	 *      /   \
    	 *   node2 node4   
    	 *     /     \
    	 *   node3  node5
    	 */    	
        StringBuffer objectXmlBuffer = new StringBuffer();
        objectXmlBuffer.append("<" + HierarchyDef.tagHierarchy + ">");                
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
        
        objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
        objectXmlBuffer.append("node1");
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");
        
       	objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
       		objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
       		objectXmlBuffer.append("node2");        	
       		objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");
       		
       		objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
       		objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
       		objectXmlBuffer.append("node3");
       		objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");        
       		objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");       	
		objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");
        	
     	objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
     		objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
     		objectXmlBuffer.append("node4");
     		objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");
       		objectXmlBuffer.append("<" + HierarchyNodeDef.tagNode + ">");
       		objectXmlBuffer.append("<" + HierarchyNodeDef.tagName + ">");
       		objectXmlBuffer.append("node5");
       		objectXmlBuffer.append("</" + HierarchyNodeDef.tagName + ">");        
       		objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");       	     		
    	objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");
                        
        objectXmlBuffer.append("</" + HierarchyNodeDef.tagNode + ">");
        objectXmlBuffer.append("</" + HierarchyDef.tagHierarchy + ">");        
        
        String ObjectXmlString = objectXmlBuffer.toString();

        InputStream is = new ByteArrayInputStream(ObjectXmlString.getBytes());
	    Source inputSource = new StreamSource(is);        
        Node node = getDomFromSource(inputSource);
        
        HierarchyDef hierarchy = new HierarchyDef();         
        hierarchy.parse(node.getFirstChild());
        System.out.println(hierarchy.toString());        
        HierarchyNodeDef hNodeDef1 = hierarchy.getNode();
        
        assertTrue("node1".equals(hNodeDef1.getName()));
        assertTrue(hNodeDef1.getChildren() != null);
        assertTrue(hNodeDef1.getParent() == null);
        assertTrue(hNodeDef1.getChildren().size() == 2);       
        
        HierarchyNodeDef hNodeDef2 = hNodeDef1.getChildren().get(0);
        assertTrue("node2".equals(hNodeDef2.getName()));
        assertTrue(hNodeDef2.getChildren() != null);
        assertTrue("node1".equals(hNodeDef2.getParent().getName()));
        assertTrue(hNodeDef2.getChildren().size() == 1);       
        
        HierarchyNodeDef hNodeDef3 = hNodeDef2.getChildren().get(0);
        assertTrue("node3".equals(hNodeDef3.getName()));
        assertTrue(hNodeDef3.getChildren() == null);
        assertTrue("node2".equals(hNodeDef3.getParent().getName()));
                       
        HierarchyNodeDef hNodeDef4 = hNodeDef1.getChildren().get(1);
        assertTrue("node4".equals(hNodeDef4.getName()));
        assertTrue(hNodeDef4.getChildren() != null);
        assertTrue("node1".equals(hNodeDef4.getParent().getName()));
        assertTrue(hNodeDef4.getChildren().size() == 1);       
        
        HierarchyNodeDef hNodeDef5 = hNodeDef4.getChildren().get(0);
        assertTrue("node5".equals(hNodeDef5.getName()));
        assertTrue(hNodeDef5.getChildren() == null);
        assertTrue("node4".equals(hNodeDef5.getParent().getName()));                
    }
    
}
