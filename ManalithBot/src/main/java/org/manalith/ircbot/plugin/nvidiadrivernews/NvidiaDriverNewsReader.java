/*
 	org.manalith.ircbot.plugin.nvidiadrivernews/NvidiaDriverNewsRunner.java
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
package org.manalith.ircbot.plugin.nvidiadrivernews;

import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class NvidiaDriverNewsReader {

	public String read() {
		StringBuilder result = new StringBuilder();

		try {
			Connection conn = Jsoup
					.connect("http://www.nvnews.net/vbulletin/showthread.php?t=122606");
			conn.timeout(10000);

			Elements e = conn.get().select("div#post_message_1836667").get(0)
					.select("a");
			result.append("Current long-lived branch release: ");
			result.append(e.get(0).text());
			result.append(", Current official release: ");
			result.append(e.get(1).text());
			result.append(", Current beta release: ");
			result.append(e.get(2).text());
		} catch (IOException ioe) {
			result.append(ioe.getMessage());
		}

		return result.toString();
	}
}
