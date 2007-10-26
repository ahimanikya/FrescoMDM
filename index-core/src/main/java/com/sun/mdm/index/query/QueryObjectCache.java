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
package com.sun.mdm.index.query;

import java.util.List;



/**
 * @author sdua
 */
 class QueryObjectCache {
    private QMIterator mqmIterator;
    private QueryObject mqo;
    private List [] mBindParamList;
    
    /**
     * Creates a new instance of QueryObjectCache
     *
     * @param qo
     * @param qmIterator
     */
     QueryObjectCache(QueryObject qo, QMIterator qmIterator, List[] bindParamLists) {
        mqo = qo;
        qo.setCacheRef(this);
        mqmIterator = qmIterator;
        mBindParamList = bindParamLists;
    }

    /*
     * Returns a String representation of this object.
     * @return a String representation of this object.
     */
    public String toString() {
        String s = "[\n";
        if ( mqo != null ) {
            s = "QueryCache = " + mqo + "\nBindParams = {\n";
        }
        if ( mBindParamList != null ) {
            for ( int j =0; j < mBindParamList.length; j++ ) {
                List l = (List)mBindParamList[j];
                s = s + "Index changed to " + j; 
                for ( int k =0; k < l.size(); k++ ) {
                    s = s + l.get(k) + " ,"; 
                }
                s = s + "\n";
            }
        }
        s = s + "\n}\n]";
        return s;
    }
     
    QMIterator getQMIterator() {
        return mqmIterator;
    }

     void setQMIterator(QMIterator qmIterator) {
        this.mqmIterator = qmIterator;
    }
    
    QueryObject getQueryObject() {
        return mqo;
    }

    boolean isPrepare() {
        return mqo.isPrepare();
    }

    List[] getBindParamList() {
        return mBindParamList;
    }

    void setBindParamList(List [] bindParamList) {
        this.mBindParamList = bindParamList;
    }

    /*
     * Returns a boolean value indiciating if the parameter is
     * equal to the current object.
     * 
     * @param  QueryObjectCache.
     * @return a boolean value
     */
    public boolean equals(QueryObjectCache theOther) {
       return mqo.equals(theOther.getQueryObject());
    }
    
 }
