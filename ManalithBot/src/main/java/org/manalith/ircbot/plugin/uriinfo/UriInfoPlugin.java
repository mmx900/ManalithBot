/*
 	org.manalith.ircbot.plugin.uriinfo/UriInfoPlugin.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2012  Changwoo Ryu <cwryu@debian.org>
 	Copyright (C) 2012  Seong-ho Cho <darkcircle.0426@gmail.com>

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
package org.manalith.ircbot.plugin.uriinfo;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class UriInfoPlugin extends AbstractBotPlugin {

	private Logger logger = Logger.getLogger(getClass());
	private boolean enablePrintContentType;

	public String getCommands() {
		return null;
	}

	public String getName() {
		return "URI 정보";
	}

	public String getHelp() {
		return "대화 중 등장하는 URI의 정보를 표시합니다";
	}

	public void setEnablePrintContentType(boolean enablePrintContentType) {
		this.enablePrintContentType = enablePrintContentType;
	}

	public boolean enablePrintContentType() {
		return enablePrintContentType;
	}

	private String findUri(String msg) {
		if (!msg.contains("http"))
			return null;

		String URI_REGEX = ".*(https?://\\S+).*";
		Pattern pattern = Pattern.compile(URI_REGEX);
		Matcher matcher = pattern.matcher(msg);

		if (!matcher.matches())
			return null;

		return matcher.group(1);
	}

	private String getInfo(String uri) {
		String result = null;

		try {
			Response response = Jsoup
					.connect(uri)
					.header("User-Agent", // 일부 사이트에서는 User Agent가 있어야 접근을 허용
							"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/12.0")
					.execute();

			if (StringUtils.contains(response.contentType(), "text/html")
					|| StringUtils.contains(response.contentType(), "text/xml")) {
				Document document = response.parse();

				String title = document.title();

				if (StringUtils.isNotBlank(title)) {
					result = "[Link Title] "
							+ title.replaceAll("\\n", "").replaceAll("\\r", "")
									.replaceAll("(\\s){2,}", " ");
				}
			} else {
				if (enablePrintContentType) {
					result = "[Link Content-type] " + response.contentType();
				}
			}
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
		}

		return result;
	}

	public void onMessage(MessageEvent event) {
		String message = event.getMessage();
		String channel = event.getChannel().getName();

		String uri = findUri(message);
		if (uri == null)
			return;

		String info = getInfo(uri);
		if (info != null) {
			bot.sendLoggedMessage(channel, info);
		}

		// This plugin runs implicitly; it does NOT need to call
		// event.setExecuted(true)
	}
}
