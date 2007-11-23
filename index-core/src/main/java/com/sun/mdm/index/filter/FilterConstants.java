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

package com.sun.mdm.index.filter;

/**
  * This interface for managing the constants of the filters.
 */
public interface FilterConstants {
    
    //the constants for the exclusion type.
    String SBR_EXCLUSION_TYPE = "SBR_EXCLUSION_TYPE";
    String BLOCK_EXCLUSION_TYPE = "BLOCK_EXCLUSION_TYPE";
    String MATCH_EXCLUSION_TYPE = "BLOCKING_EXCLUSION_TYPE";
    
     //the constants for the filter config file tags.
    String TAG_FIELD = "field";
    String TAG_NAME = "name";
    String TAG_VALUE = "value";
    String TAG_FILE = "file";
    String TAG_FIELD_VALUES = "field-value";
    String TAG_FILE_NAME = "file-name";
    
     //the constants for the filter config file attribute tags.
    String ATR_SBR = "sbr";
    String ATR_BLOCKING = "blocking";
    String ATR_MATCHING = "matching";
    String ATR_DELIMITER = "delimiter";
    
    
    
}
