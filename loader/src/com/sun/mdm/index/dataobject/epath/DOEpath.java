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

package com.sun.mdm.index.dataobject.epath;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.mdm.index.Resource;
import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.objectdef.Lookup;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.epath.EPathParser;

/**
 * 
 * This is equivalent to EPathAPI for ObjectNode. The utility can be used to
 * access DataObject using ePath.
 * 
 * 
 * The utility methods throws usually throws EPathException exception in case of
 * a error condition with respect ePath, however there are one special cases of
 * EPathException that is .
 * 
 * <p>
 * 
 * 1. InvalidFieldException
 * <p>
 * This exceptions are sub-class of EPathException
 * 
 * 
 * 
 * 
 * <p>
 * 
 * InvalidFieldException is thrown if the ePath specified a fieldName which does
 * not exist according to ObjectDefinition
 * 
 * 
 * <p>
 * Note if Child is Missing for a given ePath, null is returned, example if the
 * ePath tries to access a child instance using an index which does not exist.
 * Example there are 2 Person.Address instance then it is legitimate to access
 * Person.Address[0].city and Person.Address[1].city, however trying to access
 * Person.Address[2].city will return null. If the method is a set method which
 * does not return any value then a log warning message will be spit
 * 
 * 
 * 
 * 
 * 
 * 
 * @author Sujit Biswas
 * 
 */
public class DOEpath {

	private static Logger logger = Logger.getLogger(DOEpath.class.getName(),
			Resource.BUNDLE_NAME);

	/**
	 * example get field Value using Person.Address.city or
	 * Person.Address[0].city or Person.Address[1].city. if the field value is
	 * not set it will returns null.
	 * 
	 * <p>
	 * Will also return null if the ePath tries to access a child instance using
	 * an index which does not exist. Example there are 2 Person.Address
	 * instance then it is legitimate to access Person.Address[0].city and
	 * Person.Address[1].city, however trying to access Person.Address[2].city
	 * will return null
	 * 
	 * 
	 * @param e
	 *            ePath to use
	 * @param context
	 *            dataObject with respect to which the field value will be
	 *            accessed
	 * @param l
	 *            lookup object based on the ObjectDefinition
	 * @return the field Value
	 * @throws EPathException
	 * 
	 */
	public static String getFieldValue(EPath e, DataObject context, Lookup l)
			throws EPathException {

		DataObject current = context;

		for (int i = 0; i < e.getOps().length; i++) {
			if ((current == null)) {
				return null;
			}

			// end add
			switch (e.getOps()[i]) {
			case EPath.OP_NOOP:
				break;

			case EPath.OP_SECONDARY_BY_KEY:

				break;

			case EPath.OP_ALL_FIELD:
				break;

			case EPath.OP_FIELD:
				int j = getFieldIndex(e, l, i);

				if (current.hasField(j))
					return current.getFieldValue(j);
				else
					return null;

			case EPath.OP_ALL_SECONDARY: {

				logger.info(Resource.getProperty("not_supported") + e);
				throw new EPathException(Resource.getProperty("not_supported")
						+ e);

			}
			case EPath.OP_SECONDARY_BY_INDEX:

				int k = getChildTypeIndex(e, l, i);
				current = getChild(e, current, i, k);

				break;

			default:
				throw new EPathException(Resource
						.getProperty("unrecognized_op")
						+ e.getOps()[i]);
			}
		}

		return null;
	}

	/**
	 * example set field Value using Person.Address.city or
	 * Person.Address[0].city or Person.Address[1].city.
	 * 
	 * 
	 * <p>
	 * Will return without setting if the ePath tries to access a child instance
	 * using an index which does not exist. Example there are 2 Person.Address
	 * instance then it is legitimate to access Person.Address[0].city and
	 * Person.Address[1].city, however trying to access Person.Address[2].city
	 * will return without setting
	 * 
	 * @param e
	 *            ePath to use
	 * @param context
	 *            dataObject with respect to which the field value will be
	 *            accessed
	 * @param value
	 *            the field value to set
	 * @param l
	 *            lookup object based on the ObjectDefinition
	 * 
	 * 
	 * @throws EPathException
	 */
	public static void setFieldValue(EPath e, DataObject context, String value,
			Lookup l) throws EPathException {

		DataObject current = context;

		for (int i = 0; i < e.getOps().length; i++) {
			if ((current == null)) {
				return;
			}

			// end add
			switch (e.getOps()[i]) {
			case EPath.OP_NOOP:
				break;

			case EPath.OP_SECONDARY_BY_KEY:

				break;

			case EPath.OP_ALL_FIELD:
				break;

			case EPath.OP_FIELD:
				int j = getFieldIndex(e, l, i);
				current.setFieldValue(j, value);
				return;
			case EPath.OP_ALL_SECONDARY: {

				logger.info(Resource.getProperty("not_supported") + e);
				throw new EPathException(Resource.getProperty("not_supported")
						+ e);

			}
			case EPath.OP_SECONDARY_BY_INDEX:

				int k = getChildTypeIndex(e, l, i);

				current = getChild(e, current, i, k);

				break;

			default:
				throw new EPathException(Resource
						.getProperty("unrecognized_op")
						+ e.getOps()[i]);
			}
		}

	}

	/**
	 * example get the list of field Values using Person.Address[*].city. If no
	 * such child instance exist, the method will return null
	 * 
	 * 
	 * This also work with a ePath expression like Person.Address[0].city,
	 * though the ePath in this case points to a single field value, but if the
	 * user wants to get the single field value as list. The more appropriate
	 * method to get the field value for a single field is <b>getFieldValue()</b>
	 * 
	 * @param e
	 *            ePath to use
	 * @param context
	 *            dataObject with respect to which the field value will be
	 *            accessed
	 * @param l
	 *            the lookup object based on the ObjectDefinition
	 * @return the list field values
	 * @throws EPathException
	 */
	public static List<String> getFieldValueList(EPath e, DataObject context,
			Lookup l) throws EPathException {

		if (!hasChildren(e)) {
			return getFieldValueAsList(e, context, l);
		}

		DataObject current = context;
		List<DataObject> children = null;
		ArrayList<String> list = new ArrayList<String>();

		for (int i = 0; i < e.getOps().length; i++) {
			if ((current == null)) {
				return null;
			}

			// end add
			switch (e.getOps()[i]) {
			case EPath.OP_NOOP:
				break;

			case EPath.OP_SECONDARY_BY_KEY:

				break;

			case EPath.OP_ALL_FIELD:

				break;

			case EPath.OP_FIELD:
				int j = getFieldIndex(e, l, i);

				for (DataObject d : children) {

					if (d.hasField(j))
						list.add(d.getFieldValue(j));
					else
						list.add(null);
					;

				}

				return list;

			case EPath.OP_ALL_SECONDARY: {

				int k = getChildTypeIndex(e, l, i);

				children = getChildren(current, k);

				if (children == null) {
					return null;
				}

				break;

			}
			case EPath.OP_SECONDARY_BY_INDEX:
				int k = getChildTypeIndex(e, l, i);

				current = getChild(e, current, i, k);

				break;

			default:
				throw new EPathException(Resource
						.getProperty("unrecognized_op")
						+ e.getOps()[i]);
			}
		}

		return null;
	}

	/**
	 * @param current
	 * @param k
	 * @return
	 * 
	 */
	private static List<DataObject> getChildren(DataObject current, int k) {
		List<DataObject> children = null;
		try {
			children = current.getChildren(k);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
		return children;
	}

	/**
	 * example get the DataObject using Person.Address or Person.Address[0] or
	 * Person.Address[1] .
	 * 
	 * 
	 * <p>
	 * Will return null if the ePath tries to access a child instance using an
	 * index which does not exist. Example there are 2 Person.Address instance
	 * then it is legitimate to access Person.Address[0] and Person.Address[1],
	 * however trying to access Person.Address[2] will return null
	 * 
	 * @param e
	 *            ePath to use
	 * @param context
	 *            dataObject with respect to which the child DataObject will be
	 *            retrieved
	 * @param l
	 *            the lookup object based on the ObjectDefinition
	 * @return the DataObject pointed by the ePath expression
	 * @throws EPathException
	 * @throws {@link}
	 * 
	 */
	public static DataObject getDataObject(EPath e, DataObject context, Lookup l)
			throws EPathException {

		// TODO ePath does not tokeniZe expression like Person.Address[0] in a
		// meaningful way
		EPath e_ = e;

		String s = e.toString() + ".dummy";
		e = EPathParser.parse(s);

		DataObject current = context;

		for (int i = 0; i < e.getOps().length; i++) {
			if ((current == null)) {
				return null;
			}

			// end add
			switch (e.getOps()[i]) {
			case EPath.OP_NOOP:
				break;

			case EPath.OP_SECONDARY_BY_KEY:

				break;

			case EPath.OP_ALL_FIELD:
				break;

			case EPath.OP_FIELD:
				logger.info(Resource.getProperty("not_supported") + e_);
				throw new EPathException(Resource.getProperty("not_supported")
						+ e_);

			case EPath.OP_ALL_SECONDARY: {

				logger.info(Resource.getProperty("not_supported") + e_);
				throw new EPathException(Resource.getProperty("not_supported")
						+ e_);

			}
			case EPath.OP_SECONDARY_BY_INDEX:

				int k = getChildTypeIndex(e, l, i);

				current = getChild(e, current, i, k);

				if (e.getOps().length == i + 2)
					return current;
				else
					break;

			default:
				throw new EPathException(Resource
						.getProperty("unrecognized_op")
						+ e_);
			}
		}

		return null;
	}

	/**
	 * example set the DataObject using Person.Address or Person.Address[0] or
	 * Person.Address[1] . This <b>always</b> replaces an existing data Object. To add
	 * a new DataObject use <b>addDataObject()</b>
	 * <p>
	 * Will log warning message if the ePath tries to access a child instance
	 * using an index which does not exist. Example there are 2 Person.Address
	 * instance then it is legitimate to access Person.Address[0] and
	 * Person.Address[1], however trying to access Person.Address[2] will log
	 * warning message
	 * 
	 * 
	 * 
	 * @param e
	 *            ePath to use
	 * @param context
	 *            dataObject with respect to which the child DataObject will be
	 *            retrieved
	 * @param value
	 *            the data object to set
	 * @param l
	 *            the lookup object based on the ObjectDefinition
	 * 
	 * @throws EPathException
	 */
	public static void setDataObject(EPath e, DataObject context,
			DataObject value, Lookup l) throws EPathException {

		EPath e_ = e;

		// TODO ePath does not tokeniZe expression like Person.Address[0] in a
		// meaningful way
		String s = e.toString() + ".dummy";
		e = EPathParser.parse(s);

		DataObject current = context;

		for (int i = 0; i < e.getOps().length; i++) {
			if ((current == null)) {
				return;
			}

			// end add
			switch (e.getOps()[i]) {
			case EPath.OP_NOOP:
				break;

			case EPath.OP_SECONDARY_BY_KEY:

				break;

			case EPath.OP_ALL_FIELD:
				break;

			case EPath.OP_FIELD:
				logger.info(Resource.getProperty("not_supported") + e_);
				throw new EPathException(Resource.getProperty("not_supported")
						+ e_);

			case EPath.OP_ALL_SECONDARY: {

				logger.info(Resource.getProperty("not_supported") + e_);
				throw new EPathException(Resource.getProperty("not_supported")
						+ e_);

			}
			case EPath.OP_SECONDARY_BY_INDEX:

				int k = getChildTypeIndex(e, l, i);

				if (e.getOps().length == i + 2) {
					setChild(e, value, current, i, k);
					return;
				} else {
					current = getChild(e, current, i, k);
					break;
				}

			default:
				throw new EPathException(Resource.getProperty("invalid_epath")
						+ e_);
			}
		}

		return;
	}

	/**
	 * @param e
	 * @param child
	 * @param current
	 * @param i
	 * @param k
	 * 
	 */
	private static void setChild(EPath e, DataObject child, DataObject current,
			int i, int k) {
		try {
			current.setChild(k, child, e.getIndices()[i]);
		} catch (RuntimeException e1) {
			logger.log(Level.WARNING, "child_missing_set");
		}
	}

	/**
	 * example add the DataObject using Person.Address, The method adds the data
	 * object to the existing list of children for a given childType
	 * 
	 * 
	 * 
	 * @param e
	 *            ePath to use
	 * @param context
	 *            dataObject with respect to which the child DataObject will be
	 *            retrieved
	 * @param value
	 *            the data object to add
	 * @param l
	 *            the lookup object based on the ObjectDefinition
	 * 
	 * @throws EPathException
	 */
	public static void addDataObject(EPath e, DataObject context,
			DataObject value, Lookup l) throws EPathException {

		EPath e_ = e;
		// TODO ePath does not tokeniZe expression like Person.Address[0] in a
		// meaningful way
		String s = e.toString() + ".dummy";
		e = EPathParser.parse(s);

		DataObject current = context;

		for (int i = 0; i < e.getOps().length; i++) {
			if ((current == null)) {
				return;
			}

			// end add
			switch (e.getOps()[i]) {
			case EPath.OP_NOOP:
				break;

			case EPath.OP_SECONDARY_BY_KEY:

				break;

			case EPath.OP_ALL_FIELD:
				break;

			case EPath.OP_FIELD:
				logger.info(Resource.getProperty("not_supported") + e_);
				throw new EPathException(Resource.getProperty("not_supported")
						+ e_);

			case EPath.OP_ALL_SECONDARY: {

				logger.info(Resource.getProperty("not_supported") + e_);
				throw new EPathException(Resource.getProperty("not_supported")
						+ e_);

			}
			case EPath.OP_SECONDARY_BY_INDEX:

				int k = getChildTypeIndex(e, l, i);

				if (e.getOps().length == i + 2) {
					current.addChild(k, value);
					return;
				} else {
					current = getChild(e, current, i, k);
					break;
				}

			default:
				throw new EPathException(Resource.getProperty("invalid_epath")
						+ e_);
			}
		}

		return;
	}

	/**
	 * example get the list of DataObject using Person.Address or
	 * Person.Address[*], The method will return all the address instance for a
	 * given Person Object
	 * 
	 * 
	 * 
	 * 
	 * @param e
	 *            ePath to use
	 * @param context
	 *            dataObject with respect to which the child DataObject will be
	 *            retrieved
	 * 
	 * @param l
	 *            the lookup object based on the ObjectDefinition
	 * 
	 * @return the list of data object
	 * 
	 * @throws EPathException
	 */
	public static List<DataObject> getDataObjectList(EPath e,
			DataObject context, Lookup l) throws EPathException {

		EPath e_ = e;

		// TODO ePath does not tokeniZe expression like Person.Address[0] in a
		// meaningful way
		String s = e.toString() + ".dummy";
		e = EPathParser.parse(s);

		DataObject current = context;

		for (int i = 0; i < e.getOps().length; i++) {
			if ((current == null)) {
				return null;
			}

			// end add
			switch (e.getOps()[i]) {
			case EPath.OP_NOOP:
				break;

			case EPath.OP_SECONDARY_BY_KEY:

				break;

			case EPath.OP_ALL_FIELD:
				break;

			case EPath.OP_FIELD:
				logger.info(Resource.getProperty("not_supported") + e_);
				throw new EPathException(Resource.getProperty("not_supported")
						+ e_);

			case EPath.OP_ALL_SECONDARY: {

				int k = getChildTypeIndex(e, l, i);

				if (e.getOps().length == i + 2) {
					try {
						return getChildren(current, k);
					} catch (Exception ex) {
						return new ArrayList<DataObject>();
					}
				} else {
					current = getChild(e, current, i, k);

					break;
				}

			}
			case EPath.OP_SECONDARY_BY_INDEX:

				int k = getChildTypeIndex(e, l, i);

				if (e.getOps().length == i + 2) {
					return getChildren(current, k);
				} else {
					current = getChild(e, current, i, k);

					break;
				}

			default:
				throw new EPathException(Resource.getProperty("invalid_epath")
						+ e_);
			}
		}

		return null;
	}

	/**
	 * @param e
	 * @param i
	 * 
	 * @return
	 */
	private static String constructPrefix(EPath e, int i) {

		String prefix = "";
		for (int j = 0; j <= i; j++) {
			if (j == i)
				prefix = prefix + e.getTokenQueue()[j];
			else
				prefix = prefix + e.getTokenQueue()[j] + ".";
		}
		return prefix;
	}

	private static boolean hasChildren(EPath e) {
		for (int i = 0; i < e.getOps().length; i++) {
			if (e.getOps()[1] == EPath.OP_ALL_SECONDARY)
				return true;
		}
		return false;
	}

	/**
	 * return a single field value as list
	 * 
	 * @param e
	 * @param context
	 * @param l
	 * @return
	 * @throws EPathException
	 * 
	 */
	private static List<String> getFieldValueAsList(EPath e,
			DataObject context, Lookup l) throws EPathException {

		ArrayList<String> arr = new ArrayList<String>();

		String s = getFieldValue(e, context, l);

		arr.add(s);

		return arr;

	}

	/**
	 * @param e
	 * @param current
	 * @param epathIndex
	 * @param childTypeIndex
	 * @return DataObject
	 * 
	 */
	private static DataObject getChild(EPath e, DataObject current,
			int epathIndex, int childTypeIndex) {
		try {
			current = current.getChild(childTypeIndex,
					e.getIndices()[epathIndex]);
		} catch (IndexOutOfBoundsException ex) {
			return null;

		} catch (NullPointerException ex1) {
			return null;
		}
		return current;
	}

	/**
	 * @param e
	 * @param l
	 * @param i
	 * @return
	 * @throws EPathException
	 */
	private static int getChildTypeIndex(EPath e, Lookup l, int i)
			throws EPathException {
		String prefix = constructPrefix(e, i);
		int k = l.getChildTypeIndex(prefix);

		if (k == -1) {
			throw new EPathException(Resource
					.getProperty("invalid_epath_childtype"));
		}
		return k;
	}

	/**
	 * @param e
	 * @param l
	 * @param i
	 * @return
	 * @throws EPathException
	 */
	private static int getFieldIndex(EPath e, Lookup l, int i)
			throws InvalidFieldException {
		String prefix = constructPrefix(e, i - 1);
		int j = l.getFieldIndex(e.getTokenQueue()[i], prefix);

		if (j == -1) {
			throw new InvalidFieldException(Resource
					.getProperty("invalid_field")
					+ e);

		}
		return j;
	}

}
