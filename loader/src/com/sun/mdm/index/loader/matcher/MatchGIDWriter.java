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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MatchGIDWriter implements MatchWriter {
	
	private DataOutputStream dostream_;
	
	MatchGIDWriter(File file) throws FileNotFoundException {
		FileOutputStream is = new FileOutputStream(file, false);
		dostream_ = new DataOutputStream(is);
				
	}
	
	public void write(MatchRecord mrecord) throws IOException {
		MatchGIDRecord mr = (MatchGIDRecord) mrecord;
		dostream_.writeLong(mr.getGIDFrom());
		dostream_.writeLong(mr.getGIDTo());
		dostream_.writeDouble(mr.getWeight());
	}
	
	public void close() throws IOException {
		dostream_.close();
	}

}
