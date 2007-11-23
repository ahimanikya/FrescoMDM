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
 
package com.sun.mdm.index.ejb.master;

import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import javax.ejb.SessionContext;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import com.sun.mdm.index.assumedmatch.AssumedMatchManager;
import com.sun.mdm.index.audit.AuditManager;
import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.configurator.impl.decision.DecisionMakerConfiguration;
import com.sun.mdm.index.configurator.impl.idgen.EuidGeneratorConfiguration;
import com.sun.mdm.index.configurator.impl.master.MasterControllerConfiguration;
import com.sun.mdm.index.configurator.impl.querybuilder.QueryBuilderConfiguration;
import com.sun.mdm.index.decision.DecisionMaker;
import com.sun.mdm.index.decision.DecisionMakerResult;
import com.sun.mdm.index.decision.DecisionMakerStruct;
import com.sun.mdm.index.ops.TransactionMgr;
import com.sun.mdm.index.ops.TransactionMgrFactory;
import com.sun.mdm.index.ejb.page.PageData;
import com.sun.mdm.index.query.AssembleDescriptor;
import com.sun.mdm.index.query.Condition;
import com.sun.mdm.index.query.EOSearchResultAssembler;
import com.sun.mdm.index.query.QMIterator;
import com.sun.mdm.index.query.QueryHelper;
import com.sun.mdm.index.query.QueryManager;
import com.sun.mdm.index.query.QueryManagerFactory;
import com.sun.mdm.index.query.QueryObject;
import com.sun.mdm.index.update.UpdateHelper;
import com.sun.mdm.index.update.UpdateResult;
import com.sun.mdm.index.update.UpdateManager;
import com.sun.mdm.index.update.UpdateManagerFactory;
import com.sun.mdm.index.master.MatchResult;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.master.MatchFieldChange;
import com.sun.mdm.index.master.ObjectNodeFilter;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.ConnectionInvalidException;
import com.sun.mdm.index.master.SystemDefinition;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchIterator;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSearchObject;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSummary;
import com.sun.mdm.index.master.search.audit.AuditDataObject;
import com.sun.mdm.index.master.search.audit.AuditIterator;
import com.sun.mdm.index.master.search.audit.AuditSearchObject;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.master.search.enterprise.EOGetOptions;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultIterator;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord;
import com.sun.mdm.index.master.search.merge.MergeHistoryHelper;
import com.sun.mdm.index.master.search.merge.MergeHistoryNode;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import com.sun.mdm.index.master.search.transaction.TransactionSearchObject;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.ExecuteMatchLogics;
import com.sun.mdm.index.matching.MatchEngineController;
import com.sun.mdm.index.matching.MatchEngineControllerFactory;
import com.sun.mdm.index.matching.MatchOptions;
import com.sun.mdm.index.matching.ScoreElement;
import com.sun.mdm.index.monitor.Agent;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.PotentialDuplicate;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SystemObjectException;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.metadata.MetaDataService;
import com.sun.mdm.index.objects.validation.ValidationRuleRegistry;
import com.sun.mdm.index.objects.validation.ObjectDescriptor;
import com.sun.mdm.index.objects.validation.ObjectValidator;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.ops.RecreateResult;
import com.sun.mdm.index.page.EuidPageAdapter;
import com.sun.mdm.index.page.IteratorPageAdapter;
import com.sun.mdm.index.page.PageAdapter;
import com.sun.mdm.index.page.TransactionPageAdapter;
import com.sun.mdm.index.persistence.TMResult;
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.outbound.OutBoundException;
import com.sun.mdm.index.potdup.PotentialDuplicateManager;
import com.sun.mdm.index.querybuilder.QueryBuilder;
import com.sun.mdm.index.survivor.SurvivorCalculator;
import com.sun.mdm.index.util.Constants;
import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.util.JNDINames;
import com.sun.mdm.index.util.ConnectionUtil;
import com.sun.mdm.index.outbound.OutBoundSender;
import com.sun.mdm.index.outbound.OutBoundMessages;
import java.util.Map;
import javax.transaction.UserTransaction;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * The MasterControllerCoreImpl is the implementation of MasterControllerCore
 */
public class MasterControllerCoreImpl implements MasterControllerCore {

    private final static String TOPIC_CONNECTION_FACTORY_PROP_KEY = "ConnectionFactory";
    private final static String TOPIC_PROP_KEY = "TopicName";
    private final static String TOPIC_PROP_FILE = "eviewoutbound.properties";

    private final static String MBEAN_PROP_FILE = "appmbean.properties";

    /**
     * /** Handle to assumed match manager
     */
    private AssumedMatchManager mAssumedMatchMgr;

    /**
     * Handle to audit manager
     */
    private AuditManager mAuditManager;

    /**
     * Handle to survivor calculator
     */
    private SurvivorCalculator mCalculator;

    /**
     * Handle to decision maker
     */
    private DecisionMaker mDecision;

    /**
     * Duplicate threshold as set in properties
     */
    private float mDuplicateThreshold;

    /** 
     * Assumed match threshold as set in properties
     */
    private float mAssumedMatchThreshold = Float.POSITIVE_INFINITY;
    
    /**
     * match engine controller implementation
     */
    private MatchEngineController mMatch;

    /**
     * Handle to merge history helper
     */
    private MergeHistoryHelper mMergeHistoryHelper;

    /**
     * Handle to object node filter
     */
    private ObjectNodeFilter mObjectNodeFilter;

    /**
     * Handle to potential duplicate manager
     */
    private PotentialDuplicateManager mPotDup;

    /**
     * Handle to query manager
     */
    private QueryManager mQuery;

    /**
     * Handle to query helper
     */
    private QueryHelper mQueryHelper;

    /**
     * Handle to search options
     */
    private EOSearchOptions mSearchOptions;

    /**
     * Handle to array of valid system codes
     */
    private String[] mSysCodeArray;

    /**
     * Handle to transaction manager
     */
    private TransactionMgr mTrans;

    /**
     * Handle to update manager
     */
    private UpdateManager mUpdate;

    /**
     * Handle to update manager helper
     */
    private UpdateHelper mUpdateHelper;

    /**
     * True if pessimistic mode enabled in properties
     */
    private boolean pessimisticModeEnabled;

    /**
     * JMS outbound message sender
     */
    private OutBoundSender mOutBoundSender;

    private MatchFieldChange mMatchFieldChange;

    private boolean mMergedRecordUpdateEnabled;

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient Localizer mLocalizer = Localizer.get();

    /**
     * Object path
     */
    private String mObjectPath;

    // MBean server related members
    private ObjectName mMBeanObjectName;

    private MBeanServer mMBeanServer;

    private ExecuteMatchLogics mUserLogics = null;

    private ExecuteMatchLogics mUserLogicsGui = null;

    /*
     * appox size of children for each primary object. This is used to limit
     * rows in alpha search
     */
    private int averageChildrenSize = 10;

    /**
     * Session context
     */
    private SessionContext context;

    /**
     * Flag to control whether it is in transactional mode or not
     */
    private boolean mIsTransactional = false;

    /**
     * type of transaction support
     */

	private String transactionType = "BMT_LOCAL";
	
	
	
	private String objectName="mural";

    /**
     * UserTransaction for bean-managed transaction
     */
    private UserTransaction utx;

    /**
     * No argument constructor required by container.
     */
    public MasterControllerCoreImpl() {
    }

    /**
     * initialize MasterControllerCoreImpl
     * 
     * @param context
     *            SessionContext
     * @exception java.lang.Exception
     *                An error has occured.
     */
    public void init(SessionContext context) throws Exception {
        try {
            this.context = context;

            if (transactionType.equals("BMT_XA"))
                this.utx = context.getUserTransaction();
            setMBeanServer();

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("MasterControllerImpl: Creating UpdateManager.");
            }

            mUpdate = UpdateManagerFactory.getInstance();

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("MasterControllerImpl: UpdateManager created.");
                mLogger.fine("MasterControllerImpl: Creating QueryManager.");
            }

            mQuery = QueryManagerFactory.getInstance();
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("MasterControllerImpl: QueryManager created.");
                mLogger.fine("MasterControllerImpl: Creating Match Engine Controller.");
            }

            mMatch = MatchEngineControllerFactory.getInstance();
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("MasterControllerImpl: Match Engine Controller created.");
                mLogger.fine("MasterControllerImpl: Creating Survivor Calculator.");
            }
            mCalculator = new com.sun.mdm.index.survivor.SurvivorCalculator(
                    mMatch);
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("MasterControllerImpl: Survivor Calculator created.");
                mLogger.fine("MasterControllerImpl: Creating Transaction Manager.");
            }
            mTrans = TransactionMgrFactory.getInstance();

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("MasterControllerImpl: Transaction Manager created.");
            }

            mObjectNodeFilter = new ObjectNodeFilter();
            mQueryHelper = new QueryHelper();
            mUpdateHelper = new UpdateHelper();
            mMergeHistoryHelper = new MergeHistoryHelper(mTrans);
            MasterController mc = (MasterController) context
                    .getBusinessObject(MasterControllerRemote.class);
            mPotDup = new PotentialDuplicateManager(mc);
            mAssumedMatchMgr = new AssumedMatchManager();
            mAuditManager = new AuditManager();
            mMatchFieldChange = new MatchFieldChange();

            // Determine update mode: pessimistic or optimistic
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("MasterControllerImpl: Reading Master Controller Configuration.");
            }
            MasterControllerConfiguration mcConfig = (MasterControllerConfiguration) ConfigurationService
                    .getInstance().getConfiguration(
                            MasterControllerConfiguration.MASTER_CONTROLLER);

            pessimisticModeEnabled = mcConfig.isPessimisticEnabled();
            mMergedRecordUpdateEnabled = mcConfig.isMergedRecordUpdateEnabled();
            mSearchOptions = mcConfig.getSearchOptions();
            mLogger.info(mLocalizer.x("MSC001: Execute match search options are: {0}", mSearchOptions.toString()));
            mLogger.info(mLocalizer.x("MSC002: Pessimistic mode set to: {0}", pessimisticModeEnabled));
            mLogger.info(mLocalizer.x("MSC003: Merge record update mode set to: {0}", mMergedRecordUpdateEnabled));

            String classString = mcConfig.getUserLogicClass();
            if (classString == null || classString.length() == 0) {
                classString = "com.sun.mdm.index.master.ExecuteMatchLogics";
                mUserLogics = (ExecuteMatchLogics) Class.forName(classString)
                        .newInstance();
            } else {
                mLogger.info(mLocalizer.x("MSC004: The Create User logic class is: {0}", classString));
                Object logicInstance = Class.forName(classString).newInstance();
                if (logicInstance instanceof com.sun.mdm.index.master.ExecuteMatchLogics) {
                    mUserLogics = (ExecuteMatchLogics) logicInstance;
                } else {
                    throw new Exception(mLocalizer.t("MSC500: UserLogic class " + 
                                        "must inherit from the ExecuteMatchLogics class."));
                }
            }
            mLogger.info(mLocalizer.x("MSC005: The Create User logic class is : {0}", classString));

            classString = mcConfig.getUserLogicClassGui();
            if (classString == null || classString.length() == 0) {
                classString = "com.sun.mdm.index.master.ExecuteMatchLogics";
                mUserLogicsGui = (ExecuteMatchLogics) Class
                        .forName(classString).newInstance();
            } else {
                Object logicInstance = Class.forName(classString).newInstance();
                if (logicInstance instanceof com.sun.mdm.index.master.ExecuteMatchLogics) {
                    mUserLogicsGui = (ExecuteMatchLogics) logicInstance;
                } else {
                    throw new Exception(mLocalizer.t("MSC501: UserLogicGUI class " + 
                                        "must inherit from the ExecuteMatchLogics class."));
                }
            }
            mLogger.info(mLocalizer.x("MSC006: MasterControllerImpl: Created user GUI logic class : {0}", classString));

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("MasterControllerImpl: Setting up outbound sender.");
            }
            mOutBoundSender = setupOutBoundSender();
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("MasterControllerImpl: Outbound sender setup complete.");
            }

            // Determine decision maker
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("MasterControllerImpl: Setting up decision maker.");
            }
            DecisionMakerConfiguration dmConfig = (DecisionMakerConfiguration) ConfigurationService
                    .getInstance().getConfiguration(
                            DecisionMakerConfiguration.DECISION_MAKER);
            mDecision = dmConfig.getDecisionMaker();
            mDecision.setMasterController(this);
            mDuplicateThreshold = mDecision.getDuplicateThreshold();
            mAssumedMatchThreshold = mDecision.getMatchThreshold();
            mLogger.info(mLocalizer.x("MSC008: Potential duplicate threshold is set to: {0}", mDuplicateThreshold));

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("MasterControllerImpl: Initialization complete.");
            }

        } catch (Exception e) {
            mLogger.severe(mLocalizer.x("MSC009: Initialization failed: {0}.", e.getMessage()));
            sendCriticalError("MasterController initializing failed: "
                    + e.getClass().getName() + ": " + e.getMessage());
            throw e;
        }
    }

    public void cleanUp() {
        if (mMBeanServer != null) {
            Agent a = new Agent(mMBeanServer, this);
            a.unregisterMBean(objectName);
        }
    }

    /**
     * Return EUID associated with a system object key or null if not found.
     * 
     * @param con
     *            Connection
     * @param key
     *            The system object key on which to perform the action
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid key (nulls or empty strings)
     * @return EUID for given key.
     */
    public String getEUID(Connection con, SystemObjectPK key)
            throws ProcessingException, UserException {
        String euid = null;

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("getEuid(): looking up system key: [" + key.lID + ", "
                    + key.systemCode + "]");
            }

            SystemObject sysObj = new SystemObject();
            sysObj.setLID(key.lID);
            sysObj.setSystemCode(key.systemCode);
            ValidationRuleRegistry reg = ValidationRuleRegistry.getInstance();
            ObjectValidator lidValidator = reg
                    .getCustomValidatorByObject("SystemObject");
            if (lidValidator != null) {
                try {
                    lidValidator.validate(sysObj);
                } catch (ValidationException v) {
                    String systemDesc = lookupSystemDefinition(con,
                            v.getSystemCode()).getDescription();
                    String format = v.getFormat();
                    String id = v.getId();
                    String sysCode = v.getSystemCode();
                    throw new ValidationException(mLocalizer.t("MSC502: getEUID() encountered a format validation error. " + 
                                    "The ID {0} does not conform to the format of SystemObject[LocalId] for "
                                    + "the system description {1}, which is {2}", id, systemDesc, format));
                }
            }
            euid = mQueryHelper.getEUID(con, key);
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("getEuid(): found euid: " + euid);
            }
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throwProcessingException(e);
        }
        return euid;
    }

    /**
     * Return EnterpiseObject associated with EUID or null if not found.
     * 
     * @param con
     *            Connection
     * @param euid
     *            The EUID on which to perform the action.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid euid (null or empty string)
     * @return EnterpriseObject for given EUID or null if not found.
     */
    public EnterpriseObject getEnterpriseObject(Connection con, String euid)
            throws ProcessingException, UserException {
        EnterpriseObject eo = null;

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("getEnterpriseObject(): looking up EUID: " + euid);
            }

            eo = mTrans.getEnterpriseObject(con, euid);

        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return eo;
    }

    /**
     * Return EnterpiseObject associated with EUID or null if not found.
     * 
     * @param con
     *            Connection
     * @param euid
     *            The EUID on which to perform the action.
     * @patam EOGetOptions specify list of epaths that define what kind of
     *        objects need to be retrieved to compose an EnterpriseObject.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid euid (null or empty string)
     * @return EnterpriseObject for given EUID or null if not found.
     */
    public EnterpriseObject getEnterpriseObject(Connection con, String euid,
            EOGetOptions options) throws ProcessingException, UserException {
        EnterpriseObject eo = null;

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("getEnterpriseObject(): looking up EUID: " + euid);
            }

            eo = mTrans.getEnterpriseObject(con, euid, options
                    .getFieldsToRetrieve());

        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return eo;
    }

    /**
     * Return EnterpriseObject associated with a system object key or null if
     * not found. Only active system objects can be used for the lookup.
     * 
     * @param con
     *            Connection
     * @param key
     *            The system object key on which to perform the action.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid key (null or empty string)
     * @return EnterpriseObject for given key.
     */
    public EnterpriseObject getEnterpriseObject(Connection con,
            SystemObjectPK key) throws ProcessingException, UserException {
        String euid = null;

        EnterpriseObject eo = null;

        try {
            // validate the local id
            SystemObject sysObj = new SystemObject();
            sysObj.setLID(key.lID);
            sysObj.setSystemCode(key.systemCode);
            ValidationRuleRegistry reg = ValidationRuleRegistry.getInstance();
            ObjectValidator lidValidator = reg
                    .getCustomValidatorByObject("SystemObject");
            if (lidValidator != null) {
                try {
                    lidValidator.validate(sysObj);
                } catch (ValidationException v) {
                    String systemDesc = lookupSystemDefinition(con,
                            v.getSystemCode()).getDescription();
                    String format = v.getFormat();
                    String id = v.getId();
                    String sysCode = v.getSystemCode();
                    throw new ValidationException(mLocalizer.t("MSC503: getEnterpriseObject() encountered a format validation " + 
                                    "error. The ID {0} does not conform to the format of SystemObject[LocalId] for "
                                    + "the system description {1}, which is {2}", id, systemDesc, format));
                }
            }

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("getEnterpriseObject(): looking up system key: "
                            + key);
            }

            euid = mQueryHelper.getEUID(con, key);
            if (euid == null) {
                return null;
            }
            eo = getEnterpriseObject(con, euid);
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throwProcessingException(e);
        }
        return eo;
    }

    /**
     * Return a tree like structure representing all of the merge transactions
     * that have taken place that are related to the given EUID.
     * 
     * @param con
     *            Connection
     * @param euid
     *            The EUID on which to perform the action.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid euid (null or empty string)
     * @return Merge history for given EUID.
     */
    public MergeHistoryNode getMergeHistory(Connection con, String euid)
            throws ProcessingException, UserException {
        MergeHistoryNode mergeHistoryNode = null;

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("getMergeHistory(): looking up EUID: " + euid);
            }

            mergeHistoryNode = mMergeHistoryHelper.getMergeHistory(con, euid);
        } catch (SQLException e) {
            throwProcessingException(e);
        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return mergeHistoryNode;
    }

    /**
     * Return SBR associated with an EUID or null if not found.
     * 
     * @param con
     *            Connection
     * @param euid
     *            The EUID on which to perform the action.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid euid (null or empty string)
     * @return SBR for given EUID.
     */
    public SBR getSBR(Connection con, String euid) throws ProcessingException,
            UserException {
        SBR sbr = null;

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("getSBR(): looking up EUID: " + euid);
            }

            sbr = mTrans.getSystemSBR(con, euid);

        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return sbr;
    }

    /**
     * Return SystemObject associated with a key or null if not found.
     * 
     * @param con
     *            Connection
     * @param key
     *            The system object key on which to perform the action.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid key (null or empty string)
     * @return SystemObject for given key or null if not found.
     */
    public SystemObject getSystemObject(Connection con, SystemObjectPK key)
            throws ProcessingException, UserException {
        SystemObject so = null;

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("getSystemObject(): looking up system key: " + key);
            }

            so = mTrans.getSystemObject(con, key.systemCode, key.lID);

        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return so;
    }

    /**
     * Return a deactivated system object back to active status.
     * 
     * @param con
     *            Connection
     * @param systemKey
     *            The system object key on which to perform the action.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid key (null or empty string)
     */
    public void activateSystemObject(Connection con, SystemObjectPK systemKey)
            throws ProcessingException, UserException {

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("activateSystemObject(): looking up system key: "
                    + systemKey);
            }

            String euid = mQueryHelper.getEUID(con, systemKey,
                    SystemObject.STATUS_INACTIVE);
            if (euid == null) {
                throw new ProcessingException(mLocalizer.t("MSC504: Inactive system object not found: {0}", 
                                                           systemKey.toString()));
            }
            EnterpriseObject eo = mTrans.getEnterpriseObject(con, euid);
            SystemObject so = eo.getSystemObject(systemKey.systemCode,
                    systemKey.lID);
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("activateSystemObject(): setting status of "
                    + systemKey + " to " + SystemObject.STATUS_ACTIVE);
            }
            so.setValue("Status", SystemObject.STATUS_ACTIVE);

            String user = getCallerUserId();

            Object[] beforeMatchFields = mMatchFieldChange.getMatchFields(eo);

            UpdateResult result = mUpdate.updateEnterprise(con, eo, 0, user);
            String transId = result.getTransactionResult().getTMID();

            EnterpriseObject eo2 = result.getEnterpriseObject1();

            SBR sbr = eo2.getSBR();

            if ((sbr.getStatus() != null)
                    && (sbr.getStatus().equals(SystemObject.STATUS_ACTIVE))) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("activateSystemObject(): execute pessimistic for EUID: "
                                + eo2.getEUID());
                }
                if (pessimisticModeEnabled) {
                    execPessimistic(con, euid, transId, beforeMatchFields, eo2,
                            null);
                }
            }

            mOutBoundSender.send(OutBoundMessages.REA, result
                    .getTransactionResult().getTMID(), eo);
        } catch (UserException e) {
            throw new UserException(mLocalizer.t("MSC505: Could not activate a SystemObject due to a " + 
                                                 "UserException: {0}", e));
        } catch (Exception e) {
            throwProcessingException(e);
        }
    }

    /**
     * Return a deactivated enterprise object back to active status.
     * 
     * @param con
     *            Connection
     * @param euid
     *            The euid on which to perform the action.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid euid (null or empty string)
     */
    public void activateEnterpriseObject(Connection con, String euid)
            throws ProcessingException, UserException {

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("activateEnterpriseObject(): activating EUID: "
                        + euid);
            }

            EnterpriseObject eo = mTrans.getEnterpriseObject(con, euid);
            if (eo == null) {
                throw new ProcessingException(mLocalizer.t("MSC506: Could not activate an EnterpriseObject due " + 
                                                 "to an invalid EUID: {0}", euid));
            }

            Object[] beforeMatchFields = mMatchFieldChange.getMatchFields(eo);

            SBR sbr = eo.getSBR();
            if (!sbr.getStatus().equals(SystemObject.STATUS_INACTIVE)) {
                throw new ProcessingException(mLocalizer.t("MSC507: Could not activate an " + 
                                        "EnterpriseObject with EUID {0} because it is currently " + 
                                        "not inactive", euid));
            }

            String user = getCallerUserId();

            UpdateResult result = mUpdate.activateEnterprise(con, eo, user);

            String transId = result.getTransactionResult().getTMID();
            EnterpriseObject afterEO = result.getEnterpriseObject1();

            SBR mergedSBR = afterEO.getSBR();
            findInsertDuplicates(con, afterEO.getEUID(), transId, mergedSBR,
                    null);

            mOutBoundSender.send(OutBoundMessages.REA, result
                    .getTransactionResult().getTMID(), eo);
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throwProcessingException(e);
        }
    }

    /**
     * Adds the SystemObject to the EnterpriseObject specified by EUID.
     * 
     * @param con
     *            Connection
     * @param euid
     *            The EUID on which to perform the action.
     * @param sysObj
     *            The system object to be added.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     */
    public void addSystemObject(Connection con, String euid, SystemObject sysObj)
            throws ProcessingException, UserException {

        try {
            SystemObjectPK systemKey = new SystemObjectPK(sysObj
                    .getSystemCode(), sysObj.getLID());
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("addSystemObject(): adding system key " + systemKey
                    + " to EUID: " + euid);
            }
            sysObj = mMatch.standardize(sysObj);

            validateSystemObject(con, sysObj);

            // Check to make sure the system object key is not already assigned

            String existingEuid = mQueryHelper.getEUID(con, systemKey);
            if (existingEuid != null) {
                throw new ProcessingException(mLocalizer.t("MSC508: Could not add a SystemObject. " + 
                                        "The System Key {0} is already mapped to EUID {1}", 
                                        systemKey.toString(), existingEuid));
            }
            EnterpriseObject beforeEO = mTrans.getEnterpriseObject(con, euid);
            if (beforeEO == null) {
                throw new ProcessingException(mLocalizer.t("MSC509: Could not add a SystemObject " + 
                                        "to a non-existent EUID {0}", euid));
            }

            String user = getCallerUserId();

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("addSystemObject(): adding: " + systemKey
                    + " to EUID: " + euid);
            }
            Object[] beforeMatchFields = mMatchFieldChange
                    .getMatchFields(beforeEO);
            int flag = Constants.FLAG_UM_REPLACE_SO;
            UpdateResult result = mUpdate.updateEnterprise(con, sysObj,
                    beforeEO, flag, user);

            if (pessimisticModeEnabled) {
                String transId = result.getTransactionResult().getTMID();
                EnterpriseObject afterEO = result.getEnterpriseObject1();
                execPessimistic(con, euid, transId, beforeMatchFields, afterEO,
                        null);
            }

            mOutBoundSender.send(OutBoundMessages.ADD, result
                    .getTransactionResult().getTMID(), result
                    .getEnterpriseObject1());
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throwProcessingException(e);
        }
    }

    /**
     * Calculates the new SBR given an enterprise object that has been modified.
     * 
     * @param eo
     *            The enterprise object to compute the SBR.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A validation error occured
     * @return The computed SBR for the enterprise object.
     */
    public SBR calculateSBR(EnterpriseObject eo) throws ProcessingException,
            UserException {
        SBR sbr = null;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("calculateSBR(): invoked for EUID: " + eo.getEUID());
            }
            mCalculator.determineSurvivor(eo);
            sbr = eo.getSBR();

        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return sbr;
    }

    /**
     * Adds a new enterprise object to the database using the given system
     * object.
     * 
     * @param con
     *            Connection
     * @param sysobj
     *            The system object to use as basis for new EO.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     * @return New EO.
     */
    public EnterpriseObject createEnterpriseObject(Connection con,
            SystemObject sysobj) throws ProcessingException, UserException {

        EnterpriseObject eo = null;
        try {
            SystemObjectPK systemKey = new SystemObjectPK(sysobj
                    .getSystemCode(), sysobj.getLID());
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("createEnterpriseObject(): creating EO for system key: "
                            + systemKey);
            }
            sysobj = mMatch.standardize(sysobj);
            validateSystemObject(con, sysobj);

            // Check to make sure the system object key is not already assigned
            String existingEuid = mQueryHelper.getEUID(con, systemKey);
            if (existingEuid != null) {
                throw new ProcessingException(mLocalizer.t("MSC510: Could not create " + 
                                        "an EnterpriseObject. The SystemKey {0} is " + 
                                        "already mapped to an existing EUID: {1}", 
                                        systemKey.toString(), existingEuid));
            }

            String user = sysobj.getUpdateUser();
            if (user == null || user.trim().length() == 0) {
                user = getCallerUserId();
                sysobj.setUpdateUser(user);
            }
            // sysobj.setCreateUser(user);

            UpdateResult result = mUpdate.createEnterpriseObject(con, sysobj);
            eo = result.getEnterpriseObject1();

            mOutBoundSender.send(OutBoundMessages.ADD, result
                    .getTransactionResult().getTMID(), eo);
        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
        return eo;
    }

    /**
     * Adds a new enterprise object to the database using the given array of
     * system objects.
     * 
     * @param con
     *            Connection
     * @param sysobj
     *            The system objects to use as basis for new EO.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     * @return New EO.
     */
    public EnterpriseObject createEnterpriseObject(Connection con,
            SystemObject[] sysobj) throws ProcessingException, UserException {

        EnterpriseObject eo = null;
        try {
            for (int i = 0; i < sysobj.length; i++) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("createEnterpriseObject(): processing system key ["
                                + sysobj[i].getSystemCode()
                                + ", "
                                + sysobj[i]
                                + "]");
                }
                sysobj[i] = mMatch.standardize(sysobj[i]);
            }

            for (int i = 0; i < sysobj.length; i++) {
                validateSystemObject(con, sysobj[i]);
            }

            for (int i = 0; i < sysobj.length; i++) {
                // Check to make sure the system object key is not already
                // assigned
                SystemObjectPK systemKey = new SystemObjectPK(sysobj[i]
                        .getSystemCode(), sysobj[i].getLID());
                String existingEuid = mQueryHelper.getEUID(con, systemKey);
                if (existingEuid != null) {
                    throw new ProcessingException(mLocalizer.t("MSC511 Could not create " + 
                                        "an EnterpriseObject.  The SystemKey {0} is " + 
                                        "already mapped to an existing EUID: {1}", 
                                        systemKey.toString(), existingEuid));
                }
            }
            for (int i = 0; i < sysobj.length; i++) {
                String user = sysobj[i].getUpdateUser();
                if (user == null || user.trim().length() == 0) {
                    user = getCallerUserId();
                    sysobj[i].setUpdateUser(user);
                }
                // sysobj[i].setCreateUser(user);
            }

            UpdateResult result = mUpdate.createEnterpriseObject(con, sysobj);
            eo = result.getEnterpriseObject1();

            mOutBoundSender.send(OutBoundMessages.ADD, result
                    .getTransactionResult().getTMID(), eo);
        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
        return eo;
    }

    /**
     * Deactivate a system object based on the given key. Note that this is
     * different than deleteSystemObject in that the record is not removed from
     * the database, only its status is changed.
     * 
     * @param con
     *            Connection
     * @param systemKey
     *            The system object key on which to perform the action.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid key (null or empty string)
     */
    public void deactivateSystemObject(Connection con, SystemObjectPK systemKey)
            throws ProcessingException, UserException {

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("deactivateSystemObject(): invoked for system key: "
                    + systemKey);
            }

            String euid = mQueryHelper.getEUID(con, systemKey,
                    SystemObject.STATUS_ACTIVE);
            if (euid == null) {
                throw new ProcessingException(mLocalizer.t("MSC512 Could not deactivate " + 
                                        "a SystemObject.  The SystemKey {0} is " + 
                                        "either not active or does not exist.",
                                        systemKey.toString()));
            }
            EnterpriseObject eo = mTrans.getEnterpriseObject(con, euid);

            // These two lines are added to get the beforeMatchFields to be used
            // in execPessimintic()
            EnterpriseObject beforeEO = mTrans.getEnterpriseObject(con, euid);
            Object[] beforeMatchFields = mMatchFieldChange
                    .getMatchFields(beforeEO);

            SystemObject so = eo.getSystemObject(systemKey.systemCode,
                    systemKey.lID);
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("deactivateSystemObject() setting status of key "
                    + systemKey + " to " + SystemObject.STATUS_INACTIVE);
            }
            so.setValue("Status", SystemObject.STATUS_INACTIVE);

            String user = getCallerUserId();

            UpdateResult result = mUpdate.updateEnterprise(con, eo, 0, user);

            EnterpriseObject afterEO = result.getEnterpriseObject1();

            if ((afterEO.getSBR().getStatus() != null)
                    && (!afterEO.getSBR().getStatus().equals(
                            SystemObject.STATUS_ACTIVE))) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("deactivateSystemObject(): removing potential duplicates for non-active EUID: "
                                + afterEO.getEUID());
                }
                mPotDup.deletePotentialDuplicates(con, afterEO.getEUID());
            } else if (pessimisticModeEnabled) {
                String transId = result.getTransactionResult().getTMID();
                execPessimistic(con, afterEO.getEUID(), transId,
                        beforeMatchFields, afterEO, null);
            }

            mOutBoundSender.send(OutBoundMessages.DEA, result
                    .getTransactionResult().getTMID(), eo);
        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
    }

    /**
     * Deactivate enterprise object based on key.
     * 
     * @param con
     *            Connection
     * @param euid
     *            The euid on which to perform the action.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid euid (null or empty string)
     */
    public void deactivateEnterpriseObject(Connection con, String euid)
            throws ProcessingException, UserException {

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("deactivateEnterpriseObject(): invoked for EUID: "
                    + euid);
            }

            EnterpriseObject eo = mTrans.getEnterpriseObject(con, euid);
            if (eo == null) {
                throw new ProcessingException(mLocalizer.t("MSC513 Could not deactivate " + 
                                        "an EnterpriseObject.  The EUID {0} is " + 
                                        "invalid.", euid));
            }

            // These two lines are added to get the beforeMatchFields to be used
            // in execPessimintic()
            EnterpriseObject beforeEO = mTrans.getEnterpriseObject(con, euid);
            Object[] beforeMatchFields = mMatchFieldChange
                    .getMatchFields(beforeEO);

            SBR sbr = eo.getSBR();
            if (!sbr.getStatus().equals(SystemObject.STATUS_ACTIVE)) {
                throw new ProcessingException(mLocalizer.t("MSC514 Could not deactivate " + 
                                        "an EnterpriseObject.  The EUID {0} is " + 
                                        "does not have active status.  The current " + 
                                        "status is: {1}", euid, sbr.getStatus()));
            }

            String user = getCallerUserId();

            UpdateResult result = mUpdate.deactivateEnterprise(con, eo, user);

            EnterpriseObject afterEO = result.getEnterpriseObject1();

            if ((afterEO.getSBR().getStatus() != null)
                    && (!afterEO.getSBR().getStatus().equals(
                            SystemObject.STATUS_ACTIVE))) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("deactivateEnterpriseObject(): removing potential duplicates for non-active EUID: "
                                + afterEO.getEUID());
                }
                mPotDup.deletePotentialDuplicates(con, afterEO.getEUID());
            }

            mOutBoundSender.send(OutBoundMessages.DEA, result
                    .getTransactionResult().getTMID(), eo);
        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
    }

    /**
     * Permanently delete a system object from an enterprise object. This allows
     * the system object key to be reused.
     * 
     * @param con
     *            Connection
     * @param systemKey
     *            The system object key on which to perform the action.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid key (null or empty string)
     */
    public void deleteSystemObject(Connection con, SystemObjectPK systemKey)
            throws ProcessingException, UserException {

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("deleteSystemObject(): invoked for system key "
                    + systemKey);
            }

            String euid = mQueryHelper.getEUID(con, systemKey);
            if (euid == null) {
                throw new ProcessingException(mLocalizer.t("MSC515 Could not delete " + 
                                        "a SystemObject.  The EUID could not " + 
                                        "be found for SystemKey {1}", systemKey.toString()));
            }
            EnterpriseObject beforeEO = mTrans.getEnterpriseObject(con, euid);
            Object[] beforeMatchFields = mMatchFieldChange
                    .getMatchFields(beforeEO);
            if (beforeEO == null) {
                throw new ProcessingException(mLocalizer.t("MSC516 Could not delete " + 
                                        "a SystemObject.  The EUID could not " + 
                                        "be loaded", euid));
            }

            String user = getCallerUserId();

            UpdateResult result = mUpdate.removeSystem(con, beforeEO,
                    systemKey.systemCode, systemKey.lID, user);

            EnterpriseObject afterEO = result.getEnterpriseObject1();

            if (afterEO != null) {
                if (afterEO.isRemoved()
                        || ((afterEO.getSBR().getStatus() != null) && (!afterEO
                                .getSBR().getStatus().equals(
                                        SystemObject.STATUS_ACTIVE)))) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("deleteSystemObject(): removing potential duplicates for non-active EUID: "
                                    + afterEO.getEUID());
                    }
                    mPotDup.deletePotentialDuplicates(con, afterEO.getEUID());
                } else {
                    if (pessimisticModeEnabled) {
                        String transId = result.getTransactionResult()
                                .getTMID();
                        execPessimistic(con, afterEO.getEUID(), transId,
                                beforeMatchFields, afterEO, null);
                    }
                }
            }

            mOutBoundSender.send(OutBoundMessages.DEA, result
                    .getTransactionResult().getTMID(), result
                    .getEnterpriseObject1());
        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
    }

    /**
     * If the SO record exists, the incoming record replaces the existing
     * record, including all the children.
     * 
     * @param con
     *            Connection
     * @param sysObj
     *            System object to process.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     * @return The result of the processing.
     */
    public MatchResult executeMatch(Connection con, SystemObject sysObj)
            throws ProcessingException, UserException {

        boolean replaceSO = true;
        return processMatch(con, sysObj, replaceSO, mUserLogics, new Boolean(
                pessimisticModeEnabled));
    }

    /**
     * If the SO record exists, the incoming record replaces the existing
     * record, including all the children. If performPessimistic is set to
     * false, the pessimistic mode processing is deferred.
     * 
     * @param con
     *            Connection
     * @param sysObj
     *            System object to process.
     * @param performPessimistic
     *            set to true to enable pessimistic mode processing, false
     *            otherwise
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     * @return The result of the processing.
     */
    public MatchResult executeMatchDupRecalc(Connection con,
            SystemObject sysObj, Boolean performPessimistic)
            throws ProcessingException, UserException {

        boolean replaceSO = true;
        return processMatch(con, sysObj, replaceSO, mUserLogics,
                performPessimistic);
    }

    /**
     * If the SO record exists, the incoming record replaces the existing
     * record, including all the children.
     * 
     * @param con
     *            Connection
     * @param sysObj
     *            System object to process.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     * @return The result of the processing.
     */
    public MatchResult executeMatchGui(Connection con, SystemObject sysObj)
            throws ProcessingException, UserException {

        boolean replaceSO = true;
        return processMatch(con, sysObj, replaceSO, mUserLogicsGui,
                new Boolean(pessimisticModeEnabled));
    }

    /**
     * If the SO record exists, updates existing fields and children. If the
     * field is null, it remains untouched.
     * 
     * @param con
     *            Connection
     * @param sysObj
     *            System object to process.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     * @return The result of the processing.
     */
    public MatchResult executeMatchUpdate(Connection con, SystemObject sysObj)
            throws ProcessingException, UserException {

        boolean replaceSO = false;
        return processMatch(con, sysObj, replaceSO, mUserLogicsGui,
                new Boolean(pessimisticModeEnabled));
    }

    /**
     * If the SO record exists, updates existing fields and children. If the
     * field is null, it remains untouched. If performPessimistic is set to
     * false, the pessimistic mode processing is deferred.
     * 
     * @param con
     *            Connection
     * @param sysObj
     *            System object to process.
     * @param performPessimistic
     *            set to true to enable pessimistic mode processing, false
     *            otherwise
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     * @return The result of the processing.
     */
    public MatchResult executeMatchUpdateDupRecalc(Connection con,
            SystemObject sysObj, Boolean performPessimistic)
            throws ProcessingException, UserException {

        boolean replaceSO = false;
        return processMatch(con, sysObj, replaceSO, mUserLogicsGui,
                performPessimistic);
    }

    /**
     * Processes the system object based on configuration options defined for
     * the master controller and associated components in the runtime xml file.
     * The options affect executeMatch in the following ways: a) Query Builder
     * class and options sets for blocking b) Matching rules, Pass Controller
     * and Block Picker classes c) Decision Maker class and options. If
     * performPessimistic is set to false, then pessimistic mode processing is
     * deferred.
     * 
     * @param con
     *            Connection
     * @param sysObj
     *            System object to process.
     * @param replaceSO
     *            Flag to indicate whether to replace the existing SO record
     * @param userLogics
     *            Execute Match Logics
     * @param performPessimistic
     *            set to true to enable pessimistc mode processing, false
     *            otherwise
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     * @return The result of the processing.
     */
    private MatchResult processMatch(Connection con, SystemObject sysObj,
            boolean replaceSO, ExecuteMatchLogics userLogics,
            Boolean performPessimistic) throws ProcessingException,
            UserException {

        UpdateResult result = null;
        String euid = null;

        int resultCode = -1;
        SBR outboundSBR = null;
        String outboundEvent = null;
        String transId = null;
        PotentialDuplicate potDups[] = null;
        DecisionMakerResult dmr = null;
        DecisionMakerStruct assumedMatch = null;

        boolean createNewEO = false;

        SystemObjectPK systemKey = new SystemObjectPK(sysObj.getSystemCode(),
                sysObj.getLID());
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("processMatch(): invoked for system key: " + systemKey);
        }
        try {
            sysObj = mMatch.standardize(sysObj);
        } catch (InstantiationException e) {
            throwProcessingException(e);
        }
        validateSystemObject(con, sysObj);
        String user = sysObj.getUpdateUser();
        if (user == null || user.trim().length() == 0) {
            user = getCallerUserId();
            sysObj.setUpdateUser(user);
        }

        boolean matchFieldChangedVal = false;
        try {

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("processMatch(): looking up system key " + systemKey);
            }
            euid = mQueryHelper.getEUID(con, systemKey);
            if (euid != null) {

                if (userLogics.disallowUpdate(sysObj)) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("processMatch(): Update disallowed: "
                                + euid);
                    }
                    resultCode = MatchResult.UPDATE_NOT_ALLOWED;
                } else {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("processMatch(): loading EUID " + euid);
                    }
                    EnterpriseObject beforeEO = mTrans.getEnterpriseObject(con,
                            euid);
                    // Check to see if system object found with merged status
                    String sysObjStatus = beforeEO.getSystemObject(
                            systemKey.systemCode, systemKey.lID).getStatus();
                    if (!mMergedRecordUpdateEnabled
                            && sysObjStatus.equals(SystemObject.STATUS_MERGED)) {
                        throw new ProcessingException(mLocalizer.t("MSC517: Merged record " + 
                                        "update parameter is false and system key has merged status " + 
                                        "for SystemKey {0}", systemKey.toString()));
                    }
                    if (sysObjStatus.equals(SystemObject.STATUS_INACTIVE)) {
                        throw new ProcessingException(mLocalizer.t("MSC518: system key {0} " + 
                                        "has inactive status." , systemKey.toString()));
                    }
                    if (beforeEO.getStatus().equals(
                            SystemObject.STATUS_INACTIVE)) {
                        throw new ProcessingException(mLocalizer.t("MSC519: EnterpriseObject {0} " + 
                                        "has inactive status." , euid));
                    }
                    if (userLogics.rejectUpdate(sysObj, beforeEO)) {
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("processMatch(): Update rejected: "
                                    + euid);
                        }
                        resultCode = MatchResult.UPDATE_REJECTED;
                    } else {
                        Object[] beforeMatchFields = mMatchFieldChange
                                .getMatchFields(beforeEO);
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("processMatch(): updating EUID " + euid);
                        }
                        int flag = replaceSO ? Constants.FLAG_UM_REPLACE_SO
                                : Constants.FLAG_UM_NONE;
                        result = mUpdate.updateEnterprise(con, sysObj,
                                beforeEO, flag, user);
                        if (result.getTransactionResult() != TMResult.NO_CHANGE_TRANSACTION) {
                            transId = result.getTransactionResult().getTMID();
                            EnterpriseObject afterEO = result
                                    .getEnterpriseObject1();
                            if (performPessimistic.booleanValue() == true) {
                                potDups = execPessimistic(con, euid, transId,
                                        beforeMatchFields, afterEO, null);
                            }
                            Object[] afterMatchFields = mMatchFieldChange
                                    .getMatchFields(afterEO);
                            matchFieldChangedVal = mMatchFieldChange
                                    .isMatchFieldChanged(beforeMatchFields,
                                            afterMatchFields);
                        }
                        outboundSBR = result.getEnterpriseObject1().getSBR();
                        outboundEvent = OutBoundMessages.UPD;
                        resultCode = MatchResult.SYS_ID_MATCH;
                    }
                }
            } else {
                if (userLogics.bypassMatching(sysObj)) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("processMatch(): Matching bypassed");
                    }
                    if (userLogics.disallowAdd(sysObj)) {
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("processMatch(): Add disallowed");
                        }
                        resultCode = MatchResult.ADD_NOT_ALLOWED;
                    } else {
                        // bypass matching, create new EO object
                        createNewEO = true;
                    }
                } else {
                    ArrayList list = null;
                    // proceed to matching process
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("processMatch(): system key " + systemKey
                            + " not found, invoking match engine");
                    }

                    // try to use the match engine to find a suitable record to
                    // link with
                    MatchOptions matchOpts = new MatchOptions();
                    matchOpts.setSortWeightResults(true);
                    matchOpts.setMinimumWeight(mDuplicateThreshold);
                    EOSearchCriteria crit = new EOSearchCriteria(sysObj);
                    /* Start filter changes */
                    //for filters take the copy of the sysOject
                    SystemObject originalObjToMatch = (SystemObject) sysObj.copy();
                    /* End filter changes */
                    list = mMatch.findMatch(con, crit, mSearchOptions,
                            matchOpts);
                    /* Start filter changes */
                    //assign the original value to systemobject 
                    sysObj = originalObjToMatch;
                    /* End filter changes */
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("processMatch(): match query completed");
                    }

                    if (list != null && list.size() > 0) {
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("processMatch(): records found, invoking decision maker");
                        }
                        dmr = mDecision.process(con, list, sysObj);
                        assumedMatch = dmr.getAssumedMatch();
                    } else {
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("processMatch(): no records found");
                        }
                    }

                    if (assumedMatch == null) {
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("processMatch(): no EO matched, creating new EO");
                        }
                        if (userLogics.disallowAdd(sysObj)) {
                            if (mLogger.isLoggable(Level.FINE)) {
                                mLogger.fine("processMatch(): Add disallowed");
                            }
                            resultCode = MatchResult.ADD_NOT_ALLOWED;
                        } else {
                            // no match is found, create new EO object
                            createNewEO = true;
                        }
                    } else {
                        euid = assumedMatch.euid;
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("processMatch(): assumed match, updating EUID "
                                        + euid);
                        }
                        EnterpriseObject beforeEO = mTrans.getEnterpriseObject(
                                con, euid);

                        if (userLogics.rejectAssumedMatch(sysObj, beforeEO)) {
                            if (mLogger.isLoggable(Level.FINE)) {
                                mLogger.fine("processMatch(): Assumed match rejected");
                            }
                            // new Enterprise object
                            if (userLogics.disallowAdd(sysObj)) {
                                if (mLogger.isLoggable(Level.FINE)) {
                                    mLogger.fine("processMatch(): Add disallowed");
                                }
                                resultCode = MatchResult.ADD_NOT_ALLOWED;
                            } else {
                                createNewEO = true;
                            }
                        } else {
                            // assumed match found, proform update
                            if (userLogics.disallowUpdate(sysObj)) {
                                if (mLogger.isLoggable(Level.FINE)) {
                                    mLogger.fine("processMatch(): Update disallowed");
                                }
                                resultCode = MatchResult.UPDATE_NOT_ALLOWED;
                            } else {
                                Object[] beforeMatchFields = mMatchFieldChange
                                        .getMatchFields(beforeEO);
                                int flag = replaceSO ? Constants.FLAG_UM_REPLACE_SO
                                        : Constants.FLAG_UM_NONE;
                                result = mUpdate.updateEnterprise(con, sysObj,
                                        beforeEO, flag, user);
                                EnterpriseObject afterEO = result
                                        .getEnterpriseObject1();
                                outboundSBR = afterEO.getSBR();
                                outboundEvent = OutBoundMessages.UPD;
                                if (result.getTransactionResult() != TMResult.NO_CHANGE_TRANSACTION) {
                                    transId = result.getTransactionResult()
                                            .getTMID();
                                    if (performPessimistic.booleanValue() == true) {
                                        potDups = execPessimistic(con, euid,
                                                transId, beforeMatchFields,
                                                afterEO, null);
                                    }
                                    mAssumedMatchMgr.addAssumedMatch(con,
                                            assumedMatch, sysObj
                                                    .getSystemCode(), sysObj
                                                    .getLID(), transId);
                                    afterEO = result.getEnterpriseObject1();
                                    Object[] afterMatchFields = mMatchFieldChange
                                            .getMatchFields(afterEO);
                                    matchFieldChangedVal = mMatchFieldChange
                                            .isMatchFieldChanged(
                                                    beforeMatchFields,
                                                    afterMatchFields);
                                }
                                resultCode = MatchResult.ASSUMED_MATCH;
                            }
                        }
                    }
                }
            }

            if (createNewEO) {
                sysObj.setUpdateUser(user);
                result = mUpdate.createEnterpriseObject(con, sysObj);

                EnterpriseObject entObj = result.getEnterpriseObject1();
                outboundSBR = entObj.getSBR();
                outboundEvent = OutBoundMessages.ADD;
                euid = entObj.getEUID();
                transId = result.getTransactionResult().getTMID();
                if (dmr != null) {
                    DecisionMakerStruct[] dmsArray = dmr
                            .getPotentialDuplicates();
                    if (dmsArray != null) {
                        potDups = constructPotentialDuplicates(euid, dmsArray);
                        mPotDup.addPotentialDuplicates(con, euid, potDups,
                                transId);
                    }
                }
                resultCode = MatchResult.NEW_EO;
            }
            if (result != null) {

                if (result.getTransactionResult() != TMResult.NO_CHANGE_TRANSACTION) {
                    mOutBoundSender.send(outboundEvent, result
                            .getTransactionResult().getTMID(), result
                            .getEnterpriseObject1());
                }
            }
        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
        Boolean matchFieldChanged = new Boolean(matchFieldChangedVal);
        return new MatchResult(euid, resultCode, transId, potDups,
                matchFieldChanged);
    }

    /**
     * Insert an audit log record.
     * 
     * @param con
     *            Connection
     * @param auditObject
     *            The audit log record to insert.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid audit object
     */
    public void insertAuditLog(Connection con, AuditDataObject auditObject)
            throws ProcessingException, UserException {

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("insertAuditLog(): record " + auditObject);
            }

            mAuditManager.insertAuditLog(con, auditObject);
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throwProcessingException(e);
        }
    }

    /**
     * Returns an iterator of assumed match summaries based on search object
     * criteria.
     * 
     * @param con
     *            Connection
     * @param o
     *            Search criteria.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid search object
     * @return Iterator of search results.
     */
    public AssumedMatchIterator lookupAssumedMatches(Connection con,
            AssumedMatchSearchObject o) throws ProcessingException,
            UserException {

        AssumedMatchIterator iterator = null;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("lookupAssumedMatches(): invoked with search object: \n" + o);
            }

            // validate the local id
            SystemObject sysObj = new SystemObject();
            String lid = o.getLID();
            sysObj.setLID(lid);
            sysObj.setSystemCode(o.getSystemCode());
            ValidationRuleRegistry reg = ValidationRuleRegistry.getInstance();
            ObjectValidator lidValidator = reg
                    .getCustomValidatorByObject("SystemObject");
            if (lidValidator != null && (lid != null && lid.length() != 0)) {
                try {
                    lidValidator.validate(sysObj);
                } catch (ValidationException v) {
                    String systemDesc = lookupSystemDefinition(con,
                            v.getSystemCode()).getDescription();
                    String format = v.getFormat();
                    String id = v.getId();
                    String sysCode = v.getSystemCode();
                    throw new ValidationException(mLocalizer.t("MSC520: lookupAssumedMatches() encountered a format validation " + 
                                    "error. The ID {0} does not conform to the format of SystemObject[LocalId] for "
                                    + "the system description {1}, which is {2}", id, systemDesc, format));
                }
            }

            // Check properties of search obj.
            int defaultPageSize = Constants.DEFAULT_PAGE_SIZE;
            int defaultMaxElements = Constants.DEFAULT_MAX_ELEMENTS;
            if (o.getPageSize() == 0) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("lookupAssumedMatches(): setting page size to default "
                                    + defaultPageSize);
                }
                o.setPageSize(defaultPageSize);
            }
            int maxElements = o.getMaxElements();
            // if (maxElements == 0 || maxElements > defaultMaxElements) {
            if (maxElements == 0) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("lookupAssumedMatches(): setting max elements to default "
                                    + defaultMaxElements);
                }
                o.setMaxElements(defaultMaxElements);
            }

            iterator = mAssumedMatchMgr.lookupAssumedMatches(con, o);

        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return iterator;
    }

    /** Counts the number of assumed match records matching the 
     * date criteria specified in search object.  This does not
     * handle searches based on EUID nor SystemCode/LID.
     *
     * @param obj Search criteria.
     * @throws ProcessingException An error has occured.
     * @throws UserException Invalid search object
     * @return count of the assumed match records matching the search criteria.
     */
    public int countAssumedMatches(AssumedMatchSearchObject amso)
        throws ProcessingException, UserException {
        Connection con = null;
        int count = 0;
        try {
            con = getConnection();
            count = mAssumedMatchMgr.countAssumedMatches(con, amso);
           
        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        } 
        return count;
    }
    
    /**
     * Undo an assumed match.
     * 
     * @param con
     *            Connection
     * @param assumedMatchId
     *            Id of assumed match to be resolved
     * @return EUID of new EO
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid id
     */
    public String undoAssumedMatch(Connection con, String assumedMatchId)
            throws ProcessingException, UserException {

        String newEuid = null;
        String transId = null;

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("undoAssumedMatch(): invoked for assumed match id: "
                    + assumedMatchId);
            }

            AssumedMatchSearchObject searchObj = new AssumedMatchSearchObject();
            searchObj.setAssumedMatchId(assumedMatchId);
            searchObj.setPageSize(1);
            searchObj.setMaxElements(1);
            AssumedMatchIterator i = mAssumedMatchMgr.lookupAssumedMatches(con,
                    searchObj);
            if (i.hasNext()) {
                AssumedMatchSummary assumedMatch = i.next();
                String sysCode = assumedMatch.getSystemCode();
                String lid = assumedMatch.getLID();
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("undoAssumedMatch(): system key to undo assumed match ["
                                + sysCode
                                + ", "
                                + lid
                                + "], EUID: "
                                + assumedMatch.getEUID());
                }
                EnterpriseObject eo = mTrans.getEnterpriseObject(con,
                        assumedMatch.getEUID());

                Object[] beforeMatchFields = mMatchFieldChange
                        .getMatchFields(eo);

                if (eo == null) {
                    throw new ProcessingException(mLocalizer.t("MSC521: Assumed Match record " +
                             "could not be undone.  Record has been modified by another user. " +
                             "EUID has already been merged: {0}", assumedMatch.getEUID()));
                }

                String user = getCallerUserId();

                UpdateResult result = mUpdate.splitSystem(con, sysCode, lid,
                        eo, user);
                transId = result.getTransactionResult().getTMID();

                newEuid = result.getEnterpriseObject2().getEUID();
                SBR newSbr = result.getEnterpriseObject2().getSBR();

                findInsertDuplicates(con, newEuid, transId, newSbr, null);

                EnterpriseObject origEO = result.getEnterpriseObject1();
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("undoAssumedMatch(): execute pessimistic for EUID: "
                                    + origEO.getEUID());
                }
                if (pessimisticModeEnabled) {
                    execPessimistic(con, origEO.getEUID(), transId,
                            beforeMatchFields, origEO, null);
                }

                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("undoAssumedMatch(): new EUID constructed: "
                        + newEuid);
                }

            } else {
                throw new ProcessingException(mLocalizer.t("MSC522: Assumed Match record " +
                             "could not be undone.  Record has been modified by another user. " +
                             "Assumed Match ID {0} has already been undone.", assumedMatchId));
            }

            // Delete the assumed match
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("undoAssumedMatch(): deleting assumed match id: "
                        + assumedMatchId);
            }
            mAssumedMatchMgr.deleteAssumedMatch(con, assumedMatchId);

        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
        return newEuid;
    }

    /**
     * Search for audit records.
     * 
     * @param con
     *            Connection
     * @param obj
     *            Search criteria.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid search object
     * @return Iterator of search results.
     */
    public AuditIterator lookupAuditLog(Connection con, AuditSearchObject obj)
            throws ProcessingException, UserException {

        AuditIterator iterator = null;

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("lookupAuditLog() invoked with search object:\n" + obj);
            }
            
            iterator = mAuditManager.lookupAuditLog(con, obj);
            return iterator;
        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return iterator;
    }

    /**
     * Returns an iterator of potential duplicate summaries based on search
     * object criteria.
     * 
     * @param con
     *            Connection
     * @param obj
     *            Search criteria.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid search object
     * @return Iterator of search results.
     */
    public PotentialDuplicateIterator lookupPotentialDuplicates(Connection con,
            PotentialDuplicateSearchObject obj) throws ProcessingException,
            UserException {

        PotentialDuplicateIterator iterator = null;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("lookupPotentialDuplicates(): invoked with search object:\n" + obj);
            }

            // validate the local id
            SystemObject sysObj = new SystemObject();
            String lid = obj.getLID();
            sysObj.setLID(lid);
            sysObj.setSystemCode(obj.getSystemCode());
            ValidationRuleRegistry reg = ValidationRuleRegistry.getInstance();
            ObjectValidator lidValidator = reg
                    .getCustomValidatorByObject("SystemObject");
            if (lidValidator != null && (lid != null && lid.length() != 0)) {
                try {
                    lidValidator.validate(sysObj);
                } catch (ValidationException v) {
                    String systemDesc = lookupSystemDefinition(con,
                            v.getSystemCode()).getDescription();
                    String format = v.getFormat();
                    String id = v.getId();
                    String sysCode = v.getSystemCode();
                    throw new ValidationException(mLocalizer.t("MSC523: lookupPotentialDuplicates() encountered a format validation " + 
                                    "error. The ID {0} does not conform to the format of SystemObject[LocalId] for "
                                    + "the system description {1}, which is {2}", id, systemDesc, format));
                }
            }

            // Check properties of search obj.
            int defaultPageSize = Constants.DEFAULT_PAGE_SIZE;
            int defaultMaxElements = Constants.DEFAULT_MAX_ELEMENTS;
            if (obj.getPageSize() == 0) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("lookupPotentialDuplicates(): setting page size to default "
                                    + defaultPageSize);
                }
                obj.setPageSize(defaultPageSize);
            }
            int maxElements = obj.getMaxElements();

            // if (maxElements == 0 || maxElements > defaultMaxElements) {
            if (maxElements == 0) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("lookupPotentialDuplicates(): setting max elements to default "
                                    + defaultMaxElements);
                }
                obj.setMaxElements(defaultMaxElements);
            }

            iterator = mPotDup.lookupPotentialDuplicates(con, obj);

        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return iterator;
    }

    /** Counts the number of potential duplicate records matching the 
     * criteria specified in search object.  This does not
     * handle searches based on EUID nor SystemCode/LID.
     *
     * @param obj Search criteria.
     * @throws ProcessingException An error has occured.
     * @throws UserException Invalid search object
     * @return count of the potential duplicate records matching the search criteria.
     */
    public int countPotentialDuplicates(PotentialDuplicateSearchObject pdso) 
            throws ProcessingException, UserException {
        Connection con = null;
        int count = 0;
        try {
            con = getConnection();
            count = mPotDup.countPotentialDuplicates(con, pdso);
        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return count;
    }
    
    /**
     * Return array of system definition
     * 
     * @param con
     *            Connection
     * @exception ProcessingException
     *                An error has occured.
     * @return Array of system codes.
     */
    public SystemDefinition[] lookupSystemDefinitions(Connection con)
            throws ProcessingException {

        SystemDefinition[] sd = null;
        try {
            sd = mQueryHelper.lookupSystemDefinitions(con);
            if (mLogger.isLoggable(Level.FINE)) {
                if (sd == null) {
                    mLogger.fine("lookupSystemDefinitions(): no systems defined");
                } else {
                    for (int i = 0; i < sd.length; i++) {
                        mLogger.fine("lookupSystemDefinitions(): " 
                                      + "retrieved definition: " + sd[i]);
                    }
                }
            }
        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return sd;
    }

    /**
     * Return a system definition for a given system code.
     * 
     * @param con
     *            Connection
     * @param systemCode
     *            The system code to look up.
     * @exception ProcessingException
     *                An error has occured.
     * @return System definition for a given system code.
     */
    public SystemDefinition lookupSystemDefinition(Connection con,
            String systemCode) throws ProcessingException {

        SystemDefinition sd = null;
        try {
            sd = mQueryHelper.lookupSystemDefinition(con, systemCode);
            if (mLogger.isLoggable(Level.FINE)) {
                if (sd == null) {
                    mLogger.fine("lookupSystemDefinition() for: " + systemCode
                            + ": no systems defined");
                }
            }
        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return sd;
    }

    /**
     * Returns an array of all system object keys belonging to the given EUID.
     * 
     * @param con
     *            Connection
     * @param euid
     *            The EUID on which to perform the action.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid euid
     * @return Array of system object keys.
     */
    public SystemObjectPK[] lookupSystemObjectPKs(Connection con, String euid)
            throws ProcessingException, UserException {
        SystemObjectPK[] retVal = null;

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("lookupSystemObjectPKs(): invoked with EUID: " + euid);
            }

            retVal = mQueryHelper.lookupSystemObjectKeys(con, euid);
            if (mLogger.isLoggable(Level.FINE)) {
                if (retVal != null) {
                    for (int i = 0; i < retVal.length; i++) {
                        mLogger.fine("lookupSystemObjectPKs(): retrieved System Object Key: " + retVal[i]);
                    }
                } else {
                    mLogger.fine("lookupSystemObjectPKs(): "
                                 + "no matching system object key for EUID: "
                                 + euid);
                }
            }
        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return retVal;
    }

    /**
     * Returns an array of system object keys with the given status belonging to
     * the given EUID.
     * 
     * @param con
     *            Connection
     * @param euid
     *            The EUID on which to perform the action.
     * @param status
     *            Status filter.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid euid or status
     * @return Array of system object keys or null.
     */
    public SystemObjectPK[] lookupSystemObjectPKs(Connection con, String euid,
            String status) throws ProcessingException, UserException {
        SystemObjectPK[] retVal = null;

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("lookupSystemObjectPKs(): invoked with EUID: " + euid
                        + ", status: " + status);
            }
            validateSystemObjectStatus(status);

            retVal = mQueryHelper.lookupSystemObjectKeys(con, euid, status);
            if (mLogger.isLoggable(Level.FINE)) {
                if (retVal != null) {
                    for (int i = 0; i < retVal.length; i++) {
                        mLogger.fine("lookupSystemObjectPKs(): retrieved System Object Key" + retVal[i]);
                    }
                } else {
                    mLogger.fine("lookupSystemObjectPKs(): "
                                 + "no matching system object key for EUID: " + euid
                                 + ", status: " + status);
                }
            }
        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return retVal;
    }

    /**
     * Returns an array of system object keys with the given status belonging to
     * the given destination system whose EUID matches the source system code /
     * lid.
     * 
     * @param con
     *            Connection
     * @param sourceSystem
     *            the source system
     * @param sourceLID
     *            the source local id
     * @param destSystem
     *            the destination system
     * @param status
     *            status of records in destination system to search for
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid parameters
     * @return Array of system object keys or null if not found
     */
    public SystemObjectPK[] lookupSystemObjectPKs(Connection con,
            String sourceSystem, String sourceLID, String destSystem,
            String status) throws ProcessingException, UserException {
        SystemObjectPK[] retVal = null;

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("lookupSystemObjectPKs(): invoked with source system code: "
                                + sourceSystem
                                + ", source LID: "
                                + sourceLID
                                + ", destination system code: "
                                + destSystem
                                + ", status: " + status);
            }
            validateSystemObjectStatus(status);

            retVal = mQueryHelper.lookupSystemObjectKeys(con, sourceSystem,
                    sourceLID, destSystem, status);
            if (mLogger.isLoggable(Level.FINE)) {
                if (retVal != null) {
                    for (int i = 0; i < retVal.length; i++) {
                        mLogger.fine("lookupSystemObjectPKs(): retrieved System Object Key: " + retVal[i]);
                    }
                }
            }
        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return retVal;
    }

    /**
     * Lookup active system objects only for the given EUID.
     * 
     * @param con
     *            Connection
     * @param euid
     *            The EUID on which to perform the action.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid euid
     * @return Array of system objects.
     */
    public SystemObject[] lookupSystemObjects(Connection con, String euid)
            throws ProcessingException, UserException {
        SystemObject[] sysObjArray = null;

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("lookupSystemObjects(): invoked with EUID: " + euid);
            }

            SystemObjectPK[] sysObjPKArray = mQueryHelper
                    .lookupSystemObjectKeys(con, euid);
            if (sysObjPKArray == null) {
                return null;
            }
            sysObjArray = new SystemObject[sysObjPKArray.length];

            for (int i = 0; i < sysObjArray.length; i++) {
                sysObjArray[i] = mTrans.getSystemObject(con,
                        sysObjPKArray[i].systemCode, sysObjPKArray[i].lID);
            }

        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return sysObjArray;
    }

    /**
     * Lookup system objects with the given EUID and status
     * 
     * @param con
     *            Connection
     * @param euid
     *            The EUID on which to perform the action.
     * @param status
     *            Status filter.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid parameters
     * @return Array of system objects.
     */
    public SystemObject[] lookupSystemObjects(Connection con, String euid,
            String status) throws ProcessingException, UserException {
        SystemObject[] sysObjArray = null;

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("lookupSystemObjects(): invoked with EUID: " + euid
                        + ", status: " + status);
            }
            validateSystemObjectStatus(status);

            SystemObjectPK[] sysObjPKArray = mQueryHelper
                    .lookupSystemObjectKeys(con, euid, status);
            if (sysObjPKArray == null) {
                return null;
            }

            sysObjArray = new SystemObject[sysObjPKArray.length];
            for (int i = 0; i < sysObjArray.length; i++) {
                sysObjArray[i] = mTrans.getSystemObject(con,
                        sysObjPKArray[i].systemCode, sysObjPKArray[i].lID);
            }
        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }

        return sysObjArray;
    }

    /**
     * Returns the transaction summary based on the given search transaction id.
     * 
     * @param con
     *            Connection
     * @param transId
     *            transaction id to lookup.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid id
     * @return Transaction summary.
     */
    public TransactionSummary lookupTransaction(Connection con, String transId)
            throws ProcessingException, UserException {

        TransactionSummary summary = null;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("lookupTransaction(): invoked with transaction id: "
                        + transId);
            }

            TransactionObject transObj = mTrans
                    .findTransactionLog(con, transId);
            if (transObj == null) {
                throw new ProcessingException(mLocalizer.t("MSC524: Could not retrieve " + 
                            "a transaction due to an invalid transaction ID: {0}", transId));
            }
            RecreateResult history = null;
            boolean validTransaction = true;
            try {
                history = mTrans.recreateObject(con, transId);
            } catch (Exception e) {
                // Create an empty RecreateResult object if an error was
                // encountered. This is dealt with in SearchResultForm.java
                // for the GUI. Otherwise, it must be handled by a
                // user-defined API.
                history = new RecreateResult();
                mLogger.warn(mLocalizer.x("MSC010: Invalid Transaction Log for this Transaction ID: {0}", 
                                           transId));
                validTransaction = false;
            }
            summary = new TransactionSummary(transObj);
            summary.setEnterpriseObjectHistory(new EnterpriseObjectHistory(
                    history));
            summary.setValidTransaction(validTransaction);

        } catch (ProcessingException e) {
            throwProcessingException(e);
        } catch (RuntimeException e) {
            throwProcessingException(e);
        }
        return summary;
    }

    /**
     * Returns an array of transaction summaries based on the given search
     * criteria.
     * 
     * @param con
     *            Connection
     * @param searchObj
     *            Search criteria.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid search object
     * @return Iterator of search results.
     */
    public TransactionIterator lookupTransactions(Connection con,
            TransactionSearchObject searchObj) throws ProcessingException,
            UserException {
        TransactionIterator retIterator = null;

        int defaultPageSize = Constants.DEFAULT_PAGE_SIZE;
        int defaultMaxElements = Constants.DEFAULT_MAX_ELEMENTS;

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("lookupTransactions(): invoked with search object: "
                             + searchObj);
            }
            // Get transactions from TM
            String orderBy = "timestamp asc, transactionnumber asc";
            TransactionObject[] arTrans = mTrans.findTransactionLogs(con,
                    searchObj.getTransactionObject(), searchObj.getStartDate(),
                    searchObj.getEndDate(), orderBy);

            if (arTrans != null) {

                if (searchObj.getPageSize() == 0) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("lookupTransactions(): setting page size to default "
                                        + defaultPageSize);
                    }
                    searchObj.setPageSize(defaultPageSize);
                }

                if (searchObj.getMaxElements() == 0) {
                    searchObj.setMaxElements(defaultMaxElements);
                }
                TransactionPageAdapter adapter = new TransactionPageAdapter(
                        arTrans, searchObj);

                int pageSize = searchObj.getPageSize();
                // disable PageData due to serializable bug 6198
                if (adapter.count() < 2 * pageSize) {
                    TransactionSummary resultRecord[] = new TransactionSummary[adapter
                            .count()];
                    int i = 0;
                    while (adapter.hasNext()) {
                        resultRecord[i++] = (TransactionSummary) adapter.next();
                    }
                    retIterator = new TransactionIterator(resultRecord);
                } else {
                    PageData pd = (PageData) context
                            .lookup(JNDINames.EJB_REF_PAGEDATA);
                    pd.setPageAdapter(adapter);
                    retIterator = new TransactionIterator(pd, pageSize,
                            searchObj.getMaxElements());
                }

            }
        } catch (Exception t) {
            throwProcessingException(t);
        }
        return retIterator;
    }

    /**
     * Merge the enterprise records based on the given EUID's.
     * 
     * @param con
     *            Connection
     * @param sourceEUID
     *            The EUID to be merged.
     * @param destinationEUID
     *            The EUID to be kept.
     * @param calculateOnly
     *            Indicate whether to commit changes to DB or just compute the
     *            MergeResult. See Constants.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid euids
     * @return Result of merge operation.
     */
    public MergeResult mergeEnterpriseObject(Connection con, String sourceEUID,
            String destinationEUID, boolean calculateOnly)
            throws ProcessingException, UserException {

        MergeResult mr = null;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("mergeEnterpriseObject(): invoked with source EUID: "
                        + sourceEUID + " destination EUID: " + destinationEUID
                        + "calculateOnly: " + calculateOnly);
            }

            EnterpriseObject destinationEO = mTrans.getEnterpriseObject(con,
                    destinationEUID);
            if (destinationEO == null) {
                throw new ProcessingException(mLocalizer.t("MSC525: Could not merge " + 
                            "EnterpriseObjects.  Record has been modified by another user. " +
                            "Destination EUID not found: {0}", destinationEUID));
            }
            mr = mergeEnterpriseObject(con, sourceEUID, destinationEO,
                    calculateOnly);
            // Commit already performed by mergeEnterpriseObject above

        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
        return mr;
    }

    /**
     * Merge the enterprise records based on the given EUID's.
     * 
     * @param con
     *            Connection
     * @param sourceEUID
     *            The EUID to be merged.
     * @param destinationEUID
     *            The EUID to be kept.
     * @param srcRevisionNumber
     *            The SBR revision number of the EUID to be merged.
     * @param destRevisionNumber
     *            The SBR revision number of the EUID to be kept.
     * @param calculateOnly
     *            Indicate whether to commit changes to DB or just compute the
     *            MergeResult. See Constants.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid euids
     * @return Result of merge operation.
     */
    public MergeResult mergeEnterpriseObject(Connection con, String sourceEUID,
            String destinationEUID, String srcRevisionNumber,
            String destRevisionNumber, boolean calculateOnly)
            throws ProcessingException, UserException {

        MergeResult mr = null;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("mergeEnterpriseObject(): invoked with source EUID: "
                        + sourceEUID + " destination EUID: " + destinationEUID
                        + "calculateOnly: " + calculateOnly);
            }
            EnterpriseObject destinationEO = mTrans.getEnterpriseObject(con,
                    destinationEUID);
            if (destinationEO == null) {
                throw new ProcessingException(mLocalizer.t("MSC526: Could not merge " + 
                            "EnterpriseObjects.  Record has been modified by another user. " +
                            "Destination EUID not found: {0}", destinationEUID));
            }
            mr = mergeEnterpriseObject(con, sourceEUID, destinationEO,
                    srcRevisionNumber, destRevisionNumber, calculateOnly);
            // Commit already performed by mergeEnterpriseObject above

        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
        return mr;
    }

    /**
     * Merge the enterprise records based on the given source EUID and the
     * destination EO.
     * 
     * @param con
     *            Connection
     * @param sourceEUID
     *            The EUID to be merged.
     * @param destinationEO
     *            The EUID to be kept.
     * @param calculateOnly
     *            Indicate whether to commit changes to DB or just compute the
     *            MergeResult. See Constants.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     * @return Result of merge operation.
     */
    public MergeResult mergeEnterpriseObject(Connection con, String sourceEUID,
            EnterpriseObject destinationEO, boolean calculateOnly)
            throws ProcessingException, UserException {

        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("mergeEnterpriseObject(): invoked with source EUID: "
                    + sourceEUID + " destination EUID: " + destinationEO.getEUID()
                    + "calculateOnly: " + calculateOnly
                    + " and updated destination image");
        }
        validateEnterpriseObject(con, destinationEO);
        MergeResult mergeResult = new MergeResult();
        try {

            // Get original values if pessimistic mode requires it
            Object[] destMatchFields = null;
            if (!calculateOnly && pessimisticModeEnabled) {
                EnterpriseObject origDestinationEO = mTrans
                        .getEnterpriseObject(con, destinationEO.getEUID());
                destMatchFields = mMatchFieldChange
                        .getMatchFields(origDestinationEO);
            }

            EnterpriseObject sourceEO = mTrans.getEnterpriseObject(con,
                    sourceEUID);
            if (sourceEO == null) {
                throw new ProcessingException(mLocalizer.t("MSC527: Could not merge " + 
                            "EnterpriseObjects.  Record has been modified by another user. " +
                            "Source EUID not found: {0}", sourceEUID));
            }

            int flag = Constants.FLAG_UM_NONE;
            if (calculateOnly) {
                flag = Constants.FLAG_UM_CALC_ONLY;
            }

            String user = getCallerUserId();

            UpdateResult result = mUpdate.mergeEnterprise(con, sourceEO,
                    destinationEO, flag, user);

            mergeResult.setDestinationEO(result.getEnterpriseObject1());
            mergeResult.setSourceEO(result.getEnterpriseObject2());

            if (!calculateOnly) {
                String transId = result.getTransactionResult().getTMID();
                TransactionObject transObj = mTrans.findTransactionLog(con,
                        transId);
                mergeResult.setTransactionObject(transObj);

                // Delete potential duplicates for 'from' record
                mPotDup.deletePotentialDuplicates(con, sourceEUID);

                // Reevaluate duplicates for 'to' record if pessimistic mode on
                if (pessimisticModeEnabled) {
                    EnterpriseObject afterEO = result.getEnterpriseObject1();
                    execPessimistic(con, destinationEO.getEUID(), transId,
                            destMatchFields, afterEO, sourceEUID);
                }

                mOutBoundSender.send(OutBoundMessages.MRG, result
                        .getTransactionResult().getTMID(), mergeResult);
            }

        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
        return mergeResult;
    }

    /**
     * Merge the enterprise records based on the given source EUID and the
     * destination EO.
     * 
     * @param con
     *            Connection
     * @param sourceEUID
     *            The EUID to be merged.
     * @param destinationEO
     *            The EUID to be kept.
     * @param srcRevisionNumber
     *            The SBR revision number of the EUID to be merged.
     * @param destRevisionNumber
     *            The SBR revision number of the EUID to be kept.
     * @param calculateOnly
     *            Indicate whether to commit changes to DB or just compute the
     *            MergeResult. See Constants.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     * @return Result of merge operation.
     */
    public MergeResult mergeEnterpriseObject(Connection con, String sourceEUID,
            EnterpriseObject destinationEO, String srcRevisionNumber,
            String destRevisionNumber, boolean calculateOnly)
            throws ProcessingException, UserException {

        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("mergeEnterpriseObject(): invoked with source EUID: "
                    + sourceEUID + " destination EUID: " + destinationEO.getEUID()
                    + "calculateOnly: " + calculateOnly
                    + " and updated destination image (set log to DEBUG to view)");
        }
        validateEnterpriseObject(con, destinationEO);
        MergeResult mergeResult = new MergeResult();
        try {

            // Get original values if pessimistic mode requires it
            Object[] destMatchFields = null;
            if (!calculateOnly && pessimisticModeEnabled) {
                EnterpriseObject origDestinationEO = mTrans
                        .getEnterpriseObject(con, destinationEO.getEUID());
                destMatchFields = mMatchFieldChange
                        .getMatchFields(origDestinationEO);
            }

            EnterpriseObject sourceEO = mTrans.getEnterpriseObject(con,
                    sourceEUID);
            if (sourceEO == null) {
                throw new ProcessingException(mLocalizer.t("MSC528: Could not merge " + 
                            "EnterpriseObjects.  Record has been modified by another user. " +
                            "Source EUID not found: {0}", sourceEUID));
            }

            int flag = Constants.FLAG_UM_NONE;
            if (calculateOnly) {
                flag = Constants.FLAG_UM_CALC_ONLY;
            }

            String user = getCallerUserId();

            UpdateResult result = mUpdate.mergeEnterprise(con, sourceEO,
                    destinationEO, srcRevisionNumber, destRevisionNumber, flag,
                    user);

            mergeResult.setDestinationEO(result.getEnterpriseObject1());
            mergeResult.setSourceEO(result.getEnterpriseObject2());

            if (!calculateOnly) {
                String transId = result.getTransactionResult().getTMID();
                TransactionObject transObj = mTrans.findTransactionLog(con,
                        transId);
                mergeResult.setTransactionObject(transObj);

                // Delete potential duplicates for 'from' record
                mPotDup.deletePotentialDuplicates(con, sourceEUID);

                // Reevaluate duplicates for 'to' record if pessimistic mode on
                if (pessimisticModeEnabled) {
                    EnterpriseObject afterEO = result.getEnterpriseObject1();
                    execPessimistic(con, destinationEO.getEUID(), transId,
                            destMatchFields, afterEO, sourceEUID);
                }

                mOutBoundSender.send(OutBoundMessages.MRG, result
                        .getTransactionResult().getTMID(), mergeResult);
            }

        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
        return mergeResult;
    }

    /**
     * Merge the two lids for the given system. Note that the keys may both
     * belong to a single EO, or may belong to two different EO's.
     * 
     * @param con
     *            Connection
     * @param systemCode
     *            The system to which these local id's belong.
     * @param sourceLID
     *            The lid to be merged.
     * @param destLID
     *            The lid to be kept.
     * @param calculateOnly
     *            Indicate whether to commit changes to DB or just compute the
     *            MergeResult. See Constants.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid parameters
     * @return Result of merge operation.
     */
    public MergeResult mergeSystemObject(Connection con, String systemCode,
            String sourceLID, String destLID, boolean calculateOnly)
            throws ProcessingException, UserException {

        SystemObjectPK sourceSystemKey = new SystemObjectPK(systemCode,
                sourceLID);
        SystemObjectPK destSystemKey = new SystemObjectPK(systemCode, destLID);
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("mergeSystemObject(): invoked with source system key: "
                    + sourceSystemKey + " and destination system key: "
                    + destSystemKey);
        }
        MergeResult mr = null;
        try {

            mr = mergeSystemObject(con, sourceSystemKey, destSystemKey, null,
                    null, null, calculateOnly, pessimisticModeEnabled);

        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
        return mr;
    }

    /**
     * Merge the two lids for the given system. Note that the keys may both
     * belong to a single EO, or may belong to two different EO's. The revision
     * numbers of both the source and destination SBRs are passed as arguments.
     * At a later stage, these are compared to the SBRs stored in the database.
     * If they differ, it means that either the source or destination records
     * were modified by another user. In this case, the merge should not be
     * allowed.
     * 
     * @param con
     *            Connection
     * @param systemCode
     *            The system to which these local id's belong.
     * @param sourceLID
     *            The lid to be merged.
     * @param destLID
     *            The lid to be kept.
     * @param srcRevisionNumber
     *            The revision number of the source record's SBR
     * @param destRevisionNumber
     *            The revision number of the destination record's SBR
     * @param calculateOnly
     *            Indicate whether to commit changes to DB or just compute the
     *            MergeResult. See Constants.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid parameters
     * @return Result of merge operation.
     */
    public MergeResult mergeSystemObject(Connection con, String systemCode,
            String sourceLID, String destLID, String srcRevisionNumber,
            String destRevisionNumber, boolean calculateOnly)
            throws ProcessingException, UserException {

        if (srcRevisionNumber == null) {
            throw new UserException(mLocalizer.t("MSC529: Could not merge " + 
                            "SystemObjects.  srcRevisionNumber cannot be null."));
        }
        if (destRevisionNumber == null) {
            throw new UserException(mLocalizer.t("MSC530: Could not merge " + 
                            "SystemObjects.  destRevisionNumber cannot be null."));
        }

        SystemObjectPK sourceSystemKey = new SystemObjectPK(systemCode,
                sourceLID);
        SystemObjectPK destSystemKey = new SystemObjectPK(systemCode, destLID);
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("mergeSystemObject(): invoked with source system key: "
                    + sourceSystemKey + ", source revision number: "
                    + srcRevisionNumber + ", destination system key: "
                    + destSystemKey + ", destination revision number: "
                    + destRevisionNumber);
        }
        MergeResult mr = null;
        try {

            mr = mergeSystemObject(con, sourceSystemKey, destSystemKey, null,
                    srcRevisionNumber, destRevisionNumber, calculateOnly,
                    pessimisticModeEnabled);

        } catch (UserException e) {
            throw new UserException(mLocalizer.t("MSC560: Could not merge " + 
                            "SystemObjects: {0}", e));
        } catch (Exception e) {
            throwProcessingException(e);
        }
        return mr;
    }

    /**
     * Merge the given local id into the destination system object. This method
     * allows the new image of the merged system object to be defined in case
     * the sending system includes this information.
     * 
     * @param con
     *            Connection
     * @param systemCode
     *            The system code to which the local id's belong
     * @param sourceLID
     *            The local id to be merged.
     * @param destLID
     *            The local id to be kept.
     * @param destImage
     *            The new object image for the destination system object. For
     *            example, if the SystemObject contains a PersonObject, then a
     *            PersonObject should be passed in to this field.
     * @param calculateOnly
     *            Indicate whether to commit changes to DB or just compute the
     *            MergeResult. See Constants.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     * @return Result of merge operation.
     */
    public MergeResult mergeSystemObject(Connection con, String systemCode,
            String sourceLID, String destLID, ObjectNode destImage,
            boolean calculateOnly) throws ProcessingException, UserException {

        SystemObjectPK sourceSystemKey = new SystemObjectPK(systemCode,
                sourceLID);
        SystemObjectPK destSystemKey = new SystemObjectPK(systemCode, destLID);
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("mergeSystemObject(): invoked with source system key: "
                    + sourceSystemKey + ", destination system key: "
                    + destSystemKey
                    + " and updated destination image (set log to DEBUG to view)");
        }

        // validateObjectNode(destImage);

        MergeResult mr = null;
        try {
            SystemObject destSO = new SystemObject();
            destSO.setLID(destLID);
            destSO.setSystemCode(systemCode);
            destSO.setChildType(destImage.pGetTag());
            destSO.setObject(destImage);

            mMatch.standardize(destSO);
            validateObjectNode(destImage);

            mr = mergeSystemObject(con, sourceSystemKey, destSystemKey, destSO,
                    null, null, calculateOnly, pessimisticModeEnabled);

        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
        return mr;
    }

    /**
     * Merge the given local id into the destination system object. This method
     * allows the new image of the merged system object to be defined in case
     * the sending system includes this information. The revision numbers of
     * both the source and destination SBRs are passed as arguments. At a later
     * stage, these are compared to the SBRs stored in the database. If they
     * differ, it means that either the source or destination records were
     * modified by another user. In this case, the merge should not be allowed.
     * 
     * @param con
     *            Connection
     * @param systemCode
     *            The system code to which the local id's belong
     * @param sourceLID
     *            The local id to be merged.
     * @param destLID
     *            The local id to be kept.
     * @param destImage
     *            The new object image for the destination system object. For
     *            example, if the SystemObject contains a PersonObject, then a
     *            PersonObject should be passed in to this field.
     * @param srcRevisionNumber
     *            The revision number of the source record's SBR
     * @param destRevisionNumber
     *            The revision number of the destination record's SBR
     * @param calculateOnly
     *            Indicate whether to commit changes to DB or just compute the
     *            MergeResult. See Constants.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     * @return Result of merge operation.
     */
    public MergeResult mergeSystemObject(Connection con, String systemCode,
            String sourceLID, String destLID, ObjectNode destImage,
            String srcRevisionNumber, String destRevisionNumber,
            boolean calculateOnly) throws ProcessingException, UserException {

        if (srcRevisionNumber == null) {
            throw new UserException(mLocalizer.t("MSC531: Could not merge " + 
                            "SystemObjects.  srcRevisionNumber cannot be null."));
        }
        if (destRevisionNumber == null) {
            throw new UserException(mLocalizer.t("MSC532: Could not merge " + 
                            "SystemObjects.  destRevisionNumber cannot be null."));
        }

        SystemObjectPK sourceSystemKey = new SystemObjectPK(systemCode,
                sourceLID);
        SystemObjectPK destSystemKey = new SystemObjectPK(systemCode, destLID);
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("mergeSystemObject(): invoked with source system key:"
                    + sourceSystemKey + ", source revision number: "
                    + srcRevisionNumber + ", destination system key: "
                    + destSystemKey + " destination revision number: "
                    + destRevisionNumber
                    + " and updated destination image (set log to DEBUG to view)");
        }

        MergeResult mr = null;
        try {
            SystemObject destSO = new SystemObject();
            destSO.setLID(destLID);
            destSO.setSystemCode(systemCode);
            destSO.setChildType(destImage.pGetTag());
            destSO.setObject(destImage);

            mMatch.standardize(destSO);
            validateObjectNode(destImage);

            mr = mergeSystemObject(con, sourceSystemKey, destSystemKey, destSO,
                    srcRevisionNumber, destRevisionNumber, calculateOnly,
                    pessimisticModeEnabled);

        } catch (UserException e) {
            throw new UserException(mLocalizer.t("MSC533: Could not merge " + 
                            "SystemObjects: {0}", e));
        } catch (Exception e) {

            throwProcessingException(e);
        }
        return mr;
    }

    /**
     * Merge the given local id into the destination system object. This method
     * allows the new image of the merged system object to be defined in case
     * the sending system includes this information. The revision numbers of
     * both the source and destination SBRs are passed as arguments. At a later
     * stage, these are compared to the SBRs stored in the database. If they
     * differ, it means that either the source or destination records were
     * modified by another user. In this case, the merge should not be allowed.
     * 
     * @param con
     *            Connection
     * @param systemCode
     *            The system code to which the local id's belong
     * @param sourceLID
     *            The local id to be merged.
     * @param destLID
     *            The local id to be kept.
     * @param destImage
     *            The new object image for the destination system object. For
     *            example, if the SystemObject contains a PersonObject, then a
     *            PersonObject should be passed in to this field.
     * @param srcRevisionNumber
     *            The revision number of the source record's SBR
     * @param destRevisionNumber
     *            The revision number of the destination record's SBR
     * @param calculateOnly
     *            Indicate whether to commit changes to DB or just
     * @param performPessimistic
     *            A Boolean indicator of whether to defer potential duplicate
     *            processing. Specify <b>true</b> to recalculate potential
     *            duplicates on update; specify <b>false</b> to defer
     *            recalculation to a later time. compute the MergeResult. See
     *            Constants.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     * @return Result of merge operation.
     */
    public MergeResult mergeSystemObject(Connection con, String systemCode,
            String sourceLID, String destLID, ObjectNode destImage,
            String srcRevisionNumber, String destRevisionNumber,
            boolean calculateOnly, Boolean performPessimistic)
            throws ProcessingException, UserException {

        if (srcRevisionNumber == null) {
            throw new UserException(mLocalizer.t("MSC534: Could not merge " + 
                            "SystemObjects.  srcRevisionNumber cannot be null."));
        }
        if (destRevisionNumber == null) {
            throw new UserException(mLocalizer.t("MSC535: Could not merge " + 
                            "SystemObjects.  destRevisionNumber cannot be null."));
        }

        SystemObjectPK sourceSystemKey = new SystemObjectPK(systemCode,
                sourceLID);
        SystemObjectPK destSystemKey = new SystemObjectPK(systemCode, destLID);
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("mergeSystemObject(): invoked with source system key:"
                    + sourceSystemKey + ", source revision number: "
                    + srcRevisionNumber + ", destination system key: "
                    + destSystemKey + " destination revision number: "
                    + destRevisionNumber
                    + " and updated destination image.");
        }

        MergeResult mr = null;
        try {
            SystemObject destSO = new SystemObject();
            destSO.setLID(destLID);
            destSO.setSystemCode(systemCode);
            destSO.setChildType(destImage.pGetTag());
            destSO.setObject(destImage);

            mMatch.standardize(destSO);
            validateObjectNode(destImage);

            mr = mergeSystemObject(con, sourceSystemKey, destSystemKey, destSO,
                    srcRevisionNumber, destRevisionNumber, calculateOnly,
                    performPessimistic.booleanValue());

        } catch (UserException e) {
            throw new UserException(mLocalizer.t("MSC536: Could not merge " + 
                "SystemObjects: {0}", e));
        } catch (Exception e) {
            throwProcessingException(e);
        }
        return mr;
    }

    /**
     * Mark the potential duplicate with resolve or auto resolve status. Auto
     * resolve differs from resolve in that once it is flagged, later
     * reevaluations of duplicates (as would occur in pessimistic update mode)
     * will not result in the potential duplicate status being changed back to
     * unresolved.
     * 
     * @param con
     *            Connection
     * @param id
     *            The potential duplicate id to be resolved.
     * @param autoResolve
     *            Set true to auto resolve.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid id
     */
    public void resolvePotentialDuplicate(Connection con, String id,
            boolean autoResolve) throws ProcessingException, UserException {

        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("resolvePotentialDuplicate(): invoked with id: " + id
                    + ", autoResolve: " + autoResolve);
        }
        try {

            mPotDup.resolvePotentialDuplicate(con, id, autoResolve);

        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
    }

    /**
     * Returns an iterator of objects that match the given search criteria and
     * options. Criteria consists of a system object, and options consists of a
     * query builder id as well as other aspects of how the search should be
     * conducted.
     * 
     * @param criteria
     *            Search criteria.
     * @param searchOptions
     *            Search options.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid parameters
     * @return Iterator of search results.
     */
    public EOSearchResultIterator searchEnterpriseObject(
            EOSearchCriteria criteria, EOSearchOptions searchOptions)
            throws ProcessingException, UserException {

        EOSearchResultIterator retIterator = null;

        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("searchEnterpriseObject(): search criteria:" + criteria);
            mLogger.fine("searchEnterpriseObject(): search options:" +searchOptions);
        }

        // Check if EUID was selected. If not, throw a ProcessingException
        boolean euidSelected = false;
        EPathArrayList fields = searchOptions.getFieldsToRetrieve();
        for (int i = 0; i < fields.size(); i++) {
            String epath = fields.get(i).toString();
            int index = epath.lastIndexOf('.');
            String fieldName = epath.substring(index + 1);
            if (fieldName.compareTo("EUID") == 0) {
                euidSelected = true;
                break;
            }
        }
        if (!euidSelected) {
            throw new ProcessingException(mLocalizer.t("MSC537: EUID must be a selected field."));
        }
        QMIterator qmIterator = null;
        try {
            PageAdapter adapter = null;
            SystemObject sysObj = criteria.getSystemObject();
            // RANGE_SEARCH: we now allow more than one SystemObject to be
            // passed in
            SystemObject sysObj2 = criteria.getSystemObject2();
            SystemObject sysObj3 = criteria.getSystemObject3();

            // Determine max number of elements to retrieve. Server side limits
            // override client side request if server limit is smaller.
            int maxElements = searchOptions.getMaxElements();
            int maxConfigElements = Constants.DEFAULT_MAX_ELEMENTS;

            if (maxElements == 0) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("searchEnterpriseObject(): setting max elements to default "
                                    + maxConfigElements);
                }
                maxElements = maxConfigElements;
            }

            searchOptions.setMaxQueryElements(maxElements);

            // Determine if record should be standardized
            QueryBuilderConfiguration queryBuilderConfiguration = (QueryBuilderConfiguration) ConfigurationService
                    .getInstance().getConfiguration(
                            QueryBuilderConfiguration.QUERY_BUILDER);

            QueryBuilder queryBuilder = queryBuilderConfiguration
                    .getQueryBuilder(searchOptions.getSearchId());

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("searchEnterpriseObject(): is standardization required: "
                        + queryBuilder.isStandardizeRequired());
                mLogger.fine("searchEnterpriseObject(): is phoneticization rqeuired: "
                        + queryBuilder.isPhoneticizeRequired());
            }

            if (queryBuilder.isStandardizeRequired()
                    || queryBuilder.isPhoneticizeRequired()) {
                sysObj = mMatch.standardize(sysObj);
                // RANGE_SEARCH: if other SystemObjects have been passed in,
                // they should also be standardized
                if (sysObj2 != null) {
                    sysObj2 = mMatch.standardize(sysObj2);
                }
                if (sysObj3 != null) {
                    sysObj3 = mMatch.standardize(sysObj3);
                }
            }

            // Determine if weighted or not and construct source adapter
            // accordingly
            if (searchOptions.isWeighted()) {
                MatchOptions matchOpts = new MatchOptions();
                matchOpts.setSortWeightResults(true);
                matchOpts.setMinimumWeight(Double.NEGATIVE_INFINITY);
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("searchEnterpriseObject(): "
                                    + "invoking Match Engine Controller for weighted search");
                }
                ArrayList list = mMatch.findMatch(criteria, searchOptions,
                        matchOpts);
                adapter = new EuidPageAdapter(list, searchOptions, maxElements);
            } else {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("searchEnterpriseObject(): "
                                + "invoking Query Manager for non-weighted search");
                }
                // Build query object
                String[] applicableQueryIds = queryBuilder
                        .getApplicableQueryIds(criteria, searchOptions);
                QueryObject queryObject = queryBuilder.buildQueryObject(
                        applicableQueryIds, criteria, searchOptions);

                // Set factory
                // RANGE_SEARCH: it is possible that sysObj will not be
                // populated, because
                // instead sysObj2 and/or sysObj3 have been populated (for range
                // search).
                // Therefore we need to check which has been populated.
                String fullObjPath = null;
                if (sysObj != null) {
                    fullObjPath = MetaDataService.getSBRPath(sysObj.getObject()
                            .pGetTag());
                } else if (sysObj2 != null) {
                    fullObjPath = MetaDataService.getSBRPath(sysObj2
                            .getObject().pGetTag());
                } else if (sysObj3 != null) {
                    fullObjPath = MetaDataService.getSBRPath(sysObj3
                            .getObject().pGetTag());
                } else {
                    throw new ProcessingException(mLocalizer.t("MSC538: At least one SystemObject must be populated."));
                }
                AssembleDescriptor assdesc = new AssembleDescriptor();
                EOSearchResultAssembler factory = new EOSearchResultAssembler();
                assdesc.setAssembler(factory);
                assdesc.setResultValueObjectType(factory
                        .getValueMetaNode(fullObjPath));
                queryObject.setAssembleDescriptor(assdesc);
                qmIterator = mQuery.executeAssemble(queryObject);
                adapter = new IteratorPageAdapter(qmIterator, maxElements);
            }

            // If result set is small enough, send all results to client without
            // going through PageData session bean
            int pageSize = searchOptions.getPageSize();
            // disable PageData due to serializable bug 6198
            if (adapter.count() < 2 * pageSize) {
                EOSearchResultRecord resultRecord[] = new EOSearchResultRecord[adapter
                        .count()];
                int i = 0;
                while (adapter.hasNext()) {
                    resultRecord[i++] = (EOSearchResultRecord) adapter.next();
                }
                retIterator = new EOSearchResultIterator(resultRecord);

            } else {
                PageData pd = (PageData) context
                        .lookup(JNDINames.EJB_REF_PAGEDATA);
                pd.setPageAdapter(adapter);
                pd.setPageAdapter(adapter);
                retIterator = new EOSearchResultIterator(pd, pageSize,
                        maxElements);
            }
        } catch (Exception t) {
            throwProcessingException(t);
        } finally {
            try {
                if (qmIterator != null) {
                    qmIterator.close();
                }
            } catch (Exception ex) {
                throwProcessingException(ex);
            }

        }

        return retIterator;
    }

    /**
     * Returns an iterator of objects that match the given search criteria and
     * options. Criteria consists of a array of EUID's, and options consists of
     * a query builder id as well as other aspects of how the search should be
     * conducted.
     * 
     * @param Euids
     *            EUID's to search for.
     * @param searchOptions
     *            Search options.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid parameters
     * @return Iterator of search results.
     */
    public EOSearchResultIterator searchEnterpriseObject(String Euids[],
            EOSearchOptions searchOptions) throws ProcessingException,
            UserException {

        EOSearchResultIterator retIterator = null;
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("searchEnterpriseObject(): search options:\n" + searchOptions);
            mLogger.fine(searchOptions);
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < Euids.length; i++) {
            sb.append(Euids[i]).append(',');
        }
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("searchEnterpriseObject(): EUIDs to retrieve:"
                    + sb.toString());
        }

        QMIterator iterator = null;
        try {
            PageAdapter adapter = null;

            // Determine max number of elements to retrieve. Server side limits
            // override client side request if server limit is smaller.
            int maxElements = searchOptions.getMaxElements();
            int maxConfigElements = Constants.DEFAULT_MAX_ELEMENTS;
            if (maxElements == 0) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("searchEnterpriseObject(): setting max elements to default "
                                    + maxConfigElements);
                }
                maxElements = maxConfigElements;
            }

            // Build query object
            EPathArrayList fieldArrayList = searchOptions.getFieldsToRetrieve();
            String[] fieldArray = fieldArrayList.toStringArray();
            String objPath = getObjectPath(fieldArray);
            String euidPath = objPath + "." + "EUID";

            QueryObject queryObject = new QueryObject();
            queryObject.setSelect(fieldArray);
            queryObject.addCondition(new Condition(euidPath, "IN", Euids));

            // Set factory
            AssembleDescriptor assdesc = new AssembleDescriptor();
            EOSearchResultAssembler factory = new EOSearchResultAssembler();
            assdesc.setAssembler(factory);
            assdesc.setResultValueObjectType(factory.getValueMetaNode(objPath));
            queryObject.setAssembleDescriptor(assdesc);
            iterator = mQuery.executeAssemble(queryObject);

            adapter = new IteratorPageAdapter(iterator, maxElements);

            // If result set is small enough, send all results to client without
            // going through PageData session bean
            int pageSize = searchOptions.getPageSize();
            // disable PageData due to serializable bug
            if (adapter.count() < 2 * pageSize) {
                EOSearchResultRecord resultRecord[] = new EOSearchResultRecord[adapter
                        .count()];
                int i = 0;
                while (adapter.hasNext()) {
                    resultRecord[i++] = (EOSearchResultRecord) adapter.next();
                }
                retIterator = new EOSearchResultIterator(resultRecord);

            } else {
                PageData pd = (PageData) context
                        .lookup(JNDINames.EJB_REF_PAGEDATA);
                pd.setPageAdapter(adapter);
                retIterator = new EOSearchResultIterator(pd, pageSize,
                        maxElements);
            }

            iterator.close();
        } catch (Exception t) {
            throwProcessingException(t);
        } finally {
            try {
                if (iterator != null) {
                    iterator.close();
                }
            } catch (Exception ex) {
                throw new ProcessingException(mLocalizer.t("MSC539: searchEnterpriseObject() " + 
                                            "encountered an error: {0}", ex));
            }
        }
        return retIterator;
    }

    /**
     * Determines the path of the root object that will be returned based on the
     * fields that have been selected. Method assumeds that the EUID has been
     * selected. This was copied from EuidPageAdapter.java.
     * 
     * @param fieldList
     *            Fields to be retrieved
     * @throws PageException
     *             An error occured.
     * @return The root path
     */
    private String getObjectPath(String[] fieldList) throws ProcessingException {
        if (mObjectPath == null) {
            for (int i = 0; i < fieldList.length; i++) {
                if (fieldList[i].endsWith("EUID")) {
                    mObjectPath = fieldList[i].substring(0, fieldList[i]
                            .length() - 5);
                    return mObjectPath;
                }
            }
            throw new ProcessingException(mLocalizer.t("MSC540: getObjectPath() " + 
                                                       "requires EUID to be selected."));
        }
        return mObjectPath;
    }

    /**
     * Transfer system object from one enterprise object to another
     * 
     * @param con
     *            Connection
     * @param destinationEUID
     *            The EUID to transfer the SO to.
     * @param systemKey
     *            The key of the SO to transfer.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid parameters
     */
    public void transferSystemObject(Connection con, String destinationEUID,
            SystemObjectPK systemKey) throws ProcessingException, UserException {

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("transferSystemObject(): invoked with destination EUID: "
                                + destinationEUID + ", system key: " + systemKey);
            }

            String euid = mQueryHelper.getEUID(con, systemKey,
                    SystemObject.STATUS_ACTIVE);

            EnterpriseObject srcEO = mTrans.getEnterpriseObject(con, euid);
            EnterpriseObject destEO = mTrans.getEnterpriseObject(con,
                    destinationEUID);

            if (srcEO == null) {
                throw new UserException(mLocalizer.t("MSC541: EntepriseObject does not " + 
                                                     "exist for SystemCode={0}, LID={1}", 
                                                     systemKey.systemCode, systemKey.lID));
            } else if (destEO == null) {
                throw new UserException(mLocalizer.t("MSC542: Invalid destinationEUID: {0}", destinationEUID));
            }

            Object[] srcMatchFields = mMatchFieldChange.getMatchFields(srcEO);
            Object[] destMatchFields = mMatchFieldChange.getMatchFields(destEO);

            if (srcEO.getEUID().equals(destinationEUID)) {
                throw new ProcessingException(mLocalizer.t("MSC543: SystemObjects must " +
                                    "be transferred between two different EUIDs.  Both " + 
                                    "EUIDs are: {0}", destinationEUID));
            }

            String user = getCallerUserId();

            UpdateResult result = mUpdate.transferSystem(con, srcEO, destEO,
                    systemKey.systemCode, systemKey.lID, user);

            EnterpriseObject afterDestEO = result.getEnterpriseObject1();
            EnterpriseObject afterSrcEO = result.getEnterpriseObject2();

            if (afterSrcEO != null) {
                if (afterSrcEO.isRemoved()
                        || (afterSrcEO.getSBR().getStatus() != null && !afterSrcEO
                                .getSBR().getStatus().equals(
                                        SystemObject.STATUS_ACTIVE))) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("transferSystemObject(): removing potential duplicates for non-active EUID: "
                                        + afterSrcEO.getEUID());
                    }
                    mPotDup.deletePotentialDuplicates(con, afterSrcEO.getEUID());
                } else {
                    if (pessimisticModeEnabled) {
                        String transId = result.getTransactionResult()
                                .getTMID();
                        execPessimistic(con, afterSrcEO.getEUID(), transId,
                                srcMatchFields, afterSrcEO, null);
                    }
                }
            }

            if (pessimisticModeEnabled) {
                String transId = result.getTransactionResult().getTMID();
                // If the afterSourceEO is removed, that EUID should be ignored
                // when recalculating potential duplicates for the 'to' record.
                // If it still has at least one SO, it may still be a potential
                // duplicate, so it must be included when recalculating
                // potential duplicates.
                if (afterSrcEO == null || afterSrcEO.isRemoved()) {
                    execPessimistic(con, afterDestEO.getEUID(), transId,
                            destMatchFields, afterDestEO, euid);
                } else {
                    execPessimistic(con, afterDestEO.getEUID(), transId,
                            destMatchFields, afterDestEO, null);
                }
            }

            mOutBoundSender.send(OutBoundMessages.MRG, result
                    .getTransactionResult().getTMID(), result
                    .getEnterpriseObject1());
        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
    }

    /**
     * Decouple the enterprise objects involved in the last merge operation for
     * the given EUID. This will result in the merge object being reestablished
     * as an active enterprise object. The EO represented by activeEUID can be
     * retrieved using MergeResult.getDesinationEO(). The reactivated (formerly
     * merged) EO can be retrieved using MergeResult.getSourceEO().
     * 
     * @param con
     *            Connection
     * @param activeEUID
     *            The EUID to be unmerged.
     * @param calculateOnly
     *            Indicate whether to commit changes to DB or just compute the
     *            MergeResult. See Constants.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid euid
     * @return Result of unmerge operation.
     */
    public MergeResult unmergeEnterpriseObject(Connection con,
            String activeEUID, boolean calculateOnly)
            throws ProcessingException, UserException {

        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("unmergeEnterpriseObject(): invoked with EUID: "
                    + activeEUID + ", calculateOnly: " + calculateOnly);
        }
        MergeResult mergeResult = new MergeResult();
        try {

            // Load up the EO's involved
            EnterpriseObject activeEO = mTrans.getEnterpriseObject(con,
                    activeEUID);
            // To_DO: getStatus always returns null!!!
            if (activeEO == null /* || !activeEO.getStatus().equals("active") */) {
                throw new ProcessingException(mLocalizer.t("MSC544: EnterpriseObjects could " +
                                    "not be unmerged from EUID {0}. Record has been " + 
                                    "modified by another user.", activeEUID));
            }
            Object[] activeMatchFields = mMatchFieldChange
                    .getMatchFields(activeEO);
            MergeHistoryNode mergeHistoryNode = getMergeHistory(con, activeEUID);
            // if mergeHistoryNode is null, then the record has already been
            // unmerged
            if (mergeHistoryNode == null) {
                throw new ProcessingException(mLocalizer.t("MSC545: Record has been " + 
                                    "modified by another user. EUID {0} has already " +
                                    "been unmerged", activeEUID));
            }
            TransactionObject transObj = mergeHistoryNode
                    .getTransactionObject();
            if (transObj == null) {
                throw new ProcessingException(mLocalizer.t("MSC546: Could not retrieve merge transaction."));
            }
            String transId = transObj.getTransactionNumber();
            mergeHistoryNode = mergeHistoryNode.getSourceNode();
            if (mergeHistoryNode == null) {
                throw new ProcessingException(mLocalizer.t("MSC547: EUID {0} " +
                                    "does not have merge history.", activeEUID));
            }
            String mergedEUID = mergeHistoryNode.getEUID();
            int flag = Constants.FLAG_UM_NONE;
            if (calculateOnly) {
                flag = Constants.FLAG_UM_CALC_ONLY;
            }

            String user = getCallerUserId();
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("unmergeEnterpriseObject(): Unmerging transaction ["
                              + transId + "]");
            }
            UpdateResult result = mUpdate.unmergeEnterprise(con, transId, flag,
                    user);
            EnterpriseObject afterDestEO = result.getEnterpriseObject1();
            mergeResult.setDestinationEO(afterDestEO);
            mergeResult.setSourceEO(result.getEnterpriseObject2());
            if (!calculateOnly) {
                // Reevaluate potential duplicates for source record
                transId = result.getTransactionResult().getTMID();
                transObj = mTrans.findTransactionLog(con, transId);
                mergeResult.setTransactionObject(transObj);

                SBR mergedSBR = mergeResult.getSourceEO().getSBR();
                findInsertDuplicates(con, mergedEUID, transId, mergedSBR,
                        activeEUID);
                // Reevaluate duplicates for 'destination' record if
                // pessimistic mode on
                if (pessimisticModeEnabled) {
                    SBR afterSBR = activeEO.getSBR();
                    execPessimistic(con, activeEUID, transId,
                            activeMatchFields, afterDestEO, null);
                }

                mOutBoundSender.send(OutBoundMessages.UNMRG, result
                        .getTransactionResult().getTMID(), mergeResult);
            }
        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }

        return mergeResult;
    }

    /**
     * Decouple the enterprise objects involved in the last merge operation for
     * the given EUID. This will result in the merge object being reestablished
     * as an active enterprise object. The EO represented by activeEUID can be
     * retrieved using MergeResult.getDesinationEO(). The reactivated (formerly
     * merged) EO can be retrieved using MergeResult.getSourceEO().
     * 
     * @param con
     *            Connection
     * @param activeEUID
     *            The EUID to be unmerged.
     * @param srcRevisionNumber
     *            The revision number of the record.
     * @param calculateOnly
     *            Indicate whether to commit changes to DB or just compute the
     *            MergeResult. See Constants.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid euid
     * @return Result of unmerge operation.
     */
    public MergeResult unmergeEnterpriseObject(Connection con,
            String activeEUID, String srcRevisionNumber, boolean calculateOnly)
            throws ProcessingException, UserException {

        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("unmergeEnterpriseObject(): invoked with EUID: "
                    + activeEUID + ", revisionNumber: " + srcRevisionNumber
                    + ", calculateOnly: " + calculateOnly);
        }
        MergeResult mergeResult = new MergeResult();
        try {

            // Load up the EO's involved
            EnterpriseObject activeEO = mTrans.getEnterpriseObject(con,
                    activeEUID);
            // To_DO: getStatus always returns null!!!
            if (activeEO == null) {
                throw new ProcessingException(mLocalizer.t("MSC548: Record for EUID {0} " +
                                    "has been modified by another user.", activeEUID));
            }
            // if srcRevisionNumber is changed, then the record has been
            // modified by other user
            if (srcRevisionNumber != null) {
                Integer curRevisionNumber = activeEO.getSBR()
                        .getRevisionNumber();
                if (!srcRevisionNumber.equalsIgnoreCase(curRevisionNumber
                        .toString())) {
                    throw new UserException(mLocalizer.t("MSC549: Source record " +
                                    "has been modified by another user."));
                            
                }
            }

            Object[] activeMatchFields = mMatchFieldChange
                    .getMatchFields(activeEO);
            MergeHistoryNode mergeHistoryNode = getMergeHistory(con, activeEUID);
            // if mergeHistoryNode is null, then the record has already been
            // unmerged
            if (mergeHistoryNode == null) {
                throw new ProcessingException(mLocalizer.t("MSC550: Record has been " +
                                    "modified by another user. EUID has already been " + 
                                    "unmerged: {0}", activeEUID));
            }
            TransactionObject transObj = mergeHistoryNode
                    .getTransactionObject();
            if (transObj == null) {
                throw new ProcessingException(mLocalizer.t("MSC551: Could not " +
                                    "retrieve merge transaction."));
            }
            String transId = transObj.getTransactionNumber();

            mergeHistoryNode = mergeHistoryNode.getSourceNode();
            if (mergeHistoryNode == null) {
                throw new ProcessingException(mLocalizer.t("MSC552: Could not " +
                                    "unmerge EnterpriseObject for EUID {0}. " + 
                                    "Record does not have a merge history."));
            }
            String mergedEUID = mergeHistoryNode.getEUID();

            int flag = Constants.FLAG_UM_NONE;
            if (calculateOnly) {
                flag = Constants.FLAG_UM_CALC_ONLY;
            }

            String user = getCallerUserId();

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("unmergeEnterpriseObject(): Unmerging transaction ["
                             + transId + "]");
            }
            UpdateResult result = mUpdate.unmergeEnterprise(con, transId, flag,
                    user);
            EnterpriseObject afterDestEO = result.getEnterpriseObject1();
            mergeResult.setDestinationEO(afterDestEO);
            mergeResult.setSourceEO(result.getEnterpriseObject2());

            if (!calculateOnly) {
                // Reevaluate potential duplicates for source record
                transId = result.getTransactionResult().getTMID();
                transObj = mTrans.findTransactionLog(con, transId);
                mergeResult.setTransactionObject(transObj);

                SBR mergedSBR = mergeResult.getSourceEO().getSBR();
                findInsertDuplicates(con, mergedEUID, transId, mergedSBR,
                        activeEUID);
                // Reevaluate duplicates for 'destination' record if
                // pessimistic mode on
                if (pessimisticModeEnabled) {
                    SBR afterSBR = activeEO.getSBR();
                    execPessimistic(con, activeEUID, transId,
                            activeMatchFields, afterDestEO, null);
                }

                mOutBoundSender.send(OutBoundMessages.UNMRG, result
                        .getTransactionResult().getTMID(), mergeResult);
            }
        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }

        return mergeResult;
    }

    /**
     * Return the given merged local identifier to active status.
     * 
     * @param con
     *            Connectionv
     * @param systemCode
     *            The system to which the local id's belong
     * @param sourceLID
     *            The LID to be unmerged.
     * @param destLID
     *            The LID that was the destination.
     * @param calculateOnly
     *            Indicate whether to commit changes to DB or just compute the
     *            MergeResult. See Constants.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid parameters
     * @return Result of unmerge operation.
     */
    public MergeResult unmergeSystemObject(Connection con, String systemCode,
            String sourceLID, String destLID, boolean calculateOnly)
            throws ProcessingException, UserException {

        MergeResult mergeResult = new MergeResult();
        SystemObjectPK sourceSystemKey = new SystemObjectPK(systemCode,
                sourceLID);
        SystemObjectPK destSystemKey = new SystemObjectPK(systemCode, destLID);
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("unmergeSystemObject(): invoked with source system key: "
                    + sourceSystemKey + ", destination system key: "
                    + destSystemKey + ", calculateOnly: " + calculateOnly);
        }
        try {

            String fromEUID = mQueryHelper.getEUID(con, sourceSystemKey);
            if (fromEUID == null) {
                String sysDesc = lookupSystemDefinition(con, systemCode)
                        .getDescription();
                String sysMsg = "[" + sysDesc + "," + sourceLID + "]";
                throw new ProcessingException(mLocalizer.t("MSC553: Source system record not found " +
                                    "for System Description={0}, LID={1}", 
                                    sysDesc, sourceLID));
            } else {
                String status = mQueryHelper.getSOStatus(con, sourceSystemKey);
                String errMsg = null;
                if (status == null) {
                    errMsg = "Source system record not found: ";
                } else if (status
                        .compareToIgnoreCase(SystemObject.STATUS_INACTIVE) == 0) {
                    errMsg = "Source system record has already been deactivated: ";
                } else if (status
                        .compareToIgnoreCase(SystemObject.STATUS_ACTIVE) == 0) {
                    errMsg = "Source system record is not in merged status: ";
                } else if (status
                        .compareToIgnoreCase(SystemObject.STATUS_MERGED) != 0) { // unrecognized
                    // status
                    errMsg = "Source system status unrecognized: ";
                }
                if (errMsg != null) {
                    String sysDesc = lookupSystemDefinition(con, systemCode)
                            .getDescription();
                    String sysMsg = "[" + sysDesc + "," + sourceLID + "]";
                    throw new ProcessingException(mLocalizer.t("MSC554: Record has been modified by another user " +
                                    "for System Description={0}, LID={1}: {3}", 
                                    sysDesc, sourceLID, errMsg));
                }
            }

            String toEUID = mQueryHelper.getEUID(con, destSystemKey,
                    SystemObject.STATUS_ACTIVE);
            if (toEUID == null) {
                String sysDesc = lookupSystemDefinition(con, systemCode)
                        .getDescription();
                String sysMsg = "[" + sysDesc + "," + destLID + "]";
                throw new ProcessingException(mLocalizer.t("MSC555: Destination system record not found " +
                                    "for System Description={0}, LID={1}", 
                                    sysDesc, sourceLID));

            }

            EnterpriseObject beforedestinationEO = mTrans.getEnterpriseObject(
                    con, toEUID);

            TransactionObject tObj = new TransactionObject();
            tObj.setLID1(destSystemKey.lID);
            tObj.setLID2(sourceSystemKey.lID);
            tObj.setSystemCode(sourceSystemKey.systemCode);
            tObj.setFunction("lidMerge");
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("unmergeSystemObject(): searching for unmerge transaction:\n" + tObj);
            }
            TransactionObject transObjArray[] = mTrans.findTransactionLogs(con,
                    tObj, null, null);
            if (transObjArray.length == 0) {
                throw new ProcessingException(mLocalizer.t("MSC556: No transactions found for LID merge."));
            }
            int flag = Constants.FLAG_UM_NONE;
            if (calculateOnly) {
                flag = Constants.FLAG_UM_CALC_ONLY;
            }
            // Unmerge the last transaction
            TransactionObject transObj = transObjArray[transObjArray.length - 1];
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("unmergeSystemObject(): undoing transaction: "
                        + transObj.getTransactionNumber());
            }

            String user = getCallerUserId();

            UpdateResult ur = mUpdate.unmergeSystem(con, transObjArray[0]
                    .getTransactionNumber(), flag, user);

            mergeResult.setDestinationEO(ur.getEnterpriseObject1());
            mergeResult.setSourceEO(ur.getEnterpriseObject2());
            if (!calculateOnly) {
                String transId = ur.getTransactionResult().getTMID();
                mergeResult.setTransactionObject(mTrans.findTransactionLog(con,
                        transId));
                EnterpriseObject sourceEO = mergeResult.getSourceEO();

                if (sourceEO != null) {
                    Collection srcSysObjs = sourceEO.getSystemObjects();

                    int activeCount = 0;

                    if (srcSysObjs != null) {
                        Iterator i = srcSysObjs.iterator();
                        while (i.hasNext()) {
                            SystemObject so = (SystemObject) i.next();
                            // Note that merged system objects do not get their
                            // status changed
                            if (so.getStatus().equals(
                                    SystemObject.STATUS_ACTIVE)) {
                                activeCount = activeCount + 1;
                            }
                        }
                    }

                    SBR sourceSBR = sourceEO.getSBR();

                    if (activeCount > 1) {
                        if (pessimisticModeEnabled) {
                            mPotDup.deletePotentialDuplicates(con, sourceEO
                                    .getEUID());
                            findInsertDuplicates(con, sourceEO.getEUID(),
                                    transId, sourceSBR, null);
                        }
                    } else {
                        if ((sourceEO.getSBR().getStatus() != null)
                                && (sourceEO.getSBR().getStatus()
                                        .equals(SystemObject.STATUS_ACTIVE))) {
                            if (mLogger.isLoggable(Level.FINE)) {
                                mLogger.fine("unmergeSystemObject(): find and "
                                            + "insert potential duplicates for activated EUID: "
                                            + sourceEO.getEUID());
                            }
                            findInsertDuplicates(con, sourceEO.getEUID(),
                                    transId, sourceSBR, null);
                        }
                    }
                }

                if (pessimisticModeEnabled) {
                    EnterpriseObject destinationEO = mergeResult
                            .getDestinationEO();
                    Object[] activeDestinationMatchFields = mMatchFieldChange
                            .getMatchFields(beforedestinationEO);

                    SBR destinationSBR = destinationEO.getSBR();
                    execPessimistic(con, destinationEO.getEUID(), transId,
                            activeDestinationMatchFields, destinationEO, null);
                }

                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("unmergeSystemObject(): unmerge transaction completed, trans id: "
                                    + transId);
                }

                mOutBoundSender.send(OutBoundMessages.UNMRG, ur
                        .getTransactionResult().getTMID(), mergeResult);
            }
        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
        return mergeResult;
    }

    /**
     * Return the given merged local identifier to active status. The revision
     * numbers of the source SBR is passed as an argument. At a later stage, it
     * is compared to the SBR stored in the database. If they differ, it means
     * that either the source record was modified by another user. In this case,
     * the unmerge should not be allowed.
     * 
     * @param con
     *            Connection
     * @param systemCode
     *            The system to which the local id's belong
     * @param sourceLID
     *            The LID to be unmerged.
     * @param destLID
     *            The LID that was the destination.
     * @param srcRevisionNumber
     *            The revision number of the source record's SBR
     * @param calculateOnly
     *            Indicate whether to commit changes to DB or just compute the
     *            MergeResult. See Constants.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid parameters
     * @return Result of unmerge operation.
     */
    public MergeResult unmergeSystemObject(Connection con, String systemCode,
            String sourceLID, String destLID, String srcRevisionNumber,
            boolean calculateOnly) throws ProcessingException, UserException {

        MergeResult mergeResult = new MergeResult();
        SystemObjectPK sourceSystemKey = new SystemObjectPK(systemCode,
                sourceLID);
        SystemObjectPK destSystemKey = new SystemObjectPK(systemCode, destLID);
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("unmergeSystemObject(): invoked with source system key: "
                    + sourceSystemKey + ", destination system key: "
                    + destSystemKey + ", calculateOnly: " + calculateOnly);
        }
        try {

            String fromEUID = mQueryHelper.getEUID(con, sourceSystemKey);
            if (fromEUID == null) {
                String sysDesc = lookupSystemDefinition(con, systemCode)
                        .getDescription();
                String sysMsg = "[" + sysDesc + "," + sourceLID + "]";
                throw new ProcessingException(mLocalizer.t("MSC561: Source system record not found " +
                                    "for System Description={0}, LID={1}", 
                                    sysDesc, sourceLID));
            } else {
                String status = mQueryHelper.getSOStatus(con, sourceSystemKey);
                String errMsg = null;
                if (status == null) {
                    errMsg = "Source system record not found: ";
                } else if (status
                        .compareToIgnoreCase(SystemObject.STATUS_INACTIVE) == 0) {
                    errMsg = "Source system record has already been deactivated: ";
                } else if (status
                        .compareToIgnoreCase(SystemObject.STATUS_ACTIVE) == 0) {
                    errMsg = "Source system record is not in merged status: ";
                } else if (status
                        .compareToIgnoreCase(SystemObject.STATUS_MERGED) != 0) { // unrecognized
                    // status
                    errMsg = "Source system status unrecognized: ";
                }
                if (errMsg != null) {
                    String sysDesc = lookupSystemDefinition(con, systemCode)
                            .getDescription();
                    String sysMsg = "[" + sysDesc + "," + sourceLID + "]";
                    throw new ProcessingException(mLocalizer.t("MSC557: System records could not be unmerged. " +
                                    "Record has been modified by another user " +
                                    "for System Description={0}, LID={1}: {2}", 
                                    sysDesc, sourceLID, errMsg));
                }
            }

            String toEUID = mQueryHelper.getEUID(con, destSystemKey,
                    SystemObject.STATUS_ACTIVE);
            if (toEUID == null) {
                String sysDesc = lookupSystemDefinition(con, systemCode)
                        .getDescription();
                String sysMsg = "[" + sysDesc + "," + destLID + "]";
                throw new ProcessingException(mLocalizer.t("MSC558: System records could not be unmerged. " +
                                    "Destination system record not found for " +
                                    "for System Description={0}, LID={1}", 
                                    sysDesc, sourceLID));
            }

            EnterpriseObject beforedestinationEO = mTrans.getEnterpriseObject(
                    con, toEUID);

            TransactionObject tObj = new TransactionObject();
            tObj.setLID1(destSystemKey.lID);
            tObj.setLID2(sourceSystemKey.lID);
            tObj.setSystemCode(sourceSystemKey.systemCode);
            tObj.setFunction("lidMerge");
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("unmergeSystemObject(): searching for unmerge transaction" + tObj);
            }
            TransactionObject transObjArray[] = mTrans.findTransactionLogs(con,
                    tObj, null, null);
            if (transObjArray.length == 0) {
                throw new ProcessingException(mLocalizer.t("MSC559: System records could not be unmerged. " +
                                    "No transactions found for LID merge."));
            }
            int flag = Constants.FLAG_UM_NONE;
            if (calculateOnly) {
                flag = Constants.FLAG_UM_CALC_ONLY;
            }
            // Unmerge the last transaction
            TransactionObject transObj = transObjArray[transObjArray.length - 1];
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("unmergeSystemObject(): undoing transaction: "
                        + transObj.getTransactionNumber());
            }

            String user = getCallerUserId();

            // Check if the revision numbers match. If srcRevisionNumber
            // is null, no checking is done. The calling methods must handle a
            // null srcRevisionNumber correctly.

            if (srcRevisionNumber != null) {
                Integer curRevisionNumber = beforedestinationEO.getSBR()
                        .getRevisionNumber();
                if (!srcRevisionNumber.equalsIgnoreCase(curRevisionNumber
                        .toString())) {
                    throw new UserException(mLocalizer.t("MSC562: System records could not be unmerged. " +
                                    "Source record has been modified by another user."));
                }
            }

            UpdateResult ur = mUpdate.unmergeSystem(con, transObjArray[0]
                    .getTransactionNumber(), flag, user);

            mergeResult.setDestinationEO(ur.getEnterpriseObject1());
            mergeResult.setSourceEO(ur.getEnterpriseObject2());
            if (!calculateOnly) {
                String transId = ur.getTransactionResult().getTMID();
                mergeResult.setTransactionObject(mTrans.findTransactionLog(con,
                        transId));
                EnterpriseObject sourceEO = mergeResult.getSourceEO();

                if (sourceEO != null) {
                    Collection srcSysObjs = sourceEO.getSystemObjects();

                    int activeCount = 0;

                    if (srcSysObjs != null) {
                        Iterator i = srcSysObjs.iterator();
                        while (i.hasNext()) {
                            SystemObject so = (SystemObject) i.next();
                            // Note that merged system objects do not get their
                            // status changed
                            if (so.getStatus().equals(
                                    SystemObject.STATUS_ACTIVE)) {
                                activeCount = activeCount + 1;
                            }
                        }
                    }

                    SBR sourceSBR = sourceEO.getSBR();

                    if (activeCount > 1) {
                        if (pessimisticModeEnabled) {
                            mPotDup.deletePotentialDuplicates(con, sourceEO
                                    .getEUID());
                            findInsertDuplicates(con, sourceEO.getEUID(),
                                    transId, sourceSBR, null);
                        }
                    } else {
                        if ((sourceEO.getSBR().getStatus() != null)
                                && (sourceEO.getSBR().getStatus()
                                        .equals(SystemObject.STATUS_ACTIVE))) {
                            if (mLogger.isLoggable(Level.FINE)) {
                                mLogger.fine("unmergeSystemObject(): find and insert "
                                            + "potential duplicates for activated EUID: "
                                            + sourceEO.getEUID());
                            }
                            findInsertDuplicates(con, sourceEO.getEUID(),
                                    transId, sourceSBR, null);
                        }
                    }
                }

                if (pessimisticModeEnabled) {
                    EnterpriseObject destinationEO = mergeResult
                            .getDestinationEO();
                    Object[] activeDestinationMatchFields = mMatchFieldChange
                            .getMatchFields(beforedestinationEO);

                    SBR destinationSBR = destinationEO.getSBR();
                    execPessimistic(con, destinationEO.getEUID(), transId,
                            activeDestinationMatchFields, destinationEO, null);
                }

                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("unmergeSystemObject(): unmerge transaction completed, trans id: "
                                    + transId);
                }

                mOutBoundSender.send(OutBoundMessages.UNMRG, ur
                        .getTransactionResult().getTMID(), mergeResult);
            }
        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
        return mergeResult;
    }

    /**
     * Change the status of a resolved or auto resolved potential duplicate back
     * to unresolved.
     * 
     * @param con
     *            Connection
     * @param id
     *            The id of the potential duplicate to unresolve.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                Invalid id
     */
    public void unresolvePotentialDuplicate(Connection con, String id)
            throws ProcessingException, UserException {

        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("unresolvePotentialDuplicate(): invoked with id: " + id);
            }
            mPotDup.unresolvePotentialDuplicate(con, id);

        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
    }

    /**
     * Update the database to reflect the new values of the given modified
     * enterprise object.
     * 
     * @param con
     *            Connection
     * @param eo
     *            The EO to be updated.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     */
    public void updateEnterpriseObject(Connection con, EnterpriseObject eo)
            throws ProcessingException, UserException {
                
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("updateEnterpriseObject() invoked for EUID: "
                        + eo.getEUID());
        }
        Object[] matchFields = null;
        try {
            Iterator i = eo.getSystemObjects().iterator();
            while (i.hasNext()) {
                SystemObject sysObjOrig = (SystemObject) i.next();
                if (sysObjOrig.isAdded() || isObjectChanged(sysObjOrig)) {
                    SystemObject sysObjNew = mMatch.standardize(sysObjOrig);
                    sysObjOrig.setObject(sysObjNew.getObject());
                }
            }
            // Even though the so's are changed, the SBR is still the same
            // as original, so it is OK to get match fields from this copy
            matchFields = mMatchFieldChange.getMatchFields(eo);
        } catch (InstantiationException e) {
            throwProcessingException(e);
        }
        validateEnterpriseObject(con, eo);

        try {

            String user = getCallerUserId();

            UpdateResult result = mUpdate.updateEnterprise(con, eo, 0, user);

            EnterpriseObject afterEO = result.getEnterpriseObject1();

            if ((afterEO.getSBR().getStatus() != null)
                    && (!afterEO.getSBR().getStatus().equals(
                            SystemObject.STATUS_ACTIVE))) {
                mPotDup.deletePotentialDuplicates(con, afterEO.getEUID());
            } else if (pessimisticModeEnabled
                    && result.getTransactionResult() != TMResult.NO_CHANGE_TRANSACTION) {
                String transId = result.getTransactionResult().getTMID();
                execPessimistic(con, eo.getEUID(), transId, matchFields,
                        afterEO, null);
            }

            if (result.getTransactionResult() != TMResult.NO_CHANGE_TRANSACTION) {
                mOutBoundSender.send(OutBoundMessages.UPD, result
                        .getTransactionResult().getTMID(), result
                        .getEnterpriseObject1());
            }
        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
    }

    /**
     * Update the database to reflect the new values of the given modified
     * enterprise object. If performPessimistic is set to false, then
     * pessimistic mode processing is deferred.
     * 
     * @param con
     *            Connection
     * @param eo
     *            The EO to be updated.
     * @param performPessimistic
     *            set to true to enable pessimistc mode processing, false
     *            otherwise.
     * @return UpdateResult object containing the results of the update
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     */
    public UpdateResult updateEnterpriseDupRecalc(Connection con,
            EnterpriseObject eo, Boolean performPessimistic)
            throws ProcessingException, UserException {
                
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("updateEnterpriseDupRecalc() invoked for EUID: "
                    + eo.getEUID());
        }
        Object[] matchFields = null;
        try {
            Iterator i = eo.getSystemObjects().iterator();
            while (i.hasNext()) {
                SystemObject sysObjOrig = (SystemObject) i.next();
                if (sysObjOrig.isAdded() || isObjectChanged(sysObjOrig)) {
                    SystemObject sysObjNew = mMatch.standardize(sysObjOrig);
                    sysObjOrig.setObject(sysObjNew.getObject());
                }
            }
            // Even though the so's are changed, the SBR is still the same
            // as original, so it is OK to get match fields from this copy
            matchFields = mMatchFieldChange.getMatchFields(eo);
        } catch (InstantiationException e) {
            throwProcessingException(e);
        }
        validateEnterpriseObject(con, eo);

        UpdateResult result = null;
        try {

            String user = getCallerUserId();

            result = mUpdate.updateEnterprise(con, eo, 0, user);
            if (result.getTransactionResult() != TMResult.NO_CHANGE_TRANSACTION
                    && performPessimistic.booleanValue() == true) {
                String transId = result.getTransactionResult().getTMID();
                EnterpriseObject afterEO = result.getEnterpriseObject1();
                execPessimistic(con, eo.getEUID(), transId, matchFields,
                        afterEO, null);
            }

            EnterpriseObject afterEO = result.getEnterpriseObject1();
            Object[] afterMatchFields = mMatchFieldChange
                    .getMatchFields(afterEO);
            boolean matchFieldChangedVal = mMatchFieldChange
                    .isMatchFieldChanged(matchFields, afterMatchFields);
            result.setMatchFieldChanged(matchFieldChangedVal);
            if (result.getTransactionResult() != TMResult.NO_CHANGE_TRANSACTION) {
                mOutBoundSender.send(OutBoundMessages.UPD, result
                        .getTransactionResult().getTMID(), result
                        .getEnterpriseObject1());
            }
        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
        return result;
    }

    /**
     * Update the database to reflect the new values of the given modified
     * system object. Passes the revision number from the user.
     * 
     * @param con
     *            Connection
     * @param sysobj
     *            The SO to be updated.
     * @param revisionNumber
     *            The revision number of the SBR of the associated SO.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     */
    public void updateSystemObject(Connection con, SystemObject sysobj,
            String revisionNumber) throws ProcessingException, UserException {

        updateSystemObjectFacade(con, sysobj, revisionNumber);
    }

    /**
     * Update the database to reflect the new values of the given modified
     * system object. passes null as revision number for legacy users.
     * 
     * @param con
     *            Connection
     * @param sysobj
     *            The SO to be updated.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     */
    public void updateSystemObject(Connection con, SystemObject sysobj)
            throws ProcessingException, UserException {

        updateSystemObjectFacade(con, sysobj, null);
    }

    /**
     * Update the database to reflect the new values of the given modified
     * system object.
     * 
     * @param con
     *            Connection
     * @param sysobj
     *            The SO to be updated.
     * @param revisionNumber
     *            The revision number of the SBR of the associated SO.
     * @exception ProcessingException
     *                An error has occured.
     * @exception UserException
     *                A user error occured
     */
    private void updateSystemObjectFacade(Connection con, SystemObject sysobj,
            String revisionNumber) throws ProcessingException, UserException {

        SystemObjectPK key = new SystemObjectPK(sysobj.getSystemCode(), sysobj
                .getLID());
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("updateSystemObject(): invoked for system key: "
                    + key + " and revisionNumber " + revisionNumber);
        }

        try {
            sysobj = mMatch.standardize(sysobj);
        } catch (InstantiationException e) {
            throwProcessingException(e);
        }
        validateSystemObject(con, sysobj);
        try {

            String euid = mQueryHelper.getEUID(con, key,
                    SystemObject.STATUS_ACTIVE);

            if (euid == null) {
                throw new ProcessingException(mLocalizer.t("MSC563: SystemObject could not be " + 
                                    "updated because the record is not active for " +
                                    "System code={0}, LID={1}",  sysobj.getSystemCode(),
                                    sysobj.getLID()));
            }

            EnterpriseObject eo = mTrans.getEnterpriseObject(con, euid);

            if (eo == null) {
                throw new ProcessingException(mLocalizer.t("MSC564: SystemObject could not be " + 
                                    "updated because the record is not active for " +
                                    "System code={0}, LID={1}",  sysobj.getSystemCode(),
                                    sysobj.getLID()));
            }

            Object[] matchFields = mMatchFieldChange.getMatchFields(eo);

            String user = getCallerUserId();
            int flag = Constants.FLAG_UM_REPLACE_SO;
            UpdateResult result = mUpdate.updateEnterprise(con, sysobj, eo,
                    revisionNumber, flag, user);

            EnterpriseObject afterEO = result.getEnterpriseObject1();

            if ((afterEO.getSBR().getStatus() != null)
                    && (afterEO.getSBR().getStatus()
                            .equals(SystemObject.STATUS_ACTIVE))) {
                if (pessimisticModeEnabled
                        && result.getTransactionResult() != TMResult.NO_CHANGE_TRANSACTION) {
                    String transId = result.getTransactionResult().getTMID();
                    execPessimistic(con, afterEO.getEUID(), transId,
                            matchFields, afterEO, null);
                }
            }

            if (result.getTransactionResult() != TMResult.NO_CHANGE_TRANSACTION) {
                mOutBoundSender.send(OutBoundMessages.UPD, result
                        .getTransactionResult().getTMID(), result
                        .getEnterpriseObject1());
            }
        } catch (UserException e) {

            throw e;
        } catch (Exception e) {

            throwProcessingException(e);
        }
    }

    /**
     * Get configuration value
     * 
     * @param param
     *            parameter name
     * @exception ProcessingException
     *                processing error
     * @exception UserException
     *                invalid parameter
     * @return parameter value
     */
    public Object getConfigurationValue(String param)
            throws ProcessingException, UserException {
        // Currently only handle EUID_LENGTH parameter
        Object retVal = null;
        if (param.equals("EUID_LENGTH")) {
            try {
                EuidGeneratorConfiguration config = (EuidGeneratorConfiguration) ConfigurationService
                        .getInstance().getConfiguration(
                                EuidGeneratorConfiguration.EUID_GENERATOR);
                retVal = new Integer(config.getEuidGenerator().getEUIDLength());
            } catch (InstantiationException e) {
                mLogger.warn(mLocalizer.x("MSC011: getConfigurationValue(): encountered an error: {0}", e.getMessage()));
                throwProcessingException(e);
            }
        } else {
            throw new UserException(mLocalizer.t("MSC565: Invalid parameter name: {0}", param));
        }
        return retVal;
    }

    /**
     * Retrieves the SBR revision number for the specified EUID.
     * 
     * @param con
     *            Connection
     * @param euid
     *            The EUID to check.
     * @return An Integer containing the revision number.
     * @exception ProcessingException
     *                Thrown if an error occurs during the lookup.
     */
    public Integer getRevisionNumber(Connection con, String euid)
            throws ProcessingException {

        if (euid == null) {
            return null;
        }

        Integer revisionNumber = null;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("getRevisionNumber(): looking up EUID: " + euid);
            }

            revisionNumber = mQueryHelper.getRevisionNumber(con, euid);
        } catch (Exception e) {
            throwProcessingException(e);
        }
        return revisionNumber;
    }

    private OutBoundSender setupOutBoundSender() throws ProcessingException {

        OutBoundSender sender = new OutBoundSender();
        sender.setInTransactMode(mIsTransactional);
        try{
            String topicName = objectName+"Topic";
            sender.initialize(topicName);
        }catch(OutBoundException e){
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("setupOutBoundSender(): " +
                "failed to initialize OutBoundSender, outbound message disabled");
            }
        }
        return sender;
    }

    // ============ private methods ==========================================

    /**
     * Get JDBC connection
     * 
     * @throws Exception
     *             An error occured.
     * @return JDBC connection from pool.
     */
    public Connection getConnection() throws ConnectionInvalidException {
        try {
            Connection con = ConnectionUtil.getConnection();
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("getConnection(): obtaining a connection: " + con);
            }
            if (transactionType.equals("BMT_LOCAL")) {
                con.setAutoCommit(false);
            }

            return con;
        } catch (Exception e) {
            throw new ConnectionInvalidException(mLocalizer.t("MSC566: Failed to obtain " + 
                                                "a database connection: {0}", e));
        }
    }

    /**
     * Construct potential duplicate based on decision maker results.
     * 
     * @param euid1
     *            EUID for which list of matches is to be listed a duplicate of.
     * @param dmArray
     *            The array of decision maker results
     * @throws SystemObjectException
     *             An error occured.
     * @throws ObjectException
     *             An error occured.
     * @return An array of potential duplicates
     */
    private PotentialDuplicate[] constructPotentialDuplicates(String euid1,
            DecisionMakerStruct[] dmArray) throws SystemObjectException,
            ObjectException {

        PotentialDuplicate[] pots = new PotentialDuplicate[dmArray.length];
        for (int i = 0; i < pots.length; i++) {
            pots[i] = new PotentialDuplicate();
            pots[i].setEUID1(euid1);
            pots[i].setEUID2(dmArray[i].euid);
            pots[i].setProbability(dmArray[i].weight);
            pots[i].setDescription(dmArray[i].comment);
        }
        return pots;
    }

    /**
     * Construct potential duplicate based on MEC results.
     * 
     * @param euid1
     *            EUID for which list of matches is to be listed a duplicate of.
     * @param list
     *            The list of MEC results
     * @param ignoreEUID
     *            do not create pot dup with this euid
     * @throws SystemObjectException
     *             An error occured.
     * @throws ObjectException
     *             An error occured.
     * @return An array of potential duplicates
     */
    private PotentialDuplicate[] constructPotentialDuplicates(String euid1,
            ArrayList list, String ignoreEuid) throws SystemObjectException,
            ObjectException {

        ArrayList potDups = new ArrayList(list.size());
        Object scoreElements[] = list.toArray();
        for (int i = 0; i < scoreElements.length; i++) {
            ScoreElement se = (ScoreElement) scoreElements[i];
            String euid2 = se.getEUID();
            if (!euid1.equals(euid2)
                    && (ignoreEuid == null || !euid2.equals(ignoreEuid))) {
                PotentialDuplicate pd = new PotentialDuplicate();
                pd.setEUID1(euid1);
                pd.setEUID2(euid2);
                pd.setProbability((float) se.getWeight());
                potDups.add(pd);
            }
        }
        PotentialDuplicate[] pots = new PotentialDuplicate[potDups.size()];
        for (int i = 0; i < pots.length; i++) {
            pots[i] = (PotentialDuplicate) potDups.get(i);
        }

        return pots;
    }

    /**
     * Perform pessimistic mode handling.
     * 
     * @param con
     *            Connection
     * @param euid
     *            EUID to perform pessimistic handling on
     * @param transId
     *            Current transaction
     * @param beforeSBR
     *            Before image of SBR
     * @param afterSBR
     *            After image of SBR
     * @param ignoreEUID
     *            do not create pot dup with this euid
     * @throws ProcessingException
     *             An error occured.
     */
    private PotentialDuplicate[] execPessimistic(Connection con, String euid,
            String transId, Object[] beforeMatchFields,
            EnterpriseObject afterEO, String ignoreEuid)
            throws ProcessingException {
        try {
            Object afterMatchFields[] = mMatchFieldChange
                    .getMatchFields(afterEO);
            boolean sbrMatchFieldChanged = mMatchFieldChange
                    .isMatchFieldChanged(beforeMatchFields, afterMatchFields);
            PotentialDuplicate[] potDups = null;
            if (sbrMatchFieldChanged) {
                mPotDup.deletePotentialDuplicates(con, euid);
                potDups = findInsertDuplicates(con, euid, transId, afterEO
                        .getSBR(), ignoreEuid);
            }
            return potDups;
        } catch (Exception e) {
            throw new ProcessingException(mLocalizer.t("MSC567: execPessimistic() failed: {0}", e));
        }
    }

    /**
     * Calculates potential duplicates for a specified EUID and transaction ID.
     * These are persisted to the database.
     * 
     * @param con
     *            Connection
     * @param euid
     *            EUID to process
     * @param transID
     *            transaction ID to process
     * @return potential duplicates
     * @throws ProcessingException
     *             An error occured.
     * @throws RemoteException
     *             An error occured.
     */
    public void calculatePotentialDuplicates(Connection con, String euid,
            String transID) throws ProcessingException {

        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("calculatePotentialDuplicates(): invoked with EUID: "
                    + euid);
        }
        
        PotentialDuplicate pots[];
        try {
            MatchOptions opts = new MatchOptions();
            opts.setSortWeightResults(false);
            opts.setMinimumWeight(mDuplicateThreshold);

            SBR sbr = getSBR(con, euid);
            EOSearchCriteria eoSearchCriteria = new EOSearchCriteria(sbr);
            mPotDup.deletePotentialDuplicates(con, euid);
            ArrayList list = mMatch.findMatch(con, eoSearchCriteria,
                    mSearchOptions, opts);
            pots = constructPotentialDuplicates(euid, list, null);
            if (pots != null) {
                mPotDup.addPotentialDuplicates(con, euid, pots, transID);
            }

        } catch (Exception e) {
            throw new ProcessingException(mLocalizer.t("MSC568: calculatePotentialDuplicates() failed: {0}" , e));
        }
        return;
    }

    /**
     * Insert potential duplicates.
     * 
     * @param con
     *            Connection
     * @param euid
     *            EUID of duplicates
     * @param transId
     *            Current transaction
     * @param sbr
     *            SBR for which to find duplicates
     * @param ignoreEuid
     *            do not create pot dup with this euid
     * @throws ProcessingException
     *             An error occured.
     */
    private PotentialDuplicate[] findInsertDuplicates(Connection con,
            String euid, String transId, SBR sbr, String ignoreEuid)
            throws ProcessingException {
        try {
            MatchOptions opts = new MatchOptions();
            opts.setSortWeightResults(false);
            opts.setMinimumWeight(mDuplicateThreshold);
            EOSearchCriteria eoSearchCriteria = new EOSearchCriteria(sbr);
            ArrayList list = mMatch.findMatch(con, eoSearchCriteria,
                    mSearchOptions, opts);
            PotentialDuplicate pots[] = constructPotentialDuplicates(euid,
                    list, ignoreEuid);
            if (pots != null) {
                mPotDup.addPotentialDuplicates(con, euid, pots, transId);
            }
            return pots;
        } catch (Exception e) {
            throw new ProcessingException(mLocalizer.t("MSC569: findInsertDuplicates() failed: {0}" , e));
        }
    }

    /**
     * Internal method to service public mergeSystemObject methods. The revision
     * numbers of both the source and destination SBRs are passed as arguments.
     * At a laterstage, these are compared to the SBRs stored in the database.
     * If they differ, it means that either the source or destination records
     * were modified by another user. In this case, the merge should not be
     * allowed.
     * 
     * @param con
     *            Connection
     * @param sourceSystemKey
     *            The key of the system object to be merged
     * @param destinationSystemKey
     *            The key of the system object to be kept
     * @param newSO
     *            The actual system object to be kept. This parameter can be
     *            null if the kept system object should not be changed as a
     *            result of the merge.
     * @param srcRevisionNumber
     *            The revision number of the source record's SBR
     * @param destRevisionNumber
     *            The revision number of the destination record's SBR
     * @param calculateOnly
     *            Indicate whether to commit changes to DB or just compute the
     *            MergeResult. See Constants.
     * @param performPessimisticMode
     *            Set to true to enable pessimistic mode processing, false
     *            otherwise
     * @exception Exception
     *                An error occured.
     * @return Result of merge operation.
     */
    private MergeResult mergeSystemObject(Connection con,
            SystemObjectPK sourceSystemKey,
            SystemObjectPK destinationSystemKey, SystemObject newSO,
            String srcRevisionNumber, String destRevisionNumber,
            boolean calculateOnly, boolean performPessimisticMode)
            throws ProcessingException, UserException, InstantiationException {

        MergeResult mergeResult = new MergeResult();
        String transId = null;

        // Validate keys
        String fromEUID = mQueryHelper.getEUID(con, sourceSystemKey,
                SystemObject.STATUS_ACTIVE);
        if (fromEUID == null) {
            String status = mQueryHelper.getSOStatus(con, sourceSystemKey);
            String errMsg = null;
            String sysDesc = lookupSystemDefinition(con,
                    sourceSystemKey.systemCode).getDescription();
            String lid = sourceSystemKey.lID;
            String sysMsg = "[" + sysDesc + "," + lid + "]";

            if (status == null) {
                errMsg = "Source system record not found: " + sysMsg;
            } else if (status.compareToIgnoreCase(SystemObject.STATUS_INACTIVE) == 0) {
                errMsg = "Source system record has already been deactivated: "
                        + sysMsg;
            } else if (status.compareToIgnoreCase(SystemObject.STATUS_MERGED) == 0) {
                errMsg = "Source system record has already been merged: "
                        + sysMsg;
            } else { // unrecognized status
                errMsg = "Source system record not found: " + sysMsg;
            }
            throw new ProcessingException(mLocalizer.t("MSC570: Could not merge SystemObjects. " +
                                        "Record has been modified by another user: {0}", errMsg));
        }

        String toEUID = mQueryHelper.getEUID(con, destinationSystemKey,
                SystemObject.STATUS_ACTIVE);
        if (toEUID == null) {
            String status = mQueryHelper.getSOStatus(con, destinationSystemKey);
            String errMsg = null;
            String sysDesc = lookupSystemDefinition(con,
                    destinationSystemKey.systemCode).getDescription();
            String lid = destinationSystemKey.lID;
            String sysMsg = "[" + sysDesc + "," + lid + "]";
            if (status == null) {
                errMsg = "Destination system record not found: " + sysMsg;
            } else if (status.compareToIgnoreCase(SystemObject.STATUS_INACTIVE) == 0) {
                errMsg = "Destination system record has already been deactivated: "
                        + sysMsg;
            } else if (status.compareToIgnoreCase(SystemObject.STATUS_MERGED) == 0) {
                errMsg = "Destination system record has already been merged: "
                        + sysMsg;
            } else { // unrecognized status
                errMsg = "Destination system record not found: " + sysMsg;
            }
            throw new ProcessingException(mLocalizer.t("MSC571: Could not merge SystemObjects. " +
                                        "Record has been modified by another user: {0}", errMsg));
        }
        if (sourceSystemKey.equals(destinationSystemKey)) {
            String sysDesc = lookupSystemDefinition(con,
                    sourceSystemKey.systemCode).getDescription();
            String lid = sourceSystemKey.lID;
            String sysMsg = "[" + sysDesc + "," + lid + "]";
            throw new ProcessingException(mLocalizer.t("MSC572: Could not merge SystemObjects. " +
                                          "System object keys are equal.  SystemCode={0}, LID={1}", 
                                          sysDesc, lid));
        }
        if (newSO != null) {
            if (!newSO.getSystemCode().equals(destinationSystemKey.systemCode)
                    || !newSO.getLID().equals(destinationSystemKey.lID)) {
                String sysDesc = lookupSystemDefinition(con,
                        newSO.getSystemCode()).getDescription();
                throw new ProcessingException(mLocalizer.t("MSC573: Could not merge SystemObjects. " +
                                        "Destination SystemKey (SystemCode={0}, LID={1}) " +
                                        "does not match the SystemObject (SystemCode={2}, LID={3})",  
                                        destinationSystemKey.systemCode, destinationSystemKey.lID,
                                        sysDesc, newSO.getLID()));
            }
        }

        int flag = Constants.FLAG_UM_NONE;
        if (calculateOnly) {
            flag = Constants.FLAG_UM_CALC_ONLY;
        }

        // Determine if merge is being performed within a single EUID, or
        // two EUID's
        if (fromEUID.equals(toEUID)) {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("mergeSystemObject(): both system objects in EUID: "
                        + toEUID);
            }
            EnterpriseObject srcEO = mTrans.getEnterpriseObject(con, fromEUID);
            Object[] srcMatchFields = mMatchFieldChange.getMatchFields(srcEO);
            // If newSO is null, then use the current image of the system object
            if (newSO == null) {
                newSO = srcEO.getSystemObject(destinationSystemKey.systemCode,
                        destinationSystemKey.lID);
            }

            String user = getCallerUserId();

            // Check if the revision numbers match. srcRevisionNumber and
            // destRevisionNumber are identical. If srcRevisionNumber is null,
            // no checking is done. The calling methods must handle a null
            // srcRevisionNumber
            // correctly. For example, if the caller is expecting a
            // srcRevisionNumber,
            // then it needs to throw a UserException if a null
            // srcRevisionNumber
            // was passed in.
            // srcRevisionNumber must match the revision numbers retrieved from
            // the database. If they differ, it means that the SBR was modified
            // by another user. In this case, the merge should not continue
            // because
            // the current record in memory may be incorrect.
            if (destRevisionNumber != null && srcRevisionNumber != null) {
                Integer curRevisionNumber = srcEO.getSBR().getRevisionNumber();
                if (!srcRevisionNumber.equalsIgnoreCase(curRevisionNumber
                        .toString())) {
                    throw new UserException(mLocalizer.t("MSC574: Could not merge SystemObjects. " +
                                                         "Record has been modified by another user."));
                }
            }

            UpdateResult result = mUpdate.mergeSystem(con, srcEO,
                    sourceSystemKey.systemCode, sourceSystemKey.lID, newSO,
                    flag, user);
            mergeResult.setDestinationEO(result.getEnterpriseObject1());
            mergeResult.setSourceEO(result.getEnterpriseObject1());

            // Reevaluate duplicates if pessimistic mode on
            if (!calculateOnly && pessimisticModeEnabled) {
                transId = result.getTransactionResult().getTMID();
                if (performPessimisticMode) {
                    EnterpriseObject afterEO = result.getEnterpriseObject1();
                    execPessimistic(con, fromEUID, transId, srcMatchFields,
                            afterEO, null);
                }
            }
        } else {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("mergeSystemObject(): source system object in EUID: "
                            + fromEUID + " and destination system object in EUID: "
                            + toEUID);
            }
            EnterpriseObject sourceEO = mTrans.getEnterpriseObject(con,
                    fromEUID);
            Object[] sourceMatchFields = mMatchFieldChange
                    .getMatchFields(sourceEO);
            EnterpriseObject destinationEO = mTrans.getEnterpriseObject(con,
                    toEUID);
            // Check if the revision numbers match. If both srcRevisionNumber
            // or destRevisionNumber are null, no checking is done. The calling
            // methods must handle a null destRevisionNumber and
            // srcRevisionNumber
            // correctly. For example, if the caller is expecting a
            // destRevisionNumber,
            // then it needs to throw a UserException if a null
            // destRevisionNumber
            // was passed in.
            // srcRevisionNumber must match the revision numbers retrieved from
            // the database. If they differ, it means that the SBR was modified
            // by another user. In this case, the merge should not continue
            // because
            // the current source record in memory may be incorrect. The same is
            // also true for the destRevisionNumber.
            if (destRevisionNumber != null && srcRevisionNumber != null) {
                Integer curRevisionNumber = destinationEO.getSBR()
                        .getRevisionNumber();
                if (!destRevisionNumber.equalsIgnoreCase(curRevisionNumber
                        .toString())) {
                    throw new UserException(mLocalizer.t("MSC575: Could not merge SystemObjects. " +
                                                         "Destination record has been modified by another user."));
                }
                curRevisionNumber = sourceEO.getSBR().getRevisionNumber();
                if (!srcRevisionNumber.equalsIgnoreCase(curRevisionNumber
                        .toString())) {
                    throw new UserException(mLocalizer.t("MSC576: Could not merge SystemObjects. " +
                                                         "Source record has been modified by another user."));
                }
            }
            Object[] destinationMatchFields = mMatchFieldChange
                    .getMatchFields(destinationEO);
            if (newSO == null) {
                newSO = destinationEO.getSystemObject(
                        destinationSystemKey.systemCode,
                        destinationSystemKey.lID);
            }

            String user = getCallerUserId();

            UpdateResult result = mUpdate.mergeSystem(con, sourceEO,
                    destinationEO, sourceSystemKey.systemCode,
                    sourceSystemKey.lID, newSO, flag, user);
            EnterpriseObject afterDestEO = result.getEnterpriseObject1();
            EnterpriseObject afterSourceEO = result.getEnterpriseObject2();
            mergeResult.setDestinationEO(afterDestEO);
            mergeResult.setSourceEO(afterSourceEO);

            if (!calculateOnly) {
                transId = result.getTransactionResult().getTMID();
                if (afterSourceEO.isRemoved()) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("mergeSystemObject(): deleting potential duplicates "
                                        + "from inactive EUID: " + fromEUID);
                    }
                    mPotDup.deletePotentialDuplicates(con, fromEUID);
                } else {
                    // Reevaluate duplicates for 'from' record if
                    // pessimistic mode on
                    if (performPessimisticMode) {
                        execPessimistic(con, fromEUID, transId,
                                sourceMatchFields, afterSourceEO, null);
                    }
                }

                // Reevaluate duplicates for 'to' record if pessimistic mode on
                if (performPessimisticMode) {
                    SBR afterToSBR = result.getEnterpriseObject1().getSBR();
                    // If the afterSourceEO is removed, that EUID should be
                    // ignored
                    // when recalculating potential duplicates for the 'to'
                    // record.
                    // If it still has at least one SO, it may still be a
                    // potential
                    // duplicate, so it must be included when recalculating
                    // potential duplicates.
                    if (afterSourceEO.isRemoved()) {
                        execPessimistic(con, toEUID, transId,
                                destinationMatchFields, afterDestEO, fromEUID);
                    } else {
                        execPessimistic(con, toEUID, transId,
                                destinationMatchFields, afterDestEO, null);
                    }
                }
            }
        }
        if (!calculateOnly) {
            mergeResult.setTransactionObject(mTrans.findTransactionLog(con,
                    transId));
            mOutBoundSender.send(OutBoundMessages.MRG, transId, mergeResult);
        }
        return mergeResult;
    }

    /**
     * Release JDBC connection (return to pool) and JMS resources.
     * 
     * @param con
     *            Connection
     */
    public void releaseResources(Connection con) {
        try {
            if (con != null) {
                con.close();
                con = null;
            }
            mOutBoundSender.release();
        } catch (SQLException e) {
            mLogger.warn(mLocalizer.x("MSC012: MasterControllerCoreImpl could not close JDBC connection: {0}", 
                                       e.getMessage()));
        } catch (OutBoundException e) {
            mLogger.warn(mLocalizer.x("MSC013: MasterControllerCoreImpl could not release JMS resources: {0}", 
                                       e.getMessage()));
        }
    }

    private void rollback(Connection con) {
        try {
            if (con != null) {
                con.rollback();
            }
        } catch (SQLException e) {
            mLogger.severe(mLocalizer.x("MSC014: MasterControllerCoreImpl could not rollback transaction: {0}", e.getMessage()));
        }
    }

    /**
     * commit a transaction.
     * 
     * @param con
     *            Connection
     */
    private void commit(Connection con) {
        try {
            if (con != null) {
                con.commit();
            }
        } catch (SQLException e) {
            mLogger.severe(mLocalizer.x("MSC015: MasterControllerCoreImpl could not commit transaction: {0}", e.getMessage()));
        }
    }

    /**
     * Ensure system object status is valid.
     * 
     * @param status
     *            Status to be validated
     * @throws ProcessingException
     *             An error occured.
     */
    private void validateSystemObjectStatus(String status)
            throws ProcessingException {
        if (!status.equals(SystemObject.STATUS_ACTIVE)
                && !status.equals(SystemObject.STATUS_INACTIVE)
                && !status.equals(SystemObject.STATUS_MERGED)) {
            throw new ProcessingException(mLocalizer.t("MSC577: validateSystemObjectStatus() failed. " +
                                                       "The status is invalid: {0}", status));
        }
    }

    /**
     * Run object node through validation service
     * 
     * @param objNode
     *            object to be validated
     * @throws UserException
     *             object failed validation
     */
    private void validateObjectNode(ObjectNode objNode)
            throws ValidationException {
        if (Constants.VALIDATION) {
            ValidationRuleRegistry reg = ValidationRuleRegistry.getInstance();
            ObjectDescriptor odsc = reg.getObjectDescriptor(objNode.pGetTag());
            odsc.validate(objNode);
        }
    }

    /**
     * Run system object through validation service
     * 
     * @param con
     *            Connection
     * @param sysObj
     *            object to be validated
     * @throws ValidationException
     *             object failed validation
     */
    private void validateSystemObject(Connection con, SystemObject sysObj)
            throws ProcessingException, ValidationException {
        String sysCode = null;
        String lid = null;
        if (Constants.VALIDATION) {
            try {
                sysCode = sysObj.getSystemCode();
                lid = sysObj.getLID();
            } catch (Exception e) {
                throw new ProcessingException(mLocalizer.t("MSC578: validateSystemObject() failed. " +
                                                           "The SystemCode or LID could not be retrieved: {0}"));
            }
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("validateSystemObject(): validating system object ["
                                + sysCode + ", " + lid + "]");
            }

            ValidationRuleRegistry reg = ValidationRuleRegistry.getInstance();
            ObjectValidator lidValidator = reg
                    .getCustomValidatorByObject("SystemObject");
            if (lidValidator != null) {
                try {
                    lidValidator.validate(sysObj);
                } catch (ValidationException v) {
                    String id = sysObj.getLID();
                    String sysCode1 = sysObj.getSystemCode();

                    SystemDefinition def = lookupSystemDefinition(con, sysCode1);
                    String systemDesc = null;
                    if (def != null) {
                        systemDesc = def.getDescription();
                    }
                    String format = v.getFormat();

                    throw new ValidationException(mLocalizer.t("MSC579: validateSystemObject() encountered a format validation " + 
                                    "error. The ID {0} does not conform to the format of SystemObject[LocalId] for "
                                    + "the system description {1}, which is {2}", id, systemDesc, format));
                }
            }
            ObjectNode objNode = sysObj.getObject();
            ObjectDescriptor odsc = reg.getObjectDescriptor(objNode.pGetTag());
            try {
                odsc.validate(objNode);
            } catch (ValidationException e) {
                // Retrieve the system definition and use it for the
                // error message.
                SystemDefinition sd = lookupSystemDefinition(con, sysCode);
                String desc = sd.getDescription();
                throw new ValidationException(mLocalizer.t("MSC580: validateSystemObject() encountered a format validation " + 
                                    "error for SystemDescription={0}, LID={1}: {2}", desc, lid, e));
            }
        }
    }

    /**
     * Run enterprise object through validation service
     * 
     * @param con
     *            Connection
     * @param entObj
     *            object to be validated
     * @throws UserException
     *             object failed validation
     */
    private void validateEnterpriseObject(Connection con,
            EnterpriseObject entObj)

    throws ProcessingException, UserException {
        if (Constants.VALIDATION) {
            try {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("validateEnterpriseObject(): validating EUID "
                                    + entObj.getEUID());
                }
            } catch (Exception e) {
                throw new ProcessingException(mLocalizer.t("MSC581: validateEnterpriseObject() could not retrieve " +
                                                           "the EUID for an EnterpriseObject: {0}", e));
            }
            Collection sysObjs = entObj.getSystemObjects();
            if (sysObjs != null) {
                ValidationRuleRegistry reg = ValidationRuleRegistry
                        .getInstance();
                ObjectNode objNode = entObj.getSBR().getObject();
                ObjectDescriptor odsc = reg.getObjectDescriptor(objNode
                        .pGetTag());
                Iterator sysObjIterator = sysObjs.iterator();
                while (sysObjIterator.hasNext()) {
                    SystemObject sysObj = (SystemObject) sysObjIterator.next();
                    validateSystemObject(con, sysObj);
                }
            }
        }
    }

    private void throwProcessingException(Exception e)
            throws ProcessingException {

        sendAlert(e.getClass().getName() + ": " + e.getMessage());
        throw new ProcessingException(mLocalizer.t("MSC582: MasterControllerImpl encountered an ProcessingException: {0}", e));
    }

    private synchronized void setMBeanServer() {
        List srvrList = MBeanServerFactory.findMBeanServer(null);
        Iterator slist = srvrList.iterator();
        while (slist.hasNext()) {
            MBeanServer mBeanServer = (MBeanServer) slist.next();
            if (mBeanServer.getDefaultDomain().equals("DefaultDomain")) {
                mMBeanServer = mBeanServer;
                break;
            }
        }
        if (mMBeanServer == null) {
            mLogger.severe(mLocalizer.x("MSC017: Could not find the mBeanServer."));
        }

        registerMbean();
    }

    private void registerMbean() {

        if (mMBeanServer != null) {
            Agent a = new Agent(mMBeanServer, this);

            a.registerMBean(objectName);
        }

    }

    private void sendAlert(String message) {
        try {
            if (mMBeanServer != null) {
                Object obj[] = { "MasterController", message };
                String sig[] = { "java.lang.String", "java.lang.String" };
                mMBeanServer.invoke(mMBeanObjectName, "logAlert", obj, sig);
            }
        } catch (Exception ex) {
            mLogger.warn(mLocalizer.x("MSC018: sendAlert(): error sending message: {0}", ex.getMessage()));
        }
    }

    private void sendCriticalError(String message) {
        try {
            if (mMBeanServer != null) {
                Object obj[] = { "MasterController", message };
                String sig[] = { "java.lang.String", "java.lang.Exception" };
                mMBeanServer.invoke(mMBeanObjectName, "logCriticalError", obj,
                        sig);
            }
        } catch (Exception ex) {
            mLogger.warn(mLocalizer.x("MSC019: sendCriticalError(): error sending message: {0}", ex.getMessage()));
        }
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

    private static String TMP_USER = "eGate";

    private String getCallerUserId() {
        java.security.Principal principal = context.getCallerPrincipal();
        String user = principal.getName();
        if (user == null || user.trim().length() == 0) {
            return TMP_USER;
        } else if (user.equalsIgnoreCase("GUEST")
                || user.equalsIgnoreCase("ANONYMOUS")
                || user.indexOf("UNAUTHENTICATED") > -1) {
            return TMP_USER;
        }
        return user;
    }

    private String getMBeanObjectNameStr() throws ProcessingException {

        String mBeanObjectNameStr = null;
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(
                MBEAN_PROP_FILE);
        if (is != null) {
            Properties mBeanProp = new Properties();
            try {
                mBeanProp.load(is);
            } catch (IOException e) {
                throwProcessingException(e);
            }

            mBeanObjectNameStr = mBeanProp.getProperty("eViewAppMBeanName");
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("AppMBean object name is : "
                                + mBeanObjectNameStr);
            }
        } else {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("getMBeanObjectNameStr(): " + MBEAN_PROP_FILE
                        + "not found");
            }
        }
        return mBeanObjectNameStr;
    }

    public String getDatabaseStatus() throws ProcessingException, UserException {

        return ConnectionUtil.pingDatabase();
    }

    /**
     * Start a new transaction.
     * 
     * @exception ProcessingException
     */
    public void beginTransaction() throws ProcessingException {

        try {
            if (transactionType.equals("BMT_XA")) {
                utx.begin();
            }
        } catch (javax.transaction.NotSupportedException e) {
            throwProcessingException(e);
        } catch (javax.transaction.SystemException e) {
            throwProcessingException(e);
        }
    }

    /**
     * Commit current transaction.
     * 
     * @param con
     *            database connection
     * @exception ProcessingException
     */
    public void commitTransaction(java.sql.Connection con)
            throws ProcessingException {
        try {
            if (transactionType.equals("CMT_XA")) {
                // do nothing container will take care of this
            } else if (transactionType.equals("BMT_XA")) {
                utx.commit();
            } else {
                commit(con);
            }
        } catch (javax.transaction.RollbackException e) {
            throwProcessingException(e);
        } catch (javax.transaction.HeuristicMixedException e) {
            throwProcessingException(e);
        } catch (javax.transaction.HeuristicRollbackException e) {
            throwProcessingException(e);
        } catch (javax.transaction.SystemException e) {
            throwProcessingException(e);
        }
    }

    /**
     * Rollback current transaction.
     * 
     * @param con
     *            database connection
     * @exception ProcessingException
     */
    public void rollbackTransaction(java.sql.Connection con)
            throws ProcessingException {
        try {

            if (transactionType.equals("CMT_XA")) {
                context.setRollbackOnly();
            } else if (transactionType.equals("BMT_XA")) {
                utx.rollback();
            } else {
                rollback(con);
            }

        } catch (javax.transaction.SystemException e) {
            throwProcessingException(e);
        }
    }

    /**
     * Flag to indicate whether it is transactional mode.
     * 
     * @return true or false depending on the transactional flag.
     */
    private boolean isTransactional() {
        return mIsTransactional;
    }

    /**
     * Set whtether it is in transacitonal mode.
     * 
     * @param mIsTransactional
     *            specify whether it is in transactional mode.
     */
    public void setTransactionMode(boolean mIsTransactional) {
        this.mIsTransactional = mIsTransactional;
    }

    public void setTransactionType(String type) {
        this.transactionType = type;
    }

    public void setObjectName(String objectName) {
        this.objectName=objectName;     
    }
    
    /**
     *  Retrieve the potential duplicate threshold.
     *
     * @returns the value of the potential duplicate threshold.
     */
    public float getDuplicateThreshold()  {
        return mDuplicateThreshold;
    }

    /**
     *  Retrieve the Assumed Match threshold.
     *
     * @returns the value of the Assumed Match threshold.
     */
    public float getAssumedMatchThreshold() {
        return mAssumedMatchThreshold;
    }
    
    // for SBROverriding
    /** Updates SBR by collecting the values from MAP to the SBR that specified by EUID.
    *
    * @param mapSystems The Map consists of epath as key and System as value from which the filed should take for updating SBR
    * @param euid The EUID of SBR on which the updation of SBR to perform.
    *
    */
    public void updateSBR(Map mapSystems, EnterpriseObject eo, boolean removalFlag)
            throws ProcessingException, UserException {
        mCalculator.updateSBR(mapSystems, eo, removalFlag);
    }
    
    /** Returns a map with (fieldName, actual value for link) for the given EO.
    *
    * @param eo The EnterpriseObject that has LINKs
    * @return resultMap map with (fieldName, actual value for link) for the given EO.
    * @throws ObjectException An error occured.
    * @throws ConnectionInvalidException An error occured.
    * @throws OPSException An error occured.
    *
    */
    public Map getLinkValues(EnterpriseObject eo, Connection conn) 
            throws ObjectException, ConnectionInvalidException, OPSException {
            
            Map resultMap = null;
            resultMap = mCalculator.getLinkValues(eo, conn);
            
            return resultMap;
    }
}
