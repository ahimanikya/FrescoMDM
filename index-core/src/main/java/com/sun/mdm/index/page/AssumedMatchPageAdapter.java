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
package com.sun.mdm.index.page;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import javax.naming.Context;
import javax.naming.InitialContext;
import com.sun.mdm.index.ops.RecreateResult;
import com.sun.mdm.index.ops.TransactionMgr;
import com.sun.mdm.index.ops.TransactionMgrFactory;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSearchObject;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSummary;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.util.JNDINames;
import javax.ejb.EJBContext;
import com.sun.mdm.index.util.ConnectionUtil;
import com.sun.mdm.index.util.Localizer;



/**
 * Given a list of AssumedMatchSummaries, adapt the result for use within page
 * iterator framework. FieldsToRetrieve parameter is read from options and is
 * used to construct query object to get data as needed. Once data is retrieved
 * it remains in memory. Future optimization may include unloading some of the
 * data to conserve resources.
 * @author Dan Cidon
 */
public class AssumedMatchPageAdapter implements PageAdapter, java.io.Serializable {

    private transient final Localizer mLocalizer = Localizer.get();
    
    /** Used internally to signal forward traversal
     */    
    private static final int FORWARD = 1;
    /** Used internally to signal reverse traversal
     */    
    private static final int REVERSE = 2;
    
    /** Current row position
     */    
    private int mPosition = 0;

    /** Number of elements in the array
     */    
    private final int mNumElements;

    /** Array to store constructed assumed match summaries
     */    
    private final AssumedMatchSummary[] mObjArray;
    /** Page size
     */    
    private final int mPageSize;
    /** Forward only mode 
     */    
    private boolean mForwardOnly = false;     
 
    
    /** Creates a new instance of AssumedMatchPageAdapter
     *
     * @param list List of assumed matche summaries
     * @param options Search object
     * @exception PageException An error occured.
     */
    public AssumedMatchPageAdapter(ArrayList list, 
        AssumedMatchSearchObject options) throws PageException {

        try {
                    
            mPageSize = options.getPageSize();
            Object[] objArray = list.toArray();
            mObjArray = new AssumedMatchSummary[objArray.length];
            for (int i = 0; i < objArray.length; i++) {
                mObjArray[i] = (AssumedMatchSummary) objArray[i];
            }
            mNumElements = mObjArray.length;
        } catch (Exception e) {
            throw new PageException(mLocalizer.t("PAG502: AssumedMatchPageAdapter " +
                                        "could not be initialized: {0}", e));
        }
    }


    /** See PageAdapter
     * @param index See PageAdapter
     * @exception PageException See PageAdapter
     */
    public void setCurrentPosition(int index)
        throws PageException {
        if (index + 1 >= mNumElements) {
            throw new PageException(mLocalizer.t("PAG503: AssumedMatchPageAdapter " +
                                        "index out of bounds: {0}", index));
        }
        mPosition = index;
    }

    /** Set the forward only mode that will clear all the DataPage objects of 
     * of a given loaded page i when we start reading/loading the next page i+1.
     * @param forwardOnly forward only mode
     * @exception PageException An error occured. 
     */
    public void setReadForwardOnly(boolean forwardOnly) 
        throws PageException {
        mForwardOnly = forwardOnly;
    }    
    /** See PageAdapter
     */
    public void activate() {
    }


    /** See PageAdapter
     */
    public void close() {
    }


    /** See PageAdapter
     * @exception PageException See PageAdapter
     * @return See PageAdapter
     */
    public int count()
        throws PageException {
        return mNumElements;
    }


    /** See PageAdapter
     * @return See PageAdapter
     */
    public boolean hasNext() {
        return (mPosition < mNumElements);
    }


    /** See PageAdapter
     */
    public void idleTimeOut() {
    }


    /** See PageAdapter
     * @exception PageException See PageAdapter
     * @return See PageAdapter
     */
    public Object next()
        throws PageException {
        if (mPosition == mNumElements) {
            throw new PageException(mLocalizer.t("PAG504: AssumedMatchPageAdapter " +
                                        "has no more elements to retrieve."));
        } else {
            if (mForwardOnly && mPosition > 0 && (mPosition%mPageSize == 0)) {   
                clearPageSizeCache();
            }                         
            loadRows(FORWARD, false);
            return mObjArray[mPosition++];
        }
    }


    /** See PageAdapter
     */
    public void passivate() {
    }


    /** See PageAdapter
     * @exception PageException See PageAdapter
     * @return See PageAdapter
     */
    public Object prev()
        throws PageException {
        if (mPosition == 0) {
            throw new PageException(mLocalizer.t("PAG505: AssumedMatchPageAdapter " +
                                        "already at the beginning of the iterator."));
        } else {
            mPosition--;
            loadRows(REVERSE, false);
            return mObjArray[mPosition];
        }
    }


    /** See PageAdapter
     * @param c See PageAdapter
     * @exception PageException See PageAdapter
     */
    public void sort(Comparator c)
        throws PageException {
        setCurrentPosition(0);
        loadRows(FORWARD, true);
        Arrays.sort(mObjArray, c);
    }

    /** See PageAdapter
     * @param c See PageAdapter
     * @exception PageException See PageAdapter
     */
    public void sortSummary(Comparator c)
        throws PageException {
        setCurrentPosition(0);       
        Arrays.sort(mObjArray, c);
    }    

    /** Get JDBC connection
     * @throws PageException An error occured.
     * @return JDBC connection
     */    
    private Connection getConnection()
        throws PageException {
        Connection con = null;
        try {
            con = ConnectionUtil.getConnection();
        } catch (Exception e) {
            throw new PageException(mLocalizer.t("PAG506: AssumedMatchPageAdapter " +
                                        "could not obtain a database connection: {0}", e));
        }
        return con;
    }

    /** lear local cache of the previous page
     */      
    private void clearPageSizeCache() throws PageException { 
        
        int startPosition = mPosition - mPageSize; 
        int endPosition = mPosition -1;
        if (startPosition < 0) {
            startPosition = 0;
        }    
        for (int i = startPosition; i <= endPosition; i++) {
             mObjArray[i].setBeforeEO(null);
             mObjArray[i].setAfterEO(null);
             mObjArray[i].setNewSO(null);
        }        
    }             

    /** Ensure page data is loaded for the current row position. In addition 
     * load rows in vicinity of this row.
     *
     * @param direction FORWARD or REVERSE
     * @param allRows True if should load all rows
     * @exception PageException An error occured
     */
    private void loadRows(int direction, boolean allRows)
        throws PageException {
        if (mObjArray[mPosition].getNewSO() == null) {
            //Determine start and end indexes of records to load
            int startIndex;
            int endIndex;
            switch (direction) {
            case FORWARD:
                startIndex = mPosition;
                endIndex = mPosition + mPageSize - 1;
                if (endIndex >= mNumElements || allRows) {
                    endIndex = mNumElements - 1;
                }
                break;
            case REVERSE:
                startIndex = mPosition - mPageSize + 1;
                if (startIndex < 0 || allRows) {
                    startIndex = 0;
                }
                endIndex = mPosition;
                break;
            default:
                throw new PageException(mLocalizer.t("PAG507: Invalid load " +
                                        "direction: {0}", direction));
            }
            //Load records
            Connection con = null;
            try {
                
                TransactionMgr mTrans = TransactionMgrFactory.getInstance();
                con = getConnection();
                for (int i = startIndex; i <= endIndex; i++) {
                    AssumedMatchSummary ams = mObjArray[i];
                    String transNum = ams.getTransactionNumber();
                    RecreateResult history = 
                        mTrans.recreateObject(con, transNum);
                    EnterpriseObject beforeEO = history.getBeforeEO1();
                    EnterpriseObject afterEO = history.getAfterEO();
                    ams.setBeforeEO(beforeEO);
                    SystemObject so = afterEO.getSystemObject(ams.getSystemCode(), 
                        ams.getLID());
                    ams.setAfterEO(afterEO);
                    ams.setNewSO(so);
                }
            } catch (Exception e) {
                throw new PageException(mLocalizer.t("PAG508: Could not load " +
                                        "Assumed Match Summary records."));
            } finally {
                if (con != null) {
                    releaseConnection(con);
                }
            }
        }
    }


    /** Release JDBC connection
     * @param con JDBC connection
     * @throws PageException An error occured
     */    
    private void releaseConnection(Connection con)
        throws PageException {
        try {
            con.close();
        } catch (SQLException e) {
            throw new PageException(mLocalizer.t("PAG509: AssumedMatchPageAdapter " +
                                        "could not close the database connection: {0}", e));
        }
    }
 
}
