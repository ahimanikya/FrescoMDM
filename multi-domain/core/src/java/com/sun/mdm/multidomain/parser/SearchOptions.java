/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

import java.util.ArrayList;

/**
 *
 * @author wee
 */
public class SearchOptions {
    
    private String mQueryBuilder = null;
    
    private boolean mWeighted = false;
    
    private ArrayList<Parameter> mParameterList = new ArrayList<Parameter>();
    
    public SearchOptions() {        
    }
    
    public SearchOptions(String queryBuilder, boolean weighted) {
        this.mQueryBuilder = queryBuilder;
        this.mWeighted = weighted;
    }
    
    public void setQueryBuilder(String queryBuilder) {
        this.mQueryBuilder = queryBuilder;
    }
    
    public String getQueryBulder() {
        return this.mQueryBuilder;
    }
    
    public void setWeighted(boolean weighted) {
        this.mWeighted = weighted;
    }
    
    public boolean getWeighted() {
        return this.mWeighted;
    }
    
    public ArrayList<Parameter> getParameterList() {
        return this.mParameterList;
    }
    
    public void setParameterList(ArrayList<Parameter> params) {
        this.mParameterList = params;
    }
    
    public void addParameter(String name, String value) {
        Parameter param = new Parameter(name, value);
        mParameterList.add(param);
    }
    
    public void deleteParameter(String name) {
        for (Parameter param : mParameterList) {
            if (param.getName().equals(name)) {
                mParameterList.remove(param);
                break;
            }
        }
    }
        
    public Parameter createParameter(String name, String value) {
        return new Parameter(name, value);
    }
    
    
    public class Parameter{
        
        private String mName;
        
        private String mValue;
        
        public Parameter(String name, String value) {
            this.mName = name;
            this.mValue = value;
        }
        
        public String getName() {
            return this.mName;
        }
        
        public String getValue() {
            return this.mValue;
        }
        
        public void setName(String name) {
            this.mName = name;
        }
        
        public void setValue(String value) {
            this.mValue = value;
        }        
        
        
    }

}
