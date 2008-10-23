/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

import java.util.ArrayList;

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
    
    private ArrayList<FieldGroup> mRecordIDFields = new ArrayList<FieldGroup>();
    
    private ArrayList<FieldGroup> mRecordSummaryFields = new ArrayList<FieldGroup>();
    
    private int searchDetailSeqValue = 0;
    
    private int recordDetailSeqValue = 0;
    
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
    
    public ArrayList<FieldGroup> getRecordIDFields() {
        return this.mRecordIDFields;
    }
    
    public ArrayList<FieldGroup> getRecordSummaryFields() {
        return this.mRecordSummaryFields;
    }
    
    public void setRecordIDFields(ArrayList<FieldGroup> fieldGroups) {
        mRecordIDFields = fieldGroups;
    }
    
    public void setRecordSummaryFields(ArrayList<FieldGroup> fieldGroups) {
        mRecordSummaryFields = fieldGroups;
    }
    
    public void setRecordDetailList(ArrayList<RecordDetail> recordDetailList) {
         mRecordDetailList = recordDetailList;
        //this.mRecordDetail = recordDetail;
    }
    
    public int generateRecordDetailID() {
        recordDetailSeqValue++;
        return recordDetailSeqValue;
    }
    
    public int generateSearchResultID() {
        searchDetailSeqValue++;
        return searchDetailSeqValue;
    }
    
    public void addRecordDetail(RecordDetail recordDetail) {

        if (recordDetail.getRecordDetailId() > recordDetailSeqValue) {
            recordDetailSeqValue = recordDetail.getRecordDetailId();
        }
        mRecordDetailList.add(recordDetail);
    }
    
    public void addRecordIDField(FieldGroup fieldGroup) {
        mRecordIDFields.add(fieldGroup);
    }
    
    public void addRecordSummary(FieldGroup fieldGroup) {
        this.mRecordSummaryFields.add(fieldGroup);
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

        if (searchDetailSeqValue < searchDetailObj.getSearchResultID()) {
                searchDetailSeqValue = searchDetailObj.getSearchResultID();
        }
        
        mSearchDetail.add(searchDetailObj);
    }
    
    public void deleteSearchDetail(String domainName, int searchResultID) throws Exception {
        for (SearchDetail  searchDetail : mSearchDetail) {
            if (searchDetail.getSearchResultID() == searchResultID) {
                mSearchDetail.remove(searchDetail);
                break;
            }
        }
    }
    
    public boolean isSearchDetailUsed(SearchDetail searchDetail) {
        for (SimpleSearchType searchType : mSearchType) {
            if (searchType.getScreenResultID() == searchDetail.getSearchResultID())
                return true;
        }
        
        return false;
    }
    
    public boolean isRecordDetailUsed(RecordDetail recordDetail) {
        for (SearchDetail searchDet :mSearchDetail) {
            if (searchDet.getRecordDetailID() == recordDetail.getRecordDetailId()) {
                return true;
            }
        }
        
        return false;
    }
    
}
