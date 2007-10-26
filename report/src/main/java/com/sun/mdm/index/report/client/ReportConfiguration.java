/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */
package com.sun.mdm.index.report.client;

import java.util.ArrayList;

/**
 *
 * @author  jwu
 */
public class ReportConfiguration {
    
    private String mAppServer = null;
    private String mApplication = null;
    private String mOutputFolder = null;
    private ArrayList mReportDefinitions = null;

    /** Creates a new instance of ReportConfiguration */
    public ReportConfiguration() {
        mReportDefinitions = new ArrayList();
    }
    
    /**
     * Set the application name 
     * @parm String application
     */
    public void setApplication(String application) {
        mApplication = application;
    }

    /**
     * Get the application name 
     * @return application name
     */
    public String getApplication() {
        return mApplication;
    }

    /**
     * Get the report output folder
     * @return output folder
     */
    public void setReportOutputFolder(String outputFolder) {
        mOutputFolder = outputFolder;
    }

    /**
     * Get the report output folder
     * @return output folder
     */
    public String getReportOutputFolder() {
        return mOutputFolder;
    }


    /**
     * Set the name of the application server
     * @parm the name of the application server
     */
    public void setApplicationServer(String applicationServer) {
        mAppServer = applicationServer;
    }

    /**
     * Get the name of the application server
     * @return the name of the application server
     */
    public String getApplicationServer() {
        return mAppServer;
    }

    /**
     * Add a report definition to the configuration
     * @parm def ReportDefinition 
     */
    public void addReportDefinition(ReportDefinition def) {
        mReportDefinitions.add(def);
    }

    /**
     * Get the report definitions of the configuration
     * @return ArrayList reprot definitions 
     */
    public ArrayList getReportDefinitions() {
        return mReportDefinitions;
    }
}
