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
package com.sun.mdm.index.ejb.master.helper;

import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.objects.epath.EPathArrayList;

/** Helper class for lookupPotentialDuplicates unit test
 * @author Dan Cidon
 */
public class LookupPotentialDuplicatesHelper extends BasicHelper {
    
    /** Constructor
     */    
    public LookupPotentialDuplicatesHelper() {       
    }

    /**
     * Lookup potential duplicates
     * @param args command line argument
     * @return EUID
     * @throws Exception error occured
     */
    
    public PotentialDuplicateIterator run(String args[]) throws Exception {
        setArgs(args);
        String euid = getStringValue("euid");
        MasterController mc = MCFactory.getMasterController();
        PotentialDuplicateSearchObject searchObj = new PotentialDuplicateSearchObject();
        searchObj.setMaxElements(200);
        searchObj.setPageSize(10);
        EPathArrayList fields = new EPathArrayList();           
        fields.add("Enterprise.SystemSBR.Person.PersonId");
        fields.add("Enterprise.SystemSBR.Person.EUID");
        fields.add("Enterprise.SystemSBR.Person.SSN");
        fields.add("Enterprise.SystemSBR.Person.FirstName");
        fields.add("Enterprise.SystemSBR.Person.LastName");  
        searchObj.setFieldsToRetrieve(fields);
        if (euid != null) {
            searchObj.setEUID(euid);
        }
        PotentialDuplicateIterator i = mc.lookupPotentialDuplicates(searchObj);
        return i;
    }
    
    /**
     * Main entry point
     * @param args command line argument
     */

    public static void main(String[] args) {
        try {
            LookupPotentialDuplicatesHelper helper = new LookupPotentialDuplicatesHelper();
            PotentialDuplicateIterator i = helper.run(args);
            System.out.println("Count: " + i.count());
             while (i.hasNext()) {
                System.out.println(i.next());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
