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
package com.sun.mdm.index.matching;

/**
 * The options to control the matching and results for the findWeights methods
 * @author  aegloff
 * @version $Revision: 1.1 $
 */
public class MatchOptions implements java.io.Serializable {

    private boolean sortWeightResults = true;
    private double minimumWeight = Double.NEGATIVE_INFINITY;
    
   /** Creates new MatchOptions */
    public MatchOptions() {
    }
    
    /** 
     * Creates new MatchOptions 
     * @param sortWeightResults whether to sort the match results by weight
     * @param minimumWeight the minimum weight for which to return results
     */
    public MatchOptions(boolean sortWeightResults, double minimumWeight) {
        this.sortWeightResults = sortWeightResults;
        this.minimumWeight = minimumWeight;
    }

    /**
     * Whether to return the result weights in sorted order, from highest to lowest
     * @return true if they results should be sorted
     */    
    public boolean getSortWeightResults() {
        return this.sortWeightResults;
    }
    
    /**
     * Whether to return the result weights in sorted order, from highest to lowest
     * @param sort set to true if they results should be sorted
     */
    public void setSortWeightResults(boolean sort) {
        this.sortWeightResults = sort;
    }

    /**
     * The minimum Weight a result has to have so it gets returned.
     * @return the minimum weight
     */    
    public double getMinimumWeight() {
        return this.minimumWeight;
    }

    /**
     * The minimum Weight a result has to have so it gets returned.
     * @param weight set the minimum weight
     */        
    public void setMinimumWeight(double weight) {
        this.minimumWeight = weight;
    }
}
