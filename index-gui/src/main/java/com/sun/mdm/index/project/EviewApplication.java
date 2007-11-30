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
package com.sun.mdm.index.project;

import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.EditableProperties;

import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileLock;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.openide.nodes.Node;


import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.InputSource;
import java.util.ArrayList;

import com.sun.mdm.index.parser.Utils;
import com.sun.mdm.index.parser.EIndexObject;
import com.sun.mdm.index.parser.EDMType;
import com.sun.mdm.index.parser.MasterType;
import com.sun.mdm.index.parser.UpdateType;
import com.sun.mdm.index.parser.QueryType;
import com.sun.mdm.index.parser.MatchFieldDef;

public class EviewApplication extends EviewProject {
    private static final java.util.logging.Logger mLog = java.util.logging.Logger.getLogger(
            EviewApplication.class.getName()
        );

    private AntProjectHelper mHelper;
    private Node mRootNode;
    public final String EVIEW_APPLICATION_NAME = "eViewApplicationName";
    public final String EVIEW_OBJECT_NAME = "eViewObjectName";
    private String mMatchConfigFileString = "";
    // the following variables can be modified by Configuration Editor
    // and need to be synched up with xml files or reloaded
    private boolean mModified = false;
    private boolean mModifiedMatchConfig = false;
    private EIndexObject mEindexObject = null;      // OBJECT_XML
    private EDMType mEDMType = null;                // EDM_XML
    private UpdateType mUpdateType = null;          // UPDATE_XML
    private MatchFieldDef mMatchFieldDef = null;    // MEFA_XML
    private QueryType mQueryType = null;            // QUERY_XML
    private MasterType mMasterType = null;          // MASTER_XML
    private ArrayList<MatchRuleRow> mAlMatchRuleRows = new ArrayList<MatchRuleRow>();  // MATCH_CONFIG_FILE
    private FileObject fileObjectCheckedOut;
    private FileObject fileEdmCheckedOut;
    private FileObject fileMasterCheckedOut;
    private FileObject fileMefaCheckedOut;
    private FileObject fileQueryCheckedOut;
    private FileObject fileSecurityCheckedOut;
    private FileObject fileUpdateCheckedOut;
    private FileObject fileValidationCheckedOut;
    private FileObject fileMatchConfigCheckedOut;
    
    private ArrayList<String> mAlMatchTypes = null;  // MATCH_CONFIG_FILE
    private String msgCheckedOut = "";
    private boolean bNeedToCheckIn;
    
    /** Creates a new instance of EviewApplication */
    public EviewApplication(final AntProjectHelper helper) throws IOException {
        super(helper);
        mHelper = helper;
        setStandardizationFieldIDs();
    }
    
    private EditableProperties getEditableProperties() {
        EditableProperties ep = mHelper.getProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH);
        return ep;
    }

    /** set application name
     * @param appName
     */
    public void setApplicationName(String appName) {
        EditableProperties ep = getEditableProperties();
        ep.setProperty(EVIEW_APPLICATION_NAME, appName);
        mHelper.putProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH, ep);
    }
    
    /** return the application name
     * @return application name
     */    
    public String getApplicationName() {
        return getEditableProperties().getProperty(EVIEW_APPLICATION_NAME);
    }
    
    /** set object name
     * @param objName
     */
    public void setObjectName(String objName) {
        EditableProperties ep = getEditableProperties();        
        ep.setProperty(EVIEW_OBJECT_NAME, objName);
        mHelper.putProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH, ep);
    }
    
    /** return the Object name
     * @return Object name
     */    
    public String getObjectName() {
        return getEditableProperties().getProperty(EVIEW_OBJECT_NAME);
    }
    
    /** Parses object.xml and returns com.sun.mdm.index.parser.EIndexObject by parsing object.xml
     * @throws RepositoryException failed to get value
     * @return the EIndexObject
     */    
    public EIndexObject getEIndexObject(boolean bRefresh) {
        try {
            if (mEindexObject == null || bRefresh == true) {
                FileObject cf = this.getObjectDefinitionFile();
                if (cf != null) {
                    InputStream objectdef = cf.getInputStream();
                    InputSource source = new InputSource(objectdef);
                    mEindexObject = Utils.parseEIndexObject(source);
                }
            }
        } catch (Exception ex) {
        }
        return mEindexObject;
    }
    
    //public void setEIndexObject(EIndexObject eIndexObject) {
    //    mEindexObject = eIndexObject;
    //}
    
    /** Parses edm.xml and returns com.sun.mdm.index.parser.EDMType 
     * @return com.sun.mdm.index.parser.EDMType
     */
    public EDMType getEDMType(boolean bRefresh) {
        try {
            if (mEDMType == null || bRefresh) {
                FileObject cf = getConfigurationFile(EviewProjectProperties.CONFIGURATION_FOLDER, EviewProjectProperties.EDM_XML, false);                    
                if (cf != null) {
                    InputStream objectdef = cf.getInputStream();
                    InputSource source = new InputSource(objectdef);
                    mEDMType = Utils.parseEDMType(source);

                }
            }
        } catch (Exception ex) {
        }
        return mEDMType;                    
    }
    
    //public void setEDMType(EDMType edmType) {
    //    mEDMType = edmType;
    //}
    
    /** Parses update.xml and returns com.sun.mdm.index.parser.UpdateType 
     * @return com.sun.mdm.index.parser.UpdateType
     */
    public UpdateType getUpdateType(boolean bRefresh) {
        try {
            if (mUpdateType == null || bRefresh) {
                FileObject cf = getConfigurationFile(EviewProjectProperties.CONFIGURATION_FOLDER, EviewProjectProperties.UPDATE_XML, false);
                if (cf != null) {
                    InputStream objectdef = cf.getInputStream();
                    InputSource source = new InputSource(objectdef);
                    mUpdateType = Utils.parseUpdateType(source);
                }
            }
        } catch (Exception ex) {
        }
        return mUpdateType;
    }
    
    //public void setUpdateType(UpdateType updateType) {
    //    mUpdateType = updateType;
    //}
    
    public class MatchRuleRow {
        private String matchType = "";
        private String size = "";
        private String nullField = "";
        private String function = "";
        private String mprob = "";
        private String uprob = "";
        private String aggrementW = "";
        private String disagreementW = ""; 
        private String parameters = "";

        public MatchRuleRow() {};
        
        public MatchRuleRow(String matchType, String size, String nullField,
                          String function, String mprob, String uprob,
                          String aggrementW, String disagreementW, String parameters) {
            this.matchType = matchType;
            this.size = size;
            this.nullField = nullField;
            this.function = function;
            this.mprob = mprob;
            this.uprob = uprob;
            this.aggrementW = aggrementW;
            this.disagreementW = disagreementW; 
            this.parameters = parameters;
            
        }
        
        public void setMember(int col, Object value) {
            switch (col) {
                case 0:
                    setMatchType((String) value);
                    break;
                case 1:
                    setSize((String) value);
                    break;
                case 2:
                    setNullField((String) value);
                    break;
                case 3:
                    setFunction((String) value);
                    break;
                case 4:
                    setMProb((String) value);
                    break;
                case 5:
                    setUProb((String) value);
                    break;
                case 6:
                    setAgreementWeight((String) value);
                    break;
                case 7:
                    setDisagreementWeight((String) value);
                    break;
                case 8:
                    setParameters((String) value);
                    break;
                    
            }
        }
        
    	public String getMatchType() {
            return matchType;
        }

        public String getSize() {
            return size;
        }
        
        public String getNullField() {
            return nullField;
        }
        
        public String getFunction() {
            return function;
        }
        
        public String getMProb() {
            return mprob;
        }
        
        public String getUProb() {
            return uprob;
        }
        
        public String getAgreementWeight() {
            return aggrementW;
        }
        
        public String getDisagreementWeight() {
            return disagreementW;
        }
        
        public String getParameters() {
            return parameters;
        }
        
        public void setMatchType(String matchType) {
            this.matchType = matchType;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public void setNullField(String nullField) {
            this.nullField = nullField;
        }

        public void setFunction(String function) {
            this.function = function;
        }

        public void setMProb(String mprob) {
            this.mprob = mprob;
        }

        public void setUProb(String uprob) {
            this.uprob = uprob;
        }

        public void setAgreementWeight(String aggrementW) {
            this.aggrementW = aggrementW;
        }

        public void setDisagreementWeight(String disagreementW) {
            this.disagreementW = disagreementW;
        }

        public void setParameters(String parameters) {
            this.parameters = parameters;
        }
        
    }
    
    String probabilityType = "0";
    /**
     * @return String 0 or 1
     */
    public String getMatchingProbabilityType() {
        return this.probabilityType;
    }
    
    /**
     * @param String 0 or 1
     */
    public void setMatchingProbabilityType(String probabilityType) {
        this.probabilityType = probabilityType;
    }
    
    public MatchRuleRow createMatchRuleRow(String matchType, String size, String nullField,
                                           String function, String mprob, String uprob,
                                           String aggrementW, String disagreementW, String parameters) {
        MatchRuleRow row = new MatchRuleRow(matchType, size, nullField,
                                              function, mprob, uprob,
                                              aggrementW, disagreementW, parameters);
        return row;
    }
    
    /** Parses MATCH_CONFIG_FILE and returns array of match type rows 
     * @return ArrayList mAlMatchRuleRows
     */
    public ArrayList getMatchRuleRows() {
        mAlMatchRuleRows.clear();
        getMatchTypeList(true);
        return mAlMatchRuleRows;
    }
    
    /** Parses MATCH_CONFIG_FILE and returns array of match type names 
     * @return ArrayList mAlMatchTypes
     */
    public ArrayList getMatchTypeList(boolean bRefresh) {
        if (mAlMatchTypes == null || bRefresh) {
            FileObject file = getConfigurationFile(EviewProjectProperties.MATCH_ENGINE_FOLDER, EviewProjectProperties.MATCH_CONFIG_FILE, false);        

            if (file != null) {
                mAlMatchTypes = new ArrayList<String>();
                try {
                    InputStream is = file.getInputStream();
                    InputSource source = new InputSource(is);
                    int avail = is.available();
                    byte[] input = new byte[avail];
                    is.read(input);
                    String strAll = new String(input);

                    String[] strSplit = strAll.split("\n");
                    for (int i=0; i < strSplit.length; i++) {
                        // building MatchRuleRow
                        MatchRuleRow matchRuleRow = new MatchRuleRow();
                        String sParameters = "";
                        String[] strSplitPerLine = strSplit[i].split(" ");  
                        int k = 0;
                        for (int j=0; j < strSplitPerLine.length; j++) {   
                            String s = strSplitPerLine[j];
                            if (!s.equals("")) {
                                if (k < 8) {
                                    matchRuleRow.setMember(k++, s);
                                } else {
                                    if (sParameters.equals("")) {
                                        sParameters += s;
                                    } else {
                                        sParameters += " " + s;
                                    }
                                }
                            }
                        }
                        
                        if (k == 2) { //ProbabilityType
                            if (matchRuleRow.getMatchType().equals("ProbabilityType")) { // column 0
                                probabilityType = matchRuleRow.getSize().substring(0, 1);    // column 1
                            }
                        } else if (k==8) {
                            matchRuleRow.setParameters(sParameters);
                            mAlMatchRuleRows.add(matchRuleRow);                            

                            int idx = strSplit[i].indexOf(' ');
                            if (idx > 0) {
                                String strMatchField = strSplit[i].substring(0, idx);
                                mAlMatchTypes.add(strMatchField);
                            }
                        }                        
                    }
                } catch (Exception ex) {
                }
            }
        }
        return mAlMatchTypes;
    }
    
    public void setMatchTypeList(ArrayList<String> alMatchFields) {
        this.mAlMatchTypes = alMatchFields;
    }
    
    /** parses mefa.xml and returns com.sun.mdm.index.parser.MatchFieldDef
     * @throws RepositoryException failed to get value
     * @return com.sun.mdm.index.parser.MatchFieldDef
     */    
    public MatchFieldDef getMatchFieldDef(boolean bRefresh) {
        if (mMatchFieldDef == null || bRefresh) {
            try {
                FileObject cf = getConfigurationFile(EviewProjectProperties.CONFIGURATION_FOLDER, EviewProjectProperties.MEFA_XML, false);
                if (cf != null) {
                    InputStream objectdef = cf.getInputStream();
                    InputSource source = new InputSource(objectdef);
                    mMatchFieldDef = Utils.parseMatchFieldDef(source);
                }
            } catch (Exception ex) {
            }
        }
        return mMatchFieldDef;
    }
    
    public void setMatchFieldDef(MatchFieldDef matchFieldDef) {
        mMatchFieldDef = matchFieldDef;
    }
    
    // Should include Match Engine when more engines are added
    // These Field IDs should also match those listed in "MefaStandardization.tmpl"
    // address
    // businessname
    // personname
    // The following are not supported
    // char
    // date
    // number
    // personfirstname
    // personlastname
    // string
    final String STANDTYPE_ADDRESS = "Address";
    final String STANDTYPE_BUSINESSNAME = "BusinessName";
    final String STANDTYPE_PERSONNAME = "PersonName";
    final ArrayList<String> alFieldIDsAddress = new ArrayList<String>();
    final ArrayList<String> alFieldIDsBusinessName = new ArrayList<String> ();
    final ArrayList<String> alFieldIDsPersonName = new ArrayList<String> ();
    /*
    final String STANDTYPE_PERSONFIRSTNAME = "PersonFirstName";
    final String STANDTYPE_PERSONLASTNAME = "PersonLastName";
    final String STANDTYPE_CHAR = "Char";
    final String STANDTYPE_DATE = "Date";
    final String STANDTYPE_NUMBER = "Number";
    final String STANDTYPE_SRING = "String";
    final ArrayList<String> alFieldIDsPersonFirstName = new ArrayList<String>();
    final ArrayList<String> alFieldIDsPersonLastName = new ArrayList<String>();
    final ArrayList<String> alFieldIDsCharName = new ArrayList<String>();
    final ArrayList<String> alFieldIDsDateName = new ArrayList<String>();
    */     
    private void setStandardizationFieldIDs() {
        // STANDTYPE_ADDRESS
        alFieldIDsAddress.add("HouseNumber");
        alFieldIDsAddress.add("RuralRouteIdentif");
        alFieldIDsAddress.add("BoxIdentif");
        alFieldIDsAddress.add("MatchStreetName");        
        alFieldIDsAddress.add("RuralRouteDescript");        
        alFieldIDsAddress.add("BoxDescript");        
        alFieldIDsAddress.add("PropDesPrefDirection");        
        alFieldIDsAddress.add("PropDesSufDirection");        
        alFieldIDsAddress.add("StreetNamePrefDirection");        
        alFieldIDsAddress.add("StreetNameSufDirection");                
        alFieldIDsAddress.add("StreetNameSufType");                
        alFieldIDsAddress.add("StreetNamePrefType");                
        alFieldIDsAddress.add("PropDesSufType");                
        alFieldIDsAddress.add("PropDesPrefType");     
        // STANDTYPE_BUSINESSNAME
        alFieldIDsBusinessName.add("PrimaryName");
        alFieldIDsBusinessName.add("OrgTypeKeyword");
        alFieldIDsBusinessName.add("AssocTypeKeyword");
        alFieldIDsBusinessName.add("IndustrySectorList");
        alFieldIDsBusinessName.add("IndustryTypeKeyword");
        alFieldIDsBusinessName.add("Url");
        // STANDTYPE_PERSONNAME
        alFieldIDsPersonName.add("FirstName");
        alFieldIDsPersonName.add("LastName");
        alFieldIDsPersonName.add("MiddleName");        
    }
    
    public ArrayList getStandardizationFieldIDsByType(String standardizationType) {
        ArrayList al = null;
        if (standardizationType.equals(STANDTYPE_ADDRESS)) {
                return alFieldIDsAddress;
        } else if (standardizationType.equals(STANDTYPE_BUSINESSNAME)) {
            return alFieldIDsBusinessName;
        } else if (standardizationType.equals(STANDTYPE_PERSONNAME)) {
            return alFieldIDsPersonName;
        }

        return al;
    }
    
    /** parses query.xml and returns com.sun.mdm.index.parser.QueryType
     * @throws RepositoryException failed to get value
     * @return com.sun.mdm.index.parser.QueryType
     */    
    public QueryType getQueryType(boolean bRefresh) {
        if (mQueryType == null || bRefresh) {
            try {
                FileObject cf = getConfigurationFile(EviewProjectProperties.CONFIGURATION_FOLDER, EviewProjectProperties.QUERY_XML, false);
                if (cf != null) {
                    InputStream objectdef = cf.getInputStream();                
                    InputSource source = new InputSource(objectdef);
                    mQueryType = Utils.parseQueryType(source);
                }
            } catch (Exception ex) {
            }
        }
        return mQueryType;
    }
    
    public void setQueryType(QueryType queryType) {
        mQueryType = queryType;
    }
    
    /** parses master.xml and returns com.sun.mdm.index.parser.MasterType
     * @throws RepositoryException failed to get value
     * @return com.sun.mdm.index.parser.MasterType
     */    
    public MasterType getMasterType(boolean bRefresh) {
        if (mMasterType == null || bRefresh) {
            try {
                FileObject cf = getConfigurationFile(EviewProjectProperties.CONFIGURATION_FOLDER, EviewProjectProperties.MASTER_XML, false);
                if (cf != null) {
                    InputStream objectdef = cf.getInputStream();                
                    InputSource source = new InputSource(objectdef);
                    mMasterType = Utils.parseMasterType(source);
                }
            } catch (Exception ex) {
            }
        }
        return mMasterType;
    }
    
    public void setMasterType(MasterType masterType) {
        mMasterType = masterType;
    }
    
    public void setRootNode(Node node) {
        mRootNode = node;
    }
    
    @Override
    public Node getRootNode() {
        return mRootNode;
    }
    
    /* set dirty bit by various configuration panels
     * so we know we need to rewrite xml files
     */
    public void setModified(boolean flag) {
        mModified = flag;
    }
    
    /* set dirty bit by various configuration panels
     * so we know we need to rewrite xml files
     */
    public void setModifiedMatchConfig(boolean flag) {
        mModifiedMatchConfig = flag;
    }
    
    /* Called by EntityNode so cbMatchType contains the right list of match types
     *@return mModifiedMatchConfig
     */
    public boolean isMatchConfigModified() {
        return mModifiedMatchConfig;
    }
    
    public void setMatchConfigFileString(String data) {
        mMatchConfigFileString = data;
    }
    
    public boolean isModified() {
        mModified = mModified || 
                    mModifiedMatchConfig ||
                    mEindexObject.isModified() || 
                    mEDMType.isModified() || 
                    mQueryType.isModified() || 
                    mMasterType.isModified() || 
                    mMatchFieldDef.isModified() ||
                    mUpdateType.isModified();
        return mModified;
    }
    
    public boolean isValidated() {
        boolean validated = true;
        return validated;
    }
    
    public void setNeedToCheckIn(boolean flag) {
        bNeedToCheckIn = flag;
        String value = (flag == true) ? "true" : "false";
        //getVersionManager().checkOutForWrite(this);
        //this.setPartOfProperty(NEED_TO_CHECKIN, value);
        //getVersionManager().checkIn(applicationSubPlugIn, "setNeedToCheckIn = " + value);
    }
    
    public boolean getNeedToCheckIn() {
        //String flag = (String) this.getPartOfProperty(NEED_TO_CHECKIN);
        //bNeedToCheckIn = flag != null && flag.equals("true");
        return bNeedToCheckIn;
    }
    
    /** chaeck out MATCH_CONFIG_FILE
     *
     *@return boolean checked out
     */
    public boolean getMatchConfigFile() {
        boolean checkedOut = true;
        try {
            // check out MATCH_CONFIG_FILE
            fileMatchConfigCheckedOut = getConfigurationFile(EviewProjectProperties.MATCH_ENGINE_FOLDER, EviewProjectProperties.MATCH_CONFIG_FILE, false);
        } catch (Exception ex) {
            msgCheckedOut += ex.getMessage() + "\n";
            checkedOut = false;
        }
        return checkedOut;
    }
    
    /** update MATCH_CONFIG_FILE in repository
     *
     *@param data
     */
    public void saveMatchConfigFile(String data, String comment, boolean checkIn) {
        try {
            if (fileMatchConfigCheckedOut != null) {
                if (mModifiedMatchConfig) {                    
                    writeConfigurationFile(fileMatchConfigCheckedOut, data);
                }
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }
    
    /** chaeck out OBJECT_DEF_FILE
     *
     *@return boolean checked out
     */
    private boolean getObjectXml() {
        boolean checkedOut = true;
        try {
            // check out OBJECT_DEF_FILE
            fileObjectCheckedOut = getObjectDefinitionFile();
        } catch (Exception ex) {
            msgCheckedOut += ex.getMessage() + "\n";
            checkedOut = false;
        }
        return checkedOut;
    }

    /** Save OBJECT_DEF_FILE
     *
     *@param data
     */
    public void saveObjectXml(String strXml, String comment, boolean checkIn) {
        try {
            if (fileObjectCheckedOut != null) {
                writeConfigurationFile(fileObjectCheckedOut, strXml);
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }
    
    /** chaeck out EDM
     *
     *@return boolean checked out
     */
    private boolean getEDMXml() {
        boolean checkedOut = true;
        try {
            // check out EDM
            fileEdmCheckedOut = getConfigurationFile(EviewProjectProperties.CONFIGURATION_FOLDER, EviewProjectProperties.EDM_XML, false);
        } catch (Exception ex) {
            msgCheckedOut += ex.getMessage() + "\n";
            checkedOut = false;
        }
        return checkedOut;
    }

    /** Save GuiConfigurationFile in repository
     *
     *@param data
     */
    public void saveEDMXml(String strXml, String comment, boolean checkIn) {
        try {
            if (fileEdmCheckedOut != null) {
                writeConfigurationFile(fileEdmCheckedOut, strXml);
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }
    
    /** chaeck out Mefa
     *
     *@return boolean checked out
     */
    private boolean getMefaXml() {
        boolean checkedOut = true;
        try {
            // check out mefa
            fileMefaCheckedOut = getConfigurationFile(EviewProjectProperties.CONFIGURATION_FOLDER, EviewProjectProperties.MEFA_XML, false);
        } catch (Exception ex) {
            msgCheckedOut += ex.getMessage() + "\n";
            checkedOut = false;
        }
        return checkedOut;
    }

    /** update MefaConfigurationFile in repository
     *
     *@param data
     */
    public void saveMefaConfigurationFile(String strXml, String comment, boolean checkIn) {
        try {
            if (fileMefaCheckedOut != null) {
                writeConfigurationFile(fileMefaCheckedOut, strXml);
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }
    
    /** chaeck out Query
     *
     *@return boolean checked out
     */
    private boolean getQueryXml() {
        boolean checkedOut = true;
        try {
            // check out query
            fileQueryCheckedOut = getConfigurationFile(EviewProjectProperties.CONFIGURATION_FOLDER, EviewProjectProperties.QUERY_XML, false);
        } catch (Exception ex) {
            msgCheckedOut += ex.getMessage() + "\n";
            checkedOut = false;
        }
        return checkedOut;
    }
    
    /** Save QueryConfigurationFile in repository
     *
     *@param data
     */
    public void saveQueryConfigurationFile(String strXml, String comment, boolean checkIn) {
        try {
            if (fileQueryCheckedOut != null) {
                writeConfigurationFile(fileQueryCheckedOut, strXml);
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }
    
    /** chaeck out Security
     *
     *@return boolean checked out
     */
    public boolean checkoutSecurityConfigurationFile() {
        boolean checkedOut = true;
        try {
            // check out security
            fileSecurityCheckedOut = getConfigurationFile(EviewProjectProperties.CONFIGURATION_FOLDER, EviewProjectProperties.SECURITY_XML, false);
        } catch (Exception ex) {
            msgCheckedOut += ex.getMessage() + "\n";
            checkedOut = false;
        }
        return checkedOut;
    }
    
    /** update SecurityConfigurationFile in repository
     *
     *@param data
     */
    public void saveSecurityConfigurationFile(String strXml, String comment, boolean checkIn) {
        try {
            if (fileSecurityCheckedOut != null) {
                writeConfigurationFile(fileSecurityCheckedOut, strXml);
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }
    
    /** chaeck out Master
     *
     *@return boolean checked out
     */
    private boolean getMasterXml() {
        boolean checkedOut = true;
        try {
            // check out master
            fileMasterCheckedOut = getConfigurationFile(EviewProjectProperties.CONFIGURATION_FOLDER, EviewProjectProperties.MASTER_XML, false);
        } catch (Exception ex) {
            msgCheckedOut += ex.getMessage() + "\n";
            checkedOut = false;
        }
        return checkedOut;
    }

    /** update MasterConfigurationFile in repository
     *
     *@param data
     */
    public void saveMasterConfigurationFile(String strXml, String comment, boolean checkIn) {
        try {
            if (fileMasterCheckedOut != null) {
                writeConfigurationFile(fileMasterCheckedOut, strXml);
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }
    
    /** chaeck out Update
     *
     *@return boolean checked out
     */
    private boolean getUpdateXml() {
        boolean checkedOut = true;
        try {
            // check out update
            fileUpdateCheckedOut = getConfigurationFile(EviewProjectProperties.CONFIGURATION_FOLDER, EviewProjectProperties.UPDATE_XML, false);
        } catch (Exception ex) {
            msgCheckedOut += ex.getMessage() + "\n";
            checkedOut = false;
        }
        return checkedOut;
    }

    /** update UpdateConfigurationFile in repository
     *
     *@param data
     */
    public void saveUpdateXml(String strXml, String comment, boolean checkIn) {
        try {
            if (fileUpdateCheckedOut != null) {
                writeConfigurationFile(fileUpdateCheckedOut, strXml);
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }
    
    /** chaeck out Validation
     *
     *@return boolean checked out
     */
    public boolean checkoutValidationConfigurationFile() {
        boolean checkedOut = true;
        try {
            // check out validation
            fileValidationCheckedOut = getConfigurationFile(EviewProjectProperties.CONFIGURATION_FOLDER, EviewProjectProperties.VALIDATION_XML, false);
        } catch (Exception ex) {
            msgCheckedOut += ex.getMessage() + "\n";
            checkedOut = false;
        }
        return checkedOut;
    }

    /** update ValidationConfigurationFile in repository
     *
     *@param data
     */
    public void saveValidationConfigurationFile(String strXml, String comment, boolean checkIn) {
        try {
            if (fileValidationCheckedOut != null) {
                writeConfigurationFile(fileValidationCheckedOut, strXml);
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }
    
    /* Reset all modified flag
     *@param flag
     */
    public void resetModified(boolean flag) {
        mModified = flag; 
        mModifiedMatchConfig = flag;
        mEindexObject.setModified(flag);
        mEDMType.setModified(flag);
        mQueryType.setModified(flag);
        mMasterType.setModified(flag);
        mMatchFieldDef.setModified(flag);
        mUpdateType.setModified(flag);
    }
    
    /* Show it to user if check out failed
     *
     *@return msgCheckedOut
     */
    public String getMsgCheckedOut() {
        return msgCheckedOut;
    }
    
    /** return the FileObject for object.xml
     * @return FileObject fo
     */ 

    public FileObject getObjectDefinitionFile() {
        FileObject fo = getConfigurationFile(EviewProjectProperties.CONFIGURATION_FOLDER, EviewProjectProperties.OBJECT_XML, false);
        return fo;
    }

    /** return the FileObject for comparatorsList.xml
     * @return FileObject fo
     */ 

    public FileObject getComparatorListFile() {
        FileObject fo = getConfigurationFile(EviewProjectProperties.MATCH_ENGINE_FOLDER, EviewProjectProperties.MATCH_COMPARATOR_XML, false);
        return fo;
    }
    
    /** returns Configuration FileObject from project directory
    If create == true, then it will create empty file if it does not exist
    Returns null, if no File exists and create == false
    throws EviewRepositoryException if  it can’t create a file
    */

    public FileObject getConfigurationFile(String folder, String name, boolean create) {
        FileObject file = null;
        try{
            FileObject srcDir = this.getSourceDirectory();
            FileObject confDir = srcDir.getFileObject(folder);            
            
            file = confDir.getFileObject(name);
            if (file == null && create == true) {
                file = confDir.createData(name);
            }
        } catch (IOException ex) {

        }
        return file;
    }
     
    /* Checkout all configurable files for editing
     *
     *@return bCheckedOut
     *
     */
    public boolean getAllConfigurableFiles() {
        return getObjectXml() &&
               getEDMXml() &&
               getMefaXml() &&
               getQueryXml() &&
               getMasterXml() &&
               getUpdateXml() &&
               getMatchConfigFile();
    }
    
    
    private void writeConfigurationFile(FileObject file, String str) {
        if (file != null) {
            try {
                FileLock fileLock = file.lock();
                OutputStream out = file.getOutputStream(fileLock);
                OutputStreamWriter writer = new OutputStreamWriter(out);
                writer.write(str);
                writer.close();
                fileLock.releaseLock();
            } catch (Exception ex) {
                String msg = ex.getMessage();
            }
        }
    }
    
    /* 
     * Reload all in-memory const
     */
    public void loadAll() {
        try {
            getEIndexObject(true);
            getEDMType(true);
            getQueryType(true);
            getMasterType(true);
            getMatchFieldDef(true);
            getMatchTypeList(true);
            getUpdateType(true);
            setModified(false);
            setModifiedMatchConfig(false);
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }
}
