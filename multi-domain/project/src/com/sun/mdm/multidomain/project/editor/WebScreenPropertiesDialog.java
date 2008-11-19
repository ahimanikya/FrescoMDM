/*
 * WebScreenPropertiesDialog.java
 *
 * Created on November 11, 2008, 2:20 PM
 */

package com.sun.mdm.multidomain.project.editor;

import com.sun.mdm.multidomain.parser.PageDefinition;
import com.sun.mdm.multidomain.parser.ScreenDefinition;
import com.sun.mdm.multidomain.parser.SearchDetail;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbBundle;

/**
 *
 * @author  wee
 */
public class WebScreenPropertiesDialog extends javax.swing.JDialog {
    private final String SCREEN_RELATIONSHIP = NbBundle.getMessage(WebScreenPropertiesDialog.class, "MSG_TAB_RELATIONSHIP");
    private final String SCREEN_HIERARCHY = NbBundle.getMessage(WebScreenPropertiesDialog.class, "MSG_TAB_HIERARCHY");
    private final String SCREEN_GROUP = NbBundle.getMessage(WebScreenPropertiesDialog.class, "MSG_TAB_GROUP");
    private final String SCREEN_CATEGORY = NbBundle.getMessage(WebScreenPropertiesDialog.class, "MSG_TAB_CATEGORY");

    private NumbericVerifier verifier = new NumbericVerifier();
    
    private PageDefinition mPageDefinition = null;
    
    ArrayList<ScreenRow> mScreenRows = null;
    
    private boolean bModified = false;
    
    
    /** Creates new form WebScreenPropertiesDialog */
    public WebScreenPropertiesDialog(PageDefinition pageDefinition) {
        super(org.openide.windows.WindowManager.getDefault().getMainWindow(), true);
        mPageDefinition = pageDefinition;
        initComponents();
        jSpinnerItemPerPage.setModel(new SpinnerNumberModel(1, 1, 300000, 1));
        jSpinnerMaxItems.setModel(new SpinnerNumberModel(1, 1, 300000, 1));
        jSpinnerItemPerPage.setInputVerifier(verifier);
        jSpinnerMaxItems.setInputVerifier(verifier);
        mScreenRows = new ArrayList<ScreenRow>();
        loadScreenDefinitions(mPageDefinition, mScreenRows);
        loadScreenList();
        loadScreenProperties();
        
        


        jCBScreenList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                loadScreenProperties();
            }
        });
        
        jTableSubscreen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = jTableSubscreen.getSelectedRow();
                TableModelSubscreen subscreenModel = (TableModelSubscreen) jTableSubscreen.getModel();
                ScreenRow screenDef = subscreenModel.getRow(selectedRow);
                if (screenDef != null) {
                    loadSubscreenProperties(screenDef);
                }
                jBtnUp.setEnabled(true);
                jBtnDown.setEnabled(true);
                if (selectedRow == 0 ) {
                    jBtnUp.setEnabled(false);
                } else if (selectedRow == jTableSubscreen.getRowCount() - 1) {
                    jBtnDown.setEnabled(false);
                }
                
               
             }
        });
        
        jSpinnerMaxItems.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                //evt.getComponent().getN
                updateSubscreenProperties(jSpinnerMaxItems);
            }
        });
        
        jSpinnerItemPerPage.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                updateSubscreenProperties(jSpinnerItemPerPage);
            }
        });
        
        
        jTxtSubscreenTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                //evt.getComponent();
                updateSubscreenProperties(evt.getComponent());
            }
        });
        
        jTextFieldScreenTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                //evt.getComponent();
                String selectedScreenType = (String) jCBScreenList.getSelectedItem();
                ScreenDefinition screenDef = mPageDefinition.getScreenDefintion(selectedScreenType);
                screenDef.setScreenTitle(jTextFieldScreenTitle.getText());

            }
        });
        
        jCheckBoxInitialScreen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBoxInitialScreen.setEnabled(false);
                        String selectedScreenType = (String) jCBScreenList.getSelectedItem();
                for (ScreenRow row : mScreenRows) {
                    if (row.getIdentifier().equalsIgnoreCase(selectedScreenType)) {
                        row.setInitialScreen(true);
                    } else {
                        row.setInitialScreen(false);
                    }
                    
                
         }

             }
        });

    }
    
    
    private void updateSubscreenProperties(Component comp) {
        TableModelSubscreen model = (TableModelSubscreen) jTableSubscreen.getModel();
        int selectedRow = jTableSubscreen.getSelectedRow();
        if (selectedRow >= 0) {
            ScreenRow screenDef = model.getRow(selectedRow);
            if (comp.equals(jSpinnerMaxItems)) {
                int maxItems = ((Integer) jSpinnerMaxItems.getValue()).intValue();
                screenDef.setMaxItems(maxItems);
            } else if (comp.equals(jSpinnerItemPerPage)) {
                screenDef.setItemPerPage(((Integer) jSpinnerItemPerPage.getValue()).intValue());
            } else if (comp.equals(jTxtSubscreenTitle)) {
                screenDef.setScreenTitle(jTxtSubscreenTitle.getText());
            }
        }
        
        
    }
    
    private void loadSubscreenProperties(ScreenRow subScreenDef) {
        jTxtSubscreenTitle.setText(subScreenDef.getScreenTitle());
        jSpinnerItemPerPage.setValue(subScreenDef.getItemPerPage());
        jSpinnerMaxItems.setValue(subScreenDef.getMaxItems());
        
    }

    private void loadScreenList() {
        jCBScreenList.addItem(SCREEN_RELATIONSHIP);
        jCBScreenList.addItem(SCREEN_HIERARCHY);
        jCBScreenList.addItem(SCREEN_GROUP);  
        jCBScreenList.addItem(SCREEN_CATEGORY);
        
        if (mPageDefinition.getScreenDefs().size() > 0 ) {
            String screenType = mScreenRows.get(0).getIdentifier();
            jCBScreenList.setSelectedItem(screenType);
        }
        
    }
    
    private void loadScreenProperties() {
        String selectedScreenType = (String) jCBScreenList.getSelectedItem();
        for (ScreenRow row : mScreenRows) {
            if (row.getIdentifier().equalsIgnoreCase(selectedScreenType)) {
                jCheckBoxInitialScreen.setSelected(row.isInitialScreen());
                if (jCheckBoxInitialScreen.isSelected()) {
                    jCheckBoxInitialScreen.setEnabled(false);
                } else {
                    jCheckBoxInitialScreen.setEnabled(true);
                }
                jTextFieldScreenTitle.setText(row.getScreenTitle());
                TableModelSubscreen subscreenModel = new TableModelSubscreen(row.getSubScreenRows());
                jTableSubscreen.setModel(subscreenModel);
                if (jTableSubscreen.getRowCount() > 0) {
                    jTableSubscreen.setRowSelectionInterval(0, 0);
                    loadSubscreenProperties(subscreenModel.getRow(0));
                    this.jBtnDown.setEnabled(true);
                }
                break;
            } 
                
         }
        
    }
    
    private void loadScreenDefinitions(PageDefinition pageDefinition, ArrayList<ScreenRow> screenRows) {
        for (ScreenDefinition screenDef : pageDefinition.getScreenDefs()) {
            ScreenRow screenRow = new ScreenRow();
            screenRow.setIdentifier(screenDef.getIdentifier());
            screenRow.setScreenId(screenDef.getScreenId());
            screenRow.setScreenTitle(screenDef.getScreenTitle());
            screenRow.setDisplayOrder(screenDef.getDisplayOrder());
            screenRow.setInitialScreen(pageDefinition.getInitialScreenId() == screenDef.getScreenId());
            screenRow.setViewPath(screenDef.getViewPath());
            if (screenDef.getItemPerPage() > 0) {
                screenRow.setItemPerPage(screenDef.getItemPerPage());
            }
            if (screenDef.getMaxItems() > 0) {
                screenRow.setMaxItems(screenDef.getMaxItems());
            }
            if (screenDef.getChildPage() != null) {
                ArrayList<ScreenRow> subScreenRows = new ArrayList<ScreenRow>();
                loadScreenDefinitions(screenDef.getChildPage(), subScreenRows);
                screenRow.setSubScreenRows(subScreenRows);
            }
            screenRows.add(screenRow);
        }

    }

    private void saveScreenDefinitions(PageDefinition pageDefinition, ArrayList<ScreenRow> screenRows) {        
        ArrayList<ScreenDefinition> sceenDefs = pageDefinition.getScreenDefs();
        for (ScreenRow screenRow : screenRows) {
            ScreenDefinition screenDef = new ScreenDefinition();
            screenDef.setIdentifier(screenRow.getIdentifier());
            screenDef.setScreenId(screenRow.getScreenId());
            screenDef.setScreenTitle(screenRow.getScreenTitle());
            screenDef.setDisplayOrder(screenRow.getDisplayOrder());
            screenDef.setViewPath(screenRow.getViewPath());
            if (screenRow.isInitialScreen()) {
                pageDefinition.setInitialScreenId(screenDef.getScreenId());
            }
            if (screenRow.getItemPerPage() > 0) {
                screenDef.setItemPerPage(screenRow.getItemPerPage());
            }
            if (screenRow.getMaxItems() > 0) {
                screenDef.setMaxItems(screenRow.getMaxItems());
            }
            if (screenRow.getSubScreenRows().size() > 0) {
                PageDefinition childPage = new PageDefinition();
                saveScreenDefinitions(childPage, screenRow.getSubScreenRows());
                screenDef.setChildPage(childPage);
            }
            sceenDefs.add(screenDef);
        }

    }
    
    public boolean isModified() {
        return bModified;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jCBScreenList = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableSubscreen = new javax.swing.JTable();
        jPanelSubScreen = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTxtSubscreenTitle = new javax.swing.JTextField();
        jLabelItemPerPage = new javax.swing.JLabel();
        jSpinnerItemPerPage = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jSpinnerMaxItems = new javax.swing.JSpinner();
        jBtnOK = new javax.swing.JButton();
        jBtnCancel = new javax.swing.JButton();
        jBtnUp = new javax.swing.JButton();
        jBtnDown = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldScreenTitle = new javax.swing.JTextField();
        jCheckBoxInitialScreen = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(WebScreenPropertiesDialog.class, "LBL_SCREEN_PROPERTIES")); // NOI18N
        setResizable(false);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(WebScreenPropertiesDialog.class, "LBL_SCREEN_NAME")); // NOI18N

        jCBScreenList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCBScreenList(evt);
            }
        });

        jTableSubscreen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null}
            },
            new String [] {
                "Subscreen"
            }
        ));
        jScrollPane1.setViewportView(jTableSubscreen);
        jTableSubscreen.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(WebScreenPropertiesDialog.class, "LBL_SUBSCREEN_NAME")); // NOI18N

        jPanelSubScreen.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(WebScreenPropertiesDialog.class, "LBL_SUBSCREEN_PROPERTIES"))); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(WebScreenPropertiesDialog.class, "LBL_SUBSCREEN_TITLE")); // NOI18N

        jLabelItemPerPage.setText(org.openide.util.NbBundle.getMessage(WebScreenPropertiesDialog.class, "LBL_ITEM_PER_PAGE")); // NOI18N

        jLabel3.setText(org.openide.util.NbBundle.getMessage(WebScreenPropertiesDialog.class, "LBL_MAX_ITEMS")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanelSubScreenLayout = new org.jdesktop.layout.GroupLayout(jPanelSubScreen);
        jPanelSubScreen.setLayout(jPanelSubScreenLayout);
        jPanelSubScreenLayout.setHorizontalGroup(
            jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelSubScreenLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelItemPerPage)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTxtSubscreenTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 244, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jSpinnerMaxItems)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jSpinnerItemPerPage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)))
                .add(125, 125, 125))
        );
        jPanelSubScreenLayout.setVerticalGroup(
            jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelSubScreenLayout.createSequentialGroup()
                .add(jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jTxtSubscreenTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelItemPerPage)
                    .add(jSpinnerItemPerPage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jSpinnerMaxItems, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jBtnOK.setText(org.openide.util.NbBundle.getMessage(WebScreenPropertiesDialog.class, "LBL_OK")); // NOI18N
        jBtnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onBtnOK(evt);
            }
        });

        jBtnCancel.setText(org.openide.util.NbBundle.getMessage(WebScreenPropertiesDialog.class, "LBL_Cancel")); // NOI18N
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onBtnCancel(evt);
            }
        });

        jBtnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sun/mdm/multidomain/project/resources/UpArrow.jpg"))); // NOI18N
        jBtnUp.setBorderPainted(false);
        jBtnUp.setEnabled(false);
        jBtnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onUpBtnPerformed(evt);
            }
        });

        jBtnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sun/mdm/multidomain/project/resources/DownArrow.JPG"))); // NOI18N
        jBtnDown.setEnabled(false);
        jBtnDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onDownBtnPerformed(evt);
            }
        });

        jLabel4.setText(org.openide.util.NbBundle.getMessage(WebScreenPropertiesDialog.class, "LBL_SCREEN_TITLE")); // NOI18N

        jCheckBoxInitialScreen.setText(org.openide.util.NbBundle.getMessage(WebScreenPropertiesDialog.class, "LBL_INITIAL_SCREEN")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                    .add(jPanelSubScreen, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 499, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jBtnUp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jBtnDown, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 116, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jTextFieldScreenTitle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                        .add(6, 6, 6))
                    .add(layout.createSequentialGroup()
                        .add(jCBScreenList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 216, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(27, 27, 27)
                        .add(jCheckBoxInitialScreen)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 32, Short.MAX_VALUE)))
                .addContainerGap(23, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .add(289, 289, 289)
                .add(jBtnOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jBtnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {jPanelSubScreen, jScrollPane1}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(19, 19, 19)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jCBScreenList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jCheckBoxInitialScreen))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(jTextFieldScreenTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(13, 13, 13)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(51, 51, 51)
                        .add(jBtnUp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(jBtnDown, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(41, 41, 41)
                        .add(jPanelSubScreen, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jBtnCancel)
                            .add(jBtnOK))
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 115, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(219, 219, 219))))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-563)/2, (screenSize.height-449)/2, 563, 449);
    }// </editor-fold>//GEN-END:initComponents

private void onCBScreenList(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCBScreenList
// TODO add your handling code here:
}//GEN-LAST:event_onCBScreenList

private void onBtnOK(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onBtnOK
// TODO add your handling code here:
    bModified = true;
    mPageDefinition.getScreenDefs().clear();
    mPageDefinition.setInitialScreenId(0);
    saveScreenDefinitions(mPageDefinition, mScreenRows);
    this.dispose();
}//GEN-LAST:event_onBtnOK

private void onBtnCancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onBtnCancel
// TODO add your handling code here:
    this.dispose();
}//GEN-LAST:event_onBtnCancel

private void onUpBtnPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onUpBtnPerformed
// TODO add your handling code here:
    TableModelSubscreen model = (TableModelSubscreen) jTableSubscreen.getModel();
    int iSelectedRow = jTableSubscreen.getSelectedRow();
    ScreenRow movedUpRow = model.getRow(iSelectedRow);
    int screenOrderUpRow = movedUpRow.getDisplayOrder();
    ScreenRow movedDownRow = model.getRow(iSelectedRow - 1);
    int screenOrderDownRow = movedDownRow.getDisplayOrder();
    movedDownRow.setDisplayOrder(screenOrderUpRow);
    movedUpRow.setDisplayOrder(screenOrderDownRow);
    model.removeRow(iSelectedRow);
    //if (iSelectedRow ==
    iSelectedRow--;
    model.addRow(iSelectedRow, movedUpRow);
    jTableSubscreen.setRowSelectionInterval(iSelectedRow, iSelectedRow);
    bModified = true;
    jBtnUp.setEnabled(true);
    jBtnDown.setEnabled(true);
    if (iSelectedRow == 0) {
        jBtnUp.setEnabled(false);
    }
}//GEN-LAST:event_onUpBtnPerformed

private void onDownBtnPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onDownBtnPerformed
// TODO add your handling code here:
    TableModelSubscreen model = (TableModelSubscreen) jTableSubscreen.getModel();
    int iSelectedRow = jTableSubscreen.getSelectedRow();
    ScreenRow movedRow = model.getRow(iSelectedRow);
    int screenOrderMovedRow = movedRow.getDisplayOrder();
    model.removeRow(iSelectedRow);
    ScreenRow previousRow = model.getRow(iSelectedRow);
    int screenOrderPrevRow = previousRow.getDisplayOrder();
    previousRow.setDisplayOrder(screenOrderMovedRow);
    iSelectedRow++;
    movedRow.setDisplayOrder(screenOrderPrevRow);
    model.addRow(iSelectedRow, movedRow);
    jTableSubscreen.setRowSelectionInterval(iSelectedRow, iSelectedRow);
    bModified = true;
    jBtnUp.setEnabled(true);
    jBtnDown.setEnabled(true);
    if (iSelectedRow == jTableSubscreen.getRowCount() - 1) {
        jBtnDown.setEnabled(false);
    }    
}//GEN-LAST:event_onDownBtnPerformed


    class NumbericVerifier extends InputVerifier {

        public boolean verify(JComponent input) {
            JTextField tf = (JTextField) input;
            String numStr = tf.getText();
            if (Integer.parseInt(numStr) > 0) {
                return true;
            }
            return false;
        }
    }
    
    class ScreenRow  {
        
        //private String screenName;
        
//      private int initialSubScreenId;
        private String identifier = null;
        private String screenTitle = null;
        private int screenId = -1;
        private int displayOrder = -1;
        private String viewPath = null;
        private int itemPerPage = -1;
        private int maxItems = -1;
        private boolean bInitialScreen = false;
        
        ArrayList<ScreenRow> subScreenRows = new ArrayList<ScreenRow>();

        public String getScreenTitle() {
            return screenTitle;
        }

        public void setScreenTitle(String screenName) {
            this.screenTitle = screenName;
        }

        public ArrayList<ScreenRow> getSubScreenRows() {
            return subScreenRows;
        }

        public void setSubScreenRows(ArrayList<ScreenRow> subScreenRows) {
            this.subScreenRows = subScreenRows;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public int getItemPerPage() {
            return itemPerPage;
        }

        public void setItemPerPage(int itemPerPage) {
            this.itemPerPage = itemPerPage;
        }

        public int getMaxItems() {
            return maxItems;
        }

        public void setMaxItems(int maxItems) {
            this.maxItems = maxItems;
        }

        public int getScreenId() {
            return screenId;
        }

        public void setScreenId(int screenId) {
            this.screenId = screenId;
        }

        public int getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(int displayOrder) {
            this.displayOrder = displayOrder;
        }

        public String getViewPath() {
            return viewPath;
        }

        public void setViewPath(String viewPath) {
            this.viewPath = viewPath;
        }

        public boolean isInitialScreen() {
            return bInitialScreen;
        }

        public void setInitialScreen(boolean initialScreen) {
            this.bInitialScreen = initialScreen;
        }
        
        
        
    }
     
    class TableModelSubscreen extends AbstractTableModel {

        private String columnNames[] = {NbBundle.getMessage(TabDomainSearch.class, "LBL_SUBSCREEN"),};
        ArrayList<ScreenRow> fieldRows;
        final static int iSubscreenName = 0;
        
        TableModelSubscreen(ArrayList rows) {
            fieldRows = rows;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (fieldRows != null) {
                return fieldRows.size();
            }
            return 0;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (fieldRows != null) {
                ScreenRow singleRow = (ScreenRow) fieldRows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iSubscreenName:
                            return singleRow.getIdentifier();
                        default:
                            return null;
                    }
                }
            }
            return null;
        }

        public ScreenRow getRow(int row) {
            if (fieldRows != null) {
                ScreenRow singleRow = (ScreenRow) fieldRows.get(row);
                return singleRow;
            }
            return null;
        }

        public Class getColumnClass(int c) {
            Object colNameObj = getValueAt(0, c);
            return colNameObj.getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            return false;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            switch (col) {
                case iSubscreenName:
                    ((ScreenRow) fieldRows.get(row)).setIdentifier((String) value);
                    break;

            }

            //fieldRows.set(row, value);
            fireTableCellUpdated(row, col);

        }

        public void removeRow(int index) {
            fieldRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }

        public void addRow(int index, ScreenRow row) {
            //fieldRows.add(row);
            fieldRows.add(index, row);
            this.fireTableRowsInserted(index, index);
        }

        /**
        public MatchRuleRowPerProbType getRow(int index) {
        //MatchRuleRowPerProbType row = (MatchRuleRowPerProbType) matchRuleRows.get(index);
        return row;
        }
         */
        public int findRowByFieldName(String fieldName) {
            for (int i = 0; i < fieldRows.size(); i++) {
                if (getValueAt(i, iSubscreenName).equals(fieldName)) {
                    return i;
                }
            }
            return -1;
        }

        public ScreenRow findRelTypeByFieldName(String fieldName) {
            for (int i = 0; i < fieldRows.size(); i++) {
                if (getValueAt(i, iSubscreenName).equals(fieldName)) {
                    return (ScreenRow) fieldRows.get(i);
                }
            }
            return null;
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnCancel;
    private javax.swing.JButton jBtnDown;
    private javax.swing.JButton jBtnOK;
    private javax.swing.JButton jBtnUp;
    private javax.swing.JComboBox jCBScreenList;
    private javax.swing.JCheckBox jCheckBoxInitialScreen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelItemPerPage;
    private javax.swing.JPanel jPanelSubScreen;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinnerItemPerPage;
    private javax.swing.JSpinner jSpinnerMaxItems;
    private javax.swing.JTable jTableSubscreen;
    private javax.swing.JTextField jTextFieldScreenTitle;
    private javax.swing.JTextField jTxtSubscreenTitle;
    // End of variables declaration//GEN-END:variables

}
