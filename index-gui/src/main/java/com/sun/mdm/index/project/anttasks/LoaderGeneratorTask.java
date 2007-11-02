/**
 * 
 */
package com.sun.mdm.index.project.anttasks;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * @author Sujit Biswas, Kevin Kao
 * 
 */
public class LoaderGeneratorTask extends Task {

	private String srcDir;

	private String configDir;

	protected void setSrcDir(String srcDir) {
		this.srcDir = getProjectPath() + File.separator + srcDir;
	}

	protected void setConfigDir(String configDir) {
		this.configDir = getProjectPath() + File.separator + configDir;
	}

	/**
	 * @return
	 */
	private String getProjectPath() {
		return getProject().getProperty("basedir");

	}

	@Override
	public void execute() throws BuildException {
		
		LoaderConfig config = new LoaderConfig();
		
		
		//TODO set the 
		
		//config.setThreshold("");
		//config.setBlockConfig(blockConfig);
		
		//config.setMatchConfig(matchConfig);
		
		
		
	}

}
