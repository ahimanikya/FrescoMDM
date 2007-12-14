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

package com.sun.mdm.index.dataobject.objectdef;

import java.util.ArrayList;

/**
 * 
 * this represent the ObjectDefinition or metaData for a given DataObject
 * 
 * 
 * @author Sujit Biswas
 * 
 */
public class ObjectDefinition {

	private String name;
	
	private String dateFormat;
	
	private String database;

	/**
	 * list of fields
	 */
	ArrayList<Field> fields = new ArrayList<Field>();

	/**
	 * list of children
	 */
	ArrayList<ObjectDefinition> children = new ArrayList<ObjectDefinition>();

	/**
	 * @param name
	 */
	public ObjectDefinition(String name) {
		super();
		this.name = name;
	}

	/**
	 * @param name
	 * @param fields
	 * @param children
	 */
	public ObjectDefinition(String name, ArrayList<Field> fields,
			ArrayList<ObjectDefinition> children) {
		super();
		this.name = name;
		this.fields = fields;
		this.children = children;
	}

	/**
	 * add field
	 * 
	 * @param o
	 * @return
	 */
	public boolean addField(Field o) {
		return fields.add(o);
	}

	/**
	 * add field at a specified location, shift the elements from the current
	 * position to the right
	 * 
	 * @param index
	 * @param element
	 */
	public void addField(int index, Field element) {
		fields.add(index, element);
	}

	private void ensureFieldCapacity(int minCapacity) {
		int size = minCapacity + 1;

		if (fields.size() < size) {
			for (int i = fields.size(); i < size; i++) {
				fields.add(i, null);
			}
		}

	}

	/**
	 * get field for the given index
	 * 
	 * @param index
	 * @return
	 */
	public Field getField(int index) {
		return fields.get(index);
	}

	/**
	 * set the field at the given index
	 * 
	 * @param index
	 * @param element
	 * @return
	 */
	public Field setField(int index, Field element) {
		ensureFieldCapacity(index);
		return fields.set(index, element);
	}

	/**
	 * add child at the given index
	 * 
	 * @param index
	 * @param element
	 */
	public void addchild(int index, ObjectDefinition element) {
		children.add(index, element);
	}

	/**
	 * add child
	 * 
	 * @param o
	 * @return
	 */
	public boolean addchild(ObjectDefinition o) {
		return children.add(o);
	}

	/**
	 * get child at the given index
	 * 
	 * @param index
	 * @return
	 */
	public ObjectDefinition getchild(int index) {
		return children.get(index);
	}

	/**
	 * return the child ObjectDefinition, if the child with a given name exist,
	 * return null otherwise. Note this access is much slower that the indexed
	 * approach, the performance can degrade if the number of children is high
	 * 
	 * @param childName
	 * @return
	 */
	public ObjectDefinition getchild(String childName) {
		for (ObjectDefinition child : children) {
			if (child.getName().equals(childName)) {
				return child;
			}
		}
		return null;
	}

	/**
	 * set child at the given index
	 * 
	 * @param index
	 * @param element
	 * @return
	 */
	public ObjectDefinition setchild(int index, ObjectDefinition element) {
		ensureChildCapacity(index);
		return children.set(index, element);
	}

	private void ensureChildCapacity(int minCapacity) {
		int size = minCapacity + 1;

		if (children.size() < size) {
			for (int i = children.size(); i < size; i++) {
				children.add(i, null);
			}
		}

	}

	/**
	 * @return the children
	 */
	public ArrayList<ObjectDefinition> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(ArrayList<ObjectDefinition> children) {
		this.children = children;
	}

	/**
	 * @return the fields
	 */
	public ArrayList<Field> getFields() {
		return fields;
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("name: " + name + "\n");

		sb.append(" Fields: \n");
		sb.append(fields.toString());

		if (!children.isEmpty())
			sb.append("\nChildren: ");

		for (ObjectDefinition c : children) {

			sb.append("\n child ");
			if (c != null)
				sb.append(c.toString());
		}

		return sb.toString();
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

}
