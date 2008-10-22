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
package com.sun.mdm.multidomain.services.core.context;

import java.util.Properties;
import javax.naming.Context;

/**
 * JndiResourceProperties class.
 * @author cye
 */
public class JndiProperties extends Properties {
    public static final String INITIAL_CONTEXT_FACTORY = Context.INITIAL_CONTEXT_FACTORY;
    public static final String PROVIDER_URL = Context.PROVIDER_URL;
    public static final String SECURITY_PRINCIPAL = Context.SECURITY_PRINCIPAL;
    public static final String SECURITY_CREDENTIALS = Context.SECURITY_CREDENTIALS;

    /**
     * Get INITIAL_CONTEXT_FACTORY attribute value.
     * @return String INITIAL_CONTEXT_FACTORY attribute value if it is defined otherwise return null.
     */
    public String getInitialContextFactory() {        
        return getProperty(INITIAL_CONTEXT_FACTORY);
    }
    
    /**
     * Get PROVIDER_URL attribute value.
     * @return String PROVIDER_URL attribute value if it is defined otherwise return null.
     */
    public String getProviderUrl() {        
        return getProperty(PROVIDER_URL);
    }

    /**
     * Get SECURITY_PRINCIPAL attribute value.
     * @return String SECURITY_PRINCIPAL attribute value if it is defined otherwise return null.
     */    
    public String getSecurityPrincipal() {        
        return getProperty(SECURITY_PRINCIPAL);
    }

    /**
     * Get SECURITY_CREDENTIALS attribute value.
     * @return String SECURITY_CREDENTIALS attribute value if it is defined otherwise return null.
     */    
    public String getSecurityCredentials() {        
        return getProperty(SECURITY_CREDENTIALS);
    }
    
}
