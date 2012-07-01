package org.manalith.ircbot.plugin.feedreader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@Component
public class FeedReaderPlugin extends AbstractBotPlugin {
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public String getName() {
		return "피드리더";
	}

	@Override
	public String getCommands() {
		return "!feed";
	}

	@Override
	public String getHelp() {
		return "설  명: RSS 피드를 읽어옵니다, 사용법: !feed [피드주소]";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String[] segments = StringUtils.split(event.getMessage(), " ");

		if (segments[0].equalsIgnoreCase(getCommands())) {

			if (segments.length != 2) {
				event.respond(getHelp());
			} else {
				try {
					URL feedUrl = new URL(segments[1]);

					SyndFeedInput input = new SyndFeedInput();
					SyndFeed feed = input.build(new XmlReader(feedUrl));
					@SuppressWarnings("unchecked")
					List<SyndEntry> feedEntries = feed.getEntries();

					if (feedEntries.size() == 0) {
						event.respond(segments[1] + "에 컨텐츠가 없습니다.");

					} else {
						SyndEntry entry = feedEntries.get(0);
						event.respond(entry.getTitle() + " / " + entry.getUri()
								+ "(" + entry.getPublishedDate() + ")");
					}

				} catch (MalformedURLException e) {
					event.respond(segments[1] + "은 지원하지 않는 URL입니다.");

					logger.warn(e.getMessage(), e);
				} catch (IllegalArgumentException e) {
					logger.warn(e.getMessage(), e);
				} catch (FeedException e) {
					logger.warn(e.getMessage(), e);
				} catch (IOException e) {
					logger.warn(e.getMessage(), e);
				}
			}

			event.setExecuted(true);
		}
	}
}
