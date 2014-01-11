package org.manalith.ircbot.plugin.relay;

import org.manalith.ircbot.ManalithBot;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

public class RelayBot extends PircBotX {
	private RelayBotListener relayBotListener;

	public RelayBot(Configuration<? extends PircBotX> configuration) {
		super(configuration);

		relayBotListener = new RelayBotListener();
		configuration.getListenerManager().addListener(relayBotListener);
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
