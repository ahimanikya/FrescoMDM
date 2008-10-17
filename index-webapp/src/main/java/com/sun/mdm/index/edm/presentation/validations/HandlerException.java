/*
 * This Exception is a generic exception that is thrown if a run time Exception occures in the Service Layer
 * @version 1.0
 * @date november 19th 2007
 */

package com.sun.mdm.index.edm.presentation.validations;

/**
 *

 */
public class HandlerException extends java.lang.Exception {
   
   public HandlerException()    {
   }

   public HandlerException(String message)    {
     super(message);   
   }   
}
