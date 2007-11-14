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
package com.sun.mdm.index.ops;

import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.util.Localizer;

import java.io.BufferedInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.InputStream; 
import java.io.OutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jwu
 * @version
 */
public class OracleBlobHelper extends BlobHelper {

    private transient final Localizer mLocalizer = Localizer.get();
    
    /** Creates a new instance of OracleBlobHelper */
    public OracleBlobHelper() {
    }

    /** Gets the blob value.
     *
     * @param rs  Result set that includes the blob value.
     * @param column  Name of the blob column.
     * @throws OPSException if an error occurs.
     * @return value of the blob.
     */
    Object getValue(ResultSet rs, String column) throws OPSException {
        oracle.sql.BLOB blob = null;
        InputStream is = null;
        InputStream bis = null;
        try {
            blob = (oracle.sql.BLOB) rs.getBlob(column.toUpperCase());
            if (blob != null) {
                is = blob.getBinaryStream();
            }
        } catch (SQLException e) {
            throw new OPSException(mLocalizer.t("OPS564: Could not retrieve " + 
                                    "the value of a Blob column: {0}", e));
        }
        ObjectInputStream iso = null;
        Object value = null;
        
        if (is != null) {
            try {
            	bis = new BufferedInputStream(is);
            	bis.mark(2147483647);            	         
            	iso = new ObjectInputstreamForBackwardCompatibility(bis);
            } catch (EOFException e) { // empty InputStream
                value = null;
                iso = null;
            } catch (IOException e) {
                throw new OPSException(mLocalizer.t("OPS565: Could not open " + 
                                    "an ObjectInputstreamForBackwardCompatibility " +
                                    "instance: {0}", e));
            }
        }
        
        if (iso != null) {
            try {
               value = iso.readObject();
            } catch (Exception e) {
            	// ObjectInputStreamForVersion will override 
            	// readClassDescriptor of ObjectInputStream for serialization.
            	try {
                	bis.reset();
                	iso = new ObjectInputStreamForVersion(bis);
                } catch (EOFException e2) { // empty InputStream
                    value = null;
                    iso = null;
                } catch (IOException e3) {
                    throw new OPSException(mLocalizer.t("OPS566: Could not open " + 
                                    "an ObjectInputStreamForVersion " +
                                    "instance: {0}:{1}", e3, e));
                }
                if (iso != null) {
                    try {
                       value = iso.readObject();
                    } catch (Exception e4) {
                        throw new OPSException(mLocalizer.t("OPS567: Could not read " + 
                                    "the object from the input stream: {0}:{1}", e4, e));
                    }
                }
                return value;
            }
        }
        return value;
    }
    
    /** Set the blob parameter.  
     *
     * @param rs  ResultSet.
     * @param column  Name of the blob column.
     * @param value  Value of the blob.
     * @throws  OPSException if error occurs.
     */
    void setParamBlob(ResultSet rs, String column, Object value) 
            throws OPSException {
		
        oracle.sql.BLOB blob = null;
        OutputStream os = null;
        try {
            blob = (oracle.sql.BLOB) rs.getBlob(column.toUpperCase());
            os = blob.getBinaryOutputStream();
        } catch (SQLException e) {
            throw new OPSException(mLocalizer.t("OPS568: Could not set " + 
                                    "the value of a Blob column: {0}", e));
        }
        ObjectOutputStream oso = null;
        try {
            oso = new ObjectOutputStream(os);
            oso.writeObject(value);
            oso.flush();
            oso.close();
    	} catch (IOException e) {
                throw new OPSException(mLocalizer.t("OPS569: Could not set " + 
                                    "the value of a Blob column: {0}", e));
    	}
    }

    /** Set the blob parameter
     *
     * @param bhp  Parameters for the blob helper.
     * @param value  Value of blob
     * @throws  OPSException if error is encountered
     */
    void setParamBlob(BlobHelperParameters bhp, Object value) throws OPSException {
        setParamBlob(bhp.getResultSet(), bhp.getColumnName(), value);
    }
}
