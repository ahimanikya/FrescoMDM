/**
 * 
 */
package com.sun.mdm.index.project.generator.descriptor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author Sujit Biswas
 * 
 */
public class SunEjbJarWriter {

	private File projectFolder;

	/**
	 * @param projectFolder
	 */
	public SunEjbJarWriter(File projectFolder) {
		this.projectFolder = projectFolder;
	}

	public void write() {
		try {
			String s = projectFolder.getAbsolutePath() + "/src/conf/";

			InputStream is = SunEjbJarWriter.class.getClassLoader()
					.getResourceAsStream("com/sun/mdm/index/project/generator/descriptor/sun-ejb-jar.xml.tmpl");

			byte[] buf = new byte[0];
			byte[] chunk = new byte[4096];
			int count;
			while ((count = is.read(chunk)) >= 0) {
				byte[] temp = new byte[buf.length + count];
				System.arraycopy(buf, 0, temp, 0, buf.length);
				System.arraycopy(chunk, 0, temp, buf.length, count);
				buf = temp;
			}
			String content = new String(buf, "ISO8859-1");
			is.close();

			FileWriter fw = new FileWriter(s + "sun-ejb-jar.xml");

			fw.write(content);

			fw.close();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
