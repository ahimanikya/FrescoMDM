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

/** Code description
 * 
 * @author jwu
 */
public class CodeDescription implements java.io.Serializable, Cloneable {
    
    private String sCode;
    private String sDescription;
    private String sModule;

    /** Creates a new instance of CodeDescription
     *
     * @param module Module name.
     * @param code Code.
     * @param description Description.
     */
    public CodeDescription(String module, String code, String description) {
        sModule = module;
        sCode = code;
        sDescription = description;
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

    /** String representation of the CodeDescription object.
     *
     * @return string representation of the CodeDescription object.
     */
    public String toString() {
        return sModule + "\t" + sCode + "\t" + sDescription;
    }
}
