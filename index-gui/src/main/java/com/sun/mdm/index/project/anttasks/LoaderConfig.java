/**
 * 
 */
package com.sun.mdm.index.project.anttasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Sujit Biswas
 * 
 */
public class LoaderConfig {

	private String threshold="";

	private String blockConfig="";

	private String matchConfig="";

	private String euidGenerator="";

	private String systemProperties="";

	private String dataObjectReader="";
	
	private String validationConfig="";

	/**
	 * 
	 */
	public LoaderConfig() {
		init();
	}

	private void init() {
		initEuidGenerator();
		initSystemProperty();
		
		

	}

	/**
	 * 
	 */
	private void initEuidGenerator() {
		euidGenerator = "<EuidGenerator class=\"com.sun.mdm.index.loader.euid.LoaderEuidGenerator\"/>";
	}

	/**
	 * 
	 */
	private void initSystemProperty() {
		InputStream ins =  this.getClass().getClassLoader().getResourceAsStream("com/sun/mdm/index/project/anttasks/loader.systemproperty.xml");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(ins));
		
		StringBuilder sb = new StringBuilder();
		
		try {
			while(true){
				String line = br.readLine();
				if(line==null)
					break;
				sb.append(line + "\n");
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		systemProperties=sb.toString();
	}

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	public String getBlockConfig() {
		return blockConfig;
	}

	public void setBlockConfig(String blockConfig) {
		this.blockConfig = blockConfig;
	}

	public String getMatchConfig() {
		return matchConfig;
	}

	public void setMatchConfig(String matchConfig) {
		this.matchConfig = matchConfig;
	}

	public String getEuidGenerator() {
		return euidGenerator;
	}

	public void setEuidGenerator(String euidGenerator) {
		this.euidGenerator = euidGenerator;
	}

	public String getSystemProperties() {
		return systemProperties;
	}

	public void setSystemProperties(String systemProperties) {
		this.systemProperties = systemProperties;
	}

	public String getDataObjectReader() {
		return dataObjectReader;
	}

	public void setDataObjectReader(String dataObjectReader) {
		this.dataObjectReader = dataObjectReader;
	}

	public String getValidationConfig() {
		return validationConfig;
	}

	public void setValidationConfig(String validationConfig) {
		this.validationConfig = validationConfig;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("<loader>\n");

		sb.append(threshold + "\n");

		sb.append(validationConfig + "\n");

		sb.append(blockConfig + "\n");

		sb.append(matchConfig + "\n");

		sb.append(euidGenerator + "\n");

		sb.append(systemProperties + "\n");

		sb.append("</loader>\n");

		return sb.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LoaderConfig lg = new LoaderConfig();
		
		System.out.print(lg);
		
		lg.toString();

	}

}
