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

import com.sun.mdm.index.util.Localizer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.CreateException;
import java.util.List;
import java.util.ArrayList;


/**
 * Splits the QueryObject into multiple 
 * QueryObjects and invokes QueryManager to execute and 
 *  return back QMIterator
 * 
 * QueryPipeLine executes the query in these stages.

•	1:  create QueryObject to select only primary keys  for primary object. 
   Invoke QueryManager.execute() to get ids. 

•	2:  Divide the ids received from step 1 into multiples of fetchsize
  and create Queryobjects based on the original queryobject 
  received in step 1. Add IN condition containing fetch ids to 
  the original queryobject and invoke QueryManager.executeAssemble. 

  It replaces the AssemblerEngine in QMIterator received from 
  QueryManager.execute by itself.
 Acting as AssemblerEngine, QueryPipeLine delegates 
 its AssmblerEngine methods  invocations to the 
  original AssemblerEngine, specified in the QueryObject. 
  
  So QueryPipeLine acts as lazy query executer that executes 
  more queryobjects when set of data returned by previous 
  QueryObject is exhausted.

 * 
 * 
 * 
 * 
 * @author sdua
 */
class QueryPipeLine implements AssemblerEngine {
        
    private transient final Localizer mLocalizer = Localizer.get();

    private AssemblerEngine assEngineDelegate;
    private int fetchSize = 1000; 
        
   private QueryManager mQueryMgr;
   private int idsPos; // current id position n ids list that is used
   // to execute retreive next set of objects
   private List ids = new ArrayList(); // list of all ids in 1st step
   private int sumIds; // total ids retrieved in 1st step
   private QueryObject originalQO;
   private String[] rootid;

    /*  
     */
    
    QueryPipeLine() 
	 throws CreateException {
      try {
      	mQueryMgr = QueryManagerFactory.getInstance();
      	
      } catch (Exception e) {
        throw new CreateException(mLocalizer.t("QUE553: Could not retrieve a " +
                                    "QueryPipeLine instance: {0}", e));
      }
    }
    
    

    
    /**
     * This method will execute the query(s) specified in QueryObject and also
     * use FactoryEngine for creating composite objects.
     *
     * @param qobject QueryObject that contains description of query
     * @exception QMException QMException
     * @return QMIterator iterator that is used to fetch each composite object.
     */
    
     QMIterator executeAssemble(QueryObject qobject)
        throws QMException {
        
            
            originalQO = qobject;
            
           
             QueryObject qo2 = createPrimayIDQO(qobject);

            
            int ftsize = qobject.getFetchSize();
            if (ftsize > 0) {
            	fetchSize = ftsize;
            }
            rootid = qo2.getRootId();
            
            QueryResults qresults = mQueryMgr.execute(qo2);
            qresults.setAssembleDescriptor(qo2.getAssembleDescriptor());
            QMIterator iterator = qresults.assemble();
                                
            while (iterator.hasNext()) {
               Object obj = iterator.next();
                ids.add(obj);	
            }
            
            
            
            sumIds = ids.size();
            
            int size = sumIds < fetchSize ? sumIds : fetchSize;
            
            Object[] values = new Object[size];
            for ( int i = 0; i < size; i++ ){
                values[i] =  ids.get(i);
            }
            idsPos = size;
                               
            QueryObject qo = addINConditionToQO(originalQO, rootid, values);
            iterator = mQueryMgr.executeAssemble(qo);
            
            /*
             *  QueryPipeLine acts as AssemblerEngine for the iterataor and delegates the calls to the original
             *  AssemblerEngine stored in assEngineDelegate.
             *  So this way when there is no more data retrieved from current QueryObject and there are still ids
             * let in ids, it can issue another query. So it is using policy of Lazily executing queries.
             */
            
            assEngineDelegate = getOriginalAssemblerEngine(iterator);
            iterator.setAssemblerEngine(this);
            
            return iterator;
        
    }
     
     /**
      * This will close the JDBC resultsets.
      *
      * @exception QMException QMException
      */
     public void close()
         throws QMException {
     	assEngineDelegate.close();
     }


     /** 
      *  returns true or false if next object can be retrieved
      * @return true or false
      * @exception QMException QMException
      * 
      */
     public boolean hasNext()
         throws QMException {
         boolean more = assEngineDelegate.hasNext();

         if (!more) {
         	assEngineDelegate.close();
         	if (idsPos < sumIds){
         		/*
         		 *  so we can still fetch more data, so execute 
         		 *  another query
         		 */
         		executeNextQuery();
         		more = assEngineDelegate.hasNext();
         		if (!more) {
                 	assEngineDelegate.close();
         		}
         		
         	}
            
         }

         return more;
     }


     /**
      * This will retrieve next composite object constructed by AssemblerEngine.
      * The subclass of QMIterator should call super.next() to retrieve next
      * composite object.
      * @return next Object
      *
      * @exception QMException QMException
      */
     public Object next()
         throws QMException {
         Object object = assEngineDelegate.next();

         if (object == null) {
         	assEngineDelegate.close();
         	if (idsPos < sumIds){
         		executeNextQuery();
         		object = assEngineDelegate.next();
         		if (object == null) {
         			assEngineDelegate.close();
         		}
         		
         	}
         }

         return object;
     }


     /*
      *  THis method initialises the data structures that can be shared and cached
      *  (only readable data structures)
      */
     public void initCompile(SQLDescriptor[] sqlDesc, AssembleDescriptor assDesc)
         throws QMException {
      

     	assEngineDelegate.initCompile(sqlDesc, assDesc);
       //  mrootObjectName = sqlDesc[0].getRoot();
     }

     /*
      *This method initialises the data structures that are used during run time and can 
      * be writeable.
     */

    public void initRun(Connection con, ResultSet[] resultSets, Statement[] statements, int maxRows)
            throws QMException {
        
        initRun(con, resultSets, statements, maxRows, true);
    }

    /**
     * This method initializes the data structures that are used during run time and can 
     * be writeable.
     *
     * @param con Connection
     * @param resultSets ResultSet[]
     * @param statements Statement[]
     * @param maxRows maximum number of rows
     * @param closeDbConnection  set to true if the database connection is to be 
     * closed by the AssemblerEngine, false if some other calling class is to close 
     * the database connection instead.
     * @throws QMException QMException
     */
    public void initRun(Connection con, ResultSet[] resultSets, Statement[] statements, 
                        int maxRows, boolean closeDbConnection)
            throws QMException {
                
     	assEngineDelegate.initRun(con, resultSets, statements, maxRows, closeDbConnection);
    }
    
    /**
     * clones the AssemblerEngine
     * @return clone of this engine
     * @throws CloneNotSupportedException CloneNotSupportedException
     */
    public Object clone()
        throws CloneNotSupportedException {
        QueryPipeLine engine = (QueryPipeLine) super.clone();
        /*
         *  We are only interested in cloning the assEngineDelegate
         */
        engine.assEngineDelegate = (AssemblerEngine)this.assEngineDelegate.clone();
        
        return engine;
    }
    
    private QueryObject createPrimayIDQO(QueryObject qobject) throws QMException {
    	String[] rootid =  qobject.getRootId();
    	QueryObject qo2 = new QueryObject();
    	qo2.setSelect(rootid);
    	/*
    	 * we are assuming that rootid contains only one field
    	 * assembler below needs to be modified to support
    	 * rootid containing more than one field
    	 */
    	AssembleDescriptor aDesc = new AssembleDescriptor();
    	aDesc.setAssembler(new ResultObjectAssembler() {
    	    public void   init() {}
    	    public Object createRoot(String objectName, AttributesData attrsData)
    	     throws VOAException
    	    {
    	      try {
    	       return attrsData.get(0);
    	      } catch (SQLException sqe) {
                throw new VOAException(mLocalizer.t("QUE554: createPrimayIDQO() failed: {0}", sqe));
              }
    	    }
             
            public Object createObjectAttributes(Object rootObject, Object parent,
               String objectName, AttributesData attrsData)
            {
              return rootObject;
            }
    	
    	 }
    	);
    	qo2.setAssembleDescriptor(aDesc);
    	qo2.setQueryOption(QueryObject.SINGLE_QUERY);	
    	qo2.setMaxRows(qobject.getMaxObjects());
    	return qo2;
    }
    
    private QueryObject addINConditionToQO(QueryObject qobject, String[] rootid, Object[] values) {
    
      QueryObject qo = new QueryObject(qobject);
      qo.addCondition(new Condition(rootid[0], "IN", values) );
      qo.setMaxRows(-1);
      qo.setMaxObjects(-1);
      return qo;
    }
    
    /*
     *  executes another Query fetching objects in the next size range.
     *  size is computed below
     */
    private boolean executeNextQuery() throws QMException {
    	 int size = (sumIds - idsPos) < fetchSize ? (sumIds -idsPos) : fetchSize;
    	 if (size == 0) {
    	 	return false;
    	 }
         
         Object[] values = new Object[size];
         for ( int i = 0; i < size; i++ ){
             values[i] =  ids.get(idsPos+i);
         }
         
         idsPos += size;
                    
         QueryObject qo = addINConditionToQO(originalQO, rootid, values);
         
         QMIterator iterator = mQueryMgr.executeAssemble(qo);
         assEngineDelegate = getOriginalAssemblerEngine(iterator);
    
         return iterator.hasNext();
    	
    }
    
    /**
     * An Iterator's assemblerEngine may be already susbstituted by QueryPipeLine(in case
     * we get the QMIterator from QueryObjectCache). So we want to get the
     * AssemblerEngine of an Iterator, that was set before replacement by QueryPipleLine. 
     * So we want to get AssemblerEngine that does the core Assembling process. 
     * Otherwise we might end up in an endless recursion of executing queries
     * @param iterator
     * @return
     */
    AssemblerEngine getOriginalAssemblerEngine(QMIterator iterator) {
       AssemblerEngine engine = iterator.getAssemblerEngine();
       while (engine instanceof QueryPipeLine) {
          engine = ((QueryPipeLine)engine).assEngineDelegate;	
       }
       return engine;
    }

}
