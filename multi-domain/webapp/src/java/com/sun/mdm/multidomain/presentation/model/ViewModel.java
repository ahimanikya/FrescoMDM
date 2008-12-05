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
package com.sun.mdm.multidomain.presentation.model;

import java.util.Map;
import java.util.HashMap;

/**
 * ViewModel class.
 * @author cye
 */
public class ViewModel<E> {
    private String modelName;
    private ModelObject<E>  modelObject;
    
    public ViewModel(){
    }
    
    public ViewModel(String modelName,ModelObject<E> modelObject){
        this.modelName = modelName;
        this.modelObject =  modelObject;
    }
    
    public String getModelName(){
        return modelName;
    }
    
    public void setModelName(String modelName){
        this.modelName = modelName;
    }
   
    public ModelObject<E> getModelObject(){
        return modelObject;
    }
    
    public void setModelObject(ModelObject<E> modelObject){
        this.modelObject = modelObject;
    }   
    
    public static class ModelObject<E> {
        private Map<String, E> objects;
        
        public ModelObject(){
            objects = new HashMap<String, E>();
        }
        
        public void setObject(String name, E value) {
            objects.put(name, value);
        }
        
        public E getObject(String name) {
            return objects.get(name);
        }
        
        public Map<String, E> getObjects() {
            return objects;
        } 
        
        public void setObjects(Map<String, E> objects) {
            this.objects = objects;
        }
    }
}
