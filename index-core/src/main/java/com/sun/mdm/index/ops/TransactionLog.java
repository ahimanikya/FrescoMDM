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
package com.sun.mdm.index.ops;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.ObjectKey;
import com.sun.mdm.index.ops.exception.OPSException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.Externalizable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.exception.DuplicateChildKeyException;
import com.sun.mdm.index.objects.epath.EPathParser;
import com.sun.mdm.index.objects.epath.EPathException;

import java.util.StringTokenizer;

import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * @author gzheng
 * Transaciton Log record class: 1. path: stores the location of the change in
 * the object 2. function: type of changes applied to the object 3. value:
 * existing value prior the change *: if function type is REMOVE, value stores
 * the whole content of removed object *: if funciton type is UPDATE, value
 * stores previous value of the changed content *: if function type is ADD,
 * value is not used
 */
public final class TransactionLog implements Externalizable {
    static final long serialVersionUID = -7193031496197804611L;
    public static final int version = 1;

    /**
     * int value indicates REMOVE function
     */
    public static final int FUNCTION_REMOVE = 1;

    /**
     * int value indicates ADD function
     */
    public static final int FUNCTION_ADD = 2;

    /**
     * int value indicates UPDATE function
     */
    public static final int FUNCTION_UPDATE = 3;
    private int mFunction;
    private String mPath;
    private Object mValue;

    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.ops.TransactionLog");
    private transient static final Localizer mLocalizer = Localizer.get();
    
    /**
     * default constructor
     */
    public TransactionLog() {
    }

    /**
     * Constructor
     *
     * @param path String for object path.
     * @param function Type of change.
     * @param value Existing value needs to be logged.
     */
    public TransactionLog(String path, int function, Object value) {
        mPath = path;
        mFunction = function;
        mValue = value;
    }


    /**
     * Get key search string.
     * @param node object node.
     * @return key search string. 
     */
     private static String getKeySearchString(ObjectNode node) {
        String idx = null;
        
        try {
            ObjectKey key = node.pGetKey();
            if (key != null) {
                idx = "";
                ArrayList names = key.getKeyNames();
                ArrayList values = key.getKeyValues();
                for (int j = 0; j < names.size(); j++) {
                    String name = (String) names.get(j);
                    Object value = EPathAPI.maskString(values.get(j));
                    
                    if (j == 0) {
                        idx += "@" + name + "=" + value;
                    } else {
                        idx += ",@" + name + "=" + value;
                    }
                }                                
            }
        } catch (ObjectException ex) {
            mLogger.warn(mLocalizer.x("OPS011: Encountered an error while retrieving the Key Search string: {0}", ex.getMessage()));
        }

        return idx;
    }
    

    /**
     * Consolidates changes logs carried inside ObjectNode by its context.
     *
     * @param context The context.
     * @param node ObjectNode from which to retrieve the logs.
     * @return an ArrayList of TransactionLogs found inside node parameter.
     */
    public static ArrayList getLogs(String context, ObjectNode node) {
        String head = (context.equals("")) ? "" : (context + ".");

        ArrayList ret = null;
        
        if (node.isRemoved()) {
            TransactionLog log = new TransactionLog();
            log.setPath(head + node.pGetTag());
            log.setFunction(TransactionLog.FUNCTION_REMOVE);
            log.setValue(node);
            
            ret = new ArrayList();
            ret.add(log);
        } else {
            ArrayList upd = node.pGetFieldUpdateLogs();
            if (upd != null) {
                ret = new ArrayList();
                ret.addAll(upd);
            }
            
            if (null != ret) {
                for (int i = 0; i < ret.size(); i++) {
                    TransactionLog log = (TransactionLog) ret.get(i);
                    String path = log.getPath();
            
                    if (!path.startsWith("Enterprise.")) {
                        log.setPath(head + log.getPath());
                    }
                }
            }
            
            ArrayList children = node.pGetChildren();
            
            if (null != children) {
                HashMap countMap = new HashMap();
            
                for (int i = 0; i < children.size(); i++) {
                    ObjectNode n = (ObjectNode) children.get(i);
            
                    if (n.isRemoved()) {
                        int count = 0;
            
                        if (countMap.containsKey(n.pGetTag())) {
                            count = ((Integer) (countMap.get(n.pGetTag()))).intValue();
                            countMap.put(n.pGetTag(), new Integer(--count));
                        }

                        String idx = getKeySearchString(n);
                                    
                        TransactionLog log = new TransactionLog();
                        if (idx != null) {
                            log.setPath(head + n.pGetTag() + "[" + idx + "]");
                        } else {
                            log.setPath(head + n.pGetTag());
                        }
                        log.setFunction(TransactionLog.FUNCTION_REMOVE);
                        log.setValue(n);
            
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("Log path is: " + log.getPath());
                            mLogger.fine("Log function is: " + log.getFunction());
                        }
                        
                        if (null == ret) {
                            ret = new ArrayList();
                        }
            
                        ret.add(log);
                    } else if (n.isAdded()) {
                        int count = 0;

                        if (countMap.containsKey(n.pGetTag())) {
                            count = ((Integer) (countMap.get(n.pGetTag()))).intValue();
                            countMap.put(n.pGetTag(), new Integer(++count));
                        } else {
                            countMap.put(n.pGetTag(), new Integer(count));
                        }

                        String idx = getKeySearchString(n);

                        TransactionLog log = new TransactionLog();
                        if (idx != null) {
                            log.setPath(head + n.pGetTag() + "[" + idx + "].*");
                        } else {
                            // Convert indices to objectId access where possible.
                            // This is used for updating unkeyed child objects.
                            try {
                                String objectId = n.getObjectId();
                                if (objectId != null) {
                                    log.setPath(head + n.pGetTag() + "[@ObjectId=" + objectId + "].*");
                                } else {
                                    log.setPath(head + n.pGetTag() + "[" + count + "].*");
                                }
                            } catch (ObjectException e) {
                                // this should never occur
                                log.setPath(head + n.pGetTag() + "[" + count + "].*");
                            }
                            
                        }
                        log.setFunction(TransactionLog.FUNCTION_ADD);
                        log.setValue(null);

                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("Log path is: " + log.getPath());
                            mLogger.fine("Log function is: " + log.getFunction());
                        }

                        if (null == ret) {
                            ret = new ArrayList();
                        }

                        ret.add(log);
                    } else {
                        int count = 0;

                        if (countMap.containsKey(n.pGetTag())) {
                            count = ((Integer) (countMap.get(n.pGetTag()))).intValue();
                        }

                        String idx = getKeySearchString(n);

                        ArrayList r = null;
                        if (idx != null) {
                            r = TransactionLog.getLogs(head + n.pGetTag() + "[" + idx + "]", n);
                        } else {
                            // Convert indices to objectId access where possible.
                            // This is used for updating unkeyed child objects.
                            try {
                                String objectId = n.getObjectId();
                                if (objectId != null) {
                                    r = TransactionLog.getLogs(head + n.pGetTag() + "[@ObjectId=" + objectId + "]", n);
                                } else {
                                    r = TransactionLog.getLogs(head + n.pGetTag() + "[" + count + "]", n);
                                }
                            } catch (ObjectException e) {
                                // this should never occur
                                r = TransactionLog.getLogs(head + n.pGetTag() + "[" + count + "]", n);
                            }
                        }

                        if (null != r) {
                            if (null == ret) {
                                ret = new ArrayList();
                            }

                            ret.addAll(r);
                        }
                    }
                }
            }
        }
                    
        return ret;
    }

    /**
     * Retreves an array of TransactionLog(s) from a blob.
     *
     * @param blob Blob containing TransactionLog objects.
     * @throws OPSException if an error occurred.
     * @return array of TransactionLog objects.
     */
    public static TransactionLog[] fromBlob(String blob)
            throws OPSException {
        TransactionLog[] ret = null;

        try {
            ByteArrayInputStream buf = new ByteArrayInputStream(blob.getBytes(
                        "ISO-8859-1"));
            ObjectInputStream in = new ObjectInputStream(buf);
            ret = (TransactionLog[]) in.readObject();
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS635: Could not retrieve " + 
                                        "Transaction Logs from the Blob: {0}", ex));
        }
        return ret;
    }

    /**
     * Serializes an array of TransactionLog(s) to a String.
     *
     * @param logs Array of TransactionLog objects.
     * @return resulting blob string for the TransactionLog set.
     * @throws OPSException if an error occurred.
     */
    public static String toBlob(TransactionLog[] logs)
            throws OPSException {
        try {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buf);
            out.writeObject(logs);
            out.close();

            return buf.toString("ISO-8859-1");
        } catch (java.io.IOException ex) {
            throw new OPSException(mLocalizer.t("OPS636: Could not serialize " + 
                                        "Transaction Logs to a String: {0}", ex));
        }
    }

    /**
     * Getter for mFunction attribute
     *
     * @return Value for the mFunction attribute.
     */
    public int getFunction() {
        return mFunction;
    }

    /**
     * Getter for mPath attribute.
     *
     * @return Value for the mPath attribute..
     */
    public String getPath() {
        return mPath;
    }

    /**
     * Getter for Value attribute
     *
     * @return Value for the mValue attribute.
     */
    public Object getValue() {
        return mValue;
    }

    /**
     * Setter for mFunction attribute.
     *
     * @param function Value to set for the mFunction attribute.
     */
    public void setFunction(int function) {
        mFunction = function;
    }

    /**
     * Setter for Path attribute.
     *
     * @param path Value to set for the mPath attribute.
     */
    public void setPath(String path) {
        mPath = path;
    }

    /**
     * Setter for mValue attribute.
     *
     * @param value Value to set for the mValue attribute.
     */
    public void setValue(Object value) {
        mValue = value;
    }

    /**
     * Checks if another TransactionLog instance equals this instance.
     * 
     * @param log TransactionLog object to check.
     * @return boolean whether the two objects are considered same.
     */
    public boolean equals(Object log) {
        boolean ret = false;
        
       if (log instanceof TransactionLog 
            && equals(((TransactionLog) log).getPath(), this.getPath()) == true 
            && ((TransactionLog) log).getFunction() == this.getFunction()) {
            ret = true;
        }
        
        return ret;
    }
    /** Checks if the last elements of two paths are identical.
     *
     *  @param path1 First path to check.
     *  @param path2 Second path to check.
     *  @returns true if they last elements of both paths are identical
     */
    public boolean equals (String path1, String path2)  {
        if (path1 == null && path2 == null)  {
            return true;
        }
        if (path1 == null & path2 != null)  {
            return false;
        }
        if (path1 != null & path2 == null)  {
            return false;
        }
        int pos1 = path1.lastIndexOf(".");
        if (pos1 == -1)  {
            pos1 = 0;
        } else {
            pos1 += 1;  // skip the "."
        }
        String element1 = path1.substring(pos1);
        int pos2 = path2.lastIndexOf(".");
        if (pos2 == -1)  {
            pos2 = 0;
        } else {
            pos2 += 1;  // skip the "."
        }
        String element2 = path2.substring(pos2);
        
        if (element1.compareTo(element2) == 0)  {
            return true;
        }  else  {
            return false;
        }
    }
    
    /**
     * Hash code.
     * @return hash code.
     */
    public int hashCode() {
        return super.hashCode();
    }
    
     
    /**
     * prints Transactionlog to String for output
     *
     * @return String
     */
    public String toString() {
        String ret = null;

        switch (mFunction) {
        case FUNCTION_REMOVE:
            ret = mPath + "(Remove)" + mValue;

            break;

        case FUNCTION_ADD:
            ret = mPath + "(Add)" + mValue;

            break;

        case FUNCTION_UPDATE:
            ret = mPath + "(Update)" + mValue;

            break;
        }

        return ret;
    }
    
    /**
     * apply delta to enterprise object
     * @param eo enterprise object
     * @param delta delta object
     * @return EnterpriseObject enterprise object
     */
    public static EnterpriseObject applyDelta(EnterpriseObject eo, Object delta) 
            throws ObjectException, EPathException {
            
        EnterpriseObject ret = null;
       
        try {
            
            if (delta != null) {
                if (eo == null) {
                    ArrayList list = (ArrayList) delta;
                    TransactionLog log = (TransactionLog) list.get(0);
                    
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("Recovering complete enterprise object.");
                        mLogger.fine("     log path is: " + log.getPath());
                        mLogger.fine("     log value is: " + log.getValue());
                        switch (log.getFunction()) {
                        case TransactionLog.FUNCTION_UPDATE:
                            mLogger.fine("     log function is: update");
                            break;
                        case TransactionLog.FUNCTION_REMOVE:
                            mLogger.fine("     log function is: remove");
                            break;
                        case TransactionLog.FUNCTION_ADD:
                            mLogger.fine("     log function is: add");
                            break;
                        }
                    }
                    
                    ret = (EnterpriseObject) log.getValue(); 
                    ret.resetAll();
                } else {
                    ArrayList list = (ArrayList) delta;
                    ArrayList nonKeyedChildToRemoveList = new ArrayList();
                    int nonKeyedChildRemoveCount = 0;
                    
                    for (int i = 0; i < list.size(); i++) {
                        TransactionLog log = (TransactionLog) list.get(i);
                        int f = log.getFunction();
                        Object v = log.getValue();
                        String p = log.getPath();
                    
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("     Log path is: " + p);
                            mLogger.fine("     Log value is: " + v);
                            switch (f) {
                            case TransactionLog.FUNCTION_UPDATE:
                                mLogger.fine("     Log function: update");
                                break;
                            case TransactionLog.FUNCTION_REMOVE:
                                mLogger.fine("     Log function: remove");
                                break;
                            case TransactionLog.FUNCTION_ADD:
                                mLogger.fine("     Log function: add");
                                break;
                            }
                        }
                    
                        if (f != TransactionLog.FUNCTION_ADD) {
                            continue;
                        }
                    
                        ObjectNode parentNode = null;
                        if (p.indexOf("SBROverWrite") > 0) {
                            parentNode = eo.getSBR();
                        } else {
                            parentNode = getParentNode(p, eo);
                        }
                        
                        if (!p.endsWith(".*")) {
                            p += ".*";
                        }
                        ObjectNode childNode = (ObjectNode) EPathAPI.getFieldValue(p, eo);
                        if (childNode == null) {
                            throw new ObjectException(mLocalizer.t("OPS637: applyDelta() failed. " + 
                                        "The child node is null."));
                        }
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("TransactionLog: child node to be removed: " + childNode);
                        }

                        if (parentNode == null) {
                            throw new ObjectException(mLocalizer.t("OPS638: applyDelta() failed. " + 
                                                            "The parent node is null."));
                        }
                        
                        // Check if it is a non-keyed child.  If it isn't, then remove.
                        // Otherwise, save it to the nonKeyedChildRemoveList ArrayList.
                        // This is done because the EPaths for non-keyed child nodes
                        // may use indices, which would result in a null-pointer exception
                        // if there is more than one child.
                        
                        if (childNode.pGetKey() != null) { 
                            parentNode.removeChild(childNode);
                        } else {
                            nonKeyedChildToRemoveList.add(p);
                            nonKeyedChildRemoveCount++;
                        }
                    }  // end FOR loop
                    
                    // Remove non-keyed children in reverse order in case indices 
                    // are used.  For example, suppose there are three objects.  
                    // If the first one is deleted at index 0, two objects remain, 
                    // and they now have "0"  and "1" as indices.  However, the 
                    // EPath index for the third object will still be "2", which
                    // no longer exists.
                    for (int j = nonKeyedChildRemoveCount - 1; j >= 0; j--) {
                        String p = (String) nonKeyedChildToRemoveList.get(j);
                        ObjectNode childNode = (ObjectNode) EPathAPI.getFieldValue(p, eo);
                        if (childNode == null) {
                            throw new ObjectException(mLocalizer.t("OPS639: applyDelta() failed. " + 
                                                    "The child node is null."));
                        }
                        ObjectNode parentNode = null;
                        if (p.indexOf("SBROverWrite") > 0) {
                            parentNode = eo.getSBR();
                        } else {
                            parentNode = getParentNode(p, eo);
                        }
                        
                        if (!p.endsWith(".*")) {
                            p += ".*";
                        }
                        parentNode.removeChild(childNode);
                    }
                        
                    for (int i = 0; i < list.size(); i++) {
                        TransactionLog log = (TransactionLog) list.get(i);
                        int f = log.getFunction();
                        Object v = log.getValue();
                        String p = log.getPath();
                    
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("     log value: " + v);
                            mLogger.fine("     log path: " + p);
                    
                            switch (f) {
                            case TransactionLog.FUNCTION_UPDATE:
                                mLogger.fine("     log function: update");
                                break;
                            case TransactionLog.FUNCTION_REMOVE:
                                mLogger.fine("     log function: remove");
                                break;
                            case TransactionLog.FUNCTION_ADD:
                                mLogger.fine("     log function: add");
                                break;
                            }
                        }
                    
                        if (f == TransactionLog.FUNCTION_ADD) {
                            continue;
                        }
                    
                        switch (f) {
                        case TransactionLog.FUNCTION_UPDATE:
                            EPathAPI.setFieldValue(EPathParser.parse(p), eo, v);
                            break;
                        case TransactionLog.FUNCTION_REMOVE:
                            ObjectNode parentNode = null;
                            if (p.indexOf("SBROverWrite") > 0) {
                                parentNode = eo.getSBR();
                            } else {
                                parentNode = getParentNode(p, eo);
                            }

                            try {
                                parentNode.addChild((ObjectNode) v);
                            } catch (DuplicateChildKeyException ex) {
                                // ignore it
                                ;
                            }
                            parentNode.resetAll();
                            break;
                        }
                    }
                
                    ret = eo;
                }
            } else {
                ret = eo;
            }
        } catch (ObjectException ex) {
            mLogger.warn(mLocalizer.x("OPS012: TransactionLog.applyDelta() encountered an ObjectException: {0}", ex.getMessage()));
            throw ex;
        } catch (EPathException ex) {
            mLogger.warn(mLocalizer.x("OPS013: TransactionLog.applyDelta() encountered an EPathException: {0}", ex.getMessage()));
            throw ex;
        }
        
        if (ret != null && 
            (ret.pGetChildren() == null || ret.pGetChildren().size() == 0)) {
            ret = null;
        }
        
        return ret;
    }    
    
    
    /**
     * get parent node
     * @param path object path
     * @param node node
     */
    private static ObjectNode getParentNode(String path, ObjectNode node) {
        StringTokenizer token = new StringTokenizer(path, ".");
        ObjectNode ret = null;
        
        try {
            if (path.endsWith(".*")) {
                int periodPos = path.lastIndexOf(".");
                String parentPath = path.substring(0, periodPos);
                periodPos = parentPath.lastIndexOf(".");
                parentPath = parentPath.substring(0, periodPos) + ".*";
                
                
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("(getParentNode): path: " + path);
                    mLogger.fine("(getParentNode): parent path: " + parentPath);
                }
            
                ret = (ObjectNode) EPathAPI.getFieldValue(parentPath, node);
            } else {
                int periodPos = path.lastIndexOf(".");
                String parentPath = path.substring(0, periodPos) + ".*";
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("(getParentNode): path: " + path);
                    mLogger.fine("(getParentNode): parent path: " + parentPath);
                }
            
                ret = (ObjectNode) EPathAPI.getFieldValue(parentPath, node);
            }
        } catch (ObjectException ex) {
            mLogger.warn(mLocalizer.x("OPS014: getParentNode() encountered an ObjectException: {0}", ex.getMessage()));
        } catch (EPathException ex) {
            mLogger.warn(mLocalizer.x("OPS015: getParentNode() encountered an EPathException: {0}", ex.getMessage()));
        }
        

        return ret;        
    }   

    private final class ExternalizableVersion1 {
        public ExternalizableVersion1() {};
        
        void writeExternal(ObjectOutput out) throws java.io.IOException {
            out.writeInt(mFunction);
            out.writeObject(mPath);
            out.writeObject(mValue);
        }
        
        void readExternal(ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
            mFunction = in.readInt();
            mPath = (String) in.readObject();
            mValue = in.readObject();
        }
    }
    
    public void writeExternal(ObjectOutput out) throws IOException {
        ExternalizableVersion1 ev = new ExternalizableVersion1();
        out.writeInt(version);
        ev.writeExternal(out);
    }

    public void readExternal(ObjectInput in) 
	throws IOException, java.lang.ClassNotFoundException 
    {
        int version = in.readInt();
        if (version == 1) {
            ExternalizableVersion1 ev = new ExternalizableVersion1();
            ev.readExternal(in);
        }
    }
}
