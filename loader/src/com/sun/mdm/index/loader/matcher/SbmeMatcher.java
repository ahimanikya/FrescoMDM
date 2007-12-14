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

import com.stc.sbme.api.SbmeConfigFilesAccess;
import com.stc.sbme.api.SbmeConfigurationException;
import com.stc.sbme.api.SbmeMatchingEngine;
import com.stc.sbmeapi.impl.StandConfigFilesAccessImpl;



public class SbmeMatcher {
    
    private final SbmeMatchingEngine mSbmeMatch;
    private final String[] matchFieldIDs;

    public SbmeMatcher(String[] matchFldIds) throws Exception {
        SbmeConfigFilesAccess files = new SbmeMatcher.CSConfigFileAccess();
        mSbmeMatch = new SbmeMatchingEngine(files);
        mSbmeMatch.initializeData(files);
        mSbmeMatch.upLoadConfigFile("US");
        StandConfigFilesAccessImpl filesAccess = new StandConfigFilesAccessImpl();
        matchFieldIDs = matchFldIds;
    }
    
    /*
    private final String[] matchFieldIDs = {
        "FirstName",
        "LastName",
        "DOB",
        "Gender",
        "SSN",
        "StreetName",
        "HouseNumber"
    };
    */
    
    public static void main(String args[]) {
        try {
        	String[] matchFldIds = {
        	        "FirstName",
        	        "LastName",
        	        "DOB",
        	        "Gender",
        	        "SSN",
        	        "StreetName",
        	        "HouseNumber"
        	    };
            HashSet exclude = new HashSet();
        //    exclude.add("999999999");
            SbmeMatcher matcher = new SbmeMatcher(matchFldIds);

            String[] rec1 = { "ROGER","MCCRIMMON","19961208","M","382146612","FIFTH","2505" };
            String[] rec2 = { "ROGER","MCCRIMMON","19961208","M","382146612","FIFTH","2505" };
            System.out.println(matcher.compareRecords(rec1, rec2));
            String[] rec3 = { "HOWARD","MCCRIMMON","19240327","M","382146612","FIFTH","2505" };
            String[] rec4 = { "HOWARD","MCCRIMMONE","19240327","M","372146612","GLENDALE","1333" };
            System.out.println(matcher.compareRecords(rec3, rec4));
            String[] rec5 = { "MICHAEL","GRANOFF","19240327","M","382146612","SKY MEADOW FARM","7" };
            String[] rec6 = { "MICHAEL","GRANOFF","19240327","M","382146612","SKY MEADOW FARM","7" };
            System.out.println(matcher.compareRecords(rec5, rec6));
            String[] rec7 = { "AAAAAAAAAAAA","AAAAAAAAAAAA","AAAAAAAAAAAA","AAAAAAAAAAAA","AAAAAAAAAAAA","AAAAAAAAAAAA","AAAAAAAAAAAA" };
            String[] rec8 = { "BBBBBBBBBBBB","BBBBBBBBBBBB","BBBBBBBBBBBB","BBBBBBBBBBBB","BBBBBBBBBBBB","BBBBBBBBBBBB","BBBBBBBBBBBB" };
            System.out.println(matcher.compareRecords(rec7, rec8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public float compareRecords(String[] rec1, String[] rec2) throws Exception {
        
        String [][] candRecArrayVals = { (rec1) };
        String [][] refRecArrayVals = { (rec2) };
        
        double[][] score = mSbmeMatch.matchWeight(matchFieldIDs, candRecArrayVals, refRecArrayVals);
        return (float) score[0][0];
    }
    
    public float compareRecords(String rec1, String rec2, String matchField) throws Exception {
        
        String [][] candRecArrayVals = { {rec1} };
        String [][] refRecArrayVals = { {rec2} };
        String [] matchFields = {matchField};
    	//mSbmeMatch.m
        
        double[][] score = mSbmeMatch.matchWeight(matchFields, candRecArrayVals, refRecArrayVals);
        return (float) score[0][0];
    }
    
    public static class CSConfigFileAccess implements SbmeConfigFilesAccess {
        
        public java.io.InputStream getConfigFileAsStream(String name)
        throws SbmeConfigurationException {
            
            InputStream stream = null;
            
            try {
                stream = ClassLoader.getSystemResourceAsStream("match/" + name);
            } catch (RuntimeException ex) {
                System.out.println("Error: " + ex);
                throw new SbmeConfigurationException(ex);
            }
            
            return stream;
        }
        
        public java.io.InputStream getConfigFileAsStream(String name, String name2)
        throws SbmeConfigurationException {
            
            InputStream stream = null;
            
            try {
                stream = ClassLoader.getSystemResourceAsStream("match/" + name);
            } catch (RuntimeException ex) {
                System.out.println("Error: " + ex);
                throw new SbmeConfigurationException(ex);
            }
            
            return stream;
        }
    }
    
}
