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

import java.sql.Connection;
import java.util.logging.Logger;

import com.sun.mdm.index.idgen.EuidGenerator;
import com.sun.mdm.index.idgen.SEQException;

/**
 * 
 * This class is same as that of eView default EuidGenerator, only the
 * difference is that this does not access database to store and retrieve the
 * Sequence number. 
 * 
 * @author Sujit Biswas
 * 
 */
public class LoaderEuidGenerator implements EuidGenerator {

	private static final Logger mLogger = Logger
			.getLogger(LoaderEuidGenerator.class.getName());

	private int mIdLength;

	private int mChecksumLength;

	private int mChunkSize;

	private long mNextEUID = -1;

	private long sequenceNo = 0;

	/**
	 * Parameters of the EUID generator represented in the configuration XML
	 * file are set using this method.
	 * 
	 * @param parameterName
	 *            parameter
	 * @param value
	 *            parameter value
	 * @exception SEQException
	 *                An error occurred
	 */
	public void setParameter(String parameterName, Object value)
			throws SEQException {
		if (parameterName.equals("IdLength")) {
			mIdLength = ((Integer) value).intValue();
		} else if (parameterName.equals("ChecksumLength")) {
			mChecksumLength = ((Integer) value).intValue();
		} else if (parameterName.equals("ChunkSize")) {
			mChunkSize = ((Integer) value).intValue();
		} else {
			throw new SEQException("Unknown parameter: " + parameterName);
		}
	}

	/**
	 * Generate the next EUID
	 * 
	 * 
	 * for the loader the SQL connection can be null. In order to keep the
	 * EuidGenerator interface same as that of the eView application the
	 * connection parameter has been kept
	 * 
	 * @exception SEQException
	 *                an error occurred
	 * @return next EUID
	 */
	public synchronized String getNextEUID(Connection con) throws SEQException {
		String euid = null;

		try {
			if (mNextEUID != -1) {
				if (mNextEUID % mChunkSize == 0) {
					mNextEUID = xgetNextEUID(con);
				}
				euid = String.valueOf(mNextEUID);
			} else {
				mNextEUID = xgetNextEUID(con);
				euid = String.valueOf(mNextEUID);
			}
		} catch (SEQException e) {
			throw e;
		}
		String retVal;
		String sChecksum;
		if (mChecksumLength > 0) {
			sChecksum = String.valueOf(getChecksum(mNextEUID));
			retVal = formatEUID(euid) + sChecksum;
		} else {
			retVal = formatEUID(euid);
		}
		mNextEUID++;
		
		

		return retVal;
	}

	/**
	 * Get the EUID length
	 * 
	 * @return euid length
	 * 
	 */
	public int getEUIDLength() {
		return mIdLength + mChecksumLength;
	}

	private long xgetNextEUID(Connection conn) throws SEQException {
		long nextValue;
		nextValue = sequenceNo;
		sequenceNo = sequenceNo + mChunkSize;

		return nextValue;
	}

	private String formatEUID(String euid) throws SEQException {
		if (euid.length() > mIdLength) {
			throw new SEQException("ID length exceeded.");
		}
		int padLength = mIdLength - euid.length();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < padLength; i++) {
			sb.append('0');
		}
		sb.append(euid);
		return sb.toString();
	}

	private String getChecksum(long value) {
		int mod = (int) Math.pow(10, mChecksumLength);
		value = (1103515243 * value + 12345) & 0x7FFFFFFF;
		long ck = value % mod;
		String retVal = String.valueOf(ck);
		int padLength = mChecksumLength - retVal.length();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < padLength; i++) {
			sb.append('0');
		}
		sb.append(retVal);

		mLogger.config("Checksum for " + value + " is " + sb);
		return sb.toString();
	}

	public static void main(String[] args) {

		
	}

}
