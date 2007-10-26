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


/**
 * Represents a parsed EPath string and encapsulates the operations needed to
 * access the object represented by the EPath string. Primary.Field
 * Primary.Secondary[@someKey=somevalue,@somemorekey=somemorevalue].* returns
 * the secondary object named Secondary and has key "someKey" of somevalue.
 * Primary.Secondary[1].* access the secondary object by index
 * AObject.BObject[2].CObject[3].* supports multiple levels of access for now,
 * this method does not support Primary.Secondary[*].Field For now, EPath does
 * not support Primary.Secondary[*].Field
 *
 * @author SeeBeyond
 * @version $Revision: 1.1 $
 */
public class EPath implements java.io.Serializable, Cloneable {
    
    /** op code */
    public static final int OP_NOOP = 0;
    /** op code - field accessor */
    public static final int OP_FIELD = 2;
    /** op code - all secondary object as list */
    public static final int OP_ALL_SECONDARY = 3;
    /** op code - all fields return as object */
    public static final int OP_ALL_FIELD = 4;
    /** op code - secondary by key */
    public static final int OP_SECONDARY_BY_KEY = 5;
    /** op code - secondary by index */
    public static final int OP_SECONDARY_BY_INDEX = 6;
    /** op code - secondary by filter*/
    public static final int OP_SECONDARY_BY_FILTER = 7;

    /** filters <TODO> */    
    Filter[][] filters;
    /** indices <TODO> */    
    int[] indices;
    /** keys <TODO> */    
    String[] keys;
    /** operations <TODO> */    
    int[] ops;
    boolean wildCard=false;

    /** tokens. After parsing is complete, tokenQueue only contains names. Any associated index or filter will be
     contained in indices/filters arrays. So if Epath is Enterprise.SystemObject.Person[firstName=foo].
     Then tokenQueue contains strings Enterprise, Systemobject, Person and *
     filters[][] will contain one Filter element (firstName,foo) in 3rd element of filters[]. 
     The rest of elements in filters[] will be null 
     **/    
    String[] tokenQueue;
    /** epath string */    
    private String mName;

    /**
     * disable default constructor
     */
    protected EPath() {
    }

    /** Creates new EPath Object. Package access only, use EPathBuilder to create
     * an EPath object
     * @param name qualified field name
     * @param len length
     */
    EPath(String name, int len) {
        mName = name;

        tokenQueue = new String[len];
        ops = new int[len];
        keys = new String[len];
        filters = new Filter[len][];
        indices = new int[len];
    }

    /** getter for name
     * @return name value
     */
    public String getName() {
        return mName;
    }

    /** get the token tag given the position
     * @param tokenPos position
     * @return The tag of the token at the given position, can represent the
     *      object type for object nodes or field names for fields.
     */
    public String getTag(int tokenPos) {
        String tag = null;

        if ((tokenPos >= 0) && (tokenPos < tokenQueue.length)) {
            tag = tokenQueue[tokenPos];
        }

        return tag;
    }
    
    /** getter for the field tag
     * @return field tag value
     */
    public String getFieldTag() {
    	return tokenQueue[tokenQueue.length-1];
    }
    
    /** getter for the field wildCard
     * @return field wildCard value
     */    
    public boolean getWildCard() {
         return wildCard;
    }


    /** is the token a field
     * @param tokenPos token position
     * @return whether the token the given position represents a field
     */
    public boolean isFieldToken(int tokenPos) {
        boolean isAField = false;

        if ((tokenPos < ops.length) && (tokenPos >= 0) && (ops[tokenPos] == OP_FIELD)) {
            isAField = true;
        }

        return isAField;
    }

    /** is there a token at the position
     * @param tokenPos position
     * @return whether a token exists at the given position
     */
    public boolean isTokenExists(int tokenPos) {
        return (((tokenQueue != null) && (tokenPos >= 0) && (tokenPos < tokenQueue.length)) ? true : false);
    }

    /** clones the epath
     * @return a clone
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnsx) {
            return null;
        }
    }

    /**
     * if two EPath objects has the same path, then they are considered equal
     *
     * @param e another EPath object
     * @return true if both points to the same field, false otherwise
     */
    public boolean equals(EPath e) {
        return mName.equals(e.getName());
    }
    
    
    /**
     * hash code
     * @return int hash code
     */
    
    public int hashCode() {
        return super.hashCode();
    }
    

    /**
     * returns the field name represented by this EPath object
     *
     * @return the field name represented by this EPath object
     */
    public String toFieldName() {
        StringBuffer buf = new StringBuffer(25);
        int len = tokenQueue.length;

        if (len > 0) {
            buf.append(tokenQueue[0]);

            for (int i = 1; i < len; i++) {
                buf.append('.');
                buf.append(tokenQueue[i]);
            }
        }

        return buf.toString();
    }

    /*
     * Returns the name of the last object on the EPath (i.e. to which the 
     * actual field name belongs).  For example: if the EPath name is 
     * "Person.Name.NameComponent.FirstName", this would return "NameComponent".
     * @return name of the last object.
     */
    public String getLastChildName() {
        StringBuffer buf = new StringBuffer(25);
        int len = tokenQueue.length;

        if (len > 1) {
            buf.append(tokenQueue[len - 2]);
        }
        return buf.toString();
    }
    
    /*
     * Returns the path of an EPath minus the field name.  For example,
     * the path of "Person.Address[*].CountryCode" would be "Person.Address"
     *
     * @return path minus the field name.
     *
     */
    public String getLastChildPath() {
        StringBuffer buf = new StringBuffer(25);
        int len = tokenQueue.length;

        if (len > 0) {
            buf.append(tokenQueue[0]);

            for (int i = 1; i < len - 1; i++) {
                buf.append('.');
                buf.append(tokenQueue[i]);
            }
        }

        return buf.toString();
    }
    
    /** gets list of  non-null filters
     *  Each token in a Epath can be associated with list of filters. 
     *  So the returned filters is array of array of Filter for associated token. If a token has no filters, then
     *  the filter[] for that token is null
     *  The name in Filter is a path name (instead of just a name that is normally used in a Filter)
     * @return list of non-null filters
     */
    Filter[][] getFilterPaths() {
    	Filter[][] paths = new Filter[filters.length][];
    	for (int i = 0; i < filters.length; i++) {
    	  if (filters[i] != null && filters[i].length > 0) {
    	  	paths[i] = new Filter[filters[i].length];
    	  	String path = getPath(i+1);
            for (int j = 0; j < filters[i].length;j++) {
            	String filterPath = path + "." + filters[i][j].getField();
            	paths[i][j] = new Filter(filterPath, filters[i][j].getValue());
            }
    	  } else {
    	  	paths[i] = null;
    	  }
            
    	}
    	return paths;
    }
    
    /**
     * Does this Epath contain any filter
     * @return true if contains filters
     */
    boolean containsFilters() {
    	boolean hasFilter = false;
    	for (int i = 0; i < filters.length; i++) {
    		if (filters[i] != null) {
    			hasFilter = true;
    			break;
    		}
    	}
    	
    	return hasFilter;
    	
    }
    
    
    String getPath(int pathLen) {
    	 StringBuffer buf = new StringBuffer(25);
         int len = tokenQueue.length;

         if (len > 0) {
             buf.append(tokenQueue[0]);

             for (int i = 1; i < pathLen  && i < len; i++) {
                 buf.append('.');
                 buf.append(tokenQueue[i]);
             }
         }

         return buf.toString();
    	
    }
    
    /**
     * get all token strings
     * @return all token strings
     */
    String[] getTokenStrings() {
    	return tokenQueue;
    }
    
    /**
     *  Return Fully qualified path names for each Object in this EPath. 
     *  @return full qualified path names for each object in this EPath.
     */
    
    String[] getObjectPaths() {
   	 StringBuffer buf = new StringBuffer(25);
        int len = tokenQueue.length;
        String[] objectPaths = new String[len-1];

        if (len > 0) {
            buf.append(tokenQueue[0]);
            objectPaths[0] = tokenQueue[0];

            /*
             * loop until the last token, which is a field or *
             */
            for (int i = 1;  i < len-1; i++) {
                buf.append('.');
                buf.append(tokenQueue[i]);
                objectPaths[i] = buf.toString();
            }
        }

        return objectPaths;
   	
   }
    
    
    /** return a string
     * @return string representation
     */
    public String toString() {
        return mName;
    }

	/**
	 * @return the filters
	 */
	public Filter[][] getFilters() {
		return filters;
	}

	/**
	 * @param filters the filters to set
	 */
	public void setFilters(Filter[][] filters) {
		this.filters = filters;
	}

	/**
	 * @return the indices
	 */
	public int[] getIndices() {
		return indices;
	}

	/**
	 * @param indices the indices to set
	 */
	public void setIndices(int[] indices) {
		this.indices = indices;
	}

	/**
	 * @return the keys
	 */
	public String[] getKeys() {
		return keys;
	}

	/**
	 * @param keys the keys to set
	 */
	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	/**
	 * @return the ops
	 */
	public int[] getOps() {
		return ops;
	}

	/**
	 * @param ops the ops to set
	 */
	public void setOps(int[] ops) {
		this.ops = ops;
	}

	/**
	 * @return the tokenQueue
	 */
	public String[] getTokenQueue() {
		return tokenQueue;
	}

	/**
	 * @param tokenQueue the tokenQueue to set
	 */
	public void setTokenQueue(String[] tokenQueue) {
		this.tokenQueue = tokenQueue;
	}
}
