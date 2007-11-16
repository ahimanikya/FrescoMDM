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
package com.sun.mdm.index.ejb.master.helper;

import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathParser;
import com.sun.mdm.index.objects.factory.SimpleFactory;
import com.sun.mdm.index.objects.metadata.MetaDataService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;


/** EiEvent 4.1.2 / 4.5 Parser
 * @author dcidon
 */
public class GenericIterator implements SystemObjectIterator {
    private BufferedReader reader;
    private String currentLine;

    /** Creates new iterator
     * @param newReader Reader object of one or more eiEvent messages
     * @throws ProcessingException error occured
     */
    public GenericIterator(Reader newReader) throws ProcessingException {
        try {
            this.reader = new BufferedReader(newReader);
            nextLine();
            nextRecord();
        } catch (IOException e) {
            throw new ProcessingException(e);
        }
    }

    // Get next record
    private void nextRecord() throws IOException {
        while ((currentLine != null) && !currentLine.startsWith("Record")) {
            nextLine();
        }
    }

    // Get next non-comment line
    private String nextLine() throws IOException {
        do {
            currentLine = reader.readLine();
        } while ((currentLine != null)  
                 && (currentLine.startsWith("//") || currentLine.trim().equals("")));

        return currentLine;
    }

    /** Get the next Person in the iterator
     * @return The next Person object in the iterator.
     * @throws ProcessingException error occured
     */
    public IteratorRecord next() throws ProcessingException {
        if (!hasNext()) {
            throw new NoSuchElementException("Reached end of iterator.");
        }

        try {
            ArrayList sysObjList = new ArrayList();
            String objectType = getValue("Record");
            nextLine();

            while ((currentLine != null) && currentLine.startsWith("Key")) {
                SystemObject sysObj = createSystemObject(objectType);
                sysObjList.add(sysObj);
            }

            SystemObject[] sysObjArray = new SystemObject[sysObjList.size()];

            for (int i = 0; i < sysObjArray.length; i++) {
                sysObjArray[i] = (SystemObject) sysObjList.get(i);
            }

            TransactionObject trans = null;

            return new IteratorRecord(sysObjArray, trans);
        } catch (Exception e) {
            throw new ProcessingException(e);
        } finally {
            //Get to the next record
            try {
                nextRecord();
            } catch (IOException e) {
                throw new ProcessingException(e);
            }
        }
    }

    private SystemObject createSystemObject(String objectType)
                                     throws ProcessingException {
        try {
            ObjectNode objNode = SimpleFactory.create(objectType);
            SystemObject sysObject = new SystemObject();
            String[] key = getValue("Key").split(",");
            sysObject.setSystemCode(key[0]);
            sysObject.setLID(key[1]);
            sysObject.setChildType(objectType);
            sysObject.setObject(objNode);
            sysObject.setValue("Status", "active");

            Date date = new Date();
            populateSystemObject(sysObject);
            //If system object date info not provided, then put current date
            if (sysObject.getUpdateDateTime() == null) {
                sysObject.setUpdateDateTime(date);
            }
            if (sysObject.getCreateDateTime() == null) {
                sysObject.setCreateDateTime(date);
            }
            return sysObject;
        } catch (Exception e) {
            throw new ProcessingException(e);
        }
    }

    private void populateSystemObject(SystemObject sysObj)
                               throws ProcessingException {
        try {
            ObjectNode objNode = sysObj.getObject();
            String tag = objNode.pGetTag();
            nextLine();

            while ((currentLine != null) && !currentLine.startsWith("Key")  
                   && !currentLine.startsWith("Record")) {
                String[] value = currentLine.split("=");
                if (value[0].startsWith("SystemObject")) {
                    String field = value[0].substring(value[0].indexOf('.') + 1);
                    EPath setterPath = EPathParser.parse(value[0]);
                    String fullPath = "Enterprise.SystemObject." + field;
                    setFieldValue(sysObj, setterPath, fullPath, value[1]);
                } else {
                    String sPath = tag + "." + value[0];
                    String fullPath = "Enterprise.SystemSBR." + sPath;
                    fullPath = fullPath.replace('[', '~');
                    fullPath = fullPath.replace(']', '~');
                    fullPath = fullPath.replaceAll("~.~", "");

                    EPath setterPath = EPathParser.parse(sPath);
                    setFieldValue(objNode, setterPath, fullPath, value[1]);
                }
                nextLine();
            }
        } catch (Exception e) {
            throw new ProcessingException(e);
        }
    }

    private void setFieldValue(ObjectNode objNode, EPath setterPath, String fullPath, 
    String value) throws Exception {
        //ToDo: handle other types
        String type = MetaDataService.getFieldType(fullPath);
        Object objValue = value;
        
        if (type.equalsIgnoreCase(ObjectField.OBJECTMETA_DATE_STRING)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            objValue = sdf.parse(value);
        }

        EPathAPI.addObjectValue(setterPath, objNode, objValue);        
    }
    
    private String getValue(String name) throws ProcessingException {
        String[] values = currentLine.split("=");

        if (values[0].equals(name)) {
            return values[1];
        } else {
            throw new ProcessingException("Invalid property: [" + values[0] 
                                          + "]. " + "  Expecting: [" + name 
                                          + "].");
        }
    }

    /** Returns boolean indicating whether there are additional Person objects in
     * the iterator
     * @return Boolean indicating whether there are additional Person
     * objects in the iterator.
     */
    public boolean hasNext() {
        return (currentLine != null);
    }

    /** Test method
     * @param args file name
     */
    public static void main(String[] args) {
        System.out.println("Reading: " + args[0]);

        try {
            FileReader reader0 = new FileReader(args[0]);
            GenericIterator iterator = new GenericIterator(reader0);

            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
