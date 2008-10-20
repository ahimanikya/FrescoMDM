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
package com.sun.mdm.multidomain.services.core.context;

import java.io.Serializable;

/**
 * JndiResource class.
 * @author cye
 */
public class JndiResource implements Serializable{
    
    private String id; //id
    private String name; //jndi-name
    private String type; //res-type
    private String description; //description
    
    /**
     * Create an instance of JndiResource.
     */
    public JndiResource(){        
    }
    
    /**
     * Create an instance of JndiResource.
     * @param id Jndi id.
     * @param name Jndi name.
     * @param type Jndi resource type.
     * @param description Jndi resource description.
     */
    public JndiResource(String id, String name, String type, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
    }
    
    /**
     * Set id attribute.
     * @param id Id.
     */
    public void setId(String id) {        
        this.id = id;
    }
    
    /**
     * Get id attribute.
     * @return String Id.
     */
    public String getId() {
        return id;
    }
    
    /**
     * Get name attribute.
     * @param name Name.
     */
    public void setName(String name) {        
        this.name = name;
    }
    
    /**
     * Get name attribute.
     * @return String Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set type attribute.
     * @param type Type.
     */
    public void setType(String type) {        
        this.type = type;
    }
    
    /**
     * Get type attribute.
     * @return String Type.
     */
    public String getType() {
        return type;
    }

    /**
     * Set description attribute.
     * @param description Description.
     */
    public void setDescription(String description) {        
        this.description = description;
    }
    
    /**
     * Get description attribute.
     * @return String description.
     */
    public String getDescription() {
        return description;
    }
}
