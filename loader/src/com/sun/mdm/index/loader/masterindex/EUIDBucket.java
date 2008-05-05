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
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.DataObjectReader;
import com.sun.mdm.index.dataobject.DataObjectWriter;




/**. 
 * This is container of many Enterprise Objects - System Records that have
 * same "EUID".
 * It is a unit execution of work and can be loaded in memory by any
 * processor.
 * @author Swaranjit Dua
 *
 */
public class EUIDBucket {
	//	private DataObjectWriter writer;
	private DataObjectReader reader;
	private Map<String, List<DataObject>> EUIDMap;
	private String bucketName_;
	private int numEOs;

	EUIDBucket(DataObjectReader reader, String bucket) {
		this.reader = reader;
		bucketName_ = bucket;
		//	file = f;
	}

	/**
	 * loads the data from the file into a HashMap.
	 * @return
	 * @throws Exception
	 */
	 Map load() throws Exception {
		EUIDMap = new HashMap<String, List<DataObject>>();
		while (true) {
			DataObject dataObject = reader.readDataObject();
			if (dataObject == null) {
				break;
			}

			String euid = dataObject.getFieldValue(0);
			List<DataObject> SOlist = EUIDMap.get(euid);
			if (SOlist == null) {
				SOlist = new ArrayList<DataObject>();
				SOlist.add(dataObject);
				EUIDMap.put(euid, SOlist);
				numEOs++;
			} else {
				SOlist.add(dataObject);
			}

		}
		return EUIDMap;
	}

	void close() throws Exception {

		if (reader != null) {
			reader.close();
		}
	}

	synchronized EOCursor getEOCursor() {
		EOCursor eoCursor = new EOCursor(EUIDMap);
		return eoCursor;
	}

	/**
	 * 
	 * @author Swaranjit Dua
	 * returns pointer to an "EO" object - list of Data Objects that have same EUID.
	 *
	 */
	class EOCursor {

		private Map.Entry<String, List<DataObject>> entry_;
		Iterator<Map.Entry<String, List<DataObject>>> eoIterator_;

		EOCursor(final Map<String, List<DataObject>> map) {

			eoIterator_ = map.entrySet().iterator();
		}

		/**
		 * returns one list of System Objects that have same EUID at a time.
		 * 
		 * @return List of DataObject representing System Objects
		 */
		synchronized List<DataObject> next() {
			if (eoIterator_.hasNext()) {
				entry_ = eoIterator_.next();
				List<DataObject> solist = entry_.getValue();
				return solist;
			}
			return null;

		}
	}

}
