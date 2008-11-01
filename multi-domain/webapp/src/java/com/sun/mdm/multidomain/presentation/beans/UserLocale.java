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

    public UserLocale(){
    }
    public UserLocale(String language){
        this.language = language;
        this.locale = toLocale(language);
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getLanguage() {
        return this.language;
    }    
     public void setLocale(Locale locale) {
        this.locale = locale;
    }
    public Locale getLocale() {
        return this.locale;
    }      
    public String getName() {
        if (locale != null) {
            return locale.toString();
        } else {
            return null;
        }
    }
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
}
