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
package com.sun.mdm.index.master.search.merge;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

/**
 * Merge history node
 */
public class MergeHistoryNode implements java.io.Serializable {
    
    /** EUID
     */    
    private String mEUID;
    /** Transaction Object
     */    
    private TransactionObject mTransactionObject;
    /** Parent node
     */    
    private MergeHistoryNode mParentNode;
    /** Destination node
     */    
    private MergeHistoryNode mDestinationNode;
    /** Source node
     */    
    private MergeHistoryNode mSourceNode;

    // logger    
    private Logger mLogger = LogUtil.getLogger(this);
    
    /** Creates a new instance of MergeHistoryNode */
    public MergeHistoryNode() { }

    /** Constructor
     * @param destEUID destination EUID
     * @param sourceEUID source EUID
     */    
    public MergeHistoryNode(String destEUID, String sourceEUID) {
        mEUID = destEUID;
        mDestinationNode = new MergeHistoryNode();
        mDestinationNode.setEUID(destEUID);
        mDestinationNode.setParentNode(this);
        mSourceNode = new MergeHistoryNode();
        mSourceNode.setEUID(sourceEUID);
        mSourceNode.setParentNode(this);
    }
    
    /** Set EUID
     * @param euid EUID
     */    
    public void setEUID(String euid) {
        mEUID = euid; 
    }
    /** Set parent node
     * @param parentNode Parent node
     */    
    public void setParentNode(MergeHistoryNode parentNode) {
        mParentNode = parentNode;
    }
    /** Set destination node
     * @param destinationNode Destination node
     */    
    public void setDestinationNode(MergeHistoryNode destinationNode) {
        mDestinationNode = destinationNode;
    }
    /** Set source node
     * @param sourceNode Source node
     */    
    public void setSourceNode(MergeHistoryNode sourceNode) {
        mSourceNode = sourceNode;
    }
    /** Set transaction object
     * @param transactionObject Transaction object
     */    
    public void setTransactionObject(TransactionObject transactionObject) {
        mTransactionObject = transactionObject;
    }   
    /** Get EUID
     * @return EUID
     */    
    public String getEUID() {
        return mEUID;
    }
    /** Get transaction object
     * @return Transaction Object
     */    
    public TransactionObject getTransactionObject() {
        return mTransactionObject;
    }
    /** Get parent node
     * @return Parent node
     */    
    public MergeHistoryNode getParentNode() {
        return mParentNode;
    }
    /** Get destination node
     * @return Destination node
     */    
    public MergeHistoryNode getDestinationNode() {
        return mDestinationNode;
    }
    /** Get source node
     * @return Source node
     */    
    public MergeHistoryNode getSourceNode() {
        return mSourceNode;
    }    
    /** Output in readable format
     * @param indent size of indent */    
    public void prettyPrint(int indent) {
        for (int i = 0; i < indent; i++) {
            mLogger.debug("  ");
        }
        String transId = null;
        if (mTransactionObject != null) {
            try {
                transId = mTransactionObject.getTransactionNumber();
            } catch (Exception e) {
                transId = "error: " + e.getMessage();
            }
        }
        if (transId != null) {
            mLogger.debug(mEUID + " [trans: " + transId + "]");
        } else {
            mLogger.debug(mEUID);
        }
        if (mDestinationNode != null) {
            mDestinationNode.prettyPrint(indent + 1);
        }
        if (mSourceNode != null) {
            mSourceNode.prettyPrint(indent + 1);
        }
    }
        
}
