/*
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
package com.sun.mdm.index.project.anttasks;

import com.sun.mdm.index.project.EviewProjectProperties;
import com.sun.mdm.index.project.generator.descriptor.AppXmlWriter;
import com.sun.mdm.index.project.generator.exception.TemplateWriterException;
import com.sun.mdm.index.project.generator.objects.EntityObjectWriter;
import com.sun.mdm.index.project.generator.ops.OPSWriter;
import com.sun.mdm.index.project.generator.persistence.DDLWriter;
import com.sun.mdm.index.project.generator.validation.ObjectDescriptorWriter;
import com.sun.mdm.index.project.generator.webservice.WebServiceWriter;
import com.sun.mdm.index.project.generator.outbound.OutboundXSDBuilder;
import com.sun.mdm.index.project.generator.descriptor.JbiXmlWriter;
import com.sun.mdm.index.objects.metadata.MetaDataService;
import com.sun.mdm.index.parser.EIndexObject;
import com.sun.mdm.index.parser.ParserException;
import com.sun.mdm.index.parser.Utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator; 

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.types.FileSet;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class EViewGeneratorTask extends Task {
    private String mSrcdir;
    private String mEjbdir;
    private String mWardir;
    private String mTemplateDir;
    private boolean mForce=false;
    
    public void setSrcdir(String srcdir) {
        Project p = this.getProject();
        String projPath = p.getProperty("basedir");
        this.mSrcdir = projPath + File.separator + srcdir;
    }
    
    public void setEjbdir(String ejbdir) {
        Project p = this.getProject();
        String projPath = p.getProperty("basedir");
        this.mEjbdir = projPath + File.separator + ejbdir;
    }
    
    public void setWardir(String wardir) {
        Project p = this.getProject();
        String projPath = p.getProperty("basedir");
        this.mWardir = projPath + File.separator + wardir;
    }
    
    public void setTemplateDir(String templateDir) {
        this.mTemplateDir = templateDir;
    }
    
    public void setForce(boolean force) {
        this.mForce = force;
    }

    public void execute() throws BuildException{  
        
        if (mSrcdir == null){
            throw new BuildException ("Must specify the eView source directory");
        }
        if (mEjbdir == null){
            throw new BuildException ("Must specify the ejb project directory");
        }
        if (mWardir == null){
            throw new BuildException ("Must specify the war project directory");
        }
        if (mTemplateDir==null){
            String modulePath = getProject().getProperty("module.install.dir");
            mTemplateDir= modulePath + File.separator + "ext" + File.separator + "eview";
        }
    
        // need to regenerate if source files have been modified
        if (modified()||mForce){
            try {
                File objectFile = new File(mSrcdir + File.separator +
                EviewProjectProperties.CONFIGURATION_FOLDER + File.separator +"object.xml");
                InputSource source = new InputSource(new FileInputStream(objectFile));
                EIndexObject eo = Utils.parseEIndexObject(source);
                String objName = eo.getName();        
        
                //generate business object files, web service files, database scripts,
                //application.xml and sun-application.xml
                generateFiles(eo);

                //generate jar file                      
                generateJars();

                //put ejb files in ebj project
                generateEbjFiles(eo);

                //put the web files into war project
                generateWarFiles();
                               
                //add lib to ejb project by modifing ejb project's project.properties file. 
                addEjbLib();

            }catch(Exception ex){
                throw new BuildException(ex.getMessage());         
            }
        
        }
    }



    public void generateFiles(EIndexObject eo ) throws FileNotFoundException, TemplateWriterException, ParserException, IOException{

        String ejbdir = mEjbdir + File.separator + "src" + File.separator + "java";
        File destDir = new File(ejbdir + "/com/sun/mdm/index/objects");
        Delete delete = (Delete) getProject().createTask("delete");
        delete.setDir(destDir);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();

        destDir = new File(ejbdir + "/com/sun/mdm/index/ops");
        delete.setDir(destDir);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();

        destDir = new File(ejbdir + "/com/sun/mdm/index/webservice");
        delete.setDir(destDir);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();

        destDir = new File(ejbdir + "/com/sun/mdm/index/ejb");
        delete.setDir(destDir);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();

        File newPath = null;
        newPath = new File(ejbdir + "/com/sun/mdm/index/objects");
        newPath.mkdirs();
        newPath = new File(ejbdir + "/com/sun/mdm/index/objects/validation");
        newPath.mkdirs();
        newPath = new File(ejbdir + "/com/sun/mdm/index/ops");
        newPath.mkdirs();
        newPath = new File(ejbdir + "/com/sun/mdm/index/webservice");
        newPath.mkdirs();            
        ObjectDescriptorWriter odw = new ObjectDescriptorWriter(ejbdir, eo);
        odw.write();

        EntityObjectWriter eow = new EntityObjectWriter(ejbdir, eo);
        eow.write();

        OPSWriter opsw = new OPSWriter(ejbdir, eo);
        opsw.write();
        
        WebServiceWriter wsWriter = new WebServiceWriter(ejbdir, eo);
        wsWriter.write();

        String tmpl = getCreateDDLWriterTemplate(eo.getDataBase());
        String outPath = mSrcdir + File.separator +
                EviewProjectProperties.DATABASE_SCRIPT_FOLDER + File.separator + "create.sql";
        DDLWriter tdw = new DDLWriter(outPath, eo, tmpl);
        tdw.write(true); 

        tmpl = getDropDDLWriterTemplate(eo.getDataBase());
        outPath = mSrcdir + File.separator + EviewProjectProperties.DATABASE_SCRIPT_FOLDER +
                File.separator + "drop.sql";
        tdw = new DDLWriter(outPath, eo, tmpl);
        tdw.write(false);
        
        String genpath = mSrcdir + File.separator + "conf";
        String ejbName = getProject().getProperty("ejb.dir")+".jar";
        String warName = getProject().getProperty("war.dir")+".war";
        AppXmlWriter appWriter = new AppXmlWriter(genpath, eo, ejbName, warName);
        appWriter.write();
        
        String projPath = getProject().getProperty("basedir");
        String generatePath = projPath + File.separator + 
                EviewProjectProperties.EVIEW_GENERATED_FOLDER;
        File generateFolder = new File(generatePath);
        generateFolder.mkdirs();       
        File xsdFile = new File(generatePath + File.separator + "outbound.xsd");
        OutboundXSDBuilder builder = new OutboundXSDBuilder();
        builder.buildXSD(eo,xsdFile);
        
        String jbiXmlpath = projPath + File.separator + 
                    EviewProjectProperties.EVIEW_GENERATED_FOLDER +
                    File.separator + "jbi"+ File.separator + "META-INF";
        File jbiXmlFolder = new File(jbiXmlpath);
        jbiXmlFolder.mkdirs();
        JbiXmlWriter jbrWriter = new JbiXmlWriter(jbiXmlpath,eo.getName());
        jbrWriter.write();
        
        File objectFile = new File(mSrcdir + File.separator +
                EviewProjectProperties.CONFIGURATION_FOLDER + File.separator +"object.xml");
        MetaDataService.registerObjectDefinition(new FileInputStream(objectFile));           
    }
    
    
    private void generate_eview_resources_jar(){
        
            String projPath = getProject().getProperty("basedir");
            File destDir = new File(projPath + File.separator + 
                    EviewProjectProperties.EVIEW_GENERATED_FOLDER + File.separator + "resource");
            
            Delete delete = (Delete) getProject().createTask("delete");
            delete.setDir(destDir);
            delete.init();
            delete.setLocation(getLocation());
            delete.execute();
            
            //copy configuration file           
            destDir.mkdir();
            File srcDir = new File(mSrcdir + File.separator + EviewProjectProperties.CONFIGURATION_FOLDER);
            FileSet srcfileSet = new FileSet(); 
            srcfileSet.setDir(srcDir);
            Copy copy = (Copy) getProject().createTask("copy");
            copy.setTodir(destDir);
            copy.addFileset(srcfileSet);
            copy.init();
            copy.setLocation(getLocation());
            copy.execute();
            
            //copy standardization file  
            destDir = new File(projPath + File.separator + EviewProjectProperties.EVIEW_GENERATED_FOLDER +
                    File.separator + "resource" + File.separator + "stand" );
            srcDir = new File(mSrcdir + File.separator + EviewProjectProperties.STANDARDIZATION_ENGINE_FOLDER);
            srcfileSet = new FileSet(); 
            srcfileSet.setDir(srcDir);
            copy = (Copy) getProject().createTask("copy");
            copy.setTodir(destDir);
            copy.addFileset(srcfileSet);
            copy.init();
            copy.setLocation(getLocation());
            copy.execute();
            
            //copy match engine file  
            destDir = new File(projPath + File.separator + EviewProjectProperties.EVIEW_GENERATED_FOLDER +
                    File.separator + "resource" + File.separator + "match" );
            srcDir = new File(mSrcdir + File.separator + EviewProjectProperties.MATCH_ENGINE_FOLDER);
            srcfileSet = new FileSet(); 
            srcfileSet.setDir(srcDir);
            copy = (Copy) getProject().createTask("copy");
            copy.setTodir(destDir);
            copy.addFileset(srcfileSet);
            copy.init();
            copy.setLocation(getLocation());
            copy.execute();
            
            //make resources.jar
            File jarFile = new File(projPath + File.separator + getProject().getProperty("build.dir") +
                    File.separator + "lib" + File.separator + "resources.jar" );
            if (jarFile.exists()){
                jarFile.delete();
            }
            srcDir = new File(projPath + File.separator + EviewProjectProperties.EVIEW_GENERATED_FOLDER +
                    File.separator + "resource");
            srcfileSet = new FileSet(); 
            srcfileSet.setDir(srcDir);
            Jar jar = (Jar) getProject().createTask("jar"); 
            jar.setDestFile(jarFile);
            jar.setCompress(true);
            jar.addFileset(srcfileSet);
            jar.setLocation(getLocation());
            jar.init ();
            jar.execute ();           
    }     
    
    
    private void generateJars() throws FileNotFoundException, IOException{
        
        String projPath   = getProject().getProperty("basedir");
        String buildPath  = getProject().getProperty("build.dir");
        String destPath   = projPath + File.separator + buildPath + File.separator + "lib";
        String srcPath    = mTemplateDir;
        File destDir = new File(destPath);
        
        Delete delete = (Delete) getProject().createTask("delete");
        delete.setDir(destDir);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();
       
        destDir.mkdir();
        
        FileSet srcFileSet = new FileSet(); 
        File srcDir = new File(srcPath);
        srcFileSet.setDir(srcDir);
        srcFileSet.setIncludes( "repository/stc_sbme.jar," +
                                "index-core.jar," +
                                "net.java.hulp.i18n.jar, " +
                                "net.java.hulp.i18ntask.jar");
        
        Copy copy = (Copy) getProject().createTask("copy");
        copy.init();
        copy.setTodir(destDir);
        copy.setFlatten(true);
        copy.addFileset(srcFileSet);
        copy.setLocation(getLocation());
        copy.execute();
        
        // make resources.jar
        generate_eview_resources_jar();
    }
    
    private void generateEbjFiles(EIndexObject eo){
        
        String srcPath    = mTemplateDir + File.separator + "repository" + File.separator + "ejb-source.zip";
        File srcFile = new File(srcPath);
        File destDir = new File(mEjbdir + File.separator + "src" + File.separator + "java");
        Expand expand = (Expand) getProject().createTask("unzip");
        expand.init();
        expand.setSrc(srcFile);
        expand.setDest(destDir);
        expand.setLocation(getLocation());
        expand.execute(); 
        
        String token= "_EVIEW_OBJECT_TOKEN_";
        String value = eo.getName();
        setEJBMappedName(token, value);
        setTransaction();
        
    }
    
    private void setTransaction(){
        String transaction = "LOCAL";
        File masterFile = new File(mSrcdir + File.separator + 
                EviewProjectProperties.CONFIGURATION_FOLDER + File.separator +"master.xml");
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(false);
            factory.setIgnoringElementContentWhitespace(true);
            factory.setIgnoringComments(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(masterFile);
            NodeList nodes = doc.getElementsByTagName("transaction"); 
            Node transactionNode = nodes.item(0);
            if (transactionNode.getFirstChild().getNodeValue()!=null){                
                transaction = transactionNode.getFirstChild().getNodeValue();
            }
        }catch(Exception e){
            transaction = "LOCAL";
        }
        HashMap<String, String> tokenMap;
        if (transaction.equalsIgnoreCase("CONTAINER")){
            tokenMap = new HashMap<String, String>();
            tokenMap.put("TransactionManagementType.BEAN","TransactionManagementType.CONTAINER");
            tokenMap.put("_CMT_XA__TOKEN","CMT_XA");            
        }else if(transaction.equalsIgnoreCase("BEAN")){
            tokenMap = new HashMap<String, String>();
            //tokenMap.put("TransactionManagementType.BEAN","TransactionManagementType.BEAN");
            tokenMap.put("_CMT_XA__TOKEN","BMT_XA");             
        }else{
            tokenMap = new HashMap<String, String>();
            //tokenMap.put("TransactionManagementType.BEAN","TransactionManagementType.BEAN");
            tokenMap.put("_CMT_XA__TOKEN","BMT_LOCAL");             
        }
                
        String ejbFilePath = mEjbdir + "/src/java/com/sun/mdm/index/ejb";
        String path = ejbFilePath + File.separator + "master" + File.separator + "MasterControllerEJB.java";
        replaceToken(path , tokenMap);   
    }
    
    private void generateWarFiles(){
        
        String srcPath    = mTemplateDir + File.separator + "repository" + File.separator + "edm.war";
        File srcFile = new File(srcPath);
        File destDir = new File(mWardir + File.separator + "web" + File.separator);
        PatternSet patternSet = new PatternSet();
        patternSet.setExcludes("**/*.jsp, **/META-INF/**");
        Expand expand = (Expand) getProject().createTask("unzip");
        expand.init();
        expand.setSrc(srcFile);
        expand.setDest(destDir);
        expand.addPatternset(patternSet);
        expand.setLocation(getLocation());
        expand.execute(); 
        
        srcFile = new File(mSrcdir + File.separator +
                EviewProjectProperties.CONFIGURATION_FOLDER + File.separator + "edm.xml");
        destDir = new File(mWardir + File.separator + "web" + File.separator + 
                "WEB-INF" + File.separator + "classes");
        Copy copy = (Copy) getProject().createTask("copy");
        copy.init();
        copy.setTodir(destDir);
        copy.setFile(srcFile);
        copy.setLocation(getLocation());
        copy.execute();
    }
    
    private void addEjbLib() throws FileNotFoundException, IOException{
                   
        String ejbPropertyPath = mEjbdir + File.separator + "nbproject" + File.separator + "project.properties";
        java.util.Properties properties = new java.util.Properties();
        properties.load(new FileInputStream(ejbPropertyPath));
        properties.setProperty("file.reference.index-core.jar", "../build/lib/index-core.jar");
        properties.setProperty("file.reference.net.java.hulp.i18ntask.jar", "../build/lib/net.java.hulp.i18ntask.jar");
        properties.setProperty("file.reference.net.java.hulp.i18n.jar", "../build/lib/net.java.hulp.i18n.jar");
        properties.setProperty("javac.classpath", "${file.reference.index-core.jar}:" +
                               "${file.reference.net.java.hulp.i18n.jar}:" + 
                               "${file.reference.net.java.hulp.i18n.jar}");

        properties.store(new FileOutputStream(ejbPropertyPath), null);
    }
    
    private boolean modified(){
        File folder = new File(getProject().getProperty("basedir") + File.separator + 
                EviewProjectProperties.EVIEW_GENERATED_FOLDER + File.separator + "resource");
        // First, an up-to-date check
        long genModified = getLastModifiedTime(folder);
        
        // check if any source files are newer.
        ArrayList<File> srcFolders = new ArrayList<File>();
        folder= new File(mSrcdir + File.separator + EviewProjectProperties.CONFIGURATION_FOLDER);
        srcFolders.add(folder);
        folder= new File(mSrcdir + File.separator + EviewProjectProperties.MATCH_ENGINE_FOLDER);
        srcFolders.add(folder);
        folder= new File(mSrcdir + File.separator + EviewProjectProperties.STANDARDIZATION_ENGINE_FOLDER);
        srcFolders.add(folder);
        long srcModified =  getLastModifiedTime(srcFolders);
        
        
        if (srcModified < genModified){
            return false;
        }else{
            return true;
        }
    }
    
    private long getLastModifiedTime(File folder){        
        long mod = Long.MIN_VALUE;
        long lastModified = Long.MIN_VALUE;
        File[] files = folder.listFiles(); 
        if (files!=null){
            for (int i = 0; i < files.length; i++) {
                if  (files[i].isFile()){
                        mod = files[i].lastModified();
                        if (mod > lastModified) {
                            lastModified = mod;
                        }

                }else{
                    mod = getLastModifiedTime(files[i]);
                    if ( mod > lastModified){
                        lastModified = mod;                
                    }
                }
            }
        }
        return lastModified;
    }
    
    private long getLastModifiedTime(ArrayList<File> folder){
        long mod = Long.MIN_VALUE;
        long lastModified = Long.MIN_VALUE; 
        for (int i = 0; i < folder.size(); i++) {
            mod = getLastModifiedTime(folder.get(i));
            if ( mod > lastModified){
                lastModified = mod;                
            }             
        }
        return lastModified;
    }
    
    private void setEJBMappedName(String token, String value){
        
        ArrayList<String> files = new ArrayList<String>();
        String ejbFilePath = mEjbdir + "/src/java/com/sun/mdm/index/ejb";
        String path = ejbFilePath + File.separator + 
                "codelookup" + File.separator + "CodeLookupEJB.java";
        files.add(path);
        path = ejbFilePath + File.separator +  
                "codelookup" + File.separator + "UserCodeLookupEJB.java";
        files.add(path);
        path = ejbFilePath + File.separator + 
                "master" + File.separator + "MasterControllerEJB.java";
        files.add(path);
        path = ejbFilePath + File.separator + 
                "page" + File.separator + "PageDataEJB.java";
        files.add(path);
        path = ejbFilePath + File.separator + 
                "report" + File.separator + "ReportGeneratorEJB.java";
        files.add(path);
        path = ejbFilePath + File.separator + 
                "report" + File.separator + "BatchReportGeneratorEJB.java";
        files.add(path);
        
        for (int i=0 ; i<files.size(); i++){
            String ejbFile = files.get(i).toString();
            replaceToken(ejbFile , token, value);
        }
    }
      
    private void replaceToken(String fileName , String token, String value){ 
        File file = new File(fileName);  
        File tempFile = new File(fileName + ".tmp");
        if (tempFile.exists()){
            tempFile.delete();
        }
        file.renameTo(tempFile);
        file = new File(fileName);

        try{
            BufferedReader buffIn = new BufferedReader(new FileReader(tempFile));
            BufferedWriter buffOut= new BufferedWriter(new FileWriter(file));        
            String strLine;            
            while ((strLine = buffIn.readLine()) != null) {
                buffOut.write(strLine.replaceAll(token, value));  
                buffOut.newLine();
            }
            buffIn.close();
            buffOut.close();    
        }catch(Exception ex){
            
            if (file.exists()){
                file.delete();
            }
            tempFile.renameTo(file);
            throw new BuildException("can not generate EJB mapped name in " + fileName);  
        }finally{
            tempFile = new File(fileName + ".tmp");
            if (tempFile.exists()){
                tempFile.delete();
            }
        }    
    }
    
    private void replaceToken(String fileName , HashMap<String, String> tokenMap){
	File file = new File(fileName);  
        File tempFile = new File(fileName + ".tmp");
        if (tempFile.exists()){
            tempFile.delete();
        }
        file.renameTo(tempFile);
        file = new File(fileName);

        try{
            BufferedReader buffIn = new BufferedReader(new FileReader(tempFile));
            BufferedWriter buffOut= new BufferedWriter(new FileWriter(file));        
            String strLine;            
            while ((strLine = buffIn.readLine()) != null) {
            	Iterator iter = tokenMap.keySet().iterator();
                while(iter.hasNext()){
                    String token = (String)iter.next();
                    String value = (String)tokenMap.get(token);
                    strLine = strLine.replaceAll(token, value);  
                }            	
              buffOut.write(strLine);  
              buffOut.newLine();
            }
            buffIn.close();
            buffOut.close();    
        }catch(Exception ex){            
            if (file.exists()){
                file.delete();
            }
            tempFile.renameTo(file);
            throw new BuildException("can not access " + fileName);  
        }finally{
            tempFile = new File(fileName + ".tmp");
            if (tempFile.exists()){
                tempFile.delete();
            }
        }
    }
    
    /**
     * Get the CreateDDLWriter template depending on the database vendor type
     * @param db database vendor type
     * @return relative path of the CreateDDLWriter template
     */
    private static String getCreateDDLWriterTemplate(String db) {
    	return getDDLWriterTemplate(db, "CreateDDLWriter.tmpl");
    }
    
    /**
     * Get the DropDDLWriter template depending on the database vendor type
     * @param db database vendor type
     * @return relative path of the DropDDLWriter template
     */    
    private static String getDropDDLWriterTemplate(String db) {
    	return getDDLWriterTemplate(db, "DropDDLWriter.tmpl");
    }    
    
    /**
     * Get the DDLWriter template depending on the database vendor type
     * @param db database vendor type
     * @param template template name
     * @return relative path of the DDLWriter template
     */
    private static String getDDLWriterTemplate(String db, String template) {
    	// we need to trim whitespaces since "SQL Server" has a space in it
    	return "com/sun/mdm/index/project/generator/persistence/templates/" + trimSpaces(db).toLowerCase() + "/" + template;   	
    }
    
    /**
     * Trim all the white spaces.
     * @param str string whose white spaces to be removed
     * @return a string without any white space
     */
    private static String trimSpaces(String str) {
    	if (str == null) {
    		return null;
    	}
    	
    	String tmp = "";
    	for (int i=0,j=0; j>-1; i=j+1) {
    		j = str.indexOf(' ', i);
    		if (j==-1) {
    			// last substriing
    			tmp += str.substring(i);
    		} else {
    			tmp += str.substring(i, j);
    		}
    	}
    	
    	return tmp;
    }
    
}
