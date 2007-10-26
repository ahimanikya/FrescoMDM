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


/**
 * This Parser parses a QueryObject and produces SQLDescriptors[] containing SQL
 * statements. Each SQL statement correspond to a path from a root to a leaf,
 * specified in selectfields in QueryObject. Example if we have an object graph
 * like: System Person Address Phone Alias Here System is the root object and
 * has one child Person. Person has children: Address, Phone, ALias. So the
 * three SQL statements would consists of select fields: {System.attributes,
 * Person.attributes, Address.attributes}, {System.keyfield, Person.keyfield,
 * Phone.attributes}, {System.keyfield, Person.keyfield, Alias.attributes} Say
 * if QueryObject has conditions: Address.city = ‘Monrovia’ and Phone.phoneType
 * = ‘Business’. Select fields are Person.firstName, Phone.phoneNumber,
 * Address.city, Alias.lastName. Primary keys are: System (euid), Person
 * (personid), Address (addressid), Phone (phoneid), Alias (aliasid). So this
 * would generate the three SQL statements as: SQL1: Select system.euid,
 * person.personid, person.firstname, address.addressid, address.city From
 * system, person, address Where system.euid = person.euid And person.personid =
 * address.personid And address.city = ‘Monrovia’ And personid in ( Select
 * personid from person, phone where person.personid = phone.personid and
 * phone.phoneType = ‘Business’ ) Order by system.euid, person.personid SQL2:
 * Select system.euid, person.personid, phone.phoneid, phone.phonenumber From
 * system, person, phone Where system.euid = person.euid And person.personid =
 * address.personid And phone.phoneType = ‘Business’ And personid in ( Select
 * person.personid from person, address where person.personid = address.personid
 * and address.city = ‘Monrovia’) Order by system.euid, person.personid SQL3:
 * Select system.euid, person.personid, phone.phoneid, alias.lastName From
 * system, person, alias Where system.euid = person.euid And person.personid =
 * alias.personid And personid in ( Select person.personid from person, address
 * where person.personid = address.personid and address.city = ‘Monrovia’) And
 * personid in ( Select person.personid from person, phone where person.personid
 * = phone.personid and phone.phoneType = ‘Business’ ) Order by system.euid,
 * person.personid
 *
 * @author sdua
 */
class MultiQueryParser extends QueryParser {
    
    private QueryObject mqo;

    /*
     root object among QueryObject select fields.
     */
    private String mroot;


    /**
     * Creates a new instance of QueryParser
     *
     * @param qo QueryObject to parse
     */
    MultiQueryParser(QueryObject qo) {
        super(qo);
        mqo = qo;
    }

    /**
     * parses the QueryObject. This is the main interface method for parsing.
     *
     * @exception QMException if there is any exception
     * @return SQLDescriptor[] Each SQLDescriptor has a different SQL statement
     */
    public SQLDescWithBindParameters[] parse()
        throws QMException {
        String[] sFields = mqo.getSelectFields();

        // convert fully qualified field names to QualifiedField object.
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
              Now create all the possible QPath from root to each different leaf.
              So as explained before, for a tree of form
              System
              Person
              Address Phone Alias, these QPaths would be
              { System, Person, Address },
              { System, Person, Phone },
              { System, Person, Alias }
              this is executed by base class QueryParser
          */
        QPath[] condQpaths = createQPath(mroot, objects);
        checkForInvalidObjects(objects, condQpaths);

        ConditionMap[] conditionMaps = createConditionMap(objects, condQpaths,
                conditions);

        /*
              Now we create another set of QPath based on objects specified in
              the select fields. Based on our top example, these paths would be same
              as ConditionPath.
          */
        HashSet selectObjects = findObjects(selectFields);
        QPath[] selectQpaths = createQPath(mroot, selectObjects);
        
        
        for ( int i=0; i < selectQpaths.length; i++ ) {
         selectQpaths[i].setConditions(conditions);            
        }

        /*
              Now create sub paths for each of selectPath.
              The sub paths contains objects whose leaf contains a condition
              and  have not been already included in the QPath.
              So QPath1: {System Person Address} would contain subqpath {Phone Person}
              QPath2 { System Person Phone} would contain subqpath {Address Person}
              QPath3 { System Person Phone} would contain subqpaths {Address Person},
              {Phone Person}
          */
        createsubQPath(selectQpaths, condQpaths, conditionMaps);

        SQLDescWithBindParameters[] sql = createSQL(selectQpaths, conditionMaps,
                selectFields, selectObjects, mqo.getMaxRows(), mqo.getHints());

        return sql;
    }


    void createsubQPath(QPath[] selectQpaths, QPath[] cqpaths,
            ConditionMap[] conditionMap) {
        for (int i = 0; i < selectQpaths.length; i++) {
            QPath qpath = selectQpaths[i];
            qpath.createsubQPath(cqpaths, conditionMap);
        }
    }


    /*
          create SQL for each selectQPath. The conditions are specified in conditionMap
      */
    private SQLDescWithBindParameters[] createSQL(QPath[] selectQpaths,
            ConditionMap[] conditionMap, QualifiedField[] selectFields,
            HashSet selectObjects, int maxRows, String[][] hints)  {
        /*
              first convert list of selectFields to a HashMap that is
              a map of {selectFields object, selectFields list}
          */
        SelectMap selectMap = new SelectMap(selectObjects, selectFields);

        SQLDescWithBindParameters[] sql = new SQLDescWithBindParameters[selectQpaths.length];

        if (hints == null) {
           hints = new String[0][0]; // avoid checking null later
        }
        for (int i = 0; i < selectQpaths.length; i++) {
            QPath qpath = selectQpaths[i];
            String[] hint = new String[0]; // avoid checking null later
            if (hints.length > i) {
             hint = hints[i];
            }

            // delegates the creating of SQLDescriptor to QPath.
            sql[i] = qpath.createSQL(conditionMap, selectMap, maxRows, hint);
        }

        return sql;
    }
}
