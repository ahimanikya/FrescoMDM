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
package com.sun.mdm.index.master;

import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.EnterpriseObject;

/**
 * The <b>ExecuteMatchLogics</b> class provides methods that allow you to customize
 * how the matching process is performed by any of the "execute match" methods of the
 * <b>MasterController</b> class. To incorporate the custom methods in an Master Index or
 * eIndex Project, create custom plug-ins for the Project. <b>executeMatch</b>
 * automatically checks the Threshold configuration file for the name of the custom
 * plug-ins that contain these custom processing methods. If no custom plug-ins are
 * defined, the value for each method defaults to false.
 */
public class ExecuteMatchLogics {

    /**
     * Creates a new instance of the ExecuteMatchLogics class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DL><DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public ExecuteMatchLogics() {
    }

    /**
     * Specifies to the execute match methods whether to bypass the matching
     * algorithm and insert the incoming record directly into the database
     * or to perform standard match processing on the record.
     * <p>
     * @param so The system object being processed.
     * @return <CODE>boolean</CODE> - An indicator of whether to bypass matching.
     * Specify <b>true</b> to bypass matching and add the record directly;
     * otherwise, specify <b>false</b>.
     * @exception CustomizationException Thrown if there is an error in the custom
     * processing.
     * @include
     */

    public boolean bypassMatching (SystemObject so) throws CustomizationException {
        return false;
    }

    /**
     * Specifies to the execute match methods whether to allow the incoming system
     * object to update a record that already exists in the database by examining only
     * the system object.
     * <p>
     * @param so The system object being processed.
     * @return <CODE>boolean</CODE> - An indicator of whether to allow an update by
     * an incoming record. Specify <b>false</b> to allow record updates;
     * otherwise, specify <b>true</b>.
     * @exception CustomizationException Thrown if there is an error in the custom
     * processing.
     * @include
     */
    public boolean disallowUpdate (SystemObject so) throws CustomizationException {
        return false;
    }

    /**
     * Specifies to the execute match methods whether to allow the incoming record
     * to be added to the database as a new enterprise object.
     * <p>
     * @param so The system object being processed.
     * @return <CODE>boolean</CODE> - An indicator of whether to allow an incoming
     * record to be added. Specify <b>false</b> to allow a record to be added;
     * otherwise, specify <b>true</b>.
     * @exception CustomizationException Thrown if there is an error in the custom
     * processing.
     * @include
     */
    public boolean disallowAdd (SystemObject so) throws CustomizationException {
        return false;
    }

    /**
     * Specifies to the execute match methods whether to reject updates to an existing
     * enterprise record by examining both the system object and the enterprise object
     * to update.
     * <p>
     * @param so The system object being processed.
     * @param eo The enterprise object being processed.
     * @return <CODE>boolean</CODE> - An indicator of whether to reject updates to
     * existing records. Specify <b>true</b> to reject updates;
     * otherwise, specify <b>false</b>.
     * @exception CustomizationException Thrown if there is an error in the custom
     * processing.
     * @include
     */
    public boolean rejectUpdate(SystemObject so, EnterpriseObject eo) throws CustomizationException {
        return false;
    }

    /**
     * Specifies to the execute match method on whether to reject an assumed match
     * after examining the record that is assumed to be a match.
     * <p>
     * @param so The system object being processed.
     * @param eo The enterprise object being processed.
     * @return <CODE>boolean</CODE> - An indicator of whether to reject assumed
     * matches of existing records. Specify <b>true</b> to reject assumed matches;
     * otherwise, specify <b>false</b>.
     * @exception CustomizationException Thrown if there is an error in the custom
     * processing.
     * @include
     */
    public boolean rejectAssumedMatch(SystemObject so, EnterpriseObject eo) throws CustomizationException {
        return false;
    }
}
