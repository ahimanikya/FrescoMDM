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
package com.sun.mdm.index.ejb.master.helper;

import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.MatchResult;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.master.UserException;

import java.io.FileReader;

import java.rmi.RemoteException;

import java.util.Locale;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.rmi.PortableRemoteObject;


/** Class that demostrates how to use eIndex classes to process records in
 * a file.
 *
 * <<< Warning >>>: if using multiple threads, out of order evaluation of
 * input file may occur.  This is not a thread safety issue as far as the
 * API is concerned, it is simply a statement that some records get processed
 * faster than others, and this test class does not ensure each record is
 * processed in the order specified in the input file.
 *
 * @author dcidon
 */
public class ProcessFileMultiThread implements Runnable {
    private static SystemObjectIterator pi;
    static int recordCount = 0;
    private static boolean haltThreads = false;
    private static int threadCount;
    private static MasterController masterController;
    private static String server;
    private String threadName;

    /**
     * Constructor
     * @param threadName thread name
     */
    public ProcessFileMultiThread(String threadName) {
        this.threadName = threadName;

        Thread worker = new Thread(this, threadName);
        worker.start();
    }

    /** Entry point to class.
     * @param args Param1: parser name (eiEvent or HL7)
     * Param2: file name (file to be processed)
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        try {
            //Check arguments
            if (args.length != 4) {
                System.out.println(
                    "Usage: ProcessFile [server] [parser name] [input file name] [threads]");
                System.out.println();
                System.out.println(
                    "       [server]:        t3://localhost:7001");
                System.out.println("       [parser name]:   eiEvent, HL7");
                System.out.println(
                    "       [file name]:     Full filename and path");
                System.out.println("       [threads]:       Number of threads");

                return;
            }

            System.out.println("Getting MasterController");
            server = (String) args[0];
            masterController = getMasterController();
            System.out.println("Processing person records");

            FileReader reader = new FileReader(args[2]);

            if (args[1].equals("eiEvent")) {
                pi = new EiEventIterator(masterController, reader);
            } else if (args[1].equals("HL7")) {
                throw new Exception("HL7 not yet supported");

                //pi = new HL7_2_2_SystemObjectIterator(eiServer, reader, false);
            } else {
                throw new Exception("Invalid parser name");
            }

            int aThreadCount = Integer.parseInt(args[3]);
            ProcessFileMultiThread[] threadArray = new ProcessFileMultiThread[aThreadCount];
            threadCount = aThreadCount;

            for (int i = 1; i <= aThreadCount; i++) {
                threadArray[i - 1] = new ProcessFileMultiThread("thread" + i);
            }

            //Wait for thread completion.
            while (threadCount > 0) {
                Thread.sleep(1000);
            }

            System.gc();
            System.runFinalization();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static synchronized PersonRecord nextRecord()
        throws ProcessingException {
        PersonRecord pr = null;

        if (pi.hasNext()) {
            try {
                pr = new PersonRecord();
                pr.record = pi.next();
                pr.recordNumber = recordCount;
            } finally {
                recordCount++;
            }
        }

        return pr;
    }

    private static synchronized MatchResult processRecord(PersonRecord pr)
        throws ProcessingException, RemoteException, UserException {
        SystemObject[] sysObj = pr.record.getSystemObjects();

        try {
            PersonObject person = (PersonObject) sysObj[0].getObject();
        } catch (Exception e) {
            throw new ProcessingException(e);
        }

        TransactionObject trans = pr.record.getTransaction();
        MatchResult mr = masterController.executeMatch(sysObj[0]);
        for (int i = 1; i < sysObj.length; i++) {
            masterController.addSystemObject(mr.getEUID(), sysObj[i]);
        }        
        
        return mr;
    }

    /**
     * Execute thread
     */
    public void run() {
        PersonRecord pr = null;

        while (!haltThreads) {
            try {
                pr = nextRecord();

                if (pr == null) {
                    break;
                }

                MatchResult mr = processRecord(pr);

                //Thread.currentThread().sleep(200);
                //System.out.println(pr.recordNumber + ": " + "UID " + uid 
                //+ " processed. " + eiServer.getDbTimestamp());
            } catch (ProcessingException e) {
                System.out.println("********* Failed to process record #: " 
                    + pr.recordNumber);
                System.out.println(e);
                e.printStackTrace();
                System.err.println("Continuing to process records...");
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
                haltThreads = true;
            }
        }

        synchronized (masterController) {
            threadCount--;
        }
    }

    private static MasterController getMasterController()
        throws java.rmi.RemoteException, javax.ejb.CreateException, 
            javax.naming.NamingException {
        Context jndiContext = null;
        
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
            "weblogic.jndi.WLInitialContextFactory");
        env.put(Context.PROVIDER_URL, server);
        jndiContext = new InitialContext(env);

        String jndiName = "ejb/MasterController";
        MasterController controller = (MasterController)jndiContext.lookup(jndiName);

        return controller;
    }

    static class PersonRecord {
        int recordNumber;
        IteratorRecord record;
    }
}
