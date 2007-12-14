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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.epath.EPathParser;

/**
 * 
 * ObjectDefinitionBuilder : this class is mainly used to build an
 * ObjectDefinition from xml input stream and from ePaths.
 * 
 * 
 * @author Sujit Biswas
 * 
 */
public class ObjectDefinitionBuilder {

	private static final String TAG = "tag";
	private static final String NODES = "nodes";
	private static final String KEY_TYPE = "key-type";
	private static final String CODE_MODULE = "code-module";
	private static final String REQUIRED = "required";
	private static final String UPDATEABLE = "updateable";
	private static final String SIZE = "size";
	private static final String FIELD_TYPE = "field-type";
	private static final String FIELD_NAME = "field-name";
	private static final String FIELDS = "fields";
	private static final String CHILDREN = "children";
	private static final String NAME = "name";
	private static final String RELATIONSHIPS = "relationships";

	/**
	 * parse the object-definition.xml inputStream to build the ObjectDefinition
	 * object
	 * 
	 * @param objectDefinition
	 *            inputStream
	 * @return ObjectDefinition
	 */
	public ObjectDefinition parse(InputStream objectDefinition) {

		ObjectDefinition parent = null;
		try {
			Element root = getRootElement(objectDefinition);

			Element relation = (Element) root.getElementsByTagName(
					RELATIONSHIPS).item(0);

			Element name = (Element) relation.getElementsByTagName(NAME)
					.item(0);

			parent = new ObjectDefinition(name.getTextContent().trim());
			
			String dateFormat =  root.getElementsByTagName("dateformat")
			.item(0).getTextContent().trim();
			
			String database = root.getElementsByTagName("database")
			.item(0).getTextContent().trim();
			
			parent.setDateFormat(dateFormat);
			parent.setDatabase(database);
			

			updateFields(parent, root);

			NodeList nl = relation.getElementsByTagName(CHILDREN);

			for (int i = 0; i < nl.getLength(); i++) {

				Element e = (Element) nl.item(i);
				ObjectDefinition child = new ObjectDefinition(e
						.getTextContent().trim());
				parent.addchild(child);
				updateFields(child, root);
				
				child.setDateFormat(dateFormat);
				child.setDatabase(database);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return parent;

	}
	
	/**
	 * Bulds Object Defintion from given set of epaths.
	 * @param epaths
	 * @return
	 * @throws EPathException
	 */
	
	public static ObjectDefinition buildObjectDefinition(String[] epaths)
	 throws EPathException {
		ObjectDefinition root = null;
		ObjectDefinition cur = root;
		for (int i = 0; i < epaths.length; i++) {
			EPath e = EPathParser.parse(epaths[i]);
			
			
			String[] names = e.getTokenQueue();
			/*
			 iterate through object names
			 before last token which is a field or *
			 * 
			 */
			for (int j = 0; j < names.length-1; j++) {
			  String name = names[j];
			  
			  if (j == 0) {
				  if (root == null) {
				    root = new ObjectDefinition(name);
				  }
				  cur = root;
			  } else if (j != 0) {
				  ObjectDefinition child = findChild(cur, name);
				  if (child == null) {
					  child = new ObjectDefinition(name);
					  cur.addchild(child);
				  }
				  cur = child;				  				  				  
			  }
			  
			}
			String field = names[names.length-1]; // last name is field name
			if (!findField(cur, field)) {
				addField(cur, field);
			}												
		}
		
		return root;										
	}
	
	/*
	 * 
	 */
	private static ObjectDefinition findChild(ObjectDefinition od, String name) {
	  List<ObjectDefinition> children = od.getChildren();
	  for (int k = 0; k < children.size(); k++) {
		  ObjectDefinition cur = children.get(k);
		  if (cur.getName().equals(name)) {
			  return cur;
		  }
	  }
	  return null;
	}
	  
	private static boolean findField(ObjectDefinition node, String field) {
		List<Field> fields = node.getFields();
		for (int i = 0; i < fields.size(); i++) {
			Field f = fields.get(i);
			if (field.equals(f.getName()) ) {
				return true;
			}
		}
		return false;
	
	}
		
	private static void addField(ObjectDefinition node, String field) {
		Field f = new Field();
		f.setName(field);
		node.addField(f);
	}
	
	
	

	/**
	 * @param objectDefinition
	 *            inputStream
	 * @return the root element for a document object
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Element getRootElement(InputStream objectDefinition)
			throws ParserConfigurationException, SAXException, IOException {
		Document doc = getDocument(objectDefinition);
		Element root = doc.getDocumentElement();
		return root;
	}

	/**
	 * update the object definition fields
	 * 
	 * @param child
	 * @param root
	 */
	private void updateFields(ObjectDefinition child, Element root) {
		Element e = getNode(child.getName(), root);

		ArrayList<Field> fields = getFields(e);

		child.setFields(fields);

	}

	/**
	 * 
	 * @param e
	 * @return the list if fields for a given object node
	 */
	private ArrayList<Field> getFields(Element e) {

		ArrayList<Field> fields = new ArrayList<Field>();
		NodeList nl = e.getElementsByTagName(FIELDS);

		for (int i = 0; i < nl.getLength(); i++) {
			Element fieldNode = (Element) nl.item(i);

			Field f = createField(fieldNode);

			fields.add(f);
		}

		return fields;
	}

	/**
	 * creates a field object from the xml
	 * 
	 * @param fieldNode
	 * @return a ObjectDefnition field
	 */
	private Field createField(Element fieldNode) {

		Field f = new Field();

		Element field_name = (Element) fieldNode.getElementsByTagName(
				FIELD_NAME).item(0);

		if (field_name != null)
			f.setName(field_name.getTextContent().trim());

		Element field_type = (Element) fieldNode.getElementsByTagName(
				FIELD_TYPE).item(0);

		if (field_type != null)
			f.setType(field_type.getTextContent().trim());

		Element size = (Element) fieldNode.getElementsByTagName(SIZE).item(0);

		if (size != null)
			f.setSize(Integer.parseInt(size.getTextContent().trim()));

		Element updateable = (Element) fieldNode.getElementsByTagName(
				UPDATEABLE).item(0);

		if (updateable != null)
			f.setUpdateable(Boolean.parseBoolean(updateable.getTextContent()
					.trim()));

		Element required = (Element) fieldNode.getElementsByTagName(REQUIRED)
				.item(0);

		if (required != null)
			f.setRequired(Boolean
					.parseBoolean(required.getTextContent().trim()));

		Element code_module = (Element) fieldNode.getElementsByTagName(
				CODE_MODULE).item(0);

		if (code_module != null)
			f.setCodeModule(code_module.getTextContent().trim());

		Element key_type = (Element) fieldNode.getElementsByTagName(KEY_TYPE)
				.item(0);

		if (key_type != null)
			f
					.setKeyType(Boolean.parseBoolean(key_type.getTextContent()
							.trim()));

		return f;
	}

	/**
	 * 
	 * @param name
	 *            Object definition name
	 * @param root
	 * @return the corresponding object definition node
	 */

	private Element getNode(String name, Element root) {
		NodeList nl = root.getElementsByTagName(NODES);

		for (int i = 0; i < nl.getLength(); i++) {

			Element e = (Element) nl.item(i);

			if (matches(name, e)) {
				return e;
			}

		}

		return null;
	}

	private boolean matches(String name, Element e) {
		Element tag = (Element) e.getElementsByTagName(TAG).item(0);

		return tag.getTextContent().trim().equals(name);

	}

	/**
	 * 
	 * @param objectDef
	 *            xml
	 * @return Document object
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Document getDocument(InputStream objectDef)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		// docFactory.setValidating(true);

		DocumentBuilder builder = docFactory.newDocumentBuilder();

		return builder.parse(objectDef);
	}

}
