package org.manalith.ircbot.plugin.distropkgfinder;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class GentooPkgFinderJsoupRunner {

	private String keyword;

	public GentooPkgFinderJsoupRunner() {
		this.setKeyword("");
	}

	public GentooPkgFinderJsoupRunner(String newKeyword) {
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
		String url = "http://gentoo-portage.com/Search?search="
				+ this.getKeyword();

		String pkgname = "";
		String description = "";

		Connection conn = Jsoup.connect(url);
		conn.timeout(120000); // timeout : 60s. This is to slow - -;

		try {
			Elements e = conn.get().select("#search_results").select("a");

			int result_size = e.size();
			if (result_size == 0) {
				result = "There is no result";
				return result;
			}

			pkgname = e.select("div").text().split("\\s")[0];
			if (!pkgname.split("\\/")[1].equals(this.getKeyword())) {
				result = "There is no result";
				return result;
			}

			description = e.select("div").get(1).text();

			String detail_url = "http://gentoo-portage.com/" + pkgname;
			Connection conn2 = Jsoup.connect(detail_url);
			conn2.timeout(120000); // timeout : 60s.

			Elements ee = conn2.get().select("#ebuild_list>ul>li");

			if (ee.size() == 0) {
				result = "There is no result";
				return result;
			}

			result = ee.get(0).select("div").get(0).select("b").text();
			result += " : " + description;

		} catch (Exception e) {
			result = e.getMessage();
			return result;
		}

		return result;
	}
}
