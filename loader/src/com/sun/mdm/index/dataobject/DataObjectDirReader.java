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
package com.sun.mdm.index.dataobject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Utility class to read/parse the input files records. Read text from a
 * character-input stream, buffering characters so as to provide for the
 * efficient reading of characters, arrays, and lines.
 * 
 * 
 * @author Sujit Biswas
 * 
 */

public class DataObjectDirReader implements DataObjectReader {

	/**
	 * logger
	 */
	private static Logger logger = Logger.getLogger(DataObjectFileReader.class
			.getName());
	
	private File[] files_;
	private DataObjectReader curReader_;
	private int curIndex_;
	
	
	
	/**
	 * 
	 * Creates a BufferedReader to read from,from the file with the specified
	 * name. A new {@link FileInputStream} object is created to represent the
	 * connection to the file.
	 * 
	 * @param fileName
	 *            the system-dependent filename
	 * @throws FileNotFoundException
	 *             if given string does not denote an existing regular file
	 */
	public DataObjectDirReader(File dir) throws FileNotFoundException {
	
		
		if (dir.isDirectory()) {
			files_ = dir.listFiles();
			curReader_ = new DataObjectFileReader(files_[0]);
			curIndex_ = 0;									
		} else {
			throw new FileNotFoundException();
		}
		
	}
		
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.dataobject.DataObjectReader#readDataObject()
	 */
	public DataObject readDataObject() throws InvalidRecordFormat {

		DataObject d = curReader_.readDataObject();
		if (d == null) {
			if (curIndex_ == files_.length -1) {
				return null;
			}
			curIndex_++;			
			try {
			 curReader_.close();
			 curReader_ = new DataObjectFileReader(files_[curIndex_]);
			 d = curReader_.readDataObject();
			} catch (Exception ex) {
				throw new InvalidRecordFormat();
			}
		}
		 return d;
		
	}
	
	public void close() throws Exception {
		curReader_.close();
	}
	
	
}
