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
import com.sun.mdm.index.loader.util.Localizer;
import com.sun.mdm.index.dataobject.DataObjectFileReader;
import com.sun.mdm.index.loader.util.Localizer;
import net.java.hulp.i18n.Logger;

/**
 * LoaderConfig bulkloader configuration class
 * @version	6.0  06 May 2008
 * @author	Sujit Biswas, Charles Ye
 */
public class LoaderConfig {
	private static Logger logger = Logger.getLogger("com.sun.mdm.index.loader.config.LoaderConfig");
	private static Localizer localizer = Localizer.getInstance();
		
	private static long MB = 1024 * 1024;
	private static long KB = 1024;
	private static long WEIGHT = 4;
	private static LoaderConfig instance;
	private static String default_config_file = "conf/loader-config.xml";
	private static Lock lock = new ReentrantLock();

	private Document doc;
	private HashMap<String, String> systemProps = new HashMap<String, String>();
	private EuidGenerator euidGenerator;

	private ArrayList<String> matchFields = new ArrayList<String>();
	private ArrayList<String> matchFieldTypes = new ArrayList<String>();
	private ArrayList<BlockDefinition> blockDefinitions = new ArrayList<BlockDefinition>();
	private HashMap<String, String> euidParams = new HashMap<String, String>();

	private Double duplicateThreshold;
	private Double matchThreshold;
	private DataObjectReader dataObjectReader;
	private GenericApplicationContext context;	
	private boolean debug = Boolean.getBoolean("loader.debug");

	/**
	 * LoaderConfig construtor  
	 * @param configFile
	 */
	protected LoaderConfig(String configFile) {
		super();
		init(configFile);
	}

	/**
	 * LoaderConfig construtor
	 */
	protected LoaderConfig() {
		super();
		init(default_config_file);
	}

	/**
	 * Initialize based on bulkloader configuration file.
	 * @param filename
	 */
	private void init(String filename) {
		try {
			doc = getDocument(filename);
		} catch (Exception ex) {
			logger.severe(localizer.x("LDR025: LoaderCofig failed initialization : {0}", ex.getMessage()), ex);
		}

		initSystemProperties();
		initEuidGenerator();
		initMatchFields();
		initThreshold();
		initBlockDefinitions();
		initDataObjectReader();
	}

	/**
	 * Initializes validation rules.
	 */
	public void initValidation() {
		try {
			Node item = doc.getElementsByTagName("ValidationConfig").item(0);
			ValidationConfiguration.getInstance().parse(item);
		} catch (ConfigException cex) {
			logger.severe(localizer.x("LDR055: LoaderCofig failed to initialize validation rules : {0}", cex.getMessage()), cex);				
		}
	}

	/**
	 * Initialize thresholds
	 */
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
		} catch (XPathExpressionException ex) {
			logger.severe(localizer.x("LDR049: LoaderCofig failed to initialize thresholds : {0}", ex.getMessage()), ex);	
		}
	}

	/**
	 * Initialize EUID generator
	 */
	private void initEuidGenerator() {

		InputStream is = getClass().getClassLoader().getResourceAsStream(
				"master.xml");
		try {
			Document document = getDocument(is);
			Node item = document.getElementsByTagName("EuidGeneratorConfig").item(0);

			initLoaderEuidGeneratorClass();
			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList elements = (NodeList) xpath.evaluate("//EuidGeneratorConfig/parameters/parameter", item,
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

		} catch (Exception ex) {
			logger.severe(localizer.x("LDR026: LoaderCofig failed to initialize EuidGenerator : {0}", ex.getMessage()), ex);
		}
	}

	/**
	 * Initialize EUID generator
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
		} catch (Exception ex) {
			logger.severe(localizer.x("LDR027: LoaderCofig failed to initialize Loader EuidGenerator : {0}", ex.getMessage()), ex);			
		}
	}

	/**
	 * Initialize match fields
	 */
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

		} catch (XPathExpressionException ex) {
			logger.severe(localizer.x("LDR028: LoaderCofig failed to initialize match fields : {0}", ex.getMessage()), ex);
		}
	}

	/**
	 * Initialize dataobject reader
	 */
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

			context = new GenericApplicationContext();
			XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(
					context);
			reader.registerBeanDefinitions(targetDocument,
					new DescriptiveResource("A document"));
			context.setClassLoader(this.getClass().getClassLoader());
			context.refresh();

		} catch (Exception ex) {
			logger.severe(localizer.x("LDR029: LoaderCofig failed to initialize DataObjectReader : {0}", ex.getMessage()), ex);									
		}
	}

	/**
	 * Gets block definitions
	 * @return ArrayList<BlockDefinition>
	 */
	public ArrayList<BlockDefinition> getBlockDefinitions() {
		return blockDefinitions;
	}

	/**
	 * Sets block definitions
	 * @param blockDefinitions ArrayList<BlockDefinition>
	 */
	public void setBlockDefinitions(ArrayList<BlockDefinition> blockDefinitions) {
		this.blockDefinitions = blockDefinitions;
	}
	
	/**
	 * Initialize block definitions
	 */
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

		} catch (XPathExpressionException ex) {
			logger.severe(localizer.x("LDR030: LoaderCofig failed to initialize block definitions : {0}", ex.getMessage()), ex);									
		}

		BlockDefinitionMerger bm = new BlockDefinitionMerger(blockDefinitions);

		blockDefinitions = bm.merge();

	}

	/**
	 * Adds rule in a block definition
	 * @param b BlockDefinition
	 * @param rule Element
	 */
	private void addRules(BlockDefinition b, Element rule) {
		NodeList nlFields = rule.getElementsByTagName("field");
		NodeList nlSource = rule.getElementsByTagName("source");

		for (int i = 0; i < nlFields.getLength(); i++) {
			try {
				String s = nlFields.item(i).getTextContent();
				String s1 = nlSource.item(i).getTextContent();
				if (s.contains("Enterprise.SystemSBR.")) {
					s = s.substring("Enterprise.SystemSBR.".length());
				}
				b.addRule(s, s1);
			} catch (Exception ex) {
				logger.severe(localizer.x("LDR031: LoaderCofig failed to add rules : {0}", ex.getMessage()), ex);												
			}
		}
	}

	/**
	 * Initialize system properties
	 */
	private void initSystemProperties() {

		Element root = doc.getDocumentElement();
		Element sys = (Element) root.getElementsByTagName("system").item(0);
		Element props = (Element) sys.getElementsByTagName("properties").item(0);
		
		NodeList nl = props.getElementsByTagName("property");
		for (int i = 0; i < nl.getLength(); i++) {
			Element prop = (Element) nl.item(i);
			systemProps.put(prop.getAttribute("name"), prop.getAttribute("value"));
		}

		try {
			File f = new File("target/test-classes/loader.properties");
			if (f.exists() && f.isFile()) {
				logger.warn(localizer.x("LDR032: Running in test environment, this will override some of the systems properties which is read from : {0}", 
										f.getName()));												
				Properties p = new Properties();
				p.load(new FileInputStream(f));
				for (Object key : p.keySet()) {
					systemProps.put((String) key, (String) p.get(key));
				}
				systemProps.toString();
			}
		} catch (FileNotFoundException ex) {
			logger.severe(localizer.x("LDR033: LoaderCofig failed to initialize system properties : {0}", ex.getMessage()), ex);
		} catch (IOException ex) {
			logger.severe(localizer.x("LDR034: LoaderCofig failed to initialize system properties : {0}", ex.getMessage()), ex);
		}
	}

	/**
	 * Gets document based on the input file name.
	 * @return loader configuration document object
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Document getDocument(String filename)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		// docFactory.setValidating(true);
		DocumentBuilder builder = docFactory.newDocumentBuilder();
		return builder.parse(filename);
	}

	/**
	 * Gets document based on the input stream.
	 * @param filename
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Document getDocument(InputStream filename)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		// docFactory.setValidating(true);
		DocumentBuilder builder = docFactory.newDocumentBuilder();
		return builder.parse(filename);
	}

	/**
	 * Gets system property value for the given property name.
	 * @param propertyName
	 * @return
	 */
	public String getSystemProperty(String propertyName) {
		return systemProps.get(propertyName);
	}

	/**
	 * Sets the system property value for the given property name.
	 * @param propertyName
	 * @param propertyValue
	 */
	public void setSystemProperty(String propertyName, String propertyValue) {
		systemProps.put(propertyName, propertyValue);
	}
	
	/**
	 * Gets EUID generator
	 * @return the euidGenerator
	 */
	public EuidGenerator getEuidGenerator() {
		return euidGenerator;
	}

	/**
	 * Gets LoaderConfig instance.
	 * @return LoaderConfig
	 */
	public static LoaderConfig getInstance() {

		lock.lock();
		if (instance == null) {
			String s = System.getProperty("loader.config");
			if (s == null) {
				logger.info(localizer.x("LDR035: loader.config property is not set, the default {0} is used", default_config_file));
				instance = new LoaderConfig();
			} else {
				instance = new LoaderConfig(s);
			}
		}
		lock.unlock();

		return instance;
	}

	/**
	 * Resets LoaderConfig instance.
	 */
	public static void initInstance() {
		instance = null;
	}
	
	/**
	 * Check if optimizeDuplicates is set.
	 * @return boolean	true if optimizeDuplicates sets to be true.
	 */
	public boolean optimizeDuplicates() {
		String optimizeDuplicates = getSystemProperty("optimizeDuplicates");
		if (optimizeDuplicates == null || optimizeDuplicates.equals("true")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets Object definition.
	 * @return ObjectDefinition
	 */
	public ObjectDefinition getObjectDefinition() {

		InputStream ins = LoaderConfig.class.getClassLoader()
				.getResourceAsStream("object.xml");
		ObjectDefinitionBuilder b = new ObjectDefinitionBuilder();
		ObjectDefinition o = b.parse(ins);
		return o;
	}

	/**
	 * Gets match fields.
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getMatchFields() {
		return new ArrayList<String>(matchFields);
	}

	/**
	 * Gets match field types.
	 * @return ArrayList<String>
	 */
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

	/**
	 * Sets DataObjectReader.
	 * @param dataObjectReader
	 */
	public void setDataObjectReader(DataObjectReader dataObjectReader) {		
		this.dataObjectReader = dataObjectReader;
	}
	
	/**
	 * Gets DataObjectReader.
	 * @return DataObjectReader
	 */
	public DataObjectReader getDataObjectReader() {
		if (dataObjectReader == null) {
			dataObjectReader = (DataObjectReader) context.getBean("dataObjectReader");
		}
		return dataObjectReader;
	}

	public DataObjectReader getCustomDataObjectReader() {
		if (context.containsBeanDefinition("customDataObjectReader")) {
			dataObjectReader = (DataObjectReader) context.getBean("customDataObjectReader");
		} else {
			dataObjectReader = (DataObjectReader) context.getBean("dataObjectReader");
		}
		return dataObjectReader;
	}

	public HashMap<String, String> getEuidParams() {
		return euidParams;
	}
	
   /**
	* Validates numBlockBuckets and numEUIDBuckets 
	* @throws InvalidConfigurationException if numBlockBuckets and numEUIDBuckets are invalid. 
	*/
	public void validation() throws ConfigException {
		
		if (getSystemProperty("numBlockBuckets")== null) {
			throw new ConfigException("numBlockBuckets is not configured.");
		}
		long numBlockBuckets  = Long.parseLong(getSystemProperty("numBlockBuckets"));
		if (numBlockBuckets <= 0) {
			throw new ConfigException("numBlockBuckets is invalid.");
		}
		if (getSystemProperty("numEUIDBuckets") == null) {
			throw new ConfigException("numEUIDBuckets is not configured.");			
		}
		long numEUIDBuckets = Long.parseLong(getSystemProperty("numEUIDBuckets"));
		if (numEUIDBuckets <= 0) {
			throw new ConfigException("numEUIDBuckets is invalid");
		}
		if (getSystemProperty("totalNoOfRecords") == null) {
			throw new ConfigException("totalNoOfRecords is not configured.");						
		}
		long totalNoOfRecords = Long.parseLong(getSystemProperty("totalNoOfRecords"));
		if (totalNoOfRecords <= 0) {
			throw new ConfigException("totalNoOfRecords is invalid.");	
		}		
		if (getSystemProperty("numThreads") == null) {
			throw new ConfigException("numThreads is not configured.");						
		}
		long numThreads = Long.parseLong(getSystemProperty("numThreads"));
		if (numThreads <= 0) {
			throw new ConfigException("numThreads is invalid.");	
		}

		/* can not assume DataObjectReader is DataObjectFileReader.
		DataObjectFileReader reader = null;
		String record = null;
		String fileName = ((DataObjectFileReader)getDataObjectReader()).getFileName();
		try {
			reader = new DataObjectFileReader(fileName);
			record = reader.readRecordString();
		} catch (FileNotFoundException ex) {
			throw new ConfigException(ex);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ignore) {
				}
			}	
		}
		
		if (record == null || record.length() == 0) {
			throw new ConfigException("The records are invalid in " + fileName);
		}		
		long recordSize = record.length();
		*/
		long recordSize = 256;
		long numOfRecordsInBlockBuckets = totalNoOfRecords/numBlockBuckets;
		long blockBucketMemory = (numOfRecordsInBlockBuckets*recordSize) * WEIGHT * numThreads;
		
		long numOfRecordsInEUIDBuckets = totalNoOfRecords/numEUIDBuckets;
		long EUIDBucketMemory = (numOfRecordsInEUIDBuckets*recordSize) * WEIGHT * numThreads;
		
		logger.info(localizer.x("LDR036: The number of input records is {0}", totalNoOfRecords));
		logger.info(localizer.x("LDR037: The size of a record is {0}", display(recordSize)));
				
		logger.info(localizer.x("LDR038: The number of block buckets is {0}", numBlockBuckets));
		logger.info(localizer.x("LDR039: The number of records in a block bucket is {0}", numOfRecordsInBlockBuckets));
		logger.info(localizer.x("LDR040: The memory size for a block bucket is {0}", display(blockBucketMemory)));
			
		logger.info(localizer.x("LDR041: The number of EUID buckets is {0}", numEUIDBuckets));
		logger.info(localizer.x("LDR042: The number of records in a EUID bucket is {0}", numOfRecordsInEUIDBuckets));
		logger.info(localizer.x("LDR043: The memory size for a EUID bucket is {0}", display(EUIDBucketMemory)));
		
		Runtime rt = Runtime.getRuntime();
		long maxMemory = rt.maxMemory() - rt.totalMemory(); 
		
		logger.info(localizer.x("LDR044: The maximum available JVM heap size is {0}", display(maxMemory)));	
		maxMemory = maxMemory - (200 * MB);
		if (maxMemory < 0) {
			throw new ConfigException("The JVM maximum heap size is not large enough to run the loader, please raise it.");	
		}

		logger.info(localizer.x("LDR045: The maximum heap size that can be used for caching buckets is {0}", display(maxMemory)));
		if (blockBucketMemory > (maxMemory/WEIGHT)) {
			long numBucketRecords = (maxMemory/recordSize)/(WEIGHT*WEIGHT);
			numBlockBuckets = (totalNoOfRecords/numBucketRecords)*WEIGHT;
			throw new ConfigException("numBlockBuckets is too small, please rasie it and suggest at least large than " + numBlockBuckets);				
		}
		
		if (EUIDBucketMemory > (maxMemory/WEIGHT)) {
			long numEUIDBucketRecords = (maxMemory/recordSize)/(WEIGHT*WEIGHT);
			numEUIDBuckets = (totalNoOfRecords/numEUIDBucketRecords)*WEIGHT;	
			throw new ConfigException("numEUIDBuckets is too small, please raise it and suggest at least large than " + numEUIDBuckets);				
		}
		
		/*
		if (getSystemProperty("matchCacheSize") == null) {
			throw new ConfigException("matcheCacheSize is not configured.");						
		}
		
		long matchCacheSize = Long.parseLong(getSystemProperty("matchCacheSize"));
	    if (matchCacheSize <= 0) {
			throw new ConfigException("matcheCacheSize is invalid.");			    	
	    }
		
		if (matchCacheSize > (maxMemory/(WEIGHT*WEIGHT))) {
			matchCacheSize = (maxMemory/(WEIGHT*WEIGHT))/48;
			throw new ConfigException("matcheCacheSize is too large and suggest less than " + matchCacheSize);			    	    	
	    }
	    */
	}
	
	/**
	 * Returns string expressed in BYTES, KB or MB.
	 * @param value
	 * @return String 
	 */
	private String display(long value) {
		String stringValue = null;
		if (value > MB) {	
			stringValue = new String("" + (value/MB) +"MB");		
		} else if (value > KB) {
			stringValue= new String("" + (value/KB) +"KB");				
		} else {
			stringValue = new String("" + value + "BYTES");	
		}	
		return stringValue;
	}
	
}
