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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import com.sun.mdm.index.query.AssembleDescriptor;
import com.sun.mdm.index.query.Condition;
import com.sun.mdm.index.query.EOSearchResultAssembler;
import com.sun.mdm.index.query.QueryManager;
import com.sun.mdm.index.query.QueryManagerFactory;
import com.sun.mdm.index.query.QueryObject;
import com.sun.mdm.index.query.QMIterator;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.util.Constants;
import com.sun.mdm.index.util.Localizer;

import javax.ejb.EJBContext;

/**
 * Given a list of PotentialDuplicateSummaries, adapt the result for use within
 * page iterator framework. FieldsToRetrieve parameter is read from options and
 * is used to construct query object to get data as needed. Once data is
 * retrieved it remains in memory. Future optimization may include unloading
 * some of the data to conserve resources.
 * @author Dan Cidon
 */
public class PotentialDuplicatePageAdapter implements PageAdapter, java.io.Serializable {

    private transient final Localizer mLocalizer = Localizer.get();
    
    /** Current row index
     */    
    private int mPosition = 0;

    /** Used to indicate forward traversal
     */    
    private static final int FORWARD = 1;
    /** Used to indicate reverse traversal
     */    
    private static final int REVERSE = 2;
    /** Fields to retrieve
     */    
    private final String[] mFieldArray;
    /** Number of elements in the data source
     */    
    private final int mNumElements;

    /** Array of potential duplicates already loaded
     */    
    private final PotentialDuplicateSummary[] mObjArray;
    /** Page size
     */    
    private final int mPageSize;
    /** Forward only mode 
     */ 
    private boolean mForwardOnly = false;     
    /** Path to root object
     */    
    private String mObjectPath;
    
    /** Creates a new instance of PotentialDuplicatePageAdapter
     *
     * @param options Search options
     * @param list list of potential duplicate summaries
     * @exception PageException An error occured.
     */
    public PotentialDuplicatePageAdapter(ArrayList list, 
        PotentialDuplicateSearchObject options) throws PageException {

        try {
            
            mPageSize = options.getPageSize();
            Object[] objArray = list.toArray();
            mObjArray = new PotentialDuplicateSummary[objArray.length];
            for (int i = 0; i < objArray.length; i++) {
                mObjArray[i] = (PotentialDuplicateSummary) objArray[i];
            }
            mNumElements = mObjArray.length;
            EPathArrayList fieldArrayList = options.getFieldsToRetrieve();
            if (fieldArrayList != null) {
                mFieldArray = fieldArrayList.toStringArray();
            } else {
                mFieldArray = null;
            }
        } catch (Exception e) {
            throw new PageException(mLocalizer.t("PAG530: PotentialDuplicatePageAdapter " +
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
            throw new PageException(mLocalizer.t("PAG531: PotentialDuplicatePageAdapter " +
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
    

    /** See PageAdapter
     */
    public void activate() {
    }


    /** See PageAdapter
     */
    public void close() {
    }


    /** See PageAdapter
     * @return See PageAdapter
     * @throws PageException See PageAdapter
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
            throw new PageException(mLocalizer.t("PAG532: PotentialDuplicatePageAdapter " +
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
     * @throws PageException See PageAdapter
     * @return See PageAdapter
     */
    public Object prev()
        throws PageException {
        if (mPosition == 0) {
            throw new PageException(mLocalizer.t("PAG533: Already at beginning of iterator."));
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

    /** lear local cache of the previous page
     */      
    private void clearPageSizeCache() throws PageException { 
        
        int startPosition = mPosition - mPageSize; 
        int endPosition = mPosition -1;
        if (startPosition < 0) {
            startPosition = 0;
        }    
        for (int i = startPosition; i <= endPosition; i++) {
             mObjArray[i].setObject1(null);
             mObjArray[i].setObject2(null);
        }        
    }    
    
    /** Determines the path of the root object that will be returned based on
     * the fields that have been selected.  Method assumeds that the EUID 
     * field has been selected.
     * @param fieldList List of fields to base path on
     * @throws PageException An error occured.
     * @return Root object path
     */    
    private String getObjectPath(String[] fieldList)
        throws PageException {
        if (mObjectPath == null) {
            for (int i = 0; i < fieldList.length; i++) {
                if (fieldList[i].endsWith("EUID")) {
                    mObjectPath = 
                        fieldList[i].substring(0, fieldList[i].length() - 5);
                    return mObjectPath;
                }
            }
            throw new PageException(mLocalizer.t("PAG534: PotentialDuplicatePageAdapter " + 
                                                "requires EUID to be selected."));
        }
        return mObjectPath;
    }


    /** Ensure page data is loaded for the current row position. In addition 
     * load rows in vicinity of this row.
     *
     * @param direction FORWARD or REVERSE
     * @param allRows true if load all rows
     * @exception PageException An error occured.
     */
    private void loadRows(int direction, boolean allRows)
        throws PageException {
        if (mFieldArray != null && mObjArray[mPosition].getObject1() == null) {
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
                throw new PageException(mLocalizer.t("PAG535: Invalid " + 
                                                "load direction: {0}", direction));
            }
            //Load records
            QMIterator iterator = null;
            try {
                String objPath = getObjectPath(mFieldArray);
                String euidPath = objPath + "." + "EUID";

	        // The "IN" clause of a SQL SELECT statement cannot have more than
	        // 1000 items.  Each potential duplicate record has 2 EUID's, so no
	        // more than 500 potential duplicate records should be retrieved at a time.
	        // (Need to subtract 1 to work properly in the FOR loop)
                int tempEndIndex = endIndex;
                int tempStartIndex = startIndex;
                while (tempStartIndex <= endIndex) {
                    int size = endIndex - tempStartIndex;
                    if (size > Constants.POT_DUP_MAX_ELEMENTS - 1) {
                        tempEndIndex = tempStartIndex + Constants.POT_DUP_MAX_ELEMENTS - 1;
                    } else {
                        tempEndIndex = endIndex;
                    }
                    
                    
                    QueryObject queryObject = new QueryObject();
                    queryObject.setSelect(mFieldArray);
                    HashMap euidsToLoad = new HashMap();
                    
                    for (int i = tempStartIndex; i <= tempEndIndex; i++) {
                        String euid1 = ((PotentialDuplicateSummary) 
                            mObjArray[i]).getEUID1();
                        String euid2 = ((PotentialDuplicateSummary) 
                            mObjArray[i]).getEUID2();
                        euidsToLoad.put(euid1, null);
                        euidsToLoad.put(euid2, null);
                    }
                    Object[] keyArray = euidsToLoad.keySet().toArray();
                    String[] euidArray = new String[keyArray.length];
                    for (int i = 0; i < keyArray.length; i++) {
                        euidArray[i] = (String) keyArray[i];
                    }
                    Condition c = new Condition(euidPath, "IN", euidArray);
                    queryObject.addCondition(c);
                    AssembleDescriptor descriptor = new AssembleDescriptor();
                    EOSearchResultAssembler factory = new EOSearchResultAssembler();
                    descriptor.setAssembler(factory);
                    descriptor.setResultValueObjectType(
                        factory.getValueMetaNode(objPath));
                    queryObject.setAssembleDescriptor(descriptor);

                    QueryManager mQuery = QueryManagerFactory.getInstance();
                    
                    iterator = mQuery.executeAssemble(queryObject);
                    while (iterator.hasNext()) {
                        EOSearchResultRecord rec = 
                            (EOSearchResultRecord) iterator.next();
                        euidsToLoad.put(rec.getEUID(), rec.getObject());
                    }
                    iterator.close();

                    for (int i = tempStartIndex; i <= tempEndIndex; i++) {
                        PotentialDuplicateSummary pds = mObjArray[i];
                        ObjectNode obj = (ObjectNode) euidsToLoad.get(pds.getEUID1());



                        pds.setObject1(obj);
                        obj = (ObjectNode) euidsToLoad.get(pds.getEUID2()); 


                        pds.setObject2(obj);
                    }
                    tempStartIndex = tempEndIndex + 1;
                    
                }  // end WHILE loop

            } catch (Exception e) {
                throw new PageException(mLocalizer.t("PAG536: Could not load " + 
                                                "the records: {0}", e));
            } finally {
                try {
            
          	      if (iterator != null) {
        	         iterator.close();
        	      }
        	    } catch (Exception ex) {
        		  throw new PageException(mLocalizer.t("PAG537: Could not close " + 
                                                "the iterator: {0}", ex));
        	    }
            }
      }
    }

}
