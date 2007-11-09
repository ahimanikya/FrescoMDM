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

import java.util.Iterator;
import java.util.logging.Level;
import net.java.hulp.i18n.Logger;

/**
 *
 * @author  kkao
 */
public class ObjectKeyType implements Comparable, java.io.Serializable {
    
    // logger
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    
        String mType;
        ObjectKey mKey;
        boolean mRemoved = false;
        
        ObjectKeyType(String type, ObjectKey key, boolean removed ) {
            mType = type;
            mKey = key;
            mRemoved = removed;
        }

        public int hashCode() {
            int hRet = 0;
            
            String buf = toString() ;

            hRet = buf.hashCode();
            return hRet;
            
        }
        
        public boolean equals(ObjectKeyType keyType) {
        	return (this.hashCode() == keyType.hashCode());
        } 
        
        public int compareTo( Object object) {
            if (mLogger.isLoggable(Level.FINE)) {
            	mLogger.fine("Comparing ObjectKeyType value: " + this.toString());
            	mLogger.fine("to: " + object.toString());
            }
           
           return (this.toString()).compareTo(object.toString());	        	
        }
        
        public String toString() { 
        	return mType + "|" + mRemoved + "|" + mKey.getKeyNames().toString() + " | " + mKey.getKeyTypes().toString() + mKey.getKeyValues().toString();
        }
}
