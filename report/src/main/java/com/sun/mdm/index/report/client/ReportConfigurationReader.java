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
import java.util.Stack;
import org.xml.sax.Attributes;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;


/**
 * @author jwu
 * @version
 */
public class ReportConfigurationReader {

    private static final String TAG_CONFIGURATION = "configuration";    
    private static final String TAG_APPLICATION = "MasterIndexReport.configuration.application";
    private static final String TAG_APPSERVER = "MasterIndexReport.configuration.appserver";
    private static final String TAG_OUTPUT_FOLDER = "MasterIndexReport.configuration.output-folder";

    private String mAppServer = null;
    private String mApplication = null;
    private String mOutputFolder = null;

    private final Stack mObjectStack;
    // logger
    private final Logger mLogger = LogUtil.getLogger(this);

    private ReportSaxReader mReader;
    private ReportConfiguration mRptConfig = null;
    /**
     * Creates new DBConfigReader
     */
    public ReportConfigurationReader() {
        mObjectStack = new Stack();
        mReader = new ReportSaxReader();
    }

    /** Retrieve the report configuration
     *
     * @returns  the report configuration
     */
    public ReportConfiguration getReportConfig()  {
        return mRptConfig;
    }
    /**
     * Set the file path of the database xml
     * @param sFile file path of the database xml
     * @throws Exception thrown by DbSaxReader
     */
    public void setInputXml(String sFile) throws Exception {
        if (mReader != null) {
            mReader.setInputFile(sFile);
        }
    }

    /**
     * setup the handler for each node that needs to be processed
     */
    public void setupHandlers() {

        mReader.addHandler("MasterIndexReport.configuration.*", 
            new ElementReader() {
        	
    	    	private String mData = "";
    	    
    	    	public void onData(String nodeName, String data) {
    	    		if ( data != null ) {
    	    			mData = mData + data.trim();
    	    		}
    	    	}

    	    	public void onEndElement(String nodeName) {
    	    		if ( ( mData != null ) && ( !mData.trim().equals("") ) )  {
    	    			if (TAG_APPSERVER.equals(nodeName)) {
    	    				mRptConfig.setApplicationServer(mData);
    	    			} else if (TAG_APPLICATION.equals(nodeName)) {
    	    				mRptConfig.setApplication(mData);
    	    			} else if (TAG_OUTPUT_FOLDER.equals(nodeName)) {
    	    				mRptConfig.setReportOutputFolder(mData);
    	    			}
    	    		}
    	    		mData = "";
    	    	}

    	    	public void onStartElement(String nodeName, Attributes attrs) {
    	    		mData = "";
    	    	}
        	
            });

        mReader.addHandler("MasterIndexReport.reports.report",
            new ElementReader() {
                public void onData(String nodeName, String data) {
                }

                public void onEndElement(String nodeName) {
                    ReportDefinition rpt = (ReportDefinition) mObjectStack.pop();
                    if (rpt != null) {
                        mRptConfig.addReportDefinition(rpt);
                    } else {
                    }
                }

                public void onStartElement(String nodeName, Attributes attrs) {
                    ReportDefinition rpt = new ReportDefinition();
                    for (int i = 0; i < attrs.getLength(); i++) {
                        String attrName = attrs.getLocalName(i);
                        if ("".equals(attrName)) {
                            attrName = attrs.getQName(i);
                        }
                        if ("name".equals(attrName)) {
                            rpt.setReportName(attrs.getValue(i));
                        } else if ("template".equals(attrName)) {
                            rpt.setReportTemplate(attrs.getValue(i));
                        }
                    }
                    mObjectStack.push(rpt);
                }
            });

        mReader.addHandler("MasterIndexReport.reports.report.*",
            new ElementReader() {

        	private String mData = "";
            
                public void onData(String nodeName, String data) {
                	if ( data != null ) {
                		mData = mData + data.trim();
                	}
                }

                public void onEndElement(String nodeName) {
                    ReportDefinition rpt = (ReportDefinition) mObjectStack.peek();

                    if (  ( mData!= null ) && ( !mData.trim().equals("") ) ) {
                    	if ("MasterIndexReport.reports.report.enable".equals(nodeName)) {
                        	rpt.setReportEnable(Boolean.valueOf(mData).booleanValue());
                    	} else if ("MasterIndexReport.reports.report.output-file".equals(nodeName)) {
                        	rpt.setReportOutput(mData);
                    	} else if ("MasterIndexReport.reports.report.max-result-size".equals(nodeName)) {
                			rpt.setMaxResultSize(Integer.valueOf(mData));
                	} else if ("MasterIndexReport.reports.report.page-size".equals(nodeName)) {//added for 88411
                            rpt.setPageSize(Integer.valueOf(mData));
                		}
                    	mData = "";                
                    }
                }

                public void onStartElement(String nodeName, Attributes attrs) {
                    mData = "";                	
                }
        	
            });

        mReader.addHandler("MasterIndexReport.reports.report.criteria.status",
                new ElementReader() {
        	
        	private String mData = "";
            
        	public void onData(String nodeName, String data) {
        		if ( data != null ) {
        			mData = mData + data.trim();
        		}
        	}

            public void onEndElement(String nodeName) {
                ReportDefinition rpt = (ReportDefinition) mObjectStack.peek();
                
                if (  ( mData!= null ) && ( !mData.trim().equals("") ) ) {
                	rpt.setStatus(mData);
                }
                mData = "";
            }

            public void onStartElement(String nodeName, Attributes attrs) {
                mData = "";
            }
        });
        
        mReader.addHandler("MasterIndexReport.reports.report.criteria.dates",
            new ElementReader() {
                public void onData(String nodeName, String data) {
                }

                public void onEndElement(String nodeName) {
                }

                public void onStartElement(String nodeName, Attributes attrs) {
                    ReportDefinition rpt = (ReportDefinition) mObjectStack.peek();
                    String type = null;
                    String fromDate = null;
                    String toDate = null;

                    for (int i = 0; i < attrs.getLength(); i++) {
                        String attrName = attrs.getLocalName(i);
                        if ("".equals(attrName)) {
                            attrName = attrs.getQName(i);
                        }
                        if ("type".equals(attrName)) {
                            type = attrs.getValue(i);
                        } else if ("from-date".equals(attrName)) {
                            fromDate = attrs.getValue(i);
                        } else if ("to-date".equals(attrName)) {
                            toDate = attrs.getValue(i);
                        } 
                    }
                    if ("range".equalsIgnoreCase(type)) {
                        try {
                            rpt.setDateRange(fromDate, toDate);
                        } catch (ConfigurationException e) {
                            mLogger.error(e.getMessage(), e);
                        }
                    } else {
                        try {
                            rpt.setDateRange(type);
                        } catch (ConfigurationException e) {
                            mLogger.error(e.getMessage(), e);
                        }
                    }
                }
            });

        mReader.addHandler("MasterIndexReport.reports.report.fields.field",
            new ElementReader() {
                public void onData(String nodeName, String data) {
                }

                public void onEndElement(String nodeName) {
                }

                public void onStartElement(String nodeName, Attributes attrs) {
                    String path = null;
                    String label = null;
                    String width = null;
                    ReportDefinition rpt = (ReportDefinition) mObjectStack.peek();
                    for (int i = 0; i < attrs.getLength(); i++) {
                        String attrName = attrs.getLocalName(i);
                        if ("".equals(attrName)) {
                            attrName = attrs.getQName(i);
                        }
                        if ("path".equals(attrName)) {
                            path = attrs.getValue(i);
                        } else if ("label".equals(attrName)) {
                            label = attrs.getValue(i);
                        } else if ("width".equals(attrName)) {
                            width = attrs.getValue(i);
                        } 
                    }
                    ReportField fld = new ReportField(path, label, Integer.parseInt(width));
                    try {
                        rpt.addReportField(fld);
                    } catch (ConfigurationException e) {
                        throw new RuntimeException("Error in adding ReportField: " 
                            + e.getMessage());
                    }
                }
            });
    }

    /**
     * Start the parser and return the ReportConfiguration
     * @return ReportConfiguration
     * @throws Configuration Exception 
     */
    public ReportConfiguration parse() throws ConfigurationException {
        mRptConfig = new ReportConfiguration();
        if (mReader != null) {
            mReader.parse();
        }
        return mRptConfig;
    }

    public static void main(String args[]) {
        
        String sInputXml = "C:\\Hawaii\\eView\\report\\config\\report.xml";
        ReportConfigurationReader cfgReader = new ReportConfigurationReader();
        ReportConfiguration config = null;
        
        try {
            cfgReader.setInputXml(sInputXml);
            cfgReader.setupHandlers();
            config = cfgReader.parse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        
        ArrayList rpts = config.getReportDefinitions();
        for (int i=0; i < rpts.size(); i++) {
            ReportDefinition def = (ReportDefinition) rpts.get(i);
            System.out.println(def.toString());
        }
        
    }

}
