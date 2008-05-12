/**
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

package com.sun.mdm.index.dataobject;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Generic Object which represents a record in any input files in
 * the initial Loader or data analysis tool. The DataObject is mapped to single
 * line or record in loader file.
 * 
 * Note the underlying data structure use by dataObject to store and retrieve
 * fields and childType is ArrayList. Hence any direct access to DataObject
 * methods apart from using ePath API will throw runtime exception like
 * IndexOutOfBoundsException if in-correct field index or childType index or
 * child instance index is used.
 * 
 * @author Sujit Biswas, Charles Ye
 * 
 */
public class DataObject {

	/** A list of fields belonging the data object. */
	protected ArrayList<String> fields = new ArrayList<String>();

    /**
	 * A list of child types, each child type contains a list of
	 * child instance which is also a DataObject.
	 */
	protected ArrayList<ChildType> childTypes = new ArrayList<ChildType>();

	/**
	 * Get a list of child types.
	 * 
	 * @return ArrayList<ChildType>	 A list of child types.
	 */
	public ArrayList<ChildType> getChildTypes() {
		return childTypes;
	}

	/**
	 * Add a child type. Use this method when childType are populated sequentially, 
     * i.e in the order specified in the object definition, other wise use epath API.
	 * 
	 * @param	childType	childType 
	 * @return	boolean		true if add successfully.
	 */
	public boolean addChildType(ChildType childType) {
		return childTypes.add(childType);
	}

	/**
	 * Get a list of field values.
	 * 
	 * @return	ArrayList<String>	A list of fields.
	 */
	public ArrayList<String> getFieldValues() {
		return fields;
	}

	/**
	 * Add a field value. Use this method when fields are populated sequentially, 
     * i.e in the order specified in the object definition, other wise use epath API.
     *  
	 * @param	field	field in String.	
	 * @return	boolean	true if add successfully.
	 */
	public boolean addFieldValue(String field) {
		return fields.add(field);
	}

	/**
	 * Insert the specified element at the specified position in this list.
	 * Shifts the element currently at that position (if any) and any subsequent
	 * elements to the right (adds one to their indices).
	 * 
	 * @param index		index at which the specified element is to be inserted.
	 * @param element	element to be inserted.
	 * @throws IndexOutOfBoundsException	if the index is out of range
	 *             						    <tt>(index &lt; 0 || index &gt; size())</tt>.
	 */
	public void add(int index, String element) {
		fields.add(index, element);
	}

	/**
	 * Ensure the minimum capacity for fields.
	 * @param minCapacity
	 */
	private void ensureCapacity(int minCapacity) {

		int size = minCapacity + 1;
		if (fields.size() < size) {
			for (int i = fields.size(); i < size; i++) {
				fields.add(i, null);
			}
		}
	}

	/**
	 * Get the value of field at the given index.
	 * 
	 * @param fieldIndex	Field index.
	 * @return	String		Field value.
	 * 
	 */
	public String getFieldValue(int fieldIndex) {
		return fields.get(fieldIndex);
	}

	/**
	 * Remove the field at the specified position in this list. Shifts any
	 * subsequent elements to the left (subtracts one from their indices).
	 * 
	 * @param 	index	The index of the element to removed.
	 * @return	String	The field that was removed from the list.
	 * @throws IndexOutOfBoundsException	If index out of range <tt>(index
	 * 		  								&lt; 0 || index &gt;= size())</tt>.
	 */
	public String remove(int index) {
		return fields.remove(index);
	}

	/**
	 * @param fieldIndex
	 * @return
	 */
	public boolean hasField(int fieldIndex) {
		if (fieldIndex < 0) {			
			return false;
		} else {
			return fieldIndex < fields.size();
		}
	}

	/**
	 * Set the value of field at the given index
	 * 
	 * @param fieldIndex
	 * @param fieldValue
	 * @return
	 * 
	 */
	public String setFieldValue(int fieldIndex, String fieldValue) {
		ensureCapacity(fieldIndex);
		return fields.set(fieldIndex, fieldValue);
	}

	public void addChild(int childType, DataObject child) {

		ChildType ct = ensureChildType(childType);
		ct.addChild(child);

	}

	/**
	 * set child for a given childType and given child position, This always
	 * replaces an existing DataObject or if the childPosition represents a null
	 * value
	 * 
	 * @param childType
	 * @param child
	 * @param childPosition
	 */
	public void setChild(int childType, DataObject child, int childPosition) {
		ChildType ct = ensureChildType(childType);
		ct.set(childPosition, child);
	}

	/**
	 * @param childType
	 * @return
	 */
	private ChildType ensureChildType(int childType) {

		int size = childType + 1;

		if (childTypes.size() < size) {
			for (int i = childTypes.size(); i < size; i++) {
				childTypes.add(i, new ChildType());
			}
		}

		ChildType ct = childTypes.get(childType);

		return ct;
	}

	/**
	 * get child for a given childType and given child position
	 * 
	 * @param childType
	 * @param childPosition
	 * @return
	 */
	public DataObject getChild(int childType, int childPosition) {
		return childTypes.get(childType).get(childPosition);

	}

	/**
	 * Check whether a child Instance exist for a given childType.
	 * 
	 * @param	childType	child type.
	 * @return	boolean		true if the specified child exists.
	 */
	public boolean hasChild(int childType) {
		if (childType < 0) {
			return false;
		}
		
		int limit = childType + 1;
		if (limit > childTypes.size()) {
			return false;
		} else {
			ChildType ct = childTypes.get(childType);
			if (ct == null || ct.getChildren().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Get all children for a given childType
	 * 
	 * @param	childType			child type.
	 * @return	List<DataObject>	A list of children for given child type. 
	 */
	public List<DataObject> getChildren(int childType) {
		return childTypes.get(childType).getChildren();

	}
	
	/**
	 * @see java.lang.Object#equals(Object).
	 */
	public boolean equals(DataObject d) {
		if (fields.size() != d.getFieldValues().size()) {
			return false;
		}
		
		for (int i = 0; i < fields.size(); i++) {
		  String val1 = fields.get(i);
		  String val2 = d.getFieldValue(i);
		  if (val1 == null) {
			  if ( val2 != null) {
				  return false;
			  } else {
				  continue;
			  }
		  } else if (val2 == null) {
			  if ( val1 != null) {
				  return false;
			  } else {
				  continue;
			  }
		  }
		  if (!val1.equals(val2) ) {
			  return false;
		  }		  
		}		
		return true;
	}

	/**
	 * @see java.lang.Object#toString().
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("fields: \n");
		for (String f : fields) {
			sb.append("\tfield: ");
			sb.append(f);
			sb.append("\n");
		}

		if (!childTypes.isEmpty()) {
			sb.append("childTypes: \n");
		}
		
		int i = 1;
		for (ChildType ct : childTypes) {
			sb.append("childType-" + i++);
			sb.append(ct);
			sb.append("\n");
		}
		return sb.toString();
	}

}
