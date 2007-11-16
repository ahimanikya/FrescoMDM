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
 
package com.sun.mdm.index.update.impl;
import com.sun.mdm.index.objects.AliasObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.SystemObjectException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SBROverWrite;
import com.sun.mdm.index.objects.epath.EPathBuilder;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

/**
 * Helper methods for alias handling
 * @author  dcidon
 */
public class AliasHelper {
    
    private final static String[] FIELDS = {
        "FirstName",
        "LastName",
        "MiddleName",
    };
    
    /** Creates a new instance of AliasHelper */
    public AliasHelper() {
    }
    
    /**
     * Given to person object images, generate alias if name fields are diffferent.
     * Alias generated from before image.
     * @param obj1 image 1
     * @param obj2 image 2
     * @throws SystemObjectException exception occured
     * @throws ObjectException exception occured
     *
     * @return alias or null if name fields not changed
     */
    private static AliasObject generateAlias(PersonObject obj1, PersonObject obj2)
    throws SystemObjectException, ObjectException {
        String beforeFirstName = obj1.getFirstName();
        String beforeLastName = obj1.getLastName();
        String beforeMiddleName = obj1.getMiddleName();
        String beforeFirstNamePhonetic = obj1.getFnamePhoneticCode();
        String beforeLastNamePhonetic = obj1.getLnamePhoneticCode();
        String beforeMiddleNamePhonetic = obj1.getMnamePhoneticCode();
        String beforeStdFirstName = obj1.getStdFirstName();
        String beforeStdLastName = obj1.getStdLastName();
        String beforeStdMiddleName = obj1.getStdMiddleName();
        
        AliasContainer beforeAlias = new AliasContainer(beforeFirstName,
        beforeLastName, beforeMiddleName, beforeFirstNamePhonetic,
        beforeLastNamePhonetic, beforeMiddleNamePhonetic, beforeStdFirstName,
        beforeStdLastName, beforeStdMiddleName);
        String afterFirstName = obj2.getFirstName();
        String afterLastName = obj2.getLastName();
        String afterMiddleName = obj2.getMiddleName();
        String afterFirstNamePhonetic = obj2.getFnamePhoneticCode();
        String afterLastNamePhonetic = obj2.getLnamePhoneticCode();
        String afterMiddleNamePhonetic = obj2.getMnamePhoneticCode();
        String afterStdFirstName = obj2.getStdFirstName();
        String afterStdLastName = obj2.getStdLastName();
        String afterStdMiddleName = obj2.getStdMiddleName();
        
        AliasContainer afterAlias = new AliasContainer(afterFirstName,
        afterLastName, afterMiddleName, afterFirstNamePhonetic,
        afterLastNamePhonetic, afterMiddleNamePhonetic, afterStdFirstName,
        afterStdLastName, afterStdMiddleName);
        if (!beforeAlias.equals(afterAlias)) {
            return beforeAlias.getAlias();
        } else {
            return null;
        }
    }
    
    /**
     * Get all aliases from active system objects and put into single
     * collection
     * @param eo enteprise object
     * @return Collection collection of aliases
     * @throws ObjectException object access exception
     */
    private static Collection getAliasesFromSOs(EnterpriseObject eo) 
    throws ObjectException, SystemObjectException {
        //Go through active system objects and add their aliases to the SBR alias set
        Collection afterSysObjs = eo.getSystemObjects();
        Set allAliases = null;
        if (afterSysObjs != null) {
            Iterator afterSysObjIterator = afterSysObjs.iterator();
            PersonObject personSBR = (PersonObject) eo.getSBR().getObject();
            while (afterSysObjIterator.hasNext()) {
                SystemObject afterSysObj = (SystemObject) afterSysObjIterator.next();
                if (afterSysObj.getStatus().equals(SystemObject.STATUS_ACTIVE) && !afterSysObj.isRemoved()) {
                    PersonObject afterPersonObject = (PersonObject) afterSysObj.getObject();
                    Collection aliases = afterPersonObject.getAlias();
                    if (aliases != null) {                        
                        Iterator i = aliases.iterator();
                        while (i.hasNext()) {
                            AliasObject alias = (AliasObject) i.next();
                            if (!alias.isRemoved()) {
                                if (allAliases == null) {
                                   allAliases = new HashSet();
                                }
                                allAliases.add(alias);
                            }
                        }
                    }
                    //If the SO has a different name then the SBR, then add it as an
                    //alias as well
                    AliasObject alias = generateAlias(afterPersonObject, personSBR);
                    if (alias != null) {
                        if (allAliases == null) {
                            allAliases = new HashSet();
                        }
                        allAliases.add(alias);
                    }
                }
            }
        }
        return allAliases;
    }
    
    /**
     * Add all aliases in the set to the person object
     * @param person person object to add aliases to
     * @param set set of aliases to add
     */
    private static void addAliasesToPersonObject(PersonObject person, AliasSet set)
    throws ObjectException {
        Collection c = person.getAlias();
        AliasObject[] aliasArray = set.getAliases();
        if (c != null) {
            AliasSet aliasSet = new AliasSet();
            aliasSet.addAliases(c);
            for (int i = 0; i < aliasArray.length; i++) {
                AliasObject alias = aliasSet.getAlias(aliasArray[i]);
                if (alias == null) {
                    person.addAlias(aliasArray[i]);
                } else {
                    if (alias.isRemoved()) {
                        alias.setRemoveFlag(false);
                    }
                }
            }
        } else {
            for (int i = 0; i < aliasArray.length; i++) {
                person.addAlias(aliasArray[i]);
            }
        }
    }    

    /**
     * Flag all aliases in the SBR for removal
     */
    public static void markAliasesForRemoval(EnterpriseObject eo) throws SystemObjectException,
    ObjectException {
        SBR sbr = eo.getSBR();
        ArrayList overwrites = sbr.getOverWrites();
        PersonObject person = (PersonObject) sbr.getObject();
        Collection aliases = person.getAlias();
        if (aliases != null) {
            Iterator aliasIter = aliases.iterator();
            //Go through all aliases in eo and flag them for removal
            while (aliasIter.hasNext()) {
                AliasObject alias = (AliasObject) aliasIter.next();
                //For each alias, also calculate the overwrites that it should have
                //and flag them for removal
                ArrayList aliasOverwrites = convertAliasToOverwrite(alias, sbr);
                alias.setRemoveFlag(true);
                Iterator overwriteIterator = aliasOverwrites.iterator();
                while (overwriteIterator.hasNext()) {
                    String path = ((SBROverWrite) overwriteIterator.next()).getEPath();
                    SBROverWrite overwrite = findOverwrite(path, overwrites);
                    if (overwrite != null) {
                        overwrite.setRemoveFlag(true);
                    }
                }
            }
        }
    }
    
    /**
     * Add alias to SO's whose name fields have changed
     * @param beforeImage before image
     * @param afterImage after image
     * @throws SystemObjectException exception
     * @throws ObjectException exception
     */
    public static void addNameChangeAliasSO(EnterpriseObject beforeImage, 
    EnterpriseObject afterImage) throws SystemObjectException, ObjectException {
        Collection afterSysObjs = afterImage.getSystemObjects();
        if (afterSysObjs != null) {
            Iterator afterSysObjIterator = afterSysObjs.iterator();
            while (afterSysObjIterator.hasNext()) {
                SystemObject afterSysObj = (SystemObject) afterSysObjIterator.next();
                String sysCode = afterSysObj.getSystemCode();
                String lid = afterSysObj.getLID();
                SystemObject beforeSysObj = beforeImage.getSystemObject(sysCode, lid);
                if (beforeSysObj != null) {
                    PersonObject afterPersonObject = (PersonObject) afterSysObj.getObject();
                    PersonObject beforePersonObject = (PersonObject) beforeSysObj.getObject();
                    AliasObject alias = AliasHelper.generateAlias(beforePersonObject, afterPersonObject);
                    if (alias != null) {
                        AliasSet set = new AliasSet();
                        set.addAlias(alias);
                        AliasHelper.addAliasesToPersonObject(afterPersonObject, set);
                    }
                }
            }
        }    
    }

    /**
     * Add alias to SBR if SBR name field has changed
     * @param beforeImage before image
     * @param afterImage after image
     * @throws SystemObjectException exception
     * @throws ObjectException exception
     */
    public static void addNameChangeAliasSBR(EnterpriseObject beforeImage, 
    EnterpriseObject afterImage) throws SystemObjectException, ObjectException {
        SBR beforeSBR = beforeImage.getSBR();
        SBR afterSBR = afterImage.getSBR();
        PersonObject beforeSBRPersonObject = (PersonObject) beforeSBR.getObject();
        PersonObject afterSBRPersonObject = (PersonObject) afterSBR.getObject();
        AliasObject alias = generateAlias(beforeSBRPersonObject, afterSBRPersonObject);
        if (alias != null) {
            if (!hasAlias(afterSBRPersonObject, alias)) {
                afterSBRPersonObject.addAlias(alias);
            }
        }
    }

    /** Determine if person object has alias
     * @param person person object
     * @param alias alias object
     * @return true if person has alias
     */
    private static boolean hasAlias(PersonObject person, AliasObject alias) {
        Collection c = person.getAlias();
        if (c != null) {
            AliasSet aliasSet = new AliasSet();
            aliasSet.addAliases(c);
            return aliasSet.hasAlias(alias);
        } else {
            return false;
        }        
    }
    
    /**
     * Copy aliases from before SBR to after SBR
     * @param beforeImage before image
     * @param afterImage after image
     * @throws SystemObjectException exception
     * @throws ObjectException exception
     */
    public static void copyAliasSBR(EnterpriseObject beforeImage, 
    EnterpriseObject afterImage) throws SystemObjectException, ObjectException {
        
        AliasSet newSBRAliases = new AliasSet();
        SBR beforeSBR = beforeImage.getSBR();
        SBR afterSBR = afterImage.getSBR();
        PersonObject beforeSBRPersonObject = (PersonObject) beforeSBR.getObject();
        PersonObject afterSBRPersonObject = (PersonObject) afterSBR.getObject();
        Collection beforeSBRAliases = beforeSBRPersonObject.getAlias();
        if (beforeSBRAliases != null) {
            newSBRAliases.addAliases(beforeSBRAliases);
        }
        
        //Remove any aliases that were transfered over from above which came from 
        //system objects that are no longer in after image
        Collection beforeSysObjs = beforeImage.getSystemObjects();
        if (beforeSysObjs != null) {
            Iterator beforeSysObjIterator = beforeSysObjs.iterator();
            while (beforeSysObjIterator.hasNext()) {
                SystemObject beforeSysObj = (SystemObject) beforeSysObjIterator.next();
                String sysCode = beforeSysObj.getSystemCode();
                String lid = beforeSysObj.getLID();
                SystemObject afterSysObj = afterImage.getSystemObject(sysCode, lid);
                if (afterSysObj == null || !afterSysObj.getStatus().equals(SystemObject.STATUS_ACTIVE)) {
                    PersonObject beforePersonObject = (PersonObject) beforeSysObj.getObject();
                    Collection aliases = beforePersonObject.getAlias();
                    if (aliases != null) {
                        newSBRAliases.removeAliases(aliases);
                    }
                }
            }
        }
        
        addAliasesToPersonObject(afterSBRPersonObject, newSBRAliases);        
    }
    
    /**
     * Create alias from maiden name and add to each SO
     * @param afterImage after image
     * @throws SystemObjectException exception
     * @throws ObjectException exception
     */
    public static void addAliasMaidenNameSO(EnterpriseObject afterImage) throws 
    SystemObjectException, ObjectException {
        Collection afterSysObjs = afterImage.getSystemObjects();
        if (afterSysObjs != null) {
            Iterator afterSysObjIterator = afterSysObjs.iterator();
            while (afterSysObjIterator.hasNext()) {
                SystemObject afterSysObj = (SystemObject) afterSysObjIterator.next();
                PersonObject obj1 = (PersonObject) afterSysObj.getObject();        
                if (obj1.getMaidenPhoneticCode() !=  null && !obj1.getMaidenPhoneticCode().equals("")) {
                    AliasObject alias = new AliasObject();
                    alias.setFirstName(obj1.getFirstName());
                    alias.setLastName(obj1.getMaiden());
                    alias.setMiddleName(obj1.getMiddleName());
                    alias.setFnamePhoneticCode(obj1.getFnamePhoneticCode());
                    alias.setLnamePhoneticCode(obj1.getMaidenPhoneticCode());
                    alias.setMnamePhoneticCode(obj1.getMnamePhoneticCode());
                    alias.setStdFirstName(obj1.getStdFirstName());
                    alias.setStdLastName(obj1.getStdLastName());
                    alias.setStdMiddleName(obj1.getStdMiddleName());
                    AliasSet set = new AliasSet();
                    set.addAlias(alias);
                    AliasHelper.addAliasesToPersonObject(obj1, set);
                }
            }
        }
    }
    
    /**
     * Create alias from maiden name and add to SBR
     * @param afterImage after image
     * @throws SystemObjectException exception
     * @throws ObjectException exception
     */
    public static void addAliasMaidenNameSBR(EnterpriseObject afterImage) throws 
    SystemObjectException, ObjectException {
        PersonObject obj1 = (PersonObject) afterImage.getSBR().getObject();
        if (obj1.getMaidenPhoneticCode() !=  null  && !obj1.getMaidenPhoneticCode().equals("")) {
            AliasObject alias = new AliasObject();
            alias.setFirstName(obj1.getFirstName());
            alias.setLastName(obj1.getMaiden());
            alias.setMiddleName(obj1.getMiddleName());
            alias.setFnamePhoneticCode(obj1.getFnamePhoneticCode());
            alias.setLnamePhoneticCode(obj1.getMaidenPhoneticCode());
            alias.setMnamePhoneticCode(obj1.getMnamePhoneticCode());
            alias.setStdFirstName(obj1.getStdFirstName());
            alias.setStdLastName(obj1.getStdLastName());
            alias.setStdMiddleName(obj1.getStdMiddleName());
            AliasSet set = new AliasSet();
            set.addAlias(alias);
            AliasHelper.addAliasesToPersonObject(obj1, set);
        }
    }    
    
    /**
     * Add all aliases for SO's to SBR
     * @param afterImage after image
     * @throws SystemObjectException exception
     * @throws ObjectException exception
     */
    public static void addAliasSBRFromSO(EnterpriseObject afterImage) 
    throws SystemObjectException, ObjectException {
        
        SBR afterSBR = afterImage.getSBR();
        PersonObject afterSBRPersonObject = (PersonObject) afterSBR.getObject();
        
        //Go through active system objects and add their aliases and names to the SBR alias set
        Collection aliases = getAliasesFromSOs(afterImage);
        if (aliases != null) {
            AliasSet newSBRAliases = new AliasSet();
            newSBRAliases.addAliases(aliases);
             //Add the aliases to the SBR if they do not already exist
            AliasHelper.addAliasesToPersonObject(afterSBRPersonObject, (AliasSet) newSBRAliases.copy());
        }
    }
     
    
    //Ensure all SBR aliases have an overwrite value
    public static void addAliasOverwrite(EnterpriseObject eo) 
    throws ObjectException, SystemObjectException { 
        SBR sbr = eo.getSBR();
        PersonObject person = (PersonObject) sbr.getObject();
        Collection alias = person.getAlias();
        if (alias != null) {
            ArrayList list = sbr.getOverWrites();
            Iterator i = alias.iterator();
            while (i.hasNext()) {
                AliasObject aliasObj = (AliasObject) i.next();
                if (!aliasObj.isRemoved()) {
                    String path = EPathBuilder.createEPath(aliasObj, "FirstName", sbr);
                    SBROverWrite overwrite = findOverwrite(path, list);
                    if (overwrite == null) {
                        ArrayList aliasOverwrite = convertAliasToOverwrite(aliasObj, sbr);
                        sbr.addOverWrites(aliasOverwrite);
                    } else {
                        //Undo any user removals if the alias has been readded to SBR
                        //by the policy
                        if (overwrite.isRemoved()) {
                            overwrite.setRemoveFlag(false);
                        }
                        path = EPathBuilder.createEPath(aliasObj, "LastName", sbr);
                        overwrite = findOverwrite(path, list);
                        if (overwrite != null && overwrite.isRemoved()) {
                            overwrite.setRemoveFlag(false);
                        }
                        path = EPathBuilder.createEPath(aliasObj, "MiddleName", sbr);
                        overwrite = findOverwrite(path, list);
                        if (overwrite != null && overwrite.isRemoved()) {
                            overwrite.setRemoveFlag(false);
                        }
                    }
                }
            }
        }        
    }
    
    //Convert an alias to a set of overwrites
    private static ArrayList convertAliasToOverwrite(AliasObject alias, SBR sbr) 
    throws ObjectException {
        ArrayList list = new ArrayList();
        for (int i = 0; i < FIELDS.length; i++) {
            Object value = (Object) alias.getField(FIELDS[i]).getValue();
            if (value != null) {
                SBROverWrite overwrite = new SBROverWrite();
                overwrite.setPath(EPathBuilder.createEPath(alias, FIELDS[i], sbr));
                overwrite.setData(value);
                list.add(overwrite);
            }
        }
        return list;
    }
    
    //Returns overwrite if path matches, otherwise null
    private static SBROverWrite findOverwrite(String path, ArrayList overwrite) 
    throws SystemObjectException, ObjectException {
        if (overwrite == null) {
            return null;
        }
        Iterator i = overwrite.iterator();
        while (i.hasNext()) {
            SBROverWrite overwriteObject = (SBROverWrite) i.next();
            if (path.equals(overwriteObject.getEPath())) {
                return overwriteObject;
            }
        }
        return null;
    }
    
}
