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
    public void localeChanged(ValueChangeEvent event) {
        // Create the faces context to access the UI and session parameters
        FacesContext context = FacesContext.getCurrentInstance();
        String selectedLocale = (String) event.getNewValue();
        
       // Create session from the external context
        HttpSession session   = (HttpSession) context.getExternalContext().getSession(true);        
        session.setAttribute("selectedLocale",event.getNewValue());
       
        if (ENGLISH.equals((String) event.getNewValue())) {
            context.getViewRoot().setLocale(Locale.US);
        } else  if (GERMAN.equals((String) event.getNewValue())) {
            context.getViewRoot().setLocale(Locale.GERMANY);
        } else  if (FRANCE.equals((String) event.getNewValue())) {
            context.getViewRoot().setLocale(Locale.FRANCE);
        } else {
            context.getViewRoot().setLocale(Locale.ENGLISH);
        }    
    }


}
