/*
 	org.manalith.ircbot/ConfigurationManager.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2005, 2011, 2012  Ki-Beom, Kim

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
package org.manalith.ircbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

public class ConfigurationManager {
	private Logger logger = Logger.getLogger(getClass());
	private static ConfigurationManager instance = null;
	private static String configFile = "ircbot.props.xml";

	public static ConfigurationManager getInstance() {
		if (instance == null)
			instance = new ConfigurationManager();

		return instance;
	}

	public static void setConfigFile(String configFile) {
		ConfigurationManager.configFile = configFile;
	}

	private Properties properties = null;

	private ConfigurationManager() {
		java.io.InputStream in = null;
		properties = null;
		try {
			in = new FileInputStream(new File(configFile));
			properties = new Properties();
			properties.loadFromXML(in);
			in.close();
		} catch (IOException e) {
			logger.error(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e);
				} finally {
					in = null;
				}
			}
		}
	}

	public String get(String key) {
		return properties.getProperty(key);
	}

	public String getBotName() {
		return properties.getProperty("org.manalith.ircbot.bot.name");
	}

	public boolean getVerbose() {
		return BooleanUtils.toBoolean(properties
				.getProperty("org.manalith.ircbot.bot.verbose"));
	}

	public String getServer() {
		return properties.getProperty("org.manalith.ircbot.bot.server");
	}

	public int getServerPort() {
		return NumberUtils.toInt(properties
				.getProperty("org.manalith.ircbot.bot.server.port"));
	}

	public String getServerEncoding() {
		return properties
				.getProperty("org.manalith.ircbot.bot.server.encoding");
	}

	public String getDefaultChannels() {
		return properties
				.getProperty("org.manalith.ircbot.bot.server.channels");
	}

	public boolean getEnableRelay() {
		return BooleanUtils.toBoolean(properties
				.getProperty("org.manalith.ircbot.bot.enablerelay"));
	}

	public String getRelayBotName() {
		return properties
				.getProperty("org.manalith.ircbot.bot.plugin.relay.name");
	}

	public boolean getRelayVerbose() {
		return BooleanUtils.toBoolean(properties
				.getProperty("org.manalith.ircbot.bot.plugin.relay.verbose"));
	}

	public String getRelayServer() {
		return properties
				.getProperty("org.manalith.ircbot.bot.plugin.relay.server");
	}

	public int getRelayServerPort() {
		return NumberUtils
				.toInt(properties
						.getProperty("org.manalith.ircbot.bot.plugin.relay.server.port"));
	}

	public String getRelayServerEncoding() {
		return properties
				.getProperty("org.manalith.ircbot.bot.plugin.relay.server.encoding");
	}

	public String getRelayDefaultChannels() {
		return properties
				.getProperty("org.manalith.ircbot.bot.plugin.relay.server.channels");
	}

	public static boolean isWindows() {
		// windows
		return (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0);
	}

	public static boolean isMac() {
		// Mac
		return (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		String os = System.getProperty("os.name").toLowerCase();
		// linux or unix
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
	}
}
