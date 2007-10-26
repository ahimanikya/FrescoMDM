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
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.TransactionObject;
import java.util.ArrayList;
import java.util.Date;

/**
 * Row of data for Update Report
 * @author  dcidon
 */
public class UpdateReportRow extends MultirowReportObject2 {
    
   
    private static ArrayList mFieldNames;
    private static ArrayList mFieldTypes;

    static
    {
        mFieldNames = new ArrayList();
        mFieldNames.add(UpdateReport.EUID);
        mFieldNames.add(UpdateReport.LID);
        mFieldNames.add(UpdateReport.TIMESTAMP);
        mFieldNames.add(UpdateReport.SYSTEM_USER);
        mFieldNames.add(UpdateReport.SYSTEM_CODE);
        mFieldNames.add(UpdateReport.TRANSACTION_NUMBER);
        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
    }
    
    /** Creates a new instance of UpdateReportRow 
     *
     * @param summary  Transaction summary.
     * @param config  Update report configuration.
     * @throws EPathException if EPath errors are encountered.
     * @throws ObjectException if any other types of errors are encountered.
     */
    public UpdateReportRow(TransactionSummary summary, 
                           UpdateReportConfig config) 
            throws ObjectException, EPathException {
                
        super("UpdateReportRow", mFieldNames, mFieldTypes, config);   
        
        //Both objects need to be reduced because the MC lookup method returns all fields
        EPathArrayList fields = config.getObjectFields();
        ObjectNode obj1 = summary.getEnterpriseObjectHistory().getBeforeEO1().getSBR().getObject();
        ObjectNode obj2 = summary.getEnterpriseObjectHistory().getAfterEO().getSBR().getObject();
        obj1 = ObjectNodeReducer.reduceObjectNode(obj1, fields);
        obj2 = ObjectNodeReducer.reduceObjectNode(obj2, fields);
        setObjects(obj1, obj2);
        
        //Set field values
        TransactionObject tObj = summary.getTransactionObject();
        setValue(UpdateReport.EUID, tObj.getEUID());
        setValue(UpdateReport.LID, tObj.getLID());
        setValue(UpdateReport.SYSTEM_CODE, tObj.getSystemCode());
        setValue(UpdateReport.SYSTEM_USER, tObj.getSystemUser());
        setValue(UpdateReport.TIMESTAMP, tObj.getTimeStamp());
        setValue(UpdateReport.TRANSACTION_NUMBER, tObj.getTransactionNumber());
     
    }
    
    /** Get local ID.
     *
     * @throws ObjectException if an error occured.
     * @return local ID.
     */    
    public String getLID() throws ObjectException {
        return (String) getValue(UpdateReport.LID);
    }
    
    /** Get EUID.
     *
     * @throws ObjectException if an error occured.
     * @return EUID.
     */    
    public String getEUID() throws ObjectException {
        return (String) getValue(UpdateReport.EUID);
    }
    
    /** Get system code.
     *
     * @throws ObjectException if an error occured.
     * @return system code.
     */    
    public String getSystemCode() throws ObjectException {
        return (String) getValue(UpdateReport.SYSTEM_CODE);
    }
    
    /** Get system user.
     *
     * @throws ObjectException if an error occured.
     * @return system user.
     */    
    public String getSystemUser() throws ObjectException {
        return (String) getValue(UpdateReport.SYSTEM_USER);
    }
    
    /** Get timestamp.
     *
     * @throws ObjectException if an error occured.
     * @return create date.
     */    
    public java.util.Date getTimestamp() throws ObjectException {
        return (java.util.Date) getValue(UpdateReport.TIMESTAMP);
    }
    
    /** Get transaction number.
     *
     * @throws ObjectException if an error occured.
     * @return transaction number.
     */    
    public String getTransactionNumber() throws ObjectException {
        return (String) getValue(UpdateReport.TRANSACTION_NUMBER);
    }  
    
}
