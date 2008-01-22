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
 * The <B>MatchTuple</B> class is used by the match engine when a candidate pool
 * of possible matches to a record is returned by the Query Manager. Each record
 * is one instance of the MatchTuple class. This class consists of the EUID of
 * the record and a String array of elements each corresponding to a field in the
 * record (converted to a String). If a field is null in the database, the
 * corresponding array element is also null. The fields included in the String
 * array are the fields defined for matching in the Match Field file in the
 * Master Index Project.
 *
 * @author SeeBeyond
 * @version $Revision: 1.1 $
 */
public class MatchTuple implements java.io.Serializable {
    private String[] data;
    private String euid;


    /**
     * Creates a new instance of the MatchTuple class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DL><DT><B>Throws:</B><DD>None.</DL>
     * @include

     */
    public MatchTuple() {
    }


    /**
     * Creates a new instance of the MatchTuple class.
     * <p>
     * @param meuid The EUID of the record.
     * @param mdata An array of data associated with the record.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public MatchTuple(String meuid, String[] mdata) {
        euid = meuid;
        data = mdata;
    }


    /**
     * Retrieves the tuple data (that is, all the fields of the tuple) for
     * the MatchTuple object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String[]</CODE> - An array of fields associated with the
     * record.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String[] getData() {
        return data;
    }


    /**
     * Retrieves the EUID for the MatchTuple object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The EUID associated with the record.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getEuid() {
        return euid;
    }
}
