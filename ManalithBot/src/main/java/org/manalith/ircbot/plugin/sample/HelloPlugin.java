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
		return "!hello";
	}

	public String getHelp() {
		return "!hello";
	}

	@BotCommand({ "!hello", "!인사" })
	public String sayHello(MessageEvent event, String... args) {
		return "Hello World!";
	}

	@BotCommand({ "!bye", "!작별인사" })
	public String sayBye(MessageEvent event, String... args) {
		return "Bye!";
	}
}