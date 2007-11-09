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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.configurator.impl.blocker.BlockerConfig;
import com.sun.mdm.index.configurator.impl.blocker.BlockRule;
import com.sun.mdm.index.configurator.impl.blocker.BlockDefinition;
import com.sun.mdm.index.configurator.impl.blocker.AbstractCondition;
import com.sun.mdm.index.configurator.impl.blocker.ExactCondition;
import com.sun.mdm.index.configurator.impl.blocker.StartsWithCondition;
import com.sun.mdm.index.configurator.impl.blocker.NotEqualsCondition;
import com.sun.mdm.index.configurator.impl.blocker.GreaterThanCondition;
import com.sun.mdm.index.configurator.impl.blocker.GreaterThanEqualsCondition;
import com.sun.mdm.index.configurator.impl.blocker.LessThanCondition;
import com.sun.mdm.index.configurator.impl.blocker.LessThanEqualsCondition;
import com.sun.mdm.index.configurator.impl.blocker.ContainsCondition;
import com.sun.mdm.index.configurator.impl.blocker.RangeCondition;

import com.sun.mdm.index.query.QueryObject;
import com.sun.mdm.index.query.Condition;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SystemObjectException;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.metadata.MetaDataService;
import com.sun.mdm.index.parser.FieldDef;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import net.java.hulp.i18n.Logger;

/**
 * The Blocker is used to limit the amount of potential candidates the match
 * engine has the work with. User configured filters are used to to retrieve a
 * block of candidates.
 * 
 * @author SeeBeyond
 * @TODO : consider using jdk1.4 assert statements instead of if..else..
 * @version $Revision: 1.1 $
 */

public class BlockerQueryBuilder extends QueryBuilder {

    private BlockerConfig config;

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());

    private final SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

    private final SimpleDateFormat mTimeFormat = new SimpleDateFormat(
            "hh:mm:ss");

    /**
     * Creates a new instance of BlockerQueryBuilder
     * 
     * @exception QueryBuilderException
     *                An error occured
     */
    public BlockerQueryBuilder() throws QueryBuilderException {
    }

    /*
     * A query builder can build more than one query object for a given system
     * object input. This method returns an array of applicable ids for each
     * query object. Return a list of block ID's that applies to the given
     * system object. Criteria for selection is that none of the fields required
     * for blocking are null. However, empty Strings are allowed @TODO: use the
     * isNull() flag in the object once its ready @return String[] containing
     * applicable ID's
     */
    /**
     * See QueryBuilder
     * 
     * @return See QueryBuilder
     * @param crit
     *            See QueryBuilder
     * @param opts
     *            See QueryBuilder
     * @exception QueryBuilderException
     *                See QueryBuilder
     */
    public String[] getApplicableQueryIds(SearchCriteria crit,
            SearchOptions opts) throws QueryBuilderException {
        try {
            EOSearchCriteria eoSearchCriteria = (EOSearchCriteria) crit;
            SystemObject sysObj = eoSearchCriteria.getSystemObject();
            SystemObject sysObj2 = eoSearchCriteria.getSystemObject2();
            SystemObject sysObj3 = eoSearchCriteria.getSystemObject3();
            ArrayList retIds = new ArrayList();

            ObjectNode eo = null;
            ObjectNode eo2 = null;
            ObjectNode eo3 = null;
            if (sysObj != null) {
                eo = sysObj.getObject();
            }
            if (sysObj2 != null) {
                eo2 = sysObj2.getObject();
            }
            if (sysObj3 != null) {
                eo3 = sysObj3.getObject();
            }
            Collection defs = config.getDefinitions();
            Iterator iter = defs.iterator();

            while (iter.hasNext()) {
                BlockDefinition bd = (BlockDefinition) iter.next();

                String currentId = bd.getId();

                Collection rules = bd.getRules();
                Iterator rulesIter = rules.iterator();
                boolean thisIsTheOne = true;

                // loop through all the rules
                while (rulesIter.hasNext()) {
                    BlockRule rule = (BlockRule) rulesIter.next();

                    Collection conditions = rule.getConditions();
                    Iterator condIter = conditions.iterator();

                    // @TODO:consider simplifying the logic, to make it more
                    // readable
                    // for each rule, see if all fields use for conditions has
                    // value
                    while (condIter.hasNext()) {
                        AbstractCondition cond = (AbstractCondition) condIter
                                .next();

                        try {
                            EPath path = cond.getSource();
                            boolean hasValue = false;
                            if (path.getWildCard()) {
                                ArrayList a = (ArrayList)EPathAPI.getFieldValueQB(path, eo);
                                if (a != null) {
                                   for (int i=0; i < a.size(); i++)
                                       if (a.get(i) != null)
                                          hasValue=true;
                                }
                            } else {
                                hasValue=(EPathAPI.getFieldValueQB(path, eo) == null)? false : true;
                            }
                                
                            if (eo == null || !hasValue) {
                                if (cond instanceof RangeCondition) {
                                    // RANGE_SEARCH: In a range condition, all
                                    // we need is either val2 or val3.
                                    // If we do not have val2, and we are not
                                    // dealing with a constant default, then
                                    // we need to check val3. If we do not have
                                    // val3 and it is also does not have
                                    // a constant default, then the query can
                                    // not be executed.
                                    RangeCondition rc = (RangeCondition) cond;
                                    if ((eo2 == null || EPathAPI.getFieldValue(
                                            path, eo2) == null)
                                            && (rc.getDefaultLowerType() != RangeCondition.RANGE_TYPE_CONSTANT)) {
                                        if ((eo3 == null || EPathAPI
                                                .getFieldValue(path, eo3) == null)
                                                && (rc.getDefaultUpperType() != RangeCondition.RANGE_TYPE_CONSTANT)) {
                                            thisIsTheOne = false;
                                            break;
                                        }
                                    }
                                } else {
                                    thisIsTheOne = false;
                                    break;
                                }
                            }
                        } catch (Exception ex) {
                            throw new QueryBuilderException(ex);
                        }
                    }
                    // if even one of the fields required is null, stop checking
                   if (!thisIsTheOne) {
                       break;
                   }
                }

                if (thisIsTheOne) {
                    retIds.add(currentId);
                }
            }
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("getApplicableBlockIds(): list of block ids to return: "
                                + retIds.toString());
            }
            return (String[]) retIds.toArray(new String[] {});
        } catch (Exception e) {
            throw new QueryBuilderException(e);
        }
    }

    /*
     * Given an id (one of the set of ids from getApplicableIds), return a query
     * object.
     */
    /**
     * See QueryBuilder
     * 
     * @param ids
     *            See QueryBuilder
     * @param crit
     *            See QueryBuilder
     * @param opts
     *            See QueryBuilder
     * @exception QueryBuilderException
     *                See QueryBuilder
     * @return See QueryBuilder
     */
    public QueryObject buildQueryObject(String[] ids, SearchCriteria crit,
            SearchOptions opts) throws QueryBuilderException {

        try {
            EOSearchCriteria eoSearchCriteria = (EOSearchCriteria) crit;
            EOSearchOptions eoSearchOptions = (EOSearchOptions) opts;
            SystemObject so = eoSearchCriteria.getSystemObject();
            SystemObject so2 = eoSearchCriteria.getSystemObject2();
            SystemObject so3 = eoSearchCriteria.getSystemObject3();

            if (so == null && so2 == null && so3 == null) {
                throw new QueryBuilderException(
                        "At least one SystemObject must not be null");
            }

            String fullObjPath = null;

            ObjectNode entity = null;
            if (so != null) {
                entity = so.getObject();
                fullObjPath = MetaDataService.getSBRPath(entity.pGetTag());
            }

            ObjectNode entity2 = null;
            if (so2 != null) {
                entity2 = so2.getObject();
                fullObjPath = MetaDataService.getSBRPath(entity2.pGetTag());
            }

            ObjectNode entity3 = null;
            if (so3 != null) {
                entity3 = so3.getObject();
                fullObjPath = MetaDataService.getSBRPath(entity3.pGetTag());
            }

            QueryObject qo = new QueryObject();

            // qo.setQueryOption( QueryObject.SINGLE_QUERY );
            qo.setRootObject(fullObjPath);

            // retrieve fields to return from match config
            EPathArrayList fieldsToRetrieve = eoSearchOptions
                    .getFieldsToRetrieve();
            if (fieldsToRetrieve == null) {
                throw new QueryBuilderException(
                        "Fields to retrieve parameter can not be null");
            }
            qo.setSelect(fieldsToRetrieve.toStringArray());

            // call bindQueryParameters
            Collection blockRules = new ArrayList();
            String[][] hints = new String[1][ids.length];
            for (int i = 0; i < ids.length; i++) {
                BlockDefinition bd = config.getDefinition(ids[i]);
                blockRules.addAll(bd.getRules());
                hints[0][i] = bd.getHint();
            }
            qo.setHints(hints);
            Iterator iter = blockRules.iterator();
            int ruleSize = blockRules.size();
            Condition[][] aConds = new Condition[ruleSize][];
            int j = 0;
            while (iter.hasNext() && j < ruleSize) {
                BlockRule rule = (BlockRule) iter.next();

                Condition[] conds = bindQueryParameters(rule, entity, entity2,
                        entity3);
                aConds[j] = conds;
                j++;
            }

            qo.setCondition(aConds);

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("buildQueryObject(): constructed query object:\n"
                        + qo);
            }
            // return query object
            return qo;
        } catch (Exception e) {
            throw new QueryBuilderException(e);
        }
    }

    /**
     * See QueryBuilder
     * 
     * @param info
     *            See QueryBuilder
     * @exception QueryBuilderException
     *                See QueryBuilder
     */
    public void init(ConfigurationInfo info) throws QueryBuilderException {
        config = (BlockerConfig) info;
    }

    /**
     * Bind values from the entity object to the query Object. Creates the
     * Condition used in the query object from the blocker configuration data.
     * 
     * @param rule
     * @param entity
     * @throws SystemObjectException
     * @throws EPathException
     * @throws ObjectException
     * @return
     */
    private Condition[] bindQueryParameters(BlockRule rule, ObjectNode entity,
            ObjectNode entity2, ObjectNode entity3)
            throws SystemObjectException, EPathException, ObjectException,
            QueryBuilderException {

        Collection c = rule.getConditions();

        Iterator itrConditions = c.iterator();

        // ObjectMap contains list of all the objects participating in ConditionTree
        Map objectMap = new TreeMap();
        String objectName = null;
        AbstractCondition ac = null;
        ObjectTable objectTable = null;

        // Create a Map of Objects, this is to groups fields belonging to same Object. 
        // All fields belonging to the same object will be "AND" togather for the innermost loop.
        
        while (itrConditions.hasNext()) {
            ac = (AbstractCondition) itrConditions.next();
            int lastindex = ac.getSource().getName().lastIndexOf('.');
            objectName = ac.getSource().getName().substring(0, lastindex);

            objectTable = (ObjectTable) objectMap.get(objectName);

            if (objectTable == null) {
                objectTable = new ObjectTable();
                objectMap.put(objectName, objectTable);
            }
            objectTable.addColumn(new ObjectColumn(ac));
        }
 
        Set objectConditionsSet = objectMap.entrySet();
        Map.Entry entry = null;
        Iterator itrObjects = objectConditionsSet.iterator();
        
        ObjectColumn column = null;

        // This loop is to extract the values from the entity, entity2 & entity3 and 
        // put them is a convenient data structures.        
        while (itrObjects.hasNext()) {
            entry = (Map.Entry) itrObjects.next();
            objectTable = (ObjectTable) entry.getValue();
            Iterator itrColumns = objectTable.getColumnIterator();
            while (itrColumns.hasNext()) {                
                column = (ObjectColumn) itrColumns.next();
                EPath source = column.getAbstractCondition().getSource();
                
                List valueList = new ArrayList();
                EPathAPI.getFieldList(source, 0, entity, valueList);
                
                Object value2 = (entity2 == null) ? null : EPathAPI.getFieldValue(
                        source, entity2);
                Object value3 = (entity3 == null) ? null : EPathAPI.getFieldValue(
                        source, entity3);
 
                column.setValueList(valueList);
                column.setValueTwo(value2);
                column.setValueThree(value3);
            }
        }

        objectConditionsSet = objectMap.entrySet();

        entry = null;
        column = null;

        Condition allObjectsCompoundCondition = null;

        itrObjects = objectConditionsSet.iterator();

        // This loop is to construct the Condition Tree
        // Outer most loop for AND ing the "same object but different values" condition tree. 
        while (itrObjects.hasNext()) {
            entry = (Map.Entry) itrObjects.next();
            objectTable = (ObjectTable) entry.getValue();

            Condition sameObjectFieldsDiffIndicesCompoundCondition = null;

            boolean isValueNull = false;
            boolean rangeValue = false;
            int sizeForValue = objectTable.getValuesSize();
            
            if ( sizeForValue == 0 ) { 
                sizeForValue = 1;
                // If size if zero then must iterate once Since it could be a range condition
                isValueNull = true;
            }

            // first innter loop for OR ing the "different values for same object fields" condition tree. 
            for (int i = 0; i < sizeForValue; i++) {

                Condition sameObjectFieldsCompoundCondition = null;
                boolean emptyValue = false;
                Iterator itrColumns = objectTable.getColumnIterator();

                // innter most loop for AND ing the "different fields of same object" condition tree. 
                while (itrColumns.hasNext()) {
                    column = (ObjectColumn) itrColumns.next();
                    ac = column.getAbstractCondition();

                    Object value = null;
                    Object value2 = (Object) column.getValueTwo();
                    Object value3 = (Object) column.getValueThree();

                    if ( !isValueNull ) {
                        value = (Object) column.getValueList().get(i);
                    }
                    
                    //if ((value == null) && (value2 == null) && (value3 == null)) { 
                    if (value == null) {                   	
                       emptyValue = true;
                    }
                    if (ac.getType() == RangeCondition.TYPE) {                    	
                    	rangeValue = true;
                    }
                    Condition conditionSingleOrRange = null;
                    if (value != null || rangeValue) {
                    	conditionSingleOrRange = constructQueryCondition(
                            ac, value, value2, value3);
                    }
                    if (conditionSingleOrRange != null) {
	                    if (sameObjectFieldsCompoundCondition == null) {
	                        sameObjectFieldsCompoundCondition = new Condition(
	                                conditionSingleOrRange);
	                    } else {
	                        sameObjectFieldsCompoundCondition.addConditions("AND",
	                                conditionSingleOrRange);
	                    }
                    }
                }

                if (!emptyValue || rangeValue) {
	                if (sameObjectFieldsDiffIndicesCompoundCondition == null) {
	                    sameObjectFieldsDiffIndicesCompoundCondition = new Condition(
	                            sameObjectFieldsCompoundCondition);
	                } else {
	                    sameObjectFieldsDiffIndicesCompoundCondition.addConditions(
	                            "OR", sameObjectFieldsCompoundCondition);
	                }
        	}
            }

            if (sameObjectFieldsDiffIndicesCompoundCondition != null) {
	            if (allObjectsCompoundCondition == null) {
	                allObjectsCompoundCondition = new Condition(
	                        sameObjectFieldsDiffIndicesCompoundCondition);
	            } else {
	                allObjectsCompoundCondition.addConditions("AND",
	                        sameObjectFieldsDiffIndicesCompoundCondition);
	            }
            }
        }
        return new Condition[] { allObjectsCompoundCondition };

    }

    /**
     * 
     * Construct a Query Condtion ( or Conditions Tree in case of a Range
     * Condition ) for a given AbstractCondition
     * 
     * @param ac
     * @param value
     * @param value2
     * @param value3
     * @throws SystemObjectException
     * @throws EPathException
     * @throws ObjectException
     * @throws QueryBuilderException
     * @return Condition
     */

    private Condition constructQueryCondition(AbstractCondition ac,
            Object value, Object value2, Object value3)
            throws SystemObjectException, EPathException, ObjectException,
            QueryBuilderException {

        Condition qmCondition = null;
        String operator;
        String field;

        field = ac.getField();

        EPath source = ac.getSource();

        int type = ac.getType();
        switch (type) {
        case ExactCondition.TYPE:
            operator = "=";
            qmCondition = new Condition(field, operator, value, true);
            break;
        case StartsWithCondition.TYPE:
            operator = "like";
            qmCondition = new Condition(field, operator, value, true);
            break;
        case NotEqualsCondition.TYPE:
            operator = "<>";
            qmCondition = new Condition(field, operator, value, true);
            break;
        case GreaterThanCondition.TYPE:
            operator = ">";
            qmCondition = new Condition(field, operator, value, true);
            break;
        case GreaterThanEqualsCondition.TYPE:
            operator = ">=";
            qmCondition = new Condition(field, operator, value, true);
            break;
        case LessThanCondition.TYPE:
            operator = "<";
            qmCondition = new Condition(field, operator, value, true);
            break;
        case LessThanEqualsCondition.TYPE:
            operator = "<=";
            qmCondition = new Condition(field, operator, value, true);
            break;
        case ContainsCondition.TYPE:
            operator = "in";
            qmCondition = new Condition(field, operator, value, true);
            break;
        case RangeCondition.TYPE:
            RangeCondition rc = (RangeCondition) ac;
            int lowerType = rc.getDefaultLowerType();
            int upperType = rc.getDefaultUpperType();
            boolean mixedRangeSearch = false;
            
            //  Check if the range search involves a mixed offset and constant
            if (lowerType != upperType) {
                mixedRangeSearch = true;
            }
            
            //  Determine now if value3 needs to be recalculated because
            //  value2 may change.  This is only for a search with an upper and
            //  lower offset.
            boolean offsetOnlyCalculateValue3 = false;
            if (value == null || value2 != null) {
                offsetOnlyCalculateValue3 = true;
            }
            //  Determine now if value3 needs to be recalculated.
            //  This is only for a mixed search with an offset and constant
            boolean mixedCalculateValue3 = false;
            if (value != null || value3 == null) {
                mixedCalculateValue3 = true;
            }
            if (value2 == null) {
                // Try to compute the value based on default
                // Check if offsets are used with an exact range value or
                // if there is a mixed offset and constant search.
                if (lowerType == RangeCondition.RANGE_TYPE_CONSTANT) {
                    if ((value == null || value3 != null)
                        || (value != null && value3 == null && mixedRangeSearch)){
                        String lowerVal = rc.getDefaultLowerValue();
                        value2 = getDefaultValue(field, value, lowerVal, lowerType);
                    }
                } else {
                    String lowerVal = rc.getDefaultLowerValue();
                    value2 = getDefaultValue(field, value, lowerVal, lowerType);
                }
            } 
            if (value3 == null) {
                // Try to compute the value based on default
                // Check if offsets are used with an exact range value or
                // if there is a mixed offset and constant search.
                if (upperType == RangeCondition.RANGE_TYPE_CONSTANT) {
                    if (offsetOnlyCalculateValue3 
                        || (mixedCalculateValue3 && mixedRangeSearch)) {
                        String upperVal = rc.getDefaultUpperValue();
                        value3 = getDefaultValue(field, value, upperVal, upperType);
                    }
                } else {
                    String upperVal = rc.getDefaultUpperValue();
                    value3 = getDefaultValue(field, value, upperVal, upperType);
                }
            } 
            if (value2 == null && value3 == null) {
                // If no defaults can be computed, then use value in an equals
                // clause
                if (value == null) {
                    // We should never get here if getApplicableQueryIds is
                    // working correctly
                    throw new QueryBuilderException(
                            "Insufficient data to execute block");
                }
                qmCondition = new Condition(field, "=", value, true);
            } 
            else {
                if (value2 != null) {
                    if (qmCondition == null) {
                        qmCondition = new Condition(field, ">=", value2, true);
                    } else {
                        qmCondition.addConditions("AND", new Condition(field,
                                ">=", value2, true));
                    }
                }
                if (value3 != null) {
                    if (qmCondition == null) {
                        qmCondition = new Condition(field, "<=", value3, true);
                    } else {
                        qmCondition.addConditions("AND", new Condition(field,
                                "<=", value3, true));

                    }
                }
            }
            break;
        default:
            throw new IllegalArgumentException("Unrecgonized condition type: "
                    + type);
        }
        return qmCondition;
    }

    // RANGE_SEARCH: method to compute the default value if it can be computed.
    // Returns null if value can not be computed (insufficient info).
    private Object getDefaultValue(String field, Object value, String rangeVal,
            int rangeType) throws QueryBuilderException {

        try {
            Object obj = null;
            if (rangeVal != null) {
                String fieldType = MetaDataService.getFieldType(field);
                switch (rangeType) {
                case RangeCondition.RANGE_TYPE_CONSTANT:
                    if (fieldType.equalsIgnoreCase(FieldDef.STRINGFIELD)) {
                        // String constants left as-is
                        obj = rangeVal;
                    } else if (fieldType.equalsIgnoreCase(FieldDef.DATEFIELD)) {
                        // Date constants are parsed in yyyy-MM-dd format (SQL
                        // standard)
                        obj = mDateFormat.parse(rangeVal);
                    } else if (fieldType.equalsIgnoreCase(FieldDef.BYTEFIELD)) {
                        // Byte constants are parsed in constructor
                        obj = new Byte(rangeVal);
                    } else if (fieldType.equalsIgnoreCase(FieldDef.CHARFIELD)) {
                        // Byte constants are parsed in constructor
                        obj = new Character(rangeVal.charAt(0));
                    } else if (fieldType.equalsIgnoreCase(FieldDef.INTFIELD)) {
                        // Int constants are parsed in constructor
                        obj = new Integer(rangeVal);
                    } else if (fieldType.equalsIgnoreCase(FieldDef.FLOATFIELD)) {
                        // Float constants are parsed in constructor
                        obj = new Float(rangeVal);
                    } else if (fieldType.equalsIgnoreCase(FieldDef.LONGFIELD)) {
                        // Long constants are parsed in constructor
                        obj = new Long(rangeVal);
                    } else {
                        throw new QueryBuilderException(
                                "Unrecognized data type for default range constant: "
                                        + fieldType);
                    }
                    break;
                case RangeCondition.RANGE_TYPE_OFFSET:
                    if (value != null) {
                        if (fieldType.equalsIgnoreCase(FieldDef.DATEFIELD)) {
                            // Date offsets add a numeric value to the day field
                            Calendar c = new GregorianCalendar();
                            c.setTime((java.util.Date) value);
                            c.add(Calendar.DATE, Integer.parseInt(rangeVal));
                            obj = c.getTime();
                        } else if (fieldType
                                .equalsIgnoreCase(FieldDef.BYTEFIELD)) {
                            // Same semantics as '+'
                            byte i = ((Byte) value).byteValue();
                            byte i2 = Byte.parseByte(rangeVal);
                            obj = new Byte((byte) (i + i2));
                        } else if (fieldType
                                .equalsIgnoreCase(FieldDef.INTFIELD)) {
                            // Same semantics as '+'
                            int i = ((Integer) value).intValue();
                            int i2 = Integer.parseInt(rangeVal);
                            obj = new Integer(i + i2);
                        } else if (fieldType
                                .equalsIgnoreCase(FieldDef.FLOATFIELD)) {
                            // Same semantics as '+'
                            float f = ((Float) value).floatValue();
                            float f2 = Float.parseFloat(rangeVal);
                            obj = new Float(f + f2);
                        } else if (fieldType
                                .equalsIgnoreCase(FieldDef.LONGFIELD)) {
                            // Same semantics as '+'
                            long i = ((Long) value).longValue();
                            long i2 = Long.parseLong(rangeVal);
                            obj = new Long(i + i2);
                        } else {
                            throw new QueryBuilderException(
                                    "Unrecognized data type for default range offset: "
                                            + fieldType);
                        }
                    }
                    break;
                default:
                    throw new QueryBuilderException(
                            "Unrecognized default range type: " + rangeType);
                }
            }
            return obj;
        } catch (Exception e) {
            throw new QueryBuilderException(e);
        }
    }

}
