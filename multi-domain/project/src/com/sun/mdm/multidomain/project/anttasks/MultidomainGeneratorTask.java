/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.project.anttasks;

import com.sun.mdm.multidomain.parser.MultiDomainModel;
import com.sun.mdm.multidomain.parser.ParserException;
import com.sun.mdm.multidomain.parser.Utils;
import com.sun.mdm.multidomain.project.EjbProjectManager;
import com.sun.mdm.multidomain.project.MultiDomainProjectProperties;
import com.sun.mdm.multidomain.project.generator.FileUtil;
import com.sun.mdm.multidomain.project.generator.descriptor.JbiXmlWriter;
import com.sun.mdm.multidomain.project.generator.descriptor.AppXmlWriter;
import com.sun.mdm.multidomain.project.generator.domainObjects.EntityObjectWriter;
import com.sun.mdm.multidomain.project.generator.exception.TemplateWriterException;
import com.sun.mdm.multidomain.project.generator.persistence.DDLWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Ant.Reference;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.taskdefs.Move;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PatternSet;
import org.xml.sax.InputSource;

/**
 * MultidomainGeneratorTask class.
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
            
            generateFiles();
                    
            // generate jar file
            generateJars();

            // put ejb files in ebj project
            generateEbjFiles();

            // add libs to ejb project by modifing ejb project's
            // project.properties file.
            addEjbLibs();

            // put the web files into war project
            generateWarFiles();
            
            File objectFile = new File(mSrcdir, MultiDomainProjectProperties.CONFIGURATION_FOLDER
                                + File.separator + MultiDomainProjectProperties.MULTI_DOMAIN_MODEL_XML);
            InputSource source = new InputSource(new FileInputStream(objectFile));
            MultiDomainModel mo = Utils.parseMultiDomainModel(source);
            // String applicationName = eo.getName();

            generateDBFiles(mo);
            
            
         } catch (Exception ex) {
//         String mode = System.getProperty("run.mode");
//         if (mode == null || !mode.equals("debug")) {
//         //delete "files-generated" folder when generation fails
//         String projPath = getProject().getProperty("basedir");
//         File destDir = new File(projPath,
//         EviewProjectProperties.EVIEW_GENERATED_FOLDER);
//         Delete delete = (Delete) getProject().createTask("delete");
//         delete.setDir(destDir);
//         delete.init();
//         delete.setLocation(getLocation());
//         delete.execute();
//         }
           mLog.severe("Could not generate MDM Multidomain Files.");
           throw new BuildException(ex.getMessage());
          }
        
    }
    
    private void generateFiles() throws BuildException{
        try {
            String projPath = getProject().getProperty("basedir");
            File genDir = new File(projPath + File.separator + MultiDomainProjectProperties.MULTIDOMAIN_GENERATED_FOLDER);
            //delete generated folder if there is a existing one
            File destDir = new File(genDir, "domain-ojects/java");
            Delete delete = (Delete) getProject().createTask("delete");
            delete.setDir(destDir);
            delete.init();
            delete.setLocation(getLocation());
            delete.execute();
            destDir.mkdirs();
            //generate domain object files at "files-generated/domain-ojects/java" directory     
            String multiDomainModelXml = mSrcdir.getAbsolutePath()+ File.separator+
                MultiDomainProjectProperties.CONFIGURATION_FOLDER+ File.separator+
                MultiDomainProjectProperties.MULTI_DOMAIN_MODEL_XML;
            MultiDomainModel mdModel = Utils.parseMultiDomainModel(multiDomainModelXml);
            ArrayList <String> domainList = mdModel.getDomainNames();
            //ArrayList <String> domainList = new ArrayList<String>();
            //domainList.add("Person");
            for (String  domain:domainList ){
                
                File objectFile = new File(mSrcdir,
                        MultiDomainProjectProperties.DOMAINS_FOLDER + 
                        "/" + domain.trim() + "/object.xml");
                generateDomainOjbects(destDir, objectFile);
           
            }           

            //generate jbi.xml at "files-generated/jbi/META-INF/jbi.xml"
            File jbiXmlFolder = new File(genDir, "/jbi/META-INF");
            String jbiJar = getProject().getProperty("jbi.jar");
            int endIndex= jbiJar.indexOf('.');
            String jbiName = jbiJar.substring(0, endIndex);
            JbiXmlWriter jbrWriter = new JbiXmlWriter(jbiXmlFolder, jbiName);
            jbrWriter.write();
            
            //generate application.xml   
            String appXmlFolderPath = mSrcdir.getAbsolutePath()+ File.separator + "conf";
            File appXmlFolder = new File(appXmlFolderPath);
            String ejbName = getProject().getProperty("ejb.dir") + ".jar";
            String warName = getProject().getProperty("war.dir") + ".war";
            String appName = ejbName.substring(0, ejbName.length() - 8); //TBD fix me
            AppXmlWriter appWriter = new AppXmlWriter(appXmlFolder, appName, ejbName, warName);
            appWriter.write();
            
        } catch (ParserException ex) {
            throw new BuildException(ex.getMessage());
        } catch (TemplateWriterException ex) {
            throw new BuildException(ex.getMessage());
        }

    }
    
    private void generateDomainOjbects(File destDir, File objectFile) throws BuildException{
        try {
            EntityObjectWriter eow = new EntityObjectWriter(destDir.getAbsolutePath(), objectFile);
            eow.write();
        } catch (TemplateWriterException ex) {
            throw new BuildException(ex.getMessage());
        }
    }
    

    private void generateDBFiles(MultiDomainModel multiDomainObj) throws BuildException{
        try {

           // multiDomainObj.get
            String tmpl = getDDLWriterTemplate(multiDomainObj.getDatabase(), "CreateMultiDomain.sql.tmpl");
            File outPath = new File(mSrcdir,
                    MultiDomainProjectProperties.DATABASE_SCRIPT_FOLDER + File.separator + "CreateMultiDomain.sql");
            DDLWriter tdw = new DDLWriter(outPath.getAbsolutePath(), multiDomainObj, tmpl);
            tdw.write(true);

            tmpl = getDDLWriterTemplate(multiDomainObj.getDatabase(), "DropMultiDomain.sql.tmpl");
            File outDropPath = new File(mSrcdir,
                    MultiDomainProjectProperties.DATABASE_SCRIPT_FOLDER + File.separator + "DropMultiDomain.sql");
            tdw = new DDLWriter(outDropPath.getAbsolutePath(), multiDomainObj, tmpl);
            tdw.write(true);
        } catch (Exception ex) {
            throw new BuildException(ex.getMessage());
        } 
    }
  
    /**
     * Get the DDLWriter template depending on the database vendor type
     * 
     * @param db
     *            database vendor type
     * @param template
     *            template name
     * @return relative path of the DDLWriter template
     */
    private static String getDDLWriterTemplate(String db, String template) {
        // we need to trim whitespaces since "SQL Server" has a space in it
        return "com/sun/mdm/multidomain/project/generator/persistence/templates/"
                + trimSpaces(db).toLowerCase() + "/" + template;
    }    

    /**
     * Trim all the white spaces.
     * 
     * @param str string whose white spaces to be removed
     * @return a string without any white space
     */
    private static String trimSpaces(String str) {
        if (str == null) {
            return null;
        }
        String tmp = "";
        for (int i = 0, j = 0; j > -1; i = j + 1) {
            j = str.indexOf(' ', i);
            if (j == -1) {
                // last substriing
                tmp += str.substring(i);
            } else {
                tmp += str.substring(i, j);
            }
        }
        return tmp;
    }
 

    private void makeDomainOjbectsJar() {
        String projPath = getProject().getProperty("basedir");
        File genDir = new File(projPath + File.separator + MultiDomainProjectProperties.MULTIDOMAIN_GENERATED_FOLDER);       
        String javacDebug = getProject().getProperty("javac.debug");        
        File destDir = new File(genDir, "domain-ojects/classes");
        // delete old class file
        Delete delete = (Delete) getProject().createTask("delete");
        delete.setDir(destDir);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();
        destDir.mkdirs();
        
        Javac javac = (Javac) getProject().createTask("javac");
        Path srcDir = new Path(getProject(), genDir + "/domain-ojects/java");
        javac.setEncoding("UTF-8");
        javac.setSrcdir(srcDir);
        javac.setDestdir(destDir);
        Reference ref = new Reference();
        ref.setProject(getProject());
        ref.setRefId("generate.class.path");
        javac.setClasspathRef(ref); 
        javac.init();
        javac.setLocation(getLocation());
        if (null!=javacDebug &&javacDebug.equalsIgnoreCase("true")){
            javac.setDebug(true);
        }else{
            javac.setDebug(false);
        }
        javac.execute();
        Jar jar = (Jar) getProject().createTask("jar");

        File jarFile = new File(projPath + File.separator + "lib"
                + File.separator + "multidomain-client.jar");
        jar.setDestFile(jarFile);
        jar.setBasedir(destDir);
        jar.init();
        jar.setLocation(getLocation());
        jar.execute();
    }
    

    private void generateEbjFiles() throws IOException {

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
        
        File ejbConfDir = new File(mEjbdir, "src/conf"); 
        File sunEjbJarXml = new File(mEjbdir, "src/java/sun-ejb-jar.xml");
        Move move = (Move) getProject().createTask("move");
        move.setTodir(ejbConfDir);
        move.setFile(sunEjbJarXml);
        move.init();
        move.setLocation(getLocation());
        move.execute();
                
        String token = "MULTIDOMAIN_APPLICATION_TOKEN_";
        String earName = getProject().getProperty("jar.name");
        int endIndex= earName.indexOf('.');
        String value = earName.substring(0, endIndex);
        
//        String value = eo.getName();
        setEJBMappedName(token, value);
//        setTransaction();
//        setRoles();
//        setSunEjbJarXML();

    }
    
    private void generateWarFiles() throws IOException {
        
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
        //patternSet.setExcludes("**/edm.xml");
        //patternSet.setExcludes("**/object.xml");
        //patternSet.setExcludes("**/roles.xml");
        //patternSet.setExcludes("**/midm.xml");
        Expand expand = (Expand) getProject().createTask("unzip");
        expand.init();
        expand.setSrc(srcFile);
        expand.setDest(destDir);
        expand.addPatternset(patternSet);
        expand.setLocation(getLocation());
        expand.execute();
        
        
        String projPath = getProject().getProperty("basedir");
        File srcDir = new File(projPath + "/lib");
        FileSet srcFileSet = new FileSet();
        srcFileSet.setDir(srcDir);
        srcFileSet.setIncludes("resources.jar" );
        destDir = new File(mWardir, "web/WEB-INF/lib");
        Copy copy = (Copy) getProject().createTask("copy");
        copy.init();
        copy.setTodir(destDir);
        copy.setFlatten(true);
        copy.addFileset(srcFileSet);
        copy.setLocation(getLocation());
        copy.execute();

        //set context root
	File sunWebXml= new File(mWardir, "web/WEB-INF/sun-web.xml");
        String appName = getProject().getProperty("jar.name"); // TBD fix me 
	FileUtil.replaceTokenInFile(sunWebXml, 
                     MultiDomainProjectProperties.MDWM_CONTEXT_ROOT_TOEKN, 
                     "/" + appName.substring(0, appName.length() - 4) + "MDWM");   
                  
    }
    
    private void addEjbLibs() throws FileNotFoundException, IOException, Exception {
        ArrayList<String> libs =new ArrayList<String>();
        libs.add("multidomain-core.jar");
        libs.add("multidomain-client.jar");
        libs.add("resources.jar");
        libs.add("index-core.jar");
        libs.add("net.java.hulp.i18n.jar");
        EjbProjectManager.addLibsToEjbProject(mEjbdir, libs, "../lib");
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
        srcFileSet.setIncludes("multidomain/multidomain-core.jar");
        
        //copy master index jar files
        FileSet srcFileSet2 = new FileSet();
        File srcDir2 = null;
        
        String netbeansPath = getProject().getProperty("nbplatform.default.netbeans.dest.dir");
        String userPath = getProject().getProperty("netbeans.user");
        File userPathIndexCoreJar = new File(userPath + "/modules/ext/mdm/index-core.jar");
        File netbeansPathIndexCoreJar = new File(netbeansPath + "/soa2/modules/ext/mdm/index-core.jar");
        if (userPathIndexCoreJar.exists()){
            srcDir2 = new File(userPath + "/modules/ext/mdm");
        }else if (netbeansPathIndexCoreJar.exists()){
            srcDir2 = new File(netbeansPath + "/soa2/modules/ext/mdm");
        }else{
            throw new BuildException(   "Could not locate the Master Index Module. ");
        }
        srcFileSet2.setDir(srcDir2);
        srcFileSet2.setIncludes("index-core.jar," + "net.java.hulp.i18n.jar" );
        
        
        //copy user plugin.jar 
        FileSet srcFileSet3 = new FileSet();
        File srcDir3 = new File(projPath + "/src/Plug-ins");
        srcFileSet3.setDir(srcDir3);
        srcFileSet3.setIncludes("*.jar" );
        
        Copy copy = (Copy) getProject().createTask("copy");
        copy.init();
        copy.setTodir(destDir);
        copy.setFlatten(true);
        copy.addFileset(srcFileSet);
        copy.addFileset(srcFileSet2);
        copy.addFileset(srcFileSet3);
        copy.setLocation(getLocation());
        copy.execute();
               
        //make resources.jar
        makeResourcesJar();
        //make master-index-domain-objects.jar
        makeDomainOjbectsJar();
    }
    
    private void makeResourcesJar() throws Exception {        
        String projPath = getProject().getProperty("basedir");
        File genDir = new File(projPath + File.separator
                + MultiDomainProjectProperties.MULTIDOMAIN_GENERATED_FOLDER);
        File destDir = new File(genDir,"resource");

        Delete delete = (Delete) getProject().createTask("delete");
        delete.setDir(destDir);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();
        destDir.mkdir();
        
        // copy configuration file      
        File srcDir = new File(mSrcdir, MultiDomainProjectProperties.CONFIGURATION_FOLDER);
        FileSet srcFileSet = new FileSet();
        srcFileSet.setDir(srcDir);
        
        // copy domain object.xml file
        File srcDir2 = mSrcdir;
        FileSet srcFileSet2 = new FileSet();
        srcFileSet2.setDir(srcDir2);
        srcFileSet2.setIncludes(MultiDomainProjectProperties.DOMAINS_FOLDER+"/**/object.xml");
        srcFileSet2.setIncludes(MultiDomainProjectProperties.DOMAINS_FOLDER+"/**/domain.xml");
                
        Copy copy = (Copy) getProject().createTask("copy");
        copy.setTodir(destDir);
        copy.addFileset(srcFileSet);
        copy.addFileset(srcFileSet2);
        copy.init();
        copy.setLocation(getLocation());
        copy.execute();
                        
        // make resources.jar
        File jarFile = new File(projPath + "/lib/resources.jar");
        if (jarFile.exists()) {
            jarFile.delete();
        }
        srcDir = new File(genDir, "resource");
        srcFileSet = new FileSet();
        srcFileSet.setDir(srcDir);
        Jar jar = (Jar) getProject().createTask("jar");
        jar.setDestFile(jarFile);
        jar.setCompress(true);
        jar.addFileset(srcFileSet);
        jar.setLocation(getLocation());
        jar.init();
        jar.execute();
    }
    
    private void setEJBMappedName(String token, String value) throws IOException {

        ArrayList<String> files = new ArrayList<String>();
        String ejbFilePath = mEjbdir.getAbsolutePath()
                + "/src/java/com/sun/mdm/multidomain/ejb/service";
        String path = ejbFilePath + "/MultiDomainMetaServiceBean.java";
        files.add(path);
        path = ejbFilePath + "/MultiDomainServiceBean.java";
        files.add(path);

        for (int i = 0; i < files.size(); i++) {
            File ejbFile = new File(files.get(i));
            FileUtil.replaceTokenInFile(ejbFile, token, value);
        }
    }
    

}



