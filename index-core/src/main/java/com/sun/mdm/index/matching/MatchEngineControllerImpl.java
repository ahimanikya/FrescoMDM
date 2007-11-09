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
package com.sun.mdm.index.matching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.matching.StandardizationException;
import com.sun.mdm.index.matching.Matcher;
import com.sun.mdm.index.matching.MatcherFactory;
import com.sun.mdm.index.matching.Standardizer;
import com.sun.mdm.index.matching.StandardizerFactory;
import com.sun.mdm.index.matching.MatchOptions;
import com.sun.mdm.index.matching.MatchingException;
import com.sun.mdm.index.matching.PassController;
import com.sun.mdm.index.matching.PassControllerHelper;
import com.sun.mdm.index.matching.BlockPicker;
import com.sun.mdm.index.matching.BlockPickerHelper;
import com.sun.mdm.index.matching.NoBlockApplicableException;
import com.sun.mdm.index.matching.CandidateThresholdException;
import com.sun.mdm.index.query.QMException;
import com.sun.mdm.index.query.QueryManager;
import com.sun.mdm.index.query.QueryManagerFactory;
import com.sun.mdm.index.query.QueryResults;
import com.sun.mdm.index.query.QueryObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.querybuilder.QueryBuilder;
import com.sun.mdm.index.querybuilder.QueryBuilderException;
import com.sun.mdm.index.configurator.impl.querybuilder.QueryBuilderConfiguration;
import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.configurator.impl.matching.MatchingConfiguration;
import com.sun.mdm.index.configurator.impl.matching.MatchColumn;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.SystemObjectException;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.master.ObjectNodeFilter;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.metadata.MetaDataService;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.util.LogUtil;

/**
 * The match engine controller provides the main entry point to the 
 * Match Engine Functional Area (MEFA) and exposes the public functionality
 * to Standardize and Match records. The strategy and means to do so are
 * determined by the configured pluggable components such as the standardization
 * and match engine, the BlockPicker and PassController.
 * 
 * @author aegloff
 * $Revision: 1.1 $
 */

public class MatchEngineControllerImpl
        implements MatchEngineController {
    
    private Standardizer mStandardizer = null;
    private Matcher mMatcher = null;    
    private QueryManager mQueryManager = null;
    private BlockPicker mBblockPicker;
    private PassController mPassController;
    private Map mMatchFieldLists;
    private QueryBuilderConfiguration mQueryBuilderConfiguration;
    private ObjectNodeFilter mObjectNodeFilter;    
    
    private static final String FIELD_EUID_STRING = "EUID";
    
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    
    
    static final String TAG_SBR = "Enterprise.SystemSBR.";    
    
    /**
     * Constructor to create Match Engine Controller
     * @throws QueryBuilderException the QueryBuilderConfiguration could not be obtained
     */    
    public MatchEngineControllerImpl() throws Exception { 
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Initializing MatchEngineControllerImpl()");
        }
        initialize();        
    }
    
    private void initialize() throws Exception{
        mMatchFieldLists = new Hashtable();
        mMatcher = MatcherFactory.getInstance();
        mStandardizer = StandardizerFactory.getInstance();
        mQueryManager = QueryManagerFactory.getInstance();
        try{
            mQueryBuilderConfiguration = 
                    (QueryBuilderConfiguration) ConfigurationService.getInstance().getConfiguration(
                    QueryBuilderConfiguration.QUERY_BUILDER);
        } catch (Exception e) {
            throw new QueryBuilderException(e);
        }    
    }
   
    /**
     * Standardize the SystemObject. Required before using the object in the 
     * findMatch function. Standardizing in this context includes phoneticization.
     *
     * @param objToStandardize the Object to standardize. 
     * If calling this through the local interface, Warning: this method may modify 
     * the passed in objToStandardized. 
     * @return the standardized and phoenticized SystemObject. 
     * If calling this through the local interface, the return value may be 
     * a reference to the same object that was passed in, modified by the function.
     * @throws StandardizationException the SystemObject could not be standardized
     * @throws ObjectException the configured fields to be standardized could not be
     * retrieved from the SystemObject
     * @throws InstantiationException the standardizer implementation could not be
     * created
     */    
    public SystemObject standardize(com.sun.mdm.index.objects.SystemObject objToStandardize) 
            throws StandardizationException, ObjectException, InstantiationException {
        objToStandardize = getStandardizer().standardize(objToStandardize);        
        return objToStandardize;
    } 

    /**
     * Attempt to find a matching object by calculating the probabilities an object matches. 
     *
     * @param crit the criteria to find the match for, contains the 
     * (already standardized) SystemObject to find match for.
     * @param opts defines what enterprise objects to match against in the database
     * @param matchOptions the options to control the matching and the results to return
     * @return An ArrayList of com.sun.mdm.index.matching.ScoreElement, containing
     * information such as EUID and weight, indicating how well the the object matches.
     * The matchOptions can influence exactly what is
     * returned, i.e. whether it is in sorted order and whether weights
     * below a certain threshold are removed before returning.
     * By default the order is from the heighest weigth
     * (= best match) first, to the lowest last - and all results are returned.
     * @throws MatchingException matching failed
     * @throws SystemObjectException accessing the configured fields required for standardization 
     * or matching on the passed in SystemObject failed
     * @throws ObjectException accessing the configured fields required for standardization 
     * or matching on the passed in objects failed
     * @throws java.sql.SQLException retrieving data from the database for matching failed
     * @throws EPathException a configured ePath is invalid
     * @throws InstantiationException a configured implementation class for a component
     * could not be instantiated
     * @throws ClassNotFoundException a configured implementation class for a component
     * could not be found
     * @throws IllegalAccessException a configured implementation class for a component
     * refused access
     */    
    public java.util.ArrayList findMatch(EOSearchCriteria crit, EOSearchOptions opts, MatchOptions matchOptions)
            throws MatchingException, SystemObjectException, ObjectException, java.sql.SQLException, EPathException, 
            InstantiationException, ClassNotFoundException, IllegalAccessException, UserException {        
        return findMatch(null, crit, opts, matchOptions);
    }
                                                        
    /**
     * Attempt to find a matching object by calculating the probabilities an object matches. 
     *
     * @param con  The database connection.
     * @param crit the criteria to find the match for, contains the 
     * (already standardized) SystemObject to find match for.
     * @param opts defines what enterprise objects to match against in the database
     * @param matchOptions the options to control the matching and the results to return
     * @return An ArrayList of com.sun.mdm.index.matching.ScoreElement, containing
     * information such as EUID and weight, indicating how well the the object matches.
     * The matchOptions can influence exactly what is
     * returned, i.e. whether it is in sorted order and whether weights
     * below a certain threshold are removed before returning.
     * By default the order is from the heighest weigth
     * (= best match) first, to the lowest last - and all results are returned.
     * @throws MatchingException matching failed
     * @throws SystemObjectException accessing the configured fields required for standardization 
     * or matching on the passed in SystemObject failed
     * @throws ObjectException accessing the configured fields required for standardization 
     * or matching on the passed in objects failed
     * @throws java.sql.SQLException retrieving data from the database for matching failed
     * @throws EPathException a configured ePath is invalid
     * @throws InstantiationException a configured implementation class for a component
     * could not be instantiated
     * @throws ClassNotFoundException a configured implementation class for a component
     * could not be found
     * @throws IllegalAccessException a configured implementation class for a component
     * refused access
     */    
    public java.util.ArrayList findMatch(Connection con, EOSearchCriteria crit, 
                                         EOSearchOptions opts, MatchOptions matchOptions)
            throws MatchingException, SystemObjectException, ObjectException, java.sql.SQLException, EPathException, 
            InstantiationException, ClassNotFoundException, IllegalAccessException, UserException {        

        ArrayList results = new ArrayList();
        boolean sortWeights = matchOptions.getSortWeightResults();        
        boolean another = false;
        int candThreshold = opts.getCandidateThreshold();
        ArrayList combinedResults = new ArrayList();
        ArrayList previousBlockIDs = new ArrayList(); 
        ArrayList remainingBlockIDs = null;
        ArrayList qresults = new ArrayList();
        ArrayList passResults = null;
        int totalRows = 0;
   
        QueryResults block = null;
        try {
            SystemObject objToMatch = crit.getSystemObject();
            //RANGE_SEARCH: lower-bound and upper-bound in SystemObject 2 & 3
            SystemObject objToBlock2 = crit.getSystemObject2();
            SystemObject objToBlock3 = crit.getSystemObject3();
            QueryBuilder queryBuilder = mQueryBuilderConfiguration.getQueryBuilder(opts.getSearchId());    
            remainingBlockIDs = new ArrayList(Arrays.asList(queryBuilder.getApplicableQueryIds(crit, opts)));
            do {
                // Pick block to use
                String[] blockIDs = getBlockPicker().pickBlock(objToMatch, opts, previousBlockIDs, remainingBlockIDs);
                
                // Retrieve block
                block = retrieveBlock(con, queryBuilder, opts, blockIDs, objToMatch, objToBlock2, objToBlock3);
                ResultSet[] resultSets = block.getResultSets();
                if (candThreshold > 0) {
                    for (int idx = 0; idx < resultSets.length; idx++) {
                        totalRows += countRows(resultSets[idx]);
                        if (totalRows > candThreshold) {
                            throw new UserException(new CandidateThresholdException("The maximum possible matches, " 
                                    + candThreshold + ", is exceeded"));
                        }
                    }
                }
                
                qresults.add(block);
                // Pass block to match engine

                // Update the list of blocks remaining/done
                java.util.List convBlockIDs = Arrays.asList(blockIDs);
                previousBlockIDs.addAll(convBlockIDs);
                remainingBlockIDs.removeAll(convBlockIDs);
                
                // Call PassController with results, if evalAnotherPass true loop to pick block
                another = getPassController().evalAnotherPass(
                    objToMatch, opts, passResults, combinedResults, previousBlockIDs, remainingBlockIDs);
            } while (another);
            for (int idx = 0; idx < qresults.size(); idx++) {
                block = (QueryResults) qresults.get(idx);
                // Pass block to match engine
                passResults = getMatcher().findWeights(objToMatch, block, matchOptions);
                // Combine results with previous pass results
                // If we get several results for the same EUID, keep the latest pass
                combinedResults.removeAll(passResults);
                combinedResults.addAll(passResults);
            }
        } catch (NoBlockApplicableException ex) {
            // The BlockPicker did not find a block definition left that would apply
            // This is not necessarily an error.
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("No applicable block found " + ex.getMessage());
            }
        } catch (com.sun.mdm.index.query.QMException qex) {
            mLogger.warn(mLocalizer.x("QueryManager execution failed: {0}", qex.getMessage()));
            throw new MatchingException(qex.getMessage(), qex);
        } catch (UserException mex) {
        	throw mex;
        } catch (Exception e) {
            mLogger.warn(mLocalizer.x("Find match encountered an exception: {0}", e.getMessage()));
            throw new MatchingException(e.getMessage(), e);
        } finally {
        	try {
                    if (block != null) {
                        block.close();
                    }
        	} catch (QMException ex) {
        		throw new MatchingException(ex);
        	}
        }
        
        // If configured to sort the weights, they are sorted in descending order,
        // the ScoreElement with the highest Weight first.
        if (sortWeights) {
            java.util.Collections.sort(combinedResults);
        }        

        return combinedResults;            
            
    }
    
    /**
     * Get an instance of the configured BlockPicker.
     * @return the BlockPicker implementation instance
     * @throws InstantiationException the configured implementation class for a component
     * could not be instantiated
     * @throws InstantiationException the configured implementation class for a component
     * could not be found
     * @throws IllegalAccessException the configured implementation class for a component
     * refused access
     */
    BlockPicker getBlockPicker() 
            throws InstantiationException, ClassNotFoundException, IllegalAccessException {
        if (mBblockPicker == null) {
            // use BlockPickerHelper as factory
            mBblockPicker = new BlockPickerHelper().getBlockPickerImpl();
        }
        return mBblockPicker;
    }

    /**
     * Get an instance of the configured PassController.
     * @return the PassController implementation instance
     * @throws InstantiationException the configured implementation class for a component
     * could not be instantiated
     * @throws InstantiationException the configured implementation class for a component
     * could not be found
     * @throws IllegalAccessException the configured implementation class for a component
     * refused access
     */    
    PassController getPassController() 
            throws InstantiationException, ClassNotFoundException, IllegalAccessException {
        if (mPassController == null) {
            // use PassControllerHelper as factory        
            mPassController = new PassControllerHelper().getPassControllerImpl();
        }
        return mPassController;
    }

    /**
     * Get the local interface instance of the Standardardizer session EJB.
     * @return the local interface instance
     */        
    Standardizer getStandardizer() {
        return mStandardizer;
    }

    /**
     * Get the local interface instance of the Matcher session EJB.
     * @return the local interface instance
     */        
    Matcher getMatcher() {
        return mMatcher;
    }
    
    /** 
     * Retrieves a block matching the specified SearchOptions.
     *
     * Uses dynamic generated queries
     * @param searchOption the options to bass to the queryBuilder
     * @param sysObj The systemObject to retrive the block for
     * @throws BlockerException a blocker operation failed
     * @throws SQLException retrieving the block from the database failed
     * @throws SystemObjectException an operation on the SystemObject failed
     * @throws EPathException using the configured ePaths for matching failed
     * @throws ObjectException an operation on an ObjectNode failed
     * @return the block of records to match against
     */
    public QueryResults retrieveBlock(QueryBuilder qBuilder, EOSearchOptions options, 
                                      String queryIds[], SystemObject sysObj, 
                                      SystemObject sysObj2, SystemObject sysObj3)
            throws QMException, SystemObjectException, ObjectException, MatchingException {
        return retrieveBlock(null, qBuilder, options, queryIds, sysObj, sysObj2, sysObj3);
    }
    
    /** 
     * Retrieves a block matching the specified SearchOptions.
     *
     * Uses dynamic generated queries
     * @param con the database connection
     * @param searchOption the options to bass to the queryBuilder
     * @param sysObj The systemObject to retrive the block for
     * @throws BlockerException a blocker operation failed
     * @throws SQLException retrieving the block from the database failed
     * @throws SystemObjectException an operation on the SystemObject failed
     * @throws EPathException using the configured ePaths for matching failed
     * @throws ObjectException an operation on an ObjectNode failed
     * @return the block of records to match against
     */
    public QueryResults retrieveBlock(Connection con, QueryBuilder qBuilder, 
                                      EOSearchOptions options, String queryIds[], 
                                      SystemObject sysObj, SystemObject sysObj2, 
                                      SystemObject sysObj3)
            throws QMException, SystemObjectException, ObjectException, MatchingException {
        
        // assert searchOption and sysObj are not null
        if (options == null || sysObj == null) {
            throw new MatchingException("Input can not be null");
        }
        
        try {
            EOSearchCriteria criteria = new EOSearchCriteria();
            criteria.setSystemObject(sysObj);
            //RANGE_SEARCH: lower-bound and upper-bound in SystemObject 2 & 3
            criteria.setSystemObject2(sysObj2);
            criteria.setSystemObject3(sysObj3);
            
            ArrayList matchFields = getMatchFieldList(sysObj);
            EPathArrayList epathList = new EPathArrayList();
            for (int i = 0; i < matchFields.size(); i++) {
                String fieldName = (String) matchFields.get(i);
                epathList.add(fieldName);
            }
            
            // override all FieldsToRetrieve values
            EPathArrayList prevFieldsToRetrieve = options.getFieldsToRetrieve();
            options.setFieldsToRetrieve(epathList);
            QueryObject qos = qBuilder.buildQueryObject(queryIds, criteria, options);
            
            // assert qos != null and length > 0
            if (qos == null) {
                throw new MatchingException("Assertion failed, Query builder result is null");
            }

            if (options.getCandidateThreshold() > 0) {
                qos.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
            }

            // only execute the first one because only one query is expected to return
            qos.setQueryOption(QueryObject.SINGLE_QUERY);
            QueryResults result = mQueryManager.execute(con, qos);
            // restore field to retrieve options
            options.setFieldsToRetrieve(prevFieldsToRetrieve);
            
            return result;
            
        } catch (ConfigurationException cex) {
            throw new MatchingException("Invalid search criteria option", cex);
        } catch (QueryBuilderException qbex) {
            throw new MatchingException("Dynamic query building failed", qbex);
        } catch (EPathException eex) {
            throw new MatchingException(eex.getMessage(), eex);
        } catch (InstantiationException iex) {
            throw new MatchingException(iex.getMessage(), iex);
        }
    }    
    
    /** 
     * Access the Matcher configuration and returns a list of field names
     * First look for it in the cache, return it if found, look it up if its not.
     * Each EJB instance has its own cache, speeds up access after the list has
     * been requested more then once.  Works based on the assumption that container
     * does not destroy stateless session beans after servicing each request.
     *
     * @TODO change so that it loads all match fields at initialization time
     *
     * @param sysObj a SystemObject defining the type of the object to find the match fields for
     * @return the list of match fields
     * @throws EPathException converting the ePaths to field names failed
     * @throws ConfigurationException the configuration could not be retrieved
     * @throws InstantiationException a configured implementation class could not be instantiated
     */
    public ArrayList getMatchFieldList(SystemObject sysObj)
        throws EPathException, ConfigurationException, InstantiationException {
        
        ArrayList ret;
        
        String objectType = sysObj.getObject().pGetType();
        
        ObjectNode objNode = sysObj.getObject();
        String fullObjPath = MetaDataService.getSBRPath(objNode.pGetTag());
        String fullSysObjEUIDPath = fullObjPath + "." + FIELD_EUID_STRING;
        
        //first look for it in the cache, return it if found, look it up if its not
        ret = (ArrayList) mMatchFieldLists.get(objectType);
        if (ret == null) {
            ret = new ArrayList();
            ret.add(fullSysObjEUIDPath);
            MatchingConfiguration mc = (MatchingConfiguration) 
                ConfigurationService.getInstance().getConfiguration(MatchingConfiguration.MATCHING);
            
            ArrayList matchCols = mc.getSystemObjectMatching(objectType).getMatchColums();
            Iterator iter = matchCols.iterator();
            while (iter.hasNext()) {
                MatchColumn c = (MatchColumn) iter.next();
                EPath e = c.getEPath();
                
                ret.add(e.toFieldName());
            }
            // add the new list to the cache
            mMatchFieldLists.put(objectType, ret);
            
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Match field list:\n" + LogUtil.listToString(ret));
            }
        }
        return ret;
    }    

    private int countRows(ResultSet rs) throws SQLException {
        int count = 0;
        if (rs != null) {
            if (rs.last()) {
                count = rs.getRow();
                rs.beforeFirst();
            } 
        } 
        return count;
    }
}
