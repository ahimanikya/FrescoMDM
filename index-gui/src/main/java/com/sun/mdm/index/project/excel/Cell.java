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
package com.sun.mdm.index.project.excel;

/**
 * Represents an individual Cell within a Sheet.  May be queried for its
 * type and its content
 *
 *
 */
public class Cell {

    /**
     * The row containing this number
     */
    private int row;
    /**
     * The column containing this number
     */
    private int column;
    /**
     * The type of this cell
     */
    private CellType type;
    /**
     * The value of this cell
     */
    private String value;
    /**
     * The row merge number (mergeDown) of this cell
     */
    private int rmerge;
    /**
     * The column merge  (mergeRight) number of this cell
     */
    private int cmerge;
    /**
     * The reference cell
     */
    private Cell refCell;
    private int refRow, refCol;

    /**
     * Constructor
     */
    public Cell(int row, int column, String value, int rmerge, int cmerge) {
        this.row = row;
        this.column = column;
        this.value = value;
        this.rmerge = rmerge;
        this.cmerge = cmerge;
        this.refCell = null;
    }

    /**
     * Constructor using a reference cell
     */
    public Cell(int row, int column, Cell refCell, int refRow, int refCol) {
        this.row = row;
        this.column = column;
        this.refCell = refCell;
        this.refRow = refRow;
        this.refCol = refCol;
    }

    /**
     * Returns the row number of this cell
     *
     * @return the row number of this cell
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column number of this cell
     *
     * @return the column number of this cell
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns the row merged number of this cell
     *
     * @return the row merged number of this cell
     */
    public int getRowMerged() {
        return rmerge;
    }

    /**
     * Returns the column merged number of this cell
     *
     * @return the column merged number of this cell
     */
    public int getColumnMerged() {
        return cmerge;
    }

    /**
     * Returns the row number of the merge cell
     *
     * @return the row number of the mrege cell
     */
    public int getRefRow() {
        return refRow;
    }

    /**
     * Returns the column number of the merge cell
     *
     * @return the column number of the merge cell
     */
    public int getRefColumn() {
        return refCol;
    }

    /**
     * Returns the content type of this cell
     *
     * @return the content type for this cell
     */
    public CellType getType() {
        return type;
    };

    /**
     * Indicates whether or not this cell is merged
     *
     * @return TRUE if this cell is merged, FALSE otherwise
     */
    public boolean isMerged() {
        return (refCell != null);
    }

    /**
     * Quick and dirty function to return the contents of this cell as a string.
     * For more complex manipulation of the contents, it is necessary to cast
     * this interface to correct subinterface
     *
     * @return the contents of this cell as a string
     */
    public String getContents() {
        return (refCell == null) ? value : refCell.getContents();
    }

    public Cell getRefCell() { return refCell; }
}
