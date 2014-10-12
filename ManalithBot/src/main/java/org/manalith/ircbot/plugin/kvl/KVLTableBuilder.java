/*
 	org.manalith.ircbot.plugin.kvl/KVLTableBuilder.java
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
package org.manalith.ircbot.plugin.kvl;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class KVLTableBuilder {

	private String url;

	public KVLTableBuilder(String url) {
		this.url = url;
	}

	public KVLTable generateKernelVersionTable() throws IOException {
		KVLTable result = new KVLTable();

		Elements elements = Jsoup.connect(url).get()
				.select("table#releases>tbody>tr");

		for (Element e : elements) {
			Elements tds = e.select("td");
			String newTag = tds.get(0).text().replaceAll("\\:", "");
			String newVerElement = tds.get(1).text();

			result.addVersionInfo(newTag, newVerElement);
		}

		return result;
	}

}
