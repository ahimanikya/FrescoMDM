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
public class RelationshipType {
    
    private String mRelTypeName = null;
    
    private String mDestionation = null;
    
    private String mSource = null;
    
    private ArrayList<RelationFieldReference> mRelFieldRefs = new ArrayList<RelationFieldReference>();
    
    public RelationshipType(String relType, String destionation, String source,
                            ArrayList<RelationFieldReference> fieldRefs) {
        
        this.mRelTypeName = relType;
        this.mDestionation = destionation;
        this.mSource = source;
        this.mRelFieldRefs = fieldRefs;
    }

    public String getRelTypeName() {
        return mRelTypeName;
    }

    public String getDestionation() {
        return mDestionation;
    }

    public String getSource() {
        return mSource;
    }

    public ArrayList<RelationFieldReference> getRelFieldRefs() {
        return mRelFieldRefs;
    }
    
    public void addRelFieldRef(RelationFieldReference fieldRef) {
        mRelFieldRefs.add(fieldRef);
    }
    
    public void deleteRelFieldRef(RelationFieldReference fieldRef) {
        for (RelationFieldReference field : mRelFieldRefs) {
            if (field.getFieldName().equals(fieldRef.getFieldName())){
                mRelFieldRefs.remove(field);
                break;
            }
        }
    }

}
