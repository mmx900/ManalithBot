/*
 	org.manalith.ircbot.plugin.distopkgfinder/ArchPkgFinderRunner.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2012  Seong-ho, Cho <darkcircle.0426@gmail.com>

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

package org.manalith.ircbot.plugin.distropkgfinder;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ArchPackageFinder implements PackageFinder {
	private Logger logger = Logger.getLogger(getClass());

	private String keyword;

	public ArchPackageFinder() {
		this.setKeyword("");
	}

	public ArchPackageFinder(String newKeyword) {
		this.setKeyword(newKeyword);
	}

	public void setKeyword(String newKeyword) {
		this.keyword = newKeyword;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public String find() {
		String result = "";

		String[] arch_keywords = { "any", "i686", "x86_64" };
		int len = arch_keywords.length;
		int pages = 100000000;
		String description = "";

		String url = "";
		try {
			String infostr = "";
			for (int i = 0; i < len; i++) {
				for (int j = 0; j < pages; j++) {
					url = "http://www.archlinux.org/packages/"
							+ (new Integer(j + 1)).toString() + "/?arch="
							+ arch_keywords[i] + "&q=" + this.getKeyword();

					if (j == 0 && pages == 100000000) {
						String pageinfo = Jsoup
								.connect(url)
								.get()
								.select("div#pkglist-results>div.pkglist-stats>p")
								.get(0).text();

						if (StringUtils.countMatches(pageinfo, ".") == 1)
							pages = 1;
						else {
							if (pageinfo.charAt(pageinfo.length() - 1) == '.')
								pageinfo = pageinfo.substring(0,
										pageinfo.length() - 1);
							String[] piarray = pageinfo.split("\\s");
							pages = NumberUtils
									.toInt(piarray[piarray.length - 1]);
						}

						j--;
						continue;
					}

					Iterator<Element> e = Jsoup.connect(url).get()
							.select("table.results>tbody>tr").iterator();

					while (e.hasNext()) {
						Elements ee = e.next().select("td");
						if (ee.get(2).select("a").get(0).text()
								.equals(this.getKeyword())) {
							if (!infostr.equals(""))
								infostr += ", ";
							infostr += "[main-" + ee.get(0).text() + "] ";
							infostr += ee.get(2).select("a").get(0).text()
									+ "  ";

							if (ee.get(3).select("span.flagged").size() > 0)
								infostr += ee.get(3).select("span.flagged")
										.get(0).text();
							else
								infostr += ee.get(3).text();

							infostr += "(" + ee.get(1).text() + ")";
							if (description.equals(""))
								description = ee.get(4).text();

							break;
						}
					}
				}
				pages = 100000000;
			}

			if (!infostr.equals(""))
				infostr += " : " + description;

			url = "http://aur.archlinux.org/packages.php?K="
					+ this.getKeyword();
			Iterator<Element> e = Jsoup.connect(url).get()
					.select("table>tbody>tr").iterator();

			boolean firstrow = false;

			while (e.hasNext()) {
				if (!firstrow) {
					firstrow = true;
					e.next();
					continue;
				}
				Elements ee = e.next().select("td");
				if (ee.get(1).select("span>a>span").get(0).text().split("\\s")[0]
						.equals(this.getKeyword())) {
					String[] namenver = ee.get(1).select("span>a>span").get(0)
							.text().split("\\s");
					if (!infostr.equals(""))
						infostr += " | ";
					infostr += "[AUR-"
							+ ee.get(0).select("span>span").get(0).text()
							+ "] ";
					infostr += namenver[0] + "  " + namenver[1];
					infostr += " : "
							+ ee.get(3).select("span>span").get(0).text();
					break;
				}
			}

			if (!infostr.equals(""))
				result = infostr;
			else
				result = "결과가 없습니다";

		} catch (IOException e) {
			logger.error(e);
		}
		return result;
	}
}
