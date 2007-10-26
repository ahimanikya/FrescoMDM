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
package com.sun.mdm.index.codelookup;

/** UserCode description
 * 
 * @author jwu
 */
public class UserCode implements java.io.Serializable, Cloneable {
    
    private String sCode;
    private String sDescription;
    private String sModule;
    private String sFormat;
    private String sInputMask;
    private String sValueMask;

    /** Creates a new instance of UserCode
     *
     * @param module Module name.
     * @param code Code.
     * @param description Description.
     */
    public UserCode(String module, String code, String description, 
            String format, String inputMask, String valueMask) {
        sModule = module;
        sCode = code;
        sFormat = format;
        sDescription = description;
        sInputMask = inputMask;
        sValueMask = valueMask;
    }

    /** Get code.
     *
     * @return code.
     */
    public String getCode() {
        return sCode;
    }

    /** Get description.
     *
     * @return description.
     */
    public String getDescription() {
        return sDescription;
    }

    /** Get module.
     *
     * @return module.
     */
    public String getModule() {
        return sModule;
    }

    /** Get format.
     *
     * @return format.
     */
    public String getFormat() {
        return sFormat;
    }

    /** Get Input Mask.
     *
     * @return Input Mask.
     */
    public String getInputMask() {
        return sInputMask;
    }

    /** Get Value Mask.
     *
     * @return Value Mask.
     */
    public String getValueMask() {
        return sValueMask;
    }

    /** String representation of the UserCode object.
     *
     * @return string representation of the UserCode object.
     */
    public String toString() {
        return sModule + "\t" + sCode + "\t" + sDescription + "\t" 
            + sFormat + "\t" + sInputMask + "\t" + sValueMask;
    }
}
