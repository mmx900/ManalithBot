/*
 	org.manalith.ircbot.plugin/AbstractBotPlugin.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2011,2012  Ki-Beom, Kim
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
package org.manalith.ircbot.plugin;

import java.io.File;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.resources.MessageEvent;

public abstract class AbstractBotPlugin implements IBotPlugin {
	@Deprecated
	protected ManalithBot bot;

	public AbstractBotPlugin() {

	}

	public AbstractBotPlugin(ManalithBot bot) {
		this.bot = bot;
	}

	@Deprecated
	public void setBot(ManalithBot bot) {
		this.bot = bot;
	}

	@Deprecated
	public ManalithBot getBot() {
		return bot;
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
		return null;
	}

	public void onJoin(String channel, String sender, String login,
			String hostname) {

	}

	public void onMessage(MessageEvent event) {

	}

	public void onPrivateMessage(MessageEvent event) {

	}
	//*
	public void onPart(MessageEvent event) {

	}

	public void onQuit(MessageEvent event) {

	}
	//*/

}
