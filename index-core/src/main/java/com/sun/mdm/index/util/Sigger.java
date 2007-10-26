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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.sun.mdm.index.objects.SystemObjectException;
import com.sun.mdm.index.objects.ObjectNode;


/** 
 * Signature calculator
 * calculates a signature for a given SystemObject.  Signature
 * can be calculated using either "MD5" or "SHA" algorithm.  This implementation
 * relys on Java 1.4 security features.  And requires at least the default provider, or
 * additional registered provider that supports "MD5" or "SHA" MAC algorithm
 *
 * @version 1.1
 */
public class Sigger 
    implements java.io.Serializable {

    /** Creates new Sigger */
    public Sigger() {
    }

    /**
     * Traverse the ObjectNode
     * @param obj the ObjectNode to traverse
     * @param md the MessageDigest to use
     * @throws SystemObjectException if traversing the ObjectNode failed
     */
    protected void traverse(ObjectNode obj, MessageDigest md)
            throws SystemObjectException {
/*        
        AttributeMetaData meta = obj.getMetaData();
        TreeMap fields = new TreeMap();
        
        // iterate through the meta data
        Collection names = meta.getFieldNames();
        Iterator iter = names.iterator();
        while( iter.hasNext() )
        {
            // check if field is match type
            String name = (String)iter.next();
            if( meta.getMatch( name ) )
            {
//                try
//                {
                    Object o = obj.getField( name );
                    // put into Map with value, sorted by field name
                    fields.put( name, o );
//                }
//                catch( Exception ex )
//                {
                    // do nothing, ignore
//                }
            }
        }
        // now iterate through the list of sorted values, and concatenate 
        // them together as string
        Collection values = fields.values();
        // assert values != null
        if( values == null )
        {
            return;
        }
        iter = values.iterator();
        StringBuffer buf = new StringBuffer(50);
        while( iter.hasNext() )
        {
           
            Object o = iter.next();
            if( o != null )
            {
                String s = o.toString();
                buf.append( s );
            }
        }
        String message = buf.toString();
        md.update( message.getBytes() );
        
        // now call traverse on each of the secondary objects
        ArrayList secondaryNames = meta.getSecondaryObjectNames();
        Collections.sort( secondaryNames );
        int size = secondaryNames.size();
        for( int i = 0; i < size; i++ )
        {
            String s = (String)secondaryNames.get( i );
            
            Collection c = obj.getSecondaryObject( s );
            // consider sorting this collection
            Iterator secondIter = c.iterator();
            while( secondIter.hasNext() )
            {
                ObjectNode tempEntity = (ObjectNode)secondIter.next();
                traverse( tempEntity, md );
            }
        }
*/
    }
    
    
    
    /** 
     * calculates the checksum for match fields.
     * concatenates each match field value in sorted order by field name
     *
     * @param algo MD5 or SHA
     * @param omega SBR object
     * @return a byte array containing the checksum
     * @throws SystemObjectException if accessing the ObjectNode failed
     */    
    protected byte[] calculateSignature(String algo, ObjectNode omega)
            throws SystemObjectException {
        byte[] result;

        try {
            MessageDigest md = MessageDigest.getInstance(algo);
            traverse(omega, md);
            result = md.digest();
        } catch (NoSuchAlgorithmException nsaex) {
            result = null;
        }
        
        return result;
        
    }
       
    /** 
     * calculates the signature using MD5 MAC algorithm
     * @param omega input object
     * @return byte array containing the signature
     * @throws SystemObjectException if accessing the ObjectNode failed
     */    
    public byte[] calculateSignatureMD5(ObjectNode omega) 
            throws SystemObjectException {
        return calculateSignature("MD5", omega);
    }
    
    /** calculates the signature using SHA MAC algorithm
     * @param omega input object
     * @return byte array containing the signature
     * @throws SystemObjectException if accessing the ObjectNode failed
     */    
    public byte[] calculateSignatureSHA(ObjectNode omega)
            throws SystemObjectException {
        return calculateSignature("SHA", omega);
    }
    
}
