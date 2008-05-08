package com.sun.mdm.index.loader.blocker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.dataobject.objectdef.Field;
import com.sun.mdm.index.dataobject.objectdef.Lookup;
import com.sun.mdm.index.dataobject.objectdef.ObjectDefinition;
import com.sun.mdm.index.dataobject.objectdef.ObjectDefinitionBuilder;
import com.sun.mdm.index.loader.common.FileManager;
import com.sun.mdm.index.loader.common.ObjectNodeUtil;
import com.sun.mdm.index.loader.config.LoaderConfig;

import com.sun.mdm.index.loader.blocker.Validator;
import com.sun.mdm.index.loader.blocker.DataObject;
import com.sun.mdm.index.loader.blocker.DataObjectReader;
import com.sun.mdm.index.loader.blocker.DataObjectFileReader;
import com.sun.mdm.index.loader.blocker.BlockDefinition.Operator;

import com.sun.mdm.index.loader.blocker.BlockDistributor;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.support.GenericApplicationContext;

public class BlockDistributorTest extends TestCase {
	private LoaderConfig config;
	private String[] matchPaths_;
	private String[] sbrmatchPaths_;
	private String[] matchTypes_;
	private Lookup inputLookup_;
	private Lookup blockLk_;
	private Lookup sbrblockLk_;
	private Lookup sbrLookup_;
	private String workingDir;
	private ObjectDefinition inputobd_;
	
	/*
	 * @param name
	 */
	public BlockDistributorTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		init();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	private void init() throws Exception {
		
		workingDir = System.getProperty("user.dir");
		System.out.println("user.dir=" + workingDir);		
		if (System.getProperty("loader.config") == null) {
			String configFilePath = System.getProperty("user.dir") + 
									File.separatorChar + "test" + 
									File.separatorChar + "config" + 
									File.separatorChar + "loader-config.xml";
			System.setProperty("loader.config", configFilePath);
		}
		
		System.out.println("loader.config=" + System.getProperty("loader.config"));	
		
		/*
		if (System.getProperty("bbe.home") == null) {
			System.setProperty("bbe.home", System.getProperty("user.dir") + File.separatorChar + "test");
		}
		
		System.out.println("bbe.home=" + System.getProperty("bbe.home"));
		*/
		
		//Load configuration
		LoaderConfig.initInstance();
		config = LoaderConfig.getInstance();

		String loaderName = config.getSystemProperty("loaderName");
		String workingDir = config.getSystemProperty("workingDir");
		String sdelInterMediateDir = config.getSystemProperty("deleteIntermediateDirs");
		boolean delInterMediateDir = true;		
		if (sdelInterMediateDir != null) {
			delInterMediateDir = Boolean.parseBoolean(sdelInterMediateDir);
		}
		
		{   //Initialize file manager
			FileManager.setWorkingDir(workingDir, loaderName, delInterMediateDir);
			
			//Clean up working directories
			FileManager.deleteBlockDir(true);
			FileManager.deleteMatchDir(true);
			FileManager.deleteEUIDDir(true);
			FileManager.deleteMasterIndexDir(true);
			FileManager.deleteSBRBlockDir(true);
			FileManager.deleteSBRMatchDir(true);
			FileManager.deleteSBRInputDir(true);
		
			//Initialize working directories 
			FileManager.initDirs();			
		}
	
		List<String> matchlPaths = config.getMatchFields();	
		String[] matchPaths = new String[matchlPaths.size()];
		matchlPaths.toArray(matchPaths);
		
		matchTypes_ = readMatchTypes();
		matchPaths_ = addExtraBucketFields(matchPaths);
		sbrmatchPaths_ = addExtraSBRFields(matchPaths);				
		
		//Initialize lookup
		ObjectDefinition obd = ObjectDefinitionBuilder.buildObjectDefinition(matchPaths_);
		blockLk_ = Lookup.createLookup(obd);
		
		obd = ObjectDefinitionBuilder.buildObjectDefinition(sbrmatchPaths_);
		sbrblockLk_ = Lookup.createLookup(obd);		
		inputLookup_ = getInputLookup();		
		sbrLookup_ = getSBRLookup();	

		// Initialize configuration service  
		ConfigurationService.getInstance();	
		
		// initialize data object adapter 
		ObjectNodeUtil.initDataObjectAdapter();	 
		
	}
	
	private void setDataObjectReader(String inputDataFileName){
		
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.
										rootBeanDefinition("com.sun.mdm.index.dataobject.DataObjectFileReader");
		
		String value1 = workingDir = workingDir 
     					+ File.separatorChar + "test"		
				     	+ File.separatorChar + "data"
				     	+ File.separatorChar + "loader" 
				     	+ File.separatorChar + inputDataFileName;				
		builder.addConstructorArg(value1);
		Boolean value2 = new Boolean("false");
		builder.addConstructorArg(value2);
		
		System.out.println("inputdata=" + value1);
		
		GenericApplicationContext context = new GenericApplicationContext();
		context.registerBeanDefinition("dataObjectReader", builder.getBeanDefinition());
		
		com.sun.mdm.index.dataobject.DataObjectReader
			dataObjectReader = (com.sun.mdm.index.dataobject.DataObjectReader) context.getBean("dataObjectReader");
		config.setDataObjectReader(dataObjectReader);		
	}
	
	private String[] readMatchTypes() {
		List<String> matchlTypes = config.getMatchFieldTypes();
		String[] matchTypes = new String[matchlTypes.size()];
		matchlTypes.toArray(matchTypes);
		
		return matchTypes;
	}
	
	private String[] addExtraBucketFields(String[] paths) {
		int size = paths.length + 4; 
		String[] blockEpaths = new String[size];
		int index = paths[0].indexOf(".");
		
		String primaryObject = null;
		
		if (index > 0) {
		   primaryObject = paths[0].substring(0, index);	
		}
		
		blockEpaths[0] = primaryObject + ".blockID";
		blockEpaths[1] = primaryObject + ".GID";
		blockEpaths[2] = primaryObject + ".systemcode";		
		blockEpaths[3] = primaryObject + ".lid";
		
		System.arraycopy(paths, 0, blockEpaths, 4, paths.length);		
		return blockEpaths;
	}
	
	private String[] addExtraSBRFields(String[] paths) {
		int size = paths.length + 2; 
		String[] blockEpaths = new String[size];
		int index = paths[0].indexOf(".");
		
		String primaryObject = null;		
		if (index > 0) {
		   primaryObject = paths[0].substring(0, index);	
		}
		
		blockEpaths[0] = primaryObject + ".blockID";
		blockEpaths[1] = primaryObject + ".EUID";
			
		System.arraycopy(paths, 0, blockEpaths, 2, paths.length);		
		return blockEpaths;
	}
	
	private Lookup getInputLookup() 
		throws Exception {
		ObjectDefinition obd = config.getObjectDefinition();
		Field f = new Field();
		f.setName("GID");
		obd.addField(0,f);
		
				
		f = new Field();
		f.setName("systemcode");
		obd.addField(1,f);
		
		f = new Field();
		f.setName("lid");
		obd.addField(2,f);
		
		f = new Field();
		f.setName("updateDateTime");
		obd.addField(3,f);
		
		f = new Field();
		f.setName("createUser");
		obd.addField(4,f);
	
		inputobd_ = obd;

		Lookup lookup = Lookup.createLookup(obd);
		return lookup;
	}
	
	private Lookup getSBRLookup() throws Exception {
		ObjectDefinition obd = config.getObjectDefinition();
		Field f = new Field();
		f.setName("EUID");
		obd.addField(0,f);
		
		Lookup lookup = Lookup.createLookup(obd);
		return lookup;
	}
	
	public void testdummmy(){
		
	}
	
	public void test0(){
		try {			
			setDataObjectReader("inputdata1.txt");
			
			ArrayList<BlockDefinition> blockDefinitions = config.getBlockDefinitions();
			blockDefinitions.clear();
						
			BlockDistributor blockDistributor = new BlockDistributor(matchPaths_, inputLookup_,inputobd_, blockLk_, false);	
			blockDistributor.distributeBlocks();
			
			String blockFileName = config.getSystemProperty("workingDir") + File.separatorChar 
							  	   + "block" + File.separatorChar
							  	   + "BlockB_1.txt";
			DataObject expected[] = new DataObject[] {new DataObject("Systemlid:CMH0000000018|1|CMH|0000000018"),
													  new DataObject()};
			DataObjectReader sources = new DataObjectFileReader(blockFileName);
			
			assertTrue(Validator.validation(sources, expected));
			
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);			
		}
	}
	
	public void test1(){
		try {
			setDataObjectReader("inputdata1.txt");
			
			ArrayList<BlockDefinition> blockDefinitions = config.getBlockDefinitions();
			blockDefinitions.clear();
			
			BlockDefinition blockDefinition = new BlockDefinition();
			blockDefinition.setId("ID0");
			blockDefinition.addRule("Person.FirstName_Phon");
			blockDefinitions.add(blockDefinition);
			config.setBlockDefinitions(blockDefinitions);
			
			BlockDistributor blockDistributor = new BlockDistributor(matchPaths_, inputLookup_, inputobd_, blockLk_, false);	
			blockDistributor.distributeBlocks();
			
			String blockFileName = config.getSystemProperty("workingDir") + File.separatorChar 
							  	   + "block" + File.separatorChar
							  	   + "BlockB_1.txt";
			DataObject expected[] = new DataObject[] {new DataObject("ID0:E421|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
											  		  new DataObject("Systemlid:CMH0000000018|1|CMH|0000000018"),
											  		  new DataObject()};
			DataObjectReader sources = new DataObjectFileReader(blockFileName);
			
			assertTrue(Validator.validation(sources, expected));
			
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);			
		}
	}
	
	public void test2(){
		try {
			setDataObjectReader("inputdata1.txt");
			
			ArrayList<BlockDefinition> blockDefinitions = config
					.getBlockDefinitions();
			blockDefinitions.clear();

			BlockDefinition blockDefinition = new BlockDefinition();
			blockDefinition.setId("ID1");
			blockDefinition.addRule("Person.LastName_Phon");
			blockDefinitions.add(blockDefinition);
			config.setBlockDefinitions(blockDefinitions);

			BlockDistributor blockDistributor = new BlockDistributor(
					matchPaths_, inputLookup_, inputobd_, blockLk_, false);
			blockDistributor.distributeBlocks();

			String blockFileName = config.getSystemProperty("workingDir")
					+ File.separatorChar + "block" + File.separatorChar
					+ "BlockB_1.txt";
			DataObject expected[] = new DataObject[] {
					new DataObject("ID1:FAL|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("Systemlid:CMH0000000018|1|CMH|0000000018"),
					new DataObject() };
			DataObjectReader sources = new DataObjectFileReader(blockFileName);

			assertTrue(Validator.validation(sources, expected));
		
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);			
		}		
	}
	
	public void test3(){
		try {
			setDataObjectReader("inputdata1.txt");
			
			ArrayList<BlockDefinition> blockDefinitions = config
					.getBlockDefinitions();
			blockDefinitions.clear();

			BlockDefinition blockDefinition = new BlockDefinition();
			blockDefinition.setId("ID2");
			blockDefinition.addRule("Person.SSN");
			blockDefinitions.add(blockDefinition);
			config.setBlockDefinitions(blockDefinitions);

			BlockDistributor blockDistributor = new BlockDistributor(
					matchPaths_, inputLookup_, inputobd_, blockLk_, false);
			blockDistributor.distributeBlocks();

			String blockFileName = config.getSystemProperty("workingDir")
					+ File.separatorChar + "block" + File.separatorChar
					+ "BlockB_1.txt";
			DataObject expected[] = new DataObject[] {
					new DataObject("ID2:482-76-0425|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("Systemlid:CMH0000000018|1|CMH|0000000018"),
					new DataObject() };
			DataObjectReader sources = new DataObjectFileReader(blockFileName);

			assertTrue(Validator.validation(sources, expected));
		
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);			
		}				
	}
	
	public void test4(){
		try {
			setDataObjectReader("inputdata1.txt");
			
			ArrayList<BlockDefinition> blockDefinitions = config
					.getBlockDefinitions();
			blockDefinitions.clear();

			BlockDefinition blockDefinition0 = new BlockDefinition();
			blockDefinition0.setId("ID0");
			blockDefinition0.addRule("Person.FirstName_Phon");
			blockDefinitions.add(blockDefinition0);
			
			BlockDefinition blockDefinition1 = new BlockDefinition();
			blockDefinition1.setId("ID1");
			blockDefinition1.addRule("Person.LastName_Phon");
			blockDefinitions.add(blockDefinition1);
			
			config.setBlockDefinitions(blockDefinitions);

			BlockDistributor blockDistributor = new BlockDistributor(
					matchPaths_, inputLookup_, inputobd_, blockLk_, false);
			blockDistributor.distributeBlocks();

			String blockFileName = config.getSystemProperty("workingDir")
					+ File.separatorChar + "block" + File.separatorChar
					+ "BlockB_1.txt";
			
			DataObject expected[] = new DataObject[] {
					new DataObject("ID0:E421|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("ID1:FAL|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),		
					new DataObject("Systemlid:CMH0000000018|1|CMH|0000000018"),
					new DataObject()};
			
			DataObjectReader sources = new DataObjectFileReader(blockFileName);

			assertTrue(Validator.validation(sources, expected));
		
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);			
		}						
	}
	
	public void test5(){
		try {
			setDataObjectReader("inputdata1.txt");
			
			ArrayList<BlockDefinition> blockDefinitions = config
					.getBlockDefinitions();
			blockDefinitions.clear();

			BlockDefinition blockDefinition0 = new BlockDefinition();
			blockDefinition0.setId("ID0");
			blockDefinition0.addRule("Person.FirstName_Phon");
			blockDefinitions.add(blockDefinition0);
			
			BlockDefinition blockDefinition2 = new BlockDefinition();
			blockDefinition2.setId("ID2");
			blockDefinition2.addRule("Person.SSN");
			blockDefinitions.add(blockDefinition2);
			
			config.setBlockDefinitions(blockDefinitions);

			BlockDistributor blockDistributor = new BlockDistributor(
					matchPaths_, inputLookup_, inputobd_, blockLk_, false);
			blockDistributor.distributeBlocks();

			String blockFileName = config.getSystemProperty("workingDir")
					+ File.separatorChar + "block" + File.separatorChar
					+ "BlockB_1.txt";
			
			DataObject expected[] = new DataObject[] {
					new DataObject("ID0:E421|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("ID2:482-76-0425|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),		
					new DataObject("Systemlid:CMH0000000018|1|CMH|0000000018"),
					new DataObject()};
			
			DataObjectReader sources = new DataObjectFileReader(blockFileName);

			assertTrue(Validator.validation(sources, expected));
		
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);			
		}								
	}
	
	public void test6(){
		try {
			setDataObjectReader("inputdata1.txt");
			
			ArrayList<BlockDefinition> blockDefinitions = config
					.getBlockDefinitions();
			blockDefinitions.clear();

			BlockDefinition blockDefinition1 = new BlockDefinition();
			blockDefinition1.setId("ID1");
			blockDefinition1.addRule("Person.LastName_Phon");
			blockDefinitions.add(blockDefinition1);
			
			BlockDefinition blockDefinition2 = new BlockDefinition();
			blockDefinition2.setId("ID2");
			blockDefinition2.addRule("Person.SSN");
			blockDefinitions.add(blockDefinition2);
			
			config.setBlockDefinitions(blockDefinitions);

			BlockDistributor blockDistributor = new BlockDistributor(
					matchPaths_, inputLookup_, inputobd_, blockLk_, false);
			blockDistributor.distributeBlocks();

			String blockFileName = config.getSystemProperty("workingDir")
					+ File.separatorChar + "block" + File.separatorChar
					+ "BlockB_1.txt";
			
			DataObject expected[] = new DataObject[] {
					new DataObject("ID1:FAL|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("ID2:482-76-0425|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),		
					new DataObject("Systemlid:CMH0000000018|1|CMH|0000000018"),
					new DataObject()};
			
			DataObjectReader sources = new DataObjectFileReader(blockFileName);

			assertTrue(Validator.validation(sources, expected));
		
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);			
		}								
	}
	
	public void test7(){
		try {
			setDataObjectReader("inputdata1.txt");
			
			ArrayList<BlockDefinition> blockDefinitions = config
					.getBlockDefinitions();
			blockDefinitions.clear();

			BlockDefinition blockDefinition0 = new BlockDefinition();
			blockDefinition0.setId("ID0");
			blockDefinition0.addRule("Person.FirstName_Phon");
			blockDefinitions.add(blockDefinition0);
			
			BlockDefinition blockDefinition1 = new BlockDefinition();
			blockDefinition1.setId("ID1");
			blockDefinition1.addRule("Person.LastName_Phon");
			blockDefinitions.add(blockDefinition1);
			
			BlockDefinition blockDefinition2 = new BlockDefinition();
			blockDefinition2.setId("ID2");
			blockDefinition2.addRule("Person.SSN");
			blockDefinitions.add(blockDefinition2);

			BlockDefinition.Rule rule = new BlockDefinition.Rule();			
			BlockDefinition.Rule rule1 = new BlockDefinition.Rule(new String[]{"Person.Address.AddressLine1_HouseNo",
			                                                                   "Person.Address.AddressLine1_HouseNo"});
			BlockDefinition.Rule rule2 = new BlockDefinition.Rule(new String[]{"Person.Address.AddressLine1_StPhon",
																			   "Person.Address.AddressLine1_StPhon"});
			rule.addChild(rule1);
			rule.addChild(rule2);			
			rule.setOperator(Operator.AND);			
			BlockDefinition blockDefinition3 = new BlockDefinition();
			blockDefinition3.setId("ID3");
			blockDefinition3.addRule(rule);
			blockDefinitions.add(blockDefinition3);
			
			config.setBlockDefinitions(blockDefinitions);

			BlockDistributor blockDistributor = new BlockDistributor(
					matchPaths_, inputLookup_, inputobd_, blockLk_, false);
			blockDistributor.distributeBlocks();

			String blockFileName = config.getSystemProperty("workingDir")
					+ File.separatorChar + "block" + File.separatorChar
					+ "BlockB_1.txt";
			
			DataObject expected[] = new DataObject[] {
					new DataObject("ID0:E421|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("ID1:FAL|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("ID2:482-76-0425|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("ID3:123123MANMAN|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("Systemlid:CMH0000000018|1|CMH|0000000018"),
					new DataObject()};
			
			DataObjectReader sources = new DataObjectFileReader(blockFileName);

			assertTrue(Validator.validation(sources, expected));
		
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);			
		}										
	}
	
	public void test8(){
		try {
			setDataObjectReader("inputdata1.txt");
			
			ArrayList<BlockDefinition> blockDefinitions = config
					.getBlockDefinitions();
			blockDefinitions.clear();

			BlockDefinition.Rule rule = new BlockDefinition.Rule();			
			BlockDefinition.Rule rule1 = new BlockDefinition.Rule(new String[]{"Person.FirstName_Phon",
			                                                                   "Person.FirstName_Phon"});
			BlockDefinition.Rule rule2 = new BlockDefinition.Rule(new String[]{"Person.LastName_Phon",
																			   "Person.LastName_Phon"});
			rule.addChild(rule1);
			rule.addChild(rule2);			
			rule.setOperator(Operator.AND);			
			BlockDefinition blockDefinition0 = new BlockDefinition();
			blockDefinition0.setId("ID0");
			blockDefinition0.addRule(rule);
			blockDefinitions.add(blockDefinition0);
			
			config.setBlockDefinitions(blockDefinitions);

			BlockDistributor blockDistributor = new BlockDistributor(
					matchPaths_, inputLookup_, inputobd_, blockLk_, false);
			blockDistributor.distributeBlocks();

			String blockFileName = config.getSystemProperty("workingDir")
					+ File.separatorChar + "block" + File.separatorChar
					+ "BlockB_1.txt";
						
			DataObject expected[] = new DataObject[] {
					//E421E421FALFAL ?
					new DataObject("ID0:E421E421FALFAL|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("Systemlid:CMH0000000018|1|CMH|0000000018"),
					new DataObject()};
			
			DataObjectReader sources = new DataObjectFileReader(blockFileName);

			assertTrue(Validator.validation(sources, expected));
		
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);			
		}										
	}		
	
	public void test9(){
		try {
			setDataObjectReader("inputdata1.txt");
			
			ArrayList<BlockDefinition> blockDefinitions = config
					.getBlockDefinitions();
			blockDefinitions.clear();

			BlockDefinition.Rule rule = new BlockDefinition.Rule();			
			BlockDefinition.Rule rule1 = new BlockDefinition.Rule(new String[]{"Person.FirstName_Phon",
			                                                                   "Person.FirstName_Phon"});
			BlockDefinition.Rule rule2 = new BlockDefinition.Rule(new String[]{"Person.LastName_Phon",
																			   "Person.LastName_Phon"});
			rule.addChild(rule1);
			rule.addChild(rule2);			
			rule.setOperator(Operator.OR);			
			BlockDefinition blockDefinition0 = new BlockDefinition();
			blockDefinition0.setId("ID0");
			blockDefinition0.addRule(rule);
			blockDefinitions.add(blockDefinition0);
			
			config.setBlockDefinitions(blockDefinitions);

			BlockDistributor blockDistributor = new BlockDistributor(
					matchPaths_, inputLookup_, inputobd_, blockLk_, false);
			blockDistributor.distributeBlocks();

			String blockFileName = config.getSystemProperty("workingDir")
					+ File.separatorChar + "block" + File.separatorChar
					+ "BlockB_1.txt";
								
			// E421E421? FALFAL?
			DataObject expected[] = new DataObject[] {
					new DataObject("ID0:E421E421|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("ID0:FALFAL|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),					
					new DataObject("Systemlid:CMH0000000018|1|CMH|0000000018"),
					new DataObject()};
			
			DataObjectReader sources = new DataObjectFileReader(blockFileName);

			assertTrue(Validator.validation(sources, expected));
		
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);			
		}										
	}		

	public void test10(){
		try {
			setDataObjectReader("inputdata1.txt");
			
			ArrayList<BlockDefinition> blockDefinitions = config
					.getBlockDefinitions();
			blockDefinitions.clear();

			BlockDefinition.Rule rule = new BlockDefinition.Rule();			
			BlockDefinition.Rule rule1 = new BlockDefinition.Rule(new String[]{"Person.FirstName_Phon",
			                                                                   "Person.FirstName_Phon"});
			BlockDefinition.Rule rule2 = new BlockDefinition.Rule(new String[]{"Person.LastName_Phon",
																			   "Person.LastName_Phon"});
			rule.addChild(rule1);
			rule.addChild(rule2);			
			rule.setOperator(Operator.OR);			
			BlockDefinition blockDefinition0 = new BlockDefinition();
			blockDefinition0.setId("ID0");
			blockDefinition0.addRule(rule);
			blockDefinitions.add(blockDefinition0);

			BlockDefinition blockDefinition1 = new BlockDefinition();
			blockDefinition1.setId("ID1");
			blockDefinition1.addRule("Person.LastName_Phon");
			blockDefinitions.add(blockDefinition1);
			
			config.setBlockDefinitions(blockDefinitions);
			
			BlockDistributor blockDistributor = new BlockDistributor(
					matchPaths_, inputLookup_, inputobd_, blockLk_, false);
			blockDistributor.distributeBlocks();

			String blockFileName = config.getSystemProperty("workingDir")
					+ File.separatorChar + "block" + File.separatorChar
					+ "BlockB_1.txt";
											
			// E421E421? FALFAL?
			DataObject expected[] = new DataObject[] {
					new DataObject("ID0:E421E421|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("ID0:FALFAL|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("ID1:FAL|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("Systemlid:CMH0000000018|1|CMH|0000000018"),
					new DataObject()};
			
			DataObjectReader sources = new DataObjectFileReader(blockFileName);

			assertTrue(Validator.validation(sources, expected));
		
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);			
		}										
	}

	public void test11(){
		try {			
			setDataObjectReader("inputdata2.txt");			
			ArrayList<BlockDefinition> blockDefinitions = config
					.getBlockDefinitions();
			blockDefinitions.clear();

			BlockDefinition blockDefinition = new BlockDefinition();
			blockDefinition.setId("ID0");
			blockDefinition.addRule("Person.FirstName_Phon");
			blockDefinitions.add(blockDefinition);
			config.setBlockDefinitions(blockDefinitions);
						
			BlockDistributor blockDistributor = new BlockDistributor(
					matchPaths_, inputLookup_, inputobd_, blockLk_, false);
			blockDistributor.distributeBlocks();

			String blockFileName = config.getSystemProperty("workingDir")
					+ File.separatorChar + "block" + File.separatorChar
					+ "BlockB_1.txt";
														
			DataObject expected[] = new DataObject[] {
					new DataObject("ID0:E421|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("Systemlid:CMH0000000018|1|CMH|0000000018"),
					new DataObject("ID0:E421|2|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("Systemlid:CMH0000000018|2|CMH|0000000018"),
					new DataObject("ID0:E421|3|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("Systemlid:CMH0000000018|3|CMH|0000000018"),					
					new DataObject()};
			
			DataObjectReader sources = new DataObjectFileReader(blockFileName);

			assertTrue(Validator.validation(sources, expected));
		
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);			
		}										
	}

	public void test12(){
		try {			
			setDataObjectReader("inputdata2.txt");			
			ArrayList<BlockDefinition> blockDefinitions = config
					.getBlockDefinitions();
			blockDefinitions.clear();

			BlockDefinition blockDefinition = new BlockDefinition();
			blockDefinition.setId("ID1");
			blockDefinition.addRule("Person.SSN");
			blockDefinitions.add(blockDefinition);
			config.setBlockDefinitions(blockDefinitions);
						
			BlockDistributor blockDistributor = new BlockDistributor(
					matchPaths_, inputLookup_, inputobd_, blockLk_, false);
			blockDistributor.distributeBlocks();

			String blockFileName = config.getSystemProperty("workingDir")
					+ File.separatorChar + "block" + File.separatorChar
					+ "BlockB_1.txt";
														
			DataObject expected[] = new DataObject[] {
					new DataObject("ID1:182-76-0425|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("Systemlid:CMH0000000018|1|CMH|0000000018"),
					new DataObject("ID1:282-76-0425|2|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("Systemlid:CMH0000000018|2|CMH|0000000018"),
					new DataObject("ID1:382-76-0425|3|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("Systemlid:CMH0000000018|3|CMH|0000000018"),					
					new DataObject()};
			
			DataObjectReader sources = new DataObjectFileReader(blockFileName);

			assertTrue(Validator.validation(sources, expected));
		
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);			
		}										
	}

	public void test13(){
		try {			
			setDataObjectReader("inputdata3.txt");			
			ArrayList<BlockDefinition> blockDefinitions = config
					.getBlockDefinitions();
			blockDefinitions.clear();

			BlockDefinition blockDefinition = new BlockDefinition();
			blockDefinition.setId("ID1");
			blockDefinition.addRule("Person.SSN");
			blockDefinitions.add(blockDefinition);
			config.setBlockDefinitions(blockDefinitions);
						
			BlockDistributor blockDistributor = new BlockDistributor(
					matchPaths_, inputLookup_, inputobd_, blockLk_, false);
			blockDistributor.distributeBlocks();

			String blockFileName = config.getSystemProperty("workingDir")
					+ File.separatorChar + "block" + File.separatorChar
					+ "BlockB_1.txt";
														
			DataObject expected[] = new DataObject[] {
					new DataObject("ID1:182-76-0425|1|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("Systemlid:CMH0000000018|1|CMH|0000000018"),
					new DataObject("ID1:182-76-0425|2|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("Systemlid:CMH0000000018|2|CMH|0000000018"),
					new DataObject("ID1:282-76-0425|3|CMH|0000000018|ELIZABETH|FULL#$MAIN|123||St"),
					new DataObject("Systemlid:CMH0000000018|3|CMH|0000000018"),					
					new DataObject()};
			
			DataObjectReader sources = new DataObjectFileReader(blockFileName);

			assertTrue(Validator.validation(sources, expected));
		
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);			
		}										
	}
	
}
