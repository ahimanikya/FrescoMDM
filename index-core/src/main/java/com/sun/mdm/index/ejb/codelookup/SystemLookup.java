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
package com.sun.mdm.index.ejb.codelookup;

import java.util.List;
import com.sun.mdm.index.master.SystemDefinition;
import com.sun.mdm.index.codelookup.CodeLookupException;

/**
 * Interface for SystemLookupEJB
 */

public interface SystemLookup {

    /** Get all system definitions as an array
     * @return Array of SystemDefiniton objects
     * @throws CodeLookupException an error occured
     */    
    SystemDefinition[] getSystemDefinitions() throws CodeLookupException;

    /** Get all system definitions as a List
     * @return List of SystemDefinition objects
     * @throws CodeLookupException an error occured
     */    
    List<SystemDefinition> getSystemDefinitionList() throws CodeLookupException;
    
    /** Get system definition for specified system code
     * @param systemCode System code
     * @return SystemDefinition for specified system code, or null no such system code
     * @throws CodeLookupException an error occured
     */    
    SystemDefinition getSystemDefinition(String systemCode) throws CodeLookupException;
    
	/** Check validity of a system code
	 * @param systemCode System code to be checked
	 * @return <b>true</b> if it is a valid system code, <b>false</b> otherwise
	 * @throws CodeLookupException an error occured
	 */
	 boolean hasSystemCode(String systemCode) throws CodeLookupException;
	 
	 /** Reset the system code list.
	  * <p>
	  * This will reload the current set of system code from the database. This should only be used if it is
	  * known or suspected that the system code list has been modified.
	  */
	  void reset();
}
