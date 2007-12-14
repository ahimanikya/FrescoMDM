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
public class S_ObjectNode extends ObjectNode{

	
	private String s = "hello";
	
	static ArrayList names = new ArrayList();
	static ArrayList types = new ArrayList();
	static ArrayList values = new ArrayList();
	
	
	static{
		for (int i = 0; i < 30; i++) {
			names.add(i, "Key" + i);
			types.add(i, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
			values.add(i, "Value" + i);
		}
	}
	
	/**
	 * @throws ObjectException 
	 * 
	 */
	public S_ObjectNode() throws ObjectException {
		
		super("RootObject", names, types, values);
		
		addChild(this.copy());
		
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
}
