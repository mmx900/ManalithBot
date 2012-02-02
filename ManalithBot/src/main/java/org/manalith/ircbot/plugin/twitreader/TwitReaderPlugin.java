/*
 	org.manalith.ircbot.plugin.twitreader/TwitReaderPlugin.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
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
package org.manalith.ircbot.plugin.twitreader;

import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEventData;

public class TwitReaderPlugin extends AbstractBotPlugin {

	public String getName() {
		return "트윗리더";
	}

	public String getCommands() {
		return null;
	}

	public String getHelp() {
		return "";
	}

	@Override
	public void onMessage(MessageEventData event) {
		String msg = event.getMessage();
		String channel = event.getChannel();

		String[] command = msg.split("\\s");

		TwitReaderRunner runner = new TwitReaderRunner(command);
		String /* [] */result = runner.run();

		if ( !result.equals("") )
		{
			bot.sendLoggedMessage(channel, result/* [i] */);
			// event.setExecuted(true);
		}
		
	}
}
