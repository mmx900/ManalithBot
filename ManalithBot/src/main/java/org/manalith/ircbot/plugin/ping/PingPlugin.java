package org.manalith.ircbot.plugin.ping;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang.StringUtils;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEventData;

public class PingPlugin extends AbstractBotPlugin {
	public String getName() {
		return "핑";
	}

	public String getCommands() {
		return "ping";
	}

	@Override
	public String getHelp() {
		return "서버에 ICMP ECHO 요청을 보내고 결과를 표시합니다.";
	}

	@Override
	public void onMessage(MessageEventData event) {
		onMessage(event, event.getChannel());
	}

	@Override
	public void onPrivateMessage(MessageEventData event) {
		onMessage(event, event.getSender());
	}

	protected void onMessage(MessageEventData event, String target) {
		String message = event.getMessage();

		if (StringUtils.startsWith(message, "!ping ")) {
			String uri = StringUtils.substringAfter(message, " ");
			if (StringUtils.isBlank(uri)) {
				bot.sendLoggedMessage(target, "[Ping] 사용법 : !ping DOMAIN/IP");
				return;
			}

			InetAddress addr = null;
			try {
				addr = InetAddress.getByName(uri);
			} catch (UnknownHostException e) {
				bot.sendLoggedMessage(target, "[Ping] 올바른 주소가 아닙니다.");
			}

			try {
				bot.sendLoggedMessage(target, String.format(
						"[Ping] %s(%s) is %s: ", addr.getHostName(), addr
								.getHostAddress(),
						addr.isReachable(3000) ? "reachable" : "not reachable"));
			} catch (IOException e) {
				bot.sendLoggedMessage(
						target,
						String.format("[Ping] 네트웍 오류가 발생했습니다.(%s)",
								e.getMessage()));
			}

			event.setExecuted(true);
		}
	}
}
