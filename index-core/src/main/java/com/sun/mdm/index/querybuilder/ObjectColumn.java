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
package com.sun.mdm.index.querybuilder;

import java.util.List;

import com.sun.mdm.index.configurator.impl.blocker.AbstractCondition;

/**
 * The ObjectColumn class is used to keep values for the given column. Three distinct ObjectNodes 
 * can be supplied. First ObjectNode can contain multiple values.   
 *
 * @author Sanjay.sharma
 * @version $Revision: 1.1 $
 */

class ObjectColumn {

	private AbstractCondition abstractCondition;
	private List valueList;
	private Object valueTwo;
	private Object valueThree;

	ObjectColumn(AbstractCondition ac ) {
		this.abstractCondition = ac;
	}
	
	List getValueList() {
		return valueList;
	}
	/**
	 * @param valueOneList The valueOneList to set.
	 */
	void setValueList(List valueList) {
		this.valueList = valueList;
	}
	/**
	 * @return Returns the valueThree.
	 */
	Object getValueThree() {
		return valueThree;
	}
	/**
	 * @param valueThree The valueThree to set.
	 */
	void setValueThree(Object valueThree) {
		this.valueThree = valueThree;
    }
    /**
     * @return Returns the valueTwo.
     */
    Object getValueTwo() {
        return valueTwo;
    }
    /**
     * @param valueTwo The valueTwo to set.
     */
    void setValueTwo(Object valueTwo) {
        this.valueTwo = valueTwo;
    }
    /**
     * @return Returns the abstractCondition.
     */
    AbstractCondition getAbstractCondition() {
        return abstractCondition;
    }
    /**
     * @param abstractCondition The abstractCondition to set.
     */
    void setAbstractCondition(AbstractCondition abstractCondition) {
        this.abstractCondition = abstractCondition;
    }
}
