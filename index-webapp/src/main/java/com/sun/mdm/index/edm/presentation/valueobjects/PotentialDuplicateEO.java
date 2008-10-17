/*
 * PotentialDuplicateEO.java
 *
 * Created on September 7, 2007, 1:29 PM
 */

package com.sun.mdm.index.edm.presentation.valueobjects;
import java.util.Date;
/**
 * 
 * This class stores the Potential duplicates for the Enterprise Objects
 */
public class PotentialDuplicateEO {
    
    private Integer EUID;
    private String firstName;
    private String middleName;
    private String lastName;
    private char gender; 
    private String language;
    private String SSN;
    private boolean inPatient;
    private Date lastUpdate;    
    private Address homeAddress;
    private Address officeAddress;
            
    
    /** Creates a new instance of PotentialDuplicateEO */
    public PotentialDuplicateEO() {
        
    }


    /**
     * Get the Enterprise Object ID
     * @return <code>firstName</code> for the Enterprise Object
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the firstName of Enterprise Object
     * @param firstName for the Enterprise Object
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the Middle Name for the Enterprise Object
     * @return <code>middleName</code> for the Enterprise Object
     */    
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Set the Middle Name for the Enterprise Object
     * @param middleName for the Enterprise Object
     */    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * 
     * Get the Last name from the Enterprise Object
     * @return <code> lastName</code>
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last Name of the Enterprise Object
     * @param lastName Patient Last Name in the Enterprise Object
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the language of the patient from the Enterprise Object
     * @return <code>language</code>
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Set the language of the patient in the Enterprise Object
     * @param language Set the patient language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Get the social sevurity number of the patient from the Enterprise Object
     * @return <code>Social security number</code>
     */
    public String getSSN() {
        return SSN;
    }

    /**
     * Set the Social security number of the patient in the Enterprise object
     * @param SSN Social security number of the patient
     */
    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    /**
     * Get the home address of the patient
     * @return home Address of the patient in the Enterprise object
     */
    public Address getHomeAddress() {
        return homeAddress;
    }

    /**
     * Set the home address of the patient
     * @param homeAddress of the patient in the Enterprise object
     */
    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    /**
     * Get the office address of the patient
     * @return <code>officeAddress</code> of the patient in the Enterprise object
     */
    public Address getOfficeAddress() {
        return officeAddress;
    }

    /**
     * Set the office address of the patient
     * @param officeAddress of the patient in the Enterprise object
     */
    public void setOfficeAddress(Address officeAddress) {
        this.officeAddress = officeAddress;
    }
    
    /**
     * Get inPatient 
     * @return <code>true</code> if the patient is in Patient in the Enterprise object
     */
    public boolean isInPatient() {
        return inPatient;
    }

    /**
     * Set inPatient
     * @param inPatient true or false depending on, if the patient is in Patient in the Enterprise object
     */
    public void setInPatient(boolean inPatient) {
        this.inPatient = inPatient;
    }

     /**
     * Get lastUpdate
     * @return <code>updateDate</code> of the patient record 
     */    
    public Date getLastUpdate() {
        return lastUpdate;
    }

     /**
     * Set the update date of this patient record
     * @param lastUpdate of the patient record
     */    
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
     /**
     * Get the gender of the patient
     * @return gender of the patient record 
     */    
    public char getGender() {
        return gender;
    }

     /**
     * Set the gender of the patient
     * @param gender of the patient record 
     */    
    
    public void setGender(char gender) {
        this.gender = gender;
    }

    public Integer getEUID() {
        return EUID;
    }

    public void setEUID(Integer EUID) {
        this.EUID = EUID;
    }
}
