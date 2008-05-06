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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import junit.framework.TestCase;

import com.sun.mdm.index.idgen.EuidGenerator;
import com.sun.mdm.index.idgen.impl.DefaultEuidGenerator;
import com.sun.mdm.index.loader.clustersynchronizer.dao.DAOFactory;
import com.sun.mdm.index.loader.config.LoaderConfig;
import com.sun.mdm.index.loader.euid.LoaderEuidGenerator;

/**
 * @author Sujit Biswas
 * 
 */
public class LoaderEuidGeneratorTest extends TestCase {

	/**
	 * @param name
	 */
	public LoaderEuidGeneratorTest(String name) {
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

	/**
	 * Test EUID uniqueness.
	 * @throws Exception If the test fails.
	 */
	public void testEUIDUniqueness() 
		throws Exception {
		
		EuidGenerator euidGenerator = new LoaderEuidGenerator();
		
		euidGenerator.setParameter("IdLength", Integer.valueOf(17));
		euidGenerator.setParameter("ChecksumLength", Integer.valueOf(5));
		euidGenerator.setParameter("ChunkSize", Integer.valueOf(3));
		
		HashSet<String> euids = new HashSet<String>();

		Random randomGenerator = new Random();
		randomGenerator.setSeed(System.currentTimeMillis());
		
		int start = randomGenerator.nextInt(10000);

		System.out.println("euid1 = " + euidGenerator.getNextEUID(null));		
		for (int i = start; i < (start + 30000); i++) {
			assertTrue(euids.add(euidGenerator.getNextEUID(null)));
		}		
		System.out.println("euid2 = " + euidGenerator.getNextEUID(null));
	}
	
	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.euid.LoaderEuidGenerator#getNextEUID(java.sql.Connection)}.
	 */
	public void testGetNextEUID() throws Exception {
		EuidGenerator e = new LoaderEuidGenerator();

		e.setParameter("IdLength", Integer.valueOf(20));
		e.setParameter("ChecksumLength", Integer.valueOf(5));
		e.setParameter("ChunkSize", Integer.valueOf(2));

		ArrayList<String> euids = new ArrayList<String>();
		for (int i = 0; i < 1000; i++) {
			euids.add(e.getNextEUID(null));

			if (i > 0)
				assertTrue(Integer.valueOf(euids.get(i)) > Integer
						.valueOf(euids.get(i - 1)));
		}

		String euid = e.getNextEUID(null);
		euid = e.getNextEUID(null);
		euid = e.getNextEUID(null);
		euid = e.getNextEUID(null);
		euid = e.getNextEUID(null);
		euid.toString();
	}

	public void testGetSequenceNumber() throws Exception {

		EuidGenerator e = new LoaderEuidGenerator();

		HashMap<String, String> params = LoaderConfig.getInstance().getEuidParams();
		
		e.setParameter("IdLength", Integer.valueOf(params.get("IdLength")));
		e.setParameter("ChecksumLength", Integer.valueOf(params.get("ChecksumLength")));
		e.setParameter("ChunkSize", Integer.valueOf(params.get("ChunkSize")));

		for (int i = 0; i < 1509; i++) {
			 e.getNextEUID(null);
		}
		
		String euid = e.getNextEUID(null);
		int checksumlen = Integer.parseInt(params.get("ChecksumLength"));
		long chunkSize = Long.parseLong(params.get("ChunkSize"));
		int seqStrLen = euid.length() - checksumlen;
		
		String seqStr = euid.substring(0, seqStrLen);
		
		long l = Long.parseLong(seqStr);
		long mod = l % chunkSize;
		long seqNo = l + chunkSize - mod;
				
		String sql = "update sbyn_seq_table set seq_count=? where seq_name='EUID'";		
		Connection c = DAOFactory.getConnection();		
		PreparedStatement ps =  c.prepareStatement(sql);		
		ps.setLong(1, seqNo);
		
		int i = ps.executeUpdate();		
		ps.close();	
		c.close();							
	}

}
