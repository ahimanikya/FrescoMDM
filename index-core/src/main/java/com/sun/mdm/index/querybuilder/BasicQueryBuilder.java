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
package com.sun.mdm.index.querybuilder;

import java.util.ArrayList;
import java.util.Iterator;
import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.configurator.impl.querybuilder.KeyValueConfiguration;
import com.sun.mdm.index.query.QueryObject;
import com.sun.mdm.index.master.ObjectNodeFilter;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.metadata.MetaDataService;
import java.util.logging.Level;
import net.java.hulp.i18n.Logger;

/**
 * Construct query by looking at which fields in the system object have been
 * populated and performing a 'like %' search against string fields and an "="
 * search for non-string fields.
 * @author Dan Cidon
 */
public class BasicQueryBuilder extends QueryBuilder {

    private boolean useWildcard = false;

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    
    
    /**
     * Creates a new instance of PartialSearchQueryBuilder
     */
    public BasicQueryBuilder() {
    }


    /** See QueryBuilder
     * @return See QueryBuilder
     * @param crit See QueryBuilder
     * @param opts See QueryBuilder
     * @exception QueryBuilderException See QueryBuilder
     */
    public String[] getApplicableQueryIds(SearchCriteria crit, SearchOptions opts)
        throws QueryBuilderException {
        String[] oneString = new String[1];
        oneString[0] = "SINGLE-QUERY";
        return oneString;
    }


    /** See QueryBuilderSee QueryBuilder
     * @return See QueryBuilder
     * @param ids See QueryBuilder
     * @param crit See QueryBuilder
     * @param opts See QueryBuilder
     * @exception QueryBuilderException See QueryBuilder
     */
    public QueryObject buildQueryObject(String[] ids, SearchCriteria crit, SearchOptions opts)
        throws QueryBuilderException {
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Starting in buildQueryObject()");
        }
        QueryObject queryObject = new QueryObject();
        if (!ids[0].equalsIgnoreCase("SINGLE-QUERY")) {
            throw new QueryBuilderException("Invalid query id: " + ids[0]);
        }
        try {
            EOSearchCriteria criteria = (EOSearchCriteria) crit;
            EOSearchOptions options = (EOSearchOptions) opts;

            //Filter out fields
            boolean objectStandardized = false;
            boolean standardize = isStandardizeRequired();
            boolean phoneticize = isPhoneticizeRequired();
            if (standardize || phoneticize) {
                objectStandardized = true;
            }
            
            ObjectNodeFilter filter = new ObjectNodeFilter();

            SystemObject sysObj = criteria.getSystemObject();
            ObjectNode objNode = null;
            if (sysObj != null) {
                objNode = sysObj.getObject().copy();

                //If standardization selected, filter out standardization source
                //fields. Otherwise filter out target fields.
                if (standardize) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("buildQueryObject(): filtering standardized source fields");
                    }
                    filter.filterStandardizedSourceFields(objNode);
                } else {
                    if (objectStandardized) {
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("buildQueryObject(): filtering standardized target fields");
                        }
                        filter.filterStandardizedTargetFields(objNode);
                    }
                }

                //If phoneticization selected, filter out phoneticization source
                //fields.  Otherwise filter target fields.
                if (phoneticize) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("buildQueryObject(): filtering phoneticized source fields");
                    }
                    filter.filterPhoneticizedSourceFields(objNode);
                } else {
                    if (objectStandardized) {
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("buildQueryObject(): filtering phoneticized target fields");
                        }
                        filter.filterPhoneticizedTargetFields(objNode);
                    }
                }
            }
            
            //If a second system object is passed in, then it must also be filtered
            SystemObject sysObj2 = criteria.getSystemObject2();
            ObjectNode objNode2 = null;
            if (sysObj2 != null) {
                objNode2 = sysObj2.getObject().copy();
                
                //If standardization selected, filter out standardization source
                //fields. Otherwise filter out target fields.
                if (standardize) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("buildQueryObject(): filtering standardized source fields");
                    }
                    filter.filterStandardizedSourceFields(objNode2);
                } else {
                    if (objectStandardized) {
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("buildQueryObject(): filtering standardized target fields");
                        }
                        filter.filterStandardizedTargetFields(objNode2);
                    }
                }

                //If phoneticization selected, filter out phoneticization source
                //fields.  Otherwise filter target fields.
                if (phoneticize) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("buildQueryObject(): filtering phoneticized source fields");
                    }
                    filter.filterPhoneticizedSourceFields(objNode2);
                } else {
                    if (objectStandardized) {
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("buildQueryObject(): filtering phoneticized target fields");
                        }
                        filter.filterPhoneticizedTargetFields(objNode2);
                    }
                }
            }
            
            //If a third system object is passed in, then it must also be filtered
            SystemObject sysObj3 = criteria.getSystemObject3();
            ObjectNode objNode3 = null;
            if (sysObj3 != null) {
                objNode3 = sysObj3.getObject().copy();
                
                //If standardization selected, filter out standardization source
                //fields. Otherwise filter out target fields.
                if (standardize) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("buildQueryObject(): filtering standardized source fields");
                    }
                    filter.filterStandardizedSourceFields(objNode3);
                } else {
                    if (objectStandardized) {
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("buildQueryObject(): filtering standardized target fields");
                        }
                        filter.filterStandardizedTargetFields(objNode3);
                    }
                }

                //If phoneticization selected, filter out phoneticization source
                //fields.  Otherwise filter target fields.
                if (phoneticize) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("buildQueryObject(): filtering phoneticized source fields");
                    }
                    filter.filterPhoneticizedSourceFields(objNode3);
                } else {
                    if (objectStandardized) {
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("buildQueryObject(): filtering phoneticized target fields");
                        }
                        filter.filterPhoneticizedTargetFields(objNode3);
                    }
                }
            }
            
            //Set the select clause
            String fullObjPath = MetaDataService.getSBRPath(objNode.pGetTag());
            queryObject.setRootObject(fullObjPath);
            EPathArrayList fieldArrayList = options.getFieldsToRetrieve();
            if (fieldArrayList == null) {
                throw new QueryBuilderException("Fields to retrieve parameter can not be null");
            }
            queryObject.setSelect(fieldArrayList.toStringArray());
            populateQueryObject(queryObject, objNode, objNode2, objNode3);
            if (opts.getMaxQueryElements() > 0) {
               queryObject.setMaxObjects(opts.getMaxQueryElements());
            }

        } catch (Exception ex) {
            throw new QueryBuilderException(ex);
        }
        return queryObject;
    }


    /** See QueryBuilder
     * @param info See QueryBuilder
     * @exception QueryBuilderException See QueryBuilder
     */
    public void init(ConfigurationInfo info)
        throws QueryBuilderException {
        KeyValueConfiguration keyValueConfig = (KeyValueConfiguration) info;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("BasicQueryBuilder.init(): useWildcard set to " + useWildcard);
            }
            useWildcard = keyValueConfig.getBooleanValue("UseWildcard");
        } catch (Exception e) {
            throw new QueryBuilderException(e);
        }
    }

    private void populateQueryObject(QueryObject queryObject, ObjectNode objNode, 
    ObjectNode objNode2, ObjectNode objNode3) throws QueryBuilderException {
        try {
            //Set the where clause based on which fields are populated
            ArrayList primaryFields = null;
            String tag = null;
            ArrayList childTags = null;
            if (objNode != null) {
                primaryFields = objNode.pGetFieldNames();
                tag = objNode.pGetTag();
                childTags = objNode.pGetChildTags();
            } else if (objNode2 != null) {
                primaryFields = objNode2.pGetFieldNames();
                tag = objNode2.pGetTag();
                childTags = objNode2.pGetChildTags();
            } else if (objNode3 != null) {
                primaryFields = objNode3.pGetFieldNames();
                tag = objNode3.pGetTag();
                childTags = objNode3.pGetChildTags();
            } else {
                throw new QueryBuilderException("At least one object must be populated");
            }
            Iterator primaryFieldIterator = primaryFields.iterator();
            String fullObjPath = MetaDataService.getSBRPath(tag);
            while (primaryFieldIterator.hasNext()) {
                String fieldName = (String) primaryFieldIterator.next();
                //RANGE_SEARCH: first see if a range search can be done
                if ((objNode2 == null || objNode2.isNull(fieldName)) &&
                    (objNode3 == null || objNode3.isNull(fieldName))) {
                     //Not enough data for range search, so try to do =/LIKE search
                    if (objNode != null && !objNode.isNull(fieldName)) {
                        Object fieldVal = objNode.getValue(fieldName);
                        if (fieldVal != null) {
                            String fullFieldPath = fullObjPath + "." + fieldName;

                            int fieldType = objNode.pGetType(fieldName);
                            if (fieldType == ObjectField.OBJECTMETA_STRING_TYPE) {
                                if (!fieldVal.equals("")) {
                                    if (useWildcard) {
                                       queryObject.addCondition(fullFieldPath, "LIKE", (String) fieldVal);
                                    } else {
                                       queryObject.addCondition(fullFieldPath, "=", fieldVal);
                                    }
                                }
                            } else {
                                queryObject.addCondition(fullFieldPath, "=", fieldVal);
                            }
                        }                    
                    }
                } else {
                    //Some data available for range search, add <=, >= conditions
                    if (objNode2 != null && !objNode2.isNull(fieldName)) {
                        Object fieldVal = objNode2.getValue(fieldName);
                        if (fieldVal != null) {
                            String fullFieldPath = fullObjPath + "." + fieldName;
                            queryObject.addCondition(fullFieldPath, ">=", fieldVal);
                        }
                    }
                    if (objNode3 != null && !objNode3.isNull(fieldName)) {
                        Object fieldVal = objNode3.getValue(fieldName);
                        if (fieldVal != null) {
                            String fullFieldPath = fullObjPath + "." + fieldName;
                            queryObject.addCondition(fullFieldPath, "<=", fieldVal);
                        }
                    }
                }
            }
                //For now, only deal with single secondary objects.
                if (childTags != null) {
                    Iterator childTagIterator = childTags.iterator();
                    while (childTagIterator.hasNext()) {
                        String childTag = (String) childTagIterator.next();
                        String fullChildPath = fullObjPath + "." + childTag;

                        ArrayList children = null;
                        if (objNode != null) {
                            children = objNode.pGetChildren(childTag);
                        }
                        ArrayList children2 = null;
                        if (objNode2 != null) {
                            children2 = objNode2.pGetChildren(childTag);
                        }
                        ArrayList children3 = null;
                        if (objNode3 != null) {
                            children3 = objNode3.pGetChildren(childTag);
                        }
                        
                        ObjectNode child = null;
                        ObjectNode child2 = null; 
                        ObjectNode child3 = null; 
                            
                        if (children != null && children.size() > 0) {
                            if (children.size() > 1) {
                                throw new QueryBuilderException("Only one child node " 
                                    + "can be defined per type.  Type: " + childTag);
                            }
                            child = (ObjectNode) children.get(0);
                        }
                        if (children2 != null && children2.size() > 0) {
                            if (children2.size() > 1) {
                                throw new QueryBuilderException("Only one child node " 
                                + "can be defined per type.  Type: " + childTag);
                            }
                            child2 = (ObjectNode) children2.get(0);
                        }
                        if (children3 != null && children3.size() > 0) {
                            if (children3.size() > 1) {
                            throw new QueryBuilderException("Only one child node " 
                                    + "can be defined per type.  Type: " + childTag);
                            }
                            child3 = (ObjectNode) children3.get(0);
                        }
                        if (child != null || child2 != null || child3 != null) {
                            populateQueryObject(queryObject, child, child2, child3);
                        }
                    }
                }
            
        } catch (Exception ex) {
            throw new QueryBuilderException(ex);
        }
    }         
}
