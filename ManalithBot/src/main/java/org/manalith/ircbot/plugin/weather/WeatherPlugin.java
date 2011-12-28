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
	private static final String NAMESPACE = "weather";

	public WeatherPlugin(ManalithBot bot) {
		super(bot);
	}

	public String getName() {
		return "구글 날씨";
	}

	public String getNamespace() {
		return NAMESPACE;
	}

	public String getHelp() {
		return "사용법 : !날씨 [영문 지명]";
	}

	public void onMessage(MessageEvent event) {
		String command = "!날씨";

		String message = event.getMessage();
		String channel = event.getChannel();
		if (message.equals(NAMESPACE + ":help")) {
			bot.sendLoggedMessage(channel, getHelp());
		} else if (message.equals(command)) {
			bot.sendLoggedMessage(channel,
					String.format("사용법 : %s [영문 지명]", command));
		} else if (message.startsWith(command)
				&& message.length() >= command.length() + 2) {
			bot.sendLoggedMessage(channel,
					getGoogleWeather(message.substring(command.length() + 1)));
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

				return "[" + keyword + "] " + condition + ". 온도 :" + temp
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
