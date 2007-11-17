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
package com.sun.mdm.index.configurator.impl.standardization;

import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathParser;
import com.sun.mdm.index.objects.metadata.MetaDataService;
import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;


/**
 * Represents the definition of a SystemObject field used in the configuration.
 * @author aegloff
 * @version $Revision: 1.1 $
 */
public class SystemObjectField {
    private EPath ePath;
    private String qualifiedName; // String version of the configured EPath, includes decorators such as [*]
    private int fieldSize;

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    
    /** Creates new SystemObjectField instance */
    public SystemObjectField() {
    }

    /**
     * Creates new SystemObjectField.
     *
     * @param qualifiedName The qualifiedName of the SystemObject field.
     * @throws ConfigurationException if the qualifiedName could not be parsed into an EPath.
     */
    public SystemObjectField(String qualifiedName)
            throws ConfigurationException {
                
        this.qualifiedName = qualifiedName;
        try {
            this.ePath = EPathParser.parse(qualifiedName);
        } catch (com.sun.mdm.index.objects.epath.EPathException ex) {
            throw new ConfigurationException(mLocalizer.t("CFG545: SystemObjectField " + 
                                    "failed to parse qualified field name {0} as an EPath: {1}", 
                                    qualifiedName, ex));
        }
        // Get the maximum field size from the MetaDataService
        try {
            String undecoratedFieldName = this.ePath.toFieldName();
            String fullyQualified = MetaDataService.getSOPath(undecoratedFieldName);
            fieldSize = MetaDataService.getFieldSize(fullyQualified);
        } catch (Exception ex) {
            throw new ConfigurationException(mLocalizer.t("CFG546: SystemObjectField failed " + 
                                    "to get the maximum field size from the MetaDataService " + 
                                    "for the configured standardization field {0}: {1}", 
                                    qualifiedName, ex));
        }
    }

    /**
     * Getter for the EPath, representing a parsed version of the qualifiedName.
     *
     * @return the EPath to access the field on the SystemObject
     */
    public EPath getEPath() {
        return ePath;
    }


    /**
     * Getter for QualifiedName attribute.
     *
     * @return the qualified name of the SystemObject field as a String, 
     * qualified as in it includes the name of the primary object such as 
     * Person.FirstName
     * This is the String version of the ePath and includes decorators such as [*].
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * Getter for FieldSize attribute.
     *
     * @return the maximum field size of the SystemObject field.
     */    
    public int getFieldSize() {
        return fieldSize;
    }
}
