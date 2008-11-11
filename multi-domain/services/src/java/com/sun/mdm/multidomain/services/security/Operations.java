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
package com.sun.mdm.multidomain.services.security;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.mdm.multidomain.services.core.ConfigException;
        
/**
 * Security Operations.
 * @author cye
 */
public class Operations {
              
    public static final String RelationshipDef_ReadWrite = "RelationshipDef_ReadWrite";
    public static final String RelationshipDef_Read = "RelationshipDef_Read";
    public static final String Relationship_ReadWrite = "Relationship_ReadWrite";
    public static final String Relationship_Read = "Relationship_Read";
    public static final String Relationship_History_Read = "Relationship_History_Read";
    
    public static final String HierarchyDef_ReadWrite = "HierarchyDef_ReadWrite";
    public static final String HierarchyDef_Read = "HierarchyDef_Read";
    public static final String Hierarchy_ReadWrite = "Hierarchy_ReadWrite";
    public static final String Hierarchy_Read = "Hierarchy_Read";
    public static final String Hierarchy_History_Read = "Hierarchy_History_Read";
    
    public static final String GroupDef_ReadWrite = "GroupDef_ReadWrite";
    public static final String GroupDef_Read = "GroupDef_Read";
    public static final String Group_ReadWrite = "Group_ReadWrite ";
    public static final String Group_Read = "Group_Read";
    public static final String Group_History_Read = "Group_History_Read";
    
    public static final String Domain_getDomains = "Domain_getDomains";
    public static final String RelationshipDef_getSensitiveField = "RelationshipDef_getSensitiveField"; 
    public static final String RelationshipDef_getDefs = "RelationshipDef_getDefs";  
    public static final String RelationshipDef_getRelationshipDefs = "RelationshipDef_getRelationshipDefs"; 
    public static final String RelationshipDef_addRelationshipDef = "RelationshipDef_addRelationshipDef";           
    public static final String RelationshipDef_deleteRelationshipDef = "RelationshipDef_deleteRelationshipDef";
    public static final String RelationshipDef_updateRelationshipDef = "RelationshipDef_updateRelationshipDef";
    public static final String RelationshipDef_getRelationshipDefCount = "RelationshipDef_getRelationshipDefCount";
    public static final String RelationshipDef_getDomainRelationshipDefsObjects = "RelationshipDef_getDomainRelationshipDefsObjects";   
         
    public static final String Relationship_getSensitiveField = "Relationship_getSensitiveField"; 
    public static final String Relationship_searchRelationships = "Relationship_searchRelationships";
    public static final String Relationship_getRelationship = "Relationship_getRelationship";
    public static final String Relationship_searchEnterprises = "Relationship_searchEnterprises";
    public static final String Relationship_getEnterprise = "Relationship_getEnterprise";
    public static final String Relationship_addRelationship = "Relationship_addRelationship";
    public static final String Relationship_updateRelationship = "Relationship_updateRelationship";
    public static final String Relationship_deleteRelationship = "Relationship_deleteRelationship";
    public static final String Relationship_searchRelationshipsByRecord = "Relationship_searchRelationshipsByRecord";     
               
    private static final String OPERATIONS_TAG = "operations";
    private static final String OPERATION_TAG = "operation";
    private static final String METHOD_TAG = "method";
    private static final String NAME_TAG = "name";        
    private static final String ACTION_TAG = "action";
     
    public static final String READ = "read"; 
    public static final String WRITE = "write";
    
    private static List<String> operationsByName = new ArrayList<String>();
    private static Map<String, List<ACL.Entry>> methodsByOperation = new HashMap<String, List<ACL.Entry>>(); 
    private static boolean parsed = false;
            
    public Operations(){     
    }
    
    public static void parser() 
        throws ConfigException { 
        if (!parsed) {
            InputStream  operationsDef = Operations.class.getResourceAsStream("operations.xml"); 
            try {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                // docFactory.setValidating(true);
                DocumentBuilder builder = docFactory.newDocumentBuilder();
                Document doc  = builder.parse(operationsDef );
                Element root = doc.getDocumentElement();
            
                Element operations = (Element) root.getElementsByTagName(OPERATIONS_TAG).item(0);
            
                NodeList operationNodeList = operations.getElementsByTagName(OPERATION_TAG);
                for (int i = 0; i < operationNodeList.getLength(); i++) {
                    Element operaionElement = (Element) operationNodeList.item(i);             
                    String operationByName = operaionElement.getAttribute(NAME_TAG);
                    operationsByName.add(operationByName);
                    NodeList methodNodeList = operaionElement.getElementsByTagName(METHOD_TAG);
                
                    List<ACL.Entry> methods = new ArrayList<ACL.Entry>();
                    for (int j = 0; j < methodNodeList.getLength(); j++) {
                        Element methodElement = (Element) methodNodeList.item(i);
                        String methodByName = methodElement.getAttribute(NAME_TAG);
                        String action = methodElement.getAttribute(ACTION_TAG);
                        methods.add(new ACL.Entry(methodByName, action));
                    }
                    methodsByOperation.put(operationByName, methods);
                }
                parsed = true;
            } catch ( IOException ioex) {
                throw new ConfigException(ioex);
            } catch (SAXException saex) {
                throw new ConfigException(saex);   
            } catch (ParserConfigurationException pex) {
                throw new ConfigException(pex);   
            }
        }   
    }
    
    public boolean isField_VIP() {
        return false;
    }
    
    public static List<String> getOperations() 
        throws ConfigException {
        parser();
        return operationsByName;
    }
    
    public static List<ACL.Entry> getMethods(String operation) 
        throws ConfigException {
        parser();
        return methodsByOperation.get(operation);     
    }
}
