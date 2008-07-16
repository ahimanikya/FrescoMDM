package com.sun.mdm.index.dataobject.validation;

import java.util.AbstractList;

import com.sun.mdm.index.objects.validation.ObjectDescriptor;

public interface ObjectDescriptorReader {
    /**
     * Gets a list of objectDescriptors.
     * @return objectDescriptors
     */
    abstract public AbstractList<ObjectDescriptor> getObjectDescriptors();

}
