/*
 	org.manalith.ircbot.plugin.weather/WeatherPlugin.java
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
package org.manalith.ircbot.plugin.weather;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class WeatherPlugin extends AbstractBotPlugin {
	private Logger logger = Logger.getLogger(getClass());
	private static final String NAMESPACE = "!날씨";

	public WeatherPlugin(ManalithBot bot) {
		super(bot);
	}

	public String getName() {
		return "구글 날씨";
	}

	public String getCommands() {
		return NAMESPACE.substring(1);
	}

	public String getHelp() {
		return "사용법 : !날씨 [영문 지명]";
	}

	public void onMessage(MessageEvent event) {
		String command = NAMESPACE;// "!날씨";

		String message = event.getMessage();
		String channel = event.getChannel();
		if (message.equals(NAMESPACE + ":help")) {
			bot.sendLoggedMessage(channel, getHelp());
			event.setExecuted(true);
		} else if (message.equals(command)) {
			bot.sendLoggedMessage(channel, this.getHelp());// String.format("사용법 : %s [영문 지명]",
															// command));
			event.setExecuted(true);
		} else if (message.startsWith(command)
				&& message.length() >= command.length() + 2) {
			bot.sendLoggedMessage(channel,
					getGoogleWeather(message.substring(command.length() + 1)));
			event.setExecuted(true);
		}
		
		
	}

	public String getGoogleWeather(String keyword) {
		final String url = "http://www.google.com/ig/api?hl=ko&weather=";

		try {
			Document doc = Jsoup.connect(url + keyword).get();
			/*
			 * <current_conditions> <condition data="부분적으로 흐림"/> <temp_f
			 * data="84"/> <temp_c data="29"/> <humidity data="습도: 59%"/> <icon
			 * data="/ig/images/weather/partly_cloudy.gif"/> <wind_condition
			 * data="바람: 서풍, 10 km/h"/> </current_conditions>
			 */
			Elements conditions = doc.select("current_conditions");

			if (!conditions.isEmpty()) {
				String condition = conditions.select("condition").attr("data");
				int temp = Integer.parseInt(conditions.select("temp_c").attr(
						"data"));
				String humidity = conditions.select("humidity").attr("data");
				String wind_condition = conditions.select("wind_condition")
						.attr("data");

				return "[" + keyword + "] " + condition + ". 온도: " + temp
						+ "℃, " + humidity + ", " + wind_condition;
			} else {
				return "[" + keyword
						+ "] 지명을 찾을 수 없습니다. 영문 지명이 정확한지 다시 확인해주세요.";
			}

		} catch (IOException e) {
			logger.error(e);
		}

		return null;
	}

}
