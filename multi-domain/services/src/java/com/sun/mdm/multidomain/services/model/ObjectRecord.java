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

import java.util.List;

import com.sun.mdm.multidomain.services.model.AttributeList;
import com.sun.mdm.multidomain.services.model.Attribute;

/**
 * ObjectRecord class.
 * @author cye
 */
public class ObjectRecord extends AttributeList {

    private String name;
    private String EUID;
              
    /**
     * Create an instance of ObjectRecord.
     */
    public ObjectRecord(){
    } 
    
    /**
     * Create an instance of ObjectRecord.
     * @param name Object name.
     * @param euid Object EUID.
     */
    public ObjectRecord(String name, String euid){
        this.name = name;
        this.EUID = euid;
    }
    
    /**
     * Create an instance of ObjectRecord.
     * @param name Object name.
     * @param euid Object EUID.
     * @param attributes List of Attribute.
     */
    public ObjectRecord(String name, String euid, List<Attribute> attributes){
        super(attributes);
        this.name = name;
        this.EUID = euid;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setEUID(String euid) {
        this.EUID = euid;
    }
    
    public String getEUID() {
        return EUID;
    }         
}
