package org.manalith.ircbot.plugin.relay;

import java.nio.charset.Charset;
import java.util.StringTokenizer;

import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;

public class RelayConfiguration {
	private String botLogin;
	private String botName;
	private boolean verbose;
	private String server;
	private int serverPort;
	private Charset serverEncoding;
	private String defaultChannels;
	private String outputFormat;
	private String ignorePattern;

	public Configuration<RelayBot> buildPircBotConfiguration() throws Exception {
		Builder<RelayBot> builder = new Configuration.Builder<RelayBot>()
				.setServer(server, serverPort).setLogin(botLogin)
				.setName(botName).setEncoding(serverEncoding);

		StringTokenizer st = new StringTokenizer(defaultChannels, ",");
		while (st.hasMoreTokens())
			builder.addAutoJoinChannel(st.nextToken());

		return builder.buildConfiguration();
	}

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

	public Charset getServerEncoding() {
		return serverEncoding;
	}

	public void setServerEncoding(Charset serverEncoding) {
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
