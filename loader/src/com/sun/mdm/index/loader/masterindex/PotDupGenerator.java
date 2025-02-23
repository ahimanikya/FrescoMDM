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
package com.sun.mdm.index.loader.masterindex;
import java.sql.Connection;

import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;

import com.sun.mdm.index.loader.clustersynchronizer.dao.DAOFactory;
import com.sun.mdm.index.loader.common.FileManager;
import com.sun.mdm.index.loader.matcher.MatchEUIDRecordReader;
import com.sun.mdm.index.loader.matcher.MatchEUIDRecord;
import com.sun.mdm.index.loader.common.LoaderException;

import static com.sun.mdm.index.loader.masterindex.MIConstants.*;


/**
 * Writes Potential Duplicates to master index directory
 * @author Swaranjit Dua
 *
 */
public class PotDupGenerator {

	private static String TRANS_PIGGY = ":T:"; // This is a seperator for a Transaction number and EUID that 
	// combined and saved as one field in EUID Match file.
	private static int TRANS_PIGGY_LENGTH = 3;
	private static String empty_str = "";
	private String masterImageDir_;
	private BufferedWriter bwriter_;
	private Connection con_;
	public PotDupGenerator() throws LoaderException {
		try {
			masterImageDir_ = FileManager.getMasterImageDir();
			File file = new File(masterImageDir_, POTENTIALDUPLICATES + ".data");
			FileWriter fwriter = new FileWriter(file);	     
			bwriter_ = new BufferedWriter(fwriter);    
		} catch (java.io.IOException e) {
			throw new LoaderException (e);
		}
	}

	public void generatePotDups() throws LoaderException {
		try {
			con_ = DAOFactory.getConnection();
			File matchFile = FileManager.getFinalSBRMatchFile();
			MatchEUIDRecordReader reader = new MatchEUIDRecordReader(matchFile);
			while (true) {
				MatchEUIDRecord record = (MatchEUIDRecord) reader.next();			
				if (record == null) {
					break;
				}
				String euid1 = record.getEUID1();
				String euid2 = record.getEUID2();
				double wt = record.getWeight();
				String weight = String.valueOf(wt);
				addPotDupTable(euid1, euid2,
						weight);
			}		
			bwriter_.close();
		} catch (Exception e) {
			throw new LoaderException (e);
		}
	}

	private void addPotDupTable(String euid1, String euid2,
			String weight) throws LoaderException {
		try {

			String potdupId = com.sun.mdm.index.idgen.CUIDManager.getNextUID(con_,
			"POTENTIALDUPLICATE");

			int index1 = euid1.indexOf(TRANS_PIGGY);

			String e1 = euid1.substring(0,index1);
			String e2 = euid2; //.substring(0,index2);
			String trans = euid1.substring(index1+TRANS_PIGGY_LENGTH);

			//"POTENTIALDUPLICATEID",
			//"WEIGHT", "TYPE", "DESCRIPTION", "STATUS", "HIGHMATCHFLAG",
			//"RESOLVEDUSER", "RESOLVEDDATE", "RESOLVEDCOMMENT", "EUID2",
			//"TRANSACTIONNUMBER", "EUID1"
			List<String> list = new ArrayList<String>();
			list.add(potdupId);
			list.add(weight);
			list.add(empty_str);
			list.add(empty_str);
			list.add("U");
			list.add(empty_str);
			list.add(empty_str);
			list.add(empty_str);
			list.add(empty_str);
			list.add(e2);
			list.add(trans);
			list.add(e1);
			MasterImageWriter.write(bwriter_, list);
		} catch (Exception e) {
			throw new LoaderException (e);
		}	
	}
}
