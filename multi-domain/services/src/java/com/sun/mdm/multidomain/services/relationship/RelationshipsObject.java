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
package com.sun.mdm.multidomain.services.relationship;

import java.util.List;
import java.util.ArrayList;
        
/**
 * RelationshipsObject class.
 * @author cye
 */
public class RelationshipsObject {

   private RelationshipDefView relationshipDefView;
   private List<RelationshipView> relationshipViews;
    
   public RelationshipsObject(){
       relationshipViews = new ArrayList<RelationshipView>();
   }
   
   public RelationshipsObject(RelationshipDefView relationshipDefView){
       relationshipViews = new ArrayList<RelationshipView>();
       this.relationshipDefView = relationshipDefView;
   }
      
   public void setRelationshipDefView(RelationshipDefView relationshipDefView) {
        this.relationshipDefView = relationshipDefView;
   }
   
   public RelationshipDefView getRelationshipDefView() {
       return this.relationshipDefView;
   }
   
   public void setRelationshipViews(List<RelationshipView> relationshipViews) {
        this.relationshipViews = relationshipViews;
   }
  
   public List<RelationshipView> getRelationshipViews() {
        return this.relationshipViews;
   }
      
   public void setRelationshipView(RelationshipView relationshipView) {
        relationshipViews.add(relationshipView);
   }
   
   public RelationshipView getRelationshipView(String relationshipId) {
       RelationshipView relationshipView = null;
       for(RelationshipView rView : relationshipViews) {
           if(relationshipId != null && relationshipId.equals(rView.getId())) {
                relationshipView = rView;
           }
       }
       return relationshipView;
   }
}
