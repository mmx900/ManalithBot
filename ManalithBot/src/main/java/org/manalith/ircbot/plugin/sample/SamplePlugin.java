/*
 	org.manalith.ircbot.plugin.sample/SamplePlugin.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2011  Ki-Beom, Kim

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

import org.pircbotx.User;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEventData;

public class SamplePlugin extends AbstractBotPlugin {

	public SamplePlugin(ManalithBot bot) {
		super(bot);
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return "Sample Plugin";
	}

	public String getCommands() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHelp() {
		return "!친반묘";
	}

	public void onJoin(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub

	}

	public void onMessage(MessageEventData event) {
		this.onMessage(event, event.getChannel());
	}

	@Override
	public void onPrivateMessage(MessageEventData event) {
		this.onMessage(event, event.getSender());

	}

	protected void onMessage(MessageEventData event, String target) {
		String message = event.getMessage();

		if (message.equals("!친반묘")) {
			User[] users = event.getUsers();
			boolean isMyo = false;
			for (User u : users) {
				if (u.getNick().equals("myojok")) {
					isMyo = true;
					break;
				}
			}
			bot.sendLoggedMessage(target, isMyo ? "(두리번) ... 친묘!"
					: "(두리번) +_+/ 멸묘!");
			event.setExecuted(true);
		}
	}

	public void onPart(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub

	}

	public void onQuit(String sourceNick, String sourceLogin,
			String sourceHostname, String reason) {
		// TODO Auto-generated method stub

	}

}
