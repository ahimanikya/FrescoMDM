/* *************************************************************************
 *
 *          Copyright (c) 2002, SeeBeyond Technology Corporation,
 *          All Rights Reserved
 *
 *          This program, and all the routines referenced herein,
 *          are the proprietary properties and trade secrets of
 *          SEEBEYOND TECHNOLOGY CORPORATION.
 *
 *          Except as provided for by license agreement, this
 *          program shall not be duplicated, used, or disclosed
 *          without  written consent signed by an officer of
 *          SEEBEYOND TECHNOLOGY CORPORATION.
 *
 ***************************************************************************/

package com.sun.mdm.multidomain.services.security.util;

import java.text.SimpleDateFormat;
import com.sun.mdm.multidomain.services.configuration.MDConfigManager;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import java.util.Date;


/** This class provides method to convert string to date and convert date to string.
 * @author Jeff Lin
 */
public class DateUtil {

    static String DATE_FORMAT_STRING;
    static SimpleDateFormat SDFDATE;
    static String DATE_ONLY_FORMAT_STRING;
    static SimpleDateFormat SDFDATEONLY;

    /**
     * Creates new DateUtil
     */
    private DateUtil() {
    }
    
    /**
     * Gets the date format of the DateUtil class
     * To be called before from Login.
     */
    public static void init() {
        // testing--raymond tam
        // RESUME HERE
/*        
        String dateFormat = MDConfigManager.getDateFormat();
        DATE_FORMAT_STRING = dateFormat + " HH:mm:ss";
        DATE_ONLY_FORMAT_STRING = dateFormat;
        SDFDATE = new SimpleDateFormat(DATE_FORMAT_STRING);
        SDFDATE.setLenient(false);
        SDFDATEONLY = new SimpleDateFormat(DATE_ONLY_FORMAT_STRING);
        SDFDATEONLY.setLenient(false);
*/        
    }

    /**
     * This method convert date to format string
     *
     * @param date a date data type
     * @return date string
     */
    public static String date2String(Date date) {
        if (date == null) {
            return "";
        }

        return SDFDATE.format(date);
    }

    /**
     * This method convert date componet to string
     *
     * @param date a date data type
     * @return date string
     */
    public static String dateOnly2String(Date date) {
        if (date == null) {
            return "";
        }

        return SDFDATEONLY.format(date);
    }

    /**
     * This method convet format string to date
     *
     * @param formatDate a string format date
     * @exception ValidationException thrown when validation fails
     * @return date
     */
    public static Date string2Date(String formatDate) throws ValidationException {
        String formatDate1 = formatDate;
        try {
            if (formatDate == null || formatDate.trim().length() == 0) {
                return null;
            } else if (formatDate.trim().length() == 10) {
                // mm/dd/yyyy
                formatDate = formatDate.trim() + " 00:00:00";
            }

            java.util.Date date = SDFDATE.parse(formatDate);

            return date;
        } catch (Exception e) {
            throw new ValidationException("Invalid date string: " + formatDate1);
        }
    }
}
