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
package com.sun.mdm.index.master;

import java.util.ArrayList;
import java.util.Iterator;
import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.
    configurator.impl.standardization.SystemObjectStandardization;
import com.sun.mdm.index.configurator.impl.standardization.PhoneticizeField;
import com.sun.mdm.index.configurator.impl.standardization.SystemObjectField;
import com.sun.mdm.index.configurator.impl.standardization.PreparsedFieldGroup;
import com.sun.mdm.index.configurator.impl.standardization.UnparsedFieldGroup;
import com.sun.mdm.index.
    configurator.impl.standardization.StandardizationConfiguration;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.util.Localizer;


/**
 * Construct query by looking at which fields in the system object have been
 * populated and performing an "=" search on each of them. If standardization or
 * phoneticization has been enabled, perform a search on the transformed field
 * rather than the original source field.
 */
public class ObjectNodeFilter {

    private transient final Localizer mLocalizer = Localizer.get();

    /**
     * Creates a new instance of FullSearchQueryBuilder
     */
    public ObjectNodeFilter() {
    }


    /** Filter out phonetic source fields
     * @param objNode Object node to filter
     * @exception ProcessingException An error occured.
     */
    public void filterPhoneticizedSourceFields(ObjectNode objNode)
        throws ProcessingException {
        try {
            SystemObjectStandardization stan = 
                getSystemObjectStandardization(objNode);
            ArrayList list = stan.getFieldsToPhoneticize();
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                PhoneticizeField field = (PhoneticizeField) iterator.next();
                SystemObjectField sysObjectField = field.getSourceField();

                String path = 
                    replaceAsteriskWithZero(sysObjectField.getQualifiedName());
                EPathAPI.setFieldNull(path, objNode, true);
            }
        } catch (Exception e) {
            throw new ProcessingException(mLocalizer.t("MAS504: Error encountered " + 
                                "in filterPhoneticizedSourceFields(): {0}", e));
        }
    }


    /** Filter out phonetic target fields
     * @param objNode Object node to filter
     * @exception ProcessingException An error occured.
     */
    public void filterPhoneticizedTargetFields(ObjectNode objNode)
        throws ProcessingException {
        try {
            SystemObjectStandardization stan = 
                getSystemObjectStandardization(objNode);
            ArrayList list = stan.getFieldsToPhoneticize();
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                PhoneticizeField field = (PhoneticizeField) iterator.next();
                SystemObjectField sysObjectField = field.getTargetField();
                String path = 
                    replaceAsteriskWithZero(sysObjectField.getQualifiedName());
                EPathAPI.setFieldNull(path, objNode, true);
            }
        } catch (Exception e) {
            throw new ProcessingException(mLocalizer.t("MAS505: Error encountered " + 
                                "in filterPhoneticizedTargetFields(): {0}", e));
        }
    }


    /** Filter out standardization source fields
     * @param objNode Object node to filter
     * @exception ProcessingException An error occured.
     */
    public void filterStandardizedSourceFields(ObjectNode objNode)
        throws ProcessingException {
        try {
            SystemObjectStandardization stan = 
                getSystemObjectStandardization(objNode);

            //Handle preparsed field groups
            ArrayList preParsedList = stan.getPreParsedFieldGroups();
            Iterator iterator = preParsedList.iterator();
            while (iterator.hasNext()) {
                PreparsedFieldGroup group = 
                    (PreparsedFieldGroup) iterator.next();
                Iterator sourceFields = 
                    group.getSourceFieldsDirectMap().keySet().iterator();
                while (sourceFields.hasNext()) {
                    SystemObjectField field = 
                        (SystemObjectField) sourceFields.next();
                    String path = 
                        replaceAsteriskWithZero(field.getQualifiedName());
                    EPathAPI.setFieldNull(path, objNode, true);
                }
            }

            //Handle unparsed field groups
            ArrayList unParsedList = stan.getUnParsedFieldGroups();
            iterator = unParsedList.iterator();
            while (iterator.hasNext()) {
                UnparsedFieldGroup group = (UnparsedFieldGroup) iterator.next();
                Iterator sourceFields = group.getSourceFields().iterator();
                while (sourceFields.hasNext()) {
                    SystemObjectField field = 
                        (SystemObjectField) sourceFields.next();
                    String path = 
                        replaceAsteriskWithZero(field.getQualifiedName());
                    EPathAPI.setFieldNull(path, objNode, true);
                }
            }
        } catch (Exception e) {
            throw new ProcessingException(mLocalizer.t("MAS506: Error encountered " + 
                                "in filterStandardizedSourceFields(): {0}", e));
        }
    }


    /** Filter out standardization source fields
     * @param objNode Object node to filter
     * @exception ProcessingException An error occured.
     */
    public void filterStandardizedTargetFields(ObjectNode objNode)
        throws ProcessingException {
        try {
            SystemObjectStandardization stan = 
                getSystemObjectStandardization(objNode);

            //Handle preparsed field groups
            ArrayList preParsedList = stan.getPreParsedFieldGroups();
            Iterator iterator = preParsedList.iterator();
            while (iterator.hasNext()) {
                PreparsedFieldGroup group = 
                    (PreparsedFieldGroup) iterator.next();
                Iterator targetFields = 
                    group.getStandardizationTargets().values().iterator();
                while (targetFields.hasNext()) {
                    SystemObjectField field = 
                        (SystemObjectField) targetFields.next();
                    String path = 
                        replaceAsteriskWithZero(field.getQualifiedName());
                    EPathAPI.setFieldNull(path, objNode, true);
                }
            }

            //Handle unparsed field groups
            ArrayList unParsedList = stan.getUnParsedFieldGroups();
            iterator = unParsedList.iterator();
            while (iterator.hasNext()) {
                UnparsedFieldGroup group = (UnparsedFieldGroup) iterator.next();
                Iterator targetFields = 
                    group.getStandardizationTargets().values().iterator();
                while (targetFields.hasNext()) {
                    SystemObjectField field = 
                        (SystemObjectField) targetFields.next();
                    String path = 
                    replaceAsteriskWithZero(field.getQualifiedName());
                    EPathAPI.setFieldNull(path, objNode, true);
                }
            }
        } catch (Exception e) {
            throw new ProcessingException(mLocalizer.t("MAS507: Error encountered " + 
                                "in filterStandardizedTargetFields(): {0}", e));
        }
    }


    /** Get meta information for node
     * @param objNode Object node to get meta data on
     * @throws ProcessingException An error occured.
     * @return Meta data
     */    
    private SystemObjectStandardization getSystemObjectStandardization(
        ObjectNode objNode) throws ProcessingException {
        try {
            StandardizationConfiguration config = (StandardizationConfiguration)
                ConfigurationService.getInstance().getConfiguration(
                StandardizationConfiguration.STANDARDIZATION);
            return config.getSystemObjectStandardization(objNode.pGetTag());
        } catch (Exception e) {
            throw new ProcessingException(mLocalizer.t("MAS508: Error encountered " + 
                                "in getSystemObjectStandardization(): {0}", e));
        }
    }


    /** Replace * with 0
     * @param input String to process
     * @return Processed string
     */    
    private String replaceAsteriskWithZero(String input) {
        return input.replace('*', '0');
    }

}
