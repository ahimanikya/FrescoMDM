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
import java.sql.ResultSet;

/**
 * @author jwu
 */
abstract public class BlobHelper {
    
    /** Creates a new instance of BlobHelper */
    public BlobHelper() {
    }
    
    /**
     * Get the Blob Helper for the specified database type
     * @param dbType database type (only Oracle and SQL Server are supported)
     * @return BlobHelper
     */
    public static BlobHelper getBlobHelper(String dbType) {
        BlobHelper helper = null;
        if (dbType.equalsIgnoreCase("oracle")) {
            helper = new OracleBlobHelper();
        } else if (dbType.equalsIgnoreCase("sql server")) {
            // support Oracle only
            helper = new SQLServerBlobHelper();
        } else {
            // default to Oracle 
            helper = new OracleBlobHelper();
        }
        return helper;
    }
    
    /** Gets the blob value.
     *
     * @param rs  Result set that includes the blob value.
     * @param column  Name of the blob column.
     * @throws OPSException if an error occurs.
     * @return value of the blob.
     */
    abstract Object getValue(ResultSet rs, String column) throws OPSException;
    
    /** Set the blob parameter.  
     *
     * @param rs  ResultSet.
     * @param column  Name of the blob column.
     * @param value  Value of the blob.
     * @throws  OPSException if error occurs.
     */
    abstract void setParamBlob(ResultSet rs, String column, Object value) 
            throws OPSException;
    
    /** Set the blob parameter.
     *
     * @param bhp  Parameters for the blob helper.
     * @param value  Value of blob.
     * @throws  OPSException if error occurs.
     */
    abstract void setParamBlob(BlobHelperParameters bhp, Object value) 
            throws OPSException;
}
