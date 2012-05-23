package org.manalith.ircbot.plugin.relay;

import org.pircbotx.PircBotX;

public class RelayBot extends PircBotX {

	public RelayBot(String botLogin, String botName) {
		setLogin(botLogin);
		setName(botName);
		getListenerManager().addListener(new RelayBotListener());
	}

}
