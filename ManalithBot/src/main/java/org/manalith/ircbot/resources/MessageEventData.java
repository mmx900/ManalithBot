/*
 *  Created on 2005. 7. 26
 	org.manalith.ircbot.resources/MessageEventData.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2005  Ki-Beom, Kim
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

package org.manalith.ircbot.resources;

import org.manalith.ircbot.ManalithBot;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.QuitEvent;

public class MessageEventData {
	private final String channel;
	private final String sender;
	private final String login;
	private final String hostname;
	private final String action;
	private final String target;

	private String message;
	private boolean executed; // 실행 완료 여부
	
	private Event<ManalithBot> event;

	// This is for Join event
	public MessageEventData( JoinEvent<ManalithBot> event)
	{
		this.channel = event.getChannel().getName();
		this.sender = event.getUser().getNick();
		this.login = event.getUser().getLogin();
		this.hostname = event.getUser().getServer();
		this.message = null;
		this.action = null;
		this.target = null;
		
		this.setEvent ( event );
	}
	
	// This is for Action event
	public MessageEventData ( ActionEvent<ManalithBot> event) 
	{
		/*
		 * String sender = event.getUser().getNick();
		 * String login = event.getUser().getLogin();
		 * String hostname = event.getUser().getServer();
		 * String target = event.getChannel().getName();
		 * String action = event.getAction();
		 */ // do not delete this code
		
		this.channel = null;
		this.sender = event.getUser().getNick();
		this.login = event.getUser().getLogin();
		this.hostname = event.getUser().getServer();
		this.target = event.getChannel().getName();
		this.action = event.getAction();
		this.message = null;
		
		this.setEvent(event);
	}
	
	// This is for PrivateMessage event
	public MessageEventData(PrivateMessageEvent<ManalithBot> event) {
		this.channel = null;
		this.sender = event.getUser().getNick();
		this.login = event.getUser().getLogin();
		this.hostname = event.getUser().getServer();
		this.message = event.getMessage();
		this.action = null;
		this.target = null;
		
		this.setEvent ( event );
	}

	// This is for (public) Message event
	public MessageEventData(MessageEvent<ManalithBot> event) {
		this.channel = event.getChannel().getName();
		this.sender = event.getUser().getNick();
		this.login = event.getUser().getLogin();
		this.hostname = event.getUser().getServer();
		this.message = event.getMessage();
		this.action = null;
		this.target = null;
		
		this.setEvent ( event );
	}
	
	// this is for Part event
	public MessageEventData(PartEvent<ManalithBot> event) {
		this.channel = event.getChannel().getName();
		this.sender = event.getUser().getNick();
		this.login = event.getUser().getLogin();
		this.hostname = event.getUser().getServer();
		this.message = null;
		this.action = null;
		this.target = null;
		
		this.setEvent ( event );
	}
	
	// this is for Quit event
	public MessageEventData(QuitEvent<ManalithBot> event)
	{
		/*
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();
		String reason = event.getReason();
		*/
		this.channel = null;
		this.sender = event.getUser().getNick();
		this.login = event.getUser().getLogin();
		this.hostname = event.getUser().getServer();
		this.message = event.getReason();
		this.action = null;
		this.target = null;
		
		this.setEvent(event);
	}
	
	private void setEvent( Event<ManalithBot> newEvent )
	{
		this.event = newEvent; 
	}
	
	public Event<ManalithBot> getEvent () 
	{
		return this.event;
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
	
	public String getTarget()
	{
		return target;
	}
	
	public String getAction()
	{
		return action;
	}
	
	public User[] getUsers()
	{
		return ( this.channel != null ) ? (User[])((MessageEvent<ManalithBot>)this.getEvent()).getChannel().getUsers().toArray() : null;
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