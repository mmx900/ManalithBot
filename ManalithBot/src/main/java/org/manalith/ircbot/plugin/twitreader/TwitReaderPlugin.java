package org.manalith.ircbot.plugin.twitreader;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class TwitReaderPlugin extends AbstractBotPlugin {

	public TwitReaderPlugin(ManalithBot bot) {
		super(bot);
	}

	public String getName() {
		return "트윗리더";
	}

	public String getNamespace() {
		return "twit";
	}

	public String getHelp() {
		return "";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String msg = event.getMessage();
		String channel = event.getChannel();

		String[] command = msg.split("\\s");

		TwitReaderRunner runner = new TwitReaderRunner(command);
		String /* [] */result = runner.run();

		if ( !result.equals("") )
		{
			bot.sendLoggedMessage(channel, result/* [i] */);
			event.setExecuted(true);
		}
		
	}
}
