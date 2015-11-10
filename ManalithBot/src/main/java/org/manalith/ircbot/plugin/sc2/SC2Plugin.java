package org.manalith.ircbot.plugin.sc2;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.pircbotx.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SC2Plugin extends SimplePlugin {

	private static final int HOUR_IN_MILLIS = 60 * 60 * 1000;

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private SC2EventDao dao;

	@Autowired
	private SC2ScheduleUpdater updater;

	@Override
	public String getName() {
		return "스타크래프트2(!sc2)";
	}

	@Override
	public String getDescription() {
		return "스타크래프트 경기 일정 플러그인입니다.";
	}

	@BotCommand("sc2")
	public void showLeagueSchedule(MessageEvent event) {
		if (event == null) {
			throw new NullArgumentException("event");
		}

		log.debug(
				"Displaying today's SC2 league schedule (channel : {}, nick: {}).",
				event.getChannel().getName(), event.getUser().getNick());

		ManalithBot bot = ManalithBot.getInstance();

		Channel channel = event.getChannel();

		try {
			List<SC2Event> events = dao.findCurrentEvents();

			if (events.isEmpty()) {
				bot.sendMessage(channel.getName(), "조회된 일정이 없습니다.");
			}

			for (SC2Event e : events) {
				bot.sendMessage(channel.getName(), e.toString());
			}
		} catch (Exception e) {
			String msg = "Failed to fetch league schedule : " + e;

			log.error(msg, e);

			bot.sendMessage(channel.getName(), "일정을 조회하는 도중 오류가 발생했습니다 : " + e);
		}
	}

	@Scheduled(fixedDelay = HOUR_IN_MILLIS)
	public void checkSchedule() throws IOException {
		updater.update();
	}

	@Scheduled(cron = "0 00 01 * * ?")
	public void deletePastEvents() throws IOException {
		dao.deletePastEvents();
	}

	/**
	 * @see org.manalith.ircbot.plugin.SimplePlugin#start()
	 */
	@Override
	public void start() throws Exception {
		super.start();

		log.info("SC2 plugin has been started successfully.");
	}

	/**
	 * @see org.manalith.ircbot.plugin.SimplePlugin#stop()
	 */
	@Override
	public void stop() throws Exception {
		super.stop();

		log.info("SC2 plugin has been stopped.");
	}
}