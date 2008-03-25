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
import java.util.ArrayList;
import java.util.List;

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
	private static final String REPLACE_WITH_CLASSPATH = "_REPLACE_WITH_CLASSPATH_";
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
					+ EviewProjectProperties.CONFIGURATION_FOLDER
					+ File.separator + "master.xml");
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
					+ EviewProjectProperties.CONFIGURATION_FOLDER
					+ File.separator + "query.xml");
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
					+ EviewProjectProperties.CONFIGURATION_FOLDER
					+ File.separator + "mefa.xml");
			source = new InputSource(new FileInputStream(xmlFile));
			MatchFieldDef matchFieldDef = Utils.parseMatchFieldDef(source);
			str = matchFieldDef.getMatchingConfigXML();

			config.setMatchConfig(str);

			// Write it out
			write(config);
			writeBuildXML();
			writeLoaderBat();
			writeSQLLoaderBat();
			writeLoaderScripts();
			writeSQLLoaderScripts();
			writeLoggingProperties();
			writeClusterSynchronizerSQL();
			writeClusterTrunkateSQL();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void writeBuildXML() {
		try {
			File f = new File(configDir + "/../build.xml");
			BufferedWriter wr = new BufferedWriter(new FileWriter(f));

			String fileResource = "com/sun/mdm/index/project/anttasks/loader.build.xml";

			StringBuilder sb = getResourceContents(fileResource);

			wr.write(sb.toString());

			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void writeClusterSynchronizerSQL() {
		try {
			File f = new File(configDir + "/../cluster-synchronizer.sql");
			BufferedWriter wr = new BufferedWriter(new FileWriter(f));

			String fileResource = "com/sun/mdm/index/project/anttasks/cluster-synchronizer.sql.xml";

			StringBuilder sb = getResourceContents(fileResource);

			wr.write(sb.toString());

			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void writeClusterTrunkateSQL() {
		try {
			File f = new File(configDir + "/../cluster-truncate.sql");
			BufferedWriter wr = new BufferedWriter(new FileWriter(f));

			String fileResource = "com/sun/mdm/index/project/anttasks/cluster-truncate.sql.xml";

			StringBuilder sb = getResourceContents(fileResource);

			wr.write(sb.toString());

			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void writeLoaderBat() {
		try {
			File f = new File(configDir + "/../run-loader.bat");
			BufferedWriter wr = new BufferedWriter(new FileWriter(f));
			String fileResource = "com/sun/mdm/index/project/anttasks/run-loader.bat.xml";

			StringBuilder sb = getResourceContents(fileResource);

			List<String> l = getJarFiles();

			StringBuilder sb1 = new StringBuilder();
			for (String s : l) {
				sb1.append("%loader_home%\\\\lib\\\\");
				sb1.append(s);
				sb1.append(";");
			}
			sb1.append("%ORACLE_JDBC_JAR%");

			String result = sb.toString().replaceAll(REPLACE_WITH_CLASSPATH,
					sb1.toString());
			wr.write(result);

			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void writeSQLLoaderBat() {
		try {
			File f = new File(configDir + "/../generate-sql-loader.bat");
			BufferedWriter wr = new BufferedWriter(new FileWriter(f));
			String fileResource = "com/sun/mdm/index/project/anttasks/run-loader.bat.xml";

			StringBuilder sb = getResourceContents(fileResource);

			List<String> l = getJarFiles();

			StringBuilder sb1 = new StringBuilder();
			for (String s : l) {
				sb1.append("%loader_home%\\\\lib\\\\");
				sb1.append(s);
				sb1.append(";");
			}
			sb1.append("%ORACLE_JDBC_JAR%");

			String result = sb.toString().replaceAll(REPLACE_WITH_CLASSPATH,
					sb1.toString());
			wr.write(result.replaceAll("com.sun.mdm.index.loader.main.BulkMatcherLoader", "com.sun.mdm.index.loader.sqlloader.ScriptGenerator"));

			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	private List<String> getJarFiles() {
		File f = new File(configDir + "/../lib");

		List<String> l = new ArrayList<String>();

		for (String s : f.list()) {
			if (s.endsWith(".jar"))
				l.add(s);
		}

		return l;

	}

	private void writeLoaderScripts() {
		try {
			File f = new File(configDir + "/../run-loader.sh");
			BufferedWriter wr = new BufferedWriter(new FileWriter(f));
			String fileResource = "com/sun/mdm/index/project/anttasks/run-loader.sh.xml";

			StringBuilder sb = getResourceContents(fileResource);

			List<String> l = getJarFiles();

			StringBuilder sb1 = new StringBuilder();
			for (String s : l) {
				sb1.append("${loader_home}/lib/");
				sb1.append(s);
				sb1.append(":");
			}
			sb1.append("${ORACLE_JDBC_JAR}");

			String result = sb.toString().replace(REPLACE_WITH_CLASSPATH,
					sb1.toString());
			wr.write(result);

			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void writeSQLLoaderScripts() {
		try {
			File f = new File(configDir + "/../generate-sql-loader.sh");
			BufferedWriter wr = new BufferedWriter(new FileWriter(f));
			String fileResource = "com/sun/mdm/index/project/anttasks/run-loader.sh.xml";

			StringBuilder sb = getResourceContents(fileResource);

			List<String> l = getJarFiles();

			StringBuilder sb1 = new StringBuilder();
			for (String s : l) {
				sb1.append("${loader_home}/lib/");
				sb1.append(s);
				sb1.append(":");
			}
			sb1.append("${ORACLE_JDBC_JAR}");

			String result = sb.toString().replace(REPLACE_WITH_CLASSPATH,
					sb1.toString());
			wr.write(result.replaceAll("com.sun.mdm.index.loader.main.BulkMatcherLoader", "com.sun.mdm.index.loader.sqlloader.ScriptGenerator"));

			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void writeLoggingProperties() {
		try {
			File f = new File(configDir + "/logging.properties");
			BufferedWriter wr = new BufferedWriter(new FileWriter(f));
			String fileResource = "com/sun/mdm/index/project/anttasks/logging.properties.xml";

			StringBuilder sb = getResourceContents(fileResource);

			wr.write(sb.toString());

			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param fileResource
	 * @return
	 */
	private StringBuilder getResourceContents(String fileResource) {
		InputStream ins = this.getClass().getClassLoader().getResourceAsStream(
				fileResource);

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
		return sb;
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
