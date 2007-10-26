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
package com.sun.mdm.index.survivor;


/**
 * The <b>SystemFieldList</b> class represents a list of system fields. This
 * class is used by the Survivor Strategy Interface when evaluating which
 * field values should populate the Single Best Record (SBR).
 * 
 * @version $Revision: 1.1 $
 */
public class SystemFieldList implements java.io.Serializable {
    java.util.Map mFields;

    /**
     * Creates a new instance of the SystemFieldList class.
	 * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DL><DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public SystemFieldList() {
        mFields = java.util.Collections.synchronizedMap(new java.util.HashMap());
        ;
    }

    /**
     * Adds a SystemField object to the system field list.
     * <p>
     * @param f The system field to add to the field list.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void add(SystemField f) {
        mFields.put(f.getName(), f);
    }

    /**
     * Retrieves the SystemField object for the given field name.
     * <p>
     * @param fieldName The name of the system field to retrieve.
     * @return <CODE>SystemField</CODE> - The system field corresponding to the
     * given field name.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public SystemField get(String fieldName) {
        return (SystemField) mFields.get(fieldName);
    }

    /**
     * Returns an iterator for the list of fields in the SystemFieldList object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.util.Iterator</CODE> - An iterator of the field list.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public java.util.Iterator iterator() {
        java.util.Collection c = mFields.values();

        return c.iterator();
    }

    public String toString() {
        return mFields == null ? null : mFields.toString();
    }
}
