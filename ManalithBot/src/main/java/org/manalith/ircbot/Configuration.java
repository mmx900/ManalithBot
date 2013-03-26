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

import java.util.List;

import org.manalith.ircbot.plugin.IBotPlugin;

public class Configuration {
	private String botLogin;
	private String botName;
	private boolean verbose;
	private String server;
	private int serverPort;
	private String serverEncoding;
	private String defaultChannels;
	private boolean tryToReconnect;
	private List<IBotPlugin> plugins;
	private boolean autoAcceptInvite;

	/**
	 * @return the botLogin
	 */
	public String getBotLogin() {
		return botLogin;
	}

	/**
	 * @param botLogin
	 *            the botLogin to set
	 */
	public void setBotLogin(String botLogin) {
		this.botLogin = botLogin;
	}

	/**
	 * @return the botName
	 */
	public String getBotName() {
		return botName;
	}

	/**
	 * @param botName
	 *            the botName to set
	 */
	public void setBotName(String botName) {
		this.botName = botName;
	}

	/**
	 * @return the verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * @param verbose
	 *            the verbose to set
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @param server
	 *            the server to set
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * @return the serverPort
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort
	 *            the serverPort to set
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * @return the serverEncoding
	 */
	public String getServerEncoding() {
		return serverEncoding;
	}

	/**
	 * @param serverEncoding
	 *            the serverEncoding to set
	 */
	public void setServerEncoding(String serverEncoding) {
		this.serverEncoding = serverEncoding;
	}

	/**
	 * @return the defaultChannels
	 */
	public String getDefaultChannels() {
		return defaultChannels;
	}

	/**
	 * @param defaultChannels
	 *            the defaultChannels to set
	 */
	public void setDefaultChannels(String defaultChannels) {
		this.defaultChannels = defaultChannels;
	}

	/**
	 * @return the tryToReconnect
	 */
	public boolean isTryToReconnect() {
		return tryToReconnect;
	}

	/**
	 * @param tryToReconnect
	 *            the tryToReconnect to set
	 */
	public void setTryToReconnect(boolean tryToReconnect) {
		this.tryToReconnect = tryToReconnect;
	}

	/**
	 * @return the plugins
	 */
	public List<IBotPlugin> getPlugins() {
		return plugins;
	}

	/**
	 * @param plugins
	 *            the plugins to set
	 */
	public void setPlugins(List<IBotPlugin> plugins) {
		this.plugins = plugins;
	}

	/**
	 * @return the autoAcceptInvite
	 */
	public boolean isAutoAcceptInvite() {
		return autoAcceptInvite;
	}

	/**
	 * @param autoAcceptInvite
	 *            the autoAcceptInvite to set
	 */
	public void setAutoAcceptInvite(boolean autoAcceptInvite) {
		this.autoAcceptInvite = autoAcceptInvite;
	}
}
