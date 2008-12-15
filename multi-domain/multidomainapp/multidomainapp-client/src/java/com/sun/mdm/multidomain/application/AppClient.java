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
package com.sun.mdm.multidomain.application;

import javax.ejb.EJB;

import com.sun.mdm.multidomain.ejb.service.MultiDomainMetaServiceRemote;
import com.sun.mdm.multidomain.ejb.service.MultiDomainServiceRemote;
import com.sun.mdm.multidomain.relationship.RelationshipDef;
import com.sun.mdm.multidomain.group.Group;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;

/**
 * Multidomain service AppClient.
 * @author cye
 */
public class AppClient {

    @EJB
    private static MultiDomainServiceRemote multiDomainServiceBean;
    @EJB
    private static MultiDomainMetaServiceRemote multiDomainMetaServiceBean;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            String[] domains = (String[])multiDomainMetaServiceBean.getDomains();
            System.out.println("MultiDomainMetaService:getDomains(): " + domains[0] + ", " + domains[1] + ", " + domains[2]);
            RelationshipDef[] types = (RelationshipDef[])multiDomainMetaServiceBean.getRelationshipDefs();            
        } catch (ProcessingException pex) {            
            pex.printStackTrace();
        }
        try {
            Group group = new Group();
            Group[] groups = (Group[])multiDomainServiceBean.searchGroup(group);
        } catch (UserException pex) {            
            pex.printStackTrace();            
        } catch (ProcessingException pex) {            
            pex.printStackTrace();
        }        
    }
}
