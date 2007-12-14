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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * 
 * Utility class to write DataObject into a file as a single record, This
 * converts the DataObject as a record(single line)and write to a
 * character-output stream, buffering characters so as to provide for the
 * efficient writing of DataObject as strings into the OutputStream.
 * 
 * 
 * 
 * 
 * 
 * @author Sujit Biswas
 * 
 */
public class DataObjectFileWriter implements DataObjectWriter {

	/**
	 * logger
	 */
	public static Logger logger = Logger.getLogger(DataObjectFileWriter.class
			.getName());

	private BufferedWriter loaderFile;

	/**
	 * if special mode is true then if the delimiters appear in the actual data,
	 * escape character "~" will prepended ahead of the delimiter
	 */
	private boolean specialMode;

	/**
	 * 
	 * @param fileName
	 *            the system-dependent filename
	 * @throws IOException
	 */
	public DataObjectFileWriter(String fileName) throws IOException {
		this.loaderFile = new BufferedWriter(new FileWriter(fileName));
	}

	/*
	 * Constructs a DataObjectFileWriter object given a file name with a
	 * appendMode (if "true" will append) indicating whether or not to append
	 * the data written.
	 */
	public DataObjectFileWriter(String fileName, String appendMode)
			throws IOException {

		boolean b = Boolean.valueOf(appendMode);

		this.loaderFile = new BufferedWriter(new FileWriter(fileName, b));
	}

	/**
	 * 
	 * use this option only when the user want to escape the delimiters in the
	 * actual data.
	 * <p>
	 * if special mode is true, then if the delimiters appear in the actual
	 * data, escape character "~" will prepended ahead of the delimiter
	 * 
	 * @param fileName
	 *            the system-dependent filename
	 * @throws IOException
	 */
	public DataObjectFileWriter(String fileName, boolean specialMode)
			throws IOException {
		this.loaderFile = new BufferedWriter(new FileWriter(fileName));
		this.specialMode = specialMode;
	}

	/**
	 * @param f
	 *            the file to write to
	 * @throws IOException
	 * 
	 */
	public DataObjectFileWriter(File f) throws IOException {
		this.loaderFile = new BufferedWriter(new FileWriter(f));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.dataobject.DataObjectWriter#writeDataObject(com.sun.eview.dataobject.DataObject)
	 */

	public void writeDataObject(DataObject d) throws IOException {

		String s = getDataObjectString(d);
		loaderFile.write(s);
	}

	private String getDataObjectString(DataObject d) {
		// TODO Auto-generated method stub

		StringBuffer sb = new StringBuffer();

		Iterator<String> it = d.getFieldValues().iterator();

		writeFields(sb, it);

		Iterator<ChildType> cit = d.getChildTypes().iterator();
		while (cit.hasNext()) {
			sb.append(Delimiter.HASH);
			writeChildType(sb, cit.next());
		}

		sb.append(Delimiter.NEW_LINE);
		return sb.toString();
	}

	/**
	 * @param sb
	 * @param it
	 */
	private void writeFields(StringBuffer sb, Iterator<String> it) {
		while (it.hasNext()) {
			sb.append(newField(it.next()));
			if (it.hasNext()) {
				sb.append(Delimiter.PIPE);
			}
		}
	}

	/**
	 * @param it
	 * @return
	 */
	private String newField(String str) {
		if (str == null || str.equals("")) {
			return "";
		}

		if (specialMode) {
			str = escapeSpecialCharacters(str);
		}

		return str;

	}

	/**
	 * @param str
	 * @return
	 */
	private String escapeSpecialCharacters(String str) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			switch (str.charAt(i)) {

			case (Delimiter.PIPE_CHAR):
			case (Delimiter.HASH_CHAR):
			case (Delimiter.TILDA_CHAR):
			case (Delimiter.DOLLAR_CHAR):
			case (Delimiter.NEW_LINE_CHAR):
				sb.append(Delimiter.TILDA_CHAR);
				sb.append(str.charAt(i));
				break;

			default:
				sb.append(str.charAt(i));
			}
		}
		// logger.info(str + ":" + sb.toString());
		return sb.toString();

	}

	private void writeChildType(StringBuffer sb, ChildType type) {
		Iterator<DataObject> it = type.getChildren().iterator();

		while (it.hasNext()) {
			sb.append(Delimiter.DOLLAR);
			writeChild(sb, it.next());
		}

	}

	private void writeChild(StringBuffer sb, DataObject child) {
		Iterator<String> it = child.getFieldValues().iterator();

		writeFields(sb, it);

	}

	/**
	 * @param args
	 */

	public static void main(String[] args) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.dataobject.DataObjectWriter#flush()
	 */
	public void flush() throws IOException {
		loaderFile.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.eview.dataobject.DataObjectWriter#close()
	 */
	public void close() throws IOException {
		loaderFile.close();
	}

}
