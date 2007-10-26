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
import com.sun.mdm.index.query.AssembleDescriptor;
import com.sun.mdm.index.query.Condition;
import com.sun.mdm.index.query.EOSearchResultAssembler;
import com.sun.mdm.index.query.QueryManager;
import com.sun.mdm.index.query.QueryManagerFactory;
import com.sun.mdm.index.query.QueryObject;
import com.sun.mdm.index.query.QMIterator;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.matching.ScoreElement;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.ObjectNode;
import javax.ejb.EJBContext;


/**
 * Given a hashmap of (String)EUID->(Float)Weight, adapt the result for use
 * within page iterator framework. FieldsToRetrieve parameter is read from
 * options and is used to construct query object to get data as needed. Once
 * data is retrieved it remains in memory. Future optimization may include
 * unloading some of the data to conserve resources.
 * @author Dan Cidon
 */
public class EuidPageAdapter implements PageAdapter, java.io.Serializable {

    /** Current row position
     */    
    private int mPosition = 0;

    /** Used internally to indiciate direction of traversal
     */    
    private static  final int FORWARD = 1;
    /** Used internally to indiciate direction of traversal
     */    
    private static  final int REVERSE = 2;
    /** Number of elements in adapter
     */    
    private final int mNumElements;
    /** Forward only mode 
     */ 
    private boolean mForwardOnly = false;  
    /** Array to store EOSearchResultRecords that have been loaded
     */    
    private final EOSearchResultRecord[] mObjArray;
    /** Search options
     */    
    private  final EOSearchOptions mOptions;
    /** Page size
     */    
    private final int mPageSize;
    
    /** List of score elements provided by MEC
     */    
    private final ArrayList mScoreElementList;
    /** Path of the root object being returned
     */    
    private String mObjectPath;

    /** Creates a new instance of EuidPageAdapter
     *
     * @param list List of ScoreElements
     * @param options Search options
     * @param maxElements Maximum number of elements to be handled
     * @exception PageException An error occured.
     */
    public EuidPageAdapter(ArrayList list, EOSearchOptions options,
            int maxElements)
        throws PageException {
        mScoreElementList = list;
        try {
            
            mOptions = options;
            mPageSize = mOptions.getPageSize();
            mObjArray = new EOSearchResultRecord[list.size()];
            if (mObjArray.length < maxElements) {
                mNumElements = mObjArray.length;
            } else {
                mNumElements = maxElements;
            }
        } catch (Exception e) {
            throw new PageException(e);
        }
    }

    /** See PageAdapter
     * @param index See PageAdapter
     * @throws PageException See PageAdapter
     */    
    public void setCurrentPosition(int index)
        throws PageException {
        if (index + 1 >= mNumElements) {
            throw new PageException("Index out of bounds: " + index);
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
     * @return See PageAdapter
     * @throws PageException See PageAdapter
     */
    public Object next()
        throws PageException {
        if (mPosition == mNumElements) {
            throw new PageException("No more elements");
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
     * @return See PageAdapter
     * @exception PageException See PageAdapter 
     */
    public Object prev()
        throws PageException {
        if (mPosition == 0) {
            throw new PageException("Already at beginning of iterator");
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

    /** Determines the path of the root object that will be returned based on
     * the fields that have been selected.  Method assumeds that the EUID 
     * has been selected.
     * @param fieldList Fields to be retrieved
     * @throws PageException An error occured.
     * @return The root path
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
            throw new 
                PageException("EuidPageAdapter requires EUID to be selected.");
        }
        return mObjectPath;
    }

    private void clearPageSizeCache() throws PageException { 
        
        int startPosition = mPosition - mPageSize; 
        int endPosition = mPosition -1;
        if (startPosition < 0) {
            startPosition = 0;
        }    
        for (int i = startPosition; i <= endPosition; i++) {
             mObjArray[i].setObject(null);
        }        
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
        if (mObjArray[mPosition] == null) {
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
                throw new PageException("Invalid direction: " + direction);
            }
            //Load records
            QMIterator iterator = null;
            try {
                
                EPathArrayList fieldArrayList = mOptions.getFieldsToRetrieve();
                String[] fieldArray = fieldArrayList.toStringArray();
                String objPath = getObjectPath(fieldArray);
                String euidPath = objPath + "." + "EUID";

                QueryObject queryObject = new QueryObject();
                queryObject.setSelect(fieldArray);
                //queryObject.setRootObject( objPath );
                String[] euidsToLoad = new String[endIndex - startIndex + 1];
                for (int i = 0; i < euidsToLoad.length; i++) {
                    ScoreElement se = (ScoreElement) 
                        mScoreElementList.get(startIndex + i);
                    euidsToLoad[i] = se.getEUID();
                }
                queryObject.addCondition(
                    new Condition(euidPath, "IN", euidsToLoad));
                Object[] resultArray;
                AssembleDescriptor descriptor = new AssembleDescriptor();
                EOSearchResultAssembler factory = new EOSearchResultAssembler();
                descriptor.setAssembler(factory);
                descriptor.setResultValueObjectType(
                    factory.getValueMetaNode(objPath));
                queryObject.setAssembleDescriptor(descriptor);
                
                QueryManager mQuery = QueryManagerFactory.getInstance();
                iterator = mQuery.executeAssemble(queryObject);
                ArrayList resultList = new ArrayList();
                while (iterator.hasNext()) {
                    resultList.add(iterator.next());
                }
                iterator.close();
                resultArray = resultList.toArray();

                // go through retrieved list - IN operator does not guarantee
                // that the result is in the same order as the list of EUID's
                for (int i = 0; i < resultArray.length; i++) {
                    // get object retrieved from DB & corresponding EUID
                    EOSearchResultRecord obj = 
                        (EOSearchResultRecord) resultArray[i];
                    String euid = obj.getEUID();
                    ObjectNode objNode = obj.getObject();



                    // find the corresponding row in mObjArray that has the EUID
                    // in the range startIndex to startIndex + result length
                    for (int j = 0; j < resultArray.length; j++) {
                        ScoreElement se = (ScoreElement) 
                            mScoreElementList.get(startIndex + j);
                        if (euid.equals(se.getEUID())) {
                            mObjArray[startIndex + j] = 
                                (EOSearchResultRecord) resultArray[i];
                            mObjArray[startIndex + j].setComparisonScore(
                                (float) se.getWeight());
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                throw new PageException(e);
            }  finally {
            	 try {    
                  	 if (iterator != null) {
                	    iterator.close();
                	  }
                 } catch (Exception ex) {
                      throw new PageException(ex);
                 }
            	
            }
        }
    }

}
