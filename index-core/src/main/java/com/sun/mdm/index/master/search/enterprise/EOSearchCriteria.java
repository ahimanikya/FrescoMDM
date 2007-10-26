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

import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.querybuilder.SearchCriteria;


/**
 * The <b>EOSearchCriteria</b> class contains the search criteria for
 * a <b>searchEnterpiseObject</b> object. The criteria is an object
 * of the class <b>SystemObject</b>.
 */
public class EOSearchCriteria extends SearchCriteria {

    /** Search object
     */
    private SystemObject mSystemObject;
    private SystemObject mSystemObject2;
    private SystemObject mSystemObject3;
    

    /**
     * Creates a new instance of the EOSearchCriteria class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @include
     */
    public EOSearchCriteria() {
    }


    /**
     * Creates a new instance of the EOSearchCriteria class.
     * <p>
     * @param sysObj A system object containing the search criteria to
     * be used by the searchEnterpriseObject class.
     * @include
     */
    public EOSearchCriteria(SystemObject sysObj) {
        mSystemObject = sysObj;
    }


    /**
     * See setSystemObject
     * @return system object
     */
    public SystemObject getSystemObject() {
        return mSystemObject;
    }

    /**
     * See setSystemObject2
     * @return system object
     */
    public SystemObject getSystemObject2() {
        return mSystemObject2;
    }

    /**
     * See setSystemObject3
     * @return system object
     */
    public SystemObject getSystemObject3() {
        return mSystemObject3;
    }    
    
    /**
     * SystemObject is transformed into query object using query builder when
     * searchEnterpriseObject is invoked.
     *
     * @param obj system object
     */
    public void setSystemObject(SystemObject obj) {
        mSystemObject = obj;
    }

    /**
     * SystemObject2 is optional and is used to convey additional information
     * to the query builder such as the lower end of a range search.
     *
     * @param obj system object
     */
    public void setSystemObject2(SystemObject obj) {
        mSystemObject2 = obj;
    }   
    
    /**
     * SystemObject3 is optional and is used to convey additional information
     * to the query builder such as the upper end of a range search.
     *
     * @param obj system object
     */
    public void setSystemObject3(SystemObject obj) {
        mSystemObject3 = obj;
    }       
    
    /** String representation
     * @return string representation
     */
    public String toString() {
        String ret;
        if (mSystemObject2 != null || mSystemObject2 != null) {
            ret = "*** SystemObject1 *** \n" + mSystemObject +
            "\n*** SystemObject2 *** \n" + mSystemObject2 +
            "\n*** SystemObject3 *** \n" + mSystemObject3;
        } else {
            ret = mSystemObject.toString();
        }
        return ret;
    }
}
