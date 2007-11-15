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

import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.configurator.impl.SurvivorHelperConfig;
import com.sun.mdm.index.util.Localizer;

/** Factory methods to create a SurvivorHelper
 *
 * 
 * @version $Revision: 1.1 $
 */
public class HelperFactory {
    
    private transient static final Localizer mLocalizer = Localizer.get();
    
    /** cache the loaded class
     */
    private static Class mHelperClass = null;
    
    /** disabled, use factory method
     */
    protected HelperFactory() {
    }

    /** creates the helper object specified in the survivor calculator
     * configuration module
     * @throws HelperCreationException failed to create
     * @return a helper object
     */
    public static AbstractSurvivorHelper createSurvivorHelper()
        throws HelperCreationException {
        try {
            ConfigurationService config = ConfigurationService.getInstance();
            SurvivorHelperConfig sc = (SurvivorHelperConfig) 
                config.getConfiguration(SurvivorHelperConfig.MODULE_NAME);
            String className = sc.getHelperClassName();

            return createSurvivorHelper(className, sc);
        } catch (InstantiationException iex) {
            throw new HelperCreationException(mLocalizer.t("SUR508: Unable to " + 
                                    "get configuration object: {0}", iex));
        }
    }

    /** creates a helper instance using the given class name and a
     * SurvivorHelperConfig object containing all the needed configuration
     * and initialization information needed
     * @return a helper instance
     * @param className helper class name
     * @param config configuration object
     * @throws HelperCreationException error creating helper */
    public static AbstractSurvivorHelper createSurvivorHelper(
        String className, SurvivorHelperConfig config)
        throws HelperCreationException {
        AbstractSurvivorHelper helper = null;

        try {
            // check cache if helper class has been loaded
            if (mHelperClass == null) {
                // load helper class into JVM
                mHelperClass = Class.forName(className);
            }

            Object obj = mHelperClass.newInstance();

            if (!AbstractSurvivorHelper.class.isInstance(obj)) {
                // throw creation exception
                throw new HelperCreationException(mLocalizer.t("SUR509: Helper class " + 
                                    "does not extend AbstractSurvivorHelper."));
            }

            helper = (AbstractSurvivorHelper) obj;
            helper.init(config);
        } catch (IllegalAccessException aex) {
            throw new HelperCreationException(mLocalizer.t("SUR510: Unable to " + 
                                    "access default constructor of helper class: {0}", aex));
        } catch (ClassNotFoundException cex) {
            throw new HelperCreationException(mLocalizer.t("SUR511: Class not " + 
                                    "found: {0}", cex));
        } catch (InstantiationException iex) {
            throw new HelperCreationException(mLocalizer.t("SUR512: InstantiationException: {0}", iex));
        } catch (StrategyCreationException scex) {
          throw new HelperCreationException(mLocalizer.t("SUR513: StrategyCreationException: {0}", scex));
        }

        // return an instance of the helper class
        return helper;
    }
}
