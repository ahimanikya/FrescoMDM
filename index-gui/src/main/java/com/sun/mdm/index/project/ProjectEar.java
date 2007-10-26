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


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.*;
import org.netbeans.modules.j2ee.deployment.devmodules.api.J2eeApplication;
import org.netbeans.modules.j2ee.deployment.devmodules.spi.J2eeApplicationImplementation;
import org.netbeans.modules.j2ee.deployment.devmodules.spi.J2eeModuleFactory;
import org.netbeans.modules.j2ee.deployment.devmodules.spi.J2eeModuleProvider;
import org.netbeans.modules.j2ee.deployment.common.api.EjbChangeDescriptor;
import org.netbeans.modules.j2ee.metadata.model.api.MetadataModel;
import org.openide.filesystems.FileObject;
import org.netbeans.modules.j2ee.deployment.devmodules.api.J2eeModule;
import org.netbeans.modules.j2ee.deployment.devmodules.api.ModuleChangeReporter;
import org.netbeans.modules.j2ee.deployment.devmodules.api.ModuleListener;
import org.openide.filesystems.FileUtil;

public final class ProjectEar extends J2eeModuleProvider
        implements 
            ModuleChangeReporter,
            J2eeApplicationImplementation {
      
    public static final String FILE_DD = "application.xml";//NOI18N   
    private EviewProject project;
    private J2eeApplication j2eeApplication;
    
    public ProjectEar (EviewProject project) {
        this.project = project;
    }
        
    public synchronized J2eeModule getJ2eeModule () {
        if (j2eeApplication == null) {
            j2eeApplication = J2eeModuleFactory.createJ2eeApplication(this);
        }
        return j2eeApplication;
    }
    
    public ModuleChangeReporter getModuleChangeReporter () {
        return this;
    }
     
    private Map<String, J2eeModuleProvider> mods = new HashMap<String, J2eeModuleProvider>();
    
    void setModules(Map<String, J2eeModuleProvider> mods) {
        if (null == mods) {
            throw new IllegalArgumentException("mods"); // NOI18N
        }
        this.mods = mods;
    }
    
    public J2eeModule[] getModules() {
        J2eeModule[] retVal = new J2eeModule[mods.size()];
        int i = 0;
        for (J2eeModuleProvider provider : mods.values()) {
            retVal[i++] = provider.getJ2eeModule();
        }
        return retVal;
    }
    
    public Object getModuleType() {
        return J2eeModule.EAR;
    }

    public FileObject getArchive() throws IOException {
        return project.getFileObject (EviewProjectProperties.DIST_JAR); //NOI18N
    }

    public Iterator getArchiveContents() throws IOException {
        return new IT(getContentDirectory());
    }

    public FileObject getContentDirectory() throws IOException {
        return project.getFileObject (EviewProjectProperties.BUILD_DIR); //NOI18N
    }

    public File getResourceDirectory() {
        return project.getFile(EviewProjectProperties.RESOURCE_DIR);
    }

    public File getDeploymentConfigurationFile(String name) {
        String path = getConfigSupport().getContentRelativePath(name);
        if (path == null) {
            path = name;
        }
        if (path.startsWith("META-INF/")) { // NOI18N
            path = path.substring(8); // removing "META-INF/"
        }
        FileObject moduleFolder = getMetaInf();
        File configFolder = FileUtil.toFile(moduleFolder);
        return new File(configFolder, path);
    }

    private FileObject getMetaInf() {
        return project.getOrCreateMetaInfDir();
    }
    
    public void setServerInstanceID(String severInstanceID) {
        throw new UnsupportedOperationException("ProjectEar.setServerInstanceID()--Not supported yet.");
    }
    
    public void addModuleListener(ModuleListener listener){
        throw new UnsupportedOperationException("ProjectEar.addModuleListener()--Not supported yet.");
    
    }
    
    public void removeModuleListener(ModuleListener arg0) {
        throw new UnsupportedOperationException("ProjectEar.removeModuleListener()--Not supported yet.");
    }  
    
    public String getModuleVersion() {
        throw new UnsupportedOperationException("ProjectEar.getModuleVersion()--Not supported yet.");
    }
    
    public String getUrl() {
        throw new UnsupportedOperationException("ProjectEar.getUrl()-- Not supported yet.");
    }
    
    public <T> MetadataModel<T> getMetadataModel(Class<T> arg0) {
        throw new UnsupportedOperationException("ProjectEar.getMetadataModel()-- Not supported yet.");
    }
    
    public void addPropertyChangeListener(PropertyChangeListener arg0) {
        throw new UnsupportedOperationException("ProjectEar.addPropertyChangeListener()-- Not supported yet.");
    }

    public void removePropertyChangeListener(PropertyChangeListener arg0) {
        throw new UnsupportedOperationException("ProjectEar.removePropertyChangeListener()--Not supported yet.");
    }

    public EjbChangeDescriptor getEjbChanges(long arg0) {
        throw new UnsupportedOperationException("ProjectEar.getEjbChanges()--Not supported yet.");
    }

    public boolean isManifestChanged(long arg0) {
        throw new UnsupportedOperationException("ProjectEar.isManifestChanged()--Not supported yet.");
    }

    public <T> MetadataModel<T> getDeploymentDescriptor(Class<T> arg0) {
        throw new UnsupportedOperationException("ProjectEar.getDeploymentDescriptor()--Not supported yet.");
    }

    private static class IT implements Iterator {
        Enumeration ch;
        FileObject root;
        
        private IT (FileObject f) {
            this.ch = f.getChildren (true);
            this.root = f;
        }
        
        public boolean hasNext () {
            return ch.hasMoreElements ();
        }
        
        public Object next () {
            FileObject f = (FileObject) ch.nextElement ();
            return new FSRootRE (root, f);
        }
        
        public void remove () {
            throw new UnsupportedOperationException ();
        }
        
    }

    private static final class FSRootRE implements J2eeModule.RootedEntry {
        FileObject f;
        FileObject root;
        
        FSRootRE (FileObject root, FileObject f) {
            this.f = f;
            this.root = root;
        }
        
        public FileObject getFileObject () {
            return f;
        }
        
        public String getRelativePath () {
            return FileUtil.getRelativePath (root, f);
        }
    }
    
}
