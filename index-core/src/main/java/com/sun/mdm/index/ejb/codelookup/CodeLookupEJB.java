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
import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.sun.mdm.index.codelookup.CodeDescription;
import com.sun.mdm.index.codelookup.CodeLookupException;
import com.sun.mdm.index.codelookup.CodeRegistry;

/**
 * Session bean to provide code display pairs
 */

@Stateless(mappedName="ejb/_EVIEW_OBJECT_TOKEN_CodeLookup")
@Remote(CodeLookupRemote.class)
@Resource(name="jdbc/BBEDataSource", 
          type=javax.sql.DataSource.class,
          mappedName="jdbc/_EVIEW_OBJECT_TOKEN_DataSource" )
public class CodeLookupEJB implements CodeLookupRemote {

    /**
     * Creates a new instance of CodeLookupEJB
     */
    public CodeLookupEJB() { 
    }

    
    /** See CodeLookup
     * @throws CodeLookupException See CodeLookup
     * @return See CodeLookup
     */    
    public Map getAllCodes() throws CodeLookupException {
        Map codeMap = CodeRegistry.getInstance().getCodeMap();
        return codeMap;
    }
    
    /** See CodeLookup
     * @param module See CodeLookup
     * @throws CodeLookupException See CodeLookup
     * @return See CodeLookup
     */    
    public Map getCodesByModule(String module) throws CodeLookupException {
        Map codeMap = CodeRegistry.getInstance().getCodeMapByModule(module);
        return codeMap;
    }
	
	/** Check validity of a module
	 * @param module module
	 * @return <b>true</b> if it is a valid module, <b>false</b> otherwise
	 * @throws CodeLookupException an error occured
	 */
	 public boolean hasModule(String module) throws CodeLookupException {
		return CodeRegistry.getInstance().hasModule(module);
	 }
    
	/** Check validity of a code
	 * @param module module
	 * @param code code
	 * @return <b>true</b> if it is a valid code in the specified modeule, <b>false</b> otherwise
	 * @throws CodeLookupException an error occured
	 */
	 public boolean hasCode(String module, String code) throws CodeLookupException {
		return CodeRegistry.getInstance().hasCode(module, code);
	}
    
	/** Get code description
	 * @param module module
	 * @param code code
	 * @return code description if this is a valid code, <b>null</b> otherwise
	 * @throws CodeLookupException an error occured
	 */
	 public String getDescription(String module, String code) throws CodeLookupException {
		Map codeMap = CodeRegistry.getInstance().getCodeMapByModule(module);
		String desc = null;
		if (codeMap != null) {
			CodeDescription cd = (CodeDescription) codeMap.get(code);
			if (cd != null) {
				desc = cd.getDescription();
			}
		}
		return desc;
	 }
	 
	 /** Reset the code list.
	  * <p>
	  * This will reload the current set of codes from the database. This should only be used if it is
	  * known or suspected that the code list has been modified.
	  */
	public void reset() {
		CodeRegistry.reset();
	}
}
