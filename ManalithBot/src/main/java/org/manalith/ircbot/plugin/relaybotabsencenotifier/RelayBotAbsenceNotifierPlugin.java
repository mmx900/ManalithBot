/*
 	org.manalith.ircbot.plugin.nvidiadrivernews/NvidiaDriverNewsPlugin.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2012  Seong-ho, Cho <darkcircle.0426@gmail.com>

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
package org.manalith.ircbot.plugin.relaybotabsencenotifier;

import java.util.Set;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.pircbotx.User;

public class RelayBotAbsenceNotifierPlugin extends AbstractBotPlugin {
	public String getName() {
		return "릴봇부재알리미";
	}

	public String getCommands() {
		return null;
	}

	public void onJoin(MessageEvent event) {
		ManalithBot bot = event.getBot();
		String sender = event.getUser().getNick();
		String channel = event.getChannel().getName();
		
		boolean beingBot = false;
		if (sender.equals("DarkCircle")) {
			Set<User> list = bot.getUsers(bot.getChannel("#gnome"));
			for (User u : list) {
				if (u.getNick().equals("♠한씨네")) {
					beingBot = true;
					break;
				}
			}

			if (!beingBot)
				bot.sendLoggedMessage(channel, "DarkCircle: ...");
		}
	}

	public void onMessage(MessageEvent event) {
		ManalithBot bot = event.getBot();
		if (event.getUser().getNick().equals("DarkCircle")) {
			boolean beingBot = false;
			Set<User> list = bot.getUsers(bot.getChannel("#gnome"));
			for (User u : list) {
				if (u.getNick().equals("♠한씨네")) {
					beingBot = true;
					break;
				}
			}

			if (!beingBot)
				bot.sendLoggedMessage(event.getChannel().getName(),
						"DarkCircle: ...");
		}
	}

	public void onPart(MessageEvent event) {
		ManalithBot bot = event.getBot();
		String sender = event.getUser().getNick();
		String channel = event.getChannel().getName();
		
		if (sender.equals("♠"))
			bot.sendLoggedMessage(channel, "DarkCircle: ...");
	}

	public void onQuit(MessageEvent event) {
		ManalithBot bot = event.getBot();
		String sourceNick = event.getUser().getNick();
		String channel = event.getChannel().getName();
		
		if (sourceNick.equals("♠"))
			bot.sendLoggedMessage(channel, "DarkCircle: ...");
	}
}
