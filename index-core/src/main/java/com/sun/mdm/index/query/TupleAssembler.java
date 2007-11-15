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

import com.sun.mdm.index.util.Localizer;

import java.sql.SQLException;
import java.util.ArrayList;


/**
 * The <B>TupleAssembler</B> class assembles tuples from the data retrieved by
 * the Query Manager. Tuples are used by the match engine.
 *
 * @author sdua
 */
public class TupleAssembler implements ResultObjectAssembler {
    
    private transient final Localizer mLocalizer = Localizer.get();
    
    /**
     * Creates a new instance of TupleAssembler
     */
    public TupleAssembler() {
    }


    /**
     * Creates an object containing the attributes for each object (other than the
     * root object) that is returned from the Query Manager iterator (QMIterator class)
     * and that has a new identity. This method may create a new object, and sets
     * the attribute values for the new object to those in the input AttributesData
     * object. createObjectAttributes also binds the new attributes object to the
     * input parent object. For example, if Parent.FirstName and Parent.Address.City
     * are retrieved in a query, createObjectAttributes is called for each Address
     * object retrieved. The Person object is both the root and parent object, and the
     * new attributes object is bound to the Person object.
     * <p>
     * @param rootObject The name of the root object.
     * @param parent The name of the parent object (this may be the same as the root
     * object).
     * @param objectName The name of the object, as defined in the Object Definition
     * configuration file in the eView Project.
     * @param attrsData - The attribute data associated with the object.
     * @return <CODE>Object</CODE> - An object containing the attributes.
     * @exception VOAException Thrown if an error occurs while creating the object
     * attributes.
     * @include
     */
    public Object createObjectAttributes(Object rootObject, Object parent,
            String objectName, AttributesData attrsData)
        throws VOAException {
        return rootObject;
    }


    /**
     * Creates the root object for each object returned from the Query Manager
     * iterator (QMIterator class). Inside createRoot(), you can create initialization
     * data structures that can later be used by the createObjectAttributes method.
     * <p>
     * @param objectName The name of the root object.
     * @param attrsData The attribute data associated with the root object.
     * @return <CODE>Object</CODE> - The root object of the fields in the SQL statement.
     * @exception VOAException Thrown if an error occurs while creating the root object.
     * @include
     */
    public Object createRoot(String objectName, AttributesData attrsData)
        throws VOAException {
        try {
            ArrayList root = new ArrayList();

            for (int i = 0; i < attrsData.size(); i++) {
                Object obj = attrsData.get(i);
                root.add(obj);
            }

            return root;
        } catch (SQLException sqe) {
            throw new VOAException(mLocalizer.t("QUE557: Could not create " +
                                        "the root object: {0}", sqe));
        }
    }


    /**
     * Initializes the resources to be used by TupleAssembler. Call this
     * method only once before starting the assembly process.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DL><DT><B>Returns:</B><DD> <CODE>void</CODE> - None.</DL>
     * <DL><DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public void init() {
    }
}
