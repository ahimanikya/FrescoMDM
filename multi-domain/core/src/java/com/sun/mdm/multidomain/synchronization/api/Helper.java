package com.sun.mdm.multidomain.synchronization.api;

import java.io.File;
import java.util.jar.JarFile;

public interface Helper {
    
    /**
     * Sets the working directory.
     * 
     * @param directory the working directory
     */
    public void setWorkingDirectory(File directory);
    
    /**
     * Registers a rule to be included in rule artifact.
     * 
     * @param rule the rule to register
     */
    public void registerRule(JarFile rule);
    
    /**
     * Delete a rule (remove from artifact)
     * 
     * @param ruleName the rule name to delete
     */
    public void deleteRule(String ruleName);
    
    /**
     * Generates rules artifact to load in rules engine.
     * 
     * @param artifact location of rules artifact
     */
    public void generateArtifact(File artifact);
}
