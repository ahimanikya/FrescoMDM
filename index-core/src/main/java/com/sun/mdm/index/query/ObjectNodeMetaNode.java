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
import java.util.Iterator;
import com.sun.mdm.index.objects.metadata.MetaDataService;


/**
 * The <B>ObjectNodeMetaNode</B> class describes the structure of object nodes
 * (ObjectNode class). An object node contains information about an object, such as
 * control flags, child and parent tags, a list of child objects, and a list of
 * fields.
 * @author Daniel Cidon
 */
public class ObjectNodeMetaNode extends ValueMetaNode {
    private ArrayList mChildren = new ArrayList();
    private String mName;
    private ObjectNodeMetaNode mParent;


    /**
     * Creates a new instance of the ObjectNodeMetaNode class, using the name of
     * the object node tag to find the object node.
     * <p>
     * @param tag The name of the object node tag.
     * @exception QMException Thrown if an error occurs while creating
     * the meta node.
     * @include
     */
    public ObjectNodeMetaNode(String tag)
        throws QMException {
        mName = tag;

        String[] childTypes = MetaDataService.getChildTypePaths(tag);

        if (childTypes != null) {
            for (int i = 0; i < childTypes.length; i++) {
                mChildren.add(new ObjectNodeMetaNode(childTypes[i], this));
            }
        }
    }

    /**
     * Retrieves the metadata for the child nodes of the given object node.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>ValueMetaNode[]</CODE> - An array of metadata for the child nodes.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public ValueMetaNode[] getChildren() {
        ObjectNodeMetaNode[] retVal = null;
        int size = mChildren.size();

        if (size > 0) {
            retVal = new ObjectNodeMetaNode[size];

            Iterator i = mChildren.iterator();
            int index = 0;

            while (i.hasNext()) {
                retVal[index++] = (ObjectNodeMetaNode) i.next();
            }
        }

        return retVal;
    }


    /**
     * Retrieves the name of the given object node.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The name of the object node.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getName() {
        return mName;
    }


    /**
     * Retrieves the parent node of the given object node.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>ValueMetaNode</CODE> - The parent node.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public ValueMetaNode getParent() {
        return mParent;
    }

    /**
     * Returns a string representation of the given object node.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A string representation of the node.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String toString() {
        return mName;
    }

     private ObjectNodeMetaNode(String tag, ObjectNodeMetaNode parent)
        throws QMException {
        this(tag);
        mParent = parent;
    }


}
