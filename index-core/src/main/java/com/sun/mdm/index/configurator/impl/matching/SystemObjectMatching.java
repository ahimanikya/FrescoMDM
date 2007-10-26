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
package com.sun.mdm.index.configurator.impl.matching;

import java.util.ArrayList;

/**
 * Represents the configuration for the matching of a system object.
 *
 * @author aegloff
 * @version $Revision: 1.1 $
 */
public class SystemObjectMatching {
    
    /** The configured match fields of the system object  */    
    private ArrayList matchColumns; // ArrayList of MatchColumn instances
    private String qualifiedName;

    /**
     * Creates new SystemObjectMatching instance.
     *
     * @param aMatchColumns An ArrayList of MatchColumn instances representing
     * the configured match fields of the system object.
     * @param aQualifiedName The qualified name of the system object this 
     * configuration relates to.
     */
    public SystemObjectMatching(ArrayList aMatchColumns, String aQualifiedName) {
        matchColumns = aMatchColumns;
        qualifiedName = aQualifiedName;
    }

    /**
     * Getter for MatchColums attribute of the SystemObjectMatching object.
     *
     * @return the match columns (ArrayList of MatchColumn instances)
     */
    public ArrayList getMatchColums() {
        return matchColumns;
    }

    /**
     * Getter for QualifiedName attribute of the SystemObjectMatching object.
     * @return the qualified name of the system object this configuration relates to.
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

}
