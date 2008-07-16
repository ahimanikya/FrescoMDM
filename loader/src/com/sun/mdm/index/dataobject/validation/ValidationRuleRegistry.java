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
package com.sun.mdm.index.dataobject.validation;

import java.util.Hashtable;

import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.loader.util.Localizer;
import com.sun.mdm.index.objects.validation.ObjectDescriptor;
import com.sun.mdm.index.objects.validation.ObjectValidator;

/**
 * This class is ValidationRuleRegistry caching configued validation rules.
 * @author cye
 */
public class ValidationRuleRegistry {
	private static Logger logger = Logger.getLogger("com.sun.mdm.index.dataobject.validation.ValidationRuleRegistry");
	private static Localizer localizer = Localizer.getInstance();

	private static ValidationRuleRegistry instance;
	
	private Hashtable<String, ObjectDescriptor> objectDescriptors;
	private Hashtable<String, ObjectValidator> objectValidators;
	
	/**
	 * Protected Constructor for singleton class
	 */
    protected ValidationRuleRegistry() {
    	initialize();
    }
    
    /**
     * Initialize ValidationRuleRegistry
     */
    private void initialize() {
    	objectDescriptors = new Hashtable<String, ObjectDescriptor>();
    	objectValidators = new Hashtable<String, ObjectValidator>();    	
    }
    
    /**
     * Gets an instance of ValidationRuleRegistry 
     * @return ValidationRuleRegistry
     */
    synchronized public static ValidationRuleRegistry getInstance() {    	
   		if (instance == null) {
   			instance = new ValidationRuleRegistry();
   		}
    	return instance;
    }
        
    /**
     * Gets ObjectValidator by the given object name.
     * @param objectName
     * @return ObjectValidator
     */
    public ObjectValidator getObjectValidator(String objectName) {
    	return (ObjectValidator)objectValidators.get(objectName);
    }
    
    /**
     * Gets ObjectDescriptor by the given object name.
     * @param objectName
     * @return ObjectDescriptor
     */
    public ObjectDescriptor getObjectDescriptor(String objectName) {
    	return (ObjectDescriptor)objectDescriptors.get(objectName);
    }

    /**
     * Adds ObjectValidator for the given object name.
     * @param objectName
     * @param objectValidator
     */
    public void putObjectValidator(String objectName, ObjectValidator objectValidator) {
    	objectValidators.put(objectName, objectValidator);
    }
    
    /**
     * Adds ObjectDescriptor for the given object name. 
     * @param objectName
     * @param objectDescriptor
     */
    public void putObjectDescriptor(String objectName, ObjectDescriptor objectDescriptor) {
    	objectDescriptors.put(objectName, objectDescriptor);
    }    
}
