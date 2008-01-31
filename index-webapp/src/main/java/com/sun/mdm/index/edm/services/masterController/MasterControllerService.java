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
/*
 * MasterControllerService.java 
 * Created on September 10, 2007, 04:45 PM
 * Author : Pratibha, Samba, Anil, Subrata
 *  
 */
package com.sun.mdm.index.edm.services.masterController;

import com.sun.mdm.index.edm.common.PullDownListItem;
import com.sun.mdm.index.edm.control.QwsController;
import com.sun.mdm.index.master.ConnectionInvalidException;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.master.MatchResult;
import com.sun.mdm.index.objects.PotentialDuplicate;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.objects.factory.SimpleFactory;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.index.edm.util.QwsUtil;
import com.sun.mdm.index.master.search.audit.AuditDataObject;
import com.sun.mdm.index.master.search.audit.AuditIterator;
import com.sun.mdm.index.master.search.audit.AuditSearchObject;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultIterator;
import com.sun.mdm.index.master.search.merge.MergeHistoryNode;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import com.sun.mdm.index.master.search.transaction.TransactionSearchObject;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.SBROverWrite;
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchIterator;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSearchObject;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSummary;

import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.page.PageException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.sql.Timestamp;

public class MasterControllerService {

    private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.services.master.MasterControllerService");
    public static final String LID = "LID";
    public static final String SYSTEM_CODE = "SYSTEM_CODE";
    public static final String MINOR_OBJECT_TYPE = "MINOR_OBJECT_TYPE";
    public static final String MINOR_OBJECT_BRAND_NEW = "MINOR_OBJECT_BRAND_NEW";
    public static final String MINOR_OBJECT_UPDATE = "MINOR_OBJECT_UPDATE";
    public static final String MINOR_OBJECT_REMOVE = "MINOR_OBJECT_REMOVE";
    public static final String MINOR_OBJECT_ID = "MINOR_OBJECT_ID";
    public static final String SYSTEM_OBJECT_BRAND_NEW = "SYSTEM_OBJECT_BRAND_NEW";
    public static final String SYSTEM_OBJECT_UPDATE = "SYSTEM_OBJECT_UPDATE";
    public static final String SBR_UPDATE = "SBR_UPDATE";
    public static final String HASH_MAP_TYPE = "HASH_MAP_TYPE";
    private String summaryInfo;
    private String auditMsg;
    private MasterController mMc;

    private String rootNodeName;
    /** Creates a new instance of MasterControllerService */
    public MasterControllerService() {
        mMc = QwsController.getMasterController();
    }

    /**
     * Returns a handle to the MasterController EJB
     *
     * @return a handle to the MasterController EJB.
     */
    public MasterController getMasterController() {
        return mMc;
    }

    /**
     *
     * @param String euid[]
     * @return ArrayList SBR
     * @exception UserException.
     */
    ArrayList getSBRs(String euid[])
            throws ProcessingException, UserException {
        ArrayList aList = new ArrayList();
        for (int i = 0; i < euid.length; i++) {
            aList.add(i, QwsController.getMasterController().getSBR(euid[i]));
        }
        return aList;
    }

    /**
     *
     * @param sourceEO 
     * @param destinationEO 
     * @return MergeResult
     * @throws com.sun.mdm.index.master.ProcessingException
     * @throws com.sun.mdm.index.master.UserException 
     * 
     */
    public MergeResult mergeEnterpriseObject(EnterpriseObject sourceEO,
            EnterpriseObject destinationEO)
            throws ProcessingException, UserException {

        if (sourceEO != null && destinationEO != null) {
            String sourceEUID = sourceEO.getEUID();
            String destEUID = destinationEO.getEUID();
            MergeResult mresult = QwsController.getMasterController().mergeEnterpriseObject(sourceEUID, destEUID, false);
            return mresult;
        } else {
            throw new UserException("None of sourceEO a nd destinationEO can be null");//user exception
        }
    }

    /**
     *
     * @param EnterpriseObject eo
     * @return MergeResult
     * @exception UserException.
     */
    MergeResult unmergeEnterpriseObject(EnterpriseObject eo)
            throws ProcessingException, UserException {
        if (eo != null) {
            String activeEUID = eo.getEUID();
            SBR sbr = eo.getSBR();
            String srcRevisionNumber = sbr.getRevisionNumber().toString();
            MergeResult mresult = QwsController.getMasterController().unmergeEnterpriseObject(activeEUID, srcRevisionNumber, false);
            return mresult;
        } else {
            throw new UserException("EnterpriseObject can not be null"); //user exception
        }
    }

    //subrata
    /**
     *
     * @param String systemCode
     * @param String sourceLID
     * @param String destinationLID
     * @param SystemObject finalSOImage
     * @param EnterpriseObject sourceEO
     * @param EnterpriseObject destinationEO
     * @return MergeResult
     * @exception UserException.
     */
    MergeResult mergeSystemObject(String systemCode,
            String sourceLID,
            String destinationLID,
            SystemObject finalSOImage,
            EnterpriseObject sourceEO,
            EnterpriseObject destinationEO)
            throws ProcessingException, UserException {
        if (systemCode != null && sourceLID != null && destinationLID != null && finalSOImage != null && sourceEO != null && destinationEO != null) {
            SBR srcSBR = sourceEO.getSBR();
            SBR destSBR = destinationEO.getSBR();
            String srcRevisionNumber = srcSBR.getRevisionNumber().toString();
            String destRevisionNumber = destSBR.getRevisionNumber().toString();
            ObjectNode destImage = finalSOImage.getObject();
            MergeResult mresult = QwsController.getMasterController().mergeSystemObject(systemCode, sourceLID,
                    destinationLID, destImage, srcRevisionNumber,
                    destRevisionNumber, false);
            return mresult;
        } else {
            throw new UserException("One of the required field is null"); //user exception
        }
    }

    /**
     *
     * @param EnterpriseObject sourceEO
     * @param EnterpriseObject destinationEO
     * @return EnterpriseObject
     * @exception UserException.
     */
    public EnterpriseObject getPostMergeEO(EnterpriseObject sourceEO, EnterpriseObject destinationEO) throws ProcessingException, UserException {
        if (sourceEO != null && destinationEO != null) {

            String sourceEUID = sourceEO.getEUID();
            String destEUID = destinationEO.getEUID();
            MergeResult mresult = QwsController.getMasterController().mergeEnterpriseObject(sourceEUID, destEUID, true);
            return mresult.getDestinationEO();
        } else {
            throw new UserException("None of sourceEO and destinationEO can be null"); //user exception
        }
    }

    public SystemObject mergeSystemObject(String systemCode, String sourceLID, String destLID, HashMap hm) throws ProcessingException, UserException, ObjectException, ValidationException, Exception {

        SystemObjectPK sourceSytemObjectPK = new SystemObjectPK(systemCode, sourceLID);
        SystemObject sourceSO = QwsController.getMasterController().getSystemObject(sourceSytemObjectPK);
        SystemObjectPK destSytemObjectPK = new SystemObjectPK(systemCode, destLID);
        SystemObject destSO = QwsController.getMasterController().getSystemObject(destSytemObjectPK);
        EnterpriseObject sourceEO = QwsController.getMasterController().getEnterpriseObject(sourceSytemObjectPK);
        EnterpriseObject destEO = QwsController.getMasterController().getEnterpriseObject(destSytemObjectPK);
        modifySystemObject(destSO, hm);
        MergeResult mergeResult = QwsController.getMasterController().mergeSystemObject(systemCode, sourceLID, destLID, destSO.getObject(), false);
        return mergeResult.getDestinationEO().getSystemObject(systemCode, destLID);

    }

        public SystemObject getPostMergeSystemObject(String systemCode, String sourceLID, String destLID, HashMap hm) throws ProcessingException, UserException, ObjectException, ValidationException, Exception {

        SystemObjectPK sourceSytemObjectPK = new SystemObjectPK(systemCode, sourceLID);
        SystemObject sourceSO = QwsController.getMasterController().getSystemObject(sourceSytemObjectPK);
        SystemObjectPK destSytemObjectPK = new SystemObjectPK(systemCode, destLID);
        SystemObject destSO = QwsController.getMasterController().getSystemObject(destSytemObjectPK);
        EnterpriseObject sourceEO = QwsController.getMasterController().getEnterpriseObject(sourceSytemObjectPK);
        EnterpriseObject destEO = QwsController.getMasterController().getEnterpriseObject(destSytemObjectPK);
        modifySystemObject(destSO, hm);
        MergeResult mergeResult = QwsController.getMasterController().mergeSystemObject(systemCode, sourceLID, destLID, destSO.getObject(), true);
        return mergeResult.getDestinationEO().getSystemObject(systemCode, destLID);

    }

    /**
     *
     * @param EnterpriseObject eo
     * @return List EnterpriseObject
     * @exception UserException.
     */
    List getPostUnmergeEOs(EnterpriseObject eo)
            throws ProcessingException, UserException {
        if (eo != null) {
            String origEUID = eo.getEUID();
            SBR origSBR = eo.getSBR();
            String origRevisionNumber = origSBR.getRevisionNumber().toString();
            MergeResult mresult = QwsController.getMasterController().unmergeEnterpriseObject(origEUID,
                    origRevisionNumber, true);
            EnterpriseObject origDestEO = mresult.getDestinationEO();
            EnterpriseObject origSourceEO = mresult.getSourceEO();
            List list = new ArrayList();
            ;
            list.add(0, origSourceEO);
            list.add(1, origDestEO);
            return list;
        } else {
            throw new UserException("EnterpriseObject can not be null"); //user exception
        }
    }

    /**
     *
     * @param String systemCode
     * @param String sourceLID
     * @param String destinationLID
     * @param SystemObject finalSOImage
     * @param EnterpriseObject sourceEO
     * @param EnterpriseObject destinationEO
     * @return List EnterpriseObject
     * @exception UserException.
     */
    List getPostLIDMergeEOs(String systemCode,
            String sourceLID,
            String destinationLID,
            SystemObject finalSOImage,
            EnterpriseObject sourceEO,
            EnterpriseObject destinationEO)
            throws ProcessingException, UserException {
        if (systemCode != null && sourceLID != null && destinationLID != null && finalSOImage != null && sourceEO != null && destinationEO != null) {
            SBR srcSBR = sourceEO.getSBR();
            String srcRevisionNumber = srcSBR.getRevisionNumber().toString();
            SBR destSBR = destinationEO.getSBR();
            String destRevisionNumber = destSBR.getRevisionNumber().toString();
            ObjectNode destImage = finalSOImage.getObject();
            MergeResult mresult = QwsController.getMasterController().mergeSystemObject(systemCode, sourceLID,
                    destinationLID, destImage, srcRevisionNumber,
                    destRevisionNumber, true);
            List list = new ArrayList();
            ;
            list.add(0, mresult.getSourceEO());//Implement this logic: If the LID merge is within the same EO, the EO at position 0 is null
            list.add(1, mresult.getDestinationEO());
            return list;
        } else {
            throw new UserException("None of systemCode, sourceLID, destinationLID, finalSOImage, sourceEO and destinationEO can be null"); //user exception
        }
    }

    /**
     *
     * @param String systemCode
     * @param String sourceLID
     * @param String destinationLID
     * @param EnterpriseObject eo
     * @return List EnterpriseObject
     * @exception UserException.
     */
    List getPostLIDUnmergeEOs(String systemCode,
            String sourceLID,
            String destinationLID,
            EnterpriseObject eo)
            throws ProcessingException, UserException {
        if (systemCode != null && sourceLID != null && destinationLID != null && eo != null) {
            SBR srcSBR = eo.getSBR();
            String srcRevisionNumber = srcSBR.getRevisionNumber().toString();
            MergeResult mresult = QwsController.getMasterController().unmergeSystemObject(systemCode, sourceLID, destinationLID, srcRevisionNumber, true);
            List list = new ArrayList();
            ;
            list.add(0, mresult.getSourceEO());//Implement this logic: If the LID merge is within the same EO, the EO at position 0 is null.
            list.add(1, mresult.getDestinationEO());
            return list;
        } else {
            throw new UserException("None of systemCode, sourceLID, destinationLID and EnterpriseObject can be null"); //user exception
        }
    }

    /**
     *
     * @param EnterpriseObject eo
     * @return void
     * @exception UserException, ProcessingException.
     */
    public void updateEnterpriseObject(EnterpriseObject eo)
            throws ProcessingException, UserException {
        if (eo != null) {
            QwsController.getMasterController().updateEnterpriseObject(eo);
        } else {
            throw new UserException("EnterpriseObject can not be null"); //user exception
        }
    }

    /**
     *
     * @param SystemObject so
     * @return void
     * @exception UserException, ProcessingException.
     */
    public void addSystemObject(SystemObject so)
            throws ProcessingException, UserException {
        if (so != null) {
            MatchResult mr = QwsController.getMasterController().executeMatchGui(so);
            switch (mr.getResultCode()) {
                case MatchResult.NEW_EO:
                    PotentialDuplicate[] potdup = mr.getPotentialDuplicates();
                    if (potdup != null && potdup.length > 0) {
                        setSummaryInfo("New enterprise object created with EUID: " + mr.getEUID()  +", which resulted in " + potdup.length + " potential duplicates");
                        setAuditMsg("New enterprise object created with EUID: " + mr.getEUID()  +", which resulted in " + potdup.length + " potential duplicates");
                    } else {                        
                        setSummaryInfo("New enterprise object created with EUID: " + mr.getEUID());
                        setAuditMsg("New enterprise object created with EUID: " + mr.getEUID());
                    }
                    break;

                case MatchResult.SYS_ID_MATCH:
                    setSummaryInfo("LID match found");
                    setAuditMsg("System/Local ID was found and enterprise object was updated");
                    break;

                case MatchResult.ASSUMED_MATCH:
                    setSummaryInfo("Assumed match is found in EUID " + mr.getEUID());
                    setAuditMsg("An assumed match was found and was updated");
                    break;

                case MatchResult.ADD_NOT_ALLOWED:
                    setSummaryInfo("This will result in an Add which is not allowed");
                    break;

                case MatchResult.UPDATE_NOT_ALLOWED:
                    setSummaryInfo("The assumed match is found, but update is not allowed");
                    break;

                case MatchResult.UPDATE_REJECTED:
                    setSummaryInfo("The processing logics has rejected an update");
                    break;

                default:
                    setSummaryInfo("Unknown result code returned by executeMatch()");
                    break;
                }

        } else {
            throw new UserException("EnterpriseObject can not be null"); //user exception
        }
    }

    /**
     *
     * @param ObjectNode majorObject
     * @param String minorObject
     * @return ObjectNode
     * @exception UserException, ProcessingException.
     */
    ObjectNode removeMinorObject(ObjectNode majorObject,
            ObjectNode minorObject)
            throws ProcessingException, UserException {
        if (majorObject != null && minorObject != null) {
            if (minorObject.isAdded()) {
                majorObject.removeChild(minorObject);
            } else {
                majorObject.deleteChild(minorObject);
            }
        } else {
            throw new UserException("None of majorObject and minorObject can be null"); //user exception
        }
        return majorObject;//which object to return, please validate; ideally it should not return anything.
    }

    /**
     *
     * @param String userName
     * @param String euid1
     * @param String euid2
     * @param String function
     * @param int screeneID
     * @param String detail
     * @return none
     * @exception ProcessingException, UserException, ObjectException.
     */
    public void insertAuditLog(String userName, String euid1, String euid2, String function, int screeneID, String detail)
            throws ProcessingException, UserException, ObjectException {
        if (userName != null && euid1 != null && function != null && detail != null) {
            String[] primaryObjType = ConfigManager.getInstance().getRootNodeNames();
            AuditDataObject ado = new AuditDataObject("", primaryObjType[0], euid1, euid2, function, detail, new Date(), userName);
            QwsController.getMasterController().insertAuditLog(ado);
        } else {
            throw new UserException("None of userName, euid1, function and detail can be null"); //user exception
        }

    }

    public void setSummaryInfo(String val) {
        summaryInfo = val;
    }

    public String getSummaryInfo() {
        return summaryInfo;
    }

    public String getAuditMsg() {
        return auditMsg;
    }

    public void setAuditMsg(String val) {
        auditMsg = val;
    }

    private boolean isObjectChanged(ObjectNode obj) {
        if (obj.isUpdated() || obj.isAdded() || obj.isRemoved()) {
            return true;
        } else {
            ArrayList children = obj.pGetChildren();
            if (children == null) {
                return false;
            } else {
                for (int i = 0; i < children.size(); i++) {
                    ObjectNode n = (ObjectNode) children.get(i);
                    if (isObjectChanged(n)) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private void throwProcessingException(Exception e) throws ProcessingException {
        mLogger.error("ProcessingException", e);
        if (e instanceof ProcessingException) {
            throw (ProcessingException) e;
        } else {
            throw new ProcessingException(e);
        }
    }

    public PotentialDuplicateIterator lookupPotentialDuplicates(PotentialDuplicateSearchObject pdso)
            throws ProcessingException, UserException {
        try {
            PotentialDuplicateIterator pdi = mMc.lookupPotentialDuplicates(pdso);
            return pdi;
        } catch (Exception e) {
            throw new ProcessingException(e);
        }

    }

    public TransactionIterator lookupTransactionHistory(TransactionSearchObject searchObj)
            throws ProcessingException, UserException {
        try {
            TransactionIterator iteratorTransactionHistory = mMc.lookupTransactions(searchObj);

            return iteratorTransactionHistory;
        } catch (Exception e) {
            throw new ProcessingException(e);
        }

    }

    public AuditIterator lookupAuditLog(AuditSearchObject obj) throws ProcessingException, UserException {
        if (obj == null) {
            throw new ProcessingException("AuditSearchObject can not null");
        }
        AuditIterator auditIterator = QwsController.getMasterController().lookupAuditLog(obj);
        return auditIterator;
    }

    public EnterpriseObject getEnterpriseObject(String euid) throws ProcessingException, UserException {
        EnterpriseObject eobject = QwsController.getMasterController().getEnterpriseObject(euid);
        return eobject;

    }

    public MergeHistoryNode getMergeHistory(String euid) throws ProcessingException, UserException {
        if (euid == null) {
            throw new UserException("euid should not be null");
        }
        MergeHistoryNode mergeHistoryNode = QwsController.getMasterController().getMergeHistory(euid);
        return mergeHistoryNode;
    }

    public EnterpriseObjectHistory viewMergeRecords(String transactionNumber) throws ProcessingException, UserException {
        if (transactionNumber == null) {
            throw new UserException("euid should not be null");
        }
        TransactionSummary transactionSummary1 = QwsController.getMasterController().lookupTransaction(transactionNumber);
        EnterpriseObjectHistory enterpriseObjectHistory = transactionSummary1.getEnterpriseObjectHistory();
        return enterpriseObjectHistory;
    }

    public MergeResult unmerge(String transactionNumber) throws ProcessingException, UserException {
        TransactionSummary transactionSummary = QwsController.getMasterController().lookupTransaction(transactionNumber);
        boolean validTrasaction = transactionSummary.getValidTransaction();
        TransactionObject transactionObject = transactionSummary.getTransactionObject();
        String transactionFunction = transactionSummary.getTransactionObject().getFunction();
        MergeResult mergeResult = null;
        if (transactionFunction.equals("euidMerge")) {
            mergeResult = QwsController.getMasterController().unmergeEnterpriseObject(transactionObject.getEUID(), false);
        } else if (transactionFunction.equals("lidMerge")) {

            String systemCode = transactionObject.getSystemCode();
            String LID1 = transactionObject.getLID1();
            String LID2 = transactionObject.getLID2();
            mergeResult = QwsController.getMasterController().unmergeSystemObject(systemCode, LID2, LID1, false);
        }
        return mergeResult;
    }

    public ArrayList getEnterpriseObjects(String euids[]) throws ProcessingException, UserException {
        ArrayList eoArrayList = new ArrayList();
        for (int i = 0; i < euids.length; i++) {
            EnterpriseObject eo = getEnterpriseObject(euids[i]);
            eoArrayList.add(eo);
        }

        return eoArrayList;
    }

    /**
     * 
     * @param  id is the duplicate id
     * @param flag is the boolean to set true or false
     * @throws com.sun.mdm.index.master.ProcessingException
     * @throws com.sun.mdm.index.master.UserException
     */
    //flag=false incase of autoresolve
    //flag = true incase of permanant resolve
    public void setAsDifferentPerson(String id, boolean flag) throws ProcessingException, UserException {
        try {
            QwsController.getMasterController().resolvePotentialDuplicate(id, flag);
        } catch (Exception e) {
            throw new ProcessingException(e);
        }
    }

    public void unresolvePotentialDuplicate(String id) throws ProcessingException, UserException {
        try {
            QwsController.getMasterController().unresolvePotentialDuplicate(id);
        } catch (Exception e) {
            throw new ProcessingException(e);
        }
    }

    public EOSearchResultIterator searchEnterpriseObject(EOSearchCriteria eoSearchCriteria, EOSearchOptions eoSearchOptions) throws ProcessingException, UserException {
        EOSearchResultIterator eoSearchResultIterator = QwsController.getMasterController().searchEnterpriseObject(eoSearchCriteria, eoSearchOptions);
        return eoSearchResultIterator;
    }

    // added by samba
    public SystemObject createSystemObject(String systemCode, String LID, HashMap hm) throws ObjectException, ValidationException, Exception {

        //System.out.println("===>: " + this.rootNodeName);
        ObjectNode majorObject = SimpleFactory.create(rootNodeName);

        Date dateTime = new Date();
        SystemObject sysObj = new SystemObject(systemCode, LID, this.rootNodeName,
                "active", "eGate", "Add",
                dateTime, "eGate", "Add", dateTime, majorObject);
        for (Object obj : hm.keySet()) {
            Object value = hm.get(obj);
            if (!obj.equals(MasterControllerService.SYSTEM_CODE) && !obj.equals(MasterControllerService.LID) && !obj.equals(MasterControllerService.HASH_MAP_TYPE)) {
                setObjectNodeFieldValue(majorObject, (String) obj, (String) value);
            }
        }
        sysObj.setObject(majorObject);
        return sysObj;
    }

    public EnterpriseObject createEnterpriseObject(SystemObject sysObj) throws ProcessingException, UserException {
        EnterpriseObject enterpriseObject = QwsController.getMasterController().createEnterpriseObject(sysObj);
        return enterpriseObject;
    }

    public SystemObject modifySystemObject(SystemObject sysObj, HashMap hm) throws ObjectException, ValidationException, Exception {
        if (hm == null) {
            return sysObj;
        }
        ObjectNode majorObject = sysObj.getObject();

        String type = majorObject.pGetType();
        for (Object obj : hm.keySet()) {
            Object value = hm.get(obj);
            if (!obj.equals(MasterControllerService.SYSTEM_CODE) && !obj.equals(MasterControllerService.LID) && !obj.equals(MasterControllerService.HASH_MAP_TYPE)) {
                String key = (String) obj;
                // for removing type prefix
                int startOfPrefix = key.indexOf(type);
                int startSubString = startOfPrefix + type.length() + 1; // 1 is for .
                key = key.substring(startSubString);
                setObjectNodeFieldValue(majorObject, key, (String) value);
            }
        }
        sysObj.setObject(majorObject);
        return sysObj;
    }

    public SystemObject addMinorObject(SystemObject systemObject, String objectType, HashMap hm) throws ObjectException, ValidationException {
        ObjectNode majorObject = systemObject.getObject();
        systemObject.setUpdateFlag(true);
        ObjectNode minorObject = SimpleFactory.create(objectType); // objectType eg value "Address"
        for (Object obj : hm.keySet()) {
            Object value = hm.get(obj);
            if (!obj.equals(MasterControllerService.SYSTEM_CODE) && !obj.equals(MasterControllerService.LID) && !obj.equals(MasterControllerService.HASH_MAP_TYPE) && !obj.equals(MINOR_OBJECT_TYPE)) {
                setObjectNodeFieldValue(minorObject, (String) obj, (String) value);
            }
        }
        majorObject.addChildHard(minorObject);
        return systemObject;
    }

    public SystemObject addMinorObject(SBR sbr, String objectType, HashMap hm) throws ObjectException, ValidationException {
        ObjectNode minorObject = SimpleFactory.create(objectType); // objectType eg value "Address"

        for (Object obj : hm.keySet()) {
            Object value = hm.get(obj);
            if (!obj.equals(MasterControllerService.SYSTEM_CODE) && !obj.equals(MasterControllerService.LID) && !obj.equals(MasterControllerService.HASH_MAP_TYPE) && !obj.equals(MINOR_OBJECT_TYPE)) {
                setObjectNodeFieldValue(minorObject, (String) obj, (String) value);
            }
        }
        // majorObject.addChild(minorObject);
        sbr.addChild(minorObject);
        return sbr;
    }

    public ObjectNode modifyMinorObject(ObjectNode minorObject, HashMap hm) throws ObjectException, ValidationException {
        for (Object obj : hm.keySet()) {
            Object value = hm.get(obj);
            //System.out.println("===> IN MODIFYING MINOR OBJECTS===> " + obj + "< ===>" + value );
            //Check the hash map objects for not MINOR_OBJECT_TYPE,MINOR_OBJECT_UPDATE ,HASH_MAP_TYPE,LID,SYSTEM_CODE 
            if (!obj.equals(MasterControllerService.MINOR_OBJECT_TYPE) 
                && !obj.equals(MasterControllerService.LID)
                && !obj.equals(MasterControllerService.SYSTEM_CODE)
                && !obj.equals(MasterControllerService.MINOR_OBJECT_UPDATE)
                && !obj.equals(MasterControllerService.MINOR_OBJECT_ID)
                && !obj.equals(MasterControllerService.HASH_MAP_TYPE)) {
                setObjectNodeFieldValue(minorObject, (String) obj, (String) value);
            } // Example Key: City Value: Bangalore
        }
        return minorObject;
    }
    
    public ObjectNode modifyMinorObjectSBR(ObjectNode minorObject, HashMap hm, SBR sbr) throws ObjectException, ValidationException {
        for (Object obj : hm.keySet()) {
            Object value = hm.get(obj);
            //System.out.println("===> IN MODIFYING MINOR OBJECTS===> " + obj + "< ===>" + value );
            //Check the hash map objects for not MINOR_OBJECT_TYPE,MINOR_OBJECT_UPDATE ,HASH_MAP_TYPE,LID,SYSTEM_CODE 
            if (!obj.equals(MasterControllerService.MINOR_OBJECT_TYPE) 
                && !obj.equals(MasterControllerService.LID)
                && !obj.equals(MasterControllerService.SYSTEM_CODE)
                && !obj.equals(MasterControllerService.MINOR_OBJECT_UPDATE)
                && !obj.equals(MasterControllerService.MINOR_OBJECT_ID)
                && !obj.equals(MasterControllerService.HASH_MAP_TYPE)) {
                setObjectNodeFieldValue(minorObject, (String) obj, (String) value, sbr);
            } // Example Key: City Value: Bangalore
        }
        return minorObject;
    }

    public SystemObject addMinorObjects(SystemObject systemObject, ArrayList minorObjects) throws ObjectException, ValidationException, UserException, ProcessingException {
        for (Object minorObj : minorObjects) {
            HashMap hm = null;
            try {
                hm = (HashMap) minorObj;
            } catch (ClassCastException ese) {
                throw new UserException("minorObjects should be type of HashMap " + minorObjects);
            }
            String objectType = (String) hm.get(MINOR_OBJECT_TYPE);
            addMinorObject(systemObject, objectType, hm);
        }
        return systemObject;
    }

    public SBR modifySBR(SBR sbr, HashMap hm) throws ObjectException, ValidationException, Exception {
        if (hm == null) {
            return sbr;
        }
        ObjectNode majorObject = sbr.getObject();

        for (Object obj : hm.keySet()) {
            Object value = hm.get(obj);
            if (!obj.equals(MasterControllerService.SYSTEM_CODE) && !obj.equals(MasterControllerService.LID) && !obj.equals(MasterControllerService.HASH_MAP_TYPE)) {
                setObjectNodeFieldValue(majorObject, (String) obj, (String) value, sbr);
            }
        }
        return sbr;
    }

    public EnterpriseObject save(EnterpriseObject eo, ArrayList changedSBR, ArrayList systemObjects, ArrayList minorObjects) throws UserException, ObjectException, ValidationException, Exception {
        EnterpriseObject localEO = null;
        if (systemObjects != null && systemObjects.isEmpty() == false) {
            localEO = modifySystemObjects(eo, systemObjects);
        }
        if (minorObjects != null && minorObjects.isEmpty() == false) {
            if (localEO == null) {
                modifySystemObjects(eo, minorObjects); //IN EDIT SO CASE
            } else {
                modifySystemObjects(localEO, minorObjects);//IN ADD SO CASE
            }
        }
        if (changedSBR != null) {
            for (Object obj : changedSBR) {
                HashMap hm = null;
                try {
                    hm = (HashMap) obj;
                } catch (ClassCastException cce) {
                    throw new UserException("ChangedSBR should contain SystemObjects only ");
                }
                if (hm.get(MasterControllerService.HASH_MAP_TYPE).equals(MasterControllerService.MINOR_OBJECT_BRAND_NEW)) {
                    addMinorObject(eo.getSBR(), (String) hm.get(MasterControllerService.MINOR_OBJECT_TYPE), hm);
                } else if (hm.get(MasterControllerService.HASH_MAP_TYPE).equals(MasterControllerService.MINOR_OBJECT_REMOVE)) {
                    SystemObject systemObject = eo.getSBR();
                    String type = (String) hm.get(MasterControllerService.MINOR_OBJECT_TYPE);
                    String id = (String) hm.get(MasterControllerService.MINOR_OBJECT_ID);
                    if (type == null || id == null) {
                        throw new UserException(" Hashmap should provide MINOR_OBJECT_TYPE, MINOR_OBJECT_ID for removing a MinorObject");
                    }
                    ObjectNode child = systemObject.getObject().getChild(type, id);
                    removeMinorObject(child, hm);
                } else if (hm.get(MasterControllerService.HASH_MAP_TYPE).equals(MasterControllerService.MINOR_OBJECT_UPDATE)) {
                    SystemObject systemObject = eo.getSBR();
                    String type = (String) hm.get(MasterControllerService.MINOR_OBJECT_TYPE);
                    String id = (String) hm.get(MasterControllerService.MINOR_OBJECT_ID);
                    if (type == null || id == null) {
                        throw new UserException("Hashmap should provide MINOR_OBJECT_TYPE, MINOR_OBJECT_ID for adding a MinorObject");
                    }
                    ObjectNode child = systemObject.getObject().getChild(type, id);
                    modifyMinorObjectSBR(child, hm, eo.getSBR());
                } else if (hm.get(MasterControllerService.HASH_MAP_TYPE).equals(MasterControllerService.SBR_UPDATE)) {
                    SystemObject systemObject = eo.getSBR();
                    modifySBR(eo.getSBR(), hm);
                }
            }
        }
        if (eo != null) {            
            updateEnterpriseObject(eo);
        } else {
            updateEnterpriseObject(localEO);
        }

        return eo;
    }

    public EnterpriseObject modifySystemObjects(EnterpriseObject eo, ArrayList systemObjects) throws UserException, ObjectException, ValidationException, Exception {
        SystemObject createdSystemObject = null;
        for (Object obj : systemObjects) {
            HashMap hm = null;
            try {
                hm = (HashMap) obj;
            } catch (ClassCastException cce) {
                throw new UserException(" Arguement systemObjects should contain SystemObjects only ");
            }
            if (hm.get(MasterControllerService.HASH_MAP_TYPE).equals(MasterControllerService.SYSTEM_OBJECT_UPDATE)) {
                SystemObject so = eo.getSystemObject((String) hm.get(MasterControllerService.SYSTEM_CODE), (String) hm.get(MasterControllerService.LID));
                modifySystemObject(so, hm);
            } else if (hm.get(MasterControllerService.HASH_MAP_TYPE).equals(MasterControllerService.SYSTEM_OBJECT_BRAND_NEW)) {
                createdSystemObject = createSystemObject((String) hm.get(MasterControllerService.SYSTEM_CODE), (String) hm.get(MasterControllerService.LID), hm);
                if (eo != null) {
                    eo.addSystemObject(createdSystemObject);
                } else {
                    eo = createEnterpriseObject(createdSystemObject);
                }
            } else if (hm.get(MasterControllerService.HASH_MAP_TYPE).equals(MasterControllerService.MINOR_OBJECT_BRAND_NEW)) {
                SystemObject systemObject = null;
                if (eo != null) {
                    systemObject = eo.getSystemObject((String) hm.get(MasterControllerService.SYSTEM_CODE), (String) hm.get(MasterControllerService.LID));
                } else {
                    systemObject = getSystemObject((String) hm.get(MasterControllerService.SYSTEM_CODE), (String) hm.get(MasterControllerService.LID));
                }
                systemObject.setUpdateUser("eview");
                addMinorObject(systemObject, (String) hm.get(MasterControllerService.MINOR_OBJECT_TYPE), hm);
            } else if (hm.get(MasterControllerService.HASH_MAP_TYPE).equals(MasterControllerService.MINOR_OBJECT_REMOVE)) {
                SystemObject systemObject = eo.getSystemObject((String) hm.get(MasterControllerService.SYSTEM_CODE), (String) hm.get(MasterControllerService.LID));
                String type = (String) hm.get(MasterControllerService.MINOR_OBJECT_TYPE);
                String id = (String) hm.get(MasterControllerService.MINOR_OBJECT_ID);
                if (type == null || id == null) {
                    throw new UserException("Hashmap should provide MINOR_OBJECT_TYPE, MINOR_OBJECT_ID for removing a MinorObject");
                }
                ObjectNode child = systemObject.getObject().getChild(type, id);
                removeMinorObject(child, hm);
            } else if (hm.get(MasterControllerService.HASH_MAP_TYPE).equals(MasterControllerService.MINOR_OBJECT_UPDATE)) {
                SystemObject systemObject = eo.getSystemObject((String) hm.get(MasterControllerService.SYSTEM_CODE), (String) hm.get(MasterControllerService.LID));
                String type = (String) hm.get(MasterControllerService.MINOR_OBJECT_TYPE);
                String id = (String) hm.get(MasterControllerService.MINOR_OBJECT_ID);
                if (type == null || id == null) {
                    throw new UserException(" Hashmap should provide MINOR_OBJECT_TYPE, MINOR_OBJECT_ID for adding a MinorObject");
                }
                ObjectNode child = systemObject.getObject().getChild(type, id);
                modifyMinorObject(child, hm);
            }
        }
        return eo;
    }

    public void removeMinorObject(ObjectNode minorObject, HashMap hm) throws ObjectException {
        minorObject.setRemoveFlag(true);
    }

    public String[][] getSystemCodes() {
        int sysCount = ValidationService.getInstance().getValueCount(ValidationService.CONFIG_MODULE_SYSTEM);
        String[][] sysMasks = ValidationService.getInstance().getSystemInputMasks();
        return sysMasks;
    }

    public boolean isSystemObjectExists(String systemCode, String LID) throws ProcessingException, UserException {
        SystemObjectPK systemObjectPK = new SystemObjectPK();
        systemObjectPK.systemCode = systemCode;
        systemObjectPK.lID = LID;
        SystemObject systemObject = QwsController.getMasterController().getSystemObject(systemObjectPK);
        if (systemObject != null) {
            return false;
        } else {
            return true;
        }
    }

    public SystemObject getSystemObject(String systemCode, String LID) throws ProcessingException, UserException {
        SystemObjectPK systemObjectPK = new SystemObjectPK();
        systemObjectPK.systemCode = systemCode;
        systemObjectPK.lID = LID;
        SystemObject systemObject = QwsController.getMasterController().getSystemObject(systemObjectPK);
        return systemObject;
    }

    public SystemObject[] getSystemObjects(String systemCode, String LID[]) throws ProcessingException, UserException {
        SystemObject[] systemObject = new SystemObject[LID.length];
        for (int i = 0; i < LID.length; i++) {
            SystemObjectPK systemObjectPK = new SystemObjectPK();
            systemObjectPK.systemCode = systemCode;
            systemObjectPK.lID = LID[i];
            systemObject[i] = QwsController.getMasterController().getSystemObject(systemObjectPK);
        }
        return systemObject;
    }

    public HashMap getEnterpriseObjectAsHashMap(EnterpriseObject eo, EPathArrayList ePathArrayList) throws ObjectException, EPathException {
        return getSystemObjectAsHashMap(eo.getSBR(), ePathArrayList);
    }

    public ArrayList getEnterpriseObjectChildrenArrayList(EnterpriseObject eo, EPathArrayList ePathArrayList, String childObjType, String operation) throws ObjectException, EPathException {
        return getSystemObjectChildrenArrayList(eo.getSBR(), ePathArrayList, childObjType, operation);
    }

    public HashMap getSystemObjectAsHashMap(SystemObject so, EPathArrayList ePathArrayList) throws ObjectException, EPathException {
        EPath[] ePaths = ePathArrayList.toArray();
        HashMap hashMap = new HashMap();
        ObjectNode objNode = so.getObject();
        String dateField = new String();

        String childType = so.getChildType();
        for (int i = 0; i < ePaths.length; i++) {
            String ePathName = ePaths[i].getName();
            if (ePathName.indexOf(childType) == 0) {
                Object fieldValue = EPathAPI.getFieldValue(ePathName, objNode);
                if (fieldValue instanceof java.util.Date) {
                    dateField = new SimpleDateFormat("MM/dd/yyyy").format(fieldValue);
                    hashMap.put(ePathName, dateField);
                } else {
                    hashMap.put(ePathName, fieldValue);
                }
            }
        }
        return hashMap;
    }

    public ArrayList getSystemObjectChildrenArrayList(SystemObject so, EPathArrayList ePathArrayList, String childObjType, String operation) throws ObjectException, EPathException {
        ArrayList resultArrayList = new ArrayList();
        EPath[] ePaths = ePathArrayList.toArray();

        ObjectNode objNode = so.getObject();
        String dateField = new String();

        ArrayList allChildrenFromHashMap = objNode.getAllChildrenFromHashMap();
        if (allChildrenFromHashMap != null && allChildrenFromHashMap.isEmpty() == false) {

            for (Object obj : allChildrenFromHashMap) {
                ObjectNode childObjectNode = (ObjectNode) obj;
                String type = childObjectNode.pGetType();
                if (childObjType.equalsIgnoreCase(type)) {
                    HashMap hashmapChild = new HashMap();
                    for (int i = 0; i < ePaths.length; i++) {
                        String ePathName = ePaths[i].getName();
                        if (ePathName.indexOf(type) == 0) {
                            Object fieldValue = EPathAPI.getFieldValue(ePathName, childObjectNode);
                            if (ePathName.contains(type)) {
                                if (fieldValue instanceof java.util.Date) {
                                    dateField = new SimpleDateFormat("MM/dd/yyyy").format(fieldValue);
                                    hashmapChild.put(ePathName, dateField);
                                } else {
                                    hashmapChild.put(ePathName, fieldValue);
                                }
                            }
                        }

                    }
                    hashmapChild.put(MasterControllerService.MINOR_OBJECT_ID, childObjectNode.getObjectId()); // add the ID of the child here
                    if (operation != null) {
                        hashmapChild.put(MasterControllerService.HASH_MAP_TYPE, operation);
                    }
                    resultArrayList.add(hashmapChild);// adding the child type finally
                }

            }
        }
        return resultArrayList;
    }

    public void activateSystemObject(SystemObject systemObject) throws ObjectException, ProcessingException, UserException {
        SystemObjectPK systemObjectPK = new SystemObjectPK();
        systemObjectPK.systemCode = systemObject.getSystemCode();
        systemObjectPK.lID = systemObject.getLID();
        QwsController.getMasterController().activateSystemObject(systemObjectPK);
    }

    public void activateSystemObject(String systemCode, String LID) throws ObjectException, ProcessingException, UserException {
        SystemObjectPK systemObjectPK = new SystemObjectPK();
        systemObjectPK.systemCode = systemCode;
        systemObjectPK.lID = LID;
        QwsController.getMasterController().activateSystemObject(systemObjectPK);
    }

    public void deactivateSystemObject(SystemObject systemObject) throws ObjectException, ProcessingException, UserException {
        SystemObjectPK systemObjectPK = new SystemObjectPK();
        systemObjectPK.systemCode = systemObject.getSystemCode();
        systemObjectPK.lID = systemObject.getLID();
        QwsController.getMasterController().deactivateSystemObject(systemObjectPK);
    }

    public void deactivateSystemObject(String systemCode, String LID) throws ObjectException, ProcessingException, UserException {
        SystemObjectPK systemObjectPK = new SystemObjectPK();
        systemObjectPK.systemCode = systemCode;
        systemObjectPK.lID = LID;
        QwsController.getMasterController().deactivateSystemObject(systemObjectPK);
    }

    public MergeHistoryNode getMergeHistoryNode(String euid) throws ProcessingException, UserException {
        MergeHistoryNode mergeHistoryNode = QwsController.getMasterController().getMergeHistory(euid); // arg: EUID
        return mergeHistoryNode;
    }

    public void viewMergeTree(String euid) throws ProcessingException, UserException {
        MergeHistoryNode mergeHistoryNode = QwsController.getMasterController().getMergeHistory(euid); // arg: EUID
        while (mergeHistoryNode != null) {
            String sourceEUID = mergeHistoryNode.getSourceNode().getEUID();
            String destEUID = mergeHistoryNode.getDestinationNode().getEUID();
            mergeHistoryNode = mergeHistoryNode.getParentNode();
        }
    }

    public ArrayList viewHistory(String euid) throws ProcessingException, UserException, PageException, RemoteException {
        TransactionSearchObject transactionSearchObject = new TransactionSearchObject();
        transactionSearchObject.setEUID(euid);
        TransactionIterator transactionIterator = QwsController.getMasterController().lookupTransactions(transactionSearchObject);
        ArrayList historyEOs = new ArrayList();
        while (transactionIterator.hasNext()) {
            TransactionSummary transactionSummary = transactionIterator.next();
            String euid1 = (String) transactionSummary.getTransactionObject().getEUID();
            String transNumber = transactionSummary.getTransactionObject().getTransactionNumber();
            String functionName = transactionSummary.getTransactionObject().getFunction();

            Date modifiedDate = transactionSummary.getTransactionObject().getTimeStamp();
            String dateOfAction = new SimpleDateFormat("MM/dd/yyyy").format(modifiedDate);

            String title = functionName + " " + dateOfAction + ":" +transNumber;

            EnterpriseObject eo = null;
            if (functionName.equalsIgnoreCase("euidMerge")) {
                if (transactionSummary.getEnterpriseObjectHistory().getAfterEO() != null) {
                    eo = transactionSummary.getEnterpriseObjectHistory().getAfterEO();
                } else if (transactionSummary.getEnterpriseObjectHistory().getAfterEO2() != null) {
                    eo = transactionSummary.getEnterpriseObjectHistory().getAfterEO2();
                }
            } else {
                eo = transactionSummary.getEnterpriseObjectHistory().getAfterEO();
            }

            HashMap eoHashMapWithFunction = new HashMap();
            eoHashMapWithFunction.put(title, eo);

            historyEOs.add(eoHashMapWithFunction);
        }
        return historyEOs;
    }

    public EnterpriseObject getEnterpriseObjectForSO(SystemObject so) {
        EnterpriseObject enterpriseObject = null;
        try {
            SystemObjectPK systemObjectPK = new SystemObjectPK();
            systemObjectPK.lID = so.getLID();
            systemObjectPK.systemCode = so.getSystemCode();
            enterpriseObject = QwsController.getMasterController().getEnterpriseObject(systemObjectPK);
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(MasterControllerService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(MasterControllerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return enterpriseObject;

    }

    public MergeResult unMerge(String euid) throws ProcessingException, UserException, PageException, RemoteException {
        String findMergeString = null;
        TransactionObject findMergeType = findMergeType(euid);
        if (findMergeType(euid) != null) {
            findMergeString = findMergeType(euid).getFunction();
        } else {
            throw new UserException("EUID has not been merged or has already been merged");
        }
        MergeResult unmergeEnterpriseObject = null;
        if (findMergeString.endsWith("euidMerge")) {
            unmergeEnterpriseObject = QwsController.getMasterController().unmergeEnterpriseObject(euid, false);
        } else if (findMergeString.endsWith("lidMerge")) {
            String systemCode = findMergeType.getSystemCode();
            String LID1 = findMergeType.getLID1();
            String LID2 = findMergeType.getLID2();
            unmergeEnterpriseObject = QwsController.getMasterController().unmergeSystemObject(systemCode, LID2, LID1, false);
        }
        return unmergeEnterpriseObject;
    }

    public TransactionObject findMergeType(String euid) throws ProcessingException, UserException, PageException, RemoteException {
        TransactionObject transactionObject = null;
        TransactionSearchObject transactionSearchObject = new TransactionSearchObject();        
        transactionSearchObject.setEUID(euid);
        TransactionIterator transactionIterator = QwsController.getMasterController().lookupTransactions(transactionSearchObject);
        ArrayList historyEOs = new ArrayList();
        while (transactionIterator.hasNext()) {
            TransactionSummary transactionSummary = transactionIterator.next();
            String functionName = transactionSummary.getTransactionObject().getFunction();
            if (functionName.indexOf("Merge") != -1) {
                transactionObject = transactionSummary.getTransactionObject();
            }
        }
        return transactionObject;
    }

    /**
     * 
     * @param transactionNumber for which user needs to find whether this Transaction is Merge or Not
     * @return true if the transaction is EUIDMerge
     * @throws com.sun.mdm.index.master.ProcessingException
     * @throws com.sun.mdm.index.master.UserException
     */
    public boolean isEUIDMerge(String transactionNumber) throws ProcessingException, UserException{
        TransactionSummary transactionSummary = QwsController.getMasterController().lookupTransaction(transactionNumber);
        return transactionSummary.getTransactionObject().getFunction().equalsIgnoreCase("euidMerge");        
    }
    
    public String getPotentialDuplicateID(String euid, String dupID) throws ProcessingException, UserException, PageException, RemoteException {
        PotentialDuplicateSearchObject potentialDuplicateSearchObject = new PotentialDuplicateSearchObject();
        potentialDuplicateSearchObject.setEUID(euid);
        PotentialDuplicateIterator pdPageIter = QwsController.getMasterController().lookupPotentialDuplicates(potentialDuplicateSearchObject);
        int iter_count = pdPageIter.count();

        String euid1 = null;
        String euid2 = null;
        String duplicateId = null;
        PotentialDuplicateSummary potentialDuplicateSummary = null;
        if (iter_count > 0) {
            while (pdPageIter.hasNext()) {
                potentialDuplicateSummary = pdPageIter.next();
                euid1 = potentialDuplicateSummary.getEUID1();
                euid2 = potentialDuplicateSummary.getEUID2();
                if (euid1.equals(euid) && euid2.equals(dupID)) {
                    duplicateId = potentialDuplicateSummary.getId();
                }
            }
        }
        return duplicateId;
    }

    public String getPotentialDuplicateStatus(String euid, String dupID) throws ProcessingException, UserException, PageException, RemoteException {
        PotentialDuplicateSearchObject potentialDuplicateSearchObject = new PotentialDuplicateSearchObject();
        potentialDuplicateSearchObject.setEUID(euid);
        PotentialDuplicateIterator pdPageIter = QwsController.getMasterController().lookupPotentialDuplicates(potentialDuplicateSearchObject);
        int iter_count = pdPageIter.count();

        String euid1 = null;
        String euid2 = null;
        String duplicateStatus = null;
        PotentialDuplicateSummary potentialDuplicateSummary = null;
        if (iter_count > 0) {
            while (pdPageIter.hasNext()) {
                potentialDuplicateSummary = pdPageIter.next();
                euid1 = potentialDuplicateSummary.getEUID1();
                euid2 = potentialDuplicateSummary.getEUID2();
                 if (euid1.equals(euid) && euid2.equals(dupID)) {
                    duplicateStatus = potentialDuplicateSummary.getStatus();
                }
                //duplicateStatus = potentialDuplicateSummary.getStatus();
            }
        }
        return duplicateStatus;
    }

    /** 
     * This method invokes method in EJB to UNDO an existing Assumed Match
     * @return EUID of new EO
     * @param assumedMatchId Id of assumed match to be resolved
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid id
     */
    public String undoAssumedMatch(String assumedMatchId) throws ProcessingException, UserException {
        String strEUID = mMc.undoAssumedMatch(assumedMatchId);
        return strEUID;
    }
    
    /** 
     * This method invokes method in EJB to preview/simulate the UNDo on an existing Assumed Match
     * @return EnterpriseObject
     * @param assumedMatchId Id of assumed match to be resolved
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid id
     */
     public EnterpriseObject previewUndoAssumedMatch(String assumedMatchId) throws ProcessingException, UserException {
        EnterpriseObject newEO = mMc.previewUndoAssumedMatch(assumedMatchId);
        return newEO;
    }
    
    /** 
     * This method fetches existing assumed Matches
     * @return AssumedMatchIterator
     * @param AssumedMatchSearchObject 
     * @exception ProcessingException An error has occured.
     * @exception UserException 
     */
    public AssumedMatchIterator lookupAssumedMatches(AssumedMatchSearchObject amso) throws ProcessingException, UserException {
        AssumedMatchIterator assumedMatchIterator = mMc.lookupAssumedMatches(amso);
        return assumedMatchIterator;
    }
    
    /** 
     * This method returns count of existing potentential duplicates
     * @return int count
     * @param 
     * @exception ProcessingException An error has occured.
     * @exception UserException 
     */
    public int countPotentialDuplicates(Timestamp startDate, Timestamp endDate)
            throws ProcessingException, UserException {
      PotentialDuplicateSearchObject pdso = new PotentialDuplicateSearchObject();
      pdso.setCreateEndDate(endDate);
      pdso.setCreateStartDate(startDate);
      int countPotentialDuplicates = 0;
        try {
            countPotentialDuplicates = mMc.countPotentialDuplicates(pdso);
        } catch (ProcessingException e) {
        throwProcessingException(e);
        } catch (RuntimeException e) {
        throwProcessingException(e);
        }
      return countPotentialDuplicates;
    }
    
    /** 
     * This method returns count of existing assumed matches
     * @return int count
     * @param 
     * @exception ProcessingException An error has occured.
     * @exception UserException 
     */
    public int countAssumedMatches(Timestamp startDate, Timestamp endDate)
            throws ProcessingException, UserException {
      AssumedMatchSearchObject amso = new AssumedMatchSearchObject();
      amso.setCreateEndDate(endDate);
      amso.setCreateStartDate(startDate);
      int countAssumedMatches = 0;
        try {
            countAssumedMatches = mMc.countAssumedMatches(amso);
        } catch (ProcessingException e) {
        throwProcessingException(e);
        } catch (RuntimeException e) {
        throwProcessingException(e);
        }
      return countAssumedMatches;
    }
    
    /**
     * 
     * @param hm contains Key is ePath for the string which the link should persisted, Value as Value as SO's SystemCode:LID from which we should collect the link
     * @param eo the EnterpriseObject for which the links should be added.
     * @return Modified Enterprise objects with the links.
     * @throws com.sun.mdm.index.master.ProcessingException
     * @throws com.sun.mdm.index.master.UserException
     */
    public EnterpriseObject saveLinks(HashMap hm, EnterpriseObject eo) throws ProcessingException, UserException {
        HashMap hashMapNew = new HashMap();
        for (Object obj : hm.keySet()) {
            String str = (String) obj;
            hashMapNew.put("LINK:" + str, resolveSystemObject((String)hm.get(str),eo));
        }       
        //EnterpriseObject eo1 = null;
        EnterpriseObject eo1 = QwsController.getMasterController().updateSBR(hashMapNew, eo, false);
        return eo1;        
    }
    
    /**
     * 
     * @param str a String in the format of SystemCode:LID 
     * @param eo EnterpriseObject having a SystemObject with SystemCode, LID specified in str String
     * @return SystemObject with SystemCode, LID specified in str String
     * @throws com.sun.mdm.index.master.UserException
     * @throws com.sun.mdm.index.objects.exception.ObjectException
     */
    
    private SystemObject resolveSystemObject(String str, EnterpriseObject eo) throws UserException, ObjectException{
        if(str == null || str.indexOf(":") == -1)
            throw new UserException("String should in the format of SystemCode:LID");
        String split[] = str.split(":");
        String systemCode = split[0];
        String lid = split[1];
        return eo.getSystemObject(systemCode, lid);      
    }
    
    /**
     * 
     * @param childrenHashmaps Arraylist of HashMaps, Each Hashmap should contain a perticuler Child's Links. This HashMap should have values for MINOR_OBJECT_ID, MINOR_OBJECT_TYPE
     * @param eo the EnterpriseObject for which the links should be added.
     * @return Modified Enterprise objects with the links for clildren.
     * @throws com.sun.mdm.index.master.ProcessingException
     * @throws com.sun.mdm.index.master.UserException
     */
    public EnterpriseObject saveLinksForChildren(ArrayList childrenHashmaps, EnterpriseObject eo) throws ProcessingException, UserException {
        HashMap hashMapNew = new HashMap();
        for(Object o: childrenHashmaps){
            HashMap hm = (HashMap) o;
            String minorObjectId = (String) hm.get(MINOR_OBJECT_ID);
            for (Object obj : hm.keySet()) {
                String str = (String) obj;
                if( !str.equals(MINOR_OBJECT_ID) );
                    hashMapNew.put("LINK:" + str + "@" + minorObjectId, resolveSystemObject((String)hm.get(str),eo));
            }       
        }
        //EnterpriseObject eo1 = null;
        EnterpriseObject eo1 = QwsController.getMasterController().updateSBR(hashMapNew, eo, false);
        return eo1;
    }
    
    /**
     * 
     * @param hm contains Key is ePath for the string which the link should persisted, Value as SO's SystemCode:LID from which we should collect the link to remove
     * @param eo the EnterpriseObject for which the links should be removed.
     * @return Modified Enterprise objects with removed links 
     * @throws com.sun.mdm.index.master.ProcessingException
     * @throws com.sun.mdm.index.master.UserException
     */
    public EnterpriseObject removeLinks(HashMap hm, EnterpriseObject eo) throws ProcessingException, UserException {
        SBR sbr = eo.getSBR();
        ArrayList overWrites = sbr.getOverWrites();
        Set keys = hm.keySet();
        if (overWrites != null) {
            for (Object obj : overWrites) {
                SBROverWrite overWrite = (SBROverWrite) obj;
                String str = overWrite.getEPath();
                if (str.indexOf("LINK:") != -1) {
                    str = str.substring(5);
                }
                if (keys.contains(str)) {
                    overWrite.setRemoveFlag(true);
                } else {
                    mLogger.debug(" LINK for " + str + " is Not available");
                }
            }
        } else {
            mLogger.debug("There exist no links to delete");
        }
        // EnterpriseObject eo1 = QwsController.getMasterController().updateSBR(hashMapew, eo, true);
        return eo;
    }

    /**
     * 
     * @param childrenHashmaps Arraylist of HashMaps, Each Hashmap should contain a perticuler Child's Links. This HashMap should have values for MINOR_OBJECT_ID, MINOR_OBJECT_TYPE
     * @param the EnterpriseObject for which the links should be removed.
     * @return Modified Enterprise objects with removed links 
     * @throws com.sun.mdm.index.master.ProcessingException
     * @throws com.sun.mdm.index.master.UserException
     */
    public EnterpriseObject removeLinksForChildren(ArrayList childrenHashmaps, EnterpriseObject eo) throws ProcessingException, UserException {
        SBR sbr = eo.getSBR();
        ArrayList overWrites = sbr.getOverWrites();
        for(Object o: childrenHashmaps){
            HashMap hm = (HashMap) o;
            String minorObjectId = (String) hm.get(MINOR_OBJECT_ID);
            Set keys = hm.keySet();
            if (overWrites != null) {
                for (Object obj : overWrites) {
                    SBROverWrite overWrite = (SBROverWrite) obj;
                    String str = overWrite.getEPath();
                    String objId = null;
                    if (str.indexOf("LINK:") != -1 && str.indexOf("@") != -1) {
                        str = str.substring(5);
                        objId = str.substring(str.indexOf("@")+1);
                        str = str.substring(0, str.indexOf("@"));                        
                    }
                    if (keys.contains(str) && objId.equals(hm.get(MINOR_OBJECT_ID))) {
                        overWrite.setRemoveFlag(true);
                    } else {
                        mLogger.debug(" LINK for " + str + " is Not available");
                    }
                }
            } else {
                mLogger.debug("There exist no links to delete");
            }
        }
        // EnterpriseObject eo1 = QwsController.getMasterController().updateSBR(hashMapew, eo, true);
        return eo;
    }
    
    /**
     * 
     * @param eo EnterpriseObject that has links which should require values.
     * @return hashmap Key as EPath String, Value as actual field value from SystemObject that linked. 
     * @throws com.sun.mdm.index.objects.exception.ObjectException
     * @throws com.sun.mdm.index.master.ConnectionInvalidException
     * @throws com.sun.mdm.index.ops.exception.OPSException
     * @throws com.sun.mdm.index.master.ProcessingException
     * @throws com.sun.mdm.index.objects.epath.EPathException
     * @throws com.sun.mdm.index.master.UserException
     */
    public HashMap getLinkValues(EnterpriseObject eo) throws ObjectException, ConnectionInvalidException, OPSException, ProcessingException, EPathException, UserException {
        SBR sbr = eo.getSBR();
        ArrayList overWrites = sbr.getOverWrites();
        HashMap hm = new HashMap();

        if (overWrites != null) {
            for (Object obj : overWrites) {
                SBROverWrite overWrite = (SBROverWrite) obj;
                String str = overWrite.getEPath();
                if (str.indexOf("LINK:") != -1 && str.indexOf("@")==-1) {
                    str = str.substring(5);
                    String value = getLinkedFieldValue((String) overWrite.getData(), str);
                    hm.put(str, value);
                }
            }
        } else {
             mLogger.debug("There exist no links.");
        }
        return hm;
    }
    
    /**
     * 
     * @param eo EnterpriseObject that has links which should require values.
     * @return hashmap Key as EPath String, Value as actual field value from SystemObject that linked. 
     * @throws com.sun.mdm.index.objects.exception.ObjectException
     * @throws com.sun.mdm.index.master.ConnectionInvalidException
     * @throws com.sun.mdm.index.ops.exception.OPSException
     * @throws com.sun.mdm.index.master.ProcessingException
     * @throws com.sun.mdm.index.objects.epath.EPathException
     * @throws com.sun.mdm.index.master.UserException
     */
    public HashMap getLinkedFields(EnterpriseObject eo) throws ObjectException, ConnectionInvalidException, OPSException, ProcessingException, EPathException, UserException {
        SBR sbr = eo.getSBR();
        ArrayList overWrites = sbr.getOverWrites();
        HashMap hm = new HashMap();

        if (overWrites != null) {
            for (Object obj : overWrites) {
                SBROverWrite overWrite = (SBROverWrite) obj;
                String str = overWrite.getEPath();
                if (str.indexOf("LINK:") != -1 && str.indexOf("@")==-1) {
                    str = str.substring(5);
                    String value = getLinkedFieldString((String) overWrite.getData(), str);
                    hm.put(str, value);
                }
            }
        } else {
             mLogger.debug("There exist no links.");
        }
        return hm;
    }
    /**
     * 
     * @param eo EnterpriseObject that has links which should require values.
     * @param minorObjectType MinorObject Type eg: Address for which the link values required.
     * @param minorObjectId MinorObjectId is Child Id using which we can get the value.
     * @return hashmap Key as EPath String, Value as actual field value from SystemObject that linked. 
     * @throws com.sun.mdm.index.objects.exception.ObjectException
     * @throws com.sun.mdm.index.master.ConnectionInvalidException
     * @throws com.sun.mdm.index.ops.exception.OPSException
     * @throws com.sun.mdm.index.master.ProcessingException
     * @throws com.sun.mdm.index.objects.epath.EPathException
     * @throws com.sun.mdm.index.master.UserException
     */
    public HashMap getLinkValuesForChildren(EnterpriseObject eo, String minorObjectType, String minorObjectId) throws ObjectException, ConnectionInvalidException, OPSException, ProcessingException, EPathException, UserException {
        SBR sbr = eo.getSBR();
        ArrayList overWrites = sbr.getOverWrites();
        HashMap hm = new HashMap();
        
        if (overWrites != null) {
            for (Object obj : overWrites) {
                SBROverWrite overWrite = (SBROverWrite) obj;
                String str = overWrite.getEPath();
                if (str.indexOf("LINK:") != -1 && str.indexOf("@")!=-1) {
                    str = str.substring(5);
                    String objectId = str.substring(str.indexOf("@")+1);
                    str = str.substring(0, str.indexOf("@"));
                    if (objectId.equals(minorObjectId))
                    {
                        String value = getLinkedFieldValue((String) overWrite.getData(), str, minorObjectType, minorObjectId);
                        hm.put(str, value);
                    }
                }
            }
        } else {
             mLogger.debug("There exist no links.");
        }
        return hm;
    }
    
    private String getLinkedFieldValue(String linkData, String path) throws ObjectException, EPathException, ProcessingException, UserException {
        String value = null;
        Object fieldValue = null;
        if (linkData != null && linkData.length() > 0) {
            if (linkData.charAt(0) == '[' && linkData.charAt(linkData.length() - 1) == ']') {
                String systemCode = linkData.substring(1, linkData.indexOf(":"));
                String lid = linkData.substring(linkData.indexOf(":") + 1, linkData.length() - 1);
                SystemObject systemObject = getSystemObject(systemCode, lid);
                fieldValue = EPathAPI.getFieldValue(path, systemObject.getObject());
            }
        }
        if (fieldValue != null) {
            value = fieldValue.toString();
        }
        return value;
    }

    private String getLinkedFieldString(String linkData, String path) throws ObjectException, EPathException, ProcessingException, UserException {
        String value = null;
        Object fieldValue = null;
        if (linkData != null && linkData.length() > 0) {
            if (linkData.charAt(0) == '[' && linkData.charAt(linkData.length() - 1) == ']') {
                String systemCode = linkData.substring(1, linkData.indexOf(":"));
                String lid = linkData.substring(linkData.indexOf(":") + 1, linkData.length() - 1);                
                fieldValue = systemCode + ":" + lid;
            }
        }
        if (fieldValue != null) {
            value = fieldValue.toString();
        }
        return value;
    }
    
    private String getLinkedFieldValue(String linkData, String path, String minorObjectType, String minorObjectId) throws ObjectException, EPathException, ProcessingException, UserException {
        String value = null;
        Object fieldValue = null;
        if (linkData != null && linkData.length() > 0) {
            if (linkData.charAt(0) == '[' && linkData.charAt(linkData.length() - 1) == ']') {
                String systemCode = linkData.substring(1, linkData.indexOf(":"));
                String lid = linkData.substring(linkData.indexOf(":") + 1, linkData.length() - 1);
                SystemObject systemObject = getSystemObject(systemCode, lid);
                ObjectNode child = systemObject.getObject().getChild(minorObjectType, minorObjectId);
                fieldValue = EPathAPI.getFieldValue(path, child);
            }
        }
        if (fieldValue != null) {
            value = fieldValue.toString();
        }
        return value;
    }
    
    public static void setObjectNodeFieldValue(ObjectNode node, String field,
            String valueString, SBR sbr) throws ObjectException, ValidationException {
        //Person.VIPFlag is converted to VIPFlag (Remove Person.) 
        //remove the Person, Address, Alias...etc from the field value 
        field = field.substring(field.indexOf(".") + 1, field.length());
        QwsUtil.setObjectNodeFieldValue(node, field, valueString, sbr);
    }

    public static void setObjectNodeFieldValue(ObjectNode node, String field,
            String valueString) throws ObjectException, ValidationException {
        setObjectNodeFieldValue(node, field, valueString, null);
    }

    public String deactivateEnterpriseObject(String euid) throws ProcessingException, UserException {
        String eoStatus = null;
        if (euid == null) {
            throw new ProcessingException("EUID can not be null");
        }
        QwsController.getMasterController().deactivateEnterpriseObject(euid);
        eoStatus = QwsController.getMasterController().getEnterpriseObject(euid).getStatus();
        return eoStatus;
    }

    public String activateEnterpriseObject(String euid) throws ProcessingException, UserException {
        String eoStatus = null;
        if (euid == null) {
            throw new ProcessingException("EUID can not be null");
        }
        QwsController.getMasterController().activateEnterpriseObject(euid);
        eoStatus = QwsController.getMasterController().getEnterpriseObject(euid).getStatus();
        return eoStatus;
    }
    // Added By Anil to get the Audit log functions with descriptions
    public HashMap getAuditLogFunctins(){
        
        HashMap audiLogFunctiontmap;
        PullDownListItem auditFunctions[] = ValidationService.getInstance().getValueItems(ValidationService.CONFIG_MODULE_AUDIT_FUNCTION);
        //System.out.println("---------items--" + auditFunctions.length);

        audiLogFunctiontmap = new HashMap();
        for (int items = 0; items < auditFunctions.length; items++) {            
            audiLogFunctiontmap.put(auditFunctions[items].getName(), auditFunctions[items].getDescription());
        }
        return audiLogFunctiontmap;
    }
    
      // Added By Anil to get the  functions with description
    public HashMap getFunctins(){
        
        HashMap functionMap;
        PullDownListItem auditFunctions[] = ValidationService.getInstance().getValueItems(ValidationService.CONFIG_MODULE_FUNCTION);
        //System.out.println("---------items--" + auditFunctions.length);

        functionMap = new HashMap();
        for (int items = 0; items < auditFunctions.length; items++) {
          functionMap.put(auditFunctions[items].getName(), auditFunctions[items].getDescription()); 
        }
        return functionMap;
    }

    public String getRootNodeName() {
        return rootNodeName;
    }

    public void setRootNodeName(String rootNodeName) {
        this.rootNodeName = rootNodeName;
    }
}
