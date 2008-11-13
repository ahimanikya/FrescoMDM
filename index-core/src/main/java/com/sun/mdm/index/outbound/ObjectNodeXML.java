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
package com.sun.mdm.index.outbound;

import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SBROverWrite;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.metadata.ObjectFactory;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.util.Localizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import java.text.SimpleDateFormat;


/**
 * Formats ObjectNode to an xml string
 * @author  sdua
 */
public class ObjectNodeXML {

    private transient final Localizer mLocalizer = Localizer.get();
    
    private final String LINEFEED
             = new String(System.getProperty("line.separator"));
    private final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    
    private static final String TAG_APPLICATION = ObjectFactory.getApplicationName();
    
    private static String namespace = " xsi:schemaLocation=\"uri:" +
                                TAG_APPLICATION + "OutMsg outbound.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:tns=\"uri:" +
                                TAG_APPLICATION + "OutMsg\"";
                        

    private SimpleDateFormat mDateFormatter = null;
    
    /** Creates a new instance of ObjectNodeXML
     * @param objectNode ObjectNode that needs to be converted to xml string
     */
    public ObjectNodeXML() {
        String format = ObjectFactory.getDateFormat();
        mDateFormatter = new SimpleDateFormat(format + " HH:mm:ss");
    }

    /**
     * Constructs the xml for the objectNode that was passed to its constructor.
     * @param event event type
     * @param id the id
     * @param eo1 surviving enterprise object
     * @param eo2 merged enterprise object
     * @return  xml string
     */
    public String constructMrgXml(String event, String id, EnterpriseObject eo1, EnterpriseObject eo2) 
    throws OutBoundException {
        String name1 = eo1.pGetTag();
        String name2 = eo2.pGetTag();
        if (!name1.equalsIgnoreCase("ENTERPRISE") || !name2.equalsIgnoreCase("ENTERPRISE")) {
            return "";
        }
        String outMsg = "OutMsg";        
        StringBuffer sb = new StringBuffer();
        sb.append("<tns:").append(outMsg).append(" Event=\"").append(event);
        sb.append("\" ID=\"").append(id).append("\"").append(ObjectNodeXML.namespace).append(">").append(LINEFEED);
        sb.append(sbrToXml(eo1));
        sb.append(sbrToXml(eo2));
        sb.append(endTag(outMsg));
        return sb.toString();
    }

    
    /**
     * Constructs the xml for the objectNode that was passed to its constructor.
     * @param event event type
     * @param id the id
     * @param eo an enterprise object
     * @return  xml string
     */
    public String constructXml(String event, String id, EnterpriseObject eo) 
    throws OutBoundException {
        String name = eo.pGetTag();
        if (!name.equalsIgnoreCase("ENTERPRISE")) {
            return "";
        }
        String outMsg = "OutMsg";
        StringBuffer sb = new StringBuffer();
        sb.append(XML_HEADER).append(LINEFEED);
        sb.append("<tns:").append(outMsg).append(" Event=\"").append(event);
        sb.append("\" ID=\"").append(id).append("\"").append(ObjectNodeXML.namespace).append(">").append(LINEFEED);
        sb.append(sbrToXml(eo));
        sb.append(endTag(outMsg));
        return sb.toString();
    }

    /**
     * Constructs an xml string for an EnterpriseObject
     * @param eo an enterprise object
     * @return xml string
     */
    private String sbrToXml(EnterpriseObject eo) throws OutBoundException {
        StringBuffer sb = new StringBuffer();
        sb.append("<tns:SBR ");        
        try {
            sb.append("EUID=\"").append(eo.getEUID()).append("\" ");
        } catch (ObjectException e) {
            throw new OutBoundException(mLocalizer.t("OUT500: Could not construct " + 
                                    "an XML string for an EnterpriseObject: {0}", e));
        }
        SBR sbr = eo.getSBR();
        sb.append(formatSbrFields(sbr));
        Collection sysObjs = eo.getSystemObjects();
        if (sysObjs != null) {
            Iterator soIterator = sysObjs.iterator();
            while (soIterator.hasNext()) {
                SystemObject sysObj = (SystemObject) soIterator.next();
                sb.append("<tns:SystemObject ");
                try {
                    sb.append("SystemCode=\"").append(sysObj.getSystemCode()).append("\" ");
                    sb.append("LID=\"").append(sysObj.getLID()).append("\" ");
                    sb.append("Status=\"").append(sysObj.getStatus()).append("\">");
                    sb.append(LINEFEED);
                } catch (ObjectException e) {
                    throw new OutBoundException(mLocalizer.t("OUT501: Could not construct " + 
                                    "an XML string for an EnterpriseObject: {0}", e));
                }
                sb.append("</tns:SystemObject>").append(LINEFEED);
            }
        }
        
        String myPath = "Enterprise.SystemSBR";
        ArrayList childNodes = sbr.pGetChildren();
        for (int i = 0; (childNodes != null) && (i < childNodes.size()); i++) {
            ObjectNode child = (ObjectNode) childNodes.get(i);
            if (!child.pGetTag().equals("SBROverWrite")) {
                sb.append(nodeToXml(child, myPath));
            }
        }
        sb.append("</tns:SBR>").append(LINEFEED);
        return sb.toString();
    }
   
    /**
     * Formats the SBR object.
     * @param sbr an SBR object
     * @return xml string
     */
    private String formatSbrFields(SBR sbr) {
        String name = sbr.pGetTag();
        if (!name.equalsIgnoreCase("SYSTEMSBR")) {
            return "";
        }
        String myPath = "Enterprise." + name;
        Object[] fldNames = null;
        StringBuffer sb = new StringBuffer();
	ArrayList fns = sbr.getFieldNames();
	if (fns != null) {
	    fldNames = fns.toArray();
	}
        for (int i = 0; i < fldNames.length; i++) {
            Object value = null;
            String fieldName = (String) fldNames[i];
            if (fieldName.equals("SystemCode")) {
                continue;
            } else if (fieldName.equals("LocalID")) {
                continue;
            }
            try {
                value = sbr.getValue(fieldName);
            } catch (ObjectException e) {
            }
            sb.append(" ").append(fieldName).append("=\"");
            if (value != null) {
                sb.append(convert(value));
            }
            sb.append("\"");
        }
        sb.append(">").append(LINEFEED);
        return sb.toString();
    }

    /**
     * Formats object is object is of type java.util.Date
     * @param obj the object to format
     * @return a formatted Date object if of type java.util.Date
     */
    private Object convert(Object obj) {
        if (obj instanceof java.util.Date) {
            return mDateFormatter.format((java.util.Date) obj);
        } else if (obj instanceof java.lang.String) {
        	if (hasEscapedCharacter((java.lang.String) obj)) {
        		return encodeString((java.lang.String) obj);
        	}
        }
        return obj;
    }

    /**
     * Check if a string contains any escaped character
     *
     * @param str the string to be checked
     * @return boolean
     */
    private boolean hasEscapedCharacter(String str) {
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            switch (ch) {
                case '\'':
                case '"':
                case '<':
                case '>':
                case '&':
                	return true;
                default:
                	continue;
            }
        }
        return false;
    }
	    
    /**
     * Encode xml escaped characters accordingly
     *
     * @param str the string to be encoded
     * @return encoded xml string
     */
    private String encodeString(String str) {
        if (!hasEscapedCharacter(str)) {
        	return str;
        }
       
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '<') {
                buf.append("&lt;");
                continue;
            } else if (ch == '&') {
                buf.append("&amp;");
                continue;
            } else if (ch == '"') {
                buf.append("&quot;");
                continue;
            } else if (ch == '\'') {
                buf.append("&apos;");
                continue;
            } else if (ch == '>') {
                buf.append("&gt;");
                continue;
            }
            buf.append(ch);            
        }
        return buf.toString();        
    }

    /**
     * Converts the object node into an xml string
     * @param object node to convert
     * @param ePath the path prefix
     * @return xml string
     */
    private String nodeToXml(ObjectNode node, String ePath) throws OutBoundException {
        
        String myName = node.pGetTag();
        String myPath = ePath + "." + myName;
        Object[] fldNames = null;
        StringBuffer sb = new StringBuffer();
        sb.append("<tns:").append(myName);
	ArrayList fns = node.pGetFieldNames();
	if (fns != null) {
	    fldNames = fns.toArray();
	}
        // loop over the fields, but ignore the ID field
        String idFieldName = node.pGetTag() + "Id";
        for (int i = 0; i < fldNames.length; i++) {
            Object value = null;
            String fieldName = (String) fldNames[i];
            if (fieldName.equals(idFieldName)) {
                // Not sure if this is required. The outbound XSD appears to allow ID fields
                continue;
            }
            try {
                value = node.getValue(fieldName);
            } catch (ObjectException e) {
            }
            sb.append(" ").append(fieldName).append("=\"");
            if (value != null) {
                sb.append(convert(value));
            }
            sb.append("\"");
        }
        sb.append(">").append(LINEFEED);
        
        String[] childTypes = null;
        try {
            childTypes = ObjectFactory.getChildTypes(myPath);
        } catch (ObjectException e) {
            throw new OutBoundException(mLocalizer.t("OUT503: Could not convert " + 
                                    "an ObjectNode to an XML string: {0}", e));
        }

        ArrayList childNodes = null;
        for (int i = 0; childTypes != null && i < childTypes.length; i++) {
            childNodes = node.pGetChildren(childTypes[i]);
            for (int j = 0; childNodes != null && j < childNodes.size(); j++) {
                ObjectNode child = (ObjectNode) childNodes.get(j);
                sb.append(nodeToXml(child, myPath));
            }
        }
        sb.append(endTag(myName));
        return sb.toString();
    }
    
    private String startTag(String name) {
        return "<tns:" + name + ">";
    }

    private String endTag(String name) {
        return "</tns:" + name + ">\n";
    }
    
    private String replaceObjectName (String namespace, String objectName) {
    	
    	StringBuffer sb = new StringBuffer(namespace);
    	if (objectName == null)
    	   objectName = "";
        
        for (int from = sb.indexOf(TAG_APPLICATION); from >= 0; from = sb.indexOf(TAG_APPLICATION)) 
             sb.replace(from, from + TAG_APPLICATION.length(), objectName);
    
    	return (new String(sb));
    	
    }	   
    

}
