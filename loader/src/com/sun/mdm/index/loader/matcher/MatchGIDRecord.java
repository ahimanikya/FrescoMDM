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
	
	class MatchGIDRecord implements MatchRecord {
		private long GIDFrom;
		private long GIDTo;
		private double weight;
				
		MatchGIDRecord(DataObject dataObject1, DataObject dataObject2, double weight) {
			String GID1 = dataObject1.getFieldValue(1);
			String GID2 = dataObject2.getFieldValue(1);
			
			GIDFrom = Long.parseLong(GID1);
			GIDTo = Long.parseLong(GID2);
			this.weight = weight;
						
		}	
		
		MatchGIDRecord(long gid1, long gid2, double weight) {
						
			GIDFrom = gid1;
			GIDTo = gid2;
			this.weight = weight;
						
		}
		
		MatchGIDRecord(String gid1, String gid2, double weight) {
			GIDFrom = Long.parseLong(gid1);
			GIDTo = Long.parseLong(gid2);			
			this.weight = weight;
						
		}	
		
		long getGIDFrom() {
			return GIDFrom;
		}
		
		long getGIDTo() {
			return GIDTo;
		}
		
		double getWeight() {
			return weight;
		}
		
		public int compare(MatchRecord rec2) {
			
			MatchGIDRecord record2 = (MatchGIDRecord) rec2;
			if (GIDFrom < record2.GIDFrom) {
				return -1;
			} else if (GIDFrom > record2.GIDFrom) {
				return 1;
			} else  {
				if (GIDTo < record2.GIDTo) {
					return -1;
				} else if (GIDTo > record2.GIDTo) {
					return 1;
				} else {
					return 0;
				}
			}
						 		
		}
	}
	
	
