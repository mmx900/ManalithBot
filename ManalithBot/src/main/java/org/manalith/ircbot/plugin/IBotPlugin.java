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
	@Deprecated
	public void setBot(ManalithBot bot);

	/**
	 * 이 메서드는 PircBotX 구조에 적합하지 않다. 따라서 MessageEvent.getBot() 을 대신 사용해야 한다.
	 * @return
	 */
	@Deprecated
	public ManalithBot getBot();

	public String getName();

	public String getCommands();

	public String getHelp();

	public void onJoin(String channel, String sender, String login,
			String hostname);

	public void onMessage(MessageEvent event);

	public void onPrivateMessage(MessageEvent event);

	public void onPart(MessageEvent event);

	public void onQuit(MessageEvent event);
}
