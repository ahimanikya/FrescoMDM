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
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.TransactionObject;
import java.util.ArrayList;
import java.util.Date;

/** Row of data for Deactivate Report
 * 
 * @author  dcidon
 */
public class DeactivateReportRow extends MultirowReportObject1 {
    
   
    private static ArrayList mFieldNames;
    private static ArrayList mFieldTypes;

    static
    {
        mFieldNames = new ArrayList();
        mFieldNames.add(DeactivateReport.EUID);
        mFieldNames.add(DeactivateReport.TIMESTAMP);
        mFieldNames.add(DeactivateReport.SYSTEM_USER);
        mFieldNames.add(DeactivateReport.TRANSACTION_NUMBER);
        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
    }
    
    /** Creates a new instance of DeactivateReportRow 
     *
     * @param summary Transaction summary.
     * @param config  Configuration instance.
     */
    public DeactivateReportRow(TransactionSummary summary, 
                               DeactivateReportConfig config) 
            throws ObjectException, EPathException {
                
        super("DeactivateReportRow", mFieldNames, mFieldTypes, config);   

        EPathArrayList fields = config.getObjectFields();
        ObjectNode obj1 = summary.getEnterpriseObjectHistory().getAfterEO().getSBR().getObject();
        obj1 = ObjectNodeReducer.reduceObjectNode(obj1, fields);
        setObject1(obj1);
        
        //Set field values
        TransactionObject tObj = summary.getTransactionObject();
        setValue(DeactivateReport.EUID, tObj.getEUID());
        setValue(DeactivateReport.SYSTEM_USER, tObj.getSystemUser());
        setValue(DeactivateReport.TIMESTAMP, tObj.getTimeStamp());
        setValue(DeactivateReport.TRANSACTION_NUMBER, tObj.getTransactionNumber());
     
    }
    
    /** Get the system object image.
     *
     * @return system object image
     */    
    public ObjectNode getImage() {
        return getObject1();
    }
    
    /** Get EUID.
     * 
     * @throws ObjectException if an error is encountered.
     * @return EUID
     */    
    public String getEUID() throws ObjectException {
        return (String) getValue(DeactivateReport.EUID);
    }

    /** Get system user.
     *
     * @throws ObjectException if an error is encountered.
     * @return system user
     */    
    public String getSystemUser() throws ObjectException {
        return (String) getValue(DeactivateReport.SYSTEM_USER);
    }
    
    /** Get timestamp.
     *
     * @throws ObjectException if an error is encountered.
     * @return create date.
     */    
    public java.util.Date getTimestamp() throws ObjectException {
        return (java.util.Date) getValue(DeactivateReport.TIMESTAMP);
    }
    
    /** Get transaction number.
     *
     * @throws ObjectException if an error is encountered.
     * @return transaction number
     */    
    public String getTransactionNumber() throws ObjectException {
        return (String) getValue(DeactivateReport.TRANSACTION_NUMBER);
    }  
    
}
