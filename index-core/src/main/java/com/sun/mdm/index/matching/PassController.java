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
package com.sun.mdm.index.matching;

import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import java.util.ArrayList;

/**
 * User implementable interface that controls whether to attempt another pass
 * with the next block.
 * @author  aegloff
 * @version $Revision: 1.1 $
 */
public interface PassController {

    /**
     * User implementable method that controls whether to attempt another pass
     * with the next block.
     * This could for example be dependent on what results have been found so 
     * far or on what blocks are remaining.
     *
     * @param inObject the Object to find matches for
     * @param searchOptions the options used for the search. From this it is 
     * possible to tell whether it is a weighted search or a normal match.
     * @param lastResults the ScoreElement results from the last pass of a block.
     * The ScoreElements are NOT sorted by weight at this point
     * @param combinedResults the ScoreElement results from the all previous 
     * passes of a blocks for a findMatch.
     * @param previousBlockIDs the Block IDs that have been evaluated up to this point
     * @param remainingBlockIDs the Block IDs that have not yet been evaluated up to this point
     * The ScoreElements are NOT sorted by weight at this point
     * @return true if it should attempt to do another pass with the next block
     */
    boolean evalAnotherPass(SystemObject inObject, EOSearchOptions searchOptions, 
            ArrayList lastResults, ArrayList combinedResults,
            ArrayList previousBlockIDs, ArrayList remainingBlockIDs); 
    
}
