/*
 * Copyright (c) 2007, Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of Sun Microsystems, Inc. nor the names of its contributors
 *   may be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.sun.mdm.multidomain.parser;

import java.util.ArrayList;

/**
 *
 * @author wee
 */
public class RelationshipType extends LinkType {
    
    private String mDisplayName = null;
    
    private ArrayList<RelationFieldReference> mRelFixedAttrs = new ArrayList<RelationFieldReference>();
    
    private ArrayList<RelationFieldReference> mRelExtendedAttrs = new ArrayList<RelationFieldReference>();

    RelationshipType () {
        super(LinkType.TYPE_RELATIONSHIP);
    }

    public RelationshipType(String relationshipName, String source, String destination, String displayName,
                            ArrayList<RelationFieldReference> fixedFieldRefs, ArrayList<RelationFieldReference> extendedFieldRefs) {
        
        this.name = relationshipName;
        this.targetDomain = destination;
        this.sourceDomain = source;
        this.mDisplayName = displayName;
        this.mRelFixedAttrs = fixedFieldRefs;
        this.mRelExtendedAttrs = extendedFieldRefs;
    }

    
    public String getDisplayName() {
        return this.mDisplayName;
    }

    public String getType() {
        return LinkType.TYPE_RELATIONSHIP;
    }
    
    public ArrayList<RelationFieldReference> getFixedRelFieldRefs() {
        return mRelFixedAttrs;
    }
    public void addFixedRelFieldRef(RelationFieldReference fieldRef) {
        mRelFixedAttrs.add(fieldRef);
    }
    
    public void deleteFixedRelFieldRef(RelationFieldReference fieldRef) {
        for (RelationFieldReference field : mRelFixedAttrs) {
            if (field.getFieldName().equals(fieldRef.getFieldName())){
                mRelFixedAttrs.remove(field);
                break;
            }
        }
    }
   
    public ArrayList<RelationFieldReference> getExtendedRelFieldRefs() {
        return mRelExtendedAttrs;
    }
    
    public void addExtendedRelFieldRef(RelationFieldReference fieldRef) {
        mRelExtendedAttrs.add(fieldRef);
    }
    
    public void deleteExtendedRelFieldRef(RelationFieldReference fieldRef) {
        for (RelationFieldReference field : mRelExtendedAttrs) {
            if (field.getFieldName().equals(fieldRef.getFieldName())){
                mRelExtendedAttrs.remove(field);
                break;
            }
        }
    }    
    
}
