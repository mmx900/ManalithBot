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
import org.springframework.stereotype.Component;

@Component
public class LinuxPackageFinderPlugin extends AbstractBotPlugin {

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
		onMessage(event, event.getChannel());
	}

	public void onPrivateMessage(MessageEvent event) {
		onMessage(event, event.getSender());
	}

	public void onMessage(MessageEvent event, String target) {
		String message = event.getMessage();
		String[] command = message.split("\\s");

		if ((command[0].equals("!deb") || command[0].equals("!ubu")
				|| command[0].equals("!fed") || command[0].equals("!gen") || command[0]
					.equals("!ar")) && command.length > 2) {
			bot.sendLoggedMessage(target, "검색 단어는 하나면 충분합니다.");
			event.setExecuted(true);
			return;
		}

		PackageFinder finder = null;

		if ((command[0].equals("!deb") || command[0].equals("!ubu")
				|| command[0].equals("!fed") || command[0].equals("!gen") || command[0]
					.equals("!ar")) && command.length == 1) {
			bot.sendLoggedMessage(target, getHelp());
			event.setExecuted(true);
		} else if (command[0].equals("!deb")) {
			finder = new DebianPackageFinder(command[1]);
		} else if (command[0].equals("!ubu")) {
			finder = new UbuntuPackageFinder(command[1]);
		} else if (command[0].equals("!fed")) {
			finder = new FedoraPackageFinder(command[1]);
		} else if (command[0].equals("!gen")) {
			finder = new GentooPackageFinder(command[1]);
		} else if (command[0].equals("!ar")) {
			finder = new ArchPackageFinder(command[1]);
		}

		if (finder != null) {
			if (finder instanceof DebianPackageFinder) {
				String[] lines = finder.find().split("\n");
				for (String l : lines)
					bot.sendLoggedMessage(target, l);
			} else {
				bot.sendLoggedMessage(target, finder.find());
			}

			event.setExecuted(true);
		}
	}
}
