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

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.Stack;


/**
 * Date formatting utility
 * @author dcidon
 */
public class DateUtil {
    private static final Stack[] FORMAT_POOL = new Stack[EnumDateFormat.getFormatCount()];
    
    static {
        for (int i = 0; i < FORMAT_POOL.length; i++) {
            FORMAT_POOL[i] = new Stack();
        }
    }
    
    private static final int NANO_LENGTH = 9;
    private static final int FMT_NANO_LENGTH = 6;

    /** Creates new DateUtil */
    public DateUtil() {
    }
    
    /** Get date format class
     * @param format format parameter
     * @return date format class
     */
    public static EiDateFormat getDateFormat(EnumDateFormat format) {
        EiDateFormat retVal = null;
        Stack stack = FORMAT_POOL[format.index];
        synchronized (stack) {
            if (stack.empty()) {
                retVal = new EiDateFormat(format);
            } else {
                retVal = (EiDateFormat) stack.pop();
            }
        }
        return retVal;
    }
    
    /** Release the date format class back into the pool
     * @param eiDateFormat date format class to free
     */
    public static void freeDateFormat(EiDateFormat eiDateFormat) {
        Stack stack = FORMAT_POOL[eiDateFormat.format.index];
        stack.push(eiDateFormat);
    }
    
    /** Get timestamp given date and time.  Note that time can be of any precision
     * from 0 to 12 characters long: HHmmssSSSNNN
     * @param dateYYYYMMDD string date in YYYYMMDD format
     * @param time component 0-12 characters
     * @throws ParseException date or time could not be parsed
     * @return timestamp
     */
    public Timestamp getTimestamp(String dateYYYYMMDD, String time) throws ParseException {
        
        //Construct yyyyMMddHHmmss string, pad with zero if necessary
        StringBuffer sb = new StringBuffer(dateYYYYMMDD);
        final int timeLength = time.length();
        for (int i = 0; i < 6; i++) {
            if (i < timeLength) {
                sb.append(time.charAt(i));
            } else {
                sb.append('0');
            }
        }
        EiDateFormat sdf = getDateFormat(EnumDateFormat.YYYYMMDDHHMMSS);
        Date date = sdf.parse(sb.toString());
        freeDateFormat(sdf);
        Timestamp timestamp = new Timestamp(date.getTime());
        //Read nanoseconds
        int nanoSeconds = 0;
        if (timeLength > 6) {
            //ToDo:  D.C. This does not look right.  Don't we need to
            //pad with zero's to the right to ensure that
            //time.substring(6) returns a 6 char value?
            // Pad zeros to time.substring(6) to get correct nanoseconds value
            StringBuffer nanoStr = new StringBuffer(time.substring(6));
            while (nanoStr.length() < NANO_LENGTH) {
                nanoStr.append("0");
            }
            nanoSeconds = Integer.parseInt(nanoStr.toString());
            timestamp.setNanos(nanoSeconds);
        }
        
        return timestamp;
    }
    
    /** 
     * Get timestamp given string date in YYYYMMDDhhmmss... format
     * @param dateTime string date time
     * @return timestamp
     * @throws ParseException invalid dateTime parameter
     */
    public Timestamp getTimestamp(String dateTime) throws ParseException {
        //For now, just split up date and time components and call overloaded getTimestamp
        String date = dateTime.substring(0, 8);
        String time = dateTime.substring(8);
        return getTimestamp(date, time);
    }
    
    /**
     * Add milliseconds to a timestamp
     * @param timestamp timestamp
     * @param milliseconds time to add
     * @return new timestamp
     */
    public Timestamp addMilliSeconds(Timestamp timestamp, long milliseconds) {
        Timestamp retVal = new Timestamp(timestamp.getTime() + milliseconds);
        retVal.setNanos(timestamp.getNanos());
        return retVal;
    }
    
    /**
     * Add nanoseconds to a timestamp
     * @param timestamp timestamp
     * @param nanoseconds time to add
     * @return new timestamp
     */
    public Timestamp addNanoSeconds(Timestamp timestamp, int nanoseconds) {
        timestamp.setNanos(timestamp.getNanos() + nanoseconds);
        return timestamp;
    }
    
    /**
     * Convert timestamp to string
     * @param timestamp timestamp
     * @return string time stamp up to nanoseconds
     */
    public String formatTimestamp(Timestamp timestamp) {
        int nanosec = timestamp.getNanos();
        StringBuffer nanoStr = new StringBuffer();
        nanoStr.append(nanosec);
        
        // Pad leading zeros to nanosec to get appropriate format length
        while (nanoStr.length() < NANO_LENGTH) {
            nanoStr.insert(0, "0");
        }
        EiDateFormat sdf = getDateFormat(EnumDateFormat.YYYYMMDDHHMMSS);
        String retVal = sdf.format(timestamp) + nanoStr.toString().substring(0, FMT_NANO_LENGTH);
        freeDateFormat(sdf);
        return retVal;
    }
    

}
