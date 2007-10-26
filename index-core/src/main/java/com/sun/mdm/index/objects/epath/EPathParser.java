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
package com.sun.mdm.index.objects.epath;

import java.util.StringTokenizer;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/** EPath parser class.  Creates an EPath object based on a valid EPath string
 * @author SeeBeyond
 * @version $Revision: 1.1 $
 */
public class EPathParser {

    /** pattern to recognize '*' */
    private static Pattern starPattern = Pattern.compile("\\[\\s*\\*\\s*\\]");

    /** pattern to recognize index */
    private static Pattern indexPattern = Pattern.compile("\\[\\s*\\d+\\s*\\]");
    
    /** 
     * character class to allow for ' and - and spaces (space may occur in name "O HARE") 
     * Added Support for the following characters !~(){}+`#$%&:;-/  
     */     
    private static String w = "[\\!\\~\\(\\)\\{\\}\\+\\`\\#\\$\\%\\&\\:\\;\\-\\/þÿÞß'à-öø-ýÀ-ÖØ-Ýa-zA-Z_0-9|\\s\\(\\#dot\\)\\(\\#comma\\)]*";
	
    /** pattern to recognize key'ed qualifier */
    private static Pattern keyPattern = Pattern.compile(
        "\\[\\s*(\\@" + w + "+\\s*\\=(\\s*" + w + "\\#*)?)(,\\s*\\@" + w 
            + "\\s*\\=(\\s*" + w + "\\#*)?)*\\s*\\]");

    /** pattern to recognize filter qualifier */
    private static Pattern filterPattern = Pattern.compile(
        "\\[\\s*(" + w + "+\\s*\\=(\\s*" + w + "\\#*)?)(,\\s*" + w 
            + "\\s*\\=(\\s*" + w + "\\#*)?)*\\s*\\]");

    /**
     * Creates a new instance of EPathParser
     */
    protected EPathParser() {
    }

    /**
     * Encodes an Object tag name with key values into EPath notation.
     *
     * @param objectTag the object tag name
     * @param kvp a map representing key-value pairs
     * @TODO : move to epath util class
     * @return a EPath string representation of the object tag with qualifiers
     */
    public static String encodeObjectByKey(String objectTag, Map kvp) {
        String beginQualifier = "[";
        String endQualifier = "]";
        String delimiter = ",";
        String keyPrefix = "@";

        StringBuffer buf = new StringBuffer();

        buf.append(objectTag);
        buf.append(beginQualifier);

        Set s = kvp.entrySet();
        Iterator iter = s.iterator();

        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            buf.append(keyPrefix);
            buf.append(key);
            buf.append("=");
            buf.append(value);

            if (iter.hasNext()) {
                buf.append(delimiter);
            }
        }

        buf.append(endQualifier);

        return buf.toString();
    }

    /**
     * The main program for the EPathParser class
     *
     * @param argv The command line arguments
     */
    public static void main(String[] argv) {
        try {
            EPath e1 = EPathParser.parse("Person.FirstName");
            EPath e2 = EPathParser.parse("Person.Address[*].*");
            EPath e22 = EPathParser.parse("Person.Address[ *   ].*");
            EPath e3 = EPathParser.parse("Person.Address[*].AddressLine1");
            EPath e4 = EPathParser.parse("Person.Address[@somekey=where].*");
            EPath e5 = EPathParser.parse("Person.Address[@somekey=who].*");
            EPath e52 = EPathParser.parse("Person.Address[@somekey = who].*");
            EPath e53 = EPathParser.parse("Person.Address[@somekey =].AddressLine1");
            EPath e54 = EPathParser.parse("Person.Address[@somekey= ].AddressLine1");
            EPath e6 = EPathParser.parse("Person.Address[1].AddressLine1");

            //EPath e61 = EPathParser.parse( "Person.Address[-1].AddressLine1" );
            EPath e7 = EPathParser.parse("Person.Address[2].AddressLine1");
            EPath e72 = EPathParser.parse(
                    "Person.Address[2].SomeChild[3].AddressLine1");
            EPath e8 = EPathParser.parse(
                    "Person.Address[@some=abc,@who=bad].AddressLine1");
            EPath e9 = EPathParser.parse(
                    "Person.Address[iam=yourdaddy, party=where].AddressLine1");
            EPath e92 = EPathParser.parse(
                    "Person.Address[iam=yourdaddy,Type =home].AddressLine1");

            Map m = new HashMap();
            m.put("Key1", "Value1");
            m.put("Key2", "Value2");

            String s = EPathParser.encodeObjectByKey("Address", m);
            EPath e0 = EPathParser.parse("Person." + s + ".*");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * parses a FQFN into an EPath Object that has a predefined command queue
     * which is used to traverse the object. Primary.Secondary[*].* returns all
     * secondary objects of named Secondary matching all keys.
     * Primary.Secondary[@someKey=somevalue,@somemorekey=somemorevalue].*
     * returns the secondary object named Secondary and has key "someKey" of
     * somevalue. Primary.Secondary[1].* access the secondary object by index
     * AObject.BObject[2].CObject[3].* supports multiple levels of access for
     * now, this method does not support Primary.Secondary[*].Field
     *
     * @todo create more exceptions to describe state 
     *
     * @param ePathStr valid EPath string
     * @exception EPathException exception in parsing
     * @return parsed EPath object
     */
    public static EPath parse(String ePathStr) throws EPathException {
        StringTokenizer tok = new StringTokenizer(ePathStr, ".");

        int len = tok.countTokens();

        // check if the EPath string is separated by the right delimiter
        if (len == 0) {
            throw new EPathException("Recognized EPath string");
        }

        EPath path = new EPath(ePathStr, len);

        for (int i = 0; i < path.tokenQueue.length; i++) {
            path.tokenQueue[i] = tok.nextToken();
        }

        // @TODO: add validation for path against meta data service
        // assumes qualified name always starts with the passed in context
        path.ops[0] = EPath.OP_NOOP;

        // epath also has to end in a field access
        int last = path.tokenQueue.length - 1;

        if (path.tokenQueue[last].equals("*")) {
            path.ops[last] = EPath.OP_ALL_FIELD;
        } else {
            // the last operation is for accessing the field
            path.ops[last] = EPath.OP_FIELD;
        }

        // process ops for secondary objects
        Pattern plainPattern;

        for (int i = 1; i < (path.ops.length - 1); i++) {
            String token = path.tokenQueue[i];

            Matcher starMatcher = starPattern.matcher(token);
            Matcher indexMatcher = indexPattern.matcher(token);
            Matcher keyMatcher = keyPattern.matcher(token);
            Matcher filterMatcher = filterPattern.matcher(token);

            if (starMatcher.find()) {
                // fits a wild card pattern
                StringBuffer buf = new StringBuffer(starMatcher.group());

                // strip [] from regEx result
                String key = buf.substring(1, buf.length() - 1);

                path.tokenQueue[i] = normalize(path.tokenQueue[i]);
                path.ops[i] = EPath.OP_ALL_SECONDARY;
                path.wildCard=true;
            } else if (indexMatcher.find()) {
                // fits a numerical index pattern
                StringBuffer buf = new StringBuffer(indexMatcher.group());

                // strip [] from regEx result
                String key = buf.substring(1, buf.length() - 1);

                // set the index as key
                try {
                    key = key.trim();
                    int index = Integer.parseInt(key);

                    if (index >= 0) {
                        path.indices[i] = index;
                    } else {
                        throw new EPathException(
                            "Index value must be greater then 0. Value is : " + index);
                    }
                } catch (NumberFormatException nfex) {
                    throw new EPathException(
                        "Assertion failed, Index value must be an integer, found : " + key);
                }

                path.tokenQueue[i] = normalize(path.tokenQueue[i]);
                path.ops[i] = EPath.OP_SECONDARY_BY_INDEX;
            } else if (keyMatcher.find()) {
                // fits a key index pattern
                StringBuffer buf = new StringBuffer(keyMatcher.group());

                // strip [] from regEx result
                String key = buf.substring(1, buf.length() - 1);
                key.trim();

                path.filters[i] = parseFilters(key);

                path.tokenQueue[i] = normalize(path.tokenQueue[i]);
                path.ops[i] = EPath.OP_SECONDARY_BY_KEY;
            } else if (filterMatcher.find()) {
                // fits a filter pattern
                StringBuffer buf = new StringBuffer(filterMatcher.group());

                // strip [] from regEx result
                String key = buf.substring(1, buf.length() - 1);

                path.filters[i] = parseFilters(key);

                path.tokenQueue[i] = normalize(path.tokenQueue[i]);
                path.ops[i] = EPath.OP_SECONDARY_BY_FILTER;
            } else {
                // TODO: consider this is an invalid path, need to validate against metadata
                // object that does not have any qualifier []
                // assume the first child of parent
                path.indices[i] = 0;
                path.ops[i] = EPath.OP_SECONDARY_BY_INDEX;
            }
        }

        return path;
    }

    private static String normalize(String s) {
        // strip [...] from token in tokenQueue as well
        StringBuffer buf = new StringBuffer(s);
        int j;

        for (j = 0; j < buf.length(); j++) {
            if (buf.charAt(j) == '[') {
                break;
            }
        }

        String newToken = buf.substring(0, j);

        return newToken;
    }

    private static Filter[] parseFilters(String s) {
        StringTokenizer tok1 = new StringTokenizer(s, ",");
        int len = tok1.countTokens();
        Filter[] filters = new Filter[len];

        for (int i = 0; i < len; i++) {
            String filter = tok1.nextToken();
            StringTokenizer tok2 = new StringTokenizer(filter, "=");

            // now accepting empty RHS, so ignore this
            if (tok2.countTokens() < 1) {
                throw new IllegalArgumentException(
                    "Invalid filter parameter : " + filter);
            }
            // see if there is a @ in the name, remove it if there is
            String name = tok2.nextToken();

            if (name.charAt(0) == '@') {
                name = name.substring(1, name.length());
            }

            name.trim();
            String value = null;
            if (tok2.hasMoreTokens()) {
                value = tok2.nextToken();
                value.trim();
            }
            filters[i] = new Filter(name, value);
        }

        return filters;
    }
}
