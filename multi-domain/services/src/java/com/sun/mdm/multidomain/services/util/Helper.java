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
package com.sun.mdm.multidomain.services.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
        
import com.sun.mdm.multidomain.services.core.ConfigException;
import com.sun.mdm.multidomain.services.configuration.SearchResultsConfig;
import com.sun.mdm.multidomain.services.configuration.FieldConfig;
import com.sun.mdm.multidomain.services.configuration.FieldConfigGroup;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathException;

/**
 * Helper class.
 * @author cye
 */
public class Helper {
    
    public static EPathArrayList toSearchResultsFieldEPaths(List<SearchResultsConfig> searchResultsConfigs) 
        throws ConfigException {
        EPathArrayList searchResultsFieldEPaths = new EPathArrayList();
        try {
            for (SearchResultsConfig searchResultsConfig : searchResultsConfigs) {
                List<EPath> ePaths = searchResultsConfig.getEPaths();
                for (EPath ePath : ePaths) {
                    searchResultsFieldEPaths.add(ePath.toString());
                }
            }
        } catch(Exception ex) {
            throw new ConfigException(ex);
        }
       return searchResultsFieldEPaths;
    }
    
    public static List<FieldConfig> toSearchResultsFieldConfigs(List<SearchResultsConfig> searchResultsConfigs)
        throws ConfigException {
        List<FieldConfig> searchFieldConfigs = new ArrayList<FieldConfig>();
        for (SearchResultsConfig searchResultsConfig : searchResultsConfigs) {
            List<FieldConfigGroup> fieldConfigGroups = searchResultsConfig.getFieldGroupConfigs();
            for (FieldConfigGroup fieldConfigGorup : fieldConfigGroups) {
                List<FieldConfig> fieldConfigs = fieldConfigGorup.getFieldConfigs();
                searchFieldConfigs.addAll(fieldConfigs);
            }           
        }
        return searchFieldConfigs;
    }
    
    public static EPathArrayList toEPathArrayList(List<FieldConfig> fieldConfigs)
        throws ConfigException {  
        EPathArrayList ePaths = new EPathArrayList();
        try {
            for (FieldConfig fieldConfig : fieldConfigs) {
                ePaths.add(fieldConfig.toEpathStyleString(fieldConfig.getFullName()));
            }
        } catch(EPathException eex) {
            throw new ConfigException(eex);
        }
        return ePaths;   
    }
}
