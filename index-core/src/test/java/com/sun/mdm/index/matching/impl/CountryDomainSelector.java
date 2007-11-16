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
 
package com.sun.mdm.index.matching.impl;
import com.sun.mdm.index.matching.MatchingException;
import com.sun.mdm.index.matching.DomainSelector;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.configurator.impl.standardization.SystemObjectField;
import com.sun.mdm.index.configurator.impl.standardization.PreparsedFieldGroup;
import com.sun.mdm.index.configurator.impl.standardization.UnparsedFieldGroup;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.AddressObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;

/**
 * Sample domain selector for eIndex showing how country code information in
 * the address data can be used to select the best domain for standardiation / 
 * normalization.
 */
public class CountryDomainSelector implements DomainSelector {
    
    /** Creates a new instance of CountryDomainSelector */
    public CountryDomainSelector() {
    }

    /**
     * Get an array of domain designations for the given preparsed field group values.
     * @param sysObj System object being standardized
     * @param fieldGroup Field group being standardized
     * @param allColumns Field group values to be standardized
     * @throws ObjectException An exception has occured
     * @return array of domains ("US", "UK", AU,...) for 
     */    
    public String[] getDomains(SystemObject sysObj, UnparsedFieldGroup fieldGroup, ArrayList allColumns) 
    throws ObjectException {
        
        ArrayList sourceFields = fieldGroup.getSourceFields();
        String[] domains;
        
        //Currently assume that the only unparsed field group is for addresses.  Address
        //field group consists of two fields (lines 1 & 2).
        int size = sourceFields.size();
        
        switch (size) {
            
            case 2:
                SystemObjectField sourceField = (SystemObjectField) sourceFields.get(0);
                if (sourceField.getQualifiedName().equals("Person.Address[*].AddressLine1")) {
                    PersonObject person = (PersonObject) sysObj.getObject();
                    
                    Collection addresses = person.getAddress();
                    if (addresses != null) {
                        domains = new String[addresses.size()];
                        Iterator i = addresses.iterator();
                        int count = 0;
                        while (i.hasNext()) {
                            //Get address objects one by one and inspect their country field
                            AddressObject address = (AddressObject) i.next();
                            String country = address.getCountryCode();
                            if (country == null) {
                                //Assume US if country code is null
                                domains[count] = "US";
                            } else if (country.equals("UNST")) {
                                domains[count] = "US";
                            } else if (country.equals("UNIT") || country.equals("GRBR")) {
                                domains[count] = "UK";
                            } else if (country.equals("AUST")) {
                                domains[count] = "AU"; 
                            } else if (country.equals("FRAN")) {
                                domains[count] = "FR";
                            } else {
                                //Assume US if country code is unrecognized
                                domains[count] = "US";
                            }
                            count++;
                        } 
                    } else {
                        //If there are no addresses, return a zero length array
                        domains = new String[0];
                    }
                } else {
                    throw new ObjectException("Unrecognized domain selector field group: " + sourceField);
                }
                break;
            default:
                throw new ObjectException("Unrecognized domain selector field group: " + sourceFields);
             
        }
            
        return domains;
    }
    
    /**
     * Get an array of domain designations for the given unparsed field group values.
     * @param sysObj System object being standardized
     * @param fieldGroup Field group being standardized
     * @param allColumns Field group values to be standardized
     * @throws ObjectException An exception has occured
     * @return array of domains ("US", "UK", AU, ...) for 
     */    
    public String[] getDomains(SystemObject sysObj, PreparsedFieldGroup fieldGroup, ArrayList allColumns) 
    throws ObjectException {
        
        PersonObject person = (PersonObject) sysObj.getObject();
        //Begin by assuming US domain
        String domain = "US";
        //Base domain selection on address info only.  If all addresses from UK, then assume
        //UK domain.  Otherwise select US domain.
        Collection addresses = person.getAddress();
        if (addresses != null) {
            Iterator i = addresses.iterator();
            while (i.hasNext()) {
                AddressObject address = (AddressObject) i.next();
                String country = address.getCountryCode();
                if (country == null || country.equals("UNST")) {
                    break;
                } else if (country.equals("UNIT") || country.equals("GRBR")) {
                    domain = "UK";
                } else if (country.equals("AUST")) {
                    domain = "AU";  
                } else if (country.equals("FRAN")) {
                    domain = "FR";
                } 
            }
        }
        //Size the return array based on the size of one of the elements in allColumns.
        //Since each element consists of an arraylist of the same size, just choose
        //the first.
        String[] domains = new String[((ArrayList)allColumns.get(0)).size()];
        for (int i = 0; i < domains.length; i++) {
            //Set all elements in the array to the domain determined by analyzing
            //the addresses
            domains[i] = domain;
        }
        return domains;
    }
    
}
