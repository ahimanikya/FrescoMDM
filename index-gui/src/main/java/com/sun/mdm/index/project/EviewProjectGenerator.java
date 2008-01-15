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
package com.sun.mdm.index.project;

import com.sun.mdm.index.project.generator.descriptor.SunEjbJarWriter;
import com.sun.mdm.index.project.ui.wizards.WizardProperties;
import com.sun.mdm.index.project.ui.wizards.Properties;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;

import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.FileLock;
import org.openide.ErrorManager;

import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.EditableProperties;
import org.netbeans.spi.project.support.ant.ProjectGenerator;
import org.netbeans.modules.j2ee.deployment.devmodules.api.AntDeploymentHelper;
import org.netbeans.modules.j2ee.deployment.devmodules.api.Deployment;
import org.openide.modules.InstalledFileLocator;
import org.openide.loaders.DataObject;
import org.apache.tools.ant.module.api.support.ActionUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.netbeans.modules.j2ee.deployment.devmodules.api.J2eeModule;
import org.netbeans.modules.j2ee.deployment.devmodules.api.J2eePlatform;

import com.sun.mdm.standardizer.StandardizerIntrospector;
import com.sun.mdm.standardizer.DataTypeDescriptor;


/**
 * Create a fresh EjbProject from scratch or by importing and exisitng web module 
 * in one of the recognized directory structures.
 *
 */
public class EviewProjectGenerator {
    public static final String DEFAULT_DOC_BASE_FOLDER = "conf"; //NOI18N
    public static final String DEFAULT_SRC_FOLDER = "src"; //NOI18N
    public static final String DEFAULT_RESOURCE_FOLDER = "setup"; //NOI18N
    public static final String DEFAULT_BPELASA_FOLDER = "bpelasa"; //NOI18N
    public static final String DEFAULT_BUILD_DIR = "build"; //NOI18N
    public static final String DEFAULT_NBPROJECT_DIR = "nbproject"; //NOI18N
    public static final String WAR_NAME = "war.name"; //NOI18N

    public static String xlateToken_EEP = "EviewEnterpriseApplication"; // NOI18N   
    public static String xlateFrom_EarProjectType = "org.netbeans.modules.j2ee.earproject"; // NOI18N                
    public static String xlateTo_ProjectType = "com.sun.mdm.index.project"; // NOI18N  
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
    private static String EVIEW_ENTERPRISE_PROJECT_EJB = "EviewEnterpriseApplication-ejb";
    private static String EVIEW_ENTERPRISE_PROJECT_WAR = "EviewEnterpriseApplication-war";
    
    private EviewProjectGenerator() {}

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
        AntProjectHelper h = setupProject (fo);
        FileObject srcRoot = fo.createFolder(DEFAULT_SRC_FOLDER); // NOI18N
        String serverInstanceID = (String)wDesc.getProperty("serverInstanceID");
        String mainProjectName = (String) wDesc.getProperty(WizardProperties.NAME);
        String j2eeLevel = (String)wDesc.getProperty(WizardProperties.J2EE_LEVEL);
        String autoGenerate = (String) wDesc.getProperty(Properties.PROP_AUTO_GENERATE);

        try{
           createEjbWar(fo, mainProjectName, serverInstanceID, j2eeLevel);           
            //FileObject j2eeModulesFolder = srcRoot.createFolder(EviewRepository.J2EE_MODULES_FOLDER); // NOI18N        
        } catch (EviewRepositoryException ex) {
            throw new IOException(ex.toString());         
        }

        //set project properties
        EditableProperties ep = h.getProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH);
        ep.setProperty(EviewProjectProperties.EJB_DIR,mainProjectName + "-ejb");
        ep.setProperty(EviewProjectProperties.WAR_DIR,mainProjectName + "-war");
        ep.setProperty("eView.generated.dir", 
                       EviewProjectProperties.EVIEW_GENERATED_FOLDER);
        ep.setProperty(EviewProjectProperties.J2EE_SERVER_TYPE, 
                       Deployment.getDefault().getServerID(serverInstanceID));
        ep.setProperty(EviewProjectProperties.J2EE_PLATFORM, j2eeLevel);
        ep.setProperty(EviewProjectProperties.EVIEW_JBI_JAR, 
                       mainProjectName+".jar");
        ep.setProperty(EviewProjectProperties.JBI_SE_TYPE, 
                       EviewProjectProperties.JAVA_EE_SE_COMPONENT_NAME);
        ep.setProperty(EviewProjectProperties.SE_DEPLOYMENT_JAR,
                       "${dist.dir}/jbi/${jbi.jar}");
        ep.setProperty(EviewProjectProperties.SRC_DIR,"src"); 
        h.putProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH, ep);
               
        // set private properties
        ep = h.getProperties(AntProjectHelper.PRIVATE_PROPERTIES_PATH);        
        ep.setProperty(EviewProjectProperties.J2EE_SERVER_INSTANCE, serverInstanceID);
        
        File deployAntPropsFile = AntDeploymentHelper.getDeploymentPropertiesFile(serverInstanceID);
        if (deployAntPropsFile != null) {
            ep.setProperty(EviewProjectProperties.DEPLOY_ANT_PROPS_FILE, deployAntPropsFile.getAbsolutePath());
        }
        
        h.putProperties(AntProjectHelper.PRIVATE_PROPERTIES_PATH, ep);
        EviewApplication p = (EviewApplication) ProjectManager.getDefault().findProject(h.getProjectDirectory ());
        // Set Application name and Object 
        p.setApplicationName(wDesc.getProperty(com.sun.mdm.index.project.ui.wizards.Properties.PROP_TARGET_VIEW_NAME).toString());
        p.setObjectName(wDesc.getProperty(com.sun.mdm.index.project.ui.wizards.Properties.PROP_OBJECT_NAME).toString());
        try{
           createConfigFile(srcRoot, wDesc, p);
        } catch (EviewRepositoryException ex) {
            throw new IOException(ex.toString());         
        }

        ProjectManager.getDefault().saveProject(p);

        if (autoGenerate.equals("Yes")) {
            try {
                FileObject buildXml = p.getProjectDirectory().getFileObject(p.getBuildXmlName ());
                ActionUtils.runTarget(buildXml, new String[] {"gen-mdm-index-files"}, null);
            } catch (IOException e) {
                ErrorManager.getDefault().notify(e);
            }
        }
        return h;
    }

    private static void createConfigFile(FileObject srcRoot, WizardDescriptor wDesc, EviewApplication eviewApplication)
            throws EviewRepositoryException, IOException{
                 
            EviewRepository repository  = EviewRepository.getEviewRepository();  
            
            // *** Sub folder - Configuration ***
            FileObject configurationFolder = srcRoot.createFolder(EviewProjectProperties.CONFIGURATION_FOLDER); // NOI18N     
            
            String strXml;            
            // object.xml/ObjectDefinitionFile
            strXml = wDesc.getProperty(com.sun.mdm.index.project.ui.wizards.Properties.PROP_XML_OBJECT_DEF_FILE).toString();      
            repository.createConfigurationFile(configurationFolder, EviewProjectProperties.OBJECT_XML, strXml);
       
            // edm.xml/GuiConfigurationFile
            strXml = wDesc.getProperty(com.sun.mdm.index.project.ui.wizards.Properties.PROP_XML_GUI_CONFIG_FILE).toString();
            repository.createConfigurationFile(configurationFolder, EviewProjectProperties.EDM_XML, strXml);
        
            // master.xml/MasterConfigurationFile;
            strXml = wDesc.getProperty(com.sun.mdm.index.project.ui.wizards.Properties.PROP_XML_MASTER_CONFIG_FILE).toString();
            repository.createConfigurationFile(configurationFolder, EviewProjectProperties.MASTER_XML, strXml);
            
            // mefa.xml/MefaConfigurationFile;
            strXml = wDesc.getProperty(com.sun.mdm.index.project.ui.wizards.Properties.PROP_XML_MEFA_CONFIG_FILE).toString();
            repository.createConfigurationFile(configurationFolder, EviewProjectProperties.MEFA_XML, strXml);
        
            // security.xml/SecurityConfigurationFile;
            strXml = wDesc.getProperty(com.sun.mdm.index.project.ui.wizards.Properties.PROP_XML_SECURITY_CONFIG_FILE).toString();
            repository.createConfigurationFile(configurationFolder, EviewProjectProperties.SECURITY_XML, strXml);
        
            // validation.xml/ValidationConfigurationFile;
            strXml = wDesc.getProperty(com.sun.mdm.index.project.ui.wizards.Properties.PROP_XML_VALID_CONFIG_FILE).toString();
            repository.createConfigurationFile(configurationFolder, EviewProjectProperties.VALIDATION_XML, strXml);
        
            // query.xml/QueryConfigurationFile;
            strXml = wDesc.getProperty(com.sun.mdm.index.project.ui.wizards.Properties.PROP_XML_QUERY_CONFIG_FILE).toString();
            repository.createConfigurationFile(configurationFolder, EviewProjectProperties.QUERY_XML, strXml);
        
            // update.xml/UpdateConfigurationFile;
            strXml = wDesc.getProperty(com.sun.mdm.index.project.ui.wizards.Properties.PROP_XML_UPDATE_CONFIG_FILE).toString();
            repository.createConfigurationFile(configurationFolder, EviewProjectProperties.UPDATE_XML, strXml);
            
            FileObject schemaFolder = getSchemaFiles(configurationFolder, EviewProjectProperties.SCHEMA_FOLDER, EviewProjectProperties.SCHEMA_TEMPLATE_LOCATION);
            
            // *** Sub folder - Database Script ***
            FileObject dbscriptFolder = srcRoot.createFolder(EviewProjectProperties.DATABASE_SCRIPT_FOLDER); // NOI18N
            String dbScript = null;
            
            dbScript = wDesc.getProperty(com.sun.mdm.index.project.ui.wizards.Properties.PROP_DBSCRIPT_SYSTEMS).toString();                      
            FileObject systemsDdl = repository.createConfigurationFile(dbscriptFolder, EviewProjectProperties.SYSTEMS_SQL, dbScript);        
            
            dbScript = wDesc.getProperty(com.sun.mdm.index.project.ui.wizards.Properties.PROP_DBSCRIPT_CODELIST).toString();
            FileObject codelistDdl = repository.createConfigurationFile(dbscriptFolder, EviewProjectProperties.CODELIST_SQL, dbScript);                
        
            // *** Sub folder - Match Engine ***
            FileObject matchEngineFolder = getTemplates(srcRoot, EviewProjectProperties.MATCH_ENGINE_FOLDER, EviewProjectProperties.MATCH_TEMPLATE_LOCATION);
         
            // *** Sub folder - Standardization ***
            FileObject standardizationEngineFolder = srcRoot.createFolder(EviewProjectProperties.STANDARDIZATION_ENGINE_FOLDER); // NOI18N
            try {
                StandardizerIntrospector introspector = eviewApplication.getStandardizerIntrospector();
                introspector.setRepository(FileUtil.toFile(standardizationEngineFolder));
                File globalRepository = InstalledFileLocator.getDefault().locate(EviewProjectProperties.STANDARDIZATION_DEPLOYMENT_LOCATION, "", false);
                if (!globalRepository.exists()) {
                    throw new Exception("Global repository '" + globalRepository.getAbsolutePath() + "' does not exist.");
                }
                DataTypeDescriptor[] dataTypeDescriptors = introspector.importDirectory(globalRepository);
                //introspector.close();
            } catch (Exception ex) {
                throw new IOException(ex.toString());
            }
            // *** Sub folder - Filter ***
            FileObject filterFolder = getTemplates(srcRoot, EviewProjectProperties.FILTER_FOLDER, EviewProjectProperties.FILTER_TEMPLATE_LOCATION);
    }
    
    private static void createEjbWar(FileObject projectDir, String mainProjectName, String serverInstanceID, String j2eeLevel )
            throws EviewRepositoryException, FileNotFoundException, IOException{
        EviewRepository repository  = EviewRepository.getEviewRepository(); 
        // Unzip -ejb and -war projects from template
        FileObject template = repository.getInstalledFile(EviewProjectProperties.TEMPLATE_ZIP);
        unZipFile(template.getInputStream(), projectDir);
        // Rename template directories here
        Enumeration e = projectDir.getFolders(true);
        while (e.hasMoreElements()) {
            FileObject subfolder = (FileObject) e.nextElement();
            if (ProjectManager.getDefault().isProject(subfolder)) {
                String subfolderName;
                subfolderName = subfolder.getName();
                if (subfolderName.equals(EVIEW_ENTERPRISE_PROJECT_EJB)) {
                    subfolderName = mainProjectName + "-ejb";
                } else if (subfolderName.equals(EVIEW_ENTERPRISE_PROJECT_WAR)) {
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
        FileOutputStream propsFile = new FileOutputStream(ejbProjDir.getPath()+"/nbproject/private/private.properties");
        EditableProperties props = new EditableProperties();
        //props.load(propsFile.getInputStream());
        props.setProperty(EviewProjectProperties.J2EE_SERVER_INSTANCE, serverInstanceID);
        // set j2ee.platform.classpath
        if (!j2eePlatform.getSupportedSpecVersions(J2eeModule.EJB).contains(j2eeLevel)) {
            Logger.getLogger("global").log(Level.WARNING,
                    "J2EE level:" + j2eeLevel + " not supported by server " + Deployment.getDefault().getServerInstanceDisplayName(serverInstanceID) + " for module type EJB"); // NOI18N
        }
        String classpath = toClasspathString(j2eePlatform.getClasspathEntries());
        props.setProperty(EviewProjectProperties.J2EE_PLATFORM_CLASSPATH,classpath);
                
        // set j2ee.platform.wscompile.classpath
        if (j2eePlatform.isToolSupported(J2eePlatform.TOOL_WSCOMPILE)) {
            classpath = toClasspathString(j2eePlatform.getClasspathEntries());
            props.setProperty(EviewProjectProperties.J2EE_PLATFORM_WSCOMPILE_CLASSPATH,classpath);
        }
        
        // ant deployment support
        File projectFolder = FileUtil.toFile(ejbProjDir);
        try {
            AntDeploymentHelper.writeDeploymentScript(new File(projectFolder, EviewProjectProperties.ANT_DEPLOY_BUILD_SCRIPT),
                    J2eeModule.EJB, serverInstanceID);
            
            new SunEjbJarWriter(projectFolder).write();
        } catch (IOException ioe) {
            Logger.getLogger("global").log(Level.INFO, null, ioe);
        }
        if (deployAntPropsFile != null) {
            props.setProperty(EviewProjectProperties.DEPLOY_ANT_PROPS_FILE, deployAntPropsFile.getAbsolutePath());
        }
        props.store(propsFile);
        propsFile.close();
        //set private property of web project
        FileObject webProjDir =projectDir.getFileObject(mainProjectName+"-war");
        propsFile = new FileOutputStream(webProjDir.getPath()+"/nbproject/private/private.properties");
        props = new EditableProperties();
        //props.load(propsFile.getInputStream());
        props.setProperty(EviewProjectProperties.J2EE_SERVER_INSTANCE, serverInstanceID);
        
        // set j2ee.platform.classpath
        if (!j2eePlatform.getSupportedSpecVersions(J2eeModule.WAR).contains(j2eeLevel)) {
            Logger.getLogger("global").log(Level.WARNING,
                    "J2EE level:" + j2eeLevel + " not supported by server " + Deployment.getDefault().getServerInstanceDisplayName(serverInstanceID) + " for module type WAR"); // NOI18N
        }
        classpath = toClasspathString(j2eePlatform.getClasspathEntries());
        props.setProperty(EviewProjectProperties.J2EE_PLATFORM_CLASSPATH, classpath);
        
        // set j2ee.platform.wscompile.classpath
        if (j2eePlatform.isToolSupported(J2eePlatform.TOOL_WSCOMPILE)) {
            File[] wsClasspath = j2eePlatform.getToolClasspathEntries(J2eePlatform.TOOL_WSCOMPILE);
            props.setProperty(EviewProjectProperties.J2EE_PLATFORM_WSCOMPILE_CLASSPATH,
                    toClasspathString(wsClasspath));
        }
        
        // set j2ee.platform.wsimport.classpath
        if (j2eePlatform.isToolSupported(J2eePlatform.TOOL_WSIMPORT)) {
            File[] wsClasspath = j2eePlatform.getToolClasspathEntries(J2eePlatform.TOOL_WSIMPORT);
            props.setProperty(EviewProjectProperties.J2EE_PLATFORM_WSIMPORT_CLASSPATH,
                    toClasspathString(wsClasspath));
        }
        
        // set j2ee.platform.wsgen.classpath
        if (j2eePlatform.isToolSupported(J2eePlatform.TOOL_WSGEN)) {
            File[] wsClasspath = j2eePlatform.getToolClasspathEntries(J2eePlatform.TOOL_WSGEN);
            props.setProperty(EviewProjectProperties.J2EE_PLATFORM_WSGEN_CLASSPATH,
                    toClasspathString(wsClasspath));
        }
        
        // set j2ee.platform.jsr109 support
        if (j2eePlatform.isToolSupported(J2eePlatform.TOOL_JSR109)) {
            props.setProperty(EviewProjectProperties.J2EE_PLATFORM_JSR109_SUPPORT,
                    "true"); //NOI18N
        }
        
        // ant deployment support
        projectFolder = FileUtil.toFile(webProjDir);
        try {
            AntDeploymentHelper.writeDeploymentScript(new File(projectFolder, EviewProjectProperties.ANT_DEPLOY_BUILD_SCRIPT),
                    J2eeModule.WAR, serverInstanceID);
        } catch (IOException ioe) {
            Logger.getLogger("global").log(Level.INFO, null, ioe);
        }
        if (deployAntPropsFile != null) {
            props.setProperty(EviewProjectProperties.DEPLOY_ANT_PROPS_FILE, deployAntPropsFile.getAbsolutePath());
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
        AntProjectHelper h = ProjectGenerator.createProject(dirFO, EviewProjectType.TYPE);
        /*
        Element data = h.getPrimaryConfigurationData(true);
        Document doc = data.getOwnerDocument();
        Element nameEl = doc.createElementNS(EviewProjectType.PROJECT_CONFIGURATION_NAMESPACE, "name"); // NOI18N
        nameEl.appendChild(doc.createTextNode(name));
        data.appendChild(nameEl);
        Element minant = doc.createElementNS(EviewProjectType.PROJECT_CONFIGURATION_NAMESPACE, "minimum-ant-version"); // NOI18N
        minant.appendChild(doc.createTextNode("1.6")); // NOI18N
        data.appendChild(minant);
        h.putPrimaryConfigurationData(data, true);
         */
        Project p = ProjectManager.getDefault().findProject(dirFO);
        ProjectManager.getDefault().saveProject(p);  
               
        return h;
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
                        FileUtil.copyFile(file2, folder2, file2.getName());
                    }
                } else {
                    FileUtil.copyFile(file, folder, file.getName());
                }
            }
        }
        return folder;
    }

    private static FileObject getSchemaFiles(FileObject parent, String folderName, String templateLocation) throws IOException {
        FileObject folder = parent.createFolder(folderName);
        File f = InstalledFileLocator.getDefault().locate(templateLocation, "", false);
        if (f != null) {
            FileObject fTemplates = FileUtil.toFileObject(f);
            FileObject[] files = fTemplates.getChildren();
            for (int i = 0; i < files.length; i++) {
                FileObject file = files[i];
                if (file.isData() &&
                    (file.getNameExt().equals(EviewProjectProperties.OBJECT_XSD) ||
                     file.getNameExt().equals(EviewProjectProperties.EDM_XSD) ||
                     file.getNameExt().equals(EviewProjectProperties.MASTER_XSD) ||
                     file.getNameExt().equals(EviewProjectProperties.MEFA_XSD) ||
                     file.getNameExt().equals(EviewProjectProperties.QUERY_XSD) ||
                     file.getNameExt().equals(EviewProjectProperties.UPDATE_XSD) ||
                     file.getNameExt().equals(EviewProjectProperties.SECURITY_XSD) ||
                     file.getNameExt().equals(EviewProjectProperties.VALIDATION_XSD))) {
                    FileUtil.copyFile(file, folder, file.getName());
                }
            }
        }
        return folder;
    }

    /*
     */
    private static FileObject getInstalledFile(FileObject folder, String fname) throws IOException {
        FileObject installedFile = null;
        File f = InstalledFileLocator.getDefault().locate(fname, "", false);
        if (f != null) {
            installedFile = FileUtil.toFileObject(f);
            FileUtil.copyFile(installedFile, folder, installedFile.getName());
        }
        return installedFile;
    }
    
    /*
     * Not used
     */
    private static FileObject createEviewFile(FileObject folder, String name, String data) 
    throws EviewRepositoryException {
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
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write(data);
            writer.close();
            fileLock.releaseLock();
            DataObject dobj = DataObject.find(file);
            return file;
        } catch (IOException ex) {
            throw new EviewRepositoryException(ex);
        }
    }
    
    
    private static void unZipFile(InputStream source, FileObject projectRoot) throws IOException {
        try {
            ZipInputStream str = new ZipInputStream(source);
            ZipEntry entry;
            String entryName = null;
            while ((entry = str.getNextEntry()) != null ) {
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
                        //if (needTranslation(entryName)) {
                            translateProjectName(str, out, projectRoot.getName(), xlateToken_EEP);
                        //} 
                        
                        try {
                            FileUtil.copy(str, out);
                        } finally {
                            out.close();
                        }
                    } finally {
                        lock.releaseLock();
                    }
                }
            }
        } finally {
            source.close();
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

    static void translateProjectName(InputStream str, OutputStream out, String name, String token) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        FileUtil.copy(str, bo);
        String strBo = bo.toString();
        if (strBo.indexOf("com.sun.mde.index.project") >= 0) {
            //strBo.replaceAll("com.sun.projects.eviewpro", xlateTo_ProjectType);
        }
        //strBo.replaceAll(xlateFrom_EarProjectType, xlateTo_ProjectType);
        //strBo.replaceAll(xlateFrom_ProjectTypeNS, xlateTo_ProjectTypeNS);

        ByteArrayInputStream bi = new ByteArrayInputStream(strBo.replaceAll(token, name).getBytes());
        FileUtil.copy(bi, out);
    }
}
