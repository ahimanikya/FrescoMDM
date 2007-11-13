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
package com.sun.mdm.index.master;

import java.io.Externalizable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;
import com.sun.mdm.index.util.Localizer;

/**
 * Return value for collaboration executeMatch
 */

public class MatchColResult implements Externalizable {
    public static final int version = 1;

    /**
     * A new enterprise object was created
     */
    public static final int NEW_EO = 1;

    /**
     * The system code / lid was found in the database and the record updated
     */
    public static final int SYS_ID_MATCH = 2;

    /**
     * The system code / lid was not found in the database. A search was
     * performed and an assumed match was found.
     */
    public static final int ASSUMED_MATCH = 3;

    private MatchResult mMatchResult;

    private transient final Localizer mLocalizer = Localizer.get();
    
    /** Creates a new instance of MatchColResult
     */
    public MatchColResult() {
    }

    /** Creates a new instance of MatchColResult
     *
     * @param matchResult MatchResult obtained from MasterController
     */
    public MatchColResult(MatchResult matchResult) {
        mMatchResult = matchResult;
    }


    /** Getter for EUID attribute of the MatchResult object
     * @return EUID
     */
    public String getEUID() {
        return mMatchResult.getEUID();
    }
    

    /** Getter for ResultCode attribute of the MatchResult object. See result
     * code field definitions.
     * @return result code
     */
    public int getResultCode() {
        return mMatchResult.getResultCode();
    }
    
    private void objectWriter(ObjectOutput out, Object obj) throws java.io.IOException {
        if (obj != null) {
            out.writeInt(1);
            out.writeObject(obj);
        } else {
            out.writeInt(0);
        }
     }
        
     private Object objectReader(ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
         int f = in.readInt();
         return (f == 1) ? in.readObject() : null;
     }
    
    private final class ExternalizableVersion1 {
        public ExternalizableVersion1() {};
        
        void writeExternal(ObjectOutput out) throws java.io.IOException {
            objectWriter(out, mMatchResult);
        }
        
        void readExternal(ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
            mMatchResult = (MatchResult) objectReader(in);
        }
    }
    
    public void writeExternal(ObjectOutput out) throws IOException {
        ExternalizableVersion1 ev = new ExternalizableVersion1();
        out.writeInt(version);
        ev.writeExternal(out);
    }
    
    public void readExternal(ObjectInput in)
    throws IOException, java.lang.ClassNotFoundException {
        int version = in.readInt();
        if (version == 1) {
            ExternalizableVersion1 ev = new ExternalizableVersion1();
            ev.readExternal(in);
        } else {
            throw new RuntimeException(mLocalizer.t("MAS500: Unsupported version: {0} ", version));
        }
    }
}
