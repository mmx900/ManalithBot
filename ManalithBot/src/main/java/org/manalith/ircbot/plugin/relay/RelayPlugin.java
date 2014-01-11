package org.manalith.ircbot.plugin.relay;

import java.util.StringTokenizer;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.SimplePlugin;
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
public class RelayPlugin extends SimplePlugin {
	public static RelayBot RELAY_BOT;
	private String outputFormat;

	public RelayPlugin(boolean enableRelay, RelayConfiguration config)
			throws Exception {
		// TODO enableRelay 인자를 받지 않고도 플러그인 초기화를 막을 수 있도록 구조 개선
		if (enableRelay) {
			init(config);
		}
	}

	private void init(RelayConfiguration config) throws Exception {
		final RelayBot bot = new RelayBot(config.buildPircBotConfiguration());
		RELAY_BOT = bot;
		// XXX Refer RelayBotListener.java:7
		bot.setIgnorePattern(config.getIgnorePattern());
		bot.setOutputFormat(config.getOutputFormat());
		bot.startBot();

		this.outputFormat = config.getOutputFormat();

		final StringTokenizer st = new StringTokenizer(
				config.getDefaultChannels(), ",");
		while (st.hasMoreTokens())
			bot.sendIRC().joinChannel(st.nextToken());
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

	public void setBot(ManalithBot bot) {
		RELAY_BOT.setTargetbot(bot);
	}

	public void onMessage(MessageEvent event) {
		String channel = event.getChannel().getName();
		String sender = event.getUser().getNick();
		String message = event.getMessage();

		if (message.equals("relay:stop")) {
			setRelaying(false);
			event.respond("릴레이를 강제로 종료합니다.");
			// 타겟 채널 이름은 소스 채널 이름과 동일한 것으로 간주.
			// 명령에 대한 안내에 대해 입력 명령은 전송하지 않음.
			RELAY_BOT.sendIRC().message(event.getChannel().getName(),
					"릴레이를 강제로 종료합니다.");
			return;
		} else if (message.equals("relay:start")) {
			setRelaying(true);
			event.respond("릴레이를 강제로 시작합니다.");
			// 타겟 채널 이름은 소스 채널 이름과 동일한 것으로 간주.
			// 명령에 대한 안내에 대해 입력 명령은 전송하지 않음.
			RELAY_BOT.sendIRC().message(event.getChannel().getName(),
					"릴레이를 강제로 시작합니다.");
			return;
		}

		if (isRelaying()) {
			/**
			 * 메인 봇에 메시지가 들어오면 릴레이 봇으로 전송한다.
			 */

			/*
			 * String targetChannel = "#setzer";
			 * 
			 * if (channel.equals("#gnome")) { targetChannel = "#gnome"; } else
			 * if (channel.equals("#setzer")) { targetChannel = "#setzer"; }
			 */

			// 타겟 서버의 타겟 채널 이름은 소스 서버의 소스 채널 이름과 동일한 것으로 간주.
			RELAY_BOT.sendIRC().message(channel,
					String.format(this.outputFormat, sender, message));
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
