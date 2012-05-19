/*
 *  Created on 2005. 7. 26
 	org.manalith.ircbot.resources/MessageEvent.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2005  Ki-Beom, Kim

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

package org.manalith.ircbot.resources;

import org.manalith.ircbot.ManalithBot;
import org.pircbotx.Channel;
import org.pircbotx.User;

public class MessageEvent {
	private final ManalithBot bot;
	private final Channel channel;
	private final User user;

	private String message;
	private boolean executed; // 실행 완료 여부

	public MessageEvent(
			org.pircbotx.hooks.events.PrivateMessageEvent<ManalithBot> event) {
		this.bot = event.getBot();
		this.user = event.getUser();
		this.channel = null;
		this.message = event.getMessage();

	}

	public MessageEvent(
			org.pircbotx.hooks.events.MessageEvent<ManalithBot> event) {
		this.bot = event.getBot();
		this.user = event.getUser();
		this.channel = event.getChannel();
		this.message = event.getMessage();
	}

	public ManalithBot getBot() {
		return bot;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getMessage() {
		return message;
	}

	public User getUser() {
		return user;
	}

	public boolean isExecuted() {
		return executed;
	}

	public void setExecuted(boolean executed) {
		this.executed = executed;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}