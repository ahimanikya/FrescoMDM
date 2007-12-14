/**
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
package com.sun.mdm.index.loader.clustersynchronizer;

public interface ClusterState {

	// loader state
	public int BLOCK_DISTRIBUTION = 0;

	public int MATCHING = 1;

	public int EUID_ASSIGNED = 2;

	public int MASTER_INDEX_GENERATE = 3;

	public int POT_DUPLICATE_BLOCK = 4;

	public int POT_DUPLICATE_MATCH = 5;

	public int MASTER_INDEX_LOAD = 7;

	public int BULK_LOADING_DONE = 8;

	public int ANALYSIS_READY = 1;

	public int ANALYSIS_DONE = 1;

}
