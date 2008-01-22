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
package com.sun.mdm.index.project.ui.applicationeditor;

import org.openide.util.NbBundle;
import javax.swing.tree.DefaultTreeModel;

public class TemplateObjects {
    static final String NODE_PRIMARY = EntityNode.getPrimaryNodeType();
    static final String NODE_SUB = EntityNode.getSubNodeType();
    static final String NODE_FIELD = EntityNode.getFieldNodeType();
    
    static final boolean SEARCHABLETRUE = true;
    static final boolean SEARCHABLEFALSE = false;
    static final boolean RESULTTRUE = true;
    static final boolean RESULTFALSE = false;
    static final boolean KEYTYPETRUE = true;
    static final boolean KEYTYPEFALSE = false;
    static final boolean UPDATEABLETRUE = true;
    static final boolean UPDATEABLEFALSE = false;
    static final boolean REQUIREDTRUE = true;
    static final boolean REQUIREDFALSE = false;
    static final boolean BLOCKINGTRUE = true;
    static final boolean BLOCKINGFALSE = false;
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

    static boolean bMatchEngineVality = false;
    
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
        if (matchEngine == NbBundle.getMessage(TemplateObjects.class, "MSG_match_engine_Vality")) {
            mMatchTypePersonFirstName = "Person.FirstName";
            mMatchTypePersonLastName = "Person.LastName";
            mMatchTypePersonSSN = "Person.SSN";
            mMatchTypePersonDOB = "Person.DOB";
            mMatchTypePersonGender = "Person.Gender";
            mMatchTypeBusiness = "Business";
        } else { // Master Index
            mMatchTypePersonLastName = "PersonLastName";
            mMatchTypePersonFirstName = "PersonFirstName";
            mMatchTypePersonSSN = MATCHTYPE_NONE;
            mMatchTypePersonDOB = MATCHTYPE_NONE;
            mMatchTypePersonGender = MATCHTYPE_NONE;
            mMatchTypeBusiness = MATCHTYPE_BUSINESSNAME;
        }
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
     *@return subNode as primaryNode for constructor only
     */
    public static EntityNode addSubNodePerson(EntityTree tree, EntityNode primaryNode) {
        EntityNode subNode;
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        if (primaryNode.isRoot()) {
            if (mViewName == null) {
                setViewName(primaryNode.getName());
            }
            subNode = new EntityNode(tree, mViewName, NODE_PRIMARY);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        } else {
            subNode = new EntityNode(tree, "Person", NODE_SUB);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        }       
        // Add Field Nodes to the Primary
        int displayOrder = 1;
        subNode.add(new EntityNode(tree, "PersonCatCode", 
                                   "Person Cat Code",
                                       NODE_FIELD, 
                                       "string", 
                                       displayOrder++,
                                       "", "",
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 8, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "FirstName", "First Name", NODE_FIELD, "string", displayOrder++,
                                       "", "",
                                       SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDTRUE,
                                       mMatchTypePersonFirstName, 40, CODEMODULE, "", BLOCKINGTRUE, "", "", true));
        subNode.add(new EntityNode(tree, "MiddleName", "Middle Name", NODE_FIELD, "string", displayOrder++,
                                       "", "",
                                       SEARCHABLEFALSE, RESULTTRUE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 30, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "LastName", "Last Name", NODE_FIELD, "string", displayOrder++,
                                       "", "",
                                       SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDTRUE,
                                       mMatchTypePersonLastName, 40, CODEMODULE, "", BLOCKINGTRUE, "", "", true));

        subNode.add(new EntityNode(tree, "Suffix", "Suffix", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 10, "SUFFIX", "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "Title", "Title", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 8, "TITLE", "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "SSN", "SSN", NODE_FIELD, "string", displayOrder++,
                                       INPUTMASKSSN, VALUEMASKSSN, 
                                       SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDTRUE,
                                       mMatchTypePersonSSN, 16, "", SSNPATTERN, BLOCKINGTRUE, "", "", true));

        subNode.add(new EntityNode(tree, "DOB", "Date of Birth", NODE_FIELD, "date", displayOrder++,
                                       "", "",
                                       SEARCHABLEFALSE, RESULTTRUE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDTRUE,
                                       mMatchTypePersonDOB, 12, CODEMODULE, "", BLOCKINGFALSE, "", "", true));
        subNode.add(new EntityNode(tree, "Death", "Death", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 1, "", "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "Gender", "Gender", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTTRUE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDTRUE,
                                       mMatchTypePersonGender, 8, "GENDER", "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "MStatus", "Marital Status", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 8, "MSTATUS", "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "Race", "Race", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 8, "RACE", "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "Ethnic", "Ethnic", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 8, "ETHNIC", "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "Religion", "Religion", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 8, "RELIGION", "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "Language", "Language", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 8, "LANGUAGE", "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "SpouseName", "Spouse Name", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 100, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "MotherName", "Mother Name", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 100, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "MotherMN", "Mother Maiden Name", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 40, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "FatherName", "Father Name", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 100, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "Maiden", "Maiden", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 40, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "PobCity", "Pob City", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 30, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "PobState", "Pob State", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 30, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "PobCountry", "Pob Country", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 20, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "VIPFlag", "VIP Flag", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 8, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "VetStatus", "Veteran Status", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 8, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "Status", "Status", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 8, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "DriverLicense", "Driver License", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 20, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "DriverLicenseSt", "Driver License St.", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 10, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "Dod", "Date of Death", NODE_FIELD, "date", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 12, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "DeathCertificate", "Death Certificate", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 10, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "Nationality", "Nationality", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 8, "NATIONAL", "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "Citizenship", "Citizenship", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 8, "CITIZEN", "", BLOCKINGFALSE, "", "", false));
        
        return subNode;
    }
    
    /**
     *@param model entity tree model
     *@param primaryNode entity tree primary node
     *@return subNode that has beed added
     */
    public static EntityNode addSubNodeAlias(EntityTree tree, EntityNode primaryNode) {
        EntityNode subNode;
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        if (primaryNode.isRoot()) {
            if (mViewName == null) {
                setViewName(primaryNode.getName());
            }
            subNode = new EntityNode(tree, mViewName, NODE_PRIMARY);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        } else {
            subNode = new EntityNode(tree, "Alias", NODE_SUB);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        }
        
        int displayOrder = 1;
        subNode.add(new EntityNode(tree, "FirstName", "First Name", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPETRUE,
                                       UPDATEABLETRUE, REQUIREDTRUE,
                                       MATCHTYPE_NONE, 40, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "MiddleName", "Middle Name", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPETRUE,
                                       UPDATEABLETRUE, REQUIREDFALSE,
                                       MATCHTYPE_NONE, 30, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "LastName", "Last Name", NODE_FIELD, "string", displayOrder++,
                                       "", "", 
                                       SEARCHABLEFALSE, RESULTFALSE, KEYTYPETRUE,
                                       UPDATEABLETRUE, REQUIREDTRUE,
                                       MATCHTYPE_NONE, 40, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        return subNode;
    }
    
    /**
     *@param model entity tree model
     *@param primaryNode entity tree primary node
     *@return subNode that has beed added
     */
    public static EntityNode addSubNodeAddress(EntityTree tree, EntityNode primaryNode) {
        EntityNode subNode;
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        if (primaryNode.isRoot()) {
            if (mViewName == null) {
                setViewName(primaryNode.getName());
            }
            subNode = new EntityNode(tree, mViewName, NODE_PRIMARY);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        } else {
            subNode = new EntityNode(tree, "Address", NODE_SUB);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        }
        int displayOrder = 1;
        subNode.add(new EntityNode(tree, "AddressType", "Address Type", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPETRUE,
                                   UPDATEABLETRUE, REQUIREDTRUE,
                                   MATCHTYPE_NONE, 8, "ADDRTYPE", "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "AddressLine1", "Address Line1", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDTRUE,
                                   MATCHTYPE_ADDRESS, 40, CODEMODULE, "", BLOCKINGTRUE, "", "", true));
        subNode.add(new EntityNode(tree, "AddressLine2", "Address Line2", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTTRUE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 40, CODEMODULE, "", BLOCKINGFALSE, "", "", true));
        subNode.add(new EntityNode(tree, "City", "City", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 30, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "StateCode", "State Code", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 10, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "PostalCode", "Postal Code", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 8, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "PostalCodeExt", "Postal Code Ext.", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 4, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "County", "County", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 20, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "CountryCode", "Country Code", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 20, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        return subNode;
    }
    
    /**
     *@param model entity tree model
     *@param primaryNode entity tree primary node
     *@return subNode that has beed added
     */
    public static EntityNode addSubNodePhone(EntityTree tree, EntityNode primaryNode) {
        EntityNode subNode;
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        if (primaryNode.isRoot()) {
            if (mViewName == null) {
                setViewName(primaryNode.getName());
            }
            subNode = new EntityNode(tree, mViewName, NODE_PRIMARY);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        } else {
            subNode = new EntityNode(tree, "Phone", NODE_SUB);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        }
        int displayOrder = 1;
        subNode.add(new EntityNode(tree, "PhoneType", "Phone Type", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPETRUE,
                                   UPDATEABLETRUE, REQUIREDTRUE,
                                   MATCHTYPE_NONE, 8, "PHONTYPE", "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "Phone", "Phone", NODE_FIELD, "string", displayOrder++,
                                   INPUTMASKPHONE, VALUEMASKPHONE, 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDTRUE,
                                   MATCHTYPE_NONE, 20, CODEMODULE, NUMPATTERNPHONE, BLOCKINGFALSE, "", "", true));
        subNode.add(new EntityNode(tree, "PhoneExt", "Phone Ext.", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 6, CODEMODULE, "", BLOCKINGFALSE, "", "", false));    
        return subNode;
    }
    
    /**
     *@param model entity tree model
     *@param primaryNode entity tree primary node
     *@return subNode that has beed added
     */
    public static EntityNode addSubNodeAuxId(EntityTree tree, EntityNode primaryNode) {
        EntityNode subNode;
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        if (primaryNode.isRoot()) {
            if (mViewName == null) {
                setViewName(primaryNode.getName());
            }
            subNode = new EntityNode(tree, mViewName, NODE_PRIMARY);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        } else {
            subNode = new EntityNode(tree, "AuxId", NODE_SUB);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        }
        int displayOrder = 1;
        subNode.add(new EntityNode(tree, "AuxIdDef", "Aux Id Def", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDTRUE,
                                   MATCHTYPE_NONE, 10, CODEMODULE, "", BLOCKINGFALSE, "AUXIDDEF", "", false));
        subNode.add(new EntityNode(tree, "Id", "Id", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDTRUE,
                                   MATCHTYPE_NONE, 40, CODEMODULE, "", BLOCKINGFALSE, "", "AuxIdDef", false));
        return subNode;
    }
    
    /**
     *@param model entity tree model
     *@param primaryNode entity tree primary node
     *@return subNode as primaryNode for constructor only
     */
    public static EntityNode addSubNodeCompany(EntityTree tree, EntityNode primaryNode) {
        EntityNode subNode;
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        if (primaryNode.isRoot()) {
            if (mViewName == null) {
                setViewName(primaryNode.getName());
            }
            subNode = new EntityNode(tree, mViewName, NODE_PRIMARY);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        } else {
            subNode = new EntityNode(tree, "Company", NODE_SUB);
            model.insertNodeInto(subNode, primaryNode, primaryNode.getChildCount());
        }       
        // Add Field Nodes to the Primary

        int displayOrder = 1;
        // Add Field Nodes to the Primary
        subNode.add(new EntityNode(tree, "CompanyName", "Company Name", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDTRUE,
                                   mMatchTypeBusiness, 40, CODEMODULE, "", BLOCKINGTRUE, "", "", true));
        
        subNode.add(new EntityNode(tree, "CompanyType", "Company Type", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 40, CODEMODULE, "", BLOCKINGFALSE, "", "", true));
        subNode.add(new EntityNode(tree, "Exchange", "Exchange", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 30, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "StockSymbol", "Stock Symbol", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 20, CODEMODULE, "", BLOCKINGTRUE, "", "", true));
        subNode.add(new EntityNode(tree, "SIC", "SIC", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 16, CODEMODULE, "", BLOCKINGTRUE, "", "", false));
        subNode.add(new EntityNode(tree, "Industry", "Industry", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 40, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "SalesRegion", "Sales Region", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 40, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "TaxPayerID", "Tax Payer ID", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLETRUE, RESULTTRUE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 40, CODEMODULE, "", BLOCKINGTRUE, "", "", false));
        subNode.add(new EntityNode(tree, "ContactPerson", "Contact Person", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 40, CODEMODULE, "", BLOCKINGFALSE, "", "", true));
        subNode.add(new EntityNode(tree, "CreditStanding", "Credit Standing", NODE_FIELD, "string", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 12, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        subNode.add(new EntityNode(tree, "NoOfEmployees", "Number of Employees", NODE_FIELD, "int", displayOrder++,
                                   "", "", 
                                   SEARCHABLEFALSE, RESULTFALSE, KEYTYPEFALSE,
                                   UPDATEABLETRUE, REQUIREDFALSE,
                                   MATCHTYPE_NONE, 8, CODEMODULE, "", BLOCKINGFALSE, "", "", false));
        return subNode;
    }
}
