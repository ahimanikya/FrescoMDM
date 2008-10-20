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
package com.sun.mdm.multidomain.services.core;

import java.util.List;
import java.util.ArrayList;
  
import com.sun.mdm.index.objects.ObjectNode;

import com.sun.mdm.multidomain.relationship.MultiObject;
import com.sun.mdm.multidomain.query.PageIterator;

import com.sun.mdm.multidomain.services.model.ObjectView;
import com.sun.mdm.multidomain.services.model.ObjectRecord;
import com.sun.mdm.multidomain.services.relationship.RelationshipView;
import com.sun.mdm.multidomain.services.relationship.RelationshipComposite;

/**
 * ViewHelper class.
 * @author cye
 */
public class ViewHelper {

    public static List<RelationshipView> buildRelationshipView(PageIterator<MultiObject> pages) {      
        List<RelationshipView> relationships = new ArrayList<RelationshipView>();
        // ToDo
        return relationships;
    }
    
    public static RelationshipComposite buildRelationshipComposite(MultiObject relationshipObject) {
        RelationshipComposite relationshipComposite = new RelationshipComposite();
        // ToDo
        return relationshipComposite;
    }
    
    public static List<ObjectView> buildObjectView(PageIterator<ObjectNode> pages){
        List<ObjectView> objects = new ArrayList<ObjectView>();
        // ToDo
        return objects;
    } 
    public static ObjectRecord buildObjectRecord(ObjectNode objectNode) {
        ObjectRecord objectRecord = new ObjectRecord();
        // ToDo
        return objectRecord;
    }
}
