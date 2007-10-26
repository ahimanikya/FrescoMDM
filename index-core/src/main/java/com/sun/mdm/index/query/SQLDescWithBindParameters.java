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

import java.util.List;

/**
 * @author sanjay.sharma
 */
 class SQLDescWithBindParameters implements java.io.Serializable {
    
    private List mfieldPrepareIndex;
    private SQLDescriptor mSqlDescriptor;

    public SQLDescWithBindParameters(SQLDescriptor sqlDescriptor, List fieldPrepareIndex) {
        mSqlDescriptor = sqlDescriptor;
    	mfieldPrepareIndex = fieldPrepareIndex;
    }
    

	/**
	 * @return Returns the mfieldPrepareIndex.
	 */
	public List getFieldPrepareIndex() {
		return mfieldPrepareIndex;
	}
	/**
	 * @param mfieldPrepareIndex The mfieldPrepareIndex to set.
	 */
	public void setFieldPrepareIndex(List fieldPrepareIndex) {
		this.mfieldPrepareIndex = fieldPrepareIndex;
	}
	/**
	 * @return Returns the sqlDescriptor.
	 */
	public SQLDescriptor getSqlDescriptor() {
		return mSqlDescriptor;
	}
	/**
	 * @param sqlDescriptor The sqlDescriptor to set.
	 */
	public void setSqlDescriptor(SQLDescriptor sqlDescriptor) {
		this.mSqlDescriptor = sqlDescriptor;
	}
}
