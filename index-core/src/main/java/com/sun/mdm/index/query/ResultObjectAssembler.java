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
  * The <B>ResultObjectAssembler</B> interface contains the methods used to assemble
  * the JDBC objects that result from a query. The relationship of these objects is
  * defined in the ValueMetaNode class.The methods in this interface are called by
  * the AssemblerEngine class (the QueryManager internal engine) as it iterates through
  * the JDBC result sets. During the iteration, when a new root object is found,
  * the assembler engine calls createRoot(). When objects other than root are found,
  * the assembler engine calls createObjectAttributes(). This interface is a handler
  * and a factory that creates composite value objects using the object name and
  * specified attributes.
  *
  * @author sdua
  */
public interface ResultObjectAssembler  {



   /**
     * Initializes the resources to be used by ResultObjectAssembler. Call this
     * method only once before starting the assembly process.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DL><DT><B>Returns:</B><DD> <CODE>void</CODE> - None.</DL>
     * <DL><DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    void init();


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
        throws VOAException;


    /**
     * Creates an object and binds its attributes to the object. This method may
     * create a new object, and sets the attribute values for the new object to those
     * in the input AttributesData object. createObjectAttributes also binds the new
     * attributes object to the input parent object.
     *<P>
     * For example, if Parent.FirstName and Parent.Address.City are retrieved in one
     * SQL query and three rows are retrieved (for example, there are three address
     * cities to retrieve), the AssemblerEngine class (the Query Manager's internal
     * assembling engine) invokes createRoot for the SBR and passes the attributes of
     * the SBR retrieved in the rows. Then the assembler engine calls createObjectAttributes
     * and passes the object name "Person" and the attributes of the Person object as
     * parameters. This method creates the Person object, binds the Person attributes, and
     * returns the Person object. For the Person object, createRoot and createObjectAttributes
     * are only called once.
     * <P>
     * The Assembler Engine then calls createObjectAttributes three times, once for each
     * row of Address information retrieved. The Address objects are then bound to the
     * parent object created previously (in this case, the Person object).
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
        throws VOAException;
}
