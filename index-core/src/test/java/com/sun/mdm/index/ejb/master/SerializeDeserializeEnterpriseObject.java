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
import com.sun.mdm.index.ejb.master.helper.MergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.MergeSystemObjectHelper;
import com.sun.mdm.index.ejb.master.helper.LookupTransactionsHelper;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.SystemObject;
import java.util.List;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import java.util.Date;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

/** Test class for Person searches as defined in the
* eIndex50.xml file
* @author Xi Song
*/
public class SerializeDeserializeEnterpriseObject extends TestCase {
   
   /** Creates new SearchEOTester
    * @see junit.framework.TestCase#
    * @param name required by JUnit
    */
   public SerializeDeserializeEnterpriseObject(String name) {
       super(name);
   }
   
   /** Tests an EO merge 
    * @throws Exception an error occured
    */
   public void testSerializeDeserializeEnterpriseObject() throws Exception {
       // Create EO 1 and 2       
       CreateEnterpriseObjectHelper helper = new CreateEnterpriseObjectHelper();
       helper.clearDb();
       
       List eoList = helper.run(new String[] {"fileName=SerializedEnterpriseObjectVersion1.txt", "fileType=generic"});        

       EnterpriseObject eo = (EnterpriseObject) eoList.get(0);
       
       assertTrue(eo != null );

       SystemObject so = (SystemObject)eo.getSystemObject("SiteA", "0001");

       assertTrue(so != null );
       
       PersonObject personObject = (PersonObject)so.getChild("Person",0);
       
       assertTrue(personObject != null );
       		
       SystemObject so2 = (SystemObject)personObject.getParent();

       assertTrue(so2 != null );
       
       String lid = so2.getLID();

       assertTrue(lid != null );
       
       writeObject(eo);        
       
       EnterpriseObject eo2 = (EnterpriseObject)readObject();

       assertTrue(eo2 != null );
       
       PersonObject po2 = (PersonObject)((SystemObject)eo2.getSystemObject("SiteA", "0001")).getChild("Person",0);

       assertTrue(po2 != null );

       SystemObject so3 = (SystemObject)po2.getParent();
       
       assertTrue(so3 != null );

       String lid2 = so3.getLID();

       assertTrue(lid2 != null );
       
   }

   public void writeObject(Object object) throws Exception {

      ObjectOutputStream objectOut = null;
      
      try{
         Properties props = System.getProperties();
         String basedir= props.getProperty("basedir");
         File outfile= new File(basedir,"target/test-classes/setest.ser");
         OutputStream out = new FileOutputStream(outfile);
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

   public Object readObject()throws Exception {
   	Object object = null;
   	try{
   	    Properties props = System.getProperties();
        String basedir= props.getProperty("basedir");
        File infile= new File(basedir,"target/test-classes/setest.ser");
       	InputStream in = new FileInputStream(infile);
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
       junit.textui.TestRunner.run(new TestSuite(SerializeDeserializeEnterpriseObject.class));
   }
   
}
