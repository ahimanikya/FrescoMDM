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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.EOFException;

import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.loader.common.FileManager;
import com.sun.mdm.index.loader.config.LoaderConfig;

import java.io.FileWriter;
import java.io.BufferedWriter;

public class MatchEUIDRecordReader implements MatchReader {
	
	private BufferedReader reader_ = null;
	public MatchEUIDRecordReader(File file) throws FileNotFoundException {
		FileReader is = new FileReader(file);
		reader_ = new BufferedReader(is);
		
		
	}
	
	public MatchRecord next() throws IOException {
	  try {
		  String line = reader_.readLine();
		  if (line == null) {
			  reader_.close();
			  return null;
		  }
		  String[] fields = line.split("\\|");
		  String euid1 = fields[0];
		  String euid2 = fields[1];
		  String sweight = fields[2];
		  double weight = Double.parseDouble(sweight);
		
		MatchRecord record = new MatchEUIDRecord(euid1, euid2, weight, true);
		return record;
		 
	  }  catch (EOFException ex) {
		  reader_.close();
		  return null;
	  }
  }
	
	public static void main(String[] args) {
		String line = "123|456|789";
		String[] fields = line.split("\\|");
		String euid1 = fields[0];
	}
	   	  	
 }
