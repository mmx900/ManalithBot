/*
 	org.manalith.ircbot.plugin.sample/SamplePlugin.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2011, 2012  Ki-Beom, Kim
 	Copyright (C) 2012 Seong-ho, Cho <darkcircle.0426@gmail.com>

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
package org.manalith.ircbot.plugin.sample;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class PlainOldBotPlugin extends AbstractBotPlugin {

	@Override
	public String getName() {
		return "Sample Plugin";
	}

	@Override
	public String getCommands() {
		return "!hello";
	}

	public String getHelp() {
		return "!hello";
	}

	@Override
	public void onMessage(MessageEvent event) {
		onMessage(event, event.getChannel().getName());
	}

	protected void onMessage(MessageEvent event, String target) {
		// this.getBot() is deprecated. so we recommend as below.
		ManalithBot bot = event.getBot();
		String message = event.getMessage();
		String channel = event.getChannel().getName();

		if (message.equals("!hello")) {
			bot.sendLoggedMessage(channel, "Hello World!");

			event.setExecuted(true);
		}
	}
}
