/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

/**
 *
 * @author kkao
 */
public class Attribute {
    public Attribute() {
        
    }
    /*
     * MultiDomainModel.xml
     * <relationships>
     *   <relationshp-type>
     *     <attributes>
     */
        String name = "";
        String type = "";       // Fixed 0, Extended 1
        String columnName = "";  // which column name in the table does it bind to
        String displayName = "";
        String searchable = "true";
        String required = "true";
        String value = "";
        String defaultValue = "";
        String dataType = "";
        String attributeID = "";
    public Attribute(String name, String columnName, String dataType, String defaultValue,
            String searchable, String required, String attributeID) {
        this.name = name;
        this.columnName = columnName;
        this.dataType = dataType;
        this.defaultValue = defaultValue;
        this.searchable = searchable;
        this.required = required;
        this.attributeID = attributeID;
    }

       
        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public void setRequired(String required) {
            this.required = required;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSearchable(String searchable) {
            this.searchable = searchable;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAttributeID() {
            return attributeID;
        }

        public void setAttributeID(String attributeID) {
            this.attributeID = attributeID;
        }

        public String getColumnName() {
            return columnName;
        }

        public String getDataType() {
            return dataType;
        }

        public String getValue() {
            return value;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public String getDisplayName() {
            return displayName;
        }

        public boolean isRequired() {
            return required.equals("true");
        }

        public String getName() {
            return name;
        }

        public String getRequired() {
            return required;
        }

        public String getSearchable() {
            return searchable;
        }

        public boolean isSearchable() {
            return searchable.equals("true");
        }

        public String getType() {
            return type;
        }

}
