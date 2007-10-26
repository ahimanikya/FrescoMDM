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
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.ObjectNode;
import java.util.ArrayList;
import java.util.Date;

/**
 * Row of data for Potential Duplicate Report
 * @author  dcidon
 */
public class PotentialDuplicateReportRow extends MultirowReportObject2 {
    
   
    private static ArrayList mFieldNames;
    private static ArrayList mFieldTypes;

    static
    {
        mFieldNames = new ArrayList();
        mFieldNames.add(PotentialDuplicateReport.ID);
        mFieldNames.add(PotentialDuplicateReport.EUID1);
        mFieldNames.add(PotentialDuplicateReport.EUID2);
        mFieldNames.add(PotentialDuplicateReport.WEIGHT);
        mFieldNames.add(PotentialDuplicateReport.CREATE_DATE);
        mFieldNames.add(PotentialDuplicateReport.CREATE_USER);
        mFieldNames.add(PotentialDuplicateReport.REASON);
        mFieldNames.add(PotentialDuplicateReport.STATUS);
        mFieldNames.add(PotentialDuplicateReport.SYSTEM_CODE);
        mFieldNames.add(PotentialDuplicateReport.RESOLVED_DATE);
        mFieldNames.add(PotentialDuplicateReport.RESOLVED_USER);
        mFieldNames.add(PotentialDuplicateReport.RESOLVED_COMMENT);
        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_FLOAT_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
    }
    
    /** Creates a new instance of PotentialDuplicateReportRow 
     *
     * @param summary  Transaction summary.
     * @param config  Merged reports configuration.
     * @throws ObjectException if any other types of errors are encountered.
     */
    public PotentialDuplicateReportRow(PotentialDuplicateSummary summary, 
                                       PotentialDuplicateReportConfig config) 
            throws ObjectException {
        super("PotentialDuplicateReportRow", mFieldNames, mFieldTypes, config);   

        setObjects(summary.getObject1(), summary.getObject2());
        
        //Set field values
        setValue(PotentialDuplicateReport.ID, summary.getId());
        setValue(PotentialDuplicateReport.EUID1, summary.getEUID1());
        setValue(PotentialDuplicateReport.EUID2, summary.getEUID2());
        setValue(PotentialDuplicateReport.SYSTEM_CODE, summary.getSystemCode());
        setValue(PotentialDuplicateReport.CREATE_USER, summary.getCreateUser());
        setValue(PotentialDuplicateReport.CREATE_DATE, summary.getCreateDate());
        setValue(PotentialDuplicateReport.WEIGHT, new Float(summary.getWeight()));
        setValue(PotentialDuplicateReport.REASON, summary.getReason());
        setValue(PotentialDuplicateReport.STATUS, summary.getStatus());
        setValue(PotentialDuplicateReport.RESOLVED_DATE, summary.getResolvedDate());
        setValue(PotentialDuplicateReport.RESOLVED_USER, summary.getResolvedUser());
        setValue(PotentialDuplicateReport.RESOLVED_COMMENT, summary.getResolvedComment());
        
    }
    
    /** Get Potential Duplicate ID.
     *
     * @throws ObjectException if an error occured.
     * @return Potential Duplicate ID.
     */    
    public String getId() throws ObjectException {
        return (String) getValue(PotentialDuplicateReport.ID);
    }
    
    /** Get EUID of the first object..
     *
     * @throws ObjectException if an error occured.
     * @return EUID of the first object.
     */    
    public String getEUID1() throws ObjectException {
        return (String) getValue(PotentialDuplicateReport.EUID1);
    }
    
    /** Get EUID of the second object..
     *
     * @throws ObjectException if an error occured.
     * @return EUID of the second object.
     */    
    public String getEUID2() throws ObjectException {
        return (String) getValue(PotentialDuplicateReport.EUID2);
    }
    
    /** Get comparison weight score.
     *
     * @throws ObjectException if an error occured.
     * @return Comparison weight score.
     */    
    public float getWeight() throws ObjectException {
        return ((Float) getValue(PotentialDuplicateReport.WEIGHT)).floatValue();
    }
    
    /** Get system code.
     *
     * @throws ObjectException if an error occured.
     * @return System code.
     */    
    public String getSystemCode() throws ObjectException {
        return (String) getValue(PotentialDuplicateReport.SYSTEM_CODE);
    }
    
    /** Get create user.
     *
     * @throws ObjectException if an error occured.
     * @return Create user.
     */    
    public String getCreateUser() throws ObjectException {
        return (String) getValue(PotentialDuplicateReport.CREATE_USER);
    }
    
    /** Get create date.
     *
     * @throws ObjectException if an error occured.
     * @return Create date.
     */    
    public java.util.Date getCreateDate() throws ObjectException {
        return (java.util.Date) getValue(PotentialDuplicateReport.CREATE_DATE);
    }
      
    /** Get reason.
     *
     * @throws ObjectException if an error occured.
     * @return Reason.
     */    
    public String getReason() throws ObjectException {
        return (String) getValue(PotentialDuplicateReport.REASON);
    }
      
    /** Get status.
     *
     * @throws ObjectException if an error occured.
     * @return Status.
     */    
    public String getStatus() throws ObjectException {
        return (String) getValue(PotentialDuplicateReport.STATUS);
    }
      
    /** Get resolved date.
     *
     * @throws ObjectException if an error occured.
     * @return Resolved date.
     */    
    public java.util.Date getResolvedDate() throws ObjectException {
        return (java.util.Date) getValue(PotentialDuplicateReport.RESOLVED_DATE);
    }
      
    /** Get user who resolved this pair of potential duplicates.
     *
     * @throws ObjectException if an error occured.
     * @return user who resolved this pair of potential duplicates.
     */    
    public String getResolvedUser() throws ObjectException {
        return (String) getValue(PotentialDuplicateReport.RESOLVED_USER);
    }
      
    /** Get Resolved comment.
     *
     * @throws ObjectException if an error occured.
     * @return Resolved comment.
     */    
    public String getResolvedComment() throws ObjectException {
        return (String) getValue(PotentialDuplicateReport.RESOLVED_COMMENT);
    }
    
}
