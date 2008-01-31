

package com.sun.mdm.index.edm.presentation.valueobjects;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Sridhar Narsingh
 */
public class Transaction {
    /**
     * Search Transaction Id
     */
    private String transactionId = null;
    /**
     * Search Start Date
     */
    private String createStartDate = null;
    /**
     * Search End Date
     */
    private String createEndDate = null;    
    /**
     * Search Start Time
     */ 
    private String createStartTime = null;
    /**
     * Search end Time
     */
    private String createEndTime = null;    
    /**
     * Search Local ID
     */
    private String localid = null;
    /**
     * Search EUID
     */
    private String euid = null;
    /**
     * Search System User
     */
    private String systemUser = null;    
    /**
     * Search Function
     */
    private String function = null;    

    /**
     * Search Transaction Source
     */    
    private String source = null;

    /**
     * Search Transaction Source
     */    
    private Date transactionDate = null;

    /**
     * Transaction First Name 
     */    
    private String firstName  = "";

    private ArrayList eoArrayList = new ArrayList();

    private int eoArrayListSize = 0;
    /**
     * Search Last Name
     */    
    private String lastName = "";
    
    /**
     * @return createStartDate
     */
    public String getCreateStartDate() {
        return createStartDate;
    }

    /**
     * @param createStartDate
     * Sets the Start Date
     */
    public void setCreateStartDate(String createStartDate) {
        this.createStartDate = createStartDate;
    }

    /**
     * @return Create End Date
     */
    public String getCreateEndDate() {
        return createEndDate;
    }

    /**
     * Sets the End date parameter for the search
     * @param createEndDate
     */
    public void setCreateEndDate(String createEndDate) {
        this.createEndDate = createEndDate;
    }

    /**
     * @return create Start Date
     */
    public String getCreateStartTime() {
        return createStartTime;
    }

    /**
     * Sets the Start timeparameter for the search
     * @param createStartTime 
     */
    public void setCreateStartTime(String createStartTime) {
        this.createStartTime = createStartTime;
    }

    /**
     * @return Create End time
     */
    public String getCreateEndTime() {
        return createEndTime;
    }

    /**
     * Sets the End time parameter for the search
     * @param createEndTime 
     */
    public void setCreateEndTime(String createEndTime) {
        this.createEndTime = createEndTime;        
    }

    /**
     * @return Local Id
     */
    public String getLocalid() {
        return localid;
    }

    /**
     * Sets the Local ID parameter for the search
     * @param localid
     */
    public void setLocalid(String localid) {
        this.localid = localid;
    }

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
     * @return System User
     */
    public String getSystemUser() {
        return systemUser;
    }

    /**
     * Sets the System User parameter for the search
     * @param systemUser
     */
    public void setSystemUser(String systemUser) {
        this.systemUser = systemUser;
    }

    /**
     * @return function
     */
    public String getFunction() {
        return function;
    }

    /**
     * Sets the function parameter for the search
     * @param function
     */
    public void setFunction(String function) {
        this.function = function;
    }
	
	
    /**
     * @return source 
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the function parameter for the search
     * @param source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return Transaction ID
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the Set transaction ID 
     * @param id
     */
    public void setTransactionId(String id) {
        this.transactionId = id;
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
     * Transaction Date
     * @return Transaction Date
     */
    public Date getTransactionDate() {
        return transactionDate;
    }

    /**
     * Set Transaction Date
     * @param transactionDate
     */
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public ArrayList getEoArrayList() {
        return eoArrayList;
    }

    public void setEoArrayList(ArrayList eoArrayList) {
        this.eoArrayList = eoArrayList;
    }

    public int getEoArrayListSize() {
        return eoArrayListSize;
    }

    public void setEoArrayListSize(int eoArrayListSize) {
        this.eoArrayListSize = eoArrayListSize;
    }
    
}
