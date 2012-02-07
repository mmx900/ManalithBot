/*
 	org.manalith.ircbot.plugin/AbstractBotPlugin.java
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
package org.manalith.ircbot.plugin;

import java.io.File;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.resources.MessageEvent;

public abstract class AbstractBotPlugin implements IBotPlugin {
	protected ManalithBot bot;

	public AbstractBotPlugin() {
	}

	public AbstractBotPlugin(ManalithBot bot) {
		this.bot = bot;
	}

	public void setBot(ManalithBot bot) {
		this.bot = bot;
	}

	public String getResourcePath() {
		StringBuilder builder = new StringBuilder();
		builder.append(System.getProperty("user.dir"));
		builder.append(File.separator);
		builder.append("data");
		builder.append(File.separator);
		builder.append(getClass().getName());
		builder.append(File.separator);
		return builder.toString();
	}

	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	public void onJoin(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub

	}

	public void onMessage(MessageEvent event) {
		// TODO Auto-generated method stub

	}

	public void onPrivateMessage(MessageEvent event) {
		// TODO Auto-generated method stub
	}
	
	protected void onMessage(MessageEvent event, String target)
	{
		// TODO !!!
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
