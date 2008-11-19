/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.services.util;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.multidomain.services.configuration.FieldConfig;

import java.util.List;
import java.util.ArrayList;

import com.sun.mdm.index.util.Localizer;

/**
 *
 * @author rtam
 */
public class DomainUtil {

    private static transient final Localizer mLocalizer = Localizer.get();
    
    public DomainUtil() { 
    }
    
    /** Retrieves the field values from an ObjectNode instance based on the 
     *  FieldConfig instances supplied.
     * 
     * @param objectNode  ObjectNode instance
     * @param fieldConfigs  ArrayList of FieldConfing objects that define the values
     * that need to be retrieved from the ObjectNode instance.
     * @return A list of values corresponding to the fields identified by the
     * FieldConfig array.
     * @throws Exception if an error is encountered.
     */
    public static List getFieldValues(ObjectNode objectNode, ArrayList<FieldConfig> fieldConfigs) 
            throws Exception {
        if (objectNode == null) {
            throw new Exception(mLocalizer.t("UTL501: The objectNode parameter cannot be null"));
        }
        if (fieldConfigs == null || fieldConfigs.size() == 0) {
            throw new Exception(mLocalizer.t("UTL502: The fieldConfigs parameter " +
                                             "cannot be null or an empty ArrayList."));
        }
        try {
            ArrayList fieldValues = new ArrayList();
            for (FieldConfig fieldConfig : fieldConfigs) {
                String fullFieldName = fieldConfig.getFullFieldName();
                Object value = EPathAPI.getFieldValue(fullFieldName, objectNode);
                fieldValues.add(value);
            }
            return fieldValues;    
        } catch (Exception e) {
            throw new Exception(mLocalizer.t("UTL503: The field values could not be " +
                                             "retrieved: {0}.", e.getMessage()));
        }
    }
    
    /** Retrieves the EUID from an EnterpriseObject.
     * 
     * @param eo  EnterpriseObject
     * @return EUID of the EnterpriseOjbect instance
     * @throws Exception if an error is encountered.
     */
    public static Object getEUIDValue(ObjectNode eo) throws Exception {
        return ((EnterpriseObject)eo).getEUID();
    }
    
    /** Retrieves the value of a field from an ObjectNode instance
     * 
     * @param objectNode  ObjectNode instance
     * @param fieldConfigs  FieldConfing object that defines the value
     * that need to be retrieved from the ObjectNode instance.
     * @return The value of the field specified by the FieldConfig parameter.
     * @throws Exception if an error is encountered.
     */
    public static Object getFieldValue(ObjectNode objectNode, FieldConfig fieldConfig) 
            throws Exception {
        if (objectNode == null) {
            throw new Exception(mLocalizer.t("UTL504: The objectNode parameter cannot be null"));
        }
        if (fieldConfig == null) {
            throw new Exception(mLocalizer.t("UTL505: The fieldConfigs parameter " +
                                             "cannot be null or an empty ArrayList."));
        }
        String fullFieldName = fieldConfig.getFullFieldName();
        Object value = EPathAPI.getFieldValue(fullFieldName, objectNode);
        return value;
    }
}
