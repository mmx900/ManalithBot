package org.manalith.ircbot.plugin.urlshortener;

import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class UrlShortenerPlugin extends AbstractBotPlugin {
	private UrlShortenerProvider provider;

	@Override
	public String getName() {
		return "URL 단축";
	}

	@Override
	public String getCommands() {
		return "!짧게 [URL]";
	}

	public UrlShortenerProvider getProvider() {
		return provider;
	}

	public void setProvider(UrlShortenerProvider provider) {
		this.provider = provider;
	}

	@BotCommand(value = { "!짧게" }, minimumArguments = 1)
	public String shorten(MessageEvent event, String... args) {
		return "[URL Shortener] " + shorten(args[0]);
	}

	public String shorten(String url) {
		return provider.shorten(url);
	}
}
