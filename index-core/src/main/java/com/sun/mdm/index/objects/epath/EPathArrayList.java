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
package com.sun.mdm.index.objects.epath;

import java.util.ArrayList;


/**
 * The <b>EPathArrayList</b> class represents an array of ePaths, which define
 * the locations of fields in eView objects and the database. For more
 * information about ePaths, see <i>Creating a Master Index with eView
 * Studio</i>.
 * @author Dan Cidon
 */
public class EPathArrayList implements java.io.Serializable {

    private final ArrayList mList;


 /**
     * Creates a new instance of the EPathArrayList class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @include
     */
    public EPathArrayList() {
        mList = new ArrayList();
    }


 /**
     * Creates a new instance of the EPathArrayList class.
     * <p>
     * @param paths An array of ePaths defining the location of the
     * desired fields.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception EPathException Thrown if an error occurs while creating
     * the array list.
     * @include
     */
    public EPathArrayList(String paths[])
        throws EPathException {
        mList = new ArrayList();
        add(paths);
    }


    /**
     * Retrieves the ePath at the specified index in an EPathArrayList
     * object.
     * <p>
     * @param index The index of the ePath to retrieve.
     * @return <CODE>EPath</CODE> - The ePath located at the specified
     * index.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public EPath get(int index) {
        return (EPath) mList.get(index);
    }


    /** Get the generic list
     * @return generic list
     */
    public ArrayList getGenericList() {
        return mList;
    }


    /**
     * Adds the specified list of ePaths to an EPathArrayList object.
     * <p>
     * @param paths A string array of ePaths to add to the
     * array list.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception EPathException Thrown if an error occurs while creating
     * the array list.
     * @include
     */
    public void add(String paths[])
        throws EPathException {
        for (int i = 0; i < paths.length; i++) {
            add(EPathParser.parse(paths[i]));
        }
    }


    /**
     * Adds the specified ePath to an EPathArrayList object.
     * <p>
     * @param path A string containing the ePath to add to the
     * array list.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception EPathException Thrown if an error occurs while adding
     * the ePath to the array list.
     * @include
     */
    public void add(String path)
        throws EPathException {
        mList.add(EPathParser.parse(path));
    }


    /**
     * Adds the specified ePath to an EPathArrayList object.
     * <p>
     * @param path The ePath to add to the array list.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void add(EPath path) {
        mList.add(path);
    }

    /**
     * Adds the specified ePath to an EPathArrayList object if that ePath
     * does not already exist in the object.
     * <p>
     * @param path The ePath to add to the array list.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception EPathException Thrown if an error occurs while adding
     * the ePath to the array list.
     * @include
     */
    public void addUnique(EPath path)
        throws EPathException {
        if (!mList.contains(path)) {
            mList.add(path);
        }
    }

    /**
     * Adds the specified ePath to an EPathArrayList object if that ePath
     * does not already exist in the object.
     * <p>
     * @param path A string containing the ePath to add to the array list.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception EPathException Thrown if an error occurs while adding
     * the ePath to the array list.
     * @include
     */
    public void addUnique(String path)
        throws EPathException {
        addUnique(EPathParser.parse(path));
    }

    /**
     * Adds the specified ePaths to an EPathArrayList object if those
     * ePaths do not already exist in the object.
     * <p>
     * @param paths A string array of ePaths to add to the
     * array list.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception EPathException Thrown if an error occurs while adding
     * the ePaths to the array list.
     * @include
     */
    public void addUnique(String paths[])
        throws EPathException {
        for (int i = 0; i < paths.length; i++) {
            addUnique(EPathParser.parse(paths[i]));
        }
    }

    /** number of elements
     * @return number of elements
     */
    public int size() {
        return mList.size();
    }


    /** Convert to array
     * @return epath array
     */
    public EPath[] toArray() {
        Object obj[] = mList.toArray();
        EPath path[] = new EPath[obj.length];
        for (int i = 0; i < obj.length; i++) {
            path[i] = (EPath) obj[i];
        }
        return path;
    }


    /** Convert to string array
     * @return string array of epaths
     */
    public String[] toStringArray() {
        Object obj[] = mList.toArray();
        String path[] = new String[obj.length];
        for (int i = 0; i < obj.length; i++) {
            path[i] = ((EPath) obj[i]).getName();
        }
        return path;
    }
    
    /** String representation of the class
     * @return string representation
     */
    public String toString() {
        String stringArray[] = toStringArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < stringArray.length; i++) {
            sb.append(stringArray[i]).append(',');
        }
        return sb.toString();
    }
    
}
