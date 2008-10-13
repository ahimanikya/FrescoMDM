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
package com.sun.mdm.multidomain.presentation.beans;

import java.util.List;
import java.util.ArrayList;

import com.sun.mdm.multidomain.relationship.Domain;

import com.sun.mdm.multidomain.services.control.ServiceManagerFactory;
import com.sun.mdm.multidomain.services.control.MetaDataManager;
import com.sun.mdm.multidomain.services.core.ServiceException;     

/**
 * DomainHandler class.
 * @author cye
 */
public class DomainHandler {
    
    private MetaDataManager metaDataManager;
    
    /**
     * Create an instance of DomainHandler.
     */
    public DomainHandler()
        throws ServiceException { 
        metaDataManager = ServiceManagerFactory.Instance().createMetaDataManager();  
    }
        
    /**
     * Get a list of domains excluded the given domain.
     * @param domain Domain excluded.
     * @return List<Domain> List of domain excluded for the given domain. 
     *                      Or list of all domains if the input domain is null. 
     * @exception ServiceException Thrown if an error occurs during processing. 
     */
    public List<Domain> getDomains(String domain) 
        throws ServiceException {        
        List<Domain> domains = null;
        try {
            List<Domain> ds = metaDataManager.getDomains();
            if (domain == null || domain.length() == 0) {
                domains = ds;
            } else {
                domains = new ArrayList<Domain>();
                for(Domain d : ds) {
                    if (!d.equals(domain)) {
                        domains.add(d);
                    }
                }                                
            }
        } catch(ServiceException sex) {
           throw sex;
        }
        return domains;
    }    
    
}
