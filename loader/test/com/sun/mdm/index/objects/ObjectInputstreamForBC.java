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

package com.sun.mdm.index.objects;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

import com.sun.mdm.index.objects.ObjectNode;

/**
 * @author Sujit Biswas
 *
 */
public class ObjectInputstreamForBC extends ObjectInputStream {

	

	/**
	 * @param in
	 * @throws IOException
	 */
	public ObjectInputstreamForBC(InputStream in) throws IOException {
		super(in);
		// TODO Auto-generated constructor stub
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

	private ObjectStreamClass getObjectStreamClass(ObjectStreamClass desc) {

		
		
		if (desc.getName().endsWith("SerializedObject")) {
			return ObjectStreamClass.lookup(DeSerializedObject.class);
		}

		
		if (desc.getName().endsWith("S_ObjectNode")) {
			return ObjectStreamClass.lookup(D_ObjectNode.class);
		}

		if (desc.getName().endsWith("com.stc.eindex.objects.ObjectNode")) {
			return ObjectStreamClass.lookup(ObjectNode.class);
		}
		
		return desc;
	}
	
	protected ObjectStreamClass readClassDescriptor() throws IOException,
			ClassNotFoundException {
		ObjectStreamClass classDescriptor = super.readClassDescriptor();

		//logger.info(classDescriptor.getName());

		Class localClass = Class.forName(getObjectStreamClass(classDescriptor).getName(), false, this
				.getClass().getClassLoader());

		ObjectStreamClass localClassDescriptor = ObjectStreamClass
				.lookup(localClass);
		
		if(!generatedClass(classDescriptor)){
			//return classDescriptor;
		}

		return localClassDescriptor;
	}

	private boolean generatedClass(ObjectStreamClass classDescriptor) {
		if (classDescriptor.getName().endsWith("SerializedObject")) {
			return true;
		}
		
		if (classDescriptor.getName().endsWith("S_ObjectNode")) {
			return true;
		}
		
		return false;
	}


}
