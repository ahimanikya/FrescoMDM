/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mdm.multidomain.relationship.service;

import com.sun.mdm.multidomain.relationship.ops.exceptions.DaoException;
import java.sql.Connection;
import com.sun.mdm.multidomain.relationship.Domain;
import com.sun.mdm.multidomain.relationship.ops.impl.DomainDaoImpl;

/**
 *
 * @author davidp
 */
public class DomainService {

    private Connection mConn = null;

    /**
     * Method 'DomainService'
     *
     */
    public DomainService() {
    }

    /**
     * Method 'DomainService'
     *
     */
    public DomainService(Connection conn) {
        this.mConn = conn;
    }

    public void addDomain(Domain domn) throws DaoException {
        new DomainDaoImpl(mConn).insert(domn);
    }

    public Domain[] getDomains() throws DaoException {
        return new DomainDaoImpl(mConn).getDomains();
    }

    public int update(Domain dom) throws DaoException{
        return new DomainDaoImpl(mConn).update(dom);
    }
}
