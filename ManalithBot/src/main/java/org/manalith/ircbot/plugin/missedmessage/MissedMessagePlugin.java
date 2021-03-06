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

import java.util.Set;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.pircbotx.User;
import org.springframework.stereotype.Component;

@Component
public class MissedMessagePlugin extends SimplePlugin {

	@Override
	public String getName() {
		return "부재중메시지";
	}

	@Override
	public String getCommands() {
		return "msg";
	}

	@Override
	public String getHelp() {
		return "설  명: 부재중인 분께 메시지를 남겨드립니다, 사용법: !msg [닉] [150자 메시지] // 3개 까지 남길 수 있습니다";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String channel = event.getChannel().getName();
		String sender = event.getUser().getNick();
		String message = event.getMessage();
		Set<User> users = event.getChannel().getUsers();

		String[] cmdnmsg = message.split("\\s");

		if (cmdnmsg[0].equals("!msg")) {
			if (cmdnmsg.length == 1) {
				event.respond(getHelp());
			} else if (cmdnmsg.length == 2) {
				String recv = cmdnmsg[1];

				for (User u : users) {
					if (u.getNick().equals(recv))
					// check someone who has a matched nick
					{
						event.respond(sender + ", ...(물끄럼)...");
						return;
					}
				}

				event.respond("남길 메시지가 없습니다");
			} else if (cmdnmsg.length >= 3) {
				String recv = cmdnmsg[1];

				for (User u : users) {
					if (u.getNick().equals(recv))
					// check someone who has a matched nick
					{
						event.respond(sender + ", ...(물끄럼)...");
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
						getResourcePath());
				event.respond(runner.addMsg(sender, channel.substring(1) + "."
						+ recv, msg.toString()));
				// exclude # in the channel name.
			}
		}
	}

	@Override
	public void onJoin(String channel, String sender, String login,
			String hostname) {
		MissedMessageRunner runner = new MissedMessageRunner(getResourcePath());

		if (!runner.isMatchedNickinList(channel.substring(1) + "." + sender))
			runner.addMsgSlot(channel.substring(1) + "." + sender);
		else {
			String[] msgs = runner.getMsg(channel.substring(1) + "." + sender);

			if (msgs != null)
				for (String msg : msgs) {
					ManalithBot.getInstance().sendMessage(channel, msg);
				}
		}
	}

	@Override
	public void onPart(String channel, String sender, String login,
			String hostname) {
		MissedMessageRunner runner = new MissedMessageRunner(getResourcePath());

		if (!runner.isMatchedNickinList(channel.substring(1) + "." + sender))
			runner.addMsgSlot(channel.substring(1) + "." + sender);
	}
}
