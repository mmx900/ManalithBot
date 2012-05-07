package org.manalith.ircbot.plugin.relay;

import org.manalith.ircbot.ManalithBot;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class RelayBotListener extends ListenerAdapter<ManalithBot> {
	@Override
	public void onMessage(MessageEvent<ManalithBot> event) throws Exception {
		String channel = event.getChannel().getName();

		if (RelayPlugin.isRelaying()) {
			/**
			 * 릴레이봇에 메시지가 들어오면 메인 봇으로 전송한다.
			 */
			String targetChannel = "#setzer";

			if (channel.equals("#gnome")) {
				targetChannel = "#gnome";
			} else if (channel.equals("#setzer")) {
				targetChannel = "#setzer";
			}

			// XXX 최신 API에 맞게 수정 필요
			// BotMain.BOT.sendLoggedMessage(targetChannel, "<" + sender + "> "
			// + message, false);
		}
	}
}
