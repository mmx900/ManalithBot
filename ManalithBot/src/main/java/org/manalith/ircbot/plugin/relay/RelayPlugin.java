package org.manalith.ircbot.plugin.relay;

import java.util.StringTokenizer;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

/**
 * 한 서버의 메시지를 다른 서버에 그대로 전달해주는 봇. 이 플러그인이 정상 작동하려면 isExecuted() 가 실행되지 않도록 반드시
 * 플러그인 목록의 최상단에 위치해야 한다.
 * 
 * @author setzer
 * 
 */
@Component
public class RelayPlugin extends AbstractBotPlugin {
	public static RelayBot RELAY_BOT;

	public RelayPlugin(boolean enableRelay,
			RelayPluginConfigurationManager config) throws Exception {
		// TODO enableRelay 인자를 받지 않고도 플러그인 초기화를 막을 수 있도록 구조 개선
		if (enableRelay) {
			init(config);
		}
	}

	private void init(RelayPluginConfigurationManager config) throws Exception {
		final RelayBot bot = new RelayBot(config.getBotLogin(),
				config.getBotName());
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

	@Override
	public void setBot(ManalithBot bot) {
		RELAY_BOT.setTargetbot(bot);
		super.setBot(bot);
	}

	public void onMessage(MessageEvent event) {
		String channel = event.getChannel().getName();

		if (event.getMessage().equals("relay:stop")) {
			setRelaying(false);
			event.getBot().sendLoggedMessage(channel, "릴레이를 강제로 종료합니다.");
		} else if (event.getMessage().equals("relay:start")) {
			setRelaying(true);
			event.getBot().sendLoggedMessage(channel, "릴레이를 강제로 시작합니다.");
		}

		if (isRelaying()) {
			/**
			 * 메인 봇에 메시지가 들어오면 릴레이 봇으로 전송한다.
			 */
			String targetChannel = "#setzer";

			if (channel.equals("#gnome")) {
				targetChannel = "#gnome";
			} else if (channel.equals("#setzer")) {
				targetChannel = "#setzer";
			}

			RELAY_BOT.sendMessage(targetChannel, "<"
					+ event.getUser().getNick() + "> " + event.getMessage());
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
