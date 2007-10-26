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

import java.sql.PreparedStatement;

public class DeltaParameters {
    
    private PreparedStatement mPs = null;   // prepared statement
    private String mColumnName = null;      // name of the delta column
    private int mColumnIndex = -1;          // parameter index of the delta 
                                            // argument

    /**  Creates a new instance of DeltaParameters
     *
     */
    public DeltaParameters() {
    }
    
    /** Setter for the mPs attribute.
     *
     * @param val value to be assigned to the mPs attribute.
     */
    public void setPreparedStatement(PreparedStatement val) {
        mPs = val;
    }
    
    /** Setter for mColumnName attribute.
     *
     * @param val value to be assigned to mColumnName attribute.
     */
    public void setColumnName(String val) {
        mColumnName = val;
    }
    
    /** Setter for mColumnIndex attribute.
     *
     * @param val value to be assigned to mColumnIndex attribute.
     */
    public void setColumnIndex(int val) {
        mColumnIndex = val;
    }
    
    /** Getter for mColumnName attribute.
     *
     * @return value of mColumnName attribute.
     */
    public String getColumnName() {
        return mColumnName;
    }
    
    /** Getter for mColumnIndex attribute.
     *
     * @return value of mColumnIndex attribute.
     */
    public int getColumnIndex() {
        return mColumnIndex;
    }
    
    /** Getter for mPs attribute.
     *
     * @return value of mPs attribute.
     */
    public PreparedStatement getPreparedStatement() {
        return mPs;
    }
}
