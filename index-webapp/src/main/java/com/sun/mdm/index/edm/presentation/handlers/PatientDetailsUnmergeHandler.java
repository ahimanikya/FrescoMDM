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
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;
import com.sun.tools.ws.wsdl.framework.ValidationException;


/**
 *
 * @author sambag
 */
public class PatientDetailsUnmergeHandler {
    private final Logger mLogger = LogUtil.getLogger(this);

        private String euid="0000005004";
        public void search()throws Exception {
            mLogger.debug("In PatientDetailsUnmergeHandler...");
            try {
                    MasterControllerService objMasterControllerService = new MasterControllerService();
                    
                    MergeHistoryNode mergeHistoryNode = objMasterControllerService.getMergeHistory(euid);
                    mLogger.debug("<<== mergeHistoryNode.getEUID() ==>" + mergeHistoryNode.getEUID());
                    mLogger.debug("<<== mergeHistoryNode.getTransactionObject().getTransactionNumber()" + mergeHistoryNode.getTransactionObject().getTransactionNumber());
                    
                    MergeResult mergeResult = objMasterControllerService.unmerge("");
                    mLogger.debug("<<== Source EO: "+mergeResult.getSourceEO());
                    mLogger.debug("<<== Destination EO: "+mergeResult.getDestinationEO());
                    
                    EnterpriseObjectHistory enterpriseObjectHistory = objMasterControllerService.viewMergeRecords("");
                    mLogger.debug("<<== enterpriseObjectHistory: " + enterpriseObjectHistory);
                    EnterpriseObject beforeEO1 = enterpriseObjectHistory.getBeforeEO1();
                    mLogger.debug("<<== eo: " + beforeEO1);                    
                    EnterpriseObject beforeEO2 = enterpriseObjectHistory.getBeforeEO2();
                    mLogger.debug("<<== eo: " + beforeEO2);
                    EnterpriseObject afterEO = enterpriseObjectHistory.getAfterEO();
                    mLogger.debug("<<== eo: " + afterEO);
                    EnterpriseObject afterEO2 = enterpriseObjectHistory.getAfterEO2();
                    mLogger.debug("<<== eo: " + afterEO2);
                }
             catch (Exception ex) {
                // UserException and ValidationException don't need a stack trace.
                // ProcessingException stack trace logged by MC
                if (ex instanceof ValidationException) {
                    mLogger.info("Validation failed. Message displayed to the user: " 
                                  + QwsUtil.getRootCause(ex).getMessage());
                } else if (ex instanceof UserException) {
                    mLogger.info("UserException. Message displayed to the user: "
                                  + QwsUtil.getRootCause(ex).getMessage());
                } else if (!(ex instanceof ProcessingException)) {
                    mLogger.error("Error occurs: " + QwsUtil.getRootCause(ex).getMessage());
                    //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
                }
            }

        }
}
