/*
 	org.manalith.ircbot.plugin.ping/PingPlugin.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2012  Ki-Beom Kim

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.manalith.ircbot.plugin.ping;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
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
	public void onMessage(MessageEvent event) {
		onMessage(event, event.getChannel().getName());
	}

	@Override
	public void onPrivateMessage(MessageEvent event) {
		onMessage(event, event.getUser().getNick());
	}

	protected void onMessage(MessageEvent event, String target) {
		String message = event.getMessage();
		ManalithBot bot = event.getBot();

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
