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
package com.sun.mdm.index.dataobject;

import java.io.IOException;

/**
 * @author Sujit Biswas
 *
 */
public interface DataObjectWriter {

	/**
	 * Ordinarily this method will write the DataObject into a stream's buffer,
	 * flushing the buffer to the underlying stream as needed. If the requested
	 * length is at least as large as the buffer, however, then this method will
	 * flush the buffer and write the characters directly to the underlying
	 * stream
	 * 
	 * <p>
	 * User should call close() or flush() once all the DataObject has been
	 * written
	 */

	public abstract void writeDataObject(DataObject d) throws IOException;

	/**
	 * Flush any thing data which is in the buffer
	 */
	public abstract void flush() throws IOException;

	/**
	 * Close the outputStream
	 */
	public abstract void close() throws IOException;

}
