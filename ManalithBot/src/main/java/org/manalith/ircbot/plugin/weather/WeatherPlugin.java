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
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.manalith.ircbot.annotation.Description;
import org.manalith.ircbot.annotation.NotNull;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.springframework.stereotype.Component;

@Component
public class WeatherPlugin extends SimplePlugin {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public String getName() {
		return "구글 날씨";
	}

	@Override
	public String getHelp() {
		return "지정한 지역의 날씨를 보여줍니다.";
	}

	@BotCommand("날씨")
	public String getYahooWeather(
			@Description("한글/영문 지명") @NotNull String keyword) {
		try {
			// TODO WOEID 로컬 캐싱
			final String url_woeid = "http://query.yahooapis.com/v1/public/yql"
					+ "?q=select%20woeid%20from%20geo.places%20where%20text%3D%22"
					+ URLEncoder.encode(keyword, "UTF-8")
					+ "%20ko-KR%22%20limit%201";
			final String url_forecast = "http://weather.yahooapis.com/forecastrss?w=%s&u=c";
			final String error_woeid = "23424868";

			Document doc = Jsoup.connect(url_woeid).get();
			// example : http://query.yahooapis.com/v1/public/yql?q=select woeid
			// from geo.places where text%3D\"서울%2C ko-KR\" limit 1
			String woeid = doc.select("woeid").text();

			if (!woeid.equals(error_woeid)) {
				// example:
				// http://weather.yahooapis.com/forecastrss?w=1132599&u=c
				doc = Jsoup.connect(String.format(url_forecast, woeid)).get();
				String location = doc.getElementsByTag("yweather:location")
						.attr("city");
				String condition = doc.getElementsByTag("yweather:condition")
						.attr("text");
				String temp = doc.getElementsByTag("yweather:condition").attr(
						"temp");
				String humidity = doc.getElementsByTag("yweather:atmosphere")
						.attr("humidity");
				String windCondition = doc.getElementsByTag("yweather:wind")
						.attr("speed");

				return String.format("[%s] %s 온도 %s℃, 습도 %s%%, 풍속 %skm/h",
						location, condition, temp, humidity, windCondition);

			} else {
				return String.format("[%s] 지명을 찾을 수 없습니다. 지명이 정확한지 다시 확인해주세요.",
						keyword);
			}

		} catch (IOException e) {
			logger.error(e);
			return "오류가 발생했습니다 : " + e.getMessage();
		}
	}
}
