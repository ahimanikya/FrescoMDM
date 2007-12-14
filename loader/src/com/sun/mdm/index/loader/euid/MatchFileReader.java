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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 
 * assumed match file reader
 * 
 * @author Sujit Biswas
 * 
 */
public class MatchFileReader {

	/**
	 * represents a long
	 */
	private static final int SYSID_FIELD_SIZE = 8;

	/**
	 * represents a double
	 */
	private static final int WEIGHT_FIELD_SIZE = 8;

	private static Logger logger = Logger.getLogger(MatchFileReader.class
			.getName());

	private static final long MATCH_FILE_RECORD_SIZE = SYSID_FIELD_SIZE
			+ SYSID_FIELD_SIZE + WEIGHT_FIELD_SIZE;

	private RandomAccessFile raf;

	/**
	 * @param matchFile
	 */
	public MatchFileReader(String matchFile) {

		try {
			raf = new RandomAccessFile(matchFile, "r");

		} catch (FileNotFoundException e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * reads one match file record at a time
	 * 
	 * @return
	 */

	public MatchFileRecord readRecord() {

		MatchFileRecord r = null;
		try {
			r = new MatchFileRecord(raf.readLong(), raf.readLong(), raf
					.readDouble());

			r.setIndex(raf.getFilePointer() / MATCH_FILE_RECORD_SIZE);

		} catch (IOException e) {
			logger.config(e.getMessage());
			return null;
		}

		return r;

	}

	/**
	 * 
	 * @return the list of consecutive records with the same sysid1 starting
	 *         from the current file pointer
	 */
	public List<MatchFileRecord> readRecordsWithSameSysid() {

		ArrayList<MatchFileRecord> result = new ArrayList<MatchFileRecord>();
		MatchFileRecord r = readRecord();
		if (r == null) {
			return result;
		} else
			result.add(r);

		while (true) {
			r = readRecord();

			if (r == null) {
				break;
			}

			if (r.getSysid1() == result.get(0).getSysid1()) {
				result.add(r);
			} else {

				// put the file pointer one record back
				try {
					raf.seek(raf.getFilePointer() - MATCH_FILE_RECORD_SIZE);
				} catch (IOException e) {
					logger.info(e.getMessage());
				}

				break;
			}
		}

		return result;

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
	 * 
	 * @return the list of consecutive records with the same sysid1, starting
	 *         from the given index
	 */
	public List<MatchFileRecord> readRecordsWithSameSysid(long index) {
		long filePointer = (index) * MATCH_FILE_RECORD_SIZE;
		try {
			seek(filePointer);
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		return readRecordsWithSameSysid();
	}
	
	public static void main(String[] args){
		MatchFileReader r = new MatchFileReader("C:\\eView\\loader\\match\\stage\\finalMatch");
		
		MatchFileRecord record = r.readRecord();
		
		while(record != null){
			//logger.info(record + "\n");
			record = r.readRecord();
		}
		
	}

}
