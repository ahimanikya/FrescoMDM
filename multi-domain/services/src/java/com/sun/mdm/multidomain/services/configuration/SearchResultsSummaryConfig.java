/*
 * BEGIN_HEADER - DO NOT EDIT
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * If applicable add the following below this CDDL HEADER,
 * with the fields enclosed by brackets "[]" replaced with
 * your own identifying information: Portions Copyright
 * [year] [name of copyright owner]
 */

/*
 * @(#)SearchResultsConfig.java
 * Copyright 2004-2008 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.multidomain.services.configuration;

import java.util.Iterator;
import java.util.ArrayList;


/**
 * Search Result configurations
 *
 * @author rtam
 * @created October 20, 2008
 */
public class SearchResultsSummaryConfig extends SearchResultsConfig implements java.io.Serializable {
    
    public SearchResultsSummaryConfig(ObjectNodeConfig rootObj, int searchResultSummaryID, 
                                      int searchResultDetailsID, int pageSize, 
                                      int maxRecords, boolean showEUID, 
                                      boolean showLID, 
                                      ArrayList<FieldConfigGroup> fieldConfigGroups) throws Exception {
                                        
        super(rootObj, searchResultSummaryID, DISABLED, searchResultDetailsID, pageSize, 
              maxRecords, showEUID, showLID, fieldConfigGroups);                    

    }
    
}