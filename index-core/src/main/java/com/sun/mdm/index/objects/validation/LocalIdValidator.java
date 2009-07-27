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
package com.sun.mdm.index.objects.validation;

import java.util.regex.Pattern;
import com.sun.mdm.index.codelookup.CodeLookupException;
import com.sun.mdm.index.codelookup.SystemRegistry;
import com.sun.mdm.index.master.SystemDefinition;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.util.Localizer;

/**
 * Local id validator.
 * <p>
 * This class validates a SystemObject, confirming that: <br>
 * <ul>
 * <li>The system code is not null
 * <li>The system code is a valid system code (from the SystemRegistry)
 * <li>The local id is not null
 * <li>The local id length does not exceed the id length for that system
 * <li>The local id format matches the format specified for that system
 * <ul>
 * @author gw194542
 */
public class LocalIdValidator implements ObjectValidator {

    private SystemRegistry mSystemRegistry = null;
    private transient final Localizer mLocalizer = Localizer.get();
    
    public LocalIdValidator() {
    }
    /**
	 * Validate a SystemObject object node.
	 * <p>
	 * Note: There is no explicit check on the object node type. This class
	 * can validate any ObjectNode that has fields <b>SystemCode</b> and <b>LocalID</b>
     * @param node object node
     * @exception ValidationException ObjectException thrown by ObjectNode
     */
    public void validate(ObjectNode node) throws ValidationException {
        String systemId = null;
        String id = null;
        
        try {
            if (mSystemRegistry == null || !mSystemRegistry.isCurrent()) {
                mSystemRegistry = SystemRegistry.getInstance();
            }
        } catch (CodeLookupException ce) {
            throw new ValidationException(mLocalizer.t("OBJ668: Unable to access SystemRegistry {0}", ce), ce);
        }
 
        try {
            systemId = (String) node.getValue("SystemCode");
            id = (String) node.getValue("LocalID");
        } catch (ObjectException e) {
            throw new ValidationException(mLocalizer.t("OBJ669: Local ID Validator " + 
                                    "could not retrieve the SystemCode " + 
                                    "or the Local ID: {0}", e), e);
        }
        if (systemId == null) {
            throw new ValidationException(mLocalizer.t("OBJ670: The value for " + 
                                    "SystemObject[SystemCode] is required."));
        }
        SystemDefinition sd = mSystemRegistry.getSystemDefinition(systemId);
        if (sd == null) {
            throw new ValidationException(mLocalizer.t("OBJ672: This is " + 
                                    "not a valid System Code: {0}", systemId));

        }
		// Only want active system codes
		if (!sd.getStatus().equals("A")) {
			throw new ValidationException(mLocalizer.t("OBJ673: System Code " +
									"{0} ({1}) is not active", systemId, sd.getDescription()));
		}
        if (id == null) {
            throw new ValidationException(mLocalizer.t("OBJ671: The value for SystemObject[LocalId] " + 
                                    "for {0} is required", sd.getDescription()));
        }
        if (id.length() > sd.getIdLength()) {
            throw new ValidationException(systemId, sd.getDescription(), sd.getFormat(), id, 
                                    mLocalizer.t("OBJ675: The value " + 
                                    "of the Local ID ({0}) does not conform " + 
                                    "to the format of the Local ID for {1}, " +
                                    "which is this pattern \"{2}\" - [maximum ID length {3} exceeded]", 
                                    id, sd.getDescription(), sd.getFormat(), sd.getIdLength()));
        }
            
        Pattern pattern = sd.getPattern();
        if (pattern != null) {
            if (!pattern.matcher(id).matches()) {
                throw new ValidationException(systemId, sd.getDescription(), sd.getFormat() ,id,
                                    mLocalizer.t("OBJ676: The value " + 
                                    "of the Local ID ({0}) does not conform " + 
                                    "to the format of the Local ID for {1}, " +
                                    "which is this pattern \"{2}\"", 
                                    id, sd.getDescription(), sd.getFormat()));
            }
        }

    }

}