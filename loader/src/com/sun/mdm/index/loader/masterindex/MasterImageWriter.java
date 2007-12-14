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
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.DataObjectReader;
import com.sun.mdm.index.dataobject.DataObjectWriter;
import com.sun.mdm.index.dataobject.DataObjectFileWriter;
import java.io.BufferedWriter;
import java.io.File;
import com.sun.mdm.index.loader.common.FileManager;
import com.sun.mdm.index.loader.config.LoaderConfig;

import static com.sun.mdm.index.loader.masterindex.MIConstants.*;


/**
 * Writes Mater image to master index directory
 * @author sdua
 *
 */
public class MasterImageWriter {
 
	private Map<String, BufferedWriter> writerMap = new HashMap<String, BufferedWriter>();
	private String masterImageDir_;
	private static LoaderConfig config = LoaderConfig.getInstance();
	private DataObjectWriter dwriter_;
	private static String recordDelim = config.getSystemProperty("sqlldr.record.delimiter");
	
	public MasterImageWriter() throws Exception {
	  masterImageDir_ = FileManager.getMasterImageDir();
	  File file = new File(masterImageDir_, TRANSACTION + ".data");
	  FileWriter fwriter = new FileWriter(file);	     
	  BufferedWriter bwriter = new BufferedWriter(fwriter);
	  writerMap.put(TRANSACTION, bwriter);
	  
	  file = new File(masterImageDir_, ASSUMEDMATCH +".data");
	  fwriter = new FileWriter(file);	     
	  bwriter = new BufferedWriter(fwriter);
	  writerMap.put(ASSUMEDMATCH, bwriter);
	  
	  file = new File(masterImageDir_, ENTERPRISE+".data");
	  fwriter = new FileWriter(file);	     
	  bwriter = new BufferedWriter(fwriter);
	  writerMap.put(ENTERPRISE, bwriter);
	  
	  file = new File(masterImageDir_, SYSTEMOBJECT+".data");
	  fwriter = new FileWriter(file);	     
	  bwriter = new BufferedWriter(fwriter);
	  writerMap.put(SYSTEMOBJECT, bwriter);
	  
	  file = new File(masterImageDir_, SYSTEMSBR+".data");
	  fwriter = new FileWriter(file);	     
	  bwriter = new BufferedWriter(fwriter);
	  writerMap.put(SYSTEMSBR, bwriter);  	 
	  
	  file = FileManager.getInputSBRFile();
	  dwriter_ = new DataObjectFileWriter(file);
	  
	}
	
	void write(List<Map<String,TableData>> tableData) throws Exception {
      
	  for (Map<String,TableData> map: tableData) {
		 Set<Map.Entry<String,TableData>> entryset = map.entrySet();
		 
    	for (Map.Entry<String,TableData> entry: entryset) {
    		String key = entry.getKey();
    		TableData data = entry.getValue();
    		BufferedWriter writer = writerMap.get(key);
    		if (writer != null) {
    			write(writer, data);    			
    		} else {
    			String name = data.getName();
    			if (name.equals(POTENTIALDUPLICATES)) {
    				 write(dwriter_, data);
    			} else {
    			  File file = new File(masterImageDir_, "SBYN_" + name +".data");
    			  FileWriter fw = new FileWriter(file);	     
    			  writer = new BufferedWriter(fw);
    			  writerMap.put(name, writer);
    			  write(writer, data);
    			}
    		}
    	}
      }			
		
	}
	
	public static void write(DataObjectWriter dwriter, TableData tableData) throws Exception{
	  	List<DataObject> dataObjects = tableData.getDataObjects();
	  	for (DataObject d: dataObjects) {
	  		dwriter.writeDataObject(d);
	  	}
 	}
	
	public void write(BufferedWriter writer, TableData tableData) throws Exception{
	  	if (tableData.getName().equals(POTENTIALDUPLICATES)) {
	  		write(dwriter_, tableData);
	  	} else {
		 List<List<String>> dataList = tableData.getData();	  	
	  	 for(List<String> data: dataList) {
	  		for (int i = 0; i < data.size(); i++) {
	  		  String str = data.get(i);
	  		  if (str == null) {
	  			  str = "";
	  		  }
	  		  writer.write(str);
	  		  if (i < data.size()-1) {
	  			  writer.write("|");
	  		  }
	  		}	  		
	  		writer.write(recordDelim);
	  	 }
	  	}
	}
	
	public static void write(BufferedWriter writer, List<String> data) throws Exception{
	  		  		  	
	  	for (int i = 0; i < data.size(); i++) {
	  		  String str = data.get(i);
	  		  if (str == null) {
	  			  str = "";
	  		  }
	  		  writer.write(str);
	  		  if (i < data.size()-1) {
	  			  writer.write("|");
	  		  }
	  	}
	  			  		
	   writer.write(recordDelim); 			
	}
	
	
		
   void close() throws IOException {
		Collection<BufferedWriter> writers = writerMap.values();
		for (BufferedWriter writer: writers) {
			writer.close();
		}
		
		dwriter_.close();
		
	}
	
   /*
	void write(List<String> list, String object) throws Exception {
		BufferedWriter writer = writerMap.get(object);
		for (int i = 0; i < list.size(); i++) {
		  String str = list.get(i);
		  writer.write(str);
		  if (i != list.size()-1) { // last string does not has | symbol
		    writer.write("|");
		  }
		}						
	}
	*/	
		 	 	
}
