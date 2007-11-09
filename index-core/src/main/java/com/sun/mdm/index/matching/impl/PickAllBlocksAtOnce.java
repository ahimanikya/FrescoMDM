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
package com.sun.mdm.index.matching.impl;

import com.sun.mdm.index.matching.BlockPicker;
import com.sun.mdm.index.matching.NoBlockApplicableException;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import java.util.ArrayList;

/**
 * A user extensible component to pick the next block definition(s) to use for 
 * the next matching pass.
 * This simple implementation picks the all remaining block definitions
 * from the remaining block IDs list.
 * @author  aegloff
 * @version $Revision: 1.1 $
 */
public class PickAllBlocksAtOnce implements BlockPicker {

    
    /** Creates new SerialBlockPicker */
    public PickAllBlocksAtOnce() {
    }

    /**
     * Pick all the remaining, applicable defined blocks at once to be executed 
     * using UNION in the next pass.
     *
     * @param inObject the SystemObject to match
     * @param searchOptions the options used for the search. From this it is 
     * possible to tell whether it is a weighted search or a normal match.
     * @param previousBlockIDs the list of block IDs that have already been used
     * in previous match passes for matching this SystemObject
     * @param remainingBlockIDs a list of remaining block IDs that have not yet
     * been used in previous match passes for matching this SystemObject
     * @throws NoBlockApplicableException if no block IDs remain that are applicable
     * This signals the MatchEngineController that matching for this SystemObject 
     * is complete
     * @return the blockID to use for the next match pass
     */
    public String[] pickBlock(SystemObject inObject, EOSearchOptions searchOptions, 
            ArrayList previousBlockIDs, ArrayList remainingBlockIDs) 
            throws NoBlockApplicableException {
        String[] blockIDs = null;
        
        int noOfRemainingIDs = remainingBlockIDs.size();
        if (noOfRemainingIDs > 0) {
            blockIDs = (String[]) remainingBlockIDs.toArray(new String[noOfRemainingIDs]);
        } else {
            throw new NoBlockApplicableException("Unable to pick a block: the list of remaining block IDs is empty.");
        }
        
        return blockIDs;
    }
}
