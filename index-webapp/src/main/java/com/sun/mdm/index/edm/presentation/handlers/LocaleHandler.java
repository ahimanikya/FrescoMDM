/*
 * LocaleHandler.java
 *
 * Created on September 11, 2007, 12:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.sun.mdm.index.edm.presentation.handlers;

import javax.faces.context.FacesContext;
import javax.faces.event.*;
import java.util.Locale;
import javax.servlet.http.HttpSession;


/**
 * This class is used for selecting the locale.
 * @author Administrator
 */
public class LocaleHandler {
    
    private String langOption; 
    private static final String ENGLISH = "English";
    private static final String GERMAN = "German";
    private static final String FRANCE = "France";
    private static final String JAPAN = "Japanese";
    private static final String CHINA = "Chinese";
 
    /** Creates a new instance of LocaleHandler */
    public LocaleHandler() {
    }
 
    /**
     * This method will return the language option picked by the user.
     * @return <CODE>String</CODE>
     */
    public String getLangOption() {
        return langOption;
    }

    /**
     * This method will set the language option picked by the user.
     */
    public void setLangOption(String langOption) {
        this.langOption = langOption;
    }
     /** 
     * Modified on 25/06/2008
     * 
     * This method will set the locale as per the user's locale preferences
     * 
     * Modified to support Japnese and chinese locales
     * 
     * @param selectedLocale  Selecte locate by the user
     * 
     * 
     */

    public void localeChanged(ValueChangeEvent event) {
        // Create the faces context to access the UI and session parameters
        FacesContext context = FacesContext.getCurrentInstance();
        String selectedLocale = (String) event.getNewValue();
        
       // Create session from the external context
        HttpSession session   = (HttpSession) context.getExternalContext().getSession(true);        
        session.setAttribute("selectedLocale",selectedLocale);
        
        //set the language option and faces context locale here
        if (ENGLISH.equals(selectedLocale)) {
            context.getViewRoot().setLocale(Locale.US);
            setLangOption(ENGLISH); 
        } else  if (GERMAN.equals(selectedLocale)) {
            context.getViewRoot().setLocale(Locale.GERMANY);
            setLangOption(GERMAN); 
        } else  if (FRANCE.equals(selectedLocale)) {
            context.getViewRoot().setLocale(Locale.FRANCE);
            setLangOption(FRANCE); 
        } else  if (JAPAN.equals(selectedLocale)) {
            context.getViewRoot().setLocale(Locale.JAPANESE);
            setLangOption(JAPAN); 
         } else  if (CHINA.equals(selectedLocale)){
            //context.getViewRoot().setLocale(Locale.CHINESE);
            context.getViewRoot().setLocale(Locale.SIMPLIFIED_CHINESE);
            //context.getViewRoot().setLocale(Locale.TRADITIONAL_CHINESE);
            setLangOption(CHINA); 
        } else {
            context.getViewRoot().setLocale(Locale.ENGLISH);
            setLangOption(ENGLISH); 
        }    
    }
    
     /** 
     * Added on 25/06/2008
     * 
     * This method will set the locale as per the user's locale preferences
     * 
     * @param selectedLocale  Selecte locate by the user
     * 
     * 
     */

    public void setChangedLocale(String selectedLocale) {
        // Create the faces context to access the UI and session parameters
        FacesContext context = FacesContext.getCurrentInstance();
        if (ENGLISH.equals(selectedLocale)) {
            context.getViewRoot().setLocale(Locale.US);
            setLangOption(ENGLISH); 
        } else  if (GERMAN.equals(selectedLocale)) {
            context.getViewRoot().setLocale(Locale.GERMANY);
            setLangOption(GERMAN); 
        } else  if (FRANCE.equals(selectedLocale)) {
            context.getViewRoot().setLocale(Locale.FRANCE);
            setLangOption(FRANCE); 
        } else  if (JAPAN.equals(selectedLocale)) {
            context.getViewRoot().setLocale(Locale.JAPANESE);
            setLangOption(JAPAN); 
        } else  if (CHINA.equals(selectedLocale)) {
           //context.getViewRoot().setLocale(Locale.CHINESE);
           context.getViewRoot().setLocale(Locale.SIMPLIFIED_CHINESE);
           //context.getViewRoot().setLocale(Locale.TRADITIONAL_CHINESE);
            setLangOption(CHINA); 
        } else {
            context.getViewRoot().setLocale(Locale.ENGLISH);
            setLangOption(ENGLISH); 
        }    
    }

}
