/**
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

package com.sun.mdm.index.loader.euid;

/**
 * 
 * represents a record in the final assumed match file
 * 
 * @author Sujit Biswas
 * 
 */
public class MatchFileRecord {

	private long sysid1;

	private long sysid2;

	private double weight;

	private long index;

	/**
	 * @param sysid1
	 * @param sysid2
	 * @param weight
	 */
	public MatchFileRecord(long sysid1, long sysid2, double weight) {
		this.sysid1 = sysid1;
		this.sysid2 = sysid2;
		this.weight = weight;

	}

	/**
	 * @return the sysid1
	 */
	public long getSysid1() {
		return sysid1;
	}

	/**
	 * @param sysid1
	 *            the sysid1 to set
	 */
	public void setSysid1(long sysid1) {
		this.sysid1 = sysid1;
	}

	/**
	 * @return the sysid2
	 */
	public long getSysid2() {
		return sysid2;
	}

	/**
	 * @param sysid2
	 *            the sysid2 to set
	 */
	public void setSysid2(long sysid2) {
		this.sysid2 = sysid2;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * @return the index
	 */
	public long getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(long index) {
		this.index = index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "sysid1=" + sysid1 + ", sysid2=" + sysid2 + ", weight=" + weight
				+ ", index=" + index;
	}

}
