/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.project.anttasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;

/**
 *
 * @author jlu
 */
public class MultidomainGeneratorTask extends Task {
    private File mSrcdir;
    private File mEjbdir;
    private File mWardir;
    private File mMdmModulesDir;
    private File mTemplateDir;
    private boolean mForce = false;
    private static final Logger mLog = Logger.getLogger(MultidomainGeneratorTask.class.getName());
        
    public void setSrcdir(File srcdir) {
        this.mSrcdir = srcdir;
    }

    public void setEjbdir(File ejbdir) {
        this.mEjbdir = ejbdir;
    }

    public void setWardir(File wardir) {
        this.mWardir = wardir;
    }

    public void setMDMModulesDir(File mdmModulesDir) {
        this.mMdmModulesDir = mdmModulesDir;
        this.mTemplateDir= new File (mdmModulesDir, "multidomain");
    }

    public void setForce(boolean force) {
        this.mForce = force;
    }
    
    public void execute() throws BuildException {

        if (mSrcdir == null||!mSrcdir.exists()) {
            throw new BuildException(
                "The attribute,\"srcdir\", must be set to " +
                "Mutidomain Project source directory.");
        }
        if (mEjbdir == null||!mEjbdir.exists()) {
            throw new BuildException("Must specify the ejb project directory");
        }
        if (mWardir == null||!mWardir.exists()) {
            throw new BuildException("Must specify the war project directory");
        }
        if (mMdmModulesDir == null) {
            String modulePath = getProject().getProperty("module.install.dir");
            mMdmModulesDir = new File(modulePath + "/ext/mdm");
            mTemplateDir = new File(modulePath + "/ext/mdm/multidomain");
        }
        
        // need to regenerate if source files have been modified
        try {
            // generate jar file
            generateJars();

            // put ejb files in ebj project
            generateEbjFiles();

            // add lib to ejb project by modifing ejb project's
            // project.properties file.
            addEjbLib();

            // put the web files into war project
            generateWarFiles();
            } catch (Exception ex) {
//                String mode = System.getProperty("run.mode");
//                if (mode == null || !mode.equals("debug")) {
//                   //delete "files-generated" folder when generation fails
//                   String projPath = getProject().getProperty("basedir");
//                   File destDir = new File(projPath,
//                   EviewProjectProperties.EVIEW_GENERATED_FOLDER);
//                   Delete delete = (Delete) getProject().createTask("delete");
//                   delete.setDir(destDir);
//                   delete.init();
//                   delete.setLocation(getLocation());
//                   delete.execute();
//                }
                mLog.severe("Could not generate MDM Multidomain Files.");
                throw new BuildException(ex.getMessage());
            }

               
        
    }
    
    private void generateEbjFiles() {

        File destDir = new File(mEjbdir, "src/java/com/sun/mdm/multidomain/ejb/service");
        Delete delete = (Delete) getProject().createTask("delete");
        delete.setDir(destDir);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();

        File srcFile = new File(mTemplateDir, "ejb-source.zip");
        destDir = new File(mEjbdir, "src/java");
        Expand expand = (Expand) getProject().createTask("unzip");
        expand.init();
        expand.setSrc(srcFile);
        expand.setDest(destDir);
        expand.setLocation(getLocation());
        expand.execute();

//        String token = "_EVIEW_OBJECT_TOKEN_";
//        String value = eo.getName();
//        setEJBMappedName(token, value);
//        setTransaction();
//        setRoles();
//        setSunEjbJarXML();

    }
    
    private void generateWarFiles() {
        
        File destDir = new File(mWardir, "web");
        FileSet fileSet = new FileSet();
        fileSet.setDir(destDir);
        //fileSet.setExcludes("**/lib/*.jar");
        Delete delete = (Delete) getProject().createTask("delete");
        //delete.setDir(destDir);
        delete.addFileset(fileSet);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();
        destDir.mkdir();

        File srcFile = new File(mTemplateDir, "multidomain-webapp.war");
        PatternSet patternSet = new PatternSet();
        patternSet.setExcludes("**/META-INF/**");
        
        patternSet.setExcludes("WEB-INF/classes/**/*.java");
//        patternSet.setExcludes("**/edm.xml");
//        patternSet.setExcludes("**/object.xml");
//        patternSet.setExcludes("**/roles.xml");
//        patternSet.setExcludes("**/midm.xml");
        Expand expand = (Expand) getProject().createTask("unzip");
        expand.init();
        expand.setSrc(srcFile);
        expand.setDest(destDir);
        expand.addPatternset(patternSet);
        expand.setLocation(getLocation());
        expand.execute();

//        FileSet srcFileSet = new FileSet();
//        File srcDir = new File(mSrcdir,
//                EviewProjectProperties.CONFIGURATION_FOLDER);
//        destDir = new File(mWardir, "web/WEB-INF/classes");
//        srcFileSet.setDir(srcDir);
//                if (null != edmVersion
//                && edmVersion.equalsIgnoreCase("master-index-edm")) {
//            srcFileSet.setIncludes("midm.xml");
//        } else {
//            srcFileSet.setIncludes("edm.xml");
//        }
//        Copy copy = (Copy) getProject().createTask("copy");
//        copy.init();
//        copy.setTodir(destDir);
//        copy.addFileset(srcFileSet);
//        copy.setLocation(getLocation());
//        copy.execute();
//
//        //set context root
//        if (null != edmVersion && edmVersion.equalsIgnoreCase("master-index-edm")) {
//            String token = "/SunEdm";
//            String sunWebXml= (mWardir.getAbsolutePath()+"/web/WEB-INF/sun-web.xml");
//            replaceToken(sunWebXml, token, "/"+applicationName+"MIDM" );         
//        }               
    }
    
    private void addEjbLib() throws FileNotFoundException, IOException {

        File ejbPropertyFile = new File(mEjbdir, "nbproject/project.properties");
        java.util.Properties properties = new java.util.Properties();
        properties.load(new FileInputStream(ejbPropertyFile));
        properties.setProperty("file.reference.index-core.jar",
                "../lib/index-core.jar");
        properties.setProperty("file.reference.multidomain-core.jar",
                "../lib/multidomain-core.jar");
        properties.setProperty("file.reference.net.java.hulp.i18n.jar",
                "../lib/net.java.hulp.i18n.jar");
        properties.setProperty("javac.classpath",
                "${file.reference.index-core.jar}:"
                        + "${file.reference.multidomain-core.jar}:"
                        + "${file.reference.net.java.hulp.i18n.jar}");

        properties.store(new FileOutputStream(ejbPropertyFile), null);
        
        String libs = "<included-library files=\"1\">file.reference.index-core.jar</included-library>"+
            "<included-library files=\"1\">file.reference.net.java.hulp.i18n.jar</included-library>"+
            "<included-library files=\"1\">file.reference.multidomain-core.jar</included-library>";
        String token = "</data>";
        
        File ejbProjectXml = new File(mEjbdir,"nbproject/project.xml");
        
        replaceToken(ejbProjectXml.getAbsolutePath(),token,libs+token);

    }
    
    private void generateJars() throws FileNotFoundException, IOException,
            Exception {

        String projPath = getProject().getProperty("basedir");
        String destPath = projPath + File.separator + "lib";
        File destDir = new File(destPath);

        Delete delete = (Delete) getProject().createTask("delete");
        delete.setDir(destDir);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();

        destDir.mkdir();

        FileSet srcFileSet = new FileSet();
        File srcDir = mMdmModulesDir;
        srcFileSet.setDir(srcDir);
        srcFileSet.setIncludes("multidomain/multidomain-core.jar, " +
                "index-core.jar," + "net.java.hulp.i18n.jar" );
        Copy copy = (Copy) getProject().createTask("copy");
        copy.init();
        copy.setTodir(destDir);
        copy.setFlatten(true);
        copy.addFileset(srcFileSet);
        copy.setLocation(getLocation());
        copy.execute();

        //make resources.jar
//        makeResourcesJar();
        //make master-index-client.jar
//        makeClientJar();
    }
    
    private void replaceToken(String fileName, String token, String value) {
        File file = new File(fileName);
        File tempFile = new File(fileName + ".tmp");
        if (tempFile.exists()) {
            tempFile.delete();
        }
        file.renameTo(tempFile);
        file = new File(fileName);

        try {
            BufferedReader buffIn = new BufferedReader(new FileReader(tempFile));
            BufferedWriter buffOut = new BufferedWriter(new FileWriter(file));
            String strLine;
            while ((strLine = buffIn.readLine()) != null) {
                buffOut.write(strLine.replaceAll(token, value));
                buffOut.newLine();
            }
            buffIn.close();
            buffOut.close();
        } catch (Exception ex) {

            if (file.exists()) {
                file.delete();
            }
            tempFile.renameTo(file);
                        throw new BuildException("can not replace token in "
                    + fileName);
        } finally {
            tempFile = new File(fileName + ".tmp");
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    
    
}



