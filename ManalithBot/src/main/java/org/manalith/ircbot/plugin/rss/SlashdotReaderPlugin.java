package org.manalith.ircbot.plugin.rss;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class SlashdotReaderPlugin extends AbstractBotPlugin {
	private static final String NAMESPACE = "!slashdot";

	public String getName() {
		return "Slashdot";
	}

	public String getCommands() {
		return NAMESPACE;
	}

	public String getHelp() {
		return "설  명: 호출하면 슬래시닷의 가장 최근 글을 출력합니다, 사용법: !slashdot";
	}

	public void onMessage(MessageEvent event) {
		String message = event.getMessage();
		String channel = event.getChannel().getName();
		ManalithBot bot = event.getBot();

		if (message.equals(NAMESPACE))
			bot.sendMessage(channel, getLastestSlashdot());
		else if (message.equals(NAMESPACE + ":help"))
			bot.sendMessage(channel, getHelp());
	}

	private String getLastestSlashdot() {
		return new SlashDotReader().read();
	}
}
