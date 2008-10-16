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
import com.sun.mdm.multidomain.relationship.RelationshipType;
import com.sun.mdm.multidomain.relationship.RelationshipObject;
import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.query.SearchCriteria;
import com.sun.mdm.multidomain.services.query.SearchOptions;
import com.sun.mdm.multidomain.services.util.ObjectBuilder;

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
    		List<RelationshipType> types = relationshipManager.getRelationshipTypes("Person", "Product");
    		assertTrue(types.size() == 2);
    		assertTrue("Person".equals(types.get(0).getSourceDomain()));
    		assertTrue("Product".equals(types.get(0).getTargetDomain()));

    		types = relationshipManager.getRelationshipTypes("Foo", "Foo");
    		assertTrue(types.size() == 0);
    		
    	} catch(ServiceException sx) {
    		fail(sx.getMessage());
    	}
    }

    public void xxxtest002() {
    	try {
    		SearchOptions searchOptions = new SearchOptions();
    		
    		EPathArrayList sourceFields = new EPathArrayList();    		
    		sourceFields.add("Person.FirstName");
    		sourceFields.add("Person.LastName");
    		sourceFields.add("Person.SSN");
    		sourceFields.add("Person.Address.AddressLine1");
    		sourceFields.add("Person.Address.City");
    		searchOptions.setOptions("Person", sourceFields);
    		
    		EPathArrayList targetFields = new EPathArrayList();
    		targetFields.add("Company.CompanyName");
    		targetFields.add("Company.StockSymbol");
    		targetFields.add("Company.TaxPayerID");
    		targetFields.add("Company.Address.AddressLine1");
    		targetFields.add("Company.Address.City");    		
    		searchOptions.setOptions("Company", targetFields);
    		    		
    		SearchCriteria searchCriteria = new SearchCriteria();
    		
    		Map<String, String> sourceSearchCriteria = new HashMap<String, String>();
    		// Person.FirstName
    		// Person.LastName
    		// Person.SSN
    		// Person.Address.AddressLine1
    		// Person.Address.City
    		sourceSearchCriteria.put("FirstName","Foo");
    		SystemObject sourceSO = ObjectBuilder.createSystemObject("Person", sourceSearchCriteria);
    		searchCriteria.setSystemObject("Person", sourceSO);

    		Map<String, String> targetSearchCriteria = new HashMap<String, String>();    		
    		// Company.CompanyName
    		// Company.StockSymbol
    		// Company.TaxPayerID
    		// Company.Address.AddressLine1
    		// Company.Address.City
    		// Fix me CompanyName
    		targetSearchCriteria.put("FirstName", "Foo");
    		
    		// Fix me Company
    		SystemObject targetSO = ObjectBuilder.createSystemObject("Person", targetSearchCriteria);
    		searchCriteria.setSystemObject("Company", targetSO);
    		
    		Relationship relationship = new Relationship();
            SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");    		
    		//fixme relationship.setStartDate(dateFormat.parse("09/17/2008"));
    		//fixme relationship.setEndDate(dateFormat.parse("09/18/2008"));
    		//fixme relationship.setPurgeDate(dateFormat.parse("09/19/2008"));    		
    		searchCriteria.setRelationship(relationship);
    		
    		List<RelationshipObject> ros =relationshipManager.searchRelationships(searchOptions, searchCriteria);
    		
    		assertTrue(ros.size() == 3);
    		//fixme assertTrue("sFoo1".equals(ros.get(0).getSourceObject().getValue("FirstName")));
    		//fixme assertTrue("sFoo1".equals(ros.get(0).getSourceObject().getValue("LastName")));
    		//fixme assertTrue("tFoo1".equals(ros.get(0).getTargetObject().getValue("FirstName")));
    		assertTrue("tFoo1".equals(ros.get(0).getTargetObject().getValue("LastName")));
    		
    		//fixme assertTrue("sFoo2".equals(ros.get(1).getSourceObject().getValue("FirstName")));
    		//fixme assertTrue("sFoo2".equals(ros.get(1).getSourceObject().getValue("LastName")));
    		assertTrue("tFoo2".equals(ros.get(1).getTargetObject().getValue("FirstName")));
    		assertTrue("tFoo2".equals(ros.get(1).getTargetObject().getValue("LastName")));
    		
    		//fixme assertTrue("sFoo3".equals(ros.get(2).getSourceObject().getValue("FirstName")));
    		//fixme assertTrue("sFoo3".equals(ros.get(2).getSourceObject().getValue("LastName")));
    		assertTrue("tFoo3".equals(ros.get(2).getTargetObject().getValue("FirstName")));
    		assertTrue("tFoo3".equals(ros.get(2).getTargetObject().getValue("LastName")));
    		
    		
    	} catch (ObjectException oe) {
    		fail(oe.getMessage());    		    		    		   		    		
    	} catch(EPathException ex) {
    		fail(ex.getMessage());    		
    	} catch(ServiceException sx) {
    		fail(sx.getMessage());
    	}
    	
    }
    
}
