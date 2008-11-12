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
    
    private boolean bModified = false;
    
    /** Creates new form WebScreenPropertiesDialog */
    public WebScreenPropertiesDialog(PageDefinition pageDefinition) {
        super(org.openide.windows.WindowManager.getDefault().getMainWindow(), true);
        mPageDefinition = pageDefinition;
        initComponents();
        loadScreenList();
        jSpinnerItemPerPage.setModel(new SpinnerNumberModel(1, 1, 300000, 1));
        jSpinnerMaxItems.setModel(new SpinnerNumberModel(1, 1, 300000, 1));
        jSpinnerItemPerPage.setInputVerifier(verifier);
        jSpinnerMaxItems.setInputVerifier(verifier);
        
        
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
                ScreenDefinition screenDef = subscreenModel.getRow(selectedRow);
                if (screenDef != null) {
                    loadSubscreenProperties(screenDef);
                }
             }
        });
        
        jSpinnerItemPerPage.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                //evt.getComponent().getN
                updateSubscreenProperties(jSpinnerItemPerPage);
            }
        });
        
        jSpinnerItemPerPage.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                updateSubscreenProperties(jSpinnerItemPerPage);
            }
        });
        
        
        jTxtScreenTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                //evt.getComponent();
                updateSubscreenProperties(evt.getComponent());
            }
        });

    }
    
    private void updateSubscreenProperties(Component comp) {
        TableModelSubscreen model = (TableModelSubscreen) jTableSubscreen.getModel();
        int selectedRow = jTableSubscreen.getSelectedRow();
        ScreenDefinition screenDef = model.getRow(selectedRow);
        if (comp.equals(jSpinnerMaxItems)) {            
            int maxItems = ((Integer) jSpinnerMaxItems.getValue()).intValue();
            screenDef.setMaxItems(maxItems);            
        } else if (comp.equals(jSpinnerItemPerPage)) {
            screenDef.setItemPerPage(((Integer) jSpinnerItemPerPage.getValue()).intValue());
        } else if (comp.equals(jTxtScreenTitle)) {
            screenDef.setScreenTitle(jTxtScreenTitle.getText());
        }
        
        
    }
    
    private void loadSubscreenProperties(ScreenDefinition subScreenDef) {
        jTxtScreenTitle.setText(subScreenDef.getScreenTitle());
        jSpinnerItemPerPage.setValue(subScreenDef.getItemPerPage());
        jSpinnerMaxItems.setValue(subScreenDef.getMaxItems());
        
    }

    private void loadScreenList() {
        jCBScreenList.addItem(SCREEN_RELATIONSHIP);
        jCBScreenList.addItem(SCREEN_HIERARCHY);
        jCBScreenList.addItem(SCREEN_GROUP);  
        jCBScreenList.addItem(SCREEN_CATEGORY);
        
        String screenType = mPageDefinition.getScreenDefs().get(0).getIdentifier();
        jCBScreenList.setSelectedItem(screenType);     
        
    }
    
    private void loadScreenProperties() {
        String selectedScreenType = (String) jCBScreenList.getSelectedItem();
        ScreenDefinition screenDef = mPageDefinition.getScreenDefintion(selectedScreenType);
        if (screenDef != null) {
            TableModelSubscreen subscreenModel = new TableModelSubscreen(screenDef.getChildPage().getScreenDefs());
            jTableSubscreen.setModel(subscreenModel);   
            if (jTableSubscreen.getRowCount() > 0) {
                jTableSubscreen.setRowSelectionInterval(0, 0);
                loadSubscreenProperties(subscreenModel.getRow(0));
            }
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
        jTxtScreenTitle = new javax.swing.JTextField();
        jLabelItemPerPage = new javax.swing.JLabel();
        jSpinnerItemPerPage = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jSpinnerMaxItems = new javax.swing.JSpinner();
        jBtnOK = new javax.swing.JButton();
        jBtnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(WebScreenPropertiesDialog.class, "LBL_SCREEN_PROPERTIES")); // NOI18N
        setResizable(false);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(WebScreenPropertiesDialog.class, "LBL_INITIAL_SCREEN")); // NOI18N

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

        jTxtScreenTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onjTxtScreenTitle(evt);
            }
        });

        jLabelItemPerPage.setText(org.openide.util.NbBundle.getMessage(WebScreenPropertiesDialog.class, "LBL_ITEM_PER_PAGE")); // NOI18N

        jLabel3.setText(org.openide.util.NbBundle.getMessage(WebScreenPropertiesDialog.class, "LBL_MAX_ITEMS")); // NOI18N

        jSpinnerMaxItems.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                onMaxItems(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanelSubScreenLayout = new org.jdesktop.layout.GroupLayout(jPanelSubScreen);
        jPanelSubScreen.setLayout(jPanelSubScreenLayout);
        jPanelSubScreenLayout.setHorizontalGroup(
            jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelSubScreenLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelItemPerPage)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTxtScreenTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 244, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jSpinnerItemPerPage)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jSpinnerMaxItems, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)))
                .addContainerGap(94, Short.MAX_VALUE))
        );
        jPanelSubScreenLayout.setVerticalGroup(
            jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelSubScreenLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jTxtScreenTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelItemPerPage)
                    .add(jSpinnerItemPerPage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(31, 31, 31)
                .add(jPanelSubScreenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jSpinnerMaxItems, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(34, 34, 34)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 116, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jCBScreenList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 216, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(layout.createSequentialGroup()
                            .add(jBtnOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(18, 18, 18)
                            .add(jBtnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(jPanelSubScreen, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(25, 25, 25)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jCBScreenList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(27, 27, 27)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 115, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jPanelSubScreen, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnCancel)
                    .add(jBtnOK))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-520)/2, (screenSize.height-479)/2, 520, 479);
    }// </editor-fold>//GEN-END:initComponents

private void onCBScreenList(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCBScreenList
// TODO add your handling code here:
}//GEN-LAST:event_onCBScreenList

private void onBtnOK(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onBtnOK
// TODO add your handling code here:
    bModified = true;
    this.dispose();
}//GEN-LAST:event_onBtnOK

private void onBtnCancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onBtnCancel
// TODO add your handling code here:
    this.dispose();
}//GEN-LAST:event_onBtnCancel

private void onjTxtScreenTitle(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onjTxtScreenTitle
// TODO add your handling code here:
}//GEN-LAST:event_onjTxtScreenTitle

private void onMaxItems(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_onMaxItems
// TODO add your handling code here:
}//GEN-LAST:event_onMaxItems


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
    
    class TableModelSubscreen extends AbstractTableModel {

        private String columnNames[] = {NbBundle.getMessage(TabDomainSearch.class, "LBL_SUBSCREEN"),};
        ArrayList fieldRows;
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
                ScreenDefinition singleRow = (ScreenDefinition) fieldRows.get(row);
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

        public ScreenDefinition getRow(int row) {
            if (fieldRows != null) {
                ScreenDefinition singleRow = (ScreenDefinition) fieldRows.get(row);
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
                    ((ScreenDefinition) fieldRows.get(row)).setIdentifier((String) value);
                    break;

            }

            //fieldRows.set(row, value);
            fireTableCellUpdated(row, col);

        }

        public void removeRow(int index) {
            fieldRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }

        public void addRow(int index, SearchDetail row) {
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

        public SearchDetail findRelTypeByFieldName(String fieldName) {
            for (int i = 0; i < fieldRows.size(); i++) {
                if (getValueAt(i, iSubscreenName).equals(fieldName)) {
                    return (SearchDetail) fieldRows.get(i);
                }
            }
            return null;
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnCancel;
    private javax.swing.JButton jBtnOK;
    private javax.swing.JComboBox jCBScreenList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelItemPerPage;
    private javax.swing.JPanel jPanelSubScreen;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinnerItemPerPage;
    private javax.swing.JSpinner jSpinnerMaxItems;
    private javax.swing.JTable jTableSubscreen;
    private javax.swing.JTextField jTxtScreenTitle;
    // End of variables declaration//GEN-END:variables

}
