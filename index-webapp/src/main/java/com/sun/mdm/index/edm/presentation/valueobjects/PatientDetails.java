/*
 * PatientDetails.java
 * Value Object for Patient Details
  *
 */

package com.sun.mdm.index.edm.presentation.valueobjects;

/**
 * @author admin
 */
public class PatientDetails {

    /**
     * EUID to be displayed on the JSF page
     */
    private String euid = null;
    
     
    /**
     * PatientDetails First Name 
     */    
    private String firstName  = null;


    /**
     * PatientDetails Last Name
     */    
    private String lastName = null;
    
    /**
     * PatientDetails SSN 
     */    
    private String ssn  = null;
    
    /**
     * PatientDetails DOB 
     */    
    private String dob  = null;

    /**
     * PatientDetails addressLine1
     */    
    private String addressLine1  = null;
   
    
   
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
   
}

