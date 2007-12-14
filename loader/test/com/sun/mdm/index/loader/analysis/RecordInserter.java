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

package com.sun.mdm.index.loader.analysis;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author Sujit Biswas
 * 
 */
public class RecordInserter implements Callable<Boolean> {

	int count;
	WeightAnalyzer analyser;
	ArrayList<String> fields;

	public RecordInserter(int count, WeightAnalyzer analyser,
			ArrayList<String> fields) {
		this.analyser = analyser;
		this.count = count;
		this.fields = fields;
	}

	
	public Boolean call() throws Exception {
		insertRecords(count, analyser, fields);
		return true;
	}

	public void insertRecords(int count, WeightAnalyzer analyser,
			ArrayList<String> fields) {
		for (int j = 0; j < count; j++) {

			ArrayList<String> object1 = new ArrayList<String>();
			ArrayList<String> object2 = new ArrayList<String>();
			ArrayList<Double> weights = new ArrayList<Double>();

			object1.add("syscode" + "-1-" + j);
			object2.add("syscode" + "-2-" + j);

			object1.add("lid" + "-1-" + j);
			object2.add("lid" + "-2-" + j);

			weights.add(50.00 + j);
			weights.add(0.0);

			for (int i = 0; i < fields.size(); i++) {

				String field1 = fields.get(i) + "-1-" + j;

				String field2 = fields.get(i) + "-2-" + j;

				if (field1.contains("FirstName")) {
					field1 = "John-1";
					field2 = "John-2";

				}

				if (field1.contains("LastName")) {
					field1 = "Doe-1";
					field2 = "Smith-2";

				}

				if (field1.contains("SSN")) {
					field1 = "999-99-1234";
					field2 = "999-99-4576";

				}

				if (field1.contains("DOB")) {
					DateFormat df = DateFormat
							.getDateInstance(DateFormat.MEDIUM);

					field1 = df.format(new Date());
					field2 = df.format(new Date());

				}

				if (field1.contains("Gender")) {
					field1 = "Male";
					field2 = "Female";

				}

				object1.add(field1);
				object2.add(field2);

				weights.add(10.00 + j);
			}

			analyser.insert(object1, object2, weights);
		}
	}

}
