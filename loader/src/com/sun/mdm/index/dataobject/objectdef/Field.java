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

/**
 * 
 * This represents the fields of a ObjectDefinition like a Person can have
 * firstName, lastName
 * 
 * @author Sujit Biswas
 * 
 */
public class Field {

	/**
	 * field name
	 */
	private String name;

	/**
	 * field type
	 */
	private String type;

	/**
	 * field size
	 */
	private int size;

	/**
	 * is updateable
	 */
	private boolean updateable;

	/**
	 * is required
	 */
	private boolean required;

	/**
	 * is keyType
	 */
	private boolean keyType;
	
	/**
	 * code-module
	 */
	
	private String codeModule;

	
	
	
	
	/**
	 * @param name
	 * @param type
	 * @param size
	 * @param updateable
	 * @param required
	 * @param keyType
	 * @param codeModule
	 */
	public Field(String name, String type, int size, boolean updateable, boolean required, boolean keyType, String codeModule) {
		this.name = name;
		this.type = type;
		this.size = size;
		this.updateable = updateable;
		this.required = required;
		this.keyType = keyType;
		this.codeModule = codeModule;
	}

	/**
	 * 
	 */
	public Field() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the keyType
	 */
	public boolean isKeyType() {
		return keyType;
	}

	/**
	 * @param keyType
	 *            the keyType to set
	 */
	public void setKeyType(boolean keyType) {
		this.keyType = keyType;
	}

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param required
	 *            the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the updateable
	 */
	public boolean isUpdateable() {
		return updateable;
	}

	/**
	 * @param updateable
	 *            the updateable to set
	 */
	public void setUpdateable(boolean updateable) {
		this.updateable = updateable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "name=" + name + ",type=" + type + ",size=" + size
				+ ",keyType=" + keyType + ",required=" + required + ",codeModule=" + codeModule
				+ ",updateable=" + updateable;
		return s;
	}

	/**
	 * @return the codeModule
	 */
	public String getCodeModule() {
		return codeModule;
	}

	/**
	 * @param codeModule the codeModule to set
	 */
	public void setCodeModule(String codeModule) {
		this.codeModule = codeModule;
	}

}
