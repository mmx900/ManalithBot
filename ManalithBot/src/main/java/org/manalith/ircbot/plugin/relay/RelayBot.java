package org.manalith.ircbot.plugin.relay;

import org.manalith.ircbot.ManalithBot;
import org.pircbotx.PircBotX;

public class RelayBot extends PircBotX {
	private RelayBotListener relayBotListener;
	
	public RelayBot(String botLogin, String botName) {
		setLogin(botLogin);
		setName(botName);
		relayBotListener = new RelayBotListener();
		getListenerManager().addListener(relayBotListener);
	}

	public void setTargetbot(ManalithBot bot) {
		relayBotListener.setTargetBot(bot);
	}
	
	public void setIgnorePattern(String ignorePattern) {
		relayBotListener.setIgnorePattern(ignorePattern);
	}
	
	public void setOutputFormat(String outputFormat) {
		relayBotListener.setOutputFormat(outputFormat);
	}

}
