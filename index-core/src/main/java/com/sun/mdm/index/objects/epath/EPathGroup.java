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
package com.sun.mdm.index.objects.epath;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import com.sun.mdm.index.objects.metadata.MetaDataService;


/**
 * 
 * Combines Epath into a group so that operations can be applied to set of Epaths.
 * Used by EORetriever to query EOs. 
 * An  Epath belong to a group if its has same object path, differed only
 * in the fieldnames or filters.
 * So Enterprise.SystemObject.* and Enterprise.SystemObject[LID=1].* epaths belong to same group.
 * But Enterprise.SystemObject.Person.* belong to different group
 * 
 * @author sdua
 * @version $Revision: 1.1 $
 */
public class EPathGroup {
    /**
     * name of fully qualified object for this group. 
     * Does not contain field name
     */
    private String mObjectPath;
    
    /*
     * contains epath fieldPath that ends with * 
     * (* denotes ALL FIELDS ), else it is null
     */
    private String mFieldPath = null;
    
    /*
     * set of all fields in this group
     */
    private Set mFields = new HashSet();
    /*
     * list of all epaths in this group
     */
    private List mEpathList = new ArrayList(); 

    
    /** add a Epath to this group
     * 
     * @param epath
     * @return false, if epath does not belong to this group
     *         true, if epath belong this group. Additionally it adds epath to this group, 
     *         if not already present
     */
    public boolean addEPath(EPath epath) {
    	String objectName = epath.getLastChildPath();
    	String fieldPath = epath.toFieldName();
    	if (mObjectPath == null) {
    	    mObjectPath = objectName;
    	    mEpathList.add(epath);
    	} else if (mObjectPath.equals(objectName)) {
    		/*
    		 * if this epath already exists
    		 */
    		if ( addNewEPath(epath) == false) {
    			return true;
    		}
    		
    	} else {
    		/*
    		 *  this epath does not belong here, so we don't add 
    		 */ 
    		return false;
    	}
    	
    	/*
    	 * add  attributes to the field list
    	 */
    	String fieldName = epath.toFieldName();
    	/* 
    	 * check if mFieldPath is null (does not contain all fields
    	 * then we add the attribute
    	 */
    	if (mFieldPath == null) {
    	  if (fieldName.endsWith("*")) {
    	     mFieldPath = fieldPath;
    	  } else {
    	     if (!mFields.contains(fieldPath)) {
    	    		mFields.add(fieldPath);
    	     }
    	  }
    	}
    	return true;    
    	
    }
    /**
     * return all the fields that are to be retrieved for query for this epathgroup. It is summation of
     * fields specified in an Epath plus any primary fields for every Epath in this group.
     * Moreover If this EPathGroup is for SBR, then it does not include Enterprise fields
     * else it include Enterprise fields. 
     * @return array of select fields
     */
    
    public String[] getSelectFields() {
    	/*
    	 * pick any epath 
    	 */
    	EPath epath = (EPath) mEpathList.get(0);
    	
    	String[] objectPaths = epath.getObjectPaths();
    	List fields = new ArrayList();
    	int initial = 0;
    	
    	if (isSBR()) {
    		/*
    		 * we don't select Enterpriese fields for SBR (1st object)
    		 */
    		initial = 1;
    		/*
    		 *  ChildType is not a primary key, but is required for binding primary object to SBR/SO.
    		 */
    		fields.add("Enterprise.SystemSBR.ChildType");
    	} else if (isSO()) {
    		fields.add("Enterprise.SystemObject.ChildType");
    	}
    	/*
    	 * add key fields to the list for all intermediate objects for an epath until the last object.
    	 * For the last object, we pick the fields specified in either mFieldPath or mFields
    	 */
    
    	for (int i = initial; i < objectPaths.length -1 ; i++) {
    		String object = objectPaths[i];
    		String[] keys = MetaDataService.getPrimaryKey(object);
    		// fields.add(object + ".*" );
    		 for ( int j = 0; j < keys.length; j++) {
    			fields.add(object + "." + keys[j]);    			
    		}
    	}
    	
    	if (mFieldPath != null) {
    		fields.add(mFieldPath);
    	} else {
    		fields.addAll(mFields);
    		
    	}
    	return (String[])fields.toArray( new String[fields.size()]);
    }
    
    /**
     * return List of Filter[][] for each EPath in this group
     * @return List
     */
    
    public List getFilterPaths() {
    	List filterList = new ArrayList();
        Iterator it = mEpathList.iterator();
        while (it.hasNext()) {
        	EPath epath = (EPath) it.next();
        	Filter[][] filters = epath.getFilterPaths();
        	filterList.add(filters);
        	
        }
        return filterList;
    }
    
    /**
     * splits an Epath into many Epaths that can be used to form QueryObjects
     * The first EPath in the list of ePaths returned is always
     * Enterprise.S0 or Enterprise.SBR.
     * So if an EPath is Enterprise.SystemObject.Person.*, then it is split into
     * Epaths: Enterprise.SystemObject.*, Enterprise.SystemObject.Person.*
     *
     * If an EPath is Enterprise.SystemObject.Person[firstName=John].Address.*, 
     * then it is split into
     * Epaths: Enterprise.SystemObject.*, Enterprise.SystemObject.Person[firstName=John].*,
     * Enterprise.SystemObject.Person[firstName=John].Address.*
     *
     * @param epath Epath that needs to be splitted
     * @return split Epaths
     * @throws EPathException
     */
    
    public static EPath[] splitIntoQueryCombinations(EPath epath) 
           throws EPathException {
    	
    	

    	String ePathStr = epath.getName();
    	
    	StringTokenizer tok = new StringTokenizer(ePathStr, ".");
    	int len = tok.countTokens();
    	String pathStr = tok.nextToken();
    	
      
        if (len > 2 && pathStr.startsWith("Enterprise") ) {
        	// We will not create EPath for just Enterprise.*, but rather combine with SO or SBR.
        	len = len-1;
        	pathStr = pathStr + "." + tok.nextToken(); // includes SO or SBR at this point
        } 
        EPath[] epaths = new EPath[len-1];
            
        epaths[0] = EPathParser.parse(pathStr + ".*");
        
        /*
         *  iterate before the last object. So if EPath is Enterprise.SO.Person.Address.*, 
          * iterate until Enterprise.SO.Person
          */
        for (int i = 1; i < len-2; i++) {
            pathStr = pathStr + "." + tok.nextToken();
            
            	/*
            	 *  if it is not last token, add .* since last token may already contains
            	 *  .* or it may have field name
            	 */
            	
            	epaths[i] = EPathParser.parse(pathStr + ".*");
           
        }
        epaths[len-2] = EPathParser.parse(ePathStr);
    	
    	return epaths;
    }
    
    /**
     * 
     * @param epath
     * @return false if this epath is already in epathlist
     *         true, if this is new epath and so added to epathlist
     */
    private boolean addNewEPath(EPath epath) {

    	if (mEpathList.contains(epath)) {
    		return false;
    	}
    	mEpathList.add(epath);
    	return true;
    }
    
    /**
     * is this EPathGroup contains SBR epaths
     * @return
     */
    public boolean isSBR() {
    	return (mObjectPath.indexOf("SystemSBR") > -1);
    }
    
    /**
     * is this EPathGroup contains SO epaths
     * @return
     */
    public boolean isSO() {
    	return (mObjectPath.indexOf("SystemObject") > -1);
    }
    
    /**
     * does input epath contains SBR epaths
     * @return
     */
    public static boolean isSBR(EPath epath) {
    	return (epath.getName().indexOf("SystemSBR") > -1);
    }
    
}
