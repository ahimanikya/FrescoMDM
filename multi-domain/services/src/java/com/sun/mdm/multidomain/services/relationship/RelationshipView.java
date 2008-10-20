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
 * RelationshipView class.
 * @author cye
 */
public class RelationshipView {
    
    private String sourceDomain;
    
    private String targetDomain;
    
    private String name;
     
    private String sourceEUID;
    
    private String targetEUID;
    
    private String id;
    /* source record highLight */
    private String sourceHighLight;
    /* target record highLight */
    private String targetHighLight;
    
    public RelationshipView(){
    }
    public String getSourceDomain() {
        return sourceDomain;
    }
    public void setSourceDomain(String sourceDomain){
        this.sourceDomain = sourceDomain;
    }
    public String getTargetDomain() {
        return targetDomain;
    }
    public void setTargetDomain(String targetDomain){
        this.targetDomain = targetDomain;
    }   
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }      
    public String getTargetEUID() {
        return targetEUID;
    }
    public void setTargetEUID(String targetEUID){
        this.targetEUID = targetEUID;
    }   
   public String getSourceEUID() {
        return sourceEUID;
    }
    public void setSourceEUID(String sourceEUID){
        this.sourceEUID = sourceEUID;
    }       
    public String getId() {
        return id;
    }
    public void setId(String id){
        this.id = id;
    }      
    public String getSourceHighLight() {
        return sourceHighLight;
    }
    public void setSourceHighLight(String sourceHighLight){
        this.sourceHighLight = sourceHighLight;
    }           
    public String getTargetHighLight() {
        return targetHighLight;
    }
    public void setTargetHighLight(String targetHighLight){
        this.targetHighLight = targetHighLight;
    }        
}
