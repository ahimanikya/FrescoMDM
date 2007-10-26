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
import java.util.Date;

/**
 * Base class for all report configurations
 * @author  dcidon
 */
public abstract class ReportConfig implements java.io.Serializable {
    
    private Date mStartDate;
    private Date mEndDate;

    private Integer mMaxResultSize;
    private Integer mPageSize;
    
    // Creates a new instance of AbstractConfig 
    public ReportConfig() {
    }
    
    /** Sets the start date.
     *
     * @param date Start date that should be set.
     */
    public void setStartDate(Date date) {
        mStartDate = date;
    }
    
    /** Sets the end date.
     *
     * @param date End date that should be set.
     */
    public void setEndDate(Date date) {
        mEndDate = date;        
    }

    /** Sets the maximum number of records to be in the results.
     *
     * @param maxResultSize  Maximum records to be in the results.
     */
    public void setMaxResultSize(Integer maxResultSize) {
        mMaxResultSize = maxResultSize;
    }
        
    /** Sets the maximum number of records to be in the page.
     *
     * @param maxResultSize  Maximum records to be in the page.
     */
    public void setPageSize(Integer pageSize) {
        mPageSize = pageSize;
    }
    
    /** Gets the start date.
     *
     * @return Start Date.
     */
    public Date getStartDate() {
        return mStartDate;
    }
    
    /** Gets the start date.
     *
     * @return End Date.
     */
    public Date getEndDate() {
        return mEndDate;
    }
        
    /** Gets the maximum result size for the search.
     *
     * @return The maximum result size.
     */
    public Integer getMaxResultSize() {
        return mMaxResultSize;
    }
    
    /** Gets the maximum page size for the search.
     *
     * @return The maximum page size.
     */
    public Integer getPageSize() {
        return mPageSize;
    }
}
