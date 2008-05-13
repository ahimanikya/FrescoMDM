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

import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;

import java.util.Collection;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.DataObjectReader;
import com.sun.mdm.index.dataobject.DataObjectWriter;
import com.sun.mdm.index.dataobject.DataObjectFileWriter;
import java.io.BufferedWriter;
import java.io.File;
import com.sun.mdm.index.loader.common.FileManager;
import com.sun.mdm.index.loader.config.LoaderConfig;
import com.sun.mdm.index.dataobject.objectdef.ObjectDefinition;
import com.sun.mdm.index.loader.common.LoaderException;

import static com.sun.mdm.index.loader.masterindex.MIConstants.*;


/**
 * Writes Mater image files to master index directory
 * @author Swaranjit Dua
 *
 */
public class MasterImageWriter {

	private Map<String, BufferedWriter> writerMap = new HashMap<String, BufferedWriter>();
	private String masterImageDir_;
	private static LoaderConfig config = LoaderConfig.getInstance();
	private DataObjectWriter dwriter_; // used for Input SBR file, that is eventually used for potential dups
	private static String recordDelim = config.getSystemProperty("sqlldr.record.delimiter");
	private final static String DATA = ".data";
	private final static String SBYN = "SBYN_";


	MasterImageWriter() throws LoaderException {
		try {
			masterImageDir_ = FileManager.getMasterImageDir();
			File file = new File(masterImageDir_, TRANSACTION + DATA);
			FileWriter fwriter = new FileWriter(file);	     
			BufferedWriter bwriter = new BufferedWriter(fwriter);
			writerMap.put(TRANSACTION, bwriter);

			file = new File(masterImageDir_, ASSUMEDMATCH +DATA);
			fwriter = new FileWriter(file);	     
			bwriter = new BufferedWriter(fwriter);
			writerMap.put(ASSUMEDMATCH, bwriter);

			file = new File(masterImageDir_, ENTERPRISE+DATA);
			fwriter = new FileWriter(file);	     
			bwriter = new BufferedWriter(fwriter);
			writerMap.put(ENTERPRISE, bwriter);

			file = new File(masterImageDir_, SYSTEMOBJECT+DATA);
			fwriter = new FileWriter(file);	     
			bwriter = new BufferedWriter(fwriter);
			writerMap.put(SYSTEMOBJECT, bwriter);

			file = new File(masterImageDir_, SYSTEMSBR+DATA);
			fwriter = new FileWriter(file);	     
			bwriter = new BufferedWriter(fwriter);
			writerMap.put(SYSTEMSBR, bwriter);  	 

			file = FileManager.getInputSBRFile();
			dwriter_ = new DataObjectFileWriter(file);

			configWriters();
		} catch (java.io.IOException ie) {
			throw new LoaderException (ie);
		}

	}

	/**
	 * writes DataObject to underlying DataObjectWriter.
	 * @param dwriter
	 * @param tableData
	 * @throws Exception
	 */
	static void write(DataObjectWriter dwriter, TableData tableData) throws LoaderException{
		try {
			if (tableData != null) {
				List<DataObject> dataObjects = tableData.getDataObjects();
				for (DataObject d: dataObjects) {
					dwriter.writeDataObject(d);
				}
			} 
		} catch (java.io.IOException ie) {
			throw new LoaderException (ie);
		}
	}

	void write(BufferedWriter writer, TableData tableData) throws LoaderException{
		try {
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
		} catch (java.io.IOException ie) {
			throw new LoaderException (ie);
		}
	}

	/**
	 * initializes writerMap.
	 * @throws Exception
	 */
	private void configWriters() throws LoaderException {
		try {
			ObjectDefinition objectDef = config.getObjectDefinition();
			String name = objectDef.getName();
			String table = name;
			File file = new File(masterImageDir_, SBYN + table  + DATA);
			FileWriter fwriter = new FileWriter(file);	     
			BufferedWriter  bwriter = new BufferedWriter(fwriter);
			writerMap.put(table, bwriter); 
			table = table + "SBR";
			file = new File(masterImageDir_, SBYN + table + DATA);
			fwriter = new FileWriter(file);	     
			bwriter = new BufferedWriter(fwriter);
			writerMap.put(table, bwriter);
			List<ObjectDefinition> children = objectDef.getChildren();

			for (ObjectDefinition child: children) {
				String childName = child.getName();
				table =  childName;
				file = new File(masterImageDir_, SBYN + table  + DATA);
				fwriter = new FileWriter(file);	     
				bwriter = new BufferedWriter(fwriter);
				writerMap.put(table, bwriter); 
				table = table + "SBR";
				file = new File(masterImageDir_, SBYN + table + DATA);
				fwriter = new FileWriter(file);	     
				bwriter = new BufferedWriter(fwriter);
				writerMap.put(table, bwriter); 
			}	
		} catch (java.io.IOException ie) {
			throw new LoaderException (ie);
		}
	}

	void write(List<Map<String,TableData>> tableData) throws LoaderException {
		try {
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
							File file = new File(masterImageDir_, SBYN + name +DATA);
							FileWriter fw = new FileWriter(file);	     
							writer = new BufferedWriter(fw);
							writerMap.put(name, writer);
							write(writer, data);
						}
					}
				}
			}
		} catch (java.io.IOException ie) {
			throw new LoaderException (ie);
		}

	}



	static void write(BufferedWriter writer, List<String> data) throws LoaderException{
		try {
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
		} catch (java.io.IOException ie) {
			throw new LoaderException (ie);
		}
	}



	void close() throws LoaderException {
		try {
			Collection<BufferedWriter> writers = writerMap.values();
			for (BufferedWriter writer: writers) {
				writer.close();
			}	
			dwriter_.close();
		} catch (java.io.IOException ie) {
			throw new LoaderException (ie);
		}
	}

}
