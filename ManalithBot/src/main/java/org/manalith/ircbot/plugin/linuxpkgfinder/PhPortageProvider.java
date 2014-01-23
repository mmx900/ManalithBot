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

import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.manalith.ircbot.resources.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PhPortageProvider implements GentooSearchEngineProvider {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String find(MessageEvent event, String... args) {
		return this.find(args[0]);
	}

	@Override
	public String find(String arg) {
		String result = "";
		String url = "http://darkcircle.kr/phportage/phportage.xml?k=" + arg
				+ "&limit=1&similarity=exact"
				+ "&showmasked=true&livebuild=false";

		try {
			Document d = Jsoup.connect(url).get();
			System.out.println(d.select("result>code").get(0).text());
			if (NumberUtils.toInt(d.select("result>code").get(0).text()) == 0) {
				if (NumberUtils.toInt(d.select("result>actualnumofres").get(0)
						.text()) == 0)
					result = "[Gentoo] 결과가 없습니다";
				else {
					Element e = d.select("result>packages>pkg").get(0);
					String pkgname = e.select("category").get(0).text() + "/"
							+ e.select("name").get(0).text();

					String ver = e.select("version").get(0).text();
					String description = e.select("description").get(0).text();

					result = pkgname + "  " + ver + " : " + description;
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = "오류: " + e.getMessage();
		}

		return result;
	}
}
