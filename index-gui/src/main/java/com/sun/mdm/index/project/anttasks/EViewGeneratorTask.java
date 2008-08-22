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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import com.sun.mdm.index.util.Logger;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.taskdefs.Move;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.mdm.index.objects.metadata.MetaDataService;
import com.sun.mdm.index.parser.EIndexObject;
import com.sun.mdm.index.parser.ParserException;
import com.sun.mdm.index.parser.Utils;
import com.sun.mdm.index.project.EviewProjectProperties;
import com.sun.mdm.index.project.generator.descriptor.AppXmlWriter;
import com.sun.mdm.index.project.generator.descriptor.JbiXmlWriter;
import com.sun.mdm.index.project.generator.exception.TemplateWriterException;
import com.sun.mdm.index.project.generator.outbound.OutboundXSDBuilder;
import com.sun.mdm.index.project.generator.persistence.DDLWriter;
import com.sun.mdm.standardizer.introspector.StandardizationIntrospector;
import com.sun.inti.components.util.ClassUtils;

public class EViewGeneratorTask extends Task {
    private File mSrcdir;
    private File mEjbdir;
    private File mWardir;
    private File mTemplateDir;
    private boolean mForce = false;
    private static final String REPOSITORY_LOCATION = "com/sun/mdm/standardizer";
    private static final String REPOSITORY_RESOURCE_NAME = "repositoryImage.zip";
    private static final String PROPERTIES_RESOURCE_NAME = "standardizationEngine.properties";
    private static final String REPOSITORY_NAME_PROPERTY = "repositoryName";
    private static final Logger mLog = Logger.getLogger(EViewGeneratorTask.class.getName());
        
    public void setSrcdir(File srcdir) {
        this.mSrcdir = srcdir;
    }

    public void setEjbdir(File ejbdir) {
        this.mEjbdir = ejbdir;
    }

    public void setWardir(File wardir) {
        this.mWardir = wardir;
    }

    public void setTemplateDir(File templateDir) {
        this.mTemplateDir = templateDir;
    }

    public void setForce(boolean force) {
        this.mForce = force;
    }

    public void execute() throws BuildException {

        if (mSrcdir == null||!mSrcdir.exists()) {
            throw new BuildException(
                "The attribute,\"srcdir\", must be set to " +
                "Master Index Project source directory.");
        }
        if (mEjbdir == null||!mEjbdir.exists()) {
            throw new BuildException("Must specify the ejb project directory");
        }
        if (mWardir == null||!mWardir.exists()) {
            throw new BuildException("Must specify the war project directory");
        }
        if (mTemplateDir == null) {
            String modulePath = getProject().getProperty("module.install.dir");
            mTemplateDir = new File(modulePath + File.separator + "ext" + File.separator + "mdm");
        }

        // need to regenerate if source files have been modified
        if (modified() || mForce) {
            try {
                File objectFile = new File(mSrcdir,
                        EviewProjectProperties.CONFIGURATION_FOLDER
                                + File.separator + "object.xml");
                InputSource source = new InputSource(new FileInputStream(
                        objectFile));
                EIndexObject eo = Utils.parseEIndexObject(source);
                String applicationName = eo.getName();

                // generate database scripts, application.xml,
                // sun-application.xml, jbi.xml, object files,
                // web service files, outbound.xsd
                generateFiles(eo, objectFile);

                // generate jar file
                generateJars();

                // put ejb files in ebj project
                generateEbjFiles(eo);

                // put the web files into war project
                generateWarFiles(applicationName);

                // add lib to ejb project by modifing ejb project's
                // project.properties file.
                addEjbLib();

                // generate report client
                generateReportClient();
                mLog.info("Master Index Files are generated Successfully.");
            } catch (Exception ex) {
                String mode = System.getProperty("run.mode");
                if (mode == null || !mode.equals("run")) {
                   //delete "files-generated" folder when generation fails
                   String projPath = getProject().getProperty("basedir");
                   File destDir = new File(projPath,
                   EviewProjectProperties.EVIEW_GENERATED_FOLDER);
                   Delete delete = (Delete) getProject().createTask("delete");
                   delete.setDir(destDir);
                   delete.init();
                   delete.setLocation(getLocation());
                   delete.execute();
                }
                mLog.severe("Could not generate Master Index Files.");
                throw new BuildException(ex.getMessage());
            }
        }
    }

    private void generateFiles(EIndexObject eo, File objectFile) 
            throws TemplateWriterException, ParserException, IOException {
        
        //generate create.sql and drop.sql
        String tmpl = getCreateDDLWriterTemplate(eo.getDataBase());
        File outPath = new File(mSrcdir,
                EviewProjectProperties.DATABASE_SCRIPT_FOLDER + File.separator + "create.sql");
        DDLWriter tdw = new DDLWriter(outPath.getAbsolutePath(), eo, tmpl);
        tdw.write(true);

        tmpl = getDropDDLWriterTemplate(eo.getDataBase());
        outPath = new File(mSrcdir,
                EviewProjectProperties.DATABASE_SCRIPT_FOLDER + File.separator + "drop.sql");
        tdw = new DDLWriter(outPath.getAbsolutePath(), eo, tmpl);
        tdw.write(false);
        
        //generate application.xml and sun-application.xml
        String genpath = mSrcdir.getAbsolutePath() + File.separator + "conf";
        String ejbName = getProject().getProperty("ejb.dir") + ".jar";
        String warName = getProject().getProperty("war.dir") + ".war";
        AppXmlWriter appWriter = new AppXmlWriter(genpath, eo, ejbName, warName);
        appWriter.write();

        String projPath = getProject().getProperty("basedir");
        String generatedPath = projPath + File.separator
                + EviewProjectProperties.EVIEW_GENERATED_FOLDER;
        
        //delete generated folder if there is a existing one
        File generateFolder = new File(generatedPath);
        Delete delete = (Delete) getProject().createTask("delete");
        delete.setDir(generateFolder);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();
       
        //generate object files and webservice java files 
        //at "files-generated/client/java" directory
        File clientdir = new File(generatedPath, "client/java");
        clientdir.mkdirs();
        ObjectGeneratorTask objectGenerator = new ObjectGeneratorTask();
        objectGenerator.setDestdir(clientdir);
        objectGenerator.setObjectFile(objectFile);
        objectGenerator.execute();

        // delete any old webservice files under ejb project
        File ejbWSDir = new File(mEjbdir,"src/java/com/sun/mdm/index/webservice");
        delete = (Delete) getProject().createTask("delete");
        delete.setDir(ejbWSDir);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();
        
        //move webservice files to ejb project
        File generatedWSDir = 
                new File(generatedPath,"client/java/com/sun/mdm/index/webservice");       
        Move move = (Move) getProject().createTask("move");
        move.setTofile(ejbWSDir);
        move.setFile(generatedWSDir);
        move.init();
        move.setLocation(getLocation());
        move.execute();
        
        //generate outbound.xsd "files-generated/outbound.xsd"
        File xsdFile = new File(generatedPath, "outbound.xsd");
        OutboundXSDBuilder builder = new OutboundXSDBuilder();
        builder.buildXSD(eo, xsdFile);
        
        //generate jbi.xml at "files-generated/jbi/META-INF/jbi.xml"
        String jbiXmlpath = generatedPath + "/jbi/META-INF";
        File jbiXmlFolder = new File(jbiXmlpath);
        jbiXmlFolder.mkdirs();
        JbiXmlWriter jbrWriter = new JbiXmlWriter(jbiXmlpath, eo.getName());
        jbrWriter.write();

        MetaDataService
                .registerObjectDefinition(new FileInputStream(objectFile));
    }

    private void makeResourcesJar() throws Exception {

        String projPath = getProject().getProperty("basedir");
        File destDir = new File(projPath + File.separator
                + EviewProjectProperties.EVIEW_GENERATED_FOLDER
                + File.separator + "resource");

        Delete delete = (Delete) getProject().createTask("delete");
        delete.setDir(destDir);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();

        // copy configuration file
        destDir.mkdir();
        File srcDir = new File(mSrcdir,
                EviewProjectProperties.CONFIGURATION_FOLDER);
        FileSet srcfileSet = new FileSet();
        srcfileSet.setDir(srcDir);
        Copy copy = (Copy) getProject().createTask("copy");
        copy.setTodir(destDir);
        copy.addFileset(srcfileSet);
        copy.init();
        copy.setLocation(getLocation());
        copy.execute();

        StandardizationIntrospector introspector = ClassUtils
                .loadDefaultService(StandardizationIntrospector.class);
        File repositoryDirectory = new File(mSrcdir,
                EviewProjectProperties.STANDARDIZATION_ENGINE_FOLDER);
        introspector.setRepositoryDirectory(repositoryDirectory);

        File repositoryLocation = new File(projPath + File.separator
                + EviewProjectProperties.EVIEW_GENERATED_FOLDER
                + File.separator + "resource" + File.separator
                + REPOSITORY_LOCATION);

        File repositoryImageZip = new File(repositoryLocation,
                REPOSITORY_RESOURCE_NAME);
        introspector.takeSnapshot(repositoryImageZip);

        Properties properties = new Properties();
        synchronized (this) {
            properties.setProperty(REPOSITORY_NAME_PROPERTY, "repository"
                    + System.currentTimeMillis());
        }
        properties.store(new FileOutputStream(new File(repositoryLocation,
                PROPERTIES_RESOURCE_NAME)), "Generated");

        // copy match engine file
        destDir = new File(projPath + File.separator
                + EviewProjectProperties.EVIEW_GENERATED_FOLDER
                + File.separator + "resource" + File.separator + "match");
        srcDir = new File(mSrcdir + File.separator
                + EviewProjectProperties.MATCH_ENGINE_FOLDER);
        srcfileSet = new FileSet();
        srcfileSet.setDir(srcDir);
        srcfileSet.setExcludes("lib/**");
        copy = (Copy) getProject().createTask("copy");
        copy.setTodir(destDir);
        copy.addFileset(srcfileSet);
        copy.init();
        copy.setLocation(getLocation());
        copy.execute();

        // copy filter configuration files
        destDir = new File(projPath + File.separator
                + EviewProjectProperties.EVIEW_GENERATED_FOLDER
                + File.separator + "resource");
        srcDir = new File(mSrcdir + File.separator
                + EviewProjectProperties.FILTER_FOLDER);
        if (srcDir.exists()) {
            srcfileSet = new FileSet();
            srcfileSet.setDir(srcDir);
            copy = (Copy) getProject().createTask("copy");
            copy.setTodir(destDir);
            copy.addFileset(srcfileSet);
            copy.init();
            copy.setLocation(getLocation());
            copy.execute();
        }

        // make resources.jar
        File jarFile = new File(projPath + File.separator + "lib"
                + File.separator + "resources.jar");
        if (jarFile.exists()) {
            jarFile.delete();
        }
        srcDir = new File(projPath + File.separator
                + EviewProjectProperties.EVIEW_GENERATED_FOLDER
                + File.separator + "resource");
        srcfileSet = new FileSet();
        srcfileSet.setDir(srcDir);
        Jar jar = (Jar) getProject().createTask("jar");
        jar.setDestFile(jarFile);
        jar.setCompress(true);
        jar.addFileset(srcfileSet);
        jar.setLocation(getLocation());
        jar.init();
        jar.execute();
    }

    private void makeClientJar() {
        String projPath = getProject().getProperty("basedir");
        String generatedPath = projPath + File.separator
                + EviewProjectProperties.EVIEW_GENERATED_FOLDER;
        String javacDebug = getProject().getProperty("javac.debug");
        
        File destDir = new File(generatedPath, "client/classes");
        // delete old class file
        Delete delete = (Delete) getProject().createTask("delete");
        delete.setDir(destDir);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();
        destDir.mkdirs();
        
        Javac javac = (Javac) getProject().createTask("javac");
        Path srcDir = new Path(getProject(), generatedPath + "/client/java");
        javac.setEncoding("UTF-8");
        javac.setSrcdir(srcDir);
        javac.setDestdir(destDir);
        Reference ref = new Reference(getProject(), "generate.class.path");
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
                + File.separator + "master-index-client.jar");
        jar.setDestFile(jarFile);
        jar.setBasedir(destDir);
        jar.init();
        jar.setLocation(getLocation());
        jar.execute();
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
        File srcDir = mTemplateDir;
        srcFileSet.setDir(srcDir);
        srcFileSet.setIncludes("repository/stc_sbme.jar," + "matcher.jar,"
                + "lucene-core.jar," + "index-core.jar,"
                + "net.java.hulp.i18n.jar, " + "standardizer/lib/*.jar");
        Copy copy = (Copy) getProject().createTask("copy");
        copy.init();
        copy.setTodir(destDir);
        copy.setFlatten(true);
        copy.addFileset(srcFileSet);
        copy.setLocation(getLocation());
        copy.execute();

        // copy match engine jar files from configuration folder to lib
        // directory
        srcDir = new File(mSrcdir + File.separator
                + EviewProjectProperties.MATCH_ENGINE_FOLDER);
        srcFileSet = new FileSet();
        srcFileSet.setDir(srcDir);
        srcFileSet.setIncludes("lib/*.jar");
        copy = (Copy) getProject().createTask("copy");
        copy.init();
        copy.setTodir(destDir);
        copy.setFlatten(true);
        copy.addFileset(srcFileSet);
        copy.setLocation(getLocation());
        copy.execute();

        //make resources.jar
        makeResourcesJar();
        //make master-index-client.jar
        makeClientJar();
    }

    private void generateEbjFiles(EIndexObject eo) {

        File destDir = new File(mEjbdir, "src/java/com/sun/mdm/index/ejb");
        Delete delete = (Delete) getProject().createTask("delete");
        delete.setDir(destDir);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();

        String srcPath = mTemplateDir + File.separator + "repository"
                + File.separator + "ejb-source.zip";
        File srcFile = new File(srcPath);
        destDir = new File(mEjbdir, "src/java");
        Expand expand = (Expand) getProject().createTask("unzip");
        expand.init();
        expand.setSrc(srcFile);
        expand.setDest(destDir);
        expand.setLocation(getLocation());
        expand.execute();

        String token = "_EVIEW_OBJECT_TOKEN_";
        String value = eo.getName();
        setEJBMappedName(token, value);
        setTransaction();
        setRoles();
        setSunEjbJarXML();

    }

    private void setSunEjbJarXML() {
        try {
            File outFile = new File(mEjbdir, "src/conf/sun-ejb-jar.xml");

            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream(
                            "com/sun/mdm/index/project/generator/descriptor/sun-ejb-jar.xml.tmpl");
            byte[] buf = new byte[0];
            byte[] chunk = new byte[4096];
            int count;
            while ((count = is.read(chunk)) >= 0) {
                byte[] temp = new byte[buf.length + count];
                System.arraycopy(buf, 0, temp, 0, buf.length);
                System.arraycopy(chunk, 0, temp, buf.length, count);
                buf = temp;
            }
            String content = new String(buf, "ISO8859-1");
            is.close();

            if (isSecurityEnable()) {
                content = content.replaceAll("_REPLACE_ROLES_TOKEN_",
                        getRoleMapping());
            } else {
                content = content.replaceAll("_REPLACE_ROLES_TOKEN_", "");

                int i = content.indexOf("<enterprise-beans>");
                int j = content.indexOf("</enterprise-beans>")
                        + "</enterprise-beans>".length();

                String s = content.substring(0, i);
                s = s + content.substring(j);

                content = s;

            }

            FileWriter fw = new FileWriter(outFile);
            fw.write(content);
            fw.close();
        } catch (UnsupportedEncodingException ex) {
            throw new BuildException(ex.getMessage());
        } catch (Exception ex) {
            throw new BuildException(ex.getMessage());
        }
    }

    private boolean isSecurityEnable() throws ParserConfigurationException,
            SAXException, IOException, XPathExpressionException {

        boolean onSwitch = true;

        File securityFile = new File(mSrcdir,
                EviewProjectProperties.CONFIGURATION_FOLDER + "/security.xml");
        Document doc = getDocument(securityFile);
        ArrayList<String> list = new ArrayList<String>();

        if (doc != null) {

            XPath xpath = XPathFactory.newInstance().newXPath();

            Element switchE = (Element) xpath.evaluate(
                    "//Configuration/SecurityConfig/ejbSecurity", doc,
                    XPathConstants.NODE);

            if (switchE != null) {
                String s = switchE.getTextContent();
                if (!"ON".equalsIgnoreCase(s.trim())) {
                    onSwitch = false;
                }
            }

        }
        return onSwitch;
    }

    private String getRoleMapping() throws ParserConfigurationException,
            SAXException, IOException {

        ArrayList<String> list = getSecurityRoles();

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (String r : list) {

            sb.append("<security-role-mapping>\n");
            sb.append("\t<role-name>");
            sb.append(r);
            sb.append("</role-name>\n");
            sb.append("\t<group-name>");
            sb.append(r);
            sb.append("</group-name>\n");
            sb.append("</security-role-mapping>\n");
        }

        return sb.toString();
    }

    private void setRoles() {

        String rolesToReplace = "\"_MasterIndex_Roles_token\"";
        StringBuilder sb = new StringBuilder();
        try {

            ArrayList<String> list = getSecurityRoles();

            sb.append("\"MasterIndex.Admin\"");

            for (String s : list) {
                String a = ",\"" + s + "\"";
                sb.append(a);
            }
            // sb.append("}");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // HashMap<String, String> tokenMap;
        // tokenMap = new HashMap<String, String>();
        // tokenMap.put(rolesToReplace, sb.toString());

        String ejbFilePath = mEjbdir.getAbsolutePath()
                + "/src/java/com/sun/mdm/index/ejb";
        String path = ejbFilePath + "/master/MasterControllerEJB.java";
        replaceToken(path, rolesToReplace, sb.toString());

    }

    /**
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private ArrayList<String> getSecurityRoles()
            throws ParserConfigurationException, SAXException, IOException {
        File securityFile = new File(mSrcdir,
                EviewProjectProperties.CONFIGURATION_FOLDER + "/security.xml");
        Document doc = getDocument(securityFile);
        ArrayList<String> list = new ArrayList<String>();

        if (doc != null) {

            XPath xpath = XPathFactory.newInstance().newXPath();

            try {
                NodeList elements = (NodeList) xpath.evaluate(
                        "//Configuration/SecurityConfig/role", doc,
                        XPathConstants.NODESET);

                for (int i = 0; i < elements.getLength(); i++) {
                    Element e = (Element) elements.item(i);

                    String roleName = e.getElementsByTagName("role-name").item(
                            0).getTextContent();
                    list.add(roleName);
                }
            } catch (XPathExpressionException ex) {
                throw new BuildException(ex.getMessage());
            } catch (DOMException ex) {
                throw new BuildException(ex.getMessage());
            }

        }
        return list;

    }

    private void setTransaction() {
        String transaction = "LOCAL";
        File masterFile = new File(mSrcdir,
                EviewProjectProperties.CONFIGURATION_FOLDER + "/master.xml");

        try {
            Document doc = getDocument(masterFile);
            NodeList nodes = doc.getElementsByTagName("transaction");
            Node transactionNode = nodes.item(0);
            if (transactionNode.getFirstChild().getNodeValue() != null) {
                transaction = transactionNode.getFirstChild().getNodeValue();
            }
        } catch (Exception e) {
            transaction = "LOCAL";
        }
        HashMap<String, String> tokenMap;
        if (transaction.equalsIgnoreCase("CONTAINER")) {
            tokenMap = new HashMap<String, String>();
            tokenMap.put("TransactionManagementType.BEAN",
                    "TransactionManagementType.CONTAINER");
            tokenMap.put("_CMT_XA__TOKEN", "CMT_XA");
        } else if (transaction.equalsIgnoreCase("BEAN")) {
            tokenMap = new HashMap<String, String>();
            // tokenMap.put("TransactionManagementType.BEAN","TransactionManagementType.BEAN");
            tokenMap.put("_CMT_XA__TOKEN", "BMT_XA");
        } else {
            tokenMap = new HashMap<String, String>();
            // tokenMap.put("TransactionManagementType.BEAN","TransactionManagementType.BEAN");
            tokenMap.put("_CMT_XA__TOKEN", "BMT_LOCAL");
        }

        String ejbFilePath = mEjbdir.getAbsolutePath()
                + "/src/java/com/sun/mdm/index/ejb";
        String path = ejbFilePath + "/master/MasterControllerEJB.java";
        replaceToken(path, tokenMap);
        replaceToken(path, tokenMap);
    }

    /**
     * @param masterFile
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private Document getDocument(File masterFile)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(false);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setIgnoringComments(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(masterFile);
        return doc;
    }

    private void generateWarFiles(String applicationName) {
        String edmVersion = getProject().getProperty("edm-version");
        String edmWarName = "edm.war";
        boolean jspExcluded = true;
        if (null != edmVersion
                && edmVersion.equalsIgnoreCase("master-index-edm")) {
            edmWarName = "index-webapp.war";
                        jspExcluded = false;
        } else {
            edmWarName = "edm.war";
        }

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

        File srcFile = new File(mTemplateDir, "repository/" + edmWarName);
        PatternSet patternSet = new PatternSet();
        // patternSet.setExcludes("**/*.jsp, **/META-INF/**");
        patternSet.setExcludes("**/META-INF/**");
        if (jspExcluded == true) {
            patternSet.setExcludes("**/*.jsp");
        }
        patternSet.setExcludes("**/edm.xml");
        patternSet.setExcludes("**/object.xml");
        patternSet.setExcludes("**/roles.xml");
        patternSet.setExcludes("**/midm.xml");
        Expand expand = (Expand) getProject().createTask("unzip");
        expand.init();
        expand.setSrc(srcFile);
        expand.setDest(destDir);
        expand.addPatternset(patternSet);
        expand.setLocation(getLocation());
        expand.execute();

        FileSet srcFileSet = new FileSet();
        File srcDir = new File(mSrcdir,
                EviewProjectProperties.CONFIGURATION_FOLDER);
        destDir = new File(mWardir, "web/WEB-INF/classes");
        srcFileSet.setDir(srcDir);
                if (null != edmVersion
                && edmVersion.equalsIgnoreCase("master-index-edm")) {
            srcFileSet.setIncludes("midm.xml");
        } else {
            srcFileSet.setIncludes("edm.xml");
        }
        Copy copy = (Copy) getProject().createTask("copy");
        copy.init();
        copy.setTodir(destDir);
        copy.addFileset(srcFileSet);
        copy.setLocation(getLocation());
        copy.execute();

        //set context root
        if (null != edmVersion && edmVersion.equalsIgnoreCase("master-index-edm")) {
            String token = "/SunEdm";
            String sunWebXml= (mWardir.getAbsolutePath()+"/web/WEB-INF/sun-web.xml");
            replaceToken(sunWebXml, token, "/"+applicationName+"MIDM" );         
        }               
    }

    private void addEjbLib() throws FileNotFoundException, IOException {

        File ejbPropertyFile = new File(mEjbdir, "nbproject/project.properties");
        java.util.Properties properties = new java.util.Properties();
        properties.load(new FileInputStream(ejbPropertyFile));
        properties.setProperty("file.reference.index-core.jar",
                "../lib/index-core.jar");
        properties.setProperty("file.reference.master-index-client.jar",
                "../lib/master-index-client.jar");
        properties.setProperty("file.reference.net.java.hulp.i18n.jar",
                "../lib/net.java.hulp.i18n.jar");
        properties.setProperty("javac.classpath",
                "${file.reference.index-core.jar}:"
                        + "${file.reference.master-index-client.jar}:"
                        + "${file.reference.net.java.hulp.i18n.jar}");

        properties.store(new FileOutputStream(ejbPropertyFile), null);
    }

    private void generateReportClient() {
        String projPath = getProject().getProperty("basedir");
        File destDir = new File(projPath + File.separator
                + EviewProjectProperties.REPORT_CLIENT_FOLDER);

        Delete delete = (Delete) getProject().createTask("delete");
        delete.setDir(destDir);
        delete.init();
        delete.setLocation(getLocation());
        delete.execute();

        destDir.mkdir();
        String modulePath = getProject().getProperty("module.install.dir");
        File srcDir = new File(modulePath + "/ext/mdm/repository/report");
        FileSet srcfileSet = new FileSet();
        srcfileSet.setDir(srcDir);

        Copy copy = (Copy) getProject().createTask("copy");
        copy.setTodir(destDir);
        copy.addFileset(srcfileSet);
        copy.init();
        copy.setLocation(getLocation());
        copy.execute();

        copy = (Copy) getProject().createTask("copy");
        destDir = new File(projPath + File.separator
                + EviewProjectProperties.REPORT_CLIENT_FOLDER + File.separator
                + "lib");
        copy.setTodir(destDir);
        srcDir = new File(modulePath + "/ext/mdm");
        srcfileSet = new FileSet();
        srcfileSet.setDir(srcDir);
        srcfileSet.setIncludes("index-core.jar");
        copy.addFileset(srcfileSet);
        copy.init();
        copy.setLocation(getLocation());
        copy.execute();
    }

    private boolean modified() {
        File folder = new File(getProject().getProperty("basedir")
                + File.separator
                + EviewProjectProperties.EVIEW_GENERATED_FOLDER
                + File.separator + "resource");
        // First, an up-to-date check
        long genModified = getLastModifiedTime(folder);

        // check if any source files are newer.
        ArrayList<File> srcFolders = new ArrayList<File>();
        folder = new File(mSrcdir + File.separator
                + EviewProjectProperties.CONFIGURATION_FOLDER);
        srcFolders.add(folder);
        folder = new File(mSrcdir + File.separator
                + EviewProjectProperties.MATCH_ENGINE_FOLDER);
        srcFolders.add(folder);
        folder = new File(mSrcdir + File.separator
                + EviewProjectProperties.STANDARDIZATION_ENGINE_FOLDER);
        srcFolders.add(folder);
        folder = new File(mSrcdir + File.separator
                + EviewProjectProperties.FILTER_FOLDER);
        srcFolders.add(folder);
        long srcModified = getLastModifiedTime(srcFolders);

        if (srcModified < genModified) {
            return false;
        } else {
            return true;
        }
    }

    private long getLastModifiedTime(File folder) {
        long mod = Long.MIN_VALUE;
        long lastModified = Long.MIN_VALUE;
        File[] files = folder.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    mod = files[i].lastModified();
                    if (mod > lastModified) {
                        lastModified = mod;
                    }

                } else {
                    mod = getLastModifiedTime(files[i]);
                    if (mod > lastModified) {
                        lastModified = mod;
                    }
                }
            }
        }
        return lastModified;
    }

    private long getLastModifiedTime(ArrayList<File> folder) {
        long mod = Long.MIN_VALUE;
        long lastModified = Long.MIN_VALUE;
        for (int i = 0; i < folder.size(); i++) {
            mod = getLastModifiedTime(folder.get(i));
            if (mod > lastModified) {
                lastModified = mod;
            }
        }
        return lastModified;
    }

    private void setEJBMappedName(String token, String value) {

        ArrayList<String> files = new ArrayList<String>();
        String ejbFilePath = mEjbdir.getAbsolutePath()
                + "/src/java/com/sun/mdm/index/ejb";
        String path = ejbFilePath + "/codelookup/CodeLookupEJB.java";
        files.add(path);
        path = ejbFilePath + "/codelookup/UserCodeLookupEJB.java";
        files.add(path);
        path = ejbFilePath + "/master/MasterControllerEJB.java";
        files.add(path);
        path = ejbFilePath + "/page/PageDataEJB.java";
        files.add(path);
        path = ejbFilePath + "/report/ReportGeneratorEJB.java";
        files.add(path);
        path = ejbFilePath + "/report/BatchReportGeneratorEJB.java";
        files.add(path);

        for (int i = 0; i < files.size(); i++) {
            String ejbFile = files.get(i).toString();
            replaceToken(ejbFile, token, value);
        }
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

    private void replaceToken(String fileName, HashMap<String, String> tokenMap) {
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
                Iterator iter = tokenMap.keySet().iterator();
                while (iter.hasNext()) {
                    String token = (String) iter.next();
                    String value = (String) tokenMap.get(token);
                    strLine = strLine.replaceAll(token, value);
                }
                buffOut.write(strLine);
                buffOut.newLine();
            }
            buffIn.close();
            buffOut.close();
        } catch (Exception ex) {
            if (file.exists()) {
                file.delete();
            }
            tempFile.renameTo(file);
                        throw new BuildException("can not access " + fileName);
        } finally {
            tempFile = new File(fileName + ".tmp");
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    /**
     * Get the CreateDDLWriter template depending on the database vendor type
     * 
     * @param db
     *            database vendor type
     * @return relative path of the CreateDDLWriter template
     */
    private static String getCreateDDLWriterTemplate(String db) {
        return getDDLWriterTemplate(db, "CreateDDLWriter.tmpl");
    }

    /**
     * Get the DropDDLWriter template depending on the database vendor type
     * 
     * @param db
     *            database vendor type
     * @return relative path of the DropDDLWriter template
     */
    private static String getDropDDLWriterTemplate(String db) {
        return getDDLWriterTemplate(db, "DropDDLWriter.tmpl");
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
        return "com/sun/mdm/index/project/generator/persistence/templates/"
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

}
