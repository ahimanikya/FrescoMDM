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

/**
 * This represents a ChildType. E.g., a Person has a list of Address, so Address
 * could the childType.  Similarly a Person may have a list of phone numbers so
 * Phone could be childType with one or more instance of Phone(child).
 * 
 * @author Sujit Biswas, Charles Ye
 */
public class ChildType {

	/** A list of child instance for this type. */
	private ArrayList<DataObject> children = new ArrayList<DataObject>();

	/**
	 * Get a list of child objects.
	 * 
	 * @return the children
	 */
	public ArrayList<DataObject> getChildren() {
		return children;
	}

     /**
	 * Add a child instance.
	 * 
	 * @param child	child DataObject. 
	 * @return	Boolean true If a child adds successfully. 
	 */
	public boolean addChild(DataObject child) {
		return children.add(child);
	}

	/**
	 * Set minimum number of children as a capacity.
	 * 
	 * @param minCapacity 
	 */
	private void ensureCapacity(int minCapacity) {
		children.ensureCapacity(minCapacity);
	}

    /**
	 * Get a child instance for the given position.
	 * 
	 * @param	childPosition index of the child.
	 * @return	DataObject a child in the specified index.
	 */
    public DataObject get(int childPosition) {
		return children.get(childPosition);
	}

    /**
	 * Set a child instance for the given position.
	 * 
	 * @param	childPosition	index of the child. 
	 * @param	child			child DataObject. 
	 * @return	DataObject 		child DataObject.
	 */
	public DataObject set(int childPosition, DataObject child) {
		ensureCapacity(childPosition);
		return children.set(childPosition, child);
	}

    /*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (DataObject c : children) {
			sb.append("\n\tchildInstance: ");
			sb.append(c);
		}
		return sb.toString();
	}
}
