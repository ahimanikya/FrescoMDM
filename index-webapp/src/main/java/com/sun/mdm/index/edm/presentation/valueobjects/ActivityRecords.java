/*
 * ReportHandler.java
 * Value Object for the Report:Assume Matches Records
 * Created on November 23, 2007, 8:12 PM
 *
 */
package com.sun.mdm.index.edm.presentation.valueobjects;

/**
 *
 * @author admin
 */
public class ActivityRecords {
 
     /**
     * Activity Day to be displayed on the JSF page
     */
    private String day = null;
     /**
     * Activity Date to be displayed on the JSF page
     */
    private String activityDate = null;   
    
      /**
     * ActivityAddTransactions to be displayed on the JSF page
     */
    private String addTransactions = null;   
    /**
     * ActivityEUID Deactivate Transactions to be displayed on the JSF page
     */
    private String euidDeactivateTrans= null; 
     /**
     * Activity EUID Merged Transactions to be displayed on the JSF page
     */
    private String euidMergedTrans= null; 
     /**
     * Activity LID Merged Transactions to be displayed on the JSF page
     */
    private String lidMergedTrans= null; 
     /**
     * Activity LID UnMerged Transactions to be displayed on the JSF page
     */
    private String lidUnMergedTrans= null; 
     /**
     * Activity LID Unmerged Transactions to be displayed on the JSF page
     */
    private String euidUnmergedTrans= null; 
     /**
     * Activity Unresolved Potential Duplicates to be displayed on the JSF page
     */
    private String unresolvedPotentialDup= null; 
     /**
     * Activity Resolved Potential Duplicates to be displayed on the JSF page
     */
    private String resolvedPotentialDup= null;
    
     /**
     * Activity LID Transfer to be displayed on the JSF page
     */
    private String lidTransfer= null;

     /**
     * Activity update count to be displayed on the JSF page
     */
    private String updateCount = null;

    /**
     * Daily Totals
     */
    private String totals = null;
    
     /**
     * Get Date
     */
    private String date = null;
    
     /**
      * returns day
     */
    
    public String getDay() {
        return day;
    }
    /**
     * Sets the Day parameter for the search
     * @param day
     */
    public void setDay(String day) {
        this.day = day;
    }
    
     /**
      * returns activityDate
     */
    
    public String getActivityDate() {
        return activityDate;
    }
    /**
     * Sets the ActivityDate parameter for the search
     * @param activityDate
     */
    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }
    
     /**
      * returns Transactions of Activity Records
     */
    
    public String getAddTransactions() {
        return addTransactions;
    }
    /**
     * Sets the Transactions parameter for the search
     * @param addTransactions
     */
    public void setAddTransactions(String addTransactions) {
        this.addTransactions = addTransactions;
    }
    
     /**
      * returns EuidDeactivatedTransactions
     */
    
    public String getEuidDeactivateTrans() {
        return euidDeactivateTrans;
    }
    /**
     * Sets the EUIDDeactivatedTransactions parameter for the search
     * @param euidDeactivateTrans
     */
    public void setEuidDeactivateTrans(String euidDeactivateTrans) {
        this.euidDeactivateTrans = euidDeactivateTrans;
    }
    
     /**
      * returns euidMergedTrans
     */
    
    public String getEuidMergedTrans() {
        return euidMergedTrans;
    }
    /**
     * Sets the EUIDMergedTransactions parameter for the search
     * @param euidMergedTrans
     */
    public void setEuidMergedTrans(String euidMergedTrans) {
        this.euidMergedTrans = euidMergedTrans;
    }
    
     /**
      * returns lidMergedTrans
     */
    
    public String getLidMergedTrans() {
        return lidMergedTrans;
    }
    /**
     * Sets the LIDMergedTransactions parameter for the search
     * @param lidMergedTrans
     */
    public void setLidMergedTrans(String lidMergedTrans) {
        this.lidMergedTrans = lidMergedTrans;
    }
    
     /**
      * returns euidUnmergedTrans
     */
    
    public String getEuidUnmergedTrans() {
        return euidUnmergedTrans;
    }
    /**
     * Sets the EuidUnmergedTransactions parameter for the search
     * @param euidUnmergedTrans
     */
    public void setEuidUnmergedTrans(String euidUnmergedTrans) {
        this.euidUnmergedTrans = euidUnmergedTrans;
    }
    
     /**
      * returns unresolvedPotentialDup
     */
    
    public String getUnresolvedPotentialDup() {
        return unresolvedPotentialDup;
    }
    /**
     * Sets the UnresolvedPotentialDuplicates parameter for the search
     * @param unresolvedPotentialDup
     */
    public void setUnresolvedPotentialDup(String unresolvedPotentialDup) {
        this.unresolvedPotentialDup = unresolvedPotentialDup;
    }
    
     /**
      * returns resolvedPotentialDup
     */
    public String getResolvedPotentialDup() {
        return resolvedPotentialDup;
    }
    /**
     * Sets the ResolvedPotentialDuplicates parameter for the search
     * @param resolvedPotentialDup
     */
    public void setResolvedPotentialDup(String resolvedPotentialDup) {
        this.resolvedPotentialDup = resolvedPotentialDup;
    }

    /**
     * 
     * @return LID UMerge
     */
    public String getLidUnMergedTrans() {
        return lidUnMergedTrans;
    }

    /**
     * Sets LID UnMerge
     * @param lidUnMergedTrans
     */
    public void setLidUnMergedTrans(String lidUnMergedTrans) {
        this.lidUnMergedTrans = lidUnMergedTrans;
    }

    /**
     * get the Daily Totals
     * @return
     */
    public String getTotals() {
        return totals;
    }

    /**
     * Set Daily totals
     * @param totals
     */
    public void setTotals(String totals) {
        this.totals = totals;
    }

    /**
     * Get the Date
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * Set Date
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    public String getLidTransfer() {
        return lidTransfer;
    }

    public void setLidTransfer(String lidTransfer) {
        this.lidTransfer = lidTransfer;
    }

    public String getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(String updateCount) {
        this.updateCount = updateCount;
    }
}

