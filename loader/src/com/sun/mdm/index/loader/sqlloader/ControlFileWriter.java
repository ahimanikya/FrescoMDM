/**
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
package com.sun.mdm.index.loader.sqlloader;

import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 * @author Sujit Biswas, David Peh
 * 
 */
public abstract class ControlFileWriter implements Writer {

    LoaderConfig config = LoaderConfig.getInstance();

    String getRecordLimiter() {
        return config.getSystemProperty("record.delimiter");
    }

    /**
     * @param i
     * @return
     */
    boolean isDeltaColumn(Table table, int i) {
        return table.getName().equalsIgnoreCase("SBYN_TRANSACTION") && table.getColumns().get(i).equalsIgnoreCase("delta");
    }

    boolean isDate(String table, String column) {
        if (table.equals("SBYN_SYSTEMOBJECT") || table.equals("SBYN_SYSTEMSBR") || table.equals("SBYN_POTENTIALDUPLICATES")) {

            if (column.equalsIgnoreCase("createdate") || column.equalsIgnoreCase("updatedate") || column.equalsIgnoreCase("resolveddate")) {
                return true;
            }
        }
        if (config.getSystemProperty("database").equalsIgnoreCase("MySQL")) {
            if (table.equalsIgnoreCase("SBYN_TRANSACTION") && column.equalsIgnoreCase("TIMESTAMP")) {
                return true;
            }
        }
        return false;
    }

    boolean isDate(String date) {
        return "date".equalsIgnoreCase(date);
    }
}
