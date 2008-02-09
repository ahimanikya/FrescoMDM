/*
 * BEGIN_HEADER - DO NOT EDIT
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * If applicable add the following below this CDDL HEADER,
 * with the fields enclosed by brackets "[]" replaced with
 * your own identifying information: Portions Copyright
 * [year] [name of copyright owner]
 */

/*
 * @(#)PullDownListItem.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.index.edm.common;


/**
 * A dropdown list item contains its description and name.
 * @author Jeff Lin
 */
public class PullDownListItem implements java.io.Serializable, Comparable {
    private String description;
    private String name;
    private boolean selected;

    /**
     * Creates a new instance of PullDownListItem
     *
     * @param name attribute of the object
     * @param description attribute of the object
     */
    public PullDownListItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * getter for description attribute of this object
     * @return description for the name
     */
    public String getDescription() {
        return description;
    }

    /**
     * getter for name attribute of this object
     * @return name of the object
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for Selected attribute of the PullDownListItem object
     * @return selected or not
     */
    public boolean getSelected() {
        return selected;
    }

    /**
     * Setter for Selected attribute of the PullDownListItem object
     *
     * @param selected or not
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @param obj another PullDownListItem object
     * @return >0 =0 <0
     */
    public int compareTo(Object obj) {
        return this.description.compareTo(((PullDownListItem) obj).description);
    }
}
