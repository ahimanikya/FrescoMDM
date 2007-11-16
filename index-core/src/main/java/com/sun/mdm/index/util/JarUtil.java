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
package com.sun.mdm.index.util;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.JarEntry;
import java.util.jar.Manifest;
import java.util.Properties;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import com.sun.mdm.index.util.Localizer;

/**
 * Utilities for jar file manipulation
 * @author  dcidon
 */
public class JarUtil {
    
    private transient static final Localizer mLocalizer = Localizer.get();

    static final int BUFFER_SIZE = 2048;
    public final static String ENC = "US-ASCII"; 
    
    /** Creates a new instance of JarUtil */
    private JarUtil() {
    }
    
    /** Add an entry to the jar file
     * @param jarFileName full path to existing jar file
     * @param fileToAdd full path of file to be added
     * @param entryName name of entry as it should appear in jar file.  
     * Example: com/sun/mdm/index/con.properties
     * @throws FileNotFoundException jarFile or fileToAdd not found
     * @throws IOException error
     */
    public static void addEntryToJar(String jarFileName, String fileToAdd, String entryName)
    throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(fileToAdd);
        addEntryToJar(jarFileName, fis, entryName);
    }
    
    /** Add an entry to the jar file
     * @param jarFileName full path to existing jar file
     * @param entryInputStream stream of data to be added
     * @param entryName name of entry as it should appear in jar file.  
     * Example: com/sun/mdm/index/con.properties
     * @throws FileNotFoundException jarFile or fileToAdd not found
     * @throws IOException error
     */
    public static void addEntryToJar(String jarFileName, InputStream entryInputStream, String entryName)
    throws FileNotFoundException, IOException {
        File jarFile = new File(jarFileName);        
        if (!jarFile.exists()) {
            throw new IOException(mLocalizer.t("UTL505: Jar file " +
                                            "does not exist: {0}", 
                                            jarFile.getAbsolutePath()));
        }
        
        //Check if there is an existing temp file.  If yes, delete it
        File jarTempFile = new File(jarFileName + ".tmp");
        deleteFile(jarTempFile);
        
        boolean bool = jarFile.renameTo(jarTempFile);
        if (!bool) {
    	    throw new IOException(mLocalizer.t("UTL506: Unable to " +
                                            "rename jar file: {0}", 
                                            jarFile.getAbsolutePath()));
        }
        
        jarFile = new File(jarFileName);        

        FileInputStream jarInputStream = new FileInputStream(jarTempFile);
        FileOutputStream jarOutputStream = new FileOutputStream(jarFile);

        addEntryToJar(jarInputStream, jarOutputStream, entryInputStream, entryName); 
        deleteFile(jarTempFile);
    }
        
    /**
     * Add an entry to existing jar file
     * @param jarInputStream stream representing jar file
     * @param jarOutputStream stream to put updated jar file
     * @param entryInputStream stream of data to be added to input
     * @param entryName name of entry to be added
     * @throws IOException io error
     */
    public static void addEntryToJar(InputStream jarInputStream, OutputStream jarOutputStream,
    InputStream entryInputStream, String entryName)
    throws IOException {
            BufferedInputStream jarBufferInput = new BufferedInputStream(jarInputStream, BUFFER_SIZE);
            BufferedOutputStream jarBufferOutput = new BufferedOutputStream(jarOutputStream, BUFFER_SIZE);
            JarInputStream jarInput = new JarInputStream(jarBufferInput);
            Manifest manifest = jarInput.getManifest();
            JarOutputStream jarOutput = new JarOutputStream(jarBufferOutput, manifest);
            JarEntry entry;
            int count;
            byte data[] = new byte[BUFFER_SIZE];
            while ((entry = jarInput.getNextJarEntry()) != null) {
                if (!entry.getName().equals(entryName)){
	                jarOutput.putNextEntry(entry);
	                while ((count = jarInput.read(data, 0, BUFFER_SIZE)) != -1) {
	                    jarOutput.write(data, 0, count);
	                }
                }
            }
            entry = new JarEntry(entryName);
            jarOutput.putNextEntry(entry);
            while ((count = entryInputStream.read(data, 0, BUFFER_SIZE)) != -1) {
                jarOutput.write(data, 0, count);
            } 
            entryInputStream.close();
            
            jarInput.close();
            jarOutput.flush();
            jarOutput.close();
    }

    /**
     * Copy an existing jar file to another jar file
     * @param jarInputStream stream representing jar file
     * @param jarOutputStream stream to put updated jar file
     * @throws IOException io error
     */
    public static void copyJar(InputStream jarInputStream, OutputStream jarOutputStream)
    throws IOException {
            BufferedInputStream jarBufferInput = new BufferedInputStream(jarInputStream, BUFFER_SIZE);
            BufferedOutputStream jarBufferOutput = new BufferedOutputStream(jarOutputStream, BUFFER_SIZE);
            JarInputStream jarInput = new JarInputStream(jarBufferInput);
            Manifest manifest = jarInput.getManifest();
            JarOutputStream jarOutput = new JarOutputStream(jarBufferOutput, manifest);
            JarEntry entry = null;
            int count = -1;
            byte data[] = new byte[BUFFER_SIZE];
            while ((entry = jarInput.getNextJarEntry()) != null) {            
                jarOutput.putNextEntry(entry);
                while ((count = jarInput.read(data, 0, BUFFER_SIZE)) != -1) {
                    jarOutput.write(data, 0, count);
                }
            }

            jarInputStream.close();
            jarOutput.close();
    }
        
    
    /**
     * Add a property file to a jar file
     * @param props prop file to be added
     * @throws IOException error
     */
    public static void addPropertiesToJarFile(Properties props) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        String header = "";
        props.store(outStream, header);
        ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
    }
    
     /**
      *   replaces strings in the entryName in a given jar file.
      *   replacement strings are specified in Map values.
      *   An example is to replace ejb/MasterController to ejb/PersonMasaterController
      *   in entry seebeyond-ejb.xml in ejb_service.jar file.
      *   @param path path for the jar file
      *   @param jarName name of jar file
      *   @entryName name of Jar entry to which replacement is applied
      *   @values  Map of {string, replaceString} that is applied to that entry
      *   
      *
      */ 
   
    
    public static void  replaceJarValues(String path, String jarName, String entryName, Map values)
       throws Exception {
        String jarPath = path + "/" + jarName;
        FileInputStream input = new FileInputStream(jarPath);
        String tempJar = path + "/" + "temp" + jarName;
        FileOutputStream output = new FileOutputStream(tempJar);
        BufferedInputStream jarBufferInput =  new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream jarBufferOutput = new BufferedOutputStream(output, BUFFER_SIZE);
              
        JarInputStream jarInput = new JarInputStream(jarBufferInput); 
        Manifest manifest = jarInput.getManifest();
        JarOutputStream jarOutput = new JarOutputStream(jarBufferOutput, manifest);
        JarEntry entry = null;
               
          byte data[] = new byte[BUFFER_SIZE];
          while ((entry = jarInput.getNextJarEntry()) != null) {
                   
                if ( entry.getName().equals(entryName)) {
                    replaceEntryValues(entry, values, jarInput, jarOutput, true);
                } else {
                    replaceEntryValues(entry, values, jarInput, jarOutput, false);
                    
                }
            }
  
          jarInput.close();
          jarOutput.close();
          
          File jarFile = new File(jarPath);
          jarFile.delete();
          File tempJarFile = new File(tempJar);
          tempJarFile.renameTo(jarFile);
          
          
    }
      
    private static void replaceEntryValues(JarEntry entry, Map values, JarInputStream jarInput,
         JarOutputStream jarOutput, boolean replaceFlag) throws Exception {
             
         long lsize = entry.getSize();
         int size = (int)lsize;
         
         if (size == -1 ) {
             size = 5*BUFFER_SIZE;
             
         }
               
          int count = -1;
         if (replaceFlag == false) { 
             // or just call copyEntry(entry, jarInput, jarOutput);
             byte data[] = new byte[BUFFER_SIZE];
            jarOutput.putNextEntry(entry);
            while ((count = jarInput.read(data, 0, BUFFER_SIZE)) != -1) {
                jarOutput.write(data, 0, count);  
            }  
         } else {
             
             ArrayList datalist = new ArrayList();
             ArrayList sizeList = new ArrayList();
             
             byte data[] = new byte[size];
             while ((count = jarInput.read(data, 0, size)) != -1) {
                 datalist.add(data);
                 sizeList.add(new Integer(count));
                 data = new byte[size];
                 
             }
             int allsize = 0;
             for (int i =0; i < sizeList.size(); i++) {
                allsize += ((Integer)sizeList.get(i)).intValue(); 
             }
             
             byte[] alldata = new byte[allsize];
             int destPos = 0;
             for (int i =0; i < sizeList.size(); i++) {
                int cnt = ((Integer)sizeList.get(i)).intValue();
                System.arraycopy(datalist.get(i), 0, alldata, destPos, cnt);  
                destPos += cnt;
             }
             
             String datastr = new String(alldata, 0, allsize, ENC);
                
             Iterator it = values.keySet().iterator();
              while (it.hasNext()) {
                    String key = (String) it.next();
                    String value = (String) values.get(key);
                    datastr = datastr.replaceAll(key, value);                                       
              }                 
              byte[] newData = datastr.getBytes(ENC);
              JarEntry nentry = new JarEntry(entry.getName());
              jarOutput.putNextEntry(nentry);
              jarOutput.write(newData, 0, newData.length);  
              jarOutput.flush();
              jarOutput.closeEntry();
         }  
         
    }
      
    private static void copyEntry(JarEntry entry, JarInputStream jarInput,
        JarOutputStream jarOutput) throws Exception {
               
        int count = -1;
        byte data[] = new byte[BUFFER_SIZE];
        jarOutput.putNextEntry(entry);
        while ((count = jarInput.read(data, 0, BUFFER_SIZE)) != -1) {
            jarOutput.write(data, 0, count);  
        }  
    }
  
    private static String getDataString(JarEntry entry, JarInputStream jarInput) throws Exception  {
    	long lsize = entry.getSize();
        int size = (int)lsize;
         
        if (size == -1 ) {
        	size = 5*BUFFER_SIZE;
        }
               
        int count = -1;
        ArrayList datalist = new ArrayList();
        ArrayList sizeList = new ArrayList();
             
        byte data[] = new byte[size];
        while ((count = jarInput.read(data, 0, size)) != -1) {
            datalist.add(data);
            sizeList.add(new Integer(count));
            data = new byte[size];
        }
        int allsize = 0;
        for (int i =0; i < sizeList.size(); i++) {
           allsize += ((Integer)sizeList.get(i)).intValue(); 
        }
             
        byte[] alldata = new byte[allsize];
        int destPos = 0;
        for (int i =0; i < sizeList.size(); i++) {
           int cnt = ((Integer)sizeList.get(i)).intValue();
           System.arraycopy(datalist.get(i), 0, alldata, destPos, cnt);  
           destPos += cnt;
        }
             
        String datastr = new String(alldata, 0, allsize, ENC);
        return datastr;
    }


    private static void addValuesToXmlEntry(JarEntry entry, String targetTag, String values, JarInputStream jarInput,
        JarOutputStream jarOutput) throws Exception {
             
        String datastr = getDataString(entry, jarInput);
        String[] sList = datastr.split(targetTag);
        if (sList != null && sList.length > 0) {
            datastr = "";
            for (int i=0; i < sList.length; i++) {
                if (i == sList.length - 1) {
                    datastr += sList[i];
                } else {
                	//generate ra resource reference id for webspere by attach"_eWay" to datasource reference id
                	String tempValue = values ;
                	if (values.indexOf("id=")> -1){ 
	                	int fromIndex = sList[i].lastIndexOf("resource-ref");
	                	int beginIndex = sList[i].indexOf('\"', fromIndex);
	                	int endIndex = sList[i].indexOf('\"', beginIndex+1);
	                	String id = sList[i].substring(beginIndex + 1, endIndex);
	                	
	                	int tempIndex = values.indexOf("id=");
	                	tempValue = values.substring(0, tempIndex+3)+ "\"" +id+"_eWay"+"\"" + 
	                		values.substring(tempIndex+3);
	     
                	}else if (values.indexOf("bindingResourceRef")> -1){ 
                		int fromIndex = sList[i].indexOf("bindingResourceRef");
	                	int beginIndex = sList[i].indexOf('#', fromIndex);
	                	int endIndex = sList[i].indexOf('\"', beginIndex+1);
	                	String id = sList[i].substring(beginIndex + 1, endIndex);
	                	
	                	fromIndex = values.indexOf("bindingResourceRef");
	                	int tempIndex = values.indexOf("#",fromIndex);
	                	tempValue = values.substring(0, tempIndex+1) +id + "_eWay" +
                			values.substring(tempIndex+1);
                	}
                    datastr += sList[i] + targetTag + "\r\n" + tempValue;
                }
            }
        }
        byte[] newData = datastr.getBytes(ENC);
        JarEntry nentry = new JarEntry(entry.getName());
        jarOutput.putNextEntry(nentry);
        jarOutput.write(newData, 0, newData.length);  
        jarOutput.flush();
        jarOutput.closeEntry();         
    }
    
    /* Called by CodeletEJBHelper.addOracleEwayResourceRef()
     *@param path target folder
     *@param jarName
     *@param targetXml
     *@param targetTag
     *@param String values
     */
    public static void  addValuseToJarXmlEntry(String path, String jarName, String targetXml, String targetTag, String values)
       throws Exception {
        String jarPath = path + "/" + jarName;
        FileInputStream input = new FileInputStream(jarPath);
        String tempJar = path + "/" + "temp" + jarName;
        FileOutputStream output = new FileOutputStream(tempJar);
        BufferedInputStream jarBufferInput =  new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream jarBufferOutput = new BufferedOutputStream(output, BUFFER_SIZE);
              
        JarInputStream jarInput = new JarInputStream(jarBufferInput); 
        Manifest manifest = jarInput.getManifest();
        JarOutputStream jarOutput = new JarOutputStream(jarBufferOutput, manifest);
        JarEntry entry = null;
               
        byte data[] = new byte[BUFFER_SIZE];
        while ((entry = jarInput.getNextJarEntry()) != null) {                   
            if ( entry.getName().equals(targetXml)) {
                // add values to targetXml and replace it in jarOutput
                addValuesToXmlEntry(entry, targetTag, values, jarInput, jarOutput);
            } else {
                // copy it to jarOutput
                copyEntry(entry, jarInput, jarOutput);
            }
        }
  
        jarInput.close();
        jarOutput.close();
          
        File jarFile = new File(jarPath);
        jarFile.delete();
        File tempJarFile = new File(tempJar);
        tempJarFile.renameTo(jarFile);          
    }
    
    
    private static void deleteFile(File file) throws IOException {
        if (file.exists()) {
            if (file.delete() != true) {
                throw new IOException(mLocalizer.t("UTL507: Unable to " +
                                            "delete file: {0}", 
                                            file.getAbsolutePath()));
            }
        }        
    }
    
    
	public static String readFileFromJar(String path, String jarName, String fileName )
	throws Exception {
		 String fileContent = null;
		 String jarPath = path + "/" + jarName;
		 FileInputStream input = new FileInputStream(jarPath);    
		 BufferedInputStream jarBufferInput =  new BufferedInputStream(input, BUFFER_SIZE);     
		 JarInputStream jarInput = new JarInputStream(jarBufferInput); 
		 JarEntry entry = null;
		 while ((entry = jarInput.getNextJarEntry()) != null) {                   
		     if ( entry.getName().equals(fileName)) {
		    	 fileContent =  getDataString(entry, jarInput);
		     }
		 }
		 jarInput.close();  
		 return fileContent;
	}
    
    /** Command line use
     * @param args 0:jarFile, 1:fileToAdd, 2:entryName
     */
    public static void main(String args[]) {
        try {
            addEntryToJar(args[0], args[1], args[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
