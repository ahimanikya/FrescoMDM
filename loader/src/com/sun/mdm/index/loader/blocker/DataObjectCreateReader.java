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

import java.util.ArrayList;
import java.util.List;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.DataObjectReader;
import com.sun.mdm.index.dataobject.InvalidRecordFormat;
import com.sun.mdm.index.dataobject.epath.DOEpath;
import com.sun.mdm.index.dataobject.objectdef.Lookup;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathParser;

/**
 * Test data.
 * ToDO: move to test directory
 * @author sdua
 *
 */
public class DataObjectCreateReader implements DataObjectReader {

	//private ObjectDefinition objectDefinition;
	private Lookup lookup;
	private List<DataObject> dataObjects;
	private EPath eGID;
	private EPath elid;
	private EPath esystemcode;	 
	private EPath eSSN;
	private EPath efirstName;
	private EPath elastName;	 
	private EPath eDOB;
	private EPath eGender;
	private EPath eAddresscity;
	private EPath eAddressstate;
	private EPath eAddresscity2;
	private EPath eAddressstate2;
	private EPath eAliasFnamePhoneticCode;
	private EPath eAliasLnamePhoneticCode;
	private EPath eFnamePhoneticCode;
	private EPath eLnamePhoneticCode;
	private EPath eAddress;
	private EPath eAlias;
	
	private int cur = 0;
	
	DataObjectCreateReader(Lookup lk) throws Exception {
		//objectDefinition = obd;
		lookup = lk;
		createDataObjects();
		
	}
		
	/**
	 * @see com.sun.mdm.index.dataobject.DataObjectReader#close();
	 */	
	public void close() throws Exception {
	}

	/**
	 * @see com.sun.mdm.index.dataobject.DataObjectReader#reset();
	 */	
	public void reset() throws Exception {
	}
	
	public DataObject readDataObject() throws InvalidRecordFormat {
		// TODO Auto-generated method stub
		if (cur == dataObjects.size()) {
			return null;
		}
	     return dataObjects.get(cur++);
	}
	
	
	private void createDataObjects() throws Exception {
		
		eGID = EPathParser.parse("Person.GID");
		elid = EPathParser.parse("Person.lid");	
		esystemcode = EPathParser.parse("Person.systemcode");
		eSSN = EPathParser.parse("Person.SSN");
		efirstName = EPathParser.parse("Person.StdFirstName");	
		elastName = EPathParser.parse("Person.StdLastName");
		eDOB = EPathParser.parse("Person.DOB");
		eGender = EPathParser.parse("Person.Gender");
		eAddresscity = EPathParser.parse("Person.Address[0].City");
		eAddresscity2 = EPathParser.parse("Person.Address[1].City");
		eAddress = EPathParser.parse("Person.Address");
		eAlias = EPathParser.parse("Person.Alias");
		
		eAddressstate = EPathParser.parse("Person.Address[0].State");
		eAddressstate2 = EPathParser.parse("Person.Address[1].State");
				
		eAliasFnamePhoneticCode = EPathParser.parse("Person.Alias[0].FnamePhoneticCode");
		eAliasLnamePhoneticCode = EPathParser.parse("Person.Alias[0].LnamePhoneticCode");;
		eFnamePhoneticCode = EPathParser.parse("Person.FnamePhoneticCode");
		eLnamePhoneticCode = EPathParser.parse("Person.LnamePhoneticCode");
		
	  dataObjects = new ArrayList<DataObject>();
	
	  
	DataObject dobject = createDataObject1();
	dataObjects.add(dobject);
	
	dobject = createDataObject2();
	dataObjects.add(dobject);
	
	dobject = createDataObject3();
	dataObjects.add(dobject);
			
		
	}
	
	private DataObject createDataObject1() throws Exception {
		DataObject dobject = new DataObject();
		
		DOEpath.setFieldValue(eGID, dobject, "1", lookup);
		DOEpath.setFieldValue(elid, dobject, "A1", lookup);
		DOEpath.setFieldValue(esystemcode, dobject,"SystemA" , lookup);		
		DOEpath.setFieldValue(eSSN, dobject, "11111111111", lookup);
		DOEpath.setFieldValue(eDOB, dobject, "121212", lookup);
		DOEpath.setFieldValue(eGender, dobject,"M" , lookup);
		DOEpath.setFieldValue(efirstName, dobject, "John", lookup);
		DOEpath.setFieldValue(elastName, dobject, "Smith", lookup);
		DOEpath.setFieldValue(eFnamePhoneticCode, dobject, "John", lookup);
		DOEpath.setFieldValue(eLnamePhoneticCode, dobject, "Smith", lookup);
		
	
	    return dobject;	
	}
	
	private DataObject createDataObject2() throws Exception {
		DataObject dobject = new DataObject();
		
		DOEpath.setFieldValue(eGID, dobject, "2", lookup);
		DOEpath.setFieldValue(elid, dobject, "B1", lookup);
		DOEpath.setFieldValue(esystemcode, dobject,"SystemB" , lookup);		
		DOEpath.setFieldValue(eSSN, dobject, "222", lookup);
		DOEpath.setFieldValue(eDOB, dobject, "121212", lookup);
		DOEpath.setFieldValue(eGender, dobject,"M" , lookup);
		DOEpath.setFieldValue(efirstName, dobject, "Joe", lookup);
		DOEpath.setFieldValue(elastName, dobject, "Smith", lookup);
		DOEpath.setFieldValue(eFnamePhoneticCode, dobject, "Joe", lookup);
		DOEpath.setFieldValue(eLnamePhoneticCode, dobject, "Smith", lookup);
	
		DataObject dobject2 = new DataObject();
		DOEpath.addDataObject(eAddress, dobject, dobject2, lookup);
		
		DOEpath.setFieldValue(eAddressstate, dobject, "CA", lookup);
		DOEpath.setFieldValue(eAddresscity, dobject, "Monrovia", lookup);
		
		
		
	    return dobject;	
	}
	
	private DataObject createDataObject3() throws Exception {
		DataObject dobject = new DataObject();
		
		DOEpath.setFieldValue(eGID, dobject, "3", lookup);
		DOEpath.setFieldValue(elid, dobject, "B2", lookup);
		DOEpath.setFieldValue(esystemcode, dobject,"SystemB" , lookup);		
		DOEpath.setFieldValue(eSSN, dobject, "33333", lookup);
		DOEpath.setFieldValue(eDOB, dobject, "121212", lookup);
		DOEpath.setFieldValue(eGender, dobject, "F" , lookup);
		DOEpath.setFieldValue(efirstName, dobject, "George", lookup);
		DOEpath.setFieldValue(elastName, dobject, "Smith", lookup);
		DOEpath.setFieldValue(eFnamePhoneticCode, dobject, "John", lookup);
		DOEpath.setFieldValue(eLnamePhoneticCode, dobject, "Smith", lookup);
	
		DataObject dobject2 = new DataObject();
		DOEpath.addDataObject(eAddress, dobject, dobject2, lookup);
		
		DOEpath.setFieldValue(eAddressstate, dobject, "CA", lookup);
		DOEpath.setFieldValue(eAddresscity, dobject, "Monrovia", lookup);
		
		
		DataObject dobject3 = new DataObject();
		DOEpath.addDataObject(eAlias, dobject, dobject3, lookup);
		
		DOEpath.setFieldValue(eAliasFnamePhoneticCode, dobject, "JohnP", lookup);
		DOEpath.setFieldValue(eAliasLnamePhoneticCode, dobject, "SmithP", lookup);
		
		DataObject dobject4 = new DataObject();
		DOEpath.addDataObject(eAddress, dobject, dobject4, lookup);
		
		DOEpath.setFieldValue(eAddressstate2, dobject, "NV", lookup);
		DOEpath.setFieldValue(eAddresscity2, dobject, "Las Vegas", lookup);
		
		
	    return dobject;	
	}
	
	

}
