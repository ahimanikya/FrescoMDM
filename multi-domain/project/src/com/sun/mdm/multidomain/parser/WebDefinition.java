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
public class WebDefinition extends Definition {
    
    private String mDisplayName = null;
    
    WebDefinition () {
        super(Definition.TYPE_RELATIONSHIP);
    }

    public WebDefinition(String relationshipName, String source, String destination, String displayName,
                            ArrayList<RelationFieldReference> fixedFieldRefs, ArrayList<RelationFieldReference> extendedFieldRefs) {
        
        this.name = relationshipName;
        this.targetDomain = destination;
        this.sourceDomain = source;
        this.mDisplayName = displayName;
        this.mRelPredefinedAttrs = fixedFieldRefs;
        this.mRelExtendedAttrs = extendedFieldRefs;
    }


    public WebDefinition createCopy() {
        WebDefinition definition = new WebDefinition();
        definition.name = "Copy" + this.name;
        definition.sourceDomain = this.sourceDomain;
        definition.targetDomain = this.targetDomain;
        definition.displayName = this.displayName;
        definition.mRelPredefinedAttrs = this.mRelPredefinedAttrs;
        definition.mRelExtendedAttrs = this.mRelExtendedAttrs;
        return definition;
    }
    
    public String getDisplayName() {
        return this.mDisplayName;
    }

    public String getType() {
        return Definition.TYPE_RELATIONSHIP;
    }
    
    
}