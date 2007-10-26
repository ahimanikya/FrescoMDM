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

import java.util.ArrayList;


/**
 * This object is used by AssemlerEngine during Assembling process. This object
 * is created for each object type in ValueMetaNode graph which is not a root
 * and which has more than one child. Say We have Object graph System Person
 * Address Phone In this case, Person is parent of Address and Phone. Person and
 * Address attributes may be retrieved in a different JDBC ResultSet and Phone
 * may be in different ResultSet. So the state of Person object per each
 * instance of root object (System) is saved temporarily in this object. So now
 * the Phone objects in a different JDBC ResultSet can be bound to their parent
 * Person objects.
 *
 * @author sdua
 */
class AssembleObjectState implements Cloneable {

    /*
          not currently used but should be used later for optimization.
      */
    private int mcurrentObjectIndex = 0;

    /*
          stores list of keyValues for each object  in mobjectList.
      */
    private ArrayList mkeyValuesList;
    private String mobjName;

    /*
          stores list of all objects per retrieval of
          a root object. So if a root System1 has persons Person1, Person2
          and System2 has persons Person3, Person4, Person5. Then when root
          is System1, mobjectList  would have Person1, Person2.
          When root is System2, mobjectList would have Person3, Person4, Person5.
      */
    private ArrayList mobjectList;


    /**
     * Creates a new instance of AssembleObjectState
     *
     * @param objectName Name of the object say Person.
     */
    AssembleObjectState(String objectName) {
        mobjName = objectName;
    }


    /**
     * clone this object. The new object does not clone the data, 
       only clones the ObjectName for which this object is temporarily saving data.
     * @return cloned object.
     * @exception CloneNotSupportedException CloneNotSupportedException
     */
    public Object clone()
        throws CloneNotSupportedException {
        AssembleObjectState aobjstate = (AssembleObjectState) super.clone();
        aobjstate.init();

        return aobjstate;
    }


    String getObjName() {
        return mobjName;
    }


    /**
     * returns the Object that matched the input keyValues. This is the parent
     * object that correspond to input keyValues and so after being returned can
     * be bound with the child object.
     *
     * @param keyValues Object[] 
     * @return Object that matches the keyValues.
     */
    Object getObject(Object[] keyValues) {
        boolean equal = true;

        for (int i = 0; i < mkeyValuesList.size(); i++) {
            equal = true;

            Object[] kValues = (Object[]) mkeyValuesList.get(i);

            for (int j = 0; j < keyValues.length; j++) {
                if (!keyValues[j].equals(kValues[j])) {
                    equal = false;

                    break;
                }
            }

            if (equal) {
                return (Object) mobjectList.get(i);
            }
        }

        return null;
    }


    void add(Object object, Object[] keyValues) {
        mobjectList.add(object);
        mkeyValuesList.add(keyValues);
        mcurrentObjectIndex++;
    }


    /*
          This needs to be called after each iteration of root object in
          AssemblerEngine.next()
      */
    void init() {
        mobjectList = new ArrayList();
        mkeyValuesList = new ArrayList();
        mcurrentObjectIndex = 0;
    }
}
