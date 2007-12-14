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

package com.sun.mdm.index.loader.config;

import java.util.ArrayList;
import java.util.List;

import com.sun.mdm.index.loader.blocker.BlockDefinition;

/**
 * @author Sujit Biswas
 * 
 */
public class BlockDefinitionMerger {

	ArrayList<BlockDefinition> blockDefinitions = new ArrayList<BlockDefinition>();

	ArrayList<BlockDefinitionBucket> buckets = new ArrayList<BlockDefinitionBucket>();

	/**
	 * @param blockDefinitions
	 */
	public BlockDefinitionMerger(ArrayList<BlockDefinition> blockDefinitions) {
		this.blockDefinitions = blockDefinitions;
		init();
	}

	private void init() {

		for (int i = 0; i < blockDefinitions.size(); i++) {
			BlockDefinitionBucket b = new BlockDefinitionBucket();
			b.add(blockDefinitions.get(i));
			buckets.add(b);
		}

	}

	public ArrayList<BlockDefinition> merge() {

		mergeBuckets();

		blockDefinitions = new ArrayList<BlockDefinition>();

		for (int i = 0; i < buckets.size(); i++) {

			BlockDefinitionBucket bc = buckets.get(i);

			if (bc.getDefinitions().size() == 1) {
				blockDefinitions.add(bc.getDefinitions().get(0));
			} else {
				blockDefinitions.add(BlockDefinition.merge(bc.getDefinitions(),
						BlockDefinition.Operator.OR));
			}

		}

		return blockDefinitions;
	}

	private void mergeBuckets() {

		int num = buckets.size();

		if (num <= 1)
			return;

		boolean b = false;

		for (int i = 0; i < buckets.size(); i++) {
			for (int j = i + 1; j < buckets.size(); j++) {
				b = compare(buckets.get(i), buckets.get(j));

				if (b) {

					BlockDefinitionBucket bc = buckets.remove(j);

					buckets.get(i).add(bc.getDefinitions().get(0));

					break;

				}

			}

			if (b)
				break;

		}

		if (b)
			mergeBuckets();
	}

	private boolean compare(BlockDefinitionBucket blockDefinitionBucket,
			BlockDefinitionBucket blockDefinitionBucket2) {
		List<BlockDefinition.Rule> r1 = blockDefinitionBucket.getRules();

		List<BlockDefinition.Rule> r2 = blockDefinitionBucket2.getRules();

		for (int i = 0; i < r1.size(); i++) {

			for (int j = 0; j < r2.size(); j++) {

				String s1 = r1.get(i).getSource();
				String s2 = r2.get(j).getSource();
				if (s1.equals(s2))
					return true;

			}

		}

		return false;

	}

}
