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

import org.jibble.pircbot.User;
import org.manalith.ircbot.BotMain;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class SamplePlugin extends AbstractBotPlugin {

	public SamplePlugin(ManalithBot bot) {
		super(bot);
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return "Sample Plugin";
	}

	public String getNamespace() {
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

	public void onMessage(MessageEvent event) {
		String message = event.getMessage();
		String channel = event.getChannel();

		if (message.equals("!친반묘")) {
			User[] users = BotMain.BOT.getUsers(channel);
			boolean isMyo = false;
			for (User u : users) {
				if (u.getNick().equals("myojok")) {
					isMyo = true;
					break;
				}
			}
			BotMain.BOT.sendLoggedMessage(channel, isMyo ? "(두리번) ... 친묘!"
					: "(두리번) +_+/ 멸묘!");
			event.setExecuted(true);
		}
	}

	@Override
	public void onPrivateMessage(MessageEvent event) {
		// TODO Auto-generated method stub
		String message = event.getMessage();
		String sender = event.getSender();

		if (message.equals("!친반묘")) {

			boolean isMyo = false;

			if (sender.equals("myojok")) {
				isMyo = true;
			}
			BotMain.BOT.sendLoggedMessage(sender, isMyo ? "친묘!" : "+_+/ 멸묘!");
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
