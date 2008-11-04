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
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import junit.framework.TestCase;

import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.exception.ObjectException;

import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.relationship.RelationshipDef;
import com.sun.mdm.multidomain.relationship.MultiObject.RelationshipObject;
import com.sun.mdm.multidomain.services.core.ServiceException;

/**
 * RelationshipManagerTest class.
 * @author cye
 */
public class RelationshipManagerTest extends TestCase {
	private RelationshipManager relationshipManager;
	
	public RelationshipManagerTest (String name) {
        super(name);
    }
    
    public void setUp() {
    	relationshipManager = new RelationshipManager();
    }
    
    public void test001() {
    	//rt1: Person -> Company
	    //rt2: Company -> Product
	    //rt3: Person -> Product    
    	try {
    		List<RelationshipDef> types = relationshipManager.getRelationshipDefs("Person", "Product");
    		assertTrue(types.size() == 2);
    		assertTrue("Person".equals(types.get(0).getSourceDomain()));
    		assertTrue("Product".equals(types.get(0).getTargetDomain()));

    		types = relationshipManager.getRelationshipDefs("Foo", "Foo");
    		assertTrue(types.size() == 0);
    		
    	} catch(ServiceException sx) {
    		fail(sx.getMessage());
    	}
    }
   
}
