/*
 	org.manalith.ircbot.common/PropertyManager.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2011  Seong-ho, Cho <darkcircle.0426@gmail.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.manalith.ircbot.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

public class PropertyManager {
	private Logger logger = Logger.getLogger(getClass());

	private String path;
	private String fileName;
	private Properties properties;

	public PropertyManager(String path, String fileName) {
		this.path = path;
		this.fileName = fileName;
	}

	public void loadProperties() {
		try {
			PropFileReadWriter fr = new PropFileReadWriter(path + fileName);
			properties = fr.bringUpPropertyFromFile();
		} catch (IOException ioe) {
			properties = new Properties();

			try {
				storeProperties();
			} catch (IOException e) {
				logger.warn(e.getMessage(), e);
			}
		}
	}

	public void storeProperties() throws FileNotFoundException, IOException {
		PropFileReadWriter fw = new PropFileReadWriter(path + fileName);
		fw.pushUpPropertyToFile(properties);
	}

	public String getValue(String key) {
		return (String) properties.get(key);
	}

	public void setKeyValue(String key, String value) {
		properties.setProperty(key, value);
	}

	public String[] getKeyList() {
		Set<String> ss = properties.stringPropertyNames();

		if (ss.size() == 0) {
			return null;
		} else {
			String[] result = new String[ss.size()];
			Object[] o = ss.toArray();

			for (int i = 0; i < ss.size(); i++) {
				result[i] = (String) o[i];
			}
			return result;
		}
	}

	public void removeKeyValue(String key) {
		properties.remove(key);
	}
}
