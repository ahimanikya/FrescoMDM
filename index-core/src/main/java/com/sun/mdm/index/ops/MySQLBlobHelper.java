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
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Blob;


/**
 * @version
 */
public class MySQLBlobHelper extends BlobHelper {

    private transient final Localizer mLocalizer = Localizer.get();
    
    /** Creates a new instance of SQLBlobHelper */
    public MySQLBlobHelper() {
    }

    /** Gets the blob value.
     *
     * @param rs  Result set that includes the blob value.
     * @param column  Name of the blob column.
     * @throws OPSException if an error occurs.
     * @return value of the blob.
     */
    Object getValue(ResultSet rs, String column) throws OPSException {
        Blob blob = null;
        InputStream is = null;
        InputStream bis = null;
        try {
            blob = (Blob) rs.getBlob(column.toUpperCase());
            if (blob != null) {
                is = blob.getBinaryStream();
            }
        } catch (SQLException e) {
            throw new OPSException(mLocalizer.t("OPS710: Could not retrieve " + 
                                    "the value of a Blob column: {0}", e));
        }
        ObjectInputStream iso = null;
        Object value = null;
        
        if (is != null) {
            try {
            	bis = new BufferedInputStream(is);
            	bis.mark(2147483647);            	         
            	iso = new ObjectInputStream(bis);
            } catch (EOFException e) { // empty InputStream
                value = null;
                iso = null;
            } catch (IOException e) {
                throw new OPSException(mLocalizer.t("OPS711: Could not open " + 
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
                } catch (EOFException e2) {
                    // empty InputStream
                    value = null;
                    iso = null;
                } catch (IOException e3) {
                    throw new OPSException(mLocalizer.t("OPS712: Could not open " + 
                                    "an ObjectInputStreamForVersion " +
                                    "instance: {0}: {1}", e3, e));
                }
                if (iso != null) {
                    try {
                       value = iso.readObject();
                    } catch (Exception e4) {
                        throw new OPSException(mLocalizer.t("OPS713: Could not read " + 
                                    "the object from the input stream: {0}: {1}", e4, e));
                    }
                }
                return value;
            }
        }
        return value;
    }
    
    /** Set the blob parameter.  This method is not supported by SQL Server.
     *
     * @param rs  ResultSet
     * @param column  column name
     * @param value  value of the blob
     * @throws  OPSException if error is encountered
     */
    void setParamBlob(ResultSet rs, String column, Object value) throws OPSException {
        throw new OPSException(mLocalizer.t("OPS714: void setParamBlob(ResultSet rs, " +
                               "String column, Object value) not supported " +
                               "for SQL Server."));
    }

    /** Set the blob parameter.
     *
     * @param bhp  Parameters for the blob helper.
     * @param value  Value of blob
     * @throws  OPSException if error is encountered.
     */
    void setParamBlob(BlobHelperParameters bhp, Object value) throws OPSException {

        PreparedStatement ps = bhp.getPreparedStatement();
        int blobColumnIndex = bhp.getColumnIndex();

        try {
            // Serialize to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
            ObjectOutput out = new ObjectOutputStream(bos) ;
            out.writeObject(value);
            out.close();
            // Get the bytes of the serialized object
            byte[] buf = bos.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(buf);
            ps.setBinaryStream(blobColumnIndex, bis, bis.available());
        
        } catch (Exception e) {
            throw new OPSException(mLocalizer.t("OPS715: Could not set " + 
                                    "the value of a Blob column: {0}", e));
        }
    }
}
