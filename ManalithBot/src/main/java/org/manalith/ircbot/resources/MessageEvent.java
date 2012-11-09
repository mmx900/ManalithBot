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

import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.ManalithBot;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class MessageEvent {
	private final GenericMessageEvent<ManalithBot> event;

	/**
	 * 실행 완료 여부
	 */
	private boolean executed;

	/**
	 * 재귀 호출 여부. 안전을 기하기 위해 외부 setter는 제공되지 않는다.
	 */
	private boolean recursive;

	/**
	 * 사용자가 보낸 메시지
	 */
	private String message;

	/**
	 * 사용자 메시지를 공백으로 쪼갠 배열
	 */
	private String[] segments;

	public MessageEvent(PrivateMessageEvent<ManalithBot> event) {
		this.event = event;
		setMessage(event.getMessage());
	}

	public MessageEvent(
			org.pircbotx.hooks.events.MessageEvent<ManalithBot> event) {
		this.event = event;
		setMessage(event.getMessage());
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

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
		segments = StringUtils.split(message);
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the segments
	 */
	public String[] getMessageSegments() {
		return segments;
	}

	public User getUser() {
		return event.getUser();
	}

	public boolean isExecuted() {
		return executed;
	}

	public void setExecuted(boolean executed) {
		this.executed = executed;
	}

	/**
	 * @return the recursive
	 */
	public boolean isRecursive() {
		return recursive;
	}

	/**
	 * 현재 상태로 이벤트를 재시작한다.
	 */
	public void restart() {
		recursive = true;
		getBot().invokeMessageEvent(this);
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
					.sendMessage(
							((org.pircbotx.hooks.events.MessageEvent<ManalithBot>) event)
									.getChannel().getName(), response);
		} else {
			event.getBot().sendMessage(event.getUser().getNick(), response);
		}
	}
}