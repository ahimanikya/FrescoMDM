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
package com.sun.mdm.index.update;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SystemObjectException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.util.Localizer;


/**helper methods for update manager
 */
public class UpdateHelper {
    
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    
    
    /** Creates new UpdateHelper */
    public UpdateHelper() {
    }
    
    /**deactivates an <code>EnterpriseObject</code>
     * @param eo EnterpriseObject to be deactivated
     * @param date Date of being deactivated
     * @throws ObjectException error accessing ObjectNode
     */
    public void deactivateEO(EnterpriseObject eo, Date date) throws ObjectException {
        SBR sbr = eo.getSBR();
        if (sbr.getStatus().equals(SystemObject.STATUS_ACTIVE)) {
            sbr.setUpdateDateTime(date);
            sbr.setUpdateFunction("euidDeactivate");
            sbr.setValue("Status", SystemObject.STATUS_INACTIVE);
        }
    }
    
    /**activates an <code>EnterpriseObject</code>
     * @param eo EnterpriseObject to be activated
     * @param date Date of being deactivated
     * @throws ObjectException error accessing ObjectNode
     */
    public void activateEO(EnterpriseObject eo, Date date) throws ObjectException {
        SBR sbr = eo.getSBR();
        if (sbr.getStatus().equals(SystemObject.STATUS_INACTIVE)) {
            sbr.setUpdateDateTime(date);
            sbr.setUpdateFunction("euidActivate");
            sbr.setValue("Status", SystemObject.STATUS_ACTIVE);
        }
    }
    
    /**transfer a SystemObject
     * @param system SystemCode of the system object to be transfered
     * @param lid local ID of the system object to be transfered
     * @param srcEo EnterpriseObject to transfer from
     * @param destEo EnterpriseObject to transfer to
     * @throws ObjectException error accessing ObjectNode
     * @throws UpdateException invalid system code / lid
     * @return reference to the SystemObject
     */
    public SystemObject transferSO(String system, String lid,
    EnterpriseObject srcEo, EnterpriseObject destEo)
    throws UpdateException, ObjectException {
        // remove so from source EO
        SystemObject xferSo = srcEo.getSystemObject(system, lid);
        SystemObject socopy = (SystemObject) xferSo.copy();
        xferSo.setRemoveFlag(true);     
        // check if removed from srcEo is null
        if (socopy != null) {
            // put the SO from srcEo into destEo
            destEo.addSystemObject(socopy);
            return socopy;
        } else {
            throw new UpdateException(mLocalizer.t("UPD500: Invalid System Code={0}, " +
                                            "Local ID={1}", system, lid));
        }
    }
    
    /** remove a SystemObject
     * @param system SystemCode
     * @param lid Local ID
     * @param srcEo EnterpriseObjec to remove from
     * @throws ObjectException error accessing ObjectNode
     */
    public void removeSO(String system, String lid, EnterpriseObject srcEo)
    throws ObjectException {
        // delete so from source EO
        srcEo.deleteSystemObject(system, lid);
    }
    
    /**create a new EnterpriseObject using a SystemObject
     * @param obj SystemObject
     * @throws SystemObjectException error accessing SystemObject
     * @throws ObjectException error accessing ObjectNode
     * @return newly created EnterpriseObject
     */
    public EnterpriseObject createEO(SystemObject obj)
    throws SystemObjectException, ObjectException {
        // create SBR, copy system object to SBR
        SBR sbr = new SBR(obj.getChildType(),
        obj.getCreateUser(), "System", SystemObject.ACTION_ADD, new Date(),
        obj.getUpdateUser(), "System", SystemObject.ACTION_ADD, new Date(),
        SystemObject.STATUS_ACTIVE, obj.getObject());
        
        ArrayList<SystemObject> sysobjs = new ArrayList<SystemObject>();
        sysobjs.add(obj);
        
        EnterpriseObject eo = new EnterpriseObject(null, sbr, sysobjs);
        return eo;
    }
    
    /**Update a SystemObject
     * @param newSO reference to new SystemObject
     * @param oldSO reference to old SystemObject
     * @param copyFlags passed to updateIfNotNull
     * @param replaceSO flag to indicate to replace SystemObject values
     * @throws SystemObjectException error accessing SystemObject
     * @throws ObjectException error accessing ObjectNode
     * @return reference to updated SystemObject
     */
    public SystemObject updateSO(SystemObject newSO, SystemObject oldSO, boolean copyFlags, boolean replaceSO)
    throws SystemObjectException, ObjectException {
        ObjectNode newObj = newSO.getObject();
        ObjectNode oldObj = oldSO.getObject();
        
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Old SystemObject:" + oldSO);
            mLogger.fine("New SystemObject:" + newSO);
        }
        // If replace system object flag is set
        if (replaceSO) {
           // update the old object with new values
           // this method will copy over null values from the new SO
           // delete children in the old that are not in new
           // add children in new that are not in old 	
           oldObj.update(newObj, true, true);
        } else {
           // update the old object with new values
           // this method will leave null values alone
           // add children in new that are not in old
           // no child is deleted
           oldObj.updateIfChanged(newObj, true, false);
        }
        return oldSO;
    }
    
    /**Update a SystemObject in an EnterpriseObject
     * @param newSO new image of SystemObject
     * @param eo EnterpriseObject
     * @param copyFlags passed to updateIfNotNull
     * @param replaceSO flag to indicate to replace SystemObject values
     * @throws SystemObjectException error accessing SystemObject
     * @throws ObjectException error accessing ObjectNode
     * @return updated EnterpriseObject
     */
    public EnterpriseObject updateSO(SystemObject newSO, EnterpriseObject eo, boolean copyFlags, 
        boolean replaceSO)
    throws SystemObjectException, ObjectException {
        Collection systemObjs = eo.getSystemObjects();
        Iterator iter = systemObjs.iterator();
        
        String sys = newSO.getSystemCode();
        String lid = newSO.getLID();
        
        while (iter.hasNext()) {
            SystemObject currSO = (SystemObject) iter.next();
            
            if (sys.equals(currSO.getSystemCode())
            && lid.equals(currSO.getLID())) {
                
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Old SystemObject:" + currSO);
                    mLogger.fine("New SystemObject:" + newSO);
                }
                updateSO(newSO, currSO, copyFlags, replaceSO);
                return eo;
            }
        }     
        // not currently part of enterprise object, and return null
        return null;
    }
       
    
    /**Checks if an array of Objects if contains the specified object
     * @param o object to be checked
     * @param array array of objects
     * @return position of the object in the array, -1 if not found
     */
    public int contains(Object o, Object[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(o)) {
                return i;
            }
        }
        
        return -1;
    }
}
