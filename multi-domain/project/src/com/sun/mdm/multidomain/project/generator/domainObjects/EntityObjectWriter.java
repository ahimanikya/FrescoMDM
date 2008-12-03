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
package com.sun.mdm.multidomain.project.generator.domainObjects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.xml.sax.InputSource;

import com.sun.mdm.multidomain.parser.Utils;
import com.sun.mdm.multidomain.parser.MiObject;
import com.sun.mdm.multidomain.parser.MiNodeDef;
import com.sun.mdm.multidomain.parser.MiRelationDef;
import com.sun.mdm.multidomain.parser.ParserException;
import com.sun.mdm.multidomain.project.generator.exception.TemplateWriterException;





public class EntityObjectWriter {
    
    private MiObject mEIndexObject;
    private String mPath;

    /**
     * @param path output path
     * @param eo Master Index object
     */
    public EntityObjectWriter(String path, MiObject eo) {
        mPath = path;
        mEIndexObject = eo;
    }
    
    /**
     * @param path output path
     * @param objectFile object.xml file
     * @exception TemplateWriterException Template writing exception
     */
    public EntityObjectWriter(String path, File objectFile)
        throws TemplateWriterException {
        try {
            mPath = path;
            InputSource source = new InputSource(new FileInputStream(objectFile));
            mEIndexObject = Utils.parseMiObject(source);
        } catch (ParserException ex) {
            throw new TemplateWriterException(ex.getMessage());
        } catch (FileNotFoundException ex) {
            throw new TemplateWriterException(ex.getMessage());
        }
    }


    /**
     * @param node Master Index object node definition
     * @param parent parent tag
     * @param childNames child names
     * @param relations list of relations
     * @exception TemplateWriterException Template writing exception
     */
    public void write(MiNodeDef node, 
                      String parent, 
                      ArrayList childNames, 
                      ArrayList relations)
            throws TemplateWriterException {
        
        ArrayList childList = null;
        if (null != childNames) {
            childList = new ArrayList();
            childList.addAll(childNames);
        }

        if (null != relations) {
            for (int i = 0; i < relations.size(); i++) {
                MiRelationDef rel = (MiRelationDef) relations.get(i);
                if (null == childList) {
                    childList = new ArrayList();
                }
                childList.add(rel.getName());
            }
        }

        ObjectNodeWriter onw
                 = new ObjectNodeWriter(mPath, 
                                        parent, 
                                        node, 
                                        childList, mEIndexObject.getApplicationName());
        onw.write();

        if (null != childNames) {
            for (int i = 0; i < childNames.size(); i++) {
                write(mEIndexObject.getNode((String) childNames.get(i)), 
                                            node.getTag(), null, null);
            }
        }

        if (null != relations) {
            for (int i = 0; i < relations.size(); i++) {
                MiRelationDef rel = (MiRelationDef) relations.get(i);
                write(mEIndexObject.getNode(rel.getName()), 
                      node.getTag(), 
                      rel.getChildren(), 
                      rel.getRelations());
            }
        }
    }


    /**
     * @exception TemplateWriterException Template writing exception
     */
    public void write() throws TemplateWriterException{
        
        ArrayList relList = mEIndexObject.getRelationships();
        if (null != relList) {
            for (int i = 0; i < relList.size(); i++) {
                MiRelationDef relDef = (MiRelationDef) relList.get(i);
                MiNodeDef node = mEIndexObject.getNode(relDef.getName());
                write(node, 
                      "", 
                      relDef.getChildren(), 
                      relDef.getRelations());
            }
        }
    }
}

