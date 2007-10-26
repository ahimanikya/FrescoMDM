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
package com.sun.mdm.index.querybuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The ObjectTable class is used to keep list of  ObjectColumn. 
 * It also provides an iterator that iterates over the list of ObjectColumn collection.   
 *
 * @author Sanjay.sharma
 * @version $Revision: 1.1 $
 */

public class ObjectTable {

    private String name;
    private List columnList;
    
    public ObjectTable() {
        this.columnList = new ArrayList();
    }
    
    /**
     * @return Returns the columnList.
     */
    public List getColumnList() {
        return columnList;
    }
    /**
     * @param columnList The columnList to set.
     */
    public void addColumn(ObjectColumn column) {
        columnList.add(column);
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public Iterator getColumnIterator() {
        return columnList.iterator();
    }
    
    public int getValuesSize() {
        return ((ObjectColumn)columnList.get(0)).getValueList().size();        
    }
}
