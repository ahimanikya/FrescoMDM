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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.openide.modules.InstalledFileLocator;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.FileLock;
import org.openide.loaders.DataObject;


/**
 * Used by Netbeans Master Index Project to retrieve files from Netbeans Installation and Project location.   
 */
    public class EviewRepository {
    /*
     * Singleton instance of EviewRepository
    */
    private static EviewRepository repository = new EviewRepository();
    private FileObject currentFolder;

    /**
      protected constructor
    */
    protected EviewRepository() {
       
    }

    /**
      get the singleton instance of EviewRepository
    */
    public static EviewRepository getEviewRepository() {
     
        return repository;     

    }

   
   

    /**
     create Configuration File at Project location
     @param folder folder name
     @name  relative path of file to folder
     @data data to be inserted into the file
    */

    public FileObject createConfigurationFile(FileObject folder, String name, String data) 
    throws EviewRepositoryException 
    {
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
/*

   FileObject createConfigurationFile(String name, String data) {
       return createConfigurationFile(currentFolder, name, data);
       
   }   

   public void setFolder(FileObject folder) {
     currentFolder = foolder;    
   }

  */ 

   
/** 
    return FileObject from Netbeans installation.
    Can be used for any file ex. Jar file
    @param name relative path of File
    @return FileObject
    Return null, if no File exists
*/
    public FileObject getInstalledFile(String fname)  {
        if(fname == null) {
            return null;
        }
        File file = InstalledFileLocator.getDefault().locate(fname, "", false);
        FileObject fileObject = null;
        if (file != null) {
            fileObject = FileUtil.toFileObject(file);
        }
        return fileObject;
    }


/** 
    return FileObject[] from folder, from Netbeans installation
    Return null, if no File exists
*/
    public FileObject[] getInstalledTemplates(String folder, String locale) {
       
        FileObject dir = getInstalledDir(folder, locale);
        if (dir == null) {
            return null;
        } 
        
        FileObject[] fileObjects = dir.getChildren();
        
        return fileObjects;
      
    }


   /** 
    return FileObject[] from Project location
    Return null, if no File exists
*/
    public FileObject[] getTemplates(String folder, String locale)  {
      
        FileObject dir = getDir(folder, locale);
        if (dir == null) {
            return null;
        }
        
        FileObject[] fileObjects = dir.getChildren();
        
        return fileObjects;
   }


/** return Configuration FileObject from project directory
    If create == true, then it will create empty file if it does not exist
    Return null, if no File exists and create == false
     throws EviewRepositoryException if  it can�t create a file
*/
    public FileObject getConfigurationFile(String folder, String name, boolean create) throws EviewRepositoryException {
        try{
            if (folder == null) {
                return null;
            }
            FileObject dir = FileUtil.toFileObject(new File(folder));
            if (dir == null || !dir.isFolder()) {
                return null;
            }
      
            FileObject file = dir.getFileObject(name);
            if (file == null && create == true) {
                file = dir.createData(name);
            }
            return file;
        } catch (IOException ex) {
            throw new EviewRepositoryException(ex);
        }
    }


/** delete configuration file from project directory
  throws EviewRepositoryException if it can�t delete a file
*/
    public boolean deleteConfiguration(String folder, String name)  throws EviewRepositoryException {
        try {
            if (folder == null) {
                return false;
            }
            FileObject dir = FileUtil.toFileObject(new File(folder));
            if (dir == null || !dir.isFolder()) {
                return false;
            }
      
            FileObject file = dir.getFileObject(name);
            file.delete();
        } catch (IOException ex) {
            throw new EviewRepositoryException(ex);
        }
        return true;
    }

/**
========================================================================================================

private methods

=======================================================================================================
*/

    private FileObject getInstalledDir(String category, String locale) {
        String name = category;
        if ( name == null) {
            return null;
        }
      
        if (locale != null) {
            name = category + "/" + locale;
        }
        File dir = InstalledFileLocator.getDefault().locate(name, "", false);
        FileObject fileObject = FileUtil.toFileObject(dir);
        if (!fileObject.isFolder()) {
            return null;
        }
        return fileObject;
    }

    private FileObject getDir(String category, String locale) {
        if ( category == null) {
            return null;
        }
        String name = category;
      
        if (locale != null) {
            name = category + "/" + locale;
        }
        File dir = new File(name);
        FileObject fileObject = FileUtil.toFileObject(dir);
        if (!fileObject.isFolder()) {
            return null;
        }
        return fileObject;
    }
}
  
