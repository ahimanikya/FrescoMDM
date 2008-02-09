/*
 * ReportHandler.java
 * Value Object for the Report:Merged Records
 * Created on November 23, 2007, 8:12 PM
 *
 */

package com.sun.mdm.index.edm.presentation.valueobjects;

import java.util.ArrayList;

/**
 * @author Sridhar Narsingh
 */
public class MergedRecords {

    /**
     * EUID to be displayed on the JSF page
     */
    private ArrayList euid = new ArrayList();
    
    /**
     * Merged First Name 
     */    
    private ArrayList firstName  = new ArrayList();


    /**
     * Merged Last Name
     */    
    private ArrayList lastName = new ArrayList();
    
    /**
     * Merged SSN 
     */    
    private ArrayList ssn  = new ArrayList();
    
    /**
     * Merged DOB 
     */    
    private ArrayList dob  = new ArrayList();

    /**
     * Merged addressLine1
     */    
    private ArrayList addressLine1  = new ArrayList();
    
    /**
     * Merged mergedDate
     */    
    private String mergedDate  = null;
    
    
    /**
     * Merged  mergedTime
     */    
    private String mergedTime  = null;
    
    /**
     * Merged description
     */    
    private String description  = null;
    
    
    
    /**
     * @return EUID
     */
    public ArrayList getEuid() {
        return euid;
    }

    /**
     * Sets the EUID parameter for the search
     * @param euid
     */
    public void setEuid(ArrayList euid) {
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
     * mergedDate to be printed in the presentation
     * @return mergedDate
     */    
    public String getMergedDate() {
        return mergedDate;
    }

    /**
     * Date to be printed in the presentation
     * @param mergedDate 
     */
    public void setMergedDate(String mergedDate) {
        this.mergedDate = mergedDate;
    }

    /**
     * mergedTime to be printed in the presentation
     * @return mergedTime
     */    
    public String getMergedtime() {
        return mergedTime;
    }

    /**
     * Time to be printed in the presentation
     * @param mergedtime 
     */
    public void setMergedtime(String mergedtime) {
        this.mergedTime = mergedtime;
    }

    /**
     * mergedDescription to be printed in the presentation
     * @return description
     */    
    public String getDescription() {
        return description;
    }

    /**
     * Description to be printed in the presentation
     * @param description 
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
}

