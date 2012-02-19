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

public class ConfigurationManager {
	private String botName;
	private boolean verbose;
	private String server;
	private int serverPort;
	private String serverEncoding;
	private String defaultChannels;

	public String getBotName() {
		return botName;
	}

	public void setBotName(String botName) {
		this.botName = botName;
	}

	public boolean getVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerEncoding() {
		return serverEncoding;
	}

	public void setServerEncoding(String serverEncoding) {
		this.serverEncoding = serverEncoding;
	}

	public String getDefaultChannels() {
		return defaultChannels;
	}

	public void setDefaultChannels(String defaultChannels) {
		this.defaultChannels = defaultChannels;
	}

	public static boolean isWindows() {
		return (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
	}
}
