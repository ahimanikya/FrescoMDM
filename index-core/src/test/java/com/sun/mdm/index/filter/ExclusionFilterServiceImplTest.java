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
package com.sun.mdm.index.filter;

import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.ObjectNode;
import java.io.IOException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author sailaja
 */
public class ExclusionFilterServiceImplTest extends TestCase {

    public ExclusionFilterServiceImplTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        try {
            System.out.println("Entering the setUp() :");
            ConfigurationService cs = ConfigurationService.getInstance();
            // First fs = First.getInstance();

            InputStream is = getConfigFileStream("filter.xml");
            load(is);
            is.close();

        } catch (InstantiationException ex) {
        // Logger.getLogger(ExclusionFilterServiceImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of blockingExclusion method, of class ExclusionFilterServiceImpl.
     */
    public void testBlockingExclusion() throws Throwable {
        System.out.println("blockingExclusion");
        ObjectNode objectToBlock = createObjNode();
        ExclusionFilterServiceImpl instance = new ExclusionFilterServiceImpl();
        ObjectNode expResult = createExpectedObjectForMatching();
        ObjectNode result = instance.blockingExclusion(objectToBlock);
        assertEquals(true, true);
        System.out.println("  end blockingExclusion");
//     TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    private ObjectNode createObjNode() throws java.lang.Throwable {

        ArrayList names = new ArrayList(6);
        names.add(0, "Address");
        names.add(1, "SSN");
        names.add(2, "FirstName");
        names.add(3, "LastName");
        names.add(4, "Age");
        names.add(5, "Gender");


        ArrayList values = new ArrayList(6);
        values.add(0, "stc");
        values.add(1, "111111111");
        values.add(2, "bill");
        values.add(3, "hello");
        values.add(4, "27");
        values.add(5, "m");

        ArrayList types = new ArrayList(6);
        types.add(0, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        types.add(1, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        types.add(2, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        types.add(3, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        types.add(4, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        types.add(5, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        ObjectNode objNode = new ObjectNode("SystemObject", names, types, values);
        return objNode;
    }

    private ObjectNode createExpectedObjectForMatching() throws Exception {

        ArrayList names = new ArrayList(6);
        names.add(0, "Address");
        names.add(1, "SSN"); //"000007605"
        names.add(2, "FirstName"); //lipika       
        names.add(3, "LastName");
        names.add(4, "Age");
        names.add(5, "Gender");

        System.out.println("names " + names.size());
        ArrayList values = new ArrayList(6);
        values.add(0, "stc");
        values.add(1, "111111111");
        values.add(2, null);
        values.add(3, "hello");
        values.add(4, "27");
        values.add(5, "m");
        System.out.println("values " + values.size());
        ArrayList types = new ArrayList(6);
        types.add(0, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        types.add(1, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        types.add(2, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        types.add(3, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        types.add(4, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        types.add(5, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));

        ObjectNode objNode = new ObjectNode("SystemObject", names, types, values);
        // ExclusionFilterServiceImpl es = new ExclusionFilterServiceImpl();
        return objNode;

    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(ExclusionFilterServiceImplTest.class));
    }

    private InputStream getConfigFileStream(String fileName)
            throws IOException {
        System.out.println("the file nem is : " + fileName);
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            System.out.println("Unable to find configuration file in classpath : " + fileName);
            throw new IOException("Unable to find configuration file in classpath : " + fileName);
        }
        return is;

    }

    public void load(InputStream fileStream) throws ParserConfigurationException,
            SAXException, ClassNotFoundException, IOException,
            InstantiationException, IllegalAccessException,
            ConfigurationException {
        // System.out.println("in load(is): ");
        Document doc;
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);

        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        org.xml.sax.InputSource input = new org.xml.sax.InputSource(fileStream);
        doc = builder.parse(input);
        parse(doc);
    }

    private void parse(Document doc) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException,
            ConfigurationException {

        //   System.out.println("in parse(): ");
        Element elem = doc.getDocumentElement();

        NodeList nl = elem.getChildNodes();

        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);

            if (n.getNodeType() == Node.ELEMENT_NODE) {
                // if it's element node, by XSD def, it must be of ModuleConfig type

                Element e = (Element) n;
                String moduleName = e.getAttribute("module-name");
                String parserClassName = e.getAttribute("parser-class");
                System.out.println("the values of moduleName  : " + moduleName + "parserClassName : " + parserClassName);
                // Class parserClass = Class.forName(parserClassName);
              //  Object obj = parserClass.newInstance();

                ExclusionFilterCofig fc = new ExclusionFilterCofig();
                fc.parse(n);

            }
        }
    }
}
