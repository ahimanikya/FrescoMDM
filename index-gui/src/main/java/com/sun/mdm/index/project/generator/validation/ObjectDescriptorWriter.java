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
package com.sun.mdm.index.project.generator.validation;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import com.sun.mdm.index.parser.EIndexObject;
import com.sun.mdm.index.parser.FieldDef;
import com.sun.mdm.index.parser.NodeDef;
import com.sun.mdm.index.parser.Utils;


/**
 * @author jwu
 */
public class ObjectDescriptorWriter {

    private EIndexObject mEO = null;
    private String mOutPath = null;

    private static final String LINEFEED
             = new String(System.getProperty("line.separator"));

    private static final String TAB = "    ";

    private static final String CONTENTHEADER
        = "package com.sun.mdm.index.objects.validation;" + LINEFEED
        + "import com.sun.mdm.index.objects.validation.exception.ValidationException;"
        + LINEFEED
        + "import java.sql.Date;" + LINEFEED
        + "import java.sql.Timestamp;" + LINEFEED
        + "import java.util.ArrayList;" + LINEFEED
        + "import com.sun.mdm.index.objects.validation.PatternValidator;" + LINEFEED
        + "import com.sun.mdm.index.objects.validation.ReferenceDescriptor;" + LINEFEED
        + "import com.sun.mdm.index.objects.validation.ObjectDescriptor;" + LINEFEED
        + "import com.sun.mdm.index.objects.validation.UserReferenceDescriptor;" + LINEFEED
        + "import com.sun.mdm.index.objects.validation.UserCodeValidator;" + LINEFEED
        + "import com.sun.mdm.index.objects.validation.FieldType;" + LINEFEED
        + "import com.sun.mdm.index.objects.validation.FieldDescriptor;" + LINEFEED
        + "import com.sun.mdm.index.objects.validation.StringValueValidator;" + LINEFEED
        + "import com.sun.mdm.index.objects.validation.DateValueValidator;" + LINEFEED
        + "import com.sun.mdm.index.objects.validation.LongValueValidator;" + LINEFEED
        + "import com.sun.mdm.index.objects.validation.TimestampValueValidator;" + LINEFEED
        + "import com.sun.mdm.index.objects.validation.IntegerValueValidator;" + LINEFEED
        + "import com.sun.mdm.index.objects.validation.FloatValueValidator;" + LINEFEED + LINEFEED
        + "public class RegisterObjectDefinitions {" + LINEFEED + LINEFEED
        + TAB + "public RegisterObjectDefinitions() {" + LINEFEED 
        + TAB + "}" + LINEFEED + LINEFEED
        + TAB + "public ArrayList registerObjects() {" + LINEFEED + LINEFEED
        + TAB + TAB + "ArrayList list = new ArrayList();" + LINEFEED 
        + TAB + TAB + "ObjectDescriptor od;"
        + LINEFEED;
        
    private static final String OBJECTHEADER1
             = TAB + TAB + "od = new ObjectDescriptor();"
             + LINEFEED + TAB + TAB
             + "od.setObjectName(\"";

    private static final String OBJECTHEADER2
             = "\");" + LINEFEED
             + TAB + TAB + "try {" + LINEFEED;

    private static final String FIELDHEADER
            = "od.addFieldDescriptor(new FieldDescriptor(\"";

    private static final String VALIDATORHEADER = "od.addValueValidator(\"";
    private static final String REFERENCEHEADER = "od.addReferenceDescriptor(\"";
    private static final String PATTERNHEADER = "od.addPatternValidator(\"";
    private static final String USER_REFERENCE_HEADER = "od.addUserReferenceDescriptor(\"";
    private static final String USER_CODE_HEADER = "od.addUserCodeValidator(\"";

    private static final String OBJECTTRAILER
            = TAB + TAB + TAB
            + "list.add(od);" + LINEFEED
            + TAB + TAB
            + "} catch (ValidationException e) {" + LINEFEED
            + TAB + TAB + TAB
            + "throw new RuntimeException(e.getMessage());" + LINEFEED
            + TAB + TAB + "}" + LINEFEED;

    private static final String CONTENTTRAILER
            = TAB + TAB + "return list;" + LINEFEED
            + TAB + "}" + LINEFEED + LINEFEED
            + "}" + LINEFEED;

    private static final String UPDATEABLE = "FieldDescriptor.UPDATEABLE";
    private static final String NILLABLE = "FieldDescriptor.NILLABLE";
    private static final String REQUIRED = "FieldDescriptor.REQUIRED";
    private static final String EXCLINSERT = "FieldDescriptor.EXCLINSERT";
    private static final String NONE = "FieldDescriptor.NONE";

    /**
     * Creates a new instance of ObjectDescriptorWriter
     *
     * @param path template path
     * @param eo elephant object
     */
    public ObjectDescriptorWriter(String path, EIndexObject eo) {
        mEO = eo;
        mOutPath = path + "\\com\\sun\\mdm\\index\\objects\\validation\\RegisterObjectDefinitions.java";
    }

    /**
     * The ain program for the ObjectDescriptorWriter class
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        try {
            //String xmlPath = args[0];
            //String genPath = args[1];
            String xmlPath = "eindex50.xml";
            String genPath = "";

            EIndexObject eo = Utils.parseEIndexObject(xmlPath);

            ObjectDescriptorWriter odw = new ObjectDescriptorWriter(genPath, eo);
            odw.write();
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @todo Document this ethod
     */
    public void write() {

        NodeDef nd;
        String objectName;
        ArrayList flds;
        StringBuffer sb = new StringBuffer();

        PrintStream outStream = null;
        try {
            outStream = new PrintStream(new FileOutputStream(mOutPath), true);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        ArrayList nodeList = mEO.getNodes();
        outStream.println(CONTENTHEADER);
        for (int i = 0; i < nodeList.size(); i++) {
            nd = (NodeDef) nodeList.get(i);
            objectName = nd.getTag();
            outStream.println(getObjectHeader(objectName));
            //flds = nd.getFields();
            flds = nd.createFieldDefs();
            for (int j = 0; j < flds.size(); j++) {
                FieldDef fldDef = (FieldDef) flds.get(j);
                String fldName = fldDef.getFieldName();
                sb.append(TAB).append(TAB).append(TAB).append(FIELDHEADER);
                sb.append(fldName);
                sb.append("\", FieldType.").append(convertType(fldDef.getFieldType())).append(", ");
                sb.append(LINEFEED).append(TAB).append(TAB).append(TAB).append(TAB);
                sb.append(getFieldAttributes(fldDef)).append("));");
                outStream.println(sb.toString());
                sb.setLength(0);
                int len = fldDef.getFieldSize();
                /**
                 * added support for maximum value/length
                 */
                String sFieldType = fldDef.getFieldType();
                String sMinValue = fldDef.getMinimumValue();
                String sMaxValue = fldDef.getMaximumValue();
                if (sFieldType.equals("string")) {
                    if (sMaxValue == null) {
                        sMaxValue = String.valueOf(len);
                    }
                }
                if (sFieldType.equals("date")) {
                    if (sMaxValue != null) {
                        sMaxValue = "\"" + sMaxValue + "\"";
                    }
                    if (sMinValue != null) {
                        sMinValue = "\"" + sMinValue + "\"";
                    }
                }
                if (sMaxValue != null || sMinValue != null) {
                    sb.append(TAB).append(TAB).append(TAB).append(VALIDATORHEADER);
                    sb.append(fldName).append("\", ");
                    sb.append(getValueValidator(fldDef.getFieldType(),
                            sMaxValue, sMinValue));
                }

                if (sFieldType.equals("string")) {
                    String sModule = fldDef.getCodeModule();
                    if (sModule != null && sModule.trim().length() != 0) {
                        sb.append(LINEFEED);
                        sb.append(TAB).append(TAB).append(TAB).append(REFERENCEHEADER);
                        sb.append(fldName).append("\", new ReferenceDescriptor(\"");
                        sb.append(sModule).append("\"));");
                    }
                    String sPattern = fldDef.getPattern();
                    if (sPattern != null && sPattern.trim().length() != 0) {
                        sb.append(LINEFEED);
                        sb.append(TAB).append(TAB).append(TAB).append(PATTERNHEADER);
                        sb.append(fldName).append("\", new PatternValidator(\"" + sPattern + "\"));");
                    }
                    String sUserCode = fldDef.getUserCode();
                    if (sUserCode != null && sUserCode.trim().length() != 0) {
                        sb.append(LINEFEED);
                        sb.append(TAB).append(TAB).append(TAB).append(USER_REFERENCE_HEADER);
                        sb.append(fldName).append("\", new UserReferenceDescriptor(\"");
                        sb.append(sUserCode).append("\"));");
                    }
                    String sConstraintByField = fldDef.getConstraintBy();
                    if (sConstraintByField != null && sConstraintByField.trim().length() != 0) {
                        sb.append(LINEFEED);
                        sb.append(TAB).append(TAB).append(TAB).append(USER_CODE_HEADER);
                        sb.append(fldName).append("\", new UserCodeValidator(\"");
                        sb.append(sConstraintByField).append("\"));");
                    }
                }
                outStream.println(sb.toString());
                sb.setLength(0);
            }
            outStream.println(OBJECTTRAILER);
        }
        outStream.println(CONTENTTRAILER);
        outStream.flush();
        outStream.close();
    }


    String getFieldAttributes(FieldDef fd) {
        StringBuffer sb = new StringBuffer();

        if (!fd.isNullable()) {
            sb.append(REQUIRED);
        }

        if (fd.isUpdateable()) {
            if (sb.length() != 0) {
                sb.append(" + ");
            }
            sb.append(UPDATEABLE);
        }

        return (sb.length() == 0 ? NONE : sb.toString());
    }


    String getObjectHeader(String objectName) {
        return (OBJECTHEADER1 + objectName + OBJECTHEADER2);
    }

    String getValueValidator(String fieldType, String sMax, String sMin) {

        String sType;
        if (sMax == null && sMin == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer("new ");

        if ("int".equals(fieldType)) {
            sType = "Integer";
        } else if ("string".equals(fieldType)) {
            sType = "String";
        } else if ("char".equals(fieldType)) {
            sType = "Character";
        } else if ("long".equals(fieldType)) {
            sType = "Long";
        } else if ("float".equals(fieldType)) {
            sType = "Float";
        } else if ("date".equals(fieldType)) {
            sType = "Date";
        } else if ("timestamp".equals(fieldType)) {
            sType = "Timestamp";
        } else {
            return "";
        }
        sb.append(sType).append("ValueValidator(");
        if (sMax == null) {
            sb.append("null, ");
        } else {
            if (sType.equals("String")) {
                sb.append("new Integer(").append(sMax).append("), ");
            } else if (sType.equals("Date")) {
                sb.append("Date.valueOf(").append(sMax).append("), ");
            } else if (sType.equals("Timestamp")) {
                sb.append("Timestamp.valueOf(").append(sMax).append("), ");
            } else {
                sb.append("new ").append(sType).append("(").append(sMax).append("), ");
            }
        }

        if (sMin == null) {
            sb.append("null");
        } else {
            if (sType.equals("String")) {
                sb.append("new Integer(").append(sMin);
            } else if (sType.equals("Date")) {
                sb.append("Date.valueOf(").append(sMin).append(")");
            } else if (sType.equals("Timestamp")) {
                sb.append("Timestamp.valueOf(").append(sMin).append(")");
            } else {
                sb.append("new ").append(sType).append("(").append(sMin).append(")");
            }
        }
        sb.append("));");
        return sb.toString();
    }

    String convertType(String fieldType) {

        if ("int".equals(fieldType)) {
            return "INTEGER";
        } else if ("boolean".equals(fieldType)) {
            return "BOOLEAN";
        } else if ("string".equals(fieldType)) {
            return "STRING";
        } else if ("byte".equals(fieldType)) {
            return "BYTE";
        } else if ("char".equals(fieldType)) {
            return "CHAR";            
        } else if ("long".equals(fieldType)) {
            return "LONG";
        } else if ("blob".equals(fieldType)) {
            return "RAW";
        } else if ("float".equals(fieldType)) {
            return "FLOAT";
        } else if ("date".equals(fieldType)) {
            return "DATE";
        } else if ("timestamp".equals(fieldType)) {
            return "TIMESTAMP";
        } else {
            return "UNKNOWN";
        }
    }
}
