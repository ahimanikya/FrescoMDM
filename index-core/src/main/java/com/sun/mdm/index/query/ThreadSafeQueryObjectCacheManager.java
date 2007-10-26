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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sanjay.sharma
 *
 */

public class ThreadSafeQueryObjectCacheManager implements QueryObjectCacheManager {

	private static QueryObjectCacheManager threadSafeQcManager = null;
	
	private QueryObjectCacheManager qcManager;
	
	public static QueryObjectCacheManager getInstance()  {
		synchronized ( ThreadSafeQueryObjectCacheManager.class ) {
			if ( threadSafeQcManager == null ) {
				threadSafeQcManager = new ThreadSafeQueryObjectCacheManager();
			}
			return threadSafeQcManager;
		}		
	}
 	
    private ThreadSafeQueryObjectCacheManager() {
    	this.qcManager = new QueryObjectCacheManagerImpl();
    }
	
    public  QueryObjectCache getQueryObjectCache(QueryObject qo) throws Exception {
        try {
            return qcManager.getQueryObjectCache(qo);
        } catch ( Exception e ) {
            throw e;
        }
    }    

    public  void setQueryObjectCache(QueryObjectCache qc) throws Exception {
        try {
            qcManager.setQueryObjectCache(qc);
        } catch ( Exception e ) {
            throw e;
        }
    }    

    public int getQueryObjectCacheCount() {
        return qcManager.getQueryObjectCacheCount();
    }
	
}
