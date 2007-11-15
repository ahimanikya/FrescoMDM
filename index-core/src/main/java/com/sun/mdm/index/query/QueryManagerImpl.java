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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.util.Constants;
import com.sun.mdm.index.util.ConnectionUtil;
import com.sun.mdm.index.objects.metadata.ObjectFactory;



/** Impl class for QueryManager
 * @author sdua
 */
public class QueryManagerImpl implements QueryManager {
        
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    
    private static int connectionCounter = 0;
   
    private final QueryObjectCacheManager qcManager =  ThreadSafeQueryObjectCacheManager.getInstance();

    /**
     * Creates a new instance of QueryManagerImpl
     */
    public QueryManagerImpl() {
    }



    /**
     * This method will execute the query(s) specified in QueryObject but will
     * not use Assembling information. The assembling information can be later
     * specified to the returned QueryResults object.
     *
     * @param qobject QueryObject that contains description of query
     * @return QueryResults should be later set with an AssembleDescriptor that
     *      will be used to assemble composite objects.
     * @throws QMException QMException
     */
    public QueryResults execute(QueryObject qobject)
            throws QMException {
                
        return execute(null, qobject);
    }    

    /**
     * This method will execute the query(s) specified in QueryObject but will
     * not use Assembling information. The assembling information can be later
     * specified to the returned QueryResults object.
     *
     * @param con The database connection used for this operation.
     * @param qobject QueryObject that contains description of query
     * @return QueryResults should be later set with an AssembleDescriptor that
     *      will be used to assemble composite objects.
     * @throws QMException QMException
     */
    public QueryResults execute(Connection con, QueryObject qobject)
            throws QMException {
                
        boolean closeDbConnection = false;
        try {

            // Get the connection if necessary 
            // This connection must be closed by the QMIterator via 
            // the AssemblerEngine.
            if (con == null) {
                con = getConnection();
                closeDbConnection = true; 
            }
            
           
            ConnectionUtil.initDBProductID(con);
        	 if (qobject.getMaxObjects() > 0 ) {
        	 	/*
        	 	 * conversion of maxobjects to maxrows for tuples
        	 	 * is just copying maxobjects to maxrows
        	 	 * 
        	 	 */
              	qobject.setMaxRows(qobject.getMaxObjects());
            }
            int queryOption = qobject.getQueryOption();

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("QueryObject:\n" + qobject);
            } 
           
            qobject.preInitializeForCacheComparision();
            
            SQLDescriptor[] sqlDesc = null;
            
            QueryObjectCache qc = qcManager.getQueryObjectCache(qobject);
            
            if (mLogger.isLoggable(Level.FINE)) {
            	if ( qc == null ) {
            		mLogger.fine(Thread.currentThread() + "No Match in  query cache found");
            	}
            	else {
            		mLogger.fine(Thread.currentThread() + "Match with cache found");            		
            	}
            } 

            if (qc == null) { 
                /*
                 so it is not in cache. so parse it.
                 */
            	qc = new QueryObjectCache(qobject, null,null);
            	
                sqlDesc = qobject.parse();
                
                 if (qobject.isPrepare()) {
                    qcManager.setQueryObjectCache(qc);
                }
               
            } else {
            	// Need to make the wrapper Around the 
            	sqlDesc = qc.getQueryObject().getSQLDescriptor();
            }

            int sqlSize = sqlDesc.length;

            if (mLogger.isLoggable(Level.FINE)) {
                for (int i = 0; i < sqlSize; i++) {
                    mLogger.fine("SQL description[" + i + "]: " + sqlDesc[i]);
                }
            }
            
            ResultSet[] resultSets = new ResultSet[sqlSize];

            Statement[] statements = null;
            if (qc.isPrepare()) {
                Condition[] conditions = qobject.getConditionsUnion();
                
                statements = prepareQuery(con, resultSets,
                        qc.getQueryObject(), conditions);

                for (int i = 0; i < sqlSize; i++) {
                    resultSets[i] = ((PreparedStatement)statements[i]).executeQuery();
                }
            } else {
                statements = executeQuery(con, resultSets, qc.getQueryObject());
            }


            return new QueryResults(con, resultSets, statements, sqlDesc, queryOption, closeDbConnection);
        } catch (Exception ex) {
            throw new QMException(mLocalizer.t("QUE546: execute() failed: {0}", ex));
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
    public QMIterator executeAssemble(QueryObject qobject)
            throws QMException {
        return executeAssemble(null, qobject);
    }    

    /**
     * This method will execute the query(s) specified in QueryObject and also
     * use FactoryEngine for creating composite objects.
     *
     * @param connection The database connection used for this operation.
     * @param qobject QueryObject that contains description of query
     * @exception QMException QMException
     * @return QMIterator iterator that is used to fetch each composite object.
     */
    public QMIterator executeAssemble(Connection con, QueryObject qobject)
            throws QMException {
        
        boolean closeDbConnection = false;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("QueryObject:\n" + qobject);
            }
            
            // Get the connection if necessary and database information
            if (con == null) {
                con = getConnection();
                closeDbConnection = true; 
            }
            ConnectionUtil.initDBProductID(con);
            
          if (qobject.getMaxObjects() > 0  && !qobject.containsCondition()) {
          	QueryPipeLine queryPipeLine = new QueryPipeLine();
          	return queryPipeLine.executeAssemble(qobject);
          }
               
  
            qobject.preInitializeForCacheComparision();
            
            SQLDescriptor[] sqlDesc = null;
            AssembleDescriptor assDesc = qobject.getAssembleDescriptor();
            
            //QueryObjectCache qc = getQueryObjectCache(qobject);
            QueryObjectCache qc = qcManager.getQueryObjectCache(qobject);
            
            if (mLogger.isLoggable(Level.FINE)) {
            	if ( qc == null ) {
                    mLogger.fine(Thread.currentThread() + " @@ No Match in  query cache found");
            	}
            	else {
            		mLogger.fine(Thread.currentThread() + " @@ Match with cache found");         		
            	}
            } 

            QMIterator qmIterator = null;
 
            if (qc == null) {
                 /*
                 so it is not in cache. so parse it.
                 */
                if (qmIterator == null) {
                    qmIterator = new QMIterator();

                }

            	qc = new QueryObjectCache(qobject, qmIterator,null); 
            	sqlDesc = qobject.parse();
                // All sqlDesc have same root.
                String root = sqlDesc[0].getRoot();

                assDesc.setDefaultResultType(new ObjectNodeMetaNode(root));
                //  qmIterator = assDesc.getIterator();

                /*
                 * compile the QMIterator, so that any assembling data structures
                 * that are read only, can be reused.
                 */
                qmIterator.initCompile(sqlDesc, assDesc);
                
                
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Will this Query use prepared Statement & participate in Cache :  " + qobject.isPrepare());
                }

                if (qobject.isPrepare()) {
                	qcManager.setQueryObjectCache(qc);
                }
            } else {
            	
                qmIterator = (QMIterator) qc.getQMIterator();
                sqlDesc = qc.getQueryObject().getSQLDescriptor();
            }

            int sqlSize = sqlDesc.length;

            if (mLogger.isLoggable(Level.FINE)) {
                for (int i = 0; i < sqlSize; i++) {
                    mLogger.fine("SQL description[" + i + "]: " + sqlDesc[i]);
                }
            }

            ResultSet[] resultSets = new ResultSet[sqlSize];
            Statement[] statements = null;
            if (qc.isPrepare()) {
                
                Condition[] conditions = qobject.getConditionsUnion();
                statements = prepareQuery(con, resultSets,
                        qc.getQueryObject(), conditions);

                for (int i = 0; i < sqlSize; i++) {
                    resultSets[i] = ((PreparedStatement)statements[i]).executeQuery();
                }
            } else {
                statements = executeQuery(con, resultSets, qc.getQueryObject());
            }
            int maxRows = qobject.getMaxRows();

            /*
             * For each QMIterator, initRun needs to be called to initialize its
             * data structures that are writeable.
             */
            qmIterator.initRun(con, resultSets, statements, maxRows, closeDbConnection);
            
            return qmIterator;
        } catch (Exception ex) {
            throw new QMException(mLocalizer.t("QUE547: executeAssemble() failed: {0}", ex));
        }
    }

    /**
     * This method will prepare the QueryObject without executing the query
     * and will put in the cache pool.
     *
     * @param qobject QueryObject that needs to be prepared and cached.
     * @exception QMException QMException
     */
    
    public void prepare(QueryObject qobject)
        throws QMException {
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("QueryObject:\n" + qobject);
            }

            // Get the connection and database information
            Connection con = getConnection();
            ConnectionUtil.initDBProductID(con);
            SQLDescriptor[] sqlDesc = null;
            AssembleDescriptor assDesc = qobject.getAssembleDescriptor();
            QueryObjectCache qc = qcManager.getQueryObjectCache(qobject);
            
            if (mLogger.isLoggable(Level.FINE)) {
            	if ( qc == null ) {
            		mLogger.fine(Thread.currentThread() + " ## No Match in  query cache found");
            	}
            	else {
            		mLogger.fine(Thread.currentThread() + " ## Match with cache found");  		
            	}
            } 
            QMIterator qmIterator = null;
            int sqlSize = 0;

            if (qc == null) {
                //
                // we will create and compile the QMIterator
                //

                if (qmIterator == null) {
                    qmIterator = new QMIterator();
                   
                } else {
                    
                	qmIterator = qc.getQMIterator();
                }
            	
                qc = new QueryObjectCache(qobject, qmIterator,null);
            	sqlDesc = qobject.parse();
                sqlSize = sqlDesc.length;

                
                // All sqlDesc have same root.
                String root = sqlDesc[0].getRoot();

                assDesc.setDefaultResultType(new ObjectNodeMetaNode(root));
                

                
                qmIterator.initCompile(sqlDesc, assDesc);
                
                if (qobject.isPrepare()) {
                	qcManager.setQueryObjectCache(qc);
                }
            }

            if (mLogger.isLoggable(Level.FINE)) {
                for (int i = 0; i < sqlSize; i++) {
                    mLogger.fine("SQL description[" + i + "]: " + sqlDesc[i]);
                }
            }

            ResultSet[] resultSets = new ResultSet[sqlSize];

            //
            //Now after creating qc, needs to create Prepared statements.
            //
            if (qc.isPrepare()) {
                
    
                Condition[] conditions = qobject.getConditionsUnion();
                prepareQuery(con, resultSets, qc.getQueryObject(), conditions);
            }
        } catch (Exception ex) {
            throw new QMException(mLocalizer.t("QUE549: prepare() failed: {0}", ex));
        }
    }

    
   
    
    private Connection getConnection()
        throws Exception {
                Connection con = ConnectionUtil.getConnection();
                boolean autocommit = false;
                if (Constants.AUTOCOMMIT) {
                  autocommit = true;
                }

                con.setAutoCommit(autocommit);
                incrementConnectionCounter();
                return con;

    }
    
        
    
   
    private Statement[] executeQuery(Connection con, ResultSet[] resultSets,
            QueryObject qo)
        throws Exception {
        SQLDescriptor[] sqlDesc = qo.getSQLDescriptor();
        Statement[] statements = new Statement[sqlDesc.length];
        int type = qo.getResultSetType();

        for (int i = 0; i < sqlDesc.length; i++) {
            String sql = sqlDesc[i].getSQL();
            if (type > 0) {
                statements[i] = con.createStatement(type, ResultSet.CONCUR_READ_ONLY);
            } else {
                statements[i] = con.createStatement();
            }
            resultSets[i] = statements[i].executeQuery(sql);
        }
        return statements;
    }

    private PreparedStatement[] prepareQuery(Connection con,
            ResultSet[] resultSets, QueryObject qo, Condition[] conditions)
        throws Exception {
        SQLDescriptor[] sqlDesc = qo.getSQLDescriptor();
        PreparedStatement[] statements = new PreparedStatement[sqlDesc.length];
        List [] bindParams = qo.getCacheRef().getBindParamList();
        int type = qo.getResultSetType();
        
        for (int i = 0; i < sqlDesc.length; i++) {
            String sql = sqlDesc[i].getSQL();
            if (type > 0) {
                statements[i] = con.prepareStatement(sql, type, ResultSet.CONCUR_READ_ONLY);
            } else {
                statements[i] = con.prepareStatement(sql);
            }

            List bindParamLists = bindParams[i];

            Condition  cond = null;            
            int prepareIndex = 0;
            int size = 0;
            
            if ( bindParamLists != null ) {		
            	for ( int j=0; j < bindParamLists.size(); j++ )  {
                	cond = (Condition)bindParamLists.get(j);
            		size = cond.getParamSize();        
                        if ( cond.isInOperator() )  {
                            Object[] values = (Object[])cond.getValues();
                            for ( int k=0; k < size; k++ ) {                            
                            	Object value = values[k];
                                setParam(statements[i], ++prepareIndex, cond.getType(), value);
                                if (mLogger.isLoggable(Level.FINE)) {
                                    mLogger.fine(Thread.currentThread() + " cond =" + cond + " value = " + value);                     
                                }
                            }
                        }
                        else {
                             
                            Object value = cond.getValue();
                            setParam(statements[i], ++prepareIndex, cond.getType(), value);
                            if (mLogger.isLoggable(Level.FINE)) {
                                mLogger.fine(Thread.currentThread() + " cond =" + cond + " value = " + value);
                            }
                        }
                }
            }        
        }

        return statements;
    }  
    
    private void setParam(PreparedStatement statement, int prepareIndex, String type, Object value) 
                          throws Exception
    {
    	
    	if (value != null) {
    	  if (value instanceof Byte) {
    		statement.setByte(prepareIndex, ((Byte)value).byteValue());
    		
    	  } else if ( value instanceof java.util.Date ) {
            value = new java.sql.Date(((java.util.Date) value).getTime());
            statement.setObject(prepareIndex, value);
          //  sqlType = Types.DATE;
    	  } else if (value instanceof Boolean) {
    		statement.setBoolean(prepareIndex, ((Boolean)value).booleanValue());
          } else if (value instanceof Short) {
    		statement.setShort(prepareIndex, ((Short)value).shortValue());
          } else if (value instanceof Integer) {
    		statement.setInt(prepareIndex, ((Integer)value).intValue());
          } else if (value instanceof Double) {
    		statement.setDouble(prepareIndex, ((Double)value).doubleValue());
          } else if (value instanceof Float) {
    		statement.setFloat(prepareIndex, ((Float)value).floatValue());
          } else if (value instanceof Long) {
    		statement.setLong(prepareIndex, ((Long)value).longValue());
          } else if (value instanceof Character) {
              statement.setString(prepareIndex, ((Character)value).toString());       		
          } else {
    	  statement.setObject(prepareIndex, value);
         }
    	} else {
    		if (type.equals(ObjectFactory.VARCHAR2)) {
                statement.setNull(prepareIndex, java.sql.Types.VARCHAR);
            } else if (type.equals(ObjectFactory.BOOLEAN)) {
                statement.setNull(prepareIndex, java.sql.Types.BOOLEAN);
            } else if (type.equals(ObjectFactory.NUMBER)) {
                statement.setNull(prepareIndex, java.sql.Types.NUMERIC);
            } else if (type.equals(ObjectFactory.LONG)) {
                statement.setNull(prepareIndex, java.sql.Types.NUMERIC);
            } else if (type.equals(ObjectFactory.FLOAT)) {
                statement.setNull(prepareIndex, java.sql.Types.NUMERIC);
            } else if (type.equals(ObjectFactory.CHAR)) {
                statement.setNull(prepareIndex, java.sql.Types.CHAR);
            } else if (type.equals(ObjectFactory.DATE)) {
                statement.setNull(prepareIndex, java.sql.Types.DATE);
            } else if (type.equals(ObjectFactory.TIMESTAMP)) {
                statement.setNull(prepareIndex, java.sql.Types.TIMESTAMP);
            }
    		
    	}
    }
    
     static void incrementConnectionCounter() {
        connectionCounter++;
    }
    
      static void decrementConnectionCounter() {
        connectionCounter--;
    }
      
    static int getConnectionCounter() {
        return connectionCounter;
    }
}
