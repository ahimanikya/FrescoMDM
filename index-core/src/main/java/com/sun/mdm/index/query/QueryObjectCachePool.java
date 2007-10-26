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

import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;

/**
* QueryObjectCachePool, based on LinkedHashMap
* This cache has a fixed maximum number of elements (poolSize).
* If the cache pool has reached its maximum size and another entry is added, the LRU (least recently used) entry is removed.
*/
public class QueryObjectCachePool  {

    private final float	mLoadFactor = 0.75f;
    private LinkedHashMap	mHashMap;
    private int 		mPoolSize;

    /**
     * Creates a new LRU cache.
     * @param cacheSize the maximum number of entries that will be kept in the pool.
     */
    public QueryObjectCachePool (int poolSize) {
        this.mPoolSize = poolSize;
        int hashMapCapacity = (int)Math.ceil(poolSize / mLoadFactor) + 1;
   		
        mHashMap = new LinkedHashMap (hashMapCapacity, mLoadFactor, true) {
            protected boolean removeEldestEntry (Map.Entry  eldest) {
                return size() > QueryObjectCachePool.this.mPoolSize; 
            }
        };
    }

    /**
     * Retrieves a QueryObjectCache from the pool
     * @param key the key of the QueryObjectCache to be returned.
     * @return    the QueryObjectCache, or null if no QueryObjectCache with this key 
     * 			exists in the pool.
     */
    public synchronized QueryObjectCache get (QueryObject key) {
        return (QueryObjectCache) mHashMap.get(key);
    }

    /**
     * Adds an entry to this cache.
     * If the cache is full, the LRU (least recently used) entry is dropped.
     * @param key    the key with which the specified value is to be associated.
     * @param value  a value to be associated with the specified key.
     */
    public synchronized void put (QueryObject key, QueryObjectCache value) {
        mHashMap.put(key, value);
    }

    /**
     * Clears the cache.
     */
    public synchronized void clear() {
        mHashMap.clear(); 
    }

    /**
     * Returns the number of used entries in the cache.
     * @return the number of entries currently in the cache.
     */
    public synchronized int usedEntries() {
        return mHashMap.size(); 
    }

    /**
     * Returns a <code>Collection</code> that contains a copy of all cache entries.
     * @return a <code>Collection</code> with a copy of the cache content.
     */
    public synchronized Collection getAll() {
        return new ArrayList(mHashMap.entrySet()); 
    }
    
}
