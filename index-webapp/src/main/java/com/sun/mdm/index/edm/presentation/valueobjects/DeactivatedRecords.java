/*
 * ReportHandler.java
 * Value Object for the Report:Deactivated Records
 * Created on November 23, 2007, 8:12 PM
 *
 */

package com.sun.mdm.index.edm.presentation.valueobjects;


public class DeactivatedRecords {

    /**
     * EUID to be displayed on the JSF page
     */
    private String euid = null;
    
    /**
     * Deactivated First Name 
     */    
    private String firstName  = null;


    /**
     * Deactivated Last Name
     */    
    private String lastName = null;
    
    /**
     * Deactivated SSN 
     */    
    private String ssn  = null;
    
    /**
     * Deactivated DOB 
     */    
    private String dob  = null;

    /**
     * Deactivated addressLine1
     */    
    private String addressLine1  = null;
    
    /**
     * Deactivated deactivatedDate
     */    
    private String deactivatedDate  = null;
    
    
    /**
     * Deactivated deactivated Time
     */    
    private String deactivatedTime  = null;
                   
    /**
     * Deactivated description
     */    
    private String description  = null;
    
    
    
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
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the First Name
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the Last Name
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * * Sets the Last Name
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * SSN to be printed in the presentation
     * @return
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * SSN to be printed in the presentation
     * @param ssn 
     */
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    /**
     * DOB to be printed in the presentation
     * @return DOB
     */    
    public String getDob() {
        return dob;
    }

    /**
     * DOB to be printed in the presentation
     * @param dob 
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    
    /**
     * AddressLine1 to be printed in the presentation
     * @return Address Line1
     */    
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * AddressLine1 to be printed in the presentation
     * @param addressLine1 
     */
    
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    /**
     * deactivatedDate to be printed in the presentation
     * @return deactivatedDate
     */    
    public String getDeactivatedDate() {
        return deactivatedDate;
    }

    /**
     * deactivatedDate to be printed in the presentation
     * @param deactivatedDate 
     */
    public void setDeactivatedDate(String deactivatedDate) {
        this.deactivatedDate = deactivatedDate;
    }

    /**
     * deactivatedDate to be printed in the presentation
     * @return deactivatedTime
     */    
    public String getDeactivatedTime() {
        return deactivatedTime;
    }

    /**
     * deactivatedTime to be printed in the presentation
     * @param deactivatedtime 
     */
    public void setDeactivatedTime(String deactivatedtime) {
        this.deactivatedTime = deactivatedtime;
    }

    /**
     * Description to be printed in the presentation
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


