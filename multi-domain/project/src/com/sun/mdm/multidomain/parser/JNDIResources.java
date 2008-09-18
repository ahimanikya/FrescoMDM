/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

import java.util.ArrayList;

/**
 *
 * @author wee
 */
public class JNDIResources {
    private ArrayList<RelationshipProperty> mProperties = null;
    private ArrayList<RelationshipJDNIResources> mJDNIResources = null;
    
    public JNDIResources(ArrayList properties, ArrayList jndiResoures) {
        this.mProperties = properties;
        this.mJDNIResources = jndiResoures;
    }
    
    public ArrayList<RelationshipProperty> getProperties() {
        return this.mProperties;
    }
    
    public ArrayList<RelationshipJDNIResources> getJDNIResources() {
        return this.mJDNIResources;
    }
    
    public void addProperty(RelationshipProperty property) {
        mProperties.add(property);
    }
    
    public void updPropertyValue(String propertyName, String propertyValue) {
        for (RelationshipProperty property : mProperties) {
            if (property.getPropertyName().equals(propertyName)) {
                property.setPropertyValue(propertyValue);
                break;
            }
        }
    }
    
    public void deleteProperty(String propertyName) {
         for (RelationshipProperty property : mProperties) {
            if (property.getPropertyName().equals(propertyName)) {
                mProperties.remove(property);
                break;
            }
        }       
    }
    
    public void addJNDIResource(RelationshipJDNIResources jndiRes) {
        mJDNIResources.add(jndiRes);        
    }
    
    public void updJDNIResValue(String jndiName, String propertyValue) {
        for (RelationshipJDNIResources jndiRes : mJDNIResources) {
            if (jndiRes.getJNDIName().equals(jndiName)) {
            }
        }
    }
    
    public void deleteJDNIRes(String jndiName, String ID) {
          for (RelationshipJDNIResources jndiRes : mJDNIResources) {
            if (jndiRes.getJNDIName().equals(jndiName) && jndiRes.getID().equals(ID)) {
                mJDNIResources.remove(jndiRes);
                break;
            }
        }       
    }    
}
