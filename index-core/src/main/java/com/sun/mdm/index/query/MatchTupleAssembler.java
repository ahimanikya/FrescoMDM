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
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * The <B>MatchTupleAssembler</B> class is used by the match engine to assemble
 * a MatchTuple object for each database tuple. The match tuple consists of a
 * String array of elements that each correspond to a record field and the EUID
 * of the record.
 *
 * @author SeeBeyond
 * @version $Revision: 1.1 $
 */
public class MatchTupleAssembler extends TupleAssembler {

    private transient final Localizer mLocalizer = Localizer.get();
    
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    // Default Date conversion format


    /**
     * Creates a new instance of the MatchTupleAssembler class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DL><DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public MatchTupleAssembler() {
    }


    /**
     * Sets the format for converting java.util.Date fields into Strings
     * in order to convert the data to the String array tuple. The default
     * format is yyyyMMdd.
     * <p>
     * @param aFormat The new date format to use for conversion.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setDateConversionFormat(DateFormat aFormat) {
        dateFormat = aFormat;
    }


    /**
     * Creates the root object for each MatchTuple object.
     * <p>
     * @param objectName The name of the root object.
     * @param attrsData The attribute data associated with the root object.
     * @return <CODE>Object</CODE> - The root object of the fields in the
     * MatchTuple object.
     * @exception VOAException Thrown if an error occurs while creating the
     * root object.
     * @include
     */
    public Object createRoot(String objectName, AttributesData attrsData)
        throws VOAException {
        try {
            String[] data = null;
            MatchTuple root;
            String euid = null;

            int len = attrsData.size();
            // the number of attributes in the resultset
            int outDataLen = (len > 0) ? (len - 1) : 0;
            // the number of fields to return (euid is returned separately)

            data = new String[outDataLen];

            if (len > 0) {
                euid = (String) attrsData.get(0);
            }

            for (int i = 1; i < len; i++) {
                Object obj = attrsData.get(i);
                String converted = null;

                if (obj != null) {
                    // Format dates in the desired format
                    // Other types are formatted according to their toString format
                    if (obj instanceof java.util.Date) {
                        converted = dateFormat.format((java.util.Date) obj);
                    } else {
                        converted = obj.toString();
                    }
                }

                data[i - 1] = converted;
            }

            if (euid == null) {
                throw new VOAException(mLocalizer.t("QUE511: EUID cannot be null."));
            }

            if (outDataLen == 0) {
                throw new VOAException(mLocalizer.t("QUE512: There are no fields to assemble."));
            }

            root = new MatchTuple(euid, data);

            return root;
        } catch (SQLException sqe) {
            throw new VOAException(mLocalizer.t("QUE513: Could not create root: {0}", sqe));
        }
    }
}
