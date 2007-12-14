/**
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

package com.sun.mdm.index.loader.log;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.text.MessageFormat;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * This is similar to SimpleLogFormatter with some variation
 * 
 * @author Sujit Biswas
 * 
 */
public class LogFormatter extends Formatter {
	private final static String format = "{0,date} {0,time}";

	Date dat = new Date();

	private MessageFormat formatter;

	private Object[] args = new Object[1];

	private boolean debug = true;

	// Line separator string. This is the value of the line.separator property
	// at the moment that the LogFormatter was created.
	@SuppressWarnings("unchecked")
	private String lineSeparator = (String) java.security.AccessController
			.doPrivileged(new sun.security.action.GetPropertyAction(
					"line.separator"));

	private String seperator = "|";

	private String begin =   "[#|";

	private String end = "|#]";

	public LogFormatter() {
		super();
	}

	public synchronized String format(LogRecord record) {
		StringBuffer sb = new StringBuffer();

		sb.append(begin);
		printDate(record, sb);
		
		printLevel(record, sb);
		
		printSourceClassName(record, sb);

		if (debug) {
			printSourceMethodName(record, sb);
		}

		printMessage(record, sb);
		printException(record, sb);
		sb.append(end);
		sb.append(lineSeparator);
		sb.append(lineSeparator);

		return sb.toString();
	}

	private void printLevel(LogRecord record, StringBuffer sb) {
		sb.append(record.getLevel().getLocalizedName());
		sb.append(seperator);
		
	}

	/**
	 * @param record
	 * @param sb
	 */
	private void printMessage(LogRecord record, StringBuffer sb) {
		String message = formatMessage(record);
		sb.append(message);
	}

	/**
	 * @param record
	 * @param sb
	 */
	private void printDate(LogRecord record, StringBuffer sb) {
		// Minimize memory allocations here.
		dat.setTime(record.getMillis());
		args[0] = dat;

		StringBuffer text = new StringBuffer();

		if (formatter == null) {
			formatter = new MessageFormat(format);
		}

		formatter.format(args, text, null);
		sb.append(text);
		sb.append(seperator);
	}

	/**
	 * @param record
	 * @param sb
	 */
	private void printSourceClassName(LogRecord record, StringBuffer sb) {
		if (record.getSourceClassName() != null) {
			sb.append(record.getSourceClassName());
		} else {
			sb.append(record.getLoggerName());
		}

		sb.append(seperator);
	}

	/**
	 * @param record
	 * @param sb
	 */
	private void printSourceMethodName(LogRecord record, StringBuffer sb) {
		if (record.getSourceMethodName() != null) {
			sb.append(record.getSourceMethodName());
			sb.append("()");
		}

		sb.append(seperator);
	}

	/**
	 * @param record
	 * @param sb
	 */
	private void printException(LogRecord record, StringBuffer sb) {
		if (record.getThrown() != null) {
			try {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				record.getThrown().printStackTrace(pw);
				pw.close();
				sb.append(lineSeparator);
				sb.append(sw.toString());
				sb.append(lineSeparator);
			} catch (Exception ex) {
			}
		}
	}
}
