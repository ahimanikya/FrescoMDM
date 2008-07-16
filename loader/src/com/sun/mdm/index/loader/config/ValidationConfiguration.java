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
package com.sun.mdm.index.loader.config;

import java.util.ArrayList;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import net.java.hulp.i18n.Logger;

import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.sun.mdm.index.dataobject.validation.ValidationRuleRegistry;
import com.sun.mdm.index.dataobject.validation.LocalIdValidator;
import com.sun.mdm.index.loader.util.Localizer;
import com.sun.mdm.index.objects.validation.ObjectValidator;
import com.sun.mdm.index.objects.validation.ObjectDescriptor;
import com.sun.mdm.index.dataobject.validation.ObjectDescriptorFileReader;
import com.sun.mdm.index.objects.validation.exception.ValidationException;

public class ValidationConfiguration {
	private static Logger logger = Logger.getLogger("com.sun.mdm.index.loader.config.ValidationConfiguration");
	private static Localizer localizer = Localizer.getInstance();

	static final String VALIDATION_CONFIGURATION_TAG = "ValidationConfig";
	static final String VALIDATION_CONFIGURATION_NAME = "Validation";
	static final String VALIDATION_CONFIGURATION_CLASS = "com.sun.mdm.index.dataobject.validation.ValidationConfiguration";
	static final String LOCALID_VALIDATOR_CLASS = "com.sun.mdm.index.dataobject.validation.LocalIdValidator";
	static final String OBJECT_DESCRIPTOR_READER_CLASS = "com.sun.mdm.index.dataobject.validation.ObjectDescriptorFileReader";	
	static final String ATTRIBUTE_NAME = "name";
	static final String ATTRIBUTE_CLASS = "class";
	static final String ATTRIBUTE_VALIDATE = "validate";	
	static final String ATTRIBUTE_REFERENCE = "reference";		
	static final String ATTRIBUTE_SYSTEM = "system";
	public static final String DEFAULT_SYSTEM_CODE = "*";	
	static final String ATTRIBUTE_LENGTH = "length";	
	static final String ATTRIBUTE_FORMAT = "format";		
    static final String RULES_TAG = "rules";
    static final String RULE_TAG = "rule";
	static final String VALIDATE_LOCAL_ID = "validate-local-id";
	static final String VALIDATE_OBJECT_VALUE = "validate-object-value";

    private static ValidationConfiguration instance;  
        
    private boolean validationEnabled = false;
    private boolean referenceValidationEnabled = false;
    
    protected ValidationConfiguration() {    	
    }

    synchronized public static ValidationConfiguration getInstance() {    	
 		if (instance == null) {
  			instance = new ValidationConfiguration();
   		}
    	return instance;
    }    

    public void setValidationEnabled(boolean validationEnabled) {
    	this.validationEnabled = validationEnabled;
    }
    
    public boolean getValidationEnabled() {
    	return this.validationEnabled;
    }

    public boolean getReferenceValidationEnabled() {
    	return this.referenceValidationEnabled;
    }
    
    public void parse(Node validationNode) 
    	throws ConfigException {

    	if (validationNode == null) {
    		return;
    	}
    
    	if (!VALIDATION_CONFIGURATION_TAG.equals(validationNode.getNodeName())) {
    		throw new ConfigException(validationNode == null ? "null" : validationNode.getNodeName() + 
    							      " is invalid node of validation configuration.");
    	}
    	
    	NamedNodeMap attributes = validationNode.getAttributes();
    	if (attributes == null || 
    		attributes.getLength() != 4) {
    		throw new ConfigException(attributes == null ? "0" : attributes.getLength() + 
    							      " is incorrect number of attributes of validation configuration.");
    		
    	}
    	
    	Node attribute = attributes.getNamedItem(ATTRIBUTE_NAME);
    	if (attribute == null ||
    		!VALIDATION_CONFIGURATION_NAME.equals(attribute.getNodeValue())) {
    		throw new ConfigException(attributes == null ? "null" : attribute.getNodeValue() +
    								  " is invalid value for attribute " + ATTRIBUTE_NAME + 
    								  " of  attributes of validation configuration.");
    	}
    	attribute = attributes.getNamedItem(ATTRIBUTE_CLASS);
    	if (attribute == null ||
    		!VALIDATION_CONFIGURATION_CLASS.equals(attribute.getNodeValue())) {
    		throw new ConfigException(attributes == null ? "null" : attribute.getNodeValue() +
					  " is invalid value for attribute " + ATTRIBUTE_CLASS + 
					  " of  attributes of validation configuration.");
    		
    	}
    	attribute = attributes.getNamedItem(ATTRIBUTE_VALIDATE);
    	if (attribute == null) {
    		throw new ConfigException("The attribute " + 
    								  ATTRIBUTE_VALIDATE + 
    								  " is not defined for" +
    								  " validation configuration.");    		
    	}
    	validationEnabled = Boolean.parseBoolean(attribute.getNodeValue());
    	
    	
    	attribute = attributes.getNamedItem(ATTRIBUTE_REFERENCE);
    	if (attribute != null) {
    		referenceValidationEnabled = Boolean.parseBoolean(attribute.getNodeValue());
    	}
    	if (validationEnabled) {    	
    		XPath xpath = XPathFactory.newInstance().newXPath();		
    		try {
    			NodeList rules = (NodeList)xpath.evaluate("//ValidationConfig/rules/rule", validationNode, XPathConstants.NODESET);
    			for (int i = 0; i < rules.getLength(); i++) {
    				Node rule = (Node) rules.item(i);
    				NamedNodeMap ruleAttributes = rule.getAttributes();
    				Node nameAttribute = ruleAttributes.getNamedItem(ATTRIBUTE_NAME);
    				Node classAttribute = ruleAttributes.getNamedItem(ATTRIBUTE_CLASS);
    				if (nameAttribute.getNodeValue()!= null &&
    						nameAttribute.getNodeValue().equals(VALIDATE_LOCAL_ID)) {
					
    					Node systemAttribute = ruleAttributes.getNamedItem(ATTRIBUTE_SYSTEM);
    					Node lengthAttribute = ruleAttributes.getNamedItem(ATTRIBUTE_LENGTH);
    					Node formatAttribute = ruleAttributes.getNamedItem(ATTRIBUTE_FORMAT);
					 
    					initLocalIdValidator(classAttribute.getNodeValue(),
    							systemAttribute, lengthAttribute, formatAttribute);
					
    				} else if (nameAttribute.getNodeValue()!= null &&
    						   nameAttribute.getNodeValue().equals(VALIDATE_OBJECT_VALUE)) {
    					initObjectValidator(classAttribute.getNodeValue());					
    				}				
    			}			
    		} catch(XPathExpressionException xex) {
    			throw new ConfigException(xex);
    		}
    	}
    }    
    
    public void initLocalIdValidator(String className, Node systemAttribute, Node lengthAttribute, Node formatAttribute) 
    	throws ConfigException {
    	
    	ValidationRuleRegistry registry = ValidationRuleRegistry.getInstance();    	
    	try {
    		ObjectValidator objectValidator = registry.getObjectValidator("SystemObject");
    		Class classLocalIdValidator = Class.forName(className);
    		if (objectValidator == null) {    		    			
    			objectValidator = (ObjectValidator)classLocalIdValidator.newInstance();    		
    			registry.putObjectValidator("SystemObject", objectValidator);
    		}
    		if (className.equals(LOCALID_VALIDATOR_CLASS)) {        			
    			LocalIdValidator localIdValidator = (LocalIdValidator)objectValidator;   
    			
    			if (systemAttribute == null || systemAttribute.equals("")) {
    				localIdValidator.add(ValidationConfiguration.DEFAULT_SYSTEM_CODE, 
    								     Integer.parseInt(lengthAttribute.getNodeValue()), 
    								     formatAttribute.getNodeValue());
    			} else {
    				localIdValidator.add(systemAttribute.getNodeValue(), 
							 			 Integer.parseInt(lengthAttribute.getNodeValue()),
							 		     formatAttribute.getNodeValue());    				
    			}
    		}
    	} catch(ClassNotFoundException cex) {
    		throw new ConfigException(cex);
    	} catch(InstantiationException iex) {
    		throw new ConfigException(iex);    		
    	} catch(IllegalAccessException iex) {
    		throw new ConfigException(iex);    		
    	}
    }
    
    public void initObjectValidator(String className) 
    	throws ConfigException {
    	
    	ValidationRuleRegistry registry = ValidationRuleRegistry.getInstance();
    	try {
    		Class classObjectDescriptorReader = Class.forName(className);    		
    		Object objectDescriptorReader = classObjectDescriptorReader.newInstance();
    		if (className.equals(OBJECT_DESCRIPTOR_READER_CLASS)) {
    			ObjectDescriptorFileReader reader = (ObjectDescriptorFileReader)objectDescriptorReader;
    			InputStream is = getClass().getClassLoader().getResourceAsStream("object.xml");
    			reader.parse(is);
    			reader.create(referenceValidationEnabled);
    		}    		    		
    		Method getObjectDescriptors = classObjectDescriptorReader.getMethod("getObjectDescriptors",new Class[]{}); 
    		Object object = getObjectDescriptors.invoke(objectDescriptorReader, new Object[]{});  
    		ArrayList<ObjectDescriptor> objectDescriptors = (ArrayList<ObjectDescriptor>)object;      		
    		for (ObjectDescriptor objectDescriptor :objectDescriptors ) {
    			registry.putObjectDescriptor(objectDescriptor.getObjectName(), objectDescriptor);
    		} 
    	} catch(ValidationException vex) {
    		throw new ConfigException(vex);
    	} catch(InvocationTargetException iex) {
    		throw new ConfigException(iex);    		    		
    	} catch(NoSuchMethodException nex) {
    		throw new ConfigException(nex);    		
    	} catch(ClassNotFoundException cex) {
    		throw new ConfigException(cex);
    	} catch(InstantiationException iex) {
    		throw new ConfigException(iex);    		
    	} catch(IllegalAccessException iex) {
    		throw new ConfigException(iex);    		
    	}    	
    }
}
