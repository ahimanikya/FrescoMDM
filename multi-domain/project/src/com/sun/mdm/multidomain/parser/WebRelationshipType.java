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
public class WebRelationshipType {
        private String mRelationName = null;
        
        private String mDestionObjName = null;
        
        private String mSourceObjName = null;
        
        private ArrayList mRelationFieldRef = null;
        
        public String getRelationName() {
            return this.mRelationName;
        }
        
        public void setRelationName(String relationShipTypeName) {
            this.mRelationName = relationShipTypeName;
        }
        
        public String getDestionObjName() {
            return this.mDestionObjName;
        }
        
        public void setDestionObjName(String destObjName) {
            this.mDestionObjName = destObjName;
        }
        
        public String getSourceObjName() {
            return this.mSourceObjName;
            
        }
        
        public void setSourceObjName(String sourceObjName) {
            this.mSourceObjName = sourceObjName;
        }
        
        public ArrayList getRelationFieldRef() {
            return this.mRelationFieldRef;
        }
        
        public void setRelationFieldRef(ArrayList fieldRef) {
            this.mRelationFieldRef = fieldRef;
        }

}
