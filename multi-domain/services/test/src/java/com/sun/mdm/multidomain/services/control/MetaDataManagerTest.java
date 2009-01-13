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

import com.sun.mdm.multidomain.services.model.Domain;
import com.sun.mdm.multidomain.services.core.ServiceException;

/**
 * MetaDataManagerTest class. 
 * @author cye
 */
public class MetaDataManagerTest extends TestCase {

    private MetaDataManager metaDataManager;
	
    public MetaDataManagerTest (String name) {
        super(name);
    }
    
    public void setUp() {
    	metaDataManager = new MetaDataManager();
    }
    
    public void test001() {
        /* TBD need to setup database.
    	try {
            List<Domain> domains = metaDataManager.getDomains();	
            assertTrue(domains.size() == 10);
            assertTrue("Person".equals(domains.get(0).getName()));
            assertTrue("Company".equals(domains.get(1).getName()));
            assertTrue("Product".equals(domains.get(2).getName()));			
    	} catch(ServiceException sx) {
            fail(sx.getMessage());
    	}
        */
    }
}
