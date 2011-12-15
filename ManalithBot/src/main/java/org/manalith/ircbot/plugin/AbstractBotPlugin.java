package org.manalith.ircbot.plugin;

import java.io.File;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.resources.MessageEvent;

public abstract class AbstractBotPlugin implements IBotPlugin {
	protected ManalithBot bot;

	public AbstractBotPlugin(ManalithBot bot) {
		this.bot = bot;
	}

	public String getResourcePath() {
		StringBuilder builder = new StringBuilder();
		builder.append(System.getProperty("user.dir"));
		builder.append(File.separator);
		builder.append("data");
		builder.append(File.separator);
		builder.append(getClass().getName());
		return builder.toString();
	}

	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	public void onJoin(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub

	}

	public void onMessage(MessageEvent event) {
		// TODO Auto-generated method stub

	}

	public void onPart() {
		// TODO Auto-generated method stub

	}

	public void onQuit() {
		// TODO Auto-generated method stub

	}

}
