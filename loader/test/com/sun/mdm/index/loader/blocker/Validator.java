package com.sun.mdm.index.loader.blocker;

import java.io.IOException;

import com.sun.mdm.index.loader.blocker.ValidationException;
import com.sun.mdm.index.loader.blocker.DataObjectReader;

public class Validator {

	public static boolean validation(DataObjectReader sources, DataObject[] targets) 
		throws ValidationException {
		try {
			for (DataObject target : targets) {
				DataObject source = sources.readDataObject();
				if (!source.equals(target)) {
					throw new ValidationException("The result is not matched [source, target]: " +
												   source.getValue() + " , " + target.getValue());
				}
			}	
			sources.close();
			return true;
		} catch(IOException ex) {
			throw new ValidationException(ex.getMessage());
		} 
	}
}
