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
package com.sun.mdm.index.project.ui.wizards;

public interface SearchInfo {
    /**
     * The search fields for use in the 'search' web service method
     * @param saAvailableSearchFields string array of all searchable fields
     */
    public void setAvailableSearchFields(String[] saAvailableSearchFields);

    /**
     * The search fields for use in the 'search' web service method
     * @param saDefaultSearchFields string array of all searchable fields
     */
    public void setDefaultSearchFields(String[] saDefaultSearchFields);

    /**
     * The search fields for use in the 'search' web service method
     * @param saAvailableResultFields string array of all searchable fields
     */
    public void setAvailableResultFields(String[] saAvailableResultFields);

    /**
     * The search fields for use in the 'search' web service method
     * @param saDefaultResultFields string array of all searchable fields
     */
    public void setDefaultResultFields(String[] saDefaultResultFields);

    /**
     * The search fields for use in the 'search' web service method
     * @param businessEntity the business entity to get the information for
     * @return All the avalailable search fields
     */
    public String[] getAvailableSearchFields(String businessEntity);

    /**
     * The default search fields for use in the 'search' web service method,
     * defined by the user
     * @param businessEntity the business entity to get the information for
     * @return the default search fields
     */
    public String[] getDefaultSearchFields(String businessEntity);

    /**
     * The available fields to be returned from the 'search' web service method
     * @param businessEntity the business entity to get the information for
     * @return All the available return fields
     */
    public String[] getAvailableResultFields(String businessEntity);

    /**
     * The default fields to be returned from the 'search' web service method,
     * defined by the user
     * @param businessEntity the business entity to get the information for
     * @return the default return fields
     */
    public String[] getDefaultResultFields(String businessEntity);

    /**
     * The search types for use in the 'search' web service method
     * @param businessEntity the business entity to get the information for
     * @return All the avalailable search types
     */
    public String[] getAvailableSearchTypes(String businessEntity);

    /**
     * The default search type for use in the 'search' web service method
     * @param businessEntity the business entity to get the information for
     * @return the default search type
     */
    public String getDefaultSearchType(String businessEntity);
}
