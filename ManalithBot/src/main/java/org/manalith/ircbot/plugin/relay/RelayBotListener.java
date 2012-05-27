package org.manalith.ircbot.plugin.relay;

import java.util.regex.Pattern;

import org.manalith.ircbot.ManalithBot;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class RelayBotListener extends ListenerAdapter<ManalithBot> {
	private ManalithBot targetBot;
	// TODO RelayPluginConfiguration.ignorePattern 사용하게 수정
	Pattern ignorePattern = Pattern.compile("(^&lt;\\S+&gt;  ).*");

	public ManalithBot getTargetBot() {
		return targetBot;
	}

	public void setTargetBot(ManalithBot targetBot) {
		this.targetBot = targetBot;
	}

	@Override
	public void onMessage(MessageEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String message = event.getMessage();

		if (ignorePattern.matcher(message).matches())
			return;

		if (RelayPlugin.isRelaying() && targetBot != null) {
			/**
			 * 릴레이봇에 메시지가 들어오면 메인 봇으로 전송한다.
			 */
			String targetChannel = "#setzer";

			if (channel.equals("#gnome")) {
				targetChannel = "#gnome";
			} else if (channel.equals("#setzer")) {
				targetChannel = "#setzer";
			}

			// TODO RelayPluginConfiguration.outputFormat 사용하게 수정
			targetBot.sendLoggedMessage(targetChannel, "<"
					+ event.getUser().getNick() + ">  " + event.getMessage(),
					false);
		}
	}
}
