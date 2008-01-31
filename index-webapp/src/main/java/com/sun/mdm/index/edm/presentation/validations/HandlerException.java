/*
 * This Exception is a generic exception that is thrown if a run time Exception occures in the Service Layer
 * @Author Sridhar Narsingh
 * @version 1.0
 * @date november 19th 2007
 */

package com.sun.mdm.index.edm.presentation.validations;

/**
 *
 * @author Sridhar Narsingh
 */
public class HandlerException extends java.lang.Exception {
   
   public HandlerException()    {
   }

   public HandlerException(String message)    {
     super(message);   
   }   
}
