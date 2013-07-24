package org.manalith.ircbot.plugin.urlshortener;

import org.manalith.ircbot.annotation.NotNull;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.springframework.stereotype.Component;

@Component
public class UrlShortenerPlugin extends SimplePlugin {
	private UrlShortenerProvider provider;

	@Override
	public String getName() {
		return "URL 단축";
	}

	@Override
	public String getDescription() {
		return "URL을 짧게 줄여줍니다.";
	}

	public UrlShortenerProvider getProvider() {
		return provider;
	}

	public void setProvider(UrlShortenerProvider provider) {
		this.provider = provider;
	}

	@BotCommand("짧게")
	public String shorten(@NotNull String url) {
		return "[URL Shortener] " + provider.shorten(url);
	}
}
