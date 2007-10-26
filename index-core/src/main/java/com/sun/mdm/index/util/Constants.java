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
package com.sun.mdm.index.util;

/**
 * Global constants for use across the application
 * @author  ckuo
 * @version $Revision: 1.1 $ 
 */
public interface Constants {
   
    /** enables/disables security */
    public static final boolean SECURITY = true;

    /**  Security mask string */
    public static final String SECURITY_MASK_STRING = "XXXXXXXXXX";

    /** enables/disables validation */
    public static final boolean VALIDATION = true;     
    
    /**  disables/enable outbound */
    public static final boolean OUTBOUND = false;
    
    /**  disables/enables Autocommit */
    public static final boolean AUTOCOMMIT = false;
    
    /** Used by UM to indicate merge operations not sent to the database */
    public static final int FLAG_UM_CALC_ONLY = 1;
    
    /** Used by UM to indicate merge operations should be sent to the database */
    public static final int FLAG_UM_NONE = 0;
    
    /** Used by UM to indicate to replace system object values */
    public static final int FLAG_UM_REPLACE_SO = 2;
    
    /** Paging system max elements */
    public static final int DEFAULT_MAX_ELEMENTS = 1000;
    
    /** Paging system default page size */
    public static final int DEFAULT_PAGE_SIZE = 20;
    
    /** Potential duplicate max elements */
    public static final int POT_DUP_MAX_ELEMENTS = 500;
    
    /** SystemCode for the QWS SystemObject */
    public static final String QWS_SYSTEM_CODE = "QWS";

}
