/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author wee
 */
public class ObjectNode implements Comparable{
        // key= child object name; value = corresponding ObjectNode object
    private HashMap childConfigMap;

    // key = field name; value=corresponding FieldConfig object
    private HashMap fieldConfigMap;

    // name of the object
    private String name;

    // key= associated object name; value = corresponding ObjectNode object -- not used yet
    // private Map associateConfigMap = new HashMap(0);
    private boolean rootNode;

    // The parent node, or null if it is a root node
    private ObjectNode parentNode;
    
    // whether this is a root obj. default to true, set to false when defined as a child node in <relationship/>

    /**
     * Constructor for the ObjectNode object
     *
     * @param name Description of the Parameter
     */
    public ObjectNode(String name) {
        super();
        this.name = name;
        this.fieldConfigMap = new HashMap(0);
        this.childConfigMap = new HashMap(0);
        this.rootNode = true;
    }

    // get child node configure

    /**
     * @param childObjectName name of the child object
     * @todo Document: Getter for ChildConfig attribute of the ObjectNode
     *      object
     * @return the child object node config object
     */
    public ObjectNode getChildConfig(String childObjectName) {
        return (ObjectNode) childConfigMap.get(childObjectName);
    }

    /*
          return all child nodes of the object
          @todo Document: get all of the child node of ObjectNode object
          @return ObjectNode[]
      */

    /**
     * @todo Document: Getter for ChildConfigs attribute of the ObjectNode
     * object
     * @return the child object node config objects
     */
    public ObjectNode[] getChildConfigs() {
        ArrayList childConfigs = new ArrayList(childConfigMap.values());
        int count = childConfigs.size();

        // compose the array
        ObjectNode[] configs = new ObjectNode[count];
        int index = 0;

        for (Iterator i = childConfigs.iterator(); i.hasNext();) {
            ObjectNode c = (ObjectNode) i.next();
            configs[index++] = c;
        }

        Arrays.sort((Object[]) configs);
        return configs;
    }

    /**
     * @todo Document: Getter for ConfigForNode attribute of the
     * ObjectNode object
     *
     * @param nodeName name of the object node
     * @return the object node config for the named object
     */
    public ObjectNode getConfigForNode(String nodeName) {
        if (name.equalsIgnoreCase(nodeName)) {
            return this;
        } else {
            return getChildConfig(nodeName);
        }
    }

    /**
     * Gets the configurationForField attribute of the ObjectNode object
     *
     * @param fieldName Description of the Parameter
     * @return the field config for the named field  
     */
    public FieldConfig getFieldConfig(String fieldName) {
        
        // Retrieve the first object.  For example, if "Person.Address.AddressLine1"
        // is passed in as the fieldName, "Person" is the first object.
        int objRefIndex = fieldName.indexOf(MIDMObjectDef.FIELD_DELIM);
        String objRef = getName();

        // default to the current object
        String xname = fieldName;

        if (objRefIndex >= 0) {
            objRef = fieldName.substring(0, objRefIndex);
            xname = fieldName.substring(objRefIndex + 1);
            // Check if a child object is the actual target
            // (e.g. Person.Address.AddressLine1)
            int childObjRefIndex = xname.indexOf(MIDMObjectDef.FIELD_DELIM);
            if (childObjRefIndex > 0) {
                String childObjRef = xname.substring(0, childObjRefIndex);
                ObjectNode childConfig 
                        = (ObjectNode) childConfigMap.get(childObjRef);

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
     * @todo Document: Getter for FieldConfig attribute of the ObjectNode
     *      object
     * @return the field config for the field specified by the objref and fieldname
     */
    public FieldConfig getFieldConfig(String objRef, String fieldName) {
        FieldConfig config = null;
        if (objRef.equalsIgnoreCase(getName())) {
            config = (FieldConfig) fieldConfigMap.get(fieldName);
        } else {
            ObjectNode childConfig = (ObjectNode) childConfigMap.get(objRef);

            if (childConfig != null) {
                config = (FieldConfig) childConfig.getFieldConfig(fieldName);
            }
        }

        return config;
    }


    /**
     * @todo Document: Getter for FieldConfigs attribute of the ObjectNode
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
     * @todo Document: Getter for Name attribute of the ObjectNode object
     * @return name of the object
     */
    public String getName() {
        return this.name;
    }

    /**
     * @todo Document: Getter for Name attribute of the ObjectNode object
     * @return parent node of the object, null if it is a root node
     */
    public ObjectNode getParentNode() {
        return this.parentNode;
    }

    /**
     * @todo Document: Getter for Name attribute of the ObjectNode object
     * @param parent node of the object, null if it is a root node
     */
    public void setParentNode(ObjectNode parent) {
        this.parentNode = parent;
    }    
    
    /**
     * @todo Document: Getter for RootNode attribute of the ObjectNode
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
                + ((ObjectNode) i.next()).getName() + "</childNodeName>\n");
        }

        buf.append("</nodes>\n");

        return buf.toString();
    }

    void setRootNode(boolean val) {
        this.rootNode = val;
    }

    // add child node configure
    void addChildConfig(ObjectNode childConfig) {
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
        int ret = getDisplayOrder() - ((ObjectNode) obj).getDisplayOrder();

        // order by display order
        // if display orders equal, order by name
        if (ret == 0) {
            return this.getName().compareTo(((ObjectNode) obj).getName());
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
