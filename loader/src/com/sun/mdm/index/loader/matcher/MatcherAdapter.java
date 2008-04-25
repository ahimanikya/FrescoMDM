/**
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
package com.sun.mdm.index.loader.matcher;
import java.io.InputStream;
import java.util.HashSet;

import com.sun.mdm.matcher.api.MatchConfigFilesAccess;
import com.sun.mdm.matcher.api.ConfigFilesAccess;
import com.sun.mdm.matcher.api.MatcherConfigurationException;
import com.sun.mdm.matcher.api.MatchingEngine;
//import com.stc.sbmeapi.impl.StandConfigFilesAccessImpl;
import com.sun.mdm.matcher.api.impl.MatchConfigFilesAccessImpl;
import com.sun.mdm.matcher.comparators.MatchComparator;

public class MatcherAdapter {
    
    private  MatchingEngine mSbmeMatch;
    private  String[] matchFieldIDs;

    public MatcherAdapter(String[] matchFldIds, String dateFormat) throws Exception {
     
    	 matchFieldIDs = matchFldIds;	
        //ConfigFilesAccess files = new NewMatcher.CSConfigFileAccess();
        String path = "match/";        
        MatchConfigFilesAccess files = new MatchConfigFilesAccessImpl(path); 
        mSbmeMatch = new MatchingEngine(files);
//        mSbmeMatch.initializeData(files);
        mSbmeMatch.upLoadConfigFile();
//        StandConfigFilesAccessImpl filesAccess = new StandConfigFilesAccessImpl();
        MatchComparator codeClass = mSbmeMatch.getComparatorManager(mSbmeMatch).getComparatorInstance("ds");
        //codeClass.setRTParameters("DateFormat", "yyyyMMdd:HH:mm:ss");
        codeClass.setRTParameters("DateFormat", dateFormat);
    
    }
        
       
    public float compareRecords(String[] rec1, String[] rec2) throws Exception {
        
        String [][] candRecArrayVals = { (rec1) };
        String [][] refRecArrayVals = { (rec2) };
       // return 0;
        
        double[][] score = mSbmeMatch.matchWeight(matchFieldIDs, candRecArrayVals, refRecArrayVals);
        return (float) score[0][0];
        
    }
    
    public float compareRecords(String rec1, String rec2, String matchField) throws Exception {
        
        String [][] candRecArrayVals = { {rec1} };
        String [][] refRecArrayVals = { {rec2} };
        String [] matchFields = {matchField};
     //   return 0;
        
        double[][] score = mSbmeMatch.matchWeight(matchFields, candRecArrayVals, refRecArrayVals);
        return (float) score[0][0];
        
    }
    
   
    
}
