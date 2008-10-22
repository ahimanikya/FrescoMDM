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
package com.sun.mdm.multidomain.services.model;

/**
 * Field class.
 * @author cye
 */
public class Field {
    
    private String name;
    private String value;
    
    private String type;
    private int size;
    private boolean updateable;
    private boolean required;
    private boolean keyType;
    private String codeModule;
    
    public Field() {
    } 
    public Field(String name, String value) {
        this.name = name;
        this.value = value;
    }  
    public Field(String name, String type, int size, boolean updateable, boolean required, boolean keyType, String codeModule) {
        this.name = name;
	this.type = type;
	this.size = size;
	this.updateable = updateable;
	this.required = required;
	this.keyType = keyType;
	this.codeModule = codeModule;
    }    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
     public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }   
    public String getType() {
        return type;
    }
    public void setType(String type) {
	this.type = type;
    }
    public boolean isKeyType() {
	return keyType;
    }
    public void setKeyType(boolean keyType) {
	this.keyType = keyType;
    }
    public boolean isRequired() {
	return required;
    }
    public void setRequired(boolean required) {
	this.required = required;
    }
    public int getSize() {
    	return size;
    }
    public void setSize(int size) {
	this.size = size;
    }
    public boolean isUpdateable() {
	return updateable;
    }
    public void setUpdateable(boolean updateable) {
	this.updateable = updateable;
    }
    public String getCodeModule() {
	return codeModule;
    }
    public void setCodeModule(String codeModule) {
	this.codeModule = codeModule;
    }
    @Override
    public String toString() {
        String s = "name=" + name + ",type=" + type + ",size=" + size
                    + ",keyType=" + keyType + ",required=" + required + ",codeModule=" + codeModule
                    + ",updateable=" + updateable +",value=" + value;
	return s;
    }    
    @Override 
    public boolean equals(Object object) {
        boolean isEqual = false;
        if(object instanceof Field) {
            Field field = (Field)object;
            if(this.name.equals(field.getName()) &&
               this.value.equals(field.getValue()))
                isEqual = true;
        }
        return isEqual;
    }
}
