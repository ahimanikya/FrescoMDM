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

import junit.framework.Assert;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import java.util.Arrays;


/**
 * Generic Testing Utilities.
 *
 * @author mpoplacenel
 * @version $Revision: 1.5 $
 */
public class TestUtils {
    /** default size of the buffer for comparing binary files (64KBytes) */
    public static final int DEFAULT_BUF_SIZE = 1 << 16;

    /**
     * returns a {@link java.io.File} wrapper to represent the given file name.
     *
     * @param fileName the name of the file to be represented
     * @param backupFile the file to be returned if the specified file does not
     *        exist.
     *
     * @return a {@link java.io.File} wrapper around the given file name, if
     *         this exists, or the backup {@link java.io.File} otherwise.
     */
    public static File getFileOrBackup(
        String fileName,
        File   backupFile) {
        return getFileOrBackup(
            new File(fileName),
            backupFile);
    }

    /**
     * returns the given file if it exists, or the backup file otherwise.
     *
     * @param file the file to be tested
     * @param backupFile the file to be returned if the specified file does not
     *        exist.
     *
     * @return the given {@link java.io.File} if it exists, or the backup file
     *         otherwise.
     */
    public static File getFileOrBackup(
        File file,
        File backupFile) {
        if (file.exists()) {
            return file;
        }

        return backupFile;
    }

    /**
     * asserts two files are identical
     *
     * @param file1 DOCUMENT ME!
     * @param file2 DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void assertBinaryFilesEqual(
        File file1,
        File file2)
        throws IOException {
        BufferedInputStream inputStream1 =
            new BufferedInputStream(new FileInputStream(file1));
        BufferedInputStream inputStream2 =
            new BufferedInputStream(new FileInputStream(file2));
        byte                buf1[] = new byte[DEFAULT_BUF_SIZE];

        //        Arrays.fill(buf1, 0);
        int   size1  = -1;
        byte  buf2[] = new byte[DEFAULT_BUF_SIZE];
        int   size2  = -1;

        while ((size1 = inputStream1.read(buf1)) != -1) {
            Assert.assertEquals(
                (size2 = inputStream2.read(buf2)),
                size2);
            Assert.assertTrue(Arrays.equals(
                    buf1,
                    buf2));
            Arrays.fill(
                buf1,
                (byte) 0);
            Arrays.fill(
                buf2,
                (byte) 0);
        }

        Assert.assertEquals(
            inputStream2.read(buf2),
            -1);
    }

    /**
     * asserts a given {@link java.io.File File} exists in the filesystem.
     *
     * @param file DOCUMENT ME!
     */
    public static void assertFileExists(File file) {
        Assert.assertTrue(
            "File " + file.getAbsolutePath() + " does not exist",
            file.exists());
    }

    /**
     * asserts a file with the given name exists in the filesystem.
     *
     * @param name DOCUMENT ME!
     *
     * @return the {@link java.io.File File} object representing the file with
     *         the specified name (only if it does not fail).
     */
    public static File assertFileExists(String name) {
        File file = new File(name);
        assertFileExists(file);

        return file;
    }

    /**
     * asserts that two text files are identical in content. You are allowed to
     * separately specify if the empty (space-only) lines should be ignored or
     * not, and if the beginning/ending spaces should be ignored.
     *
     * @param file1 the first file
     * @param file2 the second file
     * @param ignoreEmptyLines flag that specifies if the space-only lines
     *        should be ignored or not
     * @param trimLines flag that specifies if the lines should be trimmed
     *        before comparison. This allows you to ignore the beginning and
     *        trailing blanks.
     *
     * @throws IOException if the files are found at some point as not being
     *         identical.
     */
    public static void assertTextFilesEqual(
        File    file1,
        File    file2,
        boolean ignoreEmptyLines,
        boolean trimLines)
        throws IOException {
        BufferedReader reader1 = new BufferedReader(new FileReader(file1));
        BufferedReader reader2 = new BufferedReader(new FileReader(file2));
        String         line1   = null;
        String         line2   = null;
        int            lineNo  = 0;

        while ((line1 = reader1.readLine()) != null) {
            lineNo++;

            if (trimLines) {
                line1 = line1.trim();
            }

            if (!ignoreEmptyLines || !"".equals(line1)) {
                line2 = reader2.readLine();

                while (
                    ignoreEmptyLines
                        && (line2 != null)
                        && ("".equals(line2.trim()))) {
                    line2 = reader2.readLine();
                }

                Assert.assertNotNull(
                    "Premature end of file " + file2.getAbsolutePath(),
                    line2);

                if (trimLines) {
                    line2 = line2.trim();
                }

                Assert.assertEquals(
                    "Line " + lineNo + " differs in files "
                    + file1.getAbsolutePath() + " and "
                    + file2.getAbsolutePath(),
                    line1.trim(),
                    line2);
            }
        }

        Assert.assertNull(
            "Premature end of file " + file1.getAbsolutePath(),
            reader2.readLine());
    }

    /**
     * replaces the extension for file name. If the input file name doesn't
     * have any extension, the new extension will be just appended to it.
     *
     * @param inputFileName the array of file names
     * @param newExtension the extension that replaces the old one (should
     *        include the dot '.')
     *
     * @return the file name whose body matches the one of the input but the
     *         extension is the one specified as parameter.
     */
    public static String replaceFileExtension(
        String inputFileName,
        String newExtension) {
        int lastDotPos = inputFileName.lastIndexOf('.');

        if (lastDotPos == -1) {
            return inputFileName + newExtension;
        }

        return inputFileName.substring(
            0,
            lastDotPos) + newExtension;
    }

    /**
     * replaces the extension for a whole array of file names. If the input
     * file names don't have any extension, the new extension will be appended
     * to each and every one of them.
     *
     * @param inputFileNames the array of file names
     * @param newExtension the extension that replaces the old one (should
     *        include the dot '.')
     *
     * @return an array of file names whose bodies match the ones of the input
     *         but the extension is the one specified as parameter.
     */
    public static String[] replaceFilesExtension(
        String   inputFileNames[],
        String   newExtension) {
        String replacedFileNames[] = new String[inputFileNames.length];

        for (int i = 0; i < inputFileNames.length; i++) {
            replacedFileNames[i] =
                replaceFileExtension(
                    inputFileNames[i],
                    newExtension);
        }

        return replacedFileNames;
    }
}
