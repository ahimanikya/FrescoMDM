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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import com.sun.mdm.index.parser.EIndexObject;
import com.sun.mdm.index.parser.NodeDef;
import com.sun.mdm.index.parser.ParserException;
import com.sun.mdm.index.parser.RelationDef;
import com.sun.mdm.index.parser.Utils;
import java.io.File;
import java.io.FileInputStream;
import org.xml.sax.InputSource;


public class EntityObjectWriter {
    private EIndexObject mEIndexObject;
    private String mPath;


    /**
     * @param path template path
     * @param eo elephant object
     * @exception ParserException parser exception
     */
    public EntityObjectWriter(String path, EIndexObject eo)
        throws ParserException {
        mPath = path;
        mEIndexObject = eo;
    }
    
    /**
     * @param path template path
     * @param File objectFile
     * @exception ParserException parser exception
     */
    public EntityObjectWriter(String path, File objectFile)
        throws ParserException {
        try {
            mPath = path;
            InputSource source = new InputSource(new FileInputStream(objectFile));
            mEIndexObject = Utils.parseEIndexObject(source);
        } catch (FileNotFoundException ex) {
            new ParserException(ex);
        }
    }


    /**
     * @param node node definition
     * @param parent parent tag
     * @param childNames child names
     * @param relations list of relations
     * @exception ParserException parser exception
     */
    public void write(NodeDef node, 
                      String parent, 
                      ArrayList childNames, 
                      ArrayList relations)
        throws ParserException {
        try {
            ArrayList childList = null;
            if (null != childNames) {
                childList = new ArrayList();
                childList.addAll(childNames);
            }

            if (null != relations) {
                for (int i = 0; i < relations.size(); i++) {
                    RelationDef rel = (RelationDef) relations.get(i);
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
                                            childList);
            onw.write();

            if (null != childNames) {
                for (int i = 0; i < childNames.size(); i++) {
                    write(mEIndexObject.getNode((String) childNames.get(i)), 
                                                node.getTag(), null, null);
                }
            }

            if (null != relations) {
                for (int i = 0; i < relations.size(); i++) {
                    RelationDef rel = (RelationDef) relations.get(i);
                    write(mEIndexObject.getNode(rel.getName()), 
                          node.getTag(), 
                          rel.getChildren(), 
                          rel.getRelations());
                }
            }
        } catch (ParserException e) {
            throw e;
        }
    }


    /**
     * @exception ParserException parser exception
     */
    public void write()
        throws ParserException {
        try {
            ArrayList relList = mEIndexObject.getRelationships();
            if (null != relList) {
                for (int i = 0; i < relList.size(); i++) {
                    RelationDef relDef = (RelationDef) relList.get(i);
                    NodeDef node = mEIndexObject.getNode(relDef.getName());
                    write(node, 
                          "", 
                          relDef.getChildren(), 
                          relDef.getRelations());
                }
            }
        } catch (ParserException e) {
            throw e;
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }
}

