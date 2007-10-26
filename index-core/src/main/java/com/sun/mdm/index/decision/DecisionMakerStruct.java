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
package com.sun.mdm.index.decision;



/**
 * Structure used for return value of DecisionMaker.process() method.
 * @author Dan Cidon
 */
public class DecisionMakerStruct implements Comparable {
    /** Comment
     */    
    public String comment;
    /** EUID
     */    
    public String euid;
    /** weight
     */    
    public float weight;


    /** DecisionMakerStruct constructor
     *
     * @param euid EUID
     * @param weight weight
     * @param comment comment
     */
    public DecisionMakerStruct(String euid, float weight, String comment) {
        this.euid = euid;
        this.weight = weight;
        this.comment = comment;
    }


    /** Compare weights for sorting purposes.
     *
     * @see java.lang.Comparable#compareTo(Object)
     * @param obj object to compare
     * @return see Comparable
     */
    public int compareTo(Object obj) {
        DecisionMakerStruct dm = (DecisionMakerStruct) obj;
        return Float.compare(dm.weight, this.weight);
    }


    /** string representation
     * @return string representation
     */
    public String toString() {
        return "EUID: " + euid + " Weight: " + weight
                + " Comment: " + comment;
    }

}
