/**
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
package com.sun.mdm.index.loader.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.DescriptiveResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.mdm.index.Resource;
import com.sun.mdm.index.dataobject.DataObjectReader;
import com.sun.mdm.index.dataobject.objectdef.ObjectDefinition;
import com.sun.mdm.index.dataobject.objectdef.ObjectDefinitionBuilder;
import com.sun.mdm.index.idgen.EuidGenerator;
import com.sun.mdm.index.loader.blocker.BlockDefinition;
import com.sun.mdm.index.loader.euid.LoaderEuidGenerator;

/**
 * @author Sujit Biswas
 * 
 */
public class LoaderConfig {

	private static Logger logger = Logger.getLogger(LoaderConfig.class
			.getName(), Resource.BUNDLE_NAME);

	private static LoaderConfig instance;

	/**
	 * the loader configuration document object
	 */
	private Document doc;

	private HashMap<String, String> systemProps = new HashMap<String, String>();

	private EuidGenerator euidGenerator;

	private ArrayList<String> matchFields = new ArrayList<String>();
	private ArrayList<String> matchFieldTypes = new ArrayList<String>();
	ArrayList<BlockDefinition> blockDefinitions = new ArrayList<BlockDefinition>();

	private Double duplicateThreshold;
	private Double matchThreshold;
	private DataObjectReader dataObjectReader;

	/**
	 * 
	 */
	boolean debug = Boolean.getBoolean("loader.debug");

	private static String default_config_file = "conf/loader-config.xml";

	protected LoaderConfig(String configFile) {
		super();
		init(configFile);
	}

	protected LoaderConfig() {
		super();
		// TODO read from system property
		init(default_config_file);
	}

	private void init(String filename) {
		try {
			doc = getDocument(filename);
		} catch (Exception e) {
			e.printStackTrace();

			logger.log(Level.CONFIG, e.getMessage());
		}

		initSystemProperties();

		initEuidGenerator();

		initMatchFields();

		initThreshold();

		initBlockDefinitions();

		initDataObjectReader();
	}

	private void initThreshold() {
		try {
			Node item = doc.getElementsByTagName("threshold-config").item(0);

			XPath xpath = XPathFactory.newInstance().newXPath();

			String element = (String) xpath.evaluate("//duplicateThreshold",
					item, XPathConstants.STRING);

			duplicateThreshold = Double.valueOf(element);

			element = (String) xpath.evaluate("//matchThreshold", item,
					XPathConstants.STRING);

			matchThreshold = Double.valueOf(element);

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	HashMap<String, String> euidParams = new HashMap<String, String>();

	private void initEuidGenerator() {

		InputStream is = getClass().getClassLoader().getResourceAsStream(
				"master.xml");

		try {
			Document document = getDocument(is);

			Node item = document.getElementsByTagName("EuidGeneratorConfig")
					.item(0);

			// TODO get it from config
			initLoaderEuidGeneratorClass();

			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList elements = (NodeList) xpath.evaluate(
					"//EuidGeneratorConfig/parameters/parameter", item,
					XPathConstants.NODESET);

			for (int i = 0; i < elements.getLength(); i++) {
				Element e = (Element) elements.item(i);

				String name = e.getElementsByTagName("parameter-name").item(0)
						.getTextContent();
				String value = e.getElementsByTagName("parameter-value")
						.item(0).getTextContent();

				euidParams.put(name, value);
				euidGenerator.setParameter(name, Integer.valueOf(value));

			}

		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

	/**
	 * 
	 */
	private void initLoaderEuidGeneratorClass() {

		Node item = doc.getElementsByTagName("EuidGenerator").item(0);

		try {
			if (item != null) {
				String className = ((Element) item).getAttribute("class");

				euidGenerator = (EuidGenerator) Class.forName(className)
						.newInstance();
			} else {
				euidGenerator = new LoaderEuidGenerator();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initMatchFields() {
		try {
			Node item = doc.getElementsByTagName("MatchingConfig").item(0);

			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList elements = (NodeList) xpath
					.evaluate(
							"//match-system-object/match-columns/match-column/column-name",
							item, XPathConstants.NODESET);

			for (int i = 0; i < elements.getLength(); i++) {
				String s = elements.item(i).getFirstChild().getNodeValue();

				if (s.contains("Enterprise.SystemSBR."))
					s = s.substring("Enterprise.SystemSBR.".length());
				matchFields.add(s);
			}

			elements = (NodeList) xpath
					.evaluate(
							"//match-system-object/match-columns/match-column/match-type",
							item, XPathConstants.NODESET);

			for (int i = 0; i < elements.getLength(); i++) {
				String s = elements.item(i).getFirstChild().getNodeValue();
				matchFieldTypes.add(s);
			}

		} catch (XPathExpressionException e) {
			logger.info(e.getMessage());
		}
	}

	private GenericApplicationContext context;

	private void initDataObjectReader() {

		try {

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();

			String targetXmlString = "<?xml version='1.0' encoding='UTF-8'?>"
					+ "<!DOCTYPE beans>" + "<beans/>";
			Document targetDocument = documentBuilder.parse(new InputSource(
					new StringReader(targetXmlString)));

			NodeList beanList = doc.getElementsByTagName("beans").item(0)
					.getChildNodes();
			for (int i = 0; i < beanList.getLength(); i++) {
				Node node = targetDocument.importNode(beanList.item(i), true);
				targetDocument.getDocumentElement().appendChild(node);
			}

			// TransformerFactory transformerFactory =
			// TransformerFactory.newInstance();

			// Transformer identityTransformer =
			// transformerFactory.newTransformer();
			// identityTransformer.transform(new DOMSource(targetDocument), new
			// StreamResult(System.out));

			context = new GenericApplicationContext();
			XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(
					context);
			reader.registerBeanDefinitions(targetDocument,
					new DescriptiveResource("A document"));

			context.setClassLoader(this.getClass().getClassLoader());
			context.refresh();

			// dataObjectReader = (DataObjectReader)
			// context.getBean("dataObjectReader");

			// dataObjectReader = (DataObjectReader)
			// context.getBean("dataObjectReader");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<BlockDefinition> getBlockDefinitions() {
		return blockDefinitions;
	}

	private void initBlockDefinitions() {
		try {
			Node item = doc.getElementsByTagName("query-builder").item(0);

			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList elements = (NodeList) xpath.evaluate(
					"//config/block-definition", item, XPathConstants.NODESET);

			for (int i = 0; i < elements.getLength(); i++) {
				Element e = (Element) elements.item(i);

				BlockDefinition b = new BlockDefinition();

				b.setId(e.getAttribute("number"));

				NodeList nl = e.getElementsByTagName("block-rule");

				for (int j = 0; j < nl.getLength(); j++) {
					Element rule = (Element) nl.item(j);
					if (rule != null)
						addRules(b, rule);
				}

				blockDefinitions.add(b);

			}

		} catch (XPathExpressionException e) {
			logger.info(e.getMessage());
		}

		BlockDefinitionMerger bm = new BlockDefinitionMerger(blockDefinitions);

		blockDefinitions = bm.merge();

	}

	private void addRules(BlockDefinition b, Element rule) {
		NodeList nlFields = rule.getElementsByTagName("field");
		NodeList nlSource = rule.getElementsByTagName("source");

		for (int i = 0; i < nlFields.getLength(); i++) {

			try {

				String s = nlFields.item(i).getTextContent();
				String s1 = nlSource.item(i).getTextContent();

				if (s.contains("Enterprise.SystemSBR."))
					s = s.substring("Enterprise.SystemSBR.".length());

				b.addRule(s, s1);
			} catch (Exception e) {
				logger.info(e.getMessage());
			}

		}

	}

	private void initSystemProperties() {

		Element root = doc.getDocumentElement();

		Element sys = (Element) root.getElementsByTagName("system").item(0);

		Element props = (Element) sys.getElementsByTagName("properties")
				.item(0);

		NodeList nl = props.getElementsByTagName("property");

		for (int i = 0; i < nl.getLength(); i++) {

			Element prop = (Element) nl.item(i);

			systemProps.put(prop.getAttribute("name"), prop
					.getAttribute("value"));

		}

		try {
			File f = new File("target/test-classes/loader.properties");
			if (f.exists() && f.isFile()) {

				logger
						.warning("running in test environment, this will override some of the systems properties which is read from the loader config.xml");
				Properties p = new Properties();
				p.load(new FileInputStream(f));

				for (Object key : p.keySet()) {
					systemProps.put((String) key, (String) p.get(key));
				}
				systemProps.toString();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @return loader configuration document object
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Document getDocument(String filename)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		// docFactory.setValidating(true);

		DocumentBuilder builder = docFactory.newDocumentBuilder();

		return builder.parse(filename);
	}

	private Document getDocument(InputStream filename)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		// docFactory.setValidating(true);

		DocumentBuilder builder = docFactory.newDocumentBuilder();

		return builder.parse(filename);
	}

	public String getSystemProperty(String propertyName) {
		return systemProps.get(propertyName);
	}

	/**
	 * @return the euidGenerator
	 */
	public EuidGenerator getEuidGenerator() {
		return euidGenerator;
	}

	private static Lock lock = new ReentrantLock();

	public static LoaderConfig getInstance() {

		lock.lock();
		if (instance == null) {

			String s = System.getProperty("loader.config");
			if (s == null) {
				logger.log(Level.INFO, "loader_config", default_config_file);
				instance = new LoaderConfig();
			} else {
				instance = new LoaderConfig(s);
			}
		}
		lock.unlock();

		return instance;
	}

	public ObjectDefinition getObjectDefinition() {

		InputStream ins = LoaderConfig.class.getClassLoader()
				.getResourceAsStream("object.xml");

		ObjectDefinitionBuilder b = new ObjectDefinitionBuilder();

		ObjectDefinition o = b.parse(ins);

		return o;

	}

	public ArrayList<String> getMatchFields() {
		return new ArrayList<String>(matchFields);
	}

	public ArrayList<String> getMatchFieldTypes() {
		return matchFieldTypes;
	}

	public static void main(String[] args) {
		LoaderConfig lc = LoaderConfig.getInstance();

		System.out.println(lc.getMatchFields());

		System.out.println(lc.getMatchFieldTypes());

		lc.getSystemProperty("cluster.database");

		lc.getBlockDefinitions();

		lc.getDataObjectReader();

		lc.getCustomDataObjectReader();

		System.out.println(lc.getObjectDefinition());

		lc.getEuidGenerator();
	}

	public double getDuplicateThreshold() {
		return duplicateThreshold;
	}

	public double getMatchThreshold() {
		return matchThreshold;
	}

	public String getWorkingDir() {
		return this.getSystemProperty("workingDir");

	}

	public DataObjectReader getDataObjectReader() {
		dataObjectReader = (DataObjectReader) context
				.getBean("dataObjectReader");
		return dataObjectReader;
	}

	public DataObjectReader getCustomDataObjectReader() {

		if (context.containsBeanDefinition("customDataObjectReader")) {
			dataObjectReader = (DataObjectReader) context
					.getBean("customDataObjectReader");
		} else {
			dataObjectReader = (DataObjectReader) context
					.getBean("dataObjectReader");
		}
		return dataObjectReader;
	}

	public HashMap<String, String> getEuidParams() {
		return euidParams;
	}

}
