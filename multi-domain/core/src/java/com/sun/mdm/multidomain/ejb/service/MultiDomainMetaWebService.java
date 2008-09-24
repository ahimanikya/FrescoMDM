/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.ejb.service;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ejb.Stateless;

import com.sun.mdm.index.master.ProcessingException;

import com.sun.mdm.multidomain.relationship.RelationshipType;

/**
 *
 * @author cye
 */
@WebService()
@Stateless()
public class MultiDomainMetaWebService {
    @EJB
    private MultiDomainMetaServiceLocal ejbRef;

    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainMetaService#getDomains() 
     */            
    @WebMethod(operationName = "getDomains")
    public java.lang.String[] getDomains() 
        throws ProcessingException {
        return ejbRef.getDomains();
    }

    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainMetaService#getRelationshipTypes() 
     */    
    @WebMethod(operationName = "getRelationshipTypes")
    public RelationshipType[] getRelationshipTypes() 
        throws ProcessingException {
         return ejbRef.getRelationshipTypes();
    }
    
}
