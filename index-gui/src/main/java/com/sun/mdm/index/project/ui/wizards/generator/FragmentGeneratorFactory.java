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

/**
 * Factory to get the relevant fragment generator
 */
public class FragmentGeneratorFactory {
    private static java.util.HashMap defaultGenerators = new java.util.HashMap();

    static {
        defaultGenerators.put(QueryWriter.FRAGMENT_TYPE_BLOCK,
            QueryBlockWriter.class.getName());
        defaultGenerators.put(UpdateWriter.FRAGMENT_TYPE_CANDIDATE_FIELD,
            UpdateCandidateFieldWriter.class.getName());

    }

    /** Creates new FragmentGeneratorFactory */
    public FragmentGeneratorFactory() {
    }

    /**
     * Get the relevant fragment generator for a given match type and fragment type
     * @param matchType the match type to get the generator for
     * @param fragmentType the type of fragment to generate
     * @return the fragment generator instance or null if no generator is configured for the given match type
     * @throws ConfigGeneratorException if retrieving the fragment type generator failed
     */
    public static GenInterface getGenInstance(MatchType matchType,
        String fragmentType) throws ConfigGeneratorException {
        GenInterface instance = null;
        String implName = null;

        if (matchType != null) {
            implName = matchType.getGenerator(fragmentType);
        } else {
            implName = (String) defaultGenerators.get(fragmentType);
        }

        if (implName != null) {
            try {
                Class implClass = FragmentGeneratorFactory.class.forName(implName);
                Object impl = implClass.newInstance();

                if (impl instanceof GenInterface) {
                    instance = (GenInterface) impl;
                } else {
                    throw new ConfigGeneratorException(
                        "The configured generator class " + implName +
                        " does not implement the required interface " +
                        GenInterface.class.getName());
                }
            } catch (Exception ex) {
                throw new ConfigGeneratorException(
                    "Could not instantiate the configured generator class " +
                    implName, ex);
            }
        }

        return instance;
    }
}
