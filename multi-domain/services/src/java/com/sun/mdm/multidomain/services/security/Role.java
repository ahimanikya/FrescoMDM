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
 * @(#)Role.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
/*
 * Role.java
 *
 * Created on October 27, 2008, 2:30 PM
 */

package com.sun.mdm.multidomain.services.security;

import java.util.ArrayList;

/**
 *
 * @author  rtam
 */
public class Role {
    
    private String mName = null;
    private ArrayList<String> mOperations = null;   // An ArrayList of Operation names.
    
    /** Creates a new instance of Role */
    
    public Role() {
    }
    
    /** Constructor for the Role class.
     *
     * @param name Name of the role.
     * @param operations Valid operations for this role.
     */
    public Role(String name, ArrayList<String> operations) {
        mName = name;
        if (mOperations == null) {
            mOperations = new ArrayList(operations.size());
        }
        mOperations = operations;
    }
    
    /** 
     *
     * Getter for the name member.
     * @returns Value of the name member.
     */
    public String getName() {
        return mName;
    }
    
    /** 
     *
     * Getter for the operations member.
     * @returns Value of the mOperations member.
     */
    public ArrayList getOperations() {
        return mOperations;
    }
    
    /** 
     *
     * Getter for the operations member.
     * @returns Value of the name member.
     */
    public String[] getOperationsArray() {
        String[] operations = new String[0];
        return mOperations.toArray(operations);
    }
    
    /** 
     *
     * Setter for the name member.
     * @param Value to which the mOperations member is set
     */
    public void setOperations(ArrayList operations) {
        mOperations = operations;
    }
}
