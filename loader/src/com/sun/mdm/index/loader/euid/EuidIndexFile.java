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

package com.sun.mdm.index.loader.euid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Logger;

/**
 * This represent the EUID index file which has two fields <b> EUID|index </b>
 * the length of the EUID[byte array] depends on the configuration that the user
 * use to generate EUID and index field length is length of a <i>long</i> or
 * eight bytes which basically points the position of a SYSID in the assumed
 * match file. Each record in the EUID-IndexFile represent the corresponding
 * SYSID, example the 100th record in the EUID-index file represents SYSID-100
 * 
 * 
 * @author Sujit Biswas
 * 
 */
public class EuidIndexFile {

	/**
	 * represents the length of a long
	 */
	private static final int INDEX_FIELD_SIZE = 8;

	private static Logger logger = Logger.getLogger(EuidIndexFile.class
			.getName());

	private RandomAccessFile raf;

	private int euidLenght;

	public EuidIndexFile(String filename, int euidLength) {
		euidLenght = euidLength;

		try {
			raf = new RandomAccessFile(filename, "rw");

		} catch (FileNotFoundException e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * write on record into the euid index file
	 * 
	 * @param record
	 */
	public void writeRecord(EuidIndexFileRecord record) {

		byte[] bytes = new byte[euidLenght];

		if (record.getEuid() != null && record.getEuid().length() == euidLenght) {
			bytes = record.getEuid().getBytes();
		} else {
			// do nothing
		}

		try {
			raf.write(bytes);
			raf.writeLong(record.getSysidIndex());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}

	}

	/**
	 * read one record from the EUID index file
	 * 
	 * @return
	 */

	public EuidIndexFileRecord readRecord() {
		EuidIndexFileRecord r = null;

		byte[] bs = new byte[euidLenght];

		try {
			raf.read(bs);
			r = new EuidIndexFileRecord(new String(bs), raf.readLong());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}

		return r;

	}

	/**
	 * @throws IOException
	 * @see java.io.RandomAccessFile#close()
	 */
	public void close() throws IOException {
		raf.close();
	}

	/**
	 * @param pos
	 * @throws IOException
	 * @see java.io.RandomAccessFile#seek(long)
	 */
	public void seek(long pos) throws IOException {
		raf.seek(pos);
	}

	/**
	 * check if the EUID has been assigned for a given SYSID or system record
	 * 
	 * @param sysid
	 * @return
	 */
	public boolean isAssigned(long sysid) {
		try {
			long filePointer = (sysid - 1) * (euidLenght + INDEX_FIELD_SIZE);

			long lastRecordPointer = raf.length()
					- (euidLenght + INDEX_FIELD_SIZE);

			if (filePointer > lastRecordPointer) {
				return false;
			} else {
				seek(filePointer);
				byte b = raf.readByte();
				return Character.isDigit(b);

			}

		} catch (Exception ex) {
			logger.info(ex.getMessage());
		}
		return true;
	}

	/**
	 * read record for a given SYSID
	 * 
	 * @param sysid
	 * @return
	 */
	public EuidIndexFileRecord getRecord(long sysid) {
		try {
			long filePointer = (sysid - 1) * (euidLenght + INDEX_FIELD_SIZE);

			long lastRecordPointer = raf.length()
					- (euidLenght + INDEX_FIELD_SIZE);

			if (filePointer > lastRecordPointer) {
				return null;
			} else {
				seek(filePointer);
				EuidIndexFileRecord rec = readRecord();

				return rec;

			}

		} catch (Exception ex) {
			logger.info(ex.getMessage());
		}
		return null;
	}

	/**
	 * assign EUID to given system record
	 * 
	 * @param euid
	 * @param sysid
	 */
	public void assignEuid(String euid, long sysid) {

		try {
			long filePointer = (sysid - 1) * (euidLenght + INDEX_FIELD_SIZE);
			seek(filePointer);

			raf.write(euid.getBytes());

		} catch (Exception ex) {
			logger.info(ex.getMessage());
		}

	}

	/**
	 * get the index in the assumed match file for a given system record
	 * 
	 * @param sysid
	 * @return
	 */
	public long getIndex(long sysid) {

		return getRecord(sysid).getSysidIndex();
	}

	/**
	 * @param newLength
	 * @throws IOException
	 * @see java.io.RandomAccessFile#setLength(long)
	 */
	private void setLength(long newLength) throws IOException {
		raf.setLength(newLength);
	}

	public String getEuid(long sysid) {
		return getRecord(sysid).getEuid();
	}

	/**
	 * allocate space for n records, also cleanup any existing content
	 * 
	 * @param totalRecord
	 * @throws IOException
	 */
	public void allocateSpace(long totalRecord) throws IOException {

		// cleanup any previous content
		setLength(0);
		// allocate
		setLength((euidLenght + INDEX_FIELD_SIZE) * totalRecord);

	}

	/**
	 * position the file pointer to the current location of the SYSID
	 * 
	 * @param sysid
	 */
	public void position(long sysid) {
		try {
			long filePointer = (sysid - 1) * (euidLenght + INDEX_FIELD_SIZE);
			seek(filePointer);

		} catch (IOException e) {
			logger.info(e.getMessage());
		}

	}
}
