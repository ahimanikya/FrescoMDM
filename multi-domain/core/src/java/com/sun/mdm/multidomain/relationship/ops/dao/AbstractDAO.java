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

package com.sun.mdm.multidomain.relationship.ops.dao;

import java.io.*;
import java.sql.*;

/**
 * Generic Base class for DAO classes.
 *
 * This is a customizable template within FireStorm/DAO.
 */
public abstract class AbstractDAO {

    public byte[] getBlobColumn(ResultSet rs, int columnIndex)
            throws SQLException {
        try {
            Blob blob = rs.getBlob(columnIndex);
            if (blob == null) {
                return null;
            }

            InputStream is = blob.getBinaryStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            if (is == null) {
                return null;
            } else {
                byte buffer[] = new byte[64];
                int c = is.read(buffer);
                while (c > 0) {
                    bos.write(buffer, 0, c);
                    c = is.read(buffer);
                }
                return bos.toByteArray();
            }
        } catch (IOException e) {
            throw new SQLException("Failed to read BLOB column due to IOException: " + e.getMessage());
        }
    }

    public void setBlobColumn(PreparedStatement stmt, int parameterIndex, byte[] value)
            throws SQLException {
        if (value == null) {
            stmt.setNull(parameterIndex, Types.BLOB);
        } else {
            stmt.setBinaryStream(parameterIndex, new ByteArrayInputStream(value), value.length);
        }
    }

    public String getClobColumn(ResultSet rs, int columnIndex)
            throws SQLException {
        try {
            Clob clob = rs.getClob(columnIndex);
            if (clob == null) {
                return null;
            }

            StringBuffer ret = new StringBuffer();
            InputStream is = clob.getAsciiStream();

            if (is == null) {
                return null;
            } else {
                byte buffer[] = new byte[64];
                int c = is.read(buffer);
                while (c > 0) {
                    ret.append(new String(buffer, 0, c));
                    c = is.read(buffer);
                }
                return ret.toString();
            }
        } catch (IOException e) {
            throw new SQLException("Failed to read CLOB column due to IOException: " + e.getMessage());
        }
    }

    public void setClobColumn(PreparedStatement stmt, int parameterIndex, String value)
            throws SQLException {
        if (value == null) {
            stmt.setNull(parameterIndex, Types.CLOB);
        } else {
            stmt.setAsciiStream(parameterIndex, new ByteArrayInputStream(value.getBytes()), value.length());
        }
    }
}
