package org.manalith.ircbot.plugin;

import org.manalith.ircbot.resources.MessageEvent;

public interface IBotPlugin {
	public String getName();

	public String getNamespace();

	public String getHelp();

	public void onJoin(String channel, String sender, String login,
			String hostname);

	public void onMessage(MessageEvent event);
	
	public void onPrivateMessage(MessageEvent event);

	public void onPart(String channel, String sender, String login,
			String hostname);

	public void onQuit(String sourceNick, String sourceLogin,
			String sourceHostname, String reason);
}
