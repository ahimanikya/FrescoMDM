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
    /*
     * MultiDomainModel.xml
     * <relationships>
     *   <relationshp-type>
     *     <attributes>
     */
        String name;
        String type;        // Fixed 0, Extended 1
        String columnName;  // which column name in the table does it bind to
        String displayName;
        boolean searchable;
        boolean isRequired;
        String value;
        String defaultValue;
        String dataType; 

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

        public void setIsRequired(boolean isRequired) {
            this.isRequired = isRequired;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSearchable(boolean searchable) {
            this.searchable = searchable;
        }

        public void setType(String type) {
            this.type = type;
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

        public boolean isIsRequired() {
            return isRequired;
        }

        public String getName() {
            return name;
        }

        public boolean isSearchable() {
            return searchable;
        }

        public String getType() {
            return type;
        }

}
