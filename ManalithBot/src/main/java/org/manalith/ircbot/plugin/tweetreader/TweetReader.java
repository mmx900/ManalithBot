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
package org.manalith.ircbot.plugin.tweetreader;

import java.io.File;
import java.util.regex.Pattern;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TweetReader {
	public static enum TwitterUrlType {
		UserHome(
				"http(s)?\\:\\/\\/twitter\\.com\\/(\\#\\!\\/)?([a-zA-Z0-9\\_]{1,15}(\\/)?){1}"), Tweet(
				"http(s)?\\:\\/\\/twitter\\.com\\/(\\#\\!\\/)?[a-zA-Z0-9\\_]{1,15}\\/status\\/[0-9]+");

		Pattern pattern;

		TwitterUrlType(String textPattern) {
			pattern = Pattern.compile(textPattern);
		}
	}

	private static final String TARGET_DATE_PATTERN = "yyyy년 MM월 dd일 E요일 HH:mm:ss";

	private long tweetId;
	private String screenName;

	private String consumerKey;
	private String consumerSecret;
	private String username;
	private String password;

	private PropertiesConfiguration config;

	private WebDriver driver;
	private JavascriptExecutor jse;

	private Twitter tweet;
	private RequestToken requestToken;
	private AccessToken accessToken;

	public TweetReader(String resourcePath, String consumerKey,
			String consumerSecret) throws ConfigurationException {

		try {
			config = new PropertiesConfiguration(resourcePath
					+ "keybox.property");
		} catch (ConfigurationException e) {
			config = new PropertiesConfiguration();
			config.setFile(new File(resourcePath + "keybox.property"));
			config.save();
		}

		setConsumerKey(consumerKey);
		setConsumerSecret(consumerSecret);
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public void setTwitterUsernameOrEmail(String username) {
		this.username = username;
	}

	public void setTwitterPassword(String password) {
		this.password = password;
	}

	private void setAcecssToken(String accessToken, String accessSecret) {
		setAccessToken(new AccessToken(accessToken, accessSecret));
	}

	private void setAccessToken(AccessToken accessToken) {
		tweet.setOAuthAccessToken(accessToken);
	}

	private enum UrlType {
		TweetURL, UserURL
	}

	private void initWebAutomateObject() {
		driver = new HtmlUnitDriver();
		((HtmlUnitDriver) driver).setJavascriptEnabled(true);
		jse = (JavascriptExecutor) driver;
	}

	private void initTwitter4j() throws TwitterException {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		Configuration config = builder.setDebugEnabled(false)
				.setJSONStoreEnabled(true).setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret).build();

		tweet = new TwitterFactory(config).getInstance();
		requestToken = tweet.getOAuthRequestToken();
	}

	private String getTwitterAuthorizationPINPageSource() {
		String url = requestToken.getAuthenticationURL();
		driver.get(url);
		jse.executeScript("document.getElementById('username_or_email').setAttribute('value','"
				+ username + "')"); // Twitter ID
		jse.executeScript("document.getElementById('password').setAttribute('value','"
				+ password + "')"); // Twitter Password
		jse.executeScript("document.getElementById('allow').click()");
		driver.getPageSource();

		return driver.getPageSource();
	}

	private String getTwitterAuthPINString() throws TwitterException {
		return Jsoup.parse(getTwitterAuthorizationPINPageSource())
				.getAllElements().select("div#oauth_pin>p>kbd>code").text();

	}

	private void authorizeTwitter() throws TwitterException,
			ConfigurationException {
		// init selenium web automation object
		initWebAutomateObject();

		// to success authorization for owners' twitter account
		accessToken = tweet.getOAuthAccessToken(requestToken,
				getTwitterAuthPINString());

		setAccessToken(accessToken);
		tweet.verifyCredentials().getId(); // pass.

		config.setProperty("com.twitter.accessKey", accessToken.getToken());
		config.setProperty("com.twitter.accessSecret",
				accessToken.getTokenSecret());
		config.save();

	}

	private Status getStatus(String url, UrlType type) throws TwitterException {
		Status stat = null;
		String[] urlArray = url.split("\\/");

		switch (type) {
		case TweetURL:
			tweetId = NumberUtils.toLong(urlArray[urlArray.length - 1]);
			stat = tweet.showStatus(tweetId);

			if (stat == null)
				return null;

			break;
		case UserURL:
			screenName = urlArray[urlArray.length - 1];
			ResponseList<Status> resp = tweet.getUserTimeline(screenName);

			if (resp.size() == 0)
				return null;

			stat = resp.get(0);
			break;
		}

		return stat;
	}

	private String getText(String twitterurl, UrlType type) {
		if (type == null)
			return null;

		// init twitter4j
		try {
			initTwitter4j();
		} catch (TwitterException e) {
			return e.getMessage();
		}

		if (StringUtils.isEmpty(config.getString("com.twitter.accessKey"))
				|| StringUtils.isEmpty(config
						.getString("com.twitter.accessSecret"))) {
			try {
				authorizeTwitter();
			} catch (TwitterException e) {
				return "[twitter4j.TwitterException] " + e.getMessage();
			} catch (ConfigurationException e) {
				return "[org.apache.common.configuration.ConfigutaionException] "
						+ e.getMessage();
			}
		} else {
			String accessToken = config.getString("com.twitter.accessKey");
			String accessSecret = config.getString("com.twitter.accessSecret");

			setAcecssToken(accessToken, accessSecret);
		}

		Status stat = null;
		try {
			stat = getStatus(twitterurl, type);
		} catch (TwitterException e) {
			return "[twitter4j.TwitterException] " + e.getMessage();
		}

		if (stat == null)
			return null;

		String author = stat.getUser().getName();
		String createdAt = DateFormatUtils.format(stat.getCreatedAt(),
				TARGET_DATE_PATTERN);
		String message = stat.getText();

		return String.format("작성자: %s, 작성일시: %s, 본문: %s", author, createdAt,
				message);
	}

	private UrlType validateUrl(String url) {
		UrlType result = null;

		if (StringUtils.isEmpty(url))
			return null;

		if (TwitterUrlType.Tweet.pattern.matcher(url).matches())
			return UrlType.TweetURL;

		if (TwitterUrlType.UserHome.pattern.matcher(url).matches())
			return UrlType.UserURL;

		return result;
	}

	public String read(String[] strs) {

		for (String str : strs) {
			String result = getText(str, validateUrl(str));
			if (result != null)
				return result;
		}

		return null;
	}
}