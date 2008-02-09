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
package com.sun.mdm.index.matching.converter;

import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.configurator.impl.matching.MatchingConfiguration;
import com.sun.mdm.index.configurator.impl.matching.MatchColumn;
import com.sun.mdm.index.util.Localizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * Converts the matchfields configured for a SystemObject into a flat
 * tuple structure (Array of array)
 * @author aegloff
 * @version $Revision: 1.1 $
 */
public class MatchTupleConverter 
        implements SystemObjectConverter {
    
    private MatchingConfiguration mc = null; 
    // Default Date conversion format
    private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");    
    
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    
    private static final int INITIAL_CHILD = 0;
    
    /** 
     * Creates a new instance of MatchTupleConverter 
     * @throws InstantiationException if Configuration Service can not be 
     * instantiated
     */
    public MatchTupleConverter() 
            throws InstantiationException {
        mc = (MatchingConfiguration) 
                ConfigurationService.getInstance().getConfiguration(
                MatchingConfiguration.MATCHING);
    }

    /**
     * Sets the format for converting java.util.Date fields into Strings
     * to convert into the the String array tuple.
     * The default format is yyyyMMdd
     * @param aFormat the new DateFormat to use for conversion
     */
    public void setDateConversionFormat(DateFormat aFormat) {
        dateFormat = aFormat;
    }
    
    /**
     * Converts the matchfields configured for a SystemObject into a flat
     * tuple structure (Array of array)
     * @param base the base SystemObject to convert into tuples
     * @return the MatchTuples which contains the tuples and the metadata 
     * describing the tuple structure
     * @throws ObjectException If retrieving the SystemObject values fails
     * @throws EPathException If the configured EPaths for matching are invalid
     */    
    public MatchTuples convert(SystemObject base) 
            throws ObjectException, EPathException {
        
        MatchTuples matchTuples = null;
      
        ArrayList ePaths = new ArrayList();
        ArrayList columnQualifiedNames = new ArrayList();
        ArrayList columnMatchIDs = new ArrayList();

        //? now that it's not relative anymore... how to find this pos??
        int ePathPos = 2;
        
        ObjectNode eo = base.getObject();

        // Build up the relevant ePaths for the match columns of the 
        // System Object
        // At the same time, build up the metadata to describe the structure 
        // that will be built.
        ArrayList matchCols = 
            mc.getSystemObjectMatching(eo.pGetType()).getMatchColums();
        Iterator iter = matchCols.iterator();
        while (iter.hasNext()) {
            MatchColumn c = (MatchColumn) iter.next();
            String fullyQualifiedName = c.getQualifiedName();
            String matchID = c.getMatchType();
            EPath ePath = c.getEPath();
            ePaths.add(ePath);
            columnQualifiedNames.add(fullyQualifiedName);
            columnMatchIDs.add(matchID);
        }

        // Build the tuples. The column order of the tuples returned 
        // does not correspond to the order of the passed in ePaths
        TupleInfo tupleInfo = getMatchTuples(eo, ePaths, ePathPos);
        
        ArrayList arrayConverter = new ArrayList();

        // Put columns into right order
        ArrayList tuplesToOrder = tupleInfo.tuples;
        ArrayList existingOrder = tupleInfo.ePaths;
        
        // The sourcePos maps the right targetPosition (index) to 
        // the source position.
        
        int[] sourcePos = new int[ePaths.size()];
        for (int orderCount = 0; orderCount < ePaths.size(); orderCount++) {
            // Find the position of where to get the right ePath 
            // in the existing order 
            int source = existingOrder.indexOf(ePaths.get(orderCount));
            sourcePos[orderCount] = source;
        }

        // Sort the match column information.  The individual elements of each
        // tuple may be out of order because we traversed the child objects,
        // which might be in a different order than the match columns, epaths, 
        // etc.
        
        //int fieldCount = ((ArrayList) tuplesToOrder.get(0)).size();
        int fieldCount = ePaths.size(); //HF6618360 Relay Health  
        String[][] tuplesArray = new String[tuplesToOrder.size()][fieldCount];
        
        // Initialize tuplesArray in case child objects with match fields are 
        // not present.
        for (int i = 0; i < tuplesToOrder.size(); i++) {
            for (int j = 0; j < fieldCount; j++) {
                tuplesArray[i][j] = null;
            }
        }
        for (int tupleIndex = 0; tupleIndex < tuplesToOrder.size(); tupleIndex++) {
            ArrayList oldTuple = (ArrayList) tuplesToOrder.get(tupleIndex);
            for (int i = 0; i < fieldCount; i++) {
                int source = sourcePos[i];
                if (source > -1) {
                    tuplesArray[tupleIndex][i] = (String) oldTuple.get(source);
                }
            }
        }
        
        // Convert to an array of arrays.
        EPath[] ePathsArray = (EPath[]) ePaths.toArray(new EPath[ePaths.size()]);
        String[] columnMatchIDsArray = (String[]) 
            columnMatchIDs.toArray(new String[columnMatchIDs.size()]);
        String[] columnQualifiedNamesArray = (String[]) 
                columnQualifiedNames.toArray(
                new String[columnQualifiedNames.size()]);
        MatchColumn[] matchColsArray = 
                (MatchColumn[]) matchCols.toArray(
                new MatchColumn[matchCols.size()]);
        matchTuples = new MatchTuples(columnMatchIDsArray, 
                                      columnQualifiedNamesArray, 
                                      ePathsArray, 
                                      matchColsArray, 
                                      tuplesArray);
        
        return matchTuples;
    }    
    
    /**
     * Recursive method to obtain all the tuples of the match columns of 
     * an object node and it's children.
     * The column order of the tuples returned does not correspond
     * to the order of the passed in ePaths
     * @objNode current object node
     * @ePaths relevant to the current object node 
     * @ePathNodePos the element position in the EPath that points to the 
     * current position corresponding to the current object node. The method 
     * then processesthe fields and children if applicable.
     * @return all the tuples of that object node and it's relevant children, 
     * as well as the ePaths describing the order of the returned columns in 
     * the tuples.
     * @throws ObjectException if the data for the match fields 
     * could not be retrieved from the ObjectNode
     */
    TupleInfo getMatchTuples(ObjectNode objNode, ArrayList ePaths, 
                             int ePathNodePos) 
            throws ObjectException {
                
        ArrayList validEPaths = new ArrayList();
        ArrayList results = getMatchTuplesIter(objNode, ePaths, validEPaths, 
                                               ePathNodePos, INITIAL_CHILD);
        return new TupleInfo(validEPaths, results);
    }        
        
    /*
     * Creates tuples for an object and any children.
     *
     * @param objNode  current node being processed.
     * @param ePaths  all ePaths 
     * @param validEPaths  valid ePaths (used for a child object).
     * @ePathNodePos the element position in the EPath that points to the 
     * current position corresponding to the current object node. The method 
     * then processesthe fields and children if applicable.
     * @param initialChildFlag  indicates if the object is the initial child
     * of its type (or top level node) to be processed.  If so, then the ePaths 
     * should be added for it.  Otherwise, don't add them to avoid duplicates.
     * @throws ObjectException if errors are encountered.
     */
    ArrayList getMatchTuplesIter(ObjectNode objNode, ArrayList ePaths, 
                                ArrayList validEPaths, int ePathNodePos,
                                int initialChildFlag) 
            throws ObjectException {
        //  if objNode has match fields, add them to results
        ArrayList results = new ArrayList();
        TokenInfo info = getRelevantTokens(ePaths, ePathNodePos);
        // The map is from object node type of the child, to the corresponding
        // EPath it came from
        ChildInfo[] relevantChildrenMatchFields = info.relevantChildrenMatchFields;
        // array of the relevant fields on the current object node, from 
        // field name to corresponding ePath
        FieldInfo[] relevantFields = info.relevantFields;
        
        // Process all match fields for this node
        
        ArrayList fieldValues = new ArrayList();
        ArrayList fieldEPaths = new ArrayList();

        int fieldCount = 0;
        for (fieldCount = 0; fieldCount < relevantFields.length; fieldCount++) {
            String fieldName = relevantFields[fieldCount].fieldName;
            EPath fieldEPath = relevantFields[fieldCount].fullEPath;
            try {
                String convertedValue = convertValue(objNode, fieldName);
                //  Construct ArrayList of field values.
                if (convertedValue != null) {
                    fieldValues.add(convertedValue);
                } else {
                    //  placeholder for non-existent match values 
                    fieldValues.add("");
                }
            } catch (ObjectException ex) {
                mLogger.warn(mLocalizer.x("MAT030: Unable to obtain configured " + 
                                          "match field: {0} with EPath: {1}: {2}", 
                                          fieldName, fieldEPath.toString(), ex.getMessage()));
                throw ex;
            }
            fieldEPaths.add(fieldEPath);
        }
        // Add to results if there were valid fields.  Also add ePaths for the 
        // top level object or the first instance of a child of given type.
        if (fieldCount > 0) {
            results.add(fieldValues); 
            if (initialChildFlag == INITIAL_CHILD) {
                validEPaths.addAll(fieldEPaths);
            }
        }
        
        ArrayList childNodeTags = objNode.pGetChildTags();
        if (childNodeTags == null || childNodeTags.size() == 0) {
            if (fieldCount > 0) {
                return results;
            } else {
                return null;
            }
        }
        
        //  Process all children types
        
        ArrayList tempList = null;
        int childEPathNodePos = ePathNodePos + 1;
        for (int i = 0; i < childNodeTags.size(); i++) {
            ArrayList childResults = new ArrayList();
            
            //  Process all children of a given type
            
            String tag = childNodeTags.get(i).toString();
            ArrayList childInstances = objNode.pGetChildren(tag);
            if (childInstances != null) {
                ArrayList tempResults = null;
                for (int j = 0; j < childInstances.size(); j++) {
                    ObjectNode childNode = (ObjectNode) childInstances.get(j);
                    ArrayList tempChildEPath = getChildEPaths(relevantChildrenMatchFields, 
                                                              tag);
                    if (tempChildEPath != null) {
                        ArrayList childEPaths = new ArrayList(tempChildEPath);
                        
                        // During the first iteration (j = 0, the valid 
                        // ePaths will be added)
                        tempResults = getMatchTuplesIter(childNode, 
                                                         childEPaths, 
                                                         validEPaths, 
                                                         childEPathNodePos, 
                                                         j);
                        if (tempResults != null) {
                            childResults.add(tempResults);
                        }
                    } else {
                        tempResults = null;
                        break;
                    }
                }
                if (tempResults != null) {
                    tempList = new ArrayList(createTuples(results, childResults));
                    results.clear();
                    results.addAll(tempList);
                }
            }
        }
        return results;
    }        
    
    /*
     * Retrieves the ePaths for all child objects of a given node.
     *
     * @param childrenMatchFields  ChildInfo array containing the ePath information.
     * @param tag  tag for the child object
     * @return an array list of all ePaths for all child objects of a given node.
     */
    ArrayList getChildEPaths(ChildInfo[] childrenMatchFields, String tag) {
        
        if (childrenMatchFields == null || childrenMatchFields.length == 0 
            || tag == null || tag.length() == 0) {
            return null;
        }
        ArrayList ePaths = new ArrayList();
        for (int i = 0; i < childrenMatchFields.length; i++) {
            ChildInfo obj = childrenMatchFields[i];
            for (int j = 0; j < obj.ePaths.size(); j++) {
                EPath curEPath = (EPath) obj.ePaths.get(j);
                if (checkEPath(tag, curEPath) == true) {
                    ePaths.add(curEPath);
                }
            }
        }
        return ePaths;
    }

    /* 
     * Checks if an epath applies to an object with the current tag.  The epath
     * must have this tag as the last element before the field name.  For example,
     * if epath = "Person.Name.FirstName" and tag = "Name", then this would return
     * true.  If epath = "Person.Name.NameComponents.FirstName" and tag = "Name",
     * then this would return false.
     *
     * @param tag  object tag
     * @param epath  epath to check
     * @return true if the epath applies, false otherwise.
     */
    private boolean checkEPath(String tag, EPath epath) {
        if (tag == null || tag.length() == 0 || epath == null) {
            return false;
        }
        String lastChildName = epath.getLastChildName();
        if (tag.compareToIgnoreCase(lastChildName) == 0) {
            return true;
        }
        return false;
    }

    /*
     * Converts a value to a String representation
     *
     * @param value  object to convert
     * @param fieldName  name of field to retrieve
     * @return string representation of the value
     * @throws ObjectException if an error is encountered
     */
    private String convertValue(ObjectNode obj, String fieldName) throws ObjectException {
        Object curVal = (Object) obj.getValue(fieldName);
        String newVal = null;
        if (curVal != null) {
            // Format dates in the desired format 
            // Other types are formated according to their 
            // toString format
            if (curVal instanceof java.util.Date) {
                newVal = dateFormat.format((java.util.Date) curVal);
            } else {
            	// Boolean is mapped to Number in the database
            	// Need to convert it to its numeric value
            	if (curVal instanceof Boolean) {
            		newVal = ((Boolean)curVal).booleanValue() ? "1" : "0";
            	} else {
            		newVal = curVal.toString();
            	}
            }
        } 
        return newVal;
    }
    
    /**
     * Given two ArrayLists of ArrayLists, create all the
     * possible combinations
     * @param first the first ArrayList to combine with the second
     * @param second the second ArrayList to combine with the first
     * @return an ArrayList with all the tuple combinations
     */
    ArrayList createTuples(ArrayList first, ArrayList second) {
        if (first == null || first.size() == 0) {
            return second;
        }
        if (second == null || second.size() == 0) {
            return first;
        }
        
        ArrayList tuples = new ArrayList();
        
       java.util.Iterator firstIter = first.iterator();
       
       while (firstIter.hasNext()) {
           ArrayList rowInFirst = (ArrayList) firstIter.next();
           java.util.Iterator secondIter = second.iterator();
           while (secondIter.hasNext()) {
               ArrayList rowInSecond = (ArrayList) secondIter.next();
               // combine rows, save as tuple
               ArrayList newRow = (ArrayList) rowInFirst.clone();
               for (int i = 0; i < rowInSecond.size(); i++) {
                   Object element = rowInSecond.get(i);
                   if (element instanceof ArrayList) {
                       ArrayList aList = (ArrayList) element;
                       int arrayListSize = aList.size();
                       if (arrayListSize == 0) {
                           ArrayList emptyElement = new ArrayList();
                           newRow.add(emptyElement);
                       } else {
                           for (int j = 0; j < arrayListSize; j++) {
                                newRow.add(aList.get(j));
                           }
                       }
                   } else {
                        newRow.add(rowInSecond.get(i));
                   }
               }
               tuples.add(newRow);
           }
        }
       return tuples;
    }

    /**
     * Looks at the ePaths at the given position and figures out what direct
     * children are present in the ePath and what fields are present.
     * @ePaths The ePaths defining the Match fields relevant to the current 
     * node.
     * @param ePathNodePos the position of the current node token in the ePath.
     * @return The TokenInfo with The map from object node type of the child, 
     * to the corresponding EPath it came from
     * and the fieldName on the current node as well as the corresponding ePath
     */
    TokenInfo getRelevantTokens(ArrayList ePaths, int parentEPathNodePos) {
        TokenInfo info = new TokenInfo();
        ArrayList relevantFields = new ArrayList();
        ArrayList relevantChildren = new ArrayList();
        int childPos = parentEPathNodePos + 1;

        Iterator ePathIter = ePaths.iterator();
        while (ePathIter.hasNext()) {
            EPath currEPath = (EPath) ePathIter.next();
            if (currEPath.isTokenExists(childPos)) {
                String tokenName = currEPath.getTag(childPos);
                if (currEPath.isFieldToken(childPos)) {
                    relevantFields.add(new FieldInfo(tokenName, currEPath));
                } else {
                    // If type not already in list, add a new ChildInfo
                    // If type already exists, add ePath to the ChildInfo
                    int existingPos = relevantChildren.indexOf(tokenName);
                    if (existingPos == -1) {
                        ArrayList childEPaths = new ArrayList();
                        childEPaths.add(currEPath);
                        relevantChildren.add(
                            new ChildInfo(tokenName, childEPaths));
                    } else {
                        ChildInfo existingInfo = 
                            (ChildInfo) relevantChildren.get(existingPos);
                        existingInfo.ePaths.add(currEPath);
                    }
                }
            }
        }
        
        ChildInfo[] convChildren = 
            (ChildInfo[]) relevantChildren.toArray(
                new ChildInfo[relevantChildren.size()]);
        FieldInfo[] convFields = 
            (FieldInfo[]) relevantFields.toArray(
                new FieldInfo[relevantFields.size()]);
        info.relevantChildrenMatchFields= convChildren;
        info.relevantFields = convFields;
        
        return info;
    }
    
    /**
     * Utility class to return structured info from methods regarding 
     * EPath tokens
     * The lack of accessors is by design for performance reasons. 
     * The use of these classes is in the spirit of 'structs'
     */
    class TokenInfo {
        protected ChildInfo[] relevantChildrenMatchFields;
        // An array of array of the relevant fields on the current 
        // object node, from field name to corresponding ePath
        protected FieldInfo[] relevantFields;
    }

    /**
     * Utility class to return structured info from methods regarding 
     * the object node type of a child, mapping from the object node
     * type to the corresponding ePath
     * The lack of accessors is by design for performance reasons. 
     * The use of these classes is in the spirit of 'structs'
     */        
    class ChildInfo {
        protected String type;
        protected ArrayList ePaths;
        
        public ChildInfo() {
        }
        public ChildInfo(String aType, ArrayList ePaths) {
            this.type = aType;
            this.ePaths = ePaths;
        }
        
        /**
         * ChildInfo is considered equals if they type String is the same
         * @param objToCompare the object to compare against. Either a String
         * or another ChildInfo object
         */
        public boolean equals(Object objToCompare) {
            if (objToCompare == null) {
                return false;
            } else if (objToCompare instanceof java.lang.String) {
                String aType = (String) objToCompare;
                return (aType.compareTo(this.type) == 0);
            } else if (objToCompare instanceof ChildInfo) {                
                String aType = ((ChildInfo) objToCompare).type;
                return (aType.compareTo(this.type) == 0);
            } else {
                return false;
            }
        }
        
        /**
         * Calculate the hashcode of this object, based 
         * on the equals definition above, just
         * based on the type
         *
         * @return the hashcode 
         */
        public int hashCode() { 
            return type.hashCode(); 
        }
    }
    
    /**
     * Utility class to return structured info from methods regarding 
     * relevant fields on an object node, mapping from the field name
     * to the corresponding ePath
     */        
    class FieldInfo {
        protected String fieldName;
        protected EPath fullEPath;
        
        public FieldInfo() {
        }
        public FieldInfo(String aFieldName, EPath aFullEPath) {
            this.fieldName = aFieldName;
            this.fullEPath = aFullEPath;
        }        
    }
    
    /**
     * Utility class to return structured info from methods regarding 
     * the tuples and the ePaths corresponding to each tuple column
     */
    class TupleInfo {
        protected ArrayList ePaths;
        protected ArrayList tuples;
        
        public TupleInfo() {
        }
        
        public TupleInfo(ArrayList ePaths, ArrayList tuples) {
            this.ePaths = ePaths;
            this.tuples = tuples;
        }        
    }
    
}
