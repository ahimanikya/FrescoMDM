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
package com.sun.mdm.index.loader.matcher;

import com.sun.mdm.index.dataobject.DataObject;
	
/**
 * This is match between two SBR records, represented by EUID1, EUID2 and weight
 * @author Swaranjit Dua
 *
 */
public class MatchEUIDRecord implements MatchRecord {
	private String euid1;
	private String euid2;
	private double weight;
	private static String TRANS_PIGGY = ":T:";

	MatchEUIDRecord(String euid1, String euid2, double weight) {
		this.euid1 = euid1;
		/*
		 * split euid2 of Transaction number (that was put by MasterIndexTask) and so saves space in
		 * writing, since this transnumber is not used during pot dup generation in PotDupGenerator.
		 */
		int index2 = euid2.indexOf(TRANS_PIGGY);
		String e2 = euid2.substring(0, index2);;
		this.euid2 = e2;
		//this.euid2 = euid2;
		this.weight = weight;

	}	

	/**
	 * This is invoked by EUIDReader and so euid2 is already split of transaction number, 
	 * @param euid1
	 * @param euid2
	 * @param weight
	 * @param flag
	 */
	MatchEUIDRecord(String euid1, String euid2, double weight, boolean flag) {
		this.euid1 = euid1;
		this.euid2 = euid2;
		//this.euid2 = euid2;
		this.weight = weight;

	}	

	public String getEUID1() {
		return euid1;
	}

	public String getEUID2() {
		return euid2;
	}


	public double getWeight() {
		return weight;
	}

	public int compare(MatchRecord rec2) {
		MatchEUIDRecord record2 = (MatchEUIDRecord) rec2;

		if (euid1.compareTo(record2.euid1) < 0) {
			return -1;
		} else if (euid1.compareTo(record2.euid1) > 0) {
			return 1;
		} else  {
			if (euid2.compareTo(record2.euid2) < 0) {
				return -1;
			} else if (euid2.compareTo(record2.euid2) > 0) {
				return 1;
			} else {
				return 0;
			}
		}

	}

	public int hashCode() {

		return euid1.hashCode();
	}

	public boolean equals(Object o) {
		int res = compare((MatchEUIDRecord)o);
		return res == 0 ? true: false;
	}
}


