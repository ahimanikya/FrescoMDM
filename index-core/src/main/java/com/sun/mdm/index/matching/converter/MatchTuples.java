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
package com.sun.mdm.index.matching.converter;

import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.configurator.impl.matching.MatchColumn;

/**
 * Contains the tuples representing all the configured match fields of a 
 * SystemObject/SBR
 * (i.e. a de-normalized 'flatened' object tree represented as an 
 * array of arrays)
 * Additionaly it contains easily accessible meta-data describing the tuples,
 * i.e. the matchType a column was configured as.
 * @author  aegloff
 * @version $Revision: 1.1 $
 */
public class MatchTuples {

    private String[] columnMatchTypes;
    private String[] columnQualifiedNames;
    private EPath[] columnEPaths;
    private MatchColumn[] matchColumns;
    private String[][] tuples;
    
    /** Creates new MatchTuples */
    public MatchTuples() {
    }

    /**
     * Creates new MatchTuples
     * @param columnMatchTypes match type of each column
     * @param columnQualifiedNames qualified name of each column
     * @param columnEPaths ePath for each each column
     * @param matchColumns match column definition of each column
     * @param tuples all the tuples dimension one is the 'row',
     * dimension 2 is the 'column'
     */
    public MatchTuples(String[] columnMatchTypes, String[] columnQualifiedNames,
                       EPath[] columnEPaths, MatchColumn[] matchColumns, 
                       String[][] tuples) {
        this.columnMatchTypes = columnMatchTypes;
        this.columnQualifiedNames = columnQualifiedNames;
        this.columnEPaths = columnEPaths;
        this.tuples = tuples;        
    }

    
    /**
     * The match types of all the columns
     * The position of the matchType in the returned array corresponds to the
     * postion of the column in the tuples, i.e. columnTypes[0] 
     * describes tuples[row][0]
     * @return The match types of all the columns
     */
    public String[] getColumnMatchTypes() {
        return columnMatchTypes;
    }

    /**
     * The fully qualified names of all the columns
     * The position of the qualified name in the returned array corresponds to 
     * the position of the column in the tuples, i.e. qualifiedNames[0] 
     * describes tuples[row][0]
     * @return the fully qualified names of all the columns
     */    
    public String[] getColumnQualifiedNames() {
        return columnQualifiedNames;
    }
    
    /**
     * The parsed ePaths of all the columns
     * The position of the ePath in the returned array corresponds to the
     * postion of the column in the tuples, i.e. ePaths[0] 
     * describes tuples[row][0]
     * @return the parsed ePaths of all the columns
     */
    public EPath[] columnEPaths() {
        return columnEPaths;
    }

    /**
     * The match column configuration  of all the columns
     * The position of the match column in the returned array corresponds to the
     * postion of the column in the tuples, i.e. matchColumns[0] 
     * describes tuples[row][0]
     * @return the match column configuration of all the columns
     */    
    public MatchColumn[] getMatchColumns() {
        return matchColumns;
    }

    /**
     * The match tuples data, dimension 1 is the rows, dimension 2 is 
     * the columns: tuples[row][column]
     * The array of arrays returned is 'rectangular', i.e. all contained arrays
     * have the same number of elements.
     * @return the match tuples data
     */    
    public String[][] getTuples() {
        return tuples;
    }    
}
