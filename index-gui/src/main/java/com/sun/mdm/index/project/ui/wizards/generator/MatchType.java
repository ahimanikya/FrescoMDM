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
package com.sun.mdm.index.project.ui.wizards.generator;

import java.util.HashMap;


/**
 * The definition for a match type
 */
public class MatchType {
    private String description;
    private String matchTypeID;
    private HashMap generators;
    private String fragmentDir;

    /**
     * Get the description for the match type
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description for the match type
     * @param val the description
     */
    public void setDescription(String val) {
        description = val;
    }

    /**
     * Get the ID for the match type
     * @return the ID
     */
    public String getMatchTypeID() {
        return matchTypeID;
    }

    /**
     * Set the ID for the match type
     * @param val the ID
     */
    public void setMatchTypeID(String val) {
        matchTypeID = val;
    }

    /**
     * Get the generator class name for the given match and fragment type
     * @param fragmentType the fragment type to get the generator class for
     * @return the generator class name
     */
    public String getGenerator(String fragmentType) {
        return (String) generators.get(fragmentType);
    }

    /**
     * Set the generator class names for all the fragment types of the match type
     * @param framentTypeToGenerator the map from fragment type to generator class names
     */
    public void setGenerators(HashMap framentTypeToGenerator) {
        generators = framentTypeToGenerator;
    }

    /**
     * Get the fragment directory for the match type
     * @return the fragment directory
     */
    public String getFragmentDir() {
        return fragmentDir;
    }

    /**
     * Set the fragment directory for the match type
     * @param val the fragment directory
     */
    public void setFragmentDir(String val) {
        fragmentDir = val;
    }
}
