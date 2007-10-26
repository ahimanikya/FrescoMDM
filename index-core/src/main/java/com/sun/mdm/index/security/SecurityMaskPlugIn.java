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
package com.sun.mdm.index.security;

import com.sun.mdm.index.objects.ObjectNode;
import javax.ejb.EJBContext;


/**
 * @author sdua
 */
public interface SecurityMaskPlugIn {
    /**
     * mask or guard the objectNode. It is up to the implementation class to
     * implement the business rule to modify the contents of objectNode. A
     * different implementation class for each SecurityMaskPlugIn can be
     * associated with different previliges of a user. So in this class you
     * don't check for previleges of a user, rather based on the user previlges
     * appropriate implementation class of SecurityMaskPlugIn is invoked by the
     * SecurityAgent.
     *
     * @param objectNode The objectNode whose data can be modified. This can be
     *       any composite objectNode structure.
     * @exception SecurityException error occured
     */
      void maskData(ObjectNode objectNode, EJBContext context)
        throws SecurityException;
      
      /**
       *  these fields values are checked by SecurityMaskPlugIn when masking data.
       *  So the objectNode passed to maskData must have data for such fields.
       *  @return list of fields.
       */
      String[] fieldsToCheck();
}
