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
package com.sun.mdm.index.update;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.AliasObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SBROverWrite;
import com.sun.mdm.index.update.impl.EnterpriseUpdatePolicy;
import java.util.Collection;
import java.util.ArrayList;
import com.sun.mdm.index.objects.epath.EPathParser;
import com.sun.mdm.index.objects.epath.EPathAPI;
/* 
 */

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 * @author Xi Song
 */
public class UpdatePolicyTest extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public UpdatePolicyTest(String name) {
        super(name);
    }
    
    /** With middle name
     * @throws Exception an error occured
     */
    public void test1() throws Exception {
        
        //Create EnterpriseObject with SBR only
        EnterpriseObject beforeEo = new EnterpriseObject();
        SBR sbr = new SBR();
        sbr.setChildType("Person");
        sbr.setStatus(SystemObject.STATUS_ACTIVE);
        sbr.setRevisionNumber(0);
        PersonObject personObject = new PersonObject();
        personObject.setFirstName("JOHN");
        personObject.setLastName("SMITH");
        personObject.setMiddleName("A");
        sbr.setObject(personObject);
        beforeEo.setSBR(sbr);
        
        //Operation 1: modify SBR name
        EnterpriseObject afterEo = (EnterpriseObject) beforeEo.copy();
        personObject = (PersonObject) afterEo.getSBR().getObject();
        personObject.setLastName("SMITHE");
        
        EnterpriseUpdatePolicy policy = new EnterpriseUpdatePolicy();
        policy.applyUpdatePolicy(beforeEo, afterEo);
        
        Collection aliases = personObject.getAlias();        
        assertNotNull(aliases);
        assertEquals(1, aliases.size());
        AliasObject alias = (AliasObject) aliases.toArray()[0];
        assertEquals("JOHN", alias.getFirstName());
        assertEquals("SMITH", alias.getLastName());
        
        ArrayList overwrites = afterEo.getSBR().getOverWrites();
        assertEquals(3, overwrites.size());        
        
        //Operation 2: modify SBR name again
        beforeEo = afterEo;
        afterEo = (EnterpriseObject) beforeEo.copy();
        applySurvivorship(afterEo);
        personObject = (PersonObject) afterEo.getSBR().getObject();
        personObject.setLastName("SMITHEE");
        
        policy.applyUpdatePolicy(beforeEo, afterEo);
        
        aliases = personObject.getAlias();        
        assertNotNull(aliases);
        assertEquals(2, aliases.size());
        alias = (AliasObject) aliases.toArray()[1];
        assertEquals("JOHN", alias.getFirstName());
        assertEquals("SMITHE", alias.getLastName());
        
        overwrites = afterEo.getSBR().getOverWrites();
        assertEquals(6, overwrites.size());        
     
        //Operation 3: add an SO
        beforeEo = afterEo;
        afterEo = (EnterpriseObject) beforeEo.copy();
        applySurvivorship(afterEo);
        personObject = new PersonObject();
        personObject.setFirstName("JON");
        personObject.setLastName("SMITH");
        personObject.setMiddleName("A");
        SystemObject so = new SystemObject();
        so.setStatus(SystemObject.STATUS_ACTIVE);
        so.setChildType("Person");
        so.setSystemCode("SBYN");
        so.setLID("1111");
        so.setObject(personObject);
        afterEo.addSystemObject(so);
        
        policy.applyUpdatePolicy(beforeEo, afterEo);
        
        personObject = (PersonObject) afterEo.getSBR().getObject();
        aliases = personObject.getAlias();        
        assertNotNull(aliases);
        assertEquals(3, aliases.size());
        alias = (AliasObject) aliases.toArray()[2];
        assertEquals("JON", alias.getFirstName());
        assertEquals("SMITH", alias.getLastName());
        
        overwrites = afterEo.getSBR().getOverWrites();
        assertEquals(9, overwrites.size());  
        
        //Operation 4: change name of SO
        beforeEo = afterEo;
        afterEo = (EnterpriseObject) beforeEo.copy();
        applySurvivorship(afterEo);
        so = afterEo.getSystemObject("SBYN", "1111");
        personObject = (PersonObject) so.getObject();
        personObject.setLastName("SMITHE");
        
        policy.applyUpdatePolicy(beforeEo, afterEo);
        
        so = afterEo.getSystemObject("SBYN", "1111");
        personObject = (PersonObject) so.getObject();
        aliases = personObject.getAlias();        
        assertNotNull(aliases);
        assertEquals(1, aliases.size());
        alias = (AliasObject) aliases.toArray()[0];
        assertEquals("JON", alias.getFirstName());
        assertEquals("SMITH", alias.getLastName());
        
        personObject = (PersonObject) afterEo.getSBR().getObject();
        aliases = personObject.getAlias();        
        assertNotNull(aliases);
        assertEquals(4, aliases.size());
        alias = (AliasObject) aliases.toArray()[3];
        assertEquals("JON", alias.getFirstName());
        assertEquals("SMITHE", alias.getLastName());
        
        overwrites = afterEo.getSBR().getOverWrites();
        assertEquals(12, overwrites.size());  
        
        //Operation 5: add maiden name to an SO
        beforeEo = afterEo;
        afterEo = (EnterpriseObject) beforeEo.copy();
        applySurvivorship(afterEo);
        so = afterEo.getSystemObject("SBYN", "1111");
        personObject = (PersonObject) so.getObject();
        personObject.setMaiden("MAIDEN");
        personObject.setMaidenPhoneticCode("MAIDEN");
        policy.applyUpdatePolicy(beforeEo, afterEo);
        personObject = (PersonObject) afterEo.getSBR().getObject();
        aliases = personObject.getAlias();        
        assertNotNull(aliases);
        assertEquals(5, aliases.size());
        alias = (AliasObject) aliases.toArray()[4];
        assertEquals("JON", alias.getFirstName());
        assertEquals("MAIDEN", alias.getLastName());
        
        overwrites = afterEo.getSBR().getOverWrites();
        System.out.println(overwrites);
        assertEquals(15, overwrites.size());
        
        //Operation 6: add maiden name to SBR
        beforeEo = afterEo;
        afterEo = (EnterpriseObject) beforeEo.copy();
        applySurvivorship(afterEo);
        personObject = (PersonObject) afterEo.getSBR().getObject();
        personObject.setMaiden("MAIDEN2");
        personObject.setMaidenPhoneticCode("MAIDEN2");
        policy.applyUpdatePolicy(beforeEo, afterEo);
        personObject = (PersonObject) afterEo.getSBR().getObject();
        aliases = personObject.getAlias();        
        assertNotNull(aliases);
        assertEquals(6, aliases.size());
        alias = (AliasObject) aliases.toArray()[5];
        assertEquals("JOHN", alias.getFirstName());
        assertEquals("MAIDEN2", alias.getLastName());
        
        overwrites = afterEo.getSBR().getOverWrites();
        assertEquals(18, overwrites.size());
        
    }
    
    /** NO middle name
     * @throws Exception an error occured
     */
    public void test2() throws Exception {
        
        //Create EnterpriseObject with SBR only
        EnterpriseObject beforeEo = new EnterpriseObject();
        SBR sbr = new SBR();
        sbr.setChildType("Person");
        sbr.setStatus(SystemObject.STATUS_ACTIVE);
        sbr.setRevisionNumber(0);
        PersonObject personObject = new PersonObject();
        personObject.setFirstName("JOHN");
        personObject.setLastName("SMITH");
        sbr.setObject(personObject);
        beforeEo.setSBR(sbr);
        
        //Operation 1: modify SBR name
        EnterpriseObject afterEo = (EnterpriseObject) beforeEo.copy();
        personObject = (PersonObject) afterEo.getSBR().getObject();
        personObject.setLastName("SMITHE");
        
        EnterpriseUpdatePolicy policy = new EnterpriseUpdatePolicy();
        policy.applyUpdatePolicy(beforeEo, afterEo);
        
        Collection aliases = personObject.getAlias();        
        assertNotNull(aliases);
        assertEquals(1, aliases.size());
        AliasObject alias = (AliasObject) aliases.toArray()[0];
        assertEquals("JOHN", alias.getFirstName());
        assertEquals("SMITH", alias.getLastName());
        
        ArrayList overwrites = afterEo.getSBR().getOverWrites();
        assertEquals(2, overwrites.size());        
        
        //Operation 2: modify SBR name again
        beforeEo = afterEo;
        afterEo = (EnterpriseObject) beforeEo.copy();
        applySurvivorship(afterEo);
        personObject = (PersonObject) afterEo.getSBR().getObject();
        personObject.setLastName("SMITHEE");
        
        policy.applyUpdatePolicy(beforeEo, afterEo);
        
        aliases = personObject.getAlias();        
        assertNotNull(aliases);
        assertEquals(2, aliases.size());
        alias = (AliasObject) aliases.toArray()[1];
        assertEquals("JOHN", alias.getFirstName());
        assertEquals("SMITHE", alias.getLastName());
        
        overwrites = afterEo.getSBR().getOverWrites();
        assertEquals(4, overwrites.size());        
     
        //Operation 3: add an SO
        beforeEo = afterEo;
        afterEo = (EnterpriseObject) beforeEo.copy();
        applySurvivorship(afterEo);
        personObject = new PersonObject();
        personObject.setFirstName("JON");
        personObject.setLastName("SMITH");
        SystemObject so = new SystemObject();
        so.setStatus(SystemObject.STATUS_ACTIVE);
        so.setChildType("Person");
        so.setSystemCode("SBYN");
        so.setLID("1111");
        so.setObject(personObject);
        afterEo.addSystemObject(so);
        
        policy.applyUpdatePolicy(beforeEo, afterEo);
        
        personObject = (PersonObject) afterEo.getSBR().getObject();
        aliases = personObject.getAlias();        
        assertNotNull(aliases);
        assertEquals(3, aliases.size());
        alias = (AliasObject) aliases.toArray()[2];
        assertEquals("JON", alias.getFirstName());
        assertEquals("SMITH", alias.getLastName());
        
        overwrites = afterEo.getSBR().getOverWrites();
        assertEquals(6, overwrites.size());  
        
        //Operation 4: change name of SO
        beforeEo = afterEo;
        afterEo = (EnterpriseObject) beforeEo.copy();
        applySurvivorship(afterEo);
        so = afterEo.getSystemObject("SBYN", "1111");
        personObject = (PersonObject) so.getObject();
        personObject.setLastName("SMITHE");
        
        policy.applyUpdatePolicy(beforeEo, afterEo);
        
        so = afterEo.getSystemObject("SBYN", "1111");
        personObject = (PersonObject) so.getObject();
        aliases = personObject.getAlias();        
        assertNotNull(aliases);
        assertEquals(1, aliases.size());
        alias = (AliasObject) aliases.toArray()[0];
        assertEquals("JON", alias.getFirstName());
        assertEquals("SMITH", alias.getLastName());
        
        personObject = (PersonObject) afterEo.getSBR().getObject();
        aliases = personObject.getAlias();        
        assertNotNull(aliases);
        assertEquals(4, aliases.size());
        alias = (AliasObject) aliases.toArray()[3];
        assertEquals("JON", alias.getFirstName());
        assertEquals("SMITHE", alias.getLastName());
        
        overwrites = afterEo.getSBR().getOverWrites();
        assertEquals(8, overwrites.size());  
    }    
    
    //Dummy SC - assume it wipes out all aliases and then creates them via overwrite
    private void applySurvivorship(EnterpriseObject eo) throws Exception {
        SBR sbr = eo.getSBR();
        PersonObject sbrObj = (PersonObject) sbr.getObject();
        sbrObj.removeChildren("Alias");
        // now apply the override values
        ArrayList overs = sbr.getOverWrites();
        if (overs != null) {
            for (int i = 0; i < overs.size(); i++) {
                SBROverWrite over = (SBROverWrite) overs.get(i);
                if (!over.isRemoved()) {
                    String path = over.getEPath();
                    Object data = over.getData();
                    EPathAPI.addObjectValue(EPathParser.parse(path), sbrObj, data);
                }
            }
        }    
    }
    
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(UpdatePolicyTest.class));
    }
    
}
