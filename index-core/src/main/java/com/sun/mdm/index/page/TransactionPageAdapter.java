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
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import com.sun.mdm.index.master.search.transaction.TransactionSearchObject;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.ops.RecreateResult;
import com.sun.mdm.index.ops.TransactionMgr;
import com.sun.mdm.index.ops.TransactionMgrFactory;
import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.util.ConnectionUtil;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * Adapter for transaction summaries
 * @author cychow
 */
public class TransactionPageAdapter implements PageAdapter, java.io.Serializable {
    
    /** Used to indicate forward traversal
     */    
    private static final int FORWARD = 1;
    
    /** Used to indicate reverse traversal
     */    
    private static final int REVERSE = 2;

    /** Current row position
     */    
    private int mPosition = 0;
    
    /** Transaction summaries that have already been loaded
     */    
    private TransactionSummary[] mObjArray = null;
    
    /** Number of elements in data source
     */    
    private final int mNumElements;
    
    /** Page size
     */    
    private final int mPageSize;
    
    /** Forward only mode 
     */ 
    private boolean mForwardOnly = false;     
    
    /** Handle to transaction manager
     */    
    private transient TransactionMgr mTrans;

    private transient Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient Localizer mLocalizer = Localizer.get();

    /** Creates a new instance of TransactionPageAdapter
     *
     * @param transArray Array of transaction objects
     * @param searchObj Search Object
     * @exception PageException An error occured.
     */
    public TransactionPageAdapter(TransactionObject[] transArray, 
        TransactionSearchObject searchObj) throws PageException {
        try {
            mPageSize = searchObj.getPageSize();
            mNumElements = (transArray.length > searchObj.getMaxElements()  
                ? searchObj.getMaxElements() : transArray.length);

            mTrans = TransactionMgrFactory.getInstance();

            mObjArray = new TransactionSummary[mNumElements];

            // Create TransactionSummary for each transaction object
            for (int i = 0, len = mNumElements; i < len; i++) {
                TransactionObject transObj = (TransactionObject) transArray[i];
                TransactionSummary transSummary = 
                    new TransactionSummary(transObj);
                // default value of the valid transaction flag
                transSummary.setValidTransaction(true);
                mObjArray[i] = transSummary;
            }
        } catch (Exception e) {
            throw new PageException(mLocalizer.t("PAG538: TransactionPageAdapter " +
                                        "could not be initialized: {0}", e));
        }
    }

    
    /** See PageAdapter
     * @param index See PageAdapter
     * @exception PageException See PageAdapter
     */
    public void setCurrentPosition(int index)
        throws PageException {
        if ((index < 0) || (index >= mNumElements)) {
            throw new PageException(mLocalizer.t("PAG539: TransactionPageAdapter " +
                                        "index out of bounds: {0}", index));
        }
        mPosition = index;
    }

    /** Set the forward only mode that will clear all the DataPage objects of 
     * of a given loaded page i when we start reading/loading the next page i+1.
     * 
     * @param forwardOnly the 
     */
    public void setReadForwardOnly(boolean forwardOnly) 
        throws PageException {
        mForwardOnly = forwardOnly;
    }         
    /**
     * When the stateful session bean activates, it will first call this method.
     */
    public void activate() {
    }

    /**
     * When the stateful session bean is removed, it will first call this
     * method.
     */
    public void close() {
    }


    /** Get a count of all the objects in the data source
     *
     * @exception PageException See PageAdapter
     * @return See PageAdapter
     */
    public int count()
        throws PageException {
        return mNumElements;
    }


    /** Indicate if there is another object
     *
     * @exception PageException See PageAdapter
     * @return See PageAdapter
     */
    public boolean hasNext()
        throws PageException {
        return (mPosition < mNumElements);
    }


    /**
     * After the timeout period has expired, this method is called to allow the
     * data source to perform resource conservation activities.
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
            throw new PageException(mLocalizer.t("PAG540: TransactionPageAdapter " +
                                        "has no more elements to load."));
        } else {
            if (mForwardOnly && mPosition > 0 && (mPosition%mPageSize == 0)) {   
                clearPageSizeCache();
            }               
            loadRows(FORWARD);
            return mObjArray[mPosition++];
        }
    }


    /**
     * When the stateful session bean passivates, it will first call this method
     * on the data source. If there are any resources that must be freed prior
     * to passivation, this is the time to do it.
     */
    public void passivate() {
    }


    /** See PageAdapter
     * @exception PageException See PageAdapter
     * @return See PageAdapter
     */
    public Object prev()
        throws PageException {
        if (mPosition == -1) {
            throw new PageException(mLocalizer.t("PAG541: Already at beginning of iterator."));
        } else {
            loadRows(REVERSE);
            return mObjArray[mPosition--];
        }
    }


    /** See PageAdapter
     * @param c See PageAdapter
     * @exception PageException Sort array based on comparator
     */
    public void sort(Comparator c)
        throws PageException {
        setCurrentPosition(0);
        Arrays.sort(mObjArray, c);
    }

    /** See PageAdapter
     * @param c See PageAdapter
     * @exception PageException Sort array based on comparator
     */
    public void sortSummary(Comparator c)
        throws PageException {
         setCurrentPosition(0);
         Arrays.sort(mObjArray, c);
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
             mObjArray[i].setEnterpriseObjectHistory(null);
        }        
    } 
    
    /** Get JDBC connection
     * @throws PageException An error occured
     * @return JDBC connection
     */    
    private Connection getConnection()
        throws PageException {
        Connection con = null;
        try {
            con = ConnectionUtil.getConnection();
        } catch (Exception e) {
            throw new PageException(mLocalizer.t("PAG542: TransactionPageAdapter " + 
                                    "could not retrieve a database connection."));
        }
        return con;
    }


    /** Ensure page data is loaded for the current row position. In addition 
     * load rows in vicinity of this row.
     *
     * @param direction FORWARD or REVERSE
     * @exception PageException An error occured
     */
    private void loadRows(int direction)
        throws PageException {
        Connection con = null;
        if (mObjArray[mPosition].getEnterpriseObjectHistory() == null) {
            // Determine start and end indexes of records to load
            int startIndex;
            int endIndex;
            switch (direction) {
            case FORWARD:
                startIndex = mPosition;
                endIndex = mPosition + mPageSize - 1;
                if (endIndex >= mNumElements) {
                    endIndex = mNumElements - 1;
                }
                break;
            case REVERSE:
                startIndex = mPosition - mPageSize + 1;
                if (startIndex < 0) {
                    startIndex = 0;
                }
                endIndex = mPosition;
                break;
            default:
                throw new PageException(mLocalizer.t("PAG543: Invalid load direction: {0}", 
                                                     direction));
            }
            // Load EnterpriseObjectHistory record for each TransactionSummary 
            // in object array
            try {
                con = getConnection();
                mTrans = TransactionMgrFactory.getInstance();
                for (int i = startIndex; i <= endIndex; i++) {
                    TransactionSummary summaryObj = 
                        (TransactionSummary) mObjArray[i];
                    TransactionObject transObj = 
                        summaryObj.getTransactionObject();
                    String transNumber = transObj.getTransactionNumber();
                    RecreateResult history = null;
                    boolean validTransaction = true;
                    try  {
                        history = mTrans.recreateObject(con, transNumber);
                    }  catch  (Exception e)  {
                        //  Create an empty RecreateResult object if an error was
                        //  encountered.  This is dealt with in SearchResultForm.java
                        //  for the GUI.  Otherwise, it must be handled by a 
                        //  user-defined API.
                        history = new RecreateResult();
                        mLogger.warn(mLocalizer.x("PAG001: Invalid Transaction " 
                                                  + "Log. Transaction ID: {0}",
                                                  transNumber));
                        // flag this transaction as invalid
                        validTransaction = false;
                    }
                    summaryObj.setEnterpriseObjectHistory(new EnterpriseObjectHistory(history));
                    summaryObj.setValidTransaction(validTransaction);
                }
            } catch (Exception e) {
                throw new PageException(mLocalizer.t("PAG544: Could not load " + 
                                                     "the records: {0}", e));
            } finally {
                if (con != null) {
                    releaseConnection(con);
                }
            }
        }
    }

    /** Release JDBC connection
     * @param con JDBC connection
     */    
    private void releaseConnection(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            mLogger.warn(mLocalizer.x("PAG002: TransactionPageAdapter could not release the JDBC connection: {0}", e.getMessage()));
        }
    }
}
