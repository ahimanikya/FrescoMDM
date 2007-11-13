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
package com.sun.mdm.index.configurator.impl.matching;

import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathParser;
import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * Represents the configuration for a Match Column.
 *
 * @author aegloff
 * @version $Revision: 1.1 $
 */
public class MatchColumn {
    private EPath ePath;
    private int matchOrder;
    private String matchType;
    private String qualifiedName;

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();

    /** Creates a new MatchColumn instance */
    public MatchColumn() {
    }

    /**
     * Retreives the parsed fully qualified name including qualifiers 
     * (EPath string) to access value objects, that corresponds to this 
     * Column. This can be used directly with the EPath API.
     *
     * @return the ePath
     */
    public EPath getEPath() {
        return ePath;
    }


    /**
     * The matchOrder is an match engine-specific setting that can control the
     * order the columns are used in matching if the MatchEngine requires this
     * to be defined.
     *
     * @return the match order
     */
    public int getMatchOrder() {
        return matchOrder;
    }


    /**
     * Retreives the type of a column a specific MatchEngine understands so it 
     * can identify what this column represents and how to match it.
     *
     * @return the match type
     */
    public String getMatchType() {
        return matchType;
    }


    /**
     * Retrieves the fully qualified name including qualifiers (EPath string) 
     * to access value objects, that corresponds to this Column. This can be used 
     * with the EPath API.
     *
     * @return the qualified name
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * Sets the matchOrder if the chosen match engine supports this setting.
     *
     * @param value The order in which to match a column.
     */
    void setMatchOrder(int value) {
        matchOrder = value;
    }

    /**
     * Sets the matchType.
     *
     * @param value The match type.
     */
    void setMatchType(String value) {
        matchType = value;
    }

    /**
     * Sets the qualified name of the match column.
     *
     * @param value Qualified name of the match column.
     * @throws ConfigurationException if the configuration could not be parsed
     */
    void setQualifiedName(String value)
        throws ConfigurationException {
        qualifiedName = value;
        try {
            this.ePath = EPathParser.parse(qualifiedName);
        } catch (com.sun.mdm.index.objects.epath.EPathException ex) {
            throw new ConfigurationException(mLocalizer.t("CFG533: Failed to parse MatchColumn " + 
                                                        "qualified field name {0} as an EPath: {1}", qualifiedName, ex));
        }
    }

}
