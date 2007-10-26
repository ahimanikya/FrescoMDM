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
 * An enumeration type listing the available content types for a cell
 */
public final class CellType
{

  /**
   * The text description of this cell type
   */
  private String description;

  /**
   * Private constructor
   * @param desc the description of this type
   */
  private CellType(String desc)
  {
    description = desc;
  }

  /**
   * Returns a string description of this cell
   *
   * @return the string description for this type
   */
  public String toString()
  {
    return description;
  }

  /**
   */
  public static final CellType EMPTY           = new CellType("Empty");
  /**
   */
  public static final CellType LABEL           = new CellType("Label");
  /**
   */
  public static final CellType NUMBER          = new CellType("Number");
  /**
   */
  public static final CellType BOOLEAN         = new CellType("Boolean");
  /**
   */
  public static final CellType ERROR           = new CellType("Error");
  /**
   */
  public static final CellType NUMBER_FORMULA  =
                                         new CellType("Numerical Formula");
  /**
   */
  public static final CellType DATE_FORMULA  = new CellType("Date Formula");
  /**
   */
  public static final CellType STRING_FORMULA  = new CellType("String Formula");
  /**
   */
  public static final CellType BOOLEAN_FORMULA =
                                          new CellType("Boolean Formula");
  /**
   */
  public static final CellType FORMULA_ERROR   = new CellType("Formula Error");
  /**
   */
  public static final CellType DATE            = new CellType("Date");
}
