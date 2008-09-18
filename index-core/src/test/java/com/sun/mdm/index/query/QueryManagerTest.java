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


import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.objects.metadata.ObjectFactory;



/**
 * @author sdua
 */
public class QueryManagerTest extends TestCase {
    private static QueryManager mQueryManager;
    private MasterController mc;
    
    
    /**
     * Creates a new instance of QueryManagerEJB
     *
     * @param name name of test
     */
    public QueryManagerTest(String name) {
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
        QueryManagerTest qtest = new QueryManagerTest("testPrepare");
        qtest.setUp();
        long time1 = System.currentTimeMillis();
        qtest.testIN();
        long time2 = System.currentTimeMillis();
        long time = time2 - time1;
        System.out.println("time:" + time);
         time1 = System.currentTimeMillis();
        qtest.testIN();
        time2 = System.currentTimeMillis();
        time = time2 - time1;
        System.out.println("time:" + time);  
      **/
    }
    
  

    /**
     * test In clause
     *
     * @exception Exception error occurs
     */
    public void testINClause() throws Exception {
        

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
                        "IN", new String[] {"Monrovia", "Arcadia"})
                }
            };
        qo.setCondition(conditions);

        //     qo.addCondition("Enterprise.SBR.Person.FirstName", "LIKE", "C%");
        //   qo.addCondition(new Condition("Enterprise.SBR.Person.Address.City", "IN", new String[] {"Monrovia",
        //                               "Arcadia"}) );
        // qo.addCondition("Enterprise.SBR.Person.Phone.PhoneNumber", "LIKE", "626%");
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
    }

   
    /**
     * Test IN clause in cache
     *
     * @exception Exception error occurs
     */
    public void testINClauseCache() throws Exception {
        System.out.println("testPrepare");

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
                        "IN", new String[] {"Arcadia"})
                }
            };
        qo.setCondition(conditions);

        //     qo.addCondition("Enterprise.SBR.Person.FirstName", "LIKE", "C%");
        //   qo.addCondition(new Condition("Enterprise.SBR.Person.Address.City", "IN", new String[] {"Monrovia",
        //                               "Arcadia"}) );
        // qo.addCondition("Enterprise.SBR.Person.Phone.PhoneNumber", "LIKE", "626%");
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
    }

   
    /**
     * his tests the ObjectNodeFactory for Person  Object.
     *
     * @exception Exception error occurs
     */
    public void testSingleObjectNode() throws Exception {
        // for now put all the primary keys in the select fields
        String[] fields = {
            "Enterprise.SystemSBR.Person.SSN",
            "Enterprise.SystemSBR.Person.PersonId",
            "Enterprise.SystemSBR.Person.LastName",
        };
        QueryObject qo = new QueryObject();
        qo.setSelect(fields);

        Condition[][] conditions = new Condition[][] {
                {
                    new Condition("Enterprise.SystemSBR.Person.FirstName",
                        "LIKE", "FOO")
                },
                {
                    new Condition("Enterprise.SystemSBR.Person.DOB", "LIKE",
                        new java.util.Date())
                }
            };
        qo.setCondition(conditions);

        // qo.addCondition("Enterprise.SystemSBR.Person.FirstName", "LIKE", "C%");
        //  qo.addCondition("Enterprise.SystemSBR.Person.DOB", "LIKE", "1901-01-02");
        qo.setRootObject("Enterprise.SystemSBR.Person");

        System.out.println("QO:" + qo.toString());

        // set ValueObjectFactory
        AssembleDescriptor assdesc = new AssembleDescriptor();
        qo.setAssembleDescriptor(assdesc);
        assdesc.setAssembler(new ObjectNodeAssembler());
        assdesc.setResultValueObjectType(new ObjectNodeMetaNode(
                "Enterprise.SystemSBR.Person"));

        QMIterator iterator = mQueryManager.executeAssemble(qo);

        while (iterator.hasNext()) {
            Object object = iterator.next();
            System.out.println(object.toString());
        }
    }

   

    /**
     * This tests the Multiple paths for Person, Address && Phone Object
     *
     * @exception Exception error occurs
     */
    public void testMultipleSecondaryObjects() throws Exception {
    	 
         // for now put all the primary keys in the select fields
        String[] fields = {
            "Enterprise.SystemSBR.Person.EUID", 
            //"Enterprise.SystemSBR.Person.PersonId",
            "Enterprise.SystemSBR.Person.LastName",
            "Enterprise.SystemSBR.Person.Address.City",
            
            // "Enterprise.SystemSBR.Person.Address.AddressId",
            "Enterprise.SystemSBR.Person.Phone.Phone",
            "Enterprise.SystemSBR.Person.Phone.PhoneId",
        };

        QueryObject qo = new QueryObject();
        qo.setSelect(fields);

        Condition[][] conditions = new Condition[][] {
                {
                    new Condition("Enterprise.SystemSBR.Person.FirstName",
                        "LIKE", "Call%")
                },
                {
                    new Condition("Enterprise.SystemSBR.Person.Address.City",
                        "LIKE", "Mon%")
                }
            };
        qo.setCondition(conditions);

        //     qo.addCondition("Enterprise.SBR.Person.FirstName", "LIKE", "C%");
        //   qo.addCondition(new Condition("Enterprise.SBR.Person.Address.City", "IN", new String[] {"Monrovia",
        //                               "Arcadia"}) );
        // qo.addCondition("Enterprise.SBR.Person.Phone.PhoneNumber", "LIKE", "626%");
        //qo.setRootObject("Enterprise.SystemSBR.Person");
        // System.out.println("QO:" + qo.toString());
        // set Factory
        AssembleDescriptor assdesc = new AssembleDescriptor();
        qo.setAssembleDescriptor(assdesc);
        assdesc.setAssembler(new ObjectNodeAssembler());

        //        assdesc.setResultValueObjectType(new ObjectNodeMetaNode("Enterprise.SystemSBR.Person"));
        QMIterator iterator = mQueryManager.executeAssemble(qo);

        while (iterator.hasNext()) {
            Object object = iterator.next();
            System.out.println(object.toString());
        }
    }


 /**
     * This tests the * operataor for Person, Address && Phone Object
     *
     * @exception Exception error occurs
     */
    public void testStarOperator() throws Exception {
        
        String[] fields = {
            "Enterprise.SystemSBR.Person.*", 
            "Enterprise.SystemSBR.Person.Address.*",
            "Enterprise.SystemSBR.Person.Phone.*"
                    };

        QueryObject qo = new QueryObject();
        qo.setSelect(fields);

        Condition[][] conditions = new Condition[][] {
                {
                    new Condition("Enterprise.SystemSBR.Person.FirstName",
                        "LIKE", "Call%")
                },
                {
                    new Condition("Enterprise.SystemSBR.Person.Address.City",
                        "LIKE", "Mon%")
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
     * This tests prepare statement
     *
     * @exception Exception error occurs
     */
    public void testPrepare() throws Exception {
        System.out.println("testPrepare");

        // for now put all the primary keys in the select fields
        String[] fields = {
            "Enterprise.SystemSBR.Person.EUID", 
            //"Enterprise.SystemSBR.Person.PersonId",
            "Enterprise.SystemSBR.Person.LastName",
            "Enterprise.SystemSBR.Person.Address.City",
            
            //"Enterprise.SystemSBR.Person.Address.AddressId",
            "Enterprise.SystemSBR.Person.Phone.Phone",
            "Enterprise.SystemSBR.Person.Phone.PhoneId",
        };

        QueryObject qo = new QueryObject();
        qo.setSelect(fields);

        Condition[] conditions = new Condition[] {
                
                    new Condition("Enterprise.SystemSBR.Person.LastName",
                        "LIKE", "KA%", true)
                
            };
        qo.setCondition(conditions);

        //     qo.addCondition("Enterprise.SBR.Person.FirstName", "LIKE", "C%");
        //   qo.addCondition(new Condition("Enterprise.SBR.Person.Address.City", "IN", new String[] {"Monrovia",
        //                               "Arcadia"}) );
        // qo.addCondition("Enterprise.SBR.Person.Phone.PhoneNumber", "LIKE", "626%");
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
    }

    // This tests the ObjectNodeFactory for Person & Address Object.
    // This would be used in ETD

    /**
     * tests prepare statement for multiple objects
     *
     * @exception Exception error occurs
     */
    public void testPrepareMutipleObjects() throws Exception {
        System.out.println("testPrepare2");

        // for now put all the primary keys in the select fields
        String[] fields = {
             
            //"Enterprise.SystemSBR.Person.PersonId",
            "Enterprise.SystemSBR.Person.LastName",
            "Enterprise.SystemSBR.Person.Address.City",
            
            // "Enterprise.SystemSBR.Person.Address.AddressId",
            "Enterprise.SystemSBR.Person.Phone.Phone",
            "Enterprise.SystemSBR.Person.Phone.PhoneId",
        };

        QueryObject qo = new QueryObject();
        qo.setSelect(fields);

        Condition[][] conditions = new Condition[][] {
                {
                    new Condition("Enterprise.SystemSBR.Person.LastName",
                        "LIKE", "KO%", true)
                },
                {
                    new Condition("Enterprise.SystemSBR.Person.Address.City",
                        "IN", new String[] {"Mon%"})
                }
            };
        qo.setCondition(conditions);

        //     qo.addCondition("Enterprise.SBR.Person.FirstName", "LIKE", "C%");
        //   qo.addCondition(new Condition("Enterprise.SBR.Person.Address.City", "IN", new String[] {"Monrovia",
        //                               "Arcadia"}) );
        // qo.addCondition("Enterprise.SBR.Person.Phone.PhoneNumber", "LIKE", "626%");
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
        // for now put all the primary keys in the select fields
        String[] fields = {
            "Enterprise.SystemSBR.Person.FirstName",
            "Enterprise.SystemSBR.Person.LastName",
            "Enterprise.SystemSBR.Person.PersonId",
        };
        QueryObject qo = new QueryObject();
        qo.setSelect(fields);

        //qo.addCondition("Enterprise.SystemSBR.Person.FirstName", "LIKE", "Sw%");
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
        String[] fields = {
            
            //"Enterprise.EUID",
            //"Enterprise.SystemSBR.EUID",
            // "Enterprise.SystemSBR.Person.PersonId",
            "Enterprise.SystemSBR.Person.SSN",
            "Enterprise.SystemSBR.Person.LastName",
            
            //"Enterprise.SystemSBR.Person.Dob",
            // "Enterprise.SystemSBR.Person.Address.City",
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

        // qo.addCondition("Enterprise.SystemSBR.Person.FirstName", "LIKE", "C%");
        // qo.addCondition("Enterprise.SystemSBR.Person.SSN", "LIKE", "0%");
        //qo.setRootObject("Enterprise.SystemSBR.Person");
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
    public void testTuple2() throws Exception {
        String[] fields = {
            
            //"Enterprise.EUID",
            //"Enterprise.SystemSBR.EUID",
            // "Enterprise.SystemSBR.Person.PersonId",
            "Enterprise.SystemSBR.Person.SSN",
            "Enterprise.SystemSBR.Person.LastName",
            
            //"Enterprise.SystemSBR.Person.Dob",
            // "Enterprise.SystemSBR.Person.Address.City",
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

        // qo.addCondition("Enterprise.SystemSBR.Person.FirstName", "LIKE", "C%");
        // qo.addCondition("Enterprise.SystemSBR.Person.SSN", "LIKE", "0%");
        //qo.setRootObject("Enterprise.SystemSBR.Person");
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
        String[] fields = {
            
            //"Enterprise.EUID",
            "Enterprise.SystemSBR.Person.EUID",
            "Enterprise.SystemSBR.Person.PersonId",
            "Enterprise.SystemSBR.Person.SSN",
            "Enterprise.SystemSBR.Person.FirstName",
            "Enterprise.SystemSBR.Person.LastName",
            
            //"Enterprise.SystemSBR.Person.Dob",
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

        // qo.addCondition("Enterprise.SystemSBR.Person.FirstName", "LIKE", "C%");
        // qo.addCondition("Enterprise.SystemSBR.Person.SSN", "LIKE", "0%");
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

    
    /**
     * Test hints
     *
     * @exception Exception error occurs
     */
    public void testHints() throws Exception {
        String[] fields = {
            
            //"Enterprise.EUID",
            "Enterprise.SystemSBR.Person.EUID",
            // "Enterprise.SystemSBR.Person.PersonId",
            "Enterprise.SystemSBR.Person.SSN",
            "Enterprise.SystemSBR.Person.LastName",
            
            //"Enterprise.SystemSBR.Person.Dob",
            // "Enterprise.SystemSBR.Person.Address.City",
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

        // qo.addCondition("Enterprise.SystemSBR.Person.FirstName", "LIKE", "C%");
        // qo.addCondition("Enterprise.SystemSBR.Person.SSN", "LIKE", "0%");
        //qo.setRootObject("Enterprise.SystemSBR.Person");
        qo.setQueryOption(qo.SINGLE_QUERY);
        if (ObjectFactory.getDatabase().equals("Oracle")) {
          String hint = "FIRST_ROWS";
          qo.setHints(new String[][]{{hint, " /*+ ALL_ROWS */"}} );
        } else if (ObjectFactory.getDatabase().equalsIgnoreCase("SQLServer")){        
          qo.setHints(new String[][]{{"", "MAXRECURSION 2"}} );
        } else {
          qo.setHints(new String[][]{{"", ""}} );
        }
                
        //qo.parse();
        //SQLDescriptor[] sqlDesc = qo.getSQLDescriptor();
        
        //String sql = sqlDesc[0].getSQL();
        
        //assertTrue(sql.indexOf(hint)> 0);

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
     * This tests the Multiple paths for Person, Address && Phone Object
     *
     * @exception Exception error occurs
     */
    public void testBlankQuery() throws Exception {
        // for now put all the primary keys in the select fields
        String[] fields = {
            "Enterprise.SystemSBR.Person.EUID", 
            //"Enterprise.SystemSBR.Person.PersonId",
            "Enterprise.SystemSBR.Person.LastName",
            "Enterprise.SystemSBR.Person.Address.City",
            
            "Enterprise.SystemSBR.Person.Address.AddressId",
            "Enterprise.SystemSBR.Person.Phone.Phone",
            "Enterprise.SystemSBR.Person.Phone.PhoneId",
        };

        QueryObject qo = new QueryObject();
        qo.setSelect(fields);

        
        AssembleDescriptor assdesc = new AssembleDescriptor();
        qo.setAssembleDescriptor(assdesc);
        qo.setMaxObjects(100);
        assdesc.setAssembler(new ObjectNodeAssembler());
        qo.setFetchSize(1);

        QMIterator iterator = mQueryManager.executeAssemble(qo);
        int count = 0;
        while (iterator.hasNext()) {
            Object object = iterator.next();
            System.out.println(object.toString());
            count++;
        }
        assertTrue(count ==2);
    }
    


    

    /*
          This tests the Tuple Factory. This executes the QueryManager.execute().
          public  void testTupleAddressSigma() throws Exception {
          String[] fields = {
          "Enterprise.SystemSBR.Person.EUID",
          "Enterprise.SystemSBR.Person.PersonId",
          "Enterprise.SystemSBR.Person.SSN",
          "Enterprise.SystemSBR.Person.firstName",
          "Enterprise.SystemSBR.Person.LastName",
          };
          QueryObject qo = new QueryObject();
          qo.setSelect(fields);
          Condition[] conditions = new Condition[]
          {new Condition("Enterprise.SystemSBR.Person.Address.sigma_Address.StreetNamePhonetic", "LIKE", "64%")}
          ;
          qo.setCondition(conditions);
          qo.addCondition("Enterprise.SystemSBR.Person.FirstName", "LIKE", "C%");
          qo.addCondition("Enterprise.SystemSBR.Person.SSN", "LIKE", "0%");
          qo.setRootObject("Enterprise.SystemSBR.Person");
          qo.setQueryOption(qo.SINGLE_QUERY);
          System.out.println("QO:" + qo.toString());
          QueryManagerEJB queryManager = new QueryManagerEJB();
          QueryResults qresults = queryManager.execute(qo);
          set Factory
          AssembleDescriptor assdesc = new AssembleDescriptor();
          assdesc.setValueObjectFactory(new TupleFactory());
          qresults.setAssembleDescriptor(assdesc);
          QMIterator iterator = qresults.assemble();
          while (iterator.hasNext()) {
          Object object = iterator.next();
          System.out.println(object.toString());
          }
          qresults = queryManager.execute(qo);
          set Factory
          qresults.setAssembleDescriptor(assdesc);
          iterator = qresults.assemble();
          while (iterator.hasNext()) {
          Object object = iterator.next();
          System.out.println(object.toString());
          }
          }
      */
}
