/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.index.project.anttasks;

import com.sun.mdm.index.util.Logger;
import java.io.File;
import java.io.IOException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.xml.sax.SAXException;

/**
 *
 * @author jlu
 */
public class ValidationTask extends Task {
    private static final Logger mLog = Logger.getLogger(ValidationTask.class.getName());
    private File mSrcdir;
    
    public void setSrcdir(File srcdir) {
        this.mSrcdir = srcdir;
    }
    @Override
    public void execute() throws BuildException {
        if (mSrcdir == null||!mSrcdir.exists()) {
            throw new BuildException(
                "The attribute,\"srcdir\", must be set to " +
                "Master Index Project source directory.");
        } 
        
        // The xml files need to be validated. edm.xml can not be validated.
        String[]configFiles = {
            "Configuration/master.xml",
            "Configuration/mefa.xml",
            "Configuration/midm.xml",//for new edm
            "Configuration/midm-security.xml",//for new edm
            "Configuration/object.xml",
            "Configuration/query.xml",
            "Configuration/security.xml",
            "Configuration/update.xml",
            "Configuration/validation.xml",
            "Filter/filter.xml",
            "MatchEngine/comparatorsList.xml"
        };
        String edmVersion = getProject().getProperty("edm-version");
        for (int i = 0; i < configFiles.length; i++) {
            File xmlFile = new File(mSrcdir, configFiles[i]);
            if (xmlFile.exists()){
                try{
                    validateXMLFiles(xmlFile);
                }catch(BuildException e){
                    mLog.severe(e.getMessage());
                    throw e;   
                }                
            }else if (configFiles[i].equals("Configuration/midm.xml")||
                    configFiles[i].equals("Configuration/midm-security.xml")){
                //new edm should have midm.xml and midm-security.xml
                if (null != edmVersion && 
                        edmVersion.equalsIgnoreCase("master-index-edm")) {                    
                    mLog.severe("Master Index Validation failed. " +
                            xmlFile + " is missing.");
                    throw new BuildException(xmlFile + " is missing.");
                }                
            }else{                
                mLog.severe("Master Index Validation failed. " +
                            xmlFile + " is missing.");
                throw new BuildException(xmlFile + " is missing.");                
            }
        }
        System.out.println("Master Index Validation passed.");
        mLog.info("Master Index Validation passed.");
    }
    private void validateXMLFiles(File xmlFile) throws BuildException{                 
        SchemaFactory factory = 
                SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        try {
            Schema schema = factory.newSchema();
            Validator validator = schema.newValidator();
            Source source = new StreamSource(xmlFile);      
            validator.validate(source);
        } catch (SAXException e) {
            throw new BuildException("Master Index Validation failed. "+
                    "Failed to validate " + xmlFile +". " + e.getMessage());
        } catch(IOException e){
            throw new BuildException("Master Index Validation failed. "+
                    "Failed to validate " + xmlFile +". " + e.getMessage());
        }
    }
}
