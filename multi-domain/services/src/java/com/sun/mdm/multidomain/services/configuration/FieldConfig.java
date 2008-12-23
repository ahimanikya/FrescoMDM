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
 * @(#)FieldConfig.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.multidomain.services.configuration;

import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.util.Localizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

//Adding the import for select options if the FieldConfig type is "Menu List"
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * for one search field displayed on UI.
 *
 * @author wwu
 * @created August 6, 2002
 */
public class FieldConfig implements java.io.Serializable, Comparable {
    
    private static transient final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.services.configuration.FieldConfig");
    private static transient final Localizer mLocalizer = Localizer.get();
    /** constant
     */
    public static final String GUI_TYPE_MENULIST = "MenuList";
    /** constant
     */
    public static final String GUI_TYPE_TEXTBOX = "TextBox";
    
    private String displayName;
    private int displayOrder;

    // the value type for the field, used during validation.
    private String guiType;

    // by default,a field is not an extra field
    private boolean extraField;
    
    // by default,a field is not a keyType
    private boolean keyType;
    private boolean sensitive;
    private int maxLength;
    private String name;
    
    // This is the root object.  This field either belongs to the root object
    // or a child of the root object.  In the former case, rootObj will be
    // identical to objRef
    
    private String rootObj;

    // This is the object, not the group, which the field belongs to.
    private String objRef;

    // externally provided list of menu items
    private String valueList;

    // this is the same as the metaType in ObjectField
    private int valueType;

    // this is the input mask used to validate the user input for this field
    private String inputMask;

    // this is the value mask used to reverse the effect of the input mask
    private String valueMask;

    // this is the position where characters should be extracted when demasking
    private int[] exPos;
    
    private boolean required;
    private boolean oneOfTheseRequired;
    private boolean updateable;
    
    private String userCode = null;
    private String constraintBy= null;
    
    // RANGE_SEARCH:
    // the usage flag indicates how the field will be presented in the GUI.  The 
    // choices are "rangeFrom" and "rangeTo".  The default if setUsage() is not 
    // called is null.  This setting means that the field name will be displayed
    // unmodified.  The "rangeFrom" setting means the word "From" will be appended
    // to the display name and its index position will be regarded as 1 (more about
    // index positions described in getIndex().  The "rangeTo" setting means the
    // word "To" will be appended to the display name and its index position will
    // be regarded as 2. The default setting of null will have an index position 
    // of 0.
    private String usage;
    
    
    private boolean range;
    
    
    
    // --------------------------------------------------------- Public Methods

    /**
     * Construct a FieldConfig object with all required properties
     *
     * @param rootObj root object to which this field belongs
     * @param objRef object to which this field belongs
     * @param name name of the field
     * @param displayName display name of the field
     * @param objRef obj ref of the field
     * @param guiType gui type of the field
     * @param maxLength max length of the field
     */
    public FieldConfig(String rootObj, String objRef, String name, 
                       String displayName, String guiType, int maxLength) {
        super();
        this.rootObj = rootObj;
        this.objRef = objRef;
        this.name = name;
        this.displayName = displayName;
        this.maxLength = maxLength;
        this.guiType = guiType;
        this.keyType = false;
        this.sensitive = false;
        this.displayOrder = 0;
        this.valueList = null;
        this.inputMask = null;
        this.valueMask = null;
        this.exPos = new int[0];
    }

    /**
     * Construct a FieldConfig object with all required properties
     *
     * @param objRef object to which this field belongs
     * @param name name of the field
     * @param displayName display name of the field
     * @param objRef obj ref of the field
     * @param guiType gui type of the field
     * @param maxLength max length of the field
     */
    public FieldConfig(String objRef, String name, 
                       String displayName, String guiType, int maxLength) {
        this(null, objRef, name, displayName, guiType, maxLength);
    }

    /**
     * Construct a FieldConfig object with all required properties
     *
     * @param rootObj root object to which this field belongs
     * @param objRef obj ref of the field
     * @param name name of the field
     * @param displayName display name of the field
     * @param guiType gui type of the field
     * @param maxLength max length of the field
     * @param valueType value type of the field
     */
    public FieldConfig(String rootObj, String objRef, String name, 
                       String displayName, String guiType, 
                       int maxLength, int valueType) {
        super();
        this.rootObj = rootObj;
        this.objRef = objRef;
        this.name = name;
        this.displayName = displayName;
        this.maxLength = maxLength;
        this.guiType = guiType;
        this.keyType = false;
        this.sensitive = false;
        this.displayOrder = 0;
        this.valueList = null;
        this.inputMask = null;
        this.valueMask = null;
        this.exPos = new int[0];
        this.valueType = valueType;
    }

    /**
     * Construct a FieldConfig object with all required properties
     *
     * @param rootObj root object to which this field belongs
     * @param objRef obj ref of the field
     * @param name name of the field
     * @param displayName display name of the field
     * @param guiType gui type of the field
     * @param maxLength max length of the field
     * @param valueType value type of the field
     */
    public FieldConfig(String objRef, String name, 
                       String displayName, String guiType, 
                       int maxLength, int valueType) {
       this(null, objRef, name, displayName, guiType, maxLength, valueType);
    }
    
    /**
     * Construct a FieldConfig object with all required properties
     *
     * @param rootObj root object to which this field belongs
     * @param objRef obj ref of the field
     * @param name name of the field
     * @param displayName dislay name of the field
     * @param guiType gui type of the field
     * @param maxLength max length of the field 
     * @param gvalueList value list of the field
     */
    public FieldConfig(String rootObj, String objRef, String name, 
                       String displayName, String guiType, 
                       int maxLength, String gvalueList) {
        this.objRef = objRef;
        this.rootObj = rootObj;
        this.name = name;
        this.displayName = displayName;
        this.maxLength = maxLength;
        this.guiType = guiType;
        this.keyType = false;
        this.sensitive = false;
        this.displayOrder = 0;
        this.valueType = ObjectField.OBJECTMETA_STRING_TYPE;
        setValueList(gvalueList);
        this.inputMask = null;
        this.valueMask = null;
        this.exPos = new int[0];
    }

    /**
     * Construct a FieldConfig object with all required properties
     *
     * @param rootObj root object to which this field belongs
     * @param objRef obj ref of the field
     * @param name name of the field
     * @param displayName dislay name of the field
     * @param guiType gui type of the field
     * @param maxLength max length of the field 
     * @param gvalueList value list of the field
     */
    public FieldConfig(String rootObj, String objRef, String name, 
                       String displayName, String guiType, 
                       int maxLength, String valueList,
                       String inputMask, String valueMask,
                       int valueType, boolean keyType, 
                       boolean isSensitive,
                       int displayOrder) {
        this.objRef = objRef;
        this.rootObj = rootObj;
        this.name = name;
        this.displayName = displayName;
        this.maxLength = maxLength;
        this.guiType = guiType;
        this.keyType = keyType;
        this.sensitive = isSensitive;
        this.displayOrder = displayOrder;
        this.valueType = valueType;
        setValueList(valueList);
        this.inputMask = inputMask;
        this.valueMask = valueMask;
        this.exPos = new int[0];
    }
    
    /**
     * Construct a FieldConfig object with all required properties
     *
     * @param rootObj root object to which this field belongs
     * @param objRef obj ref of the field
     * @param name name of the field
     * @param displayName dislay name of the field
     * @param guiType gui type of the field
     * @param maxLength max length of the field 
     * @param gvalueList value list of the field
     */
    public FieldConfig(String objRef, String name, 
                       String displayName, String guiType, 
                       int maxLength, String gvalueList) {
        this(null, objRef, name, displayName, guiType, maxLength, gvalueList);
    }

    /**
     * @todo Document: Getter for DisplayName attribute of the FieldConfig
     *      object
     * @return the dislay name
     */
    public String getDisplayName() {

        // RANGE_SEARCH: append From or To when necessary
        if (this.usage == null) {
            // RANGE_SEARCH: default usage is null, and display name should be left as is
            return this.displayName;
        } else if (this.usage.equals("rangeFrom")) {
            return this.displayName + " From";
        } else if (this.usage.equals("rangeTo")) {
            return this.displayName + " To";
        } else {
            // RANGE_SEARCH: no other usages are yet defined, so if we get here, for
            // now just display the usage in parenthesis
            return this.displayName + "(" + this.usage + ")";
        }
    }

    /**
     * Gets the displayOrder attribute of the FieldConfig object
     *
     * @return The displayOrder value
     */
    public int getDisplayOrder() {
        return this.displayOrder;
    }

    /**
     * RANGE_SEARCH:
     * Full name that is used in the generated html to identify this field.  The
     * full name begins with the object name (e.g. Person) if it exists, followed
     * by a separator (____), followed by the name of the field.  If the usage
     * flag is null, then no other values are appended.  If the usage flag is
     * not null, then the flag is appended to the name.  This is used later
     * to identify the index position of this field in getIndex().
     * @return the full name
     */
    public String getFullName() {
        String ret;
        if (this.objRef == null) {
            ret = this.name;
        } else {
            if (this.rootObj != null) {
                ret = this.rootObj + "." + this.objRef + "___" + this.name;
            } else {
                ret = this.objRef + "___" + this.name;
            }
        }
        if (this.usage != null) {
            ret += "____" + this.usage;
        } 
        return ret;
    }

    /**
     *  Retrieves the full name of an field
     *
     * @return  full name of the field
     */
    public String getFullFieldName()  {
        if (this.objRef == null) {
            return this.name;
        } else {
            return this.objRef + "." + this.name;
        }
    }
    /**
     * RANGE_SEARCH:
     * Determine the index of a field full name.  If field full name ends with 
     * ____rangeFrom, then index is 1, if ____rangeTo, then index is 2, 
     * otherwise index is 0.  The index position is used when constructing 
     * search criteria to determine whether the field value belongs to 
     * object 0,1,or 2.  Object 1 and 2 are to convey criteria for a range
     * search.
     * @param fullName full name
     * @return the ePath style string
     */
    public static int getIndex(String fullName) {
        if (fullName == null) {
            return 0;
        }
        int index = fullName.indexOf("____rangeTo");
        if (index < 0) {
            index = fullName.indexOf("____rangeFrom");
            if (index < 0) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 2;
        }
    }
    /**
     * convert a field full name back to a ePath style string
     * @param fullName full name
     * @return the ePath style string
     */
    public static String toEpathStyleString(String fullName) {
        if (fullName == null) {
            return fullName;
        }
        int index = fullName.indexOf("___");
        if (index < 0) {
            return fullName;
        }
        String xobjRef = fullName.substring(0, index);
        // RANGE_SEARCH: since we are appending the usage to the field name,
        // we need to strip it out if exists.  The usage always follows 4
        // consecutive '_' characaters.
        int index2 = fullName.indexOf("____");
        String fieldName;
        if (index2 < 0) {
            fieldName = fullName.substring(index + 3);
        } else {
            fieldName = fullName.substring(index + 3, index2);
        }
        return xobjRef + MDConfigManager.FIELD_DELIM + fieldName;
    }

    /**
     * Gets the guiType attribute of the FieldConfig object
     *
     * @return The guiType value
     */
    public String getGuiType() {
        return this.guiType;
    }

    /**
     * Gets the maxLength attribute of the FieldConfig object
     *
     * @return The maxLength value
     */
    public int getMaxLength() {
        return this.maxLength;
    }

    /**
     * Sets the maxLength attribute of the FieldConfig object
     * @length new max length
     */
    public void setMaxLength(int length) {
        this.maxLength = length;
    }

    /**
     * @todo Document: Getter for Name attribute of the FieldConfig object
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    public String getFieldName() {
        int index = name.indexOf(".");
        if (index >= 0) {
            String objName = name.substring(0, index);
            String fieldName = name.substring(index + 1);
            index = fieldName.indexOf(".");
            if (index >= 0) {
                objName = fieldName.substring(0, index);
                fieldName = fieldName.substring(index + 1);                
            } 
            objRef = objName;
            return fieldName;
        }
        return name;
    }
    
    /**
     * @todo Document: Getter for ObjRef attribute of the FieldConfig object
     * @return the obj ref
     */
    public String getObjRef() {
        return this.objRef;
    }

    /**
     * @todo Document: Getter for PossibleValueCount attribute of the
     *      FieldConfig object
     * @return the possible value count
     */
    public int getPossibleValueCount() {
        // RESUME HERE
        // This won't work because there will be different instances of ValidationService for different domains.
        return 0;
/*        
        if (getUserCode() != null) {
            return ValidationService.getInstance().getUserCodeValueCount(getUserCode());
        } else {
            return ValidationService.getInstance().getValueCount(this.valueList);
        }
*/        
    }

    /**
     * @todo Document: Getter for PossibleValues attribute of the FieldConfig
     *      object
     * @return the items
     */
    public PullDownListItem[] getPossibleValues() 
    throws Exception
    {
        PullDownListItem[] ret;
        String lookup = getUserCode();
        boolean isUserCode = false;
        if (lookup != null) {
            // RESUME HERE
            // This won't work because there will be different instances of ValidationService for different domains.
            ret = null;
//            ret =  ValidationService.getInstance().getUserCodeValueItems(lookup);
            isUserCode = true;
           
        } else {
            // AJK - saved for error handling
            lookup = this.valueList;
            // RESUME HERE
            // This won't work because there will be different instances of ValidationService for different domains.
            return null;          
//            ret =  ValidationService.getInstance().getValueItems(lookup);
        } 
        /* AJK: If the above did not find the values try the usercode table
           In this case the user may have forgotten to define this as a 
           user-code in the object.xml so this code will check anyway to 
           see if it is there. */
        if (ret == null){
            lookup = this.valueList;
            // RESUME HERE
            // This won't work because there will be different instances of ValidationService for different domains.
            return null;
//            ret = ValidationService.getInstance().getUserCodeValueItems(lookup);
        }
        if (ret != null) {
            Arrays.sort((Object[]) ret);
	} else {
            String tag = isUserCode? "<user-code>" : "<code-module>";
            throw new Exception(mLocalizer.t(
                    "CFG004: Could not find definition for tag {0} value {1}.  " +
                    "The definition for <code-module> must be specified in the " +
                    "sbyn_common_header table and sbyn_common_detail table. " +
                    "The definition for <user-code> must be specified in the " +
                    "sbyn_user_code table. " + 
                    "Note: The <code-module> is typically defined in the "+
                    "codelist.sql file.", 
                     tag, lookup));
            
        }
        return ret;
    }

    /**
     * @todo Document: Getter for PossibleValuesList attribute of the
     * FieldConfig object
     * @return the possible values
     */
    public List getPossibleValuesList() 
    throws Exception {
        List list = new ArrayList<PullDownListItem> ();
        try {
            PullDownListItem[] items = getPossibleValues();

            if (items != null) {
                for (int i = 0; i < items.length; i++) {
                    list.add(items[i]);
                }
            }
        } catch (Exception e){
            throw new Exception(mLocalizer.t(
                    "CFG005: Could not retrieve the possible list values: {0}", e.getMessage()));
        }
        return list;
    }

    /**
     * @todo Document: Getter for ValueType attribute of the FieldConfig object
     * @return the value type
     */
    public int getValueType() {
        return this.valueType;
    }

    /**
     * Gets the keyType attribute of the FieldConfig object
     *
     * @return The keyType value
     */
    public boolean isKeyType() {
        return keyType;
    }
    
    /**
     * Gets the sensitive attribute of the FieldConfig object
     *
     * @return The sensitive value
     */
    public boolean isSensitive() {
        return sensitive;
    }
    
    /**
    /**
     * Gets the required attribute of the FieldConfig object
     *
     * @return The required value
     */
    public boolean isRequired() {
        return required;
    }
    
    /**
     * Gets the oneOftheseRequired attribute of the FieldConfig object
     *
     * @return The oneOftheseRequired value
     */
    public boolean isOneOfTheseRequired() {
        return oneOfTheseRequired;
    }
    
    /**
     * Gets the updateable attribute of the FieldConfig object
     *
     * @return The updateable value
     */
    public boolean isUpdateable() {
        return updateable;
    }
    
    /**
     * Gets the userCode attribute of the FieldConfig object
     *
     * @return The user code value
     */
    public String getUserCode() {
        return userCode;
    }
    
    /**
     * Gets the constraintBy attribute of the FieldConfig object
     *
     * @return The constraint by value
     */
    public String getConstraintBy() {
        return constraintBy;
    }

    /*
     * Getter for rootObj attribute of the FieldConfig object
     *
     * @return the name of the root object
     */
     public String getRootObj() {
         return this.rootObj;
     }
 
    /**
     * @todo compare the obj to another instance 
     * @param obj the other obj
     * @return the result: greater than 0, equal to 0, or less than 0
     */
    public int compareTo(Object obj) {
        int ret = getDisplayOrder() - ((FieldConfig) obj).getDisplayOrder();

        // order by display order
        if (ret == 0) {
            return this.getName().compareTo(((FieldConfig) obj).getName());

            // if display orders equal, order by name
        }

        return ret;
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public String toString() {
        return "<fields>\n" + "<field-name>" + getName() + "</field-name>\n" 
        + "<display-name>" + getDisplayName() + "</display-name>\n" 
        + "<display-order>" + getDisplayOrder() + "</display-order>\n" 
        + "<max-length>" + getMaxLength() + "</max-length>\n" + "<gui-type>" 
        + getGuiType() + "</gui-type>\n" + "<key-type>" 
        + (isKeyType() ? "true" : "false") + "</key-type>\n" + "<value-list>" 
        + valueList + "</value-list>\n" + "</fields>\n";
    }

    /*
     * Sets for rootObj attribute of the FieldConfig object
     *
     * @return the name of the root object
     */
     public void setRootObject(String val) {
         this.rootObj = val;
     }
 
    /**
     * Sets the displayOrder attribute of the FieldConfig object
     *
     * @param val the display order to be set to
     */
    public void setDisplayOrder(int val) {
        this.displayOrder = val;
    }

    /**
     * Gets the extraField attribute of the FieldConfig object
     *
     * @return The extraField value
     */
    public boolean isExtraField() {
        return extraField;
    }
    
    /**
     * Sets the extraField attribute of the FieldConfig object
     *
     * @param val the init value
     */
    public void setExtraField(boolean val) {
        this.extraField = val;
    }
    
    /**
     * Sets the keyType attribute of the FieldConfig object
     *
     * @param val the init value
     */
    public void setKeyType(boolean val) {
        this.keyType = val;
    }
    
    /**
     * Sets the sensitive attribute of the FieldConfig object
     *
     * @param val the init value
     */
    public void setSensitive(boolean val) {
        this.sensitive = val;
    }
    
    /**
     * Sets the required attribute of the FieldConfig object
     *
     * @param val the required value
     */
    public void setRequired(boolean val) {
        this.required = val;
    }
    
    /**
     * Sets the oneOfTheseRequired attribute of the FieldConfig object
     *
     * @param val the oneOfTheseRequired value
     */
    public void setOneOfTheseRequired(boolean val) {
        this.oneOfTheseRequired = val;
    }
    
    /**
     * Sets the updateable attribute of the FieldConfig object
     *
     * @param val the updateable value
     */
    public void setUpdateable(boolean val) {
        this.updateable = val;
    }
    
    /**
     * Sets the userCode attribute of the FieldConfig object
     *
     * @param val the user code value
     */
    public void setUserCode(String val) {
        this.userCode = val;
        this.valueList = val;   // valueList would be user code
    }
    
    /**
     * Sets the constraintBy attribute of the FieldConfig object
     *
     * @param val the constraint by value
     */
    public void setConstraintBy(String val) {
        this.constraintBy = val;
    }

    /** get the value list 
     * @return valueList a String value
     */
    public String getValueList() {
        return this.valueList;
    }
    
    /** set the value list 
     * @param valueList the list
     */
    public void setValueList(String valueList) {
        this.valueList = valueList;
    }

    /** set the input mask
     * @param inputMask the mask
     */
    public void setInputMask(String inputMask) {
        this.inputMask = inputMask;
    }

    /**
     * @todo Document: Getter for inputMask attribute of the FieldConfig
     *      object
     * @return the input mask string
     */
    public String getInputMask() {
        return this.inputMask;
    }

    /** set the value mask
     * @param valueMask the mask
     */
    public void setValueMask(String valueMask) {
        if (inputMask == null || valueMask == null || valueMask.length() != inputMask.length()) {
          this.valueMask = null;
          this.exPos = new int[0];
          return;
        }
        this.valueMask = valueMask;
        int i;

        // count the number of extract positions
        int c = 0;
        for (i = 0; i < valueMask.length(); i++) {
          if (valueMask.charAt(i) != this.inputMask.charAt(i)) {
            c++;
          }
        }

        // set the extract positions
        this.exPos = new int[c];
        int j = 0;
        for (i = 0; i < valueMask.length(); i++) {
          if (valueMask.charAt(i) != this.inputMask.charAt(i)) {
            this.exPos[j++] = i;
          }
        }
    }

    /**
     * @todo Document: Getter for valueMask attribute of the FieldConfig
     *      object
     * @return the input mask string
     */
    public String getValueMask() {
        return this.valueMask;
    }

    /** apply the input mask
     * @param value the value saved in database
     * @return the value to display in GUI
     */
    public String mask(String value) {
      if (value == null || value.length() == 0 || valueMask == null) {
        return value;
      }
      String ret = "";
      int c = 0, j = 0;
      for (int i = 0; i < inputMask.length(); i++) {
        if (c < exPos.length && exPos[c] == i) {
            ret += inputMask.charAt(i);
            c++;
        } else {
            // Added length check by Jeff for shorter value than mask
            if (j < value.length()) {
                ret += value.charAt(j++);
            } else {
                break;
            }
        }
      }
      return ret;
    }

    /** apply the value mask
     * @param value the value from GUI
     * @return the value to be saved in db
     */
    public String demask(String value) {
      if (value == null || value.length() == 0 || valueMask == null) {
        return value;
      }
      String ret = "";
      int c = 0;
      // Modified by Jeff for the case of shorter value
      //for (int i = 0; i < inputMask.length(); i++) {
      for (int i = 0; i < value.length(); i++) {
        if (c < exPos.length && exPos[c] == i) {
          c++;
        } else {
          if (i < value.length()) {
            ret += value.charAt(i);
          }
        }
      }
      return ret;
    }
    
    public Object copy() {
        FieldConfig fc = new FieldConfig(rootObj, objRef, name, displayName,
            guiType, maxLength, valueList);
        
        fc.rootObj = rootObj;
        fc.displayOrder = displayOrder;
        fc.extraField = extraField;
        fc.keyType = keyType;
        fc.sensitive = sensitive;
        fc.valueType = valueType;
        fc.inputMask = inputMask;
        fc.valueMask = valueMask;
        
        fc.exPos = new int[exPos.length];
        for (int i=0; i < exPos.length; i++) {
            fc.exPos[i] = exPos[i];        
        }
    
        fc.required = required;
        fc.oneOfTheseRequired = oneOfTheseRequired;
        fc.updateable = updateable;
        fc.userCode = userCode;
        fc.constraintBy = constraintBy;
        fc.usage = this.usage;
        return fc;
    }

    /**
     * RANGE_SEARCH: set the usage code for this field (how will the field be used)
     * If not set, then usage will be null.  If usage is non-null, then it is used
     * to determine display name and index position (see other comments in this 
     * class file).
     * @param usage usage
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }

    //Test method
    public static void main(String args[]) {
/*
        System.out.println(getIndex("Person___firstName"));
        System.out.println(getIndex("Person___firstName____rangeFrom"));
        System.out.println(getIndex("Person___firstName____rangeTo"));
        System.out.println(toEpathStyleString("Person___firstName"));
        System.out.println(toEpathStyleString("Person___firstName____rangeFrom"));
        System.out.println(toEpathStyleString("Person___firstName____rangeTo"));
        System.out.println(toEpathStyleString(null));
 */ 
 }


    /**
     * Added on:  02/06/2008

     * getter method for the range field
     * 
     * @return boolean
     */    
    public boolean isRange() {
        if(getIndex(getFullName()) !=0 ) {
            range = true;
        }
        return range;
    }

    public void setRange(boolean range) {
        this.range = range;
    }

    
}
