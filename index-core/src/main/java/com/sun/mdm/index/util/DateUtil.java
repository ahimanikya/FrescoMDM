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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;

/**
 * Date utility functions
 * @author  cchow
 */
public class DateUtil {
	
    static GregorianCalendar cal = new GregorianCalendar();
    
    /**
     * Computes the first date of the week for the specified date.
     * 
     * @param dt Specified date.
     * @return The first date of the week for the specified date.
     */
    public static Date getFirstDateOfWeek(Date dt) {
    	
    	cal.setTime(dt);
    	int day = cal.get(Calendar.DAY_OF_WEEK);
    	// Compute delta to get to the first date of the week
    	int delta = day - 1;
    	if (delta > 0) {
    	    cal.add(Calendar.DATE, -delta);
    	}
    	
    	return cal.getTime();
    }	
    
    /**
     * Computes the first date of the month for the specified date.
     *
     * @param dt Specified date.
     * @return The first date of the month for the specified date.
     */
    public static Date getFirstDateOfMonth(Date dt) {
    	
    	cal.setTime(dt);
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	
    	return cal.getTime();
    }
    
    /**
     * Computes the first date of the year for the specified date.
     *
     * @param dt Specified date.
     * @return The first date of the year for the specified date.
     */
    public static Date getFirstDateOfYear(Date dt) {
    	
    	cal.setTime(dt);
    	cal.set(Calendar.MONTH, Calendar.JANUARY);
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	
    	return cal.getTime();
    }
    
    /**
     * Computes the last date of the month for the specified date.
     * @param dt Specified date.
     * @return The last date of the month for the specified date.
     */
    public static Date getLastDateOfMonth(Date dt) {
    	
    	cal.setTime(dt);
    	cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    	
    	return cal.getTime();
    }
    
    /**
     * Computes the last date of the year for the specified date.
     *
     * @param dt Specified date.
     * @return The last date of the year for the specified date.
     */
    public static Date getLastDateOfYear(Date dt) {
    	
    	cal.setTime(dt);
    	cal.set(Calendar.MONTH, Calendar.DECEMBER);
    	cal.set(Calendar.DAY_OF_MONTH, 31);
    	
    	return cal.getTime();
    }
    
    /**
     * Get month string for the specified date.
     *
     * @param dt Specified date.
     * @return The month string of specified date
     */
    public static String getMonth(Date dt) {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
    	StringBuffer sb = new StringBuffer();
    	sdf.format(dt, sb, new FieldPosition(0));
    	
    	return sb.toString();
    }
    
    /**
     * Get year string for the specified date.
     *
     * @param dt Specified date.
     * @return The year string of specified date.
     */
    public static String getYear(Date dt) {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    	StringBuffer sb = new StringBuffer();
    	sdf.format(dt, sb, new FieldPosition(0));
    	
    	return sb.toString();
    }
    
}
