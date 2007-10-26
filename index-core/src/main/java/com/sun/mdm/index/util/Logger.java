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
package com.sun.mdm.index.util;

import com.sun.mdm.index.objects.ObjectNode;
import java.util.ResourceBundle;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * A logger that exposes the same "interface" as the Log4J logger but in fact logs to a
 * java.util.logging.Logger delegate. It can be used to make migration from the log4j
 * package to the java.util.logging package easier.
 *
 * @author Frank Kieviet
 * @version $Revision: 1.1 $
 */
public final class Logger implements Serializable {
    private transient java.util.logging.Logger mDelegate;
    private String mname;
    
    private Logger(java.util.logging.Logger delegate, String name) {
        mDelegate = delegate;
        mname = name;
    }

    /**
     * See {@link org.apache.log4j.Logger#getLogger}
     *
     * @param name name of the logger
     * @return Logger instance
     */
    public static Logger getLogger(String name) {
        return new Logger(java.util.logging.Logger.getLogger(name), name);
    }

    /**
     * See {@link org.apache.log4j.Logger#}
     *
     * @param clazz Class whose name is to be used as the logger name
     * @return Logger instance
     */
    public static Logger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }

    /**
     * See {@link org.apache.log4j.Category#debug}
     *
     * @param message msg to be logged
     */
    public final void debug(Object message) {
        String s = null;
        if (message != null) {
            s = message.toString();
        }
        mDelegate.log(java.util.logging.Level.FINE, s);
    }
    
    /**
     * Issue a log msg with a level of DEBUG.
     *
     * @param message FIXME
     * @param node Object node to log
     */
    public void debug(Object message, ObjectNode node) {
        String s = message + "\n" + node;
        debug(s);
    }

    /**
     * See {@link org.apache.log4j.Category#debug}
     *
     * @param message msg to be logged
     * @param t exception
     */
    public final void debug(Object message, Throwable t) {
        String s = null;
        if (message != null) {
            s = message.toString();
        }
        mDelegate.log(java.util.logging.Level.FINE, s, t);
    }

    /**
     * See {@link org.apache.log4j.Category#error}
     *
     * @param message msg to be logged
     */
    public final void error(Object message) {
        String s = null;
        if (message != null) {
            s = message.toString();
        }
        mDelegate.log(java.util.logging.Level.SEVERE, s);
    }

    /**
     * See {@link org.apache.log4j.Category#error}
     *
     * @param message msg to be logged
     * @param t exception to be logged
     */
    public final void error(Object message, Throwable t) {
        String s = null;
        if (message != null) {
            s = message.toString();
        }
        mDelegate.log(java.util.logging.Level.SEVERE, s, t);
    }

    /**
     * See {@link org.apache.log4j.Category#fatal}
     *
     * @param message msg to be logged
     */
    public final void fatal(Object message) {
        String s = null;
        if (message != null) {
            s = message.toString();
        }
        mDelegate.log(java.util.logging.Level.SEVERE, s);
    }

    /**
     * See {@link org.apache.log4j.Category#fatal}
     *
     * @param message msg to be logged
     * @param t exception to be logged
     */
    public final void fatal(Object message, Throwable t) {
        String s = null;
        if (message != null) {
            s = message.toString();
        }
        mDelegate.log(java.util.logging.Level.SEVERE, s, t);
    }

    /**
     * See {@link org.apache.log4j.Category#info}
     *
     * @param message msg to be logged
     */
    public final void info(Object message) {
        String s = null;
        if (message != null) {
            s = message.toString();
        }
        mDelegate.log(java.util.logging.Level.INFO, s);
    }

    /**
     * See {@link org.apache.log4j.Category#info}
     *
     * @param message msg to be logged
     * @param t exception to be logged
     */
    public final void info(Object message, Throwable t) {
        String s = null;
        if (message != null) {
            s = message.toString();
        }
        mDelegate.log(java.util.logging.Level.INFO, s, t);
    }

    /**
     * See {@link org.apache.log4j.Category#isDebugEnabled}
     *
     * @return if debug logging is enabled
     */
    public final boolean isDebugEnabled() {
        return mDelegate.isLoggable(java.util.logging.Level.FINE);
    }

    /**
     * See {@link org.apache.log4j.Category#warn}
     *
     * @param message msg to be logged
     */
    public final void warn(Object message) {
        String s = null;
        if (message != null) {
            s = message.toString();
        }
        mDelegate.log(java.util.logging.Level.WARNING, s);
    }

    /**
     * See {@link org.apache.log4j.Category#warn}
     *
     * @param message msg to be logged
     * @param t exception to be logged
     */
    public final void warn(Object message, Throwable t) {
        String s = null;
        if (message != null) {
            s = message.toString();
        }
        mDelegate.log(java.util.logging.Level.WARNING, s, t);
    }

    /**
     * See {@link org.apache.log4j.Category#getName}
     *
     * @return String
     */
    public final String getName() {
        return mDelegate.getName();
    }

    /**
     * See {@link org.apache.log4j.Category#getLevel}
     *
     * @return Level
     */
    public final java.util.logging.Level getLevel() {
        return mDelegate.getLevel();
    }

    /**
     * See {@link org.apache.log4j.Category#getResourceBundle}
     *
     * @return ResourceBundle
     */
    public final ResourceBundle getResourceBundle() {
        return mDelegate.getResourceBundle();
    }

    /**
     * See {@link org.apache.log4j.Category#isEnabledFor}
     *
     * @param level msg to be logged
     * @return boolean
     */
    public final boolean isEnabledFor(java.util.logging.Level level) {
        return mDelegate.isLoggable(level);
    }

    /**
     * See {@link org.apache.log4j.Category#isInfoEnabled}
     *
     * @return boolean
     */
    public final boolean isInfoEnabled() {
        return mDelegate.isLoggable(java.util.logging.Level.INFO);
    }

    /**
     * See {@link org.apache.log4j.Category#setLevel}
     *
     * @param level msg to be logged
     */
    public final void setLevel(java.util.logging.Level level) {
        mDelegate.setLevel(level);
    }
    
    
    private void readObject(ObjectInputStream istream) throws IOException, ClassNotFoundException {
        istream.defaultReadObject();
    	mDelegate = java.util.logging.Logger.getLogger(mname);
    }
    
    private void writeObject(ObjectOutputStream ostream) throws IOException {
    	ostream.defaultWriteObject();
    }
    
}
