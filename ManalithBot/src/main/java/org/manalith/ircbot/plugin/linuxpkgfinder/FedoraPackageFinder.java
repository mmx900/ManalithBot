/*
 	org.manalith.ircbot.plugin.distopkgfinder/FedoraPkgFinderRunner.java
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

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.manalith.ircbot.annotation.Description;
import org.manalith.ircbot.annotation.NotNull;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.springframework.stereotype.Component;

@Component
public class FedoraPackageFinder extends PackageFinder {
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public String getName() {
		return "페도라";
	}

	@Override
	public String getDescription() {
		return "지정한 이름을 가진 페도라의 패키지를 검색합니다.";
	}

	@BotCommand("fed")
	public String find(@Description("키워드") @NotNull String keyword) {
		String result = "";

		if (keyword.equals("")) {
			result = "키워드를 지정하지 않았습니다";
			return result;
		}

		try {

			String url = "http://rpmfind.net/linux/rpm2html/search.php?query="
					+ keyword + "&submit=Search";

			Connection conn = Jsoup.connect(url);
			conn.timeout(5000);

			Elements tables = conn.get().select("table");

			if (tables.size() < 2) {
				result = "[Fedora] 결과가 없습니다";
				return result;
			}

			System.out.println(tables.get(1).select("tbody>tr").toString());
			Iterator<Element> row = tables.get(1).select("tbody>tr").iterator();
			boolean firstrow = false;

			String pkgname = "";
			String description = "";
			String dist = "";
			String NamenVer = "";

			while (row.hasNext()) {
				if (!firstrow) {
					row.next(); // dummy
					firstrow = true;
					continue;
				}

				Elements e = row.next().select("td");
				System.out.println(e);
				pkgname = e.get(0).select("a").get(0).text();
				description = e.get(1).text();
				dist = e.get(2).text();

				if (dist.split("\\s")[0].equals("Fedora")) {
					int pkg_ver;
					try {
						pkg_ver = Integer.parseInt(dist.split("\\s")[1]);
					} catch (Exception ee) {
						continue;
					}

					description += " in the Fedora " + pkg_ver;

					String[] spl = pkgname.split("\\.");
					int i = 0;

					// exclude .fcxx.html string
					for (; !spl[i].contains("fc"); i++) {
						if (i != 0)
							NamenVer += ".";
						NamenVer += spl[i];
					}

					// split pkgname and version
					spl = NamenVer.split("\\-");
					String name = spl[0];
					String ver = "";
					i = 1;

					while (!(spl[i].charAt(0) >= '0' && spl[i].charAt(0) <= '9')) {
						name += "-" + spl[i++];
					}

					int initial_veridx = i;
					while (i < spl.length) {
						if (initial_veridx != i)
							ver += "-";
						ver += spl[i++];
					}

					NamenVer = name + "  " + ver;

					break;

				}
			}

			// need to give result into result.
			if (!dist.contains("Fedora")) {
				result = "[Fedora] 결과가 없습니다";
			} else {
				result = NamenVer + " : " + description;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = "오류: " + e.getMessage();
		}

		return result;
	}
}
