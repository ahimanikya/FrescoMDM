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
package com.sun.mdm.multidomain.project;

//import com.sun.mdm.index.project.generator.descriptor.SunEjbJarWriter;
//import com.sun.mdm.index.project.generator.exception.TemplateWriterException;
//import com.sun.mdm.index.project.generator.persistence.DDLWriter;
import com.sun.mdm.multidomain.parser.RelationshipModel;
import com.sun.mdm.multidomain.parser.Utils;
import com.sun.mdm.multidomain.parser.ParserException;
import com.sun.mdm.multidomain.project.wizard.WizardProperties;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.io.FileInputStream;

import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.FileLock;

import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.EditableProperties;
import org.netbeans.spi.project.support.ant.ProjectGenerator;
import org.netbeans.modules.j2ee.deployment.devmodules.api.AntDeploymentHelper;
import org.netbeans.modules.j2ee.deployment.devmodules.api.Deployment;
import org.openide.modules.InstalledFileLocator;
import org.openide.loaders.DataObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.netbeans.modules.j2ee.deployment.devmodules.api.J2eeModule;
import org.netbeans.modules.j2ee.deployment.devmodules.api.J2eePlatform;
import org.xml.sax.InputSource;
//import com.sun.mdm.index.util.Logger;
import com.sun.mdm.multidomain.util.Logger;

/**
 * Create a fresh EjbProject from scratch or by importing and exisitng web module 
 * in one of the recognized directory structures.
 *
 */
public class MultiDomainProjectGenerator {
    private static final Logger mLogger = Logger.getLogger(
            MultiDomainProjectGenerator.class.getName()
        );

    public static final String DEFAULT_DOC_BASE_FOLDER = "conf"; //NOI18N
    public static final String DEFAULT_SRC_FOLDER = "src"; //NOI18N
    public static final String DEFAULT_RESOURCE_FOLDER = "setup"; //NOI18N
    public static final String DEFAULT_BPELASA_FOLDER = "bpelasa"; //NOI18N
    public static final String DEFAULT_BUILD_DIR = "build"; //NOI18N
    public static final String DEFAULT_NBPROJECT_DIR = "nbproject"; //NOI18N
    public static final String WAR_NAME = "war.name"; //NOI18N

    public static String xlateToken_EEP = "EviewEnterpriseApplication"; // NOI18N   
    public static String xlateFrom_EarProjectType = "org.netbeans.modules.j2ee.earproject"; // NOI18N                
    public static String xlateTo_ProjectType = "com.sun.mdm.multidomain.project"; // NOI18N  
    public static String xlateFrom_ProjectTypeNS = "http://www.netbeans.org/ns/j2ee-earproject/2"; // NOI18N       
    public static String xlateTo_ProjectTypeNS = "http://www.netbeans.org/ns/j2ee-eviewproject/2"; // NOI18N                    
    public static String[] xlateFiles = {
            "project.xml", // NOI18N
            "application.xml", // NOI18N   
            "sun-application.xml", // NOI18N 
            "sun-web.xml", // NOI18N   
            "project.properties" // NOI18N

    };
    public static String[] excludedFiles = {
            "build.xml", // NOI18N
            "build-impl.xml", // NOI18N   
            "genfiles.properties", // NOI18N 
            "private.properties" // NOI18N   
    };
    private static String MULTIDOMAIN_ENTERPRISE_PROJECT_EJB = "EviewEnterpriseApplication-ejb";
    private static String MULTIDOMAIN_ENTERPRISE_PROJECT_WAR = "EviewEnterpriseApplication-war";
    private static String PRIVATE_PROPERTIES = File.separator + "nbproject" + File.separator + "private" + File.separator + "private.properties";
    private FileObject srcRoot;
    private MultiDomainProjectGenerator() {}

    /**
     * Create a new empty J2SE project.
     * @param dir the top-level directory (need not yet exist but if it does it must be empty)
     * @param mainProjectName the code name for the project
     * @return the helper object permitting it to be further customized
     * @throws IOException in case something went wrong
     */
    public static AntProjectHelper createProject(WizardDescriptor wDesc) throws IOException {
        File dir = (File) wDesc.getProperty(WizardProperties.PROJECT_DIR);
        FileObject fo = FileUtil.createFolder(dir);
        AntProjectHelper projHelper = setupProject(fo);
        FileObject srcRoot = fo.createFolder(DEFAULT_SRC_FOLDER); // NOI18N
        String serverInstanceID = (String)wDesc.getProperty("serverInstanceID");
        String mainProjectName = (String) wDesc.getProperty(WizardProperties.NAME);
        String j2eeLevel = (String)wDesc.getProperty(WizardProperties.J2EE_LEVEL);

        try{
            createEjbWar(fo, mainProjectName, serverInstanceID, j2eeLevel);           
        } catch (MultiDomainRepositoryException ex) {
            throw new IOException(ex.toString());         
        }

        //set project properties
        EditableProperties ep = projHelper.getProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH);
        ep.setProperty(MultiDomainProjectProperties.EJB_DIR, mainProjectName + "-ejb");
        ep.setProperty(MultiDomainProjectProperties.WAR_DIR, mainProjectName + "-war");
        ep.setProperty("eView.generated.dir", 
                       MultiDomainProjectProperties.MULTIDOMAIN_GENERATED_FOLDER);
        ep.setProperty(MultiDomainProjectProperties.J2EE_SERVER_TYPE, 
                       Deployment.getDefault().getServerID(serverInstanceID));
        ep.setProperty(MultiDomainProjectProperties.J2EE_PLATFORM, j2eeLevel);
        ep.setProperty(MultiDomainProjectProperties.MULTIDOMAIN_JBI_JAR, 
                       mainProjectName+".jar");
        ep.setProperty(MultiDomainProjectProperties.JBI_SE_TYPE, 
                       MultiDomainProjectProperties.JAVA_EE_SE_COMPONENT_NAME);
        ep.setProperty(MultiDomainProjectProperties.SE_DEPLOYMENT_JAR,
                       "${dist.dir}/jbi/${jbi.jar}");
        ep.setProperty(MultiDomainProjectProperties.SRC_DIR,"src");
        
        projHelper.putProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH, ep);
               
        // set private properties
        ep = projHelper.getProperties(AntProjectHelper.PRIVATE_PROPERTIES_PATH);        
        ep.setProperty(MultiDomainProjectProperties.J2EE_SERVER_INSTANCE, serverInstanceID);
        
        File deployAntPropsFile = AntDeploymentHelper.getDeploymentPropertiesFile(serverInstanceID);
        if (deployAntPropsFile != null) {
            ep.setProperty(MultiDomainProjectProperties.DEPLOY_ANT_PROPS_FILE, deployAntPropsFile.getAbsolutePath());
        }
        
        projHelper.putProperties(AntProjectHelper.PRIVATE_PROPERTIES_PATH, ep);
        
        MultiDomainApplication multiDomainApplication = (MultiDomainApplication) ProjectManager.getDefault().findProject(projHelper.getProjectDirectory ());
        // Set Application name and Object 
        //multiDomainApplication.setApplicationName(wDesc.getProperty(com.sun.mdm.index.project.ui.wizards.Properties.PROP_TARGET_VIEW_NAME).toString());
        //multiDomainApplication.setObjectName(wDesc.getProperty(com.sun.mdm.index.project.ui.wizards.Properties.PROP_OBJECT_NAME).toString());
        try{
           createConfigFile(srcRoot);
        } catch (MultiDomainRepositoryException ex) {
            throw new IOException(ex.toString());         
        }

        ProjectManager.getDefault().saveProject(multiDomainApplication);
        
        // do it again to overwrite mysterious garbage unicode
        ep = projHelper.getProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH);
        //CLIENT_MODULE_URI
        ep.setProperty(MultiDomainProjectProperties.CLIENT_MODULE_URI, mainProjectName + "-war");
        //JAR_NAME
        ep.setProperty(MultiDomainProjectProperties.JAR_NAME, mainProjectName + ".ear");

        //project.??-ejb=??-ejb
        ep.setProperty("project." + mainProjectName + "-ejb", mainProjectName + "-ejb");
        //project.??-war
        ep.setProperty("project." + mainProjectName + "-war", mainProjectName + "-war");
        //reference.??-ejb.dist-ear=${project.??-ejb}/dist/??-ejb.jar
        ep.setProperty("reference." + mainProjectName + "-ejb.dist-ear", "${project." + mainProjectName + "-ejb}" + "/dist/" + mainProjectName + "-ejb.jar");
        //reference.??-war.dist-ear=${project.??-war}/dist/??-war.war
        ep.setProperty("reference." + mainProjectName + "-war.dist-ear", "${project." + mainProjectName + "-war}" + "/dist/" + mainProjectName + "-war.war");
        /*
        //JAR_CONTENT_ADDITIONAL
        //jar.content.additional=\
            //${reference.??-ejb.dist-ear};\
            //${reference.??-war.dist-ear}
        ep.setProperty(MultiDomainProjectProperties.JAR_CONTENT_ADDITIONAL, mainProjectName + "\\" + "\n" + "${reference." + mainProjectName + "-ejb.dist-ear};" + "\\" + "\n" +
                "${reference." + mainProjectName + "-war.dist-ear}");
         */ 

        projHelper.putProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH, ep);
        
        //JAR_NAME of war
        String ppath = mainProjectName + "-war" + File.separator + AntProjectHelper.PROJECT_PROPERTIES_PATH;
        ep = projHelper.getProperties(ppath);
        ep.setProperty("war.ear.name", mainProjectName + "-war.war");
        ep.setProperty("war.name", mainProjectName + "-war.war");
        //project.�?�??-ejb=../�?�??-ejb
        ep.setProperty("project." + mainProjectName + "-ejb", ".." + File.separator + mainProjectName + "-ejb");
        //reference.�?�??-ejb.dist=${project.�?�??-ejb}/dist/�?�??-ejb.jar
        ep.setProperty("reference." + mainProjectName + "-ejb.dist", "${project." + mainProjectName + "-ejb}" + File.separator + "dist" + File.separator + mainProjectName + "-ejb.jar");
        
        projHelper.putProperties(ppath, ep);
        //JAR_NAME of ejb
        ppath = mainProjectName + "-ejb" + File.separator + AntProjectHelper.PROJECT_PROPERTIES_PATH;
        ep = projHelper.getProperties(ppath);
        ep.setProperty(MultiDomainProjectProperties.JAR_NAME, mainProjectName + "-ejb.jar");
        projHelper.putProperties(ppath, ep);
        
        return projHelper;
    }

    private static void createConfigFile(FileObject srcRoot)
            throws MultiDomainRepositoryException, IOException{
                 
            MultiDomainRepository repository  = MultiDomainRepository.getMultiDomainRepository();  
            
            // *** Sub folder - Configuration ***
            FileObject folder = srcRoot.createFolder(MultiDomainProjectProperties.CONFIGURATION_FOLDER); // NOI18N     
            String str;            
            // RELATIONSHIP_MODEL_XML
            str = "";
            repository.createConfigurationFile(folder, MultiDomainProjectProperties.RELATIONSHIP_MODEL_XML, str);
            // RELATIONSHIP_WEB_MANAGER_XML
            str = "";
            repository.createConfigurationFile(folder, MultiDomainProjectProperties.RELATIONSHIP_WEB_MANAGER_XML, str);            
            
            // *** Sub folder - Database Script ***
            folder = srcRoot.createFolder(MultiDomainProjectProperties.DATABASE_SCRIPT_FOLDER); // NOI18N
            //generateDDLFiles(srcRoot);
            str = getDBScriptRelationshipModel();
            repository.createConfigurationFile(folder, MultiDomainProjectProperties.CREATE_RELATIONSHIPS_SQL, str);        
            str = getDBScriptAttributeMappings();
            repository.createConfigurationFile(folder, MultiDomainProjectProperties.CREATE_MAPPINGS_SQL, str);                
            
            folder = srcRoot.createFolder(MultiDomainProjectProperties.RELATIONSHIP_PLUGINS_FOLDER); // NOI18N
    }
    
    private static void createEjbWar(FileObject projectDir, String mainProjectName, String serverInstanceID, String j2eeLevel )
            throws MultiDomainRepositoryException, FileNotFoundException, IOException{
        MultiDomainRepository repository  = MultiDomainRepository.getMultiDomainRepository(); 
        // Unzip -ejb and -war projects from template
        FileObject template = repository.getInstalledFile(MultiDomainProjectProperties.TEMPLATE_ZIP);
        unZipFile(template, projectDir);
        // Rename template directories here
        Enumeration e = projectDir.getFolders(true);
        while (e.hasMoreElements()) {
            FileObject subfolder = (FileObject) e.nextElement();
            if (ProjectManager.getDefault().isProject(subfolder)) {
                String subfolderName;
                subfolderName = subfolder.getName();
                if (subfolderName.equals(MULTIDOMAIN_ENTERPRISE_PROJECT_EJB)) {
                    subfolderName = mainProjectName + "-ejb";
                } else if (subfolderName.equals(MULTIDOMAIN_ENTERPRISE_PROJECT_WAR)) {
                    subfolderName = mainProjectName + "-war";
                }
                FileLock lock = subfolder.lock();
                try {
                    subfolder.rename(lock, subfolderName, null);
                } finally {
                    lock.releaseLock();
                }
            }
        }
        
        J2eePlatform j2eePlatform = Deployment.getDefault().getJ2eePlatform(serverInstanceID);
        File deployAntPropsFile = AntDeploymentHelper.getDeploymentPropertiesFile(serverInstanceID);
        //set private property of ejb project
        FileObject ejbProjDir =projectDir.getFileObject(mainProjectName+"-ejb");
        FileOutputStream propsFile = new FileOutputStream(FileUtil.toFile(ejbProjDir).getAbsolutePath() + PRIVATE_PROPERTIES);
        EditableProperties props = new EditableProperties();
        //props.load(propsFile.getInputStream());
        props.setProperty(MultiDomainProjectProperties.J2EE_SERVER_INSTANCE, serverInstanceID);
        // set j2ee.platform.classpath
        if (!j2eePlatform.getSupportedSpecVersions(J2eeModule.EJB).contains(j2eeLevel)) {
            mLogger.warn("J2EE level:" + j2eeLevel + " not supported by server " + Deployment.getDefault().getServerInstanceDisplayName(serverInstanceID) + " for module type EJB"); // NOI18N
        }
        String classpath = toClasspathString(j2eePlatform.getClasspathEntries());
        props.setProperty(MultiDomainProjectProperties.J2EE_PLATFORM_CLASSPATH,classpath);
                
        // set j2ee.platform.wscompile.classpath
        if (j2eePlatform.isToolSupported(J2eePlatform.TOOL_WSCOMPILE)) {
            classpath = toClasspathString(j2eePlatform.getClasspathEntries());
            props.setProperty(MultiDomainProjectProperties.J2EE_PLATFORM_WSCOMPILE_CLASSPATH,classpath);
        }
        
        // ant deployment support
        File projectFolder = FileUtil.toFile(ejbProjDir);
        try {
            AntDeploymentHelper.writeDeploymentScript(new File(projectFolder, MultiDomainProjectProperties.ANT_DEPLOY_BUILD_SCRIPT),
                    J2eeModule.EJB, serverInstanceID);
            
            //new SunEjbJarWriter(projectFolder).write();
        } catch (IOException ioe) {
            mLogger.severe(ioe);
        }
        if (deployAntPropsFile != null) {
            props.setProperty(MultiDomainProjectProperties.DEPLOY_ANT_PROPS_FILE, deployAntPropsFile.getAbsolutePath());
        }
        props.store(propsFile);
        propsFile.close();
        //set private property of web project
        FileObject webProjDir =projectDir.getFileObject(mainProjectName+"-war");
        propsFile = new FileOutputStream(FileUtil.toFile(webProjDir).getAbsolutePath()+ PRIVATE_PROPERTIES);
        props = new EditableProperties();
        //props.load(propsFile.getInputStream());
        props.setProperty(MultiDomainProjectProperties.J2EE_SERVER_INSTANCE, serverInstanceID);
        
        // set j2ee.platform.classpath
        if (!j2eePlatform.getSupportedSpecVersions(J2eeModule.WAR).contains(j2eeLevel)) {
            mLogger.warn("J2EE level:" + j2eeLevel + " not supported by server " + Deployment.getDefault().getServerInstanceDisplayName(serverInstanceID) + " for module type WAR"); // NOI18N
        }
        classpath = toClasspathString(j2eePlatform.getClasspathEntries());
        props.setProperty(MultiDomainProjectProperties.J2EE_PLATFORM_CLASSPATH, classpath);
        
        // set j2ee.platform.wscompile.classpath
        if (j2eePlatform.isToolSupported(J2eePlatform.TOOL_WSCOMPILE)) {
            File[] wsClasspath = j2eePlatform.getToolClasspathEntries(J2eePlatform.TOOL_WSCOMPILE);
            props.setProperty(MultiDomainProjectProperties.J2EE_PLATFORM_WSCOMPILE_CLASSPATH,
                    toClasspathString(wsClasspath));
        }
        
        // set j2ee.platform.wsimport.classpath
        if (j2eePlatform.isToolSupported(J2eePlatform.TOOL_WSIMPORT)) {
            File[] wsClasspath = j2eePlatform.getToolClasspathEntries(J2eePlatform.TOOL_WSIMPORT);
            props.setProperty(MultiDomainProjectProperties.J2EE_PLATFORM_WSIMPORT_CLASSPATH,
                    toClasspathString(wsClasspath));
        }
        
        // set j2ee.platform.wsgen.classpath
        if (j2eePlatform.isToolSupported(J2eePlatform.TOOL_WSGEN)) {
            File[] wsClasspath = j2eePlatform.getToolClasspathEntries(J2eePlatform.TOOL_WSGEN);
            props.setProperty(MultiDomainProjectProperties.J2EE_PLATFORM_WSGEN_CLASSPATH,
                    toClasspathString(wsClasspath));
        }
        
        // set j2ee.platform.jsr109 support
        if (j2eePlatform.isToolSupported(J2eePlatform.TOOL_JSR109)) {
            props.setProperty(MultiDomainProjectProperties.J2EE_PLATFORM_JSR109_SUPPORT,
                    "true"); //NOI18N
        }
        
        // ant deployment support
        projectFolder = FileUtil.toFile(webProjDir);
        try {
            AntDeploymentHelper.writeDeploymentScript(new File(projectFolder, MultiDomainProjectProperties.ANT_DEPLOY_BUILD_SCRIPT),
                    J2eeModule.WAR, serverInstanceID);
        } catch (IOException ioe) {
            mLogger.severe(ioe);
        }
        if (deployAntPropsFile != null) {
            props.setProperty(MultiDomainProjectProperties.DEPLOY_ANT_PROPS_FILE, deployAntPropsFile.getAbsolutePath());
        }       
        props.store(propsFile);
        propsFile.close();
    }
    
    private static String toClasspathString(File[] classpathEntries) {
        if (classpathEntries == null) {
            return "";
        }
        StringBuffer classpath = new StringBuffer();
        for (int i = 0; i < classpathEntries.length; i++) {
            classpath.append(classpathEntries[i].getAbsolutePath());
            if (i + 1 < classpathEntries.length) {
                classpath.append(":"); // NOI18N
            }
        }
        return classpath.toString();
    }
    
    private static AntProjectHelper setupProject (FileObject dirFO) throws IOException {
        AntProjectHelper projHelper = ProjectGenerator.createProject(dirFO, MultiDomainProjectType.TYPE);
        /*
        Element data = projHelper.getPrimaryConfigurationData(true);
        Document doc = data.getOwnerDocument();
        Element nameEl = doc.createElementNS(MultiDomainProjectType.PROJECT_CONFIGURATION_NAMESPACE, "name"); // NOI18N
        nameEl.appendChild(doc.createTextNode(name));
        data.appendChild(nameEl);
        Element minant = doc.createElementNS(MultiDomainProjectType.PROJECT_CONFIGURATION_NAMESPACE, "minimum-ant-version"); // NOI18N
        minant.appendChild(doc.createTextNode("1.6")); // NOI18N
        data.appendChild(minant);
        projHelper.putPrimaryConfigurationData(data, true);
         */
        Project p = ProjectManager.getDefault().findProject(dirFO);
        ProjectManager.getDefault().saveProject(p);  
               
        return projHelper;
    }

    private static FileObject getTemplates(FileObject parent, String folderName, String templateLocation) throws IOException {
        FileObject folder = parent.createFolder(folderName);
        File f = InstalledFileLocator.getDefault().locate(templateLocation, "", false);
        if (f != null) {
            FileObject fTemplates = FileUtil.toFileObject(f);
            FileObject[] files = fTemplates.getChildren();
            for (int i = 0; i < files.length; i++) {
                FileObject file = files[i];
                if (file.isFolder()) {
                    FileObject folder2 = folder.createFolder(file.getName());
                    FileObject[] files2 = file.getChildren();
                    for (int j = 0; j < files2.length; j++) {
                        FileObject file2 = files2[j];
                        if (file2.isData() && !file2.getExt().equals("xsd")) {
                            FileUtil.copyFile(file2, folder2, file2.getName());
                        }
                    }
                } else {
                    if (file.isData() && !file.getExt().equals("xsd")) {
                        FileUtil.copyFile(file, folder, file.getName());
                    }
                }
            }
        }
        return folder;
    }

    private static FileObject getConfigSchemaFiles(FileObject parent, String folderName, String templateLocation) throws IOException {
        FileObject folder = parent.createFolder(folderName);
        File f = InstalledFileLocator.getDefault().locate(templateLocation, "", false);
        if (f != null) {
            FileObject fTemplates = FileUtil.toFileObject(f);
            FileObject[] files = fTemplates.getChildren();
            for (int i = 0; i < files.length; i++) {
                FileObject file = files[i];
                if (file.isData() &&
                    (file.getNameExt().equals(MultiDomainProjectProperties.RELATIONSHIP_MODEL_XSD) ||
                     file.getNameExt().equals(MultiDomainProjectProperties.RELATIONSHIP_WEB_MANAGER_XSD))) {
                    FileUtil.copyFile(file, folder, file.getName());
                }
            }
        }
        return folder;
    }

    private static FileObject getSchemaFile(FileObject parent, String folderName, String templateLocation, String schemaFile) throws IOException {
        FileObject folder = parent.createFolder(folderName);
        File f = InstalledFileLocator.getDefault().locate(templateLocation, "", false);
        if (f != null) {
            FileObject fTemplates = FileUtil.toFileObject(f);
            FileObject[] files = fTemplates.getChildren();
            for (int i = 0; i < files.length; i++) {
                FileObject file = files[i];
                if (file.isData() && file.getNameExt().equals(schemaFile)) {
                    FileUtil.copyFile(file, folder, file.getName());
                }
            }
        }
        return folder;
    }
    
    /*
     * Not used
     */
    private static FileObject createEviewFile(FileObject folder, String name, String data) 
    throws MultiDomainRepositoryException {
        try {
            if (folder == null || name == null || data == null) {
                return null;
            }
            FileObject file = folder.getFileObject(name);
            if ( file == null) { 
                file = folder.createData(name);  
            }
            FileLock fileLock = file.lock();
            OutputStream out = file.getOutputStream(fileLock);
            OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
            writer.write(data);
            writer.close();
            fileLock.releaseLock();
            DataObject dobj = DataObject.find(file);
            return file;
        } catch (IOException ex) {
            throw new MultiDomainRepositoryException(ex);
        }
    }
    
    
    private static void unZipFile(FileObject templateFO, FileObject projectRoot) throws IOException {
        InputStream templateIS = null;
        try {
            templateIS = templateFO.getInputStream();
            ZipInputStream zipIS = new ZipInputStream(templateIS);
            ZipEntry entry;
            String entryName = null;
            while ((entry = zipIS.getNextEntry()) != null ) {
                entryName = entry.getName();
                if (needCopy(entryName)!=true){
                    continue ;
                }
                
                if (entry.isDirectory()) {
                    FileUtil.createFolder(projectRoot, entryName);
                } else {
                    FileObject fo = FileUtil.createData(projectRoot, entryName);
                    FileLock lock = fo.lock();
                    try {
                        OutputStream out = fo.getOutputStream(lock);
                        if (needTranslation(entryName)) {
                            translateProjectName(zipIS, out, projectRoot.getName(), xlateToken_EEP);
                        } 
                        
                        try {
                            FileUtil.copy(zipIS, out);
                        } finally {
                            out.close();
                        }
                    } finally {
                        lock.releaseLock();
                    }
                }
            }
        } finally {
            if (templateIS != null) {
                templateIS.close();
            }
        }
    }   

    
    static boolean needTranslation(String fname) {
        for (int i = 0; i < xlateFiles.length; i++) {
            if (fname.endsWith(xlateFiles[i])) {
                return true;
            }
        }
        return false;
    }
    
    static boolean needCopy(String fname) {
        for (int i = 0; i < excludedFiles.length; i++) {
            if (fname.endsWith(excludedFiles[i])) {
                return false;
            }
        }
        return true;
    }

    static void translateProjectName(InputStream zipIS, OutputStream out, String projName, String token) throws IOException {
        ByteArrayOutputStream baOS = new ByteArrayOutputStream();
        FileUtil.copy(zipIS, baOS);
        String strBAOS = baOS.toString();
        if (strBAOS.indexOf("com.sun.mdm.index.project") >= 0) {
            String tmp =strBAOS.replaceAll("com.sun.mdm.index.project", xlateTo_ProjectType);
            strBAOS = tmp;
        }
        //strBAOS.replaceAll(xlateFrom_EarProjectType, xlateTo_ProjectType);
        //strBAOS.replaceAll(xlateFrom_ProjectTypeNS, xlateTo_ProjectTypeNS);
        if (strBAOS.indexOf(token) >= 0) {
            byte[] bytes = strBAOS.replaceAll(token, projName).getBytes("UTF-8");
            ByteArrayInputStream baIS = new ByteArrayInputStream(bytes);
            FileUtil.copy(baIS, out);
        }
    }
    
    static String getDBScriptRelationshipModel() {
        String str = "";
        return str;
    }
    
    static String getDBScriptAttributeMappings() {
        String str = "";
        return str;
    }
    
    private void generateDDLFiles(File srcRoot) 
            throws /*TemplateWriterException,*/ ParserException, IOException {

            File objectFile = new File(srcRoot,
                    MultiDomainProjectProperties.CONFIGURATION_FOLDER
                    + File.separator + MultiDomainProjectProperties.RELATIONSHIP_MODEL_XML);
            InputSource source = new InputSource(new FileInputStream(
                        objectFile));
            RelationshipModel rm = Utils.parseRelationshipModel(source);

            //String tmpl = getCreateDDLWriterTemplate(rm.getDataBase());
            File outPath = new File(srcRoot,
                MultiDomainProjectProperties.DATABASE_SCRIPT_FOLDER + File.separator + MultiDomainProjectProperties.CREATE_RELATIONSHIPS_SQL);
            //DDLWriter tdw = new DDLWriter(outPath.getAbsolutePath(), rm, tmpl);
            //tdw.write(true);

    }
}
