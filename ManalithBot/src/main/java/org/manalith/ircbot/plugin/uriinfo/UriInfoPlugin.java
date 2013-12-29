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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.manalith.ircbot.util.MessageUtils;
import org.springframework.stereotype.Component;

@Component
public class UriInfoPlugin extends SimplePlugin {

	private Logger logger = Logger.getLogger(getClass());
	private boolean enablePrintContentType;

	private static final String USER_AGENT = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:12.0) "
			+ "Gecko/20100101 Firefox/12.0";

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

	private String getSiteSpecificTitle(String uri, Document document) {
		if (uri.startsWith("http://mlbpark.donga.com/bbs/view.php?") ||
		    uri.startsWith("http://mlbpark.donga.com/mbs/articleV.php?")) {
			// MLB Park article
			Element element = document.getElementsByClass("D14").first();
			if (element != null) {
				try {
					return element.child(0).text();
				} catch (IndexOutOfBoundsException e) {
					return null;
				}
			}
		} else if (uri.startsWith("http://www.slrclub.com/bbs/vx2.php?")) {
			Element element = document.getElementsByClass("sbj").first();
			if (element != null) {
				return element.text();
			}
		}

		return null;
        }

	private String getInfo(String uri) {
		String result = null;
		Response response;

		try {
			// 일부 사이트에서는 User Agent가 있어야 접근을 허용
			response = Jsoup.connect(uri).userAgent(USER_AGENT).execute();
		} catch (UnsupportedMimeTypeException e) {
			return enablePrintContentType ? "[링크 형식] " + e.getMimeType() : null;
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
			return null;
		}

		String contentType = response.contentType();

		// 일단 title이 나오는지 시도해 본다.
		try {
			Document document = response.parse();
			String title = document.title();

			if (StringUtils.isBlank(title))
				throw new IOException();

			title = title.trim().replaceAll("(\\s){1,}", " ");

			// 몇몇 사이트에 대한 처리
			String stitle = getSiteSpecificTitle(uri, document);
			if (stitle == null)
				result = "[링크 제목] " + title;
			else
				result = "[링크 제목] " + stitle + " | " + title;
		} catch (IOException e) {
			// parse 오류 또는 빈 title -- HTML의
			// 경우는 빈 제목이라도 표시하고 아니면
			// content type 표시로 패스
			if (contentType.startsWith("text/html"))
				result = "[링크 제목]";
		}

		if (result == null && enablePrintContentType) {
			result = "[링크 형식] " + contentType;
		}

		return result;
	}

	public void onMessage(MessageEvent event) {
		String message = event.getMessage();

		String uri = MessageUtils.findUri(message);
		if (uri == null)
			return;

		String info = getInfo(uri);
		if (info != null) {
			// This plugin runs implicitly
			event.respond(info, false);
		}
	}
}
