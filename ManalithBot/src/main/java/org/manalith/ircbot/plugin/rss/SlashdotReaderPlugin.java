package org.manalith.ircbot.plugin.rss;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class SlashdotReaderPlugin extends AbstractBotPlugin {
	private static final String NAMESPACE = "slashdot";

	public String getName() {
		return "Slashdot";
	}

	public String getCommands() {
		return NAMESPACE;
	}

	public String getHelp() {
		return "호출하면 슬래시닷의 가장 최근 글을 출력합니다.";
	}

	public void onMessage(MessageEvent event) {
		ManalithBot bot = event.getBot();
		String message = event.getMessage();
		String channel = event.getChannel().getName();

		if (message.equals(NAMESPACE))
			bot.sendLoggedMessage(channel, getLastestSlashdot());
		else if (message.equals(NAMESPACE + ":help"))
			bot.sendLoggedMessage(channel, getHelp());
	}

	private String getLastestSlashdot() {
		return new SlashDotReader().read();
	}
}
