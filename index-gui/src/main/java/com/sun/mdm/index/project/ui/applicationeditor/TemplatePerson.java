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
package com.sun.mdm.index.project.ui.applicationeditor;

public class TemplatePerson {
    /** Creates a new instance of TemplatePerson
     *
     *@param rootNode current root node of entity tree
     *
     */
    public TemplatePerson(EntityNode rootNode) {
        EntityNode primaryNode;
        EntityTree tree = rootNode.getEntityTree();

        // Add Person Node to the Primary
        primaryNode = TemplateObjects.addSubNodePerson(tree, rootNode);

        // Add Alias Node to the Primary
        TemplateObjects.addSubNodeAlias(tree, primaryNode);

        // Add Address Node to the Primary
        TemplateObjects.addSubNodeAddress(tree, primaryNode);

        // Add Phone Node to the Primary
        TemplateObjects.addSubNodePhone(tree, primaryNode);

        // Add AuxId Node to the Primary
        //TemplateObjects.addSubNodeAuxId(tree, primaryNode);
    }
}
