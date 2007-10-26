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
package com.sun.mdm.index.master.search.enterprise;
import java.io.Serializable;



/**
 * The <b>EOGetOptions</b> class contains the search criteria for
 * a <b>Partial retrieval of Enterprise</b> object. The criteria is set of Epaths through
 * which you specify field(s)/objects to retrieve and any filter conditions on them 
 */
public class EOGetOptions implements Serializable {
    
    // EPaths to use for partial gets
    private String[] mEpaths;

    /**
     * specify set of EPaths that define SOs/SBRs that are retrieved from master index to compose an EnterpriseObject.
     * The epaths have to be fully qualified paths starting from Enterprise.
     * Epath can be any valid Epath syntax
     * Ex: {"Enterprise.SystemObject.Person.Address[City=Monrovia]".*, "Enterprise.SystemSBR.Person.Phone.*"}
     * This will retreive an EO that contains all SOs containg children Persons and Addresses 
     * whose Address.City=Monrovia. This EO also contains SBR containing objects Person and Phone.
     * Note: The system will only retrieve those Objects that you specify in the list of Epaths.  
     * @param epaths String[] of epaths
     */
    public void setFieldsToRetrieve(String[] epaths) {
        mEpaths = epaths;	
    }

    /**
     * getter for epaths that have been set previously
     * @return set of epaths
     */
    public String[] getFieldsToRetrieve() {
            return mEpaths;
    }
}
