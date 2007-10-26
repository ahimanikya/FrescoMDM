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
package com.sun.mdm.index.ops;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.logging.Logger;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.*;
public class ObjectInputStreamForVersion extends ObjectInputStream {
    
        
    Logger logger = Logger.getLogger(ObjectInputStreamForVersion.class.getName());
    
    /** Constructor 
     *
     * @throws IOException if an error occurred.
     */
    protected  ObjectInputStreamForVersion() throws IOException  {
        super();        
    }

    /** Constructor 
     *
     * @param is InputStream instance.
     * @throws IOException if an error occurred.
     */
    protected  ObjectInputStreamForVersion(InputStream is) throws IOException  {
        super(is);      
    }
        
    /** Reads class descriptor.
     *
     * @throws IOException if an IOException occurred.
     * @throws ClassNotFoundException if the class cannot be found.
     * @return the local class descriptor.
     */
    protected ObjectStreamClass readClassDescriptor()  
            throws IOException, ClassNotFoundException {       
        ObjectStreamClass classDescriptor = super.readClassDescriptor();
        Class localClass = Class.forName(classDescriptor.getName(),false,this.getClass().getClassLoader());

        ObjectStreamClass localClassDescriptor = ObjectStreamClass.lookup(localClass);

        return localClassDescriptor;
    }
        
    /*
     * (non-Javadoc)
     * 
     * @see java.io.ObjectInputStream#resolveClass(java.io.ObjectStreamClass)
     */
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
            ClassNotFoundException {

        ObjectStreamClass cDesc = getObjectStreamClass(desc);

        return super.resolveClass(cDesc);
    }

    /** Gets an ObjectStreamClass.
     *
     * @param desc ObjectStreamClass to retrieve.
     * @return ObjectStreamClass.
     */
    private ObjectStreamClass getObjectStreamClass(ObjectStreamClass desc) {
        logger.info(desc.getName());
        
        if (desc.getName().endsWith("TransactionLog")) {
            return ObjectStreamClass.lookup(TransactionLog.class);
        }

        if (desc.getName().endsWith("EnterpriseObject")) {
            return ObjectStreamClass.lookup(EnterpriseObject.class);
        }

        
        if (desc.getName().endsWith("SBR")) {
            return ObjectStreamClass.lookup(SBR.class);
        }
        
        
        if (desc.getName().endsWith("SystemObject")) {
            return ObjectStreamClass.lookup(SystemObject.class);
        }
        
        if (desc.getName().endsWith("ObjectNode")) {
            return ObjectStreamClass.lookup(ObjectNode.class);
        }
        
        if (desc.getName().endsWith("Object")) {
            
            String s= desc.getName().substring(desc.getName().lastIndexOf('.') + 1);
            
            logger.info("created object class name" + s);
            Class c = null;
            try {
                c = Class.forName("com.sun.mdm.index.objects." + s);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            
            return ObjectStreamClass.lookup(c);
        } 
        
        return desc;
    }

}
