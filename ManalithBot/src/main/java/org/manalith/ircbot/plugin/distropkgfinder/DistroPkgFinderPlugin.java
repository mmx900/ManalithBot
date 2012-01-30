/*
 	org.manalith.ircbot.plugin.distopkgfinder/DistroPkgFinderPlugin.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2011, 2012  Seong-ho, Cho <darkcircle.0426@gmail.com>
 	Copyright (C) 2012  Changwoo, Ryu <cwryu@debian.org>

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

import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class DistroPkgFinderPlugin extends AbstractBotPlugin {

	public String getCommands() {
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
			DebianPkgFinderRunner runner = new DebianPkgFinderRunner(
					command[1]);
			String[] lines = runner.run().split("\n");
			for (String l : lines)
			    bot.sendLoggedMessage(channel, l);
			event.setExecuted(true);
		} else if (command[0].equals("!ubu")) {
			UbuntuPkgFinderRunner runner = new UbuntuPkgFinderRunner(
					command[1]);
			bot.sendLoggedMessage(channel, runner.run());
			event.setExecuted(true);
		} else if (command[0].equals("!fed")) {
			FedoraPkgFinderRunner runner = new FedoraPkgFinderRunner(command[1]);
			bot.sendLoggedMessage(channel, runner.run());
			event.setExecuted(true);
		} else if (command[0].equals("!gen")) {
			GentooPkgFinderRunner runner = new GentooPkgFinderRunner(
					command[1]);
			bot.sendLoggedMessage(channel, runner.run());
			event.setExecuted(true);
		} else if (command[0].equals("!ar")) {
			ArchPkgFinderRunner runner = new ArchPkgFinderRunner(command[1]);
			bot.sendLoggedMessage(channel, runner.run());
			event.setExecuted(true);
		}
	}
	
	public void onPrivateMessage ( MessageEvent event ) 
	{
		String message = event.getMessage();
		String sender = event.getSender();
		//String channel = event.getChannel();
		String[] command = message.split("\\s");

		if ((command[0].equals("!deb") || command[0].equals("!ubu")
				|| command[0].equals("!fed") || command[0].equals("!gen") || command[0]
					.equals("!ar")) && command.length > 2) {
			bot.sendLoggedMessage(sender, "검색 단어는 하나면 충분합니다.");
			event.setExecuted(true);
			return;
		}
		if ((command[0].equals("!deb") || command[0].equals("!ubu")
				|| command[0].equals("!fed") || command[0].equals("!gen") || command[0]
					.equals("!ar")) && command.length == 1) {
			bot.sendLoggedMessage(sender, this.getHelp());
			event.setExecuted(true);
		} else if (command[0].equals("!deb")) {
			DebianPkgFinderRunner runner = new DebianPkgFinderRunner(
					command[1]);
			String[] lines = runner.run().split("\n");
			for (String l : lines)
			    bot.sendLoggedMessage(sender, l);
			event.setExecuted(true);
		} else if (command[0].equals("!ubu")) {
			UbuntuPkgFinderRunner runner = new UbuntuPkgFinderRunner(
					command[1]);
			bot.sendLoggedMessage(sender, runner.run());
			event.setExecuted(true);
		} else if (command[0].equals("!fed")) {
			FedoraPkgFinderRunner runner = new FedoraPkgFinderRunner(command[1]);
			bot.sendLoggedMessage(sender, runner.run());
			event.setExecuted(true);
		} else if (command[0].equals("!gen")) {
			GentooPkgFinderRunner runner = new GentooPkgFinderRunner(
					command[1]);
			bot.sendLoggedMessage(sender, runner.run());
			event.setExecuted(true);
		} else if (command[0].equals("!ar")) {
			ArchPkgFinderRunner runner = new ArchPkgFinderRunner(command[1]);
			bot.sendLoggedMessage(sender, runner.run());
			event.setExecuted(true);
		}
	}
}
