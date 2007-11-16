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
package com.sun.mdm.index.ejb.master.helper;

/** Enumerations used by DateUtil format pool
 * @author dcidon
 */
public class EnumDateFormat {
    
    /** Count of format enumerations */
    private static int formatCount = 0;
    /** Date format string */
    public final String dateFormat;
    /** index of format enumeration */
    public final int index;
    
    /** Creates new SearchTypeEnum */
    private EnumDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        this.index = formatCount++;
    }
    
    /** Get count of enumerations
     * @return count
     */
    public static int getFormatCount() {
        return formatCount;
    }
    
    /** Year month day format */
    public static final EnumDateFormat YYYYMMDD = new EnumDateFormat("yyyyMMdd");
    
    /** Year month day hour min sec format */
    public static final EnumDateFormat YYYYMMDDHHMMSS = new EnumDateFormat("yyyyMMddHHmmss");
    
    /** milliseconds format */
    public static final EnumDateFormat YYYYMMDDHHMMSSSSS = new EnumDateFormat("yyyyMMddHHmmssSSS");
    
    /** SQL standard date format */
    public static final EnumDateFormat STANDARD_DATE_TIME = new EnumDateFormat("yyyy-MM-dd HH:mm:ss");
    
}
