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
package com.sun.mdm.index.parser;


/**
 * needs to merge into parser package
 *
 * @author ckuo
 * @version $Revision: 1.1 $
 */
public class BlockingRuleType {
    /**
     * blocking rule type none
     */
    public static final int TYPE_NONE = -1;
    /**
     * blocking rule type exact
     */
    public static final int TYPE_EXACT = 0;
    /**
     * blocking rule type startswith
     */
    public static final int TYPE_STARTSWITH = 1;
    /**
     * blocking rule type soundex
     */
    public static final int TYPE_SOUNDEX = 2;
    /**
     * blocking rule type nysiis
     */
    public static final int TYPE_NYSIIS = 3;


    private String mColumnName;
    private int mPriority;
    private int mRuleType;

    private String mTableName;


    //public Vector relationships;

    /**
     * Creates new BlockingRule
     *
     * @param type type
     */
    public BlockingRuleType(int type) {
        mRuleType = type;
    }


    /**
     * @param tName t name
     * @param cName c name
     * @param type type
     * @param priority priority
     */
    public BlockingRuleType(String tName, 
                            String cName, 
                            int type, 
                            int priority) {
        mTableName = tName;
        mColumnName = cName;
        mRuleType = type;
        mPriority = priority;
    }


    /**
     * @param fieldName field name
     * @param type type
     * @param priority priority
     * @return BlockingRuleType blocking rule type
     */
    public static BlockingRuleType parse(String fieldName, 
                                         int type, 
                                         int priority) {
        java.util.StringTokenizer tok 
            = new java.util.StringTokenizer(fieldName, ".");
        int len = tok.countTokens();
        String[] tokenQueue = new String[len];

        for (int i = 0; i < tokenQueue.length; i++) {
            tokenQueue[i] = tok.nextToken();
        }

        // assume the initial table name is the second to the last token
        String objectName = tokenQueue[len - 2];
        // column name is the last of the token
        String columnName = tokenQueue[len - 1];

        BlockingRuleType rule 
            = new BlockingRuleType(Utils.getSBRTableName(objectName), 
                                   columnName, type, priority);

        return rule;
    }


    /**
     * @return String ret string
     */
    public String getColumnName() {
        return mColumnName;
    }


    /**
     * @return int ret int
     */
    public int getRuleType() {
        return mRuleType;
    }


    /**
     * @return String ret string
     */
    public String getTableName() {
        return mTableName;
    }


    /**
     * @param p priority
     */
    public void setPriority(int p) {
        mPriority = p;
    }


    /**
     * @param fieldName field name
     */
    public void parseField(String fieldName) {
        java.util.StringTokenizer tok 
            = new java.util.StringTokenizer(fieldName, ".");
        int len = tok.countTokens();
        String[] tokenQueue = new String[len];

        for (int i = 0; i < tokenQueue.length; i++) {
            tokenQueue[i] = tok.nextToken();
        }

        // assume the initial table name is the second to the last token
        String objectName = tokenQueue[len - 2];
        mTableName = Utils.getSBRTableName(objectName);

        // column name is the last of the token
        mColumnName = tokenQueue[len - 1];

    }
}
