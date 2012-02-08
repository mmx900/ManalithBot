/*
 	org.manalith.ircbot.plugin/IBotPlugin.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2005, 2011, 2012  Ki-Beom, Kim
 	Copyright (C) 2012  Seong-ho, Cho  <darkcircle.0426@gmail.com>

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

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.resources.MessageEvent;

public interface IBotPlugin {
	public void setBot(ManalithBot bot);

	public String getName();

	public String getCommands();

	public String getHelp();

	public void onJoin(String channel, String sender, String login,
			String hostname);

	public void onMessage(MessageEvent event);

	public void onPrivateMessage(MessageEvent event);

	public void onPart(String channel, String sender, String login,
			String hostname);

	public void onQuit(String sourceNick, String sourceLogin,
			String sourceHostname, String reason);
}
