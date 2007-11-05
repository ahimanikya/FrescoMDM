/**
 * 
 */
package com.sun.mdm.index.project.anttasks;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import java.io.File;
import java.io.FileInputStream;
import org.xml.sax.InputSource;

import com.sun.mdm.index.parser.Utils;
import com.sun.mdm.index.parser.QueryType;
import com.sun.mdm.index.parser.QueryType.QueryBuilder;
import com.sun.mdm.index.parser.MasterType;
import com.sun.mdm.index.parser.MatchFieldDef;
import com.sun.mdm.index.project.EviewRepository;
/**
 * @author Sujit Biswas, Kevin Kao
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
            
            xmlFile = new File(srcDir + File.separator + 
                EviewRepository.CONFIGURATION_FOLDER + File.separator +"master.xml");
            source = new InputSource(new FileInputStream(xmlFile));
            MasterType masterType = Utils.parseMasterType(source);
            com.sun.mdm.index.parser.Utils.Parameter parameterDuplicateThreshold;
            com.sun.mdm.index.parser.Utils.Parameter parameterMatchThreshold;
            parameterDuplicateThreshold = masterType.getDecisionMakerConfigParameterByName(MasterType.DUPLICATETHRESHOLD);
            String duplicateThreshold = "7.25";
            String matchThreshold = "29.0";
            if (parameterDuplicateThreshold != null) {
                duplicateThreshold = parameterDuplicateThreshold.getValue();
            }
            parameterMatchThreshold = masterType.getDecisionMakerConfigParameterByName(MasterType.MATCHTHRESHOLD);
            if (parameterMatchThreshold != null) {
                matchThreshold = parameterMatchThreshold.getValue();
            }
            String strThreshold = "<threshold-config>\n" +
		"    <duplicateThreshold>" + duplicateThreshold + "</duplicateThreshold>\n" +
		"    <matchThreshold>" + matchThreshold + "</matchThreshold>\n" +
                "</threshold-config>\n";

            //config.setThreshold("");
            
            // QueryBuilder
            xmlFile = new File(srcDir + File.separator + 
                EviewRepository.CONFIGURATION_FOLDER + File.separator +"query.xml");
            source = new InputSource(new FileInputStream(xmlFile));
            QueryType queryType = Utils.parseQueryType(source);
            QueryBuilder queryBuilder = queryType.getQueryBuilderByName("BLOCKER_SEARCH");
            str = queryType.getQueryBuilderXML(queryBuilder);
            
            config.setBlockConfig(str);
		
            // MatchConfig
            xmlFile = new File(srcDir + File.separator +
                EviewRepository.CONFIGURATION_FOLDER + File.separator +"mefa.xml");
            source = new InputSource(new FileInputStream(xmlFile));
            MatchFieldDef matchFieldDef = Utils.parseMatchFieldDef(source);
            str = matchFieldDef.getMatchingConfigXML();
            
            config.setMatchConfig(str);

		
        }catch(Exception ex){
                
        }
    }
}
