/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.project;

import com.sun.mdm.multidomain.project.generator.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jlu
 */
public class EjbProjectManager {
  
        public static void addLibsToEjbProject( File ejbDir, 
                                                ArrayList<String> libs, 
                                                String relativeLocation) 
                throws FileNotFoundException, IOException, Exception {
        
        ArrayList<String> newLibs =new ArrayList<String>();
        File ejbProjectXml = new File(ejbDir,"nbproject/project.xml");
        String ejbProjectXmlString = FileUtil.readFileToString(ejbProjectXml);
        for (String libName:libs){
            if(ejbProjectXmlString.indexOf(libName)<0){
                newLibs.add(libName);
            }
        }
        if (newLibs.size()==0) return; 
        
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilder = docBuilderFactory.newDocumentBuilder();
        Document ejbProjectXmlDoc = docBuilder.parse(ejbProjectXml);
        ArrayList <Element> includedLibraryList = new ArrayList<Element>(); 
        
        File ejbPropertyFile = new File(ejbDir, "nbproject/project.properties");
        java.util.Properties properties = new java.util.Properties();
        properties.load(new FileInputStream(ejbPropertyFile));
        
        StringBuffer classpath;
        StringBuffer manifesClasspath = new StringBuffer();
        if (null==properties.getProperty("javac.classpath")){
            classpath = new StringBuffer();
        }else{
            classpath = new StringBuffer(properties.getProperty("javac.classpath").trim());
        }
        
        File buildImplXml = new File(ejbDir,"nbproject/build-impl.xml");
        docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docBuilder = docBuilderFactory.newDocumentBuilder();        
        Document buildImplXmlDoc = docBuilder.parse(buildImplXml);
        ArrayList <Element> archiveList = new ArrayList<Element>();   
        ArrayList <Element> manifestList = new ArrayList<Element>();
        for (String newLibName:newLibs){
            //for project.properties
            properties.setProperty("file.reference." + newLibName,
                relativeLocation + "/" + newLibName);
            
            if (classpath.length()< 1){
                classpath.append("${file.reference." + newLibName +"}");
            }else{
                classpath = classpath.append(":${file.reference." + newLibName +"}");
            }
            
            //ejb project.xml
            Element ejbProjectXmlElement = ejbProjectXmlDoc.createElement("included-library");
            ejbProjectXmlElement.setAttribute("files", "1");
            ejbProjectXmlElement.setTextContent("file.reference." + newLibName);
            includedLibraryList.add(ejbProjectXmlElement);
            
            //ejb build-impl.xml
            Element buildImplXmlElement = buildImplXmlDoc.createElement("copy");
            buildImplXmlElement.setAttribute("file", "${file.reference." + newLibName + "}");
            buildImplXmlElement.setAttribute("todir", "${build.classes.dir}");
            archiveList.add(buildImplXmlElement);
            
            buildImplXmlElement = buildImplXmlDoc.createElement("basename");
            buildImplXmlElement.setAttribute("file", "${file.reference." + newLibName + "}");
            buildImplXmlElement.setAttribute("property", "included.lib.file.reference." + newLibName);
            manifestList.add(buildImplXmlElement);
            buildImplXmlElement = buildImplXmlDoc.createElement("copy");
            buildImplXmlElement.setAttribute("file", "${file.reference." + newLibName + "}");
            buildImplXmlElement.setAttribute("todir", "${dist.ear.dir}");
            manifestList.add(buildImplXmlElement);
            
            manifesClasspath.append("${file.reference." + newLibName + "} ");
            
        }
        
        //write to project.properties
        properties.setProperty("javac.classpath", classpath.toString() );
        properties.store(new FileOutputStream(ejbPropertyFile), null);
        
        //write to project.xml
        NodeList dataNodes = ejbProjectXmlDoc.getElementsByTagName("data");
        Element dataElement =  (Element)dataNodes.item(0);
        for(Element includedLibrary: includedLibraryList){
            dataElement.appendChild(includedLibrary);
        }        
        FileUtil.updateFile(ejbProjectXml, FileUtil.transformXMLtoString(ejbProjectXmlDoc));
        
        //write to build-impl.xml
        NodeList nodes = buildImplXmlDoc.getElementsByTagName("target");
        for(int i=0; i<nodes.getLength(); i++){
            Element element = (Element)(nodes.item(i));
            if (element.hasAttribute("name")&&
                    element.getAttribute("name").equals("library-inclusion-in-archive")){
                for(Element archive: archiveList){
                    element.appendChild(archive);
                }
            }
            
            if (element.hasAttribute("name")&&
                    element.getAttribute("name").equals("library-inclusion-in-manifest")){
                for(Element manifest: manifestList){
                    element.insertBefore(manifest, element.getLastChild());
                }
                
                NodeList manifestNodes = element.getElementsByTagName("manifest");
                Element manifestElement =  (Element)manifestNodes.item(0);
                Node attributeNode= manifestElement.getElementsByTagName("attribute").item(0);
                Element attributeElement = null;
                if (attributeNode==null){
                    attributeElement = buildImplXmlDoc.createElement("attribute");
                    attributeElement.setAttribute("name", "Class-Path");
                    attributeElement.setAttribute("value", manifesClasspath.toString());
                    manifestElement.appendChild(attributeElement);
                }else{
                    attributeElement = (Element)attributeNode;
                    String value = attributeElement.getAttribute("value");
                    attributeElement.setAttribute("value", manifesClasspath.toString()+ value);
                }
            }

        }
        
        FileUtil.updateFile(buildImplXml, FileUtil.transformXMLtoString(buildImplXmlDoc));
            
    }

}
