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

public class DataObjectFileReader implements DataObjectReader {

	/**
	 * logger
	 */
	private static Logger logger = Logger.getLogger(DataObjectFileReader.class
			.getName());

	/**
	 * represents any loader file
	 */
	private BufferedReader loaderFile;

	/**
	 * loader input filename
	 */
	private String loaderFileName;
	
	/**
	 * this mode is used when the record fields contains delimiters characters
	 * or newline character
	 */

	private boolean specialMode = false;
	
	
	
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
	public DataObjectFileReader(String fileName) throws FileNotFoundException {
		loaderFileName = fileName;
		this.loaderFile = new BufferedReader(new FileReader(fileName));
	}

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
	public DataObjectFileReader(String fileName, boolean specialMode)
			throws FileNotFoundException {
		loaderFileName = fileName;
		this.loaderFile = new BufferedReader(new FileReader(fileName));
		this.specialMode = specialMode;
	}

	/**
	 * Creates a BufferedReader to read from,from the file specified . A new
	 * {@link FileInputStream} object is created to represent the connection to
	 * the file.
	 * 
	 * @param f
	 *            the file to read from
	 * @throws FileNotFoundException
	 */
	public DataObjectFileReader(File f) throws FileNotFoundException {
		loaderFileName = f.getAbsolutePath();
		this.loaderFile = new BufferedReader(new FileReader(f));
	}

	/**
	 * Gets loader input filename
	 * @return String
	 */
	public String getFileName() {
		return loaderFileName;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.dataobject.DataObjectReader#readDataObject()
	 */
	public DataObject readDataObject() throws InvalidRecordFormat {

		String recordStr = readRecordString();

		if (recordStr != null) {
			return newDataObject(recordStr);
		}
		return null;

	}
	
	public void close() throws IOException {
		loaderFile.close();
	}
	
	
	/**
	 * Reads the next record string from the file or null if end of the file is
	 * encountered
	 * @return String the record string.
	 */
	public String readRecordString() {
		try {
			String record = loaderFile.readLine();

			if (record == null || !specialMode) {
				return record;
			} else {

				while (true) {
					if (checkValidRecord(record)) {
						return record;
					} else {
						record = record + Delimiter.NEW_LINE
								+ loaderFile.readLine();
					}

				}

			}
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		return null;
	}

	/**
	 * check whether the full record or partial record has been read
	 * 
	 * @param record
	 * @return
	 */
	private boolean checkValidRecord(String record) {
		int tildaCount = 0;
		for (int i = record.length() - 1; i > -1; i--) {
			if (record.charAt(i) == Delimiter.TILDA_CHAR) {
				tildaCount++;
			} else
				break;
		}

		int j = tildaCount % 2;
		return j == 0;
	}

	

	/**
	 * 
	 * @param recordStr
	 * @return
	 * @throws InvalidRecordFormat
	 */
	private DataObject newDataObject(String recordStr) throws InvalidRecordFormat {
		// TODO Auto-generated method stub

		DataObject r = new DataObject();

		if (requireSpecialProcessing(recordStr)) {
			doSpecialProcessing(r, recordStr);
		} else {
			updateDataObject(r, recordStr);
		}

		return r;
	}

	/**
	 * 
	 * @param r
	 * @param recordStr
	 * @throws InvalidRecordFormat
	 */

	private void doSpecialProcessing(DataObject r, String record)
			throws InvalidRecordFormat {

		int i = doSpecialProcessingForFields(r, record);

		if (i >= record.length()) {
			return;
		}

		doSpecialProcessingForChildTypes(r, record.substring(i));

	}

	/**
	 * @param r
	 * @param record
	 * @return
	 */
	private int doSpecialProcessingForFields(DataObject r, String record) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < record.length(); i++) {
			switch (record.charAt(i)) {

			case (Delimiter.PIPE_CHAR):
				r.addFieldValue(sb.toString());
				sb = new StringBuilder();

				break;

			case (Delimiter.HASH_CHAR):
				r.addFieldValue(sb.toString());
				return i;

			case (Delimiter.TILDA_CHAR):
				i++;
				sb.append(record.charAt(i));
				break;

			default:
				sb.append(record.charAt(i));
			}
		}
		r.addFieldValue(sb.toString());
		return record.length();
	}

	private void doSpecialProcessingForChildTypes(DataObject parent,
			String record) throws InvalidRecordFormat {

		StringBuilder sb = new StringBuilder();

		ChildType ct = null;
		DataObject child = null;
		for (int i = 0; i < record.length(); i++) {
			switch (record.charAt(i)) {

			case (Delimiter.PIPE_CHAR):
				child.addFieldValue(sb.toString());
				sb = new StringBuilder();

				break;

			case (Delimiter.DOLLAR_CHAR):
				if (child != null) {
					child.addFieldValue(sb.toString());
					sb = new StringBuilder();
				}
				child = new DataObject();
				ct.addChild(child);
				break;
			case (Delimiter.HASH_CHAR):
				ct = new ChildType();
				parent.addChildType(ct);

				break;

			case (Delimiter.TILDA_CHAR):
				i++;
				sb.append(record.charAt(i));
				break;

			default:
				sb.append(record.charAt(i));
			}
		}

		child.addFieldValue(sb.toString());
	}

	/**
	 * 
	 * @param recordStr
	 * @return
	 */
	private boolean requireSpecialProcessing(String recordStr) {
		return specialMode;
	}

	/**
	 * @param recordStr
	 * @param r
	 */
	private void updateDataObject(DataObject r, String recordStr)
			throws InvalidRecordFormat {

		try {
			int i = updateDataObjectFields(r, recordStr);
			if (i >= recordStr.length()) {
				return;
			}
			updateDataObjectChildTypes(r, recordStr.substring(i));
		} catch (Exception e) {
			throw new InvalidRecordFormat(
					"bad record format, use special mode to read records");

		}
	}

	private int updateDataObjectFields(DataObject r, String record) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < record.length(); i++) {
			switch (record.charAt(i)) {

			case (Delimiter.PIPE_CHAR):
				r.addFieldValue(sb.toString());
				sb = new StringBuilder();

				break;

			case (Delimiter.HASH_CHAR):
				r.addFieldValue(sb.toString());
				return i;

			default:
				sb.append(record.charAt(i));
			}
		}
		r.addFieldValue(sb.toString());
		return record.length();
	}

	/**
	 * 
	 * @param r
	 * @param s
	 */
	private void updateDataObjectChildTypes(DataObject parent, String record) {
		StringBuilder sb = new StringBuilder();

		ChildType ct = null;
		DataObject child = null;
		for (int i = 0; i < record.length(); i++) {
			switch (record.charAt(i)) {

			case (Delimiter.PIPE_CHAR):
				child.addFieldValue(sb.toString());
				sb = new StringBuilder();

				break;

			case (Delimiter.DOLLAR_CHAR):
				if (child != null) {
					child.addFieldValue(sb.toString());
					sb = new StringBuilder();
				}
				child = new DataObject();
				ct.addChild(child);
				break;
			case (Delimiter.HASH_CHAR):
				ct = new ChildType();
				parent.addChildType(ct);

				break;

			default:
				sb.append(record.charAt(i));
			}
		}

		child.addFieldValue(sb.toString());
	}
}
