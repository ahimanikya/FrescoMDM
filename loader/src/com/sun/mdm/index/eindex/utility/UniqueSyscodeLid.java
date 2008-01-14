/**
 * 
 */
package com.sun.mdm.index.eindex.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.DataObjectFileReader;
import com.sun.mdm.index.dataobject.DataObjectFileWriter;
import com.sun.mdm.index.dataobject.DataObjectReader;
import com.sun.mdm.index.dataobject.DataObjectWriter;
import com.sun.mdm.index.dataobject.InvalidRecordFormat;

/**
 * @author Sujit Biswas
 *
 */
public class UniqueSyscodeLid {

	
	
	private String inputDir;
	
	HashMap<String, String> dataObjectMap = new HashMap<String, String>();
	
	
	
	
	/**
	 * @param inputDir
	 */
	public UniqueSyscodeLid(String inputDir) {
		this.inputDir = inputDir;
	}




	public void readAllDataObject() throws InvalidRecordFormat, IOException{
		
		DataObjectReader reader = new DataObjectFileReader(inputDir+"/finalInputData.txt" , true);
		DataObjectWriter writer = new DataObjectFileWriter(inputDir+"/finalUniqueInputData.txt" , true);
		
		int gid =1;
		
		while(true){
			
			DataObject d = reader.readDataObject();
			
			if(d==null){
				break;
			}
			
			String key = d.getFieldValue(1) + d.getFieldValue(2);
			
			String s = dataObjectMap.get(key);
			
			if(s==null){
				d.setFieldValue(0, String.valueOf(gid++));
				writer.writeDataObject(d);
			}
			
			dataObjectMap.put(key, key);
			
		}
		
		dataObjectMap.toString();
		writer.close();
		System.out.println(dataObjectMap.size());
		
	}
	
	
	
	
	public static void main(String [] args){
		
		
		UniqueSyscodeLid u = new UniqueSyscodeLid("C:/test/loader");
		
		try {
			u.readAllDataObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRecordFormat e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}
