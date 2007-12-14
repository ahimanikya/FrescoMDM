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

package com.sun.mdm.index.loader.euid;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Logger;

import com.sun.mdm.index.loader.euid.EuidIndexAssigner;
import com.sun.mdm.index.loader.euid.EuidIndexFile;
import com.sun.mdm.index.loader.euid.EuidIndexFileRecord;

import junit.framework.TestCase;

/**
 * @author Sujit Biswas
 * 
 */
public class EuidIndexAssignerTest extends TestCase {

	private static Logger logger = Logger.getLogger(EuidIndexAssignerTest.class
			.getName());

	/**
	 * @param name
	 */
	public EuidIndexAssignerTest(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		createMatchFile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.euid.EuidIndexAssigner#initIndex()}.
	 */
	public void testInitIndex() throws Exception {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.euid.EuidIndexAssigner#assignEuids()}.
	 */
	public void testAssignEuids() throws Exception {
		String euid = "/eView/loader/euid/euidIndex.txt";

		EuidIndexAssigner ea = new EuidIndexAssigner();

		ea.initIndex();
		ea.assignEuids();

		EuidIndexFile wr = new EuidIndexFile(euid, ea.getEuidGenerator()
				.getEUIDLength());
		ArrayList<EuidIndexFileRecord> list = new ArrayList<EuidIndexFileRecord>();
		long count = 0;
		while (true) {
			EuidIndexFileRecord rec = wr.readRecord();

			if (rec == null) {
				break;
			} else {
				list.add(rec);
				count++;
				logger.info(rec.toString() + ",sysid= " + count);
			}
		}

		list.toString();

		ea.distributeBucket();
	}

	public void testStart() throws Exception {

		String euid = "/eView/loader/euid/euidIndex.txt";

		HashMap<String, ArrayList<Long>> map = new HashMap<String, ArrayList<Long>>();

		EuidIndexAssigner ea = new EuidIndexAssigner();

		ea.start();

		EuidIndexFile wr = new EuidIndexFile(euid, ea.getEuidGenerator()
				.getEUIDLength());
		ArrayList<EuidIndexFileRecord> list = new ArrayList<EuidIndexFileRecord>();

		long count = 0;

		while (true) {
			EuidIndexFileRecord rec = wr.readRecord();

			if (rec == null) {
				break;
			} else {
				list.add(rec);
				count++;

				ArrayList<Long> arr = map.get(rec.getEuid());

				if (arr == null) {
					arr = new ArrayList<Long>();
					map.put(rec.getEuid(), arr);
				}
				arr.add(count);

			}
		}

		map.keySet();

		map.entrySet();

		for (String key : map.keySet()) {
			ArrayList<Long> arr = map.get(key);

			if (arr.contains(Long.valueOf(1))) {
				assertTrue(arr.contains(Long.valueOf(3)));
				assertTrue(arr.contains(Long.valueOf(6)));
				assertTrue(arr.contains(Long.valueOf(7)));
				assertTrue(arr.contains(Long.valueOf(8)));
				assertTrue(arr.contains(Long.valueOf(15)));
				assertTrue(arr.contains(Long.valueOf(16)));
				assertTrue(arr.contains(Long.valueOf(200)));
				
			}
			if (arr.contains(Long.valueOf(9))) {
				assertTrue(arr.contains(Long.valueOf(10)));
			}
			if (arr.contains(Long.valueOf(11))) {
				assertTrue(arr.contains(Long.valueOf(12)));
			}
		}

	}

	private void createMatchFile() {
		String s = "/eView/loader/match/stage/finalMatch";

		try {
			RandomAccessFile match = new RandomAccessFile(s, "rw");
			
			match.setLength(0);

			for (int i = 0; i < strArr.length; i++) {
				writeRecord(strArr[i], match);
			}

			match.close();

		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

	public void writeRecord(String record, RandomAccessFile match)
			throws Exception {

		Scanner s = new Scanner(record);

		match.writeLong(s.nextLong());
		match.writeLong(s.nextLong());
		match.writeDouble(s.nextDouble());
	}

	String[] strArr = { "1 8 50", "1 15 50.55", "1 16 50.55","1 200 50", "3 6 50",
			"6 3 50","6 7 50",  "6 8 53", "7 6 50", "8 1 70", "8 6 53", "9 10 50", "10 9 50 ",
			"11 12 50", "12 11 50", "15 1 50.55", "16 1 50.55", "99 100 50",
			"100 99 50", "200 1 50"};

	public void _testWritingSparseFile() throws Exception {

		RandomAccessFile file = new RandomAccessFile("/eview/test.txt", "rw");

		file.setLength(0);

		long t1 = System.currentTimeMillis();
		file.write(1);

		file.setLength(500 * 1000000 * 10);

		file.seek(500 * 1000000 * 10 - 10);

		file.write(1);
		file.close();
		long t2 = System.currentTimeMillis();

		logger.info("time take to write 5 gb file in sec: " + (t2 - t1) / 1000);

		file = new RandomAccessFile("/eview/test.txt", "rw");

		byte[] bs = new byte[10];
		file.setLength(0);

		t1 = System.currentTimeMillis();

		file.seek(0);

		for (int i = 0; i < 500000000; i++) {
			file.write(bs);
		}

		file.close();

		t2 = System.currentTimeMillis();

		logger.info("time take to write each record for 5 gb file in sec: "
				+ (t2 - t1) / 1000);

	}

	public void _testDigit() throws Exception {

		RandomAccessFile file = new RandomAccessFile("/eview/test.txt", "rw");

		file.setLength(10000000);

		file.seek(0);

		while (true) {
			try {
				byte b = file.readByte();

				if (Character.isDigit(b)) {
					assertTrue(false);
				}
				logger.info("byte: " + b + " char " + (char) b);
			} catch (RuntimeException e) {
				break;
			}

		}

		file.setLength(0);

	}

}
