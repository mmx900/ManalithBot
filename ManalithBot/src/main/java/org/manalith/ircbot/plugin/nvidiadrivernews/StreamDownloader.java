//
// StreamTokenAnalyzer.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.nvidiadrivernews;

import java.net.URL;
import java.net.URLConnection;
import java.nio.CharBuffer;
import java.io.IOException;
import java.io.InputStreamReader;

public class StreamDownloader {
	private String Url;
	private URLConnection urlConn = null;

	public StreamDownloader() {
		this.setUrl("");
	}

	public StreamDownloader(String newUrl) {
		this.setUrl(newUrl);
	}

	public void setUrl(String newUrl) {
		this.Url = newUrl;
	}

	public String getUrl() {
		return this.Url;
	}

	private void setUrlConnection() {
		String protocol = "";
		String host = "";
		String filename = "";

		int len = Url.length();
		int i = 0;

		while (Url.charAt(i) != ':') {
			protocol = protocol + Character.toString(Url.charAt(i));
			i++;
		}

		i += 3; // ://

		while (Url.charAt(i) != '/') {
			host = host + Character.toString(Url.charAt(i));
			i++;
		}

		while (i < len) {
			filename = filename + Character.toString(Url.charAt(i));
			i++;
		}

		try {
			URL url = new URL(protocol, host, filename);
			urlConn = url.openConnection();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String downloadDataStream() throws IOException {
		String result = "";
		this.setUrlConnection();

		InputStreamReader isr = null;

		try {
			isr = new InputStreamReader(urlConn.getInputStream(), "UTF8");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		char[] buf = new char[1024];
		int len = 0;

		while ((len = isr.read(buf, 0, 1024)) != -1) {
			result += CharBuffer.wrap(buf, 0, len).toString();
		}

		isr.close();

		return result;
	}
}
