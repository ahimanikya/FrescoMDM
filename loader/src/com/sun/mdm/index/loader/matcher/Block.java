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
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.sun.mdm.index.dataobject.DataObject;

/**
 * A  block abstracts list of records that is compared within themselves using Match Engine.
 * Each {Rx, Ry}, where x,y are 1 to block size N, pair within a block
 * is compared by Matcher using Match Engine. 
 * @author sdua
 *
 */

public class Block {
	
	
	private List<DataObject> dataObjects = new ArrayList<DataObject>();
	private String blockID;
	private boolean matched = false;
	private Map<String,List<String>> dupMap = new HashMap<String, List<String>>();
		
	Block(String blockid) {
		blockID = blockid;
		if (blockid.startsWith("MATCHED")) {
			matched = true;
		}
		
	}
	
	
	void add(DataObject dataObject){
		dataObjects.add(dataObject);
	}
	
	int getSize() {
		return dataObjects.size();
	}
	
	DataObject getRecord(int position) {
		return dataObjects.get(position);
	}
	
	List <DataObject> getRecords() {
		return dataObjects;
	}
	
	public void clear() {
		dataObjects.clear();
		
	}
	
	boolean isMatched() {
		return matched;
	}
	
	String getBlockId() {
		return blockID;
	}
	
	void addDup(String GID1, String GID2) {
	   List<String> gids = dupMap.get(GID1);
	   if (gids == null) {
		   List<String> list = new ArrayList<String>();
		   list.add(GID2);
		   dupMap.put(GID1,list);
	   } else {
		   gids.add(GID2);
	   }
	   
	}
	
	List<String> getDup(String gid) {
		return dupMap.get(gid);
	}
}
