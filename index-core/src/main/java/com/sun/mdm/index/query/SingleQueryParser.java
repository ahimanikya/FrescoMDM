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

import java.util.HashSet;
import java.util.LinkedList;
import com.sun.mdm.index.objects.metadata.MetaDataService;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;


/**
 * This Parser parses a QueryObject and produces a single SQL statement. The SQL
 * statement would contain all the objects queried in QueryObject. Say if we
 * have an object graph like: System Person Address Phone Alias This would
 * produce SQL statement: select system.attributes, person.attributes,
 * address.attributes, phone.attribute, alias.attributes from System, person,
 * address, phone, alias where system.euid = person.euid and person.personid =
 * address.personid and person.personid = phone.personid and person.personid =
 * alias.personid The SQL statement created by it would produce lots of
 * redundant data as it would use cartesian product among secondary objects. So
 * this is more useful where the applications want tuples.
 *
 * @author sdua
 */
 class SingleQueryParser extends QueryParser {
    
    // QueryObject parsed by this parser.
    private QueryObject mqo;

    // root object name
    private String mroot;

    // logger
    private final Logger mLogger = LogUtil.getLogger(this);
    

    /**
     * Creates a new instance of SingleQueryParser
     *
     * @param qo QueryObject
     */
     SingleQueryParser(QueryObject qo) {
        super(qo);
        mqo = qo;
    }


    /**
     * Test code
     *
     * @param args args
     */
    public static void main(String[] args) {
        try {
            String[] fields = {
                    "Enterprise.SystemSBR.Person.FirstName",
                    "Enterprise.SystemSBR.Person.LastName",
                    "Enterprise.SystemSBR.Person.SSN",
                    "Enterprise.SystemSBR.Person.Address.City",
                    };
            QueryObject qo = new QueryObject();
            qo.setSelect(fields);
            qo.addCondition("Enterprise.SystemSBR.Person.FirstName", "LIKE",
                    "Sw%");
            qo.addCondition("Enterprise.SystemSBR.Person.Phone.PhoneNumber",
                    "LIKE", "626%");
            qo.setRootObject("Enterprise.SystemSBR.Person");

            AssembleDescriptor assdesc = new AssembleDescriptor();
            qo.setAssembleDescriptor(assdesc);
            assdesc.setAssembler(new ObjectNodeAssembler());
            assdesc.setResultValueObjectType(new ObjectNodeMetaNode(
                    "Enterprise.SystemSBR.Person"));
            qo.setQueryOption(QueryObject.SINGLE_QUERY);

            
            qo.parse();

           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * parses the QueryObject. This is the main interface method for parsing.
     *
     * @exception QMException QMException
     * @return SQLDescriptor[]. The return array is of size 1, since
     *      SingleQueryParser generates a single SQL statement.
     */
    public SQLDescWithBindParameters[] parse()
        throws QMException {
        String[] sFields = mqo.getSelectFields();
        QualifiedField[] selectFields = qualify(sFields);
        
        Condition[] conditions = mqo.getConditionsUnion();

        mroot = getRoot(selectFields, conditions);

        /*
              find all the different objects/objects specified in both selectFields
              and conditions.
              Note it is possible to have conditions on objects that are not retrieved
              in select fields.
          */
        HashSet objects = findObjects(selectFields, conditions);

        /*
              create QPath. Note the purpose of mulitple QPaths (even though we will be 
              using a single Query) is, so that we can tie
              appropriate conditions to the conditionMap. This will aid in determining
              the outer join. ALso to create subquery on the object that has a condition,
              but is not among the select objects.
          */
        QPath[] condQpaths = createQPath(mroot, objects);
        checkForInvalidObjects(objects, condQpaths);

        ConditionMap[] conditionMaps = createConditionMap(objects, condQpaths,
                conditions);

        HashSet selectObjects = findObjects(selectFields);
        SingleQPath selectQPath = createSingleQPath(mroot, selectObjects);

                
         selectQPath.setConditions(conditions);                    
         
        selectQPath.createsubQPath(condQpaths, conditionMaps);

        SQLDescWithBindParameters[] sqlDesc = createSQL(selectQPath, conditionMaps,
                selectFields, mqo.getMaxRows(), mqo.getHints());

        return sqlDesc;
    }


    private SQLDescWithBindParameters[] createSQL(SingleQPath sqpath,
            ConditionMap[] conditionMaps, QualifiedField[] selectFields, int maxRows,
			String[][] hints) {
    	String hint[] = new String[0];
    	if (hints != null && hints.length > 0) {
    	  hint = hints[0];
    	}
        SQLDescWithBindParameters[] sql = new SQLDescWithBindParameters[1];
        sql[0] = sqpath.createSQL(conditionMaps, selectFields,  maxRows, hint);

        return sql;
    }


    /**
     * This method uses Breadth First Search algorithm to create SQL paths.
     *
     * @param root
     * @param objectsHS
     */
    private SingleQPath createSingleQPath(String root, HashSet objectsHS) {
        LinkedList bfsqueue = new LinkedList();
        JoinList joinList = new JoinList();
        SingleQPath qpath = new SingleQPath();

        /*
              Say if object graph is:
              System
              Person
              Address Phone Alias
              The joinList would be
              {System, Person, Address}, {Person, Phone}, {Person, Alias}
              So this joinList uses parent child relationships to do the joins
          */
        qpath.add(joinList);
        qpath.add(root);

        String parent = root;
        joinList.add(parent);

        BFSNode node = new BFSNode(root, joinList);
        bfsqueue.addFirst(node);

        while (!bfsqueue.isEmpty()) {
            node = (BFSNode) bfsqueue.removeFirst();
            parent = node.mobject;
            joinList = node.mjoinList;

            boolean newJoinflag = false;

            // get all children for this object. A QueryObject may not
            // use all the children of a node.
            String[] children = MetaDataService.getChildTypePaths(parent);

            for (int i = 0; (children != null) && (i < children.length); i++) {
                String child = children[i];

                // if the child belongs to the set of current objects
                if (objectsHS.contains(child)) {
                    if (!newJoinflag) {
                        newJoinflag = true;

                        // the first child belongs to the same path and so we add it
                        // to current joinList.
                        joinList.add(child);
                        qpath.add(child);
                        node = new BFSNode(child, joinList);
                        bfsqueue.addLast(node);
                    } else {
                        // the child of a node which is not first, results in another
                        // join path, and so we create another joinList.
                        joinList = new JoinList();
                        joinList.add(parent);
                        joinList.add(child);
                        qpath.add(child);
                        qpath.add(joinList);
                        node = new BFSNode(child, joinList);
                        bfsqueue.addLast(node);
                    }
                }
            }
        }

        return qpath;
    }

    private static class BFSNode {
        JoinList mjoinList;
        String mobject;


        BFSNode(String object, JoinList joinList) {
            mjoinList = joinList;
            mobject = object;
        }
    }
}
