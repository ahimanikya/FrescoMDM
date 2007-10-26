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
package com.sun.mdm.index.report.client;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

/**
 * @author jwu
 * @version
 */
public class ReportSaxReader extends DefaultHandler {

    static final String JAXP_SCHEMA_LANGUAGE =
        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    static final String W3C_XML_SCHEMA =
        "http://www.w3.org/2001/XMLSchema";
    
    private static final String PATH_SEPEARTOR = ".";

    private SAXParser mParser = null;
    private File mFile = null;
    private boolean bTopNode = true;
    private final HashMap mHandlerMap;
    private final Stack mPathStack;
    // logger
    private final Logger mLogger = LogUtil.getLogger(this);
    private String mPath;
    private String mXmlFile = null;
    
    /** Creates a new instance of SaxParser */
    public ReportSaxReader() {
        mPathStack = new Stack();
        mHandlerMap = new HashMap();
        mPath = "";

        try {
            initParser();
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param l document locator
     */
    public void setDocumentLocator(Locator l) {
    }

    /**
     * Set the Input xml file 
     *
     * @param sFileName input xml file path
     * @exception Exception File not found, Cannot not read file
     */
    public void setInputFile(String sFileName) throws Exception {
        mFile = new File(sFileName);
        if (!mFile.exists()) {
            mFile = null;
            throw new Exception(sFileName + " not found");
        }

        if (!mFile.canRead()) {
            mFile = null;
            throw new Exception("Cannot read " + sFileName);
        }
        mXmlFile = sFileName;
    }

    /**
     * Adds a feature to the Handler attribute of the DbSaxReader object
     *
     * @param sTag The feature to be added to the Handler attribute
     * @param handler The feature to be added to the Handler attribute
     */
    public void addHandler(String sTag, ElementReader handler) {
        mHandlerMap.put(sTag, handler);
    }

    /**
     *
     * @param buf the buffer that contains the data
     * @param offset index of the starting character
     * @param len length of the character string
     * @exception SAXException exceptions thrown by SAXParser
     */
    public void characters(char buf[], int offset, int len) 
        throws SAXException {

        String sData = new String(buf, offset, len);

        if (!sData.trim().equals("")) {
            ElementReader handler = getHandler(mPath);
            if (handler != null) {
                handler.onData(lastPathNode(mPath), sData);
            }
        }
    }

    /**
     *
     * @exception SAXException exceptions thrown by SAXParser
     */
    public void endDocument() throws SAXException {
    }

    /**
     *
     * @param namespaceURI NamespaceURI
     * @param sName local name
     * @param qName qualified name
     * @exception SAXException exceptions thrown by SAXParser
     */
    public void endElement(String namespaceURI, String sName, String qName) 
        throws SAXException {

        String sPath = (String) mPathStack.pop();
        ElementReader handler = getHandler(sPath);
        if (handler != null) {
            handler.onEndElement(sPath);
        }
        if (!mPathStack.empty()) {
            mPath = (String) mPathStack.peek();
        }
    }

    /**
     *
     * @param sPath node path 
     * @return Node name of the specified node path
     */
    public String lastPathNode(String sPath) {
        String sNodeName;
        int idx = sPath.lastIndexOf(PATH_SEPEARTOR);
        if (idx < 0) {
            sNodeName = sPath;
        } else {
            sNodeName = sPath.substring(idx + 1);
        }
        return sNodeName;
    }

    private String wildPath(String sPath) {
        String sNewPath;
        int idx = sPath.lastIndexOf(PATH_SEPEARTOR);
        if (idx < 0) {
            sNewPath = sPath;
        } else {
            sNewPath = sPath.substring(0, idx) + ".*";
        }
        return sNewPath;
    }

    /**
     * @todo Document this method
     *
     * @exception Exception Parser not yet initialized, 
     *  Inpu XML file has not yet been specified, 
     *  Element handler has not yet been setup
     */
    public void parse() throws ConfigurationException {
        if (mParser == null) {
            throw new ConfigurationException("Parser not yet initialized");
        }
        if (mFile == null) {
            throw new ConfigurationException("Inpu XML file has not yet been specified");
        }
        if (mHandlerMap.size() == 0) {
            throw new ConfigurationException("Element handlers have not been setup");
        }
        
        try {
            mParser.parse(mFile, this);
        } catch (SAXException e) {
            throw new ConfigurationException("Error in " + mXmlFile + ": " + e.getMessage());
        } catch (IOException ioe) {
            throw new ConfigurationException("Error in " + mXmlFile + ": " + ioe.getMessage());
        }
    }

    /**
     * @todo Document this method
     *
     * @param target target
     * @param data data
     * @exception SAXException exceptions thrown by SAXParser
     */
    public void processingInstruction(String target, String data) 
        throws SAXException {
    }

    /**
     *
     * @exception SAXException exceptions thrown by SAXParser
     */
    public void startDocument() 
        throws SAXException {
    }

    /**
     *
     * @param namespaceURI NamespaceURI
     * @param lName local name
     * @param qName qualified name
     * @param attrs attributes
     * @exception SAXException exceptions thrown by SAXParser
     */
    public void startElement(String namespaceURI, String lName, 
            String qName, Attributes attrs)
             throws SAXException {

        String eName = ("".equals(lName) ? qName : lName);
        if (bTopNode) {
            mPath = eName;
            bTopNode = false;
        } else {
            mPath = mPath + PATH_SEPEARTOR + eName;
        }
        mPathStack.push(mPath);
        ElementReader handler = getHandler(mPath);
        if (handler != null) {
            handler.onStartElement(eName, attrs);
        }
        if (attrs == null || attrs.getLength() == 0) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < attrs.getLength(); i++) {
            String aName = attrs.getLocalName(i);
            // Attr name
            if ("".equals(aName)) {
                aName = attrs.getQName(i);
            }
            sb.append(aName + " : " + attrs.getValue(i) + ", ");
        }
    }

    public void error(SAXParseException e) {
    }
    
    public void fatalError(SAXParseException e) {
    }
    
    public void warning(SAXParseException e) {
    }
    
    private ElementReader getHandler(String sTag) {
        ElementReader handler = (ElementReader) mHandlerMap.get(sTag);
        if (handler == null) {
            handler = getHandlerWild(wildPath(sTag));
        }
        return handler;
    }

    private ElementReader getHandlerWild(String sTag) {
        ElementReader handler = (ElementReader) mHandlerMap.get(sTag);
        if (handler == null) {
            ;
        }
        return handler;
    }

    private void initParser() throws ConfigurationException {

        SAXParserFactory factory = SAXParserFactory.newInstance();

        //factory.setValidating(factory.isValidating());
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        try {
            mParser = factory.newSAXParser();
        } catch (SAXException se) {
            Exception x = se;
            if (se.getException() != null) {
                x = se.getException();
            }
            mLogger.error(x.getMessage(), x);
        } catch (ParserConfigurationException pce) {
            mLogger.error(pce.getMessage(), pce);
        }
    }
}
