/*
 * Address.java
 *
 * Created on September 10, 2007, 5:08 PM
 *
 */

package com.sun.mdm.index.edm.presentation.valueobjects;

/**
 *
 * @author Administrator
 */
public class Address {
    
    private String addressLine1;
    private String addressLine2;
    private String  city;
    private String  State;
    private String  zip;
    
    /** Creates a new instance of Address */
    public Address() {
    }
    
    /**
     * Get the the address Line1 for the Address
     * @return Returns <CODE>addressLine1</CODE>
     */
    public String getAddressLine1() {
        return addressLine1;
    }
    
    /**
     * Set the <code>addressLine1</code> of the Address
     * @param addressLine1 The Address Line1 for the Address
     */
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }
    
    /**
     * Get the Address Line 2
     * @return The <code> addressline2 </code> for the Address
     */
    public String getAddressLine2() {
        return addressLine2;
    }
    
    /**
     * 
     * Set the <code>addressLine1</code> of the Address
     * @param addressLine2 Returns <CODE>addressLine1</CODE>
     */
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }
    
    /**
     * 
     * Get the <CODE>city</CODE> for the Address
     * @return <code>city</code> Address City
     */
    public String getCity() {
        return city;
    }
    
    /**
     * 
     * Set the <CODE>city</CODE> for the Address
     * @param city <code>city</code> Address City
     */
    public void setCity(String city) {
        this.city = city;
    }
    
    /**
     * 
     * 
     * Get the <CODE>state</CODE> for the Address
     * @return <code>state</code> Address City
     */
    public String getState() {
        return State;
    }
    
    /**
     * <code>State</code> Address City
     * @param State <code>state</code> Address City
     */
    public void setState(String State) {
        this.State = State;
    }
    
    /**
     * <code>zip</code> Address zip
     * @return <code>zip</code> Address zip
     */
    public String getZip() {
        return zip;
    }
    
    /**
     * 
     * Set Address <code>zip</code> for the Address
     * @param zip <code>city</code> Address City
     */
    public void setZip(String zip) {
        this.zip = zip;
    }
    
}
