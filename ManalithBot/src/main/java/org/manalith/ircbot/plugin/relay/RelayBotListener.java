package org.manalith.ircbot.plugin.relay;

import java.util.regex.Pattern;

import org.manalith.ircbot.ManalithBot;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class RelayBotListener extends ListenerAdapter<ManalithBot> {
	private ManalithBot targetBot;
	// TODO RelayPluginConfiguration.ignorePattern 사용하게 수정
	private Pattern ignorePattern;
	// Pattern ignorePattern = Pattern.compile("(^&lt;\\S+&gt;  ).*");

	private String outputFormat;

	public ManalithBot getTargetBot() {
		return targetBot;
	}

	public void setTargetBot(ManalithBot targetBot) {
		this.targetBot = targetBot;
	}

	// XXX need more test
	public void setIgnorePattern(String ignorePattern) {
		this.ignorePattern = Pattern.compile(ignorePattern);
	}

	// XXX need more test
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	@Override
	public void onMessage(MessageEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sender = event.getUser().getNick();
		String message = event.getMessage();

		if (ignorePattern.matcher(message).matches())
			return;

		if (RelayPlugin.isRelaying() && targetBot != null) {
			/**
			 * 릴레이봇에 메시지가 들어오면 메인 봇으로 전송한다.
			 */
			/*
			 * String targetChannel = "#setzer";
			 * 
			 * if (channel.equals("#gnome")) { targetChannel = "#gnome"; } else
			 * if (channel.equals("#setzer")) { targetChannel = "#setzer"; }
			 */

			// TODO RelayPluginConfiguration.outputFormat 사용하게 수정

			// Ref. 타겟 서버의 타겟 채널 이름은 소스 서버의 소스 채널과 동일한 것으로 간주
			targetBot.sendMessage(channel,
					String.format(this.outputFormat, sender, message), /* XXX */
					false);
		}
	}
}
