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
package com.sun.mdm.index.outbound;

/**
 * Outbound event names
 * @author  sdua
 */
public class OutBoundMessages {
    /** ADD */
    public static final  String ADD = "ADD";
     /** UPD */
    public static final String UPD = "UPD";
     /** MERGE */
    public static final String MRG = "MRG";
     /** UNMRG */
    public static final String UNMRG = "UNMRG";
     /** DEA */
    public static final String DEA = "DEA";
     /** REA */
    public static final String REA = "REA";
    /** UNDO ASSUMED MATCH */
    public static final String UAM = "UAM";
      
    /** Creates a new instance of OutBoundMessages */
    private OutBoundMessages() {
    }
    
    
}
