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
	// private Logger logger = Logger.getLogger(getClass());
	private static final String TWITTER_USER_HOME_PATTERN = "http(s)?\\:\\/\\/twitter\\.com\\/(\\#\\!\\/)?([a-zA-Z0-9\\_]{1,15}(\\/)?){1}";
	private static final String TWITTER_TWIT_URL_PATTERN = "http(s)?\\:\\/\\/twitter\\.com\\/(\\#\\!\\/)?[a-zA-Z0-9\\_]{1,15}\\/status\\/[0-9]+";
	private static final String TARGET_DATE_PATTERN = "yyyy년 MM월 dd일 E요일 HH:mm:ss";

	private long tweet_id;
	private String screenName;

	private String consumerKey;
	private String consumerSecret;
	private String username;
	private String password;

	private PropertiesConfiguration twitterConfiguration;

	private WebDriver driver;
	private JavascriptExecutor jse;

	private Twitter tweet;
	private RequestToken reqToken;
	private AccessToken aToken;

	public TweetReader(String resourcePath, String ck_, String cs_)
			throws ConfigurationException {

		try {
			this.twitterConfiguration = new PropertiesConfiguration(
					resourcePath + "keybox.property");
		} catch (ConfigurationException e) {
			this.twitterConfiguration = new PropertiesConfiguration();
			this.twitterConfiguration.setFile(new File(resourcePath
					+ "keybox.property"));
			this.twitterConfiguration.save();
		}

		this.setConsumerKey(ck_);
		this.setConsumerSecret(cs_);
	}

	public void setConsumerKey(String ck_) {
		this.consumerKey = ck_;
	}

	public void setConsumerSecret(String cs_) {
		this.consumerSecret = cs_;
	}

	public void setTwitterUsernameOrEmail(String un_) {
		this.username = un_;
	}

	public void setTwitterPassword(String pw_) {
		this.password = pw_;
	}

	private void setAcecssToken(String aToken, String aSecret) {
		this.setAccessToken(new AccessToken(aToken, aSecret));
	}

	private void setAccessToken(AccessToken accessToken) {
		this.tweet.setOAuthAccessToken(accessToken);
	}

	private enum UrlType {
		TweetURL, UserURL
	}

	private void initWebAutomateObject() {
		this.driver = new HtmlUnitDriver();
		((HtmlUnitDriver) this.driver).setJavascriptEnabled(true);
		this.jse = (JavascriptExecutor) this.driver;
	}

	private void initTwitter4JObject() throws TwitterException {
		ConfigurationBuilder c = new ConfigurationBuilder();
		Configuration cfg = c.setDebugEnabled(false).setJSONStoreEnabled(true)
				.setOAuthConsumerKey(this.consumerKey)
				.setOAuthConsumerSecret(this.consumerSecret).build();

		this.tweet = new TwitterFactory(cfg).getInstance();
		this.reqToken = this.tweet.getOAuthRequestToken();
	}

	private String getTwitterAuthorizationPINPageSource() {
		String url = this.reqToken.getAuthenticationURL();
		this.driver.get(url);
		this.jse.executeScript("document.getElementById('username_or_email').setAttribute('value','"
				+ this.username + "')"); // Twitter ID
		this.jse.executeScript("document.getElementById('password').setAttribute('value','"
				+ this.password + "')"); // Twitter Password
		this.jse.executeScript("document.getElementById('allow').click()");
		this.driver.getPageSource();

		return this.driver.getPageSource();
	}

	private String getTwitterAuthPINString() throws TwitterException {
		return Jsoup.parse(this.getTwitterAuthorizationPINPageSource())
				.getAllElements().select("div#oauth_pin>p>kbd>code").text();

	}

	private void authorizeTwitter() throws TwitterException,
			ConfigurationException {
		// init selenium web automation object
		this.initWebAutomateObject();

		// to success authorization for owners' twitter account
		this.aToken = this.tweet.getOAuthAccessToken(this.reqToken,
				this.getTwitterAuthPINString());

		this.setAccessToken(this.aToken);
		this.tweet.verifyCredentials().getId(); // pass.

		this.twitterConfiguration.setProperty("com.twitter.accessKey",
				this.aToken.getToken());
		this.twitterConfiguration.setProperty("com.twitter.accessSecret",
				this.aToken.getTokenSecret());
		this.twitterConfiguration.save();

	}

	private Status getStatus(String twitterurl, UrlType type)
			throws TwitterException {
		Status stat = null;
		String[] urlArray = twitterurl.split("\\/");

		switch (type) {
		case TweetURL:
			this.tweet_id = NumberUtils.toLong(urlArray[urlArray.length - 1]);
			stat = this.tweet.showStatus(this.tweet_id);

			if (stat == null)
				return null;

			break;
		case UserURL:
			this.screenName = urlArray[urlArray.length - 1];
			ResponseList<Status> resp = this.tweet
					.getUserTimeline(this.screenName);

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
			this.initTwitter4JObject();
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			return e1.getMessage();
		}

		if (StringUtils.isEmpty(this.twitterConfiguration
				.getString("com.twitter.accessKey"))
				|| StringUtils.isEmpty(this.twitterConfiguration
						.getString("com.twitter.accessSecret"))) {
			try {
				this.authorizeTwitter();
			} catch (TwitterException e) {
				return "[twitter4j.TwitterException] " + e.getMessage();
			} catch (ConfigurationException e) {
				return "[org.apache.common.configuration.ConfigutaionException] "
						+ e.getMessage();
			}
		} else {
			String accessToken = this.twitterConfiguration
					.getString("com.twitter.accessKey");
			String accessSecret = this.twitterConfiguration
					.getString("com.twitter.accessSecret");

			this.setAcecssToken(accessToken, accessSecret);
		}

		Status stat = null;
		try {
			stat = this.getStatus(twitterurl, type);
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
		else if (Pattern.compile(TWITTER_TWIT_URL_PATTERN).matcher(url)
				.matches())
			return UrlType.TweetURL;
		else if (Pattern.compile(TWITTER_USER_HOME_PATTERN).matcher(url)
				.matches())
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