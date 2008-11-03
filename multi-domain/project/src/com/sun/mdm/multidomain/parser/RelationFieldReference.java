/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mdm.multidomain.parser;

/**
 *
 * @author wee
 */
public class RelationFieldReference {

    private String mFieldName = null;
    private String mFieldDisplayName = null;
    private int mDisplayOrder = -1;
    private int mMaxLength = -1;
    private String mGuiType = null;
    private String mValueList = null;
    private String mValueType = null;
    private boolean mKeyType = false;
    private String mInputMask = null;
    private String mOutputMask = null;
    private boolean mSensitive = false;

    public RelationFieldReference(String fieldName, String displayName, int displayOrder,
            int maxLen, String guiType, String valueList,
            String valueType, boolean keyType) {

        this.mFieldName = fieldName;
        this.mFieldDisplayName = displayName;
        this.mDisplayOrder = displayOrder;
        this.mGuiType = guiType;
        this.mMaxLength = maxLen;
        this.mValueList = valueList;
        this.mValueType = valueType;
        this.mKeyType = keyType;
    }
    
     public RelationFieldReference(String fieldName, String displayName, int displayOrder,
            int maxLen, String guiType, String valueList,
            String valueType, String inputMask, String outputMask,
            boolean sensitive) {

        this.mFieldName = fieldName;
        this.mFieldDisplayName = displayName;
        this.mDisplayOrder = displayOrder;
        this.mGuiType = guiType;
        this.mMaxLength = maxLen;
        this.mValueList = valueList;
        this.mValueType = valueType;
        this.mInputMask = inputMask;
        this.mOutputMask = outputMask;
        this.mSensitive = sensitive;
    }

    public String getFieldName() {
        return this.mFieldName;
    }

    public void setFieldName(String fieldName) {
        this.mFieldName = fieldName;
    }

    public String getFieldDisplayName() {
        return this.mFieldDisplayName;
    }

    public void setFieldDisplayName(String displayName) {
        this.mFieldDisplayName = displayName;
    }

    public int getDisplayOrder() {
        return this.mDisplayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.mDisplayOrder = displayOrder;
    }

    public int getMaxLength() {
        return this.mMaxLength;
    }

    public void setMaxLength(int maxLength) {
        this.mMaxLength = maxLength;
    }

    public String getGuiType() {
        return this.mGuiType;
    }

    public void setGuiType(String guiType) {
        this.mGuiType = guiType;
    }

    public String getValueList() {
        return this.mValueList;
    }

    public void setValueList(String valueList) {
        this.mValueList = valueList;
    }

    public String getValueType() {
        return this.mValueType;
    }

    public boolean getKeyType() {
        return this.mKeyType;
    }

    public String getInputMask() {
        return mInputMask;
    }

    public void setInputMask(String inputMask) {
        this.mInputMask = inputMask;
    }

    public String getOutputMask() {
        return mOutputMask;
    }

    public void setOutputMask(String outputMask) {
        this.mOutputMask = outputMask;
    }

    public boolean isSensitive() {
        return mSensitive;
    }

    public void setSensitive(boolean sensitive) {
        this.mSensitive = sensitive;
    }
}
