/*
 	DistroPkgFinderPlugin.java
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

package org.manalith.ircbot.plugin.distropkgfinder;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class DistroPkgFinderPlugin extends AbstractBotPlugin {

	public DistroPkgFinderPlugin(ManalithBot bot) {
		super(bot);
	}

	public String getNamespace() {
		return "deb|ubu|fed|gen|ar";
	}

	public String getName() {
		return "패키지 검색";
	}

	public String getHelp() {
		return "!deb (pkg_name) | !ubu (pkg_name) | !fed (pkg_name) | !gen (pkg_name) | !ar (pkg_name)";
	}

	public void onMessage(MessageEvent event) {
		String message = event.getMessage();
		String channel = event.getChannel();
		String[] command = message.split("\\s");

		if ((command[0].equals("!deb") || command[0].equals("!ubu")
				|| command[0].equals("!fed") || command[0].equals("!gen") || command[0]
					.equals("!ar")) && command.length > 2) {
			bot.sendLoggedMessage(channel, "검색 단어는 하나면 충분합니다.");
			event.setExecuted(true);
			return;
		}
		if ((command[0].equals("!deb") || command[0].equals("!ubu")
				|| command[0].equals("!fed") || command[0].equals("!gen") || command[0]
					.equals("!ar")) && command.length == 1) {
			bot.sendLoggedMessage(channel, this.getHelp());
			event.setExecuted(true);
		} else if (command[0].equals("!deb")) {
			DebianPkgFinderJsoupRunner runner = new DebianPkgFinderJsoupRunner(
					command[1]);
			bot.sendLoggedMessage(channel, runner.run());
			event.setExecuted(true);
		} else if (command[0].equals("!ubu")) {
			UbuntuPkgFinderJsoupRunner runner = new UbuntuPkgFinderJsoupRunner(
					command[1]);
			bot.sendLoggedMessage(channel, runner.run());
			event.setExecuted(true);
		} else if (command[0].equals("!fed")) {
			FedoraPkgFinderRunner runner = new FedoraPkgFinderRunner(command[1]);
			bot.sendLoggedMessage(channel, runner.run());
			event.setExecuted(true);
		} else if (command[0].equals("!gen")) {
			GentooPkgFinderJsoupRunner runner = new GentooPkgFinderJsoupRunner(
					command[1]);
			bot.sendLoggedMessage(channel, runner.run());
			event.setExecuted(true);
		} else if (command[0].equals("!ar")) {
			ArchPkgFinderRunner runner = new ArchPkgFinderRunner(command[1]);
			bot.sendLoggedMessage(channel, runner.run());
			event.setExecuted(true);
		}
	}
}
