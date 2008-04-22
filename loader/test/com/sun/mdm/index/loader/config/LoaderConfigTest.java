package com.sun.mdm.index.loader.config;

import junit.framework.TestCase;

public class LoaderConfigTest extends TestCase {

	private LoaderConfig config;
	
	/**
	 * @param name
	 */
	public LoaderConfigTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		config = LoaderConfig.getInstance();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testNumBlockBuckets() {
		try {			
			config.setSystemProperty("numBlockBuckets", "0");
			config.validation();		
			assertTrue(false);
		} catch(ConfigException ex) {
			assertTrue(true);
		}
		try {			
			config.setSystemProperty("numBlockBuckets", "-1");
			config.validation();		
			assertTrue(false);
		} catch(ConfigException ex) {
			assertTrue(true);
		}		
	}
	
	public void testNumEUIDBuckets() {
		try {			
			config.setSystemProperty("numEUIDBuckets", "0");
			config.validation();		
			assertTrue(false);
		} catch(ConfigException ex) {
			assertTrue(true);
		}
		try {			
			config.setSystemProperty("numEUIDBuckets", "-1");
			config.validation();		
			assertTrue(false);
		} catch(ConfigException ex) {
			assertTrue(true);
		}		
	}

	public void testTotalNoOfRecords() {
		try {			
			config.setSystemProperty("totalNoOfRecords", "0");
			config.validation();		
			assertTrue(false);
		} catch(ConfigException ex) {
			assertTrue(true);
		}
		try {			
			config.setSystemProperty("totalNoOfRecords", "-1");
			config.validation();		
			assertTrue(false);
		} catch(ConfigException ex) {
			assertTrue(true);
		}		
	}
		
	public void test1() {
		try {			
			config.setSystemProperty("numBlockBuckets", "5");
			config.setSystemProperty("numEUIDBuckets", "3");
			config.setSystemProperty("totalNoOfRecords", "1000000");
			config.validation();		
			assertTrue(false);
		} catch(ConfigException ex) {
			assertTrue(true);
		}
	}	

	public void xxxtest2() {
		try {
			config.setSystemProperty("numBlockBuckets", "100");
			config.setSystemProperty("numEUIDBuckets", "100");
			config.setSystemProperty("totalNoOfRecords", "1000000");
			config.validation();		
			assertTrue(true);
		} catch(ConfigException ex) {
			ex.printStackTrace();
			assertTrue(false);
		}
	}	

	public void xxxtest3() {
		try {			
			config.setSystemProperty("numBlockBuckets", "200");
			config.setSystemProperty("numEUIDBuckets", "200");
			config.setSystemProperty("totalNoOfRecords", "1000000");
			config.validation();		
			assertTrue(true);
		} catch(ConfigException ex) {
			ex.printStackTrace();
			assertTrue(false);
		}
	}	
	
	public void xxxtest4() {
		try {			
			config.setSystemProperty("numEUIDBuckets", "5");
			config.setSystemProperty("numEUIDBuckets", "3");
			config.setSystemProperty("totalNoOfRecords", "100");
			config.validation();		
			assertTrue(true);
		} catch(ConfigException ex) {
			ex.printStackTrace();
			assertTrue(false);
		}
	}		
}
