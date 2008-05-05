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
import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.EOFException;

import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.loader.common.FileManager;
import com.sun.mdm.index.loader.config.LoaderConfig;

import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 * Reads Match File containing GID records
 * @author Swaranjit Dua
 *
 */
public class MatchGIDRecordReader implements MatchReader {

	private DataInputStream distream_ = null;
	MatchGIDRecordReader(File file) throws FileNotFoundException {
		FileInputStream is = new FileInputStream(file);
		distream_ = new DataInputStream(is);


	}

	public MatchRecord next() throws IOException {
		try {
			long GID1 = distream_.readLong();			
			long GID2 = distream_.readLong();
			double weight = distream_.readDouble();
			MatchRecord record = new MatchGIDRecord(GID1, GID2, weight);
			return record;

		}  catch (EOFException ex) {
			distream_.close();
			return null;
		}
	}

}
