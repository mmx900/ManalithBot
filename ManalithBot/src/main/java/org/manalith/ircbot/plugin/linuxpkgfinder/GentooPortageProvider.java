/*
 	org.manalith.ircbot.plugin.distopkgfinder/GentooPkgFinderRunner.java
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

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.manalith.ircbot.resources.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GentooPortageProvider implements GentooSearchEngineProvider {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String find(MessageEvent event, String... args) {
		return this.find(args[0]);
	}

	@Override
	public String find(String arg) {
		String result = "";
		String url = "http://gentoo-portage.com/Search?search=" + arg;

		String pkgname = "";
		String description = "";

		Connection conn = Jsoup.connect(url);
		conn.timeout(120000); // timeout : 60s. This is to slow - -;

		try {
			Elements e = conn.get().select("#search_results").select("a");

			int result_size = e.size();
			if (result_size == 0) {
				result = "[Gentoo] 결과가 없습니다";
				return result;
			}

			pkgname = e.select("div").text().split("\\s")[0];
			if (!pkgname.split("\\/")[1].equals(arg)) {
				result = "[Gentoo] 결과가 없습니다";
				return result;
			}

			description = e.select("div").get(1).text();

			String detail_url = "http://gentoo-portage.com/" + pkgname;
			Connection conn2 = Jsoup.connect(detail_url);
			conn2.timeout(120000); // timeout : 60s.

			Elements ee = conn2.get().select("#ebuild_list>ul>li");

			if (ee.size() == 0) {
				result = "[Gentoo] 결과가 없습니다";
				return result;
			}

			result = ee.get(0).select("div").get(0).select("b").text();
			result += " : " + description;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = "오류: " + e.getMessage();
		}

		return result;
	}
}
