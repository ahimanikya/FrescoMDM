/**
 * 
 */
package com.sun.mdm.index.project.anttasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.xml.sax.InputSource;

import com.sun.mdm.index.parser.MasterType;
import com.sun.mdm.index.parser.MatchFieldDef;
import com.sun.mdm.index.parser.QueryType;
import com.sun.mdm.index.parser.Utils;
import com.sun.mdm.index.parser.QueryType.QueryBuilder;
import com.sun.mdm.index.project.EviewProjectProperties;

/**
 * @author Sujit Biswas, Kevin Kao
 * 
 * generates the loader-config.xml file
 * 
 */
public class LoaderGeneratorTask extends Task {
	private String srcDir;
	private String configDir;

	public void setSrcDir(String srcDir) {
		this.srcDir = getProjectPath() + File.separator + srcDir;
	}

	public void setConfigDir(String configDir) {
		this.configDir = getProjectPath() + File.separator + configDir;
	}

	/**
	 * @return
	 */
	private String getProjectPath() {
		return getProject().getProperty("basedir");
	}

	@Override
	public void execute() throws BuildException {
		LoaderConfig config = new LoaderConfig();

		try {
			File xmlFile;
			InputSource source;
			String str;

			// Threshold
			xmlFile = new File(srcDir + File.separator
					+ EviewProjectProperties.CONFIGURATION_FOLDER + File.separator
					+ "master.xml");
			source = new InputSource(new FileInputStream(xmlFile));
			MasterType masterType = Utils.parseMasterType(source);
			com.sun.mdm.index.parser.Utils.Parameter parameterDuplicateThreshold;
			com.sun.mdm.index.parser.Utils.Parameter parameterMatchThreshold;
			parameterDuplicateThreshold = masterType
					.getDecisionMakerConfigParameterByName(MasterType.DUPLICATETHRESHOLD);
			String duplicateThreshold = "7.25";
			String matchThreshold = "29.0";
			if (parameterDuplicateThreshold != null) {
				duplicateThreshold = parameterDuplicateThreshold.getValue();
			}
			parameterMatchThreshold = masterType
					.getDecisionMakerConfigParameterByName(MasterType.MATCHTHRESHOLD);
			if (parameterMatchThreshold != null) {
				matchThreshold = parameterMatchThreshold.getValue();
			}
			String strThreshold = "    <threshold-config>\n"
					+ "        <duplicateThreshold>" + duplicateThreshold
					+ "</duplicateThreshold>\n" + "        <matchThreshold>"
					+ matchThreshold + "</matchThreshold>\n"
					+ "    </threshold-config>\n";

			config.setThreshold(strThreshold);

			// QueryBuilder
			xmlFile = new File(srcDir + File.separator
					+ EviewProjectProperties.CONFIGURATION_FOLDER + File.separator
					+ "query.xml");
			source = new InputSource(new FileInputStream(xmlFile));
			QueryType queryType = Utils.parseQueryType(source);
			String queryBuilderName = masterType
					.getExecuteMatchQueryBuilderName();
			QueryBuilder queryBuilder = queryType
					.getQueryBuilderByName(queryBuilderName);
			str = queryType.getQueryBuilderXML(queryBuilder);

			config.setBlockConfig(str);

			// MatchConfig
			xmlFile = new File(srcDir + File.separator
					+ EviewProjectProperties.CONFIGURATION_FOLDER + File.separator
					+ "mefa.xml");
			source = new InputSource(new FileInputStream(xmlFile));
			MatchFieldDef matchFieldDef = Utils.parseMatchFieldDef(source);
			str = matchFieldDef.getMatchingConfigXML();

			config.setMatchConfig(str);

			// Write it out
			write(config);
			writeBuildXML();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void writeBuildXML() {
		try {
			File f = new File(configDir + "/../build.xml");
			BufferedWriter wr = new BufferedWriter(new FileWriter(f));

			InputStream ins = this
					.getClass()
					.getClassLoader()
					.getResourceAsStream(
							"com/sun/mdm/index/project/anttasks/loader.build.xml");

			BufferedReader br = new BufferedReader(new InputStreamReader(ins));

			StringBuilder sb = new StringBuilder();

			try {
				while (true) {
					String line = br.readLine();
					if (line == null)
						break;
					sb.append(line + "\n");

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			wr.write(sb.toString());

			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void write(LoaderConfig config) {
		try {
			File f = new File(configDir + "/loader-config.xml");
			BufferedWriter wr = new BufferedWriter(new FileWriter(f));
			wr.write(config.toString());
			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
