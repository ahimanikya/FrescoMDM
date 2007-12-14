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


import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class MatchEUIDWriter implements MatchWriter {
	
	private BufferedWriter writer_;
	
	MatchEUIDWriter(File file) throws IOException {
		FileWriter is = new FileWriter(file);
		writer_ = new BufferedWriter(is);
				
	}
	
	public void write(MatchRecord mr) throws IOException {
		
		MatchEUIDRecord mrecord = (MatchEUIDRecord)mr;
		writer_.write(mrecord.getEUID1());
		writer_.write("|");
		writer_.write(mrecord.getEUID2());
		writer_.write("|");
		String weight = String.valueOf(mrecord.getWeight());
		writer_.write(weight);
		writer_.write('\n');
	}
	
	public void close() throws IOException {
		writer_.close();
	}
}
