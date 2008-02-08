/*
 * BEGIN_HEADER - DO NOT EDIT
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * If applicable add the following below this CDDL HEADER,
 * with the fields enclosed by brackets "[]" replaced with
 * your own identifying information: Portions Copyright
 * [year] [name of copyright owner]
 */

/*
 * @(#)ObjectNodeConfig.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.index.edm.services.configuration;

import com.sun.mdm.index.edm.util.QwsUtil;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Description of the Class
 *
 * @author wwu
 * @created August 6, 2002
 */
public class ObjectNodeConfig implements java.io.Serializable, Comparable {
    // key= child object name; value = corresponding ObjectNodeConfig object
    private HashMap childConfigMap;

    // key = field name; value=corresponding FieldConfig object
    private HashMap fieldConfigMap;

    // name of the object
    private String name;

    // key= associated object name; value = corresponding ObjectNodeConfig object -- not used yet
    // private Map associateConfigMap = new HashMap(0);
    private boolean rootNode;

    // The parent node, or null if it is a root node
    private ObjectNodeConfig parentNode;
    
    // whether this is a root obj. default to true, set to false when defined as a child node in <relationship/>

    /**
     * Constructor for the ObjectNodeConfig object
     *
     * @param name Description of the Parameter
     */
    public ObjectNodeConfig(String name) {
        super();
        this.name = name;
        this.fieldConfigMap = new HashMap(0);
        this.childConfigMap = new HashMap(0);
        this.rootNode = true;
    }

    // get child node configure

    /**
     * @param childObjectName name of the child object
     * @todo Document: Getter for ChildConfig attribute of the ObjectNodeConfig
     *      object
     * @return the child object node config object
     */
    public ObjectNodeConfig getChildConfig(String childObjectName) {
        return (ObjectNodeConfig) childConfigMap.get(childObjectName);
    }

    /*
          return all child nodes of the object
          @todo Document: get all of the child node of ObjectNodeConfig object
          @return ObjectNodeConfig[]
      */

    /**
     * @todo Document: Getter for ChildConfigs attribute of the ObjectNodeConfig
     * object
     * @return the child object node config objects
     */
    public ObjectNodeConfig[] getChildConfigs() {
        ArrayList childConfigs = new ArrayList(childConfigMap.values());
        int count = childConfigs.size();

        // compose the array
        ObjectNodeConfig[] configs = new ObjectNodeConfig[count];
        int index = 0;

        for (Iterator i = childConfigs.iterator(); i.hasNext();) {
            ObjectNodeConfig c = (ObjectNodeConfig) i.next();
            configs[index++] = c;
        }

        Arrays.sort((Object[]) configs);
        return configs;
    }

    /**
     * @todo Document: Getter for ConfigForNode attribute of the
     * ObjectNodeConfig object
     *
     * @param nodeName name of the object node
     * @return the object node config for the named object
     */
    public ObjectNodeConfig getConfigForNode(String nodeName) {
        if (name.equalsIgnoreCase(nodeName)) {
            return this;
        } else {
            return getChildConfig(nodeName);
        }
    }

    /**
     * Gets the configurationForField attribute of the ObjectNodeConfig object
     *
     * @param fieldName Description of the Parameter
     * @return the field config for the named field  
     */
    public FieldConfig getFieldConfig(String fieldName) {
        
        // Retrieve the first object.  For example, if "Person.Address.AddressLine1"
        // is passed in as the fieldName, "Person" is the first object.
        int objRefIndex = fieldName.indexOf(ConfigManager.FIELD_DELIM);
        String objRef = getName();

        // default to the current object
        String xname = fieldName;

        if (objRefIndex >= 0) {
            objRef = fieldName.substring(0, objRefIndex);
            xname = fieldName.substring(objRefIndex + 1);
            // Check if a child object is the actual target
            // (e.g. Person.Address.AddressLine1)
            int childObjRefIndex = xname.indexOf(ConfigManager.FIELD_DELIM);
            if (childObjRefIndex > 0) {
                String childObjRef = xname.substring(0, childObjRefIndex);
                ObjectNodeConfig childConfig 
                        = (ObjectNodeConfig) childConfigMap.get(childObjRef);

                if (childConfig != null) {
                    return childConfig.getFieldConfig(xname);
                }
            } 
        }

        return getFieldConfig(objRef, xname);
    }

    /**
     * @param objRef obj ref of the field
     * @param fieldName name of the field
     * @todo Document: Getter for FieldConfig attribute of the ObjectNodeConfig
     *      object
     * @return the field config for the field specified by the objref and fieldname
     */
    public FieldConfig getFieldConfig(String objRef, String fieldName) {
        FieldConfig config = null;
        if (objRef.equalsIgnoreCase(getName())) {
            config = (FieldConfig) fieldConfigMap.get(fieldName);
        } else {
            ObjectNodeConfig childConfig = (ObjectNodeConfig) childConfigMap.get(objRef);

            if (childConfig != null) {
                config = (FieldConfig) childConfig.getFieldConfig(fieldName);
            }
        }

        return config;
    }

    /*
          return all fields of ObjectNodeConfig object
          @return all the fields value (FieldCofnig) of ObjectNodeConfig object
          @return FieldConfig[]
      */

    /**
     * @todo Document: Getter for FieldConfigs attribute of the ObjectNodeConfig
     * object
     * @return the field config objects
     */
    public FieldConfig[] getFieldConfigs() {
        ArrayList fieldConfigs = new ArrayList(fieldConfigMap.values());
        int count = fieldConfigs.size();

        // compose the array
        FieldConfig[] configs = new FieldConfig[count];
        int index = 0;

        for (Iterator i = fieldConfigs.iterator(); i.hasNext();) {
            FieldConfig c = (FieldConfig) i.next();
            configs[index++] = c;
        }

        Arrays.sort((Object[]) configs);
        return configs;
    }

    // return key fields as an ordered list

    /**
     * Gets the keyFieldConfigList attribute of the EnterpriseObjectConfig
     * object
     *
     * @return The keyFieldConfigList value
     */
    public FieldConfig[] getKeyFieldConfigs() {
        ArrayList fieldConfigs = new ArrayList(fieldConfigMap.values());
        int count = 0;

        //get the number of key fields
        for (Iterator i = fieldConfigs.iterator(); i.hasNext();) {
            FieldConfig c = (FieldConfig) i.next();

            if (c.isKeyType()) {
                count++;
            }
        }

        // compose the array
        FieldConfig[] configs = new FieldConfig[count];
        int index = 0;

        for (Iterator i = fieldConfigs.iterator(); i.hasNext();) {
            FieldConfig c = (FieldConfig) i.next();

            if (c.isKeyType()) {
                configs[index++] = c;
            }
        }

        return configs;
    }

    /**
     * @todo Document: Getter for Name attribute of the ObjectNodeConfig object
     * @return name of the object
     */
    public String getName() {
        return this.name;
    }

    /**
     * @todo Document: Getter for Name attribute of the ObjectNodeConfig object
     * @return parent node of the object, null if it is a root node
     */
    public ObjectNodeConfig getParentNode() {
        return this.parentNode;
    }

    /**
     * @todo Document: Getter for Name attribute of the ObjectNodeConfig object
     * @param parent node of the object, null if it is a root node
     */
    public void setParentNode(ObjectNodeConfig parent) {
        this.parentNode = parent;
    }    
    
    /**
     * @todo Document: Getter for RootNode attribute of the ObjectNodeConfig
     *      object
     * @return whether the node is a root onde
     */
    public boolean isRootNode() {
        return this.rootNode;
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<nodes>\n");
        buf.append("<tag>" + getName() + "</tag>\n");

        ArrayList fieldConfigs = new ArrayList(fieldConfigMap.values());

        for (Iterator i = fieldConfigs.iterator(); i.hasNext();) {
            buf.append(((FieldConfig) i.next()).toString());
        }

        for (Iterator i = childConfigMap.values().iterator(); i.hasNext();) {
            buf.append("<childNodeName>" 
                + ((ObjectNodeConfig) i.next()).getName() + "</childNodeName>\n");
        }

        buf.append("</nodes>\n");

        return buf.toString();
    }

    void setRootNode(boolean val) {
        this.rootNode = val;
    }

    // add child node configure
    void addChildConfig(ObjectNodeConfig childConfig) {
        childConfigMap.put(childConfig.getName(), childConfig);
    }

    // add a field to the object
    void addFieldConfig(FieldConfig fieldConfig) {
        fieldConfigMap.put(fieldConfig.getName(), fieldConfig);
    }

    /**
     * @todo compare the obj to another instance 
     * @param obj the other obj
     * @return the result: greater than 0, equal to 0, or less than 0
     */
    public int compareTo(Object obj) {
        int ret = getDisplayOrder() - ((ObjectNodeConfig) obj).getDisplayOrder();

        // order by display order
        // if display orders equal, order by name
        if (ret == 0) {
            return this.getName().compareTo(((ObjectNodeConfig) obj).getName());
        }
        return ret;
    }

    private int displayOrder = 0;

    /**
     * Gets the displayOrder attribute of the object
     *
     * @return The displayOrder value
     */
    public int getDisplayOrder() {
        return this.displayOrder;
    }

    private boolean mustDelete = false;
    /**
     * Sets the displayOrder attribute of the object
     * @param val the display order to be set to
     */
    void setDisplayOrder(int val) {
        this.displayOrder = val;
    }
    /**
     * Gets the mustDelete attribute of the object
     *
     * @return the must delete value
     */
    public boolean getMustDelete() {
        return this.mustDelete;
    }

    /**
     * Sets the mustDelete attribute of the object
     * @param val the must delete to be set to
     */
    void setMustDelete(boolean val) {
        this.mustDelete = val;
    }
}
