/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.jdom.Attribute;
import org.jdom.DocType;
import org.jdom.JDOMException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Attribute;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;

/**
 *
 * @author wee
 */

public class XMLWriterUtil {
      
    //public static Logger log = Logger.getLogger (XMLUtil.class);
    public Document doc;
    public Element root;
    
    /** Creates a new instance of XMLWriteUtil */    
    public XMLWriterUtil() {
    } 
    
    public void setRoot(String element) throws JDOMException {
       root = new Element(element);
       doc = new Document(root);
    }

    
    public void setRoot(String element, DocType docType) throws JDOMException {
       root = new Element(element);
       doc = new Document(root, docType);
    }
    
    public void setRoot(String element, String namespace, String schemaLoc) throws JDOMException {
        Namespace xsiNS =
                Namespace.getNamespace("xsi",
                "http://www.w3.org/2001/XMLSchema-instance");
        root = new Element(element);
        root.setAttribute(new Attribute("noNamespaceSchemaLocation", schemaLoc, xsiNS));
        //root.setAttributeNS("noschemaLocation", "xsi", schemaLoc);
        doc = new Document(root);
    }
    
    
    
    public void setElement(String elem) throws Exception {
        Element element = new Element(elem);
        element.setText("");
        root.addContent(element);
    }
    
    public void setElement(Element parent, String elem) throws Exception {
        Element element = new Element(elem);
        element.setText("");
        parent.addContent(element);

    }
    
    public void setElement(String elem, String text) throws Exception {
	if (text == null ) {
	    this.setElement(elem);
	} else {
            Element element = new Element(elem);
            element.setText(text);
            root.addContent(element);
	}
    }
    
    public Element addElement(String elm) throws Exception {
        Element element = new Element (elm);
        root.addContent(element);
        return element;
    }

    public Element addElement(Element parent, String elm) throws Exception {
        Element element = new Element (elm);
        parent.addContent(element);
        return element;
    }
    
     public void setElement(Element parent, String elem, String text) throws Exception {
	if (text == null ) {
	    this.setElement(elem);
	} else {
            Element element = new Element(elem);
            element.setText(text);
            parent.addContent(element);
	}
    }   

    public void setElement(Element parent, String elem, int intValue) throws Exception {
        String text = Integer.toString(intValue);
        if (text == null) {
            this.setElement(elem);
        } else {
            Element element = new Element(elem);
            element.setText(text);
            parent.addContent(element);
        }
    }
    
    public byte[] getXMLStream() throws IOException {   
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLOutputter serializer = new XMLOutputter();
        serializer.setFormat(Format.getPrettyFormat());
        serializer.output(doc, out);
        out.flush();
        out.close();
        return out.toByteArray();
    }
    
    
   
}
