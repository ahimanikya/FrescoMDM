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
 * Represents the results of a match against a particular object, represented by
 * it's EUID.
 * @author  aegloff
 * @version $Revision: 1.1 $
 */
public class ScoreElement 
        implements Comparable, java.io.Serializable {

    private String euid;
    private double weight;
  
    /**
     * Create a new ScoreElement given a euid and weight
     * @param aEUID the euid
     * @param aWeight the weight calculated for the match of the given euid
     */
    public ScoreElement(String aEUID, double aWeight) {
        this.euid = aEUID;
        this.weight = aWeight;
    }
    
    /**
     * Accessor for EUID
     * @return the euid of the ScoreElement
     */
    public String getEUID() {
        return this.euid;
    }
    
    /**
     * Accessor for EUID
     * @param aEUID sets the euid of the ScoreElement
     */
    public void setEUID(String aEUID) {
        this.euid = aEUID;
    }

    /**
     * Accessor for the score weight
     * @return the score weight of the ScoreElement
     */    
    public double getWeight() {
        return this.weight;
    }

    /**
     * Accessor for EUID
     * @param aWeight sets the score weight of the ScoreElement
     */    
    public void setWeight(double aWeight) {
        this.weight = aWeight;
    }

    /**
     * Defines the natural ordering of ScoreElements for sorting 
     * - ScoreElement with the highest weight first.
     * Can only compare itself against instances of ScoreElement
     * @param o the ScoreElement to compare it to
     * @throws ClassCastException if comparing against an object
     * that can not be cast to ScoreElement
     * @return an int indicating whether the incoming object is 
     * considered lower/higher or the same for ordering
     */
    public int compareTo(Object o) 
            throws ClassCastException {
       // descending order of scores
       if (weight < ((ScoreElement) o).weight) {
           return 1;
       } else if (weight > ((ScoreElement) o).weight) {
           return -1;
       } else {
           return 0;
       }
    }
    
    /**
     * Defines when a ScoreElements is considered equals to another ScoreElement.
     * This defines a ScoreElement to be considered equal to another ScoreElement 
     * instance if they are for the same EUID - 
     * even if they have different weights.
     *
     * In terms of the API documentation this means that this equals implementation 
     * is 'inconsistent' with the above a comparator capable of imposing an 
     * ordering (equals is defined on EUID, sort on weights), making it important
     * to consult the API documentation about the consequences of using this 
     * class in Sets
     *
     * @param o the ScoreElement to compare to
     * @throws ClassCastException if comparing against an object
     * that can not be cast to ScoreElement
     *
     * @return true if the EUID matches
     */
    public boolean equals(Object o) 
            throws ClassCastException {
       // Equals if EUID matches
       return euid.equals(((ScoreElement) o).euid);
    }
    
    /**
     * Calculate the hashcode of this object, based 
     * on the equals definition above, just
     * based on the euid
     *
     * @return the hashcode 
     */
    public int hashCode() { 
        return euid.hashCode(); 
    }

    /**
     * Simple toString for debugging purposes
     * @return a string with state information for debugging purposes
     */
    public String toString() {
        return euid + ": " + weight;
    }
    
}
