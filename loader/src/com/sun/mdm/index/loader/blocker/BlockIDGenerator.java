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
package com.sun.mdm.index.loader.blocker;
import java.util.List;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.epath.DOEpath;
import com.sun.mdm.index.dataobject.objectdef.Lookup;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.loader.common.LoaderException;

/**
 * A user call back that generates a blockId from a given DataObject.
 * Bulk Matcher will match all records among each other which compute same blockid value.
 * 
 * @author Swaranjit Dua
 *
 */
public interface BlockIDGenerator {
	/**
	 *  computes Block Id from input data
	 * @param field EPath for a field on which this method is invoked.
	 * This is specified in block configuration file.
	 * @param data input root data object. (Can have child objects).
	 * @param l Lookup that has meta data information about data.
	 * 
	 * @return List<String> of blockIds.
	 * @throws LoaderException
	 * @see DOEpath
	 */
	
    List<String> computeBlockID(EPath field, DataObject data, Lookup l) throws LoaderException;
}
