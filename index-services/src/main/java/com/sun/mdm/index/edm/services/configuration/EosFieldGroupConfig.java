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
 * @(#)EosFieldGroupConfig.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.index.edm.services.configuration;

import com.sun.mdm.index.master.ProcessingException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Model search group
 *
 * @author wwu
 * @created August 6, 2002
 */
public class EosFieldGroupConfig implements java.io.Serializable {
    private ArrayList fieldRefs;
    private String name;

    // list of field refs Strings
    private ObjectNodeConfig rootObj;

    // RANGE_SEARCH: we need to convey more information then just the 
    // field name.  A field now also has a "choice" flag which lets the
    // user decide how the field will be presented on the GUI.  The choice
    // flag can be "exact" which is the default, and "range" which signals
    // the desire for a "From" / "To" style selection.
    class FieldRefStruct {
        FieldRefStruct(String refName, String choice, String require) { 
            this.refName = refName;
            this.choice = choice;
            this.require = require;
        }
        String refName;
        String choice;
        String require; // "true", "false", "oneof" or "" from EDM.xml
        public String toString() {
            return refName;
        }
    }
    
    // e.g. the Person config object

    /**
     * Constructor for the EosFieldGroupConfig object
     *
     * @param name Description of the Parameter
     * @param rootObj the root object
     */
    public EosFieldGroupConfig(String name, ObjectNodeConfig rootObj) {
        super();
        this.name = name;
        this.rootObj = rootObj;
        fieldRefs = new ArrayList(0);
    }

    /**
     * @todo Document: Getter for FieldConfigs attribute of the
     *      EosFieldGroupConfig object
     * @throws ProcessingException when processing fields fails
     * @return the field config objs
     */
    public FieldConfig[] getFieldConfigs() throws ProcessingException {
        ArrayList configList = new ArrayList();
        int index = 0;

        for (Iterator i = fieldRefs.iterator(); i.hasNext();) {
            FieldRefStruct fieldRefStruct = (FieldRefStruct) i.next();
            FieldConfig fc = rootObj.getFieldConfig(fieldRefStruct.refName);
            // RANGE_SEARCH: If a range is requested, then two field configs need to 
            // be inserted.  This is because the field will appear twice on the GUI.
            // In addition, the usage attribute of the field configs have to be modified 
            // to reflect which field is the "From" and which is the "To".
            
            // Add only non-null field configs
            if (fc != null) {
                if (fieldRefStruct.choice.equals("range")) {
                    FieldConfig fc1 = (FieldConfig) fc.copy();
                    FieldConfig fc2 = (FieldConfig) fc.copy();
                    fc1.setUsage("rangeFrom");
                    fc2.setUsage("rangeTo");
                    
                    // check required of one of these field is required
                    fc1.setRequired(false);
                    fc2.setRequired(false);
                    if (fieldRefStruct.require.equalsIgnoreCase("true")) {
                        fc1.setRequired(true);
                        fc2.setRequired(true);
                    } else if (fieldRefStruct.require.equalsIgnoreCase("oneof")) {
                        fc1.setOneOfTheseRequired(true);
                        fc2.setOneOfTheseRequired(true);
                    }
                    configList.add(fc1);
                    configList.add(fc2);
                } else {
                    FieldConfig fc3 = (FieldConfig) fc.copy();
                    
                    // check required of one of these field is required
                    fc3.setRequired(false);
                    if (fieldRefStruct.require.equalsIgnoreCase("true")) {
                        fc3.setRequired(true);
                    } else if (fieldRefStruct.require.equalsIgnoreCase("oneof")) {
                        fc3.setOneOfTheseRequired(true);
                    }
                    configList.add(fc3);
                }
            }   
        }
        
        FieldConfig[] configs = new FieldConfig[configList.size()];
        for (Iterator i = configList.iterator(); i.hasNext();) {
            FieldConfig fc = (FieldConfig) i.next();
            configs[index++] = fc;
        }

        return configs;
    }

    /**
     * @todo Document: Getter for FieldRefs attribute of the EosFieldGroupConfig
     *      object
     * @return the field refs as strings
     */
    public String[] getFieldRefs() {
        String[] refs = new String[fieldRefs.size()];
        int index = 0;

        for (Iterator i = fieldRefs.iterator(); i.hasNext();) {
            refs[index++] = ((FieldRefStruct)i.next()).refName;
        }

        return refs;
    }

    /**
     * @todo Document: Getter for FieldRefsAsArrayList attribute of the
     *      EosFieldGroupConfig object
     * @return the field refs as arraylist
     */
    public ArrayList getFieldRefsAsArrayList() {
        return this.fieldRefs;
    }

    /**
     * @todo Document: Getter for Name attribute of the EosFieldGroupConfig
     *      object
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<field-group>\n");
        buf.append("<root-obj>" + rootObj.getName() + "</root-obj>\n");
        buf.append("<description>" + getName() + "</description>\n");

        for (Iterator i = fieldRefs.iterator(); i.hasNext();) {
            buf.append("<field-ref>" + i.next() + "</field-ref>\n");
        }

        buf.append("</field-group>\n");

        return buf.toString();
    }

    // RANGE_SEARCH: Default choice is "exact"
    void addFieldRef(String refName, String require) {
        this.fieldRefs.add(new FieldRefStruct(refName, "exact", require));
    }
    
    // RANGE_SEARCH: "exact" and "range" are the only other options
    void addFieldRef(String refName, String choice, String require) throws ProcessingException {
        if (choice.equals("range") || choice.equals("exact")) {
            this.fieldRefs.add(new FieldRefStruct(refName, choice, require));
        } else {
            throw new ProcessingException("Invalid field choice: " + choice);
        }
    }
}
