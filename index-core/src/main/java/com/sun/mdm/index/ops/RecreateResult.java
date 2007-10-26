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

import com.sun.mdm.index.objects.EnterpriseObject;

import java.io.Serializable;


/**
 * recreate object
 * @author gzheng
 */
public class RecreateResult implements Serializable {
    private EnterpriseObject mAfterEO = null;
    private EnterpriseObject mAfterEO2 = null;
    private EnterpriseObject mBeforeEO1 = null;
    private EnterpriseObject mBeforeEO2 = null;


    /**
     * default constructor
     */
    public RecreateResult() {
    }


    /**
     * Create an instance of recreate object.
     * @param afterEO After enterprise object 1.
     * @param afterEO2 After enterprise object 2.
     * @param beforeEO1 Before enterprise object 1.
     * @param beforeEO2 Before enterprise object 2.
     */
    public RecreateResult(EnterpriseObject afterEO, 
                          EnterpriseObject afterEO2,
                          EnterpriseObject beforeEO1, 
                          EnterpriseObject beforeEO2) {
        mAfterEO = afterEO;
        mAfterEO2 = afterEO2;
        mBeforeEO1 = beforeEO1;
        mBeforeEO2 = beforeEO2;
    }


    /** Sets the mAfterEO attribute.
     *
     * @param eo EnterpriseObject to which mAfterEO is set.
     */
    public void setAfterEO(EnterpriseObject eo) {
        mAfterEO = eo;
    }

    /** Sets the mAfterEO2 attribute.
     *
     * @param eo EnterpriseObject to which mAfterEO2 is set.
     */
    public void setAfterEO2(EnterpriseObject eo) {
        mAfterEO2 = eo;
    }


    /** Sets the mBeforeEO1 attribute.
     *
     * @param eo EnterpriseObject to which mBeforeEO1 is set.
     */
    public void setBeforeEO1(EnterpriseObject eo) {
        mBeforeEO1 = eo;
    }


    /** Sets the mBeforeEO2 attribute.
     *
     * @param eo EnterpriseObject to which mBeforeEO2 is set.
     */
    public void setBeforeEO2(EnterpriseObject eo) {
        mBeforeEO2 = eo;
    }


    /** Getter for the mAfterEO attribute.
     *
     * @return value of the mAfterEO attribute.
     */
    public EnterpriseObject getAfterEO() {
        return mAfterEO;
    }

    /** Getter for the mAfterEO2 attribute.
     *
     * @return value of the mAfterEO2 attribute.
     */
    public EnterpriseObject getAfterEO2() {
        return mAfterEO2;
    }


    /** Getter for the mBeforeEO1 attribute.
     *
     * @return value of the mBeforeEO1 attribute.
     */
    public EnterpriseObject getBeforeEO1() {
        return mBeforeEO1;
    }


    /** Getter for the mBeforeEO2 attribute.
     *
     * @return value of the mBeforeEO2 attribute.
     */
    public EnterpriseObject getBeforeEO2() {
        return mBeforeEO2;
    }
}
