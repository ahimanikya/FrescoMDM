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

package com.sun.mdm.index.loader.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author Sujit Biswas
 * 
 */
public class LoaderLogManager {

	private static Logger logger = Logger.getLogger(LoaderLogManager.class
			.getName());

	private LogManager logManager;

	private String config = "conf/logging.properties";

	/**
	 * 
	 */
	public LoaderLogManager() {
	}

	/**
	 * the logger configuration file
	 * 
	 * @param config
	 */
	public LoaderLogManager(String config) {
		this.config = config;
	}

	public void init() {
		
		
		checkLogDirExist();
		
		logManager = LogManager.getLogManager();

		try {
			FileInputStream ins = new FileInputStream(config);
			logManager.readConfiguration(ins);
		} catch (FileNotFoundException e) {
			logger.info(e.getMessage());
			e.printStackTrace();

		} catch (SecurityException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}

	private void checkLogDirExist() {
		File f = new File("./logs");
		
		if(f.exists() && f.isDirectory()){
			logger.info("logger dir exist");
		}else{
			logger.info("creating new logger dir");
			f.mkdir();
		}
		
	}

	public static void main(String[] args) {
		new LoaderLogManager().init();

		for (int i = 0; i < 1000; i++) {

			logger.info("info init loader log");

			logger.severe("severe init loader log");

			logger.fine("fine init loader log");
		}

	}

}
