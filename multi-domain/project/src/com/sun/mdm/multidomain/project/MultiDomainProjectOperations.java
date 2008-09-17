/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.project;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.netbeans.api.project.Project;
import org.netbeans.spi.project.MoveOperationImplementation;
import org.openide.filesystems.FileUtil;

public class MultiDomainProjectOperations implements MoveOperationImplementation {
    
    private MultiDomainProject project;
    
    public MultiDomainProjectOperations(MultiDomainProject project) {
        this.project = project;
    }
    public List/*<FileObject>*/ getDataFiles() {
        return null;
    }       
    public List/*<FileObject>*/ getMetadataFiles() {
        return null;
    }
    public void notifyMoving() throws IOException {
    }
    
    public void notifyMoved(Project original, File originalPath, String nueName) {
        // Update all external relative paths
        String originalFilePath = originalPath.getPath();
        String newFilePath = FileUtil.toFile(project.getProjectDirectory()).getPath();
        if (!originalFilePath.equals(newFilePath)) {
        }
        project.setName(nueName);
    }
}