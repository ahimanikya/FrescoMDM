/**
 * 
 */
package com.sun.mdm.index.project.anttasks;

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

	/**
	 * 
	 */
	public LoaderConfig() {
		init();
	}

	private void init() {
		euidGenerator = "\n\t<EuidGeneratorConfig module-name=\"EuidGenerator\""
				+ "\n\t\tparser-class=\"com.sun.mdm.index.configurator.impl.idgen.EuidGeneratorConfiguration\">"
				+ "\n\t\t<euid-generator-class>com.sun.mdm.index.loader.euid.LoaderEuidGenerator</euid-generator-class>"
				+ "\n\t\t<parameters>" +
						"\n\t\t\t<parameter><parameter-name>IdLength</parameter-name>" +
						"<parameter-type>java.lang.Integer</parameter-type>	" +
						"<parameter-value>20</parameter-value></parameter>" +
						"\n\t\t\t<parameter><parameter-name>ChecksumLength</parameter-name>" +
						"<parameter-type>java.lang.Integer</parameter-type>" +
						"<parameter-value>0</parameter-value></parameter>" +
						"\n\t\t\t<parameter><parameter-name>ChunkSize</parameter-name>" +
						"<parameter-type>java.lang.Integer</parameter-type>" +
						"<parameter-value>1000</parameter-value></parameter>" +
				"\n\t\t</parameters>"
				+ "\n\t</EuidGeneratorConfig>";

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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("<loader>\n");

		sb.append(threshold + "\n");

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
