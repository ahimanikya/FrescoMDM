/*
 * ReportHandler.java
 * Value Object for the Report:Assume Matches Records
 * Created on November 23, 2007, 8:12 PM
 *
 */

package com.sun.mdm.index.edm.presentation.valueobjects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author admin
 */
public class AssumeMatchesRecords {

    private HashMap dynamicResultsFields = new HashMap();
    private String assumedMatchId = null;
    /**
     * EUID to be displayed on the JSF page
     */
    private String euid = null;
    
    /**
     * AssumeMatches Local id 
     */    
    private String localId  = null;
    
    /**
     * AssumeMatches Local id 
     */    
    private String systemCode  = null;
   
    /**
     * AssumeMatches First Name 
     */    
    private ArrayList firstName  = new ArrayList();


    /**
     * AssumeMatches Last Name
     */    
    private ArrayList lastName = new ArrayList();
    
    /**
     * AssumeMatches SSN 
     */    
    private ArrayList ssn  = new ArrayList();
    
    /**
     * AssumeMatches DOB 
     */    
    private ArrayList dob  = new ArrayList();

    /**
     * AssumeMatches addressLine1
     */    
    private ArrayList addressLine1  = new ArrayList();
   
     /**
     * AssumeMatches weight
     */    
     private String weight  = null;
    
   
    /**
     * @return EUID
     */
    public String getEuid() {
        return euid;
    }

    /**
     * Sets the EUID parameter for the search
     * @param euid
     */
    public void setEuid(String euid) {
        this.euid = euid;
    }

    /**
     * @return first Name
     */
    public ArrayList getFirstName() {
        return firstName;
    }

    /**
     * Sets the First Name
     * @param firstName
     */
    public void setFirstName(ArrayList firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the Last Name
     * @return
     */
    public ArrayList getLastName() {
        return lastName;
    }

    /**
     * * Sets the Last Name
     * @param lastName
     */
    public void setLastName(ArrayList lastName) {
        this.lastName = lastName;
    }

    /**
     * SSN to be printed in the presentation
     * @return
     */
    public ArrayList getSsn() {
        return ssn;
    }

    /**
     * SSN to be printed in the presentation
     * @param ssn 
     */
    public void setSsn(ArrayList ssn) {
        this.ssn = ssn;
    }

    /**
     * DOB to be printed in the presentation
     * @return DOB
     */    
    public ArrayList getDob() {
        return dob;
    }

    /**
     * DOB to be printed in the presentation
     * @param dob 
     */
    public void setDob(ArrayList dob) {
        this.dob = dob;
    }

    
    /**
     * AddressLine1 to be printed in the presentation
     * @return Address Line1
     */    
    public ArrayList getAddressLine1() {
        return addressLine1;
    }

    /**
     * AddressLine1 to be printed in the presentation
     * @param addressLine1 
     */
    
    public void setAddressLine1(ArrayList addressLine1) {
        this.addressLine1 = addressLine1;
    }
    
     /**
      * returns localId
     */
    public String getLocalId() {
        return localId;
    }
    
    /**
     * Sets the LocalId parameter for the search
     * @param localId
     */
    
    public void setLocalId(String localId) {
        this.localId = localId;
    }
    /**
      * returns weight
     */
    
    public String getWeight() {
        return weight;
    }
    /**
     * Sets the Weight parameter for the search
     * @param weight
     */
    
    public void setWeight(String weight) {
        this.weight = weight;
    }

    public HashMap getDynamicResultsFields() {
        return dynamicResultsFields;
    }

    public void setDynamicResultsFields(HashMap dynamicResultsFields) {
        this.dynamicResultsFields = dynamicResultsFields;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getAssumedMatchId() {
        return assumedMatchId;
    }

    public void setAssumedMatchId(String assumedMatchId) {
        this.assumedMatchId = assumedMatchId;
    }

}

