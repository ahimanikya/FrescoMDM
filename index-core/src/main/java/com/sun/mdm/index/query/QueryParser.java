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


import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import com.sun.mdm.index.objects.metadata.MetaDataService;


/**
 * This abstract QueryParser provides helper methods that can be used to parse a
 * QueryObject. The concrete Parsers should subclass this class. The actual
 * parsing method parse() should be implemented by the concrete class.
 * @author sdua
 */
 abstract class QueryParser {
    
    private QueryObject mqo;
    private String mroot;


    /**
     * create a new QueryParser for this QueryObject.
     *
     * @param qo QueryObject that needs to be parsed.
     */
     QueryParser(QueryObject qo) {
        mqo = qo;
    }


    /**
     * parse a QueryObject and return Array of SQLDescriptor. Client needs to
     * call parse() after creating the QueryParser object.
     * @return SQLDescriptor[] each SQLDescriptor contains a seperator SQL query. 
     * @exception QMException QMException
     */
     abstract SQLDescWithBindParameters[] parse()
        throws QMException;


    QueryObject getQueryObject() {
        return mqo;
    }


    String getRoot() {
        return mroot;
    }


    /*
          return the root object of all the objects specified in both selectFields
          and in conditions
          Say if selectFields are:
          Enterprise.SystemSBR.EUID, Enterprise.SystemSBR.Person.personid,
          Enterprise.SystemSBR.Person.firstName
          Conditions: Enterprise.SystemSBR.Person.Address.city = 'Monrovia'
          Then the tree of objects is
          Enterprise.SystemSBR
          Enterprise.SystemSBR.Person   Enterprise.SystemSBR.Person.Address
          So the root object would be Enterprise.SystemSBR
      */
    
    
    String getRoot(QualifiedField[] selectFields, Condition[] conditions) {
    
        mroot = mqo.getRoot();

        if (mroot != null) {
            return mroot;
        }

        ArrayList qualObjects = new ArrayList();
        HashSet objects = new HashSet();

        for (int i = 0; i < selectFields.length; i++) {
            String objectName = selectFields[i].getObject();

            if (!objects.contains(objectName)) {
                objects.add(objectName);
            }
        }

        if ( conditions != null ) {
            for (int i = 0; i < conditions.length; i++) {            
 
                ConditionBlockIterator itr = new ConditionBlockIterator(conditions[i]);
            
                while ( itr.hasNext()) {
                    Condition condition = (Condition)itr.next();       

                    String field = condition.getField();
                    QualifiedField qfield = new QualifiedField(field);
                    String objectName = qfield.getObject();

                    if (!objects.contains(objectName)) {
                        objects.add(objectName);
                    }
                }
            }
        }

        /*
              so now at this point, we have obtained all the different objects
              in selectFields and conditions
          */
        Iterator it = objects.iterator();

        while (it.hasNext()) {
            String object = (String) it.next();
            qualObjects.add(new QualifiedObject(object));
        }

        // getCommonParent among all the qualObjects returns the root.
        mroot = QualifiedObject.getCommonParent(qualObjects);

        return mroot;
    }


    void setQueryObject(QueryObject qo) {
        mqo = qo;
    }


    void checkForInvalidObjects(HashSet objects, QPath[] qpaths)
        throws QMException {
        boolean found = false;
        Iterator it = objects.iterator();

        while (it.hasNext()) {
            found = false;

            String object = (String) it.next();

            for (int i = 0; i < qpaths.length; i++) {
                if (qpaths[i].contains(object)) {
                    found = true;

                    break;
                }
            }

            if (!found) {
                throw new QMException(object + " is not valid object name");
            }
        }
    }


ConditionMap[] createConditionMap(HashSet objects, QPath[] qpaths,
            Condition[] conditions) {


        if ( conditions == null ) {
            return null;           
        }
        
        ConditionMap[] conditionsMap = new ConditionMap[conditions.length];

        for (int i = 0; i < conditions.length; i++) {
            conditionsMap[i] = new ConditionMap();

            ConditionBlockIterator itr = new ConditionBlockIterator(conditions[i]);
        
            while ( itr.hasNext()) {
                Condition condition = (Condition)itr.next();       
                conditionsMap[i].add(condition);
            }

            conditionsMap[i].setLeafNode(qpaths);
        }

        return conditionsMap;
    }


    /**
     * This is the actual method that creates the QPath[] each of which is a
     * path from root to a different leaf. So if objectsHS contain objects which
     * belong to object graph: System Person Address Phone Alias, then QPaths
     * would be { System, Person, Address }, { System, Person, Phone }, {
     * System, Person, Alias } This method uses a variation of Breadth First
     * Search algorithm to create SQL paths.
     *
     * @param root root of the tree that contains objects
     * @param objectsHS HashSet contains the objects.
     */
    QPath[] createQPath(String root, HashSet objectsHS) {
        LinkedList bfsqueue = new LinkedList();
        ArrayList qPathList = new ArrayList();

        // It should really create QPath and not MultiQPath, but due to bug
        // in cloning, (new QPath) does not work correctly when creating select fields.
        //  This should be later fixed.
        QPath qpath = new MultiQPath();
        qpath.add(root);
        qPathList.add(qpath);

        BFSNode node = new BFSNode(root, qpath);
        bfsqueue.addFirst(node);

        while (!bfsqueue.isEmpty()) {
            node = (BFSNode) bfsqueue.removeFirst();
            qpath = node.mqpath;

            boolean newQPathflag = false;

            String[] children = MetaDataService.getChildTypePaths(node.mobject);

            for (int i = 0; (children != null) && (i < children.length); i++) {
                String child = children[i];

                // if the child (as defined by MetaDataService) of an object is
                // actaully being queried, then
                // we consider it for QPath
                if (objectsHS.contains(child)) {
                    if (!newQPathflag) {
                        qpath.add(child);
                        newQPathflag = true;
                        node = new BFSNode(child, qpath);
                        bfsqueue.addLast(node);
                    } else {
                        QPath newQPath = qpath.cloneLessLeaf();
                        newQPath.add(child);
                        qPathList.add(newQPath);
                        node = new BFSNode(child, newQPath);
                        bfsqueue.addLast(node);
                    }
                }
            }
        }

        return (QPath[]) qPathList.toArray(new QPath[qPathList.size()]);
    }


    // return all the different objects specified in the selectFields.
    HashSet findObjects(QualifiedField[] selectFields) {
        HashSet objects = new HashSet();

        for (int i = 0; i < selectFields.length; i++) {
            addNewObject(objects, selectFields[i]);
        }

        return objects;
    }


    /*
          return the union of different objects specified in selectFields and in conditions.
          Note: It is possible to specify some conditions on objects which are not
          supposed to be retrieved in the SQL. In other words conditions[][] can contain
          objects none of whose fields are specified in selectFields.
      */

    HashSet findObjects(QualifiedField[] selectFields, Condition[] conditions) {
        HashSet objects = new HashSet();

        for (int i = 0; i < selectFields.length; i++) {
            addNewObject(objects, selectFields[i]);
        }

        if ( conditions != null ) {
            for (int i = 0; i < conditions.length; i++) {
                ConditionBlockIterator itr = new ConditionBlockIterator(conditions[i]);  
                while ( itr.hasNext()) {
                    Condition cond = (Condition)itr.next();       
                    QualifiedField qfield = new QualifiedField(cond.getField());
                    addNewObject(objects, qfield);
                }
            }
        }

        return objects;
    }


    /*
          convert  select string fields to QualifiedField object
      */
    QualifiedField[] qualify(String[] selectFields) throws QMException {
    	List fieldList = new ArrayList();
    	
    	for (int i=0; i < selectFields.length; i++) {
          String field = selectFields[i];
          /*
           *  If fields are in form Person.*, then retrieve all fields
           *  for Person from MetaDataService
           */
    	  if ( field.endsWith(".*")) {
  		    int lastindex = field.lastIndexOf('.');
  	        String object = field.substring(0, lastindex);
  	        try {
  	           String fields[] = MetaDataService.getFieldPaths(object);
  	           for ( int j=0; j < fields.length; j++) {
  	              fieldList.add(fields[j]);
  	           }
  	        } catch (Exception ex) {
  	          throw new QMException("Invalid Select field:" + field + ex);
  	        }
  	          	      	       
  		  } else {
  		     fieldList.add(selectFields[i]);
  		  }
    	}
        QualifiedField[] qualfields = new QualifiedField[fieldList.size()];

        for (int i = 0; i < qualfields.length; i++) {
            qualfields[i] = new QualifiedField((String)fieldList.get(i));
        }

        return qualfields;
    }
    
    /**
     * Returns full qualified path for root object primary key
     * @return
     * @throws QMException
     */
    String[] getRootId() throws QMException {
    	
    	String[] sFields = mqo.getSelectFields();

        // convert fully qualified field names to QualifiedField object.
        QualifiedField[] selectFields = qualify(sFields);

        Condition[] conditions = mqo.getConditionsUnion();
      
         String root = getRoot(selectFields, conditions);

         
          String[] key = MetaDataService.getPrimaryKey(root);
         if (key == null) {
         	 throw new QMException("Feature to retrieve Primary Key is not present");
         }
         String[] rootkeys = new String[key.length];
         for (int i =0; i < rootkeys.length; i++) {
            rootkeys[i] = root + "." + key[i];
         }
         return rootkeys;
    }


    /*
          if qfield object is not already in objects, then add it.
      */
    private void addNewObject(HashSet objects, QualifiedField qfield) {
        if (!objects.contains(qfield.mobject)) {
            objects.add(qfield.mobject);

            QualifiedObject qualObject = qfield.getQualifiedObject();
            ArrayList objectNames = qualObject.getDescendants(mroot);

            for (int i = 0; i < objectNames.size(); i++) {
                String obName = (String) objectNames.get(i);

                if (!objects.contains(obName)) {
                    objects.add(obName);
                }
            }
        }
    }


    private static class BFSNode {
        String mobject;
        QPath mqpath;


        BFSNode(String object, QPath qpath) {
            mqpath = qpath;
            mobject = object;
        }
    }
}
