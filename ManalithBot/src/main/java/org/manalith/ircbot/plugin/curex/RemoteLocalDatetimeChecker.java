/*
 	org.manalith.ircbot.plugin.curex/RemoteLocalDatetimeChecker.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2011  Seong-ho, Cho <darkcircle.0426@gmail.com>

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
package org.manalith.ircbot.plugin.curex;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.manalith.ircbot.plugin.curex.exceptions.FileDoesntSpecifiedException;

public class RemoteLocalDatetimeChecker {

	private String LocalPath;
	private String LocalFilename;
	private SimpleDateFormat sdf;

	public RemoteLocalDatetimeChecker() {
		this("", "");
	}

	public RemoteLocalDatetimeChecker(String newLocalFilename) {
		this("", newLocalFilename);
	}

	public RemoteLocalDatetimeChecker(String newLocalPath,
			String newLocalFilename) {
		this.setLocalPath(newLocalPath);
		this.setLocalFilename(newLocalFilename);
		sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREAN);
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public void setLocalPath(String newLocalPath) {
		this.LocalPath = newLocalPath;
	}

	public String getLocalPath() {
		return this.LocalPath;
	}

	public void setLocalFilename(String newLocalFilename) {
		this.LocalFilename = newLocalFilename;
	}

	public String getLocalFilename() {
		return this.LocalFilename;
	}

	public DateTimeRound checkLatestUpdatedLocalDateandTime()
			throws ConfigurationException, FileDoesntSpecifiedException,
			ParseException {
		DateTimeRound result = new DateTimeRound();
		GregorianCalendar tCalendar = new GregorianCalendar();
		tCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

		if (this.getLocalFilename().equals(""))
			throw new FileDoesntSpecifiedException();

		PropertiesConfiguration prop = new PropertiesConfiguration(
				this.getLocalPath() + this.getLocalFilename());

		if (StringUtils.isEmpty(prop.getString("date"))) {
			tCalendar.setTime(new Date(1L));
			result.setCalendar(tCalendar);
		} else {
			System.out.println((String) prop.getString("date"));
			Date localDate = sdf.parse((String) prop.getString("date"));

			tCalendar.setTime(localDate);

			result.setCalendar(tCalendar);
		}

		if (StringUtils.isEmpty(prop.getString("round"))) {
			result.setRoundVal(0);
		} else {
			result.setRoundVal(prop.getInt("round"));
		}

		return result;
	}

	public DateTimeRound checkLatestNoticeDateandTime()
			throws FileNotFoundException, IOException, ParseException {

		DateTimeRound result = new DateTimeRound();

		String url = "http://info.finance.naver.com/marketindex/";
		Connection conn = Jsoup
				.connect(url)
				.userAgent(
						"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.43 Safari/537.31");
		Elements exchange_info = conn.get().select("div.graph_info");

		System.out.println(exchange_info.select("span.time").get(0).text());
		Date remoteDate = sdf.parse(exchange_info.select("span.time").get(0)
				.text(), new ParsePosition(0));

		GregorianCalendar tCalendar = new GregorianCalendar();
		tCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		tCalendar.setTime(remoteDate);

		result.setCalendar(tCalendar);

		result.setRoundVal(NumberUtils.toInt(exchange_info.select("span.num")
				.get(0).text()));	return result;
	}
}
