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
 * The <B>AssembleDescriptor</B> class contains information used by the
 * assembler engine (AssemblerEngine class) to construct the resulting composite
 * objects. It describes the result type that comes from the Query Manager, and
 * maps the JDBC results to Java objects. The assemble descriptor can be set in
 * an instance of QueryObject before calling QueryManager. This class can also
 * be used for later assembly by setting the class in QueryResults after the
 * query results object is returned from QueryManager.
 *
 * @author sdua
 */
public class AssembleDescriptor implements java.io.Serializable {
    
    private AssemblerEngine massEngine;
   // private QMIterator miterator;
    private ValueMetaNode mrootNode;
    private ResultObjectAssembler mvalueObjectFactory;


    /**
     * Creates a new instance of the AssembleDescriptor class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DL><DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public AssembleDescriptor() {
    }


    /**
     * Retrieves the assembler engine (class AssemblerEngine) for the assemble
     * descriptor.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>AssemblerEngine</CODE> - The assembler engine for the
     * assemble descriptor.
     * <DT><B>Throws:</B><DD>None.
     *
     */

    AssemblerEngine getAssemblerEngine() {
        return massEngine;
    }


    /**
     * Retrieves the result object assembler (class ResultObjectAssembler)
     * for the assemble descriptor.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>ResultObjectAssembler</CODE> - The result object assembler
     * for the assemble descriptor.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public ResultObjectAssembler getAssembler() {
        return mvalueObjectFactory;
    }


    /**
     * Sets the assembler engine (class AssemblerEngine) for the assemble
     * descriptor.
     * <p>
     * @param assEngine The instance of AssemblerEngine for the given
     * assemble descriptor.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    void setAssemblerEngine(AssemblerEngine assEngine) {
        massEngine = assEngine;
    }


    /**
     *  Sets Iterator
     * @param iterator iterator
     * @deprecated  This method has no effect now
     */
    public void setIterator(QMIterator iterator) {
     //   miterator = iterator;
    }


    /**
     * Sets the structure of the output composite value object tree. This method
     * is optional. If you do not specify the object tree structure, the resulting
     * object tree by default has the same structure as that specified by selectFields
     * in the corresponding instance of the QueryObject class.
     * <p>
     * @param rootNode The composite value object type of the results.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setResultValueObjectType(ValueMetaNode rootNode) {
        mrootNode = rootNode;
    }


    /**
     * Specifies the result object assembler to use. Call this method when
     * specifying the structure of the output composite value object tree.
     * If this method is not used, the Query Manager uses the default assembler
     * (class ResultObjectAssembler).
     * <p>
     * @param valueObjectFactory The result object assembler to use.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setAssembler(ResultObjectAssembler valueObjectFactory) {
        mvalueObjectFactory = valueObjectFactory;
    }


  //  QMIterator getIterator() {
    //    return miterator;
    //}


    /**
     * Retrieves the root node for the assemble descriptor. The root node provides
     * metadata about the structure of the objects assembled by the assembler engine
     * and Query Manager.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>ValueMetaNode</CODE> - Metadata describing the root node structure
     * for the objects.
     * <DT><B>Throws:</B><DD>None.
     *
     */
    ValueMetaNode getRoot() {
        return mrootNode;
    }


    /**
     * Specifies a default structure for the root node of the objects assembled
     * by the assembler engine and Query Manager.
     * <p>
     * @param rootNode Metadata describing the default result type for the
     * objects.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     *
     */

    void setDefaultResultType(ValueMetaNode rootNode) {
        if (mrootNode == null) {
            mrootNode = rootNode;
        }
    }
}
