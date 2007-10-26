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
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSummary;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.ObjectNode;
import java.util.ArrayList;
import java.util.Date;

/**
 * Row of data for Assumed Match Report
 * @author  dcidon
 */
public class AssumedMatchReportRow extends MultirowReportObject2 {
    
   
    private static ArrayList mFieldNames;
    private static ArrayList mFieldTypes;

    static
    {
        mFieldNames = new ArrayList();
        mFieldNames.add(AssumedMatchReport.ID);
        mFieldNames.add(AssumedMatchReport.EUID);
        mFieldNames.add(AssumedMatchReport.WEIGHT);
        mFieldNames.add(AssumedMatchReport.CREATE_DATE);
        mFieldNames.add(AssumedMatchReport.CREATE_USER);
        mFieldNames.add(AssumedMatchReport.SYSTEM_CODE);
        mFieldNames.add(AssumedMatchReport.LID);
        mFieldNames.add(AssumedMatchReport.TRANSACTION_NUMBER);
        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_FLOAT_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
    }
    
    /** Creates a new instance of AssumedMatchReportRow 
     *
     * @param summary  Assumed match summary.
     * @param config  Assumed match configuration.
     * @throws EPathException if EPath errors are encountered.
     * @throws ObjectException if any other types of errors are encountered.
     */
    public AssumedMatchReportRow(AssumedMatchSummary summary, AssumedMatchReportConfig config)
            throws ObjectException, EPathException {
                
        super("AssumedMatchReportRow", mFieldNames, mFieldTypes, config);   
        
        //First child is the before SBR image
        //Second child is the new SO image
        //Both objects need to be reduced because the MC lookup method returns all fields
        EPathArrayList fields = config.getObjectFields();
        ObjectNode obj1 = 
            ObjectNodeReducer.reduceObjectNode(summary.getBeforeSBR().getObject(), fields);
        ObjectNode obj2 = 
            ObjectNodeReducer.reduceObjectNode(summary.getNewSO().getObject(), fields);
        setObjects(obj1, obj2);
        
        //Set field values
        setValue(AssumedMatchReport.ID, summary.getId());
        setValue(AssumedMatchReport.EUID, summary.getEUID());
        setValue(AssumedMatchReport.SYSTEM_CODE, summary.getSystemCode());
        setValue(AssumedMatchReport.LID, summary.getLID());
        setValue(AssumedMatchReport.CREATE_USER, summary.getCreateUser());
        setValue(AssumedMatchReport.CREATE_DATE, summary.getCreateDate());
        setValue(AssumedMatchReport.TRANSACTION_NUMBER, summary.getTransactionNumber());
        setValue(AssumedMatchReport.WEIGHT, new Float(summary.getWeight()));
    }
    
    /** Get new system object image added by assumed match.
     *
     * @return New system object image.
     */    
    public ObjectNode getNewSOImage() {
        return getObject2();
    }
    
    /** Get before image of SBR object.
     *
     * @return Before image of SBR object.
     */    
    public ObjectNode getBeforeSBRImage() {
        return getObject1();
    }
    
    /** Get assumed match id.
     *
     * @throws ObjectException if an error occured.
     * @return Assumed match id.
     */    
    public String getId() throws ObjectException {
        return (String) getValue(AssumedMatchReport.ID);
    }
    
    /** Get EUID.
     *
     * @throws ObjectException if an error occured.
     * @return EUID.
     */    
    public String getEUID() throws ObjectException {
        return (String) getValue(AssumedMatchReport.EUID);
    }
    
    /** Get local id.
     *
     * @throws ObjectException if an error occured.
     * @return local id.
     */    
    public String getLID() throws ObjectException {
        return (String) getValue(AssumedMatchReport.LID);
    }
    
    /** Get transaction number.
     *
     * @throws ObjectException if an error occured.
     * @return transaction number.
     */    
    public String getTransactionNumber() throws ObjectException {
        return (String) getValue(AssumedMatchReport.TRANSACTION_NUMBER);
    }

    /** Get comparison weight.
     *
     * @throws ObjectException if an error occured.
     * @return comparison score.
     */    
    public float getWeight() throws ObjectException {
        return ((Float) getValue(AssumedMatchReport.WEIGHT)).floatValue();
    }

    /** Get system code.
     *
     * @throws ObjectException if an error occured.
     * @return system code.
     */    
    public String getSystemCode() throws ObjectException {
        return (String) getValue(AssumedMatchReport.SYSTEM_CODE);
    }

    /** Get create user.
     *
     * @throws ObjectException if an error occured.
     * @return create user.
     */    
    public String getCreateUser() throws ObjectException {
        return (String) getValue(AssumedMatchReport.CREATE_USER);
    }

    /** Get create date.
     * @throws ObjectException if an error occured.
     * @return create date.
     */    
    public java.util.Date getCreateDate() throws ObjectException {
        return (java.util.Date) getValue(AssumedMatchReport.CREATE_DATE);
    }
}
