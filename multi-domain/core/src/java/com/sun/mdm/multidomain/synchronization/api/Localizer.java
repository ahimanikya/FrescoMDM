package com.sun.mdm.multidomain.synchronization.api;

import java.util.regex.Pattern;

import net.java.hulp.i18n.LocalizationSupport;

/**
 * This class provides <a href="https://hulp.dev.java.net/">Hulp</a> logging support to
 * standardization classes.
 *
 * @author Shant Gharibi (shant.gharibi@sun.com)
 */
public class Localizer extends LocalizationSupport {

    /**
     * The singelton instance.
     * 
     */
    private final static Localizer instance = new Localizer(Pattern.compile("^([A-Z]{3})(\\d{3})(:)(.*)"), "MDM-DQ", "msgs");
    
    /**
     * Returns the singleton instance.
     * 
     * @return the singleton instance
     */
    public static Localizer get() {
        return instance;
    }

    /**
     * The private constructor.
     * 
     * @param pattern the pattern to log with
     * @param prefix the prefix to prepend to the log message
     * @param bundleName name of the message bundle
     */
    private Localizer(Pattern pattern, String prefix, String bundleName) {
        super(pattern, prefix, bundleName);
    }

}
