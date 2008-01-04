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

package com.sun.mdm.index.eindex.utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.DataObjectFileReader;
import com.sun.mdm.index.dataobject.DataObjectFileWriter;

/**
 * @author Sujit Biswas
 * 
 */
public class EIndexDataObjectWriter {

	
	private static Logger logger = Logger.getLogger(EIndexDataObjectWriter.class.getName());
	
	private String inputDir;

	private ArrayList<File> inputFiles = new ArrayList<File>();

	private DataObjectFileWriter writer;

	/**
	 * @param inputDir
	 */
	public EIndexDataObjectWriter(String inputDir) {
		this.inputDir = inputDir;

		File f = new File(this.inputDir);

		if (f.isDirectory()) {
			File[] fa = f.listFiles();

			for (int i = 0; i < fa.length; i++) {

				if (fa[i].getName().endsWith(".dat")) {
					inputFiles.add(fa[i]);
				}

			}

		}

		try {
			writer = new DataObjectFileWriter(inputDir + "/finalInputData.txt",true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private AtomicLong gid = new AtomicLong(1);
	public void write() {

		for (File f : inputFiles) {

			EIndexDataObjectReader r = new EIndexDataObjectReader(f
					.getAbsolutePath());
			try {
				while (true) {

					DataObject d = r.readDataObject();

					if (d == null)
						break;

					
					d.add(0, String.valueOf(gid.getAndIncrement()));
					writer.writeDataObject(d);

				}

				r.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public void close() throws IOException {
		writer.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			EIndexDataObjectWriter wr = new EIndexDataObjectWriter(
					"C:/test/loader/eIndex");

			wr.write();
			wr.close();

//			DataObjectFileReader fr = new DataObjectFileReader("C:/test/loader/finalInputData.txt");
//
//			while (true) {
//				DataObject d = fr.readDataObject();
//				
//				
//
//				if (d == null) {
//					break;
//				}
//				
//				logger.info(d.toString());
//				
//			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
