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
package com.sun.mdm.index.objects;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Test class for ObjectNodeComparator
 * @author  dcidon
 */
public class ObjectNodeComparatorTest extends TestCase {
    
    /** Creates a new instance of ObjectNodeComparatorTest 
     * @param name test name
     */
    public ObjectNodeComparatorTest(String name) {
        super(name);
    }
    
    /** Test sorting
     * @throws Exception exception
     */
    public void testSort() throws Exception {
        TransactionObject[] mTrans = new TransactionObject[8];
        mTrans[0] = new TransactionObject();
        mTrans[1] = new TransactionObject();
        mTrans[2] = new TransactionObject();
        mTrans[3] = new TransactionObject();
        mTrans[4] = new TransactionObject();
        mTrans[5] = new TransactionObject();
        mTrans[6] = new TransactionObject();
        mTrans[7] = new TransactionObject();
        mTrans[0].setEUID("1");
        mTrans[1].setEUID("10");
        mTrans[2].setEUID("2");
        mTrans[3].setEUID("30");
        mTrans[5].setEUID("20");
        mTrans[7].setEUID("3");
        //Note EUID left null for 4 and 6 on purpose to test null handling
        Comparator c = new ObjectNodeComparator("EUID", false);
        Arrays.sort(mTrans, c);
        assertTrue(mTrans[0].getEUID().equals("1"));
        assertTrue(mTrans[1].getEUID().equals("2"));
        assertTrue(mTrans[2].getEUID().equals("3"));
        assertTrue(mTrans[3].getEUID().equals("10"));
        assertTrue(mTrans[4].getEUID().equals("20"));
        assertTrue(mTrans[5].getEUID().equals("30"));
        assertTrue(mTrans[6].getEUID() == null);
        assertTrue(mTrans[7].getEUID() == null);
    }
    
    /** Main entry point
     * @param args args
     */
     
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(ObjectNodeComparatorTest.class));
    }    
    
    
}
