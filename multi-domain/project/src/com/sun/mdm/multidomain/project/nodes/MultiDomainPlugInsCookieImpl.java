/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.project.nodes;

import org.openide.nodes.Node;

/**
 *
 * @author jlu
 */
public class MultiDomainPlugInsCookieImpl implements Node.Cookie {
    private MultiDomainPlugInsFolderNode mMultiDomainPlugInsFolderNode;

    /**
     * Creates a new MultiDomainPlugInsFolderCookieImpl object.
     *
     * @param MultiDomainPlugInsNode DOCUMENT ME!
     */
    public MultiDomainPlugInsCookieImpl(MultiDomainPlugInsFolderNode multiDomainPlugInsFolderNode) {
        mMultiDomainPlugInsFolderNode = multiDomainPlugInsFolderNode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MultiDomainPlugInsFolderNode getMultiDomainPlugInsFolderNode() {
        return mMultiDomainPlugInsFolderNode;
    }
}
