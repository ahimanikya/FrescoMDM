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

package com.sun.mdm.index.loader.clustersynchronizer.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Logger;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * 
 * FTP client used by the clusterSynchronizer for storing and retrieving files
 * from the master Loader
 * 
 * @author Sujit Biswas
 * 
 */
public class LoaderFtpClient {

	private static Logger logger = Logger.getLogger(LoaderFtpClient.class
			.getName());

	private String hostname;

	private String username;

	private String password;

	private FTPClient ftp = new FTPClient();

	public LoaderFtpClient(String hostname, String username, String password) {

		this.hostname = hostname;
		this.username = username;
		this.password = password;

	}

	/**
	 * Stores a file on the server using the given name and taking input from
	 * the given localDir/fileName. If the current file type is ASCII, line
	 * separators in the file are transparently converted to the NETASCII format
	 * 
	 * @param remoteDir
	 * @param localDir
	 * @param filename
	 * @return
	 */
	public synchronized boolean storeFile(String remoteDir, String localDir,
			String filename) {

		try {
			connect();

			ftp.changeWorkingDirectory(remoteDir);

			FileInputStream fis = new FileInputStream(localDir + filename);

			if (isFileExist(filename)) {
				if (!ftp.deleteFile(filename)) {
					logger
							.info("check ftp server permission cannot delete existing file "
									+ filename);
				}
			}

			ftp.storeFile(filename, fis);

			fis.close();

			disconnect();
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;

	}

	/**
	 * checks the file exist in the remote server for the current directory
	 * 
	 * @param filename
	 * @return
	 */
	private boolean isFileExist(String filename) {

		boolean b = false;
		try {
			FTPFile[] files = ftp.listFiles();

			for (int i = 0; i < files.length; i++) {

				if (files[i].isFile()) {

					b = files[i].getName().equals(filename);
					if (b == true)
						return b;
				}

			}

		} catch (IOException e) {
			logger.info(e.getMessage());
		}

		return b;
	}

	/**
	 * 
	 * Retrieves a named file from the server using remoteDir/fileNmae and
	 * writes it to the given remoteDir/fileName. If the current file type is
	 * ASCII, line separators in the file are converted to the local
	 * representation
	 * 
	 * @param remoteDir
	 * @param localDir
	 * @param filename
	 * @return
	 */
	public synchronized boolean retrieveFile(String remoteDir, String localDir,
			String filename) {

		boolean status = true;

		try {
			connect();

			ftp.changeWorkingDirectory(remoteDir);

			if (isFileExist(filename)) {

				
				File localFile = new File(localDir + filename);
				
				if(localFile.exists()){
					localFile.delete();
				}
				
				FileOutputStream fos = new FileOutputStream(localDir + filename);

				ftp.retrieveFile(filename, fos);

				fos.close();
			} else
				status = false;

			disconnect();
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			status = false;
		}

		return status;

	}

	private void disconnect() throws IOException {
		// ftp.logout();
		ftp.disconnect();

	}

	private void connect() throws SocketException, IOException {
		ftp.connect(hostname);
		ftp.login(username, password);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		LoaderFtpClient lftp = new LoaderFtpClient("sbiswas-d600", "sujit",
				"sujit");

		lftp.storeFile("/downloads", "test/data/", "blockBucket1");
		lftp.retrieveFile("/downloads", "test/data/", "blockBucket1");
	}

}
