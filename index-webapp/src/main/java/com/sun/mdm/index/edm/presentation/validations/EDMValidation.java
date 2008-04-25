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

     
/*
 * EDMValidation.java 
 * This Exception is a generic exception that is thrown if a run time Exception occures 
 * in the Service Layer
 * Created on November 19, 2007
 * Author : Sridhar
 *  
 */

package com.sun.mdm.index.edm.presentation.validations;

import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.logging.Level;
//import java.util.logging.Logger;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import java.util.ResourceBundle;
import net.java.hulp.i18n.LocalizationSupport;

public class EDMValidation {
    
   private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.validations.EDMValidation");
    private static transient final Localizer mLocalizer = Localizer.get();
   ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm");
   private Hashtable MONTH_DAY_HASH = new Hashtable();
   private String[] MONTHS = new String[13];   
   
   
   
   public EDMValidation()    {
   }
   
/**
 * This method does the date validation
 * @param thisDate
 * @return Success or Failure String
 */
   public String validateDate(String thisDate)    {
       
       String errorMessage = null;
        try {
            this.MONTHS[0] = "Error";
            this.MONTHS[1] = "1";
            this.MONTHS[2] = "2";
            this.MONTHS[3] = "3";
            this.MONTHS[4] = "4";
            this.MONTHS[5] = "5";
            this.MONTHS[6] = "6";
            this.MONTHS[7] = "7";
            this.MONTHS[8] = "8";
            this.MONTHS[9] = "9";
            this.MONTHS[10] = "10";
            this.MONTHS[11] = "11";
            this.MONTHS[12] = "12";

            this.MONTH_DAY_HASH.put("1", "31");
            this.MONTH_DAY_HASH.put("2", "28");
            this.MONTH_DAY_HASH.put("3", "31");
            this.MONTH_DAY_HASH.put("4", "30");
            this.MONTH_DAY_HASH.put("5", "31");
            this.MONTH_DAY_HASH.put("6", "30");
            this.MONTH_DAY_HASH.put("7", "31");
            this.MONTH_DAY_HASH.put("8", "31");
            this.MONTH_DAY_HASH.put("9", "30");
            this.MONTH_DAY_HASH.put("10", "31");
            this.MONTH_DAY_HASH.put("11", "30");
            this.MONTH_DAY_HASH.put("12", "31");

            int day = 0;
            int month = 0;
            int year = 0;

            // ---- Server Validation ---
            StringTokenizer st = new StringTokenizer(thisDate, "/");
            String[] dateTokens = new String[st.countTokens()];
            //There should be a minimum of three tokens
            if (st.countTokens() != 3) {
                errorMessage = bundle.getString("Invalid_Date_Format");
                return  errorMessage; //"Invalid Date Format! The format of Date is MM/DD/YYYY";
            }
            
            int i = 0;
            int dateInt = 0;
            while (st.hasMoreTokens()) {
                dateTokens[i] = new String();
                dateTokens[i] = st.nextToken();
                try {
                    dateInt = new Integer(dateTokens[i]);
                } catch (NumberFormatException numberFormatException) {
                    mLogger.error(mLocalizer.x("EVL006: Date is not a Number :{0}", numberFormatException.getMessage()));
                    errorMessage = bundle.getString("Date_Number");
                   return errorMessage ;//"Date is not a Number"; 
                }
                if (i == 0) {
                    month = dateInt;
                }
                if (i == 1) {
                    day = dateInt;
                }
                if (i == 2) {
                    year = dateInt;
                    if (dateTokens[i].length() != 4) {
                        String msg = bundle.getString("Year_digits");
                        return msg; //"Year Should be 4 digits"; 
                    }
                }
                i++;
            }
            if ((year % 4 == 0 || year % 100 == 0 || year % 400 == 0)) {
               this.MONTH_DAY_HASH.put("2", "29");
            } else {
                this.MONTH_DAY_HASH.put("2", "28");
            }

            //Month should < 12
            if (month > 12 || month <= 0) {
                return bundle.getString("Invalid_Month");//"Invalid Month Entered";
            }
            //Day should be according to the MONTH_DAY table
            if (day > new Integer((String) this.MONTH_DAY_HASH.get(MONTHS[month])).intValue() || day < 1) {
                return bundle.getString("Invalid_Day");//"Invalid Day Entered";
            }
            // If leap year and month should be not more than 29
//            if ((year % 4 == 0 && year % 100 == 0) || (year % 400 == 0)) {
//                if (month > 29 || month < 0) {
//                    return "Invalid Month for a Leap Year";
//                }
//            } else 
           
            if (month > 12 || month < 1) {
                return bundle.getString("Invalid_Month_format"); //"Invalid Month The format of Date is MM/DD/YYYY"; //Month should be between 1 and 12
            }
           mLogger.error(mLocalizer.x("EVL001: Day: Month: Year: {0}:{1}:{2}", new Integer(day).toString(),new Integer(month).toString(),new Integer(year).toString()));
           //mLogger.error(mLocalizer.x("EVL002: Month :: {0}", new Integer(month).toString()));
           //mLogger.error(mLocalizer.x("EVL003: Year :: {0}", new Integer(year).toString()));


       } catch (Exception ex) {
           mLogger.error(mLocalizer.x("EVL004: Date validation failed :{0}", ex.getMessage()));
       }
        return "success";
   }
   
   /**
    * This method does the time Validation
    * @param thisTime
    * @return Success or Failure string
    */
   public String validateTime(String thisTime)    {
       int hours = 0;
       int minutes = 0;
       int seconds = 0;
       
       // ---- Server Validation ---
       StringTokenizer st = new StringTokenizer(thisTime, ":");
       String timeTokens[] = new String[st.countTokens()];

       //There should be a minimum of three tokens
       if (st.countTokens() != 3) return bundle.getString("Invalid_Time_Format"); //"Invalid Time Format!, The format of Time is HH:MM:SS";

       //Parse each Token to see if it has got digits
       int i = 0;
       int timeInt = 0;
       while (st.hasMoreTokens()) {
           timeTokens[i] = new String();
           timeTokens[i] = st.nextToken();
           try {
               timeInt = new Integer(timeTokens[i]);
           } catch (NumberFormatException numberFormatException) {
               mLogger.error(mLocalizer.x("EVL005: Invalid Time Format!, The format of Time is HH:MM:SS :{0}", numberFormatException.getMessage()));
               return bundle.getString("Invalid_Time_Format"); //"Invalid Time Format!, The format of Time is HH:MM:SS";  //Either HH or MM or SS Not a number
           }
           if (i == 0) hours = timeInt;
           if (i == 1) minutes = timeInt;
           if (i == 2) seconds = timeInt;
           i++;
       }
       //System.out.println("Hours,minutes,seconds" + hours + ','+minutes+','+seconds);
       //Hours should be > 24       
       if (hours  > 24 || hours < 0) return bundle.getString("Invalid_Hours");//"Invalid Hours Entered, The format of Time is HH:MM:SS";   
       //Minutes should be > 60
       if (minutes  > 60 || minutes < 0) return bundle.getString("Invalid_Minutes"); //"Invalid Minutes Entered, The format of Time is HH:MM:SS";   
       //Hours should be > 24       
       if (seconds  > 60 || seconds < 0) return  bundle.getString("Invalid_Seconds"); //"Invalid Seconds Entered, The format of Time is HH:MM:SS";   
       
       return "success";
   }
   
/**
 * This method does the Local ID validation according to the format DDD-DDD-DDDD
 * @param thisTime
 * @return Success or Failure String
 */
   public String validateLocalId(String thisTime)    {
       // ---- Server Validation ---
       StringTokenizer st = new StringTokenizer(thisTime, "-");
       String localIDTokens[] = new String[st.countTokens()];

       //There should be a minimum of three tokens
       if (st.countTokens() != 3) return bundle.getString("Local_ID_format");//"Invalid Local ID. The format of Local ID is DDD-DDD-DDDD";

       //Parse each Token to see if it has got digits
       int i = 0;
       int localIdInt = 0;
       while (st.hasMoreTokens()) {
           localIDTokens[i] = new String();
           localIDTokens[i] = st.nextToken();           
           try {
               localIdInt = new Integer(localIDTokens[i]);               
           } catch (NumberFormatException numberFormatException) {
                mLogger.error(mLocalizer.x("EVL007: Local ID is not a Number :{0}", numberFormatException.getMessage()));
               return bundle.getString("Local_ID_Number") ; //"Local ID is not a Number";  //Either day or month or Year Not a number
           }
           if (i == 0 || i == 1)   {
               if (localIDTokens[i].length() != 3)  {                   
                   if (!"success".equalsIgnoreCase(validateNumber(localIDTokens[i])))    {
                      return bundle.getString("Invalid_Local_ID"); //"Invalid Local ID. Local ID should be Numeric";   
                   }                   
                   return bundle.getString("Local_ID_format"); //"Invalid Local ID. The format of Local ID is DDD-DDD-DDDD";
               }                 
           }
           if (i == 2) {
               if (localIDTokens[i].length() != 4) return bundle.getString("Local_ID_format");//"Invalid Local ID. The format of Local ID is DDD-DDD-DDDD";  
           }
           i++;           
       }
       return "success";              
   }   
   
   /**
    * this method validates if the String is a valid number
    * @param thisNumber
    * @return Success or Failure String
    */
   public String validateNumber(String thisNumber)    {
       try {
           int tempNum = Integer.parseInt(thisNumber);
       } catch (NumberFormatException numberFormatException) {
            mLogger.error(mLocalizer.x("EVL008: Invalid Number :{0}", numberFormatException.getMessage()));
           return  bundle.getString("Invalid_Number"); //"Invalid Number";
       }       
       return "success";
   }
}
