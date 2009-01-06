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
package com.sun.mdm.multidomain.ejb.service;



import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Date;
import javax.naming.InitialContext;
import javax.naming.Context;
import com.sun.mdm.index.ejb.master.MasterControllerRemote;
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.multidomain.relationship.service.DomainService;
import com.sun.mdm.multidomain.relationship.Domain;
import java.sql.Connection;

/**
 * Locates a MasterController for a particular domain, that allows Multi Domain to do operatons on
 * a particular domain data.
 * 
 * @author SwaranjitDua
*/

public class MasterIndexLocator {

    /**
     *
     * @param domainName domain for which Master Controller needs to be located.
     * @param con connection in MultiDomain service, that is used to get domain environment properties.
     * @return MasterController
     * @throws com.sun.mdm.index.master.ProcessingException
     */
    public MasterController getMasterController(String domainName, Connection con) throws ProcessingException {
        try {
        Hashtable env = new Hashtable();

        DomainService dService = new DomainService(con);

        Domain[] domains = dService.getDomains();

        Domain domain = null;
        for (Domain d: domains) {
           String dName = d.getDomainName();
           if (dName.equals(domainName)) {
               domain = d;
           }
        }

        String contextFactory = domain.getContextFactory();
        String url = domain.getUrl();
        String principal = domain.getPrincipal();
        String credential = domain.getCredential();

        env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
        env.put(Context.PROVIDER_URL, url);        
        if (principal != null && !principal.equals("") )
        {
          env.put(Context.SECURITY_PRINCIPAL, principal);
          env.put(Context.SECURITY_CREDENTIALS, credential);
        }
        env.put("java.naming.factory.url.pkgs","com.sun.enterprise.naming");
        env.put("java.naming.factory.state","com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");

        /* InitialContext properties for glassfish.
        env.put("java.naming.factory.initial","com.sun.enterprise.naming.SerialInitContextFactory");        
        env.put("java.naming.factory.url.pkgs","com.sun.enterprise.naming");
        env.put("java.naming.factory.state","com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
        env.put("java.naming.provider.url", "iiop://localhost:3100");
        env.put("java.naming.security.principal", "username");
        env.put("java.naming.security.credentials", "password");                
        */
        
        Context ctx = new InitialContext(env);
        MasterControllerRemote mc = (MasterControllerRemote) ctx.lookup("ejb/" + domainName + "MasterController");
        return mc;
        } catch (Exception ex) {
            throw new ProcessingException(ex);
        }
    }

}
