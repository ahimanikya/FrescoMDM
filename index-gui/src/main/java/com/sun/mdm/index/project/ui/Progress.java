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
package com.sun.mdm.index.project.ui;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Toolkit;
import java.awt.GridLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public class Progress {
    
    /**
     * progress bar to display
     */
    private static JProgressBar mProgressBar = null;
    
    /**
     * progress bar frame
     */
    private static JDialog mProgressBarDlg = null;
    
    /**
     * Title string to use for progress bar
     */
    private static String mTitle = "";
    
    /**
     * Flag for process' status
     */
    private static boolean mTaskDone = true;
    
    /**
     * Timer to use for progress bar
     */
    private static Timer mTimer;
    
    /**
     * timer to wait for
     */
    private final static int ONE_SECOND = 1000;
    
    /**
     * message to use for progress bar
     */
    private static JTextField mStatusLabel;
    
    /**
     * Constructor for the Util object
     */
    private Progress() { }
    
    /**
     * initialize progress bar
     */
    public static void initProgressBarDlg(String title, String labelBarString, boolean inDeterminate) {
        mTaskDone = false;
      
        mProgressBar = new JProgressBar();
        mProgressBar.setValue(0);
        mProgressBar.setIndeterminate(inDeterminate);

        mTitle = title;
        // Create and set up the content pane.
        JComponent pane = new JPanel();
        pane.setLayout(new GridLayout(2,1,0,0));
        pane.setOpaque(true);
       
        mStatusLabel = new JTextField(labelBarString);
        mStatusLabel.setBorder(null);
        mStatusLabel.setBackground(mProgressBar.getBackground());
        mStatusLabel.setEnabled(false);
        pane.add(mStatusLabel);
        pane.add(mProgressBar);

        JOptionPane optionPane = new JOptionPane(pane,
            JOptionPane.PLAIN_MESSAGE, JOptionPane.CLOSED_OPTION);
        mProgressBarDlg = optionPane.createDialog(new JFrame(), mTitle);
        mProgressBarDlg.setModal(true);

        //We call setStringPainted, even though we don't want the
        //string to show up until we switch to determinate mode,
        //so that the progress bar height stays the same whether
        //or not the string is shown.
        mProgressBar.setStringPainted(true); //get space for the string
        mProgressBar.setString("");          //but don't paint it

        Dimension size = new Dimension(400, 120);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mProgressBarDlg.setBounds(((screenSize.width - size.width) / 2),
          ((screenSize.height - size.height) / 2),
          size.width, size.height);

        // capture window close
        mProgressBarDlg.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
        mProgressBarDlg.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // need to stop here???
                mTaskDone = true;
         
                // stop timer
                mTimer.stop();

                // dismiss progress bar frame
                mProgressBarDlg.setVisible(false);
            }
        });

        // keep check on status of task
        doPolling();
    }

    
    public static void startProgress() {
        mTimer.start();
        mProgressBarDlg.setVisible(true);
    }
    
    public static void setTaskDone(boolean done) {
        mTaskDone = done;
    }
    
    /**
     * keep check on the task being performed
     */
    private static void doPolling() {
        // Create a timer and keep checking status of task
        mTimer = new Timer(ONE_SECOND, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                 if (mTaskDone) {
                    Toolkit.getDefaultToolkit().beep();
                    mTimer.stop();
                    mProgressBarDlg.dispose();
                }
            }
        });
    }
    
    public static void setStatus(String status) {
        mStatusLabel.setText(status);
    }
}
