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
package com.sun.mdm.index.loader.matcher;

/**
 * Position of record within a Block.
 * A thread will match a Record with every other record in the same block,
 * from recordPostion +1 until end of block
 * @author Swaranjit Dua
 *
 */
class BlockPosition {
	/**
	 * block that this BlockPosition refers to 
	 */
	private Block block;
	/*
	 * position within Block that this BlockPosition refers to
	 */
	private int recordPosition;

	BlockPosition(Block block, int recordPosition) {
		this.block = block;
		this.recordPosition = recordPosition;
	}

	Block getBlock() {
		return block;
	}

	int getRecordPosition() {
		return recordPosition;
	}

}
