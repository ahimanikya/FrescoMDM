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
	
	public class MatchEUIDRecord implements MatchRecord {
		private String euid1;
		private String euid2;
		private double weight;
				
		MatchEUIDRecord(String euid1, String euid2, double weight) {
			this.euid1 = euid1;
			this.euid2 = euid2;
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
				if (euid2.compareTo(record2.euid1) < 0) {
					return -1;
				} else if (euid2.compareTo(record2.euid1) > 0) {
					return 1;
				} else {
					return 0;
				}
			}
						 		
		}
	}
	
	
