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
package com.sun.mdm.index.objects;

import com.sun.mdm.index.objects.exception.ObjectException;

import java.util.ArrayList;


/**
 * @author gzheng
 * @version
 */
public class MergeObject extends ObjectNode {
    private static ArrayList mFieldNames;
    private static ArrayList mFieldTypes;

    static {
        mFieldNames = new ArrayList();
        mFieldNames.add("MergeID");
        mFieldNames.add("Kept_EUID");
        mFieldNames.add("Merged_EUID");
        mFieldNames.add("Merge_TransactionNumber");
        mFieldNames.add("UnMerge_TransactionNumber");
        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
    }

    /**
     * Creates new MergeObject
     *
     * @exception ObjectException object exception
     */
    public MergeObject() throws ObjectException {
        super("MergeObject", mFieldNames, mFieldTypes);

        setNullable("MergeID", false);
        setNullable("Kept_EUID", false);
        setNullable("Merged_EUID", false);
        setNullable("Merge_TransactionNumber", false);
        setNullable("UnMerge_TransactionNumber", true);

        setNull("UnMerge_TransactionNumber", true);
    }

    /**
     * @param mergeid merge id
     * @param kepteuid kept euid
     * @param mergedeuid merged euid
     * @param mergetn merge tn
     * @param unmergetn unmerge tn
     * @exception ObjectException object exception
     * @todo Document this constructor
     */
    public MergeObject(String mergeid, String kepteuid,
        String mergedeuid, String mergetn, String unmergetn) throws ObjectException {
        super("MergeObject", mFieldNames, mFieldTypes);

        setValue("MergeID", mergeid);
        setValue("Kept_EUID", kepteuid);
        setValue("Merged_EUID", mergedeuid);
        setValue("Merge_TransactionNumber", mergetn);
        setValue("UnMerge_TransactionNumber", unmergetn);
    }


    /**
     * @exception ObjectException object exception
     * @return String merge id
     */
    public String getMergeID() throws ObjectException {
        try {
            return ((String) getValue("MergeID"));
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @exception ObjectException object exception
     * @return kept euid
     */
    public String getKeptEUID() throws ObjectException {
        try {
            return ((String) getValue("Kept_EUID"));
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @exception ObjectException object exception
     * @return merged euid
     */
    public String getMergedEUID() throws ObjectException {
        try {
            return ((String) getValue("Merged_EUID"));
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @exception ObjectException object exception
     * @return merge transaction number
     */
    public String getMergeTransactionNumber() throws ObjectException {
        try {
            return ((String) getValue("Merge_TransactionNumber"));
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @exception ObjectException object exception
     * @return unmerge transaction number
     */
    public String getUnMergeTransactionNumber() throws ObjectException {
        try {
            return ((String) getValue("UnMerge_TransactionNumber"));
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @param mergeid merge id
     * @exception ObjectException object exception
     * @todo Document: Setter for CreateDateTime attribute of the
     *      MergeObject object
     */
    public void setMergeID(Object mergeid) throws ObjectException {
        try {
            setValue("MergeID", mergeid);
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @param kepteuid kept euid
     * @exception ObjectException object exception
     * @todo Document: Setter for CreateDateTime attribute of the
     *      MergeObject object
     */
    public void setKeptEUID(Object kepteuid) throws ObjectException {
        try {
            setValue("Kept_EUID", kepteuid);
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @param mergedeuid merged euid
     * @exception ObjectException object exception
     * @todo Document: Setter for CreateDateTime attribute of the
     *      MergeObject object
     */
    public void setMergedEUID(Object mergedeuid) throws ObjectException {
        try {
            setValue("Merged_EUID", mergedeuid);
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @param mergetn merge transaction number
     * @exception ObjectException object exception
     * @todo Document: Setter for CreateDateTime attribute of the
     *      MergeObject object
     */
    public void setMergeTransactionNumber(Object mergetn) throws ObjectException {
        try {
            setValue("Merge_TransactionNumber", mergetn);
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @param unmergetn unmerge transaction number
     * @exception ObjectException object exception
     * @todo Document: Setter for CreateDateTime attribute of the
     *      MergeObject object
     */
    public void setUnMergeTransactionNumber(Object unmergetn) throws ObjectException {
        try {
            setValue("UnMerge_TransactionNumber", unmergetn);
        } catch (ObjectException e) {
            throw e;
        }
    }
}
