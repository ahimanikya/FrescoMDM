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
package com.sun.mdm.index.loader.common;

import java.util.Date;
import java.text.SimpleDateFormat;


import java.util.List;

import com.sun.mdm.index.dataobject.objectdef.DataObjectAdapter;
import com.sun.mdm.index.dataobject.objectdef.Field;
import com.sun.mdm.index.dataobject.ChildType;

import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.dataobject.DataObject;

import com.sun.mdm.index.dataobject.objectdef.ObjectDefinition;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.loader.config.LoaderConfig;

/**
 *  
    Utility class to convert DataObject/from to ObjectNode.
    In turns call DataObjectAdapter.   
 * @author Swaranjit Dua
 *
 */
   public class ObjectNodeUtil {
	private static LoaderConfig config = LoaderConfig.getInstance();	
	
	private static Date date_ = new Date();	    ;
    private static SimpleDateFormat dateFormat_;  // This format is used in
        // user defined objects per object definition
    
	
	public static ObjectDefinition initDataObjectAdapter() {
		ObjectDefinition objDef = config.getObjectDefinition();
		addIDs(objDef);
		DataObjectAdapter.init(objDef);
		String dateString = objDef.getDateFormat();
		String timeFormat = config.getSystemProperty("TimeFormat");
		if (timeFormat != null) {
			dateString = dateString + " " + timeFormat;
		}
		dateFormat_ = new SimpleDateFormat(dateString); // + " hh:mm:ss");
		DataObjectAdapter.setDateFormat(dateFormat_);
		//objDef_ = objDef;
		return objDef;
	}
	
	public static void initDateFormat() {
		ObjectDefinition objDef = config.getObjectDefinition();
		String dateString = objDef.getDateFormat();
		String timeFormat = config.getSystemProperty("TimeFormat");
		if (timeFormat != null) {
			dateString = dateString + " " + timeFormat;
		}
		dateFormat_ = new SimpleDateFormat(dateString); // + " hh:mm:ss");
	}
	
	
	public static SystemObject getSystemObject(DataObject d, String lid, String systemcode,
			String updateDateTime, String updateUser) throws Exception {
		SystemObject so = new SystemObject();
		so.setSystemCode(systemcode);
		so.setLID(lid);
		so.setStatus(SystemObject.STATUS_ACTIVE);
		String s = updateDateTime;
		if (s != null && !s.equals("")) {
			Date dt = dateFormat_.parse(s);
			so.setUpdateDateTime(dt);
		}
		so.setUpdateUser(updateUser);
				  		  		  
		if (so.getUpdateDateTime() == null) {
            so.setUpdateDateTime(date_);
        }
		  
        if (so.getCreateDateTime() == null) {
           so.setCreateDateTime(so.getUpdateDateTime());
        }
        
        if (so.getCreateUser() == null) {
      	  so.setCreateUser("Loader");
      	  so.setUpdateUser("Loader");
        }
        
        addIdvals(d);
	    ObjectNode o = DataObjectAdapter.toObjectNode(d);		
		  String tag = o.pGetTag();
		  so.addChild(o);
		  so.setChildType(tag);
		
		
		return so;
	}
	
	public static DataObject fromObjectNode(ObjectNode o)throws Exception {
	
	  DataObject data = DataObjectAdapter.fromObjectNode(o);
	   data.remove(0);  // replace ObjectId (such as PersonId) that are not
	    // part of DataObject.
	   deleteChildIDs(data);
	   return data;
					
}

/*
 * remove 1st field from each child. This is invoked after converson
 * from ObjectNode to DataObject. The ObjectNode contains ids such as 
 * AddressId. But object definition does has these Ids. So these must be
 * removed before DataObject is output.
 */
private static void deleteChildIDs(DataObject d) {
	
	List<ChildType> childtypes = d.getChildTypes();
	
	for (ChildType ct: childtypes) {
		List<DataObject> children = ct.getChildren();
		
		for (DataObject c: children) {
			c.remove(0);
		}
	}

}
	
	
	/*
	 * These ids like PersonId is not part of set of fields defined in
	 * object.xml, but are implicitly created when creating Master Index
	 * object schema classes such as PersonObject.
	 */
	private static void addIDs(ObjectDefinition objdef) {
        String name = objdef.getName();
        String idName = name + "Id";
        Field f = new Field();
        f.setName(idName);
        objdef.addField(0, f);
               
        List<ObjectDefinition> children = objdef.getChildren();
        
        for (ObjectDefinition child: children) {
        	addIDs(child);
        }        
	}		
	
	private static void addIdvals(DataObject d) {
		d.add(0, "");
		List<ChildType> childTypes = d.getChildTypes();
		
		for (int i = 0; i < childTypes.size(); i++) {
			if (d.hasChild(i)) {
			  List<DataObject> children =  d.getChildren(i);
			  for (DataObject dobject: children) {
			    addIdvals(dobject);
			  }
			}
		}
	}
   				
}
