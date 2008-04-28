/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.edm.util.QwsUtil;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.search.merge.MergeHistoryNode;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
//import com.sun.mdm.index.util.LogUtil;
//import com.sun.mdm.index.util.Logger;
import com.sun.tools.ws.wsdl.framework.ValidationException;

import com.sun.mdm.index.edm.presentation.util.Localizer;
//import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 *
 * @author sambag
 */
public class PatientDetailsUnmergeHandler {
    //private final Logger mLogger = LogUtil.getLogger(this);

    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.PatientDetailsUnmergeHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    private String euid = "0000005004";

    public void search() throws Exception {
        mLogger.fine("In PatientDetailsUnmergeHandler");
        try {
            MasterControllerService objMasterControllerService = new MasterControllerService();

            MergeHistoryNode mergeHistoryNode = objMasterControllerService.getMergeHistory(euid);
            mLogger.fine(" mergeHistoryNode.getEUID() : " +mergeHistoryNode.getEUID());
            mLogger.fine("MergeHistoryNode.getTransactionObject().getTransactionNumber():"+ mergeHistoryNode.getTransactionObject().getTransactionNumber());

            MergeResult mergeResult = objMasterControllerService.unmerge("");
            mLogger.fine("Source EO: ");//+mergeResult.getSourceEO()
            //  mLogger.debug("<<== Destination EO: "+mergeResult.getDestinationEO());

            EnterpriseObjectHistory enterpriseObjectHistory = objMasterControllerService.viewMergeRecords("");
            mLogger.fine(" enterpriseObjectHistory: " + enterpriseObjectHistory);
            EnterpriseObject beforeEO1 = enterpriseObjectHistory.getBeforeEO1();
            mLogger.fine(" eo: " + beforeEO1);
            EnterpriseObject beforeEO2 = enterpriseObjectHistory.getBeforeEO2();
            mLogger.fine(" eo: " + beforeEO2);
            EnterpriseObject afterEO = enterpriseObjectHistory.getAfterEO();
            mLogger.fine(" eo: " + afterEO);
            EnterpriseObject afterEO2 = enterpriseObjectHistory.getAfterEO2();
            mLogger.fine(" eo: " + afterEO2);
        } catch (Exception ex) {
            // UserException and ValidationException don't need a stack trace.
            // ProcessingException stack trace logged by MC
            if (ex instanceof ValidationException) {
                throw new ValidationException(mLocalizer.t("PDH501: Encountered a Validation error : {0}", QwsUtil.getRootCause(ex).getMessage()),ex);
            } else if (ex instanceof UserException) {
                throw new UserException(mLocalizer.t("PDH502: Encountered an UserException : {0}", QwsUtil.getRootCause(ex).getMessage()),ex);
            } else if (!(ex instanceof ProcessingException)) {
                throw new ProcessingException(mLocalizer.t("PDH503: Encountered the error:: {0}", QwsUtil.getRootCause(ex).getMessage()),ex);

            }
        }

    }
}
