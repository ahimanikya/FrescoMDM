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
package com.sun.mdm.index.util;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.OptionalDataException;

/** 
 * Utility methods to serialize/de-serialize an object to/from a byte array
 *
 * @author ckuo
 * @version $Revision: 1.1 $
 */
public class Serializer {

    /** Creates new Serializer */
    public Serializer() {
    }

    /** 
     * Serialize an object to a byte array
     * @param obj object to be serialized
     * @return a byte array representing the serialized object,
     * null if errors occurred during the process
     */    
    public static byte[] objectToBytes(java.lang.Object obj) {
        byte[] bArray = null;

        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            
            ObjectOutputStream p = new ObjectOutputStream(bout);
            
            p.writeObject(obj);
            p.flush();
            
            bout.flush();
            
            bArray = bout.toByteArray();
        } catch (IOException ioex) {
            // TODO: log
            // then ignore
            ;
        }
        
        return bArray;
    }
    
    /** 
     * de-serialize an object from a byte array
     * @param bArray byte array representation of the object
     * @return an object de-serialized from the byte array,
     * null if errors occurred during the process
     * @throws ClassNotFoundException Class of a serialized object cannot be found
     * @throws OptionalDataException Primitive data was found in the 
     * stream instead of objects.
     * @throws IOException failed or interrupted io operation
     */    
    public static Object bytesToObject(byte[] bArray)
        throws ClassNotFoundException, OptionalDataException, IOException {
        Object o = null;
        
        ByteArrayInputStream bin = new ByteArrayInputStream(bArray);
            
        ObjectInputStream p = new ObjectInputStream(bin);
           
        o = p.readObject();
         
        return o;
    }
    
    /** 
     * for testing purpose only
     * @param argv comman line arguments. This main does not take any.  
     */    
    public static void main(String[] argv) {
        try {
            java.util.ArrayList a = new java.util.ArrayList();
            
            a.add(new Integer(5));
            a.add(new String("test"));
            double d = 1.23543;
            a.add(new Double(d));
            
            byte[] bArray = Serializer.objectToBytes(a);
            
            Object o = Serializer.bytesToObject(bArray);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
}
