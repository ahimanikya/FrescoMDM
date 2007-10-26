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
package com.sun.mdm.index.project.ui.wizards;

import javax.swing.tree.DefaultTreeModel;

public class TemplateObjects {
    static final String NODE_PRIMARY = EntityNode.getPrimaryNodeType();
    static final String NODE_PRIMARY_FIELDS = EntityNode.getPrimaryFieldsNodeType();
    static final String NODE_SUB = EntityNode.getSubNodeType();
    static final String NODE_FIELD = EntityNode.getFieldNodeType();
    
    static final String SEARCHABLETRUE = "true";
    static final String SEARCHABLEFALSE = "false";
    static final String RESULTTRUE = "true";
    static final String RESULTFALSE = "false";
    static final String KEYTYPETRUE = "true";
    static final String KEYTYPEFALSE = "false";
    static final String UPDATEABLETRUE = "true";
    static final String UPDATEABLEFALSE = "false";
    static final String REQUIREDTRUE = "true";
    static final String REQUIREDFALSE = "false";
    static final String BLOCKINGTRUE = "true";
    static final String BLOCKINGFALSE = "false";
    static final String MATCHTYPE_NONE = "None";
    static final String MATCHTYPE_ADDRESS = "Address";
    static String mMatchTypePersonLastName = "PersonLastName";
    static String mMatchTypePersonFirstName = "PersonFirstName";
    static String mMatchTypePersonSSN = "";
    static String mMatchTypePersonDOB = "";
    static String mMatchTypePersonGender = "";

    static final String MATCHTYPE_BUSINESSNAME = "BusinessName";
    static String mMatchTypeBusiness = "";
    
    static final String CODEMODULE = "";
    
    static final String SSNPATTERN = "[0-9]{9}";
    static final String INPUTMASKSSN = "DDD-DD-DDDD";
    static final String VALUEMASKSSN = "DDDxDDxDDDD";
    
    static final String NUMPATTERNPHONE = "[0-9]{10}";
    static final String INPUTMASKPHONE = "(DDD) DDD-DDDD";
    static final String VALUEMASKPHONE = "xDDDxxDDDxDDDD";
    
    static final String INPUTMASKDATE = "DD/DD/DDDD";

    static String mViewName = null;
    
    /** Creates a new instance of TemplateObjects
     *
     */
    public TemplateObjects() {
    }
    

    /**
     *@param matchEngine Match Engine
     *
     *Set match types for different match engines
     *
     */
    public static void setMatchTypes(String matchEngine) {
        mMatchTypePersonLastName = "PersonLastName";
        mMatchTypePersonFirstName = "PersonFirstName";
        mMatchTypePersonSSN = MATCHTYPE_NONE;
        mMatchTypePersonDOB = MATCHTYPE_NONE;
        mMatchTypePersonGender = MATCHTYPE_NONE;
        mMatchTypeBusiness = MATCHTYPE_BUSINESSNAME;
    }
    
    /**
     *@param viewName View Name
     *
     *Set View name for unique primary object name
     *
     */
    public static void setViewName(String viewName) {
        mViewName = viewName;
    }
    
    /**
     *@param model entity tree model
     *@param primaryNode entity tree primary node
     *@return subNode0 as primaryNode for constructor only
     */
    public static EntityNode addSubNodePerson(DefaultTreeModel model, EntityNode primaryNode) {
        EntityNode subNode0, subNode;
        if (primaryNode.isRoot()) {
            subNode = new EntityNode(mViewName, NODE_PRIMARY);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
            subNode0 = subNode;
            //subNode = new EntityNode("", NODE_PRIMARY_FIELDS);
            //model.insertNodeInto(subNode, subNode0, 0);
        } else {
            subNode = new EntityNode("Person", NODE_SUB);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
            subNode0 = subNode;
        }       
        // Add Field Nodes to the Primary
        int displayOrder = 1;
        subNode.add(new EntityNode("PersonCatCode", 
                                   "Person Cat Code",
                                       NODE_FIELD, 
                                       "string", 
                                       displayOrder++,
                                       "", "",
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "8", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("FirstName", "First Name", NODE_FIELD, "string", displayOrder++,
                                       "", "",
                                       SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDTRUE,
                                       mMatchTypePersonFirstName, "40", CODEMODULE, "", BLOCKINGTRUE, "", "", "true"));
        subNode.add(new EntityNode("MiddleName", "Middle Name", NODE_FIELD, "string", displayOrder++,
                                       "", "",
                                       SEARCHABLEFALSE, RESULTTRUE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "30", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("LastName", "Last Name", NODE_FIELD, "string", displayOrder++,
                                       "", "",
                                       SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDTRUE,
                                       mMatchTypePersonLastName, "40", CODEMODULE, "", BLOCKINGTRUE, "", "", "true"));

        subNode.add(new EntityNode("Suffix", "Suffix", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "10", "SUFFIX", "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("Title", "Title", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "8", "TITLE", "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("SSN", "SSN", NODE_FIELD, "string", displayOrder++,
                                       INPUTMASKSSN, VALUEMASKSSN, 
                                       SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDTRUE,
                                       mMatchTypePersonSSN, "16", "", SSNPATTERN, BLOCKINGTRUE, "", "", "true"));

        subNode.add(new EntityNode("DOB", "Date of Birth", NODE_FIELD, "date", displayOrder++,
                                       "", "",
                                       SEARCHABLEFALSE, RESULTTRUE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDTRUE,
                                       mMatchTypePersonDOB, "12", CODEMODULE, "", BLOCKINGFALSE, "", "", "true"));
        subNode.add(new EntityNode("Death", "Death", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "1", "", "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("Gender", "Gender", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTTRUE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDTRUE,
                                       mMatchTypePersonGender, "8", "GENDER", "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("MStatus", "Marital Status", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "8", "MSTATUS", "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("Race", "Race", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "8", "RACE", "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("Ethnic", "Ethnic", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "8", "ETHNIC", "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("Religion", "Religion", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "8", "RELIGION", "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("Language", "Language", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "8", "LANGUAGE", "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("SpouseName", "Spouse Name", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "100", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("MotherName", "Mother Name", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "100", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("MotherMN", "Mother Maiden Name", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "40", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("FatherName", "Father Name", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "100", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("Maiden", "Maiden", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "40", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("PobCity", "Pob City", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "30", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("PobState", "Pob State", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "30", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("PobCountry", "Pob Country", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "20", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("VIPFlag", "VIP Flag", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "8", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("VetStatus", "Veteran Status", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "8", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("Status", "Status", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "8", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("DriverLicense", "Driver License", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "20", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("DriverLicenseSt", "Driver License St.", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "10", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("Dod", "Date of Death", NODE_FIELD, "date", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "12", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("DeathCertificate", "Death Certificate", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "10", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("Nationality", "Nationality", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "8", "NATIONAL", "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("Citizenship", "Citizenship", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "8", "CITIZEN", "", BLOCKINGFALSE, "", "", "false"));
        
        return subNode0;
    }
    
    /**
     *@param model entity tree model
     *@param primaryNode entity tree primary node
     *@return subNode that has beed added
     */
    public static EntityNode addSubNodeAlias(DefaultTreeModel model, EntityNode primaryNode) {
        EntityNode subNode;
        if (primaryNode.isRoot()) {
            EntityNode subNode0 = new EntityNode(mViewName, NODE_PRIMARY);
            model.insertNodeInto(subNode0, primaryNode, primaryNode.getChildCount());
            subNode = new EntityNode("", NODE_PRIMARY_FIELDS);
            model.insertNodeInto(subNode, subNode0, 0);
        } else {
            subNode = new EntityNode("Alias", NODE_SUB);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        }
        
        int displayOrder = 1;
        subNode.add(new EntityNode("FirstName", "First Name", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPETRUE,
                                       UPDATEABLETRUE, REQUIREDTRUE,
                                       MATCHTYPE_NONE, "40", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("MiddleName", "Middle Name", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPETRUE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, "30", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("LastName", "Last Name", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPETRUE,
                                       UPDATEABLETRUE, REQUIREDTRUE,
                                       MATCHTYPE_NONE, "40", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        return subNode;
    }
    
    /**
     *@param model entity tree model
     *@param primaryNode entity tree primary node
     *@return subNode that has beed added
     */
    public static EntityNode addSubNodeAddress(DefaultTreeModel model, EntityNode primaryNode) {
        EntityNode subNode;
        if (primaryNode.isRoot()) {
            EntityNode subNode0 = new EntityNode(mViewName, NODE_PRIMARY);
            model.insertNodeInto(subNode0, primaryNode, primaryNode.getChildCount());
            subNode = new EntityNode("", NODE_PRIMARY_FIELDS);
            model.insertNodeInto(subNode, subNode0, 0);
        } else {
            subNode = new EntityNode("Address", NODE_SUB);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        }
        int displayOrder = 1;
        subNode.add(new EntityNode("AddressType", "Address Type", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPETRUE,
                                   UPDATEABLETRUE, REQUIREDTRUE,
                                   MATCHTYPE_NONE, "8", "ADDRTYPE", "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("AddressLine1", "Address Line1", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDTRUE,
                                   MATCHTYPE_ADDRESS, "40", CODEMODULE, "", BLOCKINGTRUE, "", "", "true"));
        subNode.add(new EntityNode("AddressLine2", "Address Line2", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTTRUE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "40", CODEMODULE, "", BLOCKINGFALSE, "", "", "true"));
        subNode.add(new EntityNode("City", "City", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "30", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("StateCode", "State Code", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "10", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("PostalCode", "Postal Code", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "8", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("PostalCodeExt", "Postal Code Ext.", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "4", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("County", "County", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "20", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("CountryCode", "Country Code", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "20", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        return subNode;
    }
    
    /**
     *@param model entity tree model
     *@param primaryNode entity tree primary node
     *@return subNode that has beed added
     */
    public static EntityNode addSubNodePhone(DefaultTreeModel model, EntityNode primaryNode) {
        EntityNode subNode;
        if (primaryNode.isRoot()) {
            EntityNode subNode0 = new EntityNode(mViewName, NODE_PRIMARY);
            model.insertNodeInto(subNode0, primaryNode, primaryNode.getChildCount());
            subNode = new EntityNode("", NODE_PRIMARY_FIELDS);
            model.insertNodeInto(subNode, subNode0, 0);
        } else {
            subNode = new EntityNode("Phone", NODE_SUB);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        }
        int displayOrder = 1;
        subNode.add(new EntityNode("PhoneType", "Phone Type", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPETRUE,
                                   UPDATEABLETRUE, REQUIREDTRUE,
                                   MATCHTYPE_NONE, "8", "PHONTYPE", "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("Phone", "Phone", NODE_FIELD, "string", displayOrder++,
                                   INPUTMASKPHONE, VALUEMASKPHONE, 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDTRUE,
                                   MATCHTYPE_NONE, "20", CODEMODULE, NUMPATTERNPHONE, BLOCKINGFALSE, "", "", "true"));
        subNode.add(new EntityNode("PhoneExt", "Phone Ext.", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "6", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));    
        return subNode;
    }
    
    /**
     *@param model entity tree model
     *@param primaryNode entity tree primary node
     *@return subNode that has beed added
     */
    public static EntityNode addSubNodeAuxId(DefaultTreeModel model, EntityNode primaryNode) {
        EntityNode subNode;
        if (primaryNode.isRoot()) {
            EntityNode subNode0 = new EntityNode(mViewName, NODE_PRIMARY);
            model.insertNodeInto(subNode0, primaryNode, primaryNode.getChildCount());
            subNode = new EntityNode("", NODE_PRIMARY_FIELDS);
            model.insertNodeInto(subNode, subNode0, 0);
        } else {
            subNode = new EntityNode("AuxId", NODE_SUB);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        }
        int displayOrder = 1;
        subNode.add(new EntityNode("AuxIdDef", "Aux Id Def", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDTRUE,
                                   MATCHTYPE_NONE, "10", CODEMODULE, "", BLOCKINGFALSE, "AUXIDDEF", "", "false"));
        subNode.add(new EntityNode("Id", "Id", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDTRUE,
                                   MATCHTYPE_NONE, "40", CODEMODULE, "", BLOCKINGFALSE, "", "AuxIdDef", "false"));
        return subNode;
    }
    
    /**
     *@param model entity tree model
     *@param primaryNode entity tree primary node
     *@return subNode0 as primaryNode for constructor only
     */
    public static EntityNode addSubNodeCompany(DefaultTreeModel model, EntityNode primaryNode) {
        EntityNode subNode0, subNode;
        if (primaryNode.isRoot()) {
            subNode = new EntityNode(mViewName, NODE_PRIMARY);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
            subNode0 = subNode;
            //subNode = new EntityNode("", NODE_PRIMARY_FIELDS);
            //model.insertNodeInto(subNode, subNode0, 0);
        } else {
            subNode = new EntityNode("Company", NODE_SUB);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
            subNode0 = subNode;
        }       
        // Add Field Nodes to the Primary

        int displayOrder = 1;
        // Add Field Nodes to the Primary
        subNode.add(new EntityNode("CompanyName", "Company Name", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDTRUE,
                                   mMatchTypeBusiness, "40", CODEMODULE, "", BLOCKINGTRUE, "", "", "true"));
        
        subNode.add(new EntityNode("CompanyType", "Company Type", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "40", CODEMODULE, "", BLOCKINGFALSE, "", "", "true"));
        subNode.add(new EntityNode("Exchange", "Exchange", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "30", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("StockSymbol", "Stock Symbol", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "20", CODEMODULE, "", BLOCKINGTRUE, "", "", "true"));
        subNode.add(new EntityNode("SIC", "SIC", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "16", CODEMODULE, "", BLOCKINGTRUE, "", "", "false"));
        subNode.add(new EntityNode("Industry", "Industry", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "40", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("SalesRegion", "Sales Region", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "40", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("TaxPayerID", "Tax Payer ID", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "40", CODEMODULE, "", BLOCKINGTRUE, "", "", "false"));
        subNode.add(new EntityNode("ContactPerson", "Contact Person", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "40", CODEMODULE, "", BLOCKINGFALSE, "", "", "true"));
        subNode.add(new EntityNode("CreditStanding", "Credit Standing", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "12", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        subNode.add(new EntityNode("NoOfEmployees", "Number of Employees", NODE_FIELD, "int", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, "8", CODEMODULE, "", BLOCKINGFALSE, "", "", "false"));
        return subNode;
    }
}
