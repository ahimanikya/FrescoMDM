/**
 * 
 */
package com.sun.mdm.index.loader.euid;

import java.util.logging.Logger;

import junit.framework.TestCase;

/**
 * @author Sujit Biswas
 * 
 */
public class MatchFileTest extends TestCase {

	/**
	 * @param name
	 */
	public MatchFileTest(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testMatchFileSanity() throws Exception {
		MatchFileReader r = new MatchFileReader(
				"test/data/loader/loader-1/match/stage/finalMatch");

		MatchFileRecord pRecord = r.readRecord();
		MatchFileRecord cRecord = pRecord;
		while (cRecord != null) {
			// logger.info(cRecord + "\n");
			cRecord = r.readRecord();

			boolean b = checkSanity(cRecord, pRecord);
			
			logger.info(pRecord.toString() );
			if (!b) {
				logger.info(cRecord + "\n");
				logger.info(pRecord + "\n");
				assertTrue(b);
			}
			
			pRecord=cRecord;
		}

	}

	private boolean checkSanity(MatchFileRecord current,
			MatchFileRecord previous) {

		if (current == null) {
			return true;
		}

		if (current.getSysid1() > previous.getSysid1()) {
			return true;
		} else {

		}
		if (current.getSysid1() == previous.getSysid1()) {
			return current.getSysid2() > previous.getSysid2();
		}

		return false;
	}

	private static Logger logger = Logger.getLogger(MatchFileTest.class
			.getName());
}
