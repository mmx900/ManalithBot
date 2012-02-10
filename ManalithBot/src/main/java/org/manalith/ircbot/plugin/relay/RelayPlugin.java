package org.manalith.ircbot.plugin.relay;

import java.util.StringTokenizer;

import org.manalith.ircbot.BotMain;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

/**
 * 한 서버의 메시지를 다른 서버에 그대로 전달해주는 봇. 이 플러그인이 정상 작동하려면 isExecuted() 가 실행되지 않도록 반드시
 * 플러그인 목록의 최상단에 위치해야 한다.
 * 
 * @author setzer
 * 
 */
public class RelayPlugin extends AbstractBotPlugin {
	public static RelayBot RELAY_BOT;

	public RelayPlugin() throws Exception {
		RelayPluginConfigurationManager config = new RelayPluginConfigurationManager();
		final RelayBot bot = new RelayBot(config.getBotName());
		RELAY_BOT = bot;
		bot.setVerbose(config.getVerbose());
		bot.setEncoding(config.getServerEncoding());
		bot.connect(config.getServer(), config.getServerPort());

		final StringTokenizer st = new StringTokenizer(
				config.getDefaultChannels(), ",");
		while (st.hasMoreTokens())
			bot.joinChannel(st.nextToken());
	}

	public String getName() {
		return "릴레이 플러그인";
	}

	public String getCommands() {
		return null;
	}

	public String getHelp() {
		return null;
	}

	public void onMessage(MessageEvent event) {
		if (event.getMessage().equals("relay:stop")) {
			setRelaying(false);
			BotMain.BOT
					.sendLoggedMessage(event.getChannel(), "릴레이를 강제로 종료합니다.");
		} else if (event.getMessage().equals("relay:start")) {
			setRelaying(true);
			BotMain.BOT
					.sendLoggedMessage(event.getChannel(), "릴레이를 강제로 시작합니다.");
		}

		if (isRelaying()) {
			/**
			 * 메인 봇에 메시지가 들어오면 릴레이 봇으로 전송한다.
			 */
			String targetChannel = "#setzer";
			String channel = event.getChannel();

			if (channel.equals("#gnome")) {
				targetChannel = "#gnome";
			} else if (channel.equals("#setzer")) {
				targetChannel = "#setzer";
			}

			RELAY_BOT.sendMessage(targetChannel, "<" + event.getSender() + "> "
					+ event.getMessage());
		}
	}

	private static boolean relaying = false;

	public static boolean isRelaying() {
		return relaying;
	}

	public static void setRelaying(boolean relaying) {
		RelayPlugin.relaying = relaying;
	}

}
