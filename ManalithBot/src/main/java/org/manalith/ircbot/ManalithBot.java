/*
 	org.manalith.ircbot/ManalithBot.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2005, 2011  Ki-Beom, Kim
 	Copyright (C) 2011, 2012  Seong-ho, Cho <darkcircle.0426@gmail.com>

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

package org.manalith.ircbot;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import org.pircbotx.PircBotX;
//import org.pircbotx.hooks.Event;
//import org.pircbotx.hooks.events.ConnectEvent;

//import org.jibble.pircbot.DccChat;
//import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
//import org.jibble.pircbot.PircBot;
//import org.jibble.pircbot.User;
//import org.manalith.ircbot.command.CommandParser;
import org.manalith.ircbot.plugin.IBotPlugin;
import org.manalith.ircbot.plugin.PluginManager;

//import org.manalith.ircbot.plugin.relay.RelayPlugin;

public class ManalithBot extends PircBotX {

	private Logger logger;

	// TODO 닉 도용 가능성이 있으니 로그인 기능을 만들 것
	// private static final String[] owners = {"setzer"};
	private final String[] owners = {};
	private PluginManager pluginManager = new PluginManager();

	/**
	 * Constructor.
	 * @param plugins : AbstractBotPlugin implements IBotPlugin
	 */
	public ManalithBot(List<IBotPlugin> plugins) {
		for (IBotPlugin plugin : plugins) {
			addPlugin(plugin);
		}
		
		logger = Logger.getLogger(getClass());
	}
	
	public Logger getLogger ()
	{
		return this.logger;
	}
	
	public String [] getOwners()
	{
		return this.owners;
	}

	/**
	 * setNickname(name) is wrapper method for PircBotX.setName(String name)
	 * @param name
	 * @throws NickAlreadyInUseException
	 * @throws IOException
	 * @throws IrcException
	 */
	public void setNickname(String name) throws NickAlreadyInUseException,
			IOException, IrcException {
		this.setName(name);
	}

	/**
	 * addPlugin(plugin) is plugin adder for ManalithBot
	 * 
	 * @param plugin : as AbstractBotPlugin implements IBotPlugin 
	 */
	private void addPlugin(IBotPlugin plugin) {
		pluginManager.add(plugin);
		plugin.setBot(this);
	}
	
	/**
	 * getPluginManager() returns pluginManager
	 */
	public PluginManager getPluginManager()
	{
		return this.pluginManager;
	}

	/**
	 * sendMessage(target, message)가 final 메서드이므로 로깅을 위해 이 메시지를 사용한다.
	 * 
	 * @param target : channel or user
	 * @param message : message string
	 */
	public void sendLoggedMessage(String target, String message,
			boolean redirectToRelayBot) {
		logger.trace(String.format("MESSAGE(LOCAL) : %s / %s", target, message));

		sendMessage(target, message);

		// if(redirectToRelayBot && RelayPlugin.RELAY_BOT != null)
		// RelayPlugin.RELAY_BOT.sendMessage(target, message);
	}

	public void sendLoggedMessage(String target, String message) {
		sendLoggedMessage(target, message, true);
	}

//	private void sendMessage(Message m) {
//		// 너무 긴 문자는 자른다.
//		if (m.getMessage().length() > 180)
//			m.setMessage(m.getMessage().substring(0, 179));
//		sendMessage(m.getChannel(), m.getSender() + ", " + m.getMessage());
//	}

	
//	private boolean isOwner(String nick) {
//		// TODO nick(String) 비교가 아닌 User 비교로
//		// below method is moved into ManalithBotEvent
//		boolean result = false;
//		for (String s : owners) {
//			if (s.equals(nick)) {
//				result = true;
//				break;
//			}
//		}
//		return result;
//	}

}