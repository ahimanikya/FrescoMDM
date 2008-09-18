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
package com.sun.mdm.multidomain.services.control;

import java.util.List;

import junit.framework.TestCase;

import com.sun.mdm.multidomain.relationship.Attribute;
import com.sun.mdm.multidomain.relationship.RelationshipType;
import com.sun.mdm.multidomain.services.control.MetaDataManager;
import com.sun.mdm.multidomain.services.core.ServiceException;

public class MetaDataManagerTest extends TestCase {

	private MetaDataManager metaDataManager;
	
	public MetaDataManagerTest (String name) {
        super(name);
    }
    
    public void setUp() {
    	metaDataManager = new MetaDataManager();
    }
    
    public void test001() {
    	try {
    		List<String> domains = metaDataManager.getDomains();
    		
    		assertTrue(domains.size() == 3);
    		assertTrue("Person".equals(domains.get(0)));
    		assertTrue("Company".equals(domains.get(1)));
    		assertTrue("Product".equals(domains.get(2)));    				
    	} catch(ServiceException sx) {
    		fail(sx.getMessage());
    	}
    }

    
    public void test002() {
    	try {
    	    List<RelationshipType> types = metaDataManager.getRelationshipTypes("Person", "Product");
        	//rt1: Person -> Company
    	    //rt2: Company -> Product
    	    //rt3: Person -> Product
    		
    		assertTrue(types.size() == 1);
    		assertTrue("designon".equals(types.get(0).getName()));
    		assertTrue("a relationship of a Person designs a Product".equals(types.get(0).getDisplayName()));
    		assertTrue("3".equals(types.get(0).getId()));
    				
    		List<Attribute> attributes = types.get(0).getAttributes();
    		assertTrue(attributes.size() == 1);
    		assertTrue("location".equals(attributes.get(0).getName()));
    		assertTrue("Monrovia".equals(attributes.get(0).getDefaultValue()));
    				    		
    	} catch(ServiceException sx) {
    		fail(sx.getMessage());
    	}
    }

    public void test003() {
    	try {
    	    List<RelationshipType> types = metaDataManager.getRelationshipTypes();
        	//rt1: Person -> Company
    	    //rt2: Company -> Product
    	    //rt3: Person -> Product
    	    
    		assertTrue(types.size() == 3);    	    
    		assertTrue("Person".equals(types.get(0).getSourceDomain()));
    		assertTrue("Company".equals(types.get(0).getTargetDomain()));

    		assertTrue("Company".equals(types.get(1).getSourceDomain()));
    		assertTrue("Product".equals(types.get(1).getTargetDomain()));

    		assertTrue("Person".equals(types.get(2).getSourceDomain()));
    		assertTrue("Product".equals(types.get(2).getTargetDomain()));
    		
    	} catch(ServiceException sx) {
    		fail(sx.getMessage());
    	}
    }
    
}
