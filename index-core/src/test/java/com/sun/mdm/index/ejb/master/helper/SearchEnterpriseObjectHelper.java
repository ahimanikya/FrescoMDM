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
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultIterator;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPathArrayList;

/**
 * Test MC.searchEnterpriseObject()
 * @author Dan Cidon
 */
public class SearchEnterpriseObjectHelper extends BasicHelper {

    /**
     * Main entry point
     * @param args command line argument
     */
    public static void main(String[] args)  {
        try {
            SearchEnterpriseObjectHelper helper = new SearchEnterpriseObjectHelper();
            EOSearchResultIterator r = helper.run(args);
            //r.sortBy("Enterprise.SystemSBR.Person.LastName",false);
            while (r.hasNext()) {
                EOSearchResultRecord rec = r.next();                
                PersonObject p = (PersonObject) rec.getObject();
                System.out.println(rec + ":" + p.getFirstName() + ", " 
                    +  p.getLastName() + ", " + p.getSSN());                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Search for enterprise objects
     * @param args command line argument
     * @return merge result
     * @throws Exception error occured
     */        
    public EOSearchResultIterator run(String args[]) throws Exception {
        setArgs(args);     
        String searchType = getStringValue("searchType");
        boolean weighted = getBooleanValue("weighted");
        String sortField = getStringValue("sortField");
        
        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        MasterController mc = MCFactory.getMasterController();
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();           
        fields.add("Enterprise.SystemSBR.Person.PersonId");
        fields.add("Enterprise.SystemSBR.Person.PersonCatCode");
        fields.add("Enterprise.SystemSBR.Person.EUID");
        fields.add("Enterprise.SystemSBR.Person.SSN");
        fields.add("Enterprise.SystemSBR.Person.FirstName");
        fields.add("Enterprise.SystemSBR.Person.LastName");
        //fields.add("Enterprise.SystemSBR.Person.VIPFlag");           
        //fields.add( "Enterprise.SystemSBR.Person.Address.AddressId" );
        //fields.add( "Enterprise.SystemSBR.Person.Address.AddressLine1" );
        //fields.add( "Enterprise.SystemSBR.Person.Address.City" );

        //Try a variety of options...
        searchOptions = new EOSearchOptions(searchType, fields);
        //searchOptions.setPageSize(20);
        searchOptions.setWeighted(weighted);
        //searchOptions.setStandardize( true );
        //searchOptions.setFilterStandardizedSourceFields( true );
        //searchOptions.setFilterPhoneticizedTargetFields( true );
        //searchOptions.setOption( "UseLikeOperator", new Boolean(true));
        
        IteratorRecord record = getNextRecord();
        SystemObject[] sysobj = record.getSystemObjects();
        if (sysobj.length != 1) {
            throw new Exception("Invalid number of system objects: " + sysobj.length);
        }
        criteria = new EOSearchCriteria();
        criteria.setSystemObject(sysobj[0]); 
        EOSearchResultIterator r = mc.searchEnterpriseObject(criteria, searchOptions);
        if (sortField != null) {
            System.out.println("Sorting results by: " + sortField);
            r.sortBy(sortField, false);
        }
        return r;
    }
    
}
