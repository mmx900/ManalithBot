/*
 	org.manalith.ircbot.plugin/IBotPlugin.java
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

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.resources.MessageEventData;

public interface IBotPlugin {
	public void setBot(ManalithBot bot);

	public String getName();

	public String getCommands();

	public String getHelp();

	public void onJoin(MessageEventData event);
	
	public void onAction(MessageEventData event);

	public void onMessage(MessageEventData event);

	public void onPrivateMessage(MessageEventData event);

	public void onPart(MessageEventData event);

	public void onQuit(MessageEventData event);
}
