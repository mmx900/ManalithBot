/*
 	org.manalith.ircbot.plugin.distopkgfinder/UbuntuPkgFinderRunner.java
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
package org.manalith.ircbot.plugin.linuxpkgfinder;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.manalith.ircbot.annotation.Description;
import org.manalith.ircbot.annotation.NotNull;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.springframework.stereotype.Component;

@Component
public class UbuntuPackageFinder extends PackageFinder {
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public String getName() {
		return "우분투";
	}

	@Override
	public String getDescription() {
		return "지정한 이름을 가진 우분투의 패키지를 검색합니다.";
	}

	@BotCommand("ubu")
	public String find(@Description("키워드") @NotNull String arg) {
		String result = "";
		String latestPkgName = "";

		try {
			latestPkgName = this.getLatestPkgName();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = "오류: " + e.getMessage();
			return result;
		}

		String url = "http://packages.ubuntu.com/search?keywords=" + arg
				+ "&searchon=names&suite=" + latestPkgName + "&section=all";

		boolean hasExacthits = false;

		String pkgname = "";
		String version = "";
		String description = "";

		try {
			Connection conn = Jsoup.connect(url);
			conn.timeout(10000);
			Elements div = conn.get().select("#psearchres");

			if (div.size() == 0) {
				result = "[Ubuntu] 결과가 없습니다";
				return result;
			}

			Elements hits = div.select("h2");
			int hsize = hits.size();

			if (hsize == 0)
				result = "[Ubuntu] 결과가 없습니다";
			for (int i = 0; i < hsize; i++) {
				if (hits.get(i).text().equals("Exact hits")) {
					hasExacthits = true;
					break;
				}

			}
			if (!hasExacthits) {
				result = "[Ubuntu] 결과가 없습니다";
				return result;
			}

			pkgname = div.select("h3").get(0).text().split("\\s")[1];

			result = pkgname + "  ";
			Elements ExactHits = div.select("ul").get(0).select("li");
			int elemCnt = ExactHits.size();
			Element latestElement = ExactHits.get(elemCnt - 1);

			String[] verSplit = latestElement.toString().split("\\<br\\s\\/>")[1]
					.split("\\:");
			if (verSplit.length == 2)
				version = verSplit[0].split("\\s")[0];
			else if (verSplit.length == 3)
				version = verSplit[1].split("\\s")[0];

			result += version + " : ";

			description = latestElement.toString().split("\\<br\\s\\/>")[0]
					.split("\\:")[1].trim();
			int a = description.indexOf("[");
			if (a != -1)
				description = description.substring(0, a);

			result += description;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = "오류: " + e.getMessage();
		}

		return result;
	}

	private String getLatestPkgName() throws Exception {
		String result = "";
		String url = "http://packages.ubuntu.com";
		String tmp;

		Document doc = Jsoup.connect(url).get();
		Elements e = doc.select("select#distro>option");

		int esize = e.size();
		for (int i = 0; i < esize; i++) {
			tmp = e.get(i).attr("selected");
			if (tmp.equals("selected")) {
				result = e.get(i).text();
				break;
			}
		}

		return result;
	}

}
