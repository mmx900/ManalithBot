package org.manalith.ircbot.plugin.urlshortener;

public interface UrlShortenerProvider {
	String shorten(String url);
}
