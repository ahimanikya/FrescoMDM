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
     private String[] mmaskCheckFields = {
            "Enterprise.SystemSBR.Person[VIPFlag=V].*",
            "SystemSBR.Person[VIPFlag=V].*",
            "Enterprise.SystemObject[*].Person[VIPFlag=V].*",
            "SystemObject[*].Person[VIPFlag=V].*",
            "Enterprise.SystemSBR.Person[VIPFlag=E].*",
            "SystemSBR.Person[VIPFlag=E].*",
            "Enterprise.SystemObject[*].Person[VIPFlag=E].*",
            "SystemObject[*].Person[VIPFlag=E].*"
            // "Person[VIPFlag=VIP].*"
            };
    
    private EPath[] mepathChecks;
    private EPath[] mepaths;
    
 


    /**
     * Creates a new instance of SecurityMaskBasePlugIn
     *
     * @exception SecurityException error occured
     */
    public VIPObjectSensitivePlugIn()
         {
             /*
        try {
            int length = mmaskCheckFields.length;
            mepathChecks = new EPath[length];

            for (int i = 0; i < length; i++) {
                mepathChecks[i] = EPathParser.parse(mmaskCheckFields[i]);
            }

            length = mmaskFields.length;
            mepaths = new EPath[length];

            for (int i = 0; i < length; i++) {
                mepaths[i] = EPathParser.parse(mmaskFields[i]);
            }
        } catch (Exception ex) {
            //throw new SecurityException(ex);
            // VIP_Flag does not exist
            mepathChecks = null;
        }
              */
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

       // if (mepathChecks == null) {
       //     sensitive = false;           
      //  }
              
        try {

       
/*
            for (int i = 0; i < mepathChecks.length; i++) {
                try {
                    // So the VIP_flag does not exist
                    //  String value = (String) EPathAPI.getFieldValue( mepathChecks[i], objectNode );
                    ArrayList list = new ArrayList();
                    EPathAPI.getFieldList(mepathChecks[i], 0, objectNode, list);

                    //if ( value != null && value.equals("VIP") ) {
                    for (int k = 0; k < list.size(); k++) {
                        ObjectNode filteredObject = (ObjectNode) list.get(k);

                        for (int j = 0; j < mmaskObjects.length; j++) {
                            filteredObject.removeChildren(mmaskObjects[j]);
                        }

                        for (int j = 0; j < mepaths.length; j++) {
                            try {
                                EPathAPI.setFieldValue(mepaths[j],
                                        filteredObject, maskString);
                                
                            } catch (EPathException eex) { 
                                ; // do nothing
                            } catch (ObjectException oex) {
                                ; // do nothing
                            }
                        }
                    }
                    
                        
                } catch (EPathException eex) { 
                    ; // do nothing
                } catch (ObjectException oex) { 
                    ; // do nothing
                }
            }
*/

            
            // explicit masking for Person ObjectNode. THis is due to bug
            // in EPathAPI that does not operate on Person root object node.
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
