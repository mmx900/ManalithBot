/*
 	org.manalith.ircbot.plugin.distopkgfinder/DebianPkgFinderRunner.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2011, 2012  Seong-ho, Cho <darkcircle.0426@gmail.com>
 	Copyright (C) 2012  Changwoo Ryu <cwryu@debian.org>

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
package org.manalith.ircbot.plugin.linuxpkgfinder;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class DebianPackageFinder extends PackageFinder {
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public String getName() {
		return "데비안";
	}

	@Override
	public String getCommands() {
		return "!deb";
	}

	public String getHelp() {
		return "설  명: 지정한 이름을 가진 데비안의 패키지를 검색합니다, 사용법: !deb [키워드]";
	}

	public String parseVersionInfo(Document doc) {
		Elements exactHits = doc.select("#psearchres").select("ul").get(0)
				.select("li");
		String result = "";

		for (Element e : exactHits) {
			String dist;
			try {
				dist = e.select("a").text().split("[\\(\\)]")[1];
			} catch (ArrayIndexOutOfBoundsException ex) {
				dist = e.select("a").text();
			}

			String version = "알 수 없음";
			String[] versionLines = e.toString().split("\\<br\\s\\/>");

			for (String line : versionLines) {
				String v = line.split(": ")[0];
				if (v.split("\\s").length > 1)
					continue;
				else {
					version = v;
					break;
				}
			}

			result += version + " (" + dist + ") ";
		}

		return result;
	}

	@BotCommand(value = { "!deb" }, minimumArguments = 1)
	public String find(MessageEvent event, String... args) {
		return this.find(args[0]);
	}

	public String find(String arg) {
		String result = "";
		String url = "http://packages.debian.org/search?keywords=" + arg
				+ "&searchon=names&suite=all&section=all";

		boolean hasExacthits = false;

		try {

			Connection conn = Jsoup.connect(url);
			conn.timeout(20000);
			Document doc = conn.get();

			if (doc.select("#psearchres").size() == 0) {
				result = "결과가 없습니다";
				return result;
			}

			Elements hits = doc.select("#psearchres").select("h2");
			int hsize = hits.size();

			if (hsize == 0)
				result = "결과가 없습니다";
			for (int i = 0; i < hsize; i++) {
				if (hits.get(i).text().equals("Exact hits")) {
					hasExacthits = true;
					break;
				}

			}
			if (!hasExacthits) {
				result = "결과가 없습니다";
				return result;
			}

			String pkgname = doc.select("#psearchres").select("h3").get(0)
					.text().split("\\s")[1];

			Elements exactHits = doc.select("#psearchres").select("ul").get(0)
					.select("li");
			int elemCnt = exactHits.size();
			Element latestElement = exactHits.get(elemCnt - 1);
			String description = latestElement.toString().split("\\<br\\s\\/>")[0]
					.split("\\:")[1].trim();

			result = pkgname + " - " + description + ", ";
			result += parseVersionInfo(doc);
			System.out.println(result);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = "오류: " + e.getMessage();
		}

		return result;
	}
}
