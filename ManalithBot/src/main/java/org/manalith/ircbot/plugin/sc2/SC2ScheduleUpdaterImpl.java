package org.manalith.ircbot.plugin.sc2;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SC2ScheduleUpdaterImpl implements SC2ScheduleUpdater {

	private static final String ACCEPT_LANGUAGE = "ko,ko-KR;q=0.7,en-US;q=0.3";

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0";

	private String url = "http://wcs.battle.net/sc2/ko/schedule";

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private SC2EventDao eventDao;

	@Autowired
	private SC2PlayerDao playerDao;

	/**
	 * @see org.manalith.ircbot.plugin.sc2.SC2ScheduleUpdater#getUrl()
	 */
	@Override
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            설정할 url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @see org.manalith.ircbot.plugin.sc2.SC2ScheduleUpdater#update()
	 */
	@Override
	public void update() throws IOException {
		log.info("Fetching SC2 league schedules from '{}'.", url);

		long started = System.currentTimeMillis();

		Document doc = connect(url).get();

		Elements items = doc
				.select(".full-schedule-container .full-schedule-item");

		log.debug("Found {} item(s).", items.size());

		Calendar calendar = Calendar.getInstance();

		calendar.setTimeZone(TimeZone.getTimeZone("KST"));
		calendar.setTime(new Date());

		calendar.add(Calendar.WEEK_OF_YEAR, -1);

		Date weekAgo = calendar.getTime();

		for (Element item : items) {
			try {
				String title = parseTitle(item);

				Date date = parseDate(item);

				List<String> links = parsePlayerLinks(item);

				log.debug("Event: {} - {}, {} players.", title, date,
						links.size());

				List<SC2Player> players = new LinkedList<SC2Player>();

				for (String link : links) {
					String id = SC2Player.getIdFromUri(link);

					SC2Player player = playerDao.findById(id);

					if (player == null
							|| player.getLastUpdate().before(weekAgo)) {
						player = parsePlayer(id);

						playerDao.add(player);
					}

					players.add(player);
				}

				SC2Event event = new SC2Event();

				event.setDate(date);
				event.setTitle(title);
				event.setPlayers(players);

				eventDao.add(event);
			} catch (ParseException e) {
				log.warn("Failed to parse item: {}", e, e);
			} catch (Exception e) {
				log.error("Unhandled exception occurred: {}", e, e);
			}
		}

		long elapsed = System.currentTimeMillis() - started;

		log.info(
				"Finished updating the SC2 league database(elapsed: {} msecs)",
				elapsed);
	}

	protected SC2Player parsePlayer(String id) throws ParseException,
			IOException {
		String url = SC2Player.getUriFromId(id);

		log.info("Parsing player information: '{}'", url);

		Document doc = Jsoup.connect(url).get();

		String nick = doc.select("#main-title").text();
		String name = doc.select("#main-title .ko-title").text();
		String team = doc.select(".player-team").text();

		if (StringUtils.isEmpty(name)) {
			name = nick;
		} else if (nick.contains(name)) {
			nick = nick.substring(0, nick.length() - name.length()).trim();
		}

		String raceProfile = doc.select("img.race").attr("src");

		SC2Race race = null;

		for (SC2Race r : SC2Race.values()) {
			if (r.matches(raceProfile)) {
				race = r;
				break;
			}
		}

		log.debug("* Nick : {}", nick);
		log.debug("* Name : {}", name);
		log.debug("* Race : {}", race);
		log.debug("* Team : {}", team);

		SC2Player player = new SC2Player();

		player.setId(id);
		player.setNick(nick);
		player.setName(name);
		player.setTeam(team);
		player.setRace(race);

		return player;
	}

	protected Connection connect(String url) {
		if (url == null) {
			throw new NullArgumentException("url");
		}

		return Jsoup.connect(url).userAgent(USER_AGENT)
				.header("Accept-Language", ACCEPT_LANGUAGE);
	}

	protected String parseTitle(Element item) throws ParseException {
		if (item == null) {
			throw new NullArgumentException("item");
		}

		return item.select(".title").text();
	}

	protected Date parseDate(Element item) throws ParseException {
		if (item == null) {
			throw new NullArgumentException("item");
		}

		String text = item.select("time").attr("datetime");

		try {
			Date date = DatatypeConverter.parseDateTime(text).getTime();

			Calendar calendar = Calendar.getInstance(TimeZone
					.getTimeZone("KST"));

			calendar.setTimeInMillis(date.getTime());

			return calendar.getTime();
		} catch (IllegalArgumentException e) {
			throw new ParseException("Unable to parse date: " + text, e);
		}
	}

	protected List<String> parsePlayerLinks(Element item) throws ParseException {
		if (item == null) {
			throw new NullArgumentException("item");
		}

		Elements links = item.select("a.spoiler");

		List<String> players = new LinkedList<String>();

		for (Element link : links) {
			players.add(link.attr("href"));
		}

		return players;
	}
}
