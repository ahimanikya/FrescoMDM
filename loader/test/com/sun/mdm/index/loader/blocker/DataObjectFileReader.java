package com.sun.mdm.index.loader.blocker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.sun.mdm.index.loader.blocker.DataObjectReader;

public class DataObjectFileReader implements DataObjectReader {

	private BufferedReader dataBufferedReader;
	
	private FileReader dataFile;
	
	public DataObjectFileReader(String fileName) 
		throws FileNotFoundException {
		dataFile = new FileReader(fileName);
		dataBufferedReader = new BufferedReader(dataFile);
	}
	
	public DataObject readDataObject() 
		throws IOException {
		String data = dataBufferedReader.readLine();
		return new DataObject(data);
	}
	
	public void close() {
		if (dataFile != null) {
			try {
				dataFile.close();
			} catch(IOException ignore) {
			}
		}
	}
}
