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
package com.sun.mdm.multidomain.services.model;

import com.sun.mdm.multidomain.services.relationship.RelationshipSearch;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import junit.framework.TestCase;

/**
 * RelationshipSearchTest class.
 * @author cye
 */
public class RelationshipSearchTest extends TestCase {

    public RelationshipSearchTest (String name) {
        super(name);
    }
    
    public void setUp() {
    }
    
    public void test001() {
        try {
            RelationshipSearch rs = new RelationshipSearch();
            rs.setName("FOO");
            Date d1 = new Date("10/01/2008");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            rs.setPurgeDate(dateFormat.format(d1));
            assertTrue("FOO".equals(rs.getName()));
            assertTrue("10/01/2008".equals(rs.getPurgeDate()));
            Date d2 = dateFormat.parse("10/01/2008");
            assertTrue("10/01/2008".equals(dateFormat.format(d2)));
        } catch (ParseException pex) {            
            fail(pex.getMessage());
        }
    }
}
