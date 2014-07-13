/*
 	org.manalith.ircbot.plugin.sample/HelloPlugin.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2011, 2012  Ki-Beom, Kim
 	Copyright (C) 2012 Seong-ho, Cho <darkcircle.0426@gmail.com>

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
package org.manalith.ircbot.plugin.sample;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.annotation.Description;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.pircbotx.Channel;
import org.springframework.stereotype.Component;

@Component
public class HelloPlugin extends SimplePlugin {

	@Override
	public String getName() {
		return "Hello (Sample)";
	}

	@Override
	public String getDescription() {
		return "예제 플러그인 입니다.";
	}

	@BotCommand("인사")
	public String hello(String... args) {
		if (ArrayUtils.isEmpty(args)) {
			return "Hello world!";
		} else {
			return String.format("Hello %s!", StringUtils.join(args, ", "));
		}
	}

	@BotCommand("작별인사")
	public String bye() {
		return "Bye!";
	}

	@BotCommand
	public String count(MessageEvent event) {
		Channel channel = event.getChannel();

		return String.format("%s 방에 %d 명이 있습니다.", channel.getName(), channel
				.getUsers().size());
	}

	@BotCommand("채널인사")
	public String sayHelloWithCount(MessageEvent event,
			@Description("인사말") String hello) {
		return String
				.format("%s 방 여러분 %s", event.getChannel().getName(), hello);
	}
}
