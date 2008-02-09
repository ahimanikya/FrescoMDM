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

package com.sun.mdm.index.project.generator.outbound;

import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.sun.mdm.index.parser.EIndexObject;
import com.sun.mdm.index.parser.FieldDef;
import com.sun.mdm.index.parser.NodeDef;
import com.sun.mdm.index.parser.RelationDef;
import com.sun.mdm.index.parser.Utils;

import java.io.FileInputStream;
import com.sun.mdm.index.parser.ParserException;
/**
 * Generates outbound XSD that is based on object definition
 * Uses eIndexObject to get the object defintion
 * @author  sdua
 */
public class OutboundXSDBuilder {
    
    private final String TAG_BEGIN = "<";
    private final String TAG_END = ">";
    private final String TAG_SLASH_END = "/>";
    private final String XSD = "xsd:";
    private final String TAG_BEGIN_COMMENT = "<!-- ";
    private final String TAG_END_COMMENT = " -->";
    private final String TAG_ELEMENT_NAME = "<xsd:element name=";
    private final String TAG_ELEMENT_END = "</xsd:element>";
    private final String TAG_PREFIX = "Out";
    private final String TAG_APPLICATION = "$APPLICATION$";
    private final String TAG_PRIMARY = "$PRIMARY$";
    private final String QUOTE = "\"";
    private final String TAG_COMPLEX_TYPE = "<xsd:complexType>";
    private final String TAG_SEQUENCE = "<xsd:sequence>";
    private final String TAG_ELEMENT_REF = "<xsd:element ref=";
    private final String TAG_ATTRIBUTE_NAME = "<xsd:attribute name=";
    private final String TAG_ATTRIBUTE_END = "</xsd:attribute>";
    private final String TAG_COMPLEX_END = "</xsd:complexType>";
    private final String TAG_SCHEMA_END = "</xsd:schema>";
    private final String TAG_SEQUENCE_END = "</xsd:sequence>";
    private final String REF = "ref=";
    private final String MINOCCURS = "minOccurs=";
    private final String MAXOCCURS = "maxOccurs=";
    private final String UNBOUNDED = "\"unbounded\"";
    private final String TYPE = "type=";
    private final String USE = "use=";
    private final String XSDSTRING = "\"xsd:string\"";
    private final String TAG_DATA_REQUIRED = "\"required\"";
    private final String TAG_DATA_OPTIONAL = "\"optional\"";
    private final String LINEFEED
             = new String(System.getProperty("line.separator"));
    private final String TAB = "\t";

    private String TAG_HEADER 
        = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LINEFEED
        + "<!--" + LINEFEED 
        + TAB + "Generated XSD for $APPLICATION$" + LINEFEED
        + "-->" + LINEFEED
        + "<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" + LINEFEED
        //+ "targetNamespace=\"www.eview.Application\"" + LINEFEED
        + "elementFormDefault=\"qualified\">"  + LINEFEED
        + "<xsd:element name=\"OutMsg\">" + LINEFEED
        + "<xsd:complexType>" + LINEFEED
        + "<xsd:sequence>" + LINEFEED
        + "<xsd:element ref=\"SBR\" minOccurs=\"1\" maxOccurs=\"1\"/>" + LINEFEED
        +  "</xsd:sequence>" + LINEFEED
        + "<xsd:attribute name=\"Event\" use=\"required\">" + LINEFEED
        + "<xsd:simpleType>" + LINEFEED
        + "<xsd:restriction base=\"xsd:string\">" + LINEFEED
        + "<xsd:enumeration value=\"ADD\"/>"
        + "<xsd:enumeration value=\"UPD\"/>"
        + "<xsd:enumeration value=\"MRG\"/>"
        + "<xsd:enumeration value=\"UMRG\"/>"
        + "<xsd:enumeration value=\"DEA\"/>"
        + "<xsd:enumeration value=\"REA\"/>"
        + "</xsd:restriction>" + LINEFEED
        + "</xsd:simpleType>" + LINEFEED
        + "</xsd:attribute>" + LINEFEED
        + "<xsd:attribute name=\"ID\" type=\"xsd:string\" use=\"required\"/>" + LINEFEED
        + "</xsd:complexType>" + LINEFEED
        + "</xsd:element>" + LINEFEED 
        + "<xsd:element name=\"SBR\">" + LINEFEED
        + "<xsd:complexType>" + LINEFEED
        + "<xsd:sequence>" + LINEFEED
        + "<xsd:element ref=\"SystemObject\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>" + LINEFEED
        + "<xsd:element ref=\"$PRIMARY$\" minOccurs=\"0\" maxOccurs=\"1\"/>" + LINEFEED
        +  "</xsd:sequence>" + LINEFEED
        + "<xsd:attribute name=\"EUID\" type=\"xsd:string\" use=\"required\"/>" + LINEFEED
        + "<xsd:attribute name=\"Status\" type=\"xsd:string\" use=\"optional\"/>" + LINEFEED
        + "<xsd:attribute name=\"CreateFunction\" type=\"xsd:string\" use=\"optional\"/>" + LINEFEED
        + "<xsd:attribute name=\"CreateUser\" type=\"xsd:string\" use=\"optional\"/>" + LINEFEED
        + "<xsd:attribute name=\"UpdateSystem\" type=\"xsd:string\" use=\"optional\"/>" + LINEFEED
        + "<xsd:attribute name=\"ChildType\" type=\"xsd:string\" use=\"optional\"/>" + LINEFEED
        + "<xsd:attribute name=\"CreateSystem\" type=\"xsd:string\" use=\"optional\"/>" + LINEFEED
        + "<xsd:attribute name=\"UpdateDateTime\" type=\"xsd:string\" use=\"optional\"/>" + LINEFEED
        + "<xsd:attribute name=\"CreateDateTime\" type=\"xsd:string\" use=\"optional\"/>" + LINEFEED
        + "<xsd:attribute name=\"UpdateFunction\" type=\"xsd:string\" use=\"optional\"/>" + LINEFEED
        + "<xsd:attribute name=\"RevisionNumber\" type=\"xsd:string\" use=\"optional\"/>" + LINEFEED
        + "<xsd:attribute name=\"UpdateUser\" type=\"xsd:string\" use=\"optional\"/>" + LINEFEED
        + "</xsd:complexType>" + LINEFEED
        + "</xsd:element>" + LINEFEED
        + "<xsd:element name=\"SystemObject\">" + LINEFEED
        + "<xsd:complexType>" + LINEFEED
        + "<xsd:attribute name=\"SystemCode\" type=\"xsd:string\" use=\"required\"/>" + LINEFEED
        + "<xsd:attribute name=\"LID\" type=\"xsd:string\" use=\"required\"/>" + LINEFEED
        + "<xsd:attribute name=\"Status\" type=\"xsd:string\" use=\"required\"/>" + LINEFEED
        + "</xsd:complexType>" + LINEFEED
        + "</xsd:element>" + LINEFEED;
        
      
    private EIndexObject mEO = null;
    private Hashtable mRelationTable = null; /* stores (object, list of children) */
    
    /** Creates a new instance of OutboundXSDBuilder */
    public OutboundXSDBuilder() {
        mRelationTable = new Hashtable();
    }

    
    /**
     * Build a XSD String from an EIndexObject
     * @param eo eIndex object
     * @return String a String contains XSD
     */
    public String buildXSD(EIndexObject eo) {
        
        String applName = eo.getName();
        ArrayList nodes = eo.getNodes();
        NodeDef topNode = (NodeDef) nodes.get(0);
        if (topNode == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer(TAG_HEADER);
        int from = sb.indexOf(TAG_APPLICATION);
        sb.replace(from, from + TAG_APPLICATION.length(), applName);
        from = sb.indexOf(TAG_PRIMARY);
        sb.replace(from, from + TAG_PRIMARY.length(), topNode.getTag());
        buildDependentTable(eo);
        for (int i = 0; i < nodes.size(); i++) {
            sb.append(formatXSDString((NodeDef) nodes.get(i)));
        }
        sb.append(LINEFEED).append(TAG_SCHEMA_END).append(LINEFEED);
        
        return sb.toString();
    }

    /**
     * Build a xsd file from an EIndexObject
     * @param eo eIndex object
     * @param xsdFile output file
     * @return String a String contains XSD
     */
    public void buildXSD(EIndexObject eo, File xsdFile) throws IOException {

        FileWriter writer = new FileWriter(xsdFile);
        String xsdStr = buildXSD(eo);
        writer.write(xsdStr);
        writer.flush();
        writer.close();
        return;
    }

    /**  create XSD respresenation for the topNode and its children
     */
    private StringBuffer formatXSDString(NodeDef topNode) {
        
        StringBuffer sbElement = new StringBuffer(); 
  
        String nodeName = topNode.getTag();
        
        sbElement.append(TAG_ELEMENT_NAME).append(QUOTE).append(nodeName).
                append(QUOTE).append(TAG_END).append(LINEFEED);
        sbElement.append(TAG_COMPLEX_TYPE).append(LINEFEED);
        List children = (List) mRelationTable.get(nodeName);
        /*
         *   add children
         *
         */
        if (children != null) {
          sbElement.append(TAG_SEQUENCE).append(LINEFEED);
        
          for (int i = 0; i < children.size(); i++) {
            String child = (String) children.get(i);
            sbElement.append(TAG_ELEMENT_REF).append(quote(child));
            sbElement.append(MINOCCURS).append(quote("0")).append(MAXOCCURS).append(UNBOUNDED).append(TAG_SLASH_END);
            sbElement.append(LINEFEED);
          }
          sbElement.append(TAG_SEQUENCE_END).append(LINEFEED);
        }
        
        /*
         *   add attributes from field definitions
         */
        ArrayList flds = topNode.createFieldDefs();
        if (flds != null) {
            
            for (int i = 0; i < flds.size(); i++) {
                FieldDef fldDef = (FieldDef) flds.get(i);
                sbElement.append(TAG_ATTRIBUTE_NAME).append(quote(fldDef.getFieldName())).append(TYPE);
                sbElement.append(QUOTE).append(XSD).append(fldDef.getFieldType()).append(QUOTE).append(TAB);
                sbElement.append(USE);
                if (!fldDef.isNullable()) {
                    sbElement.append(TAG_DATA_REQUIRED).append(TAG_SLASH_END);
                } else {
                    sbElement.append(TAG_DATA_OPTIONAL).append(TAG_SLASH_END);
                }
                sbElement.append(LINEFEED);
            }
        }
        sbElement.append(TAG_COMPLEX_END);
        sbElement.append(LINEFEED);
        sbElement.append(TAG_ELEMENT_END);
        sbElement.append(LINEFEED);
        
        return sbElement;
    }

    private void buildDependentTable(EIndexObject eo) {
        ArrayList list = eo.getRelationships();
        for (int i = 0; i < list.size(); i++) {
            RelationDef relDef = (RelationDef) list.get(i);
            ArrayList dependents = relDef.getChildren();
            if (dependents != null) {
                mRelationTable.put(relDef.getName(), dependents);
            }
        }
    }
    
    private String quote(String str) {
       return "\"" + str + "\" ";	
    }
    
    public static void main(String args[]) {
        StringBuffer sb = null;
        FileInputStream reader = null;
        EIndexObject eo = null;
        String fileName = "eIndex50.xml";
        try {
            eo = Utils.parseEIndexObject(fileName);
        } catch (ParserException e) {
            e.printStackTrace();
            System.exit(1);
        }
        OutboundXSDBuilder builder = new OutboundXSDBuilder();
        String xsd = builder.buildXSD(eo);
        System.out.println(xsd);
    }
}

