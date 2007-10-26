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
package com.sun.mdm.index.survivor;


/**
 * The <b>SurvivorStrategyInterface</b> class is the interface for all default
 * and custom survivor calculators. This class must be implemented by any
 * custom calculators plugged in to a master index application.
 *
 * 
 * @version $Revision: 1.1 $
 */
public interface SurvivorStrategyInterface {
    /**
     * Selects the field value to populate into the Single Best Record (SBR).
     * The field value is contained within a SystemField object.
	 * <p>
     * @param candidateId The candidate field name.
     * @param fields The mapping of a system fields list to a system key.
     * @return <CODE>SystemField</CODE> - The value of the field that has been
     * selected to populate the SBR. This method returns
     * <b>null</b> if the new value cannot be determined and the original
     * candidate value should be used.
     * @exception SurvivorCalculationException Thrown if there is an error
     * with the calculation configuration.
     * @include
     */
    public SystemField selectField(String candidateId, SystemFieldListMap fields)
        throws SurvivorCalculationException;

    /**
     * Initializes the survivor strategy object. Call this method after creating
     * the object.
     * <p>
     * @param parameters The parameters given in the Best Record configuration file.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception StrategyCreationException Thrown if the strategy cannot be
     * initialized.
     * @include
     */
    public void init(java.util.Collection parameters)
        throws StrategyCreationException;

    /**
     * Returns an exact copy of the survivor strategy object. This method must
     * be implemented by all sub-classes, and is required for the prototype
     * pattern.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Object</CODE> - A copy of the object.
     * @exception CloneNotSupportedException Thrown if the strategy does not
     * support cloning.
     * @include
     */
    public Object clone() throws CloneNotSupportedException;
}
