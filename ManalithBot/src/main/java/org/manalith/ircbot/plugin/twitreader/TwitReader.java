/*
 	org.manalith.ircbot.plugin.twitreader/TwitReaderRunner.java
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;
import org.json.JSONObject;

public class TwitReader {
	private String[] str;

	private enum StrType {
		TwitURL, UserURL, ScrName, Unknown
	}

	public TwitReader() {
		this.setStrArray(new String[1]);
	}

	public TwitReader(String[] newStrArray) {
		this.setStrArray(newStrArray);
	}

	private void setStrArray(String[] newStrArray) {
		this.str = new String[newStrArray.length];
		int len = newStrArray.length;
		for (int i = 0; i < len; i++) {
			str[i] = newStrArray[i];
		}
	}

	private String[] getStrArray() {
		return this.str;
	}

	public String read() {

		String result = "";

		String[] strs = this.getStrArray();
		int len = strs.length;
		for (int i = 0; i < len; i++) {
			result = getText(strs[i], this.validateTwitterStr(strs[i]));
			if (!result.equals(""))
				break;
		}

		return result;
	}

	private String getText(String twitterurl, StrType type) {
		String result = "";

		if (type == StrType.TwitURL) {
			try {
				String[] PathnQuery = this.getJSONPathNQuery(twitterurl, type);
				URI uri = URIUtils.createURI("https", "api.twitter.com", -1,
						PathnQuery[0], PathnQuery[1], null);
				HttpGet get = new HttpGet(uri);

				DefaultHttpClient httpclient = new DefaultHttpClient();

				JSONObject obj = new JSONObject(new JSONTokener(
						(new BufferedReader(new InputStreamReader(httpclient
								.execute(get).getEntity().getContent())))
								.readLine()));

				String written_by = obj.getJSONObject("user").getString("name");
				String body = obj.getString("text");

				result = "작성자 : " + written_by + ", 본문 : " + body;
			} catch (Exception e) {
				result = e.getMessage();
				return result;
			}
		} else if (type == StrType.UserURL) {
			try {
				String[] PathnQuery = this.getJSONPathNQuery(twitterurl, type);
				URI uri = URIUtils.createURI("https", "api.twitter.com", -1,
						PathnQuery[0], PathnQuery[1], null);
				HttpGet get = new HttpGet(uri);

				DefaultHttpClient httpclient = new DefaultHttpClient();

				JSONArray arr = new JSONArray(new JSONTokener(
						(new BufferedReader(new InputStreamReader(httpclient
								.execute(get).getEntity().getContent())))
								.readLine()));

				if (arr.length() == 0) {
					result = "게시물이 존재하지 않습니다";
				} else {
					JSONObject obj = arr.getJSONObject(0);

					String written_datetime = obj.getString("created_at");
					String body = obj.getString("text");

					result = "작성시각 : "
							+ getDateTimeinKoreanFormat(written_datetime)
							+ ", 본문 : " + body;
				}
			} catch (NullPointerException e) {
				result = "페이지가 존재하지 않습니다";
				e.printStackTrace();
				return result;
			} catch (IOException ie) {
				result = ie.getMessage();
				ie.printStackTrace();
				return result;
			} catch (JSONException je) {
				result = je.getMessage();
				je.printStackTrace();
				return result;
			} catch (URISyntaxException urie) {
				result = urie.getMessage();
				urie.printStackTrace();
				return result;
			}
		}

		return result;
	}

	private StrType validateTwitterStr(String twitterurl) {
		StrType result = StrType.Unknown;

		if (twitterurl.equals(""))
			return result;

		Pattern twit_url_pattern = Pattern
				.compile("http(s)?\\:\\/\\/twitter\\.com\\/\\#\\!\\/[a-zA-Z0-9\\_]{1,15}\\/status\\/[0-9]+");
		Matcher twit_url_pattern_matcher = twit_url_pattern.matcher(twitterurl);

		Pattern user_url_pattern = Pattern
				.compile("http(s)?\\:\\/\\/twitter\\.com\\/(\\#\\!\\/)?([a-zA-Z0-9\\_]{1,15}(\\/)?){1}");
		Matcher user_url_pattern_matcher = user_url_pattern.matcher(twitterurl);

		if (twit_url_pattern_matcher.matches())
			result = StrType.TwitURL;
		else if (user_url_pattern_matcher.matches())
			result = StrType.UserURL;

		return result;
	}

	private String[] getJSONPathNQuery(String twitterurl, StrType type) {
		String[] json_requrl = new String[2];
		// [0] : path, [1] query, [2] path, [3] query

		if (type == StrType.TwitURL) {
			String[] split_url = twitterurl.split("\\/");
			String twit_id = split_url[split_url.length - 1];
			json_requrl[0] = "1/statuses/show.json";
			json_requrl[1] = "id=" + twit_id + "&include_entities=false";
		} else if (type == StrType.UserURL) {
			String[] userurl = twitterurl.split("\\/");
			String scrname = userurl[userurl.length - 1];
			json_requrl[0] = "1/statuses/user_timeline.json";
			json_requrl[1] = "include_entities=false&include_rts=true&screen_name="
					+ scrname + "&count=1";
		}

		return json_requrl;
	}

	private String getDateTimeinKoreanFormat(String datetime) {
		String result = "";
		String[] dayOfWeekKorean = { "일", "월", "화", "수", "목", "금", "토" };
		String[] month = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" };

		String[] split_datetime_by_space = datetime.split("\\s");

		int yyyy = 0;
		int MM = 0;
		int dd = 0;
		int hh = 0;
		int mm = 0;
		int ss = 0;
		String[] hhmmss;

		GregorianCalendar gdt = null;

		try {
			yyyy = Integer.parseInt(split_datetime_by_space[5]);
			MM = this.indexOf(month, split_datetime_by_space[1]);
			dd = Integer.parseInt(split_datetime_by_space[2]);

			hhmmss = split_datetime_by_space[3].split("\\:");

			hh = Integer.parseInt(hhmmss[0]);
			mm = Integer.parseInt(hhmmss[1]);
			ss = Integer.parseInt(hhmmss[2]);
		} catch (Exception e) {
			;
		}

		if (split_datetime_by_space[4].equals("+0000")) {
			try {

				gdt = new GregorianCalendar(yyyy, MM, dd);
				gdt.set(Calendar.ERA, GregorianCalendar.AD);
				gdt.setTimeZone(TimeZone.getTimeZone("UTC"));
				gdt.set(Calendar.HOUR, hh);
				gdt.set(Calendar.MINUTE, mm);
				gdt.set(Calendar.SECOND, ss);

				gdt.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
				gdt.add(Calendar.HOUR, 9);

			} catch (Exception e) {
				;
			}
		} else {
			try {
				gdt = new GregorianCalendar(yyyy, MM, dd);
				gdt.set(Calendar.ERA, GregorianCalendar.AD);
				gdt.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
				gdt.set(Calendar.HOUR, hh);
				gdt.set(Calendar.MINUTE, mm);
				gdt.set(Calendar.SECOND, ss);

				result += gdt.get(Calendar.YEAR) + "년 "
						+ (gdt.get(Calendar.MONTH) + 1) + "월 "
						+ gdt.get(Calendar.DAY_OF_MONTH) + "일 ";
				result += dayOfWeekKorean[gdt.get(Calendar.DAY_OF_WEEK) - 1]
						+ "요일 ";
				result += String.format("%02d", gdt.get(Calendar.HOUR_OF_DAY))
						+ ":" + String.format("%02d", gdt.get(Calendar.MINUTE))
						+ ":" + String.format("%02d", gdt.get(Calendar.SECOND));

			} catch (Exception e) {
				;
			}
		}

		result += gdt.get(Calendar.YEAR) + "년 " + (gdt.get(Calendar.MONTH) + 1)
				+ "월 " + gdt.get(Calendar.DAY_OF_MONTH) + "일 ";
		result += dayOfWeekKorean[gdt.get(Calendar.DAY_OF_WEEK) - 1] + "요일 ";
		result += String.format("%02d", gdt.get(Calendar.HOUR_OF_DAY)) + ":"
				+ String.format("%02d", gdt.get(Calendar.MINUTE)) + ":"
				+ String.format("%02d", gdt.get(Calendar.SECOND));

		return result;
	}

	private int indexOf(String[] str, String value) {
		int result = -1;
		int length = str.length;

		for (int i = 0; i < length; i++) {
			if (str[i].equals(value)) {
				result = i;
				break;
			}
		}

		return result;
	}
}