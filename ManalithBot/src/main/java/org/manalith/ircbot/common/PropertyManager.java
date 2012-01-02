//
// PropertyManager.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.
package org.manalith.ircbot.common;

import java.util.Properties;
import java.util.Set;
import java.io.IOException;
import java.io.FileNotFoundException;

public class PropertyManager {

	private String path;
	private String filename;
	private Properties prop;

	public PropertyManager() {
		this.setPath("");
		this.setFilename("");
		this.setProp(null);
	}

	public PropertyManager(String newFilename) {
		this.setPath("");
		this.setFilename(newFilename);
		this.setProp(null);
	}

	public PropertyManager(String newPath, String newFilename) {
		this.setPath(newPath);
		this.setFilename(newFilename);
		this.setProp(null);
	}

	public void setPath(String newPath) {
		this.path = newPath;
	}

	public String getPath() {
		return this.path;
	}

	public void setFilename(String newFilename) {
		this.filename = newFilename;
	}

	public String getFilename() {
		return this.filename;
	}

	public void initProp() {
		this.setProp(new Properties());
	}

	public void setProp(Properties newProperty) {
		this.prop = newProperty;
	}

	public Properties getProp() {
		return this.prop;
	}

	public void loadProperties() throws IOException {
		PropFileReadWriter fr = new PropFileReadWriter(this.getPath()
				+ this.getFilename());
		this.setProp(fr.bringUpPropertyFromFile());

		if (this.getProp() == null)
			this.initProp();
	}

	public void storeProperties() throws FileNotFoundException, IOException {
		PropFileReadWriter fw = new PropFileReadWriter(this.getPath()
				+ this.getFilename());
		fw.pushUpPropertyToFile(this.getProp());
	}

	public String getValue(String key) {
		return (String) this.getProp().get(key);
	}

	public void setValue(String key, String value) {
		this.getProp().setProperty(key, value);
	}

	public String[] getKeyList() {
		Set<String> ss = this.getProp().stringPropertyNames();
		if (ss.size() == 0)
			return null;
		else {
			String[] result = new String[ss.size()];
			Object[] o = ss.toArray();

			for (int i = 0; i < ss.size(); i++) {
				result[i] = (String) o[i];
			}
			return result;
		}
	}

	public void removeKeyValue(String key) {
		this.getProp().remove(key);
	}
}
