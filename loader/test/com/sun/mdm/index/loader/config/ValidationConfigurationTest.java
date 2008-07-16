package com.sun.mdm.index.loader.config;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.sun.mdm.index.dataobject.validation.ValidationRuleRegistry;
import com.sun.mdm.index.objects.validation.ObjectValidator;
import com.sun.mdm.index.objects.validation.ObjectDescriptor;

import junit.framework.TestCase;

public class ValidationConfigurationTest extends TestCase {

	private static String default_config_file = "C:/MDM/open-dm-mi/loader/conf/loader-config.xml";
	
	/**
	 * @param name
	 */
	public ValidationConfigurationTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
		
	private Element getDocument(String filename)
		throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		// docFactory.setValidating(true);
		DocumentBuilder builder = docFactory.newDocumentBuilder();
		return builder.parse(filename).getDocumentElement();
	}
	
	public void test001() {
		try {
			Element doc = getDocument(default_config_file);
			Node item = doc.getElementsByTagName("ValidationConfig").item(0);
			ValidationConfiguration configuration = ValidationConfiguration.getInstance();
			configuration.parse(item);
			
			ValidationRuleRegistry registry = ValidationRuleRegistry.getInstance();
		
			assertTrue(configuration.getValidationEnabled() == false);
			
			ObjectValidator localIdValidator = registry.getObjectValidator("SystemObject");
			
			ObjectDescriptor ObjectDescriptor1 = registry.getObjectDescriptor("Person");
			ObjectDescriptor ObjectDescriptor2 = registry.getObjectDescriptor("Alias");
			ObjectDescriptor ObjectDescriptor3 = registry.getObjectDescriptor("Phone");
						
			assertTrue(true);
		} catch (Exception ex) {
			assertTrue(false);
		}		
	}
}
