/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */
package com.sun.mdm.index.report;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Base configuration class for reports that contain one object image
 * @author  dcidon
 */
public abstract class MultirowReportConfig1 extends ReportConfig {
    
    private final HashMap mObjectFieldAttributeMap1 = new HashMap();
    private final HashMap mTransactionFieldAttributeMap = new HashMap();
    private final EPathArrayList mObjectFields = new EPathArrayList();
    private final LinkedList mTransactionFields = new LinkedList();
    
    // Creates a new instance of Config 
    public MultirowReportConfig1() {
    }
    
    /** 
     * Add a Transaction Field.
     *
     * @param field Name of the field.
     * @param label Label for the field.
     * @param length Length of the field.
     */
    public void addTransactionField(String field, String label, int length) {
        mTransactionFieldAttributeMap.put(field, new FieldAttributeStruct(label, length, false, false));
        mTransactionFields.add(field);
    }
    
    /** 
     * Add a Transaction Field.
     *
     * @param field Name of the field.
     * @param label Label for the field.
     * @param length Length of the field.
     */
    public void addTransactionFieldVisibleLine1(String field, String label, int length) {
        mTransactionFieldAttributeMap.put(field, new FieldAttributeStruct(label, length, true, false));
        mTransactionFields.add(field);
    }
    
    /** 
     * Add a Transaction Field.
     *
     * @param field Name of the field.
     * @param label Label for the field.
     * @param length Length of the field.
     */
    public void addTransactionFieldVisibleLine2(String field, String label, int length) {
        mTransactionFieldAttributeMap.put(field, new FieldAttributeStruct(label, length, true, true));
        mTransactionFields.add(field);
    }
    
    /** 
     * Add an Object Field.
     *
     * @param epath EPath of the object field.
     * @param label1 Label for the field.
     * @param length Length of the field.
     */
    public void addObjectField(String epath, String label1, int length) 
    throws EPathException {
        mObjectFieldAttributeMap1.put(epath, new FieldAttributeStruct(label1, length, false, false));
        mObjectFields.add(epath);
    }
    
    /** 
     * Gets the Transaction Fields.
     *
     * @return Transaction Fields.
     */
    public List getTransactionFields() {
        return mTransactionFields;
    }
    
    /** 
     * Gets the EPaths for all Object Fields.
     *
     * @return EPaths for all Object Fields.
     */
    public EPathArrayList getObjectFields() {
        return mObjectFields;
    }
    
    /** 
     * Gets the field label for an EPath.
     *
     * @return Field label for an EPath.
     */
    public String getObjectFieldLabel1(String epath) {
        FieldAttributeStruct fieldAttr = (FieldAttributeStruct) mObjectFieldAttributeMap1.get(epath);
        return fieldAttr.fieldLabel;
    }
    
    /** 
     * Gets the field length for an EPath.
     *
     * @return Field length for an EPath.
     */
    public int getObjectFieldLength(String epath) {
        FieldAttributeStruct fieldAttr = (FieldAttributeStruct) mObjectFieldAttributeMap1.get(epath);
        return fieldAttr.fieldLength;
    }
    
    /** 
     * Gets the field label for a Transaction Field.
     *
     * @return Field label for a Transaction Field.
     */
    public String getTransactionFieldLabel(String field) {
        FieldAttributeStruct fieldAttr = (FieldAttributeStruct) mTransactionFieldAttributeMap.get(field);
        return fieldAttr.fieldLabel;
    }
    
    /** 
     * Gets the field length for a Transaction Field.
     *
     * @return Field length for a Transaction Field.
     */
    public int getTransactionFieldLength(String field) {
        FieldAttributeStruct fieldAttr = (FieldAttributeStruct) mTransactionFieldAttributeMap.get(field);
        return fieldAttr.fieldLength;
    }
    
    /** 
     * Gets visible line1 for a field.
     *
     * @return Visible line1 for a field.
     */
    public boolean getTransactionFieldVisibleLine1(String field) {
        FieldAttributeStruct fieldAttr = (FieldAttributeStruct) mTransactionFieldAttributeMap.get(field);
        return fieldAttr.visibleLine1;
    }
    
    /** 
     * Gets visible line2 for a field.
     *
     * @return Visible line2 for a field.
     */
    public boolean getTransactionFieldVisibleLine2(String field) {
        FieldAttributeStruct fieldAttr = (FieldAttributeStruct) mTransactionFieldAttributeMap.get(field);
        return fieldAttr.visibleLine2;
    }
}
