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
public class WebRelationshipTypes {
    
        private ArrayList<WebRelationshipType> mWebRelationshipList;
        
        public ArrayList<WebRelationshipType> getWebRelationshipList() {
            return this.mWebRelationshipList;
        }
        
        public void addWebRelationshipType(WebRelationshipType type) {
            mWebRelationshipList.add(type);
        }
        
        public WebRelationshipType getRelationshipType(String typeName, String souceDomain, String destDomain) {
            for (WebRelationshipType relType : mWebRelationshipList){
                if (relType.getRelationName().equals(typeName) &&
                    relType.getDestionObjName().equals(destDomain) &&
                    relType.getSourceObjName().equals(souceDomain)) {
                    return relType;
                }
            } 
            
            return null;
        }
        public void deleteWebRelationshipType(String typeName, String sourceDomain, String destDomain) throws Exception {
            
            WebRelationshipType relationInstance = getRelationshipType(typeName, sourceDomain, destDomain);
            if (relationInstance != null) {
                mWebRelationshipList.remove(relationInstance);
            } else {
                throw new Exception("Could not find Specific relationship to remove. Type Name [" + typeName + "], " 
                        + "Source Domain[" + sourceDomain + "], Destination Domain [" + destDomain + "].");
            }
        }

}
