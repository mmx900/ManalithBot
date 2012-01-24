/*
 	org.manalith.ircbot.plugin.cer2/RemoteLocalDatetimeChecker.java
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
package org.manalith.ircbot.plugin.cer2;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import org.manalith.ircbot.common.PropertyManager;
import org.manalith.ircbot.plugin.cer2.exceptions.FileDoesntSpecifiedException;

public class RemoteLocalDatetimeChecker {

	private String LocalPath;
	private String LocalFilename;

	public RemoteLocalDatetimeChecker() {
		this.setLocalPath("");
		this.setLocalFilename("");
	}

	public RemoteLocalDatetimeChecker(String newLocalFilename) {
		this.setLocalPath("");
		this.setLocalFilename(newLocalFilename);
	}

	public RemoteLocalDatetimeChecker(String newLocalPath,
			String newLocalFilename) {
		this.setLocalPath(newLocalPath);
		this.setLocalFilename(newLocalFilename);
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
			throws IOException, FileDoesntSpecifiedException {
		DateTimeRound result = new DateTimeRound();

		if (this.getLocalFilename().equals(""))
			throw new FileDoesntSpecifiedException();

		PropertyManager prop = new PropertyManager(this.getLocalPath(),
				this.getLocalFilename());

		prop.loadProperties();

		if (prop.getValue("date") == null || prop.getValue("date").equals("")) {
			GregorianCalendar tCalendar = new GregorianCalendar();
			tCalendar.setTime(new Date(1L));
			result.setCalendar(tCalendar);
		} else {
			String[] dateandtime = ((String) prop.getValue("date"))
					.split("\\s");

			String[] YYMMDD = dateandtime[0].split("\\.");
			String[] hhmm = dateandtime[1].split("\\:");

			int year = Integer.parseInt(YYMMDD[0]);
			int month = Integer.parseInt(YYMMDD[1]);
			int date = Integer.parseInt(YYMMDD[2]);

			int hour = Integer.parseInt(hhmm[0]);
			int minute = Integer.parseInt(hhmm[1]);

			GregorianCalendar tCalendar = new GregorianCalendar(year,
					month - 1, date, hour, minute);
			tCalendar.set(GregorianCalendar.ZONE_OFFSET, 32400000);
			tCalendar.set(GregorianCalendar.ERA, 1);
			tCalendar.set(GregorianCalendar.DST_OFFSET, 0);
			tCalendar.set(GregorianCalendar.MILLISECOND, 1);

			result.setCalendar(tCalendar);
		}

		if (prop.getValue("round") == null) {
			result.setRoundVal(0);
		} else {
			String temp = (String) prop.getValue("round");
			result.setRoundVal(Integer.parseInt(temp));
		}

		return result;
	}

	public DateTimeRound checkLatestNoticeDateandTime()
			throws FileNotFoundException, IOException // ,
														// EmptyTokenStreamException
	{
		DateTimeRound result = new DateTimeRound();

		String url = "http://info.finance.naver.com/marketindex/exchangeMain.nhn";
		Element exchange_info = Jsoup.connect(url).get()
				.select("div.exchange_info").get(0);

		String dateStr = exchange_info.select("span.date").get(0).text();

		String[] datetime = dateStr.split("\\s");
		String[] date = datetime[0].split("\\.");
		String[] time = datetime[1].split("\\:");

		int year = Integer.parseInt(date[0]);
		int month = Integer.parseInt(date[1]);
		int day = Integer.parseInt(date[2]);

		int hour = Integer.parseInt(time[0]);
		int minute = Integer.parseInt(time[1]);

		GregorianCalendar tCalendar = new GregorianCalendar(year, month - 1,
				day, hour, minute);
		tCalendar.set(GregorianCalendar.ZONE_OFFSET, 32400000);
		tCalendar.set(GregorianCalendar.ERA, 1);
		tCalendar.set(GregorianCalendar.DST_OFFSET, 0);
		tCalendar.set(GregorianCalendar.MILLISECOND, 1);

		result.setCalendar(tCalendar);

		String roundStr = exchange_info.select("span.round").get(0)
				.select("em").text();
		result.setRoundVal(Integer.parseInt(roundStr));

		return result;
	}

}
