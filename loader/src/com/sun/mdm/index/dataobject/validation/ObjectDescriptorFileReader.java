/* DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
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
package com.sun.mdm.index.dataobject.validation;

import java.io.InputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.ArrayList;
import java.util.AbstractList;

import java.sql.Date;
import java.sql.Timestamp;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.loader.util.Localizer;
import com.sun.mdm.index.parser.EIndexObject;
import com.sun.mdm.index.parser.FieldDef;
import com.sun.mdm.index.parser.NodeDef;
import com.sun.mdm.index.parser.Utils;

import com.sun.mdm.index.objects.validation.PatternValidator;
import com.sun.mdm.index.objects.validation.ReferenceDescriptor;
import com.sun.mdm.index.objects.validation.ObjectDescriptor;
import com.sun.mdm.index.objects.validation.UserReferenceDescriptor;
import com.sun.mdm.index.objects.validation.UserCodeValidator;
import com.sun.mdm.index.objects.validation.FieldType;
import com.sun.mdm.index.objects.validation.FieldDescriptor;
import com.sun.mdm.index.objects.validation.StringValueValidator;
import com.sun.mdm.index.objects.validation.DateValueValidator;
import com.sun.mdm.index.objects.validation.LongValueValidator;
import com.sun.mdm.index.objects.validation.TimestampValueValidator;
import com.sun.mdm.index.objects.validation.IntegerValueValidator;
import com.sun.mdm.index.objects.validation.FloatValueValidator;
import com.sun.mdm.index.objects.validation.CharacterValueValidator;
import com.sun.mdm.index.objects.validation.ValueValidator;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.codelookup.UserCodeRegistry;
import com.sun.mdm.index.codelookup.CodeRegistry;

/**
 * This class is for reading object descriptor.
 * @author cye
 */
public class ObjectDescriptorFileReader implements ObjectDescriptorReader {

	private static Logger logger = Logger.getLogger("com.sun.mdm.index.dataobject.validation.ObjectDescriptorReader");
	private static Localizer localizer = Localizer.getInstance();
	
    private static final String LINEFEED = System.getProperty("line.separator", "\n");
    private static final String TAB = "    ";

    private static final String CONTENTHEADER
        					    = "package com.sun.mdm.index.objects.validation;" + LINEFEED
        						+ "import com.sun.mdm.index.objects.validation.exception.ValidationException;" + LINEFEED
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
             				    = TAB + TAB + "od = new ObjectDescriptor();" + LINEFEED 
             				    + TAB + TAB + "od.setObjectName(\"";

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

    private EIndexObject eIndexObject;
    private String destinationPath;    
    private AbstractList<ObjectDescriptor> objectDescriptors = new ArrayList<ObjectDescriptor>();
        
    /**
     * ObjectDescriptor Constructor
     */    
    public ObjectDescriptorFileReader() {    	
	}
    
    /**
     * Parses object descriptor input stream.
     * @param inputStream
     * @throws ValidationException
     */
    public void parse(InputStream inputStream) 
    	throws ValidationException {
    	try {
    		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    		Document doc = docBuilder.parse(inputStream);
    		eIndexObject = new EIndexObject();
    		eIndexObject.parse(doc);
    	} catch (ParserConfigurationException pex) {
    		throw new ValidationException(pex);
    	} catch(SAXException sex) {
    		throw new ValidationException(sex);    		
    	} catch (IOException ioex) {
    		throw new ValidationException(ioex);    		
    	}
    }
    
    /**
     * Creates a new instance of ObjectDescriptorWriter
     * @param path template path
     * @param eo elephant object
     */
    public ObjectDescriptorFileReader(String path, EIndexObject eIndexObject) {
    	this.eIndexObject = eIndexObject;
        this.destinationPath = path + "/com/sun/mdm/index/objects/validation/RegisterObjectDefinitions.java";
    }
    
    /**
     * Gets a list of objectDescriptors.
     * @return objectDescriptors
     */
    public AbstractList<ObjectDescriptor> getObjectDescriptors() {
    	return  objectDescriptors;
    }
    
    /**
     * Creates ObjectDescriptors
     * @param isValidateReference
     * @throws ValidationException
     */
    public void create(boolean validateReferenceEnabled) 
    	throws ValidationException {    	
    	try {    		
    		ArrayList<NodeDef> objectNodes = eIndexObject.getNodes();
    	    for (NodeDef objectNode : objectNodes) {
    	    	// object name
    	    	String objectName = objectNode.getTag();
    	    	ObjectDescriptor objectDescriptor = new DataObjectDescriptor();  
    	    	objectDescriptor.setObjectName(objectName);    	    	    	
    	    	// object fields
    	    	ArrayList<FieldDef> fields = objectNode.createFieldDefs();
    	    	for (FieldDef field : fields) {
    	    		String fieldName = field.getFieldName();
    	    		int fieldType = ToType(field);
    	    		int fieldAttribute = ToAttribute(field);	    		
    	    		// field descriptor
    	    		FieldDescriptor fieldDescriptor =  new FieldDescriptor(fieldName, fieldType, fieldAttribute);    	    		
    	    		objectDescriptor.addFieldDescriptor(fieldDescriptor);
    	    		
    	    		// Value validator
    	    		int length = field.getFieldSize();
                    String sFieldType = field.getFieldType();
                    String sMinValue = field.getMinimumValue();
                    String sMaxValue = field.getMaximumValue();
                    if (sFieldType.equals("string")) {
                        if (sMaxValue == null) {
                            sMaxValue = String.valueOf(length);
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
                    	objectDescriptor.addValueValidator(fieldName, toValueValidator(sFieldType, sMaxValue, sMinValue));
                    }
    	    		    	    	
                    if (sFieldType.equals("string")) {
                    	// Reference validator
                        String codeModule = field.getCodeModule();
                        if (validateReferenceEnabled && codeModule != null && codeModule.trim().length() != 0) {
                        	CodeRegistry codeRegistry = ValidationCodeRegistry.getInstance().getCodeRegistry();
                        	objectDescriptor.addReferenceDescriptor(fieldName, new ReferenceDescriptor(codeModule, codeRegistry));                        	                            
                        }                        
                        // Pattern validator
                        String pattern = field.getPattern();
                        if (pattern != null && pattern.trim().length() != 0) {
                        	objectDescriptor.addPatternValidator(fieldName, new PatternValidator(pattern));                        	
                        }                        
                        // UserReference validator 
                        String userCode = field.getUserCode();
                        if (validateReferenceEnabled && userCode != null && userCode.trim().length() != 0) {
                        	UserCodeRegistry userCodeRegistry = ValidationCodeRegistry.getInstance().getUserCodeRegistry();
                        	objectDescriptor.addUserReferenceDescriptor(fieldName, new UserReferenceDescriptor(userCode, userCodeRegistry));
                        }                        
                        // UserCode validator 
                        String constraintByField = field.getConstraintBy();
                        if (validateReferenceEnabled && constraintByField != null && constraintByField.trim().length() != 0) {
                        	UserCodeRegistry userCodeRegistry = ValidationCodeRegistry.getInstance().getUserCodeRegistry();                        	
                        	objectDescriptor.addUserCodeValidator(fieldName, new UserCodeValidator(constraintByField, userCodeRegistry));
                        }
                    }    	    		
    	    	}  
    	    	objectDescriptors.add(objectDescriptor);
    	    }    		
    	} catch (ValidationException vex) {
    		throw vex;
    	}
    }
        
   /**
    * Writes ObjectDescriptors into the file.
    */
    public void write() {

        NodeDef nd;
        String objectName;
        ArrayList<FieldDef> flds;
        StringBuffer sb = new StringBuffer();

        try {
            RandomAccessFile outStream = new RandomAccessFile(this.destinationPath, "rw");
            ArrayList<NodeDef> nodeList = eIndexObject.getNodes();
            outStream.write(CONTENTHEADER.getBytes("UTF-8"));
            for (int i = 0; i < nodeList.size(); i++) {
                nd = (NodeDef) nodeList.get(i);
                objectName = nd.getTag();
                String str = getObjectHeader(objectName);
                outStream.write(str.getBytes("UTF-8"));             
                flds = nd.createFieldDefs();
                for (int j = 0; j < flds.size(); j++) {
                    FieldDef fldDef = (FieldDef) flds.get(j);
                    String fldName = fldDef.getFieldName();
                    sb.append(TAB).append(TAB).append(TAB).append(FIELDHEADER);
                    sb.append(fldName);
                    sb.append("\", FieldType.").append(convertType(fldDef.getFieldType())).append(", ");
                    sb.append(LINEFEED).append(TAB).append(TAB).append(TAB).append(TAB);
                    sb.append(getFieldAttributes(fldDef)).append("));");
                    str = sb.toString();
                    outStream.write(str.getBytes("UTF-8"));
                    sb.setLength(0);
                    int len = fldDef.getFieldSize();
                    // Add support for maximum value/length                   
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
                    str = sb.toString();
                    outStream.write(str.getBytes("UTF-8"));

                    sb.setLength(0);
                }
                outStream.write(OBJECTTRAILER.getBytes("UTF-8"));
            }
            outStream.write(CONTENTTRAILER.getBytes("UTF-8"));
            outStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Gets field attributes.
     * @param fd
     * @return field attributes.
     */
    private String getFieldAttributes(FieldDef fd) {
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

    private int ToAttribute(FieldDef fieldDef) {
    	int fieldAttribute = FieldDescriptor.NONE; 
    	if (!fieldDef.isNullable()) {
    		fieldAttribute += FieldDescriptor.REQUIRED;
    	}
    	if (fieldDef.isUpdateable()) {
    		fieldAttribute += FieldDescriptor.UPDATEABLE;
    	}
    	return fieldAttribute;
    }
    
    /**
     * Gets Object Header.
     * @param objectName
     * @return object header.
     */
    private String getObjectHeader(String objectName) {
        return (OBJECTHEADER1 + objectName + OBJECTHEADER2);
    }

    /**
     * Gets ValueValidator
     * @param fieldType
     * @param sMax
     * @param sMin
     * @return String
     */
    private String getValueValidator(String fieldType, String sMax, String sMin) {
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

    /**
     * Gets ValueValidator
     * @param fieldType
     * @param maxValue
     * @param minValue
     * @return ValueValidator
     */
    private ValueValidator toValueValidator(String fieldType, String maxValue, String minValue) {
        ValueValidator valueValidator = null;
        if (maxValue == null && minValue == null) {
            return valueValidator;
        }
        StringBuffer sb = new StringBuffer("new ");
        if ("int".equals(fieldType)) {
            valueValidator = new IntegerValueValidator(maxValue != null ? Integer.parseInt(maxValue) : null, 
            										   minValue != null ? Integer.parseInt(minValue) : null);
        } else if ("string".equals(fieldType)) {
            valueValidator = new StringValueValidator(maxValue != null ? new Integer(maxValue) : null, 
            										  minValue != null ? new Integer(minValue) : null);            
        } else if ("char".equals(fieldType)) {        	
            valueValidator = new CharacterValueValidator(maxValue != null ? new Integer(maxValue) : null, 
            											 minValue != null ? new Integer(minValue) : null);            
        } else if ("long".equals(fieldType)) {
            valueValidator = new LongValueValidator(maxValue != null ? Long.parseLong(maxValue) : null, 
            										minValue != null ? Long.parseLong(minValue) : null);            
        } else if ("float".equals(fieldType)) {
            valueValidator = new FloatValueValidator(maxValue != null ? Float.parseFloat(maxValue) : null, 
            										 minValue != null ? Float.parseFloat(minValue) : null);            
        } else if ("date".equals(fieldType)) {
            valueValidator = new DateValueValidator(maxValue != null ? Date.valueOf(maxValue) : null, 
            										minValue != null ? Date.valueOf(minValue) : null);            
        } else if ("timestamp".equals(fieldType)) {
            valueValidator = new TimestampValueValidator(maxValue != null ? Timestamp.valueOf(maxValue) : null, 
            											 minValue != null ? Timestamp.valueOf(minValue) : null);            
        } else {
            return valueValidator;
        }        
        return valueValidator;
    }
    
    /**
     * Gets data field type.
     * @param fieldType.
     * @return fieldType.
     */
    private int ToType(FieldDef fieldDef) {
    	
    	String fieldType = fieldDef.getFieldType();
        if ("int".equals(fieldType)) {
            return FieldType.INTEGER;
        } else if ("boolean".equals(fieldType)) {
            return FieldType.BOOLEAN;
        } else if ("string".equals(fieldType)) {
            return FieldType.STRING;
        } else if ("byte".equals(fieldType)) {
            return FieldType.BYTE;
        } else if ("char".equals(fieldType)) {
            return FieldType.CHAR;            
        } else if ("long".equals(fieldType)) {
            return FieldType.LONG;
        } else if ("blob".equals(fieldType)) {
            return FieldType.RAW;
        } else if ("float".equals(fieldType)) {
            return FieldType.FLOAT;
        } else if ("date".equals(fieldType)) {
            return FieldType.DATE;
        } else if ("timestamp".equals(fieldType)) {
            return FieldType.TIMESTAMP;
        } else {
            return FieldType.UNKNOWN;
        }
    }
    
    /**
     * Converts data field type.
     * @param fieldType.
     * @return fieldType.
     */
    private String convertType(String fieldType) {
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
    
    /**
     * ObjectDescriptor main 
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        try { 
        	if (args.length < 2) {
        		System.out.println("Usage: java com.sun.mdm.index.dataobject.validation.ObjectDescriptorReader [object_descriptor_file_path] [working_dir]");
        		System.exit(0);
        	}
            String xmlPath = args[0];
            String genPath = args[1];
            EIndexObject eo = Utils.parseEIndexObject(xmlPath);
            ObjectDescriptorFileReader odw = new ObjectDescriptorFileReader(genPath, eo);
            odw.write();
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}
