/*
 * ReportHandler.java
 * Value Object for the Report:Update Records
 * Created on November 23, 2007, 8:12 PM
 *
 */

package com.sun.mdm.index.edm.presentation.valueobjects;

import java.util.ArrayList;

/**
 *
 * @author Sridhar Narsingh
 */
public class UpdateRecords {
    /**
     * EUID to be displayed on the JSF page
     */
    private ArrayList euid = new ArrayList();
    
    /**
     * Update First Name 
     */    
    private ArrayList firstName  = new ArrayList();


    /**
     * Update Last Name
     */    
    private ArrayList lastName =  new ArrayList();
    
    /**
     * Update SSN 
     */    
    private ArrayList ssn  =  new ArrayList();
    
    /**
     * Update DOB 
     */    
    private ArrayList dob  =  new ArrayList();

    /**
     * Update addressLine1
     */    
    private ArrayList addressLine1  =  new ArrayList();
    
    /**
     * Update updateDate
     */    
    private String updateDate  = null;
    
    
    /**
     * Update  updateTime
     */    
    private String updateTime  = null;
    
    /**
     * Update description
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
     * @return updateDate
     */    
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * Date to be printed in the presentation
     * @param updateDate 
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * UpdateTime to be printed in the presentation
     * @return UpdateTime
     */    
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * Time to be printed in the presentation
     * @param updatetime 
     */
    public void setUpdateTime(String updatetime) {
        this.updateTime = updatetime;
    }

    /**
     * updateDescription to be printed in the presentation
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

