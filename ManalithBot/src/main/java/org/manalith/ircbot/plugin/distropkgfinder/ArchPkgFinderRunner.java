/*
 	ArchPkgFinderRunner.java
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ArchPkgFinderRunner {
	private String keyword;

	public ArchPkgFinderRunner() {
		this.setKeyword("");
	}

	public ArchPkgFinderRunner(String newKeyword) {
		this.setKeyword(newKeyword);
	}

	public void setKeyword(String newKeyword) {
		this.keyword = newKeyword;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public String run() {
		String result = "";

		String[] arch_keywords = { "any", "i686", "x86_64" };
		int len = arch_keywords.length;
		String description = "";

		String url = "";
		try {
			String infostr = "";
			for (int i = 0; i < len; i++) {
				url = "http://www.archlinux.org/packages/?arch="
						+ arch_keywords[i] + "&q=" + this.getKeyword();
				Iterator<Element> e = Jsoup.connect(url).get()
						.select("table.results>tbody>tr").iterator();
				
				while (e.hasNext()) {
					Elements ee = e.next().select("td");
					if (ee.get(2).select("a").get(0).text()
							.equals(this.getKeyword())) {
						if ( !infostr.equals("") )
							infostr += ", ";
						infostr += "[main-" + ee.get(0).text() + "] ";
						infostr += ee.get(2).select("a").get(0).text() + "  ";

						if (ee.get(3).select("span.flagged").size() > 0)
							infostr += ee.get(3).select("span.flagged").get(0)
									.text();
						else
							infostr += ee.get(3).text();

						infostr += "(" + ee.get(1).text() + ")";
						if (description.equals(""))
							description = ee.get(4).text();
						
						break;
					}
				}
			}

			if ( !infostr.equals("") )
				infostr += " : " + description;
			
			url = "http://aur.archlinux.org/packages.php?K=" + this.getKeyword();
			Iterator<Element> e = Jsoup.connect(url).get().select("table>tbody>tr").iterator();
			
			boolean firstrow = false; 
			//*
			while ( e.hasNext() )
			{
				if ( !firstrow )
				{
					firstrow = true;
					e.next();
					continue;
				}
				Elements ee = e.next().select("td");
				if ( ee.get(1).select("span>a>span").get(0).text().split("\\s")[0].equals(this.getKeyword()) )
				{
					String [] namenver = ee.get(1).select("span>a>span").get(0).text().split("\\s");
					if ( !infostr.equals("") ) infostr += " | ";
					infostr += "[AUR-" + ee.get(0).select("span>span").get(0).text() + "] ";
					infostr += namenver[0] + "  " + namenver[1];
					infostr += " : " + ee.get(3).select("span>span").get(0).text();
					break;
				}
			}
			//*/
			if ( !infostr.equals("") )
				result = infostr;
			else
				result = "There is no result";
			
			
		} catch (IOException ioe) {
			result = ioe.getMessage();
			ioe.printStackTrace();
		}
		return result;
	}
}
