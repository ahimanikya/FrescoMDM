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
 * The <B>ValueMetaNode</B> class describes the structure of the composite value objects
 * assembled by the Query Manager and the assembler engine. This structure is used by
 * the assembler engine to create the objects at the appropriate time. For example, if
 * the desired object structure contains a parent object named "Person" and two child
 * objects, one named "Address" and one named "Phone", the root ValueMetaNode object is
 * Person. The children of the root are Address and Phone, and the children of Address
 * and Phone are null. This class contains metadata and only describes the structure. It
 * does not contain any instance data, such as the attributes of the parent and child nodes.
 *
 * @author sdua
 */
public abstract class ValueMetaNode {
    
    /**
     * Retrieves the method name to be used to add the parent node. This method
     * is optional and only needs to be used if the assembler engine is using
     * reflection to create objects. The name of the parent method is defined
     * based on the object structure defined for the Master Index Project.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The parent method name.
     * If no method is found, <B>getAddParentMethod</B> returns null.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getAddParentMethod() {
        return null;
    }


    /**
     * return the attributes for this valueMetaNode. This is optional, to be
     * provided only if AssemblerEngine is using reflection to create objects.
     * @return ValueMetaNodeAttribute[]
     *


     THis is reserved for future so putting in comments.
    public ValueMetaNodeAttribute[] getAttributes() {
        return null;
    }
    */


    /**
     * Retrieves the child nodes in the value metanode.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>ValueMetaNode[]</CODE> - An array of child nodes.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public abstract ValueMetaNode[] getChildren();


    /**
     * Retrieves the class name for the value metanode. This method
     * is optional and only needs to be used if the assembler engine is using
     * reflection to create objects (using the default reflection assembler).
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The class name for the node. If no class
     * name is found, <B>getClassName</B> returns null.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getClassName() {
        return null;
    }


    /**
     * Retrieves the name of the node.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The name of the node.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public abstract String getName();


    /**
     * Retrieves the parent node of the given node.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>ValueMetaNode</CODE> - The parent node of the
     * given node.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public abstract ValueMetaNode getParent();


    /**
     * Retrieves the object name that is mapped to the given object. This method
     * is optional and only needs to be used if the assembler engine is using
     * reflection to create objects or if the object name is different than
     * the source object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The name of the object. If no
     * name is found, this method returns null.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getSourceObject() {
        return null;
    }
}
