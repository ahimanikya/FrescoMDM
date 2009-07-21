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

import java.util.Map;
import com.sun.mdm.index.codelookup.CodeLookupException;
/**
 * Interface for CodeLookupEJB
 */

public interface CodeLookup {

    /** Get all codes for given application
     * @return map of string code->module->CodeDescription
     * @throws CodeLookupException an error occured
     */    
    Map getAllCodes() throws CodeLookupException;
    
    /** Get all codes for given application and module
     * @param module module
     * @return map of string code->CodeDescription
     * @throws CodeLookupException an error occured
     */    
    Map getCodesByModule(String module) throws CodeLookupException;
    
	/** Check validity of a module
	 * @param module module
	 * @return <b>true</b> if it is a valid module, <b>false</b> otherwise
	 * @throws CodeLookupException an error occured
	 */
	 boolean hasModule(String module) throws CodeLookupException;
    
	/** Check validity of a code
	 * @param module module
	 * @param code code
	 * @return <b>true</b> if it is a valid code in the specified modeule, <b>false</b> otherwise
	 * @throws CodeLookupException an error occured
	 */
	 boolean hasCode(String module, String code) throws CodeLookupException;
    
	/** Get code description
	 * @param module module
	 * @param code code
	 * @return code description if this is a valid code, <b>null</b> otherwise
	 * @throws CodeLookupException an error occured
	 */
	 String getDescription(String module, String code) throws CodeLookupException;
	 
	 /** Reset the code list.
	  * <p>
	  * This will reload the current set of codes from the database. This should only be used if it is
	  * known or suspected that the code list has been modified.
	  */
	  void reset();
}
