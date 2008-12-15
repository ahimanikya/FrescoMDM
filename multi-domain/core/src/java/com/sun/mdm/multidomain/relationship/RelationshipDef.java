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

import com.sun.mdm.multidomain.attributes.AttributesDef;
import java.io.Serializable;

/**
 * RelationshipDef class.
 * @author SwaranjitDua
 */
public class RelationshipDef extends AttributesDef implements Serializable {

    public static enum DirectionMode {

        UNIDIRECTIONAL(0), BIDIRECTIONAL(1),;
        private final int intValue;

        DirectionMode(int val) {
            this.intValue = val;
        }

        /**
         * Return true if it's BIDIRECTIONAL
         * @return boolean
         */
        public boolean IsBidirectional() {
            return (intValue == 1 ? true : false);

        }
    };
    public static int UNIDIRECTIONAL = 0;
    public static int BIDIRECTIONAL = 1;
    private String sourceDomain;
    private String targetDomain;
    private DirectionMode direction;
    private long id;
 
    /**
     * Create an instance of RelationshipType. 
     */
    public RelationshipDef() {
    }

    /**
     * Create an instance of RelationshipType.
     * @param name Relationship type name. 
     * @param displayName Relationship type display name.
     * @param id Relationship def Id.
     */
    public RelationshipDef(String name, long id) {
        super(name);
        this.id = id;

    }

    /**
     * Create an instance of RelationshipType.
     * @param name Relationship type name.
     * @param sourceDomainName  Name of the source domain.
     * @param targetDomainName  Name of the target domain.
     * @param id Relationship def Id.
     * @param dir  Relationship direction.
     */
    public RelationshipDef(String name, String sourceDomainName,
            String targetDomainName, int id,
            int dir) {
        super(name);
        this.id = id;
        sourceDomain = sourceDomainName;
        targetDomain = targetDomainName;
        if (dir == UNIDIRECTIONAL) {
            direction = DirectionMode.UNIDIRECTIONAL;
        } else if (dir == BIDIRECTIONAL) {
            direction = DirectionMode.BIDIRECTIONAL;
        }
    }

    /**
     * Get RelationshipDefinition ID
     * @return long RelationshipDef ID
     */
    public long getId() {
        return id;
    }

    /**
     * Set Relationship Definition ID
     * @param id RelationshipDef ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get Relationship type source domain.
     * @return String Relationship type source domain.
     */
    public String getSourceDomain() {
        return sourceDomain;
    }

    /**
     * Set relationship type source domain.
     * @param sourceDomain Relationship type source domain.
     */
    public void setSourceDomain(String sourceDomain) {
        this.sourceDomain = sourceDomain;
    }

    /**
     * Get relationship type target domain.
     * @return String Relationship type target domain.
     */
    public String getTargetDomain() {
        return targetDomain;
    }

    /**
     * Set relationship type target domain.
     * @param targetDomain Relationship type target domain.
     */
    public void setTargetDomain(String targetDomain) {
        this.targetDomain = targetDomain;
    }

    /**
     * Get relationship type direction.
     * @return DirectionMode Relationship type direction.
     */
    public DirectionMode getDirection() {
        return direction;
    }

    /**
     * Set relationship type direction.
     * @param direction Relationship type direction.
     */
    public void setDirection(DirectionMode direction) {
        this.direction = direction;
    }

    /**
     * Copy RelationshipType.
     * @param type RelationshipType.
     */
    public void copy(RelationshipDef type) {

        this.sourceDomain = type.getSourceDomain();
        this.targetDomain = type.getTargetDomain();

        this.direction = type.getDirection();

    }
}
