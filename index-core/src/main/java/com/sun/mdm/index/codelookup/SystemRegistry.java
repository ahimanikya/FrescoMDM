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
package com.sun.mdm.index.codelookup;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;
import com.sun.mdm.index.master.SystemDefinition;
import com.sun.mdm.index.query.QueryHelper;
import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.util.JNDINames;
import com.sun.mdm.index.util.ConnectionUtil;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/** SystemRegistry
 *
 * @author  gw194542
 */
public class SystemRegistry {
    
    private static SystemRegistry SINGLETON;


    private final static String DB_PROP_KEY = "resJNDI";
    private final static String DB_PROP_FILE = "eviewdb.properties";
    private LinkedHashMap<String,SystemDefinition> tmCodes = null;
	private boolean mCurrent = true;
    private transient static Logger mLogger = Logger.getLogger(SystemRegistry.class.getName());
    private transient static Localizer mLocalizer = Localizer.get();

    /** Creates a new instance of SystemRegistry.
     *
     * @param con  Database connection handle.
     * @throws CodeLookupException if an error is encountered.
     */
    private SystemRegistry(Connection con) throws CodeLookupException {
		// use a linked hash map to retain the sort order from the original SQL query
        tmCodes = new LinkedHashMap<String,SystemDefinition>();
        loadCodes(con);
    }

    /** Get instance of SystemRegistry
     *
     * @param con  Database connection handle.
     * @throws CodeLookupException if an error is encountered.
     * @return SystemRegistry instance.
     */
    public static synchronized SystemRegistry getInstance(Connection con)
            throws CodeLookupException {
    
        try {
            if (SINGLETON == null) {
                if (con == null) {
                    con = ConnectionUtil.getConnection();
                }
                SINGLETON = new SystemRegistry(con);
            }
            return SINGLETON;
        } catch (Exception e) {
            throw new CodeLookupException(mLocalizer.t("COD505: Could not retrieve the SystemRegistry instance: {0}", e));
        }
    }

    /** Get instance of SystemRegistry.
     * 
     * @throws CodeLookupException if an error is encountered.
     * @return SystemRegistry instance
     */    
    public static synchronized SystemRegistry getInstance() 
            throws CodeLookupException {
                
        return getInstance(null);
    }    
    
	/** Reset current instance of SystemRegistry.
	 * <p>
	 * This will reload the system registry from the database the next time getInstance() is invoked. 
	 */
	public static synchronized void reset() {
		if (SINGLETON != null) {
			SINGLETON.setCurrent(false);
			SINGLETON = null;
		}
	}
    
	/** Check if this is the current instance of SystemRegistry.
	 * @return <b>true</b> if this is the current instance, and <b>false</b> otherwise 
	 */
	public boolean isCurrent() {
		return mCurrent;
	}
	
	/** Set the instance status
	 * @param status new status
	 */
	protected void setCurrent(boolean status) {
		mCurrent = status;
	}
	
    /** Get system definition for a specified system code.
     *
     * @return SystemDefinition for specified system code, or null if the system code is not known
     * @param systemCode System code.
     */
    public SystemDefinition getSystemDefinition(String systemCode) {

        SystemDefinition sd = tmCodes.get(systemCode);
		// clone the system definition so that the calling process
		// can't use the "set" methods to change the value in the registry
		if (sd != null) {
			sd = (SystemDefinition) sd.clone();
		}
        return sd;
    }

    /** Get system definitions
     *
     * @return array of all system definitions
     */    
    public SystemDefinition[] getSystemDefinitions() {
	    int size = tmCodes.size();
		if (size == 0) {
		    return null;
		}
	    SystemDefinition sdArray[] = new SystemDefinition[size];
		int i = 0;
		Collection<SystemDefinition> sdColl = tmCodes.values();
		for (SystemDefinition sd : sdColl) {
			// clone the system definition so that the calling process
			// can't use the "set" methods to change the value in the registry
			sdArray[i++] = (SystemDefinition) sd.clone();
		}
        return sdArray;
    }

    /** Get system definitions
     *
     * @return array of all system definitions
     */    
    public List<SystemDefinition> getSystemDefinitionList() {
	    int size = tmCodes.size();
		if (size == 0) {
		    return null;
		}
		ArrayList sdArray = new ArrayList<SystemDefinition>();
		Collection<SystemDefinition> sdColl = tmCodes.values();
		for (SystemDefinition sd : sdColl) {
			// clone the system definition so that the calling process
			// can't use the "set" methods to change the value in the registry
			sdArray.add( (SystemDefinition) sd.clone() );
		}
        return sdArray;
    }

    
    /** Check if a system code exists.
     *
     * @param systemCode System Code.
     * @return true if the system code exists, false otherwise.
     */
    public boolean hasSystemCode(String systemCode) {
        return tmCodes.containsKey(systemCode);
    }

    /** Loads the system codes from the database into the tmCodes Hashmap.
     *
     * @param con  Database connection handle.
     * @throws CodeLookupException if an error is encountered.
     */
    private void loadCodes(Connection con) throws CodeLookupException {
	
	    QueryHelper mQueryHelper = new QueryHelper();
        SystemDefinition[] sd = null;
        try {
            sd = mQueryHelper.lookupSystemDefinitions(con);
			if (sd != null) {
				for (int i=0; i < sd.length; i++) {
					tmCodes.put(sd[i].getSystemCode(), sd[i]);
				}
			}
        } catch (Exception e) {
            throw new CodeLookupException(mLocalizer.t("COD506: Could not load codes for the SystemRegistry: {0}", e), e);
        }
    }
}
