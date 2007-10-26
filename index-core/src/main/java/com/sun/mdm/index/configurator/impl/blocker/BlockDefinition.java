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
package com.sun.mdm.index.configurator.impl.blocker;

import java.util.ArrayList;
import java.util.Collection;


/**
 * @version $Revision: 1.1 $
 */
public class BlockDefinition {
    
    /** id */
    private String id;
    
    /** list of rules */
    private Collection rules;
    
    /** overriding SQL statemet */
    private String sql;
    private String hint = "";

    /**
     * Creates a new instance of BlockDefinition
     */
    public BlockDefinition() {
        rules = new ArrayList();
    }

    /**
     * Getter for ID attribute of the BlockDefinition object.
     *
     * @return id.
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for Rules attribute of the BlockDefinition object.
     *
     * @return list of rules.
     */
    public Collection getRules() {
        return rules;
    }

    /**
     * Getter for Sql attribute of the BlockDefinition object.
     *
     * @return SQL statement
     */
    public String getSql() {
        return sql;
    }

    /**
     * Setter for ID attribute of the BlockDefinition object.
     *
     * @param val ID value.
     */
    public void setId(String val) {
        id = val;
    }

    /**
     * Setter for Sql attribute of the BlockDefinition object.
     *
     * @param val Sql value.
     */
    public void setSql(String val) {
        sql = val;
    }

    /**
     * Adds a feature to the Rule attribute of the BlockDefinition object.
     *
     * @param rule The feature to be added to the Rule attribute.
     */
    public void addRule(BlockRule rule) {
        rules.add(rule);
    }
    
    /**
     * Setter for Hint attribute of the BlockDefinition object.
     *
     * @param val Hint value.
     */
    public void setHint(String val) {
        hint = val;
    }
    
    /**
     * Getter for Hint attribute of the BlockDefinition object.
     *
     * @return Hint String
     */
    public String getHint() {
        return hint;
    }
}
