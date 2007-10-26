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
 * Row of data for Unmerge Report
 * @author  dcidon
 */
public class UnmergeReportRow extends MultirowReportObject2 {
    
   
    private static ArrayList mFieldNames;
    private static ArrayList mFieldTypes;

    static
    {
        mFieldNames = new ArrayList();
        mFieldNames.add(UnmergeReport.EUID1);
        mFieldNames.add(UnmergeReport.EUID2);
        mFieldNames.add(UnmergeReport.TIMESTAMP);
        mFieldNames.add(UnmergeReport.SYSTEM_USER);
        mFieldNames.add(UnmergeReport.TRANSACTION_NUMBER);
        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
    }
    
    /** Creates a new instance of UnmergeReportRow 
     *
     * @param summary  Transaction summary.
     * @param config  Unmerge report configuration.
     * @throws EPathException if EPath errors are encountered.
     * @throws ObjectException if any other types of errors are encountered.
     */
    public UnmergeReportRow(TransactionSummary summary, 
                            UnmergeReportConfig config) 
            throws ObjectException, EPathException {
                
        super("UnmergeReportRow", mFieldNames, mFieldTypes, config);   

        // Both objects need to be reduced because the MasterController 
        // lookup method returns all fields
        EPathArrayList fields = config.getObjectFields();
        ObjectNode obj1 
            = summary.getEnterpriseObjectHistory().getAfterEO().getSBR().getObject();
        ObjectNode obj2 
            = summary.getEnterpriseObjectHistory().getAfterEO2().getSBR().getObject();
        obj1 = ObjectNodeReducer.reduceObjectNode(obj1, fields);
        obj2 = ObjectNodeReducer.reduceObjectNode(obj2, fields);
        setObjects(obj1, obj2);
        
        //Set field values
        TransactionObject tObj = summary.getTransactionObject();
        setValue(UnmergeReport.EUID1, tObj.getEUID());
        setValue(UnmergeReport.EUID2, tObj.getEUID2());
        setValue(UnmergeReport.SYSTEM_USER, tObj.getSystemUser());
        setValue(UnmergeReport.TIMESTAMP, tObj.getTimeStamp());
        setValue(UnmergeReport.TRANSACTION_NUMBER, tObj.getTransactionNumber());
     
    }
    
    /** Get EUID1.
     *
     * @throws ObjectException if an error occured.
     * @return EUID1.
     */    
    public String getEUID1() throws ObjectException {
        return (String) getValue(UnmergeReport.EUID1);
    }
    
    /** Get EUID2.
     *
     * @throws ObjectException if an error occured.
     * @return EUID2.
     */    
    public String getEUID2() throws ObjectException {
        return (String) getValue(UnmergeReport.EUID2);
    }
    
    /** Get system user.
     *
     * @throws ObjectException if an error occured.
     * @return system user.
     */    
    public String getSystemUser() throws ObjectException {
        return (String) getValue(UnmergeReport.SYSTEM_USER);
    }
    /** Get timestamp.
     *
     * @throws ObjectException if an error occured.
     * @return create date.
     */    
    public java.util.Date getTimestamp() throws ObjectException {
        return (java.util.Date) getValue(UnmergeReport.TIMESTAMP);
    }
    /** Get transaction number.
     *
     * @throws ObjectException if an error occured.
     * @return transaction number.
     */    
    public String getTransactionNumber() throws ObjectException {
        return (String) getValue(UnmergeReport.TRANSACTION_NUMBER);
    }  
    
}
