/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.index.edm.presentation.util;

import java.util.regex.Pattern;
import net.java.hulp.i18n.LocalizationSupport;

/**
 * This class supports localization.
 * 
 */
public class Localizer extends LocalizationSupport {
    
    private static final String DEFAULT_PATTERN = "([A-Z][A-Z][A-Z]\\d\\d\\d)(:)(.*)";
    private static final String DEFAULT_PREFIX = "MDM-MI-";
    private static final String DEFAULT_BUNDLENAME = "mdm_errorCode";
    private static Localizer instance = null; 
    
    public Localizer(Pattern idpattern, String prefix, String bundlename) {
        super(idpattern, prefix, bundlename);
    }
    
    /**  Returns an instance of Localizer.
     * 
     * @return a Localizer instance.
     * 
     */
    public static Localizer get() {
        if (instance == null) {
            Pattern pattern = Pattern.compile(DEFAULT_PATTERN);
            instance = new Localizer(pattern, DEFAULT_PREFIX, DEFAULT_BUNDLENAME);

        }
        return instance;
    }    
}

