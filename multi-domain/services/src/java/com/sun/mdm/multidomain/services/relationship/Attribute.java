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
package com.sun.mdm.multidomain.services.relationship;

/**
 * Attribute class.
 * @author cye
 */
public class Attribute {
    private String name;
    private String value;
    
    public Attribute() {
    } 
    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
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
    @Override 
    public boolean equals(Object object) {
        boolean isEqual = false;
        if(object instanceof Attribute) {
            Attribute attribute = (Attribute)object;
            if(this.name.equals(attribute.getName()) &&
               this.value.equals(attribute.getValue()))
                isEqual = true;
        }
        return isEqual;
    }    
}