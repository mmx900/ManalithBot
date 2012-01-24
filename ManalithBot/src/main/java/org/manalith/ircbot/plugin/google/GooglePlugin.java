/*
 	org.manalith.ircbot.plugin.google/GooglePlugin.java
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
package org.manalith.ircbot.plugin.google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.manalith.ircbot.ConfigurationManager;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class GooglePlugin extends AbstractBotPlugin {
	private Logger logger = Logger.getLogger(getClass());
	private static final String NAMESPACE = "구글";

	public GooglePlugin(ManalithBot bot) {
		super(bot);
	}

	public String getName() {
		return "Google";
	}

	public String getCommands() {
		return NAMESPACE;
	}

	public String getHelp() {
		return "사용법 : !구글 [키워드], !구글:match [키워드1] [키워드2]";
	}

	public void onMessage(MessageEvent event) {
		String message = event.getMessage();
		String channel = event.getChannel();

		if (message.equals(NAMESPACE + ":help")) {
			bot.sendLoggedMessage(channel, getHelp());
		} else if (message.length() >= 12
				&& message.substring(0, 9).equals("!구글:match ")) {
			String[] keywords = message.substring(9).split(" ");
			bot.sendLoggedMessage(channel,
					getGoogleMatch(keywords[0], keywords[1]));
		} else if (message.length() >= 4
				&& message.substring(0, 3).equals("구글 ")) {
			bot.sendLoggedMessage(channel,
					getGoogleTopResult(message.substring(3)));
		} else if (message.length() >= 5
				&& message.substring(0, 4).equals("!구글 ")) {
			bot.sendLoggedMessage(channel,
					getGoogleTopResult(message.substring(4)));
		}
		event.setExecuted(true);
	}

	private int getGoogleCount(String keyword) {
		try {
			// http://code.google.com/apis/websearch/docs/#fonje
			URL url = new URL(
					"https://ajax.googleapis.com/ajax/services/search/web?v=1.0&"
							+ "q="
							+ URLEncoder.encode(keyword, "UTF-8")
							+ "&key="
							+ ConfigurationManager
									.getInstance()
									.get("org.manalith.ircbot.bot.plugin.google.key")
							+ "&userip="
							+ InetAddress.getLocalHost().getHostAddress());
			URLConnection connection = url.openConnection();
			connection.addRequestProperty("Referer", "http://manalith.org");

			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			return Integer.parseInt(new JSONObject(builder.toString())
					.getJSONObject("responseData").getJSONObject("cursor")
					.getString("estimatedResultCount"));

		} catch (MalformedURLException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} catch (JSONException e) {
			logger.error(e);
		}

		return -1;

	}

	public String getGoogleMatch(String keyword1, String keyword2) {
		return getGoogleCount(keyword1) + " : " + getGoogleCount(keyword2);
	}

	public String getGoogleTopResult(String keyword) {
		try {
			// http://code.google.com/apis/websearch/docs/#fonje
			URL url = new URL(
					"https://ajax.googleapis.com/ajax/services/search/web?v=1.0&"
							+ "q="
							+ URLEncoder.encode(keyword, "UTF-8")
							+ "&key="
							+ ConfigurationManager
									.getInstance()
									.get("org.manalith.ircbot.bot.plugin.google.key")
							+ "&userip="
							+ InetAddress.getLocalHost().getHostAddress());
			URLConnection connection = url.openConnection();
			connection.addRequestProperty("Referer", "http://manalith.org");

			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			JSONObject firstResult = new JSONObject(builder.toString())
					.getJSONObject("responseData").getJSONArray("results")
					.getJSONObject(0);
			String result = firstResult.getString("content") + " : "
					+ firstResult.getString("url");

			// HTML 코드 처리
			result = result.replace("<b>", "[").replace("</b>", "]")
					.replace("&quot;", "\"").replace("&amp;", "&");

			return result;

		} catch (MalformedURLException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} catch (JSONException e) {
			logger.error(e);
		}

		return null;
	}
}
