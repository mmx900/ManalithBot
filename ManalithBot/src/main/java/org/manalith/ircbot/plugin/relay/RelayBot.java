package org.manalith.ircbot.plugin.relay;

import org.pircbotx.PircBotX;

public class RelayBot extends PircBotX {

	public RelayBot(String botName) {
		setName(botName);
		getListenerManager().addListener(new RelayBotListener());
	}

}
