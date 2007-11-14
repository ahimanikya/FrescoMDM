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

import java.util.ArrayList;
import java.util.Date;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.exception.PotentialDupMissingFieldException;
import com.sun.mdm.index.util.Localizer;

/**
 * @author gzheng
 * @version
 */
public class PotentialDuplicate extends ObjectNode {
    private static ArrayList mFieldNames;
    private static ArrayList mFieldTypes;
    private transient final Localizer mLocalizer = Localizer.get();
    static {
        mFieldNames = new ArrayList();
        mFieldNames.add("EUID1");
        mFieldNames.add("EUID2");
        mFieldNames.add("Probability");
        mFieldNames.add("HighMatchType");
        mFieldNames.add("DateTime");
        mFieldNames.add("PotDupSignature");
        mFieldNames.add("TargetSignature");
        mFieldNames.add("Description");
        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_FLOAT_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
    }


    /**
     * Creates new potentialDuplicate
     *
     * @exception ObjectException object exception
     */
    public PotentialDuplicate()
        throws ObjectException {
        super("PotentialDuplicate", mFieldNames, mFieldTypes);
    }


    /**
     * Creates new potentialDuplicate
     *
     * @param euid1 euid1
     * @param euid2 euid2
     * @param probability probability
     * @param highmatchflag high match flag
     * @param datetime date time
     * @param potdupsignature potential duplicate signature
     * @param targetsignature target signature
     * @param description description
     * @exception ObjectException object exception
     */
    public PotentialDuplicate(String euid1, 
                              String euid2, 
                              float probability, 
                              String highmatchflag, 
                              Date datetime, 
                              String potdupsignature, String targetsignature, String description)
        throws ObjectException {
        super("PotentialDuplicate", mFieldNames, mFieldTypes);
        if (null == euid1 || euid1.equals("")) {
            throw new PotentialDupMissingFieldException(mLocalizer.t("OBJ542: Could not " + 
                                        "instantiate a PotentialDuplicate instance.  The " + 
                                        "EUID1 field is null or empty."));
        }
        if (null == euid2 || euid2.equals("")) {
            throw new PotentialDupMissingFieldException(mLocalizer.t("OBJ543: Could not " + 
                                        "instantiate a PotentialDuplicate instance.  The " + 
                                        "EUID2 field is null or empty."));
        }
        if (null == highmatchflag || highmatchflag.equals("")) {
            throw new PotentialDupMissingFieldException(mLocalizer.t("OBJ544: Could not " + 
                                        "instantiate a PotentialDuplicate instance.  The " + 
                                        "High Match Flag field is null or empty: {0}"));
        }
        if (null == datetime) {
            throw new PotentialDupMissingFieldException(mLocalizer.t("OBJ545: Could not " + 
                                        "instantiate a PotentialDuplicate instance.  The " + 
                                        "DateTime field is null."));
        }
        if (null == potdupsignature) {
            throw new PotentialDupMissingFieldException(mLocalizer.t("OBJ546: Could not " + 
                                        "instantiate a PotentialDuplicate instance.  The " + 
                                        "Potential Duplicate Signature field is null."));
        }
        if (null == targetsignature) {
            throw new PotentialDupMissingFieldException(mLocalizer.t("OBJ547: Could not " + 
                                        "instantiate a PotentialDuplicate instance.  The " + 
                                        "Target Signature field is null."));
        }

        setValue("EUID1", euid1);
        setValue("EUID2", euid2);
        setValue("Probability", new Float(probability));
        setValue("HighMatchType", highmatchflag);
        setValue("DateTime", datetime);
        setValue("PotDupSignature", potdupsignature);
        setValue("TargetSignature", targetsignature);
        setValue("Description", description);
    }


    /**
     * @exception ObjectException object exception
     * @return Date date time
     */
    public Date getDateTime()
        throws ObjectException {
        try {
            return (Date) getValue("DateTime");
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @exception ObjectException object exception
     * @return String description
     */
    public String getDescription()
        throws ObjectException {
        try {
            return (String) getValue("Description");
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @exception ObjectException object exception
     * @return String euid 1
     */
    public String getEUID1()
        throws ObjectException {
        try {
            return (String) getValue("EUID1");
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @exception ObjectException object exception
     * @return String euid 2
     */
    public String getEUID2()
        throws ObjectException {
        try {
            return (String) getValue("EUID2");
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @exception ObjectException object exception
     * @return String high match flag
     */
    public String getHighMatchFlag()
        throws ObjectException {
        try {
            return (String) getValue("HighMatchFlag");
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @exception ObjectException object exception
     * @return String potential duplicate signature
     */
    public String getPotDupSignature()
        throws ObjectException {
        try {
            return (String) getValue("PotDupSignature");
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @exception ObjectException object exception
     * @return float probability
     */
    public float getProbability()
        throws ObjectException {
        try {
            return ((Float) getValue("Probability")).floatValue();
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @exception ObjectException object exception
     * @return String target signature
     */
    public String getTargetSignature()
        throws ObjectException {
        try {
            return (String) getValue("TargetSignature");
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @param datetime date time
     * @exception ObjectException object exception
     * @todo Document: Setter for DateTime attribute of the PotentialDuplicate
     *      object
     */
    public void setDateTime(Date datetime)
        throws ObjectException {
        if (null == datetime) {
            throw new PotentialDupMissingFieldException(mLocalizer.t("OBJ657: The datetime " + 
                                        "field cannot be set to null."));
        }
        setValue("DateTime", datetime);
    }


    /**
     * @param description description
     * @exception ObjectException object exception
     * @todo Document: Setter for Description attribute of the
     *      PotentialDuplicate object
     */
    public void setDescription(String description)
        throws ObjectException {
        setValue("Description", description);
    }


    /**
     * @param euid1 EUI d1
     * @exception ObjectException object exception
     * @todo Document: Setter for EUID1 attribute of the PotentialDuplicate
     *      object
     */
    public void setEUID1(String euid1)
        throws ObjectException {
        if (null == euid1 || euid1.equals("")) {
            throw new PotentialDupMissingFieldException(mLocalizer.t("OBJ658: The EUID1 " + 
                                        "field cannot be set to null."));
        }
        setValue("EUID1", euid1);
    }


    /**
     * @param euid2 EUI d2
     * @exception ObjectException object exception
     * @todo Document: Setter for EUID2 attribute of the PotentialDuplicate
     *      object
     */
    public void setEUID2(String euid2)
        throws ObjectException {
        if (null == euid2 || euid2.equals("")) {
            throw new PotentialDupMissingFieldException(mLocalizer.t("OBJ659: The EUID2 " + 
                                        "field cannot be set to null."));
        }
        setValue("EUID2", euid2);
    }


    /**
     * @param highmatchflag high match flag
     * @exception ObjectException object exception
     * @todo Document: Setter for HighMatchFlag attribute of the
     *      PotentialDuplicate object
     */
    public void setHighMatchFlag(String highmatchflag)
        throws ObjectException {
        if (null == highmatchflag || highmatchflag.equals("")) {
            throw new PotentialDupMissingFieldException(mLocalizer.t("OBJ660: The High " + 
                                        "Match Flag field cannot be set to null."));
        }
        setValue("HighMatchFlag", highmatchflag);
    }


    /**
     * @param potdupsignature pot dup signature
     * @exception ObjectException object exception
     * @todo Document: Setter for PotDupSignature attribute of the
     *      PotentialDuplicate object
     */
    public void setPotDupSignature(byte[] potdupsignature)
        throws ObjectException {
        if (null == potdupsignature) {
            throw new PotentialDupMissingFieldException(mLocalizer.t("OBJ661: The Potential " + 
                                        "Duplicate Signature field cannot be set to null."));
        }
        setValue("PotDupSignature", potdupsignature);
    }


    /**
     * @param probability probability
     * @exception ObjectException object exception
     * @todo Document: Setter for Probability attribute of the
     *      PotentialDuplicate object
     */
    public void setProbability(float probability)
        throws ObjectException {
        setValue("Probability", new Float(probability));
    }


    /**
     * @param targetsignature target signature
     * @exception ObjectException object exception
     * @todo Document: Setter for TargetSignature attribute of the
     *      PotentialDuplicate object
     */
    public void setTargetSignature(byte[] targetsignature)
        throws ObjectException {
        if (null == targetsignature) {
            throw new PotentialDupMissingFieldException(mLocalizer.t("OBJ662: The Target " + 
                                        "Signature field cannot be set to null."));
        }
        setValue("Target_Signature", targetsignature);
    }


    /**
     * @param potdup potential duplicate
     * @exception ObjectException object exception
     * @return boolean boolean flag
     */
    public boolean equals(PotentialDuplicate potdup)
        throws ObjectException {
        boolean bRet = false;

        try {
            if (potdup.getEUID1().equals(this.getEUID1()) && potdup.getEUID2().equals(this.getEUID2())) {
                bRet = true;
            }
        } catch (ObjectException e) {
            throw e;
        }
        return bRet;
    }
    
    
    
    /**
     * hash code
     * @return int hash code
     */
    public int hashCode() {
        return super.hashCode();
    }
}
