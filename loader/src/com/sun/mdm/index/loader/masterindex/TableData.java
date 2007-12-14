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

package com.sun.mdm.index.loader.masterindex;

import java.util.ArrayList;
import java.util.List;
import com.sun.mdm.index.dataobject.DataObject;

/**
 * @author Swaranjit Dua
 * 
 */
public class TableData {

	private String name;
	private List<List<String>> data = new ArrayList<List<String>>();
	private List<DataObject> dataObjects = new ArrayList<DataObject>();

	/**
	 * @param name
	 * @param columns
	 */
     TableData(String name) {
		this.name = name;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<List<String>> getData() {
		return data;
	}
	
	public List<DataObject> getDataObjects() {
		return dataObjects;
	}

	public void addData(List<String> data) {
		this.data.add(data);
	}
	
	public void addData(DataObject d) {
	     dataObjects.add(d);	
	}

}
