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

public class MessageEvent {
	private final String channel;
	private final String sender;
	private final String login;
	private final String hostname;

	private String message;
	private boolean executed; // 실행 완료 여부

	// This is for PrivateMessage
	public MessageEvent(String sender, String login, String hostname,
			String message) {
		this.channel = null;
		this.sender = sender;
		this.login = login;
		this.hostname = hostname;
		this.message = message;
	}

	public MessageEvent(String channel, String sender, String login,
			String hostname, String message) {
		this.channel = channel;
		this.sender = sender;
		this.login = login;
		this.hostname = hostname;
		this.message = message;
	}

	public String getChannel() {
		return channel;
	}

	public String getHostname() {
		return hostname;
	}

	public String getLogin() {
		return login;
	}

	public String getMessage() {
		return message;
	}

	public String getSender() {
		return sender;
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