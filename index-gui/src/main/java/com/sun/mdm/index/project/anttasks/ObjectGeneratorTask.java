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

import java.io.File;
import org.apache.tools.ant.Task;
import com.sun.mdm.index.parser.EIndexObject;
import com.sun.mdm.index.parser.ParserException;
import com.sun.mdm.index.parser.Utils;
import com.sun.mdm.index.project.generator.exception.TemplateWriterException;
import com.sun.mdm.index.project.generator.objects.EntityObjectWriter;
import com.sun.mdm.index.project.generator.ops.OPSWriter;
import com.sun.mdm.index.project.generator.validation.ObjectDescriptorWriter;
import com.sun.mdm.index.project.generator.webservice.WebServiceWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.xml.sax.InputSource;

/**
 *
 * @author jlu
 */
public class ObjectGeneratorTask extends Task {
    private File mDestDir;
    private File mObjectFile;
    
    public void setDestdir(File destDir) {
        this.mDestDir = destDir;
    }
    public void setObjectFile(File objectFile) {
        this.mObjectFile = objectFile;
    }
    
    public void execute() throws BuildException{  
        
        if (mDestDir == null){
            throw new BuildException ("Must specify the destination directory.");
        }
        if (mObjectFile == null){
            throw new BuildException ("Must specify the Object File.");
        }
        try{
            InputSource source = new InputSource(new FileInputStream(mObjectFile));
            EIndexObject eo = Utils.parseEIndexObject(source);  
            generateFiles(eo);
        }catch(Exception ex){
                throw new BuildException(ex.getMessage());         
        }
    }
    
    public void generateFiles(EIndexObject eo ) throws FileNotFoundException, TemplateWriterException, ParserException, IOException{      
        File newPath = null;
        newPath = new File(mDestDir, "com/sun/mdm/index/objects");
        newPath.mkdirs();
        newPath = new File(mDestDir, "com/sun/mdm/index/objects/validation");
        newPath.mkdirs();
        newPath = new File(mDestDir, "com/sun/mdm/index/ops");
        newPath.mkdirs();
        newPath = new File(mDestDir, "com/sun/mdm/index/webservice");
        newPath.mkdirs();            
        ObjectDescriptorWriter odw = new ObjectDescriptorWriter(mDestDir.getAbsolutePath(), eo);
        odw.write();
        
        EntityObjectWriter eow = new EntityObjectWriter(mDestDir.getAbsolutePath(), eo);
        eow.write();
        
        OPSWriter opsw = new OPSWriter(mDestDir.getAbsolutePath(), eo);
        opsw.write();
        
        WebServiceWriter wsWriter = new WebServiceWriter(mDestDir.getAbsolutePath(), eo);
        wsWriter.write();                
    }
    

}
