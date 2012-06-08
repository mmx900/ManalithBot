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
import org.springframework.stereotype.Component;

@Component
public class WeatherPlugin extends AbstractBotPlugin {
	private Logger logger = Logger.getLogger(getClass());
	private static final String COMMAND = "!날씨";

	public String getName() {
		return "구글 날씨";
	}

	public String getCommands() {
		return COMMAND.substring(1);
	}

	public String getHelp() {
		return "사용법 : !날씨 [영문 지명]";
	}

	public void onMessage(MessageEvent event) {
		onMessage(event, event.getChannel().getName());

	}

	public void onPrivateMessage(MessageEvent event) {
		onMessage(event, event.getUser().getNick());
	}

	protected void onMessage(MessageEvent event, String target) {
		ManalithBot bot = event.getBot();
		String message = event.getMessage();

		if (message.equals(COMMAND + ":help")) {
			bot.sendLoggedMessage(target, getHelp());
			event.setExecuted(true);
		} else if (message.equals(COMMAND)) {
			bot.sendLoggedMessage(target, this.getHelp());
			event.setExecuted(true);
		} else if (message.startsWith(COMMAND)
				&& message.length() >= COMMAND.length() + 2) {
			bot.sendLoggedMessage(target,
					getGoogleWeather(message.substring(COMMAND.length() + 1)));
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
