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
package com.sun.mdm.index.project.ui.wizards;

import com.sun.mdm.index.project.ui.wizards.generator.ConfigGenerator;
import com.sun.mdm.index.project.ui.wizards.generator.MatchType;

import org.openide.util.NbBundle;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;

import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

public class PropertySheetModel {
    static final String DISPLAY_NAME = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_DisplayName");
    static final String BLOCKING = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_Blocking");
    static final String KEY_TYPE = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_KeyType");
    static final String DATA_TYPE = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_DataType");
    static final String MATCH_TYPE = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_MatchType");
    static final String UPDATEABLE = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_Updateable");
    static final String REQUIRED = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_Required");
    static final String PROPERTIES = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_Properties");
    static final String EDM = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_EDM");
    static final String DATASIZE = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_Size");
    static final String PATTERN = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_Pattern");
    static final String CODE_MODULE = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_CodeModule");
    static final String USER_CODE = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_UserCode");
    static final String CONSTRAINT_BY = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_ConstraintBy");
    static final String SEARCH_SCREEN = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_SearchScreen");
    static final String SEARCH_REQUIRED = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_SearchRequired");
    static final String SEARCH_RESULT = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_SearchResult");
    static final String GENERATE_REPORT = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_GenerateReport");
    static final String INPUT_MASK = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_InputMask");
    static final String VALUE_MASK = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_ValueMask");
    static final int EDITOR_DATATYPE = 0;
    static final int EDITOR_MATCHTYPE = 1;
    static final int EDITOR_BOOLEAN = 2;
    static final int EDITOR_EDMTEXTFIELDS = 3;
    static final int EDITOR_MISC = 4;
    
    JTable mTableEntityProperties = null;
    JTable mTableEDMProperties = null;
    JPanel mPanelEntityProperties = null;
    JPanel mPanelEDMProperties = null;
    JTabbedPane mPropertiesTabbedPane = null;
    
    String mDisplayName = null;
    String mDefaultDataType = "string";
    String mDefaultDataSize = "32";
    String mDefaultInputMask = "";
    String mDefaultValueMask = "";
    String mDefaultSearchable = "false";
    String mDefaultSearchRequired = "false";
    String mDefaultDisplayedInResult = "false";
    String mDefaultGenerateReport = "false";
    String mDefaultBlocking = "false";
    String mDefaultKeyType = "false";
    String mDefaultUpdateable = "true";
    String mDefaultRequired = "false";
    String mDefaultMatchType = "None";
    String mDefaultCodeModule = "";
    String mDefaultUserCode = "";
    String mDefaultConstraintBy = "";
    String mDefaultPattern = "";
    // Column
    final int iValueCol = 1;
    // Row: Entity properties
    final int iDataTypeRow = 0;
    final int iMatchTypeRow = 1;
    final int iBlockingRow = 2;
    final int iKeyTypeRow = 3;
    final int iUpdateableRow = 4;
    final int iRequiredRow = 5;
    final int iDataSizeRow = 6;
    final int iPatternRow = 7;
    final int iCodeModuleRow = 8;
    final int iUserCodeRow = 9;
    final int iConstraintByRow = 10;
    // Row: EDM properties
    final int iDisplayNameRow = 0;
    final int iInputMaskRow = 1;
    final int iValueMaskRow = 2;
    final int iSearchableRow = 3;
    final int iSearchRequiredRow = 4;
    final int iDisplayedResultRow = 5;
    final int iGenerateReportRow = 6;
    // Table Cell Editors
    DefaultCellEditor mDataTypeEditor;
    DefaultCellEditor mMatchTypeEditor;
    DefaultCellEditor mBooleanEditor;
    DefaultCellEditor mIntegerEditor;
    DefaultCellEditor mTextEditor;
    DefaultCellEditor mTextEditorPattern;
    DefaultCellEditor mTextEditorCodeModule;
    DefaultCellEditor mTextEditorUserCode;
    DefaultCellEditor mTextEditorConstraintBy;
    DefaultCellEditor mTextEditorDisplayName;
    DefaultCellEditor mTextEditorInputMask;
    DefaultCellEditor mTextEditorValueMask;
    DefaultCellEditor mSearchRequiredEditor;
    DefaultCellEditor mSearchableEditor;
    
    /** Creates a new instance of PropertySheetModel
     *
     *@param fieldName for default display name
     *@param defaultDataType for default data type
     *
     */
    public PropertySheetModel(String fieldName, String defaultDataType) {
        this.mDisplayName = fieldName; // use field name as default Display Name
        this.mDefaultDataType = defaultDataType; // best guess of Data Type
        getPropertySheet();
    }

    /** Creates a new instance of PropertySheetModel
     *
     *@param defaultDisplayName for default display name
     *@param defaultDataType for default data type
     *@param defaultInputMask for default Input Mask
     *@param defaultValueMask for default Value Mask
     *@param defaultSearchable for default simple search
     *@param defaultDisplayedInResult for displayed in search result
     *@param defaultGenerateReport for report
     *@param defaultKeyType for KeyType
     *@param defaultUpdateable for updateable
     *@param defaultRequired for required
     *@param defaultMatchType for MatchType
     *@param defaultDataSize for Size
     *@param defaultCodeModule for code module
     *@param defaultPattern for pattern
     *@param defaultBlocking for blocking
     *
     */
    public PropertySheetModel(String defaultDisplayName,
        String defaultDataType, String defaultInputMask,
        String defaultValueMask, String defaultSearchable,
        String defaultDisplayedInResult, String defaultKeyType,
        String defaultUpdateable, String defaultRequired,
        String defaultMatchType, String defaultDataSize,
        String defaultCodeModule, String defaultPattern, 
        String defaultBlocking, String defaultUserCode,
        String defaultConstraintBy, String defaultGenerateReport) {
        this.mDisplayName = defaultDisplayName; // use field name as default Display Name
        this.mDefaultDataType = defaultDataType; // best guess of Data Type
        this.mDefaultInputMask = defaultInputMask; // best guess of Input Mask
        this.mDefaultValueMask = defaultValueMask; // best guess of Value Mask
        this.mDefaultSearchable = defaultSearchable;
        this.mDefaultDisplayedInResult = defaultDisplayedInResult;
        this.mDefaultGenerateReport = defaultGenerateReport;
        this.mDefaultKeyType = defaultKeyType;
        this.mDefaultUpdateable = defaultUpdateable;
        this.mDefaultRequired = defaultRequired;
        this.mDefaultMatchType = defaultMatchType;
        this.mDefaultDataSize = defaultDataSize;
        this.mDefaultCodeModule = defaultCodeModule;
        this.mDefaultUserCode = defaultUserCode;
        this.mDefaultConstraintBy = defaultConstraintBy;
        this.mDefaultPattern = defaultPattern;
        this.mDefaultBlocking = defaultBlocking;
        getPropertySheet();
    }

    // I tried to use netBeans BeanNode with propertySheet built in, but...
    // Creating my own property sheet, not the best solution
    // but it is less netBeans hassels
    //
    // All the properties will get put in to the panel "mPanelEntityProperties"
    // that can be inserted in DefineEntity wizard panel

    /**
     *@return JTabbedPane that contains field's property sheet
     */
    public JTabbedPane getPropertySheet() {
        if (mPropertiesTabbedPane == null) {
            createCellEditors();            

            // TableModelObjectProperties
            if (mTableEntityProperties == null) {
                TableModelObjectProperties model = new TableModelObjectProperties();
                mTableEntityProperties = new JTable(model) {
                    @Override
                    public TableCellEditor getCellEditor(int row, int column) {
                        if (column == iValueCol) {
                            switch (row) {
                                case iDataTypeRow:
                                    return mDataTypeEditor;
                                case iMatchTypeRow:
                                    return mMatchTypeEditor;
                                case iBlockingRow:
                                case iKeyTypeRow:
                                case iUpdateableRow:
                                case iRequiredRow:
                                    return mBooleanEditor;
                                case iDataSizeRow:
                                    return mIntegerEditor;
                                case iPatternRow:
                                    return mTextEditorPattern;
                                case iCodeModuleRow:
                                    return mTextEditorCodeModule;
                                case iUserCodeRow:
                                    return mTextEditorUserCode;
                                case iConstraintByRow:
                                    return mTextEditorConstraintBy;
                                default:
                                    break;
                            }
                        }
                        return this.getCellEditor(row, column);
                    }
                };
                //mTableEntityProperties.setBackground(Color.LIGHT_GRAY);
                mTableEntityProperties.setRowHeight(22);
                //mTableEntityProperties.setSelectionBackground(Color.LIGHT_GRAY);
            }

            mPanelEntityProperties = new JPanel();
            mPanelEntityProperties.setLayout(new BoxLayout(mPanelEntityProperties,
                    BoxLayout.Y_AXIS));
            mPanelEntityProperties.add(mTableEntityProperties);
            mTableEntityProperties.addFocusListener(new FocusListener() {
                    public void focusGained(FocusEvent ev) {
                    }

                    public void focusLost(FocusEvent ev) {
                        mTableEntityProperties.editingStopped(new ChangeEvent(this));
                    }
                });
                
            // TableModelEDMProperties
            if (mTableEDMProperties == null) {
                TableModelEDMProperties model = new TableModelEDMProperties();
                mTableEDMProperties = new JTable(model) {
                    @Override
                    public TableCellEditor getCellEditor(int row, int column) {
                        if (column == iValueCol) {
                            switch (row) {
                                case iDisplayNameRow:
                                    return mTextEditorDisplayName;
                                case iInputMaskRow:
                                    return mTextEditorInputMask;
                                case iValueMaskRow:
                                    return mTextEditorValueMask;
                                case iSearchRequiredRow:
                                    return mSearchRequiredEditor;
                                case iSearchableRow:
                                    return mSearchableEditor;
                                case iDisplayedResultRow:
                                case iGenerateReportRow:
                                    return mBooleanEditor;
                                default:
                                    break;
                            }
                        }
                        return this.getCellEditor(row, column);
                    }
                };
                //mTableEDMProperties.setBackground(Color.LIGHT_GRAY);
                mTableEDMProperties.setRowHeight(22);
                //mTableEDMProperties.setSelectionBackground(Color.LIGHT_GRAY);
            }

            mPanelEDMProperties = new JPanel();
            mPanelEDMProperties.setLayout(new BoxLayout(mPanelEDMProperties,
                    BoxLayout.Y_AXIS));
            mPanelEDMProperties.add(mTableEDMProperties);
            mTableEDMProperties.addFocusListener(new FocusListener() {
                    public void focusGained(FocusEvent ev) {
                    }

                    public void focusLost(FocusEvent ev) {
                        mTableEDMProperties.editingStopped(new ChangeEvent(
                                this));
                    }
                });

            

            mPropertiesTabbedPane = new JTabbedPane();
            mPropertiesTabbedPane.addTab(PROPERTIES, mPanelEntityProperties);
            mPropertiesTabbedPane.addTab(EDM, mPanelEDMProperties);
        }

        return mPropertiesTabbedPane;
    }

    private void createCellEditors() {
        //Set up the editor for the integer cells.
        final WholeNumberField integerField = new WholeNumberField(0, 5);
        integerField.setHorizontalAlignment(WholeNumberField.LEFT);
        integerField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent ev) {
                integerField.selectAll();
            }

            public void focusLost(FocusEvent ev) {
            }
        });

        mIntegerEditor = 
            new DefaultCellEditor(integerField) {
                //Override DefaultCellEditor's getCellEditorValue method
                //to return an Integer, not a String:
                @Override
                public Object getCellEditorValue() {
                    return new Integer(integerField.getValue());
                }
                @Override
                public boolean isCellEditable(EventObject event) {
                    boolean bEditable = false;
                    if (event instanceof MouseEvent) {
                        bEditable = true;
                    }
                    return bEditable;
                }
            };
            
        mTextEditorPattern = new ObjectTableCellEditor(new JTextField());
        mTextEditorCodeModule = new ObjectTableCellEditor(new JTextField());
        mTextEditorUserCode = new ObjectTableCellEditor(new JTextField());
        mTextEditorConstraintBy = new ObjectTableCellEditor(new JTextField());
        mTextEditorDisplayName = new ObjectTableCellEditor(new JTextField());
        mTextEditorInputMask = new ObjectTableCellEditor(new JTextField());
        mTextEditorValueMask = new ObjectTableCellEditor(new JTextField());
        //mTextEditor.addCellEditorListener(new TextCellEditorListener());
            
        final JComboBox booleanComboBox = new JComboBox();
        booleanComboBox.addItem("true");
        booleanComboBox.addItem("false");
        mBooleanEditor = 
            new DefaultCellEditor(booleanComboBox) {
                @Override
                public Object getCellEditorValue() {
                    return booleanComboBox.getSelectedItem().toString();
                }
            };
            
        final JComboBox datatypeComboBox = new JComboBox();
        datatypeComboBox.addItem("string");
        datatypeComboBox.addItem("char");
        datatypeComboBox.addItem("date");
        //datatypeComboBox.addItem("timestamp");
        datatypeComboBox.addItem("int");
        //datatypeComboBox.addItem("long");
        datatypeComboBox.addItem("float");
        datatypeComboBox.addItem("boolean");
        mDataTypeEditor = 
            new DefaultCellEditor(datatypeComboBox) {
                @Override
                public Object getCellEditorValue() {
                    return datatypeComboBox.getSelectedItem().toString();
                }
           };
            
        mDataTypeEditor.addCellEditorListener(new CellEditorListener() {
            public void editingStopped(ChangeEvent e) {
                String value = (String) mDataTypeEditor.getCellEditorValue();
                if (value.equals("char") || value.equals("boolean")) {
                	mTableEntityProperties.setValueAt("1", iDataSizeRow, iValueCol);
                	integerField.setEnabled(false);
                } else {
                	mTableEntityProperties.setValueAt(mDefaultDataSize, iDataSizeRow, iValueCol);
                	integerField.setEnabled(true);                	
                }
            }

            public void editingCanceled(ChangeEvent e) {
            }        	
        });
        
        final JComboBox matchtypeComboBox = new JComboBox();
        matchtypeComboBox.addItem("None");
        MatchType[] mMatchTypes;
        mMatchTypes = ConfigGenerator.getMatchTypes(DefineDeploymentPanel.matchEngine);

        for (int i = 0; i < mMatchTypes.length; i++) {
            matchtypeComboBox.addItem(mMatchTypes[i].getMatchTypeID());
        }
        mMatchTypeEditor = 
            new DefaultCellEditor(matchtypeComboBox) {
                @Override
                public Object getCellEditorValue() {
                    return matchtypeComboBox.getSelectedItem().toString();
                }
            };

        final JComboBox searchRequiredComboBox = new JComboBox();
        searchRequiredComboBox.addItem("true");
        searchRequiredComboBox.addItem("false");
        searchRequiredComboBox.addItem("oneof");
        mSearchRequiredEditor = 
            new DefaultCellEditor(searchRequiredComboBox) {
                public Object getCellEditorValue() {
                    return searchRequiredComboBox.getSelectedItem().toString();
                }
            };
        searchRequiredComboBox.setEnabled(this.mDefaultSearchable.equals("true"));
            
        final JComboBox searchableComboBox = new JComboBox();
        searchableComboBox.addItem("true");
        searchableComboBox.addItem("false");
        mSearchableEditor = 
            new DefaultCellEditor(searchableComboBox) {
                @Override
                public Object getCellEditorValue() {
                    return searchableComboBox.getSelectedItem().toString();
                }
            };
 
        mSearchableEditor.addCellEditorListener(new CellEditorListener() {
                public void editingStopped(ChangeEvent e) {
                    String value = (String) mSearchableEditor.getCellEditorValue();
                    if (value.equals("false")) {
                        mTableEDMProperties.setValueAt("false", iSearchRequiredRow, iValueCol);
                        searchRequiredComboBox.setEnabled(false);
                    } else if (value.equals("true")) {
                        searchRequiredComboBox.setEnabled(true);
                    }
                }

                public void editingCanceled(ChangeEvent e) {
                }
            });



    }
        

    /**
     *@return DataType
     */
    public String getDataType() {
        String dataType = mTableEntityProperties.getValueAt(iDataTypeRow, iValueCol).toString();
        return dataType;
    }

    /**
     *@return MatchType
     */
    public String getMatchType() {
        return mTableEntityProperties.getValueAt(iMatchTypeRow, iValueCol)
                                       .toString();
    }

    /**
     *@return Blocking
     */
    public String getBlocking() {
        return mTableEntityProperties.getValueAt(iBlockingRow, iValueCol).toString();
    }

    /**
     *@return DataSize
     */
    public String getDataSize() {
        return mTableEntityProperties.getValueAt(iDataSizeRow, iValueCol).toString();
    }

    /**
     *@return Pattern
     */
    public String getPattern() {
        return mTableEntityProperties.getValueAt(iPatternRow, iValueCol).toString();
    }

    /**
     *@return Code Module
     */
    public String getCodeModule() {
        return mTableEntityProperties.getValueAt(iCodeModuleRow, iValueCol)
                                  .toString();
    }

    /**
     *@return User Code
     */
    public String getUserCode() {
        return mTableEntityProperties.getValueAt(iUserCodeRow, iValueCol)
                                  .toString();
    }

    /**
     *@return Constraint By
     */
    public String getConstraintBy() {
        return mTableEntityProperties.getValueAt(iConstraintByRow, iValueCol)
                                  .toString();
    }

    /**
     *@return KeyType
     */
    public String getKeyType() {
        return mTableEntityProperties.getValueAt(iKeyTypeRow, iValueCol)
                                     .toString();
    }

    /**
     *@return Required
     */
    public String getRequired() {
        return mTableEntityProperties.getValueAt(iRequiredRow, iValueCol)
                                     .toString();
    }

    /**
     *@return Updateable
     */
    public String getUpdateable() {
        return mTableEntityProperties.getValueAt(iUpdateableRow, iValueCol)
                                     .toString();
    }

    /**
     *@return DisplayName
     */
    public String getDisplayName() {
        return mTableEDMProperties.getValueAt(iDisplayNameRow,
            iValueCol).toString();
    }

    /**
     *@return InputMask
     */
    public String getInputMask() {
        return mTableEDMProperties.getValueAt(iInputMaskRow, iValueCol)
                                           .toString();
    }

    /**
     *@return ValueMask
     */
    public String getValueMask() {
        return mTableEDMProperties.getValueAt(iValueMaskRow, iValueCol)
                                           .toString();
    }

    /**
     *@return UsedInSearchScreen
     */
    public boolean getUsedInSearchScreen() {
        return (mTableEDMProperties.getValueAt(iSearchableRow, iValueCol)
                                         .toString().equals("true"));
    }
    
    
    /**
     *@return SearchRequired
     */
    public String getSearchRequired() {
        return (mTableEDMProperties.getValueAt(iSearchRequiredRow, iValueCol)
                                         .toString());
    }

    /**
     *@return DisplayedInSearchResult
     */
    public boolean getDisplayedInSearchResult() {
        return (mTableEDMProperties.getValueAt(iDisplayedResultRow,
            iValueCol).toString().equals("true"));
    }

    /**
     *@return GenerateReport
     */
    public boolean getGenerateReport() {
        return (mTableEDMProperties.getValueAt(iGenerateReportRow,
            iValueCol).toString().equals("true"));
    }

    // Table model for EDM properties
    class TableModelEDMProperties extends AbstractTableModel {
        final String[] columnNames = { null, null };
        final Object[][] data = {
            { DISPLAY_NAME, mDisplayName },
            { INPUT_MASK, mDefaultInputMask },
            { VALUE_MASK, mDefaultValueMask },
            { SEARCH_SCREEN, mDefaultSearchable },
            { SEARCH_REQUIRED, mDefaultSearchRequired },
            { SEARCH_RESULT, mDefaultDisplayedInResult },
            { GENERATE_REPORT, mDefaultGenerateReport }
        };

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        @Override
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col == 0) {
                return false;
            }

            return true;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (data[0][col] instanceof Integer && !(value instanceof Integer)) {
                //With JFC/Swing 1.1 and JDK 1.2, we need to create    
                //an Integer from the value; otherwise, the column     
                //switches to contain Strings.  Starting with v 1.3,   
                //the table automatically converts value to an Integer,
                //so you only need the code in the 'else' part of this 
                //'if' block.                                          
                //XXX: See TableEditDemo.java for a better solution!!!
                data[row][col] = new Integer(value.toString());
                fireTableCellUpdated(row, col);
            } else {
                data[row][col] = value;
                fireTableCellUpdated(row, col);
            }
        }
    }

    // Table model for Object properties
    class TableModelObjectProperties extends AbstractTableModel {
        final String[] columnNames = { null, null };
        final Object[][] data = {
            { DATA_TYPE, mDefaultDataType },
            { MATCH_TYPE, mDefaultMatchType },
            { BLOCKING, mDefaultBlocking },
            { KEY_TYPE, mDefaultKeyType },
            { UPDATEABLE, mDefaultUpdateable },
            { REQUIRED, mDefaultRequired },
            { DATASIZE, mDefaultDataSize }, // DataSize
            { PATTERN, mDefaultPattern },
            { CODE_MODULE, mDefaultCodeModule },
            { USER_CODE, mDefaultUserCode },
            { CONSTRAINT_BY, mDefaultConstraintBy }
        };
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col == 0) {
                return false;
            }

            return true;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (data[0][col] instanceof Integer && !(value instanceof Integer)) {
                //With JFC/Swing 1.1 and JDK 1.2, we need to create    
                //an Integer from the value; otherwise, the column     
                //switches to contain Strings.  Starting with v 1.3,   
                //the table automatically converts value to an Integer,
                //so you only need the code in the 'else' part of this 
                //'if' block.                                          
                //XXX: See TableEditDemo.java for a better solution!!!
                data[row][col] = new Integer(value.toString());
                fireTableCellUpdated(row, col);
            } else {
                data[row][col] = value;
                fireTableCellUpdated(row, col);
            }
        }
    }
    
    // until we know what are the valid character sets
    private class TextCellEditorListener
             implements CellEditorListener {

        /**
         *  This method ...
         *
         * @param  e  This ...
         */
        public void editingCanceled(ChangeEvent e) {
        }
        
        /** This tells the listeners the editor has canceled editing  */
        public void editingStopped(ChangeEvent e) {
            String value = (String) mTextEditor.getCellEditorValue();
            for (int i = 0; i < value.length(); i++)
            {
                char c = value.charAt(i);
                if (!Character.isLetterOrDigit(c) && c != '-' && c != '_')
                {
                    Toolkit.getDefaultToolkit().beep();
                    String msg = "Invalid character - '" + c + "' !";
                    NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
                    DialogDisplayer.getDefault().notify(desc);
                    break;
                }
            }
        }
        
    }
    
    private class ObjectTableCellEditor extends DefaultCellEditor {
        JTextField mText;
        /**
         * Constructor for ObjectTableCellEditor.
         *
         * @param textfield default cell editor
         */
        public ObjectTableCellEditor(JTextField textfield) {
            super(textfield);
            mText = textfield;
            mText.addFocusListener(new FocusListener() {
                    public void focusGained(FocusEvent ev) {
                        mText.selectAll();
                    }

                    public void focusLost(FocusEvent ev) {
                    }
                });
        }
        
        public Object getCellEditorValue() {
            return mText.getText();
        }
        
        public boolean isCellEditable(EventObject event) {
            boolean bEditable = false;
            if (event instanceof MouseEvent) {
                bEditable = true;
            }
            return bEditable;
        }
    }
    
}
