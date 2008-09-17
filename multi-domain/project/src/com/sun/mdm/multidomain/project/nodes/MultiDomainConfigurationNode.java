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
package com.sun.mdm.multidomain.project.nodes;

import org.openide.actions.OpenAction;

import org.openide.loaders.DataObject;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.netbeans.api.project.Project;
import org.openide.util.actions.SystemAction;

import javax.swing.Action;
//import com.sun.mdm.index.util.Logger;
import com.sun.mdm.multidomain.util.Logger;

public class MultiDomainConfigurationNode extends DataNode {
    private static final Logger mLog = Logger.getLogger(
            MultiDomainConfigurationNode.class.getName()
        
        );
    private MultiDomainConfigurationDataObject mObj;
    private Project project;

    /**
     * Creates a new MultiDomainConfigurationNode object.
     *
     * @param obj DOCUMENT ME!
     */
    public MultiDomainConfigurationNode(MultiDomainConfigurationDataObject obj) {
        this(obj, Children.LEAF);
    }

    /**
     * Creates a new MultiDomainConfigurationNode object.
     *
     * @param obj DOCUMENT ME!
     * @param ch DOCUMENT ME!
     */
    public MultiDomainConfigurationNode(DataObject obj, Children ch) {
        super(obj, ch);
        mObj = (MultiDomainConfigurationDataObject) obj;
        this.setIconBaseWithExtension("com/sun/mdm/multidomain/project/resources/eviewFileNode.png");
        this.getCookieSet().add(new MultiDomainConfigurationCookieImpl(this));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Action getPreferredAction() {
        return SystemAction.get(OpenAction.class);
    }
}
