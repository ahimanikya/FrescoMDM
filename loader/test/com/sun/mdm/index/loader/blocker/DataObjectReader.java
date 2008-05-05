package com.sun.mdm.index.loader.blocker;

import java.io.IOException;

import com.sun.mdm.index.loader.blocker.DataObject;

public interface DataObjectReader {

	public abstract DataObject readDataObject() throws IOException;

	public abstract void close() throws IOException;

}
