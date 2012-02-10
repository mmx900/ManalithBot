package org.manalith.ircbot.plugin.relay;

import org.manalith.ircbot.ConfigurationManager;

public class RelayPluginConfigurationManager {
	private ConfigurationManager mgr = ConfigurationManager.getInstance();

	public RelayPluginConfigurationManager() {

	}

	public String getBotName() {
		return mgr.get("org.manalith.ircbot.bot.plugin.relay.name");
	}

	public boolean getVerbose() {
		return Boolean.parseBoolean(mgr
				.get("org.manalith.ircbot.bot.plugin.relay.verbose"));
	}

	public String getServerEncoding() {
		return mgr.get("org.manalith.ircbot.bot.plugin.relay.server.encoding");
	}

	public int getServerPort() {
		return Integer.parseInt(mgr
				.get("org.manalith.ircbot.bot.plugin.relay.server.port"));
	}

	public String getServer() {
		return mgr.get("org.manalith.ircbot.bot.plugin.relay.server");
	}

	public String getDefaultChannels() {
		return mgr.get("org.manalith.ircbot.bot.plugin.relay.server.channels");
	}
}
