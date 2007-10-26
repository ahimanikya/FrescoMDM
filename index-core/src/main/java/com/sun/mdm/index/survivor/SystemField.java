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
 * The <b>SystemField</b> class represents a field in a system object. This
 * class contains the parsed field names, as well as the operations needed to
 * traverse the object graph to access the field. The current version assumes
 * only one secondary object level.
 *
 * @version $Revision: 1.1 $
 */
public class SystemField implements java.io.Serializable, Cloneable {
    /**
     *
     */
    private String mName;

    /**
     *
     */
    private Object mValue;

    /**
     * Creates a new instance of the SystemField class given the field name
     * and field value.
	 * <p>
     * @param name The name of the field.
     * @param value The value of the field.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public SystemField(String name, Object value) {
        mName = name;
        mValue = value;
    }

    /**
     * Determines whether the field name and field value between two fields
     * are equal, and returns <b>true</b> if they are. If the names and values
     * are not equal, this method returns <b>false</b>.
	 * <p>
     * @param f The name of the field to compare against.
     * @return <CODE>boolean</CODE> - An indicator of whether the field names and field values
     * match. <B>True</B> indicates the names and values match in both fields;
     * <B>false</B> indicates they do not match.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public boolean equals(SystemField f) {
        return mName.equals(f.getName()) ? mValue.equals(f.getValue()) : false;
    }


    /**
     * Returns the hash code value for this SystemField object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>int</CODE> - A hash code value.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Retrieves the field name from an instance of SystemField.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The name of the system field.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getName() {
        return mName;
    }

    /**
     * Retrieves the field value from an instance of SystemField.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Object</CODE> - The value of the system field.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Object getValue() {
        return mValue;
    }

    /**
     * Sets the value of the system field in an instance of
     * SystemField.
     * <p>
     * @param o The object containing the value to place in the field.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setValue(Object o) {
        mValue = o;
    }

    /**
     * Returns an exact copy of the SystemField object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Object</CODE> - A copy of the SystemField object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnsx) {
            return null;
        }
    }

    /**
     * Retrieves a string representation of the field name and field value
     * from the SystemField object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The name of the system field and its value.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String toString() {
        return mName + "=" + mValue;
    }
}
