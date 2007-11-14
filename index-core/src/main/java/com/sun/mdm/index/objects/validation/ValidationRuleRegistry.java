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
package com.sun.mdm.index.objects.validation;

import java.util.Hashtable;
import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.configurator.impl.validation.ValidationConfiguration;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Localizer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * @author jwu
 */
public class ValidationRuleRegistry {

    private final String sRegisterObjectClass 
        = "com.sun.mdm.index.objects.validation.RegisterObjectDefinitions";
    private static ValidationRuleRegistry mThisRegistry = null;
    private Hashtable hDefaultValidator = null;
    private Hashtable hCustomObjectValidator = null;
    private Hashtable hCustomRuleValidator = null;

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    
    /**
     * Creates a new instance of ValidationRuleRegistry
     */
    private ValidationRuleRegistry() {
        init();
    }

    /**
     * Get an instance of ValidationRuleRegistry
     *
     * @return an instance ValidationRuleRegistry
     */
    public static synchronized ValidationRuleRegistry getInstance() {

        if (mThisRegistry == null) {
            mThisRegistry = new ValidationRuleRegistry();
        }
        return mThisRegistry;
    }

    /**
     * Get the Validator for the specified object
     *
     * @param objectName object name
     * @return an ObjectDescriptor
     */
    public ObjectValidator getCustomValidatorByObject(String objectName) {
        ObjectValidator validator
                 = (ObjectValidator) hCustomObjectValidator.get(objectName.toUpperCase());

        return validator;
    }

    /**
     * Get the ObjectValidator for the specified rule
     *
     * @param ruleName rule name
     * @return an ObjectValidator
     */
    public ObjectValidator getCustomValidatorByRule(String ruleName) {
        ObjectValidator validator
                 = (ObjectValidator) hCustomRuleValidator.get(ruleName.toUpperCase());

        return validator;
    }

    /**
     * Get the ObjectDescriptor for the specified object name
     *
     * @param objectName object name
     * @return an ObjectDescriptor
     */
    public ObjectDescriptor getObjectDescriptor(String objectName) {
        ObjectDescriptor objDesc = (ObjectDescriptor) hDefaultValidator.get(objectName);
        return objDesc;
    }

    /**
     * @param objectName object name
     * @param objDesc ObjectDescriptor
     * @todo Document this method
     */
    synchronized public void putObjectDescriptor(String objectName, ObjectDescriptor objDesc) {
        if (mThisRegistry == null) {
            mThisRegistry = new ValidationRuleRegistry();
        }
        if (objectName == null) {
            throw new RuntimeException(mLocalizer.t("OBJ743: Cannot register an " + 
                                        "object descriptor with a null object name."));
        }
        if (objDesc == null) {
            throw new RuntimeException(mLocalizer.t("OBJ744: Cannot register a " + 
                                        "null object descriptor."));
        }
        hDefaultValidator.put(objectName, objDesc);
    }

    private void displayAllRules() {

        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Validation Registry Rule  : " + hCustomRuleValidator.toString());
            mLogger.fine("Validation Registry Object: " + hCustomObjectValidator.toString());
        }
    }

    private void init() {

        Class registerClass;
        try {
            registerClass = Class.forName(sRegisterObjectClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(mLocalizer.t("OBJ745: Could not initialize " + 
                                        "ValidationRuleRegistry: {0}", e));
        } 
        
        Object registerInstance;
        try {
            registerInstance = registerClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(mLocalizer.t("OBJ746: Could not initialize " + 
                                        "ValidationRuleRegistry: {0}", e));
        } 
        
        Method methedToInvoke;
        try {
            Class[] parameterTypes = null;
            methedToInvoke = registerClass.getMethod("registerObjects", parameterTypes);
        } catch (Exception e) {
            throw new RuntimeException(mLocalizer.t("OBJ747: Could not initialize " + 
                                        "ValidationRuleRegistry: {0}", e));
        }
        
        ArrayList list;
        try {
            Object[] parms = null;
            list = (ArrayList) methedToInvoke.invoke(registerInstance, parms);
        } catch (Exception e) {
            throw new RuntimeException(mLocalizer.t("OBJ748: Could not initialize " + 
                                        "ValidationRuleRegistry: {0}", e));
        }
        
        hDefaultValidator = new Hashtable();
        for (int i = 0; i < list.size(); i++) {
            ObjectDescriptor descriptor = (ObjectDescriptor) list.get(i);
            hDefaultValidator.put(descriptor.getObjectName(), descriptor);
        }

        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Generated validation rules:\n" + LogUtil.mapToString(hDefaultValidator));
        }
                
        ConfigurationService cfgService = null;
        try {
            cfgService = ConfigurationService.getInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(mLocalizer.t("OBJ749: Could not initialize " + 
                                        "ValidationRuleRegistry: {0}", e));
        }

        ValidationConfiguration cfg
                 = (ValidationConfiguration) cfgService.getConfiguration("Validation");
        hCustomObjectValidator = cfg.getValidatorsByObject();
        hCustomRuleValidator = cfg.getValidatorsByRule();
    }
}
