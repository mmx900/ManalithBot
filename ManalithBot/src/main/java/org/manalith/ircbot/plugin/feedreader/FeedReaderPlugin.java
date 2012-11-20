package org.manalith.ircbot.plugin.feedreader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@Component
public class FeedReaderPlugin extends AbstractBotPlugin {
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private FeedDao feedDao;

	@Override
	public String getName() {
		return "피드리더";
	}

	@Override
	public String getCommands() {
		return "!feed, !feedreg, !feedrm";
	}

	@Override
	public String getHelp() {
		return "설  명: RSS 피드를 읽어옵니다, 사용법: !feed|!feedreg|!feedrm [피드주소]";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String[] segments = event.getMessageSegments();

		if (segments.length >= 1 && segments[0].startsWith("!feed")) {
			if (segments.length == 2 && segments[0].equals("!feed")) {
				try {
					String url = segments[1];
					SyndEntry entry = getNewestEntry(url);

					if (entry != null) {
						event.respond(entry.getTitle() + " - " + entry.getUri());
					} else {
						event.respond(url + " 에 컨텐츠가 없습니다.");
					}
				} catch (MalformedURLException e) {
					logger.warn(e.getMessage(), e);
					event.respond(segments[1] + "은 지원하지 않는 URL입니다.");
				} catch (IllegalArgumentException | FeedException | IOException e) {
					logger.warn(e.getMessage(), e);
					event.respond("오류가 발생했습니다 : " + e.getMessage());
				}
			} else if (segments.length == 2 && segments[0].equals("!feedreg")) {
				String url = segments[1];

				try {
					Feed feed = feedDao.findByUrl(url);

					if (feed != null) {
						event.respond("이미 등록된 피드입니다 : " + url);
						return;
					}

					SyndEntry entry = getNewestEntry(url);

					feed = new Feed();
					feed.setUrl(url);
					feed.setDate(new Date());
					feed.setUser(event.getUser().getNick());
					feed.setChannel(event.getChannel().getName());
					feed.setLatestContents(entry.getTitle());
					feedDao.save(feed);

					event.respond(entry.getTitle() + " - " + entry.getLink());
					event.respond("이제 주기적으로 다음 피드를 확인합니다 : " + url);
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
					event.respond("피드를 등록하던 중 오류가 발생했습니다 : " + e.getMessage());
				}
			} else if (segments.length == 2 && segments[0].equals("!feedrm")) {
				String url = segments[1];
				Feed feed = feedDao
						.findByUrl(url, event.getChannel().getName());

				if (feed != null) {
					feedDao.delete(feed);
					event.respond("피드가 삭제되었습니다 : " + url);
				} else {
					event.respond("등록된 피드가 없습니다 : " + url);
				}
			} else {
				event.respond(getHelp());
			}

			event.setExecuted(true);
		}
	}

	@Scheduled(fixedDelay = 1000 * 60 * 5)
	public void feedUpdateCheck() {
		List<Feed> feeds = feedDao.findAll();

		for (Feed f : feeds) {
			logger.trace("피드 검사 : " + f.getUrl());

			try {
				// FIXME 비동기로 동작하도록 수정
				SyndEntry entry = getNewestEntry(f.getUrl());
				if (!entry.getTitle().equals(f.getLatestContents())) {
					// TODO 단축 주소로 변환
					ManalithBot.getInstance().sendMessage(f.getChannel(),
							entry.getTitle() + " - " + entry.getLink());
					f.setLatestContents(entry.getTitle());
					feedDao.update(f);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		}
	}

	private SyndEntry getNewestEntry(String url) throws MalformedURLException,
			IllegalArgumentException, FeedException, IOException {
		URL feedUrl = new URL(url);

		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(feedUrl));
		@SuppressWarnings("unchecked")
		List<SyndEntry> feedEntries = feed.getEntries();

		if (feedEntries.size() == 0) {
			return null;
		} else {
			return feedEntries.get(0);
		}
	}
}
