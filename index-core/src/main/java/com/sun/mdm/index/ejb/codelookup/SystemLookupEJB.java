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
import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.sun.mdm.index.master.SystemDefinition;
import com.sun.mdm.index.codelookup.CodeLookupException;
import com.sun.mdm.index.codelookup.SystemRegistry;

/**
 * Session bean to provide System code information
 */

@Stateless(mappedName="ejb/_EVIEW_OBJECT_TOKEN_SystemLookup")
@Remote(SystemLookupRemote.class)
@Resource(name="jdbc/BBEDataSource", 
          type=javax.sql.DataSource.class,
          mappedName="jdbc/_EVIEW_OBJECT_TOKEN_DataSource" )
public class SystemLookupEJB implements SystemLookupRemote {

    /**
     * Creates a new instance of SystemLookupEJB
     */
    public SystemLookupEJB() { 
    }

    
    /** Get all system definitions as an array
     * @return Array of SystemDefiniton objects
     * @throws CodeLookupException an error occured
     */    
    public SystemDefinition[] getSystemDefinitions() throws CodeLookupException {
        return SystemRegistry.getInstance().getSystemDefinitions();
    }
    
    /** Get all system definitions as a List
     * @return List of SystemDefinition objects
     * @throws CodeLookupException an error occured
     */    
    public List<SystemDefinition> getSystemDefinitionList() throws CodeLookupException {
        return SystemRegistry.getInstance().getSystemDefinitionList();
    }
    
    /** Get system definition for specified system code
     * @param systemCode System code
     * @return SystemDefinition for specified system code, or null no such system code
     * @throws CodeLookupException an error occured
     */    
    public SystemDefinition getSystemDefinition(String systemCode) throws CodeLookupException {
        return SystemRegistry.getInstance().getSystemDefinition(systemCode);
    }
	
	/** Check validity of a system code
	 * @param systemCode System code
	 * @return <b>true</b> if it is a valid system code, <b>false</b> otherwise
	 * @throws CodeLookupException an error occured
	 */
	 public boolean hasSystemCode(String systemCode) throws CodeLookupException {
		return SystemRegistry.getInstance().hasSystemCode(systemCode);
	 }
	 
	 /** Reset the system code list.
	  * <p>
	  * This will reload the current set of codes from the database. This should only be used if it is
	  * known or suspected that the system code list has been modified.
	  */
	public void reset() {
		SystemRegistry.reset();
	}
}
