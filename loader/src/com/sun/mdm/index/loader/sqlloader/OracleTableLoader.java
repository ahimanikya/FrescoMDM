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

package com.sun.mdm.index.loader.sqlloader;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas
 * 
 */
public class OracleTableLoader implements Callable<String> {

	private String tableName;
	private File sqlldrDir;
	LoaderConfig config = LoaderConfig.getInstance();

	Logger logger = Logger.getLogger(OracleTableLoader.class.getName());

	/**
	 * @param tableName
	 */
	public OracleTableLoader(String tableName) {
		this.tableName = tableName;

		String workingDir = config.getWorkingDir();

		File sqlldr = new File(workingDir + "/sqlldr");

		sqlldrDir = sqlldr.getAbsoluteFile();

	}

	public String call() throws Exception {

		String command = "cmd /C " + tableName;

		String osName = System.getProperty("os.name", "");

		boolean isWindows = false;

		if (osName.indexOf("Windows") >= 0) {
			isWindows = true;
		}

		if (isWindows) {
			command = "cmd /C " + tableName + ".bat";
		} else {
			command = "bash -c " + tableName + ".sh";
		}

		Runtime.getRuntime().exec(command);

		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(command, null, sqlldrDir);

		new OracleTableStreamReader(tableName,process.getInputStream(), "OUTPUT").start();
		new OracleTableStreamReader(tableName,process.getErrorStream(), "OUTPUT").start();

		long t1 = System.currentTimeMillis();
		int i = process.waitFor();
		long t2 = System.currentTimeMillis();

		logger.info("time taken in millis  " + tableName + " : " + (t2 - t1));

		return String.valueOf(i);
	}

}
