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
package com.sun.mdm.index.ejb.master.helper;

/**
 * Constants for MC test classes
 * @author  dcidon
 */
public interface TestConstants {
    
    /** Set true to use EJB proxy
     */
    public static boolean USE_EJB_PROXY = true;
    
    /**
     * Tables to be reset by clearDB
     */
    public static final String[] TABLES = 
    {
        "SBYN_AUDIT",
        "SBYN_AUXIDSBR",
        "SBYN_AUXID",
        "SBYN_ADDRESSSBR",
        "SBYN_ADDRESS",
        "SBYN_ALIASSBR",
        "SBYN_ALIAS",
        "SBYN_COMMENT",
        "SBYN_COMMENTSBR",
        "SBYN_PHONESBR",
        "SBYN_PHONE",
        "SBYN_PERSONSBR",
        "SBYN_PERSON",
        "SBYN_MERGE",
        "SBYN_AUDIT",
        "SBYN_POTENTIALDUPLICATES",
        "SBYN_ASSUMEDMATCH",        
        "SBYN_TRANSACTION",     
        "SBYN_ENTERPRISE",
        "SBYN_OVERWRITE",
        "SBYN_SYSTEMSBR",
        "SBYN_SYSTEMOBJECT",
        "SBYN_SYSTEMS"
    };
    
    /**
     * Sequence numbers to be reset by clearDB
     */
    public static final String[][] SEQNUM = 
    {
        {"ADDRESS", "0"},
        {"ADDRESSSBR", "0"},
        {"ALIAS", "0"},
        {"ALIASSBR", "0"},
        {"ASSUMEDMATCH", "0"},
        {"AUDIT", "0"},
        {"AUXID", "0"},
        {"AUXIDSBR", "0"},
        {"COMMENT", "0"},
        {"COMMENTSBR", "0"},
        {"EUID", "0000000000"},
        {"MERGE", "0"},
        {"PERSON", "0"},
        {"PERSONSBR", "0"},
        {"PHONE", "0"},
        {"PHONESBR", "0"},
        {"POTENTIALDUPLICATE", "0"},
        {"TRANSACTIONNUMBER", "0"},
    };
    
}
