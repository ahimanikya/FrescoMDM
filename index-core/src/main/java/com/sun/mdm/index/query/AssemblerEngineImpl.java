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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;
import com.sun.mdm.index.util.Localizer;

/**
 * This AssemblerEngine is responsible to initialize the data structures that
 * are used during Assembling. It holds reference to Array of QueryResultSet and
 * call them in a loop until the next root composite object has been fetched and
 * assembled. The actual assembling for each JDBC resultset is delegated to
 * QueryResultSet.
 *
 * @author sdua
 */
public class AssemblerEngineImpl implements AssemblerEngine, Cloneable {
    
    private AssembleDescriptor massDesc;
    /*  
     * copies of mcompileAssObjectStates. This will have the actual Assemble
     * Object state during run time.
     */
    private AssembleObjectState[] massObjectStates;

    /* holds reference to AssmebleObjectState for each object in object tree,
    that is not a root and has more than one child. This list once created is never 
     *modified. So doesnot contain run time data but is used for cloning.
     **/
    private AssembleObjectState[] mcompileAssObjectStates;
    private Connection mconnection;
    
    // Used to determine if the database connection is to be closed by 
    // AssemblerEngineImpl.  It's set to true in this case.  It's set to false 
    // if some other calling class is to close the database connection instead.
    // Close by default to mimic current operations (i.e. backwards-
    // compatibility).
    private boolean mCloseDbConnection = true;  
    
    private CreateObjectList[] mcreateObjectList;
    private QueryResultSet[] mqrsets;
    private Statement mstatement;
    private Statement[] mstatements;
    private Object rootCache;
    

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    
    
    /**
     * Creates a new instance of AssemblerEngine
     */
    public AssemblerEngineImpl() {
    }


    /**
     * clones the AssemblerEngine
     * @return clone of this engine
     * @throws CloneNotSupportedException CloneNotSupportedException
     */
    public Object clone()
        throws CloneNotSupportedException {
        AssemblerEngineImpl engine = (AssemblerEngineImpl) super.clone();
        engine.massObjectStates = (AssembleObjectState[]) mcompileAssObjectStates.clone();

        return engine;
    }


    /**
     * close the underlying connection.
     *
     * @throws QMException QMException
     */
    public void close()
        throws QMException {
        try {
            if (mconnection != null) {
               for (int i = 0; i < mqrsets.length; i++) {
                   mqrsets[i].close();
                   mstatements[i].close();
               }
               if (mCloseDbConnection == true) {
                   mconnection.close();
                   mconnection = null;
               }
            }
        } catch (SQLException sqe) {
        	try {
        		/*
        		 * if exception is thrown when closing resultset/statement
        		    so still close connection
        		**/
        		if (mconnection != null) {
                            if (mCloseDbConnection == true) {
                                mconnection.close();
                                mconnection = null;
                            }
        		}
        	} catch (Exception ex){
        		throw new QMException(ex);
        	} 
            throw new QMException(sqe);
        }
    }


    /**
     * finalizer closes the database connection
     *
     * @throws QMException QMException
     */
    public void finalizer()
        throws QMException {
        try {
            super.finalize();

            if (mconnection != null) {
                if (mCloseDbConnection == true) {
                    mconnection.close();
                }
            }
        } catch (SQLException sqe) {
            throw new QMException(sqe);
        } catch (Throwable th) {
            throw new QMException(th);
        }
    }


   


    /**
     * part of compilation is to create CreateObjectList and create AssembleObjectState
     * These will be used during run time execution of engine.
     * @param sqlDesc SQLDescriptor[]
     * @param assDesc AssembleDescriptor
     * @throws QMException QMException
     */
    public void initCompile(SQLDescriptor[] sqlDesc, AssembleDescriptor assDesc)
        throws QMException {
        try {
            massDesc = assDesc;
            mcreateObjectList = createBFSobjectCreateList(assDesc, sqlDesc);
            mcompileAssObjectStates = setChildrenRelations(assDesc,
                    mcreateObjectList);
        } catch (Exception sqe) {
            mLogger.warn(mLocalizer.x("QUE001: initCompile() failed: {0}", sqe.getMessage()));
            throw new QMException(sqe);
        }
    }


    /**
     * This initialises data structures that are used during assembling.
     *
     * @param con JDBC DataSource Connection  
     * @param resultSets ResultSet[] that
     * contain multiple ResultSets executed by QueryManager
     * @param maxRows Maximum Rows
     * @throws QMException QMException
     */
    public void initRun(Connection con, ResultSet[] resultSets, Statement[] statements, int maxRows )
            throws QMException {
        initRun(con, resultSets, statements, maxRows, true);
    }

    /**
     * This initialises data structures that are used during assembling.
     *
     * @param con JDBC DataSource Connection  
     * @param resultSets ResultSet[] that
     * contain multiple ResultSets executed by QueryManager
     * @param maxRows  maximum number of rows
     * @param closeDbConnection  set to true if the database connection is to be 
     * closed by the AssemblerEngineImpl, false if some other calling class is to 
     * close the database connection instead
     * @throws QMException QMException
     */
    public void initRun(Connection con, ResultSet[] resultSets, Statement[] statements, 
                        int maxRows, boolean closeDbConnection)
            throws QMException {
        try {
            mconnection = con;
            mCloseDbConnection = closeDbConnection;
            mstatements = statements;
            massObjectStates = (AssembleObjectState[]) mcompileAssObjectStates.clone();
            mqrsets = createQueryResultSets(resultSets, mcreateObjectList,
                    massObjectStates, maxRows);
        } catch (SQLException sqe) {
            mLogger.warn(mLocalizer.x("QUE002: initRun() failed: {0}", sqe.getMessage()));
            throw new QMException(sqe);
        }
    }
    /**
     * returns true if there is another composite value object else returns
     * false.
     * @return true or false
     * @throws QMException QMException
     */
    public boolean hasNext()
        throws QMException {
        /*
         *  hasNext uses next() to determine if there are more objects. But when it uses next, it should cache the
         * object so that using next() should returned this cached object
         */
    	if (rootCache == null) {
    		rootCache = next();
    	}
    	if ( rootCache != null) {
    		return true;
    	} else {
    		return false;
    	}
    }

    /**
     * get the next Root Value object.
     * @return return next Root of Composite Value Object
     * @throws QMException QMException
     */
    public Object next()
        throws QMException {
    	Object root = null;
        try {
        	if ( rootCache != null){
        		 root = rootCache;
        		 rootCache = null;
        		return root;
        	}
        	
            for (int i = 0;
                    (massObjectStates != null)
                    && (i < massObjectStates.length); i++) {
                massObjectStates[i].init();
            }

            // The first QueryResultSet creates the root object.
            root = mqrsets[0].next();
            if ( root != null) {

            // iterate through each QueryResultSet.
               for (int i = 1; i < mqrsets.length; i++) {
                  mqrsets[i].setRoot(root, mqrsets[0].getRootKeyValues());
                  // Every QueryResultSet returns back the same root, or null if it finds partial Root in its
                  // ResultSet
                  root = mqrsets[i].next();
                  if ( root == null) {
                  	break;
                  }
               }
            }

            return root;
        } catch (SQLException sqe) {
            throw new QMException(sqe);
        }
    }

    /** Sets the value of mCloseDbConnection.
     * 
     * @param val  value to which mCloseDbConnection is to be set.
     */
    public void setDbConnectionCondition(boolean val) {
        mCloseDbConnection = val;
    }
    
    /** Gets the value of mCloseDbConnection.
     * 
     * @return  value of mCloseDbConnection.
     */
    public boolean setDbConnectionCondition() {
        return mCloseDbConnection;
    }


    /*
          This sets the object parent child relationships in the CreateObjectList[].
          The relationship is done either in CreateObjectMeta.mchildIndex or
          by setting CreateObjectMeta.mparentObjectState. In the process it also creates
          AssembleObjectState[] for objects which are not root and which have more than
          one child.
      */
    private AssembleObjectState[] setChildrenRelations(
            AssembleDescriptor assDesc, CreateObjectList[] createObjectLists) {
        ArrayList assembleObjectList = new ArrayList();

        boolean assembleObjectFlag = false;

        for (int i = 0; i < createObjectLists.length; i++) {
            CreateObjectList createObjectList = createObjectLists[i];

            // the last object is always leaf, so would not have children
            for (int j = 0; j < (createObjectList.size() - 1); j++) {
                assembleObjectFlag = false;

                AssembleObjectState assObjState = null;
                CreateObjectMeta cObjMeta = createObjectList.get(j);
                ValueMetaNode valueNode = cObjMeta.getValueMetaNode();
                ValueMetaNode[] children = valueNode.getChildren();

                // so we set AssembleState only if it is not root and has more than
                // one child. So that means that its children are in different resultsets
                // so we maintain the state of parent. If it is root, then it is single
                // value so don't need AssembleObjectState for that.
                if ((valueNode.getParent() != null) && (children.length > 1)) {
                    assembleObjectFlag = true;
                    assObjState = new AssembleObjectState(valueNode.getName());
                    assembleObjectList.add(assObjState);
                    cObjMeta.setAssembleObjectState(assObjState);
                }

                for (int k = 0; k < children.length; k++) {
                    // so for each child of CreateObjectMeta
                    ValueMetaNode child = children[k];
                    String valueName = child.getName();

                    // create AssembleObjectState
                    for (int l = 0; l < createObjectLists.length; l++) {
                        int childIndex = createObjectLists[l].getIndex(valueName);

                        if (childIndex >= 0) {
                            // so this createObjectLists[l] contains
                            // the child ValueMetaNode
                            if (assembleObjectFlag) {
                                // so the child is in the same CreateObjectList and so we
                                // safely sets its children index, because parent and child
                                // are retrieved in the same jdbc result set.
                                if (l == i) {
                                    cObjMeta.setChildIndex(childIndex);
                                } else {
                                    CreateObjectMeta childMetaObj = createObjectLists[l].get(childIndex);
                                    childMetaObj.setParentObjectState(assObjState);
                                }
                            }  else if (l == i) {  //set children indices only if they are in same list
                                
                                cObjMeta.setChildIndex(childIndex);

                                break;
                            }
                        }
                    }
                }
            }
        }

        return (AssembleObjectState[]) assembleObjectList.toArray(new AssembleObjectState[assembleObjectList.size()]);
    }


    /*
     convert ValueMetaNode graph to list of objects lists. Each CreateObjectList
     correspond for each JDBC result. The objects in CreateObjectList are in the
    order in which they need to be created as specified in ValueMetaNode.
    */
     private CreateObjectList[] createBFSobjectCreateList(
            AssembleDescriptor assDesc, SQLDescriptor[] sqlDesc) {
        // create a single BFS list.
        ArrayList createAllObjectList = createBFSobjectList(assDesc);

        CreateObjectList[] createObjectList = new CreateObjectList[sqlDesc.length];

        // Now for each SQLDescriptor, we create a list that contains objects
        //  in the SQLDescriptor and the order in this new list has the same
        // order as in createAllObjectList.
        for (int i = 0; i < sqlDesc.length; i++) {
            createObjectList[i] = new CreateObjectList();

            for (int j = 0; j < createAllObjectList.size(); j++) {
                CreateObjectMeta cobjMeta = (CreateObjectMeta) createAllObjectList.get(j);
                String objName = cobjMeta.getObjName();

                if (sqlDesc[i].contains(objName)) {
                    createObjectList[i].add(cobjMeta);
                    sqlDesc[i].copyTo(cobjMeta);
                }
            }
        }

        return createObjectList;
    }


    /**
     * This converts the ValueMetaNode graph from the input AssembleDescriptor
     * to single list using Breadth First Search Algorithm. So node in the left
     * are parents and at right are children. Say if the value object graph is
     * System Person Account Address Phone Alias Loan then this is converted
     * into BFS list of {System, Person, Account, Address, Phone, Alias, Loan}
     * This is the order in which objects need to be created.
     *
     * @param assDesc
     */
    private ArrayList createBFSobjectList(AssembleDescriptor assDesc) {
        ArrayList createAllObjectList = new ArrayList();
        LinkedList bfsqueue = new LinkedList();
        ValueMetaNode node = assDesc.getRoot();
        CreateObjectMeta cObjMeta = new CreateObjectMeta(node);

        //  cObjMeta.setValueMetaNode(node);
        createAllObjectList.add(cObjMeta);
        bfsqueue.addFirst(node);

        while (!bfsqueue.isEmpty()) {
            node = (ValueMetaNode) bfsqueue.removeFirst();

            ValueMetaNode[] children = node.getChildren();

            for (int i = 0; (children != null) && (i < children.length); i++) {
                node = children[i];
                cObjMeta = new CreateObjectMeta(node);
                createAllObjectList.add(cObjMeta);
                bfsqueue.addLast(node);
            }
        }

        return createAllObjectList;
    }


    /*
          creates QueryResultSet for each SQLDescriptor. In other words
          creates QueryResultSet for each SQL statement.
      */
    private QueryResultSet[] createQueryResultSets(ResultSet[] resultSets,
            CreateObjectList[] createObjectLists,
            AssembleObjectState[] assObjectStates, int maxRows)
        throws SQLException {
        boolean createRootFlag = true;
        QueryResultSet[] qrsets = new QueryResultSet[createObjectLists.length];

        for (int i = 0; i < resultSets.length; i++) {
            int size = createObjectLists[i].size();
            CreateObjectMeta[] createObjectMetaList = new CreateObjectMeta[size];

            for (int j = 0; j < size; j++) {
                createObjectMetaList[j] = createObjectLists[i].get(j);
            }

            qrsets[i] = new QueryResultSet(resultSets[i], createObjectMetaList,
                    massDesc.getAssembler(), assObjectStates,
                    createRootFlag, maxRows);
            createRootFlag = false;
        }

        return qrsets;
    }
    
  
}
