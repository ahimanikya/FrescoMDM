/**
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
package com.sun.mdm.index.dataobject.objectdef;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.exception.InvalidFieldValueException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import junit.framework.TestCase;

/**
 *
 * @author admin
 */
public class DataObjectAdapterTest extends TestCase {
    
    public DataObjectAdapterTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

   
    /**
     * Test of main method, of class DataObjectAdapter.
     */
    public void testParseFieldValue()  {
        //System.out.println("main");
        String[] args = null;
        //pos test for String datatype
        
        String input = "StringTest";
        String output = null;
        String inchar = "a";
        Character outchar = null;
        try {
            output = (String) DataObjectAdapter.parseFieldValue(input, ObjectField.OBJECTMETA_STRING_TYPE);
            assertEquals("test",DataObjectAdapter.valueAsString("test", ObjectField.OBJECTMETA_STRING_TYPE));
            assertEquals(input, output);
            output = (String) DataObjectAdapter.parseFieldValue(null, ObjectField.OBJECTMETA_STRING_TYPE);
            assertEquals(null, output);
            output = (String) DataObjectAdapter.parseFieldValue("", ObjectField.OBJECTMETA_STRING_TYPE);
            assertEquals("", output);
            Float f = (Float) DataObjectAdapter.parseFieldValue(String.valueOf(Float.MIN_VALUE), ObjectField.OBJECTMETA_FLOAT_TYPE);
            //System.out.println(DataObjectAdapter.valueAsString(f, ObjectField.OBJECTMETA_FLOAT_TYPE));
            
            Boolean b = (Boolean) DataObjectAdapter.parseFieldValue("false", ObjectField.OBJECTMETA_BOOL_TYPE);
            //System.out.println(DataObjectAdapter.valueAsString(b, ObjectField.OBJECTMETA_BOOL_TYPE));
            
            Long l = (Long) DataObjectAdapter.parseFieldValue(String.valueOf(Long.MAX_VALUE), ObjectField.OBJECTMETA_LONG_TYPE);
            //System.out.println(Long.MAX_VALUE + "," + DataObjectAdapter.valueAsString(l, ObjectField.OBJECTMETA_LONG_TYPE));
            
            Byte bte = (Byte) DataObjectAdapter.parseFieldValue(String.valueOf(Byte.MAX_VALUE), ObjectField.OBJECTMETA_BYTE_TYPE);
            //System.out.println(Byte.MAX_VALUE + "," + DataObjectAdapter.valueAsString(bte, ObjectField.OBJECTMETA_BYTE_TYPE));
            
            Integer intObj = (Integer) DataObjectAdapter.parseFieldValue(String.valueOf(Integer.MAX_VALUE), ObjectField.OBJECTMETA_INT_TYPE);
            //System.out.println(Integer.MAX_VALUE + "," + DataObjectAdapter.valueAsString(intObj, ObjectField.OBJECTMETA_INT_TYPE));
            
            Date date = new java.util.Date(System.currentTimeMillis());
            String dateValue = DateFormat.getDateInstance().format(date);
            
            if (DataObjectAdapter.getDateFormat() != null) {
            	// Test dateformat from object definition.
            	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");            
            	dateValue = dateFormat.format(date);
            }
            Date dateObj = (Date) DataObjectAdapter.parseFieldValue(dateValue, ObjectField.OBJECTMETA_DATE_TYPE);            
            //System.out.println(DateFormat.getDateInstance().format(new java.util.Date(System.currentTimeMillis())) + "," + DataObjectAdapter.valueAsString(dateObj, ObjectField.OBJECTMETA_DATE_TYPE));
            
            Date timeObj = (Date) DataObjectAdapter.parseFieldValue(DateFormat.getDateTimeInstance().format(new java.util.Date(System.currentTimeMillis())), ObjectField.OBJECTMETA_TIMESTAMP_TYPE);
            outchar = (Character) DataObjectAdapter.parseFieldValue(inchar, ObjectField.OBJECTMETA_CHAR_TYPE);
            //System.out.println("all pass");
        } catch (Exception e) {
        	e.printStackTrace();
            fail("test failed");
        }
      
   
      
        try {
            outchar = (Character)DataObjectAdapter.parseFieldValue("aaa", ObjectField.OBJECTMETA_CHAR_TYPE);
        } catch(InvalidFieldValueException e) {
        } catch(ClassCastException e) {
            fail("char neg. test failed");
        } catch(Throwable e) {
            fail("char neg. test failed");
        }
          
        
        
        // TODO review the generated test code and remove the default call to fail.
    }

}
