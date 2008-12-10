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
package com.sun.mdm.multidomain.relationship;

import java.io.Serializable;

public class Domain implements Serializable {

    /**
     * This attribute maps to the column DOMAIN_NAME in the DOMAINS table.
     */
    protected String domainName;
    /**
     * This attribute maps to the column CONTEXT_FACTORY in the DOMAINS table.
     */
    protected String contextFactory;
    /**
     * This attribute maps to the column URL in the DOMAINS table.
     */
    protected String url;
    /**
     * This attribute maps to the column PRINCIPAL in the DOMAINS table.
     */
    protected String principal;
    /**
     * This attribute maps to the column CREDENTIAL in the DOMAINS table.
     */
    protected String credential;

    /**
     * Method 'HierarchyDef'
     *
     */
    public Domain() {
    }

    /**
     * Method 'getDomainName'
     *
     * @return String
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * Method 'setDomainName'
     *
     * @param domain
     */
    public void setDomainName(String domain) {
        this.domainName = domain;
    }

    /**
     * Method 'getContextFactory'
     *
     * @return String
     */
    public String getContextFactory() {
        return contextFactory;
    }

    /**
     * Method 'setContextFactory'
     *
     * @param contextFactory
     */
    public void setContextFactory(String contextFactory) {
        this.contextFactory = contextFactory;
    }

    /**
     * Method 'getUrl'
     *
     * @return String
     */
    public String getUrl() {
        return url;
    }

    /**
     * Method 'setUrl'
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Method 'getPrincipal'
     *
     * @return String
     */
    public String getPrincipal() {
        return principal;
    }

    /**
     * Method 'setDescription'
     *
     * @param principal
     */
    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    /**
     * Method 'getCredential'
     *
     * @return String
     */
    public String getCredential() {
        return credential;
    }

    /**
     * Method 'setCredential'
     *
     * @param credential
     */
    public void setCredential(String credential) {
        this.credential = credential;
    }
}
