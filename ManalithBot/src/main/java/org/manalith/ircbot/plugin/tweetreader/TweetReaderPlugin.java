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
package org.manalith.ircbot.plugin.tweetreader;

import org.apache.commons.configuration.ConfigurationException;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class TweetReaderPlugin extends AbstractBotPlugin {

	private TweetReader reader;

	private String consumerKey;
	private String consumerSecret;
	private String username;
	private String password;

	public String getName() {
		return "트윗리더";
	}

	public String getCommands() {
		return null;
	}

	public String getHelp() {
		return "";
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void onMessage(MessageEvent event) {
		String[] command = event.getMessageSegments();

		try {
			reader = new TweetReader(getResourcePath(), consumerKey,
					consumerSecret);
		} catch (ConfigurationException e) {
			event.respond(e.getMessage());
			return;
		}

		reader.setTwitterUsernameOrEmail(username);
		reader.setTwitterPassword(password);

		String result = reader.read(command);

		if (result != null) {
			event.respond(result);
		}
	}
}
