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
package com.sun.mdm.index.outbound;

import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.util.JNDINames;
import com.sun.mdm.index.util.Localizer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.jms.TextMessage;

import javax.naming.InitialContext;
import java.util.logging.Level;
import net.java.hulp.i18n.Logger;
import javax.naming.NamingException;


/**
 * Publish actions as events to a Topic.
 * Currently we are not handling the case when a JMS server is down.
 * We rely on optimistic case and persume that JMS server is always up.
 * In the next version, we will handle failover case.
 * @author  sdua
 */
public class OutBoundSender {

    private transient final Localizer mLocalizer = Localizer.get();

    /** event key that is published to Topic header. */
    public static final  String EVENT = "event";
    private InitialContext mInitCtx;

    private String mJndiConnectionFactory = null;;
    private TopicConnection mTopicConnection = null;
    private String mJndiTopic = null;
    private boolean mOutboundOn = false;
    
    /** topic connection factory */
    private TopicConnectionFactory mTopicConnectionFactory = null;
    
    /** topic session */
    private TopicSession mTopicSession = null;
    
    /** topic publisher */
    private TopicPublisher mTopicPublisher = null;
    
    /** in transact mode */
    private boolean mInTransactMode = false;
    
    /** session ready flag */
    private boolean mIsSessionReady = false;
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    
    /** Creates a new instance of OutBoundPublisher */
    public OutBoundSender(){
    }

    /**
     * Publish the eventType and its enterprise object information to a topic
     * @param eventType values are UPD, ADD, MRG, UNMRG, DEA, REA
     * @param id the id
     * @param EnterpriseObject an enterprise object that is used to construct xml  
     */
    public void send(String eventType, String id, EnterpriseObject eo) throws OutBoundException {
        if (!mOutboundOn) {
            return;
        }
        ObjectNodeXML msgBuilder = new ObjectNodeXML();
        String xml = msgBuilder.constructXml(eventType, id, eo);
        send(eventType, xml);
    }

    /**
     * Publish the eventType and two EnterpriseObject to a topic
     * @param eventType values are UPD, ADD, MRG, UNMRG, DEA, REA
     * @param id the id
     * @param mresult Pairs of SBRs are extracted from  mresult and  is stringified to xml
     *    and is sent to the topic.
     */
    public void send(String eventType, String id, MergeResult mresult) throws OutBoundException {
        this.send(eventType, id, mresult.getSourceEO(), mresult.getDestinationEO());
    }
    
    /**
     * Publish the eventType and two EnterpriseObject to a topic
     * @param eventType values are UPD, ADD, MRG, UNMRG, DEA, REA
     * @param id the id
     * @param enterpriseObj1 first enterprise object to be stringified to xml
     * @param enterpriseObj2 second enterprise object to be stringified to xml
     *    and is sent to the topic.
     */
    public void send(String eventType, String id, 
        EnterpriseObject enterpriseObj1, EnterpriseObject enterpriseObj2) throws OutBoundException {
        
        if (!mOutboundOn) {
            return;
        }
                      
        ObjectNodeXML msgBuilder = new ObjectNodeXML();
        String xml = msgBuilder.constructMrgXml(eventType, id, enterpriseObj1, enterpriseObj2);
        send(eventType, xml);
    }
    
    /**
     * Send event and data to JMS
     * @param eventType the event type
     * @param data the data to be sent
     */
    private void send(String eventType, String data) throws OutBoundException {
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Sending Event= " + eventType + " Data = " + data);
        }
        
        try {
            // To support transaction, session has to be created after UserTransaction.begin()
            if (!mIsSessionReady) {
                mTopicConnection = mTopicConnectionFactory.createTopicConnection();
                mTopicSession = mTopicConnection.createTopicSession(isInTransactMode(), Session.AUTO_ACKNOWLEDGE);
        if (mTopicSession == null) {
            return;
        }
                Topic myTopic = mTopicSession.createTopic(mJndiTopic);
                mTopicPublisher = mTopicSession.createPublisher(myTopic);
                mIsSessionReady = true;
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Topic session is ready!");
                }
            }
            TextMessage msg = mTopicSession.createTextMessage(data);
            mTopicPublisher.publish(msg);
        } catch (JMSException e) {
            throw new OutBoundException(mLocalizer.t("OUT504: Could not send " + 
                                    "an outbound message: {0}", e));
        }
    }

    /**
     * Initialize the OutBound Sender
     * @param connectionFactoryName JNDI name of the topic connection factory 
     * @param topicName JNDI name of the topic 
     */
    public void initialize(String connectionFactoryName, String topicName) throws OutBoundException {
        if (connectionFactoryName == null) {
            throw new OutBoundException(mLocalizer.t("OUT505: JNDI for outbound " + 
                                    "ConnectionFactory was not specified."));
        }
        if (topicName == null) {
            throw new OutBoundException(mLocalizer.t("OUT506: JNDI for outbound " + 
                                    "Topic was not specified."));
        }
        
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("JNDI for outbound ConnectionFactory = " + connectionFactoryName);
            mLogger.fine("JNDI for outbound Topic = " + topicName);
        }
        
        mJndiConnectionFactory = connectionFactoryName;
        mJndiTopic = topicName;
        
        try {
            mInitCtx = new InitialContext();
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Outbound: Lookup TopicConnectionFactory using " + mJndiConnectionFactory);
            }
            
            mTopicConnectionFactory = (TopicConnectionFactory) mInitCtx.lookup(mJndiConnectionFactory);
        } catch (NamingException e) {
            throw new OutBoundException(mLocalizer.t("OUT507: Failed to locate " + 
                                    "the topic connection factory: {0}", e));
        }
        
        mOutboundOn = true;
        
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Sender is initialized");
        }
    }
    
    /**
     * Initialize the OutBound Sender
     */
    public void initialize(String topicName) throws OutBoundException {
        if (topicName == null) {
            throw new OutBoundException(mLocalizer.t("OUT508: JNDI for outbound " + 
                                    "Topic was not specified."));
        }
        mJndiTopic = topicName;
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("JNDI for outbound Topic = " + topicName);
        }
        try {
            mInitCtx = new InitialContext();
            
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Outbound: Lookup TopicConnectionFactory using " + JNDINames.OUTBOUND_TOPIC_CONN_FACTORY);
            }
                        
            mTopicConnectionFactory = (TopicConnectionFactory) mInitCtx.lookup(JNDINames.OUTBOUND_TOPIC_CONN_FACTORY); 
        } catch (NamingException e) {
            throw new OutBoundException(mLocalizer.t("OUT509: Failed to locate " + 
                                    "the topic connection factory: {0}", e));
        }       
        mOutboundOn = true;
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Sender is initialized");
        }
    }
    
    /**
     * Release connections
     */
    public void release() throws OutBoundException {
        try {
            if (mTopicPublisher != null) {
                mTopicPublisher.close();
                mTopicPublisher = null;
            }
            if (mTopicSession != null) {
                mTopicSession.close();
                mTopicSession = null;
            }
			if (mTopicConnection != null) {
				mTopicConnection.close();
				mTopicConnection = null;
			}
			if (mLogger.isLoggable(Level.FINE)) {
				mLogger.fine("Topic publishing resources released");
			}
            mIsSessionReady = false;
        } catch (JMSException e) {
            throw new OutBoundException(mLocalizer.t("OUT510: JMS operations failed: {0}", e));
        }
        
    }
       
    public boolean isInTransactMode() {
        return mInTransactMode;
    }
    
    public void setInTransactMode(boolean mInTransactMode) {
        this.mInTransactMode = mInTransactMode;
    }
}
