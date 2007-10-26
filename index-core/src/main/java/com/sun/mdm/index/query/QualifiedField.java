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
package com.sun.mdm.index.query;


/**
 * This class splits a fully qualified name into Object name and field name.
 * So a fqfn Enterprise.SystemSBR.Person.firstName is split into
 * object: Enterprise.SystemSBR.Person
 * field: firstName
 * @author sdua
 */
class QualifiedField {
    
    private QualifiedObject mqfObject = null;
    String mfield;
    String mobject;


    /**
     * Creates a new instance of QualifiedField
     *
     * @param field fieldname
     */
     QualifiedField(String field) {
        parse(field);
    }


    String getField() {
        return mfield;
    }


    String getObject() {
        return mobject;
    }


    QualifiedObject getQualifiedObject() {
        if (mqfObject == null) {
            mqfObject = new QualifiedObject(mobject);
        }

        return mqfObject;
    }


    private void parse(String field) {
        int lastindex = field.lastIndexOf('.');
        mobject = field.substring(0, lastindex);
        mfield = field.substring(lastindex + 1);
    }
    
    public String toString() {
       return mobject +":" + mfield;	
    	
    }
}
