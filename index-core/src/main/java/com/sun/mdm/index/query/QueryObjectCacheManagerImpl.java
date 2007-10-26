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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

/**
 * @author sanjay.sharma
 *
 */

public class QueryObjectCacheManagerImpl implements QueryObjectCacheManager {

    private final Logger mLogger = LogUtil.getLogger(this);

    private QueryObjectCachePool mQueryObjectCachePool = null;    	
	private int  mOldestCacheIndex = 0;
    private int  mCacheSize = 50;
	
    public QueryObjectCacheManagerImpl() {
    	this.mQueryObjectCachePool = new QueryObjectCachePool(mCacheSize);
    }
	
	public QueryObjectCache getQueryObjectCache(QueryObject qo) throws Exception {
        QueryObjectCache qcNew = null;
        QueryObject qoMatched = null;
        QueryObjectCache qoCacheMatched = null;
        Hashtable bindingLookup = new Hashtable();
        boolean flag = false;

        qoCacheMatched = (QueryObjectCache) mQueryObjectCachePool.get(qo);
        if (qoCacheMatched != null) {
            qoMatched = (QueryObject) qoCacheMatched.getQueryObject();
            QueryObject qoNew = new QueryObject(qoMatched); 
            QMIterator qmIter = null;
            if (qoCacheMatched.getQMIterator() != null) {
            	qmIter = (QMIterator) qoCacheMatched.getQMIterator().clone();
            }
            qcNew = new QueryObjectCache(qoNew, qmIter, null);             	
            Hashtable conditionLookup = buildConditionLookup(qo, qoNew);
            updateCacheBinding(qcNew, qoCacheMatched, conditionLookup);                
            return qcNew;
        }

        return null;
    }    
    
	public  void setQueryObjectCache(QueryObjectCache qc) throws Exception {

        mQueryObjectCachePool.put(qc.getQueryObject(), qc);
        if (mLogger.isDebugEnabled()) {
            mLogger.debug(Thread.currentThread() + " Current Cache Size is : " + mQueryObjectCachePool.usedEntries());
        } 
   	}
	
    public int getQueryObjectCacheCount() {
        return mQueryObjectCachePool.usedEntries();
    }
    
    private  void updateCacheBinding(QueryObjectCache qc, QueryObjectCache matchedQc, Hashtable conditionLookup) {
        List [] bindParamsMatched = matchedQc.getBindParamList();        
        List [] bindParams = new ArrayList[bindParamsMatched.length];

    	for ( int i=0; i < matchedQc.getQueryObject().getSQLDescriptor().length; i++ ) {
            List bindParamMatchedList = bindParamsMatched[i];        
            bindParams[i] = new ArrayList();
            if ( bindParamMatchedList != null ) {
    		Condition cond = null;
                Condition condNew = null;
        
    		for ( int j = 0; j < bindParamMatchedList.size(); j++ )  {
                    cond = (Condition) bindParamMatchedList.get(j);
                    condNew = (Condition) conditionLookup.get(cond);    				
                    bindParams[i].add(condNew);    			
    		}
            }
    	}
    	qc.setBindParamList(bindParams);
    }    
    
    private Hashtable buildConditionLookup(QueryObject qo, QueryObject otherQueryObject) {
        Condition [] conditions = qo.getReconstructedConditionsUnion();
        Condition [] otherConditions = otherQueryObject.getReconstructedConditionsUnion();
        Hashtable conditionLookup = new Hashtable();
        Condition condition = null;
        for ( int i=0; i < conditions.length; i++ ) {
            conditions[i].chains(otherConditions[i], conditionLookup);
        }
        return conditionLookup;
    }
}
