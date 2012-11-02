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

import org.manalith.ircbot.resources.MessageEvent;
import org.osgi.framework.BundleContext;

public abstract class AbstractBotPlugin implements IBotPlugin {
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

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public void onJoin(String channel, String sender, String login,
			String hostname) {

	}

	@Override
	public void onMessage(MessageEvent event) {

	}

	@Override
	public void onPrivateMessage(MessageEvent event) {

	}

	@Override
	public void onPart(String channel, String sender, String login,
			String hostname) {

	}

	@Override
	public void onQuit(String sourceNick, String sourceLogin,
			String sourceHostname, String reason) {

	}

	@Override
	public void start(BundleContext arg0) throws Exception {

	}

	@Override
	public void stop(BundleContext arg0) throws Exception {

	}

	@Override
	public boolean test() {
		return true;
	}

}
