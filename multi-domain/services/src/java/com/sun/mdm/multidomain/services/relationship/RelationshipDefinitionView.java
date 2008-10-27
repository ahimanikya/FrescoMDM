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
 * RelationshipDefinitionView class.
 * @author cye
 */
public class RelationshipDefinitionView {
    private String name;
    private String id; 
    private String sourceDomain;
    private String targetDomain;
    private boolean biDirection;  
    
    public RelationshipDefinitionView(){        
    }
    public RelationshipDefinitionView(String name, String id, String sourceDomain, String targetDomain, boolean biDirection){        
        this.name = name;
        this.id = id; 
        this.sourceDomain = sourceDomain;
        this.targetDomain = targetDomain;
        this.biDirection = biDirection;          
    }    
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;;
    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getSourceDomain(){
        return sourceDomain;
    }
    public void setSourceDomain(String sourceDomain){
        this.sourceDomain = sourceDomain;
    }
    public String getTargetDomain(){
        return targetDomain;
    }
    public void setTargetDomain(String targetDomain){
        this.targetDomain = targetDomain;
    }
    public boolean getBiDirection(){
        return biDirection;
    }
    public void setBiDirection(boolean biDirection){
        this.biDirection = biDirection;
    }        
}
