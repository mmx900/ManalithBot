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
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.QuitEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.hooks.Event;

public class MessageEvent {
	// private final GenericMessageEvent<ManalithBot> event;
	// private final GenericEvent<ManalithBot> event;
	private final Event<ManalithBot> event;

	private boolean executed; // 실행 완료 여부

	public MessageEvent(
			org.pircbotx.hooks.events.PrivateMessageEvent<ManalithBot> event) {
		this.event = event;
	}

	public MessageEvent(
			org.pircbotx.hooks.events.MessageEvent<ManalithBot> event) {
		this.event = event;
	}
	
	public MessageEvent(
			org.pircbotx.hooks.events.PartEvent<ManalithBot> event) {
		this.event = event;
	}
	
	public MessageEvent(
			org.pircbotx.hooks.events.QuitEvent<ManalithBot> event) {
		this.event = event;
	}

	public ManalithBot getBot() {
		return event.getBot();
	}

	public Channel getChannel() {
		if (event instanceof org.pircbotx.hooks.events.MessageEvent)
			return ((org.pircbotx.hooks.events.MessageEvent<ManalithBot>) event)
					.getChannel();
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	public String getMessage() {
		if ( event instanceof org.pircbotx.hooks.events.MessageEvent)
			return ((GenericMessageEvent<ManalithBot>)event).getMessage();
		else
			return "";
	}

	@SuppressWarnings("unchecked")
	public User getUser() {
		if ( event instanceof org.pircbotx.hooks.events.MessageEvent)
			return ((GenericMessageEvent<ManalithBot>)event).getUser();
		else if ( event instanceof org.pircbotx.hooks.events.PartEvent)
			return ((PartEvent<ManalithBot>)event).getUser();
		else if ( event instanceof org.pircbotx.hooks.events.QuitEvent)
			return ((QuitEvent<ManalithBot>)event).getUser();
		else 
			return null;
	}
	
	public String getReason()
	{
		if ( event instanceof PartEvent )
			return ((PartEvent<ManalithBot>)event).getReason();
		else if ( event instanceof QuitEvent )
			return ((QuitEvent<ManalithBot>)event).getReason();
		else 
			return "";
	}

	public boolean isExecuted() {
		return executed;
	}

	public void setExecuted(boolean executed) {
		this.executed = executed;
	}

	/**
	 * 이벤트의 형식에 따라 발신 채널(OnMessage) 혹은 개인(OnPrivateMessage)에게 응답을 보낸다.
	 * 
	 * @param response
	 *            응답 메시지
	 */
	public void respond(String response) {
		if (event instanceof org.pircbotx.hooks.events.MessageEvent) {
			event.getBot()
					.sendLoggedMessage(
							((org.pircbotx.hooks.events.MessageEvent<ManalithBot>) event)
									.getChannel().getName(), response);
		} else {
			event.getBot().sendLoggedMessage(this.getUser().getNick(),
					response);
		}
	}
}