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
package com.sun.mdm.index.survivor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * The <b>SystemFieldListMap</b> class represents a mapping of a system key
 * (that is, a system code and local ID pair) to a corresponding list of system
 * fields. This class is used by the Survivor Strategy Interface when evaluating
 * which field values should populate the Single Best Record (SBR).
 *
 * @version $Revision: 1.1 $
 */
public class SystemFieldListMap implements java.io.Serializable {
    /** A map of system field lists */
    private Map mListMap;

    /**
     * Creates a new instance of the SystemFieldListMap class.
	 * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DL><DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public SystemFieldListMap() {
        mListMap = new HashMap();
    }

    /**
     * Places a system field list in the map, associating the list with
     * the specified system key.
     * <p>
     * @param key The system key.
     * @param list The list of system fields to be mapped to the specified
     * system key.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void put(SystemKey key, SystemFieldList list) {
        mListMap.put(key, list);
    }

    /**
     * Places a system field list in the map using the specified system code
     * and local ID.
     * <p>
     * @param systemCode The system code.
     * @param lid The local ID corresponding to the given system code.
     * @param list The list of system fields to be mapped to the specified
     * system and local ID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void put(String systemCode, String lid, SystemFieldList list) {
        SystemKey key = new SystemKey(systemCode, lid);
        mListMap.put(key, list);
    }

    /**
     * Retrieves the list of fields mapped to a specific system key.
     * <p>
     * @param key The system key for the field list to retrieve.
     * @return <CODE>SystemFieldList</CODE> - The system field list
     * corresponding to the given key.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public SystemFieldList get(SystemKey key) {
        return (SystemFieldList) mListMap.get(key);
    }

    /**
     * Retrieves a collection of system field lists mapped to a specific
     * system code.
     * <p>
     * @param systemCode The system code for the field list to retrieve.
     * @return <CODE>Collection</CODE> - A collection of system field lists
     * corresponding to the given system code.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Collection get(String systemCode) {
        ArrayList list = new ArrayList(mListMap.size());
        Set keySet = mListMap.keySet();
        Iterator iter = keySet.iterator();
        while (iter.hasNext()) {
            SystemKey key = (SystemKey) iter.next();
            if (key.getSystemCode().equals(systemCode)) {
                list.add(mListMap.get(key));
            }
        }
        return list.size() > 0 ? list : null;
    }

    /**
     * Retrieves the list of system keys from the mapping.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Set</CODE> - A set of system keys.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Set keySet() {
        return mListMap.keySet();
    }

    /**
     * Retrieves the set of system keys that correspond to the given system code.
     * <p>
     * @param systemCode The system code for the system keys to retrieve.
     * @return <CODE>Set</CODE> - A set of system keys.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Set keySet(String systemCode) {
        Set allKeys = mListMap.keySet();
        Set matchingKeys = new HashSet();
        Iterator iter = allKeys.iterator();
        while (iter.hasNext()) {
            SystemKey key = (SystemKey) iter.next();
            if (key.getSystemCode().equals(systemCode)) {
                matchingKeys.add(key);
            }
        }
        return matchingKeys;
    }

   /**
     * Retrieves a string representation of the system field list map.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A string representation of the map.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String toString() {
        return mListMap.toString();
    }

    /**
     * Retrieves the initial set of objects in the map.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Set</CODE> - A set of system key objects.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Set entrySet() {
        return mListMap.entrySet();
    }

    /**
     * Retrieves all the SystemFieldList objects in the map.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Collection</CODE> - A collection of system field lists.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Collection values() {
        return mListMap.values();
    }

    /**
     * Returns the number of entries in the map.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>int</CODE> - The number of map entries.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public int size() {
        return mListMap.size();
    }

    /**
     * The <b>SystemKey</b> class is an inner class representing a system
     * code and local ID pair mapped to a corresponding list of system
     * fields in an instance of SystemFieldListMap.
     * @include
     */
    public class SystemKey {
        /** A system code */
        private String systemCode;
        /** A local id */
        private String lid;

        /**
         * Creates a new instance of the SystemKey class.
	     * <p>
         * @param code The system code of the system key.
         * @param id The local ID of the system key.
         * <DT><B>Throws:</B><DD>None.
         * @include
         */
        public SystemKey(String code, String id) {
            systemCode = code;
            lid = id;
        }

        /**
         * Retrieves the system code from an instance of SystemKey.
	     * <p>
         * <DL><DT><B>Parameters:</B><DD>None.</DL>
         * @return <CODE>String</CODE> - A system code.
         * <DT><B>Throws:</B><DD>None.
         * @include
         */
        public String getSystemCode() {
            return systemCode;
        }

        /**
         * Retrieves the local ID from an instance of SystemKey.
	     * <p>
         * <DL><DT><B>Parameters:</B><DD>None.</DL>
         * @return <CODE>String</CODE> - A local ID.
         * <DT><B>Throws:</B><DD>None.
         * @include
         */
        public String getLid() {
            return lid;
        }

        /**
         * Retrieves a string representation of a SystemKey object.
	     * <p>
         * <DL><DT><B>Parameters:</B><DD>None.</DL>
         * @return <CODE>String</CODE> - A string representation of the system key.
         * <DT><B>Throws:</B><DD>None.
         * @include
         */

        public String toString() {
            return new String("SystemCode: " + systemCode + ", LID: " + lid);
        }
    }
}
