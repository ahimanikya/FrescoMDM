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

import java.util.ArrayList;

import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.exception.ObjectException;

/**
 * @author Sujit Biswas
 *
 */
public class D_ObjectNode extends ObjectNode {
	
	private String s = "good morning";
	
	
	static ArrayList names = new ArrayList();
	static ArrayList types = new ArrayList();
	static ArrayList values = new ArrayList();
	
	
	static{
		for (int i = 0; i < 3; i++) {
			names.add(i, "K" + i);
			types.add(i, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
			values.add(i, "V" + i);
		}
	}
	
	/**
	 * @throws ObjectException 
	 * 
	 */
	public D_ObjectNode() throws ObjectException {
		
		super("RootObject", names, types, values);
		
	}

	/**
	 * @return the s
	 */
	public String getS() {
		
		
		return s;
	}

	/**
	 * @param s the s to set
	 */
	public void setS(String s) {
		this.s = s;
	}

	/* (non-Javadoc)
	 * @see com.sun.eview.objects.ObjectNode#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	
}
