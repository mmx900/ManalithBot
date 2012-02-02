/*
 	org.manalith.ircbot.plugin.missedmessage/MissedMessagePlugin.java
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
package org.manalith.ircbot.plugin.missedmessage;

import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEventData;
import org.pircbotx.User;

public class MissedMessagePlugin extends AbstractBotPlugin {

	public String getName() {
		// TODO Auto-generated method stub
		return "부재중메시지";
	}

	public String getCommands() {
		// TODO Auto-generated method stub
		return "msg";
	}

	public String getHelp() {
		return "!msg [닉] [150자 메시지] // 3개 까지 남길 수 있습니다";
	}

	public void onMessage(MessageEventData event) {
		// TODO <- is lie =3
		String channel = event.getChannel();
		String sender = event.getSender();
		String message = event.getMessage();

		User[] users = event.getUsers();

		String[] cmdnmsg = message.split("\\s");

		if (cmdnmsg[0].equals("!msg")) {
			if (cmdnmsg.length == 1) {
				bot.sendLoggedMessage(channel, this.getHelp());
			} else if (cmdnmsg.length == 2) {
				String recv = cmdnmsg[1];

				for (User u : users) {
					if (u.getNick().equals(recv))
					// check someone who has a matched nick
					{
						bot.sendLoggedMessage(channel, sender + ", ...(물끄럼)...");
						event.setExecuted(true);
						return;
					}
				}

				bot.sendLoggedMessage(channel, "남길 메시지가 없습니다");
				event.setExecuted(true);
			} else if (cmdnmsg.length >= 3) {
				String recv = cmdnmsg[1];

				for (User u : users) {
					if (u.getNick().equals(recv))
					// check someone who has a matched nick
					{
						bot.sendLoggedMessage(channel, sender + ", ...(물끄럼)...");
						event.setExecuted(true);
						return;
					}
				}

				StringBuilder msg = new StringBuilder();

				for (int i = 2; i < cmdnmsg.length; i++) {
					if (i != 2)
						msg.append(" ");
					msg.append(cmdnmsg[i]);
				}

				MissedMessageRunner runner = new MissedMessageRunner(
						this.getResourcePath());
				bot.sendLoggedMessage(channel, runner.addMsg(sender,
						channel.substring(1) + "." + recv, msg.toString()));
				// exclude # in the channel name.
			}
			event.setExecuted(true);
		}

		return;
	}

	public void onJoin(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub
		MissedMessageRunner runner = new MissedMessageRunner(
				this.getResourcePath());

		if (!runner.isMatchedNickinList(channel.substring(1) + "." + sender))
			runner.addMsgSlot(channel.substring(1) + "." + sender);
		else {
			String[] msgs = runner.getMsg(channel.substring(1) + "." + sender);

			if (msgs != null)
				for (int i = 0; i < msgs.length; i++) {
					bot.sendLoggedMessage(channel, msgs[i]);
				}
		}
	}

	public void onPart(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub
		MissedMessageRunner runner = new MissedMessageRunner(
				this.getResourcePath());

		if (!runner.isMatchedNickinList(channel.substring(1) + "." + sender))
			runner.addMsgSlot(channel.substring(1) + "." + sender);
	}
}
