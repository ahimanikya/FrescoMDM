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
package com.sun.mdm.index.ejb.master;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.GetEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.ejb.master.helper.MergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.MergeSystemObjectHelper;
import com.sun.mdm.index.ejb.master.helper.LookupTransactionsHelper;
import com.sun.mdm.index.objects.AddressObject;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SystemObjectPK;

import java.util.Collection;
import java.util.List;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import java.util.Date;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

/** Test class for Person searches as defined in the
* eIndex50.xml file
* @author Sanjay Sharma
*/
public class ReadSerializeEnterpriseObjectFromVersion1 extends TestCase {
   
   /** Creates new SearchEOTester
    * @see junit.framework.TestCase#
    * @param name required by JUnit
    */
   public ReadSerializeEnterpriseObjectFromVersion1(String name) {
       super(name);
   }
   
   /** Tests an EO merge 
    * @throws Exception an error occured
    */
   public void testReadSerializeEO() throws Exception {

   	   EnterpriseObject eo = (EnterpriseObject)readObject("SerializedEnterpriseObjectVersion1.ser", true);

       assertTrue(eo != null );

       SystemObject so = (SystemObject)eo.getSystemObject("SiteA", "0001");
       
       assertTrue(so != null );

       PersonObject personObject = (PersonObject)so.getChild("Person",0);

       assertTrue(personObject != null );

       SystemObject so2 = (SystemObject)personObject.getParent();

       assertTrue(so2 != null );
       
       String lid = so2.getLID();

       assertTrue(lid != null );

       writeObject(eo, "c:/temp/test.ser");        
       
       EnterpriseObject eo2 = (EnterpriseObject)readObject("c:/temp/test.ser", false);
       
       assertTrue(eo2 != null );

       PersonObject po2 = (PersonObject)((SystemObject)eo2.getSystemObject("SiteA", "0001")).getChild("Person",0);

       assertTrue(po2 != null );

       SystemObject so3 = (SystemObject)po2.getParent();

       assertTrue(so3 != null );
       
       String lid2 = so3.getLID();
       
       assertTrue(lid2 != null );
       
   }   
   
   public void writeObject(Object object, String serializedEOFileName) throws Exception {

      ObjectOutputStream objectOut = null;
      
      try{
         OutputStream out = new FileOutputStream(serializedEOFileName);
         OutputStream outBuffer = new BufferedOutputStream(out);
         objectOut = new ObjectOutputStream(outBuffer);
         objectOut.writeObject(object);

      } catch(Exception e){
      		e.printStackTrace();
      } finally {
      	    
      		if(objectOut != null) {
      			objectOut.close();
      		}
      }       
   }

   public Object readObject(String serializedEOFileName, boolean readAsResource )throws Exception {
   	Object object = null;
   	try{
   		InputStream in = null;
   		if ( readAsResource ) {
   			in = this.getClass().getClassLoader().getResourceAsStream(serializedEOFileName);
   		}
   		else {
   			in = new FileInputStream(serializedEOFileName);
   		}
       	InputStream inBuffer = new BufferedInputStream(in);
       	ObjectInputStream objectIn = new ObjectInputStream(inBuffer);
       	object = objectIn.readObject();
       } catch(Exception e) { 
       	e.printStackTrace();
       }
   	return object;
   }
   
   private void log(String msg) {
       System.out.println(msg);
   }
   
   /** Main entry point
    * @param args args
    */
   public static void main(String[] args) {
       junit.textui.TestRunner.run(new TestSuite(ReadSerializeEnterpriseObjectFromVersion1.class));
   }
   
}
