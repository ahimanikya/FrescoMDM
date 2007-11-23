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
package com.sun.mdm.index.filter;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.survivor.SystemFieldListMap;

/**
 * The <b>ExclusionFilterService</b>  is the interface which 
 * contains primary functions of the  exclusion filters.
 * <UL>
 * <LI>exclusionSystemFieldList
 * <LI> blockingExclusion
 * <LI>exclusionMatchField
 * <UL>
 * <p>
 * 
 */
public interface ExclusionFilterService {

    /**This method checks the data available  for the SBR calculation  is 
     * present in the exclusion list. If it is present in the exclusion list then
     * ignore that SystemField for the survivor calculation. 
     * @param SystemFieldListMap 
     * @param  candidateId
     * @return SystemFieldListMap.
    */
    public SystemFieldListMap exclusionSystemFieldList(SystemFieldListMap sysFields,
            String candidateId);

    /**This method checks  the data available  for the blocking process 
     * is present in the exclusion list. If it is present in the exclusion list
     * then ignore that field  for the Blocking queries.
     * @param  ObjectNode
     * @return  ObjectNode.
     */
    public ObjectNode blockingExclusion(ObjectNode objToblock) ;

    /**This method checks  the data available  for the matching process 
     * is present in the exclusion list. If it is present in the exclusion list
     * then ignore that field  for the Blocking queries.
     * @param   ObjectNode
     * @return  ObjectNode.
       */
    public ObjectNode exclusionMatchField(ObjectNode objTomatch) ;
    
    
    
}
