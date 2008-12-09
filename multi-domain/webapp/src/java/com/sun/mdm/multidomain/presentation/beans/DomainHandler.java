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

import com.sun.mdm.multidomain.services.model.Domain;

import com.sun.mdm.multidomain.services.core.ServiceManagerFactory;
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
    public DomainHandler() {  
    }
    
    /**
     * Initialize MetaDataManager.
     * @throws ServiceException Thrown if an error occurs during processing. 
     */
    private void initialize() 
        throws ServiceException {
        if (metaDataManager == null) {
            metaDataManager = ServiceManagerFactory.Instance().createMetaDataManager(); 
        }
    }
    
    /**
     * Get a list of domains.
     * @return List<Domain> List of domain. 
     * @exception ServiceException Thrown if an error occurs during processing. 
     */
    public List<Domain> getDomains() 
        throws ServiceException {        
        List<Domain> domains = null;
        try {
            initialize();
            domains = metaDataManager.getDomains();
        } catch(ServiceException sex) {
           throw sex;
        }
        return domains;
    }    
    
}
