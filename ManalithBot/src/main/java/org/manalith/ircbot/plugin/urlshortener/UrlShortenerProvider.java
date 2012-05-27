package org.manalith.ircbot.plugin.urlshortener;

public interface UrlShortenerProvider {
	public String shorten(String url);
}
