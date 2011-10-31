package org.manalith.ircbot.plugin;

import org.manalith.ircbot.BotMain;
import org.manalith.ircbot.resources.MessageEvent;

public abstract class AbstractBotPlugin implements IBotPlugin {
	protected BotMain bot;
	
	public AbstractBotPlugin(BotMain bot){
		this.bot = bot;
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
