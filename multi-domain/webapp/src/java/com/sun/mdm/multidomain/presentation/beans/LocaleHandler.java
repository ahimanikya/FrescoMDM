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

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Locale;
        
import net.java.hulp.i18n.Logger;
import com.sun.mdm.multidomain.presentation.util.Localizer;

/**
 * LocaleHandler class.
 * @author cye
 */
public class LocaleHandler {
    private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.presentation.beans.ApplicationHandler");
    private static Localizer localizer = Localizer.getInstance();
    private static final String[] SUPPORTED_LANGUAGES = new String[]{UserLocale.ENGLISH, 
                                                                     UserLocale.GERMAN, 
                                                                     UserLocale.FRANCE,
                                                                     UserLocale.JAPANESE, 
                                                                     UserLocale.CHINESE};
    
    private String selectedLang; 
    private String selectedLocale;
    private UserLocale userLocale;
    private List<String> languages;
    private List<UserLocale> locales;
   
    /**
     * Create an instance of LocaleHandler
     */
    public LocaleHandler() {
        languages = Arrays.asList(SUPPORTED_LANGUAGES);
        locales = new ArrayList<UserLocale>();
        for(String language : languages) {
            locales.add(new UserLocale(language));
        }
        userLocale = new UserLocale();
        logger.info(localizer.x("003: locale handler initialization completed."));        
    }
 
    /**
     * Get selected name locale.
     * @return String Selected locale name.
     */
    public String getSelectedLocale(){
        return selectedLocale;
    }
    
    /**
     * Set selected locale in name.
     * @param selectedLocale Selected locale in name.
     */
    public void setSelectedLocale(String selectedLocale){
        this.selectedLocale = selectedLocale;
        this.userLocale.setLocale(new Locale(selectedLocale));
        this.selectedLang = userLocale.getLanguage();
    }
    
    /**
     * Get selected language.
     * @return String Selected language.
     */
    public String getSelectedLang() {
        return selectedLang;
    }
    
    /**
     * Set selected language.
     * @param selectedLang Selected language.
     */
    public void setSelectedLang(String selectedLang) {
        this.selectedLang = selectedLang;
        this.userLocale.setLanguage(selectedLang);
        this.selectedLocale = userLocale.getName();
    }
    
    /**
     * Get supported languages.
     * @return A list of languages.
     */
    public List<String> getLanguages(){
        return languages;
    }
}
