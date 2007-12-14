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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @author Sujit Biswas
 * 
 */
public class Lookup {

	private static Logger logger = Logger.getLogger(Lookup.class.getName());

	private HashMap<String, HashMap<String, Integer>> lookupMap = new HashMap<String, HashMap<String, Integer>>();

	private HashMap<String, Integer> childIndex = new HashMap<String, Integer>();

	private transient ObjectDefinition objectdef;

	/**
	 * @param objectdef
	 */
	private Lookup(ObjectDefinition objectdef) {
		this.objectdef = objectdef;
		createLookup();
	}

	/**
	 * 
	 * @param objectDefinition
	 */
	private Lookup(InputStream objectDefinition) {
		ObjectDefinitionBuilder b = new ObjectDefinitionBuilder();
		this.objectdef = b.parse(objectDefinition);
		createLookup();
	}

	
	/**
	 * populate the lookupMap and the childIndex
	 * 
	 */
	private void createLookup() {
		createLooupMap(lookupMap, objectdef, objectdef.getName());
		createChildIndex(childIndex, objectdef, objectdef.getName());

	}

	/**
	 * populate the childIndex
	 * 
	 */
	private void createChildIndex(HashMap<String, Integer> childTypeIndex,
			ObjectDefinition context, String prefix) {
		for (int i = 0; i < context.getChildren().size(); i++) {

			ObjectDefinition child = context.getChildren().get(i);
			String cname = child.getName();
			String key = prefix + "." + cname;
			childTypeIndex.put(key, i);
			createChildIndex(childTypeIndex, child, key);
		}

	}

	/**
	 * populate the lookupMap
	 * 
	 */
	private void createLooupMap(HashMap<String, HashMap<String, Integer>> lmap,
			ObjectDefinition context, String prefix) {

		lmap.put(prefix, createFieldMap(context));

		for (int i = 0; i < context.getChildren().size(); i++) {

			ObjectDefinition child = context.getChildren().get(i);
			String cname = child.getName();
			String key = prefix + "." + cname;
			createLooupMap(lmap, child, key);
		}

	}

	/**
	 * create the field map for a given ObjectDefinition
	 * 
	 * @param context
	 * @return
	 */
	private HashMap<String, Integer> createFieldMap(ObjectDefinition context) {

		HashMap<String, Integer> map = new HashMap<String, Integer>();

		ArrayList<Field> fields = context.getFields();

		for (int i = 0; i < fields.size(); i++) {
			map.put(fields.get(i).getName(), i);
		}
		return map;
	}

	
	/**
	 * utility method to create Lookup from objectDefinition InputStream
	 * 
	 * @param objectDefinition
	 * @return
	 */
	public static Lookup createLookup(InputStream objectDefinition) {

		Lookup l = new Lookup(objectDefinition);
		return l;

	}

	/**
	 * utility method to create Lookup from objectDefinition Object
	 * 
	 * @param objectDefinition
	 * @return
	 */
	public static Lookup createLookup(ObjectDefinition objectDefinition) {
		Lookup l = new Lookup(objectDefinition);
		return l;

	}

	/**
	 * returns the field index for a given fieldName and prefix where given an
	 * ePath Person.Address.city , fieldName=city and prefix=Person.Address,
	 * will return -1 if the field does not exist in the object definition
	 * 
	 * @param fieldName
	 * @param prefix
	 * @return
	 */
	public int getFieldIndex(String fieldName, String prefix) {

		if(lookupMap.get(prefix) == null)
			return -1;
		
		Integer i = lookupMap.get(prefix).get(fieldName);

		if (i == null)
			return -1;
		else
			return i.intValue();

	}

	/**
	 * returns the child index for a given prefix can be Person.Address
	 * 
	 * @param prefix
	 * @return
	 */
	public int getChildTypeIndex(String prefix) {
		Integer i = childIndex.get(prefix);

		if (i == null)
			return -1;
		else
			return i.intValue();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			FileInputStream fis = new FileInputStream(
					"conf/object-definition.xml");

			Lookup l = createLookup(fis);

			logger.info(l.childIndex.toString());

			logger.info(l.lookupMap.toString());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public HashMap<String, HashMap<String, Integer>> getLookupMap() {
		return lookupMap;
	}

	public HashMap<String, Integer> getChildIndex() {
		return childIndex;
	}

}
