package org.manalith.ircbot.plugin.google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.manalith.ircbot.ConfigurationManager;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class GooglePlugin extends AbstractBotPlugin {
	private Logger logger = Logger.getLogger(getClass());
	private static final String NAMESPACE = "gg";

	public GooglePlugin(ManalithBot bot) {
		super(bot);
	}

	public String getName() {
		return "Google";
	}

	public String getNamespace() {
		return NAMESPACE;
	}

	public String getHelp() {
		return "사용법 : gg [키워드], gg:match [키워드1] [키워드2]";
	}

	public void onMessage(MessageEvent event) {
		String message = event.getMessage();
		String channel = event.getChannel();

		if (message.equals(NAMESPACE + ":help")) {
			bot.sendLoggedMessage(channel, getHelp());
		} else if (message.length() >= 12
				&& message.substring(0, 9).equals("gg:match ")) {
			String[] keywords = message.substring(9).split(" ");
			bot.sendLoggedMessage(channel,
					getGoogleMatch(keywords[0], keywords[1]));
		} else if (message.length() >= 4
				&& message.substring(0, 3).equals("gg ")) {
			bot.sendLoggedMessage(channel,
					getGoogleTopResult(message.substring(3)));
		} else if (message.length() >= 5
				&& message.substring(0, 4).equals("!gg ")) {
			bot.sendLoggedMessage(channel,
					getGoogleTopResult(message.substring(4)));
		}
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
		// google.co.kr은 검색 결과가 다르다. (최상단의 핫링크에 대한 처리 필요)
		final String url = "http://www.google.com/search?hl=ko";

		try {
			// google.co.kr은 User-Agent가 Mozilla 계열로 감지되어야만 UTF-8 문서를 반환하며 다른
			// 헤더들은 무시된다.
			Document doc = Jsoup
					.connect(url)
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:8.0) Gecko/20100101 Firefox/8.0")
					.header("Accept-Charset", "utf-8")
					.header("Accept-Language", "ko-kr, ko;q=0.8")
					.header("Content-type", "text/html; charset=UTF-8")
					.data("q", keyword).get();

			Elements topResult = doc.select("#rso>li:eq(0)");

			if (!topResult.isEmpty()) {
				Elements titleElements = topResult.select("h3:eq(0)>a");
				String title = titleElements.text();
				String resultUrl = titleElements.attr("href");

				String summary = topResult.select("span.st").text();

				String result = "[" + title + "] " + summary + " - "
						+ resultUrl;

				// HTML 코드 처리
				// result = result.replace("<em>", "[").replace("</em>", "]")
				// .replace("<b>", "").replace("</b>", "")
				// .replace("<br />", "").replace("&quot;", "\"")
				// .replace("&amp;", "&").replace("&middot;", "-");

				// 길이 점검
				result = StringUtils.substring(result, 0,
						bot.getMaxLineLength() / 3);

				return result;
			} else {
				return String.format("[%s] 검색 결과가 없습니다.", keyword);
			}

		} catch (IOException e) {
			logger.error(e);
		}

		return null;
	}
}
