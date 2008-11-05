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
package com.sun.mdm.multidomain.presentation.beans;

import java.util.Locale;

/**
 * UserLocale class.
 * @author cye
 */
public class UserLocale {
    
    public static final String ENGLISH = "English";
    public static final String GERMAN = "German";
    public static final String FRANCE = "France";
    public static final String JAPANESE = "Japanese";
    public static final String CHINESE = "Chinese";
    
    private String language;
    private Locale locale;

    /**
     * Create an instance of UserLocale.
     */
    public UserLocale(){
    }
    
    /**
     * Create an instance of UserLocale.
     * @param language User language.
     */
    public UserLocale(String language){
        this.language = language;
        this.locale = toLocale(language);
    }
    
    /**
     * Set user language.
     * @param language User language.
     */
    public void setLanguage(String language) {
        this.language = language;
        this.locale = toLocale(language);
    }
    
    /**
     * Get user language.
     * @return User language.
     */
    public String getLanguage() {
        return this.language;
    }  
    
    /**
     * Set locale.
     * @param locale Locale.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
        this.language = toLanguage(locale);
    }
    
    /**
     * Get locale.
     * @return Locale.
     */
    public Locale getLocale() {
        return this.locale;
    }
    
    /**
     * Get locale name.
     * @return Locale name.
     */
    public String getName() {
        if (locale != null) {
            return locale.toString();
        } else {
            return null;
        }
    }
    
    /**
     * Convert user language to locale.
     * @param language User language.
     * @return Locale.
     */
    public static Locale toLocale(String language) {
        Locale locale = null;
        if (UserLocale.ENGLISH.equals(language)) {
            locale = Locale.US;
        } else  if (UserLocale.GERMAN.equals(language)) {
            locale = Locale.GERMANY;          
        } else  if (UserLocale.FRANCE.equals(language)) {
            locale = Locale.FRANCE;
        } else  if (UserLocale.JAPANESE.equals(language)) {
            locale = Locale.JAPANESE;
        } else  if (UserLocale.CHINESE.equals(language)) {
            //Locale.CHINESE, Locale.TRADITIONAL_CHINESE, Locale.CHINA, Locale.PRC, Locale.TAIWAN
            locale = Locale.SIMPLIFIED_CHINESE;   
        } else {
            locale = Locale.ENGLISH;
        }                    
        return locale;
    }
    
    /**
     * Convert locale to user language.
     * @param locale Locale.
     * @return String User language.
     */
    public static String toLanguage(Locale locale) {
        String language = null;
        if (Locale.US.equals(locale)) {
            language = UserLocale.ENGLISH;
        } else  if (Locale.GERMANY.equals(locale)) {
            language = UserLocale.GERMAN;       
        } else  if (Locale.FRANCE.equals(locale)) {
            language = UserLocale.FRANCE;
        } else  if (Locale.JAPANESE.equals(locale)) {
            language = UserLocale.JAPANESE;
        } else  if (Locale.SIMPLIFIED_CHINESE.equals(locale)) {
            //Locale.CHINESE, Locale.TRADITIONAL_CHINESE, Locale.CHINA, Locale.PRC, Locale.TAIWAN
            language = UserLocale.CHINESE;   
        } else {
            language = UserLocale.ENGLISH;
        }                    
        return language;
    }    
}
