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
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.DataObjectReader;
import com.sun.mdm.index.dataobject.DataObjectWriter;
import java.io.File;
import com.sun.mdm.index.dataobject.ChildType;
import com.sun.mdm.index.loader.blocker.BlockDistributor;


/**
 * Bucket is a persisted container of many groups. 
 * This is container of many "Block".
 * It is a unit execution of work and can be loaded in memory by any
 * processor.
 * @author sdua
 *
 */
public class Bucket {
	private DataObjectWriter writer;
	private DataObjectReader reader;
	private Map<String,Block> blockMap; 
	private File file;
	private boolean isSBR_;
	private int numBlocks;
	
	
	public Bucket(DataObjectWriter writer, File f) {
		this.writer = writer;
		file = f;
	}
	
	public Bucket(DataObjectReader reader, File f, boolean sbr) {
		this.reader = reader;
		file = f;
		isSBR_ = sbr;
	}
	
	public File getFile() {
		return file;
	}
	
	public int getNumBlocks() {
		return numBlocks;
	}
	
	
	/**
	 * loads the data from the file into a HashMap.
	 * @return
	 * @throws Exception
	 */
	public Map load() throws Exception {
		blockMap = new HashMap<String,Block>();
		while (true) {			
			DataObject dataObject = reader.readDataObject();
			if (dataObject == null) {
				break;
			}
			
			String blockid = dataObject.getFieldValue(0);
			Block block = blockMap.get(blockid);
			if (block == null) {
				block = new Block(blockid);
				block.add(dataObject);
				blockMap.put(blockid, block);
				numBlocks++;
			} else {
				block.add(dataObject);
			}
			
		}
		if (!isSBR_) {
		  removeDups(blockMap);
		}
		return blockMap;				
	}
	
	public void write(DataObject dataObject) throws Exception {
		writer.writeDataObject(dataObject);
	}
	
	public void close() throws Exception {
		if (writer != null) {
		  writer.close();
		}
		if (reader != null) {
		   reader.close();
		}
	}
	
	private void removeDups(Map<String,Block> blockMap) {
						
		for(Map.Entry<String,Block> entry: blockMap.entrySet() ) {
			Block b = entry.getValue();
	        removeDup(b);	      
		}		
				
	}
	
	private void removeDup(Block block) {
		if (BlockDistributor.isSystemBlock(block.getBlockId())) {
			// systemlid block have records that have same systemcode, lid. We don't want to remove them. As these
			// are special case of determining EUID for records with same systemcode, lid
			return;
		}
		
		Map<String,DataObject> map = new HashMap<String,DataObject>();
		
		List<DataObject> records = block.getRecords();
		for (int i = 0; i < records.size(); i++) {
		  DataObject d = records.get(i);
		  String lid = d.getFieldValue(2);
		  String systemcode = d.getFieldValue(3);
		  DataObject dexist = map.get(systemcode+lid);
		  
		  if (dexist == null) {
			  map.put(systemcode+lid, d);
		  } else {
			  String GID1 = dexist.getFieldValue(1);
			  String GID2 = d.getFieldValue(1);
			  block.addDup(GID1, GID2);
			  combine(dexist, d);
			  //map.put(systemcode+lid, dexist);
			 // b.addDup()
		  }		  
		}
		block.clear();
		
		for (DataObject d: map.values()) {
			block.add(d);
		}							
	}
	
	
	private void combine(DataObject d1, DataObject d2) {
		List<String> fieldValues = d1.getFieldValues();
		// copy parent field values from d2 to d1 for which d1 field value is null
		for (int i = 0; i < fieldValues.size(); i++) {
			String val = d1.getFieldValue(i);
			if (val == null || val.equals("") ) {
				d1.setFieldValue(0, d2.getFieldValue(i));
			}			
		}
		
		List<ChildType>  d1childTypes = d1.getChildTypes();
		List<ChildType>  d2childTypes = d2.getChildTypes();
		
		List<ChildType> bigger = d1childTypes;
		if (d1childTypes.size() < d2childTypes.size()) {
			bigger = d2childTypes;
		}
		
		for (int i = 0; i < d1childTypes.size(); i++) {
	        ChildType ctype = d1childTypes.get(i);
	        List<DataObject> children = ctype.getChildren();
	        if (i < d2childTypes.size()) {
	           List<DataObject>  children2 = d2childTypes.get(i).getChildren();
 	           for (int j = 0; j < children2.size(); j++) {
	          	 DataObject d = children2.get(j);
	             boolean found = contains(children, d);
	             if (found == false) {
	            	 ctype.addChild(d);
	             }
	           } 
	        }	        	        
		}
		
		/**
		 * Add childtypes from d2 which are not in d1
		 */
		for (int i = d1childTypes.size(); i < d2childTypes.size(); i++) {
			ChildType ctype = d2childTypes.get(i);
			d1.addChildType(ctype);
		}				
	}
	
	private boolean contains(List<DataObject> children, DataObject d) {
		boolean found = false;
		for (DataObject d1: children) {
			if (d1.equals(d)) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	
	public synchronized MatchCursor getMatchCursor() {
		MatchCursor matchCursor = new MatchCursor(blockMap);
		return matchCursor;
	}
	
	 class MatchCursor {
		   //private final Map map_;
		   private Map.Entry<String,Block> entry_;
		   Iterator<Map.Entry<String,Block>> blockIterator_;
		   private int listPos_;
		   
		   MatchCursor(final Map<String,Block> map) {
			//    map_ = map;
			    blockIterator_ =
			    	 map.entrySet().iterator();
		   }
		   
		   /**
		    * returns one BlockPosition at a time.
		    * A BlockPosition is defined as a position of a record within a block.
		    * It is expected that calling code would match the record at blockPosition.recordPosition
		    * to all records within this block that are at position > recordPosition.
		    * This requires {B-record position in the block} comparions by calling thread, where B is size of block.
		    * For multi-threading, this seems most performance oriented algorithm.
		    * Other variations that were considered but not used are:
		    *  1   This wouldn't scale if size of block is very big such as for common names,
		    *     causing only one thread to do all the work.
		    *  2. return two records at a time. This would cause B*B invocations for each block 
		           Since this method is synchronized, too much time will be spent on object monitor
		           release and acquire method, which is synchronized.  
		    * @return BlockPosition
		    */
		   public synchronized BlockPosition next()  {
			   BlockPosition bp = null;
			   if (listPos_ == 0) {
			     while (blockIterator_.hasNext()) {
				   entry_ = blockIterator_.next();
				   Block block = entry_.getValue();
				   if (block.getSize() < 2) {
					   continue; // find block that has at least 2 records
				   }
				   bp = new BlockPosition(block, 0);				  
				   if (++listPos_ == block.getSize()-1) {
					   // listPos should be at least 1 less than block size
					   // because a match comparison needs at least two records
					  listPos_ = 0;
				   }
				   break;
			     }
			   } else {
				   Block block = entry_.getValue();
				   bp = new BlockPosition(block, listPos_);
				   if (++listPos_ == block.getSize()-1) {
						  listPos_ = 0;
				   }				   
			   }
			   return bp;				  			   
		   }
		   
		   String getBucketFile() {
			   return file.getName();
		   }
	 }
	 	 	 
	 	
}
