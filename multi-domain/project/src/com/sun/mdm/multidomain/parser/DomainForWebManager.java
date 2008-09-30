/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

import java.util.HashMap;

/**
 *
 * @author wee
 * @version 
 */
public class DomainForWebManager {
    
    private String mDomainName = null;
    
    private HashMap mSearchType = new HashMap();
    
    private HashMap mSearchDetail = new HashMap();
    
    private RecordDetail mRecordDetail;
    
    public DomainForWebManager(String name) {
        this.mDomainName = name;
    }
    
    public String getDomainName() {
        return this.mDomainName;
    }
    
    public HashMap getSearchType() {
        return this.mSearchType;
    }
    
    public HashMap getSearchDetail() {
        return this.mSearchDetail;
    }
    
    public RecordDetail getRecordDetail() {
        return this.mRecordDetail;
    }
    
    public void setRecordDetail(RecordDetail recordDetail) {
        this.mRecordDetail = recordDetail;
    }
    
    public void addSearchType(String screenTitle, SimpleSearchType searchType) {
        mSearchType.put(screenTitle, searchType);
    }
    
    public void deleteSearchType(String screenTitle) throws Exception {
        if (mSearchType.containsKey(screenTitle)) {
            mSearchType.remove(screenTitle);
        } else {
            throw new Exception ("Could not find SearchType [" + screenTitle + "] to delete.");
        }
    }
    
    public void addSearchDetail(String domainName, int searchResultID, SearchDetail searchDetailObj) {
        mSearchDetail.put(domainName + "=" + searchResultID, searchDetailObj);
    }
    
    public void deleteSearchDetail(String domainName, String searchResultID) throws Exception {
        if (mSearchDetail.containsKey(domainName + "=" + searchResultID)) {
            mSearchDetail.remove(domainName + "=" + searchResultID);
        } else {
            throw new Exception ("Could not find Search Detail for Domain[" + domainName + "] to delete.");
        }
    }
    
}
