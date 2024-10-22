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

package com.sun.mdm.index.util;

/**
 * JNDI name constants
 * @author  ckuo
 * @version $Revision: 1.1 $
 */
public interface JNDINames {
    /** JNDI name for the MasterControllerRemote */ 
    public static final String EJB_REF_MASTER ="java:comp/env/ejb/MasterControllerRemote";
    /** JNDI name for the MasterControllerLocal */ 
    public static final String EJB_LOCAL_REF_MASTER ="java:comp/env/ejb/MasterControllerLocal";
    /** JNDI name for the MasterControllerLocal */ 
    public static final String EJB_SEQUENCE ="java:comp/env/ejb/SequenceEJBLocal";
    /** JNDI name for the PageDataRemote */    
    public static final String EJB_REF_PAGEDATA ="java:comp/env/ejb/PageDataRemote";
    /** JNDI name for the CodeLookupRemote */    
    public static final String EJB_REF_CODELOOKUP ="java:comp/env/ejb/CodeLookupRemote";
    /** JNDI name for the ReportGeneratorRemote */    
    public static final String EJB_REF_REPORTGENERATOR ="java:comp/env/ejb/ReportGeneratorRemote";
    /** JNDI name for the BatchReportGeneratorRemote */    
    public static final String EJB_REF_BATCHREPORTGENERATOR ="java:comp/env/ejb/BatchReportGeneratorRemote";
    /** JNDI name for the main data source */    
    public static final String BBE_DATASOURCE = "java:comp/env/jdbc/BBEDataSource";
    /** JNDI name for the sequence data source */    
    public static final String SEQ_DATASOURCE = "java:comp/env/jdbc/SEQDataSource";
    /** JNDI name for the XA data source */    
    public static final String BBE_XADATASOURCE = "java:comp/env/jdbc/XABBEDataSource";
    /** JNDI name for the OuntBound TopicConnectionFactory */    
    public static final String OUTBOUND_TOPIC_CONN_FACTORY = "java:comp/env/jms/outBoundSender";

}
