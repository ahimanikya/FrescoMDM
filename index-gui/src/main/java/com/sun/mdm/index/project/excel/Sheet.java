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

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;
import java.util.LinkedList;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Represents a sheet within a workbook.  Provides a handle to the individual
 * cells, or lines of cells (grouped by Row or Column)
 *
 *
 */
public class Sheet {

    /**
     * Excel XML element and attribute tags
     */
    private static final String TAG_WORKBOOK = "Workbook";
    private static final String TAG_WORKSHEET = "Worksheet";
    private static final String TAG_TABLE = "Table";
    private static final String TAG_COLUMN = "Column";
    private static final String TAG_ROW = "Row";
    private static final String TAG_CELL = "Cell";
    private static final String TAG_DATA = "Data";
    private static final String TAG_COLUMNCOUNT = "ss:ExpandedColumnCount";
    private static final String TAG_ROWCOUNT = "ss:ExpandedRowCount";
    private static final String TAG_MERGEDOWN = "ss:MergeDown";
    private static final String TAG_INDEX = "ss:Index";
    private static final String TAG_WIDTH = "ss:Width";
    private static final String TAG_TYPE = "ss:Type";

    /**
     * The excel file
     */
    private String excelFile;

    /**
     * The name of this sheet
     */
    private String name;

    /**
     * The  number of rows
     */
    private int numRows;

    /**
     * The number of columns
     */
    private int numCols;

    /**
     * The cells
     */
    private Cell[][] cells;

    /**
     * Column data types
     */
    private String[] mColumnTypes;

    /**
     * Column data names
     */
    private String[] mColumnNames;

    /**
     * Indicates whether the columnInfos array has been initialized
     */
    private boolean columnInfosInitialized;

    /**
     * Indicates whether the rowRecords array has been initialized
     */
    private boolean rowRecordsInitialized;


    /**
     * The hidden flag
     */
    private boolean hidden;

    /**
     * Constructor
     *
     * @param f the excel file
     * @exception Exception
     */
    public Sheet(String f) throws Exception {
        excelFile = f;
        File xf = new File(excelFile);
        if (xf.exists()) {
            readWorkSheet();  // load excel data
        }
    }

    /**
     * Returns the cell specified at this row and at this column
     *
     * @param row the row number
     * @param column the column number
     * @return the cell at the specified co-ordinates
     */
    public Cell getCell(int column, int row)
    {
      // just in case this has been cleared, but something else holds
      // a reference to it
      if (cells == null)
      {
        readWorkSheet();
      }

      Cell c = cells[row][column];

      if (c == null)
      {
        c = null; // new EmptyCell(column, row);
        cells[row][column] = c;
      }

      return c;
    }

    /**
     * Gets the cell whose contents match the string passed in.
     * If no match is found, then null is returned.  The search is performed
     * on a row by row basis, so the lower the row number, the more
     * efficiently the algorithm will perform
     *
     * @param  contents the string to match
     * @return the Cell whose contents match the paramter, null if not found
     */
    public Cell findCell(String contents)
    {
      Cell cell = null;
      boolean found = false;

      for (int i = 0; i < getRows() && !found; i++)
      {
        Cell[] row = getRow(i);
        for (int j = 0; j < row.length && !found; j++)
        {
          if (row[j].getContents().equals(contents))
          {
            cell = row[j];
            found = true;
          }
        }
      }

      return cell;
    }

    /**
     * Returns the number of rows in this sheet
     *
     * @return the number of rows in this sheet
     */
    public int getRows()
    {
      // just in case this has been cleared, but something else holds
      // a reference to it
      if (cells == null)
      {
        readWorkSheet();
      }

      return numRows;
    }

    /**
     * Returns the number of columns in this sheet
     *
     * @return the number of columns in this sheet
     */
    public int getColumns()
    {
      // just in case this has been cleared, but something else holds
      // a reference to it
      if (cells == null)
      {
        readWorkSheet();
      }

      return numCols;
    }

    /**
     * Gets all the cells on the specified row.  The returned array will
     * be stripped of all trailing empty cells
     *
     * @param row the rows whose cells are to be returned
     * @return the cells on the given row
     */
    public Cell[] getRow(int row)
    {
      // just in case this has been cleared, but something else holds
      // a reference to it
      if (cells == null)
      {
        readWorkSheet();
      }

      // Find the last non-null cell
      boolean found = false;
      int col = numCols - 1;
      while (col >= 0 && !found)
      {
        if (cells[row][col] != null)
        {
          found = true;
        }
        else
        {
          col--;
        }
      }

      // Only create entries for non-null cells
      Cell[] c = new Cell[col + 1];

      for (int i = 0; i <= col; i++)
      {
        c[i] = getCell(i, row);
      }
      return c;
    }

    /**
     * Gets all the cells on the specified column.  The returned array
     * will be stripped of all trailing empty cells
     *
     * @param col the column whose cells are to be returned
     * @return the cells on the specified column
     */
    public Cell[] getColumn(int col)
    {
      // just in case this has been cleared, but something else holds
      // a reference to it
      if (cells == null)
      {
        readWorkSheet();
      }

      // Find the last non-null cell
      boolean found = false;
      int row = numRows - 1;
      while (row >= 0 && !found)
      {
        if (cells[row][col] != null)
        {
          found = true;
        }
        else
        {
          row--;
        }
      }

      // Only create entries for non-null cells
      Cell[] c = new Cell[row + 1];

      for (int i = 0; i <= row; i++)
      {
        c[i] = getCell(col, i);
      }
      return c;
    }

    /**
     * Determines whether the sheet is hidden
     *
     * @return whether or not the sheet is hidden
     * @deprecated in favour of the getSettings function
     */
    public boolean isHidden()
    {
      return hidden;
    }

    /**
     * Reads in the contents of this sheet
     */
    private void readWorkSheet() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);

            Document doc = factory.newDocumentBuilder().parse(new File(excelFile));

            NodeList tables = doc.getElementsByTagName(TAG_TABLE);
            int cn = 1, rn = 1;
            for (int i = 0, isize = tables.getLength(); i < isize; i++) {
                Element dataElement = (Element) tables.item(i);
                String cc = dataElement.getAttribute(TAG_COLUMNCOUNT);
                String rc = dataElement.getAttribute(TAG_ROWCOUNT);
                // System.out.println("Table has: "+rc+" rows, "+cc+" columns");

                numCols = Integer.parseInt(cc);
                numRows = Integer.parseInt(rc);
                mColumnNames = new String[numCols];
                mColumnTypes = new String[numCols];
                int[][] mdPos = new int[numCols][2];
                Cell[] mdCells = new Cell[numCols];
                cells = new Cell[numRows][numCols];
                NodeList kids = tables.item(i).getChildNodes();
                for (int k = 0, ksize = kids.getLength(); k < ksize; k++) {
                    Node n = kids.item(k);
                    if (n.getNodeName().compareTo(TAG_ROW) == 0) {
                        // System.out.println("Row[ "+rn+" ]:");
                        cn=1;
                        NodeList clist = n.getChildNodes();
                        for (int j = 0, jsize = clist.getLength(); j < jsize; j++) {
                            Node citem = clist.item(j);
                            if (citem.getNodeName().compareTo(TAG_CELL) == 0) {
                                Element cellElement = (Element) citem;
                                String val = null;
                                String mgd = cellElement.getAttribute(TAG_MERGEDOWN);
                                String idx = cellElement.getAttribute(TAG_INDEX);
                                int cmerge = 0;
                                if ((mgd != null) && (mgd.length()>0)) {
                                    cmerge = Integer.parseInt(mgd);
                                }

                                if ((idx != null) && (idx.length()>0)) {
                                    int nx = Integer.parseInt(idx);
                                    for (int x = cn; x < nx; x++) {
                                        String mval = (mdCells[x] != null) ? mdCells[x].getContents() : "";
                                        Cell c = null;
                                        if (mdCells[x] != null) { // merged..
                                            c = new Cell(rn, x, mdCells[x], mdPos[x][0], mdPos[x][1]);
                                        }
                                        cells[rn-1][cn-1] = c;
                                        // System.out.println("\tCell["+x+"]: --Skipped-- " +mval+" % "+mdCells[x]);
                                    }
                                    cn = nx;
                                }

                                boolean empty = true;
                                NodeList vals = citem.getChildNodes();
                                for (int x = 0, xsize = vals.getLength(); x < xsize; x++) {
                                    Node v = vals.item(x);
                                    if (v.getNodeName().compareTo(TAG_DATA) == 0) {
                                        if (v.getFirstChild() != null) {
                                            val = v.getFirstChild().getNodeValue();
                                            if (rn == 1) {
                                                if (val != null) {
                                                    if (val.trim().length() < 1) {
                                                        val = null;
                                                    }
                                                }
                                                mColumnNames[cn-1] = val;
                                            } else {
                                                mColumnTypes[cn-1] = validateDataType(val, mColumnTypes[cn-1]);
                                            }
                                            // System.out.println("\tCell["+cn+"]: "+val+" ("+mgd+", "+idx+"), Type: " + mColumnTypes[cn-1]);
                                            empty = false;
                                        }
                                    }
                                }
                                /*
                                if (empty) {
                                    System.out.println("\tCell["+cn+"]: **noValue** ("+mgd+", "+idx+")");
                                }
                                */
                                Cell cell = new Cell(rn, cn, val, cmerge, 0);
                                cells[rn-1][cn-1] = cell;
                                if (cn < numCols) {
                                    if ((cmerge == 0)) {
                                        mdCells[cn] = null;
                                        mdPos[cn][0] = -1;
                                        mdPos[cn][1] = -1;
                                    } else {
                                        mdCells[cn] = cell;
                                        mdPos[cn][0] = rn-1;
                                        mdPos[cn][1] = cn-1;
                                    }
                                }
                                cn++;
                            }
                        }
                        rn++;
                    }
                }
            }

        } catch (Exception e) {
            // A parsing error occurred; the xml input is not valid
            e.printStackTrace();
        }
    }

    private void writeWorkSheet() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element wbook = doc.createElement(TAG_WORKBOOK);
            doc.appendChild(wbook);
            wbook.setAttribute("xmlns", "urn:schemas-microsoft-com:office:spreadsheet");
            wbook.setAttribute("xmlns:ss", "urn:schemas-microsoft-com:office:spreadsheet");

            ProcessingInstruction pi = doc.createProcessingInstruction("mso-application", "progid=\"Excel.Sheet\"");
            wbook.getParentNode().insertBefore(pi, wbook);

            Element wsheet = doc.createElement(TAG_WORKSHEET);
            wbook.appendChild(wsheet);
            wsheet.setAttribute("ss:Name", "eview Worksheet");

            Element table = doc.createElement(TAG_TABLE);
            wsheet.appendChild(table);

            // add columns
            for (int i = 0; i<numCols; i++) {
                 Element column = doc.createElement(TAG_COLUMN);
                 table.appendChild(column);
                 column.setAttribute(TAG_WIDTH, "80");
            }

            // add rows
            if (cells != null) {
                for (int r=0; r<numRows; r++) {
                    //System.out.println("Row[ "+ (r+1) +" ]:");
                    Element row = doc.createElement(TAG_ROW);
                    table.appendChild(row);
                    boolean indexed = true;
                    for (int c=0; c<numCols; c++) {
                        Cell cell = cells[r][c];
                        if (cell == null) {
                            //System.out.println("\tCell["+(c+1)+"]: --Skipped-- Null");
                        } else if (cell.isMerged()) {
                            //System.out.println("\tCell["+(c+1)+"]: --Skipped-- "+cell.getContents()+" % "+cell.getRefCell()+", ["+cell.getRefRow()+", "+cell.getRefColumn()+"] "+cells[cell.getRefRow()][cell.getRefColumn()].getContents());
                        } else {
                            int mgd = cell.getRowMerged();
                            String val = cell.getContents();
                            if (val == null) {
                                System.out.println("\tCell["+(c+1)+"]: **noValue** ("+mgd+")");
                            } else {
                                //System.out.println("\tCell["+(c+1)+"]: "+val+" ("+mgd+")");

                                Element celement = doc.createElement(TAG_CELL);
                                row.appendChild(celement);
                                if (indexed) {
                                    indexed = false;
                                    if (c > 0) {
                                        celement.setAttribute(TAG_INDEX, ""+(c+1));
                                    }
                                }
                                if (mgd > 0)
                                  celement.setAttribute(TAG_MERGEDOWN, ""+mgd);
                                Element data = doc.createElement(TAG_DATA);
                                celement.appendChild(data);
                                data.appendChild(doc.createTextNode(val));
                                if (mColumnTypes[c].equalsIgnoreCase("double") ||
                                    mColumnTypes[c].equalsIgnoreCase("number")) {
                                    try {
                                        double dv = Double.parseDouble(val);
                                        data.setAttribute(TAG_TYPE, "Number");
                                    } catch (Exception ex) {
                                        data.setAttribute(TAG_TYPE, "String");
                                    }
                                } else {
                                    data.setAttribute(TAG_TYPE, "String");
                                }
                            }
                        }
                    }
                }
            }

            for (int i=0; i<numCols; i++) {
                System.out.println("Col Type["+i+"]: "+mColumnTypes[i]);
            }

            System.out.println("OutputXML:\n\n" + toXml(doc, "UTF-8", false));

        } catch (Exception e) {
            // A parsing error occurred; the xml input is not valid
            e.printStackTrace();
        }

    }

    private void createWorkSheet(int inNum, String[] inNames, String[] inTypes, int[] inFields,
                                 int outNum, String[] outNames, String[] outTypes) {
        try {
            numCols = inNum + 1 +outNum;
            int lastColumn = inNum - 1;
            numRows = 1;
            int[] inRows = new int[inNum];
            for (int k = lastColumn; k >= 0; k--) {
                inRows[k] = numRows;
                numRows = numRows * inFields[k];
            }
            numRows++; // add the title row..
            mColumnNames = new String[numCols];
            mColumnTypes = new String[numCols];

            int[][] mdPos = new int[numCols][2];
            Cell[] mdCells = new Cell[numCols];
            cells = new Cell[numRows][numCols];
            String[] prefix = new String[inNum];

            for (int i = 0; i < inNum; i++) {
                 mColumnTypes[i] = inTypes[i];
                 cells[0][i] = new Cell(1, i+1, inNames[i], 0, 0);
            }
            mColumnTypes[inNum] = "String";
            cells[0][inNum] = new Cell(1, inNum+1, "", 0, 0);
            for (int i = 0; i < outNum; i++) {
                 mColumnTypes[i+inNum+1] = outTypes[i];
                 cells[0][i+inNum+1] = new Cell(1, i+inNum+1+1, outNames[i], 0, 0);
            }
            for (int rn = 1; rn < numRows; rn++) {
                for (int cn = 0; cn < inNum; cn++) {
                    int ridx = (cn < lastColumn) ? ((rn-1) % inRows[cn]) : 0;
                    if (ridx == 0) {
                        int k = ((rn-1)/inRows[cn]) % inFields[cn];
                        int cmerge = 0;
                        if (cn < lastColumn) {
                           cmerge = inRows[cn] - 1;
                        }
                        String v = (inTypes[cn].equalsIgnoreCase("number")) ? "" : "String";
                        cells[rn][cn] = new Cell(rn+1, cn+1, v+k, cmerge, 0);
                    }
                }
            }
            for (int rn = 1; rn < numRows; rn++) {
                cells[rn][inNum] = new Cell(rn+1, inNum+1, "", 0, 0);
                for (int cn = 0; cn < outNum; cn++) {
                    String v = (outTypes[cn].equalsIgnoreCase("number")) ? "" : "String";
                    cells[rn][cn+inNum+1] = new Cell(rn+1, cn+inNum+1+1, v+rn, 0, 0);
                }
            }
        } catch (Exception e) {
            // A parsing error occurred; the xml input is not valid
        }
    }

    public String toXml(Node node, String encoding, boolean omitXMLDeclaration) {
        String ret = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.setOutputProperty(OutputKeys.ENCODING, encoding);
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            trans.setOutputProperty(OutputKeys.METHOD, "xml");
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omitXMLDeclaration? "yes":"no");
            trans.transform(new DOMSource(node), new StreamResult(baos));
            ret = baos.toString(encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private String validateDataType(String data, String type) {
        if (data.trim().equals("*")) { // skip wildcards...
            return type;
        }

        String nType = parseDataType(data);

        if (type == null) {  // OK, if inital type is undefined
            return nType;
        } else if (type.equalsIgnoreCase(nType)) { // same type as before..
            return type;
        } else if (nType.equalsIgnoreCase("string")) { // downcast to string
            return nType;
        }

        // illegal type found..
        System.out.println("**ERROR**: illegal type conversion ["+type+" -> "+nType+"]");

        return "string";
    }


    public String parseDataType(String data) {
         String mName;
         String mExpression;
         String mType; // Test || Assignment
         String mExpressionType; // ExactNumber || LessThanNumber || GreaterThanNumber || NumberRange || String
         double mValue;
         double mValueOne;
         double mValueTwo;
         Date mBeginDate;
         Date mEndDate;
         Date mDate;
         String mExpressionOne;
         String mExpressionTwo;
         LinkedList mSubFields;
         HashMap mAssignments;

        // Check for {ExactNumber}
        Pattern p = Pattern.compile("[ ]*[0123456789]+[ ]*");
        Matcher m = p.matcher(data);
        if (m.matches()) {
            try {
                String s[] = data.split("[ ]+");
                mExpression = s[0];
                mExpressionType = "ExactNumber";
                mValue = Double.parseDouble(s[0]);
                mType = "double";
                return mType;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Check for {RealNumber}
        p = Pattern.compile("[ ]*[0123456789]+\\.[0123456789]+[ ]*");
        m = p.matcher(data);
        if (m.matches()) {
            try {
                String s[] = data.split("[ ]+");
                mExpressionOne = s[0];
                mExpressionType = "NumberRange";
                mValueOne = Double.parseDouble(s[0]);
                mType = "double";
                return mType;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Check for {LessThanNumber}
        p = Pattern.compile("less[ ]+than[ ]+[0123456789]+[ ]*");
        m = p.matcher(data);
        if (m.matches()) {
            try {
                String s[] = data.split("[ ]+");
                mExpression = s[2];
                mExpressionType = "LessThanNumber";
                mValue = Double.parseDouble(s[2]);
                mType = "double";
                return mType;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Check for {GreaterThanNumber}
        p = Pattern.compile("greater[ ]+than[ ]+[0123456789]+[ ]*");
        m = p.matcher(data);
        if (m.matches()) {
            try {
                String s[] = data.split("[ ]+");
                mExpression = s[2];
                mExpressionType = "GreaterThanNumber";
                mValue = Double.parseDouble(s[2]);
                mType = "double";
                return mType;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Check for {NumberRange}
        p = Pattern.compile("[ ]*[0123456789]+[ ]*\\.\\.[ ]*[0123456789]+[ ]*");
        m = p.matcher(data);
        if (m.matches()) {
            try {
                String s[] = data.split("[ ]+");
                mExpressionOne = s[0];
                mExpressionTwo = s[2];
                mExpressionType = "NumberRange";
                mValueOne = Double.parseDouble(s[0]);
                mValueTwo = Double.parseDouble(s[2]);
                mType = "double";
                return mType;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Check for {DateRange}
        p = Pattern.compile("[ ]*[^ ]+[ ]*\\.\\.[ ]*[^ ]+[ ]*");
        m = p.matcher(data);
        if (m.matches()) {
            try {
                String s[] = data.split("\\.\\.");
                try {
                    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                    mBeginDate = formatter.parse(s[0]);
                    mEndDate = formatter.parse(s[1]);
                    mExpressionType = "DateRange";
                    mType = "dateTime";
                    return mType;
                } catch (Exception e) {
                    // Just continue if we cannot parse the dates.
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Check for {BeforeDate}
        p = Pattern.compile("[ ]*before[ ]*[^ ]+[ ]*");
        m = p.matcher(data);
        if (m.matches()) {
            try {
                String s[] = data.split("[ ]+");
                try {
                    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                    mDate = formatter.parse(s[1]);
                    mExpressionType = "BeforeDate";
                    mType = "dateTime";
                    return mType;
                } catch (Exception e) {
                    // Just continue if we cannot parse the dates.
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Check for {AfterDate}
        p = Pattern.compile("[ ]*after[ ]*[^ ]+[ ]*");
        m = p.matcher(data);
        if (m.matches()) {
            try {
                String s[] = data.split("[ ]+");
                try {
                    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                    mDate = formatter.parse(s[1]);
                    mExpressionType = "AfterDate";
                    mType = "dateTime";
                    return mType;
                } catch (Exception e) {
                    // Just continue if we cannot parse the dates.
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
//                DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            mDate = DateFormat.getDateTimeInstance().parse(data);
            mExpressionType = "ExactDate";
            mType = "dateTime";
            return mType;
        } catch (Exception e) {
            // If we cannot parse the date then it must be a string
        }

        mExpressionType = "ExactString";
        mExpression = data;
        mType = "string";

        return mType;
    }


    public void dump() {
        if (cells != null) {
            for (int r=0; r<numRows; r++) {
                System.out.println("Row[ "+ (r+1) +" ]:");
                for (int c=0; c<numCols; c++) {
                    Cell cell = cells[r][c];
                    if (cell == null) {
                        System.out.println("\tCell["+(c+1)+"]: --Skipped-- Null");
                    } else if (cell.isMerged()) {
                        System.out.println("\tCell["+(c+1)+"]: --Skipped-- "+cell.getContents()+" % "+cell.getRefCell()+", ["+cell.getRefRow()+", "+cell.getRefColumn()+"] "+cells[cell.getRefRow()][cell.getRefColumn()].getContents());
                    } else {
                        int mgd = cell.getRowMerged();
                        String val = cell.getContents();
                        if (val == null) {
                            System.out.println("\tCell["+(c+1)+"]: **noValue** ("+mgd+")");
                        } else {
                            System.out.println("\tCell["+(c+1)+"]: "+val+" ("+mgd+")");
                        }
                    }
                }
            }
        }
    }

    private String getName(String fn) {
        File f = new File(fn);
        return f.getName();
    }

    private String convertName(String fn) {
        return getName(fn).replace('.', '_');
    }

    public String genWsdl() {
        String name = convertName(excelFile);

        String wsdl_hdr =
                  "<?xml version=\"1.0\"?>\n"
                + "<definitions xmlns=\"http://schemas.xmlsoap.org/wsdl/\"\n";

        String wsdl_namespaces =
                  "  targetNamespace=\""+name+"\"\n"
                + "  xmlns:tns=\""+name+"\"\n"
                + "  xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
                + "  xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\"\n"
                + "  xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">\n";

        String wsdl_types_hdr =
                  "<types>\n"
                + "<xsd:schema targetNamespace=\""+name+"\"\n"
                + "            xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n";

        String wsdl_types_in =
                  "  <xsd:element name=\"inElement\">\n"
                + "    <xsd:complexType>\n"
                + "      <xsd:sequence>\n";

        String wsdl_types_ioend =
                  "      </xsd:sequence>\n"
                + "    </xsd:complexType>\n"
                + "  </xsd:element>\n";

        String wsdl_types_out =
                "  <xsd:element name=\"outElement\">\n"
              + "    <xsd:complexType>\n"
              + "      <xsd:sequence>\n";

        String wsdl_types_end =
                  "</xsd:schema>\n"
                + "</types>\n";

        String wsdl_message =
                  "<message name=\"InputMessage\">\n"
                + "  <part name=\"input\" element=\"tns:inElement\"/>\n"
                + "</message>\n"
                + "<message name=\"OutputMessage\">\n"
                + "  <part name=\"output\" element=\"tns:outElement\"/>\n"
                + "</message>\n";

        String wsdl_port =
                  "<portType name=\"eviewPortType\">\n"
                + "  <operation name=\"eviewOperation\">\n"
                + "    <input message=\"tns:InputMessage\"/>\n"
                + "    <output message=\"tns:OutputMessage\"/>\n"
                + "  </operation>\n"
                + "</portType>\n";

        String wsdl_binding =
                  "<binding name=\"eviewBinding\" type=\"tns:eviewPortType\">\n"
                + "  <soap:binding style=\"document\" transport=\"http://schemas.xmlsoap.org/soap/http\"/>\n"
                + "  <operation name=\"eviewOperation\">\n"
                + "     <soap:operation soapAction=\"\"/>\n"
                + "     <input>\n"
                + "       <soap:body use=\"literal\"/>\n"
                + "    </input>\n"
                + "    <output>\n"
                + "       <soap:body use=\"literal\"/>\n"
                + "    </output>\n"
                + "  </operation>\n"
                + "</binding>\n";

        String wsdl_service =
                  "<service name=\"eviewService\">\n"
                + "   <port name=\"eviewPort\" binding=\"tns:eviewBinding\">\n"
                + "   <soap:address location=\"http://localhost:12121/service/"+name+"\"/>\n"
                + "  </port>\n"
                + "</service>\n";

        String wsdl_slnk =
                  "<plnk:partnerLinkType name=\"serviceLinkType\"\n"
                + "  xmlns:plnk=\"http://schemas.xmlsoap.org/ws/2004/03/partner-link/\">\n"
                + "  <plnk:role name=\"server\" portType=\"eviewPortType\"/>\n"
                + "</plnk:partnerLinkType>\n";

        String wsdl_end = "\n</definitions>\n";

        String wsdl = wsdl_hdr
                    + wsdl_namespaces
                    + wsdl_types_hdr;

        if ((mColumnNames != null) && (mColumnTypes != null)) {
            wsdl += wsdl_types_in;

            int out = 0;
            for (int i=0; i<numCols; i++) {
                if (mColumnNames[i] == null) { // switch to output..
                    if (out == 0) {
                        wsdl +=wsdl_types_ioend;
                        wsdl += wsdl_types_out;
                    } else if (out == 1) {
                        wsdl +=wsdl_types_ioend;
                    }
                    out++;
                } else if (out < 2) {
                    wsdl += "      <xsd:element name=\""+mColumnNames[i]+"\" type=\"xsd:"+mColumnTypes[i]+"\"/>\n";
                }
            }
            if (out == 1) {
                wsdl +=wsdl_types_ioend;
            }

            wsdl += wsdl_types_end
                  + wsdl_message
                  + wsdl_port
                  + wsdl_binding
                  + wsdl_service
                  + wsdl_slnk
                  + wsdl_end;

        }
        return wsdl;
    }

    public String genMapData() {
        String name = convertName(excelFile);
        String map = "<eview type=\"requestReplyService\"\n"
                   + "      partnerLink=\"{"+name+"}plink\"\n"
                   + "      partnerLinkType=\"{"+name+"}serviceLinkType\"\n"
                   + "      roleName=\"server\"\n"
                   + "      portType=\"{"+name+"}eviewPortType\"\n"
                   + "      operation=\"eviewOperation\"\n"
                   + "      file=\""+getName(excelFile)+"\"/>\n";
        return map;
    }

    public String genJbiData() {
        String name = convertName(excelFile);
        String map = "<jbi version=\"1.0\"\n"
                   + "     xmlns=\"http://java.sun.com/xml/ns/jbi\"\n"
                   + "     xmlns:ns1=\""+name+"\">\n"
                   + "    <services binding-component=\"false\">\n"
                   + "       <provides interface-name=\"ns1:eviewPortType\"\n"
                   + "                 service-name=\"ns1:plink\"\n"
                   + "                 endpoint-name=\"server_myRole\"/>\n"
                   + "    </services>\n"
                   + "</jbi>\n";

        return map;
    }


    public static void main(String[] args) {
        try {
            /*
            Sheet ss = new Sheet("C:/test/test.eview");
            // System.out.println(ss.genWsdl());

            String[] inNames =  new String[] {"LastName", "Age", "Weight"};
            String[] inTypes =  new String[] {"String", "Number", "Number"};
            int[] inFields = new int[] { 4, 2, 3 };
            String[] outTypes = new String[] { "Number" };
            String[] outNames = new String[] { "Rate" };
            ss.createWorkSheet(3, inNames, inTypes, inFields, 1, outNames, outTypes);
            //ss.dump();
            ss.writeWorkSheet();
            */
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            String fn = "C:\\alaska\\root\\jbi\\test\\bpel\\src\\echo\\echo1.bpel";
            Document doc = factory.newDocumentBuilder().parse(new File(fn));

            NodeList ps = doc.getElementsByTagName("process");
            for (int i = 0, isize = ps.getLength(); i < isize; i++) {
                Element dataElement = (Element) ps.item(i);
                String ts = dataElement.getAttribute("targetNamespace");
                String ns = dataElement.getAttribute("name");
                System.out.println("Table ["+ns+"], ts:"+ts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  }
