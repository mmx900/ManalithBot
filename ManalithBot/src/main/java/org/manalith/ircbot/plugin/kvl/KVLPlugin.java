/*
 	org.manalith.ircbot.plugin.kvl/KVLPlugin.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2011, 2012  Seong-ho, Cho <darkcircle.0426@gmail.com>

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
package org.manalith.ircbot.plugin.kvl;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class KVLPlugin extends AbstractBotPlugin {

	public String getName() {
		return "커널버전리스트";
	}

	public String getCommands() {
		return "커널|kernel";
	}

	@Override
	public String getHelp() {
		return "!커널 (latest[default]|all|help), !kernel (latest[default]|all|help)";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String msg = event.getMessage();
		String channel = event.getChannel();

		String[] command = msg.split("\\s");

		if (command[0].equals("!커널") || command[0].equals("!kernel")) {
			if (command.length >= 3) {
				bot.sendLoggedMessage(channel, "Too many arguments!");
				event.setExecuted(true);
				return;
			}

			KVLRunner runner = new KVLRunner();

			if (command.length >= 2)
				bot.sendLoggedMessage(channel, runner.run(command[1]));
			else
				bot.sendLoggedMessage(channel, runner.run(""));
			event.setExecuted(true);
		}
	}

}