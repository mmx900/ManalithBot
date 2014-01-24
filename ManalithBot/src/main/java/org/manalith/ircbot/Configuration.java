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

import java.nio.charset.Charset;
import java.util.List;
import java.util.StringTokenizer;

import org.manalith.ircbot.plugin.EventDispatcher;
import org.manalith.ircbot.plugin.EventLogger;
import org.manalith.ircbot.plugin.Plugin;
import org.pircbotx.Configuration.Builder;

public class Configuration {

	private String botLogin;
	private String botName;
	private boolean verbose;
	private String server;
	private int serverPort;
	private Charset serverEncoding;
	private String defaultChannels;
	private boolean tryToReconnect;
	private List<Plugin> plugins;
	private boolean autoAcceptInvite;

	public org.pircbotx.Configuration<ManalithBot> buildPircBotConfiguration(
			EventLogger eventLogger, EventDispatcher eventDispatcher)
			throws Exception {

		Builder<ManalithBot> builder = new Builder<ManalithBot>()
				.setServer(server, serverPort).setLogin(botLogin)
				.setName(botName).setEncoding(serverEncoding)
				.addListener(eventLogger).addListener(eventDispatcher);

		StringTokenizer st = new StringTokenizer(defaultChannels, ",");
		while (st.hasMoreTokens())
			builder.addAutoJoinChannel(st.nextToken());

		return builder.buildConfiguration();
	}

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
	public Charset getServerEncoding() {
		return serverEncoding;
	}

	/**
	 * @param serverEncoding
	 *            the serverEncoding to set
	 */
	public void setServerEncoding(Charset serverEncoding) {
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
	public List<Plugin> getPlugins() {
		return plugins;
	}

	/**
	 * @param plugins
	 *            the plugins to set
	 */
	public void setPlugins(List<Plugin> plugins) {
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
