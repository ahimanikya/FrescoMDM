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
package com.sun.mdm.index.project.generator.fieldxsd;

import java.io.File;
import java.util.ArrayList;
import com.sun.mdm.index.project.generator.TemplateWriter;
import com.sun.mdm.index.project.generator.exception.TemplateWriterException;
import com.sun.mdm.index.project.generator.exception.TemplateFileNotFoundException;
import com.sun.mdm.index.parser.EIndexObject;
import com.sun.mdm.index.parser.NodeDef;
import com.sun.mdm.index.parser.ParserException;
import com.sun.mdm.index.parser.RelationDef;
import com.sun.mdm.index.parser.Utils;



/**
 * @author gzheng
 * @version
 */
 public class FieldXSDWriter {
    private String mPath;
    private TemplateWriter mTW;
    private EIndexObject mEIndexObject;


    /**
     * @param path template path
     * @param eo elephant object
     * @exception TemplateWriterException template writer exception
     */
    public FieldXSDWriter(String path, EIndexObject eo)
        throws TemplateWriterException {
        try {
            // always create tables
            if (true) {
                mTW = new TemplateWriter("com/sun/mdm/index/project/generator/fieldxsd/basefieldname.xsd.tmpl");
            }
        } catch (TemplateFileNotFoundException ex) {
            throw ex;
        }
        mEIndexObject = eo;
        mPath = path ;
    }


    /**
     * @param context context
     * @param rel relation
     * @return ArrayList list of paths
     */
    public ArrayList getPaths(String context, RelationDef rel) {
        ArrayList ret = new ArrayList();

        String name = rel.getName();
        NodeDef node = mEIndexObject.getNode(name);
        String head = "";
        if (!context.equals("")) {
            head = context + ".";
        }

        ret.add(head + name + ".*");
        ArrayList fields = node.getFieldNames();
        for (int i = 0; i < fields.size(); i++) {
            ret.add(head + name + "." + (String) fields.get(i));
        }

        ArrayList childList = rel.getChildren();
        if (null != childList) {
            for (int i = 0; i < childList.size(); i++) {
                String childName = (String) childList.get(i);
                ret.add(head + name + "." + childName + ".*");
                NodeDef n = mEIndexObject.getNode((String) childList.get(i));
                ArrayList f = n.getFieldNames();
                for (int j = 0; j < f.size(); j++) {
                    ret.add(head 
                            + name 
                            + "." 
                            + childName 
                            + "." 
                            + (String) f.get(j));
                }
            }
        }

        ArrayList relList = rel.getRelations();
        if (null != relList) {
            for (int i = 0; i < relList.size(); i++) {
                RelationDef r = (RelationDef) relList.get(i);
                ret.addAll(getPaths(head + name, r));
            }
        }

        return ret;
    }


    /**
     * @exception ParserException parser exception
     */
    public void write()
        throws ParserException {
        try {
            String res = "";
            ArrayList cons = mTW.construct();
            RelationDef relDef = null;
            ArrayList relList = mEIndexObject.getRelationships();
            if (null != relList) {
                relDef = (RelationDef) relList.get(0);
            } else {
                throw new ParserException("No relationship defined");
            }

            ArrayList values = new ArrayList();
            values.add(getPaths("", relDef));
            res += mTW.writeConstruct((String) cons.get(0), values);

            Utils.writeFile(mPath + File.separator + "basefieldname.xsd", res);
        } catch (TemplateWriterException e) {
            throw new ParserException(e.getMessage());
        } catch (ParserException e) {
            throw e;
        }
    }
}
