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
import java.nio.charset.Charset;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.json.JSONException;
import org.json.JSONObject;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GooglePlugin extends AbstractBotPlugin {
	private Logger logger = Logger.getLogger(getClass());
	private static final String NAMESPACE = "!구글";
	private static final String HIGH_INTENSITY = "\u0002";
	private static final String LOW_INTENSITY = "\u000f";
	// private static final String HIGH_INTENSITY = "\u001B[1m";
	// private static final String LOW_INTENSITY = "\u001B[2m";
	private String apiKey;
	private String apiReferer;

	public String getName() {
		return "Google";
	}

	public String getCommands() {
		return NAMESPACE;
	}

	public String getHelp() {
		return "설 명: 구글 검색 결과를 보여줍니다, 사용법: !구글 [키워드], !gg [키워드], !구글:match [키워드1] [키워드2], !gg:match [키워드1] [키워드2]";
	}

	public void onMessage(MessageEvent event) {
		String message = event.getMessage();

		if (message.equals(NAMESPACE + ":help")) {
			event.respond(getHelp());
		} else if (message.length() >= 12
				&& (message.substring(0, 9).equals(NAMESPACE + ":match ") || message
						.substring(0, 9).equals("!gg:match "))) {
			String[] keywords = message.substring(9).split(" ");
			event.respond(getGoogleMatch(keywords[0], keywords[1]));
		} else if (message.length() >= 5
				&& (message.substring(0, 4).equals(NAMESPACE + " ") || message
						.substring(0, 4).equals("!gg "))) {
			event.respond(getGoogleTopResult(message.substring(4)));
		}

	}

	private int getGoogleCount(String keyword) {
		try {
			// http://code.google.com/apis/websearch/docs/#fonje
			URL url = new URL(
					"https://ajax.googleapis.com/ajax/services/search/web?v=1.0&"
							+ "q=" + URLEncoder.encode(keyword, "UTF-8")
							+ "&key=" + apiKey + "&userip="
							+ InetAddress.getLocalHost().getHostAddress());
			URLConnection connection = url.openConnection();
			connection.addRequestProperty("Referer", apiReferer);

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
			final String url = "https://ajax.googleapis.com/ajax/services/search/web?v=1.0" //
					+ "&q=" + keyword //
					+ "&key=" + apiKey //
					+ "&userip=" + InetAddress.getLocalHost().getHostAddress();

			MappingJacksonHttpMessageConverter conv = new MappingJacksonHttpMessageConverter();
			conv.setSupportedMediaTypes(Collections
					.singletonList(new MediaType("text", "javascript", Charset
							.forName("UTF-8"))));

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(conv);

			SearchResponse res = restTemplate.getForObject(url,
					SearchResponse.class);

			if (res.responseData != null
					&& ArrayUtils.isNotEmpty(res.responseData.results)) {

				SearchResult result = res.responseData.results[0];

				// HTML 코드 처리
				return result.content.replace("<b>", HIGH_INTENSITY)
						.replace("</b>", LOW_INTENSITY).replace("&quot;", "\"")
						.replace("&amp;", "&").replace("&#39;", "'")
						.replace("&gt;", ">").replace("&lt;", "<")
						+ " : " + result.url;
			}
		} catch (IOException e) {
			logger.warn(e);
		}

		return null;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiReferer() {
		return apiReferer;
	}

	public void setApiReferer(String apiReferer) {
		this.apiReferer = apiReferer;
	}

	@SuppressWarnings("unused")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class SearchResponse {
		public SearchResponseData responseData;
		public int responseStatus;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class SearchResponseData {
		public SearchResult[] results;
	}

	@SuppressWarnings("unused")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class SearchResult {
		public String GsearchResultClass;
		public String unescapedUrl;
		public String url;
		public String visibleUrl;
		public String cacheUrl;
		public String title;
		public String titleNoFormatting;
		public String content;
	}
}
