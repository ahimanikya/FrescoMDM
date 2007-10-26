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
package com.sun.mdm.index.project.generator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import com.sun.mdm.index.project.generator.exception.UnmatchedTagsException;
import com.sun.mdm.index.project.generator.exception.TemplateFileNotFoundException;
import com.sun.mdm.index.project.generator.exception.InvalidTemplateFileException;
import com.sun.mdm.index.util.CodeGeneratorUtil;



/**
 *
 * @author gzheng
 * @version $Revision: 1.1 $
 */
public class TemplateWriter {
    private final String mDefineTag = "[SBYNTAG:DEFINE:";
    private final String mEndTag = "]";
    private final String mRepeatBeginTag = "[SBYNTAG:REP-BEGIN]";
    private final String mRepeatEndTag = "[SBYNTAG:REP-END]";
    private final String mIfBeginTag = "[SBYNTAG:IF-BEGIN]";
    private final String mIfEndTag = "[SBYNTAG:IF-END]";
    private final String mParamBeginTag = "[SBYNTAG:PARAM-BEGIN]";
    private final String mParamEndTag = "[SBYNTAG:PARAM-END]";
    private final String mParamSQLServerEndTag = "[SBYNTAG:PARAM-SQLSERVER-END]";   // SQL Server
    // TODO: add values for additional database vendors as needed
    private final String mSQLColBeginTag = "[SBYNTAG:SQLCOLLIST-BEGIN]";
    private final String mSQLColEndTag = "[SBYNTAG:SQLCOLLIST-END]";
    private final String mSQLQMarkBeginTag = "[SBYNTAG:SQL?LIST-BEGIN]";
    private final String mSQLQMarkEndTag = "[SBYNTAG:SQL?LIST-END]";
    private final String mSQLSetBeginTag = "[SBYNTAG:SQLSETLIST-BEGIN]";
    private final String mSQLSetEndTag = "[SBYNTAG:SQLSETLIST-END]";
    private String mTemplateBuffer;
    private String mTemplateFileName;

    private final int ORACLE = 0;       // Oracle processing
    private final int SQLSERVER= 1;     // SQL Server processing
    // TODO: add values for additional database vendors as needed
    

    /**
     * @param templatefilename template file name
     * @exception TemplateFileNotFoundException template fiel not found
     * exception
     */
    public TemplateWriter(String templatefilename)
        throws TemplateFileNotFoundException {
        mTemplateFileName = templatefilename;
        try {
            InputStream is = TemplateWriter.class.getResourceAsStream("/" + templatefilename);
            if (is == null) {
                throw new TemplateFileNotFoundException(templatefilename);
            }
            byte[] buf = new byte[0];
            byte[] chunk = new byte[4096];
            int count;
            while ((count = is.read(chunk)) >= 0) {
                byte[] temp = new byte[buf.length + count];
                System.arraycopy(buf, 0, temp, 0, buf.length);
                System.arraycopy(chunk, 0, temp, buf.length, count);
                buf = temp;
            }
            mTemplateBuffer = new String(buf, "ISO8859-1");
            is.close();            
        } catch (java.io.IOException ex) {
            throw new TemplateFileNotFoundException(templatefilename);
        }
    }

    /**
     * Construct a template writer from an InputStream
     * @param is the template file as input stream
     * @param streamName the name of the stream to identify it for logging/exceptions
     * @exception TemplateFileNotFoundException template fiel not found
     * exception
     */
    public TemplateWriter(java.io.InputStream is, String streamName)
        throws TemplateFileNotFoundException {
        mTemplateFileName = streamName;
        try {
            byte[] buf = new byte[0];
            byte[] chunk = new byte[4096];
            int count;
            while ((count = is.read(chunk)) >= 0) {
                byte[] temp = new byte[buf.length + count];
                System.arraycopy(buf, 0, temp, 0, buf.length);
                System.arraycopy(chunk, 0, temp, buf.length, count);
                buf = temp;
            }
            mTemplateBuffer = new String(buf, "ISO8859-1");
            is.close();
        } catch (java.io.IOException ex) {
            throw new TemplateFileNotFoundException(streamName);
        }
    }
    
    /**
     * @param list list
     * @return ArrayList list 
     */
    public static ArrayList removeFirst(ArrayList list) {
        ArrayList ret = null;
        if (null != list) {
            ret = new ArrayList(list);
            ret.remove(0);
        }

        return ret;
    }


    /**
     * @param construct
     * Getter for Body attribute of the TemplateWriter object
     * @return String ret string
     */
    public String getBody(String construct) {
        int idx = construct.indexOf(mEndTag);
        int idx2 = construct.indexOf("\n");
        return construct.substring(idx2 + 1, construct.length());
    }


    /**
     * @param construct
     * Getter for Tags attribute of the TemplateWriter object
     * @return ArraytList array list
     */
    public ArrayList getTags(String construct) {
        int idx = construct.indexOf(mEndTag);
        String head;
        if (isRepeatConstruct(construct)) {
            head = mDefineTag + "()";
        } else {
            head = mDefineTag;
        }
        String header = construct.substring(head.length(), idx);

        ArrayList ret = new ArrayList();

        StringTokenizer token = new StringTokenizer(header, ",");
        while (token.hasMoreTokens()) {
            ret.add(token.nextToken());
        }

        return ret;
    }


    /**
     * @param tag tag
     * Getter for Array attribute of the TemplateWriter object
     * @return boolean boolean
     */
    public boolean isArray(String tag) {
        return (tag.endsWith("()"));
    }


    /**
     * @param param parameter
     * Getter for String attribute of the TemplateWriter object
     * @return boolean boolean
     */
    public boolean isString(Object param) {
        return (param instanceof java.lang.String);
    }


    /**
     * @exception InvalidTemplateFileException invalid template file exception
     * @return ArrayList list of construct
     */
    public ArrayList construct()
        throws InvalidTemplateFileException {
        ArrayList ret = new ArrayList();

        if (!mTemplateBuffer.startsWith(mDefineTag)) {
            throw new InvalidTemplateFileException(mTemplateFileName);
        }

        mTemplateBuffer 
            = mTemplateBuffer.substring(mDefineTag.length(), 
                                        mTemplateBuffer.length());
        int idx = mTemplateBuffer.indexOf(mDefineTag);
        while (idx >= 0) {
            ret.add(mDefineTag + mTemplateBuffer.substring(0, idx));
            mTemplateBuffer = 
            mTemplateBuffer.substring(idx + mDefineTag.length(), 
                                      mTemplateBuffer.length());
            idx = mTemplateBuffer.indexOf(mDefineTag);
        }
        ret.add(mDefineTag + mTemplateBuffer);

        return ret;
    }


    // tags are normalized with the array of values
    /**
     * @param body body
     * @param tags tags
     * @param values values
     * @return String ret string
     */
    public String flatReplace(String body, ArrayList tags, ArrayList values) {
        for (int i = 0; i < tags.size(); i++) {
            String tag = "[sbyntag:" + (String) tags.get(i);
            if (body.indexOf(tag) < 0) {
                continue;
            }

            String value;
            if (isString(values.get(i))) {
                value = (String) values.get(i);
                body = replaceAll(body, tag, value);
            }
        }

        return body;
    }


    /**
     * @param chunk chunk
     * @param tags tags
     * @param values values
     * @exception UnmatchedTagsException unmatched tags exception
     * @return String ret string
     */
    public String ifReplace(String chunk, ArrayList tags, ArrayList values)
        throws UnmatchedTagsException {
        String ret = "";
        int arraysize = 0;
        try {
            arraysize = validateArray(chunk, tags, values);
        } catch (UnmatchedTagsException e) {
            throw e;
        }

        for (int i = 0; i < arraysize; i++) {
            String buf = chunk;
            for (int j = 0; j < tags.size(); j++) {
                String tag = (String) tags.get(j);
                if (chunk.indexOf(tag) < 0) {
                    continue;
                }

                String value = "";
                if (!isArray(tag)) {
                    value = (String) values.get(j);
                } else {
                    value = (String) ((ArrayList) values.get(j)).get(i);
                }
                buf = replaceAll(buf, "[sbyntag:" + tag, value);
            }
            buf = 
            replaceAll(buf, "[sbyntag:count", (new Integer(i)).toString());
            if (i > 0) {
                ret += buf.replaceFirst("if", "else if") + "\n";
            } else {
                ret += buf + "\n";
            }
        }

        return ret;
    }

    /**
     * @param chunk chunk
     * @param tags tags
     * @param values values
     * @param databaseType database type
     * @exception UnmatchedTagsException unmatched tags exception
     * @return String ret string
     */
    public String paramReplace(String chunk, ArrayList tags, ArrayList values, int databaseType)
            throws UnmatchedTagsException {
                
        String ret = "";
        int arraysize = 0;
        try {
            arraysize = validateArray(chunk, tags, values);
        } catch (UnmatchedTagsException e) {
            throw e;
        }

        for (int i = 0; i < arraysize; i++) {
            String buf = chunk;
            for (int j = 0; j < tags.size(); j++) {
                String tag = (String) tags.get(j);
                if (chunk.indexOf(tag) < 0) {
                    continue;
                }

                String value = "";
                if (!isArray(tag)) {
                    value = (String) values.get(j);
                } else {
                    value = (String) ((ArrayList) values.get(j)).get(i);
                }
                buf = replaceAll(buf, "[sbyntag:" + tag, value);
            }
            buf = 
                replaceAll(buf, "[sbyntag:count", (new Integer(i)).toString());
            if (i == arraysize - 1) {
                // For Oracle, replace the comma with an end parenthesis
                if (databaseType == ORACLE) {   
                    ret += buf.replaceFirst(",", ")") + "\n";
                } else { // For SQL Server, do not replace the comma
                    ret += buf;
                }
                // TODO:  Add code to handle other database venders as needed.
            } else {
                ret += buf + "\n";
            }
        }

        return ret;
    }
    /**
     * @param chunk chunk
     * @param tags tags
     * @param values values
     * @exception UnmatchedTagsException unmatched tags exception
     * @return String ret string
     */
    public String paramReplace(String chunk, ArrayList tags, ArrayList values)
            throws UnmatchedTagsException {
            
        return paramReplace(chunk, tags, values, ORACLE);      // Oracle by default
    }


    /**
     * @param chunk chunk 
     * @param tags tags
     * @param values values
     * @exception UnmatchedTagsException unmatched tags exception
     * @return String string
     */
    public String repeatReplace(String chunk, ArrayList tags, ArrayList values)
        throws UnmatchedTagsException {
        String ret = "";
        int arraysize = 0;
        try {
            arraysize = validateArray(chunk, tags, values);
        } catch (UnmatchedTagsException e) {
            throw e;
        }
        for (int i = 0; i < arraysize; i++) {
            String buf = chunk;
            for (int j = 0; j < tags.size(); j++) {
                String tag = (String) tags.get(j);
                if (chunk.indexOf(tag) < 0) {
                    continue;
                }

                String value = "";
                if (!isArray(tag)) {
                    value = (String) values.get(j);
                } else {
                    value = (String) ((ArrayList) values.get(j)).get(i);
                }
                buf = replaceAll(buf, "[sbyntag:" + tag, value);
            }
            buf = 
            replaceAll(buf, "[sbyntag:count", (new Integer(i)).toString());
            ret += buf + "\n";
        }

        return ret;
    }


    /**
     * @param chunk chunk
     * @param tags tags
     * @param values values
     * @exception UnmatchedTagsException unmatched tags exception
     * @return String sql string
     */
    public String sqlColReplace(String chunk, ArrayList tags, ArrayList values)
        throws UnmatchedTagsException {
        String ret = "";
        int arraysize = 0;
        try {
            arraysize = validateArray(chunk, tags, values);
        } catch (UnmatchedTagsException e) {
            throw e;
        }

        for (int i = 0; i < arraysize; i++) {
            String buf = chunk;
            for (int j = 0; j < tags.size(); j++) {
                String tag = (String) tags.get(j);
                if (chunk.indexOf(tag) < 0) {
                    continue;
                }

                String value = "";
                if (!isArray(tag)) {
                    value = (String) values.get(j);
                } else {
                    value = (String) ((ArrayList) values.get(j)).get(i);
                }
                if (i == arraysize - 1) {
                    buf = replaceAll(buf, "[sbyntag:" + tag, value);
                } else {
                    buf = replaceAll(buf, "[sbyntag:" + tag, value + ",");
                }
            }
            ret += buf + "\n";
        }

        return ret;
    }


    /**
     * @param chunk chunk
     * @param tags tags
     * @param values values
     * @exception UnmatchedTagsException unmatched tags exception
     * @return String sql string
     */
    public String 
        sqlQMarkReplace(String chunk, ArrayList tags, ArrayList values)
        throws UnmatchedTagsException {
        String ret = "";
        int arraysize = 0;
        try {
            arraysize = validateArray(chunk, tags, values);
        } catch (UnmatchedTagsException e) {
            throw e;
        }

        for (int i = 0; i < arraysize; i++) {
            String buf = chunk;
            for (int j = 0; j < tags.size(); j++) {
                String tag = (String) tags.get(j);
                if (chunk.indexOf(tag) < 0) {
                    continue;
                }

                String value = "";
                if (!isArray(tag)) {
                    value = (String) values.get(j);
                } else {
                    value = (String) ((ArrayList) values.get(j)).get(i);
                }
                if (i == arraysize - 1) {
                    buf = replaceAll(buf, "[sbyntag:" + tag, "?");
                } else {
                    buf = replaceAll(buf, "[sbyntag:" + tag, "?,");
                }
            }
            ret += buf + "\n";
        }

        return ret;
    }


    /**
     * @param chunk chunk
     * @param tags tags
     * @param values values
     * @exception UnmatchedTagsException unmatched tag exception
     * @return String sql string
     */
    public String sqlSetReplace(String chunk, ArrayList tags, ArrayList values)
        throws UnmatchedTagsException {
        String ret = "";
        int arraysize = 0;
        try {
            arraysize = validateArray(chunk, tags, values);
        } catch (UnmatchedTagsException e) {
            throw e;
        }

        for (int i = 0; i < arraysize; i++) {
            String buf = chunk;
            for (int j = 0; j < tags.size(); j++) {
                String tag = (String) tags.get(j);
                if (chunk.indexOf(tag) < 0) {
                    continue;
                }

                String value = "";
                if (!isArray(tag)) {
                    value = (String) values.get(j);
                } else {
                    value = (String) ((ArrayList) values.get(j)).get(i);
                }
                if (i == arraysize - 1) {
                    buf = replaceAll(buf, "[sbyntag:" + tag, value + " = ? ");
                } else {
                    buf = replaceAll(buf, "[sbyntag:" + tag, value + " = ?,");
                }
            }
            ret += buf + "\n";
        }

        return ret;
    }


    // return number of iterations

    /**
     * @param tags tags
     * @param params parameter list
     * @exception UnmatchedTagsException unmatched tags exception
     */
    public void validateParams(ArrayList tags, ArrayList params)
        throws UnmatchedTagsException {
        if (null == tags && null != params) {
            throw new UnmatchedTagsException("No tags are used");
        }

        if (null != tags && null == params) {
            throw new UnmatchedTagsException("Need to match tags");
        }

        if (tags != null && params != null && tags.size() != params.size()) {
            throw new UnmatchedTagsException("Value array (" 
                + 
                params.size() 
                + 
                ") has different size from number of tags (" 
                + 
                tags.size() 
                + ")");
        }

        if (tags != null) {
            for (int i = 0; i < tags.size(); i++) {
                String tag = (String) tags.get(i);
                java.lang.Object param = params.get(i);
            
                if (!isArray(tag)) {
                    // expecting string value
            
                    if (!isString(param)) {
                        throw new UnmatchedTagsException("Value[" 
                            + 
                            i 
                            + 
                            "] is not of String value");
                    }
                } else if (isArray(tag)) {
                    if (isString(param)) {
                        throw new UnmatchedTagsException("Value[" 
                        +
                        i 
                        + 
                        "] is not of array type");
                    }
                }
            }
        }
    }


    /**
     * @param construct construct
     * @param values values
     * @exception UnmatchedTagsException unmatched tag exception
     * @return String construct
     */
    public String writeConstruct(String construct, ArrayList values)
        throws UnmatchedTagsException {
        String ret = "";
        if (isRepeatConstruct(construct)) {
            if (null != values) {
                try {
                    for (int i = 0; i < values.size(); i++) {
                        ArrayList val = (ArrayList) values.get(i);
                        ret += 
                            writeConstruct(construct.substring(0, 
                                mDefineTag.length()) 
                                + 
                                construct.substring(mDefineTag.length() + 2, 
                                construct.length()), val);
                    }
                } catch (UnmatchedTagsException e) {
                    throw e;
                }
            }
        } else {
            ArrayList tags = getTags(construct);
            String body = getBody(construct);
            try {

                validateParams(tags, values);
                if (null == tags) {
                    ret = body;
                } else {
                    String chunk;
                    int idx = body.indexOf("[SBYNTAG:");
                    while (idx >= 0) {
                        chunk = body.substring(0, idx);
                        ret += flatReplace(chunk, tags, values);
                        body = body.substring(idx, body.length());
                        if (body.startsWith(mRepeatBeginTag)) {
                            int idx2 = body.indexOf("\n");
                            idx = body.indexOf(mRepeatEndTag);
                            chunk = body.substring(idx2 + 1, idx - 1);
                            ret += repeatReplace(chunk, tags, values);
                            body = body.substring(idx, body.length());
                            idx2 = body.indexOf("\n");
                            body = body.substring(idx2 + 1, body.length());
                        } else if (body.startsWith(mIfBeginTag)) {
                            int idx2 = body.indexOf("\n");
                            idx = body.indexOf(mIfEndTag);
                            chunk = body.substring(idx2 + 1, idx - 1);
                            ret += ifReplace(chunk, tags, values);
                            body = body.substring(idx, body.length());
                            idx2 = body.indexOf("\n");
                            body = body.substring(idx2 + 1, body.length());
                        } else if (body.startsWith(mParamBeginTag)) {
                            int idx2 = body.indexOf("\n");
                            idx = body.indexOf(mParamEndTag);
                            int dbType = ORACLE;    // default value
                            if (idx == -1) {        // SQL Server
                                idx = body.indexOf(mParamSQLServerEndTag);
                                dbType = SQLSERVER;
                            }  // TODO: add values for additional database vendors as needed
                            chunk = body.substring(idx2 + 1, idx - 1);
                            ret += paramReplace(chunk, tags, values, dbType);
                            body = body.substring(idx, body.length());
                            idx2 = body.indexOf("\n");
                            body = body.substring(idx2 + 1, body.length());
                        } else if (body.startsWith(mSQLColBeginTag)) {
                            int idx2 = body.indexOf("\n");
                            idx = body.indexOf(mSQLColEndTag);
                            chunk = body.substring(idx2 + 1, idx - 1);
                            ret += sqlColReplace(chunk, tags, values);
                            body = body.substring(idx, body.length());
                            idx2 = body.indexOf("\n");
                            body = body.substring(idx2 + 1, body.length());
                        } else if (body.startsWith(mSQLQMarkBeginTag)) {
                            int idx2 = body.indexOf("\n");
                            idx = body.indexOf(mSQLQMarkEndTag);
                            chunk = body.substring(idx2 + 1, idx - 1);
                            ret += sqlQMarkReplace(chunk, tags, values);
                            body = body.substring(idx, body.length());
                            idx2 = body.indexOf("\n");
                            body = body.substring(idx2 + 1, body.length());
                        } else if (body.startsWith(mSQLSetBeginTag)) {
                            int idx2 = body.indexOf("\n");
                            idx = body.indexOf(mSQLSetEndTag);
                            chunk = body.substring(idx2 + 1, idx - 1);
                            ret += sqlSetReplace(chunk, tags, values);
                            body = body.substring(idx, body.length());
                            idx2 = body.indexOf("\n");
                            body = body.substring(idx2 + 1, body.length());
                        }

                        idx = body.indexOf("[SBYNTAG:");
                    }

                    ret += flatReplace(body, tags, values);
                }
            } catch (UnmatchedTagsException ex) {
                throw ex;
            }
        }

        return ret;
    }


    private boolean isRepeatConstruct(String construct) {
        return (construct.startsWith(mDefineTag + "()"));
    }


    private String replaceAll(String buf, String tag, String value) {
        String ret = buf;
        int idx = ret.indexOf(tag);
        while (idx >= 0) {
            String t = tag;
            String tmp = ret.substring(idx, ret.length());
            int idx2 = tmp.indexOf(mEndTag);
            String val;
            if (tmp.substring(0, idx2).endsWith(".toLowerCase")) {
                t += ".toLowerCase" + mEndTag;
                val = value.toLowerCase();
            } else if (tmp.substring(0, idx2).endsWith(".makeClassName")) {
                t += ".makeClassName" + mEndTag;
                val = CodeGeneratorUtil.makeClassName(value);
            } else if (tmp.substring(0, idx2).endsWith(".makeJavaName")) {
                t += ".makeJavaName" + mEndTag;
                val = CodeGeneratorUtil.makeJavaName(value);
            } else if (tmp.substring(0, idx2).endsWith(".makeBeanName")) {
                t += ".makeBeanName" + mEndTag;
                val = CodeGeneratorUtil.makeBeanName(value);
            } else if (tmp.substring(0, idx2).endsWith(".toUpperCase")) {
                t += ".toUpperCase" + mEndTag;
                val = value.toUpperCase();
            } else if (tmp.substring(0, idx2).endsWith(".toLowerInitial")) {
                t += ".toLowerInitial" + mEndTag;
                val 
                    = value.substring(0, 1).toLowerCase() 
                      + 
                      value.substring(1, value.length());
            } else {
                t += mEndTag;
                val = value;
            }

            ret = ret.substring(0, idx) 
                  + 
                  val 
                  + ret.substring(idx + t.length(), ret.length());
            idx = ret.indexOf(tag);
        }

        return ret;
    }


    private int validateArray(String chunk, ArrayList tags, ArrayList values)
        throws UnmatchedTagsException {
        int arraysize = -1;

        for (int i = 0; i < tags.size(); i++) {
            String tag = (String) tags.get(i);
            if (chunk.indexOf("[sbyntag:" + tag) < 0) {
                continue;
            }

            if (isArray(tag)) {
                ArrayList val = (ArrayList) values.get(i);
                if (arraysize == -1) {
                    arraysize = val.size();
                } else if (arraysize != val.size()) {
                    throw new UnmatchedTagsException(
                        "value[" 
                        + 
                        i 
                        + 
                        "] has different size(" 
                        +
                        val.size() 
                        + 
                        ") from previously detected array size of " 
                        + 
                        arraysize);
                }
            }
        }

        return arraysize;
    }
}
