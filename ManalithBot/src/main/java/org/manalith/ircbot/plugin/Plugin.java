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

import org.manalith.ircbot.resources.MessageEvent;

public interface Plugin {

	public void start() throws Exception;

	public void stop() throws Exception;

	/**
	 * 이름을 반환. 이름에 '플러그인'을 포함하지 않기를 권장함.
	 */
	public String getName();

	/**
	 * 사용되는 모든 명령어를 반환. 현재는 자동으로 출력되는 것을 사용하도록 권장.
	 */
	@Deprecated
	public String getCommands();

	/**
	 * 도움말을 반환. 현재는 getDescription()과 getUsage()로 나눠서 사용하기를 권장.
	 */
	@Deprecated
	public String getHelp();

	public String getUsage();

	public String getDescription();

	public void onJoin(String channel, String sender, String login,
			String hostname);

	public void onMessage(MessageEvent event);

	public void onPrivateMessage(MessageEvent event);

	public void onPart(String channel, String sender, String login,
			String hostname);

	public void onQuit(String sourceNick, String sourceLogin,
			String sourceHostname, String reason);

	public boolean test();
}
