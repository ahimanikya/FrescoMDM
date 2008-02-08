/* *************************************************************************
 *
 *          Copyright (c) 2002, SeeBeyond Technology Corporation,
 *          All Rights Reserved
 *
 *          This program, and all the routines referenced herein,
 *          are the proprietary properties and trade secrets of
 *          SEEBEYOND TECHNOLOGY CORPORATION.
 *
 *          Except as provided for by license agreement, this
 *          program shall not be duplicated, used, or disclosed
 *          without  written consent signed by an officer of
 *          SEEBEYOND TECHNOLOGY CORPORATION.
 *
 ***************************************************************************/
package com.sun.mdm.index.edm.util;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SBROverWrite;
import com.sun.mdm.index.objects.epath.EPathBuilder;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.control.UserProfile;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.rmi.RemoteException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * Utility class contains commonly used functions across QWS.
 * @author Wei Wu
 */
public final class QwsUtil {
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.util.QwsUtil");
    private transient static final Localizer mLocalizer = Localizer.get();
    private static ResourceBundle qwsResources = null;
    private static final String EPATH_PREFIX = "Enterprise.SystemSBR.";
    private static final int EPATH_PREFIX_LENGTH = 21;
    
    /**
     * Get resouce for the indicated key.
     *
     * @param key the key Sting used to identify a resource entry.
     * @return string resource value of the intended key.
     */
    public static String getResourceForKey(String key) {
        return getQwsResources().getString(key);
    }

    /**
     * Get value for the specified field name. Note: 10/15/2002, <code>EPath</code>
     * not working. Add a work-arodun here.
     *
     * @param majorNode an <code>ObjectNode</code> that represents the top-level
     *      object. E.g., "Person".
     * @param fieldPath the field of interest. E.g., for major object, it's in
     *      the format of "Person.SSN". For minor object, it's in the format of
     *      "Person.Address.City".
     * @return value for the specified field.
     */
    public static Collection getValueForField(ObjectNode majorNode,
        String fieldPath, UserProfile user) throws ObjectException {
        List valueList = new ArrayList();

        String objRef = getLastObjRefFromFieldPath(fieldPath);
        
        boolean isObjSensitive = isObjectNodeSensitive(majorNode);
        
        if (objRef == null) {
            // dealing with a major object.
            String fieldName = getFieldNameFromFieldPath(fieldPath);
            String val = getValueFromNode(majorNode, fieldName, user, isObjSensitive);
            valueList.add(val);
        } else {
            // dealing with minor object fields.
            ArrayList childNodes = majorNode.pGetChildren();
            String fieldName = getFieldNameFromFieldPath(fieldPath);
            
            if (childNodes != null) {
                for (Iterator i = childNodes.iterator(); i.hasNext();) {
                    String val = getValueFromNode((ObjectNode) i.next(),
                                            fieldName, user,  isObjSensitive);
                    // Ignore null values but keep empty strings
                    if (val != null) {
                        valueList.add(val);
                    }
                }
            } else {
                return null; // no children found
            }
        }

        return valueList;
    }

    /**
     * Convert a Map values to a List.
     *
     * @param map the Map to be converted.
     * @return the List that contains the Map's values.
     */
    public static List convertMapValueToList(Map map) {
        List list = new ArrayList(map.size());

        for (Iterator i = map.values().iterator(); i.hasNext();) {
            list.add(i.next());
        }

        Collections.sort(list);

        return list;
    }

    /**
     * Conver the space within a String to underscore.
     *
     * @param inStr any String.
     * @return the String that does not contain any spaces.
     * @todo Document this method
     */
    public static String escapeSpaceInString(String inStr) {
        return inStr.replace(' ', '_');
    }

    /**
     * Convert a ordinary List to a List of List, each entry in the returned
     * List holds <code>count</code> objects in the original list.
     *
     * @param orgList the input List.
     * @param count the number of objects in each element of the returned List.
     * @return the List where each element contains <code>count</code> objects.
     */
    public static List formatList(List orgList, int count) {
        List newList = new ArrayList();

        for (Iterator iter = orgList.iterator(); iter.hasNext();) {
            List rowList = new ArrayList(count);

            for (int i = 0; (i < count) && iter.hasNext(); i++) {
                rowList.add(iter.next());
            }

            newList.add(rowList);
        }

        return newList;
    }

    /**
     * Format the paramater map from HttpRequest.
     *
     * @param parameterMap the map obtained via <code>request.getParameterMap()</code>
     *      where <code>request</code> is of type <code>HttpServletRequest</code>
     *      .
     * @return a map with string key/value.
     */
    public static Map formatRequestParameterMap(Map parameterMap) {
        Map resultMap = new HashMap();

        for (Iterator iter = parameterMap.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            String[] values = (String[]) parameterMap.get(key);

            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    resultMap.put(key, values[i]);
                }
            }
        }

        return resultMap;
    }

    /** 
     * Retrieves the field name from a field path.  For example, if 
     * "Person.Address.City" was passed in as the fieldPath parameter, this
     * would return "City".
     * @param fieldPath String representing the field path.
     * @return The field name from a field path.
     */
    public static String getFieldNameFromFieldPath(String fieldPath) {
        String fieldName = null;

        int index = fieldPath.lastIndexOf(ConfigManager.FIELD_DELIM);

        if (index > -1) {
            fieldName = fieldPath.substring(index + 1);
        } else {
            fieldName = fieldPath;
        }

        return fieldName;
    }

    public static String getObjRefFromFieldPath(String fieldPath) {
        String objRef = null;

        int index = fieldPath.indexOf(ConfigManager.FIELD_DELIM);

        if (index > -1) {
            objRef = fieldPath.substring(0, index);
        }

        return objRef;
    }

    /**
     *  Retrieves the last object reference from a field path.  This is the
     * object to which a field belongs.  For example, if "Person.Address.City"
     * is passed in as the fieldPath parameter, this would return "Address".
     * However, for the major object (e.g. Person), if "Person.SSN" is passed
     * in as the fieldPath parameter, this would return null.  For this, the
     * EPATH_PREFIX is ignored.
     * 
     * @param fieldPath  String representing the field path.
     * @return the last object reference from a field path.
     */
    public static String getLastObjRefFromFieldPath(String fieldPath) {
        
        // Ignore the EPATH_PREFIX
        int index = fieldPath.indexOf(EPATH_PREFIX);

        String partialFieldPath = null;
        if (index > -1) {
            partialFieldPath = fieldPath.substring(EPATH_PREFIX_LENGTH);
        } else {
            partialFieldPath = fieldPath;
        }
        
        String[] tokens = partialFieldPath.split("[" + ConfigManager.FIELD_DELIM + "]");
        int len = tokens.length;
        if (len > 2) {
            return tokens[len - 2];
        } else if (len == 2) {
            // This is for a top level object.
            return null;
        } else {
            // RESUME HERE
            // throw an exception because it is improperly formatted
            return null;
        }
    }

    /**
     * Get a handle to "QWS.properties".
     */
    private static ResourceBundle getQwsResources() {
        try {
            if (qwsResources == null) {
                qwsResources = ResourceBundle.getBundle("QWS");
            }
        } catch (MissingResourceException e) {
            mLogger.severe(mLocalizer.x("SRU001: Error occurred while getting " + 
                                        "resource bundle: {0}", e.getMessage()));
        }

        return qwsResources;
    }

    /**
     * Get value for the specified field.
     *
     * @param fieldName the field name.
     * @param targetNode
     * @return value of the field.
     */
    private static String getValueFromNode(ObjectNode targetNode,
        String fieldName, UserProfile user, boolean isObjSensitive) throws ObjectException {
        Object value = null;
        ObjectNodeConfig objc = ConfigManager.getInstance().getObjectNodeConfig(targetNode.pGetTag());
        // ignore the EUID tag, which is only a placeholder
        if (objc == null) {
            return null;
        }
        FieldConfig fconfig = objc.getFieldConfig(fieldName);
        boolean isFieldSensitive = fconfig.isSensitive();

        List fieldNames = targetNode.pGetFieldNames();
        List fieldValues = targetNode.pGetFieldValues();
        int size = fieldNames.size();

        for (int i = 0; i < size; i++) {
            String name = (String) fieldNames.get(i);
                
            if (name.equalsIgnoreCase(fieldName)) {
                value = fieldValues.get(i);
                break;
            }
        }

// TODO: This should be handled by the Presentation Layer
//	if (isObjSensitive && isFieldSensitive && !user.isAllowed(UserProfile.PERM_EVIEW_VIP)) {
//	    return "XXXXXXXX";
//	}

        if (value == null) {
            return "";
        } else {
            // add date checking by Jeff
            if (value instanceof java.util.Date) {
                return (DateUtil.dateOnly2String((Date) value));
            } else {
                return value.toString();
            }
        }
    }

    /**
     * Set value for the named field in the specified object.
     *
     * @param node the object node
     * @param field the field name in the object node.
     * @param valueString the string value to set to
     * @exception ObjectException when object operation fails
     * @exception ValidationException when validation fails
     */
    public static void setObjectNodeFieldValue(ObjectNode node, String field, 
        String valueString) throws ObjectException, ValidationException {
      setObjectNodeFieldValue(node, field, valueString, null);
    }

    /**
     * Set value for the named field in the specified object.
     *
     * @param node the object node
     * @param field the field name in the object node.
     * @param valueString the string value to set to
     * @param sbr the sbr
     * @exception ObjectException when object operation fails
     * @exception ValidationException when validation fails
     */
    public static void setObjectNodeFieldValue(ObjectNode node, String field, 
        String valueString, SBR sbr)
            throws ObjectException, ValidationException {

        boolean isSBR = (sbr != null);
        SBROverWrite ovr = null;
        if (sbr != null) {  // if (isSBR)
          ovr = getOverWrite(sbr, node, field);
          if (ovr == null) { // not found
            ovr = new SBROverWrite();
            String ep = EPathBuilder.createEPath(node, field, (ObjectNode) sbr);
            ovr.setPath(ep);
            sbr.addOverWrite(ovr);
          }
        }

        if (valueString == null) {
          if (node.isNullable(field)) {
            if (isSBR) {
              if (ovr != null) {
                ovr.setData(null);
              }
            } else {
              // Modified by Jeff for bug 5975
              //node.setNull(field, true);
              node.setValue(field, null);
            }
            return;
          } else {
                ObjectNodeConfig config = ConfigManager.getInstance()
                                               .getObjectNodeConfig(node.pGetType());
                String fieldDisplayName = config.getFieldConfig(field).getDisplayName(); 
                throw new ValidationException(mLocalizer.t("SRU500: Field [{0}] is required", fieldDisplayName));
          }
        }

        int type = node.pGetType(field);
        try {
                switch (type) {
                case ObjectField.OBJECTMETA_DATE_TYPE:
                case ObjectField.OBJECTMETA_TIMESTAMP_TYPE:
                    if (isSBR) {
                      ovr.setData((Object) DateUtil.string2Date(valueString));
                    } else {
                      node.setValue(field, (Object) DateUtil.string2Date(valueString));
                    }
                    break;

                case ObjectField.OBJECTMETA_INT_TYPE:
                    if (isSBR) {
                      if (ovr != null) {
                        ovr.setData((Object) Integer.valueOf(valueString));
                      }
                    } else {
                      node.setValue(field, (Object) Integer.valueOf(valueString));
                    }
                    break;

                case ObjectField.OBJECTMETA_BOOL_TYPE:
                    if (isSBR) {
                      ovr.setData((Object) Boolean.valueOf(valueString));
                    } else {
                      node.setValue(field, (Object) Boolean.valueOf(valueString));
                    }
                    break;

                case ObjectField.OBJECTMETA_BYTE_TYPE:
                    if (isSBR) {
                        ovr.setData((Object) Byte.valueOf(valueString));
                      } else {
                        node.setValue(field, (Object) Byte.valueOf(valueString));
                      }
                    break;

                case ObjectField.OBJECTMETA_CHAR_TYPE:
                    if (isSBR) {
                      ovr.setData((Object) new Character(valueString.charAt(0)));
                    } else {
                      node.setValue(field, (Object) new Character(valueString.charAt(0)));
                    }
                    break;

                case ObjectField.OBJECTMETA_LONG_TYPE:
                    if (isSBR) {
                      if (ovr != null) {
                        ovr.setData((Object) Long.valueOf(valueString));
                      }
                    } else {
                      node.setValue(field, (Object) Long.valueOf(valueString));
                    }
                    break;

                case ObjectField.OBJECTMETA_FLOAT_TYPE:
                    if (isSBR) {
                      ovr.setData((Object) Float.valueOf(valueString));
                    } else {
                      node.setValue(field, (Object) Float.valueOf(valueString));
                    }
                    break;

                case ObjectField.OBJECTMETA_STRING_TYPE:
                default:
                    if (isSBR) {
                      ovr.setData((Object) valueString);
                    } else {
                      node.setValue(field, (Object) valueString);
                    }
                    break;
                }
        } catch (NumberFormatException e) {
            ObjectNodeConfig config = ConfigManager.getInstance()
                                               .getObjectNodeConfig(node.pGetType());
            String fieldDisplayName = config.getFieldConfig(field).getDisplayName(); 
            throw new ValidationException(mLocalizer.t("SRU501: Invalid value [{0}] for field [(1)]: {2}", 
                                                       valueString, fieldDisplayName, e.getMessage()));
        }
    }


    /**
     * Get the SBROverWrite object
     * @param sbr the SBR
     * @param node the node
     * @param fname the field name in the sbr
     * @return SBROverWrite the SBROverWrite object found or null
     * @throws ObjectException when object operation fails
     */
    public static SBROverWrite getOverWrite(SBR sbr, ObjectNode node, String fname) 
      throws ObjectException {
      ArrayList ovrs = sbr.getOverWrites();
      String epath = EPathBuilder.createEPath(node, fname, (ObjectNode) sbr);
      if (ovrs == null) {
        return null;
      }
      for (Iterator iter = ovrs.iterator(); iter.hasNext();) {
        SBROverWrite ovr = (SBROverWrite) iter.next();
        String ovrpath = ovr.getEPath();
        if (ovrpath.equals(epath)) {
          return ovr;
        }
      }
      return null;
    }

    /**
     * Convert a string value to an object: e.g., int -> Integer, date -> Date
     * @param valueString the value string
     * @param type the type of the field
     * @return an object
     * @throws ObjectException when invalid field value is encountered
     */
    public static Object getObjectValue(String valueString, int type)
            throws ObjectException {
        try {
                switch (type) {
                case ObjectField.OBJECTMETA_DATE_TYPE:
                case ObjectField.OBJECTMETA_TIMESTAMP_TYPE:
                      return (Object) DateUtil.string2Date(valueString);
                case ObjectField.OBJECTMETA_INT_TYPE:
                      return (Object) Integer.valueOf(valueString);
                case ObjectField.OBJECTMETA_BOOL_TYPE:
                      return (Object) Boolean.valueOf(valueString);
                case ObjectField.OBJECTMETA_BYTE_TYPE:
                      return (Object) Byte.valueOf(valueString);
                case ObjectField.OBJECTMETA_LONG_TYPE:
                      return (Object) Long.valueOf(valueString);
                case ObjectField.OBJECTMETA_FLOAT_TYPE:
                    return (Object) Float.valueOf(valueString);
                case ObjectField.OBJECTMETA_CHAR_TYPE:
                    return (Object) new Character(valueString.charAt(0));
                case ObjectField.OBJECTMETA_STRING_TYPE:
                default:
                      return (Object) valueString;
                }
        } catch (Exception e) {
            throw new ObjectException(mLocalizer.t("SRU502: Invalid value [{0}] for field type [(1)]: {2}", 
                                                   valueString, type, e.getMessage()));
        }
    }
    
    /**
     * Get the throwable of the root cause
     * @param exception e
     * @return throwable exception
     */
    public static Throwable getRootCause(Exception e) {
        Throwable t = e;
        if (e instanceof RemoteException) {
            Throwable t2 = ((RemoteException)e).getCause();
            if (t2 != null) {
                if (t2 instanceof InvocationTargetException) {
                    Throwable t3 = ((InvocationTargetException)t2).getTargetException();
                    if (t3 != null) {
                        t = t3;
                    }
                } else {
                    t = t2;
                }
            }
        } else if (e instanceof UndeclaredThrowableException) {
            Throwable t2 = ((UndeclaredThrowableException)e).getCause();
            if (t2 != null) {
                if (t2 instanceof InvocationTargetException) {
                    Throwable t3 = ((InvocationTargetException)t2).getTargetException();
                    if (t3 != null) {
                        t = t3;
                    }
                } else {
                    t = t2;
                }
            }
        } else if (e.getCause() != null) {
        	t = e.getCause();
        }
        return t;
    }


    public static boolean isObjectNodeSensitive(ObjectNode obj) throws ObjectException {
      if (ConfigManager.getInstance().getSecurityPlugIn() == null) {
          return false;
      }
      try {
          return ConfigManager.getInstance().getSecurityPlugIn().isDataSensitive(obj);
      } catch (Exception ex) {
            throw new ObjectException(mLocalizer.t("SRU503: Failed to detect sensitivity of object node: {0}",
                                                   ex.getMessage()));
      }
    }
}
