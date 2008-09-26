/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.query;

import junit.framework.TestCase;

/**
 * PageIteratorTest class.
 * @author cye
 */
public class PageIteratorTest extends TestCase {

    public PageIteratorTest (String name) {
        super(name);
    }
    
    public void setUp() {
    }
    
    public void test001() {
        String[] foos = new String[]{};
        PageIterator<String> fooIt = new PageIterator<String>(foos);
        assertTrue(fooIt.hasNext() == false);
        assertTrue(fooIt.size() == 0);
    }
     
    public void test002() {
       String[] foos = null;  
        PageIterator<String> fooIt = new PageIterator<String>(foos);
        assertTrue(fooIt.hasNext() == false);
        assertTrue(fooIt.size() == 0);
    }
    
    public void test003() {
        String[] foos = new String[]{"foo1", "foo2", "foo3"};
        PageIterator<String> fooIt = new PageIterator<String>(foos);
        assertTrue(fooIt.size() == 3);
        assertTrue("foo1".equals(fooIt.first()));
        assertTrue(fooIt.hasNext() == true);
        assertTrue("foo2".equals(fooIt.next()));
        assertTrue(fooIt.hasNext() == true);
        assertTrue("foo3".equals(fooIt.next()));
        assertTrue(fooIt.hasNext() == false);
        try {
            fooIt.next();
            fail();
        } catch (Exception ex) {
            assertTrue(true);
        }
    }
}
