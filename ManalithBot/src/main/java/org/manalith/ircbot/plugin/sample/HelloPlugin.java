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
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class HelloPlugin extends AbstractBotPlugin {

	public String getName() {
		return "Hello Plugin (Sample)";
	}

	public String getCommands() {
		return "!hello, !bye";
	}

	public String getHelp() {
		return "!hello, !bye";
	}

	@BotCommand({ "!hello", "!인사" })
	public String sayHello(String... args) {
		if (ArrayUtils.isEmpty(args)) {
			return "Hello world!";
		} else {
			return String.format("Hello %s!", StringUtils.join(args, ", "));
		}
	}

	@BotCommand({ "!bye", "!작별인사" })
	public String sayBye() {
		return "Bye!";
	}

	@BotCommand({ "!count" })
	public String count(MessageEvent event) {
		return String.format("%s 방에 %d 명이 있습니다.", event.getChannel(), getBot()
				.getChannel(event.getChannel()).getUsers().size());
	}

	@BotCommand(value = { "!채널인사" }, minimumArguments = 1)
	public String sayHelloWithCount(MessageEvent event, String... args) {
		return String.format("%s 방 여러분 %s", event.getChannel(), args[0]);
	}
}
