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
package com.sun.mdm.multidomain.project.generator;

import java.io.File;


/**
 * utility class for code generation. contains a variety of generic code
 * generation utility methods
 *
 * @author bhahn, hlin
 * @version 0.1
 */
public class CodeGeneratorUtil {

    private static final String [] DOS_SERVICE_NAMES =
    {
        "CON", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4",
        "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4",
        "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"
    };

    public static final String GROVE = "Grove";

    public static final String PKG = "Pkg";

    private static final String KEYWORDS[] =
    {
        "abstract", "boolean", "break", "byte", "byvalue", "case", "cast",
        "catch", "char", "class", "const", "continue", "default", "do",
        "double", "else", "extends", "false", "final", "finally", "float",
        "for", "future", "generic", "goto", "if", "implements", "import",
        "inner", "instanceof", "int", "interface", "long", "native", "new",
        "null", "operator", "outer", "package", "private", "protected",
        "public", "rest", "return", "short", "static", "strictfp", "string", "super",
        "switch", "synchronized", "this", "throw", "throws", "transient",
        "true", "try", "var", "void", "volatile", "while"
    };

    // These are the names of classes that are imported by source code
    // generated by Antlr (www.antlr.org).

    private static final String [] ANTLR_NAMES =
    {
        "TokenBuffer", "TokenStreamException", "TokenStreamIOException",
        "ANTLRException", "LLkParser", "Token", "TokenStream",
        "RecognitionException", "NoViableAltException",
        "MismatchedTokenException", "SemanticException",
        "ParserSharedInputState", "BitSet", "AST", "ASTPair", "ASTArray"
    };

    /** keywords to check against */
    protected static java.util.Set mKeywordSet = new java.util.HashSet();

    /** DOS device names to check names against */
    protected static java.util.Set mDOSDeviceNamesSet =
        new java.util.HashSet();

    /** ANTLR names to check names against */
    protected static java.util.Set mAntlrNamesSet =
        new java.util.HashSet();

    static {
        for (int i = 0; i < KEYWORDS.length; i++) {
            mKeywordSet.add(KEYWORDS[i]);
        }
    }

    static {
        for (int i = 0; i < DOS_SERVICE_NAMES.length; i++) {
            mDOSDeviceNamesSet.add(DOS_SERVICE_NAMES[i].toUpperCase());
        }
    }

    static {
        for (int i = 0; i < ANTLR_NAMES.length; i++) {
            mAntlrNamesSet.add(ANTLR_NAMES[i]);
        }
    }

    /**
     * default constructor
     */
    public CodeGeneratorUtil() {
    }


    /**
     * Create the full package working directory.
     *
     * @param baseDir The base working directory.
     * @param pkgName The user-defined package name.
     * @return The created package directory.
     * @throws Exception if an error occurs during directory creation.
     */
    public static File createPackageDir(File baseDir, String pkgName) 
            throws Exception {
        if (baseDir == null) {
            throw new Exception("Invalid working directory specified.");
        }

        File pkgDir = new File(baseDir, packageToPath(pkgName));

        if (pkgDir.exists() && pkgDir.isFile()) {
            throw new Exception("Invalid package directory specified:"+pkgName);
        }

        pkgDir.mkdirs();

        return pkgDir;
    }

    /**
     * Create the base working directory.
     *
     * @param baseDir The base working directory.
     * @return The created working directory.
     * @throws Exception if an error occurs during directory creation.
     */
    public static File createWorkingDir(File baseDir)
            throws Exception {
                
        java.text.SimpleDateFormat sf =
            new java.text.SimpleDateFormat("yyyyMMddHHmmssSSSS");

        if (baseDir == null) {
            throw new Exception("Invalid working directory specified.");
        }

        File workingDir = new File(baseDir, sf.format(new java.util.Date()));

        if (!workingDir.isDirectory()) {
            workingDir.mkdirs();
        }

        return workingDir;
    }

    /**
     * Checks generated Java names against Java keywords.
     *
     * @param name A Java identifier name.
     * @return A non-Java keyword name.
     */
    public static String crossCheckKeywords(String name) {
        String rname = name;

        if (mKeywordSet.contains(rname.toLowerCase())) {
            rname = name + "_";
        }

        return rname;
    }

    /**
     * Creates an appropriate Java bean name using the standard Java naming
     * convention.
     *
     * @param name String to create bean name from.
     * @return The Java bean name.
     */
    public static String makeBeanName(String name) {
        int pos = 0;
        String javaName = makeJavaName(name);
        String beanName =
            javaName.substring(pos, pos + 1)
                    .toUpperCase() + javaName.substring(pos + 1);

        // Cannot override final getClass() in java.lang.object.
        if (beanName.equals("Class")) {
            beanName = beanName + "_";
        }

        return beanName;
    }

    /**
     * Creates a legal Java class name from a given string.
     *
     * @param name A generic name.
     * @return A qualified Java class name.
     */
    public static String makeClassName(String name) {
        String result = makeJavaName(name);

        if (mDOSDeviceNamesSet.contains(result.toUpperCase())) {
            result = result + "_";
        } else if (mAntlrNamesSet.contains(result)) {
            result = result + "_";
        }

        result = result.substring(0,1)
                       .toUpperCase() + result.substring(1);

        return result;
    }

    /**
     * Creates a legal Java identifier name from a given String.
     *
     * @param name Name to create Java name from.
     * @return A qualified Java identifier name.
     */
    public static String makeJavaName(String name) {
        StringBuffer jName = new StringBuffer();
        if (name == null) {
            name = "";
        }
        name = name.trim();

        int size = name.length();
        char ncChars[] = name.toCharArray();
        int i = 0;

        for (i = 0; i < size; i++) {
            char ch = ncChars[i];

            if (((i == 0) && !Character.isJavaIdentifierStart(ch)
                        && !Character.isDigit(ch))
                    || ((i > 0) && !Character.isJavaIdentifierPart(ch))) {
                jName.append('_');
            } else {
                jName.append(ncChars[i]);
            }
        }

        if ((i > 0) && Character.isDigit(jName.charAt(0))) {
            jName.insert(0,"X_");
        }
        if ((i > 0) && jName.charAt(0) == '_') {
            jName.insert(0,"X");
        }

        return crossCheckKeywords(jName.toString());
    }



    /**
     * Transforms package names to directory path names.
     *
     * @param pkgName The package name.
     * @return The package path name.
     */
    public static String packageToPath(String pkgName) {
        if (pkgName == null) {
            return "";
        }
        return pkgName.replace('.',System.getProperty("file.separator").charAt(0));
    }

    /**
     * Transforms package names to unix directory path names.
     *
     * @param pkgName The package name.
     * @return The package path name.
     */
    public static String packageToUnixPath(String pkgName) {
        return pkgName.replace('.','/');
    }

    /**
     * Transforms path names to package names.
     *
     * @param name the path name
     * @return the package path name, "" is returned if name is null
     */
    public static String pathToPackage(String name) {
        if (name == null || name.equals(""))
            return "";

        StringBuffer pkgName = new StringBuffer();
        java.util.StringTokenizer st 
            = new java.util.StringTokenizer(name,
                    String.valueOf(System.getProperty("file.separator").charAt(0)));

        while (st.hasMoreElements()) {
            String tp = makeJavaName(st.nextToken());
            pkgName.append(String.valueOf(tp.charAt(0)).toLowerCase());
            pkgName.append(tp.substring(1) + ".");
        }

        return pkgName.toString().substring(0,pkgName.length() - 1);
    }

    /**
     * Transforms path names to package names.
     *
     * @param path The path name.
     * @param otdName OTD name.
     * @return The package path name.
     */
    public static String pathToPackage(String path,String otdName) {
        String name = null;

        if ((path != null) && (path.trim().length() > 0)) {
            name = path + System.getProperty("file.separator").charAt(0) + otdName;
        } else {
            name = otdName;
        }

        StringBuffer pkgName = new StringBuffer();
        java.util.StringTokenizer st 
            = new java.util.StringTokenizer(name,
                String.valueOf(System.getProperty("file.separator").charAt(0)));

        while (st.hasMoreElements()) {
            String tp = makeJavaName(st.nextToken());
            pkgName.append(String.valueOf(tp.charAt(0)).toLowerCase());
            pkgName.append(tp.substring(1) + ".");
        }

        return pkgName.toString().substring(0,pkgName.length() - 1);
    }

}
