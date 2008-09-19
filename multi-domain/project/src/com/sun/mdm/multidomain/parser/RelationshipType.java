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
    public static final String FIXED_ATTRIBUTE_TYPE = "0";
    public static final String EXTENDED_ATTRIBUTE_TYPE = "1";

    /*
     * RelationshipModel.xml
     * <relationships>
     *   <relationshp-type>
     *     <attributes>
     */
        String name;
        String type;        // Relationship/Hierachy/Group/Category
        String plugin;      // Relationship plugin
        String relTypeId;   // primary key which is generated, unique across domains, used during run time Relationships
        String displayName;
        String sourceRelationshipName; //
        String targetRelationshipName;
        String sourceDomain;
        String targetDomain;
        int direction; // 1 one direction, 2 bidirectional
        boolean includeEffectiveFrom;
        boolean includeEffectiveTo;
        boolean includePurgeDate;
        boolean effectiveFromRequired;
        boolean effectiveToRequired;
        boolean purgeDateRequired;
        ArrayList <Attribute> attributes;

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
        public void setAttributes(ArrayList<Attribute> attributes) {
            this.attributes = attributes;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public void setEffectiveFromRequired(boolean effectiveFromRequired) {
            this.effectiveFromRequired = effectiveFromRequired;
        }

        public void setEffectiveToRequired(boolean effectiveToRequired) {
            this.effectiveToRequired = effectiveToRequired;
        }

        public void setIncludeEffectiveFrom(boolean includeEffectiveFrom) {
            this.includeEffectiveFrom = includeEffectiveFrom;
        }

        public void setIncludeEffectiveTo(boolean includeEffectiveTo) {
            this.includeEffectiveTo = includeEffectiveTo;
        }

        public void setIncludePurgeDate(boolean includePurgeDate) {
            this.includePurgeDate = includePurgeDate;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPlugin(String plugin) {
            this.plugin = plugin;
        }

        public void setPurgeDateRequired(boolean purgeDateRequired) {
            this.purgeDateRequired = purgeDateRequired;
        }

        public void setRelTypeId(String relTypeId) {
            this.relTypeId = relTypeId;
        }

        public void setSourceRelationshipName(String sourceRelationshipName) {
            this.sourceRelationshipName = sourceRelationshipName;
        }

        public void setSourceDomain(String sourceDomain) {
            this.sourceDomain = sourceDomain;
        }

        public void setTargetDomain(String targetDomain) {
            this.targetDomain = targetDomain;
        }

        public void setTargetRelationshipName(String targetRelationshipName) {
            this.targetRelationshipName = targetRelationshipName;
        }

        public void setType(String type) {
            this.type = type;
        }

        public ArrayList<Attribute> getAttributesByType(String type) {
            ArrayList al = new ArrayList();
            for (int i=0; i<attributes.size(); i++) {
                Attribute attr = (Attribute) attributes.get(i);
                if (attr.type.equals(type)) {
                    al.add(attr); 
                }
            }
            return al;

        }

        public ArrayList<Attribute> getFixedAttributes() {
            return getAttributesByType(FIXED_ATTRIBUTE_TYPE);
        }

        public ArrayList<Attribute> getExtendedAttributes() {
            return getAttributesByType(EXTENDED_ATTRIBUTE_TYPE);
        }

        public ArrayList<Attribute> getAttributes() {
            return attributes;
        }

        public int getDirection() {
            return direction;
        }

        public String getDisplayName() {
            return displayName;
        }

        public boolean isEffectiveFromRequired() {
            return effectiveFromRequired;
        }

        public boolean isEffectiveToRequired() {
            return effectiveToRequired;
        }

        public boolean isIncludeEffectiveFrom() {
            return includeEffectiveFrom;
        }

        public boolean isIncludeEffectiveTo() {
            return includeEffectiveTo;
        }

        public boolean isIncludePurgeDate() {
            return includePurgeDate;
        }

        public String getName() {
            return name;
        }

        public String getPlugin() {
            return plugin;
        }

        public boolean isPurgeDateRequired() {
            return purgeDateRequired;
        }

        public String getRelTypeId() {
            return relTypeId;
        }

        public String getSourceRelationshipName() {
            return sourceRelationshipName;
        }

        public String getSourceDomain() {
            return sourceDomain;
        }

        public String getTargetDomain() {
            return targetDomain;
        }

        public String getTargetRelationshipName() {
            return targetRelationshipName;
        }

        public String getType() {
            return type;
        }
}
