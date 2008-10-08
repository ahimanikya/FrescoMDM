/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author wee
 * @version 
 */
public class DomainForWebManager {
    
    private String mDomainName = null;
    
    private ArrayList<SimpleSearchType> mSearchType = new ArrayList<SimpleSearchType>();
    
    private ArrayList<SearchDetail> mSearchDetail = new ArrayList<SearchDetail>();
    
    private ArrayList<RecordDetail> mRecordDetailList = new ArrayList<RecordDetail>();
    
    public DomainForWebManager(String name) {
        this.mDomainName = name;
    }
    
    public String getDomainName() {
        return this.mDomainName;
    }
    
    public ArrayList<SimpleSearchType> getSearchType() {
        return this.mSearchType;
    }
    
    public ArrayList<SearchDetail> getSearchDetail() {
        return this.mSearchDetail;
    }
        
    public ArrayList<RecordDetail> getRecordDetailList() {
        return this.mRecordDetailList;
    }
    
    public void setRecordDetailList(ArrayList<RecordDetail> recordDetailList) {
         mRecordDetailList = recordDetailList;
        //this.mRecordDetail = recordDetail;
    }
    
    public void addRecordDetail(RecordDetail recordDetail) {
        mRecordDetailList.add(recordDetail);
    }
    
    public void deleteRecordDetail(RecordDetail recordDetail) {
        for (RecordDetail recDet : mRecordDetailList) {
            if (recDet.getRecordDetailId() == recordDetail.getRecordDetailId()) {
                mRecordDetailList.remove(recDet);
                break;
            }
        }
    }
    
    public void addSearchType(String screenTitle, SimpleSearchType searchType) {
        mSearchType.add(searchType);
        //mSearchType.put(screenTitle, searchType);
    }
    
    public void deleteSearchType(String screenTitle) throws Exception {
        for (SimpleSearchType searchType : mSearchType) {
            if (searchType.getScreenTitle().equals(screenTitle)) {
                mSearchType.remove(searchType);
                break;
            }
        }
    }
    
    public void addSearchDetail(String domainName, int searchResultID, SearchDetail searchDetailObj) {
        mSearchDetail.add(searchDetailObj);
        //mSearchDetail.put(domainName + "=" + searchResultID, searchDetailObj);
    }
    
    public void deleteSearchDetail(String domainName, int searchResultID) throws Exception {
        for (SearchDetail  searchDetail : mSearchDetail) {
            if (searchDetail.getSearchResultID() == searchResultID) {
                mSearchDetail.remove(searchDetail);
                break;
            }
        }
    }
    
}
