package com.sun.mdm.index.loader.util;

import java.util.regex.Pattern;
import net.java.hulp.i18n.LocalizationSupport;

/**
 * This class supports localization.
 * @author Charles Ye
 */
public class Localizer extends LocalizationSupport {
    
    private static final String DEFAULT_PATTERN = "([A-Z][A-Z][A-Z]\\d\\d\\d)(:)(.*)";
    private static final String DEFAULT_PREFIX = "MDM-MI-";
    private static final String DEFAULT_BUNDLENAME = "messages";
    private static Localizer instance = null; 
    
    /**
     * Localizer Constructor 
     * @param idpattern		the text pattern of messages.
     * @param prefix		the prefix of messages.
     * @param bundlename	the name of resource bundle.
     */
    public Localizer(Pattern idpattern, String prefix, String bundlename) {
        super(idpattern, prefix, bundlename);
    }
    
    /**  
     * Get an instance of Localizer.
     * 
     * @return a Localizer instance.
     */
    public static Localizer getInstance() {
        if (instance == null) {
            Pattern pattern = Pattern.compile(DEFAULT_PATTERN);
            instance = new Localizer(pattern, DEFAULT_PREFIX, DEFAULT_BUNDLENAME);
        }
        return instance;
    }    
}

