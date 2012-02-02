/*
 	org.manalith.ircbot.plugin/PluginManager.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2005, 2011  Ki-Beom, Kim

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
package org.manalith.ircbot.plugin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.resources.MessageEventData;

import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.QuitEvent;

public class PluginManager {
	private List<IBotPlugin> list = new ArrayList<IBotPlugin>();

	public void add(IBotPlugin plugin) {
		list.add(plugin);
	}

	public void remove(IBotPlugin plugin) {
		list.remove(plugin);
	}

	public void onJoin(JoinEvent<ManalithBot> event) {
		/*
		String channel = event.getChannel().getName();
		String sender = event.getUser().getNick();
		String login = event.getUser().getLogin();
		String hostName = event.getUser().getServer();
		*/ // do not delete for recovery
		
		MessageEventData msg = new MessageEventData ( event );
		
		for (IBotPlugin plugin : list)
			plugin.onJoin(msg);
	}
	
	public void onAction (ActionEvent<ManalithBot> event)
	{
		MessageEventData msg = new MessageEventData(event);
		
		for ( IBotPlugin plugin : list )
		{
			plugin.onAction(msg);
			if ( msg.isExecuted() )
				break;
		}
	}

	public void onMessage(MessageEvent<ManalithBot> event) {
		/*
		String channel = event.getChannel().getName();
		String sender = event.getUser().getNick();
		String login = event.getUser().getLogin();
		String hostName = event.getUser().getServer();
		String message = event.getMessage();
		*/ // do not delete for recovery
		
		MessageEventData msg = new MessageEventData(event);

		for (IBotPlugin plugin : list) {
			plugin.onMessage(msg);
			if (msg.isExecuted())
				break;
		}
	}

	public void onPrivateMessage(PrivateMessageEvent<ManalithBot> event) {
		/*
		String sender = event.getUser().getNick();
		String login = event.getUser().getLogin();
		String hostName = event.getUser().getServer();
		String message = event.getMessage();
		*/ // do not delete for recovery
		
		MessageEventData msg = new MessageEventData(event);

		for (IBotPlugin plugin : list) {
			plugin.onPrivateMessage(msg);
			if (msg.isExecuted())
				break;
		}
	}

	public void onPart(PartEvent<ManalithBot> event) {
		/*
		String channel = event.getChannel().getName();
		String sender = event.getUser().getNick();
		String login = event.getUser().getLogin();
		String hostName = event.getUser().getServer();
		//*/
		
		MessageEventData msg = new MessageEventData ( event );
		
		for (IBotPlugin plugin : list)
		{
			plugin.onPart(msg);
		}
	}
	
	public void onQuit (QuitEvent<ManalithBot> event)
	{
		/*
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();
		String reason = event.getReason();
		*/
		
		MessageEventData msg = new MessageEventData ( event );
		
		for ( IBotPlugin plugin : list )
		{
			plugin.onQuit(msg);
		}
	}

	public List<IBotPlugin> getList() {
		return list;
	}

	public String getPluginInfo() {
		StringBuilder sb = new StringBuilder();
		String name = "";
		int i = 0; // To make well-formed message
		for (IBotPlugin p : list) {
			name = p.getName();
			if (name != null) {
				if (i != 0)
					sb.append(", "); // To make well-formed message
				else
					i++; //

				sb.append(name);

				String commands = p.getCommands();
				if (StringUtils.isNotBlank(commands)) {
					sb.append("(" + commands + ")");
				}
			}
		}

		return sb.toString();
	}
}
