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
package com.sun.mdm.index.query;


import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
* @author Sanjay.Sharma
*/
public class ORAndBindingEnhancementsTest extends TestCase {
   private static QueryManager mQueryManager;

   /**
    * Creates a new instance of QueryManagerEJB
    *
    * @param name name of test
    */
   public ORAndBindingEnhancementsTest(String name) {
       super(name);
   }
   
   /**
    * setUp
    *@exception Exception error occurs
    **/
   
   protected void setUp() throws Exception {
    
          if (mQueryManager == null) {
        	ClearDb.run();
            CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
            createHelper.run(new String[] {"fileName=SearchEnterpriseObject1.txt", "fileType=eiEvent"});       
            mQueryManager = QueryManagerFactory.getInstance();
        
          }
    }
   
   /**
    * Used by JUnit (called after each test method)
    */
   protected void tearDown() {
       //mQueryManager = null;
   }

   /** main method
    * @param args input arguements
    * @exception Exception error occurs
    *
    */
   public static void main(String[] args) throws Exception {
       junit.textui.TestRunner.run(new TestSuite(QueryManagerTest.class));
       
        /*
   	   ORAndBindingEnhancementsTest qtest = new ORAndBindingEnhancementsTest("testPrepare2");
       qtest.setUp();
       
       long time1 = System.currentTimeMillis();
       qtest.testEmptyConditions01();
       qtest.testEmptyConditions02();
       qtest.testINClause();
       qtest.testINClauseCache();
       qtest.testOptimizeExtraJoin();
       qtest.testPrepare10();
       qtest.testPrepare11();
       qtest.testPrepare12();
       qtest.testPrepare2();
       qtest.testPrepare21();
       qtest.testPrepare3();
       qtest.testPrepare4();
       qtest.testPrepare5();
       qtest.testPrepare5AddCondition();
       qtest.testPrepare9();
       qtest.testPrepareA();
       qtest.testPrepareB();
       qtest.testPrepareC();
       qtest.testPrepareCache();
       qtest.testPrepareD();
       qtest.testPrepareJoinTypes();
       qtest.testPrepareMutipleObjects();
       qtest.testPrepareUnion();
       qtest.testSingleObjectNode2();
       qtest.testTuple();
       qtest.testTupleAssembler();
       qtest.testWithAddConditions();
       qtest.testWithoutConditions();
       qtest.testWithoutJoin();
       qtest.testWithoutWhere();        
       
       long time2 = System.currentTimeMillis();
       long time = time2 - time1;
       System.out.println("time:" + time);
       */
   }

   // This tests the ObjectNodeFactory for Person & Address Object.
   // This would be used in ETD

   /**
    * test In clause
    *
    * @exception Exception error occurs
    */
   public void testINClause() throws Exception {
       System.out.println("<<<<<<<<<<<<<<<<<<<testINClause>>>>>>>>>>>>>>>>>>>>");
       

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",            
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition[][] conditions = new Condition[][] {
               {
                   new Condition("Enterprise.SystemSBR.Person.LastName",
                       "LIKE", "Ka%",true)
               },
               {
                  new Condition("Enterprise.SystemSBR.Person.Address.City",
                       "IN", new String[] {
                                "Irvine" , 
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",                                 
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",
                                "Monrovia",                                                                                            
                                "Monrovia"                                                                                                                           
                  },true)
               }
           };
       qo.setCondition(conditions);

       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
   }

   /**
    * Test IN clause in cache
    *
    * @exception Exception error occurs
    */
   public void testINClauseCache() throws Exception {
       System.out.println("testINClauseCache");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",        
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition[][] conditions = new Condition[][] {
               {
                   new Condition("Enterprise.SystemSBR.Person.LastName",
                       "LIKE", "KA%", true)
               },
               {
                   new Condition("Enterprise.SystemSBR.Person.Address.City",
                       "IN", new String[] {"Arcadia"},true)
               }
           };
       qo.setCondition(conditions);

       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }

       String[] fields2 = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",        
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo2 = new QueryObject();
       qo2.setSelect(fields2);

       Condition[][] conditions2 = new Condition[][] {
               {
                   new Condition("Enterprise.SystemSBR.Person.LastName",
                       "LIKE", "KA%", true)
               },
               {
                   new Condition("Enterprise.SystemSBR.Person.Address.City",
                       "IN", new String[] {"Monrovia"},true)
               }
           };
       qo2.setCondition(conditions2);

       AssembleDescriptor assdesc2 = new AssembleDescriptor();
       qo2.setAssembleDescriptor(assdesc2);
       assdesc2.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator2 = mQueryManager.executeAssemble(qo2);

       while (iterator2.hasNext()) {
           Object object2 = iterator2.next();
           System.out.println(object2.toString());
       }        
   }

   public void testWithAddConditions() throws Exception {
       System.out.println("testWithAddConditions");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition[] conditions = new Condition[] {
               
                   new Condition("Enterprise.SystemSBR.Person.Address.City",
                       "LIKE", "Ir%", false),            
                   new Condition("Enterprise.SystemSBR.Person.LastName",
                       "LIKE", "Ka%", false)
           };
           
       qo.setCondition(conditions);
       qo.addCondition("Enterprise.SystemSBR.Person.Address.StateCode",
                       "LIKE", "CA%");
       
       //qo.setRootObject("Enterprise.SystemSBR.Person");
       // System.out.println("QO:" + qo.toString());
       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       //        assdesc.setResultValueObjectType(new ObjectNodeMetaNode("Enterprise.SystemSBR.Person"));
       //queryManager.prepare(qo);
       QMIterator iterator = mQueryManager.executeAssemble(qo);

       if ( iterator != null )  {
           while (iterator.hasNext()) {
               Object object = iterator.next();
               System.out.println(object.toString());
           }
       }
   }
   
   
   public void testEmptyConditions01() throws Exception {
       System.out.println("testEmptyConditions01");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition[] conditions = new Condition[] {
               
                   new Condition("Enterprise.SystemSBR.Person.Address.City",
                       "LIKE", "Ir%", false),            
                   new Condition("Enterprise.SystemSBR.Person.LastName",
                       "LIKE", "Ka%", false),
                   new Condition("Enterprise.SystemSBR.Person.Address.StateCode",
                       "LIKE", "CA%", true)           
           };
           
       qo.setCondition(new Condition[0]);    

       //qo.setRootObject("Enterprise.SystemSBR.Person");
       // System.out.println("QO:" + qo.toString());
       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       //        assdesc.setResultValueObjectType(new ObjectNodeMetaNode("Enterprise.SystemSBR.Person"));
       //queryManager.prepare(qo);
       QMIterator iterator = mQueryManager.executeAssemble(qo);

       if ( iterator != null )  {
           while (iterator.hasNext()) {
               Object object = iterator.next();
               System.out.println(object.toString());
           }
           //iterator.close();
       }
   }

   public void testEmptyConditions02() throws Exception {
       System.out.println("testEmptyConditions02");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition[] conditions = new Condition[] {
               
                   new Condition("Enterprise.SystemSBR.Person.Address.City",
                       "LIKE", "Ir%", false),            
                   new Condition("Enterprise.SystemSBR.Person.LastName",
                       "LIKE", "Ka%", false),
                   new Condition("Enterprise.SystemSBR.Person.Address.StateCode",
                       "LIKE", "CA%", true)           
           };
           
       qo.setCondition(new Condition[0][]);    
       
       //qo.setRootObject("Enterprise.SystemSBR.Person");
       // System.out.println("QO:" + qo.toString());
       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       //        assdesc.setResultValueObjectType(new ObjectNodeMetaNode("Enterprise.SystemSBR.Person"));
       //queryManager.prepare(qo);
       QMIterator iterator = mQueryManager.executeAssemble(qo);

       if ( iterator != null )  {
           while (iterator.hasNext()) {
               Object object = iterator.next();
               System.out.println(object.toString());
           }
           //iterator.close();
       }
   }
   
   public void testOptimizeExtraJoin() throws Exception {
       System.out.println("testOptimizeExtraJoin");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition[] conditions = new Condition[] {
               
                   new Condition("Enterprise.SystemSBR.Person.Address.City",
                       "LIKE", "Ir%", false),            
                   new Condition("Enterprise.SystemSBR.Person.LastName",
                       "LIKE", "Ka%", false),
                   new Condition("Enterprise.SystemSBR.Person.Address.StateCode",
                       "LIKE", "CA%", true)           
           };
           
       qo.setCondition(conditions);

       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator = mQueryManager.executeAssemble(qo);

       if ( iterator != null )  {
           while (iterator.hasNext()) {
               Object object = iterator.next();
               System.out.println(object.toString());
           }
           //iterator.close();
       }
   }    
   
   public void testDateRange() throws Exception {
       System.out.println("testDateRange");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition condRangeFrom  = new Condition( "Enterprise.SystemSBR.Person.LastName", ">=", "20040704" );
       Condition condRangeTo  = new Condition("Enterprise.SystemSBR.Person.LastName","<=", "20040809");

       condRangeFrom.addConditions("AND", condRangeTo);

       Condition condRoot = new Condition(condRangeFrom);

       Condition condOr1 = new Condition( "Enterprise.SystemSBR.Person.LastName", "=", "200407" );
       Condition condOr2 = new Condition( "Enterprise.SystemSBR.Person.LastName", "=", "2004" );
 
       condRoot.addConditions("OR",condOr1);
       condRoot.addConditions("OR",condOr2);

       Condition[] conditions = new Condition[] { condRoot };

       qo.setCondition(conditions);

       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator = mQueryManager.executeAssemble(qo);

       if ( iterator != null )  {
           while (iterator.hasNext()) {
               Object object = iterator.next();
               System.out.println(object.toString());
           }
           iterator.close();
       }
   }    
   
   public void testPrepareUnion() throws Exception {
       System.out.println("testPrepareUnion");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition[][] conditions = new Condition[][] {
               {
                   new Condition("Enterprise.SystemSBR.Person.Address.City","LIKE", "IR%", true),            
                   new Condition("Enterprise.SystemSBR.Person.LastName","IN", "KA%", true)
               },
               {
                   new Condition("Enterprise.SystemSBR.Person.Phone.PhoneType","LIKE", "Home%", true),
                   new Condition("Enterprise.SystemSBR.Person.Address.StateCode","LIKE", "CA%", false)           
               }
           };
    
       qo.setCondition(conditions);

       //qo.setRootObject("Enterprise.SystemSBR.Person");
       // System.out.println("QO:" + qo.toString());
       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       //        assdesc.setResultValueObjectType(new ObjectNodeMetaNode("Enterprise.SystemSBR.Person"));
       //queryManager.prepare(qo);
       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
       //iterator.close();
   }

  public void testPrepareUnion01() throws Exception {
       System.out.println("testPrepareUnionSanjay");

      String[] fields = {
           
           "Enterprise.SystemSBR.Person.SSN",
           "Enterprise.SystemSBR.Person.LastName",
           
           "Enterprise.SystemSBR.Person.Phone.Phone"
       };
       QueryObject qo = new QueryObject();
       qo.setSelect(fields);
       Condition[][] conditions = new Condition[][] {
               {
                   new Condition("Enterprise.SystemSBR.Person.FirstName",
                       "LIKE", "CA%", true)
               },
               {
                   new Condition("Enterprise.SystemSBR.Person.Address.City",
                       "LIKE", "M%",true)
               }
           };
       qo.setCondition(conditions);
       qo.setQueryOption(qo.SINGLE_QUERY);

       
       QueryResults qresults = mQueryManager.execute(qo);

       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       assdesc.setAssembler(new TupleAssembler());
       qresults.setAssembleDescriptor(assdesc);

       QMIterator iterator = qresults.assemble();

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }       
   }

   public void testPrepare2() throws Exception {
       System.out.println("testPrepare2");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition c1 = new Condition("Enterprise.SystemSBR.Person.Address.City","LIKE", "MO%", true);
       Condition c2 = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","LIKE", "CA%", true);
       Condition c3 = new Condition("Enterprise.SystemSBR.Person.Phone.Phone","LIKE", "949%", true);
       c1.addConditions("OR", c2);        
       c1.addConditions("OR", c3);        
       
       Condition[] conditions = new Condition[] {  c1  };
       qo.setCondition(conditions);
       
       //qo.setRootObject("Enterprise.SystemSBR.Person");
       // System.out.println("QO:" + qo.toString());
       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       //        assdesc.setResultValueObjectType(new ObjectNodeMetaNode("Enterprise.SystemSBR.Person"));
       //queryManager.prepare(qo);
       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
       //iterator.close();
   }

   public void testPrepare21() throws Exception {
       System.out.println("testPrepare21");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition c1 = new Condition("Enterprise.SystemSBR.Person.Address.City","LIKE", "MO%", true);
       Condition c2 = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","LIKE", "CA%", true);
       Condition c4 = new Condition("Enterprise.SystemSBR.Person.Phone.Phone","LIKE", "949%", true);
       c1.addConditions("OR", c2);
       Condition c3 = new Condition(c1);
       
       c3.addConditions("OR", c4);        
       
       Condition[] conditions = new Condition[] {  c3  };
       qo.setCondition(conditions);
       
       //qo.setRootObject("Enterprise.SystemSBR.Person");
       // System.out.println("QO:" + qo.toString());
       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       //        assdesc.setResultValueObjectType(new ObjectNodeMetaNode("Enterprise.SystemSBR.Person"));
       //queryManager.prepare(qo);
       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
       //iterator.close();
   }
    
    public void testPrepareJoinTypes() throws Exception {
       System.out.println("testPrepareJoinTypes");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition c1 = new Condition("Enterprise.SystemSBR.Person.Address.City","LIKE", "MO%", true);
       Condition c2 = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","LIKE", "CA%", true);
       c1.addConditions("AND", c2);
       Condition c3 = new Condition(c1);
       
       Condition[] conditions = new Condition[] {  c3  };
       qo.setCondition(conditions);
       
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
       //iterator.close();
   }
    
   public void testPrepare3() throws Exception {
       System.out.println("testPrepare3");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition c1 = new Condition("Enterprise.SystemSBR.Person.Address.City","LIKE", "IR%", true);
       Condition c2 = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","LIKE", "CA%", true);
       
       Condition c3 = new Condition("Enterprise.SystemSBR.Person.Phone.PhoneType","LIKE", "HOME%", true);
       
       c1.addConditions("OR", c2);
  
       Condition c4 = new Condition(c1);
       
       
       c4.addConditions("AND",c3);
       
       
       Condition[] conditions = new Condition[] {  c4  };
       qo.setCondition(conditions);
       
       //qo.setRootObject("Enterprise.SystemSBR.Person");
       // System.out.println("QO:" + qo.toString());
       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       // assdesc.setResultValueObjectType(new ObjectNodeMetaNode("Enterprise.SystemSBR.Person"));
       //queryManager.prepare(qo);
       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
       //Added by Sanjay
       iterator.close();
   }
   
   public void testPrepare4() throws Exception {
       System.out.println("testPrepare4");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition c1 = new Condition("Enterprise.SystemSBR.Person.Address.City","LIKE", "Ir%", true);
       Condition c2 = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","LIKE", "CA%", true);
       
       Condition c3 = new Condition("Enterprise.SystemSBR.Person.Address.City","=", "Irvine%", true);
       
       c1.addConditions("AND", c2);  
       Condition c4 = new Condition(c1);
       c4.addConditions("OR",c3);
              
       Condition[] conditions = new Condition[] {  c4  };
       qo.setCondition(conditions);
       
       //qo.setRootObject("Enterprise.SystemSBR.Person");
       // System.out.println("QO:" + qo.toString());
       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       //        assdesc.setResultValueObjectType(new ObjectNodeMetaNode("Enterprise.SystemSBR.Person"));
       //queryManager.prepare(qo);
       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
       //iterator.close();
   }

   public void testPrepare5() throws Exception {
       System.out.println("testPrepare5");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition area_714 = new Condition("Enterprise.SystemSBR.Person.Phone.Phone","LIKE", "949", true);
       Condition state_ca = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "CA", true);

       Condition phoneType_home = new Condition("Enterprise.SystemSBR.Person.Phone.PhoneType","=", "HOME", true);
       
       Condition phoneType_home2 = new Condition("Enterprise.SystemSBR.Person.Phone.PhoneType","=", "HOME", true);

       Condition state_ny = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "NY", true);

       Condition state_az = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "AZ", true);

       Condition zip_92612 = new Condition("Enterprise.SystemSBR.Person.Address.PostalCode","=", "92606", true);

       Condition street_123Main = new Condition("Enterprise.SystemSBR.Person.Address.AddressLine1","=", "324 Santa Barbara", true);

       Condition city_monrovia = new Condition("Enterprise.SystemSBR.Person.Address.City","=", "Monrovia", true);

       area_714.addConditions("OR", state_ca);
       area_714.addConditions("OR", phoneType_home);
       
       Condition c1 = new Condition(area_714);
       
       zip_92612.addConditions("OR", state_ny);
               
       street_123Main.addConditions("AND",state_az);
       street_123Main.addConditions("OR",zip_92612);
       
       c1.addConditions("OR", city_monrovia);
       c1.addConditions("AND", phoneType_home2);
       c1.addConditions("AND", street_123Main);

       Condition[] conditions = new Condition[] {  c1  };
       qo.setCondition(conditions);
       
       //qo.setRootObject("Enterprise.SystemSBR.Person");
       // System.out.println("QO:" + qo.toString());
       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       //        assdesc.setResultValueObjectType(new ObjectNodeMetaNode("Enterprise.SystemSBR.Person"));
       //queryManager.prepare(qo);
       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
       //iterator.close();
   }

   public void testPrepare5AddCondition() throws Exception {
       System.out.println("testPrepare5AddCondition");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition area_714 = new Condition("Enterprise.SystemSBR.Person.Phone.Phone","LIKE", "949", true);

       Condition zip_92612 = new Condition("Enterprise.SystemSBR.Person.Address.PostalCode","=", "92606", true);

       Condition street_123Main = new Condition("Enterprise.SystemSBR.Person.Address.AddressLine1","=", "324 Santa Barbara", true);

       area_714.addCondition("OR", "Enterprise.SystemSBR.Person.Address.StateCode","=", "CA", true);
       area_714.addCondition("OR", "Enterprise.SystemSBR.Person.Phone.PhoneType","=", "HOME", true);
       
       Condition c1 = new Condition(area_714);
       
       zip_92612.addCondition("OR", "Enterprise.SystemSBR.Person.Address.StateCode","=", "NY", true);
               
       street_123Main.addCondition("AND","Enterprise.SystemSBR.Person.Address.StateCode","=", "AZ", true);
       street_123Main.addConditions("OR",zip_92612);
       
       c1.addCondition("OR", "Enterprise.SystemSBR.Person.Address.City","=", "Monrovia", true);
       c1.addCondition("AND", "Enterprise.SystemSBR.Person.Phone.PhoneType","=", "HOME", true);
       c1.addConditions("AND", street_123Main);

       Condition[] conditions = new Condition[] {  c1  };
       qo.setCondition(conditions);
       
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
       iterator.close();
   }

   public void testPrepareCache() throws Exception {
       System.out.println("testPrepareCache");
       long time1 = System.currentTimeMillis();

       // for now put all the primary keys in the select fields
       
       String[] fields = {
           "Enterprise.SystemSBR.EUID",
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition area_714 = new Condition("Enterprise.SystemSBR.Person.Phone.Phone","LIKE", "9149", true);
       Condition state_ca = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "1CA", true);

       Condition phoneType_home = new Condition("Enterprise.SystemSBR.Person.Phone.PhoneType","=", "HOME", true);
       
       Condition phoneType_home2 = new Condition("Enterprise.SystemSBR.Person.Phone.PhoneType","=", "HOME", true);

       Condition state_ny = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "NY", true);

       Condition state_az = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "AZ", true);

       Condition zip_92612 = new Condition("Enterprise.SystemSBR.Person.Address.PostalCode","=", "192606", true);

       Condition street_123Main = new Condition("Enterprise.SystemSBR.Person.Address.AddressLine1","=", "1324 Santa Barbara", true);

       Condition city_monrovia = new Condition("Enterprise.SystemSBR.Person.Address.City","=", "Monrovia", true);

       area_714.addConditions("OR", state_ca);
       area_714.addConditions("OR", phoneType_home);
       
       Condition c1 = new Condition(area_714);
       
       zip_92612.addConditions("OR", state_ny);
               
       street_123Main.addConditions("AND",state_az);
       street_123Main.addConditions("OR",zip_92612);
       
       c1.addConditions("OR", city_monrovia);
       c1.addConditions("AND", phoneType_home2);
       c1.addConditions("AND", street_123Main);

       Condition[] conditions = new Condition[] {  c1  };
       qo.setCondition(conditions);
       
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator = mQueryManager.executeAssemble(qo);
       int j=1;
       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
           //System.out.println("Count : " + (j++));
       }
       //iterator.close();

             String[] fields2 = {
           "Enterprise.SystemSBR.EUID",
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       long time2 = System.currentTimeMillis();
       long time = time2 - time1;
       System.out.println("time1 :" + time);
       
       QueryObject qo2 = new QueryObject();
       qo2.setSelect(fields2);

       Condition area_7142 = new Condition("Enterprise.SystemSBR.Person.Phone.Phone","LIKE", "949", true);
       Condition state_ca2 = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "CA", true);

       Condition phoneType_home12 = new Condition("Enterprise.SystemSBR.Person.Phone.PhoneType","=", "HOME", true);
       
       Condition phoneType_home22 = new Condition("Enterprise.SystemSBR.Person.Phone.PhoneType","=", "HOME", true);

       Condition state_ny2 = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "NY", true);

       Condition state_az2 = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "AZ", true);

       Condition zip_926122 = new Condition("Enterprise.SystemSBR.Person.Address.PostalCode","=", "92606", true);

       Condition street_123Main2 = new Condition("Enterprise.SystemSBR.Person.Address.AddressLine1","=", "324 Santa Barbara", true);

       Condition city_monrovia2 = new Condition("Enterprise.SystemSBR.Person.Address.City","=", "Monrovia1", true);

       area_7142.addConditions("OR", state_ca2);
       area_7142.addConditions("OR", phoneType_home12);
       
       Condition c12 = new Condition(area_7142);
       
       zip_926122.addConditions("OR", state_ny2);
               
       street_123Main2.addConditions("AND",state_az2);
       street_123Main2.addConditions("OR",zip_926122);
       
       c12.addConditions("OR", city_monrovia2);
       c12.addConditions("AND", phoneType_home22);
       c12.addConditions("AND", street_123Main2);

       Condition[] conditions2 = new Condition[] {  c12  };
       qo2.setCondition(conditions2);
       
       AssembleDescriptor assdesc2 = new AssembleDescriptor();
       qo2.setAssembleDescriptor(assdesc2);
       assdesc2.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator2 = mQueryManager.executeAssemble(qo2);
       int i = 1;
       while (iterator2.hasNext()) {
           Object object2 = iterator2.next();
           System.out.println(object2.toString());
           //System.out.println("Count : " + (i++));
       }
       //iterator2.close();

       long time3 = System.currentTimeMillis();
       long timeCache = time3 - time2;
       System.out.println("time cache :" + timeCache);
   }

   public void testPrepare9() throws Exception {
       System.out.println("testPrepare9");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition[] conditions = new Condition[] {
               
                   new Condition("Enterprise.SystemSBR.Person.Address.City",
                       "LIKE", "IR%", true),
                       new Condition("Enterprise.SystemSBR.Person.Phone.PhoneType",
                       "LIKE", "HOME%", true),
                       new Condition("Enterprise.SystemSBR.Person.Address.StateCode",
                       "LIKE", "CA%", true)                       
           };
       
       qo.setCondition(conditions);
       
       qo.addCondition("Enterprise.SystemSBR.Person.Address.PostalCode","LIKE", "92606%");

       //qo.setRootObject("Enterprise.SystemSBR.Person");
       // System.out.println("QO:" + qo.toString());
       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       //        assdesc.setResultValueObjectType(new ObjectNodeMetaNode("Enterprise.SystemSBR.Person"));
       //queryManager.prepare(qo);
       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
       //iterator.close();
   }
   
   public void testPrepare10() throws Exception {
       System.out.println("testPrepare10");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",           
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition c1 = new Condition("Enterprise.SystemSBR.Person.Address.City","LIKE", "IR%", true);
       Condition c2 = new Condition("Enterprise.SystemSBR.Person.Phone.Phone","LIKE", "949%", true);
       Condition c3 = new Condition("Enterprise.SystemSBR.Person.Address.StateCode", "LIKE", "CA%", true);
       Condition c4 = new Condition("Enterprise.SystemSBR.Person.Address.PostalCode", "LIKE", "92606%", true);
               
       c2.addConditions("AND", c4);
       
       Condition[] conditions = new Condition[] { c1, c2, c3 };
       
       qo.setCondition(conditions);
       
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
       //iterator.close();
   }
   
   public void testPrepare11() throws Exception {
       System.out.println("testPrepare11");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition c1 = new Condition("Enterprise.SystemSBR.Person.Address.City","LIKE", "IR%", true);
       Condition c2 = new Condition("Enterprise.SystemSBR.Person.Phone.Phone","LIKE", "949%", true);
       Condition c3 = new Condition("Enterprise.SystemSBR.Person.Address.StateCode", "LIKE", "CA%", true);
       Condition c4 = new Condition("Enterprise.SystemSBR.Person.Address.PostalCode", "LIKE", "92606%", true);
               
       c1.addConditions("AND", c2);
       c1.addConditions("OR", c4);
       c1.addConditions("AND", c3);
       
       Condition[] conditions = new Condition[] { c1 };
       
       qo.setCondition(conditions);
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
       //iterator.close();
   }
      
   public void testPrepare12() throws Exception {
       System.out.println("testPrepare12");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition c1 = new Condition("Enterprise.SystemSBR.Person.Address.City","LIKE", "IR%", true);
       Condition c2 = new Condition("Enterprise.SystemSBR.Person.Address.AddressLine1","LIKE", "324%", true);
       Condition c3 = new Condition("Enterprise.SystemSBR.Person.Address.StateCode", "LIKE", "CA%", true);
       Condition c4 = new Condition("Enterprise.SystemSBR.Person.Address.PostalCode", "LIKE", "92606%", true);
               
       c1.addConditions("AND", c2);
       c1.addConditions("OR", c4);
       c1.addConditions("AND", c3);
       
       Condition[] conditions = new Condition[] { c1 };
       
       qo.setCondition(conditions);
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
       //iterator.close();
   }

   /**
    * tests prepare statement for multiple objects
    *
    * @exception Exception error occurs
    */
   public void testPrepareMutipleObjects() throws Exception {
       System.out.println("testPrepareMutipleObjects");

       // for now put all the primary keys in the select fields
       String[] fields = {
            
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition[][] conditions = new Condition[][] {
               {
                   new Condition("Enterprise.SystemSBR.Person.LastName","LIKE", "Ka%", true)
               },
               {
                   new Condition("Enterprise.SystemSBR.Person.Address.City","=", "Irvine", true)
               }
       };
       qo.setCondition(conditions);

       //qo.setRootObject("Enterprise.SystemSBR.Person");
       // System.out.println("QO:" + qo.toString());
       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       //        assdesc.setResultValueObjectType(new ObjectNodeMetaNode("Enterprise.SystemSBR.Person"));
       // queryManager.prepare(qo);
       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
   }

 
   /**
    * tests SIngle object node
    *
    * @exception Exception error occurs
    */
   public void testSingleObjectNode2() throws Exception {
       System.out.println("testSingleObjectNode2");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.FirstName",
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.PersonId",
       };
       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       qo.setRootObject("Enterprise.SystemSBR.Person");

       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());
       assdesc.setResultValueObjectType(new ObjectNodeMetaNode(
               "Enterprise.SystemSBR.Person"));

       System.out.println("QO:" + qo.toString());

       QMIterator iterator = mQueryManager.executeAssemble(qo);

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
   }

  
   /**
    * Tests tuple
    *
    * @exception Exception error occurs
    */
   public void testTuple() throws Exception {
       System.out.println("testTuple");

       String[] fields = {
           
           "Enterprise.SystemSBR.Person.SSN",
           "Enterprise.SystemSBR.Person.LastName",           
           "Enterprise.SystemSBR.Person.Phone.Phone"
       };
       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition[][] conditions = new Condition[][] {
               {
                   new Condition("Enterprise.SystemSBR.Person.FirstName",
                       "LIKE", "Aj%",true)
               },
               {
                   new Condition("Enterprise.SystemSBR.Person.Address.City",
                       "LIKE", "M%",true)
               }
           };
       qo.setCondition(conditions);

       qo.setQueryOption(qo.SINGLE_QUERY);

       System.out.println("QO:" + qo.toString());

       QueryResults qresults = mQueryManager.execute(qo);

       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       assdesc.setAssembler(new TupleAssembler());
       qresults.setAssembleDescriptor(assdesc);

       QMIterator iterator = qresults.assemble();

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }               
  }

  public void testTuple02() throws Exception {
       System.out.println("testTupleSanjay");
       String[] fields = {
           
           "Enterprise.SystemSBR.Person.SSN",
           "Enterprise.SystemSBR.Person.LastName",
           
           "Enterprise.SystemSBR.Person.Phone.Phone"
       };
       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition[][] conditions = new Condition[][] {
               {
                   new Condition("Enterprise.SystemSBR.Person.FirstName",
                       "LIKE", "CA%", true)
               },
               {
                   new Condition("Enterprise.SystemSBR.Person.Address.City",
                       "LIKE", "M%")
               }
           };
       qo.setCondition(conditions);

       qo.addCondition("Enterprise.SystemSBR.Person.FirstName", "LIKE", "C%");
       qo.addCondition("Enterprise.SystemSBR.Person.SSN", "LIKE", "0%");
       qo.setRootObject("Enterprise.SystemSBR.Person");
       qo.setQueryOption(qo.SINGLE_QUERY);

       System.out.println("QO:" + qo.toString());

       QueryResults qresults = mQueryManager.execute(qo);

       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       assdesc.setAssembler(new TupleAssembler());
       qresults.setAssembleDescriptor(assdesc);

       QMIterator iterator = qresults.assemble();

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
   }

   /**
    * Another tuple construction tests
    *
    * @exception Exception error occurs
    */
   public void testTuple03() throws Exception {
       System.out.println("testTuple2");
       String[] fields = {
           "Enterprise.SystemSBR.Person.SSN",
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Phone.Phone"
       };
       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition[][] conditions = new Condition[][] {
               {
                   new Condition("Enterprise.SystemSBR.Person.FirstName",
                       "LIKE", "CA%", true)
               },
               {
                   new Condition("Enterprise.SystemSBR.Person.Address.City",
                       "LIKE", "M%")
               }
           };
       qo.setCondition(conditions);

       qo.addCondition("Enterprise.SystemSBR.Person.FirstName", "LIKE", "C%");
       qo.addCondition("Enterprise.SystemSBR.Person.SSN", "LIKE", "0%");
       qo.setRootObject("Enterprise.SystemSBR.Person");
       qo.setQueryOption(qo.SINGLE_QUERY);

       System.out.println("QO:" + qo.toString());

       QueryResults qresults = mQueryManager.execute(qo);

       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       assdesc.setAssembler(new TupleAssembler());
       qresults.setAssembleDescriptor(assdesc);

       QMIterator iterator = qresults.assemble();

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
   }
   
   
  
   /**
    * Tests the Tuple Assembler
    *
    * @exception Exception error occurs
    */
   public void testTupleAssembler() throws Exception {
       System.out.println("testTupleAssembler");
       String[] fields = {
           
           "Enterprise.SystemSBR.Person.EUID",
           "Enterprise.SystemSBR.Person.PersonId",
           "Enterprise.SystemSBR.Person.SSN",
           "Enterprise.SystemSBR.Person.FirstName",
           "Enterprise.SystemSBR.Person.LastName",
           
           "Enterprise.SystemSBR.Person.Address.PostalCode",
           "Enterprise.SystemSBR.Person.Address.AddressId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition[] conditions = new Condition[] {
               new Condition("Enterprise.SystemSBR.Person.FirstName", "LIKE",
                   "Cal%")
           };
       qo.setCondition(conditions);

       qo.addCondition("Enterprise.SystemSBR.Person.FirstName", "LIKE", "C%");
       qo.addCondition("Enterprise.SystemSBR.Person.SSN", "LIKE", "0%");
       qo.setRootObject("Enterprise.SystemSBR.Person");
       qo.setQueryOption(qo.SINGLE_QUERY);

       System.out.println("QO:" + qo.toString());

       QueryResults qresults = mQueryManager.execute(qo);

       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       assdesc.setAssembler(new TupleAssembler());
       qresults.setAssembleDescriptor(assdesc);

       QMIterator iterator = qresults.assemble();

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }

       qresults = mQueryManager.execute(qo);

       // set Factory
       qresults.setAssembleDescriptor(assdesc);
       iterator = qresults.assemble();

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
   }

  public void testPrepareA() throws Exception {
       System.out.println("testPrepareA");
       long time1 = System.currentTimeMillis();

             String[] fields2 = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       long time2 = System.currentTimeMillis();
       long time = time2 - time1;
       System.out.println("time1 :" + time);
       
       QueryObject qo2 = new QueryObject();
       qo2.setSelect(fields2);

       Condition area_7142 = new Condition("Enterprise.SystemSBR.Person.Phone.Phone","LIKE", "949", true);
       Condition state_ca2 = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "CA", true);

       Condition phoneType_home12 = new Condition("Enterprise.SystemSBR.Person.Phone.PhoneType","=", "HOME", true);
       
       Condition phoneType_home22 = new Condition("Enterprise.SystemSBR.Person.Phone.PhoneType","=", "HOME", true);

       Condition state_ny2 = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "NY", true);

       Condition state_az2 = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "AZ", true);

       Condition zip_926122 = new Condition("Enterprise.SystemSBR.Person.Address.PostalCode","=", "92606", true);

       Condition street_123Main2 = new Condition("Enterprise.SystemSBR.Person.Address.AddressLine1","=", "324 Santa Barbara", true);

       Condition city_monrovia2 = new Condition("Enterprise.SystemSBR.Person.Address.City","=", "Monrovia", true);

       area_7142.addConditions("OR", state_ca2);
       area_7142.addConditions("OR", phoneType_home12);
       
       Condition c12 = new Condition(area_7142);
       
       zip_926122.addConditions("OR", state_ny2);
               
       street_123Main2.addConditions("AND",state_az2);
       street_123Main2.addConditions("OR",zip_926122);
       
       c12.addConditions("OR", city_monrovia2);
       c12.addConditions("AND", phoneType_home22);
       c12.addConditions("AND", street_123Main2);

       Condition[] conditions2 = new Condition[] {  c12  };
       qo2.setCondition(conditions2);
       
       AssembleDescriptor assdesc2 = new AssembleDescriptor();
       qo2.setAssembleDescriptor(assdesc2);
       assdesc2.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator2 = mQueryManager.executeAssemble(qo2);
       int i = 1;
       while (iterator2.hasNext()) {
           Object object2 = iterator2.next();
           System.out.println(object2.toString());
       }
       //iterator2.close();

       long time3 = System.currentTimeMillis();
       long timeCache = time3 - time2;
       System.out.println("time cache :" + timeCache);

       
   }

  public void testPrepareB() throws Exception {
       System.out.println("testPrepareB");
       long time1 = System.currentTimeMillis();

       // for now put all the primary keys in the select fields
       
       String[] fields = {
           "Enterprise.SystemSBR.EUID",
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition area_714 = new Condition("Enterprise.SystemSBR.Person.Phone.Phone","LIKE", "949", true);
       Condition state_ca = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "CA", true);

       Condition phoneType_home = new Condition("Enterprise.SystemSBR.Person.Phone.PhoneType","=", "HOME", true);
       
       Condition phoneType_home2 = new Condition("Enterprise.SystemSBR.Person.Phone.PhoneType","=", "HOME", true);

       Condition state_ny = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "NY", true);

       Condition state_az = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "AZ", true);

       Condition zip_92612 = new Condition("Enterprise.SystemSBR.Person.Address.PostalCode","=", "92606", true);

       Condition street_123Main = new Condition("Enterprise.SystemSBR.Person.Address.AddressLine1","=", "324 Santa Barbara", true);

       Condition city_monrovia = new Condition("Enterprise.SystemSBR.Person.Address.City","=", "Monrovia", true);

       area_714.addConditions("OR", state_ca);
       area_714.addConditions("OR", phoneType_home);
       
       Condition c1 = new Condition(area_714);
       
       zip_92612.addConditions("OR", state_ny);
               
       street_123Main.addConditions("AND",state_az);
       street_123Main.addConditions("OR",zip_92612);
       
       c1.addConditions("OR", city_monrovia);
       c1.addConditions("AND", phoneType_home2);
       c1.addConditions("AND", street_123Main);

       Condition[] conditions = new Condition[] {  c1  };
       qo.setCondition(conditions);
       
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator = mQueryManager.executeAssemble(qo);
       int j=1;
       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
       //iterator.close();
       

       long time3 = System.currentTimeMillis();
       long timeCache = time3 - time1;
       System.out.println("time cache :" + timeCache);

       
   }   

  public void testPrepareC() throws Exception {
       System.out.println("testPrepareC");
       long time1 = System.currentTimeMillis();

             String[] fields = {
           "Enterprise.SystemSBR.EUID",
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };

       long time2 = System.currentTimeMillis();
       long time = time2 - time1;
       System.out.println("time1 :" + time);
       
       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition area_714 = new Condition("Enterprise.SystemSBR.Person.Phone.Phone","LIKE", "949", true);
       Condition state_ca = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "CA", true);

       Condition phoneType_home = new Condition("Enterprise.SystemSBR.Person.Phone.PhoneType","=", "HOME", true);
       
       Condition phoneType_home2 = new Condition("Enterprise.SystemSBR.Person.Phone.PhoneType","=", "HOME", true);

       Condition state_ny = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "NY", true);

       Condition state_az = new Condition("Enterprise.SystemSBR.Person.Address.StateCode","=", "AZ", true);

       Condition zip_92612 = new Condition("Enterprise.SystemSBR.Person.Address.PostalCode","=", "92606", true);

       Condition street_123Main = new Condition("Enterprise.SystemSBR.Person.Address.AddressLine1","=", "324 Santa Barbara", true);

       Condition city_monrovia = new Condition("Enterprise.SystemSBR.Person.Address.City","=", "Monrovia", true);

       area_714.addConditions("OR", state_ca);
       area_714.addConditions("OR", phoneType_home);
       
       Condition c1 = new Condition(area_714);
       
       zip_92612.addConditions("OR", state_ny);
               
       street_123Main.addConditions("AND",state_az);
       street_123Main.addConditions("OR",zip_92612);
       
       c1.addConditions("OR", city_monrovia);
       c1.addConditions("AND", phoneType_home2);
       c1.addConditions("AND", street_123Main);

       Condition[] conditions = new Condition[] {  c1  };
       qo.setCondition(conditions);
       
       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator = mQueryManager.executeAssemble(qo);
       int i = 1;
       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
       //iterator.close();

       long time3 = System.currentTimeMillis();
       long timeCache = time3 - time2;
       System.out.println("time cache :" + timeCache);        
   }
  
      public void testPrepareD() throws Exception {
       System.out.println("testPrepareD");

       // for now put all the primary keys in the select fields
       String[] fields = {
           //"Enterprise.SystemSBR.EUID",
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
           "Enterprise.SystemSBR.Person.Phone.Phone",
           "Enterprise.SystemSBR.Person.Phone.PhoneId",
       };
           
       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition[] conditions = new Condition[] {
               
                   new Condition("Enterprise.SystemSBR.Person.LastName",
                       "LIKE", "Ka%", true),
                   new Condition("Enterprise.SystemSBR.Person.Address.City",
                       "LIKE", "Ir%", true),
                   new Condition("Enterprise.SystemSBR.Person.Address.StateCode",
                       "LIKE", "CA%", true)
               

       };
       qo.setCondition(conditions);

       //qo.setRootObject("Enterprise.SystemSBR.Person");
       // System.out.println("QO:" + qo.toString());
       // set Factory
       
       qo.setQueryOption(qo.SINGLE_QUERY);
       
      QueryResults qresults = mQueryManager.execute(qo);

       // set Factory
       AssembleDescriptor assdesc = new AssembleDescriptor();
       assdesc.setAssembler(new TupleAssembler());
       qresults.setAssembleDescriptor(assdesc);

       QMIterator iterator = qresults.assemble();

       while (iterator.hasNext()) {
           Object object = iterator.next();
           System.out.println(object.toString());
       }
       
   }

   public void testWithoutConditions() throws Exception {
       System.out.println("testWithoutConditions");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
           "Enterprise.SystemSBR.Person.Address.City",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator = mQueryManager.executeAssemble(qo);

       if ( iterator != null )  {
           while (iterator.hasNext()) {
               Object object = iterator.next();
               System.out.println(object.toString());
           }
           //iterator.close();
       }
   }

   public void testWithoutJoin() throws Exception {
       System.out.println("testWithoutJoin");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName"
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       Condition[] conditions = new Condition[] {
               
                   new Condition("Enterprise.SystemSBR.Person.LastName",
                       "LIKE", "Ka%", false),
                   new Condition("Enterprise.SystemSBR.Person.Address.StateCode",
                       "LIKE", "CA%", true)           
           };
       qo.setCondition(conditions);

       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator = mQueryManager.executeAssemble(qo);

       if ( iterator != null )  {
           while (iterator.hasNext()) {
               Object object = iterator.next();
               System.out.println(object.toString());
           }
           iterator.close();
       }
   }
   
   public void testWithoutWhere() throws Exception {
       System.out.println("testWithoutWhere");

       // for now put all the primary keys in the select fields
       String[] fields = {
           "Enterprise.SystemSBR.Person.EUID", 
           "Enterprise.SystemSBR.Person.LastName",
       };

       QueryObject qo = new QueryObject();
       qo.setSelect(fields);

       AssembleDescriptor assdesc = new AssembleDescriptor();
       qo.setAssembleDescriptor(assdesc);
       assdesc.setAssembler(new ObjectNodeAssembler());

       QMIterator iterator = mQueryManager.executeAssemble(qo);

       if ( iterator != null )  {
           while (iterator.hasNext()) {
               Object object = iterator.next();
               System.out.println(object.toString());
           }
           iterator.close();
       }
   }    
   
}
