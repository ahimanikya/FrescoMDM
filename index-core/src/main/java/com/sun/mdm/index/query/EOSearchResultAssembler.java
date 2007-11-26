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

import com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.util.Localizer;

import java.sql.SQLException;


/**
 * The <B>EOSearchResultAssembler</B> class creates the results data (that is,
 * EOSearchResultRecord objects) used by the Enterprise Data Manager to display
 * the results of a search. The search is executed from a call to
 * MasterController.SearchEnterpriseObject.
 *
 * @author  sdua
 */
public class EOSearchResultAssembler implements ResultObjectAssembler {

    private transient final Localizer mLocalizer = Localizer.get();

       private final ObjectNodeAssembler assembler;

    /**
     * Creates a new instance of the EOSearchResultAssembler class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @exception VOAException Thrown if an error occurs while creating
     * the object.
     * @include
     */
    public EOSearchResultAssembler() throws VOAException {
        assembler = new ObjectNodeAssembler();
    }

    /**
     * Creates an object containing the attributes for each object (other than the
     * root object) that is returned from the Query Manager iterator (QMIterator class)
     * and that has a new identity. This method may create a new object, and sets
     * the attribute values for the new object to those in the input AttributesData
     * object. createObjectAttributes also binds the new attributes object to the
     * input parent object. For example, if Parent.FirstName and Parent.Address.City
     * are retrieved in a query, createObjectAttributes is called for each Address
     * object retrieved. The parent object is the Person object, to which the new
     * attributes object is bound.
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
    public Object createObjectAttributes(Object rootObject, Object parent, String objectName,
    AttributesData attrsData) throws VOAException {
        Object resultRow = ((EOSearchResultRecord) rootObject).getObject();
        if (rootObject == parent) {
            parent = resultRow;
        }
        return assembler.createObjectAttributes(resultRow, parent, objectName, attrsData);
    }

    /**
     * Creates the root object for each object returned from the Query Manager
     * iterator (QMIterator class). Inside createRoot(), you can create initialization
     * data structures that can later be used by the createObjectAttributes method.
     * <p>
     * @param objectName The name of the root object.
     * @param attrsData The attribute data associated with the root object.
     * @return <CODE>Object</CODE> - The root object of the fields in the SQL statement.
     * @exception VOAException Thrown if an error occurs while creating the root
     * object.
     * @include
     */
    public Object createRoot(String objectName, AttributesData attrsData) throws VOAException {
        ObjectNode resultRow = (ObjectNode) assembler.createRoot(objectName, attrsData);
        String euid = null;
        try {
            euid = (String) attrsData.get("EUID");
            if (euid == null || euid.equals("")) {
                throw new VOAException(mLocalizer.t("QUE509: EUID must be selected."));
            }
        } catch (SQLException e) {
            throw new VOAException(mLocalizer.t("QUE510: Could not create root: {0}", e));
        }
        EOSearchResultRecord resultRecord = new EOSearchResultRecord();
        resultRecord.setEUID(euid);
        resultRecord.setObject(resultRow);
        return resultRecord;
    }

    /**
     * Retrieves the value meta node, which describes an object structure, for the
     * given object path.
     * <p>
     * @param fullObjPath The fully qualified path to the object. Fully qualified path
     * names have "Enterprise" as the root; for example,
     * "Enterprise.SystemSBR.Person.Address.City".
     * @return <CODE>ValueMetaNode</CODE> - An object describing the structure of the
     * assembled object.
     * @exception VOAException Thrown if an error occurs while creating the value meta
     * node.
     * @exception QMException Thrown if an error occurs in the Query Manager.
     * @include
     */
    public ValueMetaNode getValueMetaNode(String fullObjPath)
    throws QMException, VOAException {
        ValueMetaNode valueMetaNode = new ObjectNodeMetaNode(fullObjPath);
        return valueMetaNode;
    }

    /**
     * Initializes the resources to be used by EOSearchResultAssembler. Call this method only
     * once before starting the assembly process.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public void init() {
        assembler.init();
    }

}
