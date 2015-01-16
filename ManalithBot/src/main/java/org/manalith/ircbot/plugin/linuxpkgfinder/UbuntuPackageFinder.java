/*
 	org.manalith.ircbot.plugin.distopkgfinder/UbuntuPkgFinderRunner.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2011, 2012, 2013, 2014, 2015  Seong-ho, Cho <darkcircle.0426@gmail.com>

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

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.manalith.ircbot.annotation.Option;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UbuntuPackageFinder extends PackageFinder {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String getName() {
		return "우분투";
	}

	@Override
	public String getDescription() {
		return "지정한 이름을 가진 우분투의 패키지를 검색합니다.";
	}

	@Override
	@BotCommand("ubu")
	public String find(
			@Option(name = "키워드", help = "검색할 단어") String arg) {
		StringBuilder result = new StringBuilder("");
		String url = "http://packages.ubuntu.com/search?keywords=" + arg
				+ "&searchon=names&suite=all&section=all";

		String pkgname = "";
		String version = "";
		String description = "";

		boolean isCheckedOfficialPeriodicVersion = false;
		boolean isCheckedOfficialLTSVersion = false;

		try {
			Connection conn = Jsoup.connect(url);
			conn.timeout(10000);
			Elements div = conn.get().select("#psearchres");

			if (div.size() == 0 || !isBeingExactHits(div.select("h2"))) {
				result.append("[Ubuntu] 결과가 없습니다");
				return result.toString();
			}

			pkgname = div.select("h3").get(0).text().split("\\s")[1];

			Elements ExactHits = div.select("ul").get(0).select("li");
			int elemCnt = ExactHits.size();
			Element elem = ExactHits.get(elemCnt - 1);

			// Description
			description = elem.toString().split("\\<br\\s\\/>")[0].split("\\:")[1]
					.trim();

			int a = description.indexOf("[");
			if (a != -1)
				description = description.substring(0, a);

			result.append("[Ubuntu] ");
			result.append("\u0002");
			result.append(pkgname);
			result.append("\u0002");
			result.append(" - " + description + ", ");

			ArrayList<String> resultStr = new ArrayList<String>();
			StringBuilder tStrBdr;

			for (int cnt = elemCnt - 1; cnt >= 0; cnt--) {
				elem = ExactHits.get(cnt);
				tStrBdr = new StringBuilder("");
				String distVerStr = elem.select("a").text();
				String[] pkgVerSplit = elem.toString().split("\\<br\\s\\/>")[1]
						.split("\\:");

				if (pkgVerSplit.length == 2)
					version = pkgVerSplit[0].split("\\s")[0];
				else if (pkgVerSplit.length == 3)
					version = pkgVerSplit[1].split("\\s")[0];

				// Periodic, LTS (and Updates)
				if (isOfficial(distVerStr)) {
					if (isPeriodic(distVerStr)) {
						if (isCheckedOfficialPeriodicVersion) {
							resultStr.remove(resultStr.size() - 1);
							continue;
						}
						isCheckedOfficialPeriodicVersion = true;
					} else if (isLTS(distVerStr)) {
						if (isCheckedOfficialLTSVersion) {
							resultStr.remove(resultStr.size() - 1);
							continue;
						}
						isCheckedOfficialLTSVersion = true;
					}
					if (!isUpdates(distVerStr))
						distVerStr = distVerStr.replace("(", "").replace(")",
								"");

					tStrBdr = tStrBdr.append("\u0002(").append(distVerStr)
							.append(")\u0002 ").append(version);
					resultStr.add(tStrBdr.toString());
				} else // Unofficial (!)
				{
					if (cnt == elemCnt - 1) {
						tStrBdr = tStrBdr.append("\u0002(").append(distVerStr)
								.append(")\u0002 ").append(version);
						resultStr.add(tStrBdr.toString());
					}
				}

				if (isCheckedOfficialPeriodicVersion
						&& isCheckedOfficialLTSVersion)
					break;
			}

			elemCnt = resultStr.size();

			for (int cnt = elemCnt - 1; cnt >= 0; cnt--) {
				if (cnt != elemCnt - 1)
					result.append(", ");
				result.append(resultStr.get(cnt));
			}

			result.append(".");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = new StringBuilder("");
			result.append("오류: " + e.getMessage());
		}

		return result.toString();
	}

	private boolean isBeingExactHits(Elements hit) {
		Elements hits = hit;
		int hsize = hits.size();

		for (int i = 0; i < hsize; i++) {
			if (hits.get(i).text().equals("Exact hits")) {
				return true;
			}
		}

		return false;
	}

	private boolean isOfficial(String distVerStr) {
		return Pattern
				.compile(
						"[a-z]+(\\s\\([0-9]{2}\\.[0-9]{2}(LTS)?\\)|\\-updates)")
				.matcher(distVerStr).matches();
	}

	private boolean isLTS(String distVerStr) {
		return distVerStr.contains("LTS");
	}

	private boolean isPeriodic(String distVerStr) {
		return Pattern.compile("[a-z]+\\s\\([0-9]{2}\\.[0-9]{2}\\)")
				.matcher(distVerStr).matches();
	}

	private boolean isUpdates(String distVerStr) {
		return distVerStr.contains("-updates");
	}
}
