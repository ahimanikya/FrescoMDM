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
 
 package com.sun.mdm.index.security;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathParser;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.exception.ObjectException;
//import com.sun.mdm.index.util.Constants;
import com.sun.mdm.index.security.SecurityMaskPlugIn;
import com.sun.mdm.index.security.SecurityException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import com.sun.mdm.index.util.ObjectSensitivePlugIn;


/**
 * @author sdua 
 */
public class VIPObjectSensitivePlugIn implements ObjectSensitivePlugIn {
    
    private static final String maskString = "XXXXXXXX";
    


    /**
     * Creates a new instance of SecurityMaskBasePlugIn
     *
     * @exception SecurityException error occured
     */
    public VIPObjectSensitivePlugIn()
         {
             
    }


    /**
     * checks if data is sensitive such as Person whose VIPFlag = V.
     *
     * @param objectNode to be masked
       @ret true if objectNode contains sensitive data that needs to be masked else false.
     * @exception SecurityException error occured.
     */
    public boolean isDataSensitive(ObjectNode objectNode) throws Exception {

        boolean sensitive = false;

       
              
        try {

                         
             if (objectNode.pGetTag().equals("Person")) {            
                  String value = (String)objectNode.getValue("VIPFlag");
                  if (value != null 
                        && (value.compareToIgnoreCase("V") == 0 || value.compareToIgnoreCase("E") == 0)) {
                            sensitive = true;
                  }
            }  else if (objectNode.pGetTag().equals("Enterprise")) {     
                //  mask person child objects for SBR and SystemObjects, if necessary
                EnterpriseObject enterpriseObjNode = (EnterpriseObject) objectNode;
                SBR sbrObj = (SBR) enterpriseObjNode.getSBR();
                if (sbrObj != null)  {
                    PersonObject personObj = (PersonObject) sbrObj.getObject();
                    if (personObj != null)  {
                        String value = (String) personObj.getValue("VIPFlag");
                        if (value != null 
                                && (value.compareToIgnoreCase("V") == 0 || value.compareToIgnoreCase("E") == 0)) {
                            sensitive = true;
                        }
                    }
                }
                Collection sysObjNodes = enterpriseObjNode.getSystemObjects();
                Iterator nodeIter = sysObjNodes.iterator();
                while (nodeIter.hasNext())  {
                    SystemObject sysObj = (SystemObject) nodeIter.next();
                    if (sysObj != null)  {
                        PersonObject personObj = (PersonObject) sysObj.getObject();
                        if (personObj != null)  {
                            String value = (String) personObj.getValue("VIPFlag");
                            if (value != null 
                                    && (value.compareToIgnoreCase("V") == 0 || value.compareToIgnoreCase("E") == 0)) {
                                sensitive = true;
                            }
                        }
                    }
                }
            }  else if (objectNode.pGetTag().equals("SystemSBR")) {     
                //  mask Person child objects for SBR 
                SBR sbrObjNode = (SBR) objectNode;
                PersonObject personObj = (PersonObject) sbrObjNode.getObject();
                if (personObj != null)  {
                    String value = (String) personObj.getValue("VIPFlag");
                    if (value != null 
                            && (value.compareToIgnoreCase("V") == 0 || value.compareToIgnoreCase("E") == 0)) {
                        sensitive = true;
                    }
                }
            }  else if (objectNode.pGetTag().equals("SystemObject")) {     
                //  mask Person child objects for SystemObjects
                SystemObject sysObjNode = (SystemObject) objectNode;
                PersonObject personObj = (PersonObject) sysObjNode.getObject();
                if (personObj != null)  {
                    String value = (String) personObj.getValue("VIPFlag");
                    if (value != null 
                            && (value.compareToIgnoreCase("V") == 0 || value.compareToIgnoreCase("E") == 0)) {
                        sensitive = true;
                    }
                }
            }
                
            return sensitive;
        } catch (Exception ex) {
            throw ex;
        }
    }
    
   
    
  
    
}
