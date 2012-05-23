package org.manalith.ircbot.plugin.relay;

public class RelayPluginConfigurationManager {
	private String botLogin;
	private String botName;
	private boolean verbose;
	private String server;
	private int serverPort;
	private String serverEncoding;
	private String defaultChannels;
	private String outputFormat;
	private String ignorePattern;

	public String getBotLogin() {
		return botLogin;
	}

	public void setBotLogin(String botLogin) {
		this.botLogin = botLogin;
	}

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

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	public String getIgnorePattern() {
		return ignorePattern;
	}

	public void setIgnorePattern(String ignorePattern) {
		this.ignorePattern = ignorePattern;
	}
}
